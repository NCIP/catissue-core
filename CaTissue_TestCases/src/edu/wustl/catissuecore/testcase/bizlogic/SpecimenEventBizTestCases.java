package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.Status;

public class SpecimenEventBizTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Negative test case : Test case for decreasing capacity of already filled Storage Container.
	 */
	public void testDecreseCapacityOfFullContainer()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			MolecularSpecimen mSpecimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
			FluidSpecimen fSpecimenObj = (FluidSpecimen) BaseTestCaseUtility.initFluidSpecimen();
			CellSpecimen cSpecimenObj = (CellSpecimen) BaseTestCaseUtility.initCellSpecimen();

			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Capacity capacity = new Capacity();
			capacity.setOneDimensionCapacity(new Integer(2));
			capacity.setTwoDimensionCapacity(new Integer(2));
			storageContainer.setCapacity(capacity);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);

			SpecimenPosition pos2 = new SpecimenPosition();
			pos2.setPositionDimensionOne(1);
			pos2.setPositionDimensionTwo(2);
			pos2.setStorageContainer(storageContainer);
			mSpecimenObj.setSpecimenPosition(pos2);

			SpecimenPosition pos3 = new SpecimenPosition();
			pos3.setPositionDimensionOne(2);
			pos3.setPositionDimensionTwo(1);
			pos3.setStorageContainer(storageContainer);
			cSpecimenObj.setSpecimenPosition(pos3);

			SpecimenPosition pos4 = new SpecimenPosition();
			pos4.setPositionDimensionOne(2);
			pos4.setPositionDimensionTwo(2);
			pos4.setStorageContainer(storageContainer);
			fSpecimenObj.setSpecimenPosition(pos4);

			tSpecimenObj.setSpecimenCollectionGroup(scg);
			cSpecimenObj.setSpecimenCollectionGroup(scg);
			mSpecimenObj.setSpecimenCollectionGroup(scg);
			fSpecimenObj.setSpecimenCollectionGroup(scg);

			System.out.println("Before Creating Specimen object");

			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			mSpecimenObj = (MolecularSpecimen) appService.createObject(mSpecimenObj);
			fSpecimenObj = (FluidSpecimen) appService.createObject(fSpecimenObj);
			cSpecimenObj = (CellSpecimen) appService.createObject(cSpecimenObj);

			Capacity cap = storageContainer.getCapacity();
			cap.setOneDimensionCapacity(new Integer(1));
			cap.setTwoDimensionCapacity(new Integer(1));
			storageContainer = (StorageContainer) appService.updateObject(storageContainer);
			assertFalse("Container is already Full:" + storageContainer, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testDecreseCapacityOfFullContainer()");
			e.printStackTrace();
			assertTrue("Failed to Update as Storage Container is already Full :" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for Transfer Specimen Position from one position to another.
	 */
	public void testTransferPositionOFSpecimen()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			TransferEventParameters spe = new TransferEventParameters();
			spe.setSpecimen(tSpecimenObj);
			spe.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			//(User)TestCaseUtility.getObjectMap(User.class);
			spe.setUser(user);
			spe.setComment("Transfer Event");
			spe.setFromPositionDimensionOne(1);
			spe.setFromPositionDimensionTwo(1);
			spe.setFromStorageContainer(storageContainer);
			spe.setToPositionDimensionOne(2);
			spe.setToPositionDimensionTwo(2);
			spe.setToStorageContainer(storageContainer);
			System.out.println("Before Creating TransferEvent");
			spe = (TransferEventParameters) appService.createObject(spe);
			System.out.println("After Creating Specimen object");
			assertTrue("Specimen Position Sucessfully Transfered :" + spe, true);

		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testTransferPositionOFSpecimen()");
			e.printStackTrace();
			assertFalse("Container is already Full:" + e.getMessage(), true);
		}
	}

	/**
	 * Negative test case: Test case for Transfer Specimen Position from one position to another
	 * Occupied container.
	 */
	public void testTransferSpecimenToOccupiedLocation()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			MolecularSpecimen mSpecimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Tissue Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Tissue Specimen object");

			SpecimenPosition pos2 = new SpecimenPosition();
			pos2.setPositionDimensionOne(2);
			pos2.setPositionDimensionTwo(2);
			pos2.setStorageContainer(storageContainer);
			mSpecimenObj.setSpecimenPosition(pos2);
			mSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Molecular Specimen object");
			mSpecimenObj = (MolecularSpecimen) appService.createObject(mSpecimenObj);
			System.out.println("After Creating Molecular Specimen object");

			TransferEventParameters spe = new TransferEventParameters();
			spe.setSpecimen(tSpecimenObj);
			spe.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			//(User)TestCaseUtility.getObjectMap(User.class);
			spe.setUser(user);
			spe.setComment("Transfer Event");
			spe.setFromPositionDimensionOne(1);
			spe.setFromPositionDimensionTwo(1);
			spe.setFromStorageContainer(storageContainer);
			spe.setToPositionDimensionOne(2);
			spe.setToPositionDimensionTwo(2);
			spe.setToStorageContainer(storageContainer);
			System.out.println("Before Creating TransferEvent");
			spe = (TransferEventParameters) appService.createObject(spe);
			System.out.println("After Creating Specimen object");
			assertFalse("Container is already Full", true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testTransferPositionOFSpecimen()");
			e.printStackTrace();
			assertTrue("Specimen Position Sucessfully Transfered :" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Dispose Specimen Event.
	 */
	public void testInsertDisposeSpecimenEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			DisposalEventParameters disposalEvent = new DisposalEventParameters();
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
			disposalEvent.setSpecimen(tSpecimenObj);
			disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			disposalEvent.setUser(user);
			disposalEvent.setReason("Testing API");
			disposalEvent.setComment("Dispose Event");
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
			System.out.println("Before Creating DisposeEvent");
			disposalEvent = (DisposalEventParameters) appService.createObject(disposalEvent);
			TestCaseUtility.setObjectMap(disposalEvent, DisposalEventParameters.class);
			if (Status.ACTIVITY_STATUS_CLOSED.toString().equals(disposalEvent.getSpecimen().getActivityStatus()))
			{
				assertTrue("Disposed event sucessfully fired: Activity Status Closed :" + disposalEvent, true);
			}
			else
			{
				assertFalse("Disposed event Failed:" + disposalEvent, true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertDisposeSpecimenEvent()");
			e.printStackTrace();
			assertFalse("Disposed Specimen Event failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for update Dispose Specimen Event from closed to disabled activity status.
	 */
	public void testUpdateDisposeSpecimenEventFromClosedToDisabled()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			tSpecimenObj.setCollectionStatus("Collected");
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			DisposalEventParameters disposalEvent = new DisposalEventParameters();
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
			disposalEvent.setSpecimen(tSpecimenObj);
			disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			disposalEvent.setUser(user);
			disposalEvent.setReason("Disposing Specimen");
			disposalEvent.setComment("Dispose Event");
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
			
			System.out.println("Before Creating DisposeEvent");
			disposalEvent = (DisposalEventParameters) appService.createObject(disposalEvent);
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
			disposalEvent = (DisposalEventParameters) appService.updateObject(disposalEvent);
			if (Status.ACTIVITY_STATUS_DISABLED.toString().equals(disposalEvent.getSpecimen().getActivityStatus()))
			{
				assertTrue("Disposed event sucessfully fired: Activity Status changed from Closed to Disbled:" + disposalEvent, true);
			}
			else
			{
				assertFalse("Disposed event Failed:" + disposalEvent, true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertDisposeSpecimenEvent()");
			e.printStackTrace();
			assertFalse("Disposed Specimen Event failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Dispose Specimen Event.
	 */
	public void testUpdateDisposeSpecimenEvent()
	{
		try
		{
			DisposalEventParameters disposalEvent = (DisposalEventParameters) TestCaseUtility.getObjectMap(DisposalEventParameters.class);
			disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
			disposalEvent = (DisposalEventParameters) appService.updateObject(disposalEvent);
			if (Status.ACTIVITY_STATUS_DISABLED.toString().equals(disposalEvent.getActivityStatus()))
			{
				assertTrue("Disposed event sucessfully fired" + disposalEvent, true);
			}
			else
			{
				assertFalse("Disposed event Failed:", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateDisposeSpecimenEvent()");
			e.printStackTrace();
			assertFalse("Disposed event Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Dispose Specimen Event.
	 */
	public void testInsertCellSpecimenReviewEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			CellSpecimenReviewParameters cellSpecimenReviewParameters = new CellSpecimenReviewParameters();
			cellSpecimenReviewParameters.setSpecimen(tSpecimenObj);
			cellSpecimenReviewParameters.setTimestamp(new Date(System.currentTimeMillis()));
			cellSpecimenReviewParameters.setNeoplasticCellularityPercentage(2D);
			cellSpecimenReviewParameters.setViableCellPercentage(4D);
			User user = new User();
			user.setId(1l);//admin
			cellSpecimenReviewParameters.setUser(user);
			cellSpecimenReviewParameters.setComment("CellSpecimenReviewEvent");
			cellSpecimenReviewParameters = (CellSpecimenReviewParameters) appService.createObject(cellSpecimenReviewParameters);
			TestCaseUtility.setObjectMap(cellSpecimenReviewParameters, CellSpecimenReviewParameters.class);
			assertTrue("Cell Specimen Review Parameters event sucessfully fired" + cellSpecimenReviewParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertCellSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("Cell Specimen Review Parameters event Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Cell Specimen Review Parameters.
	 */
	public void testUpdateCellSpecimenReviewParameters()
	{
		try
		{
			CellSpecimenReviewParameters csrp = (CellSpecimenReviewParameters) TestCaseUtility.getObjectMap(CellSpecimenReviewParameters.class);
			csrp.setNeoplasticCellularityPercentage(66D);
			csrp.setViableCellPercentage(40D);
			csrp = (CellSpecimenReviewParameters) appService.updateObject(csrp);
			if (csrp.getNeoplasticCellularityPercentage() != 66D)
			{
				assertFalse("Cell Specimen Review Parameters event Failed:", true);
			}
			else
			{
				assertTrue("Cell Specimen Review Parameters event sucessfully fired" + csrp, true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateCellSpecimenReviewParameters()");
			e.printStackTrace();
			assertFalse("Cell Specimen Review Parameters event Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Check In Check Out Event.
	 */
	public void testInsertCheckInCheckOutEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			CheckInCheckOutEventParameter checkInCheckOutEventParameter = new CheckInCheckOutEventParameter();
			checkInCheckOutEventParameter.setSpecimen(tSpecimenObj);
			checkInCheckOutEventParameter.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			checkInCheckOutEventParameter.setUser(user);
			checkInCheckOutEventParameter.setStorageStatus("CHECK IN");
			checkInCheckOutEventParameter.setComment("CheckInCheckOutEventParameter");
			checkInCheckOutEventParameter = (CheckInCheckOutEventParameter) appService.createObject(checkInCheckOutEventParameter);
			TestCaseUtility.setObjectMap(checkInCheckOutEventParameter, CheckInCheckOutEventParameter.class);
			assertTrue("CheckInCheckOutEvent sucessfully fired" + checkInCheckOutEventParameter, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertCheckInCheckOutEvent()");
			e.printStackTrace();
			assertFalse("CheckInCheckOutEventevent Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Check In Check Out Event.
	 */
	public void testUpdateCheckInCheckOutEvent()
	{
		try
		{
			CheckInCheckOutEventParameter cico = (CheckInCheckOutEventParameter) TestCaseUtility.getObjectMap(CheckInCheckOutEventParameter.class);
			cico.setStorageStatus("CHECK OUT");
			cico = (CheckInCheckOutEventParameter) appService.updateObject(cico);
			if ("CHECK OUT".equals(cico.getStorageStatus()))
			{
				assertTrue("Check In Check Out event sucessfully fired" + cico, true);
			}
			else
			{
				assertFalse("Check In Check Out event Failed:", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateCheckInCheckOutEvent()");
			e.printStackTrace();
			assertFalse("Check In Check Out event Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Collection Event.
	 */
	public void testInsertCollectionEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			CollectionEventParameters collectionEventParameter = new CollectionEventParameters();
			collectionEventParameter.setSpecimen(tSpecimenObj);
			collectionEventParameter.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			collectionEventParameter.setUser(user);
			collectionEventParameter.setComment("CollectionEvent");
			collectionEventParameter.setCollectionProcedure("Lavage");
			collectionEventParameter.setContainer("EDTA Vacutainer");
			collectionEventParameter = (CollectionEventParameters) appService.createObject(collectionEventParameter);
			TestCaseUtility.setObjectMap(collectionEventParameter, CollectionEventParameters.class);
			assertTrue("Collection event sucessfully fired" + collectionEventParameter, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertCollectionEvent()");
			e.printStackTrace();
			assertFalse("Collection event Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Collection event.
	 */
	public void testUpdateCollectionEvent()
	{
		try
		{
			CollectionEventParameters collectionEventParameter = (CollectionEventParameters) TestCaseUtility
					.getObjectMap(CollectionEventParameters.class);
			collectionEventParameter.setCollectionProcedure("Needle Core Biopsy");
			collectionEventParameter = (CollectionEventParameters) appService.updateObject(collectionEventParameter);
			if ("Needle Core Biopsy".equals(collectionEventParameter.getCollectionProcedure()))
			{
				assertTrue("Collection event sucessfully fired" + collectionEventParameter, true);
			}
			else
			{
				assertFalse("Collection event Failed:", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateCollectionEvent()");
			e.printStackTrace();
			assertFalse("CollectionEventParameters Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Embedded Event.
	 */
	public void testInsertEmbeddedEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			EmbeddedEventParameters embeddedEventParameters = new EmbeddedEventParameters();
			embeddedEventParameters.setSpecimen(tSpecimenObj);
			embeddedEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			embeddedEventParameters.setUser(user);
			embeddedEventParameters.setEmbeddingMedium("Plastic");
			embeddedEventParameters.setComment("EmbeddedEventParameters");
			embeddedEventParameters = (EmbeddedEventParameters) appService.createObject(embeddedEventParameters);
			TestCaseUtility.setObjectMap(embeddedEventParameters, EmbeddedEventParameters.class);
			assertTrue("EmbeddedEventParameters sucessfully fired" + embeddedEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertEmbeddedEvent()");
			e.printStackTrace();
			assertFalse("EmbeddedEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Check In Check Out Event.
	 */
	public void testUpdateEmbeddedEvent()
	{
		try
		{
			EmbeddedEventParameters embeddedEventParameters = (EmbeddedEventParameters) TestCaseUtility.getObjectMap(EmbeddedEventParameters.class);
			embeddedEventParameters.setEmbeddingMedium("Paraffin");
			embeddedEventParameters = (EmbeddedEventParameters) appService.updateObject(embeddedEventParameters);
			if ("Paraffin".equals(embeddedEventParameters.getEmbeddingMedium()))
			{
				assertTrue("Embedded Event sucessfully fired" + embeddedEventParameters, true);
			}
			else
			{
				assertFalse("Embedded Event Failed:", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateEmbeddedEvent()");
			e.printStackTrace();
			assertFalse("Embedded Event Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Fixed Event.
	 */
	public void testInsertFixedEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			FixedEventParameters fixedEventParameters = new FixedEventParameters();
			fixedEventParameters.setSpecimen(tSpecimenObj);
			fixedEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			fixedEventParameters.setUser(user);
			fixedEventParameters.setFixationType("Methanol");
			fixedEventParameters.setComment("CheckInCheckOutEventParameter");
			fixedEventParameters = (FixedEventParameters) appService.createObject(fixedEventParameters);
			TestCaseUtility.setObjectMap(fixedEventParameters, FixedEventParameters.class);
			assertTrue("FixedEventParameters sucessfully fired" + fixedEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertFixedEvent()");
			e.printStackTrace();
			assertFalse("FixedEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Update Fixed Event.
	 */
	public void testUpdateFixedEvent()
	{
		try
		{
			FixedEventParameters fixedEventParameters = (FixedEventParameters) TestCaseUtility.getObjectMap(FixedEventParameters.class);
			fixedEventParameters.setFixationType("RNALater");
			fixedEventParameters = (FixedEventParameters) appService.updateObject(fixedEventParameters);
			if ("RNALater".equals(fixedEventParameters.getFixationType()))
			{
				assertTrue("Fixed Event Parameters sucessfully fired" + fixedEventParameters, true);
			}
			else
			{
				assertFalse("Fixed Event Parameters Failed:", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateFixedEvent()");
			e.printStackTrace();
			assertFalse("Fixed Event Parameters Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Fluid Specimen Review Event.
	 */
	public void testInsertFluidSpecimenReview()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			FluidSpecimenReviewEventParameters fluidSpecimenReviewEventParameters = new FluidSpecimenReviewEventParameters();

			fluidSpecimenReviewEventParameters.setSpecimen(tSpecimenObj);
			fluidSpecimenReviewEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			fluidSpecimenReviewEventParameters.setCellCount(20D);
			User user = new User();
			user.setId(1l);//admin
			fluidSpecimenReviewEventParameters.setUser(user);

			fluidSpecimenReviewEventParameters.setComment("Fluid Specimen Review Event");
			fluidSpecimenReviewEventParameters = (FluidSpecimenReviewEventParameters) appService.createObject(fluidSpecimenReviewEventParameters);
			TestCaseUtility.setObjectMap(fluidSpecimenReviewEventParameters, FluidSpecimenReviewEventParameters.class);
			assertTrue("fluidSpecimenReviewEventParameters sucessfully fired" + fluidSpecimenReviewEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertFluidSpecimenReview()");
			e.printStackTrace();
			assertFalse("fluidSpecimenReviewEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating fluid SpecimenReview Event Parameters.
	 */
	public void testUpdateFluidSpecimenReviewEventParameters()
	{
		try
		{
			FluidSpecimenReviewEventParameters fluidSpecimenReviewEventParameters = (FluidSpecimenReviewEventParameters) TestCaseUtility
					.getObjectMap(FluidSpecimenReviewEventParameters.class);
			fluidSpecimenReviewEventParameters.setCellCount(40D);
			fluidSpecimenReviewEventParameters = (FluidSpecimenReviewEventParameters) appService.updateObject(fluidSpecimenReviewEventParameters);

			FluidSpecimenReviewEventParameters fSRE = new FluidSpecimenReviewEventParameters();
			fSRE.setId(fluidSpecimenReviewEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters as fluidSpecimenReviewEventParameters where "
					+ "fluidSpecimenReviewEventParameters.id= "+fluidSpecimenReviewEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					fSRE = (FluidSpecimenReviewEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if (40D == fSRE.getCellCount())
			{
				assertTrue("FluidSpecimenReviewEventParameters sucessfully fired" + fSRE, true);
			}
			else
			{
				assertFalse("FluidSpecimenReviewEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateFluidSpecimenReviewEventParameters()");
			e.printStackTrace();
			assertFalse("FluidSpecimenReviewEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 * Test case for Insert Frozen Specimen Review event.
	 */
	public void testInsertFrozenSpecimenReviewEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			FrozenEventParameters frozenEventParameters = new FrozenEventParameters();

			frozenEventParameters.setSpecimen(tSpecimenObj);
			frozenEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			frozenEventParameters.setMethod("Cryostat");
			User user = new User();
			user.setId(1l);//admin
			frozenEventParameters.setUser(user);

			frozenEventParameters.setComment("Frozen Event");
			frozenEventParameters = (FrozenEventParameters) appService.createObject(frozenEventParameters);
			TestCaseUtility.setObjectMap(frozenEventParameters, FrozenEventParameters.class);
			assertTrue("Frozen Event Parameters sucessfully fired" + frozenEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertFrozenSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("frozenEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Frozen SpecimenReviewEventParameters.
	 */
	public void testUpdateFrozenSpecimenReviewEventParameters()
	{
		try
		{
			FrozenEventParameters frozenEventParameters = (FrozenEventParameters) TestCaseUtility.getObjectMap(FrozenEventParameters.class);
			frozenEventParameters.setMethod("Cryobath");
			frozenEventParameters = (FrozenEventParameters) appService.updateObject(frozenEventParameters);

			FrozenEventParameters frozenSRE = new FrozenEventParameters();
			frozenSRE.setId(frozenEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.FrozenEventParameters as frozenEventParameters where "
					+ "frozenEventParameters.id= "+frozenEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					frozenSRE = (FrozenEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if ("Cryobath".equals(frozenSRE.getMethod()))
			{
				assertTrue("Frozen Specimen ReviewEventParameters sucessfully fired" + frozenSRE, true);
			}
			else
			{
				assertFalse("Frozen Specimen Review Event Parameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateFrozenSpecimenReviewEventParameters()");
			e.printStackTrace();
			assertFalse("Frozen Specimen Review Event Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert Spun Event.
	 */
	public void testInsertSpunSpecimenEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			System.out.println("after Creating scg");
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			SpunEventParameters spunEventParameters = new SpunEventParameters();
			spunEventParameters.setSpecimen(tSpecimenObj);
			spunEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			spunEventParameters.setUser(user);
			spunEventParameters.setComment("Spun Specimen Event");
			spunEventParameters.setGravityForce(10D);
			spunEventParameters = (SpunEventParameters) appService.createObject(spunEventParameters);
			TestCaseUtility.setObjectMap(spunEventParameters, SpunEventParameters.class);
			assertTrue("spunSpecimenEventParameters sucessfully fired" + spunEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertSpunSpecimenEvent()");
			e.printStackTrace();
			assertFalse("spunSpecimenEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Spun Event.
	 */
	public void testUpdatetestInsertSpunSpecimenEvent()
	{
		try
		{
			SpunEventParameters spunEventParameters = (SpunEventParameters) TestCaseUtility.getObjectMap(SpunEventParameters.class);
			spunEventParameters.setGravityForce(20D);
			spunEventParameters = (SpunEventParameters) appService.updateObject(spunEventParameters);

			SpunEventParameters spunEP = new SpunEventParameters();
			spunEP.setId(spunEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.SpunEventParameters as spunEventParameters where "
					+ "spunEventParameters.id= "+spunEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					spunEP = (SpunEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if (20D == spunEP.getGravityForce())
			{
				assertTrue("SpunSpecimenEventParameters sucessfully fired" + spunEP, true);
			}
			else
			{
				assertFalse("SpunSpecimenEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdatetestInsertSpunSpecimenEvent()");
			e.printStackTrace();
			assertFalse("SpunSpecimenEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert Thaw Event.
	 */
	public void testInsertThawSpecimenEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			System.out.println("after Creating scg");
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			ThawEventParameters thawEventParameters = new ThawEventParameters();
			thawEventParameters.setSpecimen(tSpecimenObj);
			thawEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			thawEventParameters.setUser(user);
			thawEventParameters.setComment("Thaw Specimen Event");
			thawEventParameters = (ThawEventParameters) appService.createObject(thawEventParameters);
			TestCaseUtility.setObjectMap(thawEventParameters, ThawEventParameters.class);
			assertTrue("ThawSpecimenEventParameters sucessfully fired" + thawEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertThawSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ThawSpecimenEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating Thaw Event.
	 */
	public void testUpdateThawSpecimenEvent()
	{
		try
		{
			ThawEventParameters thawEventParameters = (ThawEventParameters) TestCaseUtility.getObjectMap(ThawEventParameters.class);
			String comment = "update Thaw Specimen Event";
			thawEventParameters.setComment(comment);
			thawEventParameters = (ThawEventParameters) appService.updateObject(thawEventParameters);

			ThawEventParameters thawEP = new ThawEventParameters();
			thawEP.setId(thawEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.ThawEventParameters as ThawEventParameters where "
					+ "ThawEventParameters.id= "+thawEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					thawEP = (ThawEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}
			if (comment.equals(thawEP.getComment()))
			{
				assertTrue("ThawSpecimenEventParameters sucessfully fired" + thawEP, true);
			}
			else
			{
				assertFalse("ThawSpecimenEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateThawSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ThawSpecimenEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert TissueSpecimenReview Event.
	 */
	public void testInsertTissueSpecimenReviewEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			TissueSpecimenReviewEventParameters tsReviewEventParameters = new TissueSpecimenReviewEventParameters();
			tsReviewEventParameters.setSpecimen(tSpecimenObj);
			tsReviewEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			tsReviewEventParameters.setUser(user);
			tsReviewEventParameters.setComment("TissueSpecimenReview Specimen Event");
			tsReviewEventParameters.setHistologicalQuality("Excellent- Definable Nuclear Detail");
			tsReviewEventParameters.setNecrosisPercentage(10D);
			tsReviewEventParameters = (TissueSpecimenReviewEventParameters) appService.createObject(tsReviewEventParameters);
			TestCaseUtility.setObjectMap(tsReviewEventParameters, TissueSpecimenReviewEventParameters.class);
			assertTrue("TissueSpecimenReviewEventParameters sucessfully fired" + tsReviewEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertTissueSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("TissueSpecimenReviewEventParameters Failed:" + e.getMessage(), true);
		}
	}

	/**
	 * Test case for updating TissueSpecimenReview Event.
	 */
	public void testUpdateTissueSpecimenReviewEvent()
	{
		try
		{
			TissueSpecimenReviewEventParameters tsReviewEventParameters = (TissueSpecimenReviewEventParameters) TestCaseUtility
					.getObjectMap(TissueSpecimenReviewEventParameters.class);
			tsReviewEventParameters.setNecrosisPercentage(20D);
			tsReviewEventParameters = (TissueSpecimenReviewEventParameters) appService.updateObject(tsReviewEventParameters);

			TissueSpecimenReviewEventParameters tissueSpecimenEP = new TissueSpecimenReviewEventParameters();
			tissueSpecimenEP.setId(tsReviewEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters as tissueSpecimenReviewEventParameters where "
					+ "tissueSpecimenReviewEventParameters.id= "+tsReviewEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					tissueSpecimenEP = (TissueSpecimenReviewEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}
			if (20D == tissueSpecimenEP.getNecrosisPercentage())
			{
				assertTrue("TissueSpecimenReviewEventParameters sucessfully fired" + tissueSpecimenEP, true);
			}
			else
			{
				assertFalse("TissueSpecimenReviewEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateTissueSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("TissueSpecimenReviewEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert ReceivedSpecimen Event.
	 */
	public void testInsertReceivedSpecimenEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			System.out.println("after Creating scg");
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
			receivedEventParameters.setSpecimen(tSpecimenObj);
			receivedEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			receivedEventParameters.setUser(user);
			receivedEventParameters.setComment("Received Specimen Event");
			receivedEventParameters.setReceivedQuality("Cauterized");
			receivedEventParameters = (ReceivedEventParameters) appService.createObject(receivedEventParameters);
			TestCaseUtility.setObjectMap(receivedEventParameters, ReceivedEventParameters.class);
			assertTrue("ReceivedEventParameters sucessfully fired" + receivedEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertReceivedSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ReceivedEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating ReceivedSpecimen Event.
	 */
	public void testUpdateReceivedSpecimenEvent()
	{
		try
		{
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) TestCaseUtility.getObjectMap(ReceivedEventParameters.class);
			receivedEventParameters.setReceivedQuality("Acceptable");
			receivedEventParameters = (ReceivedEventParameters) appService.updateObject(receivedEventParameters);

			ReceivedEventParameters receivedEP = new ReceivedEventParameters();
			receivedEP.setId(receivedEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.ReceivedEventParameters as receivedEventParameters where "
					+ "receivedEventParameters.id= "+receivedEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					receivedEP = (ReceivedEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if ("Acceptable".equals(receivedEP.getReceivedQuality()))
			{
				assertTrue("ReceivedEventParameters sucessfully fired" + receivedEP, true);
			}
			else
			{
				assertFalse("ReceivedEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateReceivedSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ReceivedEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert ProcedureSpecimen Event.
	 */
	public void testInsertProcedureSpecimenEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);

			System.out.println("after Creating scg");
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			ProcedureEventParameters procEventParameters = new ProcedureEventParameters();
			procEventParameters.setSpecimen(tSpecimenObj);
			procEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			procEventParameters.setUser(user);
			procEventParameters.setComment("Procedure Specimen Event");
			procEventParameters.setName("ProcedureEvent");
			procEventParameters.setUrl("http://localhost:8080/catissuecore/");
			procEventParameters = (ProcedureEventParameters) appService.createObject(procEventParameters);
			TestCaseUtility.setObjectMap(procEventParameters, ProcedureEventParameters.class);
			assertTrue("ProcedureEventParameters sucessfully fired" + procEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertProcedureSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ProcedureEventParameters Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating ProcedureSpecimen Event.
	 */
	public void testUpdateProcedureSpecimenEvent()
	{
		try
		{
			ProcedureEventParameters procEventParameters = (ProcedureEventParameters) TestCaseUtility.getObjectMap(ProcedureEventParameters.class);
			procEventParameters.setName("NewProcedureEvent");
			procEventParameters = (ProcedureEventParameters) appService.updateObject(procEventParameters);

			ProcedureEventParameters procEP = new ProcedureEventParameters();
			procEP.setId(procEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.ProcedureEventParameters as procedureEventParameters where "
					+ "procedureEventParameters.id= "+procEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					procEP = (ProcedureEventParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if ("NewProcedureEvent".equals(procEP.getName()))
			{
				assertTrue("ProcedureEventParameters sucessfully fired" + procEP, true);
			}
			else
			{
				assertFalse("ProcedureEventParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateProcedureSpecimenEvent()");
			e.printStackTrace();
			assertFalse("ProcedureEventParameters Failed to update " + e.getMessage(), true);
		}
	}

	/**
	 *  Test case for Insert MolecularSpecimenReviewParameters Event.
	 */
	public void testInsertMolecularSpecimenReviewEvent()
	{
		try
		{
			TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			System.out.println("after Creating scg");
			StorageContainer storageContainer = BaseTestCaseUtility.initStorageContainer();
			Collection collectionProtocolCollection = new HashSet();
			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
			storageContainer = (StorageContainer) appService.createObject(storageContainer);

			SpecimenPosition pos1 = new SpecimenPosition();
			pos1.setPositionDimensionOne(1);
			pos1.setPositionDimensionTwo(1);
			pos1.setStorageContainer(storageContainer);
			System.out.println("after Creating storageContainer");
			tSpecimenObj.setSpecimenPosition(pos1);
			tSpecimenObj.setSpecimenCollectionGroup(scg);
			System.out.println("Before Creating Specimen object");
			tSpecimenObj = (TissueSpecimen) appService.createObject(tSpecimenObj);
			System.out.println("After Creating Specimen object");

			MolecularSpecimenReviewParameters molecularSpecEventParameters = new MolecularSpecimenReviewParameters();
			molecularSpecEventParameters.setSpecimen(tSpecimenObj);
			molecularSpecEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
			User user = new User();
			user.setId(1l);//admin
			molecularSpecEventParameters.setUser(user);
			molecularSpecEventParameters.setComment("MolecularSpecimenReview Specimen Event");
			molecularSpecEventParameters.setLaneNumber("12");
			molecularSpecEventParameters.setQualityIndex("1");
			molecularSpecEventParameters = (MolecularSpecimenReviewParameters) appService.createObject(molecularSpecEventParameters);
			TestCaseUtility.setObjectMap(molecularSpecEventParameters, MolecularSpecimenReviewParameters.class);
			assertTrue("MolecularSpecimenReview sucessfully fired" + molecularSpecEventParameters, true);
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertMolecularSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("MolecularSpecimenReview Failed:" + e.getMessage(), true);

		}
	}

	/**
	 * Test case for updating MolecularSpecimenReviewParameters Event.
	 */
	public void testUpdateMolecularSpecimenReviewEvent()
	{
		try
		{
			MolecularSpecimenReviewParameters molecularSpecEventParameters = (MolecularSpecimenReviewParameters) TestCaseUtility
					.getObjectMap(MolecularSpecimenReviewParameters.class);
			molecularSpecEventParameters.setLaneNumber("13");
			molecularSpecEventParameters = (MolecularSpecimenReviewParameters) appService.updateObject(molecularSpecEventParameters);

			MolecularSpecimenReviewParameters molecularEP = new MolecularSpecimenReviewParameters();
			molecularEP.setId(molecularSpecEventParameters.getId());
			try
			{
				String query = "from edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters as molecularSpecimenReviewParameters where "
					+ "molecularSpecimenReviewParameters.id= "+molecularSpecEventParameters.getId();	
				List resultList = appService.search(query);
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					molecularEP = (MolecularSpecimenReviewParameters) resultsIterator.next();
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in Search");
				e.printStackTrace();
				assertFalse("Does not find Domain Object", true);
			}

			if ("13".equals(molecularEP.getLaneNumber()))
			{
				assertTrue("MolecularSpecimenReviewParameters sucessfully fired" + molecularEP, true);
			}
			else
			{
				assertFalse("MolecularSpecimenReviewParameters Failed to update :", true);
			}
		}
		catch (Exception e)
		{
			System.out.println("SpecimenEventTestCases.testUpdateMolecularSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("MolecularSpecimenReviewParameters Failed to update " + e.getMessage(), true);
		}
	}
}