/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.querysuite.SaveQueryForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.util.dbManager.HibernateUtility;

/**
 * @author chetan_patil
 * @created September 12, 2007, 10:24:05 PM
 */
public class RetrieveQueryAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionForward = null;

		try
		{
			SaveQueryForm saveQueryForm = (SaveQueryForm) actionForm;
			Collection<IParameterizedQuery> parameterizedQueryCollection = (Collection<IParameterizedQuery>) HibernateUtility
					.executeHQL(HibernateUtility.GET_PARAMETERIZED_QUERIES_DETAILS);

			String message = null;
			
			if (parameterizedQueryCollection != null)
			{
				saveQueryForm.setParameterizedQueryCollection(parameterizedQueryCollection);
				message = parameterizedQueryCollection.size() + "";
			}
			else
			{
				saveQueryForm.setParameterizedQueryCollection(new ArrayList<IParameterizedQuery>());
				message = "No";
			}

			ActionMessages actionMessages = new ActionMessages();
			ActionMessage actionMessage = new ActionMessage("query.resultFound.message", message);
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
			saveMessages(request, actionMessages);

			actionForward = actionMapping.findForward(Constants.SUCCESS);
		}
		catch (HibernateException hibernateException)
		{
			actionForward = actionMapping.findForward(Constants.FAILURE);
		}

		return actionForward;
	}

}
