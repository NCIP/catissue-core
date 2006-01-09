/**
 * <p>Title: CaTissueHTTPClient Class>
 * <p>Description:	This is the wrapper class over HTTP API that provides a 
 * functionality to APIs to connect& access the caTISSUE Core Application.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 15, 2005
 */

package edu.wustl.catissuecore.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import gov.nih.nci.system.applicationservice.ApplicationService;

public class CaTissueHTTPClient
{
    private static CaTissueHTTPClient caTissueHTTPClient = new CaTissueHTTPClient();
    private static final String propertiesFile = "httpclient.properties";
    
    private String httpSessionId;
    private CaTissueHTTPClient()
    {
        
    }
    public static CaTissueHTTPClient getInstance()
    {
        return caTissueHTTPClient;
    }
    
	public boolean connect(String userName, String password) throws Exception
	{
	    User user = new User();
		user.setLoginName(userName);
		user.setPassword(password);
		
		HTTPWrapperObject wrapperObject = new HTTPWrapperObject(user,Constants.LOGIN);
	    
	    //FIXME : Needs to be defined in Constants.java or ApplicationResources.properties
//		String servletURL = "http://localhost:8080/catissuecore/LoginHTTP.do";
		
		String servletURL =readHttpClientProperty("loginservleturl");
		
//		System.out.println("#############Login Servlet URL from properties file--->"+servletURL);
		
		HTTPMessage httpMessage=(HTTPMessage)sendData(wrapperObject, "http://localhost:8080/catissuecore/LoginHTTP.do");
		
		if(Constants.SUCCESS.equals(httpMessage.getResponseStatus()))
		{
			httpSessionId = httpMessage.getSessionId();
			return true;
		}
		
		return false;
	}
	
	private Object doOperation(Object domainObject,String operation) throws Exception
	{
	    HTTPWrapperObject wrapperObject = new HTTPWrapperObject(domainObject,operation);
	    
	    
	    //FIXME : Needs to be defined in Constants.java or ApplicationResources.properties
	    String servletURL = "http://localhost:8080/catissuecore/OperationHTTP.do;jsessionid=" + httpSessionId; 
	    
//	    System.out.println("#############Login Servlet URL from properties file--->"+servletURL);
	    
	    return sendData(wrapperObject, servletURL);
	}
	
	private Object sendData(HTTPWrapperObject wrapperObject, String urlString) throws Exception
	{
	    // Opens a connection to the specific URL
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		
		con.setDoOutput(true);
		con.setRequestProperty(Constants.CONTENT_TYPE,Constants.HTTP_API);
		ObjectOutputStream objectOutStream = new ObjectOutputStream(con.getOutputStream());
		
				
		objectOutStream.writeObject(wrapperObject);
		objectOutStream.flush();
		objectOutStream.close();
		
		ObjectInputStream objectInStream = new ObjectInputStream(con.getInputStream());
		HTTPMessage httpMessage = (HTTPMessage)objectInStream.readObject();
		
		objectInStream.close();
		
		List messageList = httpMessage.getMessageList();
		
		for(int i=0;i<messageList.size();i++)
		{
			System.out.println("\n-->" + messageList.get(i));
		}
		
		return httpMessage;
	}
	
	public Object add(Object domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.ADD);
	}
	
	public Object edit(Object domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.EDIT);
	}
	
	private String readHttpClientProperty(String property)
	{
	    Properties properties = new Properties();
	    
		CaTissueHTTPClient client = new CaTissueHTTPClient();
	    	
		try 
		{
		    java.io.InputStream is = client.getClass().getClassLoader().getResourceAsStream(propertiesFile);
		}
		catch (Exception e) 
		{
		    e.printStackTrace();
	    }
		
		return properties.getProperty(property);
	}
	
	public static void main(String [] args)
	{
	    /*Variables.catissueHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		PropertyConfigurator.configure(Variables.catissueHome+"\\ApplicationResources.properties");
		Logger.out.debug("here"); 
		
		CaTissueHTTPClient client = new CaTissueHTTPClient();
		
		//List resultList=new ArrayList();
		Object resultObj=new Object();
		
		Department dept=new DepartmentImpl();
		Institution institution=new InstitutionImpl();
		CancerResearchGroup crg=new CancerResearchGroupImpl();
		StorageType stgType=new StorageTypeImpl();
		Site site=new SiteImpl();
		StorageContainer sc=new StorageContainerImpl();
		Biohazard bhd=new BiohazardImpl();
		CollectionProtocol cp=new CollectionProtocolImpl();
		DistributionProtocol dp=new DistributionProtocolImpl();
		Participant participant=new ParticipantImpl();
		SpecimenCollectionGroup scg=new SpecimenCollectionGroupImpl();
		CollectionProtocolRegistration cpr=new CollectionProtocolRegistrationImpl();
		Specimen specimen=new SpecimenImpl();
		Distribution dist=new DistributionImpl();
		
		try
		{
		    //The below method call is required when the Client is behind a firewall. This method reads the firewall related properties from
			//the network.properties. The client needs to be configured for the firewall settings. If the client is not behind a firewall
			//the below method class is not required at all.
			Connection.proxy();
			
		    ApplicationService appService = ApplicationService.getRemoteInstance("http://localhost:8000/caTISSUE_CORE_API/server/HTTPServer");
		
		    System.out.println("****************** LOGIN STATUS : " + client.connect("admin@admin.com","login123"));
		
		    
		    //Department				tested
//		    dept.setId(new Long(1));
//	    	List resultList = appService.search("edu.wustl.catissuecore.domain.Department", dept);
//		    dept=(Department)resultList.get(0);
//		    client.add(dept);
		    
		    //Institute					tested
//		    institution.setId(new Long(1));
//	    	List resultListInstitution = appService.search("edu.wustl.catissuecore.domain.Institution", institution);
//	    	institution=(Institution)resultListInstitution.get(0);
//	    	client.add(institution);

		    //CancerResearchGroup		tested
//	    	crg.setId(new Long(1));
//	       	List resultListCrg = appService.search("edu.wustl.catissuecore.domain.CancerResearchGroup", crg);
//	    	crg=(CancerResearchGroup)resultListCrg.get(0);
//		    client.add(crg);
		    
	    	//StorageType		tested
//	    	stgType.setId(new Long(1));
//	       	resultObj=searchObj("StorageType",stgType);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((StorageType)resultObj);
//		    }
		    
	    	//Site			tested
//	    	site.setId(new Long(1));
//	    	resultObj=searchObj("Site",site);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((Site)resultObj);
//		    }
		    
	    	
	    	//Storage Container		NullPointerException
//	    	sc.setId(new Long(2));
//	    	resultObj=searchObj("StorageContainer",sc);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        StorageContainer cont = (StorageContainer)resultObj;
//		        System.out.println("PARENT: "+cont.getParentContainer());
//		        System.out.println("SITE:"+cont.getSite());
//		        System.out.println("STORAGE TYPE:"+cont.getStorageType());
//		        System.out.println("Dimension One:"+cont.getParentContainer().getPositionDimensionOne());
//		        cont.setParentContainer(null);
//		        System.out.println("PARENT: "+cont.getParentContainer());
//		        cont.setId(new Long(0));
//		        client.add(cont);
//		    }

//		    StorageContainer cont=new StorageContainerImpl();
//	        cont.setId(new Long(0));
//	        
//	        StorageType sType=new StorageTypeImpl();
//	        sType.setId(new Long(1));
//	        cont.setStorageType(sType);
//	        
////	        Site scSite=new SiteImpl();
////	        scSite.setId(new Long(1));
//	        cont.setSite(null);
//	        
//	        StorageContainer parent=new StorageContainerImpl();
//	        parent.setId(new Long(1));
//	        cont.setParentContainer(parent);
//	        cont.setPositionDimensionOne(new Integer(1));
//	        cont.setPositionDimensionTwo(new Integer(2));
//
//	        cont.setBarcode(new String("Barcode"));
//	        cont.setNumber(new Integer(1));
//	        
//	        StorageContainerCapacity scCapacity=new StorageContainerCapacityImpl();
//	        scCapacity.setId(new Long(1));
//	        scCapacity.setOneDimensionCapacity(new Integer(5));
//	        scCapacity.setTwoDimensionCapacity(new Integer(5));
//	        cont.setStorageContainerCapacity(scCapacity);
//	        
//	        
//	        StorageContainerDetails scDetail=new StorageContainerDetailsImpl();
//	        scDetail.setId(new Long(0));
//	        scDetail.setParameterName("ParamName");
//	        scDetail.setParameterValue("ParamValue");
//	        scDetail.setStorageContainer(cont);
//	        
//	        Collection scDetails=new ArrayList();
//	        scDetails.add(scDetail);
//	        cont.setStorageContainerDetailsCollection(scDetails);
//	        
//	        cont.setTempratureInCentigrade(new Double(10));
//	        cont.setIsFull(new Boolean(false));
//	        
//	        client.add(cont);
	        
	        
		    //Biohazard		tested
//	    	bhd.setId(new Long(1));
//	    	resultObj=searchObj("Biohazard",bhd);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((Biohazard)resultObj);
//		    }
	    	
		    //Collection Protocol	tested
//		    cp.setId(new Long(4));
//		    resultObj=searchObj("CollectionProtocol",cp);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        CollectionProtocol cptest=(CollectionProtocol)resultObj;
//		        Collection eventList=new ArrayList(); 
//		        eventList=cptest.getCollectionProtocolEventCollection();
//		        Iterator itr=eventList.iterator();
//		        CollectionProtocolEvent cpevent=(CollectionProtocolEvent)itr.next();
//		        System.out.println("Event clinical status-->"+cpevent.getClinicalStatus());
//		        client.add((CollectionProtocol)resultObj);
//		    }
	    
		    // Distribution Protocol		tested	
		    dp.setId(new Long(2));
		    resultObj=searchObj("DistributionProtocol",dp);
		    if(resultObj instanceof String)
		    {
		        System.out.println((String)resultObj);
		    }
		    else
		    {
		        DistributionProtocol distpro=(DistributionProtocol)resultObj;
		        distpro.setId(new Long(0));
		        System.out.println("########################Adding Distribution Protocol###################");
		        System.out.println(distpro.getTitle());
		        client.add(distpro);
		    }
		    
		    //Participant			tested
//		    participant.setId(new Long(1));
//		    resultObj=searchObj("Participant",participant);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        Participant part=(Participant)resultObj;
//		        part.setId(new Long(0));
//		        client.add(part);
//		    }
		    
//		    Participant testParticipant=new ParticipantImpl();
//		    testParticipant.setId(new Long(0));
//		    testParticipant.setFirstName("ParticipantName");
//		    testParticipant.setLastName("LastName");
//		    testParticipant.setParticipantMedicalIdentifierCollection(null);
//		    testParticipant.setActivityStatus("Active");
//		    client.add(testParticipant);
		    		    
	    	//Collection Protocol Registration	tested
//		    cpr.setId(new Long(1));
//		    resultObj=searchObj("CollectionProtocolRegistration",cpr);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((CollectionProtocolRegistration)resultObj);
//		    }
		    
		    //SpecimenCollectionGroup		
//	    	scg.setId(new Long(1));
//	    	resultObj=searchObj("SpecimenCollectionGroup",scg);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {	System.out.println("Report ID:-"+((SpecimenCollectionGroup)resultObj).getClinicalReport().getId()); 
//		    	ClinicalReport crp=((SpecimenCollectionGroup)resultObj).getClinicalReport();
//		    	crp.setParticipantMedicalIdentifier(null);
//		    	SpecimenCollectionGroup sg=(SpecimenCollectionGroup)resultObj;
//		    	sg.setClinicalReport(crp);
//		    	System.out.println("SPNo.-->"+crp.getSurgicalPathologyNumber());
//		    	client.add(sg);
//		    }
		    
		    //Specimen
//		    specimen.setId(new Long(1));
//	    	resultObj=searchObj("Specimen",specimen);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((Specimen)resultObj);
//		    }
		    
		    //Distribution
//		    dist.setId(new Long(3));
//	    	resultObj=searchObj("Distribution",dist);
//		    if(resultObj instanceof String)
//		    {
//		        System.out.println((String)resultObj);
//		    }
//		    else
//		    {
//		        client.add((Distribution)resultObj);
//		    }
		    
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		*/
	}
	
	private static Object searchObj(String className, Object obj)
	{
	    List resultList=new ArrayList();
	    
	    Object resultObj=new Object();
	    
	    String packageHierarchy="edu.wustl.catissuecore.domain."; 
	    
	    ApplicationService appService = ApplicationService.getRemoteInstance("http://localhost:8000/caTISSUE_CORE_API/server/HTTPServer");
	    
	    try
	    {
	        resultList=appService.search(packageHierarchy+className, obj);
	        if(resultList.size()>0)
	        {
	            resultObj=resultList.get(0);
	            return resultObj;
	        }
	        else
	        {
	            resultObj=new String("No "+className+" matched...");
	        }
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        System.out.println("in serachObj");
	    }
	    return resultObj;
	}
	
}