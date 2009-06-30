
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;

/**
 * @author renuka_bajpai
 *
 */
public class RedirectToSCGAction extends Action
{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
		Long id = (Long) request.getSession().getAttribute("SCGFORM");
		IDAOFactory daoFact = DAOConfigFactory.getInstance().getDAOFactory(
				CommonServiceLocator.getInstance().getAppName());
		DAO hibernateDao = null;
		hibernateDao = daoFact.getDAO();

		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);

		hibernateDao.openSession(sessionDataBean);

		Object object = hibernateDao.retrieveById(SpecimenCollectionGroup.class.getName(),
				new Long(id));
		if (object != null)
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) object;
			specimenCollectionGroupForm.setAllValues(specimenCollectionGroup);
		}
		hibernateDao.closeSession();
		return mapping.findForward(Constants.SUCCESS);
	}

}
