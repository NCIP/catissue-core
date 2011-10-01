package edu.wustl.catissuecore.testcase.bizlogic;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.ReportSection;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

public class CsmTestData extends CaTissueSuiteBaseTest{
	
	
	public void testAddSCGsUnderAllCPsWithAdminstratorLogin()
	{
		createSCGUnderCPWithAllowReadPrivilege();
		createSCGUnderCPWithDisallowReadPrivilege();
		createSCGUnderCPHavingScientistAsPI();
	}
	
	private void createSCGUnderCPWithAllowReadPrivilege()
	{
	 try {
			CollectionProtocol cp= new CollectionProtocol();
		    cp.setId(new Long(BizTestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
		    Participant participant = BaseTestCaseUtility.initParticipant();
		    participant.setSocialSecurityNumber("111-22-1232");
		    try
	        {
				participant.setBirthDate(Utility.parseDate("05-02-1984", CommonServiceLocator.getInstance().getDatePattern()));
	        }
	        catch (ParseException e)
	        {
	              Logger.out.debug(""+e);
	        }
			System.out.println("Participant"+participant);
			try{
				participant = (Participant) appService.createObject(participant);
			}
			catch(Exception e){
		       	e.printStackTrace();
	            assertFalse(e.getMessage(), true);
			}
			BizTestCaseUtility.setNameObjectMap("ParticipantUnderCPWithAllowUsePriv", participant);
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			
			collectionProtocolRegistration.setCollectionProtocol(cp);
			collectionProtocolRegistration.setParticipant(participant);
			collectionProtocolRegistration.setProtocolParticipantIdentifier("");
			collectionProtocolRegistration.setActivityStatus("Active");
			
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/2006",
						Utility.datePattern("08/15/2006")));
				
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
				System.out.println("Creating CPR");
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
		
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			
			User user =  new User();
			user.setId(new Long(1));
			
			collectionProtocolRegistration.setConsentWitness(user);
			
//			Collection consentTierResponseCollection = new LinkedHashSet();
//			Collection consentTierCollection = new LinkedHashSet();
//			
//			consentTierCollection = cp.getConsentTierCollection();
//			System.out.println("Creating CPR");
//			Iterator consentTierItr = consentTierCollection.iterator();
//			System.out.println("Creating CPR");
//			 while(consentTierItr.hasNext())
//			 {
//				 ConsentTier consent= (ConsentTier) consentTierItr.next();
//				 ConsentTierResponse response= new ConsentTierResponse();
//				 response.setResponse("Yes");
//				 response.setConsentTier(consent);
//				 consentTierResponseCollection.add(response);				 
//			 }
//				
//			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		
			try{
				collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
			}	
			BizTestCaseUtility.setNameObjectMap("CPRUnderCPWithAllowUsePriv",collectionProtocolRegistration);
			
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();			
			
			scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
			Site site = new Site();
			site.setId(new Long(BizTestCaseUtility.SITE_WITH_ALLOWUSE_PRIV));
			scg.setSpecimenCollectionSite(site);
			scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());		    
			scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
			scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());
			System.out.println("Creating SCG");
			
							
			try{
				scg = (SpecimenCollectionGroup) appService.createObject(scg);
				System.out.println("SCG::"+ scg.getName());
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
			}
			
			IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=new IdentifiedSurgicalPathologyReport();
			identifiedSurgicalPathologyReport.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			identifiedSurgicalPathologyReport.setCollectionDateTime(new Date());
			identifiedSurgicalPathologyReport.setIsFlagForReview(new Boolean(false));
			identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
			identifiedSurgicalPathologyReport.setReportSource(site);
			TextContent textContent=new TextContent();
			String data="[FINAL DIAGNOSIS]\n" +
					"This is the Final Diagnosis Text" +
					"\n\n[GROSS DESCRIPTION]" +
					"The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.";
		
			textContent.setData(data);
			textContent.setSurgicalPathologyReport(identifiedSurgicalPathologyReport);
			Set reportSectionCollection=new HashSet();
			ReportSection reportSection1=new ReportSection();
			reportSection1.setName("GDT");
			reportSection1.setDocumentFragment("The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.");
			reportSection1.setTextContent(textContent);
			
			ReportSection reportSection2=new ReportSection();
			reportSection2.setName("FIN");
			reportSection2.setDocumentFragment("This is the Final Diagnosis Text");
			reportSection2.setTextContent(textContent);
			
			reportSectionCollection.add(reportSection1);
			reportSectionCollection.add(reportSection2);
			
			textContent.setReportSectionCollection(reportSectionCollection);
			
			identifiedSurgicalPathologyReport.setTextContent(textContent);
			identifiedSurgicalPathologyReport.setSpecimenCollectionGroup(scg);
			scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());	
			
		   try{
				identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.createObject(identifiedSurgicalPathologyReport);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
			}
			BizTestCaseUtility.setObjectMap(identifiedSurgicalPathologyReport, IdentifiedSurgicalPathologyReport.class);
					
			BizTestCaseUtility.setNameObjectMap("SCGUnderCPWithAllowUsePriv",scg);
//			TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
//			specimenObj.setSpecimenCollectionGroup(scg);
//			StorageContainer sc = new StorageContainer();
//			sc.setId(new Long(1));
//			specimenObj.setStorageContainer(sc);
//			specimenObj.setPositionDimensionOne(new Integer(1));
//			specimenObj.setPositionDimensionTwo(new Integer(1));
//			try{
//				specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
//				System.out.println("Mol Specimen:"+ specimenObj.getLabel());
//			}
//			catch(Exception e){
//				Logger.out.error(e.getMessage(),e);
//	           	e.printStackTrace();
//	           	assertFalse("Failed to register participant", true);
//			}			
		}	catch(Exception e){
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
			assertFalse(e.getMessage(), true);
		}
	}  

	
  private void createSCGUnderCPWithDisallowReadPrivilege(){
   try {
		CollectionProtocol cp= new CollectionProtocol();
	    cp.setId(new Long(BizTestCaseUtility.CP_WITH_DISALLOW_READ_PRIV));
	    Participant participant = BaseTestCaseUtility.initParticipant();
	    participant.setSocialSecurityNumber("111-22-5565");
	    try
        {
			participant.setBirthDate(Utility.parseDate("05-02-1984", CommonServiceLocator.getInstance().getDatePattern()));
        }
        catch (ParseException e)
        {
              Logger.out.debug(""+e);
        }
		System.out.println("Participant"+participant);
		try{
			participant = (Participant) appService.createObject(participant);
		}
		catch(Exception e){
	       	e.printStackTrace();
            assertFalse(e.getMessage(), true);
		}
		BizTestCaseUtility.setNameObjectMap("ParticipantUnderCPWithDisallowUsePriv", participant);
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		
		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/2006",
					Utility.datePattern("08/15/2006")));
			
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			System.out.println("Creating CPR");
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
		}
	
		collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		
		User user =  new User();
		user.setId(new Long(1));
		
		collectionProtocolRegistration.setConsentWitness(user);

		try{
			collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}	
		BizTestCaseUtility.setNameObjectMap("CPRUnderCPWithDisallowReadPriv",collectionProtocolRegistration);
		
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();			
		
		scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
		Site site = new Site();
		site.setId(new Long(BizTestCaseUtility.SITE_WITH_ALLOWUSE_PRIV));
		scg.setSpecimenCollectionSite(site);
		scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());		    
		scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
		scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("Creating SCG");
		
						
		try{
			scg = (SpecimenCollectionGroup) appService.createObject(scg);
			System.out.println("SCG::"+ scg.getName());
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		
		IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport=new IdentifiedSurgicalPathologyReport();
		identifiedSurgicalPathologyReport.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		identifiedSurgicalPathologyReport.setCollectionDateTime(new Date());
		identifiedSurgicalPathologyReport.setIsFlagForReview(new Boolean(false));
		identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
		identifiedSurgicalPathologyReport.setReportSource(site);
		TextContent textContent=new TextContent();
		String data="[FINAL DIAGNOSIS]\n" +
				"This is the Final Diagnosis Text" +
				"\n\n[GROSS DESCRIPTION]" +
				"The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.";
	
		textContent.setData(data);
		textContent.setSurgicalPathologyReport(identifiedSurgicalPathologyReport);
		Set reportSectionCollection=new HashSet();
		ReportSection reportSection1=new ReportSection();
		reportSection1.setName("GDT");
		reportSection1.setDocumentFragment("The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.");
		reportSection1.setTextContent(textContent);
		
		ReportSection reportSection2=new ReportSection();
		reportSection2.setName("FIN");
		reportSection2.setDocumentFragment("This is the Final Diagnosis Text");
		reportSection2.setTextContent(textContent);
		
		reportSectionCollection.add(reportSection1);
		reportSectionCollection.add(reportSection2);
		
		textContent.setReportSectionCollection(reportSectionCollection);
		
		identifiedSurgicalPathologyReport.setTextContent(textContent);
		identifiedSurgicalPathologyReport.setSpecimenCollectionGroup(scg);
		scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());	
		
	  try{
			identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.createObject(identifiedSurgicalPathologyReport);
		}
		catch(Exception e){
			Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse(e.getMessage(), true);
		}
		BizTestCaseUtility.setObjectMap(identifiedSurgicalPathologyReport, IdentifiedSurgicalPathologyReport.class);
		
		BizTestCaseUtility.setNameObjectMap("SCGUnderCPWithDisallowReadPriv",scg);
		
	}
	catch(Exception e)
	{
		Logger.out.error(e.getMessage(),e);
		e.printStackTrace();
		assertFalse(e.getMessage(), true);
	}
}
	
	 private void createSCGUnderCPHavingScientistAsPI(){
			try {
				CollectionProtocol cp= new CollectionProtocol();
			    cp.setId(new Long(BizTestCaseUtility.CP_WITH_SCIENTIST_AS_PI));
			    Participant participant = BaseTestCaseUtility.initParticipant();
			    participant.setSocialSecurityNumber("111-22-3233");
			    try
		        {
					participant.setBirthDate(Utility.parseDate("05-02-1984", CommonServiceLocator.getInstance().getDatePattern()));
		        }
		        catch (ParseException e)
		        {
		              Logger.out.debug(""+e);
		        }
				System.out.println("Participant"+participant);
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				try{
					participant = (Participant) appService.createObject(participant);
				}
				catch(Exception e){
			       	e.printStackTrace();
		            assertFalse(e.getMessage(), true);
				}
				BizTestCaseUtility.setNameObjectMap("ParticipantWithScientistAsPI", participant);
				CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
				
				collectionProtocolRegistration.setCollectionProtocol(cp);
				collectionProtocolRegistration.setParticipant(participant);
				collectionProtocolRegistration.setProtocolParticipantIdentifier("");
				collectionProtocolRegistration.setActivityStatus("Active");
				
				try
				{
					collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/2006",
							Utility.datePattern("08/15/2006")));
					
					collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
					System.out.println("Creating CPR");
				}
				catch (ParseException e)
				{			
					e.printStackTrace();
				}
			
				collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
				
				User user =  new User();
				user.setId(new Long(1));
				
				collectionProtocolRegistration.setConsentWitness(user);
				
//				Collection consentTierResponseCollection = new LinkedHashSet();
//				Collection consentTierCollection = new LinkedHashSet();
//				
//				consentTierCollection = cp.getConsentTierCollection();
//				System.out.println("Creating CPR");
//				Iterator consentTierItr = consentTierCollection.iterator();
//				System.out.println("Creating CPR");
//				 while(consentTierItr.hasNext())
//				 {
//					 ConsentTier consent= (ConsentTier) consentTierItr.next();
//					 ConsentTierResponse response= new ConsentTierResponse();
//					 response.setResponse("Yes");
//					 response.setConsentTier(consent);
//					 consentTierResponseCollection.add(response);				 
//				 }
//					
//				collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
				try{
					collectionProtocolRegistration = (CollectionProtocolRegistration) appService.createObject(collectionProtocolRegistration);
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}	
				BizTestCaseUtility.setNameObjectMap("CPRWithScientistAsPI",collectionProtocolRegistration);
				
				SpecimenCollectionGroup scg = new SpecimenCollectionGroup();			
				
				scg =(SpecimenCollectionGroup) BaseTestCaseUtility.createSCG(collectionProtocolRegistration);
				Site site = new Site();
				site.setId(new Long(BizTestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
				scg.setSpecimenCollectionSite(site);
				scg.setName("New SCG"+UniqueKeyGeneratorUtil.getUniqueKey());		    
				scg = (SpecimenCollectionGroup) BaseTestCaseUtility.setEventParameters(scg);
				scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());
				System.out.println("Creating SCG");
				
								
				try{
					scg = (SpecimenCollectionGroup) appService.createObject(scg);
					System.out.println("SCG::"+ scg.getName());
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}
				
				IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = new IdentifiedSurgicalPathologyReport();
				identifiedSurgicalPathologyReport.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
				identifiedSurgicalPathologyReport.setCollectionDateTime(new Date());
				identifiedSurgicalPathologyReport.setIsFlagForReview(new Boolean(false));
				identifiedSurgicalPathologyReport.setReportStatus(CaTIESConstants.PENDING_FOR_DEID);
				identifiedSurgicalPathologyReport.setReportSource(site);
				TextContent textContent=new TextContent();
				String data="[FINAL DIAGNOSIS]\n" +
						"This is the Final Diagnosis Text" +
						"\n\n[GROSS DESCRIPTION]" +
						"The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.";
			
				textContent.setData(data);
				textContent.setSurgicalPathologyReport(identifiedSurgicalPathologyReport);
				Set reportSectionCollection=new HashSet();
				ReportSection reportSection1=new ReportSection();
				reportSection1.setName("GDT");
				reportSection1.setDocumentFragment("The specimen is received unfixed labeled hernia sac and consists of a soft, pink to yellow segment of fibrous and fatty tissue measuring 7.5cm in length x 3.2 x 0.9cm with a partly defined lumen.  Representative tissue submitted labeled 1A.");
				reportSection1.setTextContent(textContent);
				
				ReportSection reportSection2=new ReportSection();
				reportSection2.setName("FIN");
				reportSection2.setDocumentFragment("This is the Final Diagnosis Text");
				reportSection2.setTextContent(textContent);
				
				reportSectionCollection.add(reportSection1);
				reportSectionCollection.add(reportSection2);
				
				textContent.setReportSectionCollection(reportSectionCollection);
				
				identifiedSurgicalPathologyReport.setTextContent(textContent);
				identifiedSurgicalPathologyReport.setSpecimenCollectionGroup(scg);
				scg.setSurgicalPathologyNumber("SPN"+UniqueKeyGeneratorUtil.getUniqueKey());	
				
			  try{
					identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) appService.createObject(identifiedSurgicalPathologyReport);
				}
				catch(Exception e){
					Logger.out.error(e.getMessage(),e);
		           	e.printStackTrace();
		           	assertFalse(e.getMessage(), true);
				}
				BizTestCaseUtility.setObjectMap(identifiedSurgicalPathologyReport, IdentifiedSurgicalPathologyReport.class);
						
				BizTestCaseUtility.setNameObjectMap("SCGWithScientistAsPI",scg);
//				TissueSpecimen specimenObj = (TissueSpecimen) BaseTestCaseUtility.initTissueSpecimen();				
//				specimenObj.setSpecimenCollectionGroup(scg);
//				StorageContainer sc = new StorageContainer();
//				sc.setId(new Long(1));
//				specimenObj.setStorageContainer(sc);
//				specimenObj.setPositionDimensionOne(new Integer(3));
//				specimenObj.setPositionDimensionTwo(new Integer(1));
//				try{
//					specimenObj =  (TissueSpecimen) appService.createObject(specimenObj);
//					System.out.println("Mol Specimen:"+ specimenObj.getLabel());
//				}
//				catch(Exception e){
//					Logger.out.error(e.getMessage(),e);
//		           	e.printStackTrace();
//		           	assertFalse("Failed to register participant", true);
//				}				
			} 
			catch(Exception e)
			{
				Logger.out.error(e.getMessage(),e);
				e.printStackTrace();
				assertFalse(e.getMessage(), true);
			}
			
		}  
	  
	 
	  public void testAddSpecimenArrayType()
		{
			try
			{
				SpecimenArrayType specimenArrayType = BaseTestCaseUtility.initSpecimenSpecimenArrayType();
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				specimenArrayType = (SpecimenArrayType) appService.createObject(specimenArrayType);
				BizTestCaseUtility.setObjectMap(specimenArrayType, SpecimenArrayType.class);
			}
			catch(Exception e){
				Logger.out.error(e.getMessage(),e);
	           	e.printStackTrace();
	           	assertFalse(e.getMessage(), true);
			}
		}  
			
}  
	
		   
	 
          

