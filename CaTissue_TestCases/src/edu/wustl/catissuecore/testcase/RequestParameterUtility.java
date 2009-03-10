package edu.wustl.catissuecore.testcase;

import servletunit.struts.MockStrutsTestCase;


public class RequestParameterUtility
{
	public static void setAddSiteParams(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("name","Site_"+UniqueKeyGeneratorUtil.getUniqueKey());
		testcase.addRequestParameter("emailAddress","janhavi_hasabnis@persistent.co.in");
		testcase.addRequestParameter("coordinatorId","2");
		testcase.addRequestParameter("street", "xyz");
		testcase.addRequestParameter("state","Alaska");
		testcase.addRequestParameter("country","India");
		testcase.addRequestParameter("zipCode","335001");
		testcase.addRequestParameter("phoneNumber","9011083118");
		testcase.addRequestParameter("city","Sri Ganga Nagar");
		testcase.addRequestParameter("operation","add");
		
	}
	
	public static void setEditSiteParams(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("aliasName", "Site");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Site");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Site.NAME.varchar");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		testcase.addRequestParameter("counter","1");
		testcase.addRequestParameter("pageOf","pageOfSite");
		testcase.addRequestParameter("operation","search");
	}
}
