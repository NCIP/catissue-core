/**
 * 
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.TitliTableMapper;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Initialize Titli Search
 * 
 * @author Juber Patel
 * 
 */
public class TitliInitialiseSearchAction extends Action
{

	private transient Logger logger = Logger.getCommonLogger(TitliInitialiseSearchAction.class);

	/**
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the action form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return action forward
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Titli.getInstance();

		}
		catch (TitliException e)
		{
			logger.error("TitliException in InitialiseTitliSearchAction : " + e.getMessage(), e);
		}

		TitliTableMapper.getInstance();

		return mapping.findForward(Constants.SUCCESS);
	}

}
