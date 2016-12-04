/**
 * 
 */
package org.srs.advse.ftp.thread;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.srs.advse.ftp.Constants;
import org.srs.advse.ftp.client.SRSFTPClient;

/**
 * @author Subin
 *
 */
public class UploadHandler implements Runnable {

	private SRSFTPClient client;
	private Socket socket;
	private Path clientDirPath, serverPath;
	private List<String> inputs;
	private int terminateID;

	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private OutputStream outputStream;
	private DataOutputStream dataOutputStream;

	/**
	 * @param client
	 * @param hostname
	 * @param nPort
	 * @param inputs
	 * @param serverPath
	 * @throws Exception
	 */
	public UploadHandler(SRSFTPClient client, String hostname, int nPort, List<String> inputs, Path serverPath,
			String clientDir,String username) throws Exception {
		this.client = client;
		this.inputs = inputs;
		this.serverPath = serverPath;

		InetAddress address = InetAddress.getByName(hostname);
		socket = new Socket();
		socket.connect(new InetSocketAddress(address.getHostAddress(), nPort), 1000);

		inputStreamReader = new InputStreamReader(socket.getInputStream());
		bufferedReader = new BufferedReader(inputStreamReader);
		outputStream = socket.getOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);

		// path = Paths.get(System.getProperty("user.dir"));
		clientDirPath = Paths.get(clientDir);
//		String ftpPath = Constants.getServerPath() + File.separator + "ftp";
//		Path path = Paths.get(ftpPath + File.separator + username);
//		dataOutputStream.writeBytes(path + "\n");
	}

	/**
	 * @throws Exception
	 */
	private void upload() throws Exception {
		if (!client.transfer(serverPath.resolve(inputs.get(1)))) {
			System.out.println("already downloading");
			return;
		}

		if (Files.notExists(clientDirPath.resolve(inputs.get(1)))) {
			System.out.println("no such file");
		} else if (Files.isDirectory(clientDirPath.resolve(inputs.get(1)))) {
			System.out.println("is a directory");
		} else {
			dataOutputStream.writeBytes("up " + inputs.get(1) + "\n");

			try {
				terminateID = Integer.parseInt(bufferedReader.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}

			client.transferIN(serverPath.resolve(inputs.get(1)), terminateID);

			bufferedReader.readLine();

			Thread.sleep(100);

			byte[] fileBuffer = new byte[1000];
			try {
				File file = new File(clientDirPath.resolve(inputs.get(1)).toString());

				long fileSize = file.length();
				byte[] fileSizeBytes = ByteBuffer.allocate(8).putLong(fileSize).array();
				dataOutputStream.write(fileSizeBytes, 0, 8);

				Thread.sleep(100);

				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				int count = 0;
				while ((count = bis.read(fileBuffer)) > 0) {
					dataOutputStream.write(fileBuffer, 0, count);
				}
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			client.transferOUT(serverPath.resolve(inputs.get(1)), terminateID);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			upload();
			Thread.sleep(100);
//			dataOutputStream.writeBytes("quit" + "\n");
		} catch (Exception e) {
			System.out.println("upload handler error");
		}
	}

}
