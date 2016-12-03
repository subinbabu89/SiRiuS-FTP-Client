package org.srs.advse.ftp.commhandler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.srs.advse.ftp.client.SRSFTPClient;

public class ClientCommandChannelHandler implements Runnable {

	private SRSFTPClient client;
	private String host;
	private int port;

	private InputStreamReader clientInputStreamReader;
	private BufferedReader commandBufferedReader;

	private Socket socket;
	private Path serverPath, userPath;

	private DataInputStream commandDataInputStream;
	private DataOutputStream commandDataOutputStream;

	private List<String> input;

	private Socket dataChannelSocket;

	public ClientCommandChannelHandler(SRSFTPClient client, String host, int port, String clientDir) throws Exception {
		this.client = client;
		this.host = host;
		this.port = port;

		InetAddress hostAddress = InetAddress.getByName(host);
		socket = new Socket();
		socket.connect(new InetSocketAddress(hostAddress.getHostAddress(), port), 1000);

		clientInputStreamReader = new InputStreamReader(socket.getInputStream());
		commandBufferedReader = new BufferedReader(clientInputStreamReader);

		commandDataInputStream = new DataInputStream(socket.getInputStream());
		commandDataOutputStream = new DataOutputStream(socket.getOutputStream());

		String line;
		if (!(line = commandBufferedReader.readLine()).equals("")) {
			serverPath = Paths.get(line);
		}

		// userPath = Paths.get(System.getProperty("user.dir"));
		userPath = Paths.get(clientDir);
		System.out.println("Connected to: " + hostAddress);
	}

	@Override
	public void run() {

		try {
			Scanner scanner = new Scanner(System.in);
			String command;
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
				case "list":
					list();
					break;
				case "quit":
					break;
				case "pwd":
					pwd();
					break;
				case "pasv":
					pasv();
					break;
				default:
					System.out.println("unrecognized command");
				}
			} while (!command.equalsIgnoreCase("quit"));
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void pasv() throws Exception {
		int data_port = generateDataPort();
		commandDataOutputStream.writeUTF(String.valueOf(data_port));
		ServerSocket serverSocket = new ServerSocket(data_port);
		dataChannelSocket = serverSocket.accept();
		serverSocket.close();
	}

	private int generateDataPort() {
		int data_port;
		Random random;
		do {
			random = new Random();
			data_port = random.nextInt((65535 - 1023) + 1);
		} while (data_port < 1023);

		return data_port;
	}

	private void list() throws Exception {
		if (input.size() != 1) {
			invalid();
			return;
		}

		commandDataOutputStream.writeBytes("list" + "\n");

		String line;
		while (!(line = commandBufferedReader.readLine()).equals(""))
			System.out.println(line);
	}

	private void pwd() throws Exception {
		// only one argument
		if (input.size() != 1) {
			invalid();
			return;
		}

		// send command
		commandDataOutputStream.writeBytes("pwd" + "\n");

		// message
		System.out.println(commandBufferedReader.readLine());
	}

	public void invalid() {
		System.out.println("Invalid Arguments");
		System.out.println("Try `help' for more information.");
	}

}
