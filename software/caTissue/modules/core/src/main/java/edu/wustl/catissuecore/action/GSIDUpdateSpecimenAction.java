package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.GSID.GSIDConstant;
import edu.wustl.catissuecore.GSID.GSIDUtil;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

/***
 * @author srikalyan
 */
public class GSIDUpdateSpecimenAction extends SecureAction {

	/** The Constant LOGGER. */
	private static final Logger LOG = Logger
			.getCommonLogger(GSIDUpdateSpecimenAction.class);

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			SessionDataBean sessionData = (SessionDataBean) request
					.getSession().getAttribute(Constants.SESSION_DATA);
			if (!StringUtils.isBlank(request.getParameter("id"))) {
				long identifier = Integer.parseInt(request.getParameter("id"));
				GSIDUtil util = new GSIDUtil();
				Specimen specimen = util.registerSpecimenById(identifier,
						sessionData);
				String msg = null;
				Thread.sleep(2000);
				if (specimen != null
						&& StringUtils.isBlank(specimen
								.getGlobalSpecimenIdentifier())) {
					if (specimen.getParentSpecimen() != null
							&& specimen.getParentSpecimen() instanceof Specimen) {
						Specimen parent = (Specimen) specimen
								.getParentSpecimen();
						if (StringUtils.isBlank(parent
								.getGlobalSpecimenIdentifier())) {
							msg = GSIDConstant.GSID_UPDATE_PARENT_SPECIMEN_MSG;
						} else {
							msg = GSIDConstant.GSID_SERVICE_DOWN;
						}
					} else {
						msg = GSIDConstant.GSID_SERVICE_DOWN;
					}
				} else {
					if (specimen != null) {
						msg = specimen.getGlobalSpecimenIdentifier();
					}
				}
				LOG.debug("the message is " + msg);
				request.setAttribute("GSIDMessage", msg);
			}

			return mapping.findForward(Constants.SUCCESS);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					GSIDConstant.GSID_SERVICE_DOWN);
			return null;
		}
	}
}
