/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.test;


import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

/**
 * @author ganesh_naikwade
 *
 */
public class CaTissueBaseTestCase extends BaseTestCase{

	/**
	 * @throws java.lang.Exception
	 */
	
//	Mock hibDAO;
//	Mock jdbcDAO;
	static ApplicationService appService = null;
	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 * 
	 */
	public void setUp(){
		
		Logger.configure("");
//		hibDAO = new Mock(HibernateDAO.class);
//		jdbcDAO = new Mock(JDBCDAO.class);
//		
//		MockDAOFactory factory = new MockDAOFactory();
//		factory.setHibernateDAO((HibernateDAO) hibDAO.proxy());
//		factory.setJDBCDAO((JDBCDAO) jdbcDAO.proxy());
//		DAOFactory.setDAOFactory(factory);
		

		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("admin@admin.com", "Login123");
		} 	
					
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			fail();
			System.exit(1);
		}		
	}

//	private void initJunitForInsertArguments()
//	{
//		hibDAO.expect("closeSession");		
//		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
//		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
//		hibDAO.expect("openSession",fullConstraintMatcher);		
//	}
//	
//	private void initJunitForInsert()
//	{
//		Constraint[] constraints = {new IsInstanceOf(SessionDataBean.class)};
//		hibDAO.expect("commit");
//		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
//		hibDAO.expect("openSession",fullConstraintMatcher);
//		Constraint[] insertConstraints = {new IsAnything(),new IsAnything(),new IsAnything(),new IsAnything()};
//		FullConstraintMatcher insertConstraintMatcher = new FullConstraintMatcher(insertConstraints);
//		hibDAO.expect("insert",insertConstraintMatcher);
//		hibDAO.expect("closeSession");
//	}
//	
//	/**
//	 * 
//	 * @param absObject
//	 */
//	
//	public void testEmptyDomainObjectInInsert(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//				
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());		
//	
//		try
//		{
//	    	iBizLogic.insert(absObject, new SessionDataBean(),Constants.HIBERNATE_DAO);
//			fail("When empty  domain object is passed , it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When empty object is passed, it throws NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			fail(" Exception occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			fail(" Exception occured");
//		}
//		
//	}
//	
//	
//	public void testNullDomainObjectInInsert(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		try
//		{
//			iBizLogic.insert(null,new SessionDataBean(),Constants.HIBERNATE_DAO);
//			fail("When null collection protocol object is passed , it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When null collection protocol object is passed, it throws NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" Exception occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" Exception occured");
//		}
//	}
//	
//	public void testNullSessionDataBeanInInsert(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		
//		try
//		{
//			iBizLogic.insert(absObject,null,Constants.HIBERNATE_DAO);
//			fail("When null sessiondataBean is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When null sessiondataBean is passed, it throws NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" Exception occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" Exception occured");
//		}
//	}	
//	
//	public void testWrongDaoTypeInInsert(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		
//		try
//		{
//			iBizLogic.insert(absObject,new SessionDataBean(),0);
//			fail("When wrong DAOType is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When wrong DAOType is passed, it should throw NullPointerException", true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail("Exception occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" Exception occured");
//		}	
//	}
//	
//	
//	public void testNullSessionDataBeanInUpdate(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		
//		try
//		{
//			iBizLogic.update(absObject,absObject,Constants.HIBERNATE_DAO,null);
//			fail("When null sessiondataBean is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When null sessiondataBean is passed, it throws NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail("BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail("UserNotAuthorizedException occured");
//		}		
//	}
//	
//	public void testNullOldDomainObjectInUpdate(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		try
//		{
//			iBizLogic.update(absObject,null,Constants.HIBERNATE_DAO,new SessionDataBean());
//			fail("When null old domain object is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When null Old domain object is passed, it throws NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" UserNotAuthorizedException occured");
//		}		
//	}
//	
//	public void testNullCurrentDomainObjectInUpdate(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		try
//		{
//			
//			iBizLogic.update(null,absObject,Constants.HIBERNATE_DAO,new SessionDataBean());
//			fail("When null current domain object is passed, it throws NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When null current domain object is passed, it should throw NullPointerException", true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" UserNotAuthorizedException occured");
//		}		
//	}
//	
//	public void testWrongDaoTypeInUpdate(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		try
//		{
//			iBizLogic.update(absObject,absObject,0,new SessionDataBean());
//			fail("When wrong dao type is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When wrong dao type is passed, it should throw NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" UserNotAuthorizedException occured");
//		}		
//	}
//	
//	/**
//	 * 
//	 * @param absObject
//	 */
//	public void testEmptyCurrentDomainObjectInUpdate(AbstractDomainObject emptyObject,AbstractDomainObject initialisedObject )
//	{
//
//		//AbstractDomainObject initialisedDomainObject = returnObject(absObject);
//		
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(emptyObject.getClass().getName());
//		
//		try
//		{
//			iBizLogic.update(emptyObject,initialisedObject,Constants.HIBERNATE_DAO,new SessionDataBean());
//			fail("When empty current object is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When empty current domain object is passed, it should throw NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail(" UserNotAuthorizedException occured");
//		}		
//	}
//		
//
//	
//
//	
//	public void testEmptyOldDomainObjectInUpdate(AbstractDomainObject emptyObject , AbstractDomainObject initialisedObject)
//	{
//				
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(emptyObject.getClass().getName());
//		try
//		{
//			iBizLogic.update(emptyObject, initialisedObject,Constants.HIBERNATE_DAO,new SessionDataBean());
//			fail("When Empty old object is passed, it should throw NullPointerException");
//		}
//		catch(NullPointerException e)
//		{
//			assertTrue("When Empty domain object is passed, it should throw NullPointerException",true);
//		}
//		catch (BizLogicException e)
//		{
//			e.printStackTrace();
//			fail(" BizLogicException occured");
//		}
//		catch (UserNotAuthorizedException e)
//		{
//			e.printStackTrace();
//			fail("UserNotAuthorizedException occured");
//		}		
//	}
//	
//	
//		
//	public void testNullCurrentDomainObjectInRetrieve(AbstractDomainObject absObject)
//	{
//		initJunitForInsertArguments();
//		IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(absObject.getClass().getName());
//		try
//		{
//			Collection list = iBizLogic.retrieve(null);
//			fail("When null Old domain object is passed, it should throw NullPointerException");
//		}
//		
//		catch (DAOException e)
//		{
//			e.printStackTrace();
//			assertTrue("When null  domain object is passed, it should throw DAOException",true);
//		}	
//		catch(NullPointerException e)
//		{
//			//e.printStackTrace();
//			assertTrue("When null Old domain object is passed, it should throw NullPointerException",true);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			assertTrue("When null Old domain object is passed, it should throwException",true);
//		}
//					
//	
//	}
//	
	
}


	
/*	public Collection initConsentTier(boolean empty)
	{
//		Setting consent tiers for this protocol.
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
		
	
}  */
	
	
	/*public CollectionProtocolRegistration initCollectionProtocolRegistration()
	{
		//Logger.configure("");
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
	
	
	public SpecimenCollectionGroup initSpecimenCollectionGroup()
	{
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();

		Site site = new Site();
		site.setId(new Long(1));
		specimenCollectionGroup.setSpecimenCollectionSite(site);

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
		// modified code here. chnged funcion name to setInitialQuantity(quantity) from setQuantity(quantity)
		molecularSpecimen.setInitialQuantity(quantity);

		molecularSpecimen.setConcentrationInMicrogramPerMicroliter(new Double(10));
	//	molecularSpecimen.setComments("");
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
	//	collectionEventParameters.setComments("");
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
	//	receivedEventParameters.setComments("");
		receivedEventParameters.setReceivedQuality("Cauterized");
		
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		molecularSpecimen.setSpecimenEventCollection(specimenEventCollection);

//		Biohazard biohazard = new Biohazard();
//		biohazard.setName("Biohazard1");
//		biohazard.setType("Toxic");
//		biohazard.setId(new Long(1));
//		Collection biohazardCollection = new HashSet();
//		biohazardCollection.add(biohazard);
//		molecularSpecimen.setBiohazardCollection(biohazardCollection);

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
	
	
	

	*/