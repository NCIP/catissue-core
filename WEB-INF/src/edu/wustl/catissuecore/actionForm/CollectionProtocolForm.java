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

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
//import edu.wustl.catissuecore.domain.User;
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
	private long systemIdentifier;

	/**
	 * Represents the operation(Add/Edit) to be performed.
	 */
	private String operation ;

	private String activityStatus;

	private long principalInvestigatorId;

	private long protocolCoordinatorIds[];

	private String irbid;

	private String descriptionurl;

	private String title;

	private String shorttitle;

//	private java.util.Date startDate;
	private String startDate;
	
//	private java.util.Date endDate;
	
	private String endDate;
	
	private String enrollment;

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
	 * 
	 * @param key
	 *            the key to which the object is mapped.
	 * @param value
	 *            the object which is mapped.
	 */
	public void setValue(String key, Object value) {
		values.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * 
	 * @param key
	 *            the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key) {
		return values.get(key);
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues() {
		return values.values();
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public void setValues(Map values) {
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
	public void setAllValues(AbstractDomainObject abstractDomain) {
		try {
			CollectionProtocol collectionProtocol = (CollectionProtocol) abstractDomain;
			this.systemIdentifier =  collectionProtocol.getSystemIdentifier().longValue() ;
			this.activityStatus = collectionProtocol.getActivityStatus();
			this.principalInvestigatorId = collectionProtocol.getPrincipalInvestigator().getSystemIdentifier().longValue() ;
			this.title = collectionProtocol.getTitle();
			this.shorttitle = collectionProtocol.getShortTitle() ;
			this.startDate  = collectionProtocol.getStartDate().toString();
			this.endDate = collectionProtocol.getEndDate().toString();
			
			
			
		} catch (Exception excp) {
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
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(String endDate) {
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
	public String getEnrollment() {
		return enrollment;
	}

	/**
	 * @param participants
	 *            The participants to set.
	 */
	public void setEnrollment(String participants) {
		this.enrollment = participants;
	}

	/**
	 * @return Returns the principalinvestigatorid.
	 */
	public long getPrincipalInvestigatorId() {
		return principalInvestigatorId;
	}

	/**
	 * @param principalinvestigator
	 *            The principalinvestigator to set.
	 */
	public void setPrincipalInvestigatorId(long principalInvestigatorId) {
		this.principalInvestigatorId = principalInvestigatorId;
	}

	/**
	 * @return Returns the protocolcoordinator ids.
	 */
	public long[] getProtocolCoordinatorIds() {
		return protocolCoordinatorIds;
	}

	/**
	 * @param protocolcoordinators
	 *            The protocolcoordinators to set.
	 */
	public void setProtocolcoordinators(long[] protocolCoordinatorIds) {
		this.protocolCoordinatorIds = protocolCoordinatorIds;
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
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(String startDate) {
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
		ActionErrors errors = null;
		Validator validator = new Validator();
		try 
		{
			System.out.println("\n------------------------");
			
			
			System.out.println(this.getPrincipalInvestigatorId());
			if (this.getProtocolCoordinatorIds() != null)
			{
				System.out.println(this.getProtocolCoordinatorIds());
			}
			System.out.println(this.getTitle());
			System.out.println(this.getStartDate());
			java.util.Iterator i = this.values.keySet().iterator() ;
			
			while(i.hasNext() )
			{
				Object o = i.next();
				System.out.print(o);
				System.out.println(" : "+this.getValue(o.toString() )); 
			}
			System.out.println("\n===============\n");
		}
		catch (Exception excp)
		{
			errors = new ActionErrors();
		}
		return errors;
	}

	/**
	 * @return Returns the values.
	 */
	public Map getValues() {
		return values;
	}
	/**
	 * @param protocolCoordinatorIds The protocolCoordinatorIds to set.
	 */
	public void setProtocolCoordinatorIds(long[] protocolCoordinatorIds) {
		this.protocolCoordinatorIds = protocolCoordinatorIds;
	}
	
	
	
	
	/**
	 * @return Returns the systemIdentifier.
	 */
	public long getSystemIdentifier() {
		return systemIdentifier;
	}
	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(long systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}
}