/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.test;


import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsInstanceOf;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.test.MockDAOFactory;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author ganesh_naikwade
 *
 */
public class ModifiedConsentTrackingTest extends BaseTestCase{

/*	*//**
	 * @throws java.lang.Exception
	 *//*
		Mock hibDAO;
		Mock jdbcDAO;
		static int count=0;
		public ModifiedConsentTrackingTest(String name)
		{
			super(name);
		}

		protected void setUp() 
		{
			Logger.configure("");
			hibDAO = new Mock(HibernateDAO.class);
			jdbcDAO = new Mock(JDBCDAO.class);
			
			MockDAOFactory factory = new MockDAOFactory();
			factory.setHibernateDAO((HibernateDAO) hibDAO.proxy());
			factory.setJDBCDAO((JDBCDAO) jdbcDAO.proxy());
			DAOFactory.setDAOFactory(factory);
		}
		*//**
		 * 
		 *//*
		private void initJunitForInsertArguments()
		{
			hibDAO.expect("closeSession");		
			Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
			FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
			hibDAO.expect("openSession",fullConstraintMatcher);		
		}
		*//**
		 * 
		 *//*
		private void initJunitForInsert()
		{
			Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
			hibDAO.expect("commit");
			FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
			hibDAO.expect("openSession",fullConstraintMatcher);
			Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
			FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
			hibDAO.expect("insert",insertConstraintMatcher);
			hibDAO.expect("closeSession");
		}
		
		public void testAll()
		{
			
				AbstractDomainObject abstractDomainObject = new CollectionProtocol();
				
				testEmptyDomainObjectInInsert(abstractDomainObject);
				testNullDomainObjectInInsert(abstractDomainObject);
				testNullSessionDataBeanInInsert(abstractDomainObject);
				testWrongDaoTypeInInsert(abstractDomainObject);
				
				testNullSessionDataBeanInUpdate(abstractDomainObject);
				testNullOldDomainObjectInUpdate(abstractDomainObject);
				testNullCurrentDomainObjectInUpdate(abstractDomainObject);
				testWrongDaoTypeInUpdate(abstractDomainObject);
				testEmptyCurrentDomainObjectInUpdate(abstractDomainObject);
				testEmptyOldDomainObjectInUpdate(abstractDomainObject);		
				
				abstractDomainObject = new SpecimenCollectionGroup();
				
				testEmptyDomainObjectInInsert(abstractDomainObject);
				testNullDomainObjectInInsert(abstractDomainObject);
				testNullSessionDataBeanInInsert(abstractDomainObject);
				testWrongDaoTypeInInsert(abstractDomainObject);
				
				testNullSessionDataBeanInUpdate(abstractDomainObject);
				testNullOldDomainObjectInUpdate(abstractDomainObject);
				testNullCurrentDomainObjectInUpdate(abstractDomainObject);
				testWrongDaoTypeInUpdate(abstractDomainObject);
				testEmptyCurrentDomainObjectInUpdate(abstractDomainObject);
				testEmptyOldDomainObjectInUpdate(abstractDomainObject);
				
				abstractDomainObject = new CollectionProtocolRegistration();
				
				testEmptyDomainObjectInInsert(abstractDomainObject);
				testNullDomainObjectInInsert(abstractDomainObject);
				testNullSessionDataBeanInInsert(abstractDomainObject);
				testWrongDaoTypeInInsert(abstractDomainObject);
				
				testNullSessionDataBeanInUpdate(abstractDomainObject);
				testNullOldDomainObjectInUpdate(abstractDomainObject);
				testNullCurrentDomainObjectInUpdate(abstractDomainObject);
				testWrongDaoTypeInUpdate(abstractDomainObject);
				testEmptyCurrentDomainObjectInUpdate(abstractDomainObject);
		//		testEmptyOldDomainObjectInUpdate(abstractDomainObject);	
				
			    abstractDomainObject = new MolecularSpecimen();
				
				testEmptyDomainObjectInInsert(abstractDomainObject);
				testNullDomainObjectInInsert(abstractDomainObject);
				testNullSessionDataBeanInInsert(abstractDomainObject);
				testWrongDaoTypeInInsert(abstractDomainObject);
				
				testNullSessionDataBeanInUpdate(abstractDomainObject);
				testNullOldDomainObjectInUpdate(abstractDomainObject);
				testNullCurrentDomainObjectInUpdate(abstractDomainObject);
				testWrongDaoTypeInUpdate(abstractDomainObject);
				testEmptyCurrentDomainObjectInUpdate(abstractDomainObject);
				testEmptyOldDomainObjectInUpdate(abstractDomainObject);	
				
					
			
			
		}

		
		public void testEmptyDomainObjectInInsert(AbstractDomainObject absObject)
		{
			DefaultBizLogic obj = new CollectionProtocolBizLogic(); 
			DefaultBizLogic ObjBizlogic = (DefaultBizLogic) returnObjectBizlogic(obj);
			initJunitForInsertArguments();
					
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());		
		
			try
			{
				iBizLogic.insert(absObject, new SessionDataBean(),Constants.HIBERNATE_DAO);
				fail("When null collection protocol object is passed , it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null collection protocol object is passed, it throws NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" Exception occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" Exception occured");
			}
			
		}
		
		public void testNullDomainObjectInInsert(AbstractDomainObject absObject)
		{
			DefaultBizLogic obj = new CollectionProtocolBizLogic(); 
			DefaultBizLogic ObjBizlogic = (DefaultBizLogic) returnObjectBizlogic(obj);
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			try
			{
				iBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
				fail("When null collection protocol object is passed , it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null collection protocol object is passed, it throws NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" Exception occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" Exception occured");
			}
		}
		
		public void testNullSessionDataBeanInInsert(AbstractDomainObject absObject)
		{
			DefaultBizLogic obj = new CollectionProtocolBizLogic(); 
			DefaultBizLogic ObjBizlogic = (DefaultBizLogic) returnObjectBizlogic(obj);
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			
			try
			{
				iBizLogic.insert(absObject,null,Constants.HIBERNATE_DAO);
				fail("When null sessiondataBean is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null sessiondataBean is passes, it throws NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" Exception occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" Exception occured");
			}
		}	
		
		public void testWrongDaoTypeInInsert(AbstractDomainObject absObject)
		{
			DefaultBizLogic obj = new CollectionProtocolBizLogic(); 
			DefaultBizLogic ObjBizlogic = (DefaultBizLogic) returnObjectBizlogic(obj);
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			
			try
			{
				iBizLogic.insert(absObject,new SessionDataBean(),0);
				fail("When wrong DAOType is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When wrong DAOType is passed, it should throw NullPointerException", true);
			}
			catch (BizLogicException e)
			{
				fail("Exception occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" Exception occured");
			}	
		}
		
			
		
		public void testNullSessionDataBeanInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			initJunitForInsertArguments();
			
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			
			try
			{
				iBizLogic.update(absObject,absObject,Constants.HIBERNATE_DAO,null);
				fail("When null sessiondataBean is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null sessiondataBean is passed, it should throw NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		public void testNullOldDomainObjectInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			try
			{
				iBizLogic.update(absObject,null,Constants.HIBERNATE_DAO,new SessionDataBean());
				fail("When null Old domain object is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null Old domain object is passed, it should throw NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		public void testNullCurrentDomainObjectInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			try
			{
				
				iBizLogic.update(null,absObject,Constants.HIBERNATE_DAO,new SessionDataBean());
				fail("When null current domain object is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When null current domain object is passed, it should throw NullPointerException", true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		public void testWrongDaoTypeInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			try
			{
				iBizLogic.update(absObject,absObject,0,new SessionDataBean());
				fail("When wrong dao type is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When wrong dao type is passed, it should throw NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		
		public void testEmptyCurrentDomainObjectInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			AbstractDomainObject initialisedDomainObject = null;
			if(absObject instanceof CollectionProtocol){
				initialisedDomainObject = initCollectionProtocol(false);
			}
			else if(absObject instanceof CollectionProtocolRegistration){
				initialisedDomainObject = initCollectionProtocolRegistration();
			}
			else if(absObject instanceof SpecimenCollectionGroup){
				initialisedDomainObject = initSpecimenCollectionGroup();
			}
			else if(absObject instanceof MolecularSpecimen){
				initialisedDomainObject = initSpecimen();
			}
//			CollectionProtocol collectionProtocol = initCollectionProtocol(false);
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			
			try
			{
				iBizLogic.update(absObject,initialisedDomainObject,Constants.HIBERNATE_DAO,new SessionDataBean());
				fail("When empty CollectionProtocol object is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When empty current domain object is passed, it should throw NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		public void testEmptyOldDomainObjectInUpdate(AbstractDomainObject absObject)
		{
//			CollectionProtocolBizLogic collectionProtocolBizLogic = new CollectionProtocolBizLogic();
			AbstractDomainObject initialisedDomainObject = null;
			
			if(absObject instanceof CollectionProtocol){
				initialisedDomainObject = initCollectionProtocol(false);
			}
			else if(absObject instanceof CollectionProtocolRegistration){
				initialisedDomainObject = initCollectionProtocolRegistration();
			}
			else if(absObject instanceof SpecimenCollectionGroup){
				initialisedDomainObject = initSpecimenCollectionGroup();
			}
			else if(absObject instanceof MolecularSpecimen){
				initialisedDomainObject = initSpecimen();
			}
			initJunitForInsertArguments();
			IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
			try
			{
				iBizLogic.update(initialisedDomainObject,absObject,Constants.HIBERNATE_DAO,new SessionDataBean());
				fail("When Empty old collectionProtocol object is passed, it should throw NullPointerException");
			}
			catch(NullPointerException e)
			{
				assertTrue("When Empty collectionProtocol object is passed, it should throw NullPointerException",true);
			}
			catch (BizLogicException e)
			{
				fail(" BizLogicException occured");
			}
			catch (UserNotAuthorizedException e)
			{
				fail(" BizLogicException occured");
			}		
		}
		
		public Collection initConsentTier(boolean empty)
		{
//			Setting consent tiers for this protocol.
			Collection consentTierColl = new HashSet();
			if(!empty)
			{
				ConsentTier c1 = new ConsentTier();
				c1.setStatement("Consent for aids research");
				consentTierColl.add(c1);
				ConsentTier c2 = new ConsentTier();
				c2.setStatement("Consent for cancer research");
				consentTierColl.add(c2);		
				ConsentTier c3 = new ConsentTier();
				c3.setStatement("Consent for Tb research");
				consentTierColl.add(c3);	
			}
			return consentTierColl;
			
		}
		
		public CollectionProtocol initCollectionProtocol(boolean empty)
		{
			CollectionProtocol collectionProtocol = new CollectionProtocol();
			Collection consentTierColl = initConsentTier(empty);	
			collectionProtocol.setConsentTierCollection(consentTierColl);
			
			//Setting the unsigned doc url
			collectionProtocol.setUnsignedConsentDocumentURL("http://abc");		
			//collectionProtocol.setAliqoutInSameContainer(true);
			collectionProtocol.setDescriptionURL("");
			collectionProtocol.setActivityStatus("Active");
			try
			{
				collectionProtocol.setEndDate(Utility.parseDate("08-15-1975", Constants.DATE_PATTERN_MM_DD_YYYY));
			}
			catch (ParseException e1)
			{			
				e1.printStackTrace();
			}
			try
			{
				collectionProtocol.setStartDate(Utility.parseDate("08-15-1975", Constants.DATE_PATTERN_MM_DD_YYYY));
			}
			catch (ParseException e1)
			{			
				e1.printStackTrace();
			}
			collectionProtocol.setEnrollment(new Integer(1));
			collectionProtocol.setIrbIdentifier("777771");
			collectionProtocol.setTitle("Collection Protocol1234513");
			collectionProtocol.setShortTitle("CP!12");
			
//			Collection distributionProtocolCollection = new HashSet();
//			DistributionProtocol distributionProtocol = new DistributionProtocol();
//			distributionProtocol.setId(new Long(2));
//			distributionProtocolCollection.add(distributionProtocol);		
//			collectionProtocol.setDistributionProtocolCollection(distributionProtocolCollection);
			
			Collection userCollection = new HashSet();
			User user = new User();
			user.setId(new Long(1));
			userCollection.add(user);
			collectionProtocol.setUserCollection(userCollection);
					
			try
			{
				collectionProtocol.setStartDate(Utility.parseDate("08-15-1975", Constants.DATE_PATTERN_MM_DD_YYYY));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}

			Collection collectionProtocolEventCollectionSet = new HashSet();
			CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
			collectionProtocolEvent.setClinicalStatus("Not Specified");
			collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));

			Collection specimenRequirementCollection = new HashSet();
			SpecimenRequirement specimenRequirement = new SpecimenRequirement();
			specimenRequirement.setSpecimenClass("Molecular");
			specimenRequirement.setSpecimenType("DNA");
			specimenRequirement.setTissueSite("Placenta");
			specimenRequirement.setPathologyStatus("Malignant");
			Quantity quantity = new Quantity();
			quantity.setId(new Long(4));
			specimenRequirement.setQuantity(quantity);

			specimenRequirementCollection.add(specimenRequirement);
			collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

			collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
			collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

			User principalInvestigator = new User();
			principalInvestigator.setId(new Long(1));
			collectionProtocol.setPrincipalInvestigator(principalInvestigator);

			return collectionProtocol;
		}
		*//**
		 * @return
		 *//*
		public CollectionProtocolRegistration initCollectionProtocolRegistration()
		{
			Logger.configure("");
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

			CollectionProtocol collectionProtocol = new CollectionProtocol();
			collectionProtocol.setId(new Long(22));
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			Participant participant = new Participant();
			participant.setId(new Long(1));
			collectionProtocolRegistration.setParticipant(participant);

			collectionProtocolRegistration.setProtocolParticipantIdentifier("");
			collectionProtocolRegistration.setActivityStatus("Active");
			try
			{
				collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
						Utility.datePattern("08/15/1975")));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			//Setting Consent Tier Responses.
			try
			{
				collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
			}
			catch (ParseException e)
			{			
				e.printStackTrace();
			}
			collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
			
			User user = new User();
			user.setId(new Long(1));
			collectionProtocolRegistration.setConsentWitness(user);
			
			Collection consentTierResponseCollection = new HashSet();
			
			ConsentTierResponse r1 = new ConsentTierResponse();
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(new Long(20));
			r1.setConsentTier(consentTier);
			r1.setResponse("Yes");
			consentTierResponseCollection.add(r1);
			
			ConsentTierResponse r2 = new ConsentTierResponse();
			ConsentTier consentTier2 = new ConsentTier();
			consentTier2.setId(new Long(22));
			r2.setConsentTier(consentTier2);
			r2.setResponse("Yes");
			consentTierResponseCollection.add(r2);
			
			ConsentTierResponse r3 = new ConsentTierResponse();
			ConsentTier consentTier3 = new ConsentTier();
			consentTier3.setId(new Long(23));
			r3.setConsentTier(consentTier3);
			r3.setResponse("No");
			consentTierResponseCollection.add(r3);
			
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);		
			
			return collectionProtocolRegistration;
		}
		*//**
		 * @return SpecimenCollectionGroup
		 *//*
		public SpecimenCollectionGroup initSpecimenCollectionGroup()
		{
			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();

			Site site = new Site();
			site.setId(new Long(1));
			specimenCollectionGroup.setSite(site);

			specimenCollectionGroup.setClinicalDiagnosis("Abdominal fibromatosis");
			specimenCollectionGroup.setClinicalStatus("Operative");
			specimenCollectionGroup.setActivityStatus("Active");

			CollectionProtocolEvent collectionProtocol = new CollectionProtocolEvent();
			collectionProtocol.setId(new Long(21));
			specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocol);

			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			Participant participant = new Participant();
			participant.setId(new Long(1));
			collectionProtocolRegistration.setParticipant(participant);
			collectionProtocolRegistration.setId(new Long(5));
			CollectionProtocol collectionProt = new CollectionProtocol();
			collectionProt.setId(new Long(21));
			
			collectionProtocolRegistration.setCollectionProtocol(collectionProt);
			//collectionProtocolRegistration.setProtocolParticipantIdentifier("");
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);

			specimenCollectionGroup.setName("Collection Protocol1_1_1.1.1");

			
			//Setting Consent Tier Status.
			Collection consentTierStatusCollection = new HashSet();
			
			ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(new Long(21));
			consentTierStatus.setConsentTier(consentTier);
			consentTierStatus.setStatus("Yes");
			consentTierStatusCollection.add(consentTierStatus);
			
			ConsentTierStatus  consentTierStatus1 = new ConsentTierStatus();		
			ConsentTier consentTier1 = new ConsentTier();
			consentTier1.setId(new Long(22));
			consentTierStatus1.setConsentTier(consentTier1);
			consentTierStatus1.setStatus("Yes");
			consentTierStatusCollection.add(consentTierStatus1);
			
			ConsentTierStatus  consentTierStatus2 = new ConsentTierStatus();		
			ConsentTier consentTier2 = new ConsentTier();
			consentTier2.setId(new Long(23));
			consentTierStatus2.setConsentTier(consentTier2);
			consentTierStatus2.setStatus("Yes");
			consentTierStatusCollection.add(consentTierStatus2);
			
			specimenCollectionGroup.setConsentTierStatusCollection(consentTierStatusCollection);
			
			return specimenCollectionGroup;
		}
		*//**
		 * @return Specimen
		 *//*
		public Specimen initSpecimen()
		{
			MolecularSpecimen molecularSpecimen = new MolecularSpecimen();

			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
			specimenCollectionGroup.setId(new Long(1));
			molecularSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);

			molecularSpecimen.setLabel("Specimen 12345");
			molecularSpecimen.setBarcode("");
			molecularSpecimen.setType("DNA");
			molecularSpecimen.setAvailable(new Boolean(true));
			molecularSpecimen.setActivityStatus("Active");

			SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
			specimenCharacteristics.setTissueSide("Left");
			specimenCharacteristics.setTissueSite("Placenta");
			molecularSpecimen.setSpecimenCharacteristics(specimenCharacteristics);

			molecularSpecimen.setPathologicalStatus("Malignant");

			Quantity quantity = new Quantity();
			quantity.setValue(new Double(10));
			molecularSpecimen.setQuantity(quantity);

			molecularSpecimen.setConcentrationInMicrogramPerMicroliter(new Double(10));
			molecularSpecimen.setComments("");
			// Is virtually located
			molecularSpecimen.setStorageContainer(null);
			molecularSpecimen.setPositionDimensionOne(null);
			molecularSpecimen.setPositionDimensionTwo(null);
			

			Collection externalIdentifierCollection = new HashSet();
			ExternalIdentifier externalIdentifier = new ExternalIdentifier();
			externalIdentifier.setName("Specimen 1 ext id");
			externalIdentifier.setValue("11");
			externalIdentifierCollection.add(externalIdentifier);
			molecularSpecimen.setExternalIdentifierCollection(externalIdentifierCollection);

			CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
			collectionEventParameters.setComments("");
			User user = new User();
			user.setId(new Long(1));
		//	collectionEventParameters.setId(new Long(0));
			collectionEventParameters.setUser(user);
			try
			{
				collectionEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
						.datePattern("08/15/1975")));
			}
			catch (ParseException e1)
			{
				e1.printStackTrace();
			}
			collectionEventParameters.setContainer("No Additive Vacutainer");
			collectionEventParameters.setCollectionProcedure("Needle Core Biopsy");

			ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
			receivedEventParameters.setUser(user);
			//receivedEventParameters.setId(new Long(0));
			try
			{
				receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
						.datePattern("08/15/1975")));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			receivedEventParameters.setReceivedQuality("acceptable");
			receivedEventParameters.setComments("");
			receivedEventParameters.setReceivedQuality("Cauterized");
			
			Collection specimenEventCollection = new HashSet();
			specimenEventCollection.add(collectionEventParameters);
			specimenEventCollection.add(receivedEventParameters);
			molecularSpecimen.setSpecimenEventCollection(specimenEventCollection);

//			Biohazard biohazard = new Biohazard();
//			biohazard.setName("Biohazard1");
//			biohazard.setType("Toxic");
//			biohazard.setId(new Long(1));
//			Collection biohazardCollection = new HashSet();
//			biohazardCollection.add(biohazard);
//			molecularSpecimen.setBiohazardCollection(biohazardCollection);

			//Setting Consent Tier Response
			Collection consentTierStatusCollection = new HashSet();
			
			ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(new Long(21));
			consentTierStatus.setConsentTier(consentTier);
			consentTierStatus.setStatus("No");
			consentTierStatusCollection.add(consentTierStatus);
			
			ConsentTierStatus  consentTierStatus1 = new ConsentTierStatus();		
			ConsentTier consentTier1 = new ConsentTier();
			consentTier1.setId(new Long(22));
			consentTierStatus1.setConsentTier(consentTier1);
			consentTierStatus1.setStatus("No");
			consentTierStatusCollection.add(consentTierStatus1);
			
			ConsentTierStatus  consentTierStatus2 = new ConsentTierStatus();		
			ConsentTier consentTier2 = new ConsentTier();
			consentTier2.setId(new Long(23));
			consentTierStatus2.setConsentTier(consentTier2);
			consentTierStatus2.setStatus("No");
			consentTierStatusCollection.add(consentTierStatus2);
			
			molecularSpecimen.setConsentTierStatusCollection(consentTierStatusCollection);
			
			return molecularSpecimen;
		}

*/}
