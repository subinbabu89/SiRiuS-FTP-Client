package org.srs.advse.ftp.ui;

/**
 * Callback Listener for the download operation
 * 
 * @author Subin
 *
 */
public interface DownloadProgressListerner {

	/**
	 * Method to fire on begin of download
	 * 
	 * @param filename
	 */
	void onDownloadBegin(String filename);

	/**
	 * Method to fire on progress of download
	 * 
	 * @param progress
	 */
	void onDownloadProgress(String filename, int progress);

	/**
	 * Method to fire on end of download
	 */
	void onDownloadComplete(String filename);
}
