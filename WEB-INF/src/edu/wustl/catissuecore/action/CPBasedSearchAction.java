
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.krishagni.catissueplus.core.biospecimen.repository.impl.DaoFactoryImpl;

import edu.wustl.common.action.SecureAction;

/**
 * This Action is for forwarding to CPResultsView Page.
 *
 * @author vaishali_khandelwal
 */
public class CPBasedSearchAction extends SecureAction
{

	/**
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
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		// SessionDataBean sessionDataBean = super.getSessionData(request);

		/**
		 * Name : Aarti Sharma Reviewer: Sachin Lale Bug ID: 4111 Patch ID:
		 * 4111_1 See also: 4111_2 Desciption: If user does not have privilege
		 * to view identified data attribute "Access" is set to "Denied" so that
		 * he/she is not able to view the Participant list in CP based view
		 */
		// long csmUserId = new
		// Long(sessionDataBean.getCsmUserId()).longValue();
		// boolean hasIdentifiedDataAccess = true;
		////SecurityManager.getInstance(this.getClass()).hasIdentifiedDataAccess
		// (csmUserId);
		// if(!hasIdentifiedDataAccess)
		// {
		// request.getSession().setAttribute("Access", "Denied");
		// }
		// else
		// {
		// request.removeAttribute("Access");
		// }
		
		String specimenIdStr = request.getParameter("specimenId");
		if (!StringUtils.isBlank(specimenIdStr)) {
			String[] tokens = specimenIdStr.split(",");
			if (tokens != null && tokens.length == 2 && tokens[0].equals("specimen")) {
				Long specimenId = Long.parseLong(tokens[1]);
				Long scgId = getScgId(specimenId);
				request.setAttribute("scgId", "scg," + scgId);
			}
			
		}
		request.setAttribute("view","cpBasedView");
		return mapping.findForward("success");

	}
	
	/*
	 * This is temporary solution until we rewrite CP based view 
	 */
	private Long getScgId(Long specimenId) {
		ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(this.servlet.getServletContext());
		if (appCtx == null) {
			throw new RuntimeException("Couldn't obtain application context");
		}
		
		DaoFactoryImpl daoFactory = (DaoFactoryImpl)appCtx.getBean("biospecimenDaoFactory");
		SessionFactory sessionFactory = daoFactory.getSessionFactory();
		
		Session session = sessionFactory.getCurrentSession();
		Transaction txn = session.getTransaction();
		try {
			txn.begin();
			return daoFactory.getSpecimenDao().getScgId(specimenId);			
		} finally {
			if (txn != null && txn.isActive()) {
				txn.rollback();
			}
			
			if (session.isOpen()) {
				session.close();
			}			
		}
	}
}
