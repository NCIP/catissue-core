package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;


public class SpecimenArrayFactory implements InstanceFactory<SpecimenArray>
{
	private static SpecimenArrayFactory spArrayFactory;

	private SpecimenArrayFactory() {
		super();
	}

	public static synchronized SpecimenArrayFactory getInstance() {
		if(spArrayFactory == null) {
			spArrayFactory = new SpecimenArrayFactory();
		}
		return spArrayFactory;
	}

	public SpecimenArray createClone(SpecimenArray obj)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SpecimenArray createObject()
	{
		SpecimenArray spArray=new SpecimenArray();
		initDefaultValues(spArray);
		return spArray;
	}

	public void initDefaultValues(SpecimenArray obj)
	{
		obj.setAvailable(Boolean.FALSE);
		obj.setSpecimenArrayContentCollection(new HashSet<SpecimenArrayContent>());
		obj.setNewSpecimenArrayOrderItemCollection(new HashSet<NewSpecimenArrayOrderItem>());

	}


}
