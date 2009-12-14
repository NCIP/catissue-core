package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;
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
		NewSpecimenForm newSpecForm = new NewSpecimenForm() ;
		setRequestPathInfo("/NewSpecimenAdd");
		newSpecForm.setLabel("label_" + UniqueKeyGeneratorUtil.getUniqueKey());
		newSpecForm.setBarcode("barcode_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
		newSpecForm.setSpecimenCollectionGroupId(""+specimenCollectionGroup.getId()) ;
		newSpecForm.setSpecimenCollectionGroupName(specimenCollectionGroup.getName()) ;

		newSpecForm.setParentPresent(false);
		newSpecForm.setTissueSide("Not Specified") ;
		newSpecForm.setTissueSite("Not Specified");
		newSpecForm.setPathologicalStatus("Not Specified");

		Biohazard biohazard = (Biohazard) TestCaseUtility.getNameObjectMap("Biohazard");
		newSpecForm.setBiohazardName(biohazard.getName());
		newSpecForm.setBiohazardType(biohazard.getType());

		newSpecForm.setClassName("Fluid");
		newSpecForm.setType("Bile");
		newSpecForm.setQuantity("10") ;
		newSpecForm.setAvailable(true);
		newSpecForm.setAvailableQuantity("5");
		newSpecForm.setCollectionStatus("Pending") ;

		Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");

		newSpecForm.setCollectionEventId(event.getId()) ;

		newSpecForm.setCollectionEventSpecimenId(0L);
		newSpecForm.setCollectionEventdateOfEvent("01-28-2009");
		newSpecForm.setCollectionEventTimeInHours("11") ;
		newSpecForm.setCollectionEventTimeInMinutes("2") ;
		newSpecForm.setCollectionEventUserId(1L) ;
		newSpecForm.setCollectionEventCollectionProcedure("Use CP Defaults");
		newSpecForm.setCollectionEventContainer("Use CP Defaults") ;

		newSpecForm.setReceivedEventId(event.getId());
		newSpecForm.setReceivedEventDateOfEvent("01-28-2009");
		newSpecForm.setReceivedEventTimeInHours("11") ;
		newSpecForm.setReceivedEventTimeInMinutes("2") ;
		newSpecForm.setReceivedEventUserId(1L) ;
		newSpecForm.setReceivedEventReceivedQuality("Acceptable");

		newSpecForm.setOperation("add");
		newSpecForm.setPageOf("pageOfNewSpecimen");
		setActionForm(newSpecForm);
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
	/**
	 * Test Specimen Edit.
	 */
	//bug 11174
	@Test
	public void testSpecimenEdit()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		NewSpecimenForm specimenForm = null;
		//Retrieving Storage container object for edit
		logger.info("----StorageConatiner ID : " + specimen.getId());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "search");
		addRequestParameter("id", specimen.getId().toString());
		setRequestPathInfo("/SearchObject") ;
		actionPerform();
		verifyForward("pageOfNewSpecimen");
		verifyNoActionErrors();

		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();
		verifyNoActionErrors();

		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "15");
		addRequestParameter("showConsents", "yes");
		addRequestParameter("tableId4", "disable");
		actionPerform();
		verifyNoActionErrors();

		specimenForm=(NewSpecimenForm) getActionForm();
		String newLabel="label_" + UniqueKeyGeneratorUtil.getUniqueKey();
		String newBarcode="barcode_" + UniqueKeyGeneratorUtil.getUniqueKey();
		specimenForm.setBarcode(newBarcode);
		specimenForm.setLabel(newLabel);
		specimenForm.setAvailableQuantity("20");
		specimenForm.setQuantity("20");
		specimenForm.setCollectionStatus("Collected") ;

		specimenForm.setOperation("edit");
		setActionForm(specimenForm);
		setRequestPathInfo("/NewSpecimenEdit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();
		verifyNoActionErrors();

		System.out.println(getActualForward());
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfNewSpecimen");
		actionPerform();
		verifyNoActionErrors();

		verifyActionMessages(new String[]{"object.edit.successOnly"});
		logger.info("#############Specimen Updated Successfully##########");

		try
		{
			specimen.setAllValues(specimenForm);
		}
		catch (AssignDataException e)
		{
			logger.debug(e.getMessage(),e);
			fail("failed to assign values");
		}

		specimenForm=(NewSpecimenForm)getActionForm();

		assertNotNull("New Specimen Form cannot be null",specimenForm);
		assertEquals("Specimen label not updated",newLabel, specimenForm.getLabel());
		assertEquals("Specimen Barcode not updated",newBarcode, specimenForm.getBarcode());
		assertEquals("Collection status not updated","Collected", specimenForm.getCollectionStatus());
		TestCaseUtility.setNameObjectMap("specimen", specimen);



	}
	/**
	 * Test disabled Participant
	 */
	@Test
	public void testParticicpantAndDisableCollectedSpecimens()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		if(specimen.getCollectionStatus().equals( "Collected" ))
		{
			SpecimenCollectionGroup scg = specimen.getSpecimenCollectionGroup();
			CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();
			Participant participant = cpr.getParticipant();
			ParticipantForm partForm = new ParticipantForm() ;
			partForm.setFirstName(participant.getFirstName()) ;
			partForm.setLastName(participant.getLastName()) ;
			partForm.setGender( participant.getGender() );
			partForm.setId( participant.getId() );
			partForm.setRaceTypes( new String[] {"Asian"} );
			participant.setCollectionProtocolRegistrationCollection(participant.getCollectionProtocolRegistrationCollection());
			partForm.setActivityStatus( "Disabled" );
			partForm.setOperation("edit") ;
			setRequestPathInfo("/ParticipantEdit");
			setActionForm(partForm);
			actionPerform();
			verifyForward("failure");
			String errormsg[] = new String[]{"errors.item"};
			verifyActionErrors(errormsg);
		}
	}
	/**
	 * Test Specimen Add to my List.
	 */
//	@Test
//	//bug 11829
//	public void testSpecimenAddToMyList()
//	{
//		//Simple Search Action
//		setRequestPathInfo("/SimpleSearch");
//
//		SimpleQueryInterfaceForm simpleForm = new SimpleQueryInterfaceForm();
//		simpleForm.setAliasName("Specimen") ;
//		simpleForm.setPageOf("pageOfNewSpecimen");
//		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Specimen");
//		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Specimen.BARCODE.varchar");
//		simpleForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
//		simpleForm.setValue("SimpleConditionsNode:1_Condition_value", "1");
//
//		setActionForm(simpleForm) ;
//		actionPerform();
//		verifyForward("success");
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		DefaultBizLogic bizLogic = new DefaultBizLogic();
//		List<Specimen> specimenList = null;
//		try
//		{
//			specimenList = bizLogic.retrieve("Specimen");
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			System.out.println("SpecimenTestCases.testSpecimenEdit(): "+e.getMessage());
//			fail(e.getMessage());
//		}
//
//		if(specimenList.size() > 1)
//		{
//			verifyForward("success");
// 	  	    verifyNoActionErrors();
//		}
//		else if(specimenList.size() == 1)
//		{
//			verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id=" + specimen.getId());
//			verifyNoActionErrors();
//		}
//		else
//		{
//			verifyForward("failure");
//			//verify action errors
//			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
//			verifyActionErrors(errorNames);
//		}
//
//		setRequestPathInfo("/NewSpecimenSearch");
//		addRequestParameter("id","" + specimen.getId());
//		actionPerform();
//		verifyForward("pageOfNewSpecimen");
//		verifyNoActionErrors();
//
//		setRequestPathInfo("/AddSpecimenToCart");
//		addRequestParameter("pageOf", "cpChildSubmit");
//		actionPerform();
//
//
//		verifyForward("cpChildSubmit");
//		verifyNoActionErrors();
//	    setRequestPathInfo("/AnticipatorySpecimenView");
//		HashMap forwardToHashMap = new HashMap();
//   		forwardToHashMap.put("specimenCollectionGroupId", specimen.getSpecimenCollectionGroup().getId());
//		forwardToHashMap.put("specimenId", specimen.getId());
//		getRequest().setAttribute("forwardToHashMap",forwardToHashMap);
//		addRequestParameter("target", "pageOfMultipleSpWithMenu");
//		actionPerform();
//	    verifyForward("pageOfMultipleSpWithMenu");
//		verifyNoActionErrors();
//
//
//	}
//	@Test
//	public void testSupervisorLogin()
//	  {
//		   	addRequestParameter("loginName","super@super.com");
//	        addRequestParameter("password","Test123");
//	        setRequestPathInfo("/Login.do");
//	        actionPerform();
//	        //verifyForward("/Home.do");
//	        verifyForward("success");
//
//	        SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
//	        assertEquals("user name should be equal to loggedinusername","super@super.com",bean.getUserName());
//	        CaTissueSuiteTestUtil.USER_SESSION_DATA_BEAN=bean;
//	        verifyNoActionErrors();
//	  }
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


	 public void testCreateDerivativeOfSpecimen()
	 {

			Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
			NewSpecimenForm specimenForm = null;
			//Retrieving Storage container object for edit
			logger.info("----StorageConatiner ID : " + specimen.getId());
			addRequestParameter("pageOf", "pageOfNewSpecimen");
			addRequestParameter("operation", "search");
			addRequestParameter("id", specimen.getId().toString());
			setRequestPathInfo("/SearchObject") ;
			actionPerform();
			verifyForward("pageOfNewSpecimen");
			verifyNoActionErrors();

			System.out.println(getActualForward());
			setRequestPathInfo(getActualForward());
			addRequestParameter("pageOf", "pageOfNewSpecimen");
			actionPerform();
			verifyNoActionErrors();

			System.out.println(getActualForward());
			setRequestPathInfo(getActualForward());
			addRequestParameter("pageOf", "pageOfNewSpecimen");
			addRequestParameter("operation", "edit");
			addRequestParameter("menuSelected", "15");
			addRequestParameter("showConsents", "yes");
			addRequestParameter("tableId4", "disable");
			actionPerform();
			verifyNoActionErrors();
			//Setting the derivative to true
			specimenForm=(NewSpecimenForm) getActionForm();
			specimenForm.setDerivedClicked(true);
			specimenForm.setNumberOfSpecimens("1");
			specimenForm.setForwardTo("createNew");
			//specimenForm.setOperation("edit");
			setActionForm(specimenForm);
			setRequestPathInfo("/NewSpecimenEdit");
			actionPerform();
			verifyForward("createNew");
			verifyNoActionErrors();

			System.out.println(getActualForward());
			setRequestPathInfo(getActualForward());

			addRequestParameter("pageOf", "" );
			addRequestParameter("operation", "add" );
			addRequestParameter("menuSelected", "15" );
			addRequestParameter("virtualLocated", "true" );

			actionPerform();

			verifyNoActionErrors();

////			specimenForm=(NewSpecimenForm) getActionForm();
//			CreateSpecimenForm form= (CreateSpecimenForm)getActionForm();
//			setActionForm(form);
//			setRequestPathInfo("/AddSpecimen");
//			addRequestParameter("isQuickEvent", "true" );
//
//			actionPerform();
//			verifyForward("success");
//			verifyNoActionErrors();



	 }

}
