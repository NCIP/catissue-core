package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;


public class BulkTransferTestCases extends CaTissueSuiteBaseTest
{
	public void testBulkTransferEventOfSpecimen()
	{
		Specimen specimenObj = (Specimen) TestCaseUtility.getObjectMap(TissueSpecimen.class);
		System.out.println("specimenObj "+specimenObj);
		StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
		StorageType storageType = BaseTestCaseUtility.initTissueStorageType();
		try
		{
			storageType = (StorageType) appService.createObject(storageType);
		}
		catch (Exception e)
		{
			assertFalse(e.getMessage(), true);
		}
		storageContainer.setStorageType( storageType );
		try
		{
			storageContainer = (StorageContainer)appService.createObject(storageContainer);
		}
		catch (Exception e)
		{
			assertFalse(e.getMessage(), true);
		}
		System.out.println("storageContainer************ "+storageContainer.getName()+"  "+storageContainer.getId());
		if(!specimenObj.getCollectionStatus().equals( "Collected" ))
		{
			specimenObj.setCollectionStatus( "Collected" );
			SpecimenPosition specimenPosition = new SpecimenPosition();
			specimenPosition.setStorageContainer( storageContainer );
			specimenPosition.setPositionDimensionOne( 1 );
			specimenPosition.setPositionDimensionTwo( 1 );
			System.out.println("before updating parent");
			try
			{
				specimenObj = (TissueSpecimen) appService.updateObject( specimenObj );
			}
			catch (Exception e)
			{
				System.out.println("in parent exception");
				assertFalse(e.getMessage(), true);
			}			
		}
		SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getObjectMap(SpecimenCollectionGroup.class);
		System.out.println("scg "+scg.getId());
		TissueSpecimen childSpecimen1 = (TissueSpecimen)BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen1.setParentSpecimen(specimenObj);
		childSpecimen1.setLabel("child1_"+ UniqueKeyGeneratorUtil.getUniqueKey());
		childSpecimen1.setBarcode( "child1_barcode_"+ UniqueKeyGeneratorUtil.getUniqueKey());
		childSpecimen1.setLineage("Aliquot");
		childSpecimen1.setCollectionStatus( "Collected" );
		SpecimenPosition child1Position = new SpecimenPosition();
		child1Position.setStorageContainer( storageContainer );
		child1Position.setPositionDimensionOne( 1 );
		child1Position.setPositionDimensionTwo( 2 );
		childSpecimen1.setSpecimenPosition( child1Position );
		childSpecimen1.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
		System.out.println("Befor creating child1 Tissue Specimen");
		try
		{
			childSpecimen1 = (TissueSpecimen) appService.createObject(childSpecimen1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		TissueSpecimen childSpecimen2 = (TissueSpecimen)BaseTestCaseUtility.initTissueSpecimen();
		childSpecimen2.setParentSpecimen(specimenObj);
		childSpecimen2.setLabel("child2_"+ UniqueKeyGeneratorUtil.getUniqueKey());
		childSpecimen2.setBarcode( "child2_barcode_"+ UniqueKeyGeneratorUtil.getUniqueKey());
		childSpecimen2.setLineage("Aliquot");
		childSpecimen2.setCollectionStatus( "Collected" );
		SpecimenPosition specimenPosition2 = new SpecimenPosition();
		specimenPosition2.setStorageContainer( storageContainer );
		SpecimenPosition child2Position = new SpecimenPosition();
		child2Position.setStorageContainer( storageContainer );
		child2Position.setPositionDimensionOne( 1 );
		child2Position.setPositionDimensionTwo( 3 );
		childSpecimen2.setSpecimenPosition( child2Position );
		childSpecimen2.setSpecimenCollectionGroup(scg);//bug 12073 and 12074
		try
		{
			childSpecimen2 = (TissueSpecimen) appService.createObject(childSpecimen2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		List<String> specimenIds = new LinkedList<String>();
		specimenIds.add( ""+ childSpecimen1.getId());
		specimenIds.add( ""+ childSpecimen2.getId());
				
		setRequestPathInfo("/BulkTransferEvents");
		BulkEventOperationsForm bulkEventOperationsForm = new BulkEventOperationsForm();
		bulkEventOperationsForm.setOperation( "bulkTransfers" );
		setActionForm(bulkEventOperationsForm);
		getRequest().setAttribute( Constants.SPECIMEN_ID, specimenIds );
		getRequest().setAttribute( Constants.OPERATION, "bulkTransfers" );
		actionPerform();
		verifyForward(Constants.SUCCESS);
		verifyNoActionErrors();
		
		setRequestPathInfo("/BulkTransferEventsSubmit");
		BulkEventOperationsForm bulkEventOperationsForm1 = (BulkEventOperationsForm)getActionForm();
		bulkEventOperationsForm1.setOperation( "bulkTransfers" );
		LinkedHashMap<String, String> eventSpecificData = bulkEventOperationsForm1.getEventSpecificData();
		eventSpecificData.put( "ID_" + specimenIds.get( 0 ) + "_TOSCLABEL", storageContainer.getName() );
		eventSpecificData.put( "ID_"+ specimenIds.get( 0 ) +"_TOSCPOS1", "2" );
		eventSpecificData.put( "ID_"+ specimenIds.get( 0 ) +"_TOSCPOS2",  "2");
		for(int i=1;i<specimenIds.size();i++)
		{
			bulkEventOperationsForm1.setFieldValue("ID_" + specimenIds.get( i ) + "_TOSCLABEL", storageContainer.getName() );
			eventSpecificData.put( "ID_" + specimenIds.get( i ) + "_TOSCLABEL", storageContainer.getName() );			
		}
		
		actionPerform();
		System.out.println("child1 ***** "+childSpecimen1.getLabel());
		verifyForward("bulkTransfers");
		verifyNoActionErrors();	
	}
}
