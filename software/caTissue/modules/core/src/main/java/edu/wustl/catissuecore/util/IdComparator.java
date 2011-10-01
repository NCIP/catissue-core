
package edu.wustl.catissuecore.util;

import java.util.Comparator;

import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.bean.DeriveSpecimenBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.Specimen;

public class IdComparator implements Comparator
{

	public int compare(Object arg0, Object arg1)
	{
		Long id1 = null;
		Long id2 = null;
		int returnId= 0;
		if (arg0 != null && arg1 != null)
		{
			if (arg0 instanceof DeriveSpecimenBean && arg1 instanceof DeriveSpecimenBean)
			{
				id1 = ((DeriveSpecimenBean) arg0).getId();
				id2 = ((DeriveSpecimenBean) arg1).getId();

			}
			if (arg0 instanceof Specimen && arg1 instanceof Specimen)
			{
				id1 = ((Specimen) arg0).getId();
				id2 = ((Specimen) arg1).getId();

			}

			if (arg0 instanceof GenericSpecimen && arg1 instanceof GenericSpecimen)
			{
				id1 = ((GenericSpecimen) arg0).getId();
				id2 = ((GenericSpecimen) arg1).getId();

			}
//			bug 8905 start
			if(arg0 instanceof ConsentTierResponse && arg1 instanceof ConsentTierResponse)
			{
				id1 = ((ConsentTierResponse) arg0).getConsentTier().getId();
				id2 = ((ConsentTierResponse) arg1).getConsentTier().getId();
			}
			if(arg0 instanceof ConsentTier && arg1 instanceof ConsentTier)
			{
				id1 = ((ConsentTier) arg0).getId();
				id2 = ((ConsentTier) arg1).getId();
			}
			if(arg0 instanceof ConsentTierStatus && arg1 instanceof ConsentTierStatus)
			{
				id1 = ((ConsentTierStatus) arg0).getConsentTier().getId();
				id2 = ((ConsentTierStatus) arg1).getConsentTier().getId();
			}
			if(arg0 instanceof ConsentBean && arg1 instanceof ConsentBean)
			{
				String id3 = ((ConsentBean) arg0).getConsentTierID();
				String id4 = ((ConsentBean) arg1).getConsentTierID();
				if(id3!=null && id4!=null)
				{
					id1 = Long.parseLong(id3);
					id2 = Long.parseLong(id4);
				}
			}
			if(arg0 instanceof String && arg1 instanceof String)
			{
				id1 = Long.parseLong(getId((String)arg0));
				id2 = Long.parseLong(getId((String)arg1));
			}
			if (arg0 instanceof OrderItem && arg1 instanceof OrderItem)
			{
				id1 = ((OrderItem) arg0).getId();
				id2 = ((OrderItem) arg1).getId();
			}
//			bug 8905 end
			if (id1 != null && id2 != null)
			{
				returnId = id1.compareTo(id2);
			}
			else if (id1 == null && id2 == null)
			{
				returnId = 0;
			}
			else if (id1 == null)
			{
				returnId = 1;
			}
			else if (id2 == null)
			{
				returnId = -1;
			}
		}
		// TODO Auto-generated method stub
		return returnId;
	}
//	bug 8905
	/**
	 * This method is used to get consent id from key 
	 * @param key - ConsentBean:counter_consentTierID
	 * @return id
	 */
	private String getId(String arg)
	{
		String[] first = ((String)arg).split(":");
		String identifier = null;
		if(first.length > 0)
		{
			for(String s : first)
			{
				if(s.contains("_"))
				{
					String[] idStmt = ((String)s).split("_");
					identifier = idStmt[0];
				}
			}
		}
		return identifier;
	}

}
