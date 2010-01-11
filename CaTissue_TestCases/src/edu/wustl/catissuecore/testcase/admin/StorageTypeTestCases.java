
package edu.wustl.catissuecore.testcase.admin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

/**
 * This class contains test cases for Storage Type add/edit
 * @author Himanshu Aseeja
 */
public class StorageTypeTestCases extends CaTissueSuiteBaseTest
{

	/**
	 * Test Storage Type Add.
	 */
	
	public void testStorageTypeAdd()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("StorageType", storageType);
	}
	/**
	 * Test Storage Type Add.
	 */
	
	public void testAddRestrictedStorageType()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeFormWithTypeRestriction(this,
				"Rest_Type_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","10","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}
	/**
	 * Test Storage Type Add with same name (1x1 capacity).
	 */
	//RB S
	
	public void testStorageTypeAddWithSameName()
	{
		StorageType storageType1 = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				storageType1.getName(),1,1,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage Type Add with with only Tissue Specimen restriction.
	 */
	
	public void testStorageTypeAddTissueSpecimenClass()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"TissueStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),20,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"Tissue"});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("TissueStorageType", storageType);
	}
	/**
	 * Test Storage type Add with only Molecular Specimen restriction.
	 */
	
	public void testStorageTypeAddMolecularSpecimenClass()
	{

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"MolecularStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),10,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"Molecular"});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("MolecularStorageType", storageType);
	}
	/**
	 * Test Storage type Add with only Fluid Specimen restriction.
	 */
	
	public void testStorageTypeAddFluidSpecimenClass()
	{

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"FluidStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),10,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"Fluid"});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("FluidStorageType", storageType);
	}
	/**
	 * Test Storage type Add with only Cell Specimen restriction.
	 */
	
	public void testStorageTypeAddCellSpecimenClass()
	{

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"CellStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),10,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"Cell"});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("CellStorageType", storageType);
	}
	/**
	 * Test Storage type Add with only All Specimen restriction.
	 */
	
	public void testStorageTypeAddAllSpecimenClass()
	{

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"AllSpecimenStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),20,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"-1"});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("AllSpecimenStorageType", storageType);
	}
	/**
	 * Test Storage type Add without activity status.
	 * Negative Test Case.
	 */
	
	public void testAddStorageTypeWithoutActivityStatus()
	{

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),1,1,"row","col","22","");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}
	/**
	 * Test Storage type Add with empty parameters.
	 * Negative Test Case.
	 */
	
	public void testStorageTypeAddWithEmptyParameters()
	{
		StorageType storageType1 = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				storageType1.getName(),1,1,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setType("") ;
		storageTypeForm.setOneDimensionCapacity(0) ;
		storageTypeForm.setTwoDimensionCapacity(0) ;
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[]{"errors.item.required","errors.valid.number","errors.valid.number"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage type Add with invalid data.
	 * Negative Test Case.
	 */
	
	public void testStorageTypeAddWithInvalidData()
	{
		StorageType storageType1 = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				storageType1.getName(),1,1,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setType("storage@Type") ;
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[]{"errors.valid.data"};
		verifyActionErrors(errormsg);
	}
	/**
	 * Test Storage type Add with Box storage Type.
	 * Create Storage type hierarchy as Freezer(2X2) holds box (10x10)
	 */
	
	public void testAddBoxStorageTypeAdd()
	{
		// Adding Box of 10*10
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"Box_10_10_" + UniqueKeyGeneratorUtil.getUniqueKey(),10,10,"Dim1","Dim2","-29","Active");
		storageTypeForm.setSpecimenOrArrayType("");

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);


		TestCaseUtility.setNameObjectMap("Box_StorageType", storageType);


	}
	/**
	 * Test Storage type Add with Freezer storage Type.
	 * Create Storage type hierarchy as Freezer(2X2) holds box (10x10)
	 */
	
	public void testAddFreezerStorageType()
	{
		// Adding Box of 2*2 Freezer which holds Box of 10*10.
		StorageType boxStorageType = (StorageType) TestCaseUtility.getNameObjectMap("Box_StorageType");

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"Freezer_2_2_" + UniqueKeyGeneratorUtil.getUniqueKey(),2,2,"Dim11","Dim22","29","Active");
		storageTypeForm.setSpecimenOrArrayType("");
		storageTypeForm.setHoldsStorageTypeIds(new long[]{boxStorageType.getId()});

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		StorageType storageType2 = new StorageType();

		storageType2.setName(form2.getType());
		storageType2.setId(form2.getId());
		System.out.println("form2.getActivityStatus(): "+form2.getActivityStatus());
		storageType2.setOneDimensionLabel(form2.getOneDimensionLabel());
		storageType2.setTwoDimensionLabel(form2.getTwoDimensionLabel());
		storageType2.setDefaultTempratureInCentigrade(Double.parseDouble(form2
				.getDefaultTemperature()));

		Capacity capacity2 = new Capacity();
		capacity2.setOneDimensionCapacity(form2.getOneDimensionCapacity());
		capacity2.setTwoDimensionCapacity(form2.getTwoDimensionCapacity());
		storageType2.setCapacity(capacity2);

		TestCaseUtility.setNameObjectMap("Freezer_StorageType", storageType2);

	}
	/**
	 * Test Storage Type Search.
	 */
	
	public void testStorageTypeSearch()
	{
		/*Simple Search Action*/
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = new SimpleQueryInterfaceForm();
		simpleQueryInterfaceForm.setAliasName("StorageType");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "StorageType");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "ContainerType.NAME.varchar");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleQueryInterfaceForm.setValue("SimpleConditionsNode:1_Condition_value", "");
		simpleQueryInterfaceForm.setPageOf("pageOfStorageType");
		//addRequestParameter("operation", "search");
		setRequestPathInfo("/SimpleSearch");
		setActionForm(simpleQueryInterfaceForm);


		actionPerform();

		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}
		System.out.println("storageTypeList.size():"+storageTypeList.size());
		if (storageTypeList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (storageTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageType&operation=search&id"
					+ storageType.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}


	}
	/**
	 * Test Storage Type Edit.
	 */
	
	public void testFreezerStorageTypeEdit()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("Freezer_StorageType");
		setRequestPathInfo("/StorageTypeSearch");
		logger.info("StorageTypeId: "+storageType.getId()) ;
		addRequestParameter("id", "" + storageType.getId());
		addRequestParameter("pageOf", "pageOfStorageType" );

		// Perform serach
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("menuSelected", "6");
		actionPerform();
		verifyNoActionErrors();

		StorageTypeForm form = (StorageTypeForm)getActionForm();
		form.setSpecimenOrArrayType("Specimen") ;
		form.setOneDimensionLabel("A Row");
		form.setTwoDimensionLabel("B Row");
		form.setOneDimensionCapacity(15);
		form.setTwoDimensionCapacity(15);
		form.setDefaultTemperature("-15");
		form.setOperation("edit");
		/*Edit Action*/

		setRequestPathInfo("/StorageTypeEdit");
		setActionForm(form);
		actionPerform();
		verifyForward("success");
		//verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		assertEquals(form2.getOneDimensionCapacity(), 15);
		assertEquals(form2.getOneDimensionCapacity(), 15);
		storageType.getCapacity().setOneDimensionCapacity(15);
		storageType.getCapacity().setTwoDimensionCapacity(15);

		TestCaseUtility.setNameObjectMap("Freezer_2_2_StorageType", storageType);
	}
	/**
	 * Test Storage Type edit with increased capacity.
	 */
	
	public void testFreezerStorageTypeEditIncreseCapacity()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("Freezer_2_2_StorageType");

		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		addRequestParameter("pageOf", "pageOfStorageType" );

		// Perform serach
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("menuSelected", "6");
		actionPerform();
		verifyNoActionErrors();

		StorageTypeForm form = (StorageTypeForm)getActionForm();
		form.setType("Freezer_15_15_StorageType" + UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOneDimensionLabel("A Row");
		form.setTwoDimensionLabel("B Row");
		form.setOneDimensionCapacity(15);
		form.setTwoDimensionCapacity(15);
		form.setDefaultTemperature("-15");
		form.setOperation("edit");
		form.setActivityStatus("Active");
		form.setSpecimenOrArrayType("Specimen") ;
		/*Edit Action*/

		setRequestPathInfo("/StorageTypeEdit");
		setActionForm(form);
		actionPerform();
		verifyForward("success");
		//verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		assertEquals(form2.getOneDimensionCapacity(), 15);
		assertEquals(form2.getOneDimensionCapacity(), 15);
		storageType.getCapacity().setOneDimensionCapacity(15);
		storageType.getCapacity().setTwoDimensionCapacity(15);
		storageType.setActivityStatus(form2.getActivityStatus());

		TestCaseUtility.setNameObjectMap("Freezer_2_2_StorageType", storageType);
	}
	/**
	 * Test Storage Type edit with decreased capacity.
	 */
	
	public void testBoxStorageTypeEditDecreaseCapacity()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("Box_StorageType");
		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		addRequestParameter("pageOf", "pageOfStorageType" );

		// Perform serach
		actionPerform();
		verifyForward("pageOfStorageType");
		verifyNoActionErrors();
		setRequestPathInfo(getActualForward());
		addRequestParameter("operation", "edit");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("menuSelected", "6");
		actionPerform();
		verifyNoActionErrors();

		StorageTypeForm form = (StorageTypeForm)getActionForm();
		form.setType("Box_5_10_StorageType" + UniqueKeyGeneratorUtil.getUniqueKey());
		form.setOneDimensionCapacity(5);
		form.setTwoDimensionCapacity(10);
		form.setOperation("edit");
		form.setActivityStatus("Active") ;
		form.setSpecimenOrArrayType("Specimen");
		/*Edit Action*/

		setRequestPathInfo("/StorageTypeEdit");
		setActionForm(form);
		actionPerform();
		verifyForward("success");
		//verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.edit.successOnly"});

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		assertEquals(form2.getOneDimensionCapacity(), 5);
		assertEquals(form2.getTwoDimensionCapacity(), 10);
		storageType.getCapacity().setOneDimensionCapacity(5);
		storageType.getCapacity().setTwoDimensionCapacity(10);

		TestCaseUtility.setNameObjectMap("Box_5_10_StorageType", storageType);
	}


	/**
	 *10. Storage Type: Add Storage type and go to Storage Container page
						1. Verify Storagetype is inserteed.
						2. Forwarded to Storage container page.
	  */

	public void testStorageTypeAddAndGotoStorageContainerPage()
	{
		// Storage Type Added.

		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),1,1,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("");
		storageTypeForm.setForwardTo("storageContainer");
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("storageContainer");
		verifyNoActionErrors();

		// Add Storage COntainer
		StorageTypeForm form = (StorageTypeForm) getActionForm();
		setRequestPathInfo(getActualForward());

		actionPerform();
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});
		StorageContainerForm storageContainerForm = (StorageContainerForm)getActionForm();
		storageContainerForm.setTypeId(form.getId());
		logger.info("----StorageTypeTestCaseId : " + form.getId());
		storageContainerForm.setTypeName(form.getType());

		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");

		storageContainerForm.setSiteId(site.getId());
		storageContainerForm.setNoOfContainers(1);
		storageContainerForm.setOneDimensionCapacity(25);
		storageContainerForm.setTwoDimensionCapacity(25);
		storageContainerForm.setOneDimensionLabel("row");
		storageContainerForm.setTwoDimensionLabel("row");
		storageContainerForm.setDefaultTemperature("29");
		/*addRequestParameter("holdsSpecimenClassTypes", "Cell");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");*/

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";

		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		storageContainerForm.setIsFull("False");
		storageContainerForm.setOperation("add");
		//addRequestParameter("containerName","container_Janu_parent_" + UniqueKeyGeneratorUtil.getUniqueKey());
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		StorageContainerForm storgeContForm=(StorageContainerForm) getActionForm();
		StorageContainer storageContainer = new StorageContainer();

//		storageContainer.setSite(site);
	    Capacity capacity = new Capacity();
	    capacity.setOneDimensionCapacity(storgeContForm.getOneDimensionCapacity());
	    capacity.setTwoDimensionCapacity(storgeContForm.getTwoDimensionCapacity());
	    storageContainer.setCapacity(capacity);

	    storageContainer.setId(storgeContForm.getId());
	    logger.info("----StorageContainerId : " + storageContainer.getId());
	    Collection<String> holdsSpecimenClassCollection1 = new HashSet<String>();
	    String[] specimenClassTypes = storgeContForm.getHoldsSpecimenClassTypes();
	    holdsSpecimenClassCollection1.add(specimenClassTypes[0]);
	    storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection1);

	    final StorageType storageTypeAny = new StorageType();
		storageTypeAny.setId(new Long("1"));
		storageTypeAny.setName("All");
		Collection<StorageType> coll = new HashSet<StorageType>() ;
		coll.add(storageTypeAny);
		storageContainer.getHoldsStorageTypeCollection().clear();
		storageContainer.setHoldsStorageTypeCollection(coll);
	    TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);

//		fail("Need to write test case for adding Container after here");
		//TODO Check Container values is pre-populated and add container here

	}
	/**
	 * This test case is generating an error.
	 * Adding an invalid specimen class.
	 * Negative Test Case.
	 */
	
	public void testStorageTypeAddWithNoSpecimenClass()
	{
//		//TODO
//		fail("Need to write test case");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),1,1,"row","col","22","Active");
		//storageTypeForm.setSpecimenOrArrayType("Specimen");
		storageTypeForm.setHoldsSpecimenClassTypes(new String[] {"-2"}) ;
		storageTypeForm.setSpecimenOrArrayType("Specimen") ;
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("success");
		verifyNoActionErrors();
	}
	/**
	 * Test Storage Type add with empty one dimension.
	 * Negative Test Case.
	 */
	
	public void testStorageTypeAddWithEmptyOneDimension()
	{
//		//TODO
//		fail("Need to write test case");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),1,1,"row","col","22","Active");
		storageTypeForm.setOneDimensionLabel("") ;
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();

		verifyForward("failure");
		verifyActionErrors(new String[] {"errors.item.required"}) ;
	}

	/**
	 * Test Storage Type add with invalid holds storage type.
	 * Negative Test Case.
	 */
	
	public void testStorageTypeAddWithInvalidHoldsStorageType()
	{
//		//TODO
//		fail("Need to write test case");
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"srType_" + UniqueKeyGeneratorUtil.getUniqueKey(),1,1,"row","col","21","Active");
		storageTypeForm.setHoldsStorageTypeIds(new long[] {1,2}) ;
		storageTypeForm.setSpecimenOrArrayType("Specimen") ;
		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("failure");
		verifyActionErrors(new String[] {"errors.item.format"}) ;
	}
	/**
	 * Test Storage Type Add with with Specimen Array Type restriction.
	 */
	
	public void testStorageTypeAddSpecimenArrayType()
	{
		StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeForm(this,
				"SpecimenArrayTypeStorageType_" + UniqueKeyGeneratorUtil.getUniqueKey(),20,100,"row","col","22","Active");
		storageTypeForm.setSpecimenOrArrayType("Specimen");
//		storageTypeForm.setHoldsSpecimenClassTypes(new String[]{"Tissue"});
		SpecimenArrayType arrayType=(SpecimenArrayType)TestCaseUtility.getObjectMap().get("SpecimenTissueArrayType");
		long[] arrayIds={arrayType.getId()};
		storageTypeForm.setHoldsSpecimenArrTypeIds(arrayIds);

		setRequestPathInfo("/StorageTypeAdd");
		setActionForm(storageTypeForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		verifyActionMessages(new String[]{"object.add.successOnly"});

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(form.getType());
		storageType.setId(form.getId());
		storageType.setOneDimensionLabel(form.getOneDimensionLabel());
		storageType.setTwoDimensionLabel(form.getTwoDimensionLabel());
		storageType.setDefaultTempratureInCentigrade(Double.parseDouble(form
				.getDefaultTemperature()));

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
		storageType.setCapacity(capacity);

		TestCaseUtility.setNameObjectMap("SpecimenArrayStorageType", storageType);
	}
}
