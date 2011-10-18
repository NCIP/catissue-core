package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

public class UtilizeSopForm extends AbstractActionForm
{
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SOPForm.class);
	/**
	 * A string containing the name of the institute.
	 */
	private String name;

	private FormFile xmlFileName;
	/**
	 * @return the xmlFileName
	 */
	public FormFile getXmlFileName()
	{
		return xmlFileName;
	}

	/**
	 * @param xmlFileName the xmlFileName to set
	 */
	public void setXmlFileName(FormFile xmlFileName)
	{
		this.xmlFileName = xmlFileName;
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
		return errors;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 */
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return Constants.SPP_ID;
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAddNewObjectIdentifier(java.lang.String, java.lang.Long)
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor,
			Long addObjectIdentifier)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.IValueObject#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}
}
