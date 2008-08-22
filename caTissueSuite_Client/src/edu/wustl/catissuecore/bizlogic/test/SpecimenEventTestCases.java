package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;


public class SpecimenEventTestCases extends CaTissueBaseTestCase
{
	
	/**
	 * Negative test case : Test case for decreasing capacity of already filled Storage Container
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
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
			   assertFalse("Container is already Full:" +storageContainer , true);  
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testDecreseCapacityOfFullContainer()");
			e.printStackTrace();
			assertTrue("Failed to Update as Storage Container is already Full :" +e.getMessage() , true);
			
		}
	}
	
	/**
	 * Test case for Transfer Specimen Position from one position to another
	 */
	public void testTransferPositionOFSpecimen()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
			   assertTrue("Specimen Position Sucessfully Transfered :" + spe , true);
			   
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testTransferPositionOFSpecimen()");
			e.printStackTrace();
			assertFalse("Container is already Full:" +e.getMessage(), true);  
			
		}
	}
	
	/**
	 * Negative test case: Test case for Transfer Specimen Position from one position to another Occupied container
	 */
	public void testTransferSpecimenToOccupiedLocation()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   MolecularSpecimen mSpecimenObj = (MolecularSpecimen) BaseTestCaseUtility.initMolecularSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testTransferPositionOFSpecimen()");
			e.printStackTrace();
			    assertTrue("Specimen Position Sucessfully Transfered :" + e.getMessage() , true);
		}
	}
	/**
	 * Test case for Insert Dispose Specimen Event
	 */
	public void testInsertDisposeSpecimenEvent()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		       disposalEvent.setActivityStatus(Constants.ACTIVITY_STATUS_CLOSED);
		       disposalEvent.setSpecimen(tSpecimenObj);
		       disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		       User user = new User();
		       user.setId(1l);//admin
		       disposalEvent.setUser(user);
		       disposalEvent.setReason("Testing API");
		       disposalEvent.setComment("Dispose Event");
		       disposalEvent.setActivityStatus(Constants.ACTIVITY_STATUS_CLOSED);
		       System.out.println("Before Creating DisposeEvent");
		       disposalEvent = (DisposalEventParameters) appService.createObject(disposalEvent);
		       TestCaseUtility.setObjectMap(disposalEvent, DisposalEventParameters.class);
		       if(Constants.ACTIVITY_STATUS_CLOSED.equals(disposalEvent.getSpecimen().getActivityStatus()))
		       {
		    	   assertTrue("Disposed event sucessfully fired: Activity Status Closed :" + disposalEvent , true);   
		       }
		       else
		       {
		    	   assertFalse("Disposed event Failed:" + disposalEvent , true);   
		       }
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertDisposeSpecimenEvent()");
			e.printStackTrace();
			assertFalse("Disposed Specimen Event failed:" +e.getMessage(), true);  
			
		}
	}

	/**
	 * Test case for Insert Dispose Specimen Event
	 */
	public void testInsertCellSpecimenReviewEvent()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		       assertTrue("Cell Specimen Review Parameters event sucessfully fired" + cellSpecimenReviewParameters , true);
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertCellSpecimenReviewEvent()");
			e.printStackTrace();
			assertFalse("Cell Specimen Review Parameters event Failed:" +e.getMessage(), true);  
			
		}
	}
	
	/**
	 * Test case for updating Cell Specimen Review Parameters
	 */
	public void testUpdateCellSpecimenReviewParameters()
	{
		try
		{
			CellSpecimenReviewParameters csrp = (CellSpecimenReviewParameters)TestCaseUtility.getObjectMap(CellSpecimenReviewParameters.class);
			csrp.setNeoplasticCellularityPercentage(66D);
			csrp.setViableCellPercentage(40D);
			csrp = (CellSpecimenReviewParameters)appService.updateObject(csrp);
			if(csrp.getNeoplasticCellularityPercentage()!= 66D )
			{
				assertFalse("Cell Specimen Review Parameters event Failed:", true);  
			}
			else
			{
				assertTrue("Cell Specimen Review Parameters event sucessfully fired" + csrp , true);
			}
		}
		catch(Exception e)
		{
			System.out
					.println("SpecimenEventTestCases.testUpdateCellSpecimenReviewParameters()");
			e.printStackTrace();
			assertFalse("Cell Specimen Review Parameters event Failed:" +e.getMessage(), true);  
		}
	}
	
	/**
	 * Test case for Insert Check In Check Out Event
	 */
	public void testInsertCheckInCheckOutEvent()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		       assertTrue("CheckInCheckOutEvent sucessfully fired" + checkInCheckOutEventParameter , true);
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertCheckInCheckOutEvent()");
			e.printStackTrace();
			assertFalse("CheckInCheckOutEventevent Failed:" +e.getMessage(), true);  
			
		}
	}
	
	/**
	 * Test case for updating Check In Check Out Event
	 */
	public void testUpdateCheckInCheckOutEvent()
	{
		try
		{
			CheckInCheckOutEventParameter cico = (CheckInCheckOutEventParameter)TestCaseUtility.getObjectMap(CheckInCheckOutEventParameter.class);
			cico.setStorageStatus("CHECK OUT");
			cico = (CheckInCheckOutEventParameter)appService.updateObject(cico);
			if("CHECK OUT".equals(cico.getStorageStatus()))
			{
				assertTrue("Check In Check Out event sucessfully fired" + cico , true);  
			}
			else
			{
				assertFalse("Check In Check Out event Failed:", true);
			}
		}
		catch(Exception e)
		{
			System.out
					.println("SpecimenEventTestCases.testUpdateCheckInCheckOutEvent()");
			e.printStackTrace();
			assertFalse("Check In Check Out event Failed:" +e.getMessage(), true);  
		}
	}
	
	/**
	 * Test case for Insert Embedded Event
	 */
	public void testInsertEmbeddedEvent()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		       embeddedEventParameters.setComment("CheckInCheckOutEventParameter");
		       embeddedEventParameters = (EmbeddedEventParameters) appService.createObject(embeddedEventParameters);
		       TestCaseUtility.setObjectMap(embeddedEventParameters, EmbeddedEventParameters.class);
		       assertTrue("EmbeddedEventParameters sucessfully fired" + embeddedEventParameters , true);
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertEmbeddedEvent()");
			e.printStackTrace();
			assertFalse("EmbeddedEventParameters Failed:" +e.getMessage(), true);  
			
		}
	}
	
	/**
	 * Test case for updating Check In Check Out Event
	 */
	public void testUpdateEmbeddedEvent()
	{
		try
		{
			EmbeddedEventParameters embeddedEventParameters = (EmbeddedEventParameters)TestCaseUtility.getObjectMap(EmbeddedEventParameters.class);
			embeddedEventParameters.setEmbeddingMedium("Paraffin");
			embeddedEventParameters = (EmbeddedEventParameters)appService.updateObject(embeddedEventParameters);
			if("Paraffin".equals(embeddedEventParameters.getEmbeddingMedium()))
			{
				assertTrue("Embedded Event sucessfully fired" + embeddedEventParameters , true);  
			}
			else
			{
				assertFalse("Embedded Event Failed:", true);
			}
		}
		catch(Exception e)
		{
			System.out
					.println("SpecimenEventTestCases.testUpdateEmbeddedEvent()");
			e.printStackTrace();
			assertFalse("Embedded Event Failed:" +e.getMessage(), true);  
		}
	}
	
	/**
	 * Test case for Insert Fixed Event
	 */
	public void testInsertFixedEvent()
	 {
		 try
		 {
			   TissueSpecimen tSpecimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();
			   SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
			   
			   StorageContainer storageContainer =  BaseTestCaseUtility.initStorageContainer();
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
		       assertTrue("FixedEventParameters sucessfully fired" + fixedEventParameters , true);
		}
		catch(Exception e)
		{
			System.out.println("SpecimenEventTestCases.testInsertFixedEvent()");
			e.printStackTrace();
			assertFalse("FixedEventParameters Failed:" +e.getMessage(), true);  
			
		}
	}
	
	/**
	 * Test case for updating Update Fixed Event
	 */
	public void testUpdateFixedEvent()
	{
		try
		{
			FixedEventParameters fixedEventParameters = (FixedEventParameters)TestCaseUtility.getObjectMap(FixedEventParameters.class);
			fixedEventParameters.setFixationType("RNALater");
			fixedEventParameters = (FixedEventParameters)appService.updateObject(fixedEventParameters);
			if("RNALater".equals(fixedEventParameters.getFixationType()))
			{
				assertTrue("Fixed Event Parameters sucessfully fired" + fixedEventParameters , true);  
			}
			else
			{
				assertFalse("Fixed Event Parameters Failed:", true);
			}
		}
		catch(Exception e)
		{
			System.out
					.println("SpecimenEventTestCases.testUpdateFixedEvent()");
			e.printStackTrace();
			assertFalse("Fixed Event Parameters Failed:" +e.getMessage(), true);  
		}
	}

}
