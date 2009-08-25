
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

/**
 * 
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */
public class LoginForm extends AbstractActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2179334257607237377L;

	/**
	 * login ID entered by user
	 */
	private String loginName = new String();

	/**
	 * password entered by user
	 */
	private String password = new String();

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
	 * @return Returns the password.
	 */
	public String getPassword()
	{
		return this.password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password)
	{
		this.password = password;
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
		final HttpSession prevSession = request.getSession();
		if (prevSession != null)
		{
			prevSession.invalidate();
		}

		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		if (Validator.isEmpty(this.loginName))
		{
			//Mandar 05-apr-06 : bugid:928 Loginname in error changed to email address.
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("user.emailAddress")));
		}
		else
		{
			if (!Character.isLetter(this.loginName.charAt(0)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("user.loginName")));
			}
		}
		if (Validator.isEmpty(this.password))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("user.password")));
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
		this.loginName = null;
		this.password = null;
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