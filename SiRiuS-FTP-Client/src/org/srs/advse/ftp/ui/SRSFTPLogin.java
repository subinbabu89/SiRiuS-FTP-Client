package org.srs.advse.ftp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.srs.advse.ftp.Constants;

/**
 * Class for the login window for the client app
 * 
 * @author Subin
 *
 */
public class SRSFTPLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4736802206170115720L;
	private JTextField textFieldUserName;
	private JPasswordField passwordFieldPass;
	private JTextField textField_1;

	private static Socket telnetSocket;
	private static DataInputStream telnetDataInputStream;
	private static DataOutputStream telnetDataOutputStream;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new SRSFTPLogin();
	}

	/**
	 * Create the application.
	 */
	public SRSFTPLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Login");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		textFieldUserName = new JTextField();
		textFieldUserName.setText("subin");
		textFieldUserName.setBounds(197, 79, 227, 20);
		getContentPane().add(textFieldUserName);
		textFieldUserName.setColumns(10);

		passwordFieldPass = new JPasswordField();
		passwordFieldPass.setBounds(197, 110, 227, 20);
		getContentPane().add(passwordFieldPass);

		textField_1 = new JTextField();
		textField_1.setText(Constants.getHostString());
		textField_1.setBounds(197, 141, 227, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(49, 82, 74, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(49, 113, 74, 14);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Host Address");
		lblNewLabel_2.setBounds(49, 144, 74, 14);
		getContentPane().add(lblNewLabel_2);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		btnLogin.setBounds(80, 199, 89, 23);
		getContentPane().add(btnLogin);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(249, 199, 89, 23);
		getContentPane().add(btnCancel);
		setVisible(true);
	}

	/**
	 * Method to perform the login operationa
	 */
	private void login() {
		try {
			boolean logIN = false;
			telnetSocket = new Socket(Constants.getHostString(), 23);
			telnetDataInputStream = new DataInputStream(telnetSocket.getInputStream());
			telnetDataOutputStream = new DataOutputStream(telnetSocket.getOutputStream());

			String telnet_user_string = "telnetd_" + textFieldUserName.getText() + "_"
					+ new String(passwordFieldPass.getPassword());
			telnetDataOutputStream.writeUTF(telnet_user_string);
			String telOutput = telnetDataInputStream.readUTF();
			logIN = Boolean.parseBoolean(telOutput);
			if (logIN) {
				System.out.println("Login successful, Welcome User");
				dispose();
				String[] args = { Constants.getHostString(), "20", "D:\\Subin", textFieldUserName.getText() };
				SRSFTPMainWindow.main(args);
			} else {
				JOptionPane.showMessageDialog(this, "incorrect password, Try again.");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
