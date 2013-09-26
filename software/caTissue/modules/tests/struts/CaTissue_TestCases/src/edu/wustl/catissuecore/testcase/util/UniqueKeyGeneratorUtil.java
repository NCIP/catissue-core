/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.testcase.util;
import java.util.Date;


/**
 * <p>This class initializes the fields of UniqueKeyGeneratorUtil.java</p>
 * @author Himanshu Aseeja
 */
public class UniqueKeyGeneratorUtil {
	private static Date date;
	
/*	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(" Time zone :: " + date.getTime());
	}
*/	
	/**
	 * gets unique key for name,barcode etc...
	 * @return string
	 */
	public static String getUniqueKey()
	{
		date = new Date();
		String uniqueKey = String.valueOf(date.getTime());		
		return uniqueKey;
	}
}
