package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;


public class QuarantineEventParameterFactory implements InstanceFactory<QuarantineEventParameter>
{
	private static QuarantineEventParameterFactory quarantineEventParameterFactory;

	private QuarantineEventParameterFactory()
	{
		super();
	}

	public static synchronized QuarantineEventParameterFactory getInstance()
	{
		if(quarantineEventParameterFactory==null){
			quarantineEventParameterFactory = new QuarantineEventParameterFactory();
		}
		return quarantineEventParameterFactory;
	}

	public QuarantineEventParameter createClone(QuarantineEventParameter t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public QuarantineEventParameter createObject()
	{
		QuarantineEventParameter eventParameter= new QuarantineEventParameter();
		initDefaultValues(eventParameter);
		return eventParameter;
	}

	public void initDefaultValues(QuarantineEventParameter t)
	{
		// TODO Auto-generated method stub

	}

}
