package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.Institution;

public class InstitutionFactory implements InstanceFactory<Institution>
{

	private static InstitutionFactory institutionFactory;

	private InstitutionFactory()
	{
		super();
	}

	public static synchronized InstitutionFactory getInstance()
	{
		if(institutionFactory==null){
			institutionFactory = new InstitutionFactory();
		}
		return institutionFactory;
	}

	public Institution createClone(Institution obj)
	{
		Institution institution = createObject();
		institution.setName(obj.getName());
		return institution;
	}

	public Institution createObject()
	{
		Institution institution = new Institution();
		initDefaultValues(institution);
		return institution;
	}

	public void initDefaultValues(Institution obj)
	{

	}
}
