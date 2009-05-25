package edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
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
	public void testStorageContainerAdd()
	{
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		addRequestParameter("typeName", storageType.getName());
		addRequestParameter("typeId", "" + storageType.getId());
		
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		addRequestParameter("siteName", ""+site.getName());
		addRequestParameter("siteId", ""+site.getId() );
		
		addRequestParameter("noOfContainers", "1");
		addRequestParameter("oneDimensionCapacity", "5");
		addRequestParameter("twoDimensionCapacity", "5");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "29");
		/*addRequestParameter("holdsSpecimenClassTypes", "Cell");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");*/
		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";
		addRequestParameter("specimenOrArrayType", "Specimen");
		addRequestParameter("holdsSpecimenClassTypes", holdsSpecimenClassCollection);
		
		addRequestParameter("activityStatus","Active");
		addRequestParameter("isFull","False");
		
		addRequestParameter("containerName","container_Janu_parent_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageContainerAdd");
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
	    
	    Collection<String> holdsSpecimenClassCollection1 = new HashSet<String>();
	    String[] specimenClassTypes = form.getHoldsSpecimenClassTypes();
	    holdsSpecimenClassCollection1.add(specimenClassTypes[0]);
	    storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection1);
	    
	    
	    TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);	    
	}
	//bug 11546
	/*@Test
	public void testAddChildContainer()
	{
		StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		addRequestParameter("typeName", storageType.getName());
		addRequestParameter("typeId", "" + storageType.getId());
		
		Site site = (Site) TestCaseUtility.getNameObjectMap("Site");
		addRequestParameter("siteName", ""+site.getName());
		addRequestParameter("siteId", ""+site.getId() );
		
		addRequestParameter("noOfContainers", "1");
		addRequestParameter("oneDimensionCapacity", "5");
		addRequestParameter("twoDimensionCapacity", "5");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "29");
		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		holdsSpecimenClassCollection[1]="Tissue";
		holdsSpecimenClassCollection[2]="Molecular";
		holdsSpecimenClassCollection[3]="Cell";
		addRequestParameter("specimenOrArrayType", "Specimen");
		addRequestParameter("holdsSpecimenClassTypes", holdsSpecimenClassCollection);
		addRequestParameter("parentContainerSelected","Container (Manual)");
		addRequestParameter("containerId",storageContainer.getId().toString());
		addRequestParameter("pos1", "2");
		addRequestParameter("pos2", "2");		
		addRequestParameter("containerName","container_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");		
		setRequestPathInfo("/StorageContainerAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();	
	
	}*/
	
	/**
	 * Test Storage Container Edit.
	 */
	@Test
	public void testStorageContainerEdit()
	{
		/*Simple Search Action*/
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
				
		/*common search action.Generates StorageContainerForm*/
		setRequestPathInfo("/StorageContainerSearch");
		addRequestParameter("id","" + storageContainer.getId());
		actionPerform();
		verifyForward("pageOfStorageContainer");
		verifyNoActionErrors();

		/*edit operation*/
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
	}
}
