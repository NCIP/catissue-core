package edu.wustl.catissuecore.smoketest.admin;

import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.testframework.util.DataObject;

public class SpecimenArrayTypeTestCases extends CaTissueSuiteSmokeBaseTest
{
	public SpecimenArrayTypeTestCases(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SpecimenArrayTypeTestCases(String name)
	{
		super(name);
	}
	public SpecimenArrayTypeTestCases()
	{
		super();
	}

	/**
	 * Test Specimen Array Type Add.
	 */
	public void testSpecimenArrayTypeAdd()
	{
		String array[] = getDataObject().getValues();
		SpecimenArrayTypeForm specForm = new SpecimenArrayTypeForm();

		specForm.setName(array[0]);

		specForm.setSpecimenClass(array[1]);

		specForm.setSpecimenTypes(new String[]{"Plasma"});

		specForm.setOneDimensionCapacity(4) ;
		specForm.setTwoDimensionCapacity(4);
		specForm.setOperation("add");
		setRequestPathInfo("/SpecimenArrayTypeAdd");
		setActionForm(specForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.add.successOnly"});
		assertEquals(SpecimenArrayTypeForm.class.getName(),getActionForm().getClass().getName());
	}
	/**
	 * Test Specimen Array Type Edit.
	 */
	public void testSpecimenArrayTypeEdit()
	{
		String[] array = getDataObject().getValues();

		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();

		setRequestPathInfo("/SimpleQueryInterface");
		addRequestParameter("aliasName", "SpecimenArrayType");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();

		simpleQueryInterfaceForm = (SimpleQueryInterfaceForm)getActionForm();
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "SpecimenArrayType");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "SpecimenArrayType."+array[0]+".varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", array[1]);
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value",array[2]);


		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "SpecimenArrayType");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType" );
		setActionForm(simpleQueryInterfaceForm);
		actionPerform();


		setRequestPathInfo("/SearchObject");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		addRequestParameter("operation", "search");
		addRequestParameter("id", array[2]);
		actionPerform();


		setRequestPathInfo("/SpecimenArrayTypeSearch");
		addRequestParameter("operation", "search");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
     		actionPerform();

     		SpecimenArrayTypeForm specimenArrayTypeForm = (SpecimenArrayTypeForm) getActionForm() ;


		setRequestPathInfo("/SpecimenArrayType");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		setActionForm(specimenArrayTypeForm);
		actionPerform();

		specimenArrayTypeForm.setName(array[3]);
		specimenArrayTypeForm.setSpecimenClass(array[4]);
		String arr1[] = {array[5]};
		specimenArrayTypeForm.setSpecimenTypes(arr1);
		specimenArrayTypeForm.setComment(array[6]);
		specimenArrayTypeForm.setOneDimensionCapacity(Integer.parseInt(array[7].trim()));
		specimenArrayTypeForm.setTwoDimensionCapacity(Integer.parseInt(array[8].trim()));



		setRequestPathInfo("/SpecimenArrayTypeEdit");
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfSpecimenArrayType");
		setActionForm(specimenArrayTypeForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.edit.successOnly"});

	}
}
