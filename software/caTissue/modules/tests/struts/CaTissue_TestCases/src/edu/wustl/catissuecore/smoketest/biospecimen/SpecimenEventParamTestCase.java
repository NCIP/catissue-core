package edu.wustl.catissuecore.smoketest.biospecimen;

import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm;
import edu.wustl.catissuecore.actionForm.QuickEventsForm;
import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.smoketest.CaTissueSuiteSmokeBaseTest;
import edu.wustl.testframework.util.DataObject;

public class SpecimenEventParamTestCase extends CaTissueSuiteSmokeBaseTest
{
	public SpecimenEventParamTestCase(String name, DataObject dataObject)
	{
		super(name,dataObject);
	}
	public SpecimenEventParamTestCase(String name)
	{
		super(name);
	}
	public SpecimenEventParamTestCase()
	{
		super();
	}
	/*public void testAddCellSpecimenReviewParametersEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		CellSpecimenReviewParametersForm cellSpecimenReviewParametersForm = new CellSpecimenReviewParametersForm();
		setRequestPathInfo("/CellSpecimenReviewParameters");
		addRequestParameter("pageOf", "pageOfCellSpecimenReviewParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(cellSpecimenReviewParametersForm);
		actionPerform();
		verifyTilesForward("pageOfCellSpecimenReviewParameters",".catissuecore.cellSpecimenReviewParametersDef");

		cellSpecimenReviewParametersForm=(CellSpecimenReviewParametersForm)getActionForm();
		cellSpecimenReviewParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		cellSpecimenReviewParametersForm.setUserId(Long.parseLong(arr[3]));
		cellSpecimenReviewParametersForm.setDateOfEvent(arr[4]);
		cellSpecimenReviewParametersForm.setTimeInHours(arr[5]);
		cellSpecimenReviewParametersForm.setTimeInMinutes(arr[6]);
		cellSpecimenReviewParametersForm.setNeoplasticCellularityPercentage(arr[7]);
		cellSpecimenReviewParametersForm.setViableCellPercentage(arr[8]);

		setRequestPathInfo("/CellSpecimenReviewParametersAdd");
		setActionForm(cellSpecimenReviewParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});




	}*/

	/**
	 * Test CheckinCheckoutEvent Add.
	 */

	/*public void testAddCheckinCheckoutEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		CheckInCheckOutEventParametersForm checkInCheckOutEventParametersForm = new CheckInCheckOutEventParametersForm();
		setRequestPathInfo("/CheckInCheckOutEventParameters");
		addRequestParameter("pageOf", "pageOfCheckInCheckOutEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(checkInCheckOutEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfCheckInCheckOutEventParameters",".catissuecore.checkInCheckOutEventParametersDef");

		checkInCheckOutEventParametersForm=(CheckInCheckOutEventParametersForm)getActionForm();
		checkInCheckOutEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		checkInCheckOutEventParametersForm.setUserId(Long.parseLong(arr[3]));
		checkInCheckOutEventParametersForm.setDateOfEvent(arr[4]);
		checkInCheckOutEventParametersForm.setTimeInHours(arr[5]);
		checkInCheckOutEventParametersForm.setTimeInMinutes(arr[6]);
		checkInCheckOutEventParametersForm.setStorageStatus(arr[7]);


		setRequestPathInfo("/CheckInCheckOutEventParametersAdd");
		setActionForm(checkInCheckOutEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/
	/**
	 * Test CollectionEvent Add.
	 */

	/*public void testCollectionEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		CollectionEventParametersForm collectionEventParametersForm=new CollectionEventParametersForm();
		setRequestPathInfo("/CollectionEventParameters");
		addRequestParameter("pageOf", "pageOfCollectionEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(collectionEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfCollectionEventParameters",".catissuecore.collectionEventParametersDef");


		collectionEventParametersForm=(CollectionEventParametersForm)getActionForm();
		collectionEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		collectionEventParametersForm.setUserId(Long.parseLong(arr[3]));
		collectionEventParametersForm.setDateOfEvent(arr[4]);
		collectionEventParametersForm.setTimeInHours(arr[5]);
		collectionEventParametersForm.setTimeInMinutes(arr[6]);
		collectionEventParametersForm.setCollectionProcedure(arr[7]);
		collectionEventParametersForm.setContainer(arr[8]);

		setRequestPathInfo("/CollectionEventParametersAdd");
		setActionForm(collectionEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/
	/**
	 * Test EmbeddedEvent Add.
	 */

	/*public void testEmbeddedEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		EmbeddedEventParametersForm embeddedEventParametersForm=new EmbeddedEventParametersForm();
		setRequestPathInfo("/EmbeddedEventParameters");
		addRequestParameter("pageOf", "pageOfEmbeddedEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(embeddedEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfEmbeddedEventParameters",".catissuecore.embeddedEventParametersDef");


		embeddedEventParametersForm=(EmbeddedEventParametersForm)getActionForm();
		embeddedEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		embeddedEventParametersForm.setUserId(Long.parseLong(arr[3]));
		embeddedEventParametersForm.setDateOfEvent(arr[4]);
		embeddedEventParametersForm.setTimeInHours(arr[5]);
		embeddedEventParametersForm.setTimeInMinutes(arr[6]);
		embeddedEventParametersForm.setEmbeddingMedium(arr[7]);

		setRequestPathInfo("/EmbeddedEventParametersAdd");
		setActionForm(embeddedEventParametersForm);
		actionPerform();

		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}*/
	/**
	 * Test FixedEvent Add.
	 */

	/*public void testFixedEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		FixedEventParametersForm fixedEventParametersForm=new FixedEventParametersForm();
		setRequestPathInfo("/FixedEventParameters");
		addRequestParameter("pageOf", "pageOfEmbeddedEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(fixedEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfFixedEventParameters",".catissuecore.fixedEventParametersDef");


		fixedEventParametersForm=(FixedEventParametersForm)getActionForm();
		fixedEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		fixedEventParametersForm.setUserId(Long.parseLong(arr[3]));
		fixedEventParametersForm.setDateOfEvent(arr[4]);
		fixedEventParametersForm.setTimeInHours(arr[5]);
		fixedEventParametersForm.setTimeInMinutes(arr[6]);
		fixedEventParametersForm.setFixationType(arr[7]);
		fixedEventParametersForm.setDurationInMinutes(arr[8]);

		setRequestPathInfo("/FixedEventParametersAdd");
		setActionForm(fixedEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

		}*/
	/**
	 * Test FluidspecimenReviewEvent Add.
	 */

	/*public void testFluidspecimenReviewEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		FluidSpecimenReviewEventParametersForm fluidSpecimenReviewEventParametersForm=new FluidSpecimenReviewEventParametersForm();
		setRequestPathInfo("/FluidSpecimenReviewEventParameters");
		addRequestParameter("pageOf", "pageOfFluidSpecimenReviewEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(fluidSpecimenReviewEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfFluidSpecimenReviewParameters",".catissuecore.fluidSpecimenReviewEventParametersDef");


		fluidSpecimenReviewEventParametersForm=(FluidSpecimenReviewEventParametersForm)getActionForm();
		fluidSpecimenReviewEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		fluidSpecimenReviewEventParametersForm.setUserId(Long.parseLong(arr[3]));
		fluidSpecimenReviewEventParametersForm.setDateOfEvent(arr[4]);
		fluidSpecimenReviewEventParametersForm.setTimeInHours(arr[5]);
		fluidSpecimenReviewEventParametersForm.setTimeInMinutes(arr[6]);
		fluidSpecimenReviewEventParametersForm.setCellCount(arr[7]);


		setRequestPathInfo("/FluidSpecimenReviewEventParametersAdd");
		setActionForm(fluidSpecimenReviewEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/
	/**
	 * Test FrozenEvent Add.
	 */

//	public void testFrozenEvent()
//	{
//		String[] arr = getDataObject().getValues();
//
//		QuickEventsForm quickEventsForm=new QuickEventsForm();
//		setRequestPathInfo("/QuickEvents");
//		addRequestParameter("operation", "add");
//		setActionForm(quickEventsForm);
//		actionPerform();
//		verifyTilesForward("success", ".catissuecore.quickEventsDef");
//
//		setRequestPathInfo("/blankScreenAction");
//		actionPerform();
//		verifyTilesForward("success", ".catissuecore.bioBlankDef");
//
//		quickEventsForm.setSpecimenLabel(arr[0]);
//		quickEventsForm.setSpecimenEventParameter(arr[1]);
//		setRequestPathInfo("/QuickEventsSearch");
//		setActionForm(quickEventsForm);
//		actionPerform();
//
//		FrozenEventParametersForm frozenEventParametersForm=new FrozenEventParametersForm();
//		setRequestPathInfo("/FrozenEventParameters");
//		addRequestParameter("pageOf", "pageOfFluidSpecimenReviewEventParameters");
//		addRequestParameter("operation", "add");
//		addRequestParameter("specimenId",arr[2]);
//		setActionForm(frozenEventParametersForm);
//		actionPerform();
//		verifyTilesForward("pageOfFrozenEventParameters",".catissuecore.frozenEventParametersDef");
//
//
//		frozenEventParametersForm=(FrozenEventParametersForm)getActionForm();
//		frozenEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
//		frozenEventParametersForm.setUserId(Long.parseLong(arr[3]));
//		frozenEventParametersForm.setDateOfEvent(arr[4]);
//		frozenEventParametersForm.setTimeInHours(arr[5]);
//		frozenEventParametersForm.setTimeInMinutes(arr[6]);
//		frozenEventParametersForm.setMethod(arr[7]);
//
//
//		setRequestPathInfo("/FrozenEventParametersAdd");
//		setActionForm(frozenEventParametersForm);
//		actionPerform();
//		verifyForward("success");
//		verifyActionMessages(new String[]{"object.add.successOnly"});
//
//	}
	/**
	 * Test FluidspecimenReviewEvent Add.
	 */

	/*public void testMolecularspecimenReviewEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		MolecularSpecimenReviewParametersForm molecularSpecimenReviewParametersForm=new MolecularSpecimenReviewParametersForm();
		setRequestPathInfo("/MolecularSpecimenReviewParameters");
		addRequestParameter("pageOf", "pageOfMolecularSpecimenReviewParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(molecularSpecimenReviewParametersForm);
		actionPerform();
		verifyTilesForward("pageOfMolecularSpecimenReviewParameters",".catissuecore.molecularSpecimenReviewParametersDef");


		molecularSpecimenReviewParametersForm=(MolecularSpecimenReviewParametersForm)getActionForm();
		molecularSpecimenReviewParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		molecularSpecimenReviewParametersForm.setUserId(Long.parseLong(arr[3]));
		molecularSpecimenReviewParametersForm.setDateOfEvent(arr[4]);
		molecularSpecimenReviewParametersForm.setTimeInHours(arr[5]);
		molecularSpecimenReviewParametersForm.setTimeInMinutes(arr[6]);
		molecularSpecimenReviewParametersForm.setGelImageURL(arr[7]);
		molecularSpecimenReviewParametersForm.setQualityIndex(arr[8]);
		molecularSpecimenReviewParametersForm.setGelNumber(arr[9]);
		molecularSpecimenReviewParametersForm.setLaneNumber(arr[10]);
		molecularSpecimenReviewParametersForm.setAbsorbanceAt260(arr[11]);
		molecularSpecimenReviewParametersForm.setAbsorbanceAt280(arr[12]);
		molecularSpecimenReviewParametersForm.setRatio28STo18S(arr[13]);

		setRequestPathInfo("/MolecularSpecimenReviewParametersAdd");
		setActionForm(molecularSpecimenReviewParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/

	/**
	 * Test ProcedureEvent Add.
	 */

	/*public void testProcedureEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		ProcedureEventParametersForm procedureEventParametersForm=new ProcedureEventParametersForm();
		setRequestPathInfo("/ProcedureEventParameters");
		addRequestParameter("pageOf", "pageOfFluidSpecimenReviewEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(procedureEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfProcedureEventParameters",".catissuecore.procedureEventParametersDef");


		procedureEventParametersForm=(ProcedureEventParametersForm)getActionForm();
		procedureEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		procedureEventParametersForm.setUserId(Long.parseLong(arr[3]));
		procedureEventParametersForm.setDateOfEvent(arr[4]);
		procedureEventParametersForm.setTimeInHours(arr[5]);
		procedureEventParametersForm.setTimeInMinutes(arr[6]);
		procedureEventParametersForm.setUrl(arr[7]);
		procedureEventParametersForm.setName(arr[8]);

		setRequestPathInfo("/ProcedureEventParametersAdd");
		setActionForm(procedureEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/
	/**
	 * Test SpunEvent Add.
	 */

	/*public void testSpunEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		SpunEventParametersForm spunEventParametersForm=new SpunEventParametersForm();
		setRequestPathInfo("/SpunEventParameters");
		addRequestParameter("pageOf", "pageOfSpunEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(spunEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfSpunEventParameters",".catissuecore.spunEventParametersDef");


		spunEventParametersForm=(SpunEventParametersForm)getActionForm();
		spunEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		spunEventParametersForm.setUserId(Long.parseLong(arr[3]));
		spunEventParametersForm.setDateOfEvent(arr[4]);
		spunEventParametersForm.setTimeInHours(arr[5]);
		spunEventParametersForm.setTimeInMinutes(arr[6]);
		spunEventParametersForm.setGravityForce(arr[7]);
		spunEventParametersForm.setDurationInMinutes(arr[8]);

		setRequestPathInfo("/SpunEventParametersAdd");
		setActionForm(spunEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}*/
	/**
	 * Test ThawEvent Add.
	 */

	/*public void testThawEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		ThawEventParametersForm thawEventParametersForm=new ThawEventParametersForm();
		setRequestPathInfo("/ThawEventParameters");
		addRequestParameter("pageOf", "pageOfThawEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(thawEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfThawEventParameters",".catissuecore.thawEventParametersDef");


		thawEventParametersForm=(ThawEventParametersForm)getActionForm();
		thawEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		thawEventParametersForm.setUserId(Long.parseLong(arr[3]));
		thawEventParametersForm.setDateOfEvent(arr[4]);
		thawEventParametersForm.setTimeInHours(arr[5]);
		thawEventParametersForm.setTimeInMinutes(arr[6]);
		thawEventParametersForm.setComments(arr[7]);

		setRequestPathInfo("/ThawEventParametersAdd");
		setActionForm(thawEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}*/
	/**
	 * Test FrozenEvent Add.
	 */

/*	public void testTissuespecimenReviewEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		TissueSpecimenReviewEventParametersForm tissueSpecimenReviewEventParametersForm=new TissueSpecimenReviewEventParametersForm();
		setRequestPathInfo("/TissueSpecimenReviewEventParameters");
		addRequestParameter("pageOf", "pageOfTissueSpecimenReviewEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(tissueSpecimenReviewEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfTissueSpecimenReviewParameters",".catissuecore.tissueSpecimenReviewEventParametersDef");


		tissueSpecimenReviewEventParametersForm=(TissueSpecimenReviewEventParametersForm)getActionForm();
		tissueSpecimenReviewEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		tissueSpecimenReviewEventParametersForm.setUserId(Long.parseLong(arr[3]));
		tissueSpecimenReviewEventParametersForm.setDateOfEvent(arr[4]);
		tissueSpecimenReviewEventParametersForm.setTimeInHours(arr[5]);
		tissueSpecimenReviewEventParametersForm.setTimeInMinutes(arr[6]);
		tissueSpecimenReviewEventParametersForm.setNeoplasticCellularityPercentage(arr[7]);
		tissueSpecimenReviewEventParametersForm.setNecrosisPercentage(arr[8]);
		tissueSpecimenReviewEventParametersForm.setLymphocyticPercentage(arr[9]);
		tissueSpecimenReviewEventParametersForm.setTotalCellularityPercentage(arr[10]);
		tissueSpecimenReviewEventParametersForm.setHistologicalQuality(arr[11]);

		setRequestPathInfo("/TissueSpecimenReviewEventParametersAdd");
		setActionForm(tissueSpecimenReviewEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});



	}*/
	/**
	 * Test TransferEvent Add.
	 */

	public void testAddTranferEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		TransferEventParametersForm transferEventParametersForm=new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTissueSpecimenReviewEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(transferEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfTransferEventParameters",".catissuecore.transferEventParametersDef");


		transferEventParametersForm=(TransferEventParametersForm)getActionForm();
		transferEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		transferEventParametersForm.setUserId(Long.parseLong(arr[3]));
		transferEventParametersForm.setDateOfEvent(arr[4]);
		transferEventParametersForm.setTimeInHours(arr[5]);
		transferEventParametersForm.setTimeInMinutes(arr[6]);
		transferEventParametersForm.setFromStorageContainerId(Long.parseLong(arr[7]));
		transferEventParametersForm.setFromPositionDimensionOne(Integer.parseInt(arr[8]));
		transferEventParametersForm.setFromPositionDimensionTwo(Integer.parseInt(arr[9]));
		transferEventParametersForm.setStorageContainer(arr[10]);
		transferEventParametersForm.setPositionDimensionOne(arr[11]);
		transferEventParametersForm.setPositionDimensionTwo(arr[12]);


		setRequestPathInfo("/TransferEventParametersAdd");
		setActionForm(transferEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});


	}


	/**
	 * Test CollectionEvent Add.
	 */

	public void testDisposalEvent()
	{
		String[] arr = getDataObject().getValues();

		QuickEventsForm quickEventsForm=new QuickEventsForm();
		setRequestPathInfo("/QuickEvents");
		addRequestParameter("operation", "add");
		setActionForm(quickEventsForm);
		actionPerform();
		verifyTilesForward("success", ".catissuecore.quickEventsDef");

		setRequestPathInfo("/blankScreenAction");
		actionPerform();
		verifyTilesForward("success", ".catissuecore.bioBlankDef");

		quickEventsForm.setSpecimenLabel(arr[0]);
		quickEventsForm.setSpecimenEventParameter(arr[1]);
		setRequestPathInfo("/QuickEventsSearch");
		setActionForm(quickEventsForm);
		actionPerform();

		DisposalEventParametersForm disposalEventParametersForm=new DisposalEventParametersForm();
		setRequestPathInfo("/DisposalEventParameters");
		addRequestParameter("pageOf", "pageOfDisposalEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",arr[2]);
		setActionForm(disposalEventParametersForm);
		actionPerform();
		verifyTilesForward("pageOfDisposalEventParameters",".catissuecore.disposalEventParametersDef");


		disposalEventParametersForm=(DisposalEventParametersForm)getActionForm();
		disposalEventParametersForm.setSpecimenId(Long.parseLong(arr[2]));
		disposalEventParametersForm.setUserId(Long.parseLong(arr[3]));
		disposalEventParametersForm.setDateOfEvent(arr[4]);
		disposalEventParametersForm.setTimeInHours(arr[5]);
		disposalEventParametersForm.setTimeInMinutes(arr[6]);
		disposalEventParametersForm.setActivityStatus(arr[7]);
		disposalEventParametersForm.setReason(arr[8]);


		setRequestPathInfo("/DisposalEventParametersAdd");
		setActionForm(disposalEventParametersForm);
		actionPerform();
		verifyForward("success");
		verifyActionMessages(new String[]{"object.add.successOnly"});

	}

	public void testlistSpecimenEventParametersForm()
	{
		setRequestPathInfo("/DisposalEventParameters");

		ListSpecimenEventParametersForm form = new ListSpecimenEventParametersForm();
		form.setSpecimenEventParameter(null);
		actionPerform();
	}
}
