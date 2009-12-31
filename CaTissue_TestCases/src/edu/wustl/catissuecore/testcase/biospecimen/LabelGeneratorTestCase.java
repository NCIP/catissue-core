package edu.wustl.catissuecore.testcase.biospecimen;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.util.global.Variables;

public class LabelGeneratorTestCase extends CaTissueSuiteBaseTest
{
	public void initLabelGeneratorVariables() {
		
		Variables.isStorageContainerLabelGeneratorAvl = false;
		Variables.isSpecimenLabelGeneratorAvl = false;
		Variables.isSpecimenCollGroupLabelGeneratorAvl = false;
		Variables.isProtocolParticipantIdentifierLabelGeneratorAvl = false;	
		
		Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl = false;
		Variables.isSpecimenBarcodeGeneratorAvl = false;
		Variables.isSpecimenCollGroupBarcodeGeneratorAvl = false;
		Variables.isStorageContainerBarcodeGeneratorAvl = false;
		
	}
	
	public void testAddStorageContainerWithoutLabel()
	{
		initLabelGeneratorVariables();
		StorageType storageType = (StorageType) TestCaseUtility.getNameObjectMap("StorageType");
		
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

		String[] holdsSpecimenClassCollection = new String[4];
		holdsSpecimenClassCollection[0]="Fluid";
		
		storageContainerForm.setSpecimenOrArrayType("Specimen");
		storageContainerForm.setHoldsSpecimenClassTypes(holdsSpecimenClassCollection);
		storageContainerForm.setActivityStatus("Active");
		storageContainerForm.setIsFull("False");
		storageContainerForm.setOperation("add");
		setRequestPathInfo("/StorageContainerAdd");
		setActionForm(storageContainerForm);
		actionPerform();
		verifyForward("failure");
		String errormsg[] = new String[] {"errors.item.required"};
    	verifyActionErrors(errormsg);
    	
//    	StorageContainer storageContainer = new StorageContainer();
//    	storageContainer.setName(null);
//    	storageContainer =(StorageContainer) appService.createObject(storageContainer); 

    	
    	

	}
	
	public void testAddparticipantWithoutPPIlabel(){
		
	}
	
	
	
	
}
