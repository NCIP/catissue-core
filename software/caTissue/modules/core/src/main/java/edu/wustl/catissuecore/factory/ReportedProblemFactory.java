package edu.wustl.catissuecore.factory;

import java.util.Calendar;

import edu.wustl.catissuecore.domain.ReportedProblem;


public class ReportedProblemFactory implements InstanceFactory<ReportedProblem>
{
	private static ReportedProblemFactory rpFactory;

	private ReportedProblemFactory() {
		super();
	}

	public static synchronized ReportedProblemFactory getInstance()
	{
		if(rpFactory == null) {
			rpFactory = new ReportedProblemFactory();
		}
		return rpFactory;
	}
	public ReportedProblem createClone(ReportedProblem t)
	{

		return null;
	}

	public ReportedProblem createObject()
	{
		ReportedProblem reportedProblem=new ReportedProblem();
		initDefaultValues(reportedProblem);
		return reportedProblem;
	}

	public void initDefaultValues(ReportedProblem t)
	{
		t.setReportedDate(Calendar.getInstance().getTime());
	}

}
