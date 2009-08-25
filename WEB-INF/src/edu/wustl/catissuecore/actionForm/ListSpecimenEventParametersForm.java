
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 *
 */
public class ListSpecimenEventParametersForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(ListSpecimenEventParametersForm.class);
	private String specimenId;

	private String specimenEventParameter;

	/**
	 * @return Returns the specimenEventParameter.
	 */
	public String getSpecimenEventParameter()
	{
		return this.specimenEventParameter;
	}

	/**
	 * @param specimenEventParameter The specimenEventParameter to set.
	 */
	public void setSpecimenEventParameter(String specimenEventParameter)
	{
		this.specimenEventParameter = specimenEventParameter;
	}

	/**
	 * @return Returns the specimenId.
	 */
	public String getSpecimenId()
	{
		return this.specimenId;
	}

	/**
	 * @param specimenId The specimenId to set.
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * No argument constructor for UserForm class 
	 */
	public ListSpecimenEventParametersForm()
	{
		//        reset();
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 * @param abstractDomain An AbstractDomain Object  
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	}

	/**
	 * Checks the operation to be performed is add operation.
	 * @return Returns true if operation is equal to "add", else it returns false
	 * */
	@Override
	public boolean isAddOperation()
	{
		return (this.getOperation().equals(Constants.ADD));
	}

	/**
	 * @return LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID Returns the id assigned to form bean
	 */
	@Override
	public int getFormId()
	{
		return Constants.LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.  
	 * */
	@Override
	protected void reset()
	{
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
		//        Validator validator = new Validator();
		try
		{
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage(), excp);
		}
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}
