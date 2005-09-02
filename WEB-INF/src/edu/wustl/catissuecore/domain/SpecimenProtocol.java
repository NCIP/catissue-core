/**
 * <p>Title: SpecimenProtocol Class</p>
 * <p>Description:  A set of procedures that govern the collection and/or distribution of biospecimens.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A set of procedures that govern the collection and/or distribution of biospecimens. 
 * @author mandar_deshmukh
 * @hibernate.class table="CATISSUE_SPECIMEN_PROTOCOL"
 */
public abstract class SpecimenProtocol extends AbstractDomainObject implements java.io.Serializable
{
    
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier = null;
	
	/**
	 * The current principal investigator of the protocol.
	 */
	protected User principalInvestigator = new User();
	
	/**
	 * Full title assigned to the protocol.
	 */
	protected String title;
	
	/**
	 * Abbreviated title assigned to the protocol.
	 */
	protected String shortTitle;
	
	/**
	 * IRB approval number.
	 */
	protected String irbIdentifier;
	
	/**
	 * Date on which the protocol is activated.
	 */
	protected Date startDate;
	
	/**
	 * Date on which the protocol is marked as closed.
	 */
	protected Date endDate;
	
	/**
	 * Number of anticipated cases need for the protocol.
	 */
	protected Integer enrollment;
	
	/**
	 * URL to the document that describes detailed information for the biospecimen protocol.
	 */
	protected String descriptionURL;
	
	/**
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public SpecimenProtocol()
	{
		super();
	}
	
	/**
	 * Returns the systemidentifier of the protocol.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @return Returns the systemIdentifier.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the principal investigator of the protocol.
	 * @hibernate.many-to-one column="PRINCIPAL_INVESTIGATOR_ID" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the principal investigator of the protocol.
	 * @see #setPrincipalInvestigator(User)
	 */
	public User getPrincipalInvestigator()
	{
		return principalInvestigator;
	}

	/**
	 * @param principalInvestigator The principalInvestigator to set.
	 */
	public void setPrincipalInvestigator(User principalInvestigator)
	{
		this.principalInvestigator = principalInvestigator;
	}

	/**
	 * Returns the title of the protocol.
	 * @hibernate.property name="title" type="string" column="TITLE" length="50" not-null="true" unique="true"
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the short title of the protocol.
	 * @hibernate.property name="shortTitle" type="string" column="SHORT_TITLE"
	 * length="50"
	 * @return Returns the shortTitle.
	 */
	public String getShortTitle()
	{
		return shortTitle;
	}

	/**
	 * @param shortTitle The shortTitle to set.
	 */
	public void setShortTitle(String shortTitle)
	{
		this.shortTitle = shortTitle;
	}

	/**
	 * Returns the irb systemIdentifier of the protocol.
	 * @hibernate.property name="irbIdentifier" type="string" column="IRB_IDENTIFIER" length="50"
	 * @return Returns the irbIdentifier.
	 */
	public String getIrbIdentifier()
	{
		return irbIdentifier;
	}

	/**
	 * @param irbIdentifier The irbIdentifier to set.
	 */
	public void setIrbIdentifier(String irbIdentifier)
	{
		this.irbIdentifier = irbIdentifier;
	}

	/**
	 * Returns the startdate of the protocol.
	 * @hibernate.property name="startDate" type="date" column="START_DATE" length="50"
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Returns the enddate of the protocol.
	 * @hibernate.property name="endDate" type="date" column="END_DATE" length="50"
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Returns the enrollment.
	 * @hibernate.property name="enrollment" type="int" column="ENROLLMENT" length="50"
	 * @return Returns the enrollment.
	 */
	public Integer getEnrollment()
	{
		return enrollment;
	}

	/**
	 * @param enrollment The enrollment to set.
	 */
	public void setEnrollment(Integer enrollment)
	{
		this.enrollment = enrollment;
	}

	/**
	 * Returns the descriptionURL.
	 * @hibernate.property name="descriptionURL" type="string" column="DESCRIPTION_URL" length="200"
	 * @return Returns the descriptionURL.
	 */
	public String getDescriptionURL()
	{
		return descriptionURL;
	}

	/**
	 * @param descriptionURL The descriptionURL to set.
	 */
	public void setDescriptionURL(String descriptionURL)
	{
		this.descriptionURL = descriptionURL;
	}

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
	
	public void setAllValues(AbstractActionForm abstractForm)
	{
		System.out.println("setAllValues ");
        try
        {
        	SpecimenProtocolForm spForm = (SpecimenProtocolForm) abstractForm;
        	
        	this.title = spForm.getTitle();
        	this.shortTitle = spForm.getShortTitle();
        	this.irbIdentifier = spForm.getIrbID();
        	
        	if((spForm.getStartDate()!=null) ||!(spForm.getStartDate().equals("")) )
        	{
            	this.startDate = Utility.parseDate(spForm.getStartDate(),Constants.DATE_PATTERN_MM_DD_YYYY);        		
        	}
        	
        	if((spForm.getEndDate()!=null) ||!(spForm.getEndDate().equals("")) )
        	{
            	this.endDate = Utility.parseDate(spForm.getEndDate(),Constants.DATE_PATTERN_MM_DD_YYYY);        		
        	}


        	this.enrollment = new Integer(spForm.getEnrollment());
        	this.descriptionURL = spForm.getDescriptionURL();
        	
        	principalInvestigator  = new User();
        	this.principalInvestigator.setSystemIdentifier(new Long(spForm.getPrincipalInvestigatorId()));
        }
        catch (Exception excp)
        {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
        }
	}
	
	//SpecimenRequirement#FluidSpecimenRequirement:1.specimenType", "Blood");
	protected Map fixMap(Map orgMap)
	{
		Map replaceMap = new HashMap();
		Map unitMap = new HashMap();
		unitMap.put("Cell","CellCount");
		unitMap.put("Fluid","Milliliter");
		unitMap.put("Tissue","Gram");
		unitMap.put("Molecular","Microgram");
		
		Iterator it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			Logger.out.debug("Key************************"+key);
			if(key.indexOf("specimenClass")!=-1)
			{
				String value = (String)orgMap.get(key);
				Logger.out.debug("Value..........................."+value); 
				String replaceWith = "SpecimenRequirement"+"#"+value+"SpecimenRequirement";
				
				key = key.substring(0,key.lastIndexOf("_"));
				Logger.out.debug("Second Key***********************"+key);
				String newKey = key.replaceFirst("SpecimenRequirement",replaceWith);
				Logger.out.debug("New Key................"+newKey);
				replaceMap.put(key,newKey);
			}
		}
		
		Map newMap = new HashMap();
		it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)orgMap.get(key);
			if(key.indexOf("SpecimenRequirement")==-1)
			{
				newMap.put(key,value);
			}
			else
			{
				if(key.indexOf("specimenClass")==-1)
				{
					String keyPart, newKeyPart;
					if(key.indexOf("quantityIn")!=-1)
					{
						keyPart = "quantityIn";
						
						String searchKey = key.substring(0,key.lastIndexOf("_"))+"_specimenClass";
						String specimenClass = (String)orgMap.get(searchKey);
						String unit = (String)unitMap.get(specimenClass);
						newKeyPart = keyPart + unit;
						
						key = key.replaceFirst(keyPart,newKeyPart);
					}
					//Replace # and class name and FIX for abstract class
					keyPart = key.substring(0,key.lastIndexOf("_"));
					newKeyPart = (String)replaceMap.get(keyPart);
					key = key.replaceFirst(keyPart,newKeyPart);
					newMap.put(key,value);
				}
			}
		}		
		return newMap;
	}
}