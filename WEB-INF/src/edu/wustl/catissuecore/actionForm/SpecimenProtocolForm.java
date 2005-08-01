/*
 * Created on Jul 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SpecimenProtocolForm extends AbstractActionForm
{
	/**
	 * identifier is a unique id assigned to each User.
	 */
	protected long systemIdentifier;

	/**
	 * Represents the operation(Add/Edit) to be performed.
	 */
	protected String operation;

	protected String activityStatus;

	protected long principalInvestigatorId;

	protected String irbID;

	protected String descriptionURL;

	protected String title;

	protected String shortTitle;

	protected String startDate;

	protected String endDate;

	protected int enrollment;

	/**
	 * Map to handle values of all the CollectionProtocol Events
	 */
	protected Map values = new HashMap();
	
	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(String key, Object value)
	{
		values.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public SpecimenProtocolForm()
	{
		reset();
	}

	/**
	 * @return Returns the descriptionurl.
	 */
	public String getDescriptionURL()
	{
		return descriptionURL;
	}

	/**
	 * @param descriptionurl
	 *            The descriptionurl to set.
	 */
	public void setDescriptionURL(String descriptionurl)
	{
		this.descriptionURL = descriptionurl;
	}

	/**
	 * @return Returns the endDate.
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the irbid.
	 */
	public String getIrbID()
	{
		return irbID;
	}

	/**
	 * @param irbid
	 *            The irbid to set.
	 */
	public void setIrbID(String irbid)
	{
		this.irbID = irbid;
	}

	/**
	 * @return Returns the participants.
	 */
	public int getEnrollment()
	{
		return enrollment;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setEnrollment(int participants)
	{
		this.enrollment = participants;
	}

	/**
	 * @return Returns the principalinvestigatorid.
	 */
	public long getPrincipalInvestigatorId()
	{
		return principalInvestigatorId;
	}

	/**
	 * @param principalinvestigator
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorId)
	{
		this.principalInvestigatorId = principalInvestigatorId;
	}

	/**
	 * @return Returns the shorttitle.
	 */
	public String getShortTitle()
	{
		return shortTitle;
	}

	/**
	 * @param shorttitle
	 *            The shorttitle to set.
	 */
	public void setShortTitle(String shorttitle)
	{
		this.shortTitle = shorttitle;
	}

	/**
	 * @return Returns the startDate.
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @param operation
	 *            The operation to set.
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	/**
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * @param activityStatus
	 *            The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Checks the operation to be performed is add operation.
	 * 
	 * @return Returns true if operation is equal to "add", else it returns
	 *         false
	 */
	public boolean isAddOperation()
	{
		return (getOperation().equals(Constants.ADD));
	}

	/**
	 * Returns the operation(Add/Edit) to be performed.
	 * 
	 * @return Returns the operation.
	 */
	public String getOperation()
	{
		return operation;
	}

	/**
	 * @return Returns the values.
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @return Returns the systemIdentifier.
	 */
	public long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}
	/**
	 * Copies the data from an AbstractDomain object to a CollectionProtocolForm
	 * object.
	 * 
	 * @param abstractDomain
	 *            An AbstractDomain object.
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) abstractDomain;
			this.systemIdentifier = collectionProtocol.getSystemIdentifier().longValue();
			this.activityStatus = collectionProtocol.getActivityStatus();
			this.principalInvestigatorId = collectionProtocol.getPrincipalInvestigator()
					.getSystemIdentifier().longValue();
			this.title = collectionProtocol.getTitle();
			this.shortTitle = collectionProtocol.getShortTitle();
			this.startDate = collectionProtocol.getStartDate().toString();
			this.endDate = collectionProtocol.getEndDate().toString();
			this.irbID = collectionProtocol.getIrbIdentifier();
			this.enrollment = collectionProtocol.getEnrollment().intValue();
			this.descriptionURL = collectionProtocol.getDescriptionURL();
		}
		catch (Exception excp)
		{
			excp.printStackTrace();
			Logger.out.error(excp.getMessage());
		}
	}
	
	/**
	 * Resets the values of all the fields. This method defined in ActionForm is
	 * overridden in this class.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		reset();
	}

	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 */
	protected void reset()
	{
		this.systemIdentifier = 0;
		this.activityStatus = null;
		this.principalInvestigatorId = 0;
		this.title = null;
		this.shortTitle = null;
		this.startDate = null;
		this.endDate = null;
		this.irbID = null;
		this.enrollment = 0;
		this.descriptionURL = null;
		
		values = new HashMap();
	}
}