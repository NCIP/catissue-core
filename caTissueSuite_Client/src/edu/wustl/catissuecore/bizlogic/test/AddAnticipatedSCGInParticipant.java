package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import gov.nih.nci.common.util.HQLCriteria;


public class AddAnticipatedSCGInParticipant extends CaTissueBaseTestCase {
	
	private static int rowNo = 24;
	private List CpCpeId = new ArrayList();
	
	public Long getCpId(String excel[][]) throws Exception {

		String cpShortTitle = excel[rowNo][0].trim();

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setShortTitle(cpShortTitle);
		System.out.println("cpShortTitle " + cpShortTitle);
		List cpeList = null;
		Long cpId = null;
		try {
			 cpeList  = appService.query(new HQLCriteria("select cp.id from edu.wustl.catissuecore.domain.CollectionProtocol cp where cp.shortTitle='"+cpShortTitle+"'"),
					CollectionProtocol.class.getName());
			if(cpeList != null && cpeList.size() > 0) {
				cpId = (Long)cpeList.get(0);
				System.out.println("CP Id  retrived from DB "+ cpId);
			}
	} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return cpId;
	}
	
	public List getCPe(Long cpeId) throws Exception {
		List cpeList = null;
		try {
			cpeList  = appService.query(new HQLCriteria("select cpe.id from edu.wustl.catissuecore.domain.CollectionProtocolEvent cpe where cpe.collectionProtocol.id= "+cpeId),
						CollectionProtocolEvent.class.getName());
			 
			System.out.println("No of CPe retrived from DB "
					+ cpeList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return cpeList;
	}
	
	
	
	public void getAllCPR(Long cpId)
			throws Exception {
		try {
			CollectionProtocol cp = new CollectionProtocol();
			cp.setId(cpId);
			//CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
//			collectionProtocolRegistration.setCollectionProtocol(cp);
//			List cprResultList = appService.search(
//					CollectionProtocolRegistration.class,
//					collectionProtocolRegistration);
			List cprResultList  = appService.query(new HQLCriteria("select cpr.id from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id= "+cpId),
					CollectionProtocolEvent.class.getName());
		 
			Iterator cprResultIterator = cprResultList.iterator();
			System.out.println("No of CPR retrived " + cprResultList.size());
			while (cprResultIterator.hasNext()) {
				Long cprId = (Long) cprResultIterator.next();
				getAllSCGs(cprId);
				// System.out.println("CollectionProtocolEvent "+
				// collectionProtocolRegistration
				// .getSpecimenCollectionGroupCollection().size());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}
	
	public void getAllSCGs(Long cprId) {
	try {
		String query = null;
		HQLCriteria hqlcri = null;
		query = "select specimenCollectionGroup.collectionProtocolEvent.id from edu.wustl.catissuecore.domain.SpecimenCollectionGroup" +
				" as specimenCollectionGroup where "+
   			" specimenCollectionGroup.collectionProtocolRegistration.id= " + cprId ;
   			hqlcri = new HQLCriteria(query);
  	  List cpeList = appService.query(hqlcri, CollectionProtocolEvent.class.getName());
  	//  Iterator<Long> cpeItr = cpeList.iterator();
//  	  List cpeIdList = new ArrayList();
//  	  while(cpeItr.hasNext()){
//  		  cpeIdList.add(((Long)cpeItr.next()));
//  	  }
  	  if(CpCpeId != null && cpeList != null) {
	  	  Iterator<Long> itr = this.CpCpeId.iterator();
	  	  while(itr.hasNext()) {
	  		  Long id = itr.next();
	  		  if(!cpeList.contains(id)){
	  			  System.out.println("cpe not present "+id );
	  			  addSCGWithNameAndEvents(cprId,id);
	  		  }
	  	  }
	}
 	}catch (Exception ex){
		ex.printStackTrace();
	}
  }
	
	 public void addSCGs(String excel[][])  {
		 
         while (rowNo < excel.length) {
             try {
	             Long cpId = getCpId(excel);
	             if(cpId != null) {
	            	 CpCpeId = getCPe(cpId);
                     if (CpCpeId != null && CpCpeId.size() > 0) {
                             System.out.println("CpCpeId size " +CpCpeId.size());
                             getAllCPR(cpId);
                             System.out.println("End..");
                     }
                 }
             }catch (Exception e) {
                     e.printStackTrace();
             }
             rowNo++;
             CpCpeId = new ArrayList();
         }
 }

	public SpecimenCollectionGroup addSCGWithNameAndEvents(Long cprId, Long cpeId)
	{

		System.out.println(" in addSCGWithNameAndEvents ");
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		CollectionProtocolEvent collectionProtocolEvent = getCpe(cpeId);
		specimenCollectionGroup =( SpecimenCollectionGroup) createSCG(collectionProtocolEvent);
		if (specimenCollectionGroup != null) {
			Site site = createSite(); 
			specimenCollectionGroup.setSpecimenCollectionSite(site);
			String scgName="scg added through api"+UniqueKeyGeneratorUtil.getUniqueKey();
			specimenCollectionGroup.setName(scgName);
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setId(cprId);
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
			System.out.println("set cpr in scg");
			setEventParameters(specimenCollectionGroup);
			try{
				specimenCollectionGroup = (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroup);
			}catch (Exception e){
				System.out.println("Exception in addSCGWithNameAndEvents");
				System.err.println("Exception in addSCGWithNameAndEvents");
	         	e.printStackTrace();
			}
		}
		return specimenCollectionGroup;
	}
	public CollectionProtocolEvent getCpe(Long id){
		CollectionProtocolEvent cpe = null;
		try{
	
		String query = null;
		HQLCriteria hqlcri = null;
		query = "select collectionProtocolEvent from edu.wustl.catissuecore.domain.CollectionProtocolEvent" +
				" as collectionProtocolEvent where "+
   			" collectionProtocolEvent.id= " + id ;
   			hqlcri = new HQLCriteria(query);
  	  List cpeList = appService.query(hqlcri, CollectionProtocolEvent.class.getName());
  	  Iterator<CollectionProtocolEvent> cpeItr = cpeList.iterator();
	  List cpeIdList = new ArrayList();
	  while(cpeItr.hasNext()){
		 cpe =  (CollectionProtocolEvent)cpeItr.next();
	  }
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	  return cpe;
  }
	
	
	public  SpecimenCollectionGroup createSCG(CollectionProtocolEvent collectionProtocolEvent)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try 
		{
			User user = getUser("abrink@pathology.wustl.edu");
			
			specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
			System.out.println("scg label "+specimenCollectionGroup.getCollectionProtocolEvent().getCollectionPointLabel());
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolEvent.getCollectionProtocolRegistration());
			
			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
			specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
			
			Collection cloneSpecimenCollection = new LinkedHashSet();
			Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
			if(specimenCollection != null && !specimenCollection.isEmpty())
			{
				Iterator itSpecimenCollection = specimenCollection.iterator();
				while(itSpecimenCollection.hasNext())
				{
					SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
					if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
					{
						Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,user);
						LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
						specimenLableGenerator.setLabel(cloneSpecimen);
						cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
						cloneSpecimenCollection.add(cloneSpecimen);
					}
				}
			}
			
			specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
		}catch(Exception e)
		{
			System.out.println("Exception in create SCG");
			System.err.println("Exception in create SCG");
			e.printStackTrace();
		}
		return specimenCollectionGroup;
	}


	private static Specimen getCloneSpecimen(Map<Specimen, List<Specimen>> specimenMap, SpecimenRequirement reqSpecimen, Specimen pSpecimen, SpecimenCollectionGroup specimenCollectionGroup, User user)
	{
		Collection childrenSpecimen = new LinkedHashSet<Specimen>(); 
		Specimen newSpecimen = null;
		try 
		{
			newSpecimen = (Specimen) new SpecimenObjectFactory()
				.getDomainObject(reqSpecimen.getClassName(),reqSpecimen);
		}
		catch (AssignDataException e1) 
		{
			System.out.println("Exception in getCloneSpecimen");
			System.err.println("Exception in getCloneSpecimen");
			e1.printStackTrace();
			return null;
		}	
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(user.getId());
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
		if (newSpecimen.getParentSpecimen()== null)
		{
			specimenMap.put(newSpecimen, new ArrayList<Specimen>());
		}
		else
		{
			specimenMap.put(newSpecimen, null);
		}
		
		Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		if(childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		{
	    	Iterator<SpecimenRequirement> it = childrenSpecimenCollection.iterator();
	    	while(it.hasNext())
	    	{
	    		SpecimenRequirement childReqSpecimen = it.next();
	    		Specimen newchildSpecimen = getCloneSpecimen(specimenMap, childReqSpecimen,newSpecimen, specimenCollectionGroup, user);
	    		childrenSpecimen.add(newchildSpecimen);
	    		newSpecimen.setChildSpecimenCollection(childrenSpecimen);
	    	}
		}
		return newSpecimen;
	}


	private User getUser(String loginName){
		
		User user = new User();
		user.setLoginName(loginName);
		List<?> resultList1 = new LinkedList ();
		try {
		List resultList = appService.search(User.class,user);
		if(resultList.size() > 0){
			user = (User)resultList.get(0);
		}
		
		}catch (Exception e) {
			System.out.println("Exception in getUser");
			System.err.println("Exception in getUser");
			e.printStackTrace();
		}
		
		
		return user;
		
	}

	
	private void setEventParameters(SpecimenCollectionGroup sprObj)
	{
		System.out.println("Inside setEventParameters for SCG");
		Collection specimenEventParametersCollection = new HashSet();
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		collectionEventParameters.setCollectionProcedure("Not Specified");
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer("Not Specified");
		try{
			Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
			collectionEventParameters.setTimestamp(timestamp);
	}catch(Exception e){
			System.out.println("Exception while setting colection date in setEventParameters");
			e.printStackTrace();
		}
		
		
		User user = getUser("abrink@pathology.wustl.edu");
		collectionEventParameters.setUser(user);	
		collectionEventParameters.setSpecimenCollectionGroup(sprObj);	
		
		//Received Events		
		receivedEventParameters.setComment("");
		User receivedUser = getUser("abrink@pathology.wustl.edu");
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality("Not Specified");		
		Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		receivedEventParameters.setTimestamp(receivedTimestamp);		
		receivedEventParameters.setSpecimenCollectionGroup(sprObj);
		specimenEventParametersCollection.add(collectionEventParameters);
		specimenEventParametersCollection.add(receivedEventParameters);
		sprObj.setSpecimenEventParametersCollection(specimenEventParametersCollection);
	}
	private  void setSpecimenEvents(Specimen specimen,  String excel[][], int row) throws Exception

	{
		System.out.println("In setSpecimenEvents");	
		String collCotainer = excel[row][8];
		String collProcedure = excel[row][7];
		String collDate = excel[row][6];
		
	    Collection<SpecimenEventParameters> specimenEventCollection = new LinkedHashSet<SpecimenEventParameters>();
	    CollectionEventParameters collectionEvent = null;
		ReceivedEventParameters receivedEvent = null;

		
	    Collection evColl = specimen.getSpecimenEventCollection();
	   // System.out.println( " evColl.size() "+ evColl.size());
	   // if(specimen.getLineage()!="Aliquot") {
		if(evColl != null && evColl.size() != 0) {
			System.out.println("got eventColl from specimen ");
			Iterator itr  = evColl.iterator();
			while(itr.hasNext()) {
				SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
				if(spEvPar instanceof  CollectionEventParameters ) {
					System.out.println("got CollectionEventParameters");
					collectionEvent =(CollectionEventParameters) spEvPar;
				}
				else if(spEvPar instanceof  ReceivedEventParameters ) {
					System.out.println("got ReceivedEventParameters");
					receivedEvent =(ReceivedEventParameters) spEvPar;
				}
			}
		}
	   // }
		
	    if(collectionEvent == null) 
			collectionEvent = new CollectionEventParameters();
	  
	    User collectionEventUser = getUser("abrink@pathology.wustl.edu");
	    collectionEvent.setUser(collectionEventUser);
		

	    if(!specimen.getLineage().equals("New")){
			System.out.println(" in setSpecimenEventsnot a new specimen");
	    	Collection parentSpcEvColl = specimen.getParentSpecimen().getSpecimenEventCollection();
	    	
			if(parentSpcEvColl != null) {
				Iterator itr  = parentSpcEvColl.iterator();
				while(itr.hasNext()) {
					SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
					if(spEvPar instanceof  CollectionEventParameters ) {
						collectionEvent.setCollectionProcedure(((CollectionEventParameters) spEvPar).getCollectionProcedure());
						collectionEvent.setContainer(((CollectionEventParameters) spEvPar).getContainer());
					}
			     }
			}
		} else {
			System.out.println("in else ");
			collectionEvent.setCollectionProcedure(collProcedure);
			collectionEvent.setContainer(collCotainer);
		}
	  
	  try
		{
			Date timestamp = EventsUtil.setTimeStamp(collDate,"15","45");
			collectionEvent.setTimestamp(timestamp);
			System.out.println("set collDate "+ collDate);
					
		}
		catch (Exception e1)
		{
			System.out.println("exception in setSpecimenEvents");
			e1.printStackTrace();
		}
		
		collectionEvent.setSpecimen(specimen);
		specimenEventCollection.add(collectionEvent);
		
	    //setting received event values
		
		if(receivedEvent == null)
			receivedEvent = new ReceivedEventParameters();
	     
		    receivedEvent.setReceivedQuality("Not Specified"); // c ltr
			User receivedEventUser = getUser("abrink@pathology.wustl.edu");
			receivedEvent.setUser(receivedEventUser);
	   // }
		    
		try
		{
			Date receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
			receivedEvent.setTimestamp(receivedTimestamp);
		}
		catch (Exception e)
		{
			System.out.println("exception in setSpecimenEvents");
			e.printStackTrace();
		}
		receivedEvent.setSpecimen(specimen);
			specimenEventCollection.add(receivedEvent); 
	    	specimen.setSpecimenEventCollection(specimenEventCollection);
	    	
	    	 evColl = specimen.getSpecimenEventCollection();
				if(evColl != null) {
					System.out.println("got eventColl from specimen ");
					Iterator itr  = evColl.iterator();
					while(itr.hasNext()) {
						SpecimenEventParameters spEvPar =	(SpecimenEventParameters)itr.next();
						if(spEvPar instanceof  CollectionEventParameters ) {
						System.out.println("in setting event "+	((CollectionEventParameters) spEvPar).getUser().getFirstName());
						}
						else if(spEvPar instanceof  ReceivedEventParameters ) {
							//ReceivedEventParameters) spEvPar;
						}
					}
				}

	}


	public  Participant initParticipant(String excel[][]) 
	{
		String lastName = excel[rowNo][2];
		String firstName = excel[rowNo][3];
		String middleName = excel[rowNo][4];
		String dOB = excel[rowNo][5];
		
		Participant participant = new Participant();
		participant.setLastName(lastName);
		participant.setFirstName(firstName);
		System.out.println("middleName "+ middleName);
		if(middleName != "") {
			System.out.println("setting middle name");
			participant.setMiddleName(middleName);
		}
		//participant.setBirthDate(new Date(dOB));
		 try
	    {   System.out.println("dob "+ dOB); 
	        //dOB = dOB.replace("-", "\");
	        System.out.println(dOB);
	        
	        Date timestamp = EventsUtil.setTimeStamp(dOB,"15","45");
	        participant.setBirthDate(timestamp);
	        
	        
			//participant.setBirthDate(new SimpleDateFormat("MM-dd-yyyy").parse(dOB));
			System.out.println("birthdate set");
	    }
	    catch (Exception e)
	    {
	        System.out.println("exception in initParticipant in DOB");  
	        System.err.println("exception in initParticipant in DOB");  
	        e.printStackTrace();
	    }
		
		participant.setGender("Unspecified");
		participant.setEthnicity("Unknown");
		participant.setSexGenotype("XX");
		
		Collection raceCollection = new HashSet();
		Race race = new Race();
		race.setRaceName("White");
		race.setParticipant(participant);
		raceCollection.add(race);
		
		race = new Race();
		race.setRaceName("Unknown");
		race.setParticipant(participant);
		raceCollection.add(race);
		
		/*raceCollection.add("White");
		raceCollection.add("Asian");*/
		participant.setRaceCollection(raceCollection);
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");
		System.out.println("Participant initiated successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
		return participant;
	}

	public  CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant, CollectionProtocol cp, String excel[][])
	{
		//Logger.configure("");
		String pPI = excel[rowNo][1];
		String colldate = excel[rowNo][6];
		
		if(pPI.contains("-")) {
			System.out.println("pPI has -");
			pPI = pPI.split("-")[1];
		}
	    System.out.println("pPI "+ pPI);
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		collectionProtocolRegistration.setCollectionProtocol(cp);
	    collectionProtocolRegistration.setParticipant(participant);
	    collectionProtocolRegistration.setProtocolParticipantIdentifier(pPI);
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			//collectionProtocolRegistration.setRegistrationDate(Utility.parseDate(colldate.replace('/', '-'), "M-d-yyyy"));
	        Date timestamp = EventsUtil.setTimeStamp(colldate,"15","45");
	        collectionProtocolRegistration.setRegistrationDate(timestamp);
			
		}
		catch (Exception e)
		{
			System.out.println("Exception in initCollectionProtocolRegistration" );
			System.err.println("Exception in initCollectionProtocolRegistration" );
			e.printStackTrace();
		}
		
		//Setting Consent Tier Responses.
		try
		{
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",Utility.datePattern("11/23/2006")));
		}
		catch (ParseException e)
		{			
			System.err.println("exception in initCPR in Date");
	    	System.out.println("exception in initCPR in Date");
			e.printStackTrace();
		}
		// pratha collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = getUser("abrink@pathology.wustl.edu");
		collectionProtocolRegistration.setConsentWitness(user);
		
		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		Iterator consentTierItr = consentTierCollection.iterator();
		while(consentTierItr.hasNext())
		{
			ConsentTier consentTier = (ConsentTier)consentTierItr.next();
			ConsentTierResponse consentResponse = new ConsentTierResponse();
			consentResponse.setConsentTier(consentTier);
			consentResponse.setResponse("Yes");
			consentTierResponseCollection.add(consentResponse);
		}
		collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);	
		System.out.println("calling createSCG in initCollectionProtocolRegistration");
		SpecimenCollectionGroup specimenCollectionGroup = createSCG(collectionProtocolRegistration);
		
		Collection specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>(); 
		collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
		
		Collection collectionProtocolRegistrationCollection = new HashSet();

		
		System.out.println("CPR initiated");
		return collectionProtocolRegistration;
	}

	public  SpecimenCollectionGroup createSCG(CollectionProtocolRegistration collectionProtocolRegistration)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try 
		{
			Collection collectionProtocolEventCollection = collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection();
			Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
			//User user = (User)TestCaseUtility.getObjectMap(User.class);
			User user = getUser("abrink@pathology.wustl.edu");
			while(collectionProtocolEventIterator.hasNext())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolEventIterator.next();
				specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
				specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
				specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
				
				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
				
				Collection cloneSpecimenCollection = new LinkedHashSet();
				Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
				if(specimenCollection != null && !specimenCollection.isEmpty())
				{
					Iterator itSpecimenCollection = specimenCollection.iterator();
					while(itSpecimenCollection.hasNext())
					{
						SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
						if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
						{
							Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,user);
							LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
							specimenLableGenerator.setLabel(cloneSpecimen);
							cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
							cloneSpecimenCollection.add(cloneSpecimen);
						}
					}
				}
				
				specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
			}
		}catch(Exception e)
		{
			System.err.println("Exception in create SCG");
			System.out.println("Exception in create SCG");
			e.printStackTrace();
		}
			return specimenCollectionGroup;
	}

	private Site createSite(){
		
		//String collSite = excel[rowNo][9];
		Site site = new Site();
		site.setId(new Long(700036)); // Siteman Cancer Center
		List<?> resultList1 = null;
	    try
		{
			resultList1 = appService.search(Site.class, site);
			if(resultList1.size() > 0) {
				System.out.println("No of Sites retrived from DB "+ resultList1.size());
				site = (Site)resultList1.get(0);
				System.out.println("site got");
			}
		} catch (Exception e1)
		{
			System.out.println("Exception in searching site");
			System.err.println("Exception in searching site");
			e1.printStackTrace();
		}
		return site;
	}


	public static void main(String arg[]){
		try {
			System.out
					.println("---------IN ExcelTestCaseUtility.addAnticipatedSCGInParticipant-----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
			String excelFilePath = System.getProperty("user.dir")
					+ "/excelFiles/AntiScgForPart.xls";
			ExcelFileReader EX_CP = new ExcelFileReader();
			String allexcel[][] = EX_CP.setInfo(excelFilePath);
			new AddAnticipatedSCGInParticipant().addSCGs(allexcel);
			System.out
					.println("---------END ExcelTestCaseUtility.addAnticipatedSCGInParticipant-----------");
		} catch (Exception e) {
			System.out.println("Exception in addAnticipatedSCGInParticipant");
			System.err.println("Exception in addAnticipatedSCGInParticipant");
			e.printStackTrace();
		}
	}
}
