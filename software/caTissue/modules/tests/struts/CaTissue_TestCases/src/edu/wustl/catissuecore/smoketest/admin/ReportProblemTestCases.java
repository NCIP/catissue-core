package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class ReportProblemTestCases extends CaTissueSuiteSmokeBaseTest
{
	public ReportProblemTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public ReportProblemTestCases(String name)
	{
		super(name);
	}
	public ReportProblemTestCases()
	{
		super();
	}

	/* Test case For Report Problem Add */

	public void TestReportProblemAdd()
	 {
		 String[] InputData = getDataObject().getValues();

		 ReportedProblemForm reportedForm =  new ReportedProblemForm();

		 setRequestPathInfo("/ReportProblem");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","null");
		 setActionForm(reportedForm);
		 actionPerform();

		 reportedForm = (ReportedProblemForm)getActionForm();

		 setRequestPathInfo("/ReportedProblemAdd");
		 addRequestParameter("operation","add");
		 addRequestParameter("pageOf","null");

		 setActionForm(reportedForm);
		 addRequestParameter("operation","add");
		 addRequestParameter("from",InputData[0]);
		 addRequestParameter("nameOfReporter",InputData[1]);
		 addRequestParameter("affiliation",InputData[2]);
		 addRequestParameter("subject",InputData[3]);
		 addRequestParameter("messageBody",InputData[4]);
		 actionPerform();

		 verifyForward("success");
		 verifyNoActionErrors();
	}


	public void testReportProblemProcess()
	 {
		 String[] InputData = getDataObject().getValues();

		 ReportedProblemForm reportedForm =  new ReportedProblemForm();

		 setRequestPathInfo("/ReportedProblemShow");
		 addRequestParameter("pageNum","1");
		 addRequestParameter("operation","edit");
		 setActionForm(reportedForm);
		 actionPerform();

		 reportedForm = (ReportedProblemForm)getActionForm();

		 setRequestPathInfo("/ProblemDetails");
		 addRequestParameter("operation","edit");

		 addRequestParameter("id",InputData[0]);
		 setActionForm(reportedForm);
		 actionPerform();

		 setRequestPathInfo("/AdminReportProblem");
		 addRequestParameter("operation","edit");
		 actionPerform();

		 reportedForm.setActivityStatus(InputData[6]);

		 setRequestPathInfo("/ReportedProblemEdit");
		 addRequestParameter("from",InputData[1]);
		 addRequestParameter("nameOfReporter",InputData[2]);
		 addRequestParameter("affiliation",InputData[3]);
		 addRequestParameter("subject",InputData[4]);
		 addRequestParameter("messageBody",InputData[5]);
		 addRequestParameter("operation","edit");
		 addRequestParameter("menuSelected","11");
		 setActionForm(getActionForm());
		 actionPerform();
		 verifyForward("success");
		 verifyNoActionErrors();
		}
}
