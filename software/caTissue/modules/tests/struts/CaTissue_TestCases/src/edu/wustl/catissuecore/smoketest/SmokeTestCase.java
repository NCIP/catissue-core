package edu.wustl.catissuecore.smoketest;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.smoketest.util.CaTissueSuiteTestUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SmokeTestCase extends CaTissueSuiteSmokeBaseTest
{
	public SmokeTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SmokeTestCase(String name)
	{
		super(name);
	}
	public SmokeTestCase()
	{
		super();
	}

	public void testLogin()
	{

		setRequestPathInfo("/Login") ;
		//		DataObject dataObject = TestCaseDataUtil.getDataObject("testSuccessfulLogin");
		DataObject dataObject = getDataObject();
		System.out.println("login values :"+ dataObject.getUsername() + dataObject.getPassword());
		addRequestParameter("loginName", dataObject.getUsername());
		addRequestParameter("password", dataObject.getPassword());
		logger.info("start in login");
		actionPerform();
		//verifyForward("/Home.do");
		logger.info("Login: "+getActualForward());
		verifyForward("pageOfNonWashU");

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		assertEquals("user name should be equal to logged in username","admin@admin.com",bean.getUserName());
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
		verifyNoActionErrors();
		logger.info("end in login");
		System.out.println(getActualForward());
	}
	public void testLogout()
	{
		setRequestPathInfo("/Logout") ;
		actionPerform();
		//		verifyForward("success");k
		CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN = null;
	}

	/**
	 * Test Institution Add.
	 */

	public void testInstitutionAdd()
	{
		InstitutionForm institutionForm = new InstitutionForm();
		String arr[] = getDataObject().getValues();
		institutionForm.setName(arr[0]);
		institutionForm.setOperation("add");
		setRequestPathInfo("/InstitutionAdd");
		setActionForm(institutionForm);

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(InstitutionForm.class.getName(),getActionForm().getClass().getName());
	}

	/**
	 * Test Department Add.
	 */

	public void testDepartmentAdd()
	{
		String[] arr = getDataObject().getValues();
		DepartmentForm departmentForm  = new DepartmentForm ();
		departmentForm.setName(arr[0]);
		departmentForm.setOperation("add");
		setRequestPathInfo("/DepartmentAdd");
		setActionForm(departmentForm);

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
	}

	public void testDepartmentEdit()
	{
	String[] arr = getDataObject().getValues();

	SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
	setRequestPathInfo("/SimpleQueryInterface");
	addRequestParameter("aliasName", "Department");
	addRequestParameter("pageOf", "pageOfDepartment" );
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();
	verifyForward("pageOfDepartment");
	verifyTilesForward("pageOfDepartment",".catissuecore.editSearchPageDef");
	verifyNoActionErrors();


	simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "DepartmentForm");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "DepartmentForm.IDENTIFIER.varchar");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[0]);
	simpleQueryInterfaceForm.setPageOf("pageOfDepartment");
	setRequestPathInfo("/SimpleSearch");
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();


	setRequestPathInfo("/SpreadsheetView");
	actionPerform();
	verifyTilesForward("pageOfDepartment",".catissuecore.editSearchResultsDef");


	setRequestPathInfo("/SearchObject");
	addRequestParameter("pageOf", "pageOfDepartment");
	addRequestParameter("operation", "search");
	addRequestParameter("id", arr[0]);
	actionPerform();

	setRequestPathInfo("/DepartmentSearch");
	addRequestParameter("pageOf", "pageOfDepartment");
	actionPerform();
	verifyForward("pageOfDepartment");

	DepartmentForm departmentForm = (DepartmentForm) getActionForm() ;
	setRequestPathInfo("/Department");
	addRequestParameter("operation", "edit");
	addRequestParameter("pageOf", "pageOfDepartment");
	addRequestParameter("menuSelected", "3");
	setActionForm(departmentForm);
	actionPerform();
	verifyForward("pageOfDepartment");
	verifyTilesForward("pageOfDepartment",".catissuecore.departmentDef");

	departmentForm.setName(arr[1]);
	departmentForm.setOperation("edit");
	setRequestPathInfo("/DepartmentEdit");
	setActionForm(departmentForm);
	actionPerform();
	verifyForward("success");
	verifyActionMessages(new String[]{"object.edit.successOnly"});

	}

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

	public void testAddUser()
	{
		String[] arr = getDataObject().getValues();
		addRequestParameter("pageOf", "pageOfUserAdmin");
		final UserForm form = new UserForm();
		form.setFirstName(arr[0]);
		form.setLastName(arr[1]);
		form.setCity(arr[2]);
		form.setState(arr[3]);
		form.setCountry(arr[4]);
		form.setZipCode(arr[5]);
		form.setPhoneNumber(arr[6]);
		form.setInstitutionId(Long.valueOf(arr[7]));
		form.setDepartmentId((Long.valueOf(arr[8])));
		form.setCancerResearchGroupId(Long.valueOf(arr[9]));
		form.setOperation("add");
		form.setRole(arr[10]);
		form.setEmailAddress(arr[11]);
		form.setConfirmEmailAddress(arr[12]);
		final String siteIds[] = new String[1];
		siteIds[0] = arr[13];
		form.setSiteIds(siteIds);
		form.setTargetLoginName("");
		addRequestParameter("operation", "add");
		addRequestParameter("dirtyVar", "true");
		addRequestParameter("menuSelected", "1");
		setRequestPathInfo("/UserAdd");
		setActionForm(form);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();


		//add site object to objectMap

	}

	public void testUserEdit()
	{
	String[] arr = getDataObject().getValues();

	SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
	setRequestPathInfo("/SimpleQueryInterface");
	addRequestParameter("aliasName", "User");
	addRequestParameter("pageOf", "pageOfUserAdmin" );
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();
	verifyForward("pageOfUserAdmin");
	verifyTilesForward("pageOfUserAdmin",".catissuecore.editSearchPageDef");
	verifyNoActionErrors();


	simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "UserForm");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "UserForm.IDENTIFIER.varchar");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Equals");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[0]);
	simpleQueryInterfaceForm.setPageOf("pageOfUserAdmin");
	setRequestPathInfo("/SimpleSearch");
	setActionForm(simpleQueryInterfaceForm);
	actionPerform();


	setRequestPathInfo("/SpreadsheetView");
	actionPerform();
	verifyTilesForward("pageOfUserAdmin",".catissuecore.editSearchResultsDef");
	setRequestPathInfo("/SearchObject");
	addRequestParameter("pageOf", "pageOfUserAdmin");
	addRequestParameter("operation", "search");
	addRequestParameter("id", arr[0]);
	actionPerform();


	setRequestPathInfo("/UserSearch");
	addRequestParameter("pageOf", "pageOfUserAdmin");
	actionPerform();
	verifyForward("pageOfUserAdmin");


	UserForm userForm = (UserForm) getActionForm() ;
	setRequestPathInfo("/User");
	addRequestParameter("operation", "edit");
	addRequestParameter("pageOf", "pageOfUserAdmin");
	addRequestParameter("menuSelected", "1");
	setActionForm(userForm);
	actionPerform();
	verifyForward("pageOfUserAdmin");
	verifyTilesForward("pageOfUserAdmin",".catissuecore.userDef");


	userForm.setFirstName(arr[1]);
	userForm.setOperation("edit");
	setRequestPathInfo("/UserEdit");
	setActionForm(userForm);
	actionPerform();
	verifyForward("success");
	verifyActionMessages(new String[]{"object.edit.success"});
	}

}
