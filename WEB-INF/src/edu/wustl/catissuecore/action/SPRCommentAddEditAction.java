
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class SPRCommentAddEditAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SPRCommentAddEditAction.class);

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
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		final ViewSurgicalPathologyReportForm viewSurgicalPathologyReportForm = (ViewSurgicalPathologyReportForm) form;
		try
		{
			AbstractDomainObject abstractDomain = (new DomainObjectFactory().getDomainObject(
					viewSurgicalPathologyReportForm.getFormId(), viewSurgicalPathologyReportForm));
			abstractDomain = defaultBizLogic.populateDomainObject(abstractDomain.getClass()
					.getName(), new Long(viewSurgicalPathologyReportForm.getId()),
					viewSurgicalPathologyReportForm);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(viewSurgicalPathologyReportForm
					.getFormId());
			if (abstractDomain != null)
			{
				final Object object = bizLogic.retrieve(abstractDomain.getClass().getName(),
						new Long(viewSurgicalPathologyReportForm.getId()));
				final AbstractDomainObject abstractDomainOld = (AbstractDomainObject) object;
				if (abstractDomainOld instanceof QuarantineEventParameter)
				{
					final QuarantineEventParameter quarantineEventParamanter = (QuarantineEventParameter) abstractDomainOld;
					final DeidentifiedSurgicalPathologyReport deidReport = (DeidentifiedSurgicalPathologyReport) bizLogic
							.retrieveAttribute(QuarantineEventParameter.class.getName(),
									quarantineEventParamanter.getId(),
									Constants.COLUMN_NAME_DEID_REPORT);
					quarantineEventParamanter.setDeIdentifiedSurgicalPathologyReport(deidReport);
				}
				bizLogic.update(abstractDomain, abstractDomainOld, 0, this.getSessionData(request));
			}
		}
		catch (final Exception ex)
		{
			this.logger.error("Error occured in SPRCommentAddEditAction " + ex);
			return (mapping.findForward(Constants.FAILURE));
		}
		// OnSubmit
		if (viewSurgicalPathologyReportForm.getOnSubmit() != null
				&& viewSurgicalPathologyReportForm.getOnSubmit().trim().length() > 0)
		{
			final String forwardTo = viewSurgicalPathologyReportForm.getOnSubmit();
			return (mapping.findForward(forwardTo));
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
}
