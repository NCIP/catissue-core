
package edu.wustl.catissuecore.testcase;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This class contains test cases for Storage Type add/edit
 * @author Himanshu Aseeja
 */
public class StorageTypeTestCases extends CaTissueSuiteBaseTest
{

	/**
	 * Test Storage Type Add.
	 */
	@Test
	public void testStorageTypeAdd()
	{
		addRequestParameter("oneDimensionCapacity", "3");
		addRequestParameter("twoDimensionCapacity", "2");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "29");
		addRequestParameter("type", "srType_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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

	//RB S
	/**
	* 1. Storage Type: Add Storage type with same name (1x1 capacity) 
	*/
	@Test
	public void testStorageTypeAddWithSameName()
	{
		StorageType storageType1 = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		String name = storageType1.getName();

		addRequestParameter("type", name);
		addRequestParameter("oneDimensionCapacity", "1");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();

		verifyForward("failure");

		String errormsg[] = new String[]{"errors.item"};
		verifyActionErrors(errormsg);

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		StorageType storageType = new StorageType();

		storageType.setName(name);
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
	 * 2. Storage Type: Add Storage type with only Tissue Specimen restriction
	 */
	@Test
	public void testStorageTypeAddTissueSpecimenClass()
	{

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("holdsSpecimenClassTypes", "Tissue");

		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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
	 * 3. Storage Type: Add Storage type with only Molecular  Specimen restriction
	 */
	@Test
	public void testStorageTypeAddMolecularSpecimenClass()
	{

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("holdsSpecimenClassTypes", "Molecular");

		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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
	 * 4. Storage Type: Add Storage type with only Fluid Specimen restriction
	 */
	@Test
	public void testStorageTypeAddFluidSpecimenClass()
	{

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("holdsSpecimenClassTypes", "Fluid");

		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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
	 * 5. Storage Type: Add Storage type with only Cell Specimen restriction
	 */
	@Test
	public void testStorageTypeAddCellSpecimenClass()
	{

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("holdsSpecimenClassTypes", "Cell");

		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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
	 * 6. Storage Type: Add Storage type with only All Specimen restriction
	 */
	@Test
	public void testStorageTypeAddAllSpecimenClass()
	{

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "1");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "22");

		addRequestParameter("holdsSpecimenClassTypes", "-1");

		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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
	 * 7. Storage Type: Create Storage type hierarchy as Freezer(2X2) holds box (10x10)
	 */
	@Test
	public void testStorageTypeAddFreezer()
	{
		// Adding Box of 10*10 

		addRequestParameter("oneDimensionCapacity", "10");
		addRequestParameter("twoDimensionCapacity", "10");
		addRequestParameter("oneDimensionLabel", "Dim1");
		addRequestParameter("twoDimensionLabel", "Dim2");
		addRequestParameter("defaultTemperature", "29");
		addRequestParameter("type", "Box1010_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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

		// Adding Box of 2*2 Freezer which holds Box of 10*10.
		StorageType storageType1 = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		String storageTypeHold = storageType1.getName();

		addRequestParameter("oneDimensionCapacity", "2");
		addRequestParameter("twoDimensionCapacity", "2");
		addRequestParameter("oneDimensionLabel", "Dim11");
		addRequestParameter("twoDimensionLabel", "Dim22");
		addRequestParameter("defaultTemperature", "22");
		addRequestParameter("type", "Freezer22_" + UniqueKeyGeneratorUtil.getUniqueKey());

		addRequestParameter("holdsStorageTypeIds", "" + form.getId());

		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		StorageType storageType2 = new StorageType();

		storageType2.setName(form2.getType());
		storageType2.setId(form2.getId());
		storageType2.setOneDimensionLabel(form2.getOneDimensionLabel());
		storageType2.setTwoDimensionLabel(form2.getTwoDimensionLabel());
		storageType2.setDefaultTempratureInCentigrade(Double.parseDouble(form2
				.getDefaultTemperature()));

		Capacity capacity2 = new Capacity();
		capacity2.setOneDimensionCapacity(form2.getOneDimensionCapacity());
		capacity2.setTwoDimensionCapacity(form2.getTwoDimensionCapacity());
		storageType2.setCapacity(capacity2);

		TestCaseUtility.setNameObjectMap("StorageType", storageType2);

	}

	// update capacity and Verify capacity

	@Test
	public void testStorageTypeEditFreezer()
	{
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "search");
		actionPerform();

		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		if (storageTypeList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (storageTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageType&operation=search&id="
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

		//common search action.Generates StorageTypeForm
		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		actionPerform();
		verifyForward("pageOfStorageType");

		//edit action

		addRequestParameter("oneDimensionCapacity", "15");
		addRequestParameter("twoDimensionCapacity", "15");
		setRequestPathInfo("/StorageTypeEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");

		TestCaseUtility.setNameObjectMap("StorageType", storageType);

		StorageTypeForm form2 = (StorageTypeForm) getActionForm();
		assertEquals(form2.getOneDimensionCapacity(), 15);
		assertEquals(form2.getOneDimensionCapacity(), 15);
	}

	/**
	 *8. Storage Type: Edit Storage type
	                   1. increase capacity 
						2. Verify capacity
	*/

	public void testStorageTypeEditIncreaseCapicityVarifyCapicity()
	{
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "search");
		actionPerform();

		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		if (storageTypeList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (storageTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageType&operation=search&id="
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

		//common search action.Generates StorageTypeForm
		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		actionPerform();
		verifyForward("pageOfStorageType");

		//edit action
		storageType.setName("stype1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name", storageType.getName());
		addRequestParameter("oneDimensionCapacity", "10");
		addRequestParameter("twoDimensionCapacity", "10");
		setRequestPathInfo("/StorageTypeEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		assertEquals(form.getOneDimensionCapacity(), 10);

		TestCaseUtility.setNameObjectMap("StorageType", storageType);
	}

	/**
	*9. Storage Type: Edit Storage type
	                   1. decrease capacity
						2. Verify capacity
	 */

	public void testStorageTypeEditDecreaseCapicityVarifyCapicity()
	{

		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "search");
		actionPerform();

		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		if (storageTypeList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (storageTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageType&operation=search&id="
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

		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		actionPerform();
		verifyForward("pageOfStorageType");

		//Edit Action

		storageType.setName("stype1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name", storageType.getName());
		addRequestParameter("oneDimensionCapacity", "1");
		addRequestParameter("twoDimensionCapacity", "1");
		setRequestPathInfo("/StorageTypeEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");

		StorageTypeForm form = (StorageTypeForm) getActionForm();
		assertEquals(form.getOneDimensionCapacity(), 1);

		TestCaseUtility.setNameObjectMap("StorageType", storageType);

	}

	/**
	 *10. Storage Type: Add Storage type and go to Storage Container page
						1. Verify Storagetype is inserteed.
						2. Forwarded to Storage container page.
	  */

	public void testStorageTypeAddAndGotoStorageContainerPage()
	{
		// Storage Type Added.
		addRequestParameter("oneDimensionCapacity", "11");
		addRequestParameter("twoDimensionCapacity", "11");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "29");
		addRequestParameter("type", "type_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

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

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		Iterator<StorageType> itr = storageTypeList.iterator();
		StorageType storageType1 = new StorageType();
		StorageType storageType2 = new StorageType();
		StorageTypeForm form1 = (StorageTypeForm) getActionForm();
		while (itr.hasNext())
		{
			storageType1 = itr.next();
			if (storageType1.getId() == form1.getId())
			{
				storageType2 = storageType1;
			}

		}

		setRequestPathInfo("/StorageContainer");
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "add");
		addRequestParameter("typeId", "" + storageType2.getId());
		addRequestParameter("typeName", storageType2.getName());
		addRequestParameter("OneDimensionLabel", storageType2.getOneDimensionLabel());
		addRequestParameter("twoDimensionLabel", storageType2.getTwoDimensionLabel());
		addRequestParameter("oneDimensionCapacity", storageType2.getCapacity()
				.getOneDimensionCapacity().toString());
		addRequestParameter("twoDimensionCapacity", storageType2.getCapacity()
				.getTwoDimensionCapacity().toString());
		addRequestParameter("holdsSpecimenClassCollection", storageType2
				.getHoldsSpecimenClassCollection().toString());
		addRequestParameter("holdsStorageTypeCollection", storageType2
				.getHoldsStorageTypeCollection().toString());
		addRequestParameter("holdsSpecimenArrayTypeCollection", storageType2
				.getHoldsSpecimenArrayTypeCollection().toString());
		addRequestParameter("isPageFromStorageType", "yes");
		actionPerform();
		verifyForward("pageOfStorageContainer");

		StorageContainerForm form11 = (StorageContainerForm) getActionForm();
		assertEquals(form11.getTypeName(), storageType2.getName());

	}

	//RB E

	/**
	 * Test Storage Type edit.
	 */
	@Test
	public void testStorageTypeEdit()
	{

		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"StorageType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		addRequestParameter("pageOf", "pageOfStorageType");
		addRequestParameter("operation", "search");
		actionPerform();

		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageType> storageTypeList = null;
		try
		{
			storageTypeList = bizLogic.retrieve("StorageType");
		}
		catch (DAOException e)
		{
			e.printStackTrace();
			System.out.println("StorageTypeTestCases.testStorageTypeEdit(): " + e.getMessage());
			fail(e.getMessage());
		}

		if (storageTypeList.size() > 1)
		{
			verifyForward("success");
			verifyNoActionErrors();
		}
		else if (storageTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageType&operation=search&id="
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

		/*common search action.Generates StorageTypeForm*/
		setRequestPathInfo("/StorageTypeSearch");
		addRequestParameter("id", "" + storageType.getId());
		actionPerform();
		verifyForward("pageOfStorageType");

		/*edit action*/
		storageType.setName("stype1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name", storageType.getName());
		setRequestPathInfo("/StorageTypeEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");

		TestCaseUtility.setNameObjectMap("StorageType", storageType);
	}
}
