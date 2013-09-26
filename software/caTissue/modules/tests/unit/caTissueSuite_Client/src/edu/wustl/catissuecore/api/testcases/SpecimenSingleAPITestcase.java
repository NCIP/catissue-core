/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedureApplication;
//import gov.nih.nci.dynext.pathology_specimen.ProstateSpecimenPathologyAnnotation;
//import gov.nih.nci.dynext.sop.EmbeddedEventParameters;
//import gov.nih.nci.dynext.sop.FrozenEventParameters;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpecimenSingleAPITestcase extends AbstractCaCoreApiTestCasesWithRegularAuthentication
{

	/*public void testInsertDEData()
	{
//		Use case 1: Create new DE data for existing specimen
		Specimen existingSpecimen = new Specimen();
		existingSpecimen.setId(3l); // set an existing specimen id
		SpecimenRecordEntry specRecEntry = new SpecimenRecordEntry();
		Set recEntrySet = new HashSet();
		recEntrySet.add(specRecEntry);
		existingSpecimen.setSpecimenRecordEntryCollection(recEntrySet);
		specRecEntry.setSpecimen(existingSpecimen);

		ProstateSpecimenPathologyAnnotation pathologyAnnotation = new ProstateSpecimenPathologyAnnotation();
		pathologyAnnotation.setComments("test");
//		Collection annotationSet = new ArrayList();
		Set annotationSet = new HashSet();
		annotationSet.add(pathologyAnnotation);
		specRecEntry.setProstateSpecimenPathologyAnnotationCollection(annotationSet);
		try {
			specRecEntry= insert(specRecEntry);
			System.out.println(specRecEntry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testAdhocEvent() 
	{
		try
		{
			Specimen specimen = new Specimen();
			
			specimen.setLabel("47");
			specimen = getSpecimen(specimen);
			
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
	//		FrozenEventParameters parameters = new FrozenEventParameters();
			EmbeddedEventParameters parameters = new EmbeddedEventParameters();
			parameters.setEmbeddingMedium("Freezing Media");
			parameters.setActionApplicationRecordEntry_EmbeddedEventParameters(entry);
			Set<EmbeddedEventParameters> eventParameters = new HashSet<EmbeddedEventParameters>();
			eventParameters.add(parameters);
			entry.setEmbeddedEventParametersCollection(eventParameters);
			entry.setActivityStatus("Active");
			entry.setModifiedBy("admin,admin");
			entry.setModifiedDate(new Date());
			
			ActionApplication application = new ActionApplication();
			entry.setActionApplication(application);
			application.setComments("dummy sf comments");
			application.setApplicationRecordEntry(entry);
			application.setReasonDeviation("tese dfg");
	//		application.setSpecimen(specimen);
			application.setTimestamp(new Date());
			User user = new User();
			user.setId(1L);
			application.setPerformedBy(user);
			Collection<ActionApplication> applications = specimen.getActionApplicationCollection();
	//		FixedEventParameters parameters = null;
	//		ActionApplicationRecordEntry entry = null;
	//		ActionApplication a = null;
	//		for (ActionApplication actionApplication : applications) 
	//		{
	//			a=actionApplication;
	//			entry = actionApplication.getApplicationRecordEntry();
	//			parameters = entry.getFixedEventParametersCollection().iterator().next();
	//			parameters.setDurationInMinutes(3l);
	//			parameters.setFixationType("aaaa");
	//		}
	//		entry.setFixedEventParametersCollection(Arrays.asList(parameters));
	//		 a.setApplicationRecordEntry(entry);
	//		specimen.setActionApplicationCollection(Arrays.asList(a));
			applications.add(application);
			specimen.setActionApplicationCollection(applications);
			update(specimen);
		}
		catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Exception while updating Specimen", true);
		}
//		application.
		
//		procedure.s
	}
	
	public void testAdhocEvent1() 
	{
		try
		{
			Specimen specimen = new Specimen();
			
			specimen.setLabel("27");
			specimen = getSpecimen(specimen);
			
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
	//		FrozenEventParameters parameters = new FrozenEventParameters();
			EmbeddedEventParameters parameters = new EmbeddedEventParameters();
			parameters.setEmbeddingMedium("Freezing Media");
			parameters.setActionApplicationRecordEntry_EmbeddedEventParameters(entry);
			Set<EmbeddedEventParameters> eventParameters = new HashSet<EmbeddedEventParameters>();
			eventParameters.add(parameters);
			entry.setEmbeddedEventParametersCollection(eventParameters);
			entry.setActivityStatus("Active");
			entry.setModifiedBy("admin,admin");
			entry.setModifiedDate(new Date());
			
			ActionApplication application = new ActionApplication();
			entry.setActionApplication(application);
			application.setComments("dummy sf comments");
			application.setApplicationRecordEntry(entry);
			application.setReasonDeviation("tese dfg");
	//		application.setSpecimen(specimen);
			application.setTimestamp(new Date());
			User user = new User();
			user.setId(1L);
			application.setPerformedBy(user);
			Set<ActionApplication> applications = new HashSet<ActionApplication>();
			applications.add(application);
			specimen.setActionApplicationCollection(applications);
			update(specimen);
		}
		catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Exception while updating Specimen", true);
		}
	}
	public void testSPP()
	{
		try
		{
			Specimen specimen = new Specimen();
	//		specimen.setCreationEventAction(null);
			
			specimen.setLabel("31");
	//		specimen.setId(19l);
			
			
			specimen = getSpecimen(specimen);
			specimen.setComment("tgtftdahavshgd");
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
			FrozenEventParameters parameters = new FrozenEventParameters();
	//		EmbeddedEventParameters parameters = new EmbeddedEventParameters();
			parameters.setMethod("Aerosol Spray");
			parameters.setActionApplicationRecordEntry_FrozenEventParameters(entry);
			Set<FrozenEventParameters> eventParameters = new HashSet<FrozenEventParameters>();
			eventParameters.add(parameters);
			entry.setFrozenEventParametersCollection(eventParameters);
			entry.setActivityStatus("Active");
			entry.setModifiedBy("admin,admin");
			entry.setModifiedDate(new Date());
			
			ActionApplication application = new ActionApplication();
	//		application.setSpecimen(specimen);
			entry.setActionApplication(application);
			SpecimenProcessingProcedureApplication sppApplication = specimen.getProcessingSPPApplication();
			
	//		sppApplication.setId(60l);
	//		application.setSppApplication(sppApplication);
			SpecimenProcessingProcedure spp = new SpecimenProcessingProcedure();
			spp.setId(1l);
			sppApplication.setSpp(spp);
	//		specimen.setProcessingSPPApplication(sppApplication);
			application.setComments("dummy sf comments");
			application.setApplicationRecordEntry(entry);
			application.setReasonDeviation("tese dfg");
	//		application.setSpecimen(specimen);
			application.setTimestamp(new Date());
			User user = new User();
			user.setId(1L);
			application.setPerformedBy(user);
			Collection<ActionApplication> applications = sppApplication.getSppActionApplicationCollection();
			applications.add(application);
			System.out.println(specimen.getProcessingSPPApplication().getSppActionApplicationCollection().size());
			sppApplication.setSppActionApplicationCollection(applications);
			specimen.setProcessingSPPApplication(sppApplication);
	//		sppApplication.setSppActionApplicationCollection(applications);
			System.out.println(specimen.getProcessingSPPApplication().getSppActionApplicationCollection().size());
			update(specimen);
		}
		catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Exception while updating Specimen", true);
		}
	}
	
	public void testFrozenSPP()
	{
		try
		{
			Specimen specimen = new Specimen();
			specimen.setCreationEventAction(null);
			
			specimen.setLabel("24");
	//		specimen.setId(19l);
			
			
			specimen = getSpecimen(specimen);
			ActionApplicationRecordEntry entry = new ActionApplicationRecordEntry();
			FrozenEventParameters parameters = new FrozenEventParameters();
	//		EmbeddedEventParameters parameters = new EmbeddedEventParameters();
			parameters.setMethod("Aerosol Spray");
			parameters.setActionApplicationRecordEntry_FrozenEventParameters(entry);
			Set<FrozenEventParameters> eventParameters = new HashSet<FrozenEventParameters>();
			eventParameters.add(parameters);
			entry.setFrozenEventParametersCollection(eventParameters);
			entry.setActivityStatus("Active");
			entry.setModifiedBy("admin,admin");
			entry.setModifiedDate(new Date());
			
			ActionApplication application = new ActionApplication();
	//		application.setSpecimen(specimen);
			entry.setActionApplication(application);
			SpecimenProcessingProcedureApplication sppApplication = new SpecimenProcessingProcedureApplication();
			sppApplication.setId(50l);
	//		application.setSppApplication(sppApplication);
			SpecimenProcessingProcedure spp = new SpecimenProcessingProcedure();
			spp.setId(1l);
			sppApplication.setSpp(spp);
			specimen.setProcessingSPPApplication(sppApplication);
			application.setComments("dummy sf comments");
			application.setApplicationRecordEntry(entry);
			application.setReasonDeviation("tese dfg");
	//		application.setSpecimen(specimen);
			application.setTimestamp(new Date());
			User user = new User();
			user.setId(1L);
			application.setPerformedBy(user);
			Set<ActionApplication> applications = new HashSet<ActionApplication>();
			applications.add(application);
	//		sppApplication.setSppActionApplicationCollection(applications);
			
			update(specimen);
		}
		catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Exception while updating Specimen", true);
		}
	}
	private Specimen getSpecimen(Specimen specimen) throws ApplicationException {
		ApplicationService appService = getApplicationService();
		Collection<Specimen> specColl = appService.search(Specimen.class, specimen);
		specimen = specColl.iterator().next();
		
		return specimen;
	}*/
}
