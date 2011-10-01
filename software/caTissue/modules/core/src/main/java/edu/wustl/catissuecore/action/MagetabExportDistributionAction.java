
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConfigureResultViewForm;
import edu.wustl.catissuecore.actionForm.DistributionReportForm;
import edu.wustl.catissuecore.bizlogic.magetab.MagetabExportWizardBean;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.ExportReport;
import edu.wustl.common.util.SendFile;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * This is the action class for saving the Distribution report.
 *
 * @author Poornima Govindrao
 * @author Alexander Zgursky
 */

public class MagetabExportDistributionAction extends BaseDistributionReportAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(MagetabExportDistributionAction.class);

	/**
	 * Overrides the execute method of Action class. Sets the various fields in
	 * DistributionProtocol Add/Edit webpage.
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
	 * @return value for ActionForward object
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final ConfigureResultViewForm configForm = (ConfigureResultViewForm) form;
		// Retrieve the distribution ID
		final Long distributionId = configForm.getDistributionId();

		final Distribution dist = this.getDistribution(distributionId,
				this.getSessionData(request),
				edu.wustl.security.global.Constants.CLASS_LEVEL_SECURE_RETRIEVE);
		
		List<Long> specimenIds = new LinkedList<Long>();
		for (DistributedItem item : dist.getDistributedItemCollection()) {
			specimenIds.add(item.getSpecimen().getId());
		}
		
		HttpSession session = request.getSession();
		MagetabExportWizardBean wizardBean = (MagetabExportWizardBean)
				session.getAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN);
		
		if (wizardBean == null) {
			wizardBean = new MagetabExportWizardBean();
			session.setAttribute(MagetabExportWizardBean.MAGETAB_EXPORT_WIZARD_BEAN, wizardBean);
		}
		wizardBean.init(specimenIds);
		return mapping.findForward("startWizard");
	}
}