package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;

public class UpdateSpecimenStatusAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ViewSpecimenSummaryForm specimenSummaryForm =
			(ViewSpecimenSummaryForm)form;
		String eventId = specimenSummaryForm.getEventId();
		
		HttpSession session = request.getSession();
		
		Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		
		CollectionProtocolEventBean eventBean =(CollectionProtocolEventBean)
							collectionProtocolEventMap.get(eventId);
		
		LinkedHashMap specimenMap = (LinkedHashMap)eventBean.getSpecimenRequirementbeanMap();
		
		Collection specimenCollection = specimenMap.values();
		Iterator iterator = specimenCollection.iterator();
//		SessionDataBean sessionDataBean =(SessionDataBean) session.getAttribute(Constants.SESSION_DATA);

		SessionDataBean sessionDataBean = new SessionDataBean(); 
		sessionDataBean.setUserId(new Long(1));
		sessionDataBean.setUserName("admin@admin.com");


		while(iterator.hasNext())
		{
			GenericSpecimenVO specimenVO =(GenericSpecimenVO) iterator.next();
			if (specimenVO.getCheckedSpecimen())
			{
				NewSpecimenBizLogic bizLogic = new NewSpecimenBizLogic();
				Specimen specimen = new Specimen ();
				Long id = getSpecimenId(specimenVO);
				specimen.setId(id);
				bizLogic.setSpecimenCollected(specimen, sessionDataBean);
			}
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param specimenVO
	 * @return
	 */
	private Long getSpecimenId(GenericSpecimenVO specimenVO) {
		String uniqueId = specimenVO.getUniqueIdentifier();
		uniqueId = uniqueId.substring(uniqueId.indexOf("_")+1);
		Long id = new Long(uniqueId);
		return id;
	}

}
