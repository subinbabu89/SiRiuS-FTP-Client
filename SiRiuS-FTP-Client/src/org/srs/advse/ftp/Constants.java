/**
 * 
 */
package org.srs.advse.ftp;

/**
 * @author Subin
 *
 */
public class Constants {

	enum WORKING_MODE {
		EC2, LOCAL
	}

	private static WORKING_MODE currentMode = WORKING_MODE.LOCAL;

	public static String getServerPath() {
		if (currentMode == WORKING_MODE.EC2) {
			return "/home/ubuntu";
		} else {
			return System.getProperty("user.home");
		}
	}
	
	public static String getHostString(){
		if (currentMode == WORKING_MODE.EC2) {
			return "ec2-35-162-41-132.us-west-2.compute.amazonaws.com";
		} else {
			return "localhost";
		}
	}
}
