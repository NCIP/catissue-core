
package com.krishagni.catissueplus.core.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;

public class CommonValidator {
	public static final String IP_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." 
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean isEmailValid(String emailAddress) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(emailAddress);
			emailAddr.validate();
		}
		catch (Exception exp) {
			result = false;
		}
		return result;
	}

	public static Boolean isValidIP(String ip) {
		Pattern pattern = Pattern.compile(IP_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static boolean isValidPositiveNumber(Long capacity) {
		if (capacity == null || capacity <= 0) {
			return false;
		}
		return true;
	}
}
