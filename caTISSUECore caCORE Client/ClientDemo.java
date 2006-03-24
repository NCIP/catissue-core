import edu.wustl.catissuecore.domainobject.Address;
import edu.wustl.catissuecore.domainobject.Biohazard;
import edu.wustl.catissuecore.domainobject.CancerResearchGroup;
import edu.wustl.catissuecore.domainobject.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domainobject.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domainobject.ClinicalReport;
import edu.wustl.catissuecore.domainobject.CollectionEventParameters;
import edu.wustl.catissuecore.domainobject.CollectionProtocol;
import edu.wustl.catissuecore.domainobject.CollectionProtocolEvent;
import edu.wustl.catissuecore.domainobject.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domainobject.Department;
import edu.wustl.catissuecore.domainobject.DisposalEventParameters;
import edu.wustl.catissuecore.domainobject.DistributedItem;
import edu.wustl.catissuecore.domainobject.Distribution;
import edu.wustl.catissuecore.domainobject.DistributionProtocol;
import edu.wustl.catissuecore.domainobject.EmbeddedEventParameters;
import edu.wustl.catissuecore.domainobject.ExternalIdentifier;
import edu.wustl.catissuecore.domainobject.FixedEventParameters;
import edu.wustl.catissuecore.domainobject.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domainobject.FrozenEventParameters;
import edu.wustl.catissuecore.domainobject.Institution;
import edu.wustl.catissuecore.domainobject.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domainobject.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domainobject.Participant;
import edu.wustl.catissuecore.domainobject.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domainobject.ProcedureEventParameters;
import edu.wustl.catissuecore.domainobject.ReceivedEventParameters;
import edu.wustl.catissuecore.domainobject.Site;
import edu.wustl.catissuecore.domainobject.Specimen;
import edu.wustl.catissuecore.domainobject.SpecimenCharacteristics;
import edu.wustl.catissuecore.domainobject.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domainobject.SpunEventParameters;
import edu.wustl.catissuecore.domainobject.StorageContainer;
import edu.wustl.catissuecore.domainobject.StorageContainerCapacity;
import edu.wustl.catissuecore.domainobject.StorageContainerDetails;
import edu.wustl.catissuecore.domainobject.StorageType;
import edu.wustl.catissuecore.domainobject.ThawEventParameters;
import edu.wustl.catissuecore.domainobject.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domainobject.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domainobject.TransferEventParameters;
import edu.wustl.catissuecore.domainobject.User;
import edu.wustl.catissuecore.domainobject.impl.AddressImpl;
import edu.wustl.catissuecore.domainobject.impl.BiohazardImpl;
import edu.wustl.catissuecore.domainobject.impl.CancerResearchGroupImpl;
import edu.wustl.catissuecore.domainobject.impl.CellSpecimenImpl;
import edu.wustl.catissuecore.domainobject.impl.CellSpecimenReviewParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.CheckInCheckOutEventParameterImpl;
import edu.wustl.catissuecore.domainobject.impl.ClinicalReportImpl;
import edu.wustl.catissuecore.domainobject.impl.CollectionEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.CollectionProtocolEventImpl;
import edu.wustl.catissuecore.domainobject.impl.CollectionProtocolImpl;
import edu.wustl.catissuecore.domainobject.impl.CollectionProtocolRegistrationImpl;
import edu.wustl.catissuecore.domainobject.impl.DepartmentImpl;
import edu.wustl.catissuecore.domainobject.impl.DisposalEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.DistributedItemImpl;
import edu.wustl.catissuecore.domainobject.impl.DistributionImpl;
import edu.wustl.catissuecore.domainobject.impl.DistributionProtocolImpl;
import edu.wustl.catissuecore.domainobject.impl.EmbeddedEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.ExternalIdentifierImpl;
import edu.wustl.catissuecore.domainobject.impl.FixedEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.FluidSpecimenImpl;
import edu.wustl.catissuecore.domainobject.impl.FluidSpecimenReviewEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.FrozenEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.InstitutionImpl;
import edu.wustl.catissuecore.domainobject.impl.MolecularSpecimenRequirementImpl;
import edu.wustl.catissuecore.domainobject.impl.MolecularSpecimenReviewParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.ParticipantImpl;
import edu.wustl.catissuecore.domainobject.impl.ParticipantMedicalIdentifierImpl;
import edu.wustl.catissuecore.domainobject.impl.ProcedureEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.ReceivedEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.SiteImpl;
import edu.wustl.catissuecore.domainobject.impl.SpecimenCharacteristicsImpl;
import edu.wustl.catissuecore.domainobject.impl.SpecimenCollectionGroupImpl;
import edu.wustl.catissuecore.domainobject.impl.SpecimenImpl;
import edu.wustl.catissuecore.domainobject.impl.SpunEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.StorageContainerCapacityImpl;
import edu.wustl.catissuecore.domainobject.impl.StorageContainerDetailsImpl;
import edu.wustl.catissuecore.domainobject.impl.StorageContainerImpl;
import edu.wustl.catissuecore.domainobject.impl.StorageTypeImpl;
import edu.wustl.catissuecore.domainobject.impl.ThawEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.TissueSpecimenRequirementImpl;
import edu.wustl.catissuecore.domainobject.impl.TissueSpecimenReviewEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.TransferEventParametersImpl;
import edu.wustl.catissuecore.domainobject.impl.UserImpl;
import gov.nih.nci.csm.sdk.application.client.ApplicationServiceProvider;
import gov.nih.nci.csm.sdk.application.client.ClientSession;
import gov.nih.nci.system.applicationservice.SDKApplicationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


/**
 * This is the sample client program to insert/update all the caTissue Core
 * objects through API.
 */
public class ClientDemo 
{
    
    public static void main(String[] args) throws Exception  
    {	
		ApplicationServiceProvider asp = new ApplicationServiceProvider();
		SDKApplicationService appService = asp.getApplicationService();
		
		ClientSession cs = ClientSession.getInstance();
		cs.startSession("admin@admin.com","login123");
		
		//To add Institution object
	    Institution inst = (Institution)appService.createObject(getInstitution());
	    
	    //To get the identifier of the object created
	    System.out.println("The identifier of the object created is " + inst.getId());
	    
	    //To update Institution object
	    //appService.updateObject(getInstitution());
	    
	    
	    //To search all the user objects
//	    User user = new UserImpl();
//		user.setLoginName("*");
//	
//		List list = appService.search(User.class,user);
//		System.out.println("List Size......................"+list.size());
//		
//		for(int i=0;i<list.size();i++)
//		{
//			user = (User)list.get(i);
//			System.out.println((i+1)+"-->"+user.getLoginName() + " :: " + user.getId() + " :: " + user.getActivityStatus());
//		}
		
		cs.terminateSession();
    }
    
    //Use this function to create/update an object of Institution
    private static Institution getInstitution()
    {
        Institution institution=new InstitutionImpl();
		institution.setId(new Long(0));
		institution.setName("WashU");
        
		return institution;
    }
    
    //Use this function to create/update an object of Department
    private static Department getDepartment()
    {
        Department dept=new DepartmentImpl();
        dept.setId(new Long(0));
        dept.setName("Department of Bio-Informatics");
        
        return dept;
    }
    
	//Use this function to create/update an object of CancerResearchGroup
    private static CancerResearchGroup getCancerResearchGroup()
    {
    	CancerResearchGroup crg=new CancerResearchGroupImpl();
		crg.setId(new Long(0));
		crg.setName("Tata Memorial Mumbai");
		
		return crg;
    }
    
	//Use this function to create/update an object of User
    private static User getUser()
    {
        User user=new UserImpl();
        
        user.setId(new Long(0));
        user.setActivityStatus("Active");
        
        user.setEmailAddress("admin@pspl.com");
        user.setLastName("LastName");
        user.setFirstName("FirstName");
        
//      user.setCsmUserId(new Long(4));
        
        Address add=new AddressImpl();
//      add.setId(new Long(5));
        add.setStreet("Street");
        add.setCity("City");
        add.setState("Alaska");
        add.setZipCode("11111");
        add.setCountry("Canada");
        add.setPhoneNumber("999-999-999");
        user.setAddress(add);
        
        Institution ins=new InstitutionImpl();
        ins.setId(new Long(1));
        user.setInstitution(ins);
        
        Department dept=new DepartmentImpl();
        dept.setId(new Long(1));
        user.setDepartment(dept);
        
        CancerResearchGroup crg=new CancerResearchGroupImpl();
        crg.setId(new Long(1));
        user.setCancerResearchGroup(crg);
        
        return user;
    }
    
	//Use this function to create/update an object of Site
    private static Site getSite()
    {
    	Site site=new SiteImpl();
		site.setId(new Long(0));
		site.setName("My Site");
		site.setType("Laboratory");
		
		User usr=new UserImpl();
		usr.setId(new Long(2));
		site.setCoordinator(usr);
		
		Address add=new AddressImpl();
//		add.setId(new Long(5));
		add.setStreet("The Street");
		add.setCity("The City");
		add.setState("Alaska");
		add.setCountry("Canada");
		add.setZipCode("99999");
		site.setAddress(add);
		site.setActivityStatus("Active");

		return site;
    }
    
	//Use this function to create/update an object of StorageType
    private static StorageType getStorageType()
    {
    	StorageType sType=new StorageTypeImpl();
		sType.setId(new Long(0));
		
		StorageContainerCapacity scont=new StorageContainerCapacityImpl();
//		scont.setId(new Long(10));
		scont.setOneDimensionCapacity(new Integer(10));
		scont.setTwoDimensionCapacity(new Integer(20));
		sType.setDefaultStorageCapacity(scont);
		
		sType.setOneDimensionLabel("Dimension X");
		sType.setTwoDimensionLabel("Dimension Y");
		sType.setDefaultTempratureInCentigrade(new Double(-100));
		sType.setType("Deep Freezer");
        
    	return sType;
    }
    
	//Use this function to create/update an object of StorageContainer
    private static StorageContainer getStorageContainer()
    {
	    StorageContainer cont=new StorageContainerImpl();
        cont.setId(new Long(0));
        cont.setActivityStatus("Active");
        
        StorageType sType=new StorageTypeImpl();
        sType.setId(new Long(1));
        cont.setStorageType(sType);
        
        Site scSite=new SiteImpl();
        scSite.setId(new Long(1));
        cont.setSite(scSite);
        
//      StorageContainer parent=new StorageContainerImpl();
//      parent.setId(new Long(23));
        cont.setParentContainer(null);
        
//      cont.setPositionDimensionOne(new Integer(5));
//      cont.setPositionDimensionTwo(new Integer(5));

        cont.setBarcode(null);
//      cont.setNumber(new Integer(1));
        
        StorageContainerCapacity scCapacity=new StorageContainerCapacityImpl();
//      scCapacity.setId(new Long(58));
        scCapacity.setOneDimensionCapacity(new Integer(5));
        scCapacity.setTwoDimensionCapacity(new Integer(5));
        cont.setStorageContainerCapacity(scCapacity);
                
        StorageContainerDetails scDetail=new StorageContainerDetailsImpl();
//      scDetail.setId(new Long(4));
        scDetail.setParameterName("ParamName1");
        scDetail.setParameterValue("ParamValue1");
        scDetail.setStorageContainer(cont);
        
        StorageContainerDetails scDetail1=new StorageContainerDetailsImpl();
//      scDetail.setId(new Long(5));
        scDetail1.setParameterName("ParamName2");
        scDetail1.setParameterValue("ParamValue2");
        scDetail1.setStorageContainer(cont);
        
        Collection scDetails=new HashSet();
        scDetails.add(scDetail);
        scDetails.add(scDetail1);
        cont.setStorageContainerDetailsCollection(scDetails);
        
        cont.setTempratureInCentigrade(new Double(-10));
        cont.setIsFull(new Boolean(false));
        
        return cont;
    }
    
	//Use this function to create/update an object of Biohazard
    private static Biohazard getBiohazard()
    {
    	Biohazard bHazard=new BiohazardImpl();
		bHazard.setId(new Long(0));
		bHazard.setType("Mutagen");
		bHazard.setComments("My Comments");
		bHazard.setName("M-Hazard");

		return bHazard;
    }
    
	//Use this function to create/update an object of CollectionProtocol
    private static CollectionProtocol getCollectionProtocol()
    {
		CollectionProtocol cp=new CollectionProtocolImpl();
		cp.setId(new Long(0));
		cp.setActivityStatus("Active");
		
		User pi=new UserImpl();
		pi.setId(new Long(2));
		cp.setPrincipalInvestigator(pi);
		
		User co1=new UserImpl();
		co1.setId(new Long(1));
		User co2=new UserImpl();
		co1.setId(new Long(21));
		HashSet set = new HashSet();
		set.add(co1);
		set.add(co2);
		cp.setUserCollection(set);
		
		cp.setTitle("Collection Protocol");
		cp.setShortTitle("CP");
		cp.setIrbIdentifier("IRB_007");
		Date startDate=new Date();
		cp.setStartDate(startDate);
		cp.setEndDate(null);
		cp.setEnrollment(new Integer(5));
		cp.setDescriptionURL("URL");
		
		//CollectionProtocolEvent
		CollectionProtocolEvent cpEvent=new CollectionProtocolEventImpl();
//		cpEvent.setId(new Long(1));
		cpEvent.setClinicalStatus("New Diagnosis");
		cpEvent.setStudyCalendarEventPoint(new Double(5));
		cpEvent.setCollectionProtocol(cp);
		
		CollectionProtocolEvent cpEvent1=new CollectionProtocolEventImpl();
//		cpEvent1.setId(new Long(2));
		cpEvent1.setClinicalStatus("Operative");
		cpEvent1.setStudyCalendarEventPoint(new Double(2));
		cpEvent1.setCollectionProtocol(cp);
		
		
		//SpecimenRequirement
		MolecularSpecimenRequirement spReq=new MolecularSpecimenRequirementImpl();
//		spReq.setId(new Long(1));
		spReq.setSpecimenType("cDNA");
		spReq.setTissueSite("Abdomen, NOS");
		spReq.setPathologyStatus("Metastatic");
		spReq.setQuantityInMicrogram(new Double(100));
		
		TissueSpecimenRequirement tissueReq=new TissueSpecimenRequirementImpl();
//		tissueReq.setId(new Long(2));
		tissueReq.setSpecimenType("Fixed Tissue Block");
		tissueReq.setTissueSite("Appendix");
		tissueReq.setPathologyStatus("Metastatic");
		tissueReq.setQuantityInGram(new Double(50));
		
		MolecularSpecimenRequirement spReq1=new MolecularSpecimenRequirementImpl();
//		spReq1.setId(new Long(45));
		spReq1.setSpecimenType("cDNA");
		spReq1.setTissueSite("Abdomen, NOS");
		spReq1.setPathologyStatus("Metastatic");
		spReq1.setQuantityInMicrogram(new Double(300));
		
		TissueSpecimenRequirement tissueReq1=new TissueSpecimenRequirementImpl();
//		tissueReq1.setId(new Long(46));
		tissueReq1.setSpecimenType("Fixed Tissue");
		tissueReq1.setTissueSite("Appendix");
		tissueReq1.setPathologyStatus("Metastatic");
		tissueReq1.setQuantityInGram(new Double(330));
		
		//SpecimenRequirement Collection
		Collection spReqCollection=new HashSet();
		
		spReqCollection.add(spReq);
		spReqCollection.add(tissueReq);
		cpEvent.setSpecimenRequirementCollection(spReqCollection);
		
		Collection spReqCollection1=new HashSet();
		spReqCollection1.add(spReq1);
		spReqCollection1.add(tissueReq1);
		cpEvent1.setSpecimenRequirementCollection(spReqCollection1);
		
		//CollectionProtocolEvent Collection
		Collection cpEventCollection=new ArrayList();
		cpEventCollection.add(cpEvent);
		cpEventCollection.add(cpEvent1);
		cp.setCollectionProtocolEventCollection(cpEventCollection);
        
		return cp;
    }
    
	//Use this function to create/update an object of DistributionProtocol
    private static DistributionProtocol getDistributionProtocol()
    {
        DistributionProtocol dp=new DistributionProtocolImpl();
        dp.setId(new Long(0));
        dp.setActivityStatus("Active");
        
        User pi=new UserImpl();
		pi.setId(new Long(1));
        dp.setPrincipalInvestigator(pi);
        
        dp.setTitle("The Distribution Protocol");
		dp.setShortTitle("DP");
		dp.setIrbIdentifier("IRBID-001");
		Date startDate=new Date();
		dp.setStartDate(startDate);
		dp.setEndDate(null);
		dp.setEnrollment(new Integer(5));
		dp.setDescriptionURL("URL");
        
		//SpecimenRequirement
		MolecularSpecimenRequirement spReq=new MolecularSpecimenRequirementImpl();
//		spReq.setId(new Long(41));
		spReq.setSpecimenType("protein");
		spReq.setTissueSite("Abdomen, NOS");
		spReq.setPathologyStatus("Malignant");
		spReq.setQuantityInMicrogram(new Double(500));
		
		TissueSpecimenRequirement tissueReq=new TissueSpecimenRequirementImpl();
//		tissueReq.setId(new Long(42));
		tissueReq.setSpecimenType("Frozen Tissue");
		tissueReq.setTissueSite("Appendix");
		tissueReq.setPathologyStatus("Pre-Malignant");
		tissueReq.setQuantityInGram(new Double(400));
		
		//SpecimenRequirement Collection
		Collection spReqCollection=new HashSet();
		spReqCollection.add(spReq);
		spReqCollection.add(tissueReq);
		dp.setSpecimenRequirementCollection(spReqCollection);
		
		return dp;
    }
    
	//Use this function to create/update an object of Participant
    private static Participant getParticipant()
    {
    	Participant participant = new ParticipantImpl();
    	
    	participant.setId(new Long(0));
    	participant.setLastName("Lastname");
    	participant.setFirstName("Firstname");
    	participant.setGender("Male");
    	participant.setSexGenotype("XY");
    	participant.setRace("African");
    	participant.setEthnicity("Canal Zone");
    	participant.setActivityStatus("Active");
    	participant.setBirthDate(Calendar.getInstance().getTime());
    	
    	ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifierImpl();
    	pmi.setId(null);
    	pmi.setMedicalRecordNumber("MRN-1");
    	pmi.setParticipant(participant);
    	Site site = new SiteImpl();
    	site.setId(new Long(2));
    	pmi.setSite(site);
    	
    	HashSet set = new HashSet();
    	set.add(pmi);
    	
    	participant.setParticipantMedicalIdentifierCollection(set);
    	
    	return participant;
    }
    
	//Use this function to create/update an object of CollectionProtocolRegistration
    private static CollectionProtocolRegistration getCollectionProtocolRegistration()
    {
    	CollectionProtocolRegistration registration = new CollectionProtocolRegistrationImpl();
    	
    	registration.setId(new Long(0));
    	registration.setActivityStatus("Active");
    	
    	CollectionProtocol cp = new CollectionProtocolImpl();
    	cp.setId(new Long(1));
    	registration.setCollectionProtocol(cp);
    	
    	Participant participant = new ParticipantImpl();
    	participant.setId(new Long(3));
    	registration.setParticipant(participant);
    	
//    	registration.setProtocolParticipantIdentifier("1");
    	
    	registration.setRegistrationDate(Calendar.getInstance().getTime());
    	
    	return registration;
    }
    
	//Use this function to create/update an object of SpecimenCollectionGroup
    private static SpecimenCollectionGroup getSpecimenCollectionGroup()
    {
    	SpecimenCollectionGroup group = new SpecimenCollectionGroupImpl();
    	
    	group.setId(new Long(0));
    	group.setActivityStatus("Active");
    	
    	CollectionProtocolRegistration registration = new CollectionProtocolRegistrationImpl();
    	registration.setId(new Long(1));
    	group.setCollectionProtocolRegistration(registration);
    	
    	SiteImpl site = new SiteImpl();
    	site.setId(new Long(1));
    	group.setSite(site);
    	
    	group.setClinicalStatus("Operative");
    	group.setClinicalDiagnosis("Acute leukemia, NOS");

    	ClinicalReport report = new ClinicalReportImpl();
//    	report.setId(new Long(5));
    	report.setSurgicalPathologyNumber("S-1000");
    	ParticipantMedicalIdentifier id = new ParticipantMedicalIdentifierImpl();
    	id.setId(new Long(1));
    	report.setParticipantMedicalIdentifier(id);
    	group.setClinicalReport(report);
    	
    	CollectionProtocolEvent event = new CollectionProtocolEventImpl();
    	event.setId(new Long(1));
    	group.setCollectionProtocolEvent(event);
    	    	
    	return group;
    }
    
	//Use this function to create/update an object of Specimen
    private static Specimen getSpecimen()
    {
    	Specimen specimen = new FluidSpecimenImpl();
    	
    	specimen.setId(new Long(0));
    	specimen.setActivityStatus("Active");
    	specimen.setAvailable(Boolean.TRUE);
    	specimen.setBarcode(null);
    	specimen.setComments("No Comments");
    	
    	StorageContainer container = new StorageContainerImpl();
    	container.setId(new Long(25));
    	specimen.setStorageContainer(container);
    	
    	specimen.setPositionDimensionOne(new Integer(2));
    	specimen.setPositionDimensionTwo(new Integer(1));
    	
    	SpecimenCollectionGroup group = new SpecimenCollectionGroupImpl();
    	group.setId(new Long(22));
    	specimen.setSpecimenCollectionGroup(group);
    	
//    	Specimen parentSpecimen = new CellSpecimenImpl();
//    	parentSpecimen.setId(new Long(10));
//    	specimen.setParentSpecimen(parentSpecimen);
    	
    	SpecimenCharacteristics chars = new SpecimenCharacteristicsImpl();
//    	chars.setId(new Long(69));
    	chars.setPathologicalStatus("Not Specified");
    	chars.setTissueSide("Right");
    	chars.setTissueSite("Appendix");
    	specimen.setSpecimenCharacteristics(chars);
    	
    	specimen.setType("Plasma");
    	((FluidSpecimenImpl)specimen).setQuantityInMilliliter(new Double(1000));
    	((FluidSpecimenImpl)specimen).setAvailableQuantityInMilliliter(new Double(1000));
    	
    	ExternalIdentifier exId = new ExternalIdentifierImpl();
//    	exId.setId(new Long("84"));
    	exId.setName("Ex-Id-1");
    	exId.setValue("1001");
    	ExternalIdentifier exId2 = new ExternalIdentifierImpl();
//    	exId2.setId(new Long("85"));
    	exId2.setName("Ex-Id-2");
    	exId2.setValue("2002");
    	HashSet set = new HashSet();
    	set.add(exId);
    	set.add(exId2);
    	specimen.setExternalIdentifierCollection(set);

    	Biohazard hazard = new BiohazardImpl();
    	hazard.setId(new Long(11));
    	Biohazard hazard2 = new BiohazardImpl();
    	hazard2.setId(new Long(12));
    	HashSet bioSet = new HashSet();
    	bioSet.add(hazard);
    	bioSet.add(hazard2);
    	specimen.setBiohazardCollection(bioSet);
    	
    	return specimen;
    }
    
	//Use this function to create/update an object of Distribution
    public static Distribution getDistribution()
    {
        Distribution dist=new DistributionImpl();
        dist.setId(new Long(0));
        dist.setActivityStatus("Active");
        
        DistributionProtocol dp=new DistributionProtocolImpl();
        dp.setId(new Long(44));
        dist.setDistributionProtocol(dp);
        
        User user=new UserImpl();
        user.setId(new Long(61));
        dist.setUser(user);
        
        Date date=new Date();
        dist.setTimestamp(date);
        
        Site site=new SiteImpl();
        site.setId(new Long(1));
        dist.setToSite(site);
        
        dist.setComments("No Comments");
        
        Specimen specimen=new CellSpecimenImpl();
        specimen.setId(new Long(610));

        DistributedItem dItem=new DistributedItemImpl();
//      dItem.setId(new Long(0));
        dItem.setQuantity(new Double(1.0));
        dItem.setSpecimen(specimen);
        dItem.setDistribution(dist);
        
        Collection dItems=new HashSet();
        dItems.add(dItem);
        dist.setDistributedItemCollection(dItems);
                
        return dist;
    }
    
	//Use this function to create/update an object of CellSpecimenReviewParameters
    private static CellSpecimenReviewParameters getCellSpecimenReviewEventParameter()
    {
    	CellSpecimenReviewParameters param = new CellSpecimenReviewParametersImpl();
    	param.setId(new Long(0));
    	param.setComments("My Comments");
    	param.setViableCellPercentage(new Double(100));
    	param.setNeoplasticCellularityPercentage(new Double(100));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of CheckInCheckOutEventParameter
    private static CheckInCheckOutEventParameter getCheckInCheckOutEventParameter()
    {
    	CheckInCheckOutEventParameter param = new CheckInCheckOutEventParameterImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setStorageStatus("CHECK IN");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of CollectionEventParameters
    private static CollectionEventParameters getCollectionEventParameters()
    {
    	CollectionEventParameters param = new CollectionEventParametersImpl();
		param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setCollectionProcedure("Lavage");
    	param.setContainer("Not Specified");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of DisposalEventParameters
    private static DisposalEventParameters getDisposalEventParameters()
    {
    	DisposalEventParameters param = new DisposalEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setReason("No Reason");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of EmbeddedEventParameters
    private static EmbeddedEventParameters getEmbeddedEventParameters()
    {
    	EmbeddedEventParameters param = new EmbeddedEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setEmbeddingMedium("Plastic");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of FixedEventParameters
    private static FixedEventParameters getFixedEventParameters()
    {
    	FixedEventParameters param = new FixedEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setDurationInMinutes(new Integer(10));
    	param.setFixationType("Bouin's");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of FluidSpecimenReviewEventParameters
    private static FluidSpecimenReviewEventParameters getFluidSpecimenReviewEventParameters()
    {
    	FluidSpecimenReviewEventParameters param = new FluidSpecimenReviewEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setCellCount(new Double(1000));
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of FrozenEventParameters
    private static FrozenEventParameters getFrozenEventParameters()
    {
    	FrozenEventParameters param = new FrozenEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setMethod("Dry Ice");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of MolecularSpecimenReviewParameters
    private static MolecularSpecimenReviewParameters getMolecularSpecimenReviewParameters()
    {
    	MolecularSpecimenReviewParameters param = new MolecularSpecimenReviewParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setQualityIndex("Index-1");
    	param.setLaneNumber("235897");
    	param.setGelImageURL("ImgURL");
    	param.setGelNumber(new Integer(2500));
    	param.setAbsorbanceAt260(new Double(234.45));
    	param.setAbsorbanceAt280(new Double(234.56));
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of ProcedureEventParameters
    private static ProcedureEventParameters getProcedureEventParameters()
    {
    	ProcedureEventParameters param = new ProcedureEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setName("Name");
    	param.setUrl("URL");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of ReceivedEventParameters
    private static ReceivedEventParameters getReceivedEventParameters()
    {
    	ReceivedEventParameters param = new ReceivedEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setReceivedQuality("Frozen");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of SpunEventParameters
    private static SpunEventParameters getSpunEventParameters()
    {
    	SpunEventParameters param = new SpunEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setGForce(new Double(18.56));
    	param.setDurationInMinutes(new Integer(20));
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of ThawEventParameters
    private static ThawEventParameters getThawEventParameters()
    {
    	ThawEventParameters param = new ThawEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of TissueSpecimenReviewEventParameters
    private static TissueSpecimenReviewEventParameters getTissueSpecimenReviewEventParameters()
    {
    	TissueSpecimenReviewEventParameters param = new TissueSpecimenReviewEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	param.setHistologicalQuality("Poor- No Definable Features");
    	param.setNeoplasticCellularityPercentage(new Double(100));
    	param.setNecrosisPercentage(new Double(100));
    	param.setLymphocyticPercentage(new Double(10));
    	param.setTotalCellularityPercentage(new Double(100));
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(42));
    	param.setSpecimen(specimen);
    	
    	return param;
    }
    
	//Use this function to create/update an object of TransferEventParameters
    private static TransferEventParameters getTransferEventParameters()
    {
    	TransferEventParameters param = new TransferEventParametersImpl();
    	param.setId(new Long(0));
    	param.setTimestamp(Calendar.getInstance().getTime());
    	param.setComments("No Comments");
    	
    	StorageContainer fromContainer = new StorageContainerImpl();
    	fromContainer.setId(new Long(24));
    	param.setFromStorageContainer(fromContainer);
    	param.setFromPositionDimensionOne(new Integer(1));
    	param.setFromPositionDimensionTwo(new Integer(1));
    	
    	StorageContainer toContainer = new StorageContainerImpl();
    	toContainer.setId(new Long(24));
    	param.setToStorageContainer(toContainer);
    	param.setToPositionDimensionOne(new Integer(1));
    	param.setToPositionDimensionTwo(new Integer(2));
    	
    	User user = new UserImpl();
    	user.setId(new Long(61));
    	param.setUser(user);
    	
    	Specimen specimen = new SpecimenImpl();
    	specimen.setId(new Long(25));
    	param.setSpecimen(specimen);
    	
    	return param;
    }

    
}//End of class CATISSUECSMClient