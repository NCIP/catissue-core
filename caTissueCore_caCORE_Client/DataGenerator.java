import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
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

public class DataGenerator
{
	public static Map dataModelObjectMap = new HashMap();
	private static String ADMIN_ROLE_ID = "1";
	private static String SUPERVISOR_ROLE_ID = "2";
	private static String TECHNICIAN_ROLE_ID = "3";
	private static String SCIENTIST_ROLE_ID = "7";
	
//	public void init()
//	{
//		initDepartment();
//		initInstitution();
//		initCancerResearchGroup();
//		initAdminUser();
//		initSupervisorUser();
//		initTechnicianUsers();
//		initScientistUsers();
//	}
	
	private Address initAddress()
	{
		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("D.C.");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("21222324");
		address.setFaxNumber("21222324");	
		return address;
	}
	
	/**
	 *Initialize Department
	 */
	public void initDepartment()
	{
		Department department = new Department();
		department.setName("Pathology");		
		dataModelObjectMap.put("DEPARTMENT",department);
	}
	
	/**
	 * Initialize CancerResearchGroup
	 */
	public void initCancerResearchGroup()
	{
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("Siteman Cancer Center");
		dataModelObjectMap.put("CANCERRESEARCHGROUP",cancerResearchGroup);
	}

	/**
	 * Initialize Institution
	 */
	public void initInstitution()
	{
		Institution institution = new Institution();
		institution.setName("Washington University");
		dataModelObjectMap.put("INSTITUTION",institution);
	}
	
	/**
	 * Initialize BioHazard
	 */
	public void initBioHazard()
	{
		Biohazard bioHazard = new Biohazard();
		bioHazard.setName("Toxic Biohazard");
		bioHazard.setComment("NueroToxicProtein");		
		bioHazard.setType("Toxic");		
		
		dataModelObjectMap.put("BIOHAZARD",bioHazard);
	}
	
		
	private void initCommonUser(User userObj)
	{	
		Address address = initAddress();
		userObj.setAddress(address);
		
		userObj.setActivityStatus("Active");		
		userObj.setPageOf(Constants.PAGEOF_USER_ADMIN);	
		
		Institution institution = (Institution) dataModelObjectMap.get("INSTITUTION");
		userObj.setInstitution(institution);
		
		Department department = (Department)dataModelObjectMap.get("DEPARTMENT");
		userObj.setDepartment(department);

		CancerResearchGroup cancerResearchGroup = (CancerResearchGroup)dataModelObjectMap.get("CANCERRESEARCHGROUP");
		userObj.setCancerResearchGroup(cancerResearchGroup);
	}
	
	public void initAdminUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("watsonm@pathology.wustl.edu");
		userObj.setLoginName("watsonm@pathology.wustl.edu");
		userObj.setLastName("Watson");
		userObj.setFirstName("Mark");
		userObj.setRoleId(ADMIN_ROLE_ID);	
		initCommonUser(userObj);
		
		dataModelObjectMap.put("ADMINUSER",userObj);		
	}
	
	public void initSupervisorUser()
	{
		User userObj = new User();
		userObj.setEmailAddress("rakesh@wustl.edu");
		userObj.setLoginName("rakesh@wustl.edu");
		userObj.setLastName("Nagarajan");
		userObj.setFirstName("Rakesh");
		userObj.setRoleId(SUPERVISOR_ROLE_ID);
		initCommonUser(userObj);
		
		dataModelObjectMap.put("SUPERVISORUSER",userObj);		
	}
	
	public void initTechnicianUsers()
	{
		User technicianUser1 = new User();
		technicianUser1.setEmailAddress("jstone@pathology.wustl.edu");
		technicianUser1.setLoginName("jstone@pathology.wustl.edu");
		technicianUser1.setLastName("Stone");
		technicianUser1.setFirstName("Janet");
		technicianUser1.setRoleId(TECHNICIAN_ROLE_ID);	
		initCommonUser(technicianUser1);
		
		User technicianUser2 = new User();
		technicianUser2.setEmailAddress("rmeyer@pathology.wustl.edu");
		technicianUser2.setLoginName("rmeyer@pathology.wustl.edu");
		technicianUser2.setLastName("Meyer");
		technicianUser2.setFirstName("Rekha");
		technicianUser2.setRoleId(TECHNICIAN_ROLE_ID);	
		initCommonUser(technicianUser2);
		
		dataModelObjectMap.put("TECHNICIANUSER1",technicianUser1);
		dataModelObjectMap.put("TECHNICIANUSER2",technicianUser2);
	}
	
	public void initScientistUsers()
	{
		User scientistUser1 = new User();
		scientistUser1.setEmailAddress("schmandtlm@upmc.edu");
		scientistUser1.setLoginName("schmandtlm@upmc.edu");
		scientistUser1.setLastName("Schmandt");
		scientistUser1.setFirstName("Linda");
		scientistUser1.setRoleId(SCIENTIST_ROLE_ID);
		initCommonUser(scientistUser1);
		
		User scientistUser2 = new User();
		scientistUser2.setEmailAddress("mirandal@uphs.upenn.edu");
		scientistUser2.setLoginName("mirandal@uphs.upenn.edu");
		scientistUser2.setLastName("Miranda");
		scientistUser2.setFirstName("Lisa");
		scientistUser2.setRoleId(SCIENTIST_ROLE_ID);
		initCommonUser(scientistUser2);
		

		User scientistUser3 = new User();
		scientistUser3.setEmailAddress("Jack.London@mail.jci.tju.edu");
		scientistUser3.setLoginName("Jack.London@mail.jci.tju.edu");
		scientistUser3.setLastName("London");
		scientistUser3.setFirstName("Jack");
		scientistUser3.setRoleId(SCIENTIST_ROLE_ID);
		initCommonUser(scientistUser3);
		
		User scientistUser4 = new User();
		scientistUser4.setEmailAddress("gschadow@regenstrief.org");
		scientistUser4.setLoginName("gschadow@regenstrief.org");
		scientistUser4.setLastName("Schadow");
		scientistUser4.setFirstName("Gunther");
		scientistUser4.setRoleId(SCIENTIST_ROLE_ID);
		initCommonUser(scientistUser4);
		
		dataModelObjectMap.put("SCIENTISTUSER1",scientistUser1);
		dataModelObjectMap.put("SCIENTISTUSER2",scientistUser2);
		dataModelObjectMap.put("SCIENTISTUSER3",scientistUser3);
		dataModelObjectMap.put("SCIENTISTUSER4",scientistUser4);
	}
	
	public void initSite()
	{
		Address address = initAddress();		
		
		Site site1 = new Site();
		User mark = (User) dataModelObjectMap.get("ADMINUSER");
		site1.setEmailAddress("watsonm@pathology.wustl.edu");
		site1.setName("BJH");
		site1.setType("Laboratory");
		site1.setActivityStatus("Active");
		site1.setCoordinator(mark);	
		site1.setAddress(address);	
		
		Site site2 = new Site();
		User gunther = (User) dataModelObjectMap.get("SCIENTISTUSER4");
		site2.setEmailAddress("gschadow@regenstrief.org");
		site2.setName("Indiana");
		site2.setType("Laboratory");
		site2.setActivityStatus("Active");
		site2.setCoordinator(gunther);	
		site2.setAddress(address);	
		
		dataModelObjectMap.put("SITE1",site1);
		dataModelObjectMap.put("SITE2",site2);
	}
	
	/**
	 * Initialize BioHazard
	 */
	public void initStorageType()
	{
		StorageType box = initCommonStorageType("Box",5,5);
		StorageType Rack = initCommonStorageType("Rack",2,3);
		StorageType Freezer = initCommonStorageType("Freezer",1,3);
		
		dataModelObjectMap.put("STORAGE_TYPE_BOX",box);
		dataModelObjectMap.put("STORAGE_TYPE_RACK",Rack);
		dataModelObjectMap.put("STORAGE_TYPE_FREEZER",Freezer);
	}
	
	
	private StorageType initCommonStorageType(String name, int oneDimensionCapacity, int twoDimensionCapacity)
	{
		StorageType storageTypeObj = new StorageType();
		Capacity capacity = new Capacity();

		storageTypeObj.setName(name);
		storageTypeObj.setDefaultTempratureInCentigrade(new Double(-10));
		storageTypeObj.setActivityStatus("Active");
		storageTypeObj.setOneDimensionLabel("Dimension1");
		storageTypeObj.setTwoDimensionLabel("Dimension2");

		capacity.setOneDimensionCapacity(oneDimensionCapacity);
		capacity.setTwoDimensionCapacity(twoDimensionCapacity);
		storageTypeObj.setCapacity(capacity);		

		Collection holdsStorageTypeCollection = new HashSet();
		StorageType holdsStorageType = new StorageType();
		holdsStorageType.setId(new Long(1));		
		holdsStorageTypeCollection.add(holdsStorageType);
		storageTypeObj.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
		
		Collection holdsSpecimenClassCollection = new HashSet();
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");
		storageTypeObj.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);
		
		return storageTypeObj;
	}

	/**
	 * Initialize BioHazard
	 */
	public void initCollectionProtocol()
	{
		Collection consentTierColl1 = new HashSet();
		ConsentTier c1 = new ConsentTier();
		c1.setStatement("Consent for aids research");
		consentTierColl1.add(c1);
		ConsentTier c2 = new ConsentTier();
		c2.setStatement("Consent for cancer research");
		consentTierColl1.add(c2);		
		ConsentTier c3 = new ConsentTier();
		c3.setStatement("Consent for Tb research");
		consentTierColl1.add(c3);
		
		CollectionProtocol cp1 = initCommonCollectionProtocol("Generic Collection Protocol",null);
		CollectionProtocol cp2 = initCommonCollectionProtocol("Cancer Study Collection Protocol",null);
		CollectionProtocol cp3 = initCommonCollectionProtocol("Aids Study Collection Protocol",consentTierColl1);
		
		dataModelObjectMap.put("COLLECTION_PROTOCOL1",cp1);
		dataModelObjectMap.put("COLLECTION_PROTOCOL2",cp2);
		dataModelObjectMap.put("COLLECTION_PROTOCOL3",cp3);
	}
	
	/**
	 * @return CollectionProtocol
	 */
	private CollectionProtocol initCommonCollectionProtocol(String title,Collection consentTierColl)
	{
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setConsentTierCollection(consentTierColl);
		collectionProtocol.setAliqoutInSameContainer(new Boolean(false));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("1111");
		collectionProtocol.setTitle(title);
		collectionProtocol.setShortTitle(title);
		
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
		CollectionProtocolEvent collectionProtocolEvent = initCollectionProtocolEvent(); 
		collectionProtocolEvent.setClinicalStatus("Not Specified");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(0));
		Collection specimenRequirementCollection = new HashSet();		
		SpecimenRequirement specimenRequirement  = initSpecimenRequirement("Tissue","Fixed Tissue Block",1);		
		specimenRequirementCollection.add(specimenRequirement);		
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenRequirementCollection);
		collectionProtocolEventCollectionSet.add(collectionProtocolEvent);
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventCollectionSet);

	
		User jack = (User) dataModelObjectMap.get("SCIENTISTUSER3");
		collectionProtocol.setPrincipalInvestigator(jack);		

		User gunther = (User) dataModelObjectMap.get("SCIENTISTUSER4");
		Collection protocolCordinatorCollection = new HashSet();
		protocolCordinatorCollection.add(gunther);		
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		
		return collectionProtocol;
	}

	
	private SpecimenRequirement initSpecimenRequirement(String specimenClass, String specimenType,int intquantity)
	{
		SpecimenRequirement specimenRequirement = new SpecimenRequirement();
		specimenRequirement.setSpecimenClass(specimenClass);
		specimenRequirement.setSpecimenType(specimenType);
		specimenRequirement.setTissueSite("Not Specified");
		specimenRequirement.setPathologyStatus("Not Specified");
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(intquantity));
		specimenRequirement.setQuantity(quantity);
		return specimenRequirement;
	}
	
	private CollectionProtocolEvent initCollectionProtocolEvent()
	{
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();		
		return collectionProtocolEvent;
	}
	
	/**
	 * @return StorageContainer
	 */
	public void initStorageContainerForFreezer()
	{
		Site site = (Site) dataModelObjectMap.get("SITE1");		
		StorageType storageType = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_FREEZER");
		
		String containerName = site.getName() + "_" + storageType.getName() + "_" + "1";
		StorageContainer storageContainerForSpecimen = initCommonStorageContainerForSpecimen(containerName, storageType, null, site);		
		dataModelObjectMap.put("STORAGE_CONTAINER_FREEZER_1", storageContainerForSpecimen);	
		
					
		containerName = site.getName() + "_" + storageType.getName() + "_" + "2";
		StorageContainer storageContainerForSpecimenArray = initCommonStorageContainerForSpecimenArray(containerName, storageType, null, site);		
		dataModelObjectMap.put("STORAGE_CONTAINER_FREEZER_2", storageContainerForSpecimenArray);
		
	}
	
	/**
	 * @return StorageContainer
	 */
	public void initStorageContainerForRack()
	{
		StorageType storageType = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_RACK");
		StorageType freezerStorageType = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_FREEZER");		
		
		Integer oneDimensionCapacity = freezerStorageType.getCapacity().getOneDimensionCapacity();
		Integer twoDimensionCapacity = freezerStorageType.getCapacity().getTwoDimensionCapacity();
		int noOfRacks = twoDimensionCapacity.intValue()*oneDimensionCapacity.intValue();		
		
		int counter = 1; 
		for(int i=1; i<=noOfRacks; i++)
		{
			StorageContainer parentStorageContainer = (StorageContainer) dataModelObjectMap.get("STORAGE_CONTAINER_FREEZER_1");
			String containerName = parentStorageContainer.getSite().getName() + "_" + storageType.getName() + "_" + counter;
			StorageContainer storageContainer = initCommonStorageContainerForSpecimen(containerName, storageType, parentStorageContainer, null);		
			dataModelObjectMap.put("STORAGE_CONTAINER_RACK_"+counter, storageContainer);
			counter++;
		}	
		
		for(int i=1; i<=noOfRacks; i++)
		{
			StorageContainer parentStorageContainer = (StorageContainer) dataModelObjectMap.get("STORAGE_CONTAINER_FREEZER_2");
			String containerName = parentStorageContainer.getSite().getName() + "_" + storageType.getName() + "_" + counter;
			StorageContainer storageContainer = initCommonStorageContainerForSpecimenArray(containerName, storageType, parentStorageContainer, null);		
			dataModelObjectMap.put("STORAGE_CONTAINER_RACK_"+counter, storageContainer);
			counter++;
		}	
		
	}
	
	/**
	 * @return StorageContainer
	 */
	public void initStorageContainerForBox()
	{
		StorageType box = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_BOX");	
		StorageType rack = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_RACK");
		StorageType freezer = (StorageType) dataModelObjectMap.get("STORAGE_TYPE_FREEZER");	
		
		Integer oneDimensionCapacity = freezer.getCapacity().getOneDimensionCapacity();
		Integer twoDimensionCapacity = freezer.getCapacity().getTwoDimensionCapacity();		
		int noOfRacks = twoDimensionCapacity.intValue()*oneDimensionCapacity.intValue();
		
		System.out.println("-----------noOfRacks" + noOfRacks);
		
		oneDimensionCapacity = rack.getCapacity().getOneDimensionCapacity();
		twoDimensionCapacity = rack.getCapacity().getTwoDimensionCapacity();
		int noOfBoxesInOneRack = twoDimensionCapacity.intValue()*oneDimensionCapacity.intValue();
		
		System.out.println("-----------noOfBoxesInOneRack" + noOfBoxesInOneRack);
		
		int counterForBox= 1;
		int counterForRack= 1;
		
		for(int i=1; i<=noOfRacks; i++)
		{
			StorageContainer parentStorageContainer = (StorageContainer) dataModelObjectMap.get("STORAGE_CONTAINER_RACK_"+counterForRack);
			counterForRack++;
			for(int j=1; j<=noOfBoxesInOneRack; j++)
			{
				String containerName = parentStorageContainer.getSite().getName() + "_" + box.getName() + "_" + counterForBox;
				StorageContainer storageContainer = initCommonStorageContainerForSpecimen(containerName, box, parentStorageContainer, null);		
				dataModelObjectMap.put("STORAGE_CONTAINER_BOX_"+counterForBox, storageContainer);
				System.out.println("-----------dataModelObjectMap" + counterForBox + storageContainer);
				counterForBox++;
			}
		}	
		
		for(int i=1; i<=noOfRacks; i++)
		{
			StorageContainer parentStorageContainer = (StorageContainer) dataModelObjectMap.get("STORAGE_CONTAINER_RACK_"+counterForRack);
			counterForRack++;
			for(int j=1; j<=noOfBoxesInOneRack; j++)
			{
				String containerName = parentStorageContainer.getSite().getName() + "_" + box.getName() + "_" + counterForBox;
				StorageContainer storageContainer = initCommonStorageContainerForSpecimen(containerName, box, parentStorageContainer, null);		
				dataModelObjectMap.put("STORAGE_CONTAINER_BOX_"+counterForBox, storageContainer);
				System.out.println("-----------dataModelObjectMap" + counterForBox + storageContainer);
				counterForBox++;
			}
		}		
	}
	
	private StorageContainer initCommonStorageContainerForSpecimen(String name,StorageType storageType, StorageContainer parentStorageContainer, Site site)
	{
		StorageContainer storageContainer = initCommonStorageContainer(name, storageType, parentStorageContainer, site);		
		Collection holdsSpecimenClassCollection = new HashSet();		
		holdsSpecimenClassCollection.add("Tissue");
		holdsSpecimenClassCollection.add("Fluid");
		holdsSpecimenClassCollection.add("Molecular");
		holdsSpecimenClassCollection.add("Cell");		
		storageContainer.setHoldsSpecimenClassCollection(holdsSpecimenClassCollection);		
		return storageContainer;
	}
	
	private StorageContainer initCommonStorageContainerForSpecimenArray(String name,StorageType storageType, StorageContainer parentStorageContainer, Site site)
	{
		StorageContainer storageContainer = initCommonStorageContainer(name, storageType, parentStorageContainer, site);		
		Collection holdsSpArrayTypeCollection = new HashSet();		
		SpecimenArrayType specimenArrayType  = new SpecimenArrayType();
		specimenArrayType.setId(new Long(2));
		holdsSpArrayTypeCollection.add(specimenArrayType);
		storageContainer.setHoldsSpecimenArrayTypeCollection(holdsSpArrayTypeCollection);		
		return storageContainer;		
	}
	
	private StorageContainer initCommonStorageContainer(String name,StorageType storageType, StorageContainer parentStorageContainer, Site site)
	{
		StorageContainer storageContainer = new StorageContainer();
		storageContainer.setName(name);

		storageContainer.setStorageType(storageType);		
		storageContainer.setSite(site);

		Integer conts = new Integer(1);
		storageContainer.setNoOfContainers(conts);
		storageContainer.setTempratureInCentigrade(storageType.getDefaultTempratureInCentigrade());
		storageContainer.setBarcode(name);

		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(storageType.getCapacity().getOneDimensionCapacity());
		capacity.setTwoDimensionCapacity(storageType.getCapacity().getTwoDimensionCapacity());
		storageContainer.setCapacity(capacity);				
		
		Collection collectionProtocolCollection = new HashSet();		
		storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

		Collection holdsStorageTypeCollection = new HashSet();
		StorageType holdsStorageType = new StorageType();
		holdsStorageType.setId(new Long(1));		
		holdsStorageTypeCollection.add(holdsStorageType);		
		storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);
		
		storageContainer.setParent(parentStorageContainer);
		
		storageContainer.setActivityStatus("Active");
		storageContainer.setFull(Boolean.valueOf(false));		
		
		return storageContainer;
	}
	
	
	/**
	 * @return SpecimenArrayType
	 */
	public void initSpecimenArrayType()
	{		
		SpecimenArrayType specimenArrayType1 = initCommonSpecimenArrayType("Molecular_SAT","Molecular",12,8);
		specimenArrayType1.getSpecimenTypeCollection().add("DNA");
		
		SpecimenArrayType specimenArrayType2 = initCommonSpecimenArrayType("Tissue_SAT","Tissue",4,3);
		specimenArrayType2.getSpecimenTypeCollection().add("Frozen Tissue Slide");			
		
		dataModelObjectMap.put("SPECIMEN_ARRAY_TYPE_1", specimenArrayType1);
		dataModelObjectMap.put("SPECIMEN_ARRAY_TYPE_2", specimenArrayType2);
	}
	
	private SpecimenArrayType initCommonSpecimenArrayType(String name, String type, int oneDimensionCapacity, int twoDimensionCapacity)
	{
		SpecimenArrayType specimenArrayType = new SpecimenArrayType();
		specimenArrayType.setName(name);
		specimenArrayType.setSpecimenClass(type);
		Collection specimenTypeCollection = new HashSet();				
		specimenArrayType.setSpecimenTypeCollection(specimenTypeCollection);		
		specimenArrayType.setComment("");
		Capacity capacity = new Capacity();
		capacity.setOneDimensionCapacity(new Integer(oneDimensionCapacity));
		capacity.setTwoDimensionCapacity(new Integer(twoDimensionCapacity));
		specimenArrayType.setCapacity(capacity);		
		specimenArrayType.setActivityStatus("Active");		
		return specimenArrayType;
	}
	
	/**
	 * Initialize BioHazard
	 */
	public void initDistributionProtocol()
	{
		User linda = (User) dataModelObjectMap.get("SCIENTISTUSER1");
		User lisa = (User) dataModelObjectMap.get("SCIENTISTUSER2");	
		
		DistributionProtocol dp1 = initCommonDistributionProtocol("Generic Distribution Protocol",linda);		
		DistributionProtocol dp2 = initCommonDistributionProtocol("Cancer Study Distribution Protocol",lisa);
		
		dataModelObjectMap.put("DISTRIBUTION_PROTOCOL1",dp1);
		dataModelObjectMap.put("DISTRIBUTION_PROTOCOL2",dp2);
	}
	
	
	/**
	 * @return DistributionProtocol
	 */
	private DistributionProtocol initCommonDistributionProtocol(String title, User user)
	{
		DistributionProtocol distributionProtocol = new DistributionProtocol();		
		distributionProtocol.setDescriptionURL("");
		distributionProtocol.setActivityStatus("Active");
		distributionProtocol.setEndDate(null);
		distributionProtocol.setEnrollment(null);
		distributionProtocol.setIrbIdentifier("1111");
		distributionProtocol.setTitle(title);
		distributionProtocol.setShortTitle(title);
		
		try
		{
			distributionProtocol.setStartDate(Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		Collection specimenRequirementCollection = new HashSet();		
		SpecimenRequirement specimenRequirement  = initSpecimenRequirement("Tissue","Fixed Tissue Block",1);		
		specimenRequirementCollection.add(specimenRequirement);		
		distributionProtocol.setSpecimenRequirementCollection(specimenRequirementCollection);
		
		distributionProtocol.setPrincipalInvestigator(user);
		
		return distributionProtocol;
	}
	
	
	private Participant initCommonParticipant(String lName,String fName,String mName,String bDate,String gender,
			String sGenotype,String ethnicity,String ssn,Collection race,String vitalStatus,Collection pMedicalIdentifier)
	{
		
		Participant participant = new Participant();
		participant.setLastName(lName);
		participant.setFirstName(fName);
		participant.setMiddleName(mName);
		try
		{
			participant.setBirthDate(Utility.parseDate(bDate, Utility
					.datePattern(bDate)));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		participant.setGender(gender);
		participant.setSexGenotype(sGenotype);
		participant.setEthnicity(ethnicity);
		participant.setSocialSecurityNumber(ssn);
		participant.setRaceCollection(race);
		participant.setVitalStatus(vitalStatus);
		participant.setDeathDate(null);
		participant.setActivityStatus("Active");
		participant.setParticipantMedicalIdentifierCollection(pMedicalIdentifier);
						
		return participant;
	}
	
	/**
	 * Add participant
	 *
	 */
	public void initParticipant()
	{
		Collection race1 = new HashSet();
		race1.add("White");
		
		Collection race2 = new HashSet();
		race2.add("American Indian or Alaska Native");
		
		Collection race3 = new HashSet();
		race3.add("Asian");
		
		Collection race4 = new HashSet();
		race4.add("Black or African American");
		
		Collection race5 = new HashSet();
		race5.add("Unknown");
		
		ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
		Collection pMedicalIdentifier = new HashSet();
		Site site= (Site) dataModelObjectMap.get("SITE1");
		participantMedicalIdentifier.setSite(site);
		participantMedicalIdentifier.setMedicalRecordNumber("1111");
		//participantMedicalIdentifier.setParticipant();
		pMedicalIdentifier.add(participantMedicalIdentifier);
		
		Participant participant1 = initCommonParticipant("Nash","Linda","Stephen","09/20/1968","Female","Penta X Syndrome","Not Hispanic or Latino","222-22-2222",race1,"Alive",pMedicalIdentifier);
		Participant participant2 = initCommonParticipant("Smith","John","Nichol","08/15/1975","Male","XX","Hispanic or Latino","111-11-1111",race2,"Alive",null);
		Participant participant3 = initCommonParticipant("Williams","Cruise","Shane","04/11/1959","Unknown","XXX","Unknown","333-33-3333",race3,"Unknown",null);
		Participant participant4 = initCommonParticipant("McConnel","Peter","Rorger","04/11/1959","Unspecified","XY","Not Reported","444-44-4444",race4,"Dead",null);
		Participant participant5 = initCommonParticipant("Stephen","Ricky","Neal","09/29/1981","Male","XY","Hispanic or Latino","555-55-5555",race5,"Alive",null);
		
		dataModelObjectMap.put("PARTICIPANT1",participant1);
		dataModelObjectMap.put("PARTICIPANT2",participant2);
		dataModelObjectMap.put("PARTICIPANT3",participant3);
		dataModelObjectMap.put("PARTICIPANT4",participant4);
		dataModelObjectMap.put("PARTICIPANT5",participant5);
	}
	
	
	private CollectionProtocolRegistration initCommonCollectionProtocolRegistration(CollectionProtocol collectionProtocol, Participant participant,
			String signedConsentDate, String signedConsentDoc, User user)
	{
	
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);	
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");
		Collection consentTier = collectionProtocol.getConsentTierCollection();
		try
		{
			collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1999",Utility.datePattern("08/15/1999")));
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
		}
		try
		{
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate(signedConsentDate,Utility.datePattern(signedConsentDate)));
		}
		catch (ParseException e)
		{			
			e.printStackTrace();
		}
		collectionProtocolRegistration.setSignedConsentDocumentURL(signedConsentDoc);
		collectionProtocolRegistration.setConsentWitness(user);
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
		if(consentTierCollection != null)
		{
			Iterator itr = consentTierCollection.iterator();
			while(itr.hasNext())
			{
				ConsentTier consentTiers = (ConsentTier) itr.next();			
				ConsentTierResponse r1 = new ConsentTierResponse();
				r1.setConsentTier(consentTiers);
				r1.setResponse("Yes");
				consentTierResponseCollection.add(r1);
			}	
			collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
		}
		return collectionProtocolRegistration;
	}
	/**
	 * @return CollectionProtocolRegistration
	 */
	public void initCollectionProtocolRegistration()
	{
		CollectionProtocol collectionProtocol1 = (CollectionProtocol)dataModelObjectMap.get("COLLECTION_PROTOCOL1");
		CollectionProtocol collectionProtocol2 = (CollectionProtocol)dataModelObjectMap.get("COLLECTION_PROTOCOL2");
		CollectionProtocol collectionProtocol3 = (CollectionProtocol)dataModelObjectMap.get("COLLECTION_PROTOCOL3");
		
		Participant participant1 = (Participant)dataModelObjectMap.get("PARTICIPANT1");
		Participant participant2 = (Participant)dataModelObjectMap.get("PARTICIPANT2");
		Participant participant3 = (Participant)dataModelObjectMap.get("PARTICIPANT3");
		Participant participant4 = (Participant)dataModelObjectMap.get("PARTICIPANT4");
		User user = new User();
		user.setId(new Long(9));
		
		CollectionProtocolRegistration collectionProtocolRegistration1 = initCommonCollectionProtocolRegistration(collectionProtocol1, participant1,null,null,null);
		CollectionProtocolRegistration collectionProtocolRegistration2 = initCommonCollectionProtocolRegistration(collectionProtocol2, participant2,null,null,null);
		CollectionProtocolRegistration collectionProtocolRegistration3 = initCommonCollectionProtocolRegistration(collectionProtocol2, participant1,null,null,null);
		CollectionProtocolRegistration collectionProtocolRegistration4 = initCommonCollectionProtocolRegistration(collectionProtocol1, participant3,null,null,null);
		CollectionProtocolRegistration collectionProtocolRegistration5 = initCommonCollectionProtocolRegistration(collectionProtocol3, participant4,"02/21/2006","E:\\sitename\\collectionProtocol_participant1.pdf",user);
		
		
		dataModelObjectMap.put("CPR1",collectionProtocolRegistration1);
		dataModelObjectMap.put("CPR2",collectionProtocolRegistration2);
		dataModelObjectMap.put("CPR3",collectionProtocolRegistration3);
		dataModelObjectMap.put("CPR4",collectionProtocolRegistration4);
		dataModelObjectMap.put("CPR5",collectionProtocolRegistration5);
	}

	private SpecimenCollectionGroup initCommonSpecimenCollectionGroup(CollectionProtocolRegistration collectionProtocolRegistration, int iCount,
			String cDiagnosis, String cStatus)
	{
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		Site site= (Site) dataModelObjectMap.get("SITE1");
		
		specimenCollectionGroup.setSpecimenCollectionSite(site);
		specimenCollectionGroup.setClinicalDiagnosis(cDiagnosis);
		specimenCollectionGroup.setClinicalStatus(cStatus);
		specimenCollectionGroup.setActivityStatus("Active");

		CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection().iterator().next();
		//collectionProtocolEvent.setId(new Long(1));
		
		specimenCollectionGroup.setCollectionProtocolEvent(collectionProtocolEvent);
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
		
		specimenCollectionGroup.setName("scg"+iCount);
		
		Collection consentTierStatusCollection = new HashSet();
		Collection consentTierCollection = collectionProtocolRegistration.getCollectionProtocol().getConsentTierCollection();
		if(consentTierCollection != null)
		{
			Iterator itr = consentTierCollection.iterator();
			while(itr.hasNext())
			{
				ConsentTierStatus  consentTierStatus = new ConsentTierStatus();		
				ConsentTier consentTier = (ConsentTier) itr.next();
				consentTierStatus.setConsentTier(consentTier);
				consentTierStatus.setStatus("Yes");
				consentTierStatusCollection.add(consentTierStatus);
			}	
			specimenCollectionGroup.setConsentTierStatusCollection(consentTierStatusCollection);	
		}
		return specimenCollectionGroup;
	}
	/**
	 * SpecimenCollectionGroup
	 * @return
	 */
	public void initSpecimenCollectionGroup()
	{
		
		CollectionProtocolRegistration collectionProtocolRegistration1 = (CollectionProtocolRegistration)dataModelObjectMap.get("CPR1");
		CollectionProtocolRegistration collectionProtocolRegistration2 = (CollectionProtocolRegistration)dataModelObjectMap.get("CPR2");
		CollectionProtocolRegistration collectionProtocolRegistration3 = (CollectionProtocolRegistration)dataModelObjectMap.get("CPR3");
		CollectionProtocolRegistration collectionProtocolRegistration4 = (CollectionProtocolRegistration)dataModelObjectMap.get("CPR4");		
		CollectionProtocolRegistration collectionProtocolRegistration5 = (CollectionProtocolRegistration)dataModelObjectMap.get("CPR5");
		
		SpecimenCollectionGroup specimenCollectionGroup1 = initCommonSpecimenCollectionGroup(collectionProtocolRegistration1,1,"Abdominal fibromatosis","Operative");
		SpecimenCollectionGroup specimenCollectionGroup2 = initCommonSpecimenCollectionGroup(collectionProtocolRegistration2,2,"Not Specified","New Diagnosis");
		SpecimenCollectionGroup specimenCollectionGroup3 = initCommonSpecimenCollectionGroup(collectionProtocolRegistration3,3,"Papillary adenocarcinoma, NOS","New Diagnosis");
		SpecimenCollectionGroup specimenCollectionGroup4 = initCommonSpecimenCollectionGroup(collectionProtocolRegistration4,4,"Pacinian tumor","New Diagnosis");
		SpecimenCollectionGroup specimenCollectionGroup5 = initCommonSpecimenCollectionGroup(collectionProtocolRegistration5,5,"Neuroendocrine carcinoma","New Diagnosis");
		
		dataModelObjectMap.put("SCG1",specimenCollectionGroup1);
		dataModelObjectMap.put("SCG2",specimenCollectionGroup2);
		dataModelObjectMap.put("SCG3",specimenCollectionGroup3);
		dataModelObjectMap.put("SCG4",specimenCollectionGroup4);
		dataModelObjectMap.put("SCG5",specimenCollectionGroup5);
				
	}
	
	private void initCommonSpecimen(SpecimenCollectionGroup specimenCollectionGroup,String sType, String lableBarcode,
			String pathStatus, User user,String date, Specimen specimen,Specimen parentSpecimen,String linegeType,CollectionProtocol cpObject)
	{
		
		specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		specimen.setLabel(lableBarcode);
		specimen.setBarcode(lableBarcode);
		specimen.setType(sType);
		specimen.setAvailable(new Boolean(true));
		specimen.setActivityStatus("Active");
		//Setting Parent specimen	
		specimen.setParentSpecimen(parentSpecimen);
		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide("Left");
		specimenCharacteristics.setTissueSite("Placenta");
		specimen.setSpecimenCharacteristics(specimenCharacteristics);
		specimen.setPathologicalStatus(pathStatus);
		Quantity quantity = new Quantity();
		quantity.setValue(new Double(10));
		specimen.setInitialquantity(quantity);
		specimen.setAvailableQuantity(quantity);		
		specimen.setComment("");
		specimen.setLineage(linegeType);
				
		specimen.setStorageContainer(null); 
		specimen.setPositionDimensionOne(null);
		specimen.setPositionDimensionTwo(null);
		
		Collection externalIdentifierCollection = new HashSet();
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setName(lableBarcode);
		externalIdentifier.setValue(lableBarcode);
		externalIdentifier.setSpecimen(specimen);
		
		externalIdentifierCollection.add(externalIdentifier);
		specimen.setExternalIdentifierCollection(externalIdentifierCollection);
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		collectionEventParameters.setComments("");
			
		collectionEventParameters.setUser(user);
		try
		{
			collectionEventParameters.setTimestamp(Utility.parseDate(date, Utility
					.datePattern(date)));
					
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
			receivedEventParameters.setTimestamp(Utility.parseDate("08/15/1999", Utility
					.datePattern("08/15/1999")));
		}
		catch (ParseException e)
		{
			System.out.println("APIDemo");
			e.printStackTrace();
		}
		receivedEventParameters.setReceivedQuality("Acceptable");
		receivedEventParameters.setComments("");
		Collection specimenEventCollection = new HashSet();
		specimenEventCollection.add(collectionEventParameters);
		specimenEventCollection.add(receivedEventParameters);
		specimen.setSpecimenEventCollection(specimenEventCollection);

		//Biohazard biohazard = (Biohazard)dataModelObjectMap.get("BIOHAZARD");
		//Collection biohazardCollection = new HashSet();
		//biohazardCollection.add(biohazard);
		specimen.setBiohazardCollection(null);
		System.out.println(" -------- end -----------");

		//Setting Consent Tier Response
		Collection consentTierStatusCollection = new HashSet();
		Collection consentTierCollection = cpObject.getConsentTierCollection();
		Collection consentStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		
		if(consentTierCollection != null)
		{
			Iterator itr = consentTierCollection.iterator();
			Iterator itr1 = consentStatusCollection.iterator();
			while(itr.hasNext())
			{
				ConsentTierStatus  consentTierStatus = new ConsentTierStatus();
				ConsentTier consentTier = (ConsentTier) itr.next();
				ConsentTierStatus consentStatus=(ConsentTierStatus)itr1.next();
				consentTierStatus.setConsentTier(consentTier);
				consentTierStatus.setStatus(consentStatus.getStatus());
				consentTierStatusCollection.add(consentTierStatus);
			}	
			specimen.setConsentTierStatusCollection(consentTierStatusCollection);
		}
	}
	/**
	 * @return Specimen
	 */
	public void initSpecimen()
	{
		//without consents 
		SpecimenCollectionGroup specimenCollectionGroup1 = (SpecimenCollectionGroup)dataModelObjectMap.get("SCG1");
		//with consents
		SpecimenCollectionGroup specimenCollectionGroup5 = (SpecimenCollectionGroup)dataModelObjectMap.get("SCG5");
		
		CollectionProtocol cpObject1 = (CollectionProtocol) dataModelObjectMap.get("COLLECTION_PROTOCOL3");
		CollectionProtocol cpObject2 = (CollectionProtocol) dataModelObjectMap.get("COLLECTION_PROTOCOL2");
		
		User linda = (User) dataModelObjectMap.get("SCIENTISTUSER1");
		User lisa = (User) dataModelObjectMap.get("SCIENTISTUSER2");	
		
		TissueSpecimen tissueSpecimen1 = new TissueSpecimen();
		TissueSpecimen tissueSpecimen2 = new TissueSpecimen();
		TissueSpecimen tissueSpecimen3 = new TissueSpecimen();
		CellSpecimen cellSpecimen1 = new CellSpecimen();

		TissueSpecimen tissueSpecimen4 = new TissueSpecimen();
		TissueSpecimen tissueSpecimen5 = new TissueSpecimen();
		TissueSpecimen tissueSpecimen6 = new TissueSpecimen();
		CellSpecimen cellSpecimen2 = new CellSpecimen();
		
		initCommonSpecimen(specimenCollectionGroup1,"Fixed Tissue","Sp1","Malignant",linda,"08/25/2005", tissueSpecimen1,null,"New",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"Microdissected","Sp2","Not Specified",lisa,"08/15/2000",tissueSpecimen2,null,"New",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"Fresh Tissue","Sp3","Malignant, Invasive",linda,"10/10/2001",tissueSpecimen3,null,"New",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"Cryopreserved Cells","Sp4","Not Specified",lisa,"08/18/2004",cellSpecimen1,null,"New",cpObject2);
		
		initCommonSpecimen(specimenCollectionGroup5,"Fixed Tissue","Sp5","Malignant",linda,"08/25/2005", tissueSpecimen4,null,"New",cpObject1);
		initCommonSpecimen(specimenCollectionGroup5,"Microdissected","Sp6","Not Specified",lisa,"08/15/2000",tissueSpecimen5,null,"New",cpObject1);
		initCommonSpecimen(specimenCollectionGroup5,"Fresh Tissue","Sp7","Malignant, Invasive",linda,"10/10/2001",tissueSpecimen6,null,"New",cpObject1);
		initCommonSpecimen(specimenCollectionGroup5,"Cryopreserved Cells","Sp8","Not Specified",lisa,"08/18/2004",cellSpecimen2,null,"New",cpObject1);
		
		
		dataModelObjectMap.put("SPECIMEN1",tissueSpecimen1);
		dataModelObjectMap.put("SPECIMEN2",tissueSpecimen2);
		dataModelObjectMap.put("SPECIMEN3",tissueSpecimen3);
		dataModelObjectMap.put("SPECIMEN4",cellSpecimen1);
		
		dataModelObjectMap.put("SPECIMEN5",tissueSpecimen4);
		dataModelObjectMap.put("SPECIMEN6",tissueSpecimen5);
		dataModelObjectMap.put("SPECIMEN7",tissueSpecimen6);
		dataModelObjectMap.put("SPECIMEN8",cellSpecimen2);
		
	}
	/**
	 * Child Specimen
	 *
	 */
	public void initChildSpecimen()
	{
		SpecimenCollectionGroup specimenCollectionGroup1 = (SpecimenCollectionGroup)dataModelObjectMap.get("SCG1");
		Specimen specimen1= (Specimen)dataModelObjectMap.get("SPECIMEN1");
		Specimen specimen2= (Specimen)dataModelObjectMap.get("SPECIMEN2");
		Specimen specimen3= (Specimen)dataModelObjectMap.get("SPECIMEN3");
		Specimen specimen4= (Specimen)dataModelObjectMap.get("SPECIMEN4");
		
		User linda = (User) dataModelObjectMap.get("SCIENTISTUSER1");
		User lisa = (User) dataModelObjectMap.get("SCIENTISTUSER2");	
		
		CollectionProtocol cpObject2 = (CollectionProtocol) dataModelObjectMap.get("COLLECTION_PROTOCOL2");
		FluidSpecimen fluidSpecimen = new FluidSpecimen();
		MolecularSpecimen molecularSpecimen1 = new MolecularSpecimen();
		MolecularSpecimen molecularSpecimen2 = new MolecularSpecimen();
		MolecularSpecimen molecularSpecimen3 = new MolecularSpecimen();
		MolecularSpecimen molecularSpecimen4 = new MolecularSpecimen();
		MolecularSpecimen molecularSpecimen5 = new MolecularSpecimen();
		CellSpecimen cellSpecimen = new CellSpecimen();
		
		initCommonSpecimen(specimenCollectionGroup1,"Serum","Sp1_Sp1","Malignant",linda,"08/25/2005", fluidSpecimen,specimen1,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"cDNA","Sp2_Sp1","Not Specified",lisa,"08/15/2000",molecularSpecimen1,specimen2,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"DNA","Sp2_Sp2","Malignant, Invasive",linda,"10/10/2001",molecularSpecimen2,specimen2,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"DNA","Sp3_Sp1","Not Specified",lisa,"08/18/2004",molecularSpecimen3,specimen3,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"protein","Sp3_Sp2","Not Specified",lisa,"08/18/2004",molecularSpecimen4,specimen3,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"RNA","Sp3_Sp3","Not Specified",lisa,"08/18/2004",molecularSpecimen5,specimen3,"Derived",cpObject2);
		initCommonSpecimen(specimenCollectionGroup1,"Cryopreserved Cells","Sp4_Sp1","Not Specified",lisa,"08/18/2004",cellSpecimen,specimen4,"Derived",cpObject2);
		
		dataModelObjectMap.put("CHILD_SPECIMEN1",fluidSpecimen);
		dataModelObjectMap.put("CHILD_SPECIMEN2",molecularSpecimen1);
		dataModelObjectMap.put("CHILD_SPECIMEN3",molecularSpecimen2);
		dataModelObjectMap.put("CHILD_SPECIMEN4",molecularSpecimen3);
		dataModelObjectMap.put("CHILD_SPECIMEN5",molecularSpecimen4);
		dataModelObjectMap.put("CHILD_SPECIMEN6",molecularSpecimen5);
		dataModelObjectMap.put("CHILD_SPECIMEN7",cellSpecimen);
		
	}
	public void initDerivedSpecimen()
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)dataModelObjectMap.get("SCG1");
		User linda = (User) dataModelObjectMap.get("SCIENTISTUSER1");
		Specimen specimen= (Specimen)dataModelObjectMap.get("CHILD_SPECIMEN4");
		MolecularSpecimen molecularSpecimen = new MolecularSpecimen();
		CollectionProtocol cpObject2 = (CollectionProtocol) dataModelObjectMap.get("COLLECTION_PROTOCOL2");
		initCommonSpecimen(specimenCollectionGroup,"DNA","Sp3_Sp1_Sp1","Malignant, Invasive",linda,"10/10/2001",molecularSpecimen,specimen,"Derived",cpObject2);
		dataModelObjectMap.put("DERIVED_SPECIMEN",molecularSpecimen);
	}
	
	/**
	 * This funciton initializes data into Order domain object
	 */
	public void initOrder()
    {           
          OrderDetails order = new OrderDetails();  
          order.setComment("Comment");
          //Obtain Distribution Protocol
          DistributionProtocol distributionProtocolObj = (DistributionProtocol)dataModelObjectMap.get("DISTRIBUTION_PROTOCOL1");
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
  		  SpecimenArrayType specimenArrayType = (SpecimenArrayType)dataModelObjectMap.get("SPECIMEN_ARRAY_TYPE_1");
  		   		  
          NewSpecimenArrayOrderItem newSpOrderItem = new NewSpecimenArrayOrderItem();
          newSpOrderItem.setDescription("OrderDetails Item 1 of Order_Id ");
          newSpOrderItem.setStatus("Pending - For Protocol Review");           
          
          Quantity quantity = new Quantity();
          quantity.setValue(new Double(10));
          newSpOrderItem.setRequestedQuantity(quantity);
          newSpOrderItem.setName("Array Order");
          newSpOrderItem.setSpecimenArrayType(specimenArrayType);
          
          Collection specimenOrderItemCollection = new HashSet();
          ExistingSpecimenOrderItem specimenOrderItem = new ExistingSpecimenOrderItem();
          //setting details of specimen order item
          specimenOrderItem.setDescription("Order Details");
          specimenOrderItem.setStatus("Pending");
          Quantity quantity1 = new Quantity();
          quantity1.setValue(new Double(20));
          specimenOrderItem.setRequestedQuantity(quantity1);
          specimenOrderItem.setOrder(order);         
          Specimen specimen1= (Specimen)dataModelObjectMap.get("SPECIMEN1");
          specimenOrderItem.setSpecimen(specimen1);
          
          specimenOrderItem.setNewSpecimenArrayOrderItem(newSpOrderItem);
          specimenOrderItemCollection.add(specimenOrderItem);

          newSpOrderItem.setSpecimenOrderItemCollection(specimenOrderItemCollection);         
          orderItemCollection.add(newSpOrderItem);
          order.setOrderItemCollection(orderItemCollection);
          dataModelObjectMap.put("ORDER_DETAILS",order);
    }
}
