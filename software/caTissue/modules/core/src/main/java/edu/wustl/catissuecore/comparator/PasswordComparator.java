/**
 *
 */
package edu.wustl.catissuecore.comparator;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.Password;


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
