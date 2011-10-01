package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class BiohazardTestCase extends CaTissueSuiteSmokeBaseTest
{

	public BiohazardTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public BiohazardTestCase(String name)
	{
		super(name);
	}
	public BiohazardTestCase()
	{
		super();
	}

	public void testAddBiohazard()
	{
		String[] arr = getDataObject().getValues();
		BiohazardForm bioForm = new BiohazardForm();
		bioForm.setName(arr[0]);
		bioForm.setType(arr[1]);
		bioForm.setOperation("add");
		bioForm.setComments("BioHazard Comments") ;
		setRequestPathInfo("/BiohazardAdd");
		setActionForm(bioForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

	}

	public void testBiohazardEdit()
	{
		String[] arr = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "Biohazard");
		addRequestParameter("pageOf", "pageOfBioHazard" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setAliasName("Biohazard");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Biohazard");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Biohazard."+arr[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", arr[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",arr[2]);


		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfBioHazard");
		addRequestParameter("operation", "search");
		addRequestParameter("id", arr[2]);
		actionPerform();


		setRequestPathInfo("/BiohazardSearch");
		addRequestParameter("pageOf", "pageOfBioHazard" );
		actionPerform();

		BiohazardForm bioHazardForm = (BiohazardForm)getActionForm();

		setRequestPathInfo("/Biohazard");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfBioHazard");
		setActionForm(bioHazardForm);
		actionPerform();


		bioHazardForm.setName(arr[3]);
		bioHazardForm.setType(arr[4]);
		bioHazardForm.setComments(arr[5]);

		setRequestPathInfo("/BiohazardEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfBioHazard");
		setActionForm(bioHazardForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}
}
