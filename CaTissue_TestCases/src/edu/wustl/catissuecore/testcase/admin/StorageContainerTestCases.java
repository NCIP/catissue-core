package edu.wustl.catissuecore.testcase.admin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;


/**
 * This class contains test cases for Storage Container add/edit
 * @author Himanshu Aseeja
 */
public class StorageContainerTestCases extends CaTissueSuiteBaseTest
{

	/**
	 * Test Storage Container Add.
	 */
	@Test
	public void testSetUtility()
	{
//		StorageType storageType = new StorageType();
//		storageType.setName("TissueStorageType_1257421218783");
//		storageType.setId(6L);
//		TestCaseUtility.setNameObjectMap("StorageType", storageType);
//		
//		Site s = new Site();
//		s.setId(1L);
//		TestCaseUtility.setNameObjectMap("Site", s);
	}
	/**
	 * Test Storage Container Add.
	 */
	@Test
	public void testStorageContainerAdd()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		StorageContainerForm storageContainerForm = new StorageContainerForm();
		storageContainerForm.setTypeId(storageType.getId());
		logger.info("----StorageTypeId : " + storageType.getId());
		storageContainerForm.setTypeName(storageType.getName());
		
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
		
		StorageContainerForm form=(StorageContainerForm) getActionForm();
		StorageContainer storageContainer = new StorageContainer();
	
//		storageContainer.setSite(site);
	    Capacity capacity = new Capacity(); 
	    capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
	    capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
	    storageContainer.setCapacity(capacity);
	    
	    storageContainer.setId(form.getId());
	    logger.info("----StorageContainerId : " + storageContainer.getId());
	    Collection<String> holdsSpecimenClassCollection1 = new HashSet<String>();
	    String[] specimenClassTypes = form.getHoldsSpecimenClassTypes();
	    holdsSpecimenClassCollection1.add(specimenClassTypes[0]);
	    storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection1);
	    
	    TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);	    
	}
	//bug 11546
	/**
	 * Test Storage Container Add Freezer Container.
	 */
	@Test
	public void testAddFreezerContainer()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("Freezer_StorageType");
			
		StorageContainerForm storageContainerForm = new StorageContainerForm();
		storageContainerForm.setTypeId(storageType.getId());
		storageContainerForm.setTypeName(storageType.getName());
		
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		storageContainerForm.setSiteId(site.getId());
		
		storageContainerForm.setNoOfContainers(1);
		storageContainerForm.setOneDimensionCapacity(5);
		storageContainerForm.setTwoDimensionCapacity(5);
		storageContainerForm.setOneDimensionLabel("row");
		storageContainerForm.setTwoDimensionLabel("row");
		storageContainerForm.setDefaultTemperature("29");

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";
		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		storageContainerForm.setOperation("add");	
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();	
	}
	/**
	 * Test Storage Container Add invalid parent container.
	 * Negative Test Case.
	 */
	@Test
	public void testAddChildContainerWithInvalidParentContainer()
	{
		StorageContainer parentStorageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		
		StorageContainerForm storageContainerForm = new StorageContainerForm();
		storageContainerForm.setTypeId(storageType.getId());
		storageContainerForm.setTypeName(storageType.getName());
		storageContainerForm.setNoOfContainers(1);
		storageContainerForm.setOneDimensionCapacity(5);
		storageContainerForm.setTwoDimensionCapacity(5);
		storageContainerForm.setOneDimensionLabel("row");
		storageContainerForm.setTwoDimensionLabel("row");
		storageContainerForm.setDefaultTemperature("29");

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";
		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		
		storageContainerForm.setParentContainerSelected("Container (Manual)");
		storageContainerForm.setContainerId(parentStorageContainer.getId().toString());
		storageContainerForm.setPos1("1");
		storageContainerForm.setPos2("1");
		storageContainerForm.setOperation("add");	
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("failure");
		verifyActionErrors(new String[]{"errors.item"});
	}
	@Test
	public void testStorageContainerBizLogicAddWithNullObject()
	{
//		//TODO
//		fail("Need to write test case");
		
		StorageContainerBizLogic bizLogic = new StorageContainerBizLogic() ;
		
		try
		{
			bizLogic.insert(null,CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN) ;
			assertFalse("StorageType Object is NULL while inserting " +
					"through BizLogic",true);
		} 
		catch (BizLogicException e)
		{
			logger.info("Exception in StorageContainer :" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Test Storage Container Edit.
	 */
	/*@Test
	public void testStorageContainerEdit()
	{
		Simple Search Action
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "StorageContainer");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "StorageContainer");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Container.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","s");
		addRequestParameter("pageOf","pageOfStorageContainer");
		addRequestParameter("operation","search");
		actionPerform();

		StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<StorageContainer> storageContainerList = null;
		try 
		{
			storageContainerList = bizLogic.retrieve("StorageContainer");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("StorageContainerTestCases.testStorageContainerEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(storageContainerList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(storageContainerList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + storageContainer.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
				
		common search action.Generates StorageContainerForm
		setRequestPathInfo("/StorageContainerSearch");
		addRequestParameter("id","" + storageContainer.getId());
		actionPerform();
		verifyForward("pageOfStorageContainer");
		verifyNoActionErrors();

		edit operation
		storageContainer.setName("container1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",storageContainer.getName());
		
		addRequestParameter("activityStatus","Closed");
		addRequestParameter("isFull","True");
		
		setRequestPathInfo("/StorageContainerEdit");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		StorageContainerForm form = (StorageContainerForm) getActionForm();
		assertEquals(form.getActivityStatus(), "Closed");
		assertEquals(form.getIsFull(), "True");
		
		TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);
	}*/
}
