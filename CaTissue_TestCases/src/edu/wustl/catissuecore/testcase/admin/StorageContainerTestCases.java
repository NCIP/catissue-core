package edu.wustl.catissuecore.testcase.admin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;


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

	@Test
	public void testUpdateContainerCapacity()
	{
		StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		StorageContainerForm storageContainerForm = null;
		//Retrieving Storage container object for edit
		logger.info("----StorageConatiner ID : " + storageContainer.getId());
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "search");
		addRequestParameter("id", storageContainer.getId().toString());
		setRequestPathInfo("/SearchObject") ;
		actionPerform();
		verifyForward("pageOfStorageContainer");
		verifyNoActionErrors();
		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		actionPerform();

		verifyNoActionErrors();
		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfStorageContainer");
		addRequestParameter("operation", "edit");
		actionPerform();

		verifyNoActionErrors();
		//Action form
		storageContainerForm=(StorageContainerForm) getActionForm();

		//modifying the container capacity
		storageContainerForm.setNoOfContainers(3);
		storageContainerForm.setOneDimensionCapacity(30);
		storageContainerForm.setTwoDimensionCapacity(30);
		storageContainerForm.setOneDimensionLabel("row");
		storageContainerForm.setTwoDimensionLabel("row");
		storageContainerForm.setDefaultTemperature("28");
		addRequestParameter("holdsSpecimenClassTypes", "Cell");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");

		storageContainerForm.setOperation("edit");
		setActionForm(storageContainerForm);
		setRequestPathInfo("/StorageContainerEdit");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.edit.successOnly"});
		logger.info("#############Storage Container Updated Successfully##########");
		try
		{
			storageContainer.setAllValues(storageContainerForm);
		}
		catch (AssignDataException e)
		{
			logger.debug(e.getMessage(),e);
			fail("failed to assign values");
		}
		TestCaseUtility.setNameObjectMap("storageContainer", storageContainer);

	}

	@Test
	public void testCallToGetSiteList() throws BizLogicException
	{
		final String[] siteDisplayField = {"name"};
		final String valueField = "id";
		final StorageContainerBizLogic bizLogic = new StorageContainerBizLogic();

		final String[] activityStatusArray = {Status.ACTIVITY_STATUS_DISABLED.toString(),
				Status.ACTIVITY_STATUS_CLOSED.toString()};
		final List list = bizLogic.getSiteList(siteDisplayField, valueField, activityStatusArray,
				1L);
		assertNotNull(list);
		logger.info(" ::: Site List Size ::: "+ list.size());

	}
}
