package org.srs.advse.ftp.ui;

/**
 * Callback Listener for the upload operation
 * 
 * @author Subin
 *
 */
public interface UploadProgressListerner {

	/**
	 * Method to fire on begin of upload
	 * 
	 * @param filename
	 */
	void onUploadBegin(String filename);

	/**
	 * Method to fire on progress of upload
	 * 
	 * @param progress
	 */
	void onUploadProgress(String filename, int progress);

	/**
	 * Method to fire on completion of upload
	 */
	void onUploadComplete(String filename);
}
