package org.srs.advse.ftp.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.srs.advse.ftp.Constants;
import org.srs.advse.ftp.client.SRSFTPClient;
import org.srs.advse.ftp.commhandler.ClientCommunicationHandler;

/**
 * @author Subin
 *
 */
public class SRSFTPMainWindow implements DownloadProgressListerner,UploadProgressListerner {

	private JFrame frmSiriusftp;

	private JSplitPane splitPane;
	private JScrollPane clientScrollPane, serverScrollPane;

	private JButton btnUpload, btnDownload;

	private JProgressBar progressBar, progressBar_1, progressBar_2, progressBar_3, progressBar_4, progressBar_5;

	private List<JProgressBar> progressBars;
	private HashMap<JProgressBar, Boolean> progressbarMap;

	private JList localFileList;
	private JPanel clientPanel, serverPanel;

	protected String filename2Upload, filename2Download;

	protected static String[] inputArgs;

	private ClientCommunicationHandler clientCommunicationHandler;

	private JList<Object> serverFileList;

	private DefaultListModel serverFilesModel, localFilesModel;

	JButton btnRefreshlocallist, btnRefreshserverlist;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		inputArgs = args;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SRSFTPMainWindow window = new SRSFTPMainWindow(args);
					window.frmSiriusftp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public SRSFTPMainWindow(String[] inputArgs) throws Exception {
		initialize();
		frmSiriusftp.setLocationRelativeTo(null);

	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws Exception
	 */
	private void initialize() throws Exception {
		progressbarMap = new HashMap<>();
		frmSiriusftp = new JFrame();
		frmSiriusftp.setTitle("SiRiuS-FTP");
		frmSiriusftp.setBounds(100, 100, 1200, 800);
		frmSiriusftp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSiriusftp.getContentPane().setLayout(null);

		init();
		wiredEvents();
		customizeEvents();

		runApp();

		getLocalFilesModel();
		// getServerFilesList();
		getServerFilesModel();
	}

	/**
	 * @throws Exception
	 */
	private void runApp() throws Exception {
		SRSFTPClient client = new SRSFTPClient();
		clientCommunicationHandler = new ClientCommunicationHandler(client, inputArgs[0],
				Integer.parseInt(inputArgs[1]), inputArgs[2], inputArgs[3], this);
		(new Thread(clientCommunicationHandler)).start();
	}

	/**
	 * @throws Exception
	 * 
	 */
	private void customizeEvents() throws Exception {
		JButton btnIncrement = new JButton("increment");
		btnIncrement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setValue(progressBar, progressBar.getValue() + 10);
			}
		});
		btnIncrement.setBounds(1021, 562, 86, 36);
		frmSiriusftp.getContentPane().add(btnIncrement);

	}

	/**
	 * 
	 */
	private void init() {
		initializePanels();
		initializeTransferButtons();
		initializeProgressBars();
	}

	/**
	 * 
	 */
	private void initializeProgressBars() {
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setVisible(false);
		progressBar.setBounds(10, 573, 997, 14);
		frmSiriusftp.getContentPane().add(progressBar);

		progressBar_1 = new JProgressBar();
		progressBar_1.setIndeterminate(true);
		progressBar_1.setStringPainted(true);
		progressBar_1.setBounds(10, 599, 997, 14);
		progressBar_1.setVisible(false);
		frmSiriusftp.getContentPane().add(progressBar_1);

		progressBar_2 = new JProgressBar();
		progressBar_2.setIndeterminate(true);
		progressBar_2.setStringPainted(true);
		progressBar_2.setBounds(10, 625, 997, 14);
		progressBar_2.setVisible(false);
		frmSiriusftp.getContentPane().add(progressBar_2);

		progressBar_3 = new JProgressBar();
		progressBar_3.setIndeterminate(true);
		progressBar_3.setStringPainted(true);
		progressBar_3.setBounds(6, 652, 1001, 14);
		progressBar_3.setVisible(false);
		frmSiriusftp.getContentPane().add(progressBar_3);

		progressBar_4 = new JProgressBar();
		progressBar_4.setIndeterminate(true);
		progressBar_4.setStringPainted(true);
		progressBar_4.setBounds(10, 678, 997, 14);
		progressBar_4.setVisible(false);
		frmSiriusftp.getContentPane().add(progressBar_4);

		progressBar_5 = new JProgressBar();
		progressBar_5.setIndeterminate(true);
		progressBar_5.setStringPainted(true);
		progressBar_5.setVisible(false);
		progressBar_5.setBounds(6, 702, 1001, 14);
		frmSiriusftp.getContentPane().add(progressBar_5);

		progressbarMap.put(progressBar, Boolean.FALSE);
		progressbarMap.put(progressBar_1, Boolean.FALSE);
		progressbarMap.put(progressBar_2, Boolean.FALSE);
		progressbarMap.put(progressBar_3, Boolean.FALSE);
		progressbarMap.put(progressBar_4, Boolean.FALSE);
		progressbarMap.put(progressBar_5, Boolean.FALSE);

		progressBars = new ArrayList<>();
		progressBars.add(progressBar);
		progressBars.add(progressBar_1);
		progressBars.add(progressBar_2);
		progressBars.add(progressBar_3);
		progressBars.add(progressBar_4);
		progressBars.add(progressBar_5);
	}

	/**
	 * 
	 */
	private void initializeTransferButtons() {
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(237, 523, 86, 36);
		frmSiriusftp.getContentPane().add(btnUpload);

		btnDownload = new JButton("Download");
		btnDownload.setBounds(872, 523, 86, 36);
		frmSiriusftp.getContentPane().add(btnDownload);

		btnRefreshlocallist = new JButton("RefreshLocalList");
		btnRefreshlocallist.setBounds(350, 523, 147, 36);
		frmSiriusftp.getContentPane().add(btnRefreshlocallist);

		btnRefreshserverlist = new JButton("RefreshServerList");
		btnRefreshserverlist.setBounds(993, 523, 160, 36);
		frmSiriusftp.getContentPane().add(btnRefreshserverlist);

	}

	/**
	 * 
	 */
	private void initializePanels() {
		splitPane = new JSplitPane();
		splitPane.setBounds(10, 11, 1168, 500);
		splitPane.setDividerLocation(0.5);
		frmSiriusftp.getContentPane().add(splitPane);

		clientScrollPane = new JScrollPane();
		splitPane.setLeftComponent(clientScrollPane);

		clientPanel = new JPanel();
		clientScrollPane.setViewportView(clientPanel);

		serverScrollPane = new JScrollPane();
		splitPane.setRightComponent(serverScrollPane);

		serverPanel = new JPanel();
		serverScrollPane.setViewportView(serverPanel);
	}

	/**
	 * 
	 */
	private void wiredEvents() {
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					upload(filename2Upload);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					download(filename2Download);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnRefreshlocallist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							updateLocalFilesModel();
						} catch (Exception e) {
							e.printStackTrace();
						}
						frmSiriusftp.revalidate();
						frmSiriusftp.repaint();
					}
				}).start();
			}
		});

		btnRefreshserverlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							updateServerFilesModel();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	/**
	 * @param jProgressBar
	 * @param newValue
	 */
	void updateProgress(final JProgressBar jProgressBar, final int newValue) {
		jProgressBar.setValue(newValue);
	}

	/**
	 * @param bar
	 * @param j
	 */
	public void setValue(final JProgressBar bar, final int j) {
		updateProgress(bar, j);
		frmSiriusftp.revalidate();
		frmSiriusftp.repaint();
	}

	/**
	 * @param map
	 * @return
	 */
	private JProgressBar findProgressBar() {
		for (Iterator<JProgressBar> iterator = progressBars.iterator(); iterator.hasNext();) {
			JProgressBar jProgressBar = (JProgressBar) iterator.next();
			if (!progressbarMap.get(jProgressBar)) {
				progressbarMap.put(jProgressBar, Boolean.TRUE);
				return jProgressBar;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	private void getLocalFilesModel() {
		// get files list
		ArrayList<String> file_list_names = getLocalFiles();
		// initialize local model
		localFilesModel = new DefaultListModel();
		for (Iterator iterator = file_list_names.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			localFilesModel.addElement(string);
		}
		localFileList = new JList<>(localFilesModel);
		localFileList.setBounds(0, 0, 202, 114);
		localFileList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filename2Upload = (String) localFileList.getSelectedValue();
			}
		});
		clientPanel.add(localFileList);
		localFileList.setVisible(true);
	}

	/**
	 * @return
	 */
	private ArrayList<String> getLocalFiles() {
		ArrayList<String> file_list_names = new ArrayList<String>();
		File folder = new File("D:" + File.separator + "subin");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				file_list_names.add(listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
			}
		}
		return file_list_names;
	}

	/**
	 * 
	 */
	private void updateLocalFilesModel() {
		ArrayList<String> file_list_names = getLocalFiles();
		// reinitialize file list
		localFilesModel.removeAllElements();
		for (Iterator iterator = file_list_names.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			localFilesModel.addElement(string);
		}

		frmSiriusftp.revalidate();
		frmSiriusftp.repaint();
	}

	/**
	 * @throws Exception
	 */
	private void getServerFilesModel() throws Exception {
		// get files list
		setPath();
		clientCommunicationHandler.setInput(makeInput(new String[] { "list" }));
		List<String> file_list_names = clientCommunicationHandler.list();
		// initialize server model
		serverFilesModel = new DefaultListModel();
		for (Iterator iterator = file_list_names.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			serverFilesModel.addElement(string);
		}
		serverFileList = new JList<>(serverFilesModel);
		serverFileList.setBounds(0, 0, 202, 114);
		serverFileList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filename2Download = (String) serverFileList.getSelectedValue();
			}
		});
		serverPanel.add(serverFileList);
		serverFileList.setVisible(true);
	}

	/**
	 * @throws Exception
	 */
	private void updateServerFilesModel() throws Exception {
		// get file list
		setPath();
		clientCommunicationHandler.setInput(makeInput(new String[] { "list" }));
		List<String> file_list_names = clientCommunicationHandler.list();
		// reinitialize file list
		serverFilesModel.removeAllElements();
		for (Iterator iterator = file_list_names.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			serverFilesModel.addElement(string);
		}

		frmSiriusftp.revalidate();
		frmSiriusftp.repaint();
	}

	/**
	 * @throws Exception
	 */
	private void setPath() throws Exception {
		String ftpPath = Constants.getServerPath() + File.separator + "ftp";
		Path path = Paths.get(ftpPath + File.separator + inputArgs[3]);
		clientCommunicationHandler.setPath(path);
	}

	/**
	 * @param input
	 * @return
	 */
	private List<String> makeInput(String[] input) {
		List<String> inputs = new ArrayList<String>();
		for (int i = 0; i < input.length; i++) {
			inputs.add(input[i]);
		}
		return inputs;
	}

	/**
	 * @param filename
	 * @throws Exception
	 */
	private void upload(String filename) throws Exception {
		setPath();
		clientCommunicationHandler.setInput(makeInput(new String[] { "up", filename }));
		clientCommunicationHandler.upload();
		new Thread(new Runnable() {
			public void run() {
				try {
					updateServerFilesModel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * @param filename
	 * @throws Exception
	 */
	private void download(String filename) throws Exception {
		setPath();
		clientCommunicationHandler.setInput(makeInput(new String[] { "down", filename }));
		clientCommunicationHandler.download();
		updateLocalFilesModel();
	}

	@Override
	public void onUploadProgress(int progress) {
		progressBar_1.setVisible(true);
		setValue(progressBar_1, progress);
	}

	@Override
	public void onUploadComplete() {
		try {
			updateServerFilesModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDownloadProgress(int progress) {
		progressBar.setVisible(true);
		setValue(progressBar, progress);
	}

	@Override
	public void onDownloadComplete() {
		updateLocalFilesModel();
	}
}
