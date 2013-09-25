/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.catissuecore.comparator;

import java.util.Comparator;

import edu.wustl.catissuecore.passwordutil.Password;




/**
 * @author atul_kaushal
 *
 */
public class PasswordComparator implements Comparator<Password>
{

	public int compare(Password arg0, Password arg1)
	{
		final Password pwd = (Password) arg1;
		return pwd.getUpdateDate().compareTo(arg0.getUpdateDate());
	}

}
