/**
 * <p>Title: SpecimenProtocolBizLogic Class>
 * <p>Description:	SpecimenProtocolBizLogic is a class which contains the common moethods requird for 
 * Collection Protocol and Distribution Protocol.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on April 06, 2006
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * SpecimenProtocolBizLogic is a class which contains the common moethods requird for 
 * Collection Protocol and Distribution Protocol.
 * @author gautam_shetty
 */
public class SpecimenProtocolBizLogic extends DefaultBizLogic
{
	//Added by Ashish
	/*
	protected Map values = new HashMap();
	protected Map setSpecimenRequirement(String [] key, SpecimenRequirement requirement)
	{
		
	    values.put(key[0] , requirement.getSpecimenClass());
		values.put(key[1] , Utility.getUnit(requirement.getSpecimenClass() , requirement.getSpecimenType()));
		values.put(key[2] , requirement.getSpecimenType());
		values.put(key[3] , requirement.getTissueSite());
		values.put(key[4] , requirement.getPathologyStatus());
		values.put(key[5] , Utility.toString(requirement.getQuantity().getValue()));
		values.put(key[6] , Utility.toString(requirement.getId()));
		
		if(requirement.getSpecimenClass().equals(edu.wustl.catissuecore.util.global.Constants.TISSUE))
		{
			String tissueType = requirement.getSpecimenType();
			if(tissueType.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.FROZEN_TISSUE_SLIDE) || 
			        tissueType.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.FIXED_TISSUE_BLOCK) || 
			        tissueType.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.FROZEN_TISSUE_BLOCK)  || 
			        tissueType.equalsIgnoreCase(edu.wustl.catissuecore.util.global.Constants.FIXED_TISSUE_SLIDE))
			{
				values.put(key[5] , Utility.toString(new Integer(requirement.getQuantity().getValue().intValue())));
			}
		}
		
		return values;
	}
	public Object getValue(String key)
	{
		return values.get(key);
	}
	*/
	//END
    
    /**
	 * This method checks for the change in the Activity status of the object. If change is found
	 *  then it calls the setClosedDate() to update the End date.
	 * @param newObject Object representing the current data.
	 * @param oldObject Object before the changes.
	 */
	protected void checkForChangedStatus(SpecimenProtocol newObject, SpecimenProtocol oldObject)
	{
		Logger.out.debug("newObject.getActivityStatus() : " + newObject.getActivityStatus());
		Logger.out.debug("oldObject.getActivityStatus()   " + oldObject.getActivityStatus());
		
		if(!newObject.getActivityStatus().equals(oldObject.getActivityStatus()))
		{
			setClosedDate(newObject);
		}
	}
    
    /**
	 * @author mandar_deshmukh 
	 * This method is used for setting the Stop / End date for the Protocol.
	 * @param protocol The domain object whose date is to be set
	 */
	private void setClosedDate(SpecimenProtocol protocol)
	{
		String activityStatus =  protocol.getActivityStatus();
		Logger.out.debug("in setClosedDate of DBZL, ActivityStatus  : "+ activityStatus);
		if(activityStatus.equalsIgnoreCase(Constants.ACTIVITY_STATUS_CLOSED))
		{
			Date currentDate = Calendar.getInstance().getTime();
			protocol.setEndDate(currentDate);
			Logger.out.debug("EndDate set");
		}
		else if(activityStatus.equalsIgnoreCase(Constants.ACTIVITY_STATUS_ACTIVE))
		{
			protocol.setEndDate(null);
			Logger.out.debug("EndDate cleared");
		}
	}
	
	/**
	 * @param specimenProtocolIdentifier ID of the specimen protocol whose end date is to be searched.
	 * @return end date of the specimen protocol refered by the given ID. Empty string if enddate is null.
	 * @throws DAOException
	 */
	public String getEndDate(long specimenProtocolIdentifier ) throws DAOException 
	{
		String endDate="";
		
	   	String sourceObjectName = SpecimenProtocol.class.getName() ;
    	String[] selectColumnName = null;
    	String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER };
    	String[] whereColumnCondition = {"="};
    	Object[] whereColumnValue = {new Long(specimenProtocolIdentifier) };
    	String joinCondition = null ;
    	
//          Retrieve the endates 
            List protocolList = retrieve(sourceObjectName, selectColumnName, whereColumnName,
                    whereColumnCondition, whereColumnValue, joinCondition);
            
            if(protocolList != null && !protocolList.isEmpty()   )
            {
            	SpecimenProtocol tmpObject = (SpecimenProtocol ) protocolList.get(0 );
            	endDate = Utility.parseDateToString(tmpObject.getEndDate(),Constants.DATE_PATTERN_MM_DD_YYYY); 
            }
            
		return endDate ;
	}
}
