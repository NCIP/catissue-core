package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class CancerResearchGroupTestCases extends CaTissueSuiteSmokeBaseTest{

	public CancerResearchGroupTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public CancerResearchGroupTestCases(String name)
	{
		super(name);
	}
	public CancerResearchGroupTestCases()
	{
		super();
	}
	/*
	 * Test case for adding Cancer Research Group.
	 */
	public void testCancerResearchGroupAdd()
	{
		String[] arr = getDataObject().getValues();
		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();
		cancerResearchGroupForm.setName(arr[0]);
		cancerResearchGroupForm.setOperation("add");
		setRequestPathInfo("/CancerResearchGroupAdd");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}
	/*
	 * Test case for editing Cancer Research Group.
	 */
	public void testCancerResearchGroupEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "CancerResearchGroup");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");

		/*Simple Search Action*/
		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setAliasName("CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "CancerResearchGroup");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "CancerResearchGroup.NAME.varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[0]);
		simpleQueryInterfaceForm.setPageOf("pageOfCancerResearchGroup");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup");
		addRequestParameter("operation", "search");
		String actionForward = getActualForward();
		String id=actionForward.substring(70);
		addRequestParameter("id", id);
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");

		setRequestPathInfo("/CancerResearchGroupSearch");
		addRequestParameter("id", "" + id);
		addRequestParameter("pageOf", "pageOfCancerResearchGroup" );
		actionPerform();
		verifyForward("pageOfCancerResearchGroup");
		verifyNoActionErrors();

		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfCancerResearchGroup");
		addRequestParameter("menuSelected", "2");
		actionPerform();
		verifyNoActionErrors();

		CancerResearchGroupForm cancerResearchGroupForm = (CancerResearchGroupForm)getActionForm();
		cancerResearchGroupForm.setName(arr[1]);
		cancerResearchGroupForm.setOperation("edit");
		setRequestPathInfo("/CancerResearchGroupEdit");
		setActionForm(cancerResearchGroupForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}
}
