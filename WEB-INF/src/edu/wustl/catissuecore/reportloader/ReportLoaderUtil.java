package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.ehcache.CacheException;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Utility class containing all general purpose methods.
 */
public class ReportLoaderUtil
{
	/**
	 * @param participant object of Participant
	 * @return number of matching participants
	 * @throws Exception throws exception 
	 */
	public static Set checkForParticipant(Participant participant)throws Exception
	{
		Set result=null;
		List participantList=null;
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		ParticipantBizLogic bizLogic =(ParticipantBizLogic) bizLogicFactory.getBizLogic(Participant.class.getName());
		participantList = bizLogic.getListOfMatchingParticipants(participant);
		// check for matching participant list
   		if(participantList!=null && participantList.size()>0)
		{
   			result=new HashSet();
   			// prepare list of participant object out of DefaultLookupResult List
   			for(int i=0;i < participantList.size();i++)
   			{
				DefaultLookupResult participantResult=(DefaultLookupResult)participantList.get(i);
				result.add((Participant)participantResult.getObject());
			}
		}
		return result;
	}
	
	/**
	 * @param obj object
	 * @throws Exception
	 * updates the object in datastore
	 */
	public static void updateObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
		if(obj instanceof Participant)
		{
			bizLogic.update(obj,obj,Constants.HIBERNATE_DAO,sessionDataBean);
		}	
		else
		{
			bizLogic.update(obj,null,Constants.HIBERNATE_DAO,sessionDataBean);
		}	
	}
	
	/**
	 * Saves object tothe datastore
	 * @param obj object
	 * @throws Exception throws exception  
	 */
	public static void saveObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
		bizLogic.insert(obj,sessionDataBean,Constants.HIBERNATE_DAO);
	}

	/**
	 * Delete object tothe datastore
	 * @param obj object
	 * @throws Exception throws exception  
	 */
	public static void deleteObject(Object obj)throws Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IBizLogic bizLogic = bizLogicFactory.getBizLogic(obj.getClass().getName());
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
		bizLogic.delete(obj,Constants.HIBERNATE_DAO);
	}

	
	/**
	 * Gets object data grom datastore 
	 * @param objName object name
	 * @param property property of the object 
	 * @param val value for the property
	 * @return list of requested object from the datastore 
	 * @throws Exception throws exception
	 */
	public static List getObject(String objName,String property,String val) throws DAOException
	{
		List l=null;
		
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IBizLogic bizLogic = bizLogicFactory.getBizLogic(objName);
			SessionDataBean sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
			l = bizLogic.retrieve(objName,property,val);
		
		return l;
	}
	
	/**
	 * This method creates new directory.
	 * @param dirStr directory name  
	 * @throws IOException IOException occured while creating directories
	 */
	public static void createDir(String dirStr)throws IOException
	{
			File dir= new File(dirStr); 
			if(dir==null || !dir.exists())
			{	
				dir.mkdir();
			}
	}
	
	
	 /**
     * 
     * @param ssn Social Security Number to check
     * @return boolean depending on the value of ssn.
     */
    public static String getValidSSN(String ssn)
    {
    	boolean result = true;
    	String validSSN=null;
    	Pattern re=null;
    	Matcher  mat=null;
    	StringBuffer buff=null;
    	try
		{
    		re = Pattern.compile("[0-9]{3}-[0-9]{2}-[0-9]{4}", Pattern.CASE_INSENSITIVE);
    	    mat =re.matcher(ssn); 
    	    result = mat.matches();
    	    if(!result)
    	    {
    	    	re = Pattern.compile("[0-9]{9}", Pattern.CASE_INSENSITIVE);
    	    	mat =re.matcher(ssn); 
        	    result = mat.matches();
        	    if(result)
        	    {
        	    	buff=new StringBuffer();
        	    	buff.append(ssn.substring(0,3))
        	    	     .append("-")
        	    	     .append(ssn.substring(3,5))
        	    	     .append("-")
        	    	     .append(ssn.substring(5,9));
        	    	validSSN=buff.toString();
        	    }
            }
    	    else
    	    {
    	    	validSSN=ssn;
    	    }
    	}
    	catch(Exception exp)
		{
			return validSSN;
		}
    	return validSSN;
    }
    
    /**
     * This method returns the list of all the SCGs associated with the given participant
     * @param participant Participant object
     * @return scgList SpecimenCollectionGroup list
     */
    public static List getSCGList(Participant participant)
    {
    	List scgList=new ArrayList();
    	// get all CollectionProtocolRegistration for participant
    	Collection cprCollection=participant.getCollectionProtocolRegistrationCollection();
		
		CollectionProtocolRegistration cpr;
		Iterator cprIter=cprCollection.iterator();
		// iterate on all colletionProtocolRegistration for participant
		while(cprIter.hasNext())
		{
			cpr=(CollectionProtocolRegistration)cprIter.next();
			Collection tempSCGCollection=cpr.getSpecimenCollectionGroupCollection();
			Iterator scgIter=tempSCGCollection.iterator();
			// add all the scg associated with cpr to scgList
			while(scgIter.hasNext())
			{
				SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgIter.next();
				scgList.add(scg);
			}
		}
    	return scgList;
    } 
    /**
	 * @param reportText
	 * @return
	 */
	public static String getLineFromReport(String reportText,String lineToExtract)
	{
		String[] lines = reportText.split("\n");		
		for(int i = 0; i < lines.length ; i++)
		{
			String line = lines[i];
			if(line.startsWith(lineToExtract))
			{				
				return line;
			}
		}	
		return "";
	}
	
	  /**
	 * @param obrLine report information text
	 * @return String for Surgical Pathology Number
	 * @throws Exception while parsing the report text information
	 */
	public static String getSurgicalPathologyNumber(String obrLine)
	{
		try
		{
	        String newObrLine = obrLine.replace('|', '~');
	        newObrLine = newObrLine.replaceAll("~", "|~~");
	        
	        StringTokenizer st = new StringTokenizer(newObrLine, "|");

	        for (int x = 0; st.hasMoreTokens(); x++)
	        {

	            String field = st.nextToken();

	            if (field.equals("~~"))
	            {
	                continue;
	            }

	            else
	            {
	                field = field.replaceAll("~~", "");
	            } 
	            //	Accession number is now called as Surgical Pathology Number
	            if (x == Parser.REPORT_ACCESSIONNUMBER_INDEX) 
	            {
	                StringTokenizer st2 = new StringTokenizer(field, "^");
	                String accNum = st2.nextToken();

	                return accNum;
	            }           
	        }
		}
		catch(Exception e)
		{
			Logger.out.error("Error while parsing the report map",e);
		}
		return null;
	}
	
	/**
	 * Method to create new SCG
	 * @return scg object of SpecimenColectionGroup 
	 * @param participant Participant object
	 * @param identifiedReport object of IdentifiedSurgicalPathologyReport
	 * @param surgicalPathologyNumber String containing surgicalPathologyNumber for scg
	 * @throws Exception generic exception
	 */
	public static SpecimenCollectionGroup createNewSpecimenCollectionGroup(Participant participant, IdentifiedSurgicalPathologyReport identifiedReport, Site site, String surgicalPathologyNumber)throws Exception
	{
		// set default values for scg
		Logger.out.info("Creating New Specimen Collection Group");
		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		scg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//SCG Particpant direct relationship is removed
//		scg.setParticipant(this.participant);
		scg.setClinicalDiagnosis(Constants.NOT_SPECIFIED);
		scg.setClinicalStatus(Constants.NOT_SPECIFIED);
		scg.setSpecimenCollectionSite(site); 
		scg.setSurgicalPathologyNumber(surgicalPathologyNumber);
		scg.setIdentifiedSurgicalPathologyReport(identifiedReport);
		identifiedReport.setSpecimenCollectionGroup(scg);
			 
		// Retrieve collection generic protocol
		String className=CollectionProtocol.class.getName();
		String colName=new String("title");
		String colValue=XMLPropertyHandler.getValue("collectionProtocolTitle");
		BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
		CollectionProtocolBizLogic cpBizLogic=(CollectionProtocolBizLogic)bizLogicFactory.getBizLogic(CollectionProtocol.class.getName());
		List cpList=cpBizLogic.retrieve(className, colName, colValue);
		CollectionProtocol collectionProtocol=(CollectionProtocol)cpList.get(0);
		
		//Autogeneration of SCG name
		// SPR_<CollectionProtocol_Title>_<Participant_ID>_<Group_ID>
		int groupId=0;
		SpecimenCollectionGroupBizLogic scgBizLogic=(SpecimenCollectionGroupBizLogic)bizLogicFactory.getBizLogic(SpecimenCollectionGroup.class.getName());
		groupId=scgBizLogic.getNextGroupNumber();
		String collProtocolTitle=collectionProtocol.getTitle();
		if(collProtocolTitle.length()>30)
		{
			collProtocolTitle=collProtocolTitle.substring(0,29);
		}
		String scgName="SPR_"+collProtocolTitle+"_"+participant.getId()+"_"+groupId; //this.identifiedReport.getAccessionNumber().toString());
		scg.setName(scgName);
		Logger.out.info("SCG name is =====>"+scgName);
		
		// retrieve collection protocol event list
		Set collProtocolEventList=(Set)collectionProtocol.getCollectionProtocolEventCollection();
		Iterator cpEventIterator=collProtocolEventList.iterator();
		
		if(!cpEventIterator.hasNext())
		{
			Logger.out.info("Associated Collection Protocol Event not found for "+ collectionProtocol.getTitle());
		}
		else
		{
			CollectionProtocolEvent collProtocolEvent=(CollectionProtocolEvent)cpEventIterator.next();
			scg.setCollectionProtocolEvent(collProtocolEvent);
			
			// check for existing CollectionProtocolRegistration, if exists then use existing
			CollectionProtocolRegistration collProtocolReg=isCPRExists(participant, collectionProtocol);
			if(collProtocolReg==null)
			{	
				// otherwise create new CollectionProtocolRegistration
				Logger.out.info("Creating New CollectionProtocolRegistration object");
				collProtocolReg=new CollectionProtocolRegistration();
				collProtocolReg.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
				collProtocolReg.setRegistrationDate(new Date());
				collProtocolReg.setParticipant(participant);	
				collProtocolReg.setCollectionProtocol(collProtocolEvent.getCollectionProtocol());
				((Set)participant.getCollectionProtocolRegistrationCollection()).add(collProtocolReg);
				try
				{
					ReportLoaderUtil.saveObject(collProtocolReg);
				}
				catch(Exception ex)
				{
					Logger.out.error("Error: Could not save object of CollectionProtocolRegistration",ex);
					throw new Exception("Could not save object of CollectionProtocolRegistration :"+ex.getMessage());
				}		
			}
//			((Set)collProtocolReg.getSpecimenCollectionGroupCollection()).add(scg);
			// set CollectionProtocolRegistration to scg
			scg.setCollectionProtocolRegistration(collProtocolReg);
		}
			
		return scg;
	}	
	
	/**
	 * Method to check for existing collectionProtocolRegistration
	 * @param participant object of Participant
	 * @param collectionProtocol object of CollectionProtocol
	 * @return object of CollectionProtocolRegistration
	 * @throws Exception generic exception
	 */
	public static CollectionProtocolRegistration isCPRExists(Participant participant, CollectionProtocol collectionProtocol) throws Exception
	{
		try
		{
			// retrive CollectionProtocolRegistration with current participant and collectionProtocol
			String sourceObjectName=new String(CollectionProtocolRegistration.class.getName());
			String[] selectColumnName=new String[]{"participant", "collectionProtocol"};
			String[] whereColumnCondition=new String[]{"=","="};
			String[] whereColumnValue=new String[]{participant.getId().toString(), collectionProtocol.getId().toString()};
			String joinCondition=Constants.AND_JOIN_CONDITION;
			
			BizLogicFactory bizLogicFactory=BizLogicFactory.getInstance();
			CollectionProtocolRegistrationBizLogic collProtRegBizLogic=(CollectionProtocolRegistrationBizLogic)bizLogicFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
			List cprList=collProtRegBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnCondition, whereColumnValue,joinCondition);
			
			// check for existence
			if(cprList!=null && cprList.size()>0)
			{
				// cpr exist then return existing cpr
				Logger.out.info("Existing CPR found for participant id="+participant.getId()+" collectionProtocol id="+collectionProtocol.getId());
				return (CollectionProtocolRegistration)cprList.get(0);
			}
		}
		catch(DAOException ex)
		{
			Logger.out.error("DAOException occured in isCPRExists method");
			Logger.out.debug(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * Saves participant object and add it to cache
	 * @param participant Object of Participant to save
	 * @throws Exception generic exception
	 */
	public static void saveParticipant(Participant participant)throws Exception
	{
		Logger.out.info("Inside saveParticipant");
		try
		{
			ReportLoaderUtil.saveObject(participant);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while saving new Participant", ex);
		}
		
		
		// code to add participant to the cache
		Map participantMap = null;
		ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Participant.class.getName());
		Map cpMap = null;
		BizLogicFactory bizFactory=BizLogicFactory.getInstance();
		CollectionProtocolRegistrationBizLogic cpbizlogic= (CollectionProtocolRegistrationBizLogic) bizFactory.getBizLogic(CollectionProtocolRegistration.class.getName());
		List cprList=null;
		try
		{
			cprList=cpbizlogic.getAllParticipantRegistrationInfo();
			participantMap = bizlogic.getAllParticipants();
			Iterator it = participantMap.keySet().iterator();
			while (it.hasNext())
			{
				Long str = (Long) it.next();
			}
		}
		catch (Exception ex)
		{
			Logger.out.error("Exception occured getting List of Participants " ,ex);
		}
		// getting instance of catissueCoreCacheManager and adding participantMap to cache
		try
		{
			Logger.out.info("Adding participant object to cache");
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager.getInstance();
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.MAP_OF_PARTICIPANTS, (HashMap) participantMap);
			catissueCoreCacheManager.addObjectToCache(edu.wustl.catissuecore.util.global.Constants.LIST_OF_REGISTRATION_INFO, (Vector)cprList);
			Logger.out.info("Added participant to cache");
		}
		catch (CacheException e)
		{
			Logger.out
					.error("Exception occured while creating instance of CatissueCoreCacheManager" , e);
		}	
	}
}
