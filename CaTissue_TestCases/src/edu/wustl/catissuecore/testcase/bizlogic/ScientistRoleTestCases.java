package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class ScientistRoleTestCases extends CaTissueBaseTestCase 
{
 static ApplicationService appService = null;
 public void setUp()
 {
	appService = ApplicationServiceProvider.getApplicationService();
	ClientSession cs = ClientSession.getInstance();
	//System.setProperty("javax.net.ssl.trustStore", "E://jboss//server//default//conf//chap8.keystore");
	try
	{ 
		cs.startSession("scientist@admin.com", "Test123");
	} 	
				
	catch (Exception ex) 
	{ 
		System.out.println(ex.getMessage()); 
		ex.printStackTrace();
		fail("Fail to create connection");
		System.exit(1);
	}
}
 
 /**
  * Search all participant and check if PHI data is visible
  */ 
  public void testSearchParticipantWithScientistLogin()
  {
	try
	{
		Participant participant = new Participant();
		List parList = appService.search(Participant.class.getName(), participant);
		System.out.println("Size : "+parList.size());
		for(int i=0;i<parList.size();i++)
		{
			Participant retutnParticpant = (Participant)parList.get(i);
			if(retutnParticpant.getFirstName()!=null||retutnParticpant.getLastName()!=null||
					retutnParticpant.getMiddleName()!=null||retutnParticpant.getBirthDate()!=null||
					retutnParticpant.getSocialSecurityNumber()!=null)
			{
				fail("Participant PHI data is visible to scientist");
			}
			System.out.println("Participant First name :"+retutnParticpant.getFirstName());
			System.out.println("Participant Last name :"+retutnParticpant.getLastName());
			System.out.println("Participant Middle Name:"+retutnParticpant.getMiddleName());
			System.out.println("Participant SSN no:"+retutnParticpant.getSocialSecurityNumber());
			
			Collection<ParticipantMedicalIdentifier> pmiCollection 
				= retutnParticpant.getParticipantMedicalIdentifierCollection();
			for (Iterator<ParticipantMedicalIdentifier> iterator = pmiCollection.iterator();iterator.hasNext();)
		    {
		        ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) iterator.next();
		        if(participantMedId.getMedicalRecordNumber()!=null)
		        {
		        	fail("Participant PHI data is visible to scientist");
		        }
		        System.out.println("Participant->PMI MedicalRecordNumber:"+participantMedId.getMedicalRecordNumber());
		    }
			
		    Collection<CollectionProtocolRegistration> cpCollection 
		    				= retutnParticpant.getCollectionProtocolRegistrationCollection();
		    for (Iterator<CollectionProtocolRegistration> iterator=cpCollection.iterator();iterator.hasNext();)
		    {
		        CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) iterator.next();
		        if(cpr.getRegistrationDate()!=null || cpr.getConsentSignatureDate()!=null ||
		        		cpr.getSignedConsentDocumentURL()!=null)
		        {
		        	fail("Participant PHI data is visible to scientist");
		        }
		        System.out.println("Participant->CPR->RegistrationDate :"+ cpr.getRegistrationDate());
		        System.out.println("Participant->CPR->ConsentSignatureDate :"+ cpr.getConsentSignatureDate());
		        System.out.println("Participant->CPR->SignedConsentDocumentURL:"+ cpr.getSignedConsentDocumentURL());
		    }
		}
	 }
	 catch(Exception e){
		 
	     System.out
				.println("ScientistRoleTestCases.testSearchParticipantWithScientistLogin()"+e.getMessage());
		 e.printStackTrace();
		 assertFalse(e.getMessage(), true);
	 }
  }
  /**
   * Search all PMI and check if PHI data is visible
   */
  public void testSearchPMIWithScientistLogin()
  {
	  try{
			ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
			List pmiList = appService.search(ParticipantMedicalIdentifier.class.getName(), pmi);
			for(int i=0;i<pmiList.size();i++)
			{
				 ParticipantMedicalIdentifier participantMedId = (ParticipantMedicalIdentifier) pmiList.get(i);
			     if(participantMedId.getMedicalRecordNumber()!=null)
			     {
			        	fail("ParticipantMedicalIdentifier PHI data is visible to scientist");
			     }
			     System.out.println("PMI->MedicalRecordNumber:"+participantMedId.getMedicalRecordNumber());
			}
		 }
		 catch(Exception e)
		 {
			 System.out
				.println("ScientistRoleTestCases.testSearchPMIWithScientistLogin():"+e.getMessage());	
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
  }
  /**
	  * Search all CPR and check if PHI data is visible
	  *
	  */ 
	  public void testSearchProtocolRegistrationWithScientistLogin()
	  {
		try
		{
			CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
			List cprList = appService.search(CollectionProtocolRegistration.class.getName(), cpr);
			System.out.println("Size : "+cprList.size());
			for(int i=0;i<cprList.size();i++)
			{
				CollectionProtocolRegistration returnedReg = (CollectionProtocolRegistration)cprList.get(i);
				if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
						returnedReg.getConsentSignatureDate()!=null)
				{
					fail("CollectionProtocolRegistration PHI data is visible to scientist");
				}
				 System.out.println("CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
			     System.out.println("CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
			     System.out.println("CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
			}
		 }
		 catch(Exception e){
			 System.out
					.println("ScientistRoleTestCases.testSearchProtocolRegistrationWithScientistLogin() "+e.getMessage());
		     Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	  }
  /**
	  * Search all SCG and check if PHI data is visible
	  *
	  */ 
	  public void testSearchSpecimenCollectionGroupWithScientistLogin()
	  {
		try{
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			List scgList = appService.search(SpecimenCollectionGroup.class.getName(), scg);
			System.out.println("Size : "+scgList.size());
			for(int i=0;i<scgList.size();i++)
			{
				SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup)scgList.get(i);
				if(returnedSCG.getSurgicalPathologyNumber()!=null)
				{
					fail("SpecimenCollectionGroup PHI data is visible to scientist");
				}
				System.out.println("SCG->SurgicalPathologyNumber : "+returnedSCG.getSurgicalPathologyNumber());
				CollectionProtocolRegistration returnedReg = returnedSCG.getCollectionProtocolRegistration();
				if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
						returnedReg.getConsentSignatureDate()!=null)
				{
					fail("SpecimenCollectionGroup PHI data is visible to scientist");
				}
				System.out.println("SCG->CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
			    System.out.println("SCG->CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
			    System.out.println("SCG->CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
			     
				Collection<SpecimenEventParameters> spEvent = returnedSCG.getSpecimenEventParametersCollection();
			    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
			    while(eveItr.hasNext())
			    {
			    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
			    	if(spEventParam.getTimestamp()!=null)
			    	{
			    		fail("SpecimenCollectionGroup PHI data is visible to scientist");
			    	}
			    }
			}
		 }
		 catch(Exception e){
			 System.out
					.println("ScientistRoleTestCases.testSearchSpecimenCollectionGroupWithScientistLogin() "+e.getMessage());
		     Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertFalse(e.getMessage(), true);
		 }
	  }
	  /**
	   * Test search Tissue specimen and check for PHI data
	   */
	  public void testSearchTissueSpecimenWithScientistLogin()
	  {
			try
			{
				TissueSpecimen tissueSp = new TissueSpecimen();
				List<TissueSpecimen> spCollection = appService.search(TissueSpecimen.class, tissueSp);
				Iterator<TissueSpecimen> itr = spCollection.iterator();
				while(itr.hasNext())
				{
					TissueSpecimen spe = (TissueSpecimen)itr.next();
					if(spe.getCreatedOn()!=null)
					{
						fail("TissueSpecimen PHI data is visible to scientist");
					}
					System.out.println("TissueSpecimen->CreatedOn:"+spe.getCreatedOn());
				
					Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
				    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
				    while(eveItr.hasNext())
				    {
				    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
				    	if(spEventParam.getTimestamp()!=null)
				    	{
							fail("TissueSpecimen PHI data is visible to scientist");
				    	}
				    	System.out.println("TissueSpecimen TimeStamp :"+spEventParam.getTimestamp());
				    }
				    
				    if(spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
					{
						fail("TissueSpecimen->SCG PHI data is visible to scientist");
					}
				    System.out.println("TissueSpecimen->SCG->SPN:"+spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
					CollectionProtocolRegistration returnedReg = spe.getSpecimenCollectionGroup().getCollectionProtocolRegistration();
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("TissueSpecimen->SCG->CPR PHI data is visible to scientist");
					}
					 System.out.println("TissueSpecimen->SCG->CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
				     System.out.println("TissueSpecimen->SCG->CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
				     System.out.println("TissueSpecimen->SCG->CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchTissueSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
		/**
		 * Test Search Molecular Specimen and test for PHI data 
		 */
	  public void testSearchMolecularSpecimenWithScientistLogin()
	  {
			try
			{
				MolecularSpecimen moleSp = new MolecularSpecimen();
				List<MolecularSpecimen> spCollection = appService.search(MolecularSpecimen.class, moleSp);
				Iterator<MolecularSpecimen> itr = spCollection.iterator();
				while(itr.hasNext())
				{
					MolecularSpecimen spe = (MolecularSpecimen)itr.next();
					if(spe.getCreatedOn()!=null)
					{
						fail("MolecularSpecimen PHI data is visible to scientist");
					}
					System.out.println("MolecularSpecimen Created on :"+spe.getCreatedOn());
					Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
				    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
				    while(eveItr.hasNext())
				    {
				    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
				    	if(spEventParam.getTimestamp()!=null)
				    	{
							fail("MolecularSpecimen PHI data is visible to scientist");
				    	}
				    	System.out.println("TissueSpecimen TimeStamp :"+spEventParam.getTimestamp());
				    }
				    if(spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
					{
						fail("MolecularSpecimen->SCG PHI data is visible to scientist");
					}
				    System.out.println("MolecularSpecimen->SCG->SPN:"+spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
					CollectionProtocolRegistration returnedReg = spe.getSpecimenCollectionGroup().getCollectionProtocolRegistration();
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("MolecularSpecimen->SCG->CPR PHI data is visible to scientist");
					}
					System.out.println("MolecularSpecimen->SCG->CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
				    System.out.println("MolecularSpecimen->SCG->CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
				    System.out.println("MolecularSpecimen->SCG->CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchMolecularSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
	  /**
		* Test Search Cell Specimen and test for PHI data 
		*/
	  public void testSearchCellSpecimenWithScientistLogin()
	  {
			try
			{
				CellSpecimen cellSp = new CellSpecimen();
				List<CellSpecimen> spCollection = appService.search(CellSpecimen.class, cellSp);
				Iterator<CellSpecimen> itr = spCollection.iterator();
				while(itr.hasNext())
				{
					CellSpecimen spe = (CellSpecimen)itr.next();
					if(spe.getCreatedOn()!=null)
					{
						fail("CellSpecimen PHI data is visible to scientist");
					}
					System.out.println("CellSpecimen Created on :"+spe.getCreatedOn());
					Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
				    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
				    while(eveItr.hasNext())
				    {
				    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
				    	if(spEventParam.getTimestamp()!=null)
				    	{
							fail("CellSpecimen PHI data is visible to scientist");
				    	}
				    	System.out.println("CellSpecimen TimeStamp :"+spEventParam.getTimestamp());
				    }
				    if(spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
					{
						fail("CellSpecimen->SCG PHI data is visible to scientist");
					}
				    System.out.println("CellSpecimen->SCG->SPN:"+spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
					CollectionProtocolRegistration returnedReg = spe.getSpecimenCollectionGroup().getCollectionProtocolRegistration();
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("CellSpecimen->SCG->CPR PHI data is visible to scientist");
					}
					System.out.println("CellSpecimen->SCG->CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
				    System.out.println("CellSpecimen->SCG->CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
				    System.out.println("CellSpecimen->SCG->CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchCellSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
	  /**
		 * Test Search Fluid Specimen and test for PHI data 
		 */
	  public void testSearchFluidSpecimenWithScientistLogin()
	  {
			try
			{
				FluidSpecimen fluidSp = new FluidSpecimen();
				List<FluidSpecimen> spCollection = appService.search(FluidSpecimen.class, fluidSp);
				Iterator<FluidSpecimen> itr = spCollection.iterator();
				while(itr.hasNext())
				{
					FluidSpecimen spe = (FluidSpecimen)itr.next();
					if(spe.getCreatedOn()!=null)
					{
						fail("FluidSpecimen PHI data is visible to scientist");
					}
					System.out.println("FluidSpecimen Created on :"+spe.getCreatedOn());
					Collection<SpecimenEventParameters> spEvent = spe.getSpecimenEventCollection();
				    Iterator<SpecimenEventParameters> eveItr = spEvent.iterator();
				    while(eveItr.hasNext())
				    {
				    	SpecimenEventParameters spEventParam = (SpecimenEventParameters)eveItr.next();
				    	if(spEventParam.getTimestamp()!=null)
				    	{
							fail("FluidSpecimen PHI data is visible to scientist");
				    	}
				    	System.out.println("FluidSpecimen TimeStamp :"+spEventParam.getTimestamp());
				    }
				    if(spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber()!=null)
					{
						fail("FluidSpecimen->SCG PHI data is visible to scientist");
					}
				    System.out.println("FluidSpecimen->SCG->SPN:"+spe.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
					CollectionProtocolRegistration returnedReg = spe.getSpecimenCollectionGroup().getCollectionProtocolRegistration();
					if(returnedReg.getRegistrationDate()!=null||returnedReg.getSignedConsentDocumentURL()!=null||
							returnedReg.getConsentSignatureDate()!=null)
					{
						fail("FluidSpecimen->SCG->CPR PHI data is visible to scientist");
					}
					System.out.println("FluidSpecimen->SCG->CPR->RegistrationDate :"+ returnedReg.getRegistrationDate());
				    System.out.println("FluidSpecimen->SCG->CPR->ConsentSignatureDate :"+ returnedReg.getConsentSignatureDate());
				    System.out.println("FluidSpecimen->SCG->CPR->SignedConsentDocumentURL:"+ returnedReg.getSignedConsentDocumentURL());
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchFluidSpecimenWithScientistLogin() "+e.getMessage());
			     Logger.out.error(e.getMessage(),e);
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
	
	  
		/**
		 * Test search SpecimenArrayContent and test for PHI data
		 */
		public void testSpecimenArrayContentWithScientistLogin()
		{
			try
			{
				SpecimenArrayContent sac = new SpecimenArrayContent();
				List sacCollection = appService.search(SpecimenArrayContent.class,sac);
				System.out.println("Total SpecimenArrayContent Count:"+sacCollection.size());
				Iterator itr = sacCollection.iterator();
				while(itr.hasNext())
				{
					SpecimenArrayContent spe = (SpecimenArrayContent)itr.next();
					if(spe.getSpecimen().getCreatedOn()!=null)
					{
						fail("SpecimenArrayContent ->Specimen PHI data is visible to scientist");
					}
					System.out.println("SpecimenArrayContent->Specimen Created on :"+spe.getSpecimen().getCreatedOn());
				}
			}
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSpecimenArrayContentWithScientistLogin()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
			
		}
		
		/**
		 * Test search for ReceivedEventParameters and test for PHI data
		 */
		public void testSearchReceivedEventParameters()
		{
			try
			{
				ReceivedEventParameters recEvParam = new ReceivedEventParameters();
				List recColl = appService.search(ReceivedEventParameters.class, recEvParam);
				Iterator itr = recColl.iterator();
				while(itr.hasNext())
				{
					ReceivedEventParameters rec = (ReceivedEventParameters)itr.next();
					if(rec.getTimestamp()!=null)
					{
						fail("ReceivedEventParameters PHI data is visible to scientist");
					}
					System.out.println("ReceivedEventParameters:TimeStamp:"+rec.getTimestamp());
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}
		}
		/**
		 * Test search for CollectionEventParameters and test for PHI data
		 */
		public void testSearchCollectionEventParameters()
		{
			try
			{
				CollectionEventParameters colEvParam = new CollectionEventParameters();
				List collEveColl = appService.search(CollectionEventParameters.class, colEvParam);
				Iterator itr = collEveColl.iterator();
				while(itr.hasNext())
				{
					CollectionEventParameters cep = (CollectionEventParameters)itr.next();
					if(cep.getTimestamp()!=null)
					{
						fail("CollectionEventParameters PHI data is visible to scientist");
					}
					System.out.println("CollectionEventParameters:Timestamp:"+cep.getTimestamp());
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testSearchReceivedEventParameters()"+e.getMessage());
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}
		}
		/**
		 * Test search for PathologyReportReviewParameter and test for PHI data
		 */
		public void testsearchMoleEventparam()
		{
			try
			{
				MolecularSpecimenReviewParameters molSpRev = new MolecularSpecimenReviewParameters();
				List recColl = appService.search(MolecularSpecimenReviewParameters.class, molSpRev);
				Iterator itr = recColl.iterator();
				while(itr.hasNext())
				{
					MolecularSpecimenReviewParameters mol = (MolecularSpecimenReviewParameters)itr.next();
					if(mol.getTimestamp()!=null)
					{
						fail("MolecularSpecimenReviewParameters PHI data is visible to scientist");
					}
					System.out.println("MolecularSpecimenReviewParameters :Timestamp : " + mol.getTimestamp());
				}
			} 
			catch(Exception e)
			{
				System.out
						.println("ScientistRoleTestCases.testsearchMoleEventparam()"+e.getMessage());
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}
		}
		
		/**
		 * Test search for DeidentifiedSurgicalPathologyReport and test for PHI data
		 */
		public void testSearchDeidentifiedSurgicalPathologyReport()
		{
			try
			{
				DeidentifiedSurgicalPathologyReport spr = new DeidentifiedSurgicalPathologyReport();
				List spCollection = appService.search(DeidentifiedSurgicalPathologyReport.class, spr);
				Iterator itr = spCollection.iterator();
				while(itr.hasNext())
				{
					DeidentifiedSurgicalPathologyReport deid = (DeidentifiedSurgicalPathologyReport)itr.next();
					if(deid.getCollectionDateTime()!=null || deid.getId()!=null
							|| deid.getActivityStatus()!=null||deid.getIsFlagForReview()!=null)
					{
						fail("DeIdentifiedSurgicalPathologyReport PHI data is visible to scientist");
					}
					System.out.println("DeidentifiedSurgicalPathologyReport->TimeStamp = "+deid.getCollectionDateTime());
					System.out.println("DeidentifiedSurgicalPathologyReport->Identifier = "+deid.getId());
					System.out.println("DeidentifiedSurgicalPathologyReport->ActivityStatus = "+deid.getActivityStatus());
					System.out.println("DeidentifiedSurgicalPathologyReport->IsFlagForReview = "+deid.getIsFlagForReview());
				}
			 }
			 catch(Exception e)
			 {
				 System.out
						.println("ScientistRoleTestCases.testSearchDeidentifiedSurgicalPathologyReport()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
		/**
		 * Test search for IdentifiedSurgicalPathologyReport and test for PHI data
		 */
		public void testSearchIdentifiedSurgicalPathologyReport()
		{
			try
			{
				IdentifiedSurgicalPathologyReport spr = new IdentifiedSurgicalPathologyReport();
				List spCollection = appService.search(IdentifiedSurgicalPathologyReport.class, spr);
				Iterator itr = spCollection.iterator();
				while(itr.hasNext())
				{
					IdentifiedSurgicalPathologyReport ispr = (IdentifiedSurgicalPathologyReport)itr.next();
					if(ispr.getCollectionDateTime()!=null || ispr.getTextContent().getData()!= null||
							ispr.getId()!=null || ispr.getActivityStatus()!=null)
					{
						fail("IdentifiedSurgicalPathologyReport PHI data is visible to scientist");
					}
					System.out.println("IdentifiedSurgicalPathologyReport->TimeStamp ="+ispr.getCollectionDateTime());
					System.out.println("IdentifiedSurgicalPathologyReport->Data = "+ispr.getTextContent().getData());
					System.out.println("IdentifiedSurgicalPathologyReport->Identifier = "+ispr.getId());
					System.out.println("IdentifiedSurgicalPathologyReport->ActivityStatus = "+ispr.getActivityStatus());
				}
			 }
			 catch(Exception e)
			 {
				System.out
						.println("ScientistRoleTestCases.testSearchIdentifiedSurgicalPathologyReport()"+e.getMessage());
				 e.printStackTrace();
				 assertFalse(e.getMessage(), true);
			 }
		}
				
	}
