package edu.wustl.catissuecore.testcase.bizlogic;

import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class DeIdentifiedSurgicalPathologyReportBizTestCases extends CaTissueSuiteBaseTest {

	AbstractDomainObject domainObject = null;
	public void testAddDeidentifiedSurgicalPathologyReport()
	{
		try
		{
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport= BaseTestCaseUtility.initDeIdentifiedSurgicalPathologyReport();			
			System.out.println(deIdentifiedSurgicalPathologyReport);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			deIdentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) appService.createObject(deIdentifiedSurgicalPathologyReport,bean);
			IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=(IdentifiedSurgicalPathologyReport)TestCaseUtility.getObjectMap(IdentifiedSurgicalPathologyReport.class);
			identifiedSurgicalPathologyReport.setDeIdentifiedSurgicalPathologyReport(deIdentifiedSurgicalPathologyReport);
			identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.updateObject(identifiedSurgicalPathologyReport,bean);
			TestCaseUtility.setObjectMap(deIdentifiedSurgicalPathologyReport, DeidentifiedSurgicalPathologyReport.class);
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testUpdateDeidentifiedSurgicalPathologyReport()
	{
		DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport=  new DeidentifiedSurgicalPathologyReport();
    	Logger.out.info("updating domain object------->"+deIdentifiedSurgicalPathologyReport);
    	SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	    try 
		{
	    	deIdentifiedSurgicalPathologyReport=BaseTestCaseUtility.updateIdentifiedSurgicalPathologyReport(deIdentifiedSurgicalPathologyReport);	
	    	DeidentifiedSurgicalPathologyReport updatedDeIdentifiedSurgicalPathologyReport = (DeidentifiedSurgicalPathologyReport) appService.updateObject(deIdentifiedSurgicalPathologyReport,bean);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedDeIdentifiedSurgicalPathologyReport);
	       	assertTrue("Domain object successfully updated ---->"+updatedDeIdentifiedSurgicalPathologyReport, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
}
