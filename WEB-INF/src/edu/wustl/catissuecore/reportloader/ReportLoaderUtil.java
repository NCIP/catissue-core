package edu.wustl.catissuecore.reportloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.lookup.DefaultLookupResult;
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
	public static Set<Participant> checkForParticipant(Participant participant)throws Exception
	{
		Set<Participant> result=null;
		List participantList=null;
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		ParticipantBizLogic bizLogic =(ParticipantBizLogic) bizLogicFactory.getBizLogic(Participant.class.getName());
		participantList = bizLogic.getListOfMatchingParticipants(participant);
		// check for matching participant list
   		if(participantList!=null && participantList.size()>0)
		{
   			result=new HashSet<Participant>();
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
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
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
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
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
		sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
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
			sessionDataBean.setUserName(CaTIESProperties.getValue(CaTIESConstants.SESSION_DATA));
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
    public static List<SpecimenCollectionGroup> getSCGList(Participant participant)throws DAOException
    {
    	// FIRE ONLY ONE QUERY
    	
    	List<SpecimenCollectionGroup> scgList=new ArrayList<SpecimenCollectionGroup>();
    	DefaultBizLogic defaultBizLogic=new DefaultBizLogic();
    	// get all CollectionProtocolRegistration for participant
    	String sourceObjectName=CollectionProtocolRegistration.class.getName();
		String[] selectColumnName=new String[]{Constants.SYSTEM_IDENTIFIER};
		String[] whereColumnName=new String[]{Constants.COLUMN_NAME_PARTICIPANT_ID};
		String[] whereColumnValue=new String[]{participant.getId().toString()};
		String[] whereColumnCondition=new String[]{"="};
		String joinCondition="";
    	Collection cprCollection=(List)defaultBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);	
		Long cprID;
		Iterator cprIter=cprCollection.iterator();
		// iterate on all colletionProtocolRegistration for participant
		while(cprIter.hasNext())
		{
			cprID=(Long)cprIter.next();
			Collection tempSCGCollection=(Collection)defaultBizLogic.retrieveAttribute(CollectionProtocolRegistration.class.getName(), cprID, Constants.COLUMN_NAME_SCG_COLL);
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
	            if (x == CaTIESConstants.REPORT_ACCESSIONNUMBER_INDEX) 
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
	 * @return specimen collection group
	 * @throws Exception throws exception
	 */
	public static SpecimenCollectionGroup checkForSpecimenCollectionGroup(Participant participant, Site site, String surgicalPathologyNumber)throws Exception
	{
		List<SpecimenCollectionGroup> scgSet=null;
		SpecimenCollectionGroup existingSCG=null;
		Iterator<SpecimenCollectionGroup> scgIterator=null;
		try
		{
			// het list of all the scg associated with participant
			scgSet=ReportLoaderUtil.getSCGList(participant);
			if(scgSet!=null && scgSet.size()>0)
			{
				scgIterator=scgSet.iterator();
				while(scgIterator.hasNext())
				{
					// check for mathcing scg
					existingSCG=scgIterator.next();
					if((surgicalPathologyNumber.equals(existingSCG.getSurgicalPathologyNumber()) 
							|| existingSCG.getSurgicalPathologyNumber().equalsIgnoreCase(null))							
							&& (site.getName()).equals(existingSCG.getSite().getName()))
					{
						return existingSCG;
					}
				}
			}
		}
		catch(Exception ex)
		{
			Logger.out.error("Error while checking specimen collection group ",ex);
			throw ex;
		}
		return null;
	}
}
