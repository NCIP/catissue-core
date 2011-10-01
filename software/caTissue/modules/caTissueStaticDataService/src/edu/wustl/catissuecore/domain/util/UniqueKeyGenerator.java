package edu.wustl.catissuecore.domain.util;

/**
 * @author Ion C. Olaru
 *         Date: 7/21/11 - 1:34 PM
 */

import java.util.Date;

public class UniqueKeyGenerator {
	private static Date date;

	/**
	 * gets unique key for name,barcode etc...
	 * @return string
	 */
	public static String getKey() {
		date = new Date();
		String uniqueKey = String.valueOf(date.getTime());
		return uniqueKey;
	}
}
