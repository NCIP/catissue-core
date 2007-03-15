import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.global.Constants;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;


/**
 * @author prafull_kadam
 * To insert the bulk Specimen data for Participant to Specimen Heirarchy. 
 */
public class DemoQueryData
{
	private static boolean DEBUG = false;
	// Constants to configure.
	static final int NO_OF_COLLECTION_PROTOCOL = 4;

	static final int NO_OF_PARTICIPANT = 15;
	static final int NO_OF_SCG_PER_PARTICIPANT = 5;
	static final int MIN_SCG_PER_PARTICIPANT = 2;
	static final int NO_OF_SPECIMEN_PER_SCG = 5;
	static final int MIN_SPECIMEN_PER_SCG = 2;
	static final int MAX_CHILD_SPECIMEN = 3;
	static final int NO_OF_SITES = 5;
	static final int MAX_PARTICIPANT_MEDICAL_IDS = 3;
	
	private CollectionProtocol[] collectionProtocols = new CollectionProtocol[NO_OF_COLLECTION_PROTOCOL];
	private Site[] sites = new Site[NO_OF_SITES];
	
	private List<User> userList = new ArrayList<User>();
	private User protocolCoordinator, principleInvestigator;
	
	private final static String races[] = {"American Indian or Alaska Native", "Asian", "Black or African American", "Native Hawaiian or Other Pacific Islander","Not Specified", "Unknown"};
	private final static String genders[] = {"Male", "Female", "Unspecified", "Unknown", null};
	private final static String clinicalStatus[] = {"Post-Operative", "Operative", "Pre-Operative", "New Diagnosis", "Post-Therapy","Pre-Therapy"};
	private final static String clinicalDiagnosis[] = {"GERM CELL NEOPLASMS","LYMPHOSARCOMA CELL LEUKEMIA","MONOCYTIC LEUKEMIAS", "FIBROMATOUS NEOPLASMS", "PLASMA CELL TUMORS", "ODONTOGENIC TUMORS"};
	private final static String vitalStatus[] = {"Alive","Dead"};
	
	private final static int molecularSpecimen = 0;
	private final static int tissueSpecimen = 1;
	private final static int fluidSpecimen = 2;
	private final static int cellSpecimen = 3;
	
	private String molecularSpecimenTypes[] = {"cDNA","DNA","Not Specified","protein","RNA","RNA, cytoplasmic","RNA, nuclear","RNA, poly-A enriched"};
	private String tissueSpecimenTypes[] = {"Fixed Tissue","Fixed Tissue Block","Fixed Tissue Slide","Fresh Tissue","Frozen Tissue","Frozen Tissue Block","Frozen Tissue Slide","Microdissected","Not Specified"};
	private String fluidSpecimenTypes[] = {"Amniotic Fluid","Bile","Body Cavity Fluid","Bone Marrow Plasma","Cerebrospinal Fluid","Feces","Gastric Fluid","Lavage","Milk","Not Specified","Pericardial Fluid","Plasma","Saliva","Serum","Sputum","Sweat","Synovial Fluid","Urine","Vitreous Fluid","Whole Blood","Whole Bone Marrow"};
	private String cellSpecimenTypes[] = {"Cryopreserved Cells","Fixed Cell Block","Frozen Cell Block","Frozen Cell Pellet","Not Specified","Slide"};
	private String tissueSides[] = {"Right", "Left", "Not Applicable", "Not Specified"};
	private String tissueSites[] = {"DIGESTIVE ORGANS","SKIN","MALE GENITAL ORGANS","UNKNOWN PRIMARY SITE","BRAIN","SPINAL CORD, CRANIAL NERVES, AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM","EYE, BRAIN AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM","URINARY TRACT","LIP, ORAL CAVITY AND PHARYNX","BREAST"};
	private String pathologicalStatus[] = {"Malignant, Invasive","Malignant, Pre-Invasive","Non-Malignant","Non-Malignant, Diseased","Pre-Malignant","Not Specified","Malignant","Metastatic"};
	private ApplicationService appService;
	Random randomGenerator;
	
	private long participantCount,cpCount, scgCount, specimenCount,childSpecimenCount;
	Date startTime, endTime;
	String appender = "";
	public DemoQueryData()
	{
		appService = ApplicationServiceProvider.getApplicationService();
		randomGenerator = new Random();
	}
	
	private boolean openSession() throws ApplicationException
	{
		ClientSession cs = ClientSession.getInstance();
		return cs.startSession("admin@admin.com", "Login123");
	}
	
	
	public static void main(String args[])
	{
//		ApplicationServiceProvider applicationServiceProvider = new ApplicationServiceProvider();
		DemoQueryData demoQueryData = new DemoQueryData();
		demoQueryData.println("Starting.....params"+args.length);
		if (args.length>=1)
		{
			System.out.println("Appender:"+args[0]);
			demoQueryData.setAppender(args[0]);
		}
		demoQueryData.startTime = new Date();
		try
		{ 
			demoQueryData.openSession();
			demoQueryData.insertAllData();
			
		} 
		catch (Exception ex) 
		{ 
			ex.printStackTrace();
			return;
		}
		finally
		{
			demoQueryData.printSummary();
		}
		
	}
	
	/**
	 * To set the appender.
	 * @param appender
	 */
	public void setAppender(String appender)
	{
		this.appender = appender;
	}
	/**
	 * TO print summary.
	 */
	public void printSummary()
	{
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Statistics:");
		System.out.println("Number Of Collection Protocol To add:"+NO_OF_COLLECTION_PROTOCOL);
		System.out.println("Number Of Participants To add:"+NO_OF_PARTICIPANT);
		System.out.println("Number Of SCG per Participants To add:"+NO_OF_SCG_PER_PARTICIPANT);
		System.out.println("Minimum number Of SCG per Participants To add:"+MIN_SCG_PER_PARTICIPANT);
		System.out.println("Number Of Specimen per SCG To add:"+NO_OF_SPECIMEN_PER_SCG);
		System.out.println("Minimum number Of Specimen per SCG To add:"+MIN_SPECIMEN_PER_SCG);
		System.out.println("Maximum number of Child specimen To add:"+MAX_CHILD_SPECIMEN);
		System.out.println("Number Of Sites To add:"+NO_OF_SITES);
		System.out.println("-----------------------------------------------------------------------");
		System.out.println("Inserted record summary:");
		System.out.println("Participants:"+participantCount);
		System.out.println("Collection Protocols:"+cpCount);
		System.out.println("SCGs:"+scgCount);
		System.out.println("Specimens:"+specimenCount);
		System.out.println("Child Specimens:"+childSpecimenCount);
		System.out.println("Total Specimens:"+(specimenCount+childSpecimenCount));
		
		System.out.println("Start Time:"+startTime);
		System.out.println("End Time:"+new Date());
		System.out.println("-----------------------------------------------------------------------");
	}
	/**
	 * Inserts all data.
	 * @throws ApplicationException
	 */
	private void insertAllData()throws ApplicationException
	{
		insertCollectionProtocols();
		insertSites();
		println("Collection Protocol Insertion completed....inserting Participant");
		for (int participantCnt = 0 ; participantCnt < NO_OF_PARTICIPANT;participantCnt++)
		{
			Participant participant = createParticipant();
			participant = (Participant)appService.createObject(participant);
			println("inserted Participant:" + participantCnt + ":Id:"+participant.getId());
			// registering participant for some Protocol
			participantCount++;

			CollectionProtocolRegistration collectionProtocolRegistration = registerParticipant(participant);
			println("Participant Registered....Registration Id:"+collectionProtocolRegistration.getId());
			insertSCGForRegistration(collectionProtocolRegistration);
			
		}
		endTime =new Date();
	}
	
	/**
	 * will insert minimum MIN_SCG_PER_PARTICIPANT SCG for participant.
	 * @param collectionProtocolRegistration
	 * @throws ApplicationException
	 */
	private void insertSCGForRegistration(CollectionProtocolRegistration collectionProtocolRegistration) throws ApplicationException
	{
		int noOfGroups = getNumber(NO_OF_SCG_PER_PARTICIPANT-MIN_SCG_PER_PARTICIPANT)+MIN_SCG_PER_PARTICIPANT;
		for (int index = 0;index<noOfGroups;index++)
		{
			SpecimenCollectionGroup specimenCollectionGroup = createSpecimenCollectionGroup(collectionProtocolRegistration);
			specimenCollectionGroup = (SpecimenCollectionGroup)appService.createObject(specimenCollectionGroup);
			println("inserted SCG:"+scgCount+":with Id:"+specimenCollectionGroup.getId());
			scgCount++;
			insertSpecimenForSCG(specimenCollectionGroup);
		}
	}
	
	/**
	 * will insert minimum MIN_SPECIMEN_PER_SCG Specimen for SCG.
	 * @param specimenCollectionGroup
	 * @throws ApplicationException 
	 */
	private void insertSpecimenForSCG(SpecimenCollectionGroup specimenCollectionGroup) throws ApplicationException
	{
		int noOfSpecimens = getNumber(NO_OF_SPECIMEN_PER_SCG-MIN_SPECIMEN_PER_SCG)+MIN_SPECIMEN_PER_SCG;
		for (int index = 0;index<noOfSpecimens;index++)
 		{
			Specimen specimen = createSpecimen(specimenCollectionGroup);
			SpecimenCharacteristics specimenCharacteristics = (SpecimenCharacteristics)appService.createObject(specimen.getSpecimenCharacteristics());
			specimen.setSpecimenCharacteristics(specimenCharacteristics);
			println("inserted Specimen:"+specimenCount+":id:"+specimen.getId()+":"+specimen.getType()+":"+specimen.getSpecimenCharacteristics().getId());
			specimen = (Specimen) appService.createObject(specimen);
			specimenCount++;
			insertChildSpecimen(specimen);
			System.out.println("Status:"+participantCount+":"+scgCount+":"+specimenCount+":"+childSpecimenCount);
		}
	}
	/**
	 * Insert random number of specimen childs for the given parent specimen.
	 * @param parentSpecimen
	 * @throws ApplicationException
	 */
	private void insertChildSpecimen(Specimen parentSpecimen) throws ApplicationException
	{
		if (getNumber(3)==2) // creates child for only 1/3 specimens.
		{
			int numberOfChildren = getNumber(MAX_CHILD_SPECIMEN);
			for (int index=0;index<numberOfChildren;index++)
			{
				Specimen specimen = createSpecimen(parentSpecimen.getSpecimenCollectionGroup());
				specimen.setParentSpecimen(parentSpecimen);
				SpecimenCharacteristics specimenCharacteristics = (SpecimenCharacteristics)appService.createObject(specimen.getSpecimenCharacteristics());
				specimen.setSpecimenCharacteristics(specimenCharacteristics);
				
				specimen = (Specimen) appService.createObject(specimen);
				println("Child Specimen Added:"+childSpecimenCount+"id:"+specimen.getId()+":"+specimen.getLabel());
				childSpecimenCount++;
				parentSpecimen = specimen.getParentSpecimen();
				if (getNumber(4)==2) // create child to child specimen for 1/4 specimens.
					insertChildSpecimen(specimen);
			}
		}
	}
	/**
	 * Create specimen object.
	 * @return Specimen
	 */
	public Specimen createSpecimen(SpecimenCollectionGroup specimenCollectionGroup)
	{
		
		Specimen specimen = getSpecimenInstance();
		specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		
		specimen.setLabel("spec" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		specimen.setBarcode("bar" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		
		specimen.setAvailable(new Boolean(true));
		specimen.setActivityStatus("Active");
		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(tissueSides[getNumber(tissueSides.length)]);
		specimenCharacteristics.setTissueSite(tissueSites[getNumber(tissueSites.length)]);
		specimen.setSpecimenCharacteristics(specimenCharacteristics);
		specimen.setPathologicalStatus(pathologicalStatus[getNumber(pathologicalStatus.length)]);

		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimen.setInitialquantity(quantity);
		specimen.setAvailableQuantity(quantity);
		specimen.setComment("");
//		specimen.setLineage("Aliquot");

		// Is virtually located
		specimen.setStorageContainer(null); 
		specimen.setPositionDimensionOne(null);
		specimen.setPositionDimensionTwo(null);
		
		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName("eid" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		externalIdentifier.setValue(externalIdentifier.getName());
		externalIdentifier.setSpecimen(specimen);
		
		externalIdentifierCollection.add(externalIdentifier);
		specimen.setExternalIdentifierCollection(externalIdentifierCollection);
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		collectionEventParameters.setComments("");
//		User user = new User();
//		user.setId(new Long(1));
	 //	collectionEventParameters.setId(new Long(0));
		User user = userList.get(getNumber(userList.size()));	
		collectionEventParameters.setUser(user);
		
//		try
//		{
//			collectionEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
//					.datePattern("08/15/1975")));
//					
//		}
//		catch (ParseException e1)
//		{
//			System.out.println(" exception in APIDemo");
//			e1.printStackTrace();
//		}
		
		collectionEventParameters.setTimestamp(new Date());
		collectionEventParameters.setContainer("No Additive Vacutainer");
		collectionEventParameters.setCollectionProcedure("Needle Core Biopsy");
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setUser(user);
		//receivedEventParameters.setId(new Long(0));
		
//		try
//		{
//			System.out.println("--- Start ---- 10");
//			receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1975", Utility
//					.datePattern("08/15/1975")));
//		}
//		catch (ParseException e)
//		{
//			System.out.println("APIDemo");
//			e.printStackTrace();
//		}
		receivedEventParameters.setTimestamp(new Date());
		receivedEventParameters.setReceivedQuality("Acceptable");
		receivedEventParameters.setComments("");
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		specimen.setSpecimenEventCollection(specimenEventCollection);

//		Biohazard biohazard = new Biohazard();
//		biohazard.setName("Biohazard1");
//		biohazard.setType("Toxic");
//		biohazardCollection.add(biohazard);
		Collection biohazardCollection = new HashSet();
		specimen.setBiohazardCollection(biohazardCollection);
		return specimen;
	}
	
	/**
	 * To create empty instance of specimen of type molecular/tissue/cell/fluid.
	 * @return
	 */
	Specimen getSpecimenInstance()
	{
		Specimen specimen= null;
		int classType = getNumber(4);
		switch (classType)
		{
			case molecularSpecimen:
				specimen = new MolecularSpecimen();
				specimen.setType(molecularSpecimenTypes[getNumber(molecularSpecimenTypes.length)]);
				((MolecularSpecimen)specimen).setConcentrationInMicrogramPerMicroliter(randomGenerator.nextDouble());
				break;
			case tissueSpecimen:
				specimen = new TissueSpecimen();
				specimen.setType(tissueSpecimenTypes[getNumber(tissueSpecimenTypes.length)]);
				break;
			case cellSpecimen:
				specimen = new CellSpecimen();
				specimen.setType(cellSpecimenTypes[getNumber(cellSpecimenTypes.length)]);
				break;
			case fluidSpecimen:
				specimen = new FluidSpecimen();
				specimen.setType(fluidSpecimenTypes[getNumber(fluidSpecimenTypes.length)]);
				break;
			
		}
		return specimen;
	}
	/**
	 * To create Specimen Collection group object.
	 * @return SpecimenCollectionGroup
	 */
	public SpecimenCollectionGroup createSpecimenCollectionGroup(CollectionProtocolRegistration collectionProtocolRegistration)
	{
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();

		specimenCollectionGroup.setName("scg" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		specimenCollectionGroup.setClinicalDiagnosis(clinicalDiagnosis[getNumber(clinicalDiagnosis.length)]);
		specimenCollectionGroup.setClinicalStatus(clinicalStatus[getNumber(clinicalStatus.length)]);
		specimenCollectionGroup.setActivityStatus("Active");

		Site site = sites[getNumber(sites.length)];
		specimenCollectionGroup.setSpecimenCollectionSite(site);

		CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection().iterator().next();
		specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocolEvent);
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);

		return specimenCollectionGroup;
	}

	/**
	 * TO register participant to one protocol.
	 * @return CollectionProtocolRegistration
	 * @throws ApplicationException 
	 */
	public CollectionProtocolRegistration registerParticipant(Participant participant) throws ApplicationException
	{
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		CollectionProtocol collectionProtocol = collectionProtocols[getNumber(collectionProtocols.length)];
		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		collectionProtocolRegistration.setParticipant(participant);

		collectionProtocolRegistration.setProtocolParticipantIdentifier("cpReg"+appender + UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocolRegistration.setActivityStatus("Active");
		/*
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		*/
		collectionProtocolRegistration = (CollectionProtocolRegistration)appService.createObject(collectionProtocolRegistration);
		
		return collectionProtocolRegistration;
	}
	
	/**
	 * To insert Collection Protocols.
	 * @throws ApplicationException
	 */
	private void insertCollectionProtocols() throws ApplicationException
	{
		protocolCoordinator = insertAdminUser();
		principleInvestigator = insertAdminUser();
		for (int index = 0 ; index < NO_OF_COLLECTION_PROTOCOL;index++)
		{
			
			CollectionProtocol collectionProtocol = insertCollectionProtocol();
			println("inserted cp:" + index+"/"+collectionProtocols.length+":Id:"+collectionProtocol.getId());
			collectionProtocols[index] = collectionProtocol;
			cpCount++;
		}
	}
	
	/**
	 * To insert site objects.
	 * @throws ApplicationException
	 */
	private void insertSites() throws ApplicationException
	{
		for (int index = 0 ; index < NO_OF_SITES;index++)
		{
			Site site = createSite();
			site = (Site)appService.createObject(site);
			println("inserted Site:" + index+"/"+sites.length+":id:"+site.getId());
			sites[index] = site;
		}
	}
	
	/**
	 * To create site object.
	 * @return Site
	 */
	public Site createSite()
	{
		Site siteObj = new Site();
//		User userObj = new User();
//		userObj.setId(new Long(1));
		User userObj = userList.get(getNumber(userList.size()));

		siteObj.setEmailAddress(appender + UniqueKeyGeneratorUtil.getUniqueKey()+"@admin.com");
		siteObj.setName("sit" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
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
	 * To create & insert collection protocol object.
	 * @return CollectionProtocol
	 * @throws ApplicationException 
	 */
	public CollectionProtocol insertCollectionProtocol() throws ApplicationException
	{
//		User user = insertAdminUser();
		
		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setAliqoutInSameContainer(new Boolean(true));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777"+appender);
		collectionProtocol.setTitle("cp"+appender + UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setShortTitle(collectionProtocol.getTitle());
		
//		try
//		{
//			collectionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
//					.datePattern("08/15/1975")));
//		}
//		catch (ParseException e)
//		{
//			e.printStackTrace();
//		}

		collectionProtocol.setStartDate(new Date());
		Collection collectionProtocolEventCollectionSet = new HashSet();
	 
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		collectionProtocolEvent = (CollectionProtocolEvent) appService.createObject(collectionProtocolEvent); 
		collectionProtocolEvent.setClinicalStatus(clinicalStatus[getNumber(clinicalStatus.length)]);
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));
	 

		Collection specimenRequirementCollection = new HashSet();
//		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
//		specimenRequirement.setSpecimenClass("Molecular");
//		specimenRequirement.setSpecimenType("DNA");
//		specimenRequirement.setTissueSite("Placenta");
//		specimenRequirement.setPathologyStatus("Malignant");
//		Quantity quantity = new Quantity();
//		quantity.setValue(new Double(10));
//		specimenRequirement.setQuantity(quantity);
		
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		int classType = getNumber(4);
		String specimenClass,specimenType;
		switch (classType)
		{
			case molecularSpecimen:
				specimenRequirement.setSpecimenClass("Molecular");
				specimenRequirement.setSpecimenType(molecularSpecimenTypes[getNumber(molecularSpecimenTypes.length)]);
				break;
			case tissueSpecimen:
				specimenRequirement.setSpecimenClass("Tissue");
				specimenRequirement.setSpecimenType(tissueSpecimenTypes[getNumber(tissueSpecimenTypes.length)]);
				break;
			case cellSpecimen:
				specimenRequirement.setSpecimenClass("Cell");
				specimenRequirement.setSpecimenType(cellSpecimenTypes[getNumber(cellSpecimenTypes.length)]);
				break;
			case fluidSpecimen:
				specimenRequirement.setSpecimenClass("Fluid");
				specimenRequirement.setSpecimenType(fluidSpecimenTypes[getNumber(fluidSpecimenTypes.length)]);
				break;
			
		}
		specimenRequirement.setTissueSite(tissueSites[getNumber(tissueSites.length)]);
		specimenRequirement.setPathologyStatus(pathologicalStatus[getNumber(pathologicalStatus.length)]);
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimenRequirement.setQuantity(quantity);
		specimenRequirement  =(SpecimenRequirement)appService.createObject(specimenRequirement);
		specimenRequirementCollection.add(specimenRequirement);
		
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);

		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

		collectionProtocol.setPrincipalInvestigator(principleInvestigator);
		
//		User protocolCordinator = insertAdminUser();
		Collection protocolCordinatorCollection = new HashSet();
		protocolCordinatorCollection.add(protocolCoordinator);
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		
		collectionProtocol = (CollectionProtocol)appService.createObject(collectionProtocol);
		return collectionProtocol;
	}
	/**
	 * To create & insert User object
	 * @return reference to user the user object.
	 * @throws ApplicationException
	 */
	public User insertAdminUser() throws ApplicationException
	{
		// creating institution.
		Institution institution = new Institution();
		institution.setName("inst"+ appender+ UniqueKeyGeneratorUtil.getUniqueKey());
    	institution =  (Institution) appService.createObject(institution);
    			
    	// creating Department.
    	Department department = new Department();
		department.setName("dpt" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		department = (Department) appService.createObject(department);
		
		// creating cancerResearchGroup
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("crg" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		cancerResearchGroup = (CancerResearchGroup)appService.createObject(cancerResearchGroup);
		
		// creating User.
		User userObj = new User();
		userObj.setEmailAddress(appender+UniqueKeyGeneratorUtil.getUniqueKey()+ "@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" +appender+ UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + appender + UniqueKeyGeneratorUtil.getUniqueKey());

		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("D.C.");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");		
		userObj.setAddress(address);

		userObj.setInstitution(institution);
		userObj.setDepartment(department);
		userObj.setCancerResearchGroup(cancerResearchGroup);

		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");
		userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);		
		
		userObj = (User) appService.createObject(userObj);
		userList.add(userObj);
		return userObj;
	}
	/**
	 * create Participant Object.
	 * @return Participant
	 */
	private Participant createParticipant()
	{
		Participant participant = new Participant();
		participant.setLastName("last" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("frst" + appender + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setMiddleName("mdl" + appender + UniqueKeyGeneratorUtil.getUniqueKey());

		String vitalSatus = vitalStatus[getNumber(vitalStatus.length)];
		participant.setVitalStatus(vitalSatus);
		String gender = genders[getNumber(genders.length)];
		participant.setGender(gender);
		participant.setSexGenotype("XX");

		Collection raceCollection = new HashSet();
		String race = races[getNumber(races.length)];
		raceCollection.add(race);
		race = races[getNumber(races.length)];
		raceCollection.add(race);
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");

//		Collection participantMedicalIdentifierCollection = new HashSet();
//		int medicalIdNo = getNumber(MAX_PARTICIPANT_MEDICAL_IDS);
//		for (int i=0;i<=medicalIdNo;i++)
//		{
//			ParticipantMedicalIdentifier pm = new ParticipantMedicalIdentifier();
//			pm.setMedicalRecordNumber("RN:"+UniqueKeyGeneratorUtil.getUniqueKey());
//			pm.setSite(sites[getNumber(sites.length)]);
//			participantMedicalIdentifierCollection.add(pm);
//			pm.setParticipant(participant);
//		}
//		
//		participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);
		return participant;
	}
	
	/**
	 * TO generate random number ranging from 0 to max.
	 * @param max
	 * @return
	 */
	private int getNumber(int max)
	{
		int n = randomGenerator.nextInt();
		if (n<0) n = -n;
		return n%max;
	}
	/**
	 * To print message on console.
	 * @param message
	 */
	private void println(String message)
	{
		if (DEBUG)
			System.out.println(message);
	}
}
