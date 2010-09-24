
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.yale.its.tp.cas.client.filter.CASFilter;

/**
 * 
 * @author sagar_baldwa
 *
 */
public class CasLoginForm extends AbstractActionForm
{
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 2984214147273681812L;

	/**
	 * login ID entered by user
	 */
	private String loginName;

	/**
	 * @return Returns the loginName.
	 */
	public String getLoginName()
	{
		return this.loginName;
	}

	/**
	 * @param loginName The loginName to set.
	 */
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	/**
	 * 
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final HttpSession prevSession = request.getSession();
		if (prevSession != null &&
			prevSession.getAttribute(CASFilter.CAS_FILTER_USER) != null)
		{
			this.loginName = (String)prevSession.getAttribute(CASFilter.CAS_FILTER_USER);
		}
		if (Validator.isEmpty(this.loginName))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("user.emailAddress")));
		}
		return errors;
	}

	/**
	 * Resets the values of all the fields.
	 * This method defined in ActionForm is overridden in this class.
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{

	}

	/**
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 */
	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 * @return 0
	 */
	@Override
	public int getFormId()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 * @param abstractDomain An AbstractDomain Object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}