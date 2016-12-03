/**
 * 
 */
package org.srs.advse.ftp.commhandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.srs.advse.ftp.client.SRSFTPClient;
import org.srs.advse.ftp.thread.DownloadHandler;
import org.srs.advse.ftp.thread.UploadHandler;

/**
 * @author Subin
 *
 */
public class ClientCommunicationHandler implements Runnable {

	private InputStreamReader commandChannelReader;
	BufferedReader commandCbuffer;

	private DataOutputStream dataChannelOutputStream;
	private OutputStream dataOutputStream;

	private SRSFTPClient client;
	private String host;
	private int port;
	private List<String> input;

	private Socket socket;
	private Path serverPath, userPath;

	private String clientDir;

	/**
	 * @param client
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public ClientCommunicationHandler(SRSFTPClient client, String host, int port, String clientDir) throws Exception {
		this.client = client;
		this.host = host;
		this.port = port;
		this.clientDir = clientDir;

		InetAddress hostAddress = InetAddress.getByName(host);
		socket = new Socket();
		socket.connect(new InetSocketAddress(hostAddress.getHostAddress(), port), 1000);

		commandChannelReader = new InputStreamReader(socket.getInputStream());
		commandCbuffer = new BufferedReader(commandChannelReader);

		dataOutputStream = socket.getOutputStream();
		dataChannelOutputStream = new DataOutputStream(dataOutputStream);

		dataChannelOutputStream.writeBytes("pwd" + "\n");
		String line;
		if (!(line = commandCbuffer.readLine()).equals("")) {
			serverPath = Paths.get(line);
		}

		// userPath = Paths.get(System.getProperty("user.dir"));
		userPath = Paths.get(clientDir);
		System.out.println("Connected to: " + hostAddress);
	}

	/**
	 * @throws Exception
	 */
	public void pwd() throws Exception {
		// only one argument
		if (input.size() != 1) {
			invalid();
			return;
		}

		// send command
		dataChannelOutputStream.writeBytes("pwd" + "\n");

		// message
		System.out.println(commandCbuffer.readLine());
	}

	/**
	 * 
	 */
	public void invalid() {
		System.out.println("Invalid Arguments");
		System.out.println("Try `help' for more information.");
	}

	/**
	 * @throws Exception
	 */
	public void download() throws Exception {
		(new Thread(new DownloadHandler(client, host, port, input, serverPath, userPath))).start();
	}

	/**
	 * @throws Exception
	 */
	public void list() throws Exception {
		if (input.size() != 1) {
			invalid();
			return;
		}

		dataChannelOutputStream.writeBytes("list" + "\n");

		String line;
		while (!(line = commandCbuffer.readLine()).equals(""))
			System.out.println(line);

	}

	/**
	 * @throws Exception
	 */
	public void upload() throws Exception {
		(new Thread(new UploadHandler(client, host, port, input, serverPath, clientDir))).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			Scanner scanner = new Scanner(System.in);
			String command = "";
			do {
				System.out.print("srsftp::>");
				command = scanner.nextLine();
				command = command.trim();

				input = new ArrayList<String>();
				Scanner enteredInput = new Scanner(command);

				if (enteredInput.hasNext()) {
					input.add(enteredInput.next());
				}

				if (enteredInput.hasNext())
					input.add(command.substring(input.get(0).length()).trim());
				enteredInput.close();

				if (input.isEmpty())
					continue;

				switch (input.get(0)) {
				case "test":
					System.out.println("printing test in client");
					break;
				case "down":
					download();
					break;
				case "up":
					upload();
					break;
				case "list":
					list();
					break;
				case "quit":
					break;
				case "pwd":
					pwd();
					break;
				default:
					System.out.println("unrecognized command");
				}
			} while (!command.equalsIgnoreCase("quit"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
