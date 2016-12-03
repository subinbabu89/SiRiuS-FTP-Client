/**
 * 
 */
package org.srs.advse.ftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.srs.advse.ftp.client.SRSFTPClient;
import org.srs.advse.ftp.commhandler.ClientCommunicationHandler;

/**
 * @author Subin
 *
 */
public class RunClient {
	public static int nPort, tPort;
	public static String hostname;
	public static String clientDir;

	private static Socket telnetSocket;
	private static DataInputStream telnetDataInputStream;
	private static DataOutputStream telnetDataOutputStream;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter your username : ");
		String username = scanner.nextLine();
		

		try {
			hostname = args[0];
			InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		nPort = Integer.parseInt(args[1]);
		tPort = Integer.parseInt(args[2]);
		clientDir = args[3];

		try {
			boolean logIN = false;
			telnetSocket = new Socket(hostname, 23);
			telnetDataInputStream = new DataInputStream(telnetSocket.getInputStream());
			telnetDataOutputStream = new DataOutputStream(telnetSocket.getOutputStream());

			while (logIN==false) {
				System.out.println("Enter your password : ");
				String password = scanner.nextLine();
				String telnet_user_string = "telnetd_" + username + "_" + password;
				telnetDataOutputStream.writeUTF(telnet_user_string);
				String telOutput = telnetDataInputStream.readUTF();
				logIN = Boolean.parseBoolean(telOutput);
				if(logIN){
					System.out.println("Login successful, Welcome User");
				}else{
					System.out.println("incorrect password, Try again.");
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			SRSFTPClient client = new SRSFTPClient();
			(new Thread(new ClientCommunicationHandler(client, hostname, nPort, clientDir))).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
