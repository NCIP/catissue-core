package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;


public class DeidentifiedSurgicalPathologyReportFactory
		implements
			InstanceFactory<DeidentifiedSurgicalPathologyReport>
{
	private static DeidentifiedSurgicalPathologyReportFactory deIdentifiedSPRFactory;

	private DeidentifiedSurgicalPathologyReportFactory()
	{
		super();
	}

	public static synchronized DeidentifiedSurgicalPathologyReportFactory getInstance()
	{
		if(deIdentifiedSPRFactory==null){
			deIdentifiedSPRFactory = new DeidentifiedSurgicalPathologyReportFactory();
		}
		return deIdentifiedSPRFactory;
	}
	public DeidentifiedSurgicalPathologyReport createClone(DeidentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public DeidentifiedSurgicalPathologyReport createObject()
	{
		DeidentifiedSurgicalPathologyReport deIdReport=new DeidentifiedSurgicalPathologyReport();
		initDefaultValues(deIdReport);
		return deIdReport;
	}

	public void initDefaultValues(DeidentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub

	}

}
