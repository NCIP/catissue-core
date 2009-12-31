package edu.wustl.catissuecore.api.test;

import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

public class CsmScientistTestCases extends BaseTestCase{
	static ApplicationService appService = null;
	  ClientSession cs = null;
	  public void setUp(){
		//Logger.configure("");
		appService = ApplicationServiceProvider.getApplicationService();
		cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("scientist@admin.com", "Test123");
			System.out.println("Inside Csm scientist setup method ");
		}catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			fail();
			System.exit(1);
		}		
	}
	  public void tearDown(){
		  System.out.println("Inside Csm scientist teardown method");
		  cs.terminateSession();
	  }
	  
	  public void testSearchCPWithAllowReadPrevilegeForScientist()
		{
	    	CollectionProtocol collectionProtocol = new CollectionProtocol();
	    	collectionProtocol.setId(new Long(TestCaseUtility.CP_WITH_ALLOW_READ_PRIV));
	       	Logger.out.info(" searching domain object");
	    	try {
	        	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
	        	 assertEquals(1, resultList.size());
	        	 System.out.println("List Size:"+resultList.size());
	        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
	        	 {
	        		 CollectionProtocol returnedcollectionprotocol = (CollectionProtocol) resultsIterator.next();
	        		 assertEquals("ColProt_allowRead", returnedcollectionprotocol.getTitle());
	        		 assertEquals("cp_allowRead", returnedcollectionprotocol.getShortTitle());
	        		 assertEquals("Active",returnedcollectionprotocol.getActivityStatus());
	        		 System.out.println("colleproto:"+returnedcollectionprotocol.getTitle());
	        		 System.out.println("colleproto:"+returnedcollectionprotocol.getShortTitle());
	        		 System.out.println("colleproto:"+returnedcollectionprotocol.getActivityStatus());
	        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
	             }
	          } 
	          catch (Exception e) {
	        	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		fail("Doesnot found collection protocol");
	          }
		}
	 
	  public void testSearchCPWithDisallowReadPrevilegeForScientist()
		{
	    	CollectionProtocol collectionProtocol = new CollectionProtocol();
	    	collectionProtocol.setId(new Long(TestCaseUtility.CP_WITH_DISALLOW_READ_PRIV));
	       	Logger.out.info(" searching domain object");
	    	try {
	        	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
	        	 assertEquals(0, resultList.size());
	        	 Logger.out.info("No CP Found");
	    	} catch (Exception e) {
	        	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		fail("Doesnot found collection protocol");
	          }
		}  
	 
	  public void testReadPHIdataOfParticipantRegisteredToCPWithAllowReadPrivilegeForScientist()
		{
    	try{
    		System.out.println("testReadPHIdataOfParticipantRegisteredToCPWithAllowReadPrivilegeWithScientistLogin");
	    
	    	Participant cachedParticipant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantUnderCPWithAllowUsePriv");
	    	Participant participant = new Participant();
	    	participant.setId(cachedParticipant.getId());
	    	System.out.println("searching domain object");
		    Logger.out.info("searching domain object");
		  	
	  		System.out.println("searching domain object");
	      	List resultList = appService.search(Participant.class,participant);
	      	assertEquals(1,resultList.size());
	      	participant =(Participant) resultList.get(0);
	      	
	      	System.out.println("searching domain object");
	      	System.out.println("Participant Fname:"+participant.getFirstName());
	      	System.out.println("Participant Fname:"+participant.getLastName());
	      	System.out.println("Participant MidName:"+participant.getMiddleName());
	      	System.out.println("Date Of Birth:"+participant.getBirthDate());
	      	System.out.println("SSN:"+participant.getSocialSecurityNumber());
	      	
	      	assertNull(participant.getFirstName());
	    	assertNull(participant.getLastName());
	    	assertNull(participant.getMiddleName());   	
	    	assertNull(participant.getBirthDate());
	    	assertNull(participant.getSocialSecurityNumber());    	    	
	    }catch (Exception e) {
	        	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		fail("Failed to read PHI data of participant registered under CP with allow read priv for scientist");
		    }
		}
	  
	  public void testReadPHIdataOfSCGUnderCPWithAllowReadPrivilegeForScientist()
		{
	    try{
	    	System.out.println("testReadPHIdataOfSCGUnderCPWithAllowReadPrivilegeWithScientistLogin");
	    	SpecimenCollectionGroup cachedSCG =(SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SCGUnderCPWithAllowUsePriv");
	    	SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
	    	scg.setId(cachedSCG.getId());
	    	List resultList = appService.search(SpecimenCollectionGroup.class,scg);
	    	System.out.println("searching domain object");
	      	
	    	SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup) resultList.get(0);
	    	IdentifiedSurgicalPathologyReport idSurgPathReport =  returnedSCG.getIdentifiedSurgicalPathologyReport();
	    	 
	    	System.out.println("SCG Name:"+returnedSCG.getName());
	    	System.out.println("Surgical Pathology Number:"+returnedSCG.getSurgicalPathologyNumber());	    	
	    	System.out.println("Identified Report Data:"+idSurgPathReport.getTextContent().getData());
	      	
	    	assertNotNull(returnedSCG.getName());
 		    assertNull(returnedSCG.getSurgicalPathologyNumber());
 		    assertNotNull(idSurgPathReport.getTextContent().getData());
	      	
	      	}
	      catch (Exception e) {
	        	Logger.out.error(e.getMessage(),e);
		 		e.printStackTrace();
		 		fail("Failed to read PHI data of SCG under CP with allow read priv for scientist");
	        }
		} 
	  
	
	
	 public void testReadPHIdataForParticipantRegisteredUnderCPHavingScientistAsPIForScientist()
	   {  
		System.out.println("testReadPHIdataForParticipantRegisteredUnderCPHavingScientistAsPI");

		Participant cachedParticipant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantWithScientistAsPI");
		Participant participant = new Participant();
		participant.setId(cachedParticipant.getId());
	  	System.out.println("searching domain object");
	    Logger.out.info(" searching domain object");
	  	try {
	  		System.out.println("searching domain object");
	      	List resultList = appService.search(Participant.class,participant);
	      	assertEquals(1,resultList.size());
	      	System.out.println("searching domain object");
	      
	      	Participant returnedParticipant = (Participant) resultList.get(0);
	      	System.out.println("Fname:"+returnedParticipant.getFirstName());
	      	assertNotNull(returnedParticipant.getFirstName());
	      	System.out.println("Lname:"+returnedParticipant.getLastName());
 		    assertNotNull(returnedParticipant.getLastName());
 		   System.out.println("Mname:"+returnedParticipant.getMiddleName());
 		    assertNotNull(returnedParticipant.getMiddleName());
 		   System.out.println("B'date:"+returnedParticipant.getBirthDate());
 		    assertNotNull(returnedParticipant.getBirthDate());
 		   System.out.println("SSN:"+returnedParticipant.getSocialSecurityNumber());
 		    assertNotNull(returnedParticipant.getSocialSecurityNumber());
     	}catch(Exception e) {
    		System.out.println("Excpetion:"+e);
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Failed to read PHI data of participant registered under CP with allow read priv for scientist");
	      }
	   }
	  
	  public void testReadPHIDataOfSCGUnderCPHavingScientistAsPIForScientist()
	   {  
		System.out.println("testReadPHIDataOfSCGUnderCPHavingScientistAsPI");  
		SpecimenCollectionGroup cachedSCG = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SCGWithScientistAsPI"); 
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(cachedSCG.getId());
	  	System.out.println("searching domain object");
	    Logger.out.info(" searching domain object");
	  	try {
	  		List resultList = appService.search(SpecimenCollectionGroup.class, scg);
	      	System.out.println("searching domain object");
	      	assertEquals(1,resultList.size());
	      	SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup) resultList.get(0);
	      	assertNotNull(returnedSCG.getName());
 		    assertNotNull(returnedSCG.getSurgicalPathologyNumber());
 		  
 		    IdentifiedSurgicalPathologyReport idSurgPathReport =  returnedSCG.getIdentifiedSurgicalPathologyReport();
 		    assertNotNull(idSurgPathReport.getTextContent().getData());
	      		      	
//	      	System.out.println("SCG Name:"+returnedSCG.getName());
//	    	System.out.println("Surgical Pathology Number:"+returnedSCG.getSurgicalPathologyNumber());	    	
//	    	System.out.println("Identified Report Data:"+idSurgPathReport.getTextContent().getData());

   	}catch(Exception e) {
	   		System.out.println("Excpetion:"+e);
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Failed to read PHI data of SCG under CP with allow read priv for scientist");
	     }
  }
	 
	  
	  
	  public void testReadPHIdataOfParticipantRegisteredUnderCPWithDisallowReadPrivForScientist()
	   {  
		System.out.println("testReadPHIdataOfParticipantRegisteredUnderCPWithDisallowReadPriv");
		Participant cachedParticipant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantUnderCPWithDisallowUsePriv");
		Participant participant = new Participant();
		participant.setId(cachedParticipant.getId());
	  	System.out.println("searching domain object");
	    Logger.out.info(" searching domain object");
	  	try {
	  		System.out.println("searching domain object");
	      	List resultList = appService.search(Participant.class,participant);
	        assertEquals(0,resultList.size());
//	      	System.out.println("searching domain object");
//	      	Participant returnedParticipant = (Participant) resultList.get(0);
//	      	System.out.println("searching domain object");
//		    assertNotNull(returnedParticipant.getFirstName());
//		   System.out.println("Participant Fname:"+returnedParticipant.getFirstName());
//		    assertNotNull(returnedParticipant.getLastName());
//		   System.out.println("Participant Lname:"+returnedParticipant.getLastName());
//		    assertNotNull(returnedParticipant.getMiddleName());
//		   System.out.println("Participant Mnamename:"+returnedParticipant.getMiddleName());
//		    assertNotNull(returnedParticipant.getBirthDate());
//		   System.out.println("Date Of Birth:"+returnedParticipant.getBirthDate());
//		    assertNotNull(returnedParticipant.getSocialSecurityNumber());
//		   System.out.println("SSN:"+returnedParticipant.getSocialSecurityNumber());  	
	    	
	   	}catch(Exception e) {
   		    System.out.println("Excpetion:"+e);
       	    Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to read PHI data for participant registered to CP with disallow read privilege for scientist", true);
	      }
	   }
	  
	  public void testReadPHIdataOfSCGUnderCPWithDisallowReadPrivForScientist()
	   {  
		  System.out.println("testReadPHIdataOfSCGUnderCPWithDisallowReadPriv");  
		SpecimenCollectionGroup cachedSCG = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SCGUnderCPWithDisallowReadPriv"); 
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(cachedSCG.getId());
	  	System.out.println("searching domain object");
	    Logger.out.info(" searching domain object");
	  	try {
	  		System.out.println("searching domain object");
	      	List resultList = appService.search(SpecimenCollectionGroup.class, scg);
	      	System.out.println("searching domain object");
	      	assertEquals(0, resultList.size());
	      	
//	      	System.out.println("searching domain object");
//	      	SpecimenCollectionGroup returnedSCG = (SpecimenCollectionGroup) resultList.get(0);
//	      	System.out.println("searching domain object");
//	      		      	
//	      	assertNotNull(returnedSCG.getName());
//		    assertNotNull(returnedSCG.getSurgicalPathologyNumber());
//		  
//		    IdentifiedSurgicalPathologyReport idSurgPathReport =  returnedSCG.getIdentifiedSurgicalPathologyReport();
//		    assertNotNull(idSurgPathReport.getTextContent().getData());
	      	
	      	
//	      	System.out.println("SCG Name:"+returnedSCG.getName());
//	    	System.out.println("Surgical Pathology Number:"+returnedSCG.getSurgicalPathologyNumber());	    	
//	    	System.out.println("Identified Report Data:"+idSurgPathReport.getTextContent().getData());
	      	
  	}
	  	catch(Exception e) {
	   		System.out.println("Excpetion:"+e);
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertTrue("Failed to read PHI data of SCG under CP with disallow read privilege for scientist", true);
	      }
	   }  
 }
