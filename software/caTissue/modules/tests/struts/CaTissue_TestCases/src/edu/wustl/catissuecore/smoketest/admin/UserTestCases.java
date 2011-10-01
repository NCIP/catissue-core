package edu.wustl.catissuecore.smoketest.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import edu.emory.mathcs.backport.java.util.LinkedList;
import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.SummaryForm;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class UserTestCases extends CaTissueSuiteSmokeBaseTest {

	public UserTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public UserTestCases(String name)
	{
		super(name);
	}
	public UserTestCases()
	{
		super();
	}

	public void testUserAdd()
	{
		UserForm userForm = new UserForm();
		String arr[] = getDataObject().getValues();
		userForm.setFirstName(arr[0]);
		userForm.setLastName(arr[1]);
		userForm.setCity(arr[2]);
		userForm.setState(arr[3]);
		userForm.setCountry(arr[4]);
		userForm.setZipCode(arr[5]);
		userForm.setPhoneNumber(arr[6]);
		userForm.setInstitutionId(Long.parseLong(arr[7]));
		userForm.setDepartmentId(Long.parseLong(arr[8]));
		userForm.setCancerResearchGroupId(Long.parseLong(arr[9]));
		userForm.setOperation("add");
		userForm.setRole(arr[10]);
		userForm.setEmailAddress(arr[11]);
		userForm.setConfirmEmailAddress(arr[12]);
		String siteIds[] = new String[1];
        siteIds[0] = arr[13];
		userForm.setSiteIds(siteIds);
		userForm.setTargetLoginName("");
		userForm.setPageOf("");
		addRequestParameter("operation", "add");
		addRequestParameter("dirtyVar", "true");
		addRequestParameter("menuSelected", "1");
		setRequestPathInfo("/UserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(UserForm.class.getName(),getActionForm().getClass().getName());

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
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "User");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "User."+arr[0]+".varchar");
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
	simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);
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
	addRequestParameter("id", arr[2]);
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


	userForm.setFirstName(arr[3]);
	userForm.setLastName(arr[4]);
	userForm.setCity(arr[5]);
	userForm.setState(arr[6]);
	userForm.setCountry(arr[7]);
	userForm.setZipCode(arr[8]);
	userForm.setPhoneNumber(arr[9]);
	userForm.setInstitutionId(Integer.parseInt(arr[10]));
	userForm.setDepartmentId((Integer.parseInt(arr[11])));
	userForm.setCancerResearchGroupId(Integer.parseInt(arr[12]));


	userForm.setRole(arr[13]);
	userForm.setEmailAddress(arr[14]);
	userForm.setConfirmEmailAddress(arr[15]);
	userForm.setNewPassword(arr[17]);
	userForm.setConfirmNewPassword(arr[17]);


	String siteIds[] = new String[1];
	siteIds[0] = arr[16];
	userForm.setSiteIds(siteIds);


	userForm.setOperation("edit");
	setRequestPathInfo("/UserEdit");
	setActionForm(userForm);
	actionPerform();
	verifyForward("success");
	verifyActionMessages(new String[]{"object.edit.successOnly"});
	}
	public void testSignUp()
	{
		String[] arr = getDataObject().getValues();

		UserForm userForm= new UserForm();
		setRequestPathInfo("/SignUp");
		addRequestParameter("operation", "add");
		addRequestParameter("pageOf", "pageOfSignUp");
		setActionForm(userForm);
		actionPerform();
		verifyTilesForward("pageOfSignUp",".catissuecore.userSignUpDef");

		userForm.setEmailAddress(arr[0]);
		userForm.setConfirmEmailAddress(arr[1]);
		userForm.setFirstName(arr[2]);
		userForm.setLastName(arr[3]);
		userForm.setCity(arr[4]);
		userForm.setState(arr[5]);
		userForm.setZipCode(arr[6]);
		userForm.setCountry(arr[7]);
		userForm.setPhoneNumber(arr[8]);
		userForm.setInstitutionId(Long.parseLong(arr[9]));
		userForm.setDepartmentId(Long.parseLong(arr[10]));
		userForm.setCancerResearchGroupId(Long.parseLong(arr[11]));

		setRequestPathInfo("/SignUpUserAdd");
		setActionForm(userForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}
	public void testApproveNewUser()
	{
		String[] Input_Data = getDataObject().getValues();

		UserForm userForm = new UserForm();

		setRequestPathInfo("/ApproveUserShow");
		addRequestParameter("pageNum", "1" );
		setActionForm(userForm);
		actionPerform();

		setRequestPathInfo("/UserDetailsShow");
		addRequestParameter("id", Input_Data[0]);
		setActionForm(userForm);
		actionPerform();


		userForm = (UserForm) getActionForm() ;
		setRequestPathInfo("/User");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfApproveUser");
		addRequestParameter("menuSelected", "1");
		setActionForm(userForm);
		actionPerform();
		userForm.setRole(Input_Data[1]);

		String siteIds[] = new String[1];
		siteIds[0] = Input_Data[2];
		userForm.setSiteIds(siteIds);

		userForm.setStatus(Input_Data[3]);
		userForm.setComments(Input_Data[4]);

		userForm.setOperation("edit");
		setRequestPathInfo("/ApproveUserEdit");
		setActionForm(userForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});
	}

	public void testUserCancerResearchAdd()
	{
		String arr[] = getDataObject().getValues();

		UserForm userForm = new UserForm();

		setRequestPathInfo("/User");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfUserAdmin");
		setActionForm(userForm);
		actionPerform();

		CancerResearchGroupForm cancerResearchGroupForm = new CancerResearchGroupForm();

		request.setAttribute("submittedFor","AddNew");

		setRequestPathInfo("/CancerResearchGroup");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfPopUpCancerResearchGroup");
		setActionForm(cancerResearchGroupForm);
		actionPerform();

		setRequestPathInfo("/AddCancerResearchGroup");
		addRequestParameter("operation","add");
		addRequestParameter("crgName",arr[0]);
		setActionForm(cancerResearchGroupForm);
		actionPerform();

		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}

	public void testUserDepartmentAdd()
	{
		String arr[] = getDataObject().getValues();

		UserForm userForm = new UserForm();

		setRequestPathInfo("/User");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfUserAdmin");
		setActionForm(userForm);
		actionPerform();

		DepartmentForm departmentForm = new DepartmentForm();

		request.setAttribute("submittedFor","AddNew");

		setRequestPathInfo("/Department");
		addRequestParameter("operation","add");
		addRequestParameter("pageOf","pageOfPopUpDepartment");
		setActionForm(departmentForm);
		actionPerform();


		setRequestPathInfo("/AddDepartment");
		addRequestParameter("operation","add");
		addRequestParameter("departmentName",arr[0]);
		setActionForm(departmentForm);
		actionPerform();

		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}

	public void testSummary()
	{
		SummaryForm summaryForm = new SummaryForm();

		setRequestPathInfo("/Summary");
		setActionForm(summaryForm);
		actionPerform();
	}


}
