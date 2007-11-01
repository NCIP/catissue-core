package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class ParticipantTestCases extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;
	
	public void testAddParticipantWithCPR()
	{
		try{
			Participant participant= BaseTestCaseUtility.initParticipantWithCPR();			
			System.out.println(participant);
			participant = (Participant) appService.createObject(participant); 
			System.out.println("Object created successfully");
			assertTrue("Object added successfully", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("could not add object", true);
		 }
	}
	
	public void testSearchParticipant()
	{
		Participant participant = new Participant();
    	Logger.out.info(" searching domain object");
    	participant.setId(new Long(1));
   
         try {
        	 List resultList = appService.search(Participant.class,participant);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
        		 Participant returnedParticipant = (Participant) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
        		// System.out.println(" Domain Object is successfully Found ---->  :: " + returnedDepartment.getName());
             }
          } 
          catch (Exception e) {
           	Logger.out.error(e.getMessage(),e);
           	e.printStackTrace();
           	assertFalse("Does not find Domain Object", true);
	 		
          }
	}
	
	public void testUpdateParticipant()
	{
		Participant participant =  BaseTestCaseUtility.initParticipantWithCPR();
    	Logger.out.info("updating domain object------->"+participant);
	    try 
		{
	    	participant = (Participant) appService.createObject(participant);
	    	BaseTestCaseUtility.updateParticipant(participant);
	    	Participant updatedParticipant = (Participant) appService.updateObject(participant);
	       	Logger.out.info("Domain object successfully updated ---->"+updatedParticipant);
	       	assertTrue("Domain object successfully updated ---->"+updatedParticipant, true);
	    } 
	    catch (Exception e) {
	       	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		assertFalse("failed to update Object", true);
	    }
	}
	
	
	public void testCollectedSCGAndNoOfChildSpecimens()
	{
		Participant participant =  BaseTestCaseUtility.initParticipantWithCPR();
		CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)participant.getCollectionProtocolRegistrationCollection().iterator().next();
		System.out.println(cpr.getCollectionProtocol().getId());
		try
		{
			participant = (Participant) appService.createObject(participant);
			Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();
			Iterator cprCPR = cprCollection.iterator();
			CollectionProtocolRegistration cprObj  = null;
			while(cprCPR.hasNext())
			{
				cprObj = (CollectionProtocolRegistration)cprCPR.next();
			}
			System.out.println("Befor");
			Collection scgCollection  = cprObj.getSpecimenCollectionGroupCollection();
			Iterator scgItr = scgCollection.iterator();
			SpecimenCollectionGroup sprObj  = null;
			while(scgItr.hasNext())
			{
				sprObj = (SpecimenCollectionGroup)scgItr.next();
			}
			
			
			TestCaseUtility.setObjectMap(sprObj, SpecimenCollectionGroup.class);
			TestCaseUtility.setObjectMap(participant, Participant.class);
			
			Iterator itr = sprObj.getSpecimenCollection().iterator();
			Specimen sp = null;
			while(itr.hasNext())
			{
				sp = (Specimen)itr.next();
			}
			TestCaseUtility.setObjectMap(sp, Specimen.class);
			if(sp==null)
			{
				assertFalse("No Specimens created under SCG", true);
			}
			
			if(sp.getChildrenSpecimen().size()>0)
			{
				assertTrue("Correct no of Specimens inserted ---->"+participant, true);
			}
			else
			{
				assertFalse("Child Specimen not found ", true);
			}
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
		}
	}	
	
	
/*	public void testInvalidParticipantActivityStatus()
	{
		try{
			Participant participant = BaseTestCaseUtility.initParticipant();		
			participant.setActivityStatus("Invalid");
			System.out.println(participant);
			participant = (Participant) appService.createObject(participant); 
			assertFalse("Test Failed.Invalid Activity status should thorw Exception", true);
		 }
		 catch(Exception e){
			 Logger.out.error(e.getMessage(),e);
			 e.printStackTrace();
			 assertTrue("Name is required", true);
		 }
	}
*///	
//	public void testEmptyObjectInInsert_Participant(){
//		domainObject = new Participant();
//		testEmptyDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullObjectInInsert_Participant(){
//		domainObject = new Participant();
//		testNullDomainObjectInInsert(domainObject);
//	}
//	
//	public void testNullSessionDatBeanInInsert_Participant(){
//		domainObject = new Participant();
//		testNullSessionDataBeanInInsert(domainObject);
//	}
//	
//	public void testNullSessionDataBeanInUpdate_Participant(){
//		domainObject = new Participant();
//		testNullSessionDataBeanInUpdate(domainObject);
//	}
//	
//	public void testNullOldDomainObjectInUpdate_Participant(){
//		domainObject = new Participant();
//		testNullOldDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testNullCurrentDomainObjectInUpdate_Participant(){
//		domainObject = new Participant();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testWrongDaoTypeInUpdate_Participant(){
//		domainObject = new Participant();
//		testNullCurrentDomainObjectInUpdate(domainObject);
//	}
//	
//	public void testEmptyCurrentDomainObjectInUpdate_Participant()
//	{
//		domainObject = new Participant();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initParticipant();
//		testEmptyCurrentDomainObjectInUpdate(domainObject, initialisedDomainObject);
//	}
//	
//	public void testEmptyOldDomainObjectInUpdate_Participant()
//	{
//		domainObject = new Participant();
//		AbstractDomainObject initialisedDomainObject = BaseTestCaseUtility.initParticipant();
//		testEmptyOldDomainObjectInUpdate(domainObject,initialisedDomainObject);
//	}
//	public void testNullDomainObjectInRetrieve_Participant()
//	{
//		domainObject = new Participant();
//		testNullCurrentDomainObjectInRetrieve(domainObject);
//	}
//
//	
		
	
}
