
package edu.wustl.catissuecore.util;

/**
 * This comparator is used where soritng shound be done on relevance counter of NameValueBean
 */
import edu.wustl.common.beans.NameValueBean;

public class ContainerComparator implements java.util.Comparator
{

	/* (non-Javadoc)
	 * @see com.sun.msv.datatype.xsd.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0, Object arg1)
	{
		if (arg0 instanceof NameValueBean)
		{
			if (arg1 instanceof NameValueBean)
			{

				NameValueBean nvb1 = (NameValueBean) arg0;
				NameValueBean nvb2 = (NameValueBean) arg1;
				//Compare according to relevance counter
				if (nvb1.getRelevanceCounter() != null && nvb2.getRelevanceCounter() != null)
					return nvb1.getRelevanceCounter().compareTo(nvb2.getRelevanceCounter());
			}
		}

		return 0;
	}

}
