/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;
import edu.wustl.dao.exception.DAOException;
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
	private static final Logger LOG = Logger.getCommonLogger(GSIDUpdateSpecimenAction.class);

    protected static DAO getHibernateDao(String appName, SessionDataBean sessionDataBean) throws DAOException {
        IDAOFactory daofactory = DAOConfigFactory.getInstance().getDAOFactory(appName);
        DAO dao = daofactory.getDAO();
        dao.openSession(sessionDataBean);
        return dao;
    }

	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
			if (!StringUtils.isBlank(request.getParameter("id"))) {

                SessionDataBean sessionDataBean = getSessionData(request);
                DAO dao = getHibernateDao(CommonServiceLocator.getInstance().getAppName(), sessionDataBean);
                String className = "edu.wustl.catissuecore.domain.Specimen";

                long identifier = Integer.parseInt(request.getParameter("id"));
                Object object = dao.retrieveById(className, identifier);

                NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
                boolean isAuthorized;

                isAuthorized = bizLogic.isAuthorized(dao, object, sessionDataBean);
                System.out.println(">>> isAuthorized:" + isAuthorized);

                String msg = null;
                if (isAuthorized) {
                    GSIDUtil util = new GSIDUtil();
                    Specimen specimen = util.registerSpecimenById(identifier, sessionData);
                    Thread.sleep(2000);
                    if (specimen != null && StringUtils.isBlank(specimen.getGlobalSpecimenIdentifier())) {
                        if (specimen.getParentSpecimen() != null && specimen.getParentSpecimen() instanceof Specimen) {
                            Specimen parent = (Specimen) specimen.getParentSpecimen();
                            if (StringUtils.isBlank(parent.getGlobalSpecimenIdentifier())) {
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
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GSIDConstant.GSID_SERVICE_DOWN);
                    return null;
                }

                LOG.debug("the message is " + msg);
				request.setAttribute("GSIDMessage", msg);
			}

			return mapping.findForward(Constants.SUCCESS);
		} catch (edu.wustl.common.exception.BizLogicException e) {
            LOG.error(e.getMessage());
            request.setAttribute("GSIDMessage", "ERROR:" + e.getMessage());
            return mapping.findForward(Constants.SUCCESS);
        } catch (Exception e) {
			LOG.error(e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, GSIDConstant.GSID_SERVICE_DOWN);
			return null;
		}
	}
}
