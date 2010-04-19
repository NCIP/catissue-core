package edu.wustl.catissuecore.api.test;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



public class AddAnticipatedSCGInParticipant
{

	private List CpCpeId = new ArrayList();
	static ApplicationService appService = null;
	private Site site = null;
	private Map collProtEveMap = null;
	private User user = null;

	public Long getCpId(String cpTitle) throws Exception {
		System.out.println("Started getCpId");
		String cpShortTitle = cpTitle.trim();
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
	System.out.println("Completed getCpId");
		return cpId;
	}

	public List getAllCPe(Long cpId) throws Exception {
		System.out.println("Started getAllCPe");
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
		System.out.println("Completed getAllCPe");
		return cpeList;
	}

	public void getAllCPR(Long cpId)
			throws Exception {
		System.out.println("Started getAllCPR");
		try {
			List cprResultList  = appService.query(new HQLCriteria("select cpr.id, cpr.registrationDate from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.collectionProtocol.id= "+cpId),
					CollectionProtocolEvent.class.getName());
			Iterator cprResultIterator = cprResultList.iterator();
			System.out.println("No of CPR retrived " + cprResultList.size());
			Date regDate = null;
			while (cprResultIterator.hasNext()) {
				final Object[] obj = (Object[]) cprResultIterator.next();
				Long cprId = (Long) obj[0];
				regDate = (Date) obj[1];
				List consentTierResponseCollection  = appService.query(new HQLCriteria
						("select elements(cpr.consentTierResponseCollection) from edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr where cpr.id= "+cprId),
						CollectionProtocolRegistration.class.getName());
				getAllSCGs(cprId, regDate, consentTierResponseCollection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		System.out.println("Completed getAllCPR");
	}

	public void getAllSCGs(Long cprId, Date regDate, List consentTierResponseCollection) {
		System.out.println("Started getAllSCGs");
		try
		{
			String query = null;
			HQLCriteria hqlcri = null;
			query = "select specimenCollectionGroup.collectionProtocolEvent.id from edu.wustl.catissuecore.domain.SpecimenCollectionGroup" +
					" as specimenCollectionGroup where "+
	   			" specimenCollectionGroup.collectionProtocolRegistration.id= " + cprId ;
	   			hqlcri = new HQLCriteria(query);
	  	  List cpeList = appService.query(hqlcri, CollectionProtocolEvent.class.getName());

	  	  if(CpCpeId != null && cpeList != null)
	  	  {
		  	  Iterator<Long> itr = this.CpCpeId.iterator();
		  	  while(itr.hasNext()) {
		  		  Long id = itr.next();
		  		  if(!cpeList.contains(id)){
		  			  System.out.println("cpe not present "+id );
		  			  addSCGWithNameAndEvents(cprId,id, regDate, consentTierResponseCollection);
		  		  }
		  	  }
		  }
	 	}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println("Completed getAllSCGs");
  }

	 public void addSCGs(String cpTitle)  {
		 System.out.println("Started addSCGs");
             try {
	             Long cpId = getCpId(cpTitle);
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
             CpCpeId = new ArrayList();
             System.out.println("Completed addSCGs");
	 }

	public SpecimenCollectionGroup addSCGWithNameAndEvents(Long cprId, Long cpeId, Date regDate, Collection consentTierResponseCollection)
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
			Site site = new Site();
			site.setId(1L);
			System.out.println("site null");
			specimenCollectionGroup.setSpecimenCollectionSite(site);

			String scgName="scg added through api"+UniqueKeyGeneratorUtil.getUniqueKey();
			specimenCollectionGroup.setName(scgName);
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setId(cprId);
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
			System.out.println("set cpr in scg");
			setEventParameters(specimenCollectionGroup, regDate);
			try
			{
				if(consentTierResponseCollection != null && consentTierResponseCollection.size() > 0)
				{
					setConsentToSCG(specimenCollectionGroup, consentTierResponseCollection);
				}
				specimenCollectionGroup = (SpecimenCollectionGroup) appService.createObject(specimenCollectionGroup);
				System.out.println("scg created successfully");
				if(consentTierResponseCollection != null && consentTierResponseCollection.size() > 0)
				{
					setConsentToSP(specimenCollectionGroup, consentTierResponseCollection);
				}
				System.out.println("scg updated successfully ");
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

	public  SpecimenCollectionGroup createSCG(CollectionProtocolEvent collectionProtocolEvent)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try
		{
			if(user == null) {
				user = getUser("admin@admin.com");
			}

			specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
			System.out.println("scg label "+specimenCollectionGroup.getCollectionProtocolEvent().getCollectionPointLabel());
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolEvent.getCollectionProtocolRegistration());
		}catch(Exception e)
		{
			System.out.println("Exception in create SCG");
			System.err.println("Exception in create SCG");
			e.printStackTrace();
		}
		return specimenCollectionGroup;
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


	private void setEventParameters(SpecimenCollectionGroup sprObj, Date regDate)
	{
		System.out.println("Inside setEventParameters for SCG");
		Collection specimenEventParametersCollection = new HashSet();
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		collectionEventParameters.setCollectionProcedure("Not Specified");
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer("Not Specified");
		System.out.println("befor setting date");
		Calendar cal = new GregorianCalendar();
		cal.setTime(regDate);
		cal.add(cal.DAY_OF_MONTH, sprObj.getCollectionProtocolEvent().getStudyCalendarEventPoint().intValue());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = sdf.format(cal.getTime());
		System.out.println("strdate = " + strDate);
		try
		{
			Date timestamp = EventsUtil.setTimeStamp(strDate,"15","45");
			collectionEventParameters.setTimestamp(timestamp);
			collectionEventParameters.setTimestamp(timestamp);
		}catch(Exception e){
			System.out.println("Exception while setting colection date in setEventParameters");
			e.printStackTrace();
		}

		if(user == null) {
			user = getUser("admin@admin.com");
		}
		collectionEventParameters.setUser(user);
		collectionEventParameters.setSpecimenCollectionGroup(sprObj);

		//Received Events
		receivedEventParameters.setComment("");

		User receivedUser = user;
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality("Not Specified");
		Date receivedTimestamp = EventsUtil.setTimeStamp(strDate,"15","45");
		receivedEventParameters.setTimestamp(receivedTimestamp);
		receivedEventParameters.setSpecimenCollectionGroup(sprObj);
		specimenEventParametersCollection.add(collectionEventParameters);
		specimenEventParametersCollection.add(receivedEventParameters);
		sprObj.setSpecimenEventParametersCollection(specimenEventParametersCollection);
	}

	public static void setUp(){

		System.setProperty("javax.net.ssl.trustStore", "E:/WashU/jboss-4.2.2.GA_https/server/default/conf/chap8.keystore");
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
			System.exit(1);
		}
	}

	private void setConsentToSCG(SpecimenCollectionGroup scgObj, Collection consentTierResponseCollection) throws Exception
	{
		System.out.println("Started setConsentToSCG:::::352");
		List respList = (List)consentTierResponseCollection;
		System.out.println("respList.size() "+ respList.size());
		Iterator consentTierItr = respList.iterator();
		Collection consentTierStatusCollection = new HashSet();
		while(consentTierItr.hasNext())
		{
			ConsentTierResponse consentTierResponse = (ConsentTierResponse)consentTierItr.next();
			ConsentTier ct = consentTierResponse.getConsentTier();
			System.out.println("consent id "+ ct.getId());
			ConsentTierStatus consentStatus = new ConsentTierStatus();
			consentStatus.setConsentTier(ct);
			consentStatus.setStatus("Not Specified");
			consentTierStatusCollection.add(consentStatus);
		}
		scgObj.setConsentTierStatusCollection(consentTierStatusCollection);
		scgObj.getCollectionProtocolRegistration().getCollectionProtocol();
		System.out.println("Completed setConsentToSCG:::::369");

//		return scgObj;
	}

	private void setConsentToSP(SpecimenCollectionGroup scgObj, Collection consentTierResponseCollection) throws Exception
	{
		System.out.println("Started setConsentToSP:::::376");
		String query = "select elements(specimenCollection) from edu.wustl.catissuecore.domain.SpecimenCollectionGroup scg where scg.id='"+scgObj.getId().toString()+"'";
		List spList = appService.query(new HQLCriteria(query), SpecimenCollectionGroup.class.getName());

		Iterator itr = spList.iterator();
		Collection ctsColl = scgObj.getConsentTierStatusCollection();

		while(itr.hasNext())
		{
			Specimen sp = (Specimen)itr.next();
			System.out.println("sp label "+ sp.getLabel());
			setConsentTierStatus(sp, ctsColl);
			System.out.println("Before specimen Update");
			System.out.println("sp label "+ sp.getLabel());
			sp = (Specimen) appService.updateObject(sp);
			System.out.println("specimen updated successfully ");
		}
		System.out.println("Completed setConsentToSP:::::393");
	}

	/**
	 * @param specimen : specimen
	 * @param consentTierStatusCollection : specimen
	 */
	private void setConsentTierStatus(Specimen specimen,
			Collection<ConsentTierStatus> consentTierStatusCollection)
	{
		final Collection<ConsentTierStatus> consentTierStatusCollectionForSpecimen = new HashSet<ConsentTierStatus>();
		if (consentTierStatusCollection != null)
		{
			final Iterator<ConsentTierStatus> itr = consentTierStatusCollection.iterator();
			while (itr.hasNext())
			{
				final ConsentTierStatus conentTierStatus = (ConsentTierStatus) itr.next();
				final ConsentTierStatus consentTierStatusForSpecimen = new ConsentTierStatus();
				consentTierStatusForSpecimen.setStatus(conentTierStatus.getStatus());
				consentTierStatusForSpecimen.setConsentTier(conentTierStatus.getConsentTier());
				consentTierStatusCollectionForSpecimen.add(consentTierStatusForSpecimen);
			}
			specimen.setConsentTierStatusCollection(consentTierStatusCollectionForSpecimen);
		}
	}

	/**
	 * Method to read the CSV file containing CP Short Titles.
	 * @param fileName
	 * @throws Exception
	 */
	public void readCSV(String fileName) throws Exception
	{
		  System.out.println("In readCSV");
		  File file = new File(fileName);
		  BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		  String line = null;
		  System.out.println("length "+ file.length());
		  String cpTitle = new String();
		  bufRdr.readLine();
		  while((line = bufRdr.readLine()) != null)
		  {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>");
			System.out.println("line  "+line);
			try {
				 StringTokenizer st = new StringTokenizer(line,",");
				 while (st.hasMoreTokens())
				 {
					 cpTitle = st.nextToken();
					 break;
				 }
				 String title = cpTitle;
				 title = title.replace("\"", "");
				 System.out.println("title"+ title);

				addSCGs(title);
			}catch (Exception e) {
				System.out.println("Exception in while ");
				 System.out.println("scg Id "+ cpTitle);
				e.printStackTrace();
			}
		  }
		  System.out.println("Completed readCSV");
			 //close the file
			 bufRdr.close();
			// writer.close();
	}

	public static void main(String arg[]){
		try {
			System.out
					.println("---------IN AddAnticipatedSCGInParticipant.Main-----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
			String csvFilePath = System.getProperty("user.dir")
					+ "/excelFiles/AntiScgForPart.csv";
			setUp();
			new AddAnticipatedSCGInParticipant().readCSV(csvFilePath);
			System.out.println("---------END AddAnticipatedSCGInParticipant.Main-----------");
		} catch (Exception e) {
			System.out.println("Exception in addAnticipatedSCGInParticipant");
			System.err.println("Exception in addAnticipatedSCGInParticipant");
			e.printStackTrace();
		}
	}

}
