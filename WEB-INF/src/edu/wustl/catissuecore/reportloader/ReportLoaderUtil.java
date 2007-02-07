package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * @author sandeep_ranade
 * Utility class containing all general purpose methods.
 */
public class ReportLoaderUtil
{
	/**
	 * @param participant
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
   		if(participantList!=null && participantList.size()>0)
		{
   			result=new HashSet();
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
	public static List getObject(String objName,String property,String val)throws Exception
	{
		List l=null;
		try
		{
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IBizLogic bizLogic = bizLogicFactory.getBizLogic(objName);
			SessionDataBean sessionDataBean = new SessionDataBean();
			sessionDataBean.setUserName(XMLPropertyHandler.getValue(Parser.SESSION_DATA));
			l = bizLogic.retrieve(objName,property,val);
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while getObject ",ex);
		}
		return l;
	}
	
	/**
	 * his method creates new directory.
	 * @param dirStr directory name  
	 * @throws IOException
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
    	Collection cprCollection=participant.getCollectionProtocolRegistrationCollection();
		
		CollectionProtocolRegistration cpr;
		Iterator cprIter=cprCollection.iterator();
		while(cprIter.hasNext())
		{
			cpr=(CollectionProtocolRegistration)cprIter.next();
			Collection tempSCGCollection=cpr.getSpecimenCollectionGroupCollection();
			Iterator scgIter=tempSCGCollection.iterator();
			while(scgIter.hasNext())
			{
				SpecimenCollectionGroup scg=(SpecimenCollectionGroup)scgIter.next();
				scgList.add(scg);
			}
		}
    	return scgList;
    } 
}
