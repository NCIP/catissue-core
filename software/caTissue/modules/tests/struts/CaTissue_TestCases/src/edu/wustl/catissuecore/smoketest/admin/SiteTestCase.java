package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SiteTestCase extends CaTissueSuiteSmokeBaseTest
{
	public SiteTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SiteTestCase(String name)
	{
		super(name);
	}
	public SiteTestCase()
	{
		super();
	}

	/**
	 * Test Institution Add.
	 */

	public void testSiteAdd()
	{
		String[] arr = getDataObject().getValues();

		SiteForm siteForm = new SiteForm();
		setRequestPathInfo("/Site");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfSite");
		setActionForm(siteForm);
		actionPerform();
		verifyForward("pageOfSite");
		verifyTilesForward("pageOfSite",".catissuecore.siteDef");

		siteForm=(SiteForm)getActionForm();
		siteForm.setName(arr[0]);
		siteForm.setType(arr[1]);
		siteForm.setCoordinatorId(Long.parseLong(arr[2]));
		siteForm.setEmailAddress(arr[3]);
		siteForm.setStreet(arr[4]);
		siteForm.setCity(arr[5]);
		siteForm.setState(arr[6]);
		siteForm.setZipCode(arr[7]);
		siteForm.setCountry(arr[8]);
		siteForm.setPhoneNumber(arr[9]);
		siteForm.setFaxNumber(arr[10]);

		setRequestPathInfo("/SiteAdd");
		siteForm.setOperation("add") ;
		addRequestParameter("pageOf","pageOfSite");
		setActionForm(siteForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}

	public void testSiteEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Site");
		addRequestParameter("pageOf", "pageOfSite" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();
		verifyForward("pageOfSite");
		verifyTilesForward("pageOfSite",".catissuecore.editSearchPageDef");
		verifyNoActionErrors();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Site");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Site."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator",arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
		simpleQueryInterfaceForm.setPageOf("pageOfSite");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SpreadsheetView");
		actionPerform();
		verifyTilesForward("pageOfSite",".catissuecore.editSearchResultsDef");

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();

		setRequestPathInfo("/SiteSearch");
		addRequestParameter("pageOf", "pageOfSite");
		actionPerform();
		verifyForward("pageOfSite");


		SiteForm siteForm = (SiteForm) getActionForm() ;
		setRequestPathInfo("/Site");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSite");
		addRequestParameter("menuSelected", "5");
		setActionForm(siteForm);
		actionPerform();
		verifyForward("pageOfSite");
		verifyTilesForward("pageOfSite",".catissuecore.siteDef");


		siteForm.setName(arr[3]);
		siteForm.setType(arr[4]);
		siteForm.setCoordinatorId(Long.parseLong(arr[5]));
		siteForm.setEmailAddress(arr[6]);
		siteForm.setActivityStatus(arr[7]);
		siteForm.setStreet(arr[8]);
		siteForm.setCity(arr[9]);
		siteForm.setState(arr[10]);
		siteForm.setZipCode(arr[11]);
		siteForm.setCountry(arr[12]);
		siteForm.setPhoneNumber(arr[13]);
		siteForm.setFaxNumber(arr[14]);


		siteForm.setOperation("edit");
		setRequestPathInfo("/SiteEdit");
		setActionForm(siteForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}



}
