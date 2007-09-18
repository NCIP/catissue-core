package edu.wustl.catissuecore.bizlogic.test;



import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class BaseTestCaseUtility {

	public static CollectionProtocol initCollectionProtocol(){
	    CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setAliquotInSameContainer(new Boolean(true));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777");
		collectionProtocol.setTitle("ccoll proto"+UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setShortTitle("pc!");
		System.out.println("reached setUp");
		
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/2003", Utility.datePattern("08/15/1975")));
		
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}

		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();		
		collectionProtocolEvent.setClinicalStatus("New Diagnosis");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));		
	 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		specimenRequirementCollection.add(specimenRequirement);
		
//		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

		User principalInvestigator = new User();		
		principalInvestigator.setId(new Long("2"));		
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
		User protocolCordinator = new User();
		protocolCordinator.setId(new Long("3"));
		
		Collection protocolCordinatorCollection = new HashSet();
		protocolCordinatorCollection.add(protocolCordinator);
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		
		return collectionProtocol;
		
	}
	
	public static void updateCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		collectionProtocol.setAliquotInSameContainer(new Boolean(false)); //true
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active"); //Active
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("11111");//77777
		collectionProtocol.setTitle("cp consent updated title" + UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setShortTitle("cp concent"); //pc!
		
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		Collection collectionProtocolEventCollectionSet = new HashSet();
		CollectionProtocolEvent collectionProtocolEvent = null;
		collectionProtocolEventCollectionSet = collectionProtocol.getCollectionProtocolEventCollection();
		for(Iterator itr=collectionProtocolEventCollectionSet.iterator();itr.hasNext();)
		{
			collectionProtocolEvent = (CollectionProtocolEvent)itr.next(); 

			collectionProtocolEvent.setClinicalStatus("Not Specified");//New Diagnosis
			collectionProtocolEvent.setStudyCalendarEventPoint(new Double(2)); //1
		}
	
			Collection specimenRequirementCollection = new HashSet();
		//	SpecimenRequirement specimenRequirement = new 
		
		/*	specimenRequirement =(SpecimenRequirement)itr.next();
			specimenRequirement.setSpecimenClass("Fluid"); //Molecular
			specimenRequirement.setSpecimenType("Bile"); //DNA
			specimenRequirement.setTissueSite("Anal canal"); //Placenta
			specimenRequirement.setPathologyStatus("Non-Malignant");//Malignant
		
			specimenRequirementCollection.add(specimenRequirement);
			
			collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);*/
			collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
			collectionProtocol
					.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);
	
			/*User principalInvestigator = new User();
			
			collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
			User protocolCordinator = new User();
			Collection protocolCordinatorCollection = new HashSet();
			protocolCordinatorCollection.add(protocolCordinator);
			collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);*/
		
	}
	
	public static CollectionProtocolEvent initCollectionProtocolEvent()
	{
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();		
		collectionProtocolEvent.setClinicalStatus("New Diagnosis");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));		
	 

		Collection specimenRequirementCollection = new HashSet();
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		specimenRequirementCollection.add(specimenRequirement);
		
//		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		//Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value");
		
		return collectionProtocolEvent;
		
	}
	
	public static User initUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("ddd@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("Alabama");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");	 
		
		
		userObj.setAddress(address);
		
		Institution inst = new Institution();
		inst.setName("institution"+ UniqueKeyGeneratorUtil.getUniqueKey());
		inst.setId(new Long(5));
		userObj.setInstitution(inst);
		
		Department department = new Department();
		department.setName("Departmentt"+ UniqueKeyGeneratorUtil.getUniqueKey());
		department.setId(new Long(5));
	//	department.setName("dt1");
		userObj.setDepartment(department);
		
		CancerResearchGroup cancerResearchGroup =  new CancerResearchGroup();
		cancerResearchGroup.setName("CRG name"+ UniqueKeyGeneratorUtil.getUniqueKey());
		cancerResearchGroup.setId(new Long(5));
		userObj.setCancerResearchGroup(cancerResearchGroup);
		
		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");

		return userObj;
	}
	
	public static Department initDepartment()
	{
		Department dept =new Department();
		dept.setName("department name"+ UniqueKeyGeneratorUtil.getUniqueKey());
		return dept;
	}
	public static void updateDepartment(Department department)
	{
		department.setName("dt"+UniqueKeyGeneratorUtil.getUniqueKey());
	}
	
	
	
	
	public static SpecimenCollectionGroup initSpecimenCollectionGroup()
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
	
	public static Specimen initSpecimen()
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
	
	public static CollectionProtocolRegistration initCollectionProtocolRegistration()
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
	
	public Collection initConsentTier(boolean empty)
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
		
	
}
	public static Institution initInstitution()
	{
		Institution institutionObj = new Institution();
		institutionObj.setName("inst" + UniqueKeyGeneratorUtil.getUniqueKey());
		return institutionObj;
	}
	
	public static void updateInstitution(Institution institution)
	{
		institution.setName("inst"+UniqueKeyGeneratorUtil.getUniqueKey());
	}
	
	public static CancerResearchGroup initCancerResearchGrp()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("crgName" + UniqueKeyGeneratorUtil.getUniqueKey());
		return cancerResearchGroup;
	}
	
	public static void updateCancerResearchGrp(CancerResearchGroup cancerResearchGroup)
	{
		cancerResearchGroup.setName("crgName"+UniqueKeyGeneratorUtil.getUniqueKey());		
	}
	
	
	public static DistributionProtocol initDistributionProtocol()
	{
		DistributionProtocol distributionProtocol = new DistributionProtocol();

		//User principalInvestigator = initUser();
		User principalInvestigator =new User();
		principalInvestigator.setId(new Long("1"));
		
		/*	
		new User();
		principalInvestigator.setId(new Long(1));
		*/
		distributionProtocol.setPrincipalInvestigator(principalInvestigator);
		distributionProtocol.setTitle("DP"+ UniqueKeyGeneratorUtil.getUniqueKey());
		distributionProtocol.setShortTitle("DP1");
		distributionProtocol.setIrbIdentifier("52266");
		
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		distributionProtocol.setDescriptionURL("distribution protocol");
		distributionProtocol.setEnrollment(new Integer(10));

		SpecimenRequirement specimenRequirement = initSpecimenRequirement();
		/*	
		new SpecimenRequirement();
		specimenRequirement.setPathologyStatus("Malignant");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setSpecimenClass("Molecular");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		*/
			
		Collection specimenRequirementCollection = new HashSet();
		specimenRequirementCollection.add(specimenRequirement);
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);

		distributionProtocol.setActivityStatus("Active");
		return distributionProtocol;
	}
	
	public static SpecimenRequirement initSpecimenRequirement()
	{
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass("Molecular");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setPathologyStatus("Malignant");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		return specimenRequirement;
	}
	
	public static void updateDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		User principalInvestigator = initUser();
		/*	
		new User();
		principalInvestigator.setId(new Long(1));
		*/
		distributionProtocol.setPrincipalInvestigator(principalInvestigator);
		distributionProtocol.setTitle("DP"+ UniqueKeyGeneratorUtil.getUniqueKey());
		distributionProtocol.setShortTitle("DP"); //DP1
		distributionProtocol.setIrbIdentifier("11111");//55555
		
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1976", Utility
					.datePattern("08/15/1976"))); //08/15/1975
		}
		catch (ParseException e)
		{
			Logger.out.error(e.getMessage(),e);
			e.printStackTrace();
		}
		
		distributionProtocol.setDescriptionURL("");
		distributionProtocol.setEnrollment(new Integer(20)); //10

		SpecimenRequirement specimenRequirement = initSpecimenRequirement();
		specimenRequirement.setPathologyStatus("Non-Malignant"); //Malignant
		specimenRequirement.setTissueSite("Anal canal"); //Placenta
		specimenRequirement.setSpecimenType("Bile"); //DNA
		specimenRequirement.setSpecimenClass("Fluid"); //Molecular
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(20)); //10
		specimenRequirement.setQuantity(quantity);
			
		Collection specimenRequirementCollection = new HashSet();
		specimenRequirementCollection.add(specimenRequirement);
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);

		distributionProtocol.setActivityStatus("Active"); //Active
	}
	
	public static Site initSite()
	{
		Site siteObj = new Site();
//		User userObj = new User();
//		userObj.setId(new Long(1));
		User userObj = new User();
		userObj.setId(new Long(1));

		siteObj.setEmailAddress("admin@admin.com");
		siteObj.setName("sit" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Laboratory");
		siteObj.setActivityStatus("Active");
		siteObj.setCoordinator(userObj);
		
	 
		Address addressObj = new Address();
		addressObj.setCity("Saint Louis");
		addressObj.setCountry("United States");
		addressObj.setFaxNumber("555-55-5555");
		addressObj.setPhoneNumber("123678");
		addressObj.setState("Missouri");
		addressObj.setStreet("4939 Children's Place");
		addressObj.setZipCode("63110");
		siteObj.setAddress(addressObj);
		return siteObj;
	}
	
	public static void updateSite(Site siteObj)
	{
		siteObj.setEmailAddress("admin1@admin.com");
		siteObj.setName("updatedSite" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Repository");
		siteObj.setActivityStatus("Active");		
		siteObj.getAddress().setCity("Saint Louis1"); 
		siteObj.getAddress().setCountry("United States");
		siteObj.getAddress().setFaxNumber("777-77-77771");
		siteObj.getAddress().setPhoneNumber("1236781");
		siteObj.getAddress().setState("Missouri");
		siteObj.getAddress().setStreet("4939 Children's Place1");
		siteObj.getAddress().setZipCode("63111");		
	}
	
	public static StorageType initStorageType()
	{
		StorageType storageTypeObj = new StorageType();
		Capacity capacity = new Capacity();

		storageTypeObj.setName("st" + UniqueKeyGeneratorUtil.getUniqueKey());
		storageTypeObj.setDefaultTempratureInCentigrade(new Double(-30));
		storageTypeObj.setOneDimensionLabel("label 1");
		storageTypeObj.setTwoDimensionLabel("label 2");

		capacity.setOneDimensionCapacity(new Integer(3));
		capacity.setTwoDimensionCapacity(new Integer(3));
		storageTypeObj.setCapacity(capacity);		

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageTypeObj);

		storageTypeObj.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
		storageTypeObj.setActivityStatus("Active");

		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageTypeObj.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		return storageTypeObj;
	}
	

	public static void updateStorageType(StorageType updateStorageType)
	{		
		Capacity capacity = updateStorageType.getCapacity();
		
		updateStorageType.setDefaultTempratureInCentigrade(new Double(30));//-30
		updateStorageType.setOneDimensionLabel("Label-1"); //label 1
		updateStorageType.setTwoDimensionLabel("Label-2"); //label 2

		capacity.setOneDimensionCapacity(new Integer(2));//3
		capacity.setTwoDimensionCapacity(new Integer(2));//3
		updateStorageType.setCapacity(capacity);
			
	}
	
	
	public static Participant initParticipant()
	{
		Participant participant = new Participant();
		participant.setLastName("lname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("fname"+UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setGender("Male Gender");
		participant.setEthnicity("Unknown");
		participant.setSexGenotype("XX");

		Collection raceCollection = new HashSet();
		raceCollection.add("White");
		raceCollection.add("Asian");
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");
		Logger.out.info("Participant added successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
			
		
		return participant;
	}
	
	public static void updateParticipant(Participant participant)
	{
		participant.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("frst" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setMiddleName("mdl" + UniqueKeyGeneratorUtil.getUniqueKey());
		
		participant.setVitalStatus("Alive"); //Dead
		participant.setGender("Male Gender"); //
		participant.setSexGenotype(""); //XX

		Collection raceCollection = new HashSet();
		raceCollection.add("Black or African American"); //White
		raceCollection.add("Unknown"); //Asian
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active"); //Active
		participant.setEthnicity("Unknown"); //Hispanic or Latino
		//participant.setSocialSecurityNumber("333-33-3333");

		Collection participantMedicalIdentifierCollection = new HashSet();
//		participantMedicalIdentifierCollection.add("Washington University School of Medicine");
//		 participantMedicalIdentifierCollection.add("1111");
//		 
		participant
				.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
	}
	
	
	
	public static StorageContainer initStorageContainer()
	{
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName("sc" + UniqueKeyGeneratorUtil.getUniqueKey());

		StorageType storageType = new StorageType();
		storageType.setId(new Long(1));
		
		storageContainer.setStorageType(storageType);
		Site site = new Site();
		site.setId(new Long(1));
		
		storageContainer.setSite(site);

		Integer conts = new Integer(1);
		storageContainer.setNoOfContainers(conts);
		storageContainer.setTempratureInCentigrade(new Double(-30));
		storageContainer.setBarcode("barc" + UniqueKeyGeneratorUtil.getUniqueKey());

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(new Integer(1));
		capacity.setTwoDimensionCapacity(new Integer(2));
		storageContainer.setCapacity(capacity);

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(new Long(1));
		
		
		Collection collectionProtocolCollection = new HashSet();
		collectionProtocolCollection.add(collectionProtocol);
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageType);
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();		
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
/*		Container parent = new Container();
		parent.setId(new Long(2));
		storageContainer.setParent(parent);    
*/
		storageContainer.setPositionDimensionOne(new Integer(1));
		storageContainer.setPositionDimensionTwo(new Integer(2));

		storageContainer.setActivityStatus("Active");
		storageContainer.setFull(Boolean.valueOf(false));
		return storageContainer;
	}
	
	public static void updateStorageContainer(StorageContainer storageContainer)
	{	
		StorageType storageType = new StorageType();
		storageType.setId(new Long(5));  //setId(1)
		storageContainer.setStorageType(storageType);
		
		Site site = new Site();
		site.setId(new Long(1));
		storageContainer.setSite(site);
		
		storageContainer.setTempratureInCentigrade(new Double(30)); //-30
		storageContainer.setBarcode("barc" + UniqueKeyGeneratorUtil.getUniqueKey());

//		Capacity capacity = storageContainer.getCapacity();
//		capacity.setOneDimensionCapacity(new Integer(1));
//		capacity.setTwoDimensionCapacity(new Integer(2));
//		storageContainer.setCapacity(capacity);

		CollectionProtocol collectionProtocol =  new CollectionProtocol();
		collectionProtocol.setId(new Long(1));
		
		Collection collectionProtocolCollection = new HashSet();
		collectionProtocolCollection.add(collectionProtocol);
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageType);
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();		
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
		storageContainer.setPositionDimensionOne(new Integer(1));
		storageContainer.setPositionDimensionTwo(new Integer(2));

		storageContainer.setActivityStatus("Active");
		storageContainer.setFull(Boolean.valueOf(false));		
	}
	
	public static Biohazard initBioHazard()
	{
		Biohazard bioHazard = new Biohazard();
		bioHazard.setComment("NueroToxicProtein");
		bioHazard.setName("bh" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioHazard.setType("Toxic");
		return bioHazard;
	}
	
	public static void updateBiohazard(Biohazard bioHazard)
	{
		bioHazard.setComment("Radioactive");
		bioHazard.setName("bh" + UniqueKeyGeneratorUtil.getUniqueKey());
		bioHazard.setType("Radioactive"); //Toxic
	}
	
	public static OrderDetails initOrder()
    {           
          OrderDetails order = new OrderDetails();  
          order.setComment("Comment");
          
          //Obtain Distribution Protocol
          DistributionProtocol distributionProtocolObj = new DistributionProtocol();
          distributionProtocolObj.setId(new Long(2));
          
          /*DistributionProtocol distributionProtocol = new DistributionProtocol();
          distributionProtocol.setId(new Long(2));*/

          order.setDistributionProtocol(distributionProtocolObj);
          order.setName("Request1 ");
          order.setStatus("New");
          try
          {
                order.setRequestedDate(Utility.parseDate("04-02-1984", Constants.DATE_PATTERN_MM_DD_YYYY));
          }

          catch (ParseException e)
          {
                Logger.out.debug(""+e);
          }
          Collection orderItemCollection = new HashSet();       

          Specimen specimen = new Specimen();
          specimen.setId(new Long(1));

          ExistingSpecimenOrderItem exSpOrderItem = new ExistingSpecimenOrderItem();
          exSpOrderItem.setDescription("OrderDetails Item 1 of Order_Id ");
          exSpOrderItem.setStatus("New");           
          
          Quantity quantity = new Quantity();
          quantity.setValue(new Double(1));
          exSpOrderItem.setRequestedQuantity(quantity);
          exSpOrderItem.setSpecimen(specimen);
               
          orderItemCollection.add(exSpOrderItem);
          order.setOrderItemCollection(orderItemCollection);
          return order;
    }
	
	public static OrderDetails updateOrderDetails(OrderDetails orderObj)
	{
		orderObj.setComment("UpdatedComment");
		
		//Obtain Distribution Protocol
        DistributionProtocol distributionProtocolObj = new DistributionProtocol();
        distributionProtocolObj.setId(new Long(2));
		
		orderObj.setDistributionProtocol(distributionProtocolObj);
        orderObj.setName("Updated Request Name");
        orderObj.setStatus("Pending");
        try
        {
        	orderObj.setRequestedDate(Utility.parseDate("05-02-1984", Constants.DATE_PATTERN_MM_DD_YYYY));
        }
        catch (ParseException e)
        {
              Logger.out.debug(""+e);
        }
        Collection orderItemCollection = new HashSet(); 
        ExistingSpecimenOrderItem existingOrderItem =(ExistingSpecimenOrderItem) orderObj.getOrderItemCollection().iterator().next();
        existingOrderItem.setDescription("Updated OrderDetails Item 1 of Order_Id ");
        existingOrderItem.setStatus("Pending - Protocol Review");          
        existingOrderItem.setOrderDetails(orderObj);
       
        
        return orderObj;
	}
	
	public static Distribution initDistribution()
	{
		Distribution distribution = new Distribution();

		distribution.setActivityStatus("Active");

		Specimen specimen = new Specimen();
		specimen.setId(new Long(1));
		
		/*
		= new MolecularSpecimen();
	//	specimen.setBarcode("");
	//	specimen.setLabel("new label");
		specimen.setId(new Long(10));
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(15));
		specimen.setAvailableQuantity(quantity);
		*/
		
		
		
		DistributedItem distributedItem = new DistributedItem();
		distributedItem.setQuantity(new Double(1));
		distributedItem.setSpecimen(specimen);
		Collection distributedItemCollection = new HashSet();
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		
		DistributionProtocol distributionProtocol =  new DistributionProtocol();
		distributionProtocol.setId(new Long(2));
		distribution.setDistributionProtocol(distributionProtocol);
		
		Site toSite =  new Site();
		toSite.setId(new Long(6));
		//toSite.setId(new Long("1000"));
		distribution.setToSite(toSite);
		/*	
		new Site();
		toSite.setId(new Long(1));
		distribution.setToSite(toSite);
		*/
		/*
		try
		{
			distribution.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		*/
		distribution.setComment("");

		User user = new User();
		user.setId(new Long(2));
		/*	
		new User();
		user.setId(new Long(1));
		*/
		distribution.setDistributedBy(user);
	 
		return distribution;
	}
	
	public static Distribution initDistribution(Specimen specimen)
	{
		Distribution distribution = new Distribution();

		distribution.setActivityStatus("Active");		
		DistributedItem distributedItem = new DistributedItem();
		distributedItem.setQuantity(new Double(2));
		distributedItem.setSpecimen(specimen);
		Collection distributedItemCollection = new HashSet();
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);
		
		DistributionProtocol distributionProtocol = new DistributionProtocol();		
		distributionProtocol.setId(new Long(2));
		distribution.setDistributionProtocol(distributionProtocol);
		
		Site toSite =  new Site();
		toSite.setId(new Long(2));
		distribution.setToSite(toSite);	
		distribution.setComment("");

		User user =  new User();
		user.setId(new Long(2));
		distribution.setDistributedBy(user);
		return distribution;
	}
	
	public static Specimen initTissueSpecimen()
	{
		TissueSpecimen ts= new TissueSpecimen();
		ts.setLabel("TissueSpecimenXYZ"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setActivityStatus("Active");
		ts.setAvailable(true);
		ts.setBarcode("Barcode"+UniqueKeyGeneratorUtil.getUniqueKey());
		ts.setType("Fixed Tissue Block");
		//ts.setAliqoutMap(arg0);
		ts.setPathologicalStatus("Malignant");
		
		
		try{
			ts.setCreatedOn(Utility.parseDate("08/15/2003", Utility.datePattern("08/15/1975")));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage()); 
			
		}
		
				
		SpecimenCharacteristics specimenCharacteristics =  new SpecimenCharacteristics();
		specimenCharacteristics.setId(new Long(1));
		specimenCharacteristics.setTissueSide("Left");
		specimenCharacteristics.setTissueSite("Placenta");
		ts.setSpecimenCharacteristics(specimenCharacteristics);
		
		
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setId(new Long(2));
		ts.setSpecimenCollectionGroup(scg);
		
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10.0));
		ts.setInitialQuantity(quantity);
		ts.setAvailableQuantity(quantity);
		
		
		ts.setStorageContainer(null); 
		ts.setPositionDimensionOne(null);
		ts.setPositionDimensionTwo(null);
		
		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("eid" + UniqueKeyGeneratorUtil.getUniqueKey());
		externalIdentifier.setValue("11");
		externalIdentifier.setSpecimen(ts);
		externalIdentifierCollection.add(externalIdentifier);
		ts.setExternalIdentifierCollection(externalIdentifierCollection);
		
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		collectionEventParameters.setComment("");
		collectionEventParameters.setSpecimen(ts);
		
		User user = new User();
		user.setId(new Long(1));
		collectionEventParameters.setUser(user);		
		try
		{
			collectionEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
					
		}
		catch (ParseException e1)
		{
			System.out.println(" exception in APIDemo");
			e1.printStackTrace();
		}
		
		collectionEventParameters.setContainer("No Additive Vacutainer");
		collectionEventParameters.setCollectionProcedure("Needle Core Biopsy");
		
		
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setUser(user);				
		try
		{
			System.out.println("--- Start ---- 10");
			receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			System.out.println("APIDemo");
			e.printStackTrace();
		}
		
		receivedEventParameters.setReceivedQuality("Acceptable");
		receivedEventParameters.setComment("");
		receivedEventParameters.setSpecimen(ts);
		
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		ts.setSpecimenEventCollection(specimenEventCollection);
		
		Biohazard biohazard =  new Biohazard();
		biohazard.setId(new Long(1));
		biohazard.setName("Bh"+UniqueKeyGeneratorUtil.getUniqueKey());
		biohazard.setType("Toxic");
		Collection biohazardCollection = new HashSet();
		biohazardCollection.add(biohazard);
		ts.setBiohazardCollection(biohazardCollection);
		System.out.println(" -------- end -----------");
		
		//Created on date is same as Collection Date
		
		ts.setCreatedOn(collectionEventParameters.getTimestamp());
		
		Collection consentTierStatusCollection = new HashSet();
		ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
		ConsentTier consentTier = new ConsentTier();
		consentTier.setId(new Long(1));
		consentTierStatus.setConsentTier(consentTier);
		consentTierStatus.setStatus("No");
		consentTierStatusCollection.add(consentTierStatus);
		
		ts.setConsentTierStatusCollection(consentTierStatusCollection);
		
        return ts;
		
			
	}
}
