package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Map;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.AssignDataException;
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
		specimen.setSpecimenClass( form.getClassName() );
		specimen.setSpecimenType( form.getType() );
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
	 * Test aliquot add
	 * AliquotAction + CreateAliquotAction
	 */

	public void testAliquotAdd()
	{
		setRequestPathInfo("/Aliquots");
		actionPerform();
		//verifyNoActionErrors();
		AliquotForm aliquotForm = new AliquotForm();
		Specimen parent = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		aliquotForm.setSpecimenLabel( parent.getLabel() );
		aliquotForm.setClassName( parent.getSpecimenClass() );
		aliquotForm.setType( parent.getSpecimenType() );
		aliquotForm.setNoOfAliquots( "1" );
		aliquotForm.setQuantityPerAliquot( "1" );
		aliquotForm.setSpecimenID( parent.getId().toString() );
		aliquotForm.setNextForwardTo( "" );
		aliquotForm.setSpCollectionGroupId( parent.getSpecimenCollectionGroup().getId() );
		setActionForm(aliquotForm);
		setRequestPathInfo("/CreateAliquots");
		actionPerform();
		setRequestPathInfo("/AliquotAdd");
		AliquotForm form = (AliquotForm) getActionForm();
		form.setAvailableQuantity( "1" );
		StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		Map aliquotMap = form.getAliquotMap();
		aliquotMap.put( "radio_1", "2" );
		aliquotMap.put( "Specimen:1_quantity", "1" );
		aliquotMap.put( "Specimen:1_StorageContainer_id", ""+storageContainer.getId() );
		aliquotMap.put( "Specimen:1_positionDimensionOne", "2" );
		aliquotMap.put( "Specimen:1_positionDimensionTwo", "1" );
		setActionForm(form);
		actionPerform();
		//verifyNoActionErrors();
	}

	/**
	 * Test Specimen Label and Barcode after storage position changes(when page refreshes).
	 * Bug Id : 11480
	 * @author Himanshu Aseeja
	 */

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

	public void testSpecimenEdit()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		NewSpecimenForm specimenForm = null;
		//Retrieving specimen object for edit
		logger.info("----specimen ID : " + specimen.getId());
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
//
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
//
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
/*
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

//			specimenForm=(NewSpecimenForm) getActionForm();
			CreateSpecimenForm form= (CreateSpecimenForm)getActionForm();
			setActionForm(form);
			setRequestPathInfo("/AddSpecimen");
			addRequestParameter("isQuickEvent", "true" );

			actionPerform();
//			verifyForward("success");
			verifyNoActionErrors();
	 }

	 /**
		 * Test Specimen Add.
		 */

		public void testTissueSpecimenAdd()
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

			newSpecForm.setClassName("Tissue");
			newSpecForm.setType("Fixed Tissue");
			newSpecForm.setQuantity("10") ;
			newSpecForm.setAvailable(true);
			newSpecForm.setAvailableQuantity("5");
			newSpecForm.setCollectionStatus("Collected") ;



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

			newSpecForm=(NewSpecimenForm)getActionForm();
			//Retrieving specimen object for edit
			logger.info("----specimen ID : " + newSpecForm.getId());
			addRequestParameter("pageOf", "pageOfNewSpecimen");
			addRequestParameter("operation", "search");
			addRequestParameter("id", ""+newSpecForm.getId());
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

			newSpecForm=(NewSpecimenForm) getActionForm();
			String newLabel="label_" + UniqueKeyGeneratorUtil.getUniqueKey();
			String newBarcode="barcode_" + UniqueKeyGeneratorUtil.getUniqueKey();
			newSpecForm.setBarcode(newBarcode);
			newSpecForm.setLabel(newLabel);
			newSpecForm.setAvailableQuantity("20");
			newSpecForm.setQuantity("20");
			newSpecForm.setCollectionStatus("Collected") ;

			newSpecForm.setOperation("edit");
			setActionForm(newSpecForm);
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

			NewSpecimenForm form= (NewSpecimenForm) getActionForm();
			Specimen specimen = new Specimen();
			specimen.setId(form.getId());
			specimen.setSpecimenClass( form.getClassName() );
			specimen.setSpecimenType( form.getType() );
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

	    	TestCaseUtility.setNameObjectMap("TissueSpecimen",specimen);
	   	}


		/**
		 * Test Specimen Add in Storage Container with restriction.
		 */

		public void testSpecimenAddInRestrictedCont()
		{
			//Adding Storage Type

			StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeFormWithTypeRestriction(this,
					"Rest_Type_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","10","Active");
			String[] holdsSpecimenClassTypes = new String[1];
			holdsSpecimenClassTypes[0] = "Fluid";
			storageTypeForm.setHoldsSpecimenClassTypes(holdsSpecimenClassTypes);
			String[] fluid = {"Bile"};
			storageTypeForm.setHoldsTissueSpType(null);
			storageTypeForm.setHoldsCellSpType(null);
			storageTypeForm.setHoldsFluidSpType(fluid);
			storageTypeForm.setHoldsMolSpType(null);
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
			//Adding Storage Container
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

			String[] holdsSpecimenClassCollection = new String[1];
			holdsSpecimenClassCollection[0]="Fluid";
			storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
			storageContainerForm.setHoldsTissueSpType(null);
			storageContainerForm.setHoldsCellSpType(null);
			storageContainerForm.setHoldsFluidSpType(fluid);
			storageContainerForm.setHoldsMolSpType(null);

			storageContainerForm.setSpecimenOrArrayType("Specimen");

			storageContainerForm.setActivityStatus("Active");
			storageContainerForm.setIsFull("False");
			storageContainerForm.setOperation("add");
			setRequestPathInfo("/StorageContainerAdd");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyNoActionErrors();
			StorageContainerForm scform=(StorageContainerForm) getActionForm();

			//Adding New Fluid Specimen
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
			newSpecForm.setStContSelection(3);
			newSpecForm.setPos1("1");
			newSpecForm.setPos2("1");
			System.out.println(scform.getId()+"%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			DefaultBizLogic bizlogic = new DefaultBizLogic();
			StorageContainer sc = null;
			try
			{
				sc= (StorageContainer)bizlogic.
				retrieve("edu.wustl.catissuecore.domain.StorageContainer",scform.getId());
			}
			catch (BizLogicException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sc.getName()+">>>>>>>>>>>>>>");
			newSpecForm.setSelectedContainerName(sc.getName());
			newSpecForm.setClassName("Fluid");
			newSpecForm.setType("Bile");
			newSpecForm.setQuantity("10") ;
			newSpecForm.setAvailable(true);
			newSpecForm.setAvailableQuantity("10");
			newSpecForm.setCollectionStatus("Collected") ;

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
			newSpecForm=(NewSpecimenForm)getActionForm();
			//Retrieving specimen object for edit
			System.out.println("----specimen ID : " + newSpecForm.getId());
	   	}
		/**
		 * Test Specimen Add negative test case
		 */

		public void testSpecimenAddInWrongContainer()
		{
			StorageTypeForm storageTypeForm = RequestParameterUtility.createStorageTypeFormWithTypeRestriction(this,
					"Rest_Type_" + UniqueKeyGeneratorUtil.getUniqueKey(),3,3,"row","col","10","Active");
			String[] holdsSpecimenClassTypes = new String[1];
			holdsSpecimenClassTypes[0] = "Fluid";
			storageTypeForm.setHoldsSpecimenClassTypes(holdsSpecimenClassTypes);
			String[] fluid = {"Bile"};
			storageTypeForm.setHoldsTissueSpType(null);
			storageTypeForm.setHoldsCellSpType(null);
			storageTypeForm.setHoldsFluidSpType(fluid);
			storageTypeForm.setHoldsMolSpType(null);
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
			//Adding Storage Container
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

			String[] holdsSpecimenClassCollection = new String[1];
			holdsSpecimenClassCollection[0]="Fluid";
			storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
			storageContainerForm.setHoldsTissueSpType(null);
			storageContainerForm.setHoldsCellSpType(null);
			storageContainerForm.setHoldsFluidSpType(fluid);
			storageContainerForm.setHoldsMolSpType(null);
			storageContainerForm.setSpecimenOrArrayType("Specimen");
			storageContainerForm.setActivityStatus("Active");
			storageContainerForm.setIsFull("False");
			storageContainerForm.setOperation("add");
			setRequestPathInfo("/StorageContainerAdd");
			setActionForm(storageContainerForm);
			actionPerform();
			verifyForward("success");
			verifyNoActionErrors();
			StorageContainerForm scform=(StorageContainerForm) getActionForm();

			//Adding New Fluid Specimen
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
			newSpecForm.setStContSelection(3);
			newSpecForm.setPos1("1");
			newSpecForm.setPos2("1");
			DefaultBizLogic bizlogic = new DefaultBizLogic();
			StorageContainer sc = null;
			try
			{
				sc= (StorageContainer)bizlogic.
				retrieve("edu.wustl.catissuecore.domain.StorageContainer",scform.getId());
			}
			catch (BizLogicException e)
			{
				e.printStackTrace();
			}
			System.out.println(sc.getName()+">>>>>>>>>>>>>>");
			newSpecForm.setSelectedContainerName(sc.getName());
			newSpecForm.setClassName("Tissue");
			newSpecForm.setType("Fixed Tissue");
			newSpecForm.setQuantity("10") ;
			newSpecForm.setAvailable(true);
			newSpecForm.setAvailableQuantity("10");
			newSpecForm.setCollectionStatus("Collected");

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
			verifyForward("failure");
	   	}
		/**
		 * This test case will update the Specimen Class of "StorageContainer".
		 */
		public void testUpdateStorageContainerWithSpecimenClass()
		{
			StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
			StorageContainerForm storageContainerForm = null;
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

			storageContainerForm=(StorageContainerForm) getActionForm();
			String[] holdsSpecimenClassCollection = new String[2];
			holdsSpecimenClassCollection[0]="Fluid";
			holdsSpecimenClassCollection[1]="Tissue";
			storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
			storageContainerForm.setHoldsCellSpType(null);
			storageContainerForm.setHoldsMolSpType(null);
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

		public void testSpecimenAddForTransferEvent()
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
			specimen.setSpecimenClass( form.getClassName() );
			specimen.setSpecimenType( form.getType() );
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

	    	TestCaseUtility.setNameObjectMap("SpecimenForTranferEvent",specimen);
	   	}

		public void testSpecimenAddForTranfer()
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
			specimen.setSpecimenClass( form.getClassName() );
			specimen.setSpecimenType( form.getType() );
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

	    	TestCaseUtility.setNameObjectMap("SpecimenForTranfer",specimen);
	   	}

}
