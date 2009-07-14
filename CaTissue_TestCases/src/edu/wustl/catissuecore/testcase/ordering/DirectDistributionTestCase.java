package edu.wustl.catissuecore.testcase.ordering;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.Constants;

public class DirectDistributionTestCase extends CaTissueSuiteBaseTest
{

	/**
	 * Test Department Add.
	 */
	@Test
	public void testDirectDistribution()
	{
		List<String> specimenIds = new ArrayList<String>();
		//specimenIds.add("10");
		specimenIds.add("23");
		getSession().setAttribute(Constants.SPECIMEN_ID,specimenIds);
		setRequestPathInfo("/RequestToDistribute");
		actionPerform();
	
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/SpecDistribute");
		actionPerform();
		
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/specToDistribute");
		actionPerform();
		
		addRequestParameter("typeOf","specimen");
		setRequestPathInfo("/DirectDistributeSpec");
		actionPerform();
		
		addRequestParameter("type","directDistribution");
		setRequestPathInfo("/RequestDetails");
		actionPerform();
		
		List requestDetailsList = new ArrayList();
		if(request.getAttribute(Constants.REQUEST_DETAILS_LIST) != null )
			requestDetailsList = (List) request.getAttribute(Constants.REQUEST_DETAILS_LIST);	
		else 
			requestDetailsList = (List) getSession().getAttribute(Constants.REQUEST_DETAILS_LIST);	
		
		Integer count=0;
		if(requestDetailsList!=null && requestDetailsList.size()>0 )
		{
		 count=requestDetailsList.size();	
		}
		
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm)getActionForm();
		requestDetailsForm.setDistributionProtocolId("2");
		requestDetailsForm.setOrderName("Order_"+requestDetailsForm.getId());
		requestDetailsForm.setValue("RequestDetailsBean:0_assignedStatus", Constants.ORDER_REQUEST_STATUS_DISTRIBUTED);
		requestDetailsForm.setValue("RequestDetailsBean:0_canDistribute","true");
		requestDetailsForm.setIsDirectDistribution(Boolean.TRUE);
		requestDetailsForm.setSite("2");
		addRequestParameter("submittedFor","ForwardTo");
		addRequestParameter("noOfRecords",count.toString());
		
		setRequestPathInfo("/SubmitRequestDetails");
		actionPerform();
		setRequestPathInfo("/DistributionReport");
		actionPerform();
	
		ConfigureResultViewForm configureResultViewForm = (ConfigureResultViewForm)getActionForm();
		assertNotNull(configureResultViewForm.getSelectedColumnNames());
		verifyNoActionErrors();
		
		
	}
}
