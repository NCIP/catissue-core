package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.ReportSection;


public class ReportSectionFactory implements InstanceFactory<ReportSection>
{
	private static ReportSectionFactory reportSectionFactory;

	private ReportSectionFactory()
	{
		super();
	}

	public static synchronized ReportSectionFactory getInstance()
	{
		if(reportSectionFactory==null)
		{
			reportSectionFactory = new ReportSectionFactory();
		}
		return reportSectionFactory;
	}

	public ReportSection createClone(ReportSection t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ReportSection createObject()
	{
		ReportSection reportSection= new ReportSection();
		initDefaultValues(reportSection);
		return reportSection;
	}

	public void initDefaultValues(ReportSection t)
	{
		// TODO Auto-generated method stub

	}

}
