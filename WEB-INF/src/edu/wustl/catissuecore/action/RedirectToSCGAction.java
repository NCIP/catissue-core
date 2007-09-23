package edu.wustl.catissuecore.action;

import java.util.List;

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
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;

public class RedirectToSCGAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SpecimenCollectionGroupForm specimenCollectionGroupForm =(SpecimenCollectionGroupForm) form;
		Long id = (Long) request.getSession().getAttribute("SCGFORM");
		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		
		SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);

		((HibernateDAO)dao).openSession(sessionDataBean);
		
		List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", id);
		if (scgList != null && !scgList.isEmpty())
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) scgList.get(0);
			specimenCollectionGroupForm.setAllValues(specimenCollectionGroup);
		}
		((HibernateDAO)dao).closeSession();
		return mapping.findForward(Constants.SUCCESS);
	}

}
