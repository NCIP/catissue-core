
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.bizlogic.SOPBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

public class DisplaySPPForSCGAction extends SecureAction
{

	/** Initialize logger. */
	private transient final Logger LOGGER = Logger.getCommonLogger(DisplaySPPForSCGAction.class);

	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in SpecimenEventParameters.jsp Page.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return value for ActionForward object
	 *
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Initialize bizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

		//Retrieve scg object based on scg Id
		String scgId = request.getParameter("id");
		request.setAttribute("id", scgId);
		SpecimenCollectionGroup scgObject = (SpecimenCollectionGroup) bizLogic.retrieve(
				SpecimenCollectionGroup.class.getName(), new Long(scgId));
		List<NameValueBean> sppNameList = new SOPBizLogic().getSPPNameList(scgObject);
		request.setAttribute("sppNameList", sppNameList);
		List<Map<String, Object>> sppEventDataCollection = null;
		Map<String, Long> dynamicEventMap = new HashMap<String, Long>();
		new SOPBizLogic().getAllSOPEventFormNames(dynamicEventMap);
		request.getSession().setAttribute(Constants.DYNAMIC_EVENT_MAP, dynamicEventMap);
		request.setAttribute("SOPEvents", sppEventDataCollection);
		Long scgEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID) == null)
		{
			scgEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);
		}
		else
		{
			scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);

		}
		request.setAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);
		request.setAttribute(Constants.PAGE_OF, "pageOfSpecimenCollectionGroupCPQuery");

		return mapping.findForward("pageOfDynamicEvent");
	}

}
