
import java.text.ParseException;

import java.util.Collection;

import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.ClinicalReport;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Container;

import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DistributionProtocol;

import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayContent;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author ashish_gupta
 *
 */
public class APIDemo
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		/*
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.applicationHome + "\\WEB-INF\\src\\"
				+ "ApplicationResources.properties");

		System
				.setProperty("gov.nih.nci.security.configFile",
						"C:/jboss-4.0.0/server/default/catissuecore-properties/ApplicationSecurityConfig.xml");
		System
				.setProperty("app.propertiesFile",
						"C:/jboss-4.0.0/server/default/catissuecore-properties/caTissueCore_Properties.xml");
		CDEManager.init();
		XMLPropertyHandler.init("caTissueCore_Properties.xml");
		ApplicationProperties.initBundle("ApplicationResources");

		APIDemo apiDemo = new APIDemo();
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName("admin@admin.com");

		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();

		//				Department dept = apiDemo.initDepartment();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, dept);
		//		
		//				Biohazard hazardObj=apiDemo.initBioHazard();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, hazardObj);
		//		
		//				CancerResearchGroup cancerResearchGroup = apiDemo.initCancerResearchGroup();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, cancerResearchGroup);
		//		
		//				Institution institution = apiDemo.initInstitution();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, institution);
		//		
		//				Site site = apiDemo.initSite();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, site);
		//		
		//				StorageType storageType = apiDemo.initStorageType();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, storageType);
		//		
		//				SpecimenArrayType specimenArrayType = apiDemo.initSpecimenArrayType();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, specimenArrayType);
		//		
		//				User user = apiDemo.initUser();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, user);
		//		
		//				Participant participant = apiDemo.initParticipant();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, participant);
		//		
		//				StorageContainer storageContainer = apiDemo.initStorageContainer();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, storageContainer);
		//		
		//				CollectionProtocol collectionProtocol = apiDemo.initCollectionProtocol();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, collectionProtocol);
		//		
		//				DistributionProtocol distributionProtocol = apiDemo.initDistributionProtocol();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, distributionProtocol);
		//		
		//			    CollectionProtocolRegistration collectionProtocolRegistration = apiDemo.initCollectionProtocolRegistration();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, collectionProtocolRegistration);

		//				SpecimenCollectionGroup specimenCollectionGroup = apiDemo.initSpecimenCollectionGroup();
		//				apiDemo.addData(bizLogicFactory, sessionDataBean, specimenCollectionGroup);

//						Specimen specimen = apiDemo.initSpecimen();
//						apiDemo.addData(bizLogicFactory, sessionDataBean, specimen);

						Distribution distribution = apiDemo.initDistribution();
						apiDemo.addData(bizLogicFactory, sessionDataBean, distribution);
		*/				
	}

	/**
	 * @param bizLogicFactory
	 * @param sessionDataBean
	 * @param obj
	 * @throws Exception
	 */
	private void addData(BizLogicFactory bizLogicFactory, SessionDataBean sessionDataBean,
			Object obj) throws Exception
	{
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		bizLogic.insert(obj, sessionDataBean, Constants.HIBERNATE_DAO);
	}

	/**
	 * @return Department
	 */
	public Department initDepartment()
	{
		Department dept = new Department();
		dept.setName("Pathology New");
		return dept;
	}

	/**
	 * @return Biohazard
	 */
	public Biohazard initBioHazard()
	{
		Biohazard bioHazard = new Biohazard();
		bioHazard.setComments("NueroToxicProtein");
		//bioHazard.setName();
		bioHazard.setType("Toxic");
		return bioHazard;
	}

	/**
	 * @return CancerResearchGroup
	 */
	public CancerResearchGroup initCancerResearchGroup()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		//cancerResearchGroup.setName("Siteman Cancer Research Group1");//Siteman Cancer Research Group
		return cancerResearchGroup;
	}

	/**
	 * @return Institution
	 */
	public Institution initInstitution()
	{
		Institution institutionObj = new Institution();
		institutionObj.setName("Washington University School of Medicine1");//Washington University School of Medicine
		return institutionObj;
	}

	/**
	 * @return Site
	 */
	public Site initSite()
	{
		Site siteObj = new Site();
		User userObj = new User();
		userObj.setId(new Long(1));

		siteObj.setEmailAddress("admin1@admin1.com");
		siteObj.setName("Washington University School of Medicine123");
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

	/**
	 * @return StorageType
	 */
	public StorageType initStorageType()
	{
		StorageType storageTypeObj = new StorageType();
		Capacity capacity = new Capacity();

		storageTypeObj.setName("Freezer");
		storageTypeObj.setDefaultTempratureInCentigrade(new Double(-30));
		storageTypeObj.setOneDimensionLabel("label 1");
		storageTypeObj.setTwoDimensionLabel("label 2");

		capacity.setOneDimensionCapacity(1);
		capacity.setTwoDimensionCapacity(2);
		storageTypeObj.setCapacity(capacity);
		storageTypeObj.setId(new Long(21));

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageTypeObj);

		storageTypeObj.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		storageTypeObj.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);

		return storageTypeObj;
	}

	/**
	 * @return SpecimenArrayType
	 */
	public SpecimenArrayType initSpecimenArrayType()
	{
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setName("TissueArray");
		specimenArrayType.setSpecimenClass("Frozen Tissue");

		Collection specimenArrayTypeCollection = new HashSet();
		specimenArrayTypeCollection.add("Tissue");
		specimenArrayTypeCollection.add("Frozen Tissue");

		specimenArrayType.setSpecimenTypeCollection(specimenArrayTypeCollection);

		specimenArrayType.setComment("");

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(1);
		capacity.setTwoDimensionCapacity(2);
		specimenArrayType.setCapacity(capacity);

		return specimenArrayType;
	}

	/**
	 * @return StorageContainer
	 */
	public StorageContainer initStorageContainer()
	{
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName("New Container");

		StorageType storageType = new StorageType();
		storageType.setId(new Long(21));
		storageContainer.setStorageType(storageType);

		Site site = new Site();
		site.setId(new Long(4));
		storageContainer.setSite(site);

		storageContainer.setNoOfContainers(10);
		storageContainer.setTempratureInCentigrade(new Double(-30));
		storageContainer.setBarcode("");

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(1);
		capacity.setTwoDimensionCapacity(2);
		storageContainer.setCapacity(capacity);

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(new Long(2));
		Collection collectionProtocolCollection = new HashSet();
		collectionProtocolCollection.add(collectionProtocol);
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		holdsStorageTypeCollection.add(storageType);
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);

		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Molecular");
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);

		Container parent = new Container();
		parent.setId(new Long(2));
		storageContainer.setParent(parent);

		storageContainer.setPositionDimensionOne(1);
		storageContainer.setPositionDimensionTwo(2);
		return storageContainer;
	}

	/**
	 * @return User
	 */
	public User initUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("admin123@admin1.com");
		userObj.setLoginName("admin12@admin1.com");
		userObj.setLastName("admin1");
		userObj.setFirstName("admin12");

		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("D.C.");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");
		userObj.setAddress(address);

		Institution institution = new Institution();
		institution.setId(new Long(1));
		userObj.setInstitution(institution);

		Department department = new Department();
		department.setId(new Long(1));
		userObj.setDepartment(department);

		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setId(new Long(1));
		userObj.setCancerResearchGroup(cancerResearchGroup);

		userObj.setRoleId("1");
		userObj.setComments("");
		userObj.setPageOf(Constants.PAGEOF_SIGNUP);
		return userObj;
	}

	/**
	 * @return Participant
	 */
	public Participant initParticipant()
	{
		Participant participant = new Participant();
		participant.setLastName("Participant123");
		participant.setFirstName("Participant1");
		participant.setMiddleName("Participant1");

		try
		{
			participant.setBirthDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//participant.setDeathDate(date);

		participant.setVitalStatus("Alive");
		participant.setGender("Male");
		participant.setSexGenotype("XX");

		Collection raceCollection = new HashSet();
		raceCollection.add("White");
		raceCollection.add("Asian");
		participant.setRaceCollection(raceCollection);

		participant.setEthnicity("Hispanic or Latino");
		participant.setSocialSecurityNumber("111-11-1111");

		Collection participantMedicalIdentifierCollection = new HashSet();
		/*participantMedicalIdentifierCollection.add("Washington University School of Medicine");
		 participantMedicalIdentifierCollection.add("1111");
		 */
		participant
				.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		return participant;
	}

	/**
	 * @return CollectionProtocol
	 */
	public CollectionProtocol initCollectionProtocol()
	{
		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setActivityStatus("active");
		collectionProtocol.setAliqoutInSameContainer(true);
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777");
		collectionProtocol.setTitle("Collection Protocol12345");
		collectionProtocol.setShortTitle("CP!");
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
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);

		specimenRequirementCollection.add(specimenRequirement);
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

		User principalInvestigator = new User();
		principalInvestigator.setId(new Long(3));
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		return collectionProtocol;
	}

	/**
	 * @return Specimen
	 */
	public Specimen initSpecimen()
	{
		MolecularSpecimen molecularSpecimen = new MolecularSpecimen();

		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(new Long(1));
		molecularSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);

		molecularSpecimen.setLabel("Specimen 12");
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
		collectionEventParameters.setContainer("No additive Vacutainer");
		collectionEventParameters.setCollectionProcedure("Needle core biopsy");

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

		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		molecularSpecimen.setSpecimenEventCollection(specimenEventCollection);

		Biohazard biohazard = new Biohazard();
		biohazard.setName("Biohazard1");
		biohazard.setType("Toxic");
		biohazard.setId(new Long(1));
		Collection biohazardCollection = new HashSet();
		biohazardCollection.add(biohazard);
		molecularSpecimen.setBiohazardCollection(biohazardCollection);

		return molecularSpecimen;
	}

	/**
	 * @return SpecimenCollectionGroup
	 */
	public SpecimenCollectionGroup initSpecimenCollectionGroup()
	{
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();

		Site site = new Site();
		site.setId(new Long(2));
		specimenCollectionGroup.setSite(site);

		specimenCollectionGroup.setClinicalDiagnosis("Abdominal fibromatosis");
		specimenCollectionGroup.setClinicalStatus("Operative");
		specimenCollectionGroup.setActivityStatus("Active");

		CollectionProtocolEvent collectionProtocol = new CollectionProtocolEvent();
		collectionProtocol.setId(new Long(2));
		specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocol);

		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		Participant participant = new Participant();
		participant.setId(new Long(1));
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setId(new Long(1));
		//collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);

		specimenCollectionGroup.setName("Collection Protocol1_1_1.1");

		ClinicalReport clinicalReport = new ClinicalReport();
		clinicalReport.setSurgicalPathologyNumber("");
		//clinicalReport.setId(new Long(1));
		specimenCollectionGroup.setClinicalReport(clinicalReport);

		return specimenCollectionGroup;
	}

	/**
	 * @return Distribution
	 */
	public Distribution initDistribution()
	{
		Distribution distribution = new Distribution();

		distribution.setActivityStatus("Active");

		Specimen specimen = new Specimen();
		specimen.setBarcode("");
		specimen.setLabel("new label");
		specimen.setId(new Long(3));
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(15));
		specimen.setAvailableQuantity(quantity);
		
		DistributedItem distributedItem = new DistributedItem();
		distributedItem.setQuantity(new Double(10));
		distributedItem.setSpecimen(specimen);
		Collection distributedItemCollection = new HashSet();
		distributedItemCollection.add(distributedItem);
		distribution.setDistributedItemCollection(distributedItemCollection);

		DistributionProtocol distributionProtocol = new DistributionProtocol();
		distributionProtocol.setId(new Long(2));
		distribution.setDistributionProtocol(distributionProtocol);

		Site toSite = new Site();
		toSite.setId(new Long(2));
		distribution.setToSite(toSite);

		try
		{
			distribution.setTimestamp(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		distribution.setComments("");

		User user = new User();
		user.setId(new Long(1));
		distribution.setUser(user);

		return distribution;
	}

	/**
	 * @return DistributionProtocol
	 */
	public DistributionProtocol initDistributionProtocol()
	{
		DistributionProtocol distributionProtocol = new DistributionProtocol();

		User principalInvestigator = new User();
		principalInvestigator.setId(new Long(1));
		distributionProtocol.setPrincipalInvestigator(principalInvestigator);

		distributionProtocol.setTitle("Distribution Protocol1");
		distributionProtocol.setShortTitle("DP1");
		distributionProtocol.setIrbIdentifier("55555");
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		distributionProtocol.setDescriptionURL("");
		distributionProtocol.setEnrollment(new Integer(10));

		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setPathologyStatus("Malignant");
		specimenRequirement.setTissueSite("Placenta");
		specimenRequirement.setSpecimenType("DNA");
		specimenRequirement.setSpecimenClass("Molecular");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);

		Collection specimenRequirementCollection = new HashSet();
		specimenRequirementCollection.add(specimenRequirement);
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);

		distributionProtocol.setActivityStatus("Active");
		return distributionProtocol;
	}

	/**
	 * @return CollectionProtocolRegistration
	 */
	public CollectionProtocolRegistration initCollectionProtocolRegistration()
	{
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setId(new Long(1));
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
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return collectionProtocolRegistration;
	}
	public SpecimenArray initSpecimenArray()
	{
		SpecimenArray specimenArray = new SpecimenArray();
		
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setId(new Long(3));
		specimenArray.setSpecimenArrayType(specimenArrayType);
		
		specimenArray.setBarcode("");
		
		User createdBy = new User();
		createdBy.setId(new Long(1));
		specimenArray.setCreatedBy(createdBy);
		
		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(1);
		capacity.setTwoDimensionCapacity(2);
		specimenArray.setCapacity(capacity);
		
		specimenArray.setComment("");
		specimenArray.setPositionDimensionOne(1);
		specimenArray.setPositionDimensionTwo(2);
		
		Collection specimenArrayContentCollection = new HashSet();
		SpecimenArrayContent specimenArrayContent = new SpecimenArrayContent();
		Specimen specimen = new MolecularSpecimen();
		specimen.setType("");
		specimenArrayContent.setSpecimen(specimen);
		specimenArrayContentCollection.add(specimenArrayContent);
		specimenArray.setSpecimenArrayContentCollection(specimenArrayContentCollection);
		return specimenArray;
	}
}
