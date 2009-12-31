package edu.wustl.catissuecore.api.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

public class DebounDataMigration {

	private static int rowNo = 1;
	static ApplicationService appService = null;
    /**
     * This method registers participants given in the excel,collects anticipated scg and specimen and creates/collects aliquots.
     * @param excel String double array 
     */
	public void registerAndCollectSCG(String excel[][]) throws Exception {

		appService = ApplicationServiceProvider.getApplicationService();
		String cpShortTitle = excel[rowNo][0];
		
		CollectionProtocol collectionProtocol = getCP(cpShortTitle);
		Participant participant  = null;
	
		if(collectionProtocol.getId() != null ) {
			while(rowNo < excel.length -1){
	    	   try {
					System.out.println("inside While rowNo "+ rowNo);
					String studyLabel = excel[rowNo][10].trim();
					participant  = null;
					Collection<Specimen> specList = null; 
					SpecimenCollectionGroup scg = null;
					if(participant == null){ // participant add
				    	 participant = createAndRegisterPartToCp(excel, collectionProtocol);
					     scg = getSCG(participant, studyLabel);
					} 
				   if(specList == null && scg != null) {
					   	   specList = scg.getSpecimenCollection();
						   System.out.println("specList.size() "+ specList.size());
				   	}
				    if(specList.size() >= 0) {
				    	System.out.println("No of specimens in scg "+ specList.size());
				    	
				    	Iterator<Specimen> spcItr = specList.iterator();
					    	while(spcItr.hasNext()){ 
					    		Specimen specimen = (Specimen)spcItr.next(); ///////////////
					    		System.out.println("specimen.getLineage() "+specimen.getLineage());
					    		if(specimen.getLineage().equals("New"))
					    		{
					    			scg  = specimen.getSpecimenCollectionGroup();
					    			if (scg != null) {
					    			   updateSCG(excel, scg);
					    				specimen.setSpecimenCollectionGroup(scg);
					    			}
					    			updateSpecimens(specimen, excel);
					    		}
					    	} 
			      }
			  }catch (Exception ex)
				{
					System.out.println("Exception in  setCPRAndSCG for participant " + participant.getLastName() + " "+ participant.getFirstName());
					System.err.println("Exception in  setCPRAndSCG for participant " + participant.getLastName() + "" + participant.getFirstName());
					ex.printStackTrace();
					throw ex;
				}
		   	rowNo++;
			} // while ended
				System.out.println("done rowNo "+ rowNo);
		  
		} // if ended
}
	/**
     * This method searches collection site give in the excel
     * @param excel String double array 
     */
 private Site getSite(String[][] excel) throws Exception{
	
	String collSite = excel[rowNo][9];
	Site site = new Site();
	site.setName(collSite);
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
		throw e1;
	}
	return site;
}

 /**
  * This method  sets event parameters as given in the excel sheet
  * @param excel String double array
  * @param scgObj
  */
 private void setEventParameters(SpecimenCollectionGroup scgObj, String excel[][], int row)
 {
	String collCotainer = excel[row][8];
	String collProcedure = excel[row][7];
	String collDate = excel[row][6];
	
	System.out.println("Inside setEventParameters for SCG");
	Collection<SpecimenEventParameters> specimenEventParametersCollection = new HashSet<SpecimenEventParameters>();
	CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
	ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
	collectionEventParameters.setCollectionProcedure(collProcedure);
	collectionEventParameters.setComment("");
	collectionEventParameters.setContainer(collCotainer);
	try{
		//Date date = new SimpleDateFormat("mm-dd-yyyy").parse(collDate);
		System.out.println("collDate "+ collDate);
		/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date timestamp = EventsUtil.setTimeStamp(collDate,"15","45");
		Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		collectionEventParameters.setTimestamp(timestamp);
		System.out.println("set collDate "+ collDate);
	}catch(Exception e){
		System.out.println("Exception while setting colection date in setEventParameters");
		System.err.println("Exception while setting colection date in setEventParameters");
		e.printStackTrace();
	}
	
	User user = getUser(1L);
	collectionEventParameters.setUser(user);	
	collectionEventParameters.setSpecimenCollectionGroup(scgObj);	
	
	//Received Events		
	receivedEventParameters.setComment("");
	User receivedUser = getUser(1L);
	receivedEventParameters.setUser(receivedUser);
	receivedEventParameters.setReceivedQuality("Not Specified");	

	/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
	//Date receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
	Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
	receivedEventParameters.setTimestamp(receivedTimestamp);		
	receivedEventParameters.setSpecimenCollectionGroup(scgObj);
	specimenEventParametersCollection.add(collectionEventParameters);
	specimenEventParametersCollection.add(receivedEventParameters);
	scgObj.setSpecimenEventParametersCollection(specimenEventParametersCollection);
 }

 /**
  * This method  sets event parameters of the specimens as given in the excel sheet
  * @param excel String double array
  * @param specimen
  * @param row
  */
 	private  void setSpecimenEvents(Specimen specimen,  String excel[][], int row) throws Exception
 	{
	System.out.println("In setSpecimenEvents");	
	String collCotainer = excel[row][8];
	String collProcedure = excel[row][7];
	String collDate = excel[row][6];
	
    Collection<SpecimenEventParameters> specimenEventCollection = new LinkedHashSet<SpecimenEventParameters>();
    CollectionEventParameters collectionEvent = null;
	ReceivedEventParameters receivedEvent = null;
	
    Collection<SpecimenEventParameters> evColl = specimen.getSpecimenEventCollection();
	if(evColl != null && evColl.size() != 0) {
		System.out.println("got eventColl from specimen ");
		Iterator<SpecimenEventParameters> itr  = evColl.iterator();
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
  
	 if(collectionEvent == null) 
		collectionEvent = new CollectionEventParameters();
  
    User collectionEventUser = getUser(1L);
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
	 /*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
	// Date timestamp = EventsUtil.setTimeStamp(collDate,"15","45");
	  Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
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
		User receivedEventUser = getUser(1L);
		receivedEvent.setUser(receivedEventUser);
   // }
	    
	try
	{
		/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
		Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
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

/**
  * This method initialize participant object  as given in the excel sheet
  * @param excel String double array
  */
 	
private  Participant initParticipant(String excel[][]) 
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
	
	Collection<Race> raceCollection = new HashSet<Race>();
	Race race = new Race();
	race.setRaceName("White");
	race.setParticipant(participant);
	raceCollection.add(race);
	
	race = new Race();
	race.setRaceName("Unknown");
	race.setParticipant(participant);
	raceCollection.add(race);

	participant.setRaceCollection(raceCollection);
	participant.setActivityStatus("Active");
	participant.setEthnicity("Hispanic or Latino");
	System.out.println("Participant initiated successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
	return participant;
}

/**
 * This method initialize collection protocol object  as values given in the excel sheet
 * @param participant
 * @param excel String double array
 * @param cp
 */
	
private  CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant, CollectionProtocol cp, String excel[][])
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
        /*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
		//Date timestamp = EventsUtil.setTimeStamp(colldate,"15","45");
        Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
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
	User user = getUser(1L);
	collectionProtocolRegistration.setConsentWitness(user);
	
	Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
	Collection<ConsentTier> consentTierCollection = cp.getConsentTierCollection();
	Iterator<ConsentTier> consentTierItr = consentTierCollection.iterator();
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
    createSCG(collectionProtocolRegistration);
	
	Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>(); 
	collectionProtocolRegistration.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);
	System.out.println("CPR initiated");
	return collectionProtocolRegistration;
}

/**
 * This method creates specimen collection group
 * @param collectionProtocolRegistration 
 */

	private  SpecimenCollectionGroup createSCG(CollectionProtocolRegistration collectionProtocolRegistration)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try 
			{
			Collection collectionProtocolEventCollection = collectionProtocolRegistration.getCollectionProtocol().getCollectionProtocolEventCollection();
			Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
			//User user = (User)TestCaseUtility.getObjectMap(User.class);
			User user = getUser(1L);
			while(collectionProtocolEventIterator.hasNext())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)collectionProtocolEventIterator.next();
				specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
				specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
				specimenCollectionGroup.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);
				
				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
				
				Collection<Specimen> cloneSpecimenCollection = new LinkedHashSet<Specimen>();
				Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
				if(specimenCollection != null && !specimenCollection.isEmpty())
				{
					Iterator<SpecimenRequirement> itSpecimenCollection = specimenCollection.iterator();
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
			//System.err.println("Exception in create SCG");
			//System.out.println("Exception in create SCG");
			//e.printStackTrace();
		}
			return specimenCollectionGroup;
	}
	
	/**
	 * This method creates clone specimen 
	 * @param collectionProtocolRegistration 
	 */

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
	
/**
 * This method searches user  
 * @param id
 */
	private User getUser(Long id) {
	
		User user = new User();
		// user.setLoginName(loginName);
		user.setId(new Long(1)); // changes made for nightly build inegration
		try {
			List resultList = appService.search(User.class, user);
	
			if (resultList != null && resultList.size() > 0) {
				user = (User) resultList.get(0);
			}
		} catch (Exception e) {
			System.out.println("Exception in getUser");
			System.err.println("Exception in getUser");
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * This method creates aliquot specimen if number of aliquot specimen (# of Cryo vials (aliquots))in the excel is more then anticipated aliquot specimens.
	 * @param parentSpecimen
	 * @param excel String double array
	 * @param aliquotCount number of aliquots to be created
	 * @param quantityPerAliquot 
	 * @param aliquoteNo 
	 */
	 private  void getAliquotsSpecimen(Specimen parentSpecimen , String excel[][],int row, int aliquotCount, Double quantityPerAliquot, int aliquoteNo )throws Exception
	{  
		System.out.println("In Aliquot Specimen");
		String noOfAliquotes = excel[row][17].trim();
		String specimenType = excel[rowNo][13];
		String storageContainer = excel[rowNo][18];
		if(storageContainer != null && storageContainer != ""){
			storageContainer = storageContainer.trim();
		}
		try
		{
			for(int i = 1; i <= aliquotCount; i++)
			{
				System.out.println(" i "+ i);
				Specimen specimen = new Specimen();
				//specimen = createSpecimen(specimen, parentSpecimen, excel, row, lineage);
			    System.out.println("parentSpecimen.getSpecimenClass() " +parentSpecimen.getSpecimenClass());
				if(parentSpecimen.getSpecimenClass().equals("Tissue"))
				{
					System.out.println("cretating Tissue aliquote specimen");
					specimen = new TissueSpecimen();
				}
				else if(parentSpecimen.getSpecimenClass().equals("Fluid"))
				{
					specimen = new FluidSpecimen();
					specimen.setSpecimenClass("Fluid");
				}
				else if(parentSpecimen.getSpecimenClass().equals("Cell"))
				{
					specimen = new CellSpecimen();
				}
				else if(parentSpecimen.getSpecimenClass().equals("Molecular"))
				{
					specimen = new MolecularSpecimen();
				}
				System.out.println("parentSpecimen.getSpecimenType() "+parentSpecimen.getSpecimenType());
				specimen.setSpecimenType(parentSpecimen.getSpecimenType());;
				specimen.setParentSpecimen(parentSpecimen);
	            specimen.setLineage("Aliquot");
	            setSpecimenEvents(specimen, excel, row);
	            specimen.setSpecimenClass(parentSpecimen.getSpecimenClass());
	
				System.out.println("in getAliquotsSpecimen after calling setSpecimenEvents");
				specimen.setInitialQuantity(quantityPerAliquot);
				specimen.setAvailableQuantity(quantityPerAliquot);
				specimen.setNoOfAliquots(0); // c ltr
			    specimen.setSpecimenCharacteristics(parentSpecimen.getSpecimenCharacteristics());
			    System.out.println("aliquoteNo "+ aliquoteNo);
				System.out.println("aliquoteNo "+ aliquoteNo);
				specimen.setLabel(parentSpecimen.getLabel()+"_"+ ++aliquoteNo);
			    StorageContainer sc = new StorageContainer();
			    System.out.println("Storage Container "+ storageContainer);
			    sc.setName(storageContainer);
			    SpecimenPosition specPos = new SpecimenPosition();
				specPos.setStorageContainer(sc);
				specPos.setSpecimen(specimen);
				//specPos.setPositionDimensionOne(new Integer(2));
				//specPos.setPositionDimensionTwo(new Integer(2));
				specimen.setSpecimenPosition(specPos);
				specimen.setIsAvailable(new Boolean(true));
				specimen.setActivityStatus("Active");
				specimen.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
			    
				System.out.println("in getAliquotsSpecimen aliquot type " + specimen.getSpecimenType());
				System.out.println("parentSpecimen "+ parentSpecimen);
				specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
				System.out.println(" in getAliquots parentSpecimen.getSpecimenCollectionGroup  "+ parentSpecimen.getSpecimenCollectionGroup());
			   try {
				    appService.createObject(specimen);
					 }catch(Exception e){
						   System.out.println("Exception in creating aliquot so fixing virtual storage container");
						   System.err.println("Exception in creating aliquot so fixing virtual storage container ");
						   e.printStackTrace();
						   specimen.setSpecimenPosition(null);  // if storage container is full
						   appService.updateObject(specimen);
					 }
			    System.out.println("Aliquote created ");
				
			}
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("Exception in getChildSpecimenMap"+ex);
			System.err.println("Exception in getChildSpecimenMap"+ex);
	
		}
		//return specColl;
	} 

 /**
	 * This method searches collection protocol 
	 * @param cpShortTitle 
 */

  private CollectionProtocol getCP(String cpShortTitle) throws Exception {
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setShortTitle(cpShortTitle);
		System.out.println("cpShortTitle " + cpShortTitle);
		List<?> resultList1 = null;
		try {
			resultList1 = appService.search(CollectionProtocol.class,
					collectionProtocol);
			System.out.println("No of CPs retrived from DB "
					+ resultList1.size());
			if (resultList1.size() > 0) {
				collectionProtocol = (CollectionProtocol) resultList1.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return collectionProtocol;
	}

  /**
	 * This method creates and register participant to collection protocol 
	 * @param excel String double array
	 * @param  collectionProtocol
*/
  private Participant createAndRegisterPartToCp(String excel[][],
			CollectionProtocol collectionProtocol) throws Exception {
		System.out.println("in registerpartToCp");
		Participant participant = initParticipant(excel);
		CollectionProtocolRegistration collectionProtocolRegistration = initCollectionProtocolRegistration(
				participant, collectionProtocol, excel);

		Collection collectionProtocolRegistrationCollection = new HashSet();
		collectionProtocolRegistrationCollection
				.add(collectionProtocolRegistration);
		participant
				.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

		participant = (Participant) appService.createObject(participant);
		System.out.println("Participant Added .. id is " + participant.getId());
		return participant;
	}
 
  /**
	 * This method searches specimen collection group in the cpr of the participant
	 * @param studyLabel study calender event label/point given in the excel
	 */
 private SpecimenCollectionGroup getSCG(Participant participant,
			String studyLabel) {
		SpecimenCollectionGroup scg = null;
		Collection cprColl = participant
				.getCollectionProtocolRegistrationCollection();
		System.out.println("No of cpr retreived from participant "
				+ cprColl.size());
		Iterator<CollectionProtocolRegistration> cprItr = cprColl.iterator();
		while (cprItr.hasNext()) {
			CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cprItr
					.next();
			Collection scgCollection = cpr
					.getSpecimenCollectionGroupCollection();

			Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
			System.out.println("scgCollection.size() " + scgCollection.size());
			while (scgItr.hasNext()) {
				scg = (SpecimenCollectionGroup) scgItr.next();
				System.out.println("studyLabel " + studyLabel);
				System.out.println("CollectionPointLabel() "
						+ scg.getCollectionProtocolEvent()
								.getCollectionPointLabel());
				if (scg.getCollectionProtocolEvent().getCollectionPointLabel()
						.equals(studyLabel))
					break;
				else
					scg = null;
			}
		}
		return scg;
	}
  
 /**
	 * This method updates/collects scg
	 * @param  @param excel String double array
	 * @param scg object
	 */
 private void updateSCG(String[][] excel, SpecimenCollectionGroup scg) throws Exception{
	   Site site = getSite(excel);
	   scg.setSpecimenCollectionSite(site);
	   scg.setCollectionStatus("Complete");
	   
	   scg.setClinicalStatus("Not Specified");
	   scg.setClinicalDiagnosis("Not Specified");
	   setEventParameters(scg, excel, rowNo);
	   
	   appService.updateObject(scg);
	   System.out.println("scg updated");
 }
 /**
	 * This method updates new specimen
	 * @param  @param excel String double array
	 * @param specimen object
	 */ 
 private void updateSpecimens(Specimen specimen, String[][] excel) throws Exception{
	 
		String parentLabel = excel[rowNo][11].trim().split("-")[1]+"P";
		String tissueSite = excel[rowNo][14];
    	String pathoStatus = excel[rowNo][15];

		System.out.println("parentLabel "+ parentLabel);
		specimen.setLabel(parentLabel);
		specimen.setInitialQuantity(Double.parseDouble("3"));
		setSpecimenEvents(specimen, excel, rowNo);
		specimen.setCollectionStatus("Collected");

		SpecimenCharacteristics specimenCharacteristics = specimen.getSpecimenCharacteristics();
		specimenCharacteristics.setTissueSide("Not Specified");
		specimenCharacteristics.setTissueSite(tissueSite);
		specimen.setSpecimenCharacteristics(specimenCharacteristics);
		specimen.setPathologicalStatus(pathoStatus);
		
		/* collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy **/
		//Date timestamp = EventsUtil.setTimeStamp(excel[rowNo][6],"15","45");
		Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
		specimen.setCreatedOn(timestamp);
		
		appService.updateObject(specimen);
        System.out.println("New Specimen updated");
        updateDrivedSpecimens(specimen, excel);
     }
 /**
	 * This method updates derivative specimen
	 * @param  @param excel String double array
	 * @param specimen object
	 */ 
 
	 private void updateDrivedSpecimens(Specimen specimen, String[][] excel) throws Exception {
		 String query = null;
	     HQLCriteria hqlcri = null;
		 Collection<Specimen> specList = null;  
		 String pathoStatus = excel[rowNo][15];
			
		 Collection<AbstractSpecimen> childSpecColl = null;
		 query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
		     "  specimen.parentSpecimen.id = " + specimen.getId();
		 
		 System.out.println("childSpecimen query "+ query);
		   hqlcri = new HQLCriteria(query);
		   specList = appService.query(hqlcri, Specimen.class.getName());
		   System.out.println("No of Childspecimens in New Specimen " + specList.size());  
		 
			Iterator<Specimen> childItr = specList.iterator();
			//int tempRowNo = rowNo;
			while(childItr.hasNext()){
				Specimen drvSpec = (Specimen)childItr.next();
				String drivedSpecClass = excel[rowNo][12];
				String drivedSpecType = excel[rowNo][13];
				System.out.println("drv spc "+ drvSpec.getSpecimenClass() + " "+ drvSpec.getSpecimenType() );
				System.out.println("excel "+drivedSpecClass +" "+ drivedSpecType );
				if(drvSpec.getLineage().equals("Derived") && 
						drvSpec.getSpecimenClass().equals(drivedSpecClass) &&
						drvSpec.getSpecimenType().equals(drivedSpecType)) {
						String specimenLable = excel[rowNo][11];
						String drvInitQuantityExcel = excel[rowNo][16].trim();
				   
						drvSpec.setLabel(specimenLable);
						drvSpec.setSpecimenCharacteristics(drvSpec.getParentSpecimen().getSpecimenCharacteristics());
						setSpecimenEvents(drvSpec, excel, rowNo);		       
						drvSpec.setPathologicalStatus(pathoStatus);
						if(drvInitQuantityExcel.contains("ml")) {
							drvInitQuantityExcel = drvInitQuantityExcel.split("ml")[0].trim();
					    }
						
						double quantityDO = drvSpec.getInitialQuantity();
						drvSpec.setInitialQuantity(quantityDO + Double.parseDouble(drvInitQuantityExcel));
				     	drvSpec.setCollectionStatus("Collected");
		//				specimenDomainCollection.add(drvSpec);	
						drvSpec.setCreatedOn(((Specimen)drvSpec.getParentSpecimen()).getCreatedOn());
						appService.updateObject(drvSpec);
				        System.out.println("drvSpec updated");
				        updateAliquotSpecimens(drvSpec, excel, drvInitQuantityExcel);
		
				}
			}
	}
	 
	 /**
		 * This method updates and creates aliquot specimen
		 * @param  @param excel String double array
		 * @param specimen object
		 * @param drvInitQuantityExcel this quantity is divided among the all aliquot specimen of the one derivative
		 */ 
	 private void updateAliquotSpecimens(Specimen drvSpec, String[][] excel,String drvInitQuantityExcel ) throws Exception {
		 
		 String query = null;
	     HQLCriteria hqlcri = null;
		 Collection<Specimen> specList = null;  
		 String pathoStatus = excel[rowNo][15];
	     int noOfAliquotsInExcel = (int)Double.valueOf(excel[rowNo][17].trim()).doubleValue();
	     System.out.println("noOfAliquotsInExcel "+ noOfAliquotsInExcel);
		   	Double quantityPerAliquot = (Double.parseDouble(drvInitQuantityExcel)/noOfAliquotsInExcel) ;
		   	
		   	query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
	     "  specimen.parentSpecimen.id = " + drvSpec.getId() + " and specimen.collectionStatus != 'Collected'" ;
	 
		   	hqlcri = new HQLCriteria(query);
		   	Collection<Specimen> childNotCollectedList = null; 
		   	childNotCollectedList = appService.query(hqlcri, Specimen.class.getName());
		   	System.out.println("No of Childspecimens in drv which are not colllected " + childNotCollectedList.size());  	
		   	
			Iterator<Specimen> childCollectedItr = childNotCollectedList.iterator();
		//	int aliquoteNo = 0;
			
			query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where " +
	     "  specimen.parentSpecimen.id = " + drvSpec.getId() + " and specimen.collectionStatus = 'Collected'" ;
	 
		   	System.out.println("childSpecimen query "+ query);
		   	hqlcri = new HQLCriteria(query);
		     Collection<Specimen> childCollectedList = appService.query(hqlcri, Specimen.class.getName());
		     int aliquoteNo = childCollectedList.size();
		   	 System.out.println("No of Childspecimens in drv which are colllected " + childCollectedList.size());  	
			
			int i = 1;
			for( i = 1; i <= noOfAliquotsInExcel && childCollectedItr.hasNext(); i++) {
				Specimen aliquotSpec = (Specimen)childCollectedItr.next();
			    if(aliquotSpec.getLineage().equals("Aliquot")) {
				   aliquoteNo++;
				   aliquotSpec.setCollectionStatus("Collected");
				   aliquotSpec.setLabel(aliquotSpec.getParentSpecimen().getLabel()+"_"+aliquoteNo);
			       setSpecimenEvents(aliquotSpec, excel, rowNo);
			       
			   	   System.out.println("aliquot quantityPerAliquot  "+ quantityPerAliquot);
			   	   aliquotSpec.setInitialQuantity(quantityPerAliquot);
				   
			       String storageContainer = excel[rowNo][18];
			       if(storageContainer != null && storageContainer != ""){
			   		storageContainer = storageContainer.trim();
			   	}
			      
			       if (storageContainer == "Virtual") {
			    	   System.out.println("Storage container is Virtual");
			    	   aliquotSpec.setSpecimenPosition(null);
			       } else {
				       try{
					       StorageContainer sc = new StorageContainer();
					       System.out.println("storageContainer "+ storageContainer.trim());
						   sc.setName(storageContainer);
						   SpecimenPosition specPos = new SpecimenPosition();
						   specPos.setStorageContainer(sc);
						   specPos.setSpecimen(aliquotSpec);
						   aliquotSpec.setSpecimenPosition(specPos);
						   System.out.println("storageContainer in aliquote"+ aliquotSpec.getSpecimenPosition().getStorageContainer().getName());
	
				   	    }catch (Exception e){
					   		   System.out.println("Exception while allocation storage container for aliquot "+ storageContainer);
					   		   System.err.println("Exception while allocation storage container for aliquot "+ storageContainer);
					   		   e.printStackTrace();
				   	   }
			       }
				   aliquotSpec.setSpecimenCharacteristics(aliquotSpec.getParentSpecimen().getSpecimenCharacteristics());
	//									   specimenDomainCollection.add(aliquotSpec);
				   aliquotSpec.setCreatedOn(((Specimen)aliquotSpec.getParentSpecimen()).getCreatedOn());
				 try {
				   appService.updateObject(aliquotSpec);
				 }catch(Exception e){
					   System.out.println("Exception in updating aliquot so fixing virtual storage container");
					   System.err.println("Exception in updating aliquot so fixing virtual storage container ");
					   e.printStackTrace();
					   aliquotSpec.setSpecimenPosition(null);  // if storage container is full
					   appService.updateObject(aliquotSpec);
				 }
				   System.out.println("Aliquote updated ");
				   query = "select specimenCollectionGroup from edu.wustl.catissuecore.domain.Specimen as specimen where " +
		            "  specimen.id = " + aliquotSpec.getId();
			   }
	} //for ends 
			//if(i < noOfAliquotsInExcel) {
				if(childNotCollectedList.size() < noOfAliquotsInExcel) {
				int noOfAlqTobeCreated = noOfAliquotsInExcel - childNotCollectedList.size();
				System.out.println("more aliquots to be created "+ noOfAlqTobeCreated);
				getAliquotsSpecimen(drvSpec, excel, rowNo, noOfAlqTobeCreated, quantityPerAliquot,aliquoteNo);
			}
	 }
			
} //class closed

