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
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

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
		addRequestParameter("siteName", site.getName());
		addRequestParameter("siteId", "" + site.getId());
		
		addRequestParameter("noOfContainers", "1");
		addRequestParameter("oneDimensionCapacity", "4");
		addRequestParameter("twoDimensionCapacity", "2");
		addRequestParameter("oneDimensionLabel", "row");
		addRequestParameter("twoDimensionLabel", "col");
		addRequestParameter("defaultTemperature", "29");
		addRequestParameter("holdsSpecimenClassTypes", "Cell");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");
		addRequestParameter("containerName","container_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("operation", "add");
		setRequestPathInfo("/StorageContainerAdd");
		actionPerform();
		verifyForward("success");
		
		StorageContainerForm form=(StorageContainerForm) getActionForm();
		StorageContainer storageContainer = new StorageContainer();
	
		storageContainer.setSite(site);
	    Capacity capacity = new Capacity(); 
	    capacity.setOneDimensionCapacity(form.getOneDimensionCapacity());
	    capacity.setTwoDimensionCapacity(form.getTwoDimensionCapacity());
	    storageContainer.setCapacity(capacity);
	    
	    storageContainer.setId(form.getId());
	    
	    Collection<String> holdsSpecimenClassCollection = new HashSet<String>();
	    String[] specimenClassTypes = form.getHoldsSpecimenClassTypes();
	    holdsSpecimenClassCollection.add(specimenClassTypes[0]);
	    storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
	    
	    TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);	    
	}
	
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
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfStorageContainer");
		addRequestParameter("operation","search");
		actionPerform();

		StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("StorageContainer");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
		}
		
		if(l.size() > 1)
		{
		    verifyForward("success");
		}
		else if(l.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + storageContainer.getId());
		}
		else
		{
			verifyForward("failure");
		}
		try
		{
			dao.closeSession();
		}
		catch (DAOException e) 
		{
		    e.printStackTrace();
		}
		
		/*common search action.Generates StorageContainerForm*/
		setRequestPathInfo("/StorageContainerSearch");
		addRequestParameter("id","" + storageContainer.getId());
		actionPerform();
		verifyForward("pageOfStorageContainer");


		/*edit operation*/
		storageContainer.setName("container1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",storageContainer.getName());
		setRequestPathInfo("/StorageContainerEdit");
		addRequestParameter("specimenOrArrayType", "SpecimenArray");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		
		TestCaseUtility.setNameObjectMap("StorageContainer",storageContainer);
	}
}
