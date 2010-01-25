
package edu.wustl.catissuecore.action;

/**
 * This class is called when user clicks on Delete button on Specimen Array page
 *
 * @author nitesh_marwaha
 */
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DeleteSpecimenArrayAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DeleteSpecimenArrayAction.class);

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
	 * @throws IOException
	 *             : IOException
	 * @throws ServletException
	 *             : ServletException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
			
	{

		final Map arrayContentMap = (Map) request.getSession().getAttribute(
				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		final AbstractActionForm abstractForm = (AbstractActionForm) form;
		abstractForm.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
		try
		{
			if (arrayContentMap != null)
			{
				final MapDataParser mapDataParser = new MapDataParser(
						"edu.wustl.catissuecore.domain");
				final Collection specimenArrayContentList = mapDataParser
						.generateData(arrayContentMap);
				if (abstractForm instanceof SpecimenArrayForm)
				{
					final SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) abstractForm;
					specimenArrayForm.setSpecArrayContentCollection(specimenArrayContentList);
				}
			}
		}
		catch (final Exception exception)
		{
			this.logger.error(exception.getMessage(), exception);
			exception.printStackTrace();
		}
		return super.executeXSS(mapping, abstractForm, request, response);
	}
}
