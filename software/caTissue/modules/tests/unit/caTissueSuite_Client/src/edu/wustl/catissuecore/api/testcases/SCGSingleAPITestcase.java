package edu.wustl.catissuecore.api.testcases;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
//import gov.nih.nci.dynext.sop.ProcedureEventParameters;
//import gov.nih.nci.dynext.sop.SpunEventParameters;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

public class SCGSingleAPITestcase extends AbstractCaCoreApiTestCasesWithRegularAuthentication
{
	
	/*public void testScgUpdateWithSPPData()
	{
		try
		{
			SpecimenCollectionGroup group = new SpecimenCollectionGroup();
			group.setName("spp_test_33_496");
			ApplicationService appService = getApplicationService();
			Collection<SpecimenCollectionGroup> specColl = appService.search(SpecimenCollectionGroup.class, group);
			group = specColl.iterator().next();
			SpecimenProcessingProcedureApplication application = new SpecimenProcessingProcedureApplication();
			ActionApplication actionApplication = new ActionApplication();
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
			entry.setActivityStatus("Active");
			entry.setModifiedDate(new Date());
			SpunEventParameters spunEventParameters = new SpunEventParameters();
			spunEventParameters.setDurationInMinutes(22l);
			spunEventParameters.setGravityForce(2d);
			spunEventParameters.setActionApplicationRecordEntry_SpunEventParameters(entry);
			entry.setSpunEventParametersCollection(new HashSet(java.util.Arrays.asList(spunEventParameters)));
			actionApplication.setApplicationRecordEntry(entry);
			SpecimenProcessingProcedure spp = new SpecimenProcessingProcedure();
			spp.setId(2l);
			application.setSpp(spp);
			application.setSppActionApplicationCollection(new HashSet(java.util.Arrays.asList(actionApplication)));
			actionApplication.setSppApplication(application);
			group.setSppApplicationCollection(new HashSet(java.util.Arrays.asList(application)));
			update(group);
		}
		catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Exception while updating SCG", true);
		}
		
		
		
	}
	
	public void testScgUpdateWithMultipleSPPData()
	{
		try
		{
			SpecimenCollectionGroup group = new SpecimenCollectionGroup();
			group.setName("spun_6_8");
			ApplicationService appService = getApplicationService();
			Collection<SpecimenCollectionGroup> specColl = appService.search(SpecimenCollectionGroup.class, group);
			group = specColl.iterator().next();
			group.setCollectionStatus("Complete");
			SpecimenProcessingProcedureApplication application = new SpecimenProcessingProcedureApplication();
			ActionApplication actionApplication = new ActionApplication();
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
			entry.setActivityStatus("Active");
			entry.setModifiedDate(new Date());
			SpunEventParameters spunEventParameters = new SpunEventParameters();
			spunEventParameters.setDurationInMinutes(22l);
			spunEventParameters.setGravityForce(2d);
			spunEventParameters.setActionApplicationRecordEntry_SpunEventParameters(entry);
			entry.setSpunEventParametersCollection(new HashSet(java.util.Arrays.asList(spunEventParameters)));
			actionApplication.setApplicationRecordEntry(entry);
			SpecimenProcessingProcedure spp = new SpecimenProcessingProcedure();
			spp.setId(2l);
			application.setSpp(spp);
			application.setSppActionApplicationCollection(new HashSet(java.util.Arrays.asList(actionApplication)));
			actionApplication.setSppApplication(application);
			
			ActionApplication secActionApplication = new ActionApplication();
			ActionApplicationRecordEntry secEntry = new ActionApplicationRecordEntry();
			secEntry.setActivityStatus("Active");
			secEntry.setModifiedDate(new Date());
			ProcedureEventParameters parameters = new ProcedureEventParameters();
			parameters.setName("testProcedureEventParameters");
			parameters.setUrl("ProcedureEventParameters");
			parameters.setActionApplicationRecordEntry_ProcedureEventParameters(secEntry);
			secEntry.setProcedureEventParametersCollection(new HashSet(java.util.Arrays.asList(parameters)));
			secActionApplication.setApplicationRecordEntry(secEntry);
			application.getSppActionApplicationCollection().add(secActionApplication);
	//		secSPPApp.setSppActionApplicationCollection(new HashSet(java.util.Arrays.asList(secActionApplication)));
			secActionApplication.setSppApplication(application);
			
			
			
			group.setSppApplicationCollection(new HashSet(java.util.Arrays.asList(application)));
			update(group);
		}
	catch (ApplicationException e) {
		e.printStackTrace();
		assertFalse("Exception while updating SCG", true);
	}
		
		
		
	}*/
}
