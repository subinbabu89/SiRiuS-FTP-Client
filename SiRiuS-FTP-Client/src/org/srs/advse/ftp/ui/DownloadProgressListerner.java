/**
 * 
 */
package org.srs.advse.ftp.ui;

/**
 * @author Subin
 *
 */
public interface DownloadProgressListerner {

	void onDownloadProgress(int progress);
	
	void onDownloadComplete();
}
