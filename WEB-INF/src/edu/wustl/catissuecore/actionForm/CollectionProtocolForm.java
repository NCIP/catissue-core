/**
 * <p>Title: CollectionProtocolForm Class>
 * <p>Description:  CollectionProtocolForm Class is used to encapsulate all the request parameters passed 
 * from User Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from User Add/Edit webpage.
 * 
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends AbstractActionForm {
	/**
	 * identifier is a unique id assigned to each User.
	 */
	private long identifier;

	/**
	 * Represents the operation(Add/Edit) to be performed.
	 */
	private String operation;

	private String activityStatus;

	private long principalinvestigator;

	private long protocolcoordinators[];

	private String irbid;

	private String descriptionurl;

	private String title;

	private String shorttitle;

	private java.util.Date startDate;

	private java.util.Date endDate;

	private String participants;

	/*
	 * private String clinicalStatus; private String studycalendartitle; private
	 * String enrollment; private String pathologyStatus;
	 * 
	 * private String tissueSide; private String tissueSite; private String
	 * specimenType; private String specimenSubType;
	 */
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
     * @param key the required key.
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
	public CollectionProtocolForm() {
		reset();
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
	        }
	        catch (Exception excp)
	        {
	            excp.printStackTrace();
	            Logger.out.error(excp.getMessage());
	        }
	    }
	/**
	 * @return Returns the descriptionurl.
	 */
	public String getDescriptionurl() {
		return descriptionurl;
	}

	/**
	 * @param descriptionurl
	 *            The descriptionurl to set.
	 */
	public void setDescriptionurl(String descriptionurl) {
		this.descriptionurl = descriptionurl;
	}

	/**
	 * @return Returns the endDate.
	 */
	public java.util.Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Returns the irbid.
	 */
	public String getIrbid() {
		return irbid;
	}

	/**
	 * @param irbid
	 *            The irbid to set.
	 */
	public void setIrbid(String irbid) {
		this.irbid = irbid;
	}

	/**
	 * @return Returns the participants.
	 */
	public String getParticipants() {
		return participants;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setParticipants(String participants) {
		this.participants = participants;
	}

	/**
	 * @return Returns the principalinvestigator.
	 */
	public long getPrincipalinvestigator() {
		return principalinvestigator;
	}

	/**
	 * @param principalinvestigator
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalinvestigator(long principalinvestigator) {
		this.principalinvestigator = principalinvestigator;
	}

	

	/**
	 * @return Returns the protocolcoordinators.
	 */
	public long[] getProtocolcoordinators() {
		return protocolcoordinators;
	}
	/**
	 * @param protocolcoordinators The protocolcoordinators to set.
	 */
	public void setProtocolcoordinators(long[] protocolcoordinators) {
		this.protocolcoordinators = protocolcoordinators;
	}
	/**
	 * @return Returns the shorttitle.
	 */
	public String getShorttitle() {
		return shorttitle;
	}

	/**
	 * @param shorttitle
	 *            The shorttitle to set.
	 */
	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}

	/**
	 * @return Returns the startDate.
	 */
	public java.util.Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param operation
	 *            The operation to set.
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Returns the identifier assigned to User.
	 * 
	 * @return int representing the id assigned to User.
	 * @see #setIdentifier(int)
	 */
	public long getIdentifier() {
		return (this.identifier);
	}

	/**
	 * Sets an id for the User.
	 * 
	 * @param identifier
	 *            id to be assigned to the User.
	 * @see #getIdentifier()
	 */
	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the id assigned to form bean
	 */
	public int getFormId() {
		return Constants.COLLECTIONPROTOCOL_FORM_ID;
	}

	/**
	 * Resets the values of all the fields. This method defined in ActionForm is
	 * overridden in this class.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		reset();
	}

	/**
	 * Resets the values of all the fields. Is called by the overridden reset
	 * method defined in ActionForm.
	 */
	private void reset() {

	}

	/**
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus() {
		return activityStatus;
	}

	/**
	 * @param activityStatus
	 *            The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	/**
	 * Checks the operation to be performed is add operation.
	 * 
	 * @return Returns true if operation is equal to "add", else it returns
	 *         false
	 */
	public boolean isAddOperation() {
		return (getOperation().equals(Constants.ADD));
	}

	/**
	 * Returns the operation(Add/Edit) to be performed.
	 * 
	 * @return Returns the operation.
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Overrides the validate method of ActionForm.
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		try {
		} catch (Exception excp) {
		}
		return errors;
	}

	public void setSystemIdentifier(long l) {
		setIdentifier(l);
	}

	public long getSystemIdentifier() {
		return getIdentifier();
	}

}