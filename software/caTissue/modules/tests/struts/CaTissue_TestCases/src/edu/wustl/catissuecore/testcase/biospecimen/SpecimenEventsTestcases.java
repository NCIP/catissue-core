package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.DisposalEventParametersForm;
import edu.wustl.catissuecore.actionForm.ListSpecimenEventParametersForm;
import edu.wustl.catissuecore.actionForm.TransferEventParametersForm;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.util.global.Constants;


public class SpecimenEventsTestcases  extends CaTissueSuiteBaseTest
{
  	/**
	 * Test CellSpecimenReviewParametersEvent Add.
	 */

	/*public void testAddCellSpecimenReviewParametersEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		CellSpecimenReviewParametersForm form = new CellSpecimenReviewParametersForm();
		setRequestPathInfo("/CellSpecimenReviewParameters");
		addRequestParameter("pageOf", "pageOfCellSpecimenReviewParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfCellSpecimenReviewParameters");
		CellSpecimenReviewParametersForm cellForm= (CellSpecimenReviewParametersForm) getActionForm();
		cellForm.setNeoplasticCellularityPercentage("1.999999");
		cellForm.setViableCellPercentage( "1.999999" );
		addRequestParameter("specimenId",specimen.getId().toString());
		cellForm.setSpecimenId( specimen.getId() );
		cellForm.setForwardTo( "success" );
		setActionForm(form);
		setRequestPathInfo("/CellSpecimenReviewParametersAdd");
		actionPerform();
		verifyForward("success");

		setRequestPathInfo("/CellSpecimenReviewParametersSearch");
		addRequestParameter("pageOf", "pageOfCellSpecimenReviewParameters");
		addRequestParameter("operation", "edit");
		request.setAttribute(Constants.SPECIMEN_ID,specimen.getId().toString());
		actionPerform();
		verifyForward("pageOfCellSpecimenReviewParameters");

		cellForm= (CellSpecimenReviewParametersForm) getActionForm();
		assertEquals("2.0", cellForm.getNeoplasticCellularityPercentage());
		assertEquals("2.0", cellForm.getViableCellPercentage());

		CellSpecimenReviewParameters cellSpecimenReviewParametersObject = new CellSpecimenReviewParameters();
		cellSpecimenReviewParametersObject.setId( cellForm.getId() );
		cellSpecimenReviewParametersObject.setNeoplasticCellularityPercentage( Double.valueOf( cellForm.getNeoplasticCellularityPercentage() ));
		cellSpecimenReviewParametersObject.setViableCellPercentage( Double.valueOf( cellForm.getViableCellPercentage() ) );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( cellSpecimenReviewParametersObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );
	}*/

	/**
	 * Test CheckinCheckoutEvent Add.
	 */

	/*public void testAddCheckinCheckoutEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		CheckInCheckOutEventParametersForm form = new CheckInCheckOutEventParametersForm();
		setRequestPathInfo("/CheckInCheckOutEventParameters");
		addRequestParameter("pageOf", "pageOfCheckInCheckOutEventParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfCheckInCheckOutEventParameters");
		CheckInCheckOutEventParametersForm checkincheckoutForm = (CheckInCheckOutEventParametersForm) getActionForm();
		checkincheckoutForm.setStorageStatus( "CHECK IN" );
		addRequestParameter("specimenId",specimen.getId().toString());
		checkincheckoutForm.setSpecimenId( specimen.getId() );
		checkincheckoutForm.setForwardTo( "success" );
		setActionForm(form);
		setRequestPathInfo("/CheckInCheckOutEventParametersAdd");
		actionPerform();
		verifyForward("success");
		CheckInCheckOutEventParameter checkincheckoutObject = new CheckInCheckOutEventParameter();
		checkincheckoutObject.setId( checkincheckoutForm.getId() );
		checkincheckoutObject.setStorageStatus( checkincheckoutForm.getStorageStatus() );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( checkincheckoutObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}*/
	/**
	 * Test CollectionEvent Add.
	 */

	/*public void testCollectionEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		CollectionEventParametersForm form = new CollectionEventParametersForm();
		setRequestPathInfo("/CollectionEventParameters");
		addRequestParameter("pageOf", "pageOfCollectionEventParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfCollectionEventParameters");
		CollectionEventParametersForm collectionForm = (CollectionEventParametersForm) getActionForm();
		collectionForm.setCollectionProcedure( "Surgical Resection" );
		collectionForm.setContainer( "EDTA Vacutainer" );
		addRequestParameter("specimenId",specimen.getId().toString());
		collectionForm.setSpecimenId( specimen.getId() );
		collectionForm.setForwardTo( "success" );
		setActionForm(form);
		setRequestPathInfo("/CollectionEventParametersAdd");
		actionPerform();
		verifyForward("success");
		CollectionEventParameters collectionEventObject = new CollectionEventParameters();
		collectionEventObject.setId( collectionForm.getId() );
		collectionEventObject.setCollectionProcedure( collectionForm.getCollectionProcedure() );
		collectionEventObject.setContainer( collectionForm.getContainer() );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( collectionEventObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}*/
	/**
	 * Test EmbeddedEvent Add.
	 */

	/*public void testEmbeddedEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		EmbeddedEventParametersForm form = new EmbeddedEventParametersForm();
		setRequestPathInfo("/EmbeddedEventParameters");
		addRequestParameter("pageOf", "pageOfEmbeddedEventParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfEmbeddedEventParameters");
		EmbeddedEventParametersForm embeddedForm = (EmbeddedEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		embeddedForm.setEmbeddingMedium( "Paraffin" );
		embeddedForm.setSpecimenId( specimen.getId() );
		embeddedForm.setForwardTo( "success" );
		setActionForm(form);
		setRequestPathInfo("/EmbeddedEventParametersAdd");
		actionPerform();
		verifyForward("success");
		EmbeddedEventParameters embeddedObject = new EmbeddedEventParameters();
		embeddedObject.setId( embeddedForm.getId() );
		embeddedObject.setEmbeddingMedium( embeddedForm.getEmbeddingMedium() );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( embeddedObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}*/
	/**
	 * Test FixedEvent Add.
	 */

	/*public void testFixedEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		FixedEventParametersForm form = new FixedEventParametersForm();
		setRequestPathInfo("/FixedEventParameters");
		addRequestParameter("pageOf", "pageOfFixedEventParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfFixedEventParameters");
		FixedEventParametersForm fixedForm = (FixedEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		fixedForm.setFixationType( "Formaldehyde" );
		fixedForm.setDurationInMinutes( "20" );
		fixedForm.setForwardTo( "success" );
		fixedForm.setSpecimenId( specimen.getId() );
		setActionForm(form);
		setRequestPathInfo("/FixedEventParametersAdd");
		actionPerform();
		verifyForward("success");
		FixedEventParameters fixedObject = new FixedEventParameters();
		fixedObject.setId( fixedForm.getId() );
		fixedObject.setFixationType( fixedForm.getFixationType() );
		fixedObject.setDurationInMinutes( Integer.valueOf( fixedForm.getDurationInMinutes() ) );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( fixedObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}*/
	/**
	 * Test FluidspecimenReviewEvent Add.
	 */

//	public void testFluidspecimenReviewEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		FluidSpecimenReviewEventParametersForm form = new FluidSpecimenReviewEventParametersForm();
//		setRequestPathInfo("/FluidSpecimenReviewEventParameters");
//		addRequestParameter("pageOf", "pageOfFluidSpecimenReviewParameters");
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfFluidSpecimenReviewParameters");
//		FluidSpecimenReviewEventParametersForm fluidForm = (FluidSpecimenReviewEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		fluidForm.setCellCount( "1.999999" );
//		fluidForm.setForwardTo( "success" );
//		fluidForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/FluidSpecimenReviewEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//
//		setRequestPathInfo("/FluidSpecimenReviewEventParametersSearch");
//		addRequestParameter("pageOf", "pageOfFluidSpecimenReviewParameters");
//		addRequestParameter("operation", "edit");
//		request.setAttribute(Constants.SPECIMEN_ID,specimen.getId().toString());
//		actionPerform();
//		verifyForward("pageOfFluidSpecimenReviewParameters");
//
//		fluidForm = (FluidSpecimenReviewEventParametersForm) getActionForm();
//		assertEquals("2.0", fluidForm.getCellCount());
//
//		FluidSpecimenReviewEventParameters fluidSpecimenReviewEventObject = new FluidSpecimenReviewEventParameters();
//		fluidSpecimenReviewEventObject.setId( fluidForm.getId() );
//		fluidSpecimenReviewEventObject.setCellCount( Double.valueOf( fluidForm.getCellCount() ) );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( fluidSpecimenReviewEventObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test FrozenEvent Add.
	 */

//	public void testFrozenEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		FrozenEventParametersForm form = new FrozenEventParametersForm();
//		setRequestPathInfo("/FrozenEventParameters");
//		addRequestParameter("pageOf", "pageOfFrozenEventParameters");
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfFrozenEventParameters");
//		FrozenEventParametersForm frozenForm = (FrozenEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		frozenForm.setMethod( "Dry Ice" );
//		frozenForm.setForwardTo( "success" );
//		frozenForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/FrozenEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//		FrozenEventParameters frozenwEventObject = new FrozenEventParameters();
//		frozenwEventObject.setId( frozenForm.getId() );
//		frozenwEventObject.setMethod( frozenForm.getMethod() );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( frozenwEventObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test FluidspecimenReviewEvent Add.
	 */

//	public void testMolecularspecimenReviewEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		MolecularSpecimenReviewParametersForm form = new MolecularSpecimenReviewParametersForm();
//		setRequestPathInfo("/MolecularSpecimenReviewParameters");
//		addRequestParameter("pageOf", "pageOfMolecularSpecimenReviewParameters");
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfMolecularSpecimenReviewParameters");
//		MolecularSpecimenReviewParametersForm molecularForm = (MolecularSpecimenReviewParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		molecularForm.setGelImageURL( "/test/Image.jpeg" );
//		molecularForm.setQualityIndex( "1" );
//		molecularForm.setGelNumber( "2" );
//		molecularForm.setLaneNumber( "3" );
//		molecularForm.setAbsorbanceAt260( "1.999999" );
//		molecularForm.setAbsorbanceAt280( "1.999999" );
//		molecularForm.setRatio28STo18S( "1" );
//		molecularForm.setForwardTo( "success" );
//		setActionForm(form);
//		setRequestPathInfo("/MolecularSpecimenReviewParametersAdd");
//		actionPerform();
//		verifyForward("success");
//
//		setRequestPathInfo("/MolecularSpecimenReviewParametersSearch");
//		addRequestParameter("pageOf", "pageOfMolecularSpecimenReviewParameters");
//		addRequestParameter("operation", "edit");
//		request.setAttribute(Constants.SPECIMEN_ID,specimen.getId().toString());
//		actionPerform();
//		verifyForward("pageOfMolecularSpecimenReviewParameters");
//
//		molecularForm = (MolecularSpecimenReviewParametersForm) getActionForm();
//	   assertEquals("2.0", molecularForm.getAbsorbanceAt260());
//	   assertEquals("2.0", molecularForm.getAbsorbanceAt280());
//
//		MolecularSpecimenReviewParameters molecularSpecimenReviewEventObject = new MolecularSpecimenReviewParameters();
//		molecularSpecimenReviewEventObject.setId( molecularForm.getId() );
//		molecularSpecimenReviewEventObject.setGelImageURL( molecularForm.getGelImageURL() );
//		molecularSpecimenReviewEventObject.setQualityIndex( molecularForm.getQualityIndex() );
//		molecularSpecimenReviewEventObject.setGelNumber( Integer.valueOf( molecularForm.getGelNumber() ) );
//		molecularSpecimenReviewEventObject.setLaneNumber( molecularForm.getLaneNumber() );
//		molecularSpecimenReviewEventObject.setAbsorbanceAt260( Double.valueOf( molecularForm.getAbsorbanceAt260() ) );
//		molecularSpecimenReviewEventObject.setAbsorbanceAt280( Double.valueOf( molecularForm.getAbsorbanceAt280() ) );
//		molecularSpecimenReviewEventObject.setRatio28STo18S(  Double.valueOf( molecularForm.getRatio28STo18S() )  );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( molecularSpecimenReviewEventObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}

	/**
	 * Test ProcedureEvent Add.
	 */

//	public void testProcedureEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		ProcedureEventParametersForm form = new ProcedureEventParametersForm();
//		setRequestPathInfo("/ProcedureEventParameters");
//		addRequestParameter("pageOf", "pageOfProcedureEventParameters");
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfProcedureEventParameters");
//		ProcedureEventParametersForm procedureForm = (ProcedureEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		procedureForm.setUrl( "test.com" );
//		procedureForm.setName( "Procedure" );
//		procedureForm.setForwardTo( "success" );
//		procedureForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/ProcedureEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//		ProcedureEventParameters procedureObject = new ProcedureEventParameters();
//		procedureObject.setId( procedureForm.getId() );
//		procedureObject.setUrl( procedureForm.getUrl() );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( procedureObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test SpunEvent Add.
	 */

//	public void testSpunEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		SpunEventParametersForm form = new SpunEventParametersForm();
//		setRequestPathInfo("/SpunEventParameters");
//		addRequestParameter("pageOf", "pageOfSpunEventParameters");
//		addRequestParameter("specimenId",specimen.getId().toString());
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfSpunEventParameters");
//		SpunEventParametersForm spunForm = (SpunEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		spunForm.setGravityForce( "1.999999" );
//		spunForm.setDurationInMinutes( "30" );
//		spunForm.setForwardTo( "success" );
//		spunForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/SpunEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//		SpunEventParameters spunObject = new SpunEventParameters();
//
//		setRequestPathInfo("/SpunEventParametersSearch");
//		addRequestParameter("pageOf", "pageOfSpunEventParameters");
//		addRequestParameter("operation", "edit");
//		request.setAttribute(Constants.SPECIMEN_ID,specimen.getId().toString());
//		actionPerform();
//		verifyForward("pageOfSpunEventParameters");
//		spunForm = (SpunEventParametersForm)getActionForm();
//		assertEquals("2.0", spunForm.getGravityForce());
//
//		spunObject.setId( spunForm.getId() );
//		spunObject.setGravityForce( Double.valueOf( spunForm.getGravityForce() ) );
//		spunObject.setDurationInMinutes( Integer.valueOf( spunForm.getDurationInMinutes() ) );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( spunObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test ThawEvent Add.
	 */

//	public void testThawEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		ThawEventParametersForm form = new ThawEventParametersForm();
//		setRequestPathInfo("/ThawEventParameters");
//		addRequestParameter("pageOf", "pageOfThawEventParameters");
//		addRequestParameter("specimenId",specimen.getId().toString());
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfThawEventParameters");
//		ThawEventParametersForm thawForm = (ThawEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		thawForm.setForwardTo( "success" );
//		thawForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/ThawEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//		ThawEventParameters thawObject = new ThawEventParameters();
//		thawObject.setId( thawForm.getId() );
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( thawObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test FrozenEvent Add.
	 */

//	public void testTissuespecimenReviewEvent()
//	{
//		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
//		TissueSpecimenReviewEventParametersForm form = new TissueSpecimenReviewEventParametersForm();
//		setRequestPathInfo("/TissueSpecimenReviewEventParameters");
//		addRequestParameter("pageOf", "pageOfTissueSpecimenReviewParameters");
//		addRequestParameter("operation", "add");
//		setActionForm(form);
//		actionPerform();
//		verifyForward("pageOfTissueSpecimenReviewParameters");
//		TissueSpecimenReviewEventParametersForm tissueForm = (TissueSpecimenReviewEventParametersForm) getActionForm();
//		addRequestParameter("specimenId",specimen.getId().toString());
//		tissueForm.setNeoplasticCellularityPercentage( "1.999999" );
//		tissueForm.setNecrosisPercentage( "1.999999" );
//		tissueForm.setLymphocyticPercentage( "1.999999" );
//		tissueForm.setTotalCellularityPercentage( "1.999999" );
//		tissueForm.setHistologicalQuality( "Good- Definable Cellular Detail" );
//		tissueForm.setForwardTo( "success" );
//		tissueForm.setSpecimenId( specimen.getId() );
//		setActionForm(form);
//		setRequestPathInfo("/TissueSpecimenReviewEventParametersAdd");
//		actionPerform();
//		verifyForward("success");
//
//		setRequestPathInfo("/TissueSpecimenReviewEventParametersSearch");
//		addRequestParameter("pageOf", "pageOfTissueSpecimenReviewParameters");
//		addRequestParameter("operation", "edit");
//		request.setAttribute(Constants.SPECIMEN_ID,specimen.getId().toString());
//		actionPerform();
//		verifyForward("pageOfTissueSpecimenReviewParameters");
//
//		tissueForm = (TissueSpecimenReviewEventParametersForm) getActionForm();
//		assertEquals("2.0", tissueForm.getNeoplasticCellularityPercentage());
//		assertEquals("2.0", tissueForm.getNecrosisPercentage());
//		assertEquals("2.0", tissueForm.getTotalCellularityPercentage());
//		assertEquals("2.0", tissueForm.getLymphocyticPercentage());
//
//		TissueSpecimenReviewEventParameters tissueEventObject = new TissueSpecimenReviewEventParameters();
//		tissueEventObject.setId( tissueForm.getId() );
//		tissueEventObject.setNeoplasticCellularityPercentage( Double.valueOf( tissueForm.getNeoplasticCellularityPercentage() ) );
//		tissueEventObject.setNecrosisPercentage( Double.valueOf( tissueForm.getNecrosisPercentage() )  );
//		tissueEventObject.setLymphocyticPercentage( Double.valueOf( tissueForm.getLymphocyticPercentage() ) );
//		tissueEventObject.setTotalCellularityPercentage(  Double.valueOf( tissueForm.getTotalCellularityPercentage() )  );
//		tissueEventObject.setHistologicalQuality( tissueForm.getTotalCellularityPercentage());
//		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		specimenEventCollection.add( tissueEventObject );
//		specimen.setSpecimenEventCollection( specimenEventCollection );
//
//	}
	/**
	 * Test TransferEvent Add.
	 */

	public void testAddTranferEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("SpecimenForTranferEvent");
		StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		TransferEventParametersForm form = new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTransferEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",specimen.getId().toString());
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfTransferEventParameters");

		TransferEventParametersForm transferEventForm = (TransferEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		transferEventForm.setForwardTo( "success" );


		transferEventForm.setStorageContainer(container.getId().toString());
		transferEventForm.setPositionDimensionOne("20");
		transferEventForm.setPositionDimensionTwo("20");

		transferEventForm.setSpecimenId( specimen.getId() );
		transferEventForm.setStContSelection( 1 );
		setActionForm(transferEventForm);
		setRequestPathInfo("/TransferEventParametersAdd");
		actionPerform();
		verifyForward("success");
	}

	/**
	 * Test TransferEvent Add.
	 * Negative Test case
	 */
	public void testAddTranferEventWithNullToContainer()

	{//errors.item.required
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("SpecimenForTranfer");
		StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		TransferEventParametersForm form = new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTransferEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",specimen.getId().toString());
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfTransferEventParameters");

		TransferEventParametersForm transferEventForm = (TransferEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		transferEventForm.setForwardTo( "success" );


		transferEventForm.setStorageContainer(null);
		transferEventForm.setPositionDimensionOne("20");
		transferEventForm.setPositionDimensionTwo("20");

		transferEventForm.setSpecimenId( specimen.getId() );
		transferEventForm.setStContSelection( 1 );
		setActionForm(transferEventForm);
		setRequestPathInfo("/TransferEventParametersAdd");
		actionPerform();
		verifyActionErrors(new String []{"errors.item.required"});

	}

	/**
	 * Test TransferEvent Add.
	 * Negative Test case
	 */
	public void testAddTranferEventWithInvalidFromContainer()

	{//errors.item.required
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("SpecimenForTranferEvent");
		StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		TransferEventParametersForm form = new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTransferEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",specimen.getId().toString());
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfTransferEventParameters");

		TransferEventParametersForm transferEventForm = (TransferEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		transferEventForm.setForwardTo( "success" );

		transferEventForm.setFromStorageContainerId(0l);
		transferEventForm.setFromPositionDimensionOne(2);
		transferEventForm.setFromPositionDimensionTwo(2);


		transferEventForm.setStorageContainer(container.getId().toString());
		transferEventForm.setPositionDimensionOne("20");
		transferEventForm.setPositionDimensionTwo("20");

		transferEventForm.setSpecimenId( specimen.getId() );
		transferEventForm.setStContSelection( 1 );
		setActionForm(transferEventForm);
		setRequestPathInfo("/TransferEventParametersAdd");
		actionPerform();
		verifyActionErrors(new String[]{"errors.item"});

	}

	/**
	 * Test TransferEvent Add.
	 * Negative Test case
	 */
	public void testAddTranferEventWithOccupiedPositions()

	{//errors.item.required
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("SpecimenForTranfer");
		StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		TransferEventParametersForm form = new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTransferEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",specimen.getId().toString());
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfTransferEventParameters");

		TransferEventParametersForm transferEventForm = (TransferEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		transferEventForm.setForwardTo( "success" );


		transferEventForm.setStorageContainer(container.getId().toString());
		transferEventForm.setPositionDimensionOne("20");
		transferEventForm.setPositionDimensionTwo("20");

//		transferEventForm.setFromStorageContainerId(container.getId());
//		transferEventForm.setFromPositionDimensionOne(2);
//		transferEventForm.setFromPositionDimensionTwo(2);

		transferEventForm.setSpecimenId( specimen.getId() );
		transferEventForm.setStContSelection( 1 );
		setActionForm(transferEventForm);
		setRequestPathInfo("/TransferEventParametersAdd");
		actionPerform();
		verifyActionErrors(new String []{"errors.item"});

	}
	/*
	public void testTransferEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		TransferEventParametersForm form = new TransferEventParametersForm();
		setRequestPathInfo("/TransferEventParameters");
		addRequestParameter("pageOf", "pageOfTransferEventParameters");
		addRequestParameter("operation", "add");
		addRequestParameter("specimenId",specimen.getId().toString());
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfTransferEventParameters");
		TransferEventParametersForm transferEventForm = (TransferEventParametersForm) getActionForm();
		addRequestParameter("specimenId",specimen.getId().toString());
		transferEventForm.setForwardTo( "success" );
		transferEventForm.setFromPositionData( "virtual Location" );
		transferEventForm.setStContSelection( 1 );
		StorageContainer container = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		transferEventForm.setStorageContainer( container.getName() );
		transferEventForm.setSelectedContainerName( container.getName() );
		transferEventForm.setPositionDimensionOne( "1" );
		transferEventForm.setPositionDimensionTwo( "1" );
		transferEventForm.setSpecimenId( specimen.getId() );
		setActionForm(form);
		setRequestPathInfo("/TransferEventParametersAdd");
		actionPerform();
		verifyForward("success");
		TransferEventParameters transferEventObject = new TransferEventParameters();
		transferEventObject.setId( transferEventForm.getId() );
		if(transferEventForm.getPos1()!=null && transferEventForm.getPos2()!=null)
		{
		  transferEventObject.setToPositionDimensionOne( Integer.valueOf( transferEventForm.getPositionDimensionOne() )  );
		  transferEventObject.setToPositionDimensionTwo(  Integer.valueOf( transferEventForm.getPositionDimensionTwo() ) );
		}
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( transferEventObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}*/
	/**
	 * Test CollectionEvent Add.
	 */

	public void testDisposalEvent()
	{
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		DisposalEventParametersForm form = new DisposalEventParametersForm();
		setRequestPathInfo("/DisposalEventParameters");
		addRequestParameter("pageOf", "pageOfDisposalEventParameters");
		addRequestParameter("operation", "add");
		setActionForm(form);
		actionPerform();
		verifyForward("pageOfDisposalEventParameters");
		DisposalEventParametersForm disposalEventForm = (DisposalEventParametersForm) getActionForm();
		disposalEventForm.setActivityStatus( "Closed" );
		addRequestParameter("specimenId",specimen.getId().toString());
		disposalEventForm.setSpecimenId( specimen.getId() );
		disposalEventForm.setForwardTo( "success" );
		setActionForm(form);
		setRequestPathInfo("/DisposalEventParametersAdd");
		actionPerform();
		verifyForward("success");
		DisposalEventParameters disposalEventObject = new DisposalEventParameters();
		disposalEventObject.setId( disposalEventForm.getId() );
		disposalEventObject.setActivityStatus( disposalEventForm.getActivityStatus() );
		Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
		specimenEventCollection.add( disposalEventObject );
		specimen.setSpecimenEventCollection( specimenEventCollection );

	}

	public void testlistSpecimenEventParametersForm()
	{
		setRequestPathInfo("/DisposalEventParameters");

		ListSpecimenEventParametersForm form = new ListSpecimenEventParametersForm();
		form.setSpecimenEventParameter(null);
		actionPerform();
	}

}
