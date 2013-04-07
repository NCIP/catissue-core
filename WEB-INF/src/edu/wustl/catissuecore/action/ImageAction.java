package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;

public class ImageAction extends CatissueBaseAction 
{

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		request.setAttribute("isImagingConfigurred", Variables.isImagingConfigurred);
		String obj = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		request.setAttribute("specimenId", obj);
		Long specimenEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
		{
			specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
		}
		else
		{
			specimenEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
		}
		request.setAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
		request.setAttribute("entityName",AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
		return mapping.findForward(Constants.SUCCESS);
	}


}
