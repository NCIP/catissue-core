package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;


public class IdentifiedSurgicalPathologyReportFactory
		implements
			InstanceFactory<IdentifiedSurgicalPathologyReport>
{
	private static IdentifiedSurgicalPathologyReportFactory identifiedSPRFactory;

	private IdentifiedSurgicalPathologyReportFactory()
	{
		super();
	}

	public static synchronized IdentifiedSurgicalPathologyReportFactory getInstance()
	{
		if(identifiedSPRFactory==null){
			identifiedSPRFactory = new IdentifiedSurgicalPathologyReportFactory();
		}
		return identifiedSPRFactory;
	}

	public IdentifiedSurgicalPathologyReport createClone(IdentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IdentifiedSurgicalPathologyReport createObject()
	{
		IdentifiedSurgicalPathologyReport pathologyReport=new IdentifiedSurgicalPathologyReport();
		initDefaultValues(pathologyReport);
		return pathologyReport;
	}

	public void initDefaultValues(IdentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub

	}

}
