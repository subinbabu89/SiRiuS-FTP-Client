/**
 * 
 */
package org.srs.advse.ftp.ui;

/**
 * @author Subin
 *
 */
public interface DownloadProgressListerner {

	void onDownloadBegin(String filename);

	/**
	 * @param progress
	 */
	void onDownloadProgress(String filename, int progress);

	/**
	 * 
	 */
	void onDownloadComplete(String filename);
}
