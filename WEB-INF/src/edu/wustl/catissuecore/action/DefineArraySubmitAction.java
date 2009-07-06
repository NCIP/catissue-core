
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DefineArraySubmitAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(DefineArraySubmitAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DefineArrayForm defineArray = (DefineArrayForm) form;
		HttpSession session = request.getSession(true);

		List defineArrayFormList = null;
		String target = "success";

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

		try
		{
			String sourceObjectName = SpecimenArrayType.class.getName();
			String[] displayName = {"name"};
			String valueField = Constants.SYSTEM_IDENTIFIER;
			List arrayTypeList = bizLogic.getList(sourceObjectName, displayName, valueField, true);

			for (int i = 0; i < arrayTypeList.size(); i++)
			{
				NameValueBean obj = (NameValueBean) arrayTypeList.get(i);
				if (defineArray.getArraytype().equals(obj.getValue()))
				{
					defineArray.setArrayTypeName(obj.getName());
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}

		// added for checking if array of same name exists
		if (session.getAttribute("DefineArrayFormObjects") != null)
		{
			defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");

			for (int i = 0; i < defineArrayFormList.size(); i++)
			{
				DefineArrayForm defineArrayObj = (DefineArrayForm) defineArrayFormList.get(i);
				if (defineArrayObj.getArrayName().equals(defineArray.getArrayName()))
				{
					ActionErrors errors = (ActionErrors) request.getAttribute
					(Globals.ERROR_KEY);
					if (errors == null || errors.size() == 0)
					{
						errors = new ActionErrors();
					}
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"orderingsystem.arrayname.present"));
					saveErrors(request, errors);
					target = "defineArrayPage";
				}
			}
		}
		else
		{
			defineArrayFormList = new ArrayList();
		}

		String typeOf = request.getParameter("typeOf");
		request.setAttribute("typeOf", typeOf);
		if (target.equals("success"))
			{
			defineArrayFormList.add(defineArray);
			}
		session.setAttribute("DefineArrayFormObjects", defineArrayFormList);
		return mapping.findForward(target);
	}

}
