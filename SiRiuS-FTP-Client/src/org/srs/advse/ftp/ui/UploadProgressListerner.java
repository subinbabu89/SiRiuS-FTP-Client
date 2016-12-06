/**
 * 
 */
package org.srs.advse.ftp.ui;

/**
 * @author Subin
 *
 */
public interface UploadProgressListerner {

	void onUploadProgress(int progress);
	
	void onUploadComplete();
}
