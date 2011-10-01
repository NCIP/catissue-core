
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.processor.SPPEventProcessor;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author atul_kaushal
 *
 */
public class DisplaySOPEventsForSCGAction extends SecureAction
{

	/** Initialize logger. */
	private transient final Logger LOGGER = Logger
			.getCommonLogger(DisplaySOPEventsForSCGAction.class);

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
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String pageOf = "pageOfSCG";

		//Initialize bizLogic
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);

		//SOPEventProcessor
		SPPEventProcessor sppEventProcessor = new SPPEventProcessor();
		SpecimenCollectionGroup scgObject = null;
		String scgId = request.getParameter("id");

		if (scgId == null)
		{
			scgId = request.getParameter("scgId");
		}
		//Retrieve scg object based on scg Id
		String sppName = request.getParameter("sppName");
		request.setAttribute("id", scgId);
		request.setAttribute(Constants.PAGE_OF, Constants.PAGE_OF_SCG_CP_QUERY);

		scgObject = (SpecimenCollectionGroup) bizLogic.retrieve(SpecimenCollectionGroup.class
				.getName(), new Long(scgId));

		if (sppName != null && !sppName.isEmpty())
		{
			sppEventProcessor.populateSPPEventsForSCG(request, scgObject, sppName);
		}
		getSPPList(request, scgObject);
		Long scgEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID) == null)
		{
			scgEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);
		}
		else
		{
			scgEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID);

		}
		request.setAttribute(AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID, scgEntityId);

		return mapping.findForward(pageOf);
	}

	private void getSPPList(HttpServletRequest request, SpecimenCollectionGroup scgObject)
	{
		List<NameValueBean> sppNameList = new ArrayList<NameValueBean>();
		sppNameList.add(new NameValueBean(Constants.SELECT_OPTION, Constants.SELECT_OPTION));
		Collection<SOP> sppCollection = scgObject.getCollectionProtocolEvent().getSopCollection();
		Iterator<SOP> sopIter = sppCollection.iterator();
		while (sopIter.hasNext())
		{
			SOP spp = sopIter.next();
			String sppName1 = spp.getName();
			sppNameList.add(new NameValueBean(sppName1, sppName1));
		}
		request.setAttribute("sppNameList", sppNameList);
	}

}
