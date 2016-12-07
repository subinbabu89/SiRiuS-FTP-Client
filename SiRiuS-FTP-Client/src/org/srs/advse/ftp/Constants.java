package org.srs.advse.ftp;

import java.io.File;

/**
 * 
 * Constants file for the client code
 * 
 * @author Subin
 *
 */
public class Constants {
	/**
	 * enum to configure working mode
	 * 
	 * @author Subin
	 *
	 */
	enum WORKING_MODE {
		EC2, LOCAL, WIFI
	}

	/**
	 * constant to configure the current working mode
	 */
	private static WORKING_MODE currentMode = WORKING_MODE.WIFI;

	/**
	 * method to fetch the current server path based on working mode
	 * 
	 * @return string with server path
	 */
	public static String getServerPath() {
		if (currentMode == WORKING_MODE.EC2) {
			return File.separator + "home" + File.separator + "ubuntu";
		} else if (currentMode == WORKING_MODE.WIFI) {
			return "C:" + File.separator + "Users" + File.separator + "Raksha Jayaram";
		} else {
			return System.getProperty("user.home");
		}
	}

	/**
	 * method to fetch the current hostpath based on working mode
	 * 
	 * @return string with host name
	 */
	public static String getHostString() {
		if (currentMode == WORKING_MODE.EC2) {
			return "ec2-35-162-41-132.us-west-2.compute.amazonaws.com";
		} else if (currentMode == WORKING_MODE.WIFI) {
			return "192.168.43.183";
		} else {
			return "localhost";
		}
	}
}
