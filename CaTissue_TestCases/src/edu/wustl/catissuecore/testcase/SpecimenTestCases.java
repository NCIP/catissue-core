package edu.wustl.catissuecore.testcase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
/**
 * This class contains test cases for Specimen add and checks for label and barcode after its storage position is changed
 * @author Himanshu Aseeja
 */
public class SpecimenTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Specimen Add.
	 */
	@Test
	public void testSpecimenAdd()
	{
		setRequestPathInfo("/NewSpecimenAdd");
		addRequestParameter("label", "label_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("barcode", "barcode_" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
		addRequestParameter("specimenCollectionGroupId", "" + specimenCollectionGroup.getId());
		addRequestParameter("specimenCollectionGroupName", specimenCollectionGroup.getName());
				
		addRequestParameter("parentPresent", "false");
		addRequestParameter("tissueSite", "Not Specified");
		addRequestParameter("tissueSide", "Not Specified");
		addRequestParameter("pathologicalStatus", "Not Specified");
		
		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		addRequestParameter("biohazardType", biohazard.getType());
		addRequestParameter("biohazardName", biohazard.getName());
		
		addRequestParameter("className", "Fluid");
		addRequestParameter("type", "Bile");
		addRequestParameter("quantity", "10");
		addRequestParameter("availableQuantity", "5");
		addRequestParameter("collectionStatus", "Pending");
		
		Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");
		
		addRequestParameter("collectionEventId", "" + event.getId());
		
		addRequestParameter("collectionEventSpecimenId", "0");
		addRequestParameter("collectionEventdateOfEvent", "01-28-2009");
		addRequestParameter("collectionEventTimeInHours", "11");
		addRequestParameter("collectionEventTimeInMinutes", "2");
		addRequestParameter("collectionEventUserId","1");
		addRequestParameter("collectionEventCollectionProcedure","Use CP Defaults");
		addRequestParameter("collectionEventContainer","Use CP Defaults");
		
		addRequestParameter("receivedEventId", "" + event.getId());;
		addRequestParameter("receivedEventDateOfEvent", "01-28-2009");
		addRequestParameter("receivedEventTimeInHours", "11");
		addRequestParameter("receivedEventTimeInMinutes", "2");
		addRequestParameter("receivedEventUserId","1");
		addRequestParameter("receivedEventCollectionProcedure","Use CP Defaults");
		addRequestParameter("receivedEventContainer","Use CP Defaults");
		addRequestParameter("receivedEventReceivedQuality", "Acceptable");
		addRequestParameter("receivedEventUserId","1");
				
		addRequestParameter("operation","add");
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();
		verifyForward("success");
		
		NewSpecimenForm form= (NewSpecimenForm) getActionForm();
		Specimen specimen = new Specimen();
		specimen.setId(form.getId());
		specimen.setActivityStatus(form.getActivityStatus());
		specimen.setAvailableQuantity(Double.parseDouble(form.getAvailableQuantity()));
		specimen.setLabel(form.getLabel());
		specimen.setBarcode(form.getBarcode());
    	specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
    	specimen.setCollectionStatus(form.getCollectionStatus());
    	specimen.setPathologicalStatus(form.getPathologicalStatus());
    	specimen.setInitialQuantity(Double.parseDouble(form.getQuantity()));
    	
    	SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
    	specimenCharacteristics.setTissueSide(form.getTissueSide());
    	specimenCharacteristics.setTissueSite(form.getTissueSite());
    	
    	specimen.setSpecimenCharacteristics(specimenCharacteristics);
    	
    	TestCaseUtility.setNameObjectMap("Specimen",specimen);
   	}
	
	/**
	 * Test Specimen Label and Barcode after storage position changes(when page refreshes).
	 * Bug Id : 11480
	 * @author Himanshu Aseeja
	 */
	@Test
	public void testLabelandBarcodeonStoragePositionChange()
	{
       	Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		setRequestPathInfo("/NewSpecimen");		
        addRequestParameter("operation", "edit");
        addRequestParameter("id", "" + specimen.getId());
        addRequestParameter("label", "1234");
        addRequestParameter("barcode", "1234");
        addRequestParameter("stContSelection", "2");
        
        SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
        addRequestParameter("specimenCollectionGroupId", "" + specimenCollectionGroup.getId());
        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
        actionPerform();
        verifyForward("pageOfNewSpecimenCPQuery");
        assertNotSame(specimen.getLabel(), "1234");
        assertNotSame(specimen.getBarcode(), "1234");
        
        NewSpecimenForm form= (NewSpecimenForm) getActionForm();
        assertEquals(form.getLabel(),"1234" );
        assertEquals(form.getBarcode(),"1234");
  	}
	
	
	//bug 11174
	@Test
	public void testSpecimenEdit()
	{
		//Simple Search Action
		setRequestPathInfo("/SimpleSearch");
	    addRequestParameter("aliasName", "StorageContainer");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Specimen");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Specimen.BARCODE.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","1");
		addRequestParameter("pageOf","pageOfNewSpecimen");
		addRequestParameter("operation","search");
		actionPerform();

		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try 
		{
			specimenList = bizLogic.retrieve("Specimen");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("SpecimenTestCases.testSpecimenEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(specimenList.size() > 1)
		{
		    verifyForward("success");
		    for(Specimen specimenFromList : specimenList)
		    {
		    	if(!specimenFromList.getCollectionStatus().equals("Collected") && specimenFromList.getId()!=null && specimenFromList.getLineage().equals("New"))
		    	{
		    		TestCaseUtility.setNameObjectMap("specimen",specimenFromList);
		    		setRequestPathInfo("/NewSpecimenSearch");
		    		addRequestParameter("id","" + specimenFromList.getId());
		    		actionPerform();
		    		verifyForward("pageOfNewSpecimen");
		    		verifyNoActionErrors();
		    		//edit operation
		    		
		    		specimenFromList.setLabel("spcimen1_" + UniqueKeyGeneratorUtil.getUniqueKey());
		    		addRequestParameter("label",specimenFromList.getLabel());
		    	    setRequestPathInfo("/NewSpecimenEdit");
		    	    addRequestParameter("stContSelection", "2");
		    	   // addRequestParameter("available", "true");
		    	    addRequestParameter("collectionStatus", "Collected"); 
		    	    StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		    	    addRequestParameter("storageContainer", container.getId().toString()); 
		    	    addRequestParameter("positionDimensionOne", "20"); 
		    	    addRequestParameter("positionDimensionTwo", "20"); 
		    		addRequestParameter("operation", "edit");
		    		actionPerform();
		    		verifyForward("success");
		    		verifyNoActionErrors();		
		    		NewSpecimenForm form= (NewSpecimenForm) getActionForm();
		    	    //assertEquals(true,form.isAvailable());
		    		TestCaseUtility.setNameObjectMap("specimen",specimenFromList);
		    		break;
		    	}
		    }
		    verifyNoActionErrors();
		}
		else if(specimenList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + specimen.getId());
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
	
	@Test
	//bug 11829
	public void testSpecimenAddToMyList()
	{
		//Simple Search Action
		setRequestPathInfo("/SimpleSearch");
	    addRequestParameter("aliasName", "Specimen");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Specimen");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Specimen.BARCODE.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","1");
		addRequestParameter("pageOf","pageOfNewSpecimen");
		addRequestParameter("operation","search");
		actionPerform();
		verifyForward("success");
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try 
		{
			specimenList = bizLogic.retrieve("Specimen");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("SpecimenTestCases.testSpecimenEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(specimenList.size() > 1)
		{
			verifyForward("success");
 	  	    verifyNoActionErrors();
		}
		else if(specimenList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + specimen.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}	
		
		setRequestPathInfo("/NewSpecimenSearch");
		addRequestParameter("id","" + specimen.getId());
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();
		
		setRequestPathInfo("/AddSpecimenToCart");
		addRequestParameter("pageOf", "cpChildSubmit");
		actionPerform();
		
		
		verifyForward("cpChildSubmit");
		verifyNoActionErrors();
	    setRequestPathInfo("/AnticipatorySpecimenView");
		HashMap forwardToHashMap = new HashMap();
   		forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId());
		forwardToHashMap.put("specimenId", specimen.getId());
		getRequest().setAttribute("forwardToHashMap",forwardToHashMap);
		addRequestParameter("target", "pageOfMultipleSpWithMenu");
		actionPerform();
	    verifyForward("pageOfMultipleSpWithMenu");
		verifyNoActionErrors();		    
	

	}
	@Test
	public void testSupervisorLogin()
	  {
		   	addRequestParameter("loginName","super@super.com");
	        addRequestParameter("password","Test123");
	        setRequestPathInfo("/Login.do");
	        actionPerform();
	        //verifyForward("/Home.do");
	        verifyForward("success");

	        SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
	        assertEquals("user name should be equal to loggedinusername","super@super.com",bean.getUserName());
	        CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
	        verifyNoActionErrors();
	  }
	//bug 11659
/*	@Test
	public void testSpecimenEditBySupervisor()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try 
		{
			specimenList = bizLogic.retrieve("Specimen");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("SpecimenTestCases.testSpecimenEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(specimenList.size() > 1)
		{
			verifyForward("success");
 	  	    verifyNoActionErrors();		
		}
		else if(specimenList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + specimen.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
		addRequestParameter("label",specimen.getLabel());
		setRequestPathInfo("/NewSpecimenEdit");
	    addRequestParameter("tissueSide", "Right");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		//verify action errors
		String errormsg[] = new String[] {"access.addedit.object.denied"};
		verifyActionErrors(errormsg);
		
	}	*/
	
}
