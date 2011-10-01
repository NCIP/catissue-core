/**
 * <p>Title: InstitutionForm Class>
 * <p>Description:  This Class is used to encapsulate all the request parameters passed 
 * from Institute.jsp page. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to encapsulate all the request parameters passed from Institute.jsp page.
 * @author kapil_kaveeshwar
 * */
public class InstitutionForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(InstitutionForm.class);
	/**
	 * A string containing the name of the institute.
	 */
	private String name = null;

	private long remoteId;

	private boolean dirtyEditFlag;

	private boolean remoteManagedFlag;
	
	private String remoteOperation = null;

	private String syncRemoteChanges = null;
	
	private String selectRemoteEntity = null;

	private String linkRemoteEntity = null;
	/**
	 * No argument constructor for InstitutionForm class 
	 */
	public InstitutionForm()
	{
		//        reset();
	}

	/**
	 * This function Copies the data from an institute object to a InstitutionForm object.
	 * @param abstractDomain An Institute object containing the information about the institute.  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
 {
		final Institution institute = (Institution) abstractDomain;
		this.setId(institute.getId().longValue());
		this.name = institute.getName();
		if (institute.getRemoteId() != null) {
			this.remoteId = institute.getRemoteId().longValue();
		}
		if (institute.getDirtyEditFlag() != null) {
			this.dirtyEditFlag = institute.getDirtyEditFlag().booleanValue();
		}
		if (institute.getRemoteManagedFlag() != null) {
			this.remoteManagedFlag = institute.getRemoteManagedFlag()
					.booleanValue();
		}
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public boolean isDirtyEditFlag() {
		return dirtyEditFlag;
	}

	public void setDirtyEditFlag(boolean dirtyEditFlag) {
		this.dirtyEditFlag = dirtyEditFlag;
	}


	/**
	 * Returns the login name of the institute.
	 * @return String representing the login name of the institute
	 * @see #setLoginName(String)
	 */
	public String getName()
	{
		return (this.name);
	}

	/**
	 * Sets the login name of this institute
	 * @param name login name of the institute.
	 * @see #getLoginName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return INSTITUTION_FORM_ID Returns the id assigned to form bean
	 */
	@Override
	public int getFormId()
	{
		return Constants.INSTITUTION_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	@Override
	protected void reset()
	{

	}


	public boolean isRemoteManagedFlag() {
		return remoteManagedFlag;
	}

	public void setRemoteManagedFlag(boolean remoteManagedFlag) {
		this.remoteManagedFlag = remoteManagedFlag;
	}

	public String getSyncRemoteChanges() {
		return syncRemoteChanges;
	}

	public void setSyncRemoteChanges(String syncRemoteChanges) {
		this.syncRemoteChanges = syncRemoteChanges;
	}

	public String getSelectRemoteEntity() {
		return selectRemoteEntity;
	}

	public void setSelectRemoteEntity(String selectRemoteEntity) {
		this.selectRemoteEntity = selectRemoteEntity;
	}

	public String getLinkRemoteEntity() {
		return linkRemoteEntity;
	}

	public void setLinkRemoteEntity(String linkRemoteEntity) {
		this.linkRemoteEntity = linkRemoteEntity;
	}

	public String getRemoteOperation() {
		return remoteOperation;
	}

	public void setRemoteOperation(String remoteOperation) {
		this.remoteOperation = remoteOperation;
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			if (Validator.isEmpty(this.name))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("institution.name")));
			}
		}
		catch (final Exception excp)
		{
			InstitutionForm.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
		}
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}