package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.HashMap;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;


public class AddToMyListTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Specimen + Create Aliquot/Derived Specimen as per CP
	 * Click on Add to My List.
	 */
	public void testSpecimenAddToMyList()
	{
		//Simple Search Action
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		//Retrieving specimen object for edit
		logger.info("----specimen ID : " + specimen.getId());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "search");
		addRequestParameter("id", specimen.getId().toString());
		setRequestPathInfo("/NewSpecimenSearch") ;
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();
		
		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();
		verifyNoActionErrors();
		
		setRequestPathInfo("/NewSpecimenEdit") ;
		NewSpecimenForm form = (NewSpecimenForm) getActionForm();
		form.setForwardTo( "addSpecimenToCartForwardtoCpChild" );	
		addRequestParameter("target", "addSpecimenToCartForwardtoCpChild");
		addRequestParameter("pageOf", "cpChildSubmit");
		actionPerform();
			
		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "cpChildSubmit");
		actionPerform();
		
		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		HashMap forwardToHashMap = new HashMap();
		forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId());
		forwardToHashMap.put("specimenId", specimen.getId());
		getRequest().setAttribute("forwardToHashMap",forwardToHashMap);
		addRequestParameter("target", "pageOfMultipleSpWithMenu");
		actionPerform();
		verifyForward("pageOfMultipleSpWithMenu");
		verifyNoActionErrors();		

	}
}
