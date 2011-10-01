package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.sop.SOP;

public class SOPFactory implements InstanceFactory<SOP>
{

	/** The sop factory. */
	private static SOPFactory sopFactory;

	/**
	 * Instantiates a new sOP factory.
	 */
	private SOPFactory()
	{
		super();
	}

	/**
	 * Gets the single instance of SOPFactory.
	 *
	 * @return single instance of SOPFactory
	 */
	public static synchronized SOPFactory getInstance()
	{
		if(sopFactory==null){
			sopFactory = new SOPFactory();
		}
		return sopFactory;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#createClone(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public SOP createClone(SOP obj)
	{
		SOP sop = createObject();
		sop.setName(obj.getName());
		return sop;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#createObject()
	 */
	@Override
	public SOP createObject()
	{
		SOP sop = new SOP();
		initDefaultValues(sop);
		return sop;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#initDefaultValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public void initDefaultValues(SOP obj)
	{

	}
}
