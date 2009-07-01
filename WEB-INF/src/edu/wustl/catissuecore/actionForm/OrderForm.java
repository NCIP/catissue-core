
package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.Validator;

/**
 * @author deepti_phadnis
 * 
 */
public class OrderForm extends AbstractActionForm
{

	/**
	 * String contains the order name
	 */
	private String orderRequestName = null;

	/**
	 * String contains the distribution protocol
	 */
	private String distributionProtocol;

	/**
	 * String contains the comments
	 */
	private String comments;

	/**
	 * @param orderRequestName String contains the order name
	 */
	public void setOrderRequestName(String orderRequestName)
	{
		this.orderRequestName = orderRequestName;
	}

	/**
	 * @return orderRequestName
	 */
	public String getOrderRequestName()
	{
		return (this.orderRequestName);
	}

	/**
	 * @param distributionProtocol String contains the distribution protocol
	 */
	public void setDistributionProtocol(String distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	/**
	 * @return distributionProtocol
	 */
	public String getDistributionProtocol()
	{
		return (this.distributionProtocol);
	}

	/**
	 * @param comments String contains the comments
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return comments
	 */
	public String getComments()
	{
		return (this.comments);
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	}

	/**
	 * @return FormId
	 */
	public int getFormId()
	{
		return Constants.ORDER_FORM_ID;
	}

	/**
	 * function reset 
	 */
	protected void reset()
	{
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.orderRequestName = null;
		this.distributionProtocol = null;
		this.comments = null;
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();

		if ((orderRequestName == null) || (orderRequestName.length() == 0))
		{
			errors.add("orderRequestName", new ActionError("errors.ordername.required"));
		}
		if ((distributionProtocol == null) || (distributionProtocol.equals("-1")))
		{
			errors.add("distributionProtocol", new ActionError(
					"errors.distributionprotocol.required"));
		}

		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
