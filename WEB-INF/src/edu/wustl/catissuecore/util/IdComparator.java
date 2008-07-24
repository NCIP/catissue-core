
package edu.wustl.catissuecore.util;

import java.util.Comparator;

import edu.wustl.catissuecore.bean.DeriveSpecimenBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.domain.Specimen;

public class IdComparator implements Comparator
{

	public int compare(Object arg0, Object arg1)
	{
		Long id1 = null;
		Long id2 = null;

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
			if (id1 != null && id2 != null)
			{
				return id1.compareTo(id2);
			}
			if (id1 == null && id2 == null)
				return 0;

			if (id1 == null)
				return 1;
			if (id2 == null)
				return -1;
		}
		// TODO Auto-generated method stub
		return 0;
	}

}
