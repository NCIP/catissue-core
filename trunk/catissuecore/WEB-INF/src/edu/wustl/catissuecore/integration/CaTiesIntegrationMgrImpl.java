/**
 * <p>Title: CaTiesIntegratorImpl Class>
 * <p>Description:	This Class is used to implement integration of caTissue Core with caTies</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on May 19, 2006
 */

package edu.wustl.catissuecore.integration;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;


/**
 * This Class is used to implement integration of caTissue Core with caTies.
 * @author Krunal Thakkar
 */
public class CaTiesIntegrationMgrImpl extends IntegrationManager
{
    private ApplicationService appService = null ;

    private static final String publicServerName = XMLPropertyHandler.getValue(Constants.CATIES_PUBLIC_SERVER_NAME);
    
    private static final String privateServerName = XMLPropertyHandler.getValue(Constants.CATIES_PRIVATE_SERVER_NAME);
    
    public List getLinkedAppData(Object linkedObj, Long id)
    {
        Logger.out.debug("Class of object received in getLinkedAppData of CaTiesIntegrationMgrImpl===>"+ linkedObj.getClass());
        
/*        if(linkedObj instanceof SpecimenCollectionGroup || linkedObj instanceof Specimen)
        {
            Logger.out.debug("Linked object is SpecimenCollectionGroup or Specimen");
            return findIdentifiedPathologyReport(id);
        }
        else if(linkedObj instanceof Participant)
        {
            Logger.out.debug("Linked object is Participant");
            return findReportsForPatient(id);
        }
*/        
        return new ArrayList();
    }
    
    public List getMatchingObjects(Object objToMatch)
    {
        return new ArrayList();
    }
    
 /*   private void findPathologyReportByUId(String UId)
    {
    	try 
    	{
    		this.appService = ApplicationService.getRemoteInstance(publicServerName);
    		
    	    PathologyReport pathologyReport = new PathologyReportImpl();
    	    pathologyReport.setUuid(UId);
    	    
    	    List resultList = appService.search(PathologyReport.class, pathologyReport);
    	
    	    if (resultList == null) 
    	    {
    	    	Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1) 
    		{
    		    Logger.out.debug("Empty result set returned from search of deIdentified reports.");
    		}
    		else 
    		{
    		    for (Iterator iterator = resultList.iterator(); iterator.hasNext(); ) 
    		    {
    			    pathologyReport = (PathologyReport) iterator.next();
    			    System.out.println(stringifyPathologyReport(pathologyReport));
    			}
    		}
    	}
    	catch (Exception x) 
    	{
    	    Logger.out.error(x.getMessage());
    	}
    }
    
    private List findPathologyReportByConceptCode(String conceptCode)
    {
        List pathologyReportList= new ArrayList();
    	try
    	{
    		this.appService = ApplicationService.getRemoteInstance(publicServerName);
    		
    		Logger.out.debug("PublicServerName==>"+publicServerName);
    		
    	    PathologyReport pathologyReport = new PathologyReportImpl();
//    	    pathologyReport.setUuid(UId);
    	    pathologyReport.setConceptCodeSet(new String("*"+conceptCode+"*"));
    	    
    	    List resultList = appService.search(PathologyReport.class, pathologyReport);
    	    if (resultList == null)
    	    {
    	        Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1)
    		{
    		    Logger.out.debug("Empty result set returned from search of deIdentified reports.");
    		}
    		else
    		{
    		    for (Iterator iterator = resultList.iterator(); iterator.hasNext(); )
    		    {
    			    pathologyReport = (PathologyReport) iterator.next();
    			    
    			    pathologyReportList.add(stringifyPathologyReport(pathologyReport));
    			    
//    			    System.out.println(stringifyPathologyReport(pathologyReport));
    			}
    		}
    	    
    	    return pathologyReportList;
    	    
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	}
    	
    	pathologyReportList.add("caTies application is not linked currently");
    	
    	return pathologyReportList;
    }
    
    private List findIdentifiedPathologyReportByAccessionNum(String accNum)
    {
        List pathologyReportList= new ArrayList();
        
    	try
    	{
    		this.appService = ApplicationService.getRemoteInstance(privateServerName);
    	    IdentifiedPathologyReport pathologyReport = new IdentifiedPathologyReportImpl();
//    	    pathologyReport.setAccessionNumber(accNum);
    	    pathologyReport.setId(new Long(32833537));
    	   
    	    List resultList = appService.search(IdentifiedPathologyReport.class, pathologyReport);
    	    if (resultList == null)
    	    {
    	    	Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1)
    		{
    		    Logger.out.debug("Empty result set returned from search of Identified reports.");
    		}
    		else
    		{
    		    for (Iterator iterator = resultList.iterator() ; iterator.hasNext() ;)
    		    {
    		    	this.appService = ApplicationService.getRemoteInstance(privateServerName);
    		    	
    			    pathologyReport = (IdentifiedPathologyReport) iterator.next();
    			    
    			    pathologyReportList.add(stringifyIdentifiedPathologyReport(pathologyReport));
    			    
    			    System.out.println(stringifyIdentifiedPathologyReport(pathologyReport));
    			    
    			    findPathologyReportByUId(pathologyReport.getDeidentifiedId());
    			}
    		}
    	    
    	    return pathologyReportList;
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	}
    	return pathologyReportList;
    }
    
    private List findIdentifiedPathologyReport(Long id)
    {
        List pathologyReportList= new ArrayList();
        
    	try
    	{
    		this.appService = ApplicationService.getRemoteInstance(privateServerName);
    	    IdentifiedPathologyReport pathologyReport = new IdentifiedPathologyReportImpl();
//    	    pathologyReport.setId(new Long(32833537));
    	    pathologyReport.setId(id);
    	   
    	    List resultList = appService.search(IdentifiedPathologyReport.class, pathologyReport);
    	    if (resultList == null)
    	    {
    	    	Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1)
    		{
    		    Logger.out.debug("Empty result set returned from search of Identified reports.");
    		}
    		else
    		{
    		    for (Iterator iterator = resultList.iterator() ; iterator.hasNext() ;)
    		    {
    		    	this.appService = ApplicationService.getRemoteInstance(privateServerName);
    		    	
    			    pathologyReport = (IdentifiedPathologyReport) iterator.next();
    			    
    			    pathologyReportList.add(stringifyIdentifiedPathologyReport(pathologyReport));
    			    
//    			    System.out.println(stringifyIdentifiedPathologyReport(pathologyReport));
    			    
    			    findPathologyReportByUId(pathologyReport.getDeidentifiedId());
    			}
    		    
    		    return pathologyReportList;
    		}
    	    
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	    
    	    pathologyReportList.add(new String("Exception in fetching report"));
    	    
    	    return pathologyReportList;
    	}
    	
    	pathologyReportList.add(new String("No Reports available"));
    	
    	return pathologyReportList;
    }
    
    private List findPatientByIdentifiedData(String firstName,String lastName, Date birthDate, String socialSecurityNumber)
    {
    	List resultList = null;
    	
    	try 
    	{
    		this.appService = ApplicationService.getRemoteInstance(privateServerName);
    		
    	    IdentifiedPatient patient = new IdentifiedPatientImpl();
    	    
    	    patient.setFirstName(firstName);
    	    patient.setLastName(lastName);
    	    patient.setBirthDate(birthDate);
    	    patient.setSocialSecurityNumber(socialSecurityNumber);
    	    
    	    resultList = appService.search(IdentifiedPatient.class, patient);
    	    
    	    if (resultList == null)
    	    {
    	    	Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1)
    		{
    		    Logger.out.debug("Empty result set returned from search of Identified reports.");
    		}
    		else 
    		{
    		    for (Iterator iterator = resultList.iterator(); iterator.hasNext(); )
    		    {
    		    	this.appService = ApplicationService.getRemoteInstance(privateServerName);
    		    	
    		    	patient = (IdentifiedPatient) iterator.next();
    		    	System.out.println("\nPatient Info:\n");
    			    System.out.println(stringifyIdentifiedPatient(patient));
    			}
    		}
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	}
    	
    	return resultList;
    }
    
    private List findReportsForPatient(Long patientID)
    {
        List resultList = null;
        
        List patientReportsList = new ArrayList();
        
        try
        {
    		this.appService = ApplicationService.getRemoteInstance(privateServerName);
    		
    		Logger.out.debug("Private Server Name===>"+privateServerName);
    		
    	    IdentifiedPatient patient = new IdentifiedPatientImpl();
    	    patient.setId(patientID);
    	    
    	    IdentifiedPathologyReport pathologyReport = new IdentifiedPathologyReportImpl();
    	    pathologyReport.setPatient(patient);
    	   
    	    resultList = appService.search(IdentifiedPathologyReport.class, pathologyReport);

    	    if (resultList == null)
    	    {
    	    	Logger.out.debug("Returns null from search.");
    	    }
    		else if (resultList.size() < 1)
    		{
    		    Logger.out.debug("Empty result set returned from search of Identified reports.");
    		}
    		else
    		{
    		    for (Iterator iterator = resultList.iterator() ; iterator.hasNext() ;)
    		    {
    		    	this.appService = ApplicationService.getRemoteInstance(privateServerName) ;
    		    	
    			    pathologyReport = (IdentifiedPathologyReport) iterator.next() ;
    			    System.out.println(pathologyReport.getId()+"\n") ;
    			    findPathologyReportByUId(pathologyReport.getDeidentifiedId());
    			    
    			    patientReportsList.add(new NameValueBean(pathologyReport.getId().toString(), stringifyIdentifiedPathologyReport(pathologyReport)));
    			}
    		    
    		    return patientReportsList;
    		}
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	    
    	    patientReportsList.add(new NameValueBean("Exception in fetching report", "Exception in fetching report"));
    	    
    	    return patientReportsList;
    	}
    	
    	patientReportsList.add(new NameValueBean("No Reports available", "No Reports available"));
    	
    	return patientReportsList;
    }
    
    

    private String stringifyPatient(Patient patient)
    {
        String result = "" ;
        try
        {
		    result += String.valueOf(patient.getUuid()) + ", " ;
	//	    result += String.valueOf(patient.getDeidentifiedId()) + ", " ;
		    result += String.valueOf(patient.getBirthDate())  + ", " ;
		    result += String.valueOf(patient.getEthnicity())  + ", " ;
		    result += String.valueOf(patient.getGender())  + ", " ;
		    result += String.valueOf(patient.getRace())  + ", " ;
        }
        catch (Exception x)
        {
            Logger.out.error(x.getMessage());
        }
        
        return result;
    }
    
    private String stringifyIdentifiedPatient(IdentifiedPatient patient)
    {
    	String result = "" ;
    	try
    	{
    	    result += String.valueOf(patient.getId()) + ", " ;
    	    result += String.valueOf(patient.getFirstName()) + ", " ;
    	    result += String.valueOf(patient.getLastName()) + ", " ;
    	    result += String.valueOf(patient.getBirthDate())  + ", " ;
    	    result += String.valueOf(patient.getSocialSecurityNumber()) + ", " ;
    	    result += String.valueOf(patient.getEthnicity())  + ", " ;
    	    result += String.valueOf(patient.getGender())  + ", " ;
    	    result += String.valueOf(patient.getRace())  + ", " ;
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	}
    	
    	return result;
    }

    private String stringifyPathologyReport(PathologyReport pathologyReport)
    {
        String result = "" ;
		try
		{
			result += pathologyReport.getId()+" \n";
		    result += String.valueOf(pathologyReport.getUuid()) + ", " ;
		    Patient patient = pathologyReport.getPatient() ;
		    result += stringifyPatient(patient) ;
	        result += "\n";
	        result += pathologyReport.getDocumentText();
		}
		catch (Exception x)
		{
		    Logger.out.error(x.getMessage());
		}
		return result;
    }
    
    private String stringifyIdentifiedPathologyReport(IdentifiedPathologyReport pathologyReport)
    {
    	String result = "";
    	try
    	{
    	    result += String.valueOf(pathologyReport.getUuid()) + ", " ;
    	    result += " PatientInfo:";
    	    IdentifiedPatient patient = pathologyReport.getPatient() ;
    	    result += stringifyIdentifiedPatient(patient) ;
            result += " ";
            result += pathologyReport.getDocumentText();
            
            Logger.out.debug("Report in caTiesIntegrationImpl with new lines==>"+result);
            
//            result.replaceAll("\\n","\\\\\n");
//            result=result.replaceAll("\n","\\\\n");
            
            Logger.out.debug("Report in caTiesIntegrationImpl ==>"+result);
    	}
    	catch (Exception x)
    	{
    	    Logger.out.error(x.getMessage());
    	}
    	return result;
    }

    private String stringifyQuery(Query q)
    {
        String result = "" ;
        try
        {
            result += String.valueOf(q.getUuid()) + ", " ;
            result += String.valueOf(q.getName()) + ", " ;
            result += String.valueOf(q.getDescription());
        }
        catch (Exception x)
        {
            Logger.out.error(x.getMessage());
        }
        return result ;
    }
    
    private String stringifyPatientPathologyReports(Patient patient)
    {
		String result = "";
		try
		{
		    Collection reportList = patient.getPathologyReportCollection() ;
//		    if (!org.hibernate.Hibernate.isInitialized(reportList))
//		    {
//		        org.hibernate.Hibernate.initialize(reportList) ;
//		    }
		    
		    if (reportList.size() >= 1)
		    {
		        for (Iterator iterator = reportList.iterator() ; iterator.hasNext() ;)
		        {
		            PathologyReport pathologyReport = (PathologyReport) iterator.next() ;
		            result += "\t" + String.valueOf(pathologyReport.getUuid()) ;
		        }
		    }
		}
		catch (Exception x)
		{
		    result = "" ;
		    
		    Logger.out.error(x.getMessage());
		}
		
		return result ;
    }
    */
}
