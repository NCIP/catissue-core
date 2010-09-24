
package edu.wustl.catissuecore.action;

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
import edu.wustl.common.util.logger.Logger;

/**
 * <p>
 * This class initializes the fields of SpecimenArrayAddEditAction.java.
 * </p>
 *
 * @author Ashwin Gupta
 * @version 1.1
 */
public class SpecimenArrayAddEditAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(SpecimenArrayAddEditAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final Map arrayContentMap = (Map) request.getSession().getAttribute(
				Constants.SPECIMEN_ARRAY_CONTENT_KEY);
		if (arrayContentMap != null)
		{
			try
			{
				final MapDataParser mapDataParser = new MapDataParser(
						"edu.wustl.catissuecore.domain");
				final Collection specimenArrayContentList = mapDataParser
						.generateData(arrayContentMap);
				final AbstractActionForm abstractForm = (AbstractActionForm) form;

				if (abstractForm instanceof SpecimenArrayForm)
				{
					final SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) abstractForm;
					specimenArrayForm.setSpecArrayContentCollection(specimenArrayContentList);
				}
				/*
				 * AbstractDomainObject abstractDomain =
				 * abstractDomainObjectFactory
				 * .getDomainObject(abstractForm.getFormId(), abstractForm); if
				 * (abstractDomain instanceof SpecimenArray) { SpecimenArray
				 * specimenArray = (SpecimenArray) abstractDomain;
				 * specimenArray.
				 * setSpecimenArrayContentCollection(specimenArrayContentList);
				 * }
				 */
			}
			catch (final Exception exception)
			{
				this.logger.error(exception.getMessage(), exception);
			}
		}
		return super.executeXSS(mapping, form, request, response);
	}
}
