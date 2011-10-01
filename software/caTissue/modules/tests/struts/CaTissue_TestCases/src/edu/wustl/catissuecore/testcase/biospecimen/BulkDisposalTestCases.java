package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.BaseTestCaseUtility;
import edu.wustl.catissuecore.testcase.util.BizTestCaseUtility;
import edu.wustl.catissuecore.util.global.Constants;


public class BulkDisposalTestCases extends CaTissueSuiteBaseTest
{
	public void testBulkDisposalEventOfSpecimen()
	{
		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) BizTestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		System.out.println("scg "+scg.getId());
		TissueSpecimen specimen1 = (TissueSpecimen)BaseTestCaseUtility.initTissueSpecimen();
		specimen1.setSpecimenCollectionGroup(scg);
		try
		{
			specimen1 = (TissueSpecimen) appService.createObject(specimen1);
		}
		catch (Exception e)
		{
			System.out.println("BulkDisposalTestCases.testBulkDisposalEventOfSpecimen()"+e.getMessage());
		}

		TissueSpecimen specimen2 = (TissueSpecimen)BaseTestCaseUtility.initTissueSpecimen();
		specimen2.setSpecimenCollectionGroup(scg);
		try
		{
			specimen2 = (TissueSpecimen) appService.createObject(specimen2);
		}
		catch (Exception e)
		{
			System.out.println("BulkDisposalTestCases.testBulkDisposalEventOfSpecimen()"+e.getMessage());
		}

		List<String> specimenIds = new LinkedList<String>();
		specimenIds.add( ""+ specimen1.getId());
		specimenIds.add( ""+ specimen2.getId());

		setRequestPathInfo("/BulkDisposalEvents");
		BulkEventOperationsForm bulkEventOperationsForm = new BulkEventOperationsForm();
		bulkEventOperationsForm.setOperation( "bulkDisposals" );
		setActionForm(bulkEventOperationsForm);
		getRequest().setAttribute( Constants.SPECIMEN_ID, specimenIds );
		getRequest().setAttribute( Constants.OPERATION, "bulkDisposals" );
		actionPerform();
		verifyForward(Constants.SUCCESS);
		verifyNoActionErrors();

		setRequestPathInfo("/BulkTransferEventsSubmit");
		BulkEventOperationsForm bulkEventOperationsForm1 = (BulkEventOperationsForm)getActionForm();
		bulkEventOperationsForm1.setOperation( "bulkDisposals" );
		LinkedHashMap<String, String> eventSpecificData = bulkEventOperationsForm1.getEventSpecificData();
		eventSpecificData.put( "ID_ALL_REASON", "" );
		eventSpecificData.put( "ID_ALL_STATUS",  "Disabled");
		actionPerform();
		System.out.println("testBulkDisposalEventOfSpecimen child1 ***** "+specimen1.getLabel());
		System.out.println("testBulkDisposalEventOfSpecimen child2 ***** "+specimen2.getLabel());
		verifyForward("bulkDisposals");
		verifyNoActionErrors();

	}
}
