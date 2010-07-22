/**
 *
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import titli.model.Titli;
import titli.model.TitliException;
import titli.model.util.TitliTableMapper;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Initialize Titli Search/ Keyword Search
 *
 * @author Juber Patel
 *
 */
public class TitliInitialiseSearchAction extends XSSSupportedAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(TitliInitialiseSearchAction.class);

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
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Titli.getInstance();

		}
		catch (final TitliException e)
		{
			this.logger.error("TitliException in InitialiseTitliSearchAction : " + e.getMessage(),
					e);
			e.printStackTrace() ;
		}

		TitliTableMapper.getInstance();

		return mapping.findForward(Constants.SUCCESS);
	}

}
