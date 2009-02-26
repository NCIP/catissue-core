package src.edu.wustl.catissuecore.testcase;

import java.util.Map;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
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
	 * Test Specimen Label and barcode after storage position change.
	 */
	@Test
	public void testLabelandBarcodeonStoragePositionChange()
	{
        
		Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
		setRequestPathInfo("/NewSpecimen");		
        addRequestParameter("operation", "edit");
        addRequestParameter("id", "" + specimen.getId());
        addRequestParameter("label", specimen.getLabel() + "123");
        addRequestParameter("barcode", specimen.getBarcode() + "123");
        addRequestParameter("stContSelection", "2");
        
        SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
        addRequestParameter("specimenCollectionGroupId", "" + specimenCollectionGroup.getId());
        addRequestParameter("pageOf", "pageOfNewSpecimenCPQuery");
        actionPerform();
        verifyForward("pageOfNewSpecimenCPQuery");
        NewSpecimenForm form= (NewSpecimenForm) getActionForm();
        specimen.setLabel( specimen.getLabel() + "123");
        specimen.setBarcode(specimen.getBarcode() + "123");
        assertEquals(specimen.getLabel(), form.getLabel());
        assertEquals(specimen.getBarcode(),form.getBarcode());
       
        TestCaseUtility.setNameObjectMap("Specimen",specimen);
	}
}
