/**
 * <p>Title: ConflictSCGDataDetailsAction Class>
 * <p>Description: To retrieve the SCG details
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @Date 9/18/2007
 * @author kalpana Thakur
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictSCGDataDetailsAction extends BaseAction
{
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
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
		String SCGId = (String) request.getParameter(Constants.ID);

		HttpSession session = request.getSession();
		session.setAttribute(Constants.SCG_ID_TO_ASSOCIATE, SCGId);

		//Retrieved the SCG and populated the bean
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		Object object = defaultBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), new Long(
				SCGId));

		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) object;
		defaultBizLogic.populateUIBean(SpecimenCollectionGroup.class.getName(),
				(Long) specimenCollectionGroup.getId(), specimenCollectionGroupForm);

		return mapping.findForward(Constants.SUCCESS);
	}
}
