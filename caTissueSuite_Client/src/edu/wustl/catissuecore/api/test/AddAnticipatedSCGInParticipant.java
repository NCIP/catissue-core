package edu.wustl.catissuecore.api.test;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AddAnticipatedSCGInParticipant
{
	
	private static int rowNo = 1;
	private List CpCpeId = new ArrayList();
	static ApplicationService appService = null;
	private Site site = null;
	private Map collProtEveMap = null;
	private User user = null;
	
	public Long getCpId(String excel[][]) throws Exception {

		String cpShortTitle = excel[rowNo][0].trim();
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
	
	public List getAllCPe(Long cpId) throws Exception {
		List cpeList = null;
		try {
			cpeList  = appService.query(new HQLCriteria("select cpe.id from edu.wustl.catissuecore.domain.CollectionProtocolEvent cpe where cpe.collectionProtocol.id= "+cpId),
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
			List cprResultList  = appService.query(new HQLCriteria("select cpr.id from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id= "+cpId),
					CollectionProtocolEvent.class.getName());		 
			Iterator cprResultIterator = cprResultList.iterator();
			System.out.println("No of CPR retrived " + cprResultList.size());
			int i = 1;
			while (cprResultIterator.hasNext()) {
				Long cprId = (Long) cprResultIterator.next();
				System.out.println("cprId "+ cprId +   " "+ i++);
				getAllSCGs(cprId);
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
  	  if(CpCpeId != null && cpeList != null) {
	  	  Iterator<Long> itr = this.CpCpeId.iterator();
	  	  while(itr.hasNext()) {
	  		  Long id = itr.next();
	  		  if(!cpeList.contains(id)){
	  			  System.out.println("cpe not present "+id );
	  			  addSCGWithNameAndEvents(cprId,id);
	  		  } else {
	  			 // System.out.println("all cpe presents");
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
	             collProtEveMap = new HashMap<Long,CollectionProtocolEvent>();
	             if(cpId != null) {
	            	 CpCpeId = getAllCPe(cpId);
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
		CollectionProtocolEvent collectionProtocolEvent = null;
		if(collProtEveMap.containsKey(cpeId)) {			
			collectionProtocolEvent = (CollectionProtocolEvent)collProtEveMap.get(cpeId);
		} else {
			 collectionProtocolEvent = getCpe(cpeId);
		}
		specimenCollectionGroup =( SpecimenCollectionGroup) createSCG(collectionProtocolEvent);
		if (specimenCollectionGroup != null) {
			//Site site = createSite(); 
			Site site = new Site();
			site.setId(700036L);
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
				System.out.println("scg created successfully ");
			}catch (Exception e){
				System.out.println("Exception in addSCGWithNameAndEvents");
				System.err.println("Exception in addSCGWithNameAndEvents");
	         	e.printStackTrace();
			}
		}
		return specimenCollectionGroup;
	}
	
	private CollectionProtocolEvent getCpe(Long id){
		CollectionProtocolEvent cpe = null;
		System.out.println("getting cpe from database");
		try{
			String query = null;
			HQLCriteria hqlcri = null;
			query = "select collectionProtocolEvent.activityStatus, collectionProtocolEvent.clinicalDiagnosis," +
					" collectionProtocolEvent.clinicalStatus, collectionProtocolEvent.studyCalendarEventPoint, collectionProtocolEvent.collectionPointLabel from edu.wustl.catissuecore.domain.CollectionProtocolEvent" +
					" as collectionProtocolEvent where "+
	   			" collectionProtocolEvent.id= " + id ;
		  hqlcri = new HQLCriteria(query);
	  	  List list = appService.query(hqlcri, CollectionProtocolEvent.class.getName());
	  	  Iterator<Object[]> cpeItr = list.iterator();
		  List cpeIdList = new ArrayList();
		  cpe = new CollectionProtocolEvent();
		  if(cpeItr.hasNext())
		  {
			 Object[] attrlist =  (Object[])cpeItr.next();
			 cpe.setId(id);
			 cpe.setActivityStatus(attrlist[0].toString());
			 cpe.setClinicalDiagnosis(attrlist[1].toString());
			 cpe.setClinicalStatus(attrlist[2].toString());
			 cpe.setStudyCalendarEventPoint(Double.parseDouble(attrlist[3].toString()));
			 cpe.setCollectionPointLabel(attrlist[4].toString());
			 collProtEveMap.put(id, cpe);
		  }
			} catch(Exception ex) {
				ex.printStackTrace();
			}
	  return cpe;
  }
	
//	private CollectionProtocolEvent getCpe(Long id){
//		CollectionProtocolEvent cpe = null;
//		System.out.println("getting cpe from database");
//		try{
//			String query = null;
//			HQLCriteria hqlcri = null;
//			query = "select collectionProtocolEvent from edu.wustl.catissuecore.domain.CollectionProtocolEvent" +
//					" as collectionProtocolEvent where "+
//	   			" collectionProtocolEvent.id= " + id ;
//		  hqlcri = new HQLCriteria(query);
//	  	  List list = appService.query(hqlcri, CollectionProtocolEvent.class.getName());
//	  	  Iterator<CollectionProtocolEvent> cpeItr = list.iterator();
//		  List cpeIdList = new ArrayList();
//		  while(cpeItr.hasNext()){
//			 System.out.println("here 0");
//			 cpe =  (CollectionProtocolEvent)cpeItr.next();
////			 System.out.println("here 1");
////			 cpe = new CollectionProtocolEvent();
////			 cpe.setActivityStatus(attrlist[0].toString());
////			 cpe.setClinicalDiagnosis(attrlist[1].toString());
////			 cpe.setClinicalStatus(attrlist[2].toString());
////			 System.out.println("attrlist[3].toString() "+attrlist[3].toString());
////			 cpe.setStudyCalendarEventPoint(Double.parseDouble(attrlist[3].toString()));
////			 cpe.setCollectionPointLabel(attrlist[4].toString());
////			 System.out.println("here 2");
//			 collProtEveMap.put(id, cpe);
//		  }
//			} catch(Exception ex) {
//				ex.printStackTrace();
//			}
//	  return cpe;
//  }
//	
	
	public  SpecimenCollectionGroup createSCG(CollectionProtocolEvent collectionProtocolEvent)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try 
		{
			if(user == null) {
				user = getUser("abrink@pathology.wustl.edu");
			}
			
			specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
			System.out.println("scg label "+specimenCollectionGroup.getCollectionProtocolEvent().getCollectionPointLabel());
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolEvent.getCollectionProtocolRegistration());
			
//			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory.getInstance("speicmenCollectionGroupLabelGeneratorClass");
//			specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);
			
//			Collection cloneSpecimenCollection = new LinkedHashSet();
//			Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent.getSpecimenRequirementCollection();
//			if(specimenCollection != null && !specimenCollection.isEmpty())
//			{
//				Iterator itSpecimenCollection = specimenCollection.iterator();
//				while(itSpecimenCollection.hasNext())
//				{
//					SpecimenRequirement reqSpecimen = (SpecimenRequirement)itSpecimenCollection.next();
//					if(reqSpecimen.getLineage().equalsIgnoreCase("new"))
//					{
//						Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,null,specimenCollectionGroup,user);
////						LabelGenerator specimenLableGenerator = LabelGeneratorFactory.getInstance("specimenLabelGeneratorClass");
////						specimenLableGenerator.setLabel(cloneSpecimen);
//						cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
//						cloneSpecimenCollection.add(cloneSpecimen);
//					}
//				}
//			}
//			
//			specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
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
		
		System.out.println("getting User ");
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
		
		if(user == null) {
			user = getUser("abrink@pathology.wustl.edu");
		}
		collectionEventParameters.setUser(user);	
		collectionEventParameters.setSpecimenCollectionGroup(sprObj);	
		
		//Received Events		
		receivedEventParameters.setComment("");
		
		User receivedUser = user;
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
	  
	    if(user == null) {
			user = getUser("abrink@pathology.wustl.edu");
		}
	    User collectionEventUser = user;
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
		    if(user == null) {
				user = getUser("abrink@pathology.wustl.edu");
			}
			User receivedEventUser = user;
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
						System.out.println("in setting event ");
						}
						else if(spEvPar instanceof  ReceivedEventParameters ) {
							//ReceivedEventParameters) spEvPar;
						}
					}
				}
	}

	private Site createSite(){
		
		//String collSite = excel[rowNo][9];
//		Site site = null;
		if(site == null ){
			System.out.println("getting site");
			site = new Site();
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
		}
		return site;
	}

	
	public static void setUp(){
		
		System.setProperty("javax.net.ssl.trustStore", "D:/Main/pcatissue/jboss-4.2.2.GA/server/default/conf/jbosskey.keystore");
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("catissue_admin@persistent.co.in", "Test$56");
		} 	
					
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			System.exit(1);
		}		
	}

	public static void main(String arg[]){
		try {
			System.out
					.println("---------IN AddAnticipatedSCGInParticipant.Main-----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
			String excelFilePath = System.getProperty("user.dir")
					+ "/excelFiles/AntiScgForPart.xls";
			ExcelFileReader EX_CP = new ExcelFileReader();
			setUp();
			String allexcel[][] = EX_CP.setInfo(excelFilePath);
			new AddAnticipatedSCGInParticipant().addSCGs(allexcel);
			System.out
					.println("---------END AddAnticipatedSCGInParticipant.Main-----------");
		} catch (Exception e) {
			System.out.println("Exception in addAnticipatedSCGInParticipant");
			System.err.println("Exception in addAnticipatedSCGInParticipant");
			e.printStackTrace();
		}
	}
}
