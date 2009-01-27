package edu.wustl.catissuecore.testcase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.common.beans.SessionDataBean;
/**
*
 * @author sachin_lale
 *
 */
public class LoginTestCase extends CaTissueSuiteBaseTest
{

/**
 * Test successful Login.
 */
	@Test
	public void testSuccessfulLogin()
	  {
	    	addRequestParameter("loginName","admin@admin.com");
	        addRequestParameter("password","admin");
	        setRequestPathInfo("/Login.do");
	        actionPerform();
	        //verifyForward("/Home.do");
	        verifyForward("success");

	        SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	        assertEquals("user name should be equal to loggedinusername","admin@admin.com",bean.getUserName());
	        CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
	        verifyNoActionErrors();
	  }
/**
 * Test CLick Administrative->Site menu.
 */
	@Test
	 public void testSiteClick()
	  {
			addRequestParameter("operation", "add");
			addRequestParameter("pageOf", "pageOfSite");
			setRequestPathInfo("/Site");
			actionPerform();
			verifyForward("pageOfSite");
			List stateList = (List)getRequest().getAttribute(Constants.STATELIST);
			assertNotNull("State List should not be null",stateList);
			actionPerform();
			setRequestPathInfo("/Site");
			addRequestParameter("isOnChange", "true");
			addRequestParameter("coordinatorId", "1");
			actionPerform();
			verifyForward("pageOfSite");
	  }

//	public void testInstitutionEdit()
//	{
//		setRequestPathInfo("/SimpleSearch");
//		addRequestParameter("aliasName", "Institution");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Institution");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Institution.Name.varchar");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
//		addRequestParameter("value(SimpleConditionsNode:1__Condition_value)","I");
//		addRequestParameter("counter","1");
//		addRequestParameter("pageOf","pageOfInstitution");
//		addRequestParameter("operation","search");
//		actionPerform();
//
//		verifyForward("success");
//	}
	/**
	 * TEst institution add.
	 */
	@Test
	public void testAddInstitution()
	{
		InstitutionForm form = new InstitutionForm();
		form.setName("");
		form.setOperation(Constants.ADD);
		getRequest().setAttribute("institutionForm", form);
		setRequestPathInfo("/InstitutionAdd");
		actionPerform();
		verifyForward("success");

	}

	/**
	 * Test institution edit.
	 */
	@Test
	public void testInstitutionEdit()
	{
		setRequestPathInfo("/SimpleSearch");
		SimpleQueryInterfaceForm simpleQueryInterfaceform = new SimpleQueryInterfaceForm();
		simpleQueryInterfaceform.setAliasName("Institution");
		Map valueMap = new HashMap();
		valueMap.put("SimpleConditionsNode:1_Condition_DataElement_table", "Institution");
		valueMap.put("SimpleConditionsNode:1_Condition_DataElement_field","Institution.Name.varchar");
		valueMap.put("SimpleConditionsNode:1_Condition_Operator_operator","Starts With");
		valueMap.put("SimpleConditionsNode:1__Condition_value","");
		addRequestParameter("counter","1");
		addRequestParameter("pageOf","pageOfInstitution");
		addRequestParameter("operation","search");
		simpleQueryInterfaceform.setCounter("1");
		simpleQueryInterfaceform.setValues(valueMap);
		getRequest().setAttribute("simpleQueryInterfaceForm", simpleQueryInterfaceform);
		actionPerform();

		verifyForward("success");
	}
}
