/**
 * 
 */
package org.srs.advse.ftp.ui;

/**
 * @author Subin
 *
 */
public interface UploadProgressListerner {

	void onUploadBegin(String filename);

	/**
	 * @param progress
	 */
	void onUploadProgress(String filename, int progress);

	/**
	 * 
	 */
	void onUploadComplete(String filename);
}
