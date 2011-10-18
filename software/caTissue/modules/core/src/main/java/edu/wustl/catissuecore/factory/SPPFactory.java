package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;

public class SPPFactory implements InstanceFactory<SpecimenProcessingProcedure>
{

	/** The spp factory. */
	private static SPPFactory sppFactory;

	/**
	 * Instantiates a new SPP factory.
	 */
	private SPPFactory()
	{
		super();
	}

	/**
	 * Gets the single instance of SPPFactory.
	 *
	 * @return single instance of SPPFactory
	 */
	public static synchronized SPPFactory getInstance()
	{
		if(sppFactory==null){
			sppFactory = new SPPFactory();
		}
		return sppFactory;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#createClone(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public SpecimenProcessingProcedure createClone(SpecimenProcessingProcedure obj)
	{
		SpecimenProcessingProcedure spp = createObject();
		spp.setName(obj.getName());
		return spp;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#createObject()
	 */
	@Override
	public SpecimenProcessingProcedure createObject()
	{
		SpecimenProcessingProcedure spp = new SpecimenProcessingProcedure();
		initDefaultValues(spp);
		return spp;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.factory.InstanceFactory#initDefaultValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public void initDefaultValues(SpecimenProcessingProcedure obj)
	{

	}
}
