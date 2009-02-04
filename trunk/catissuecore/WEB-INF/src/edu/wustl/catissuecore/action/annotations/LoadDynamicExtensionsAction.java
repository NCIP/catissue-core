/*
 * Created on Jan 8, 2007
 * @author
 *
 */
package edu.wustl.catissuecore.action.annotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.common.action.BaseAction;

/**
 * @author preeti_munot
 *
 */
public class LoadDynamicExtensionsAction extends BaseAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
       
		AnnotationForm annotationForm = (AnnotationForm)form;
		//Get static entity id and store in cache
		String staticEntityId = annotationForm.getSelectedStaticEntityId();
        String[] conditions = annotationForm.getConditionVal();
        
		CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager.getInstance();
		if(cacheManager!=null)
		{
			cacheManager.addObjectToCache(AnnotationConstants.SELECTED_STATIC_ENTITYID, staticEntityId);
            cacheManager.addObjectToCache(AnnotationConstants.SELECTED_STATIC_RECORDID, conditions);          
		}
		//Get Dynamic extensions URL
		String dynamicExtensionsURL =getDynamicExtensionsURL(request);
		//Set as request attribute
		request.setAttribute(AnnotationConstants.DYNAMIC_EXTN_URL_ATTRIB, dynamicExtensionsURL);           
        return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param request 
	 * @return
	 */
	private String getDynamicExtensionsURL(HttpServletRequest request)
	{
		//Get Dynamic extensions URL
		String dynamicExtensionsURL = WebUIManager.getCreateContainerURL();
		//append container id if any
		if(request.getParameter("containerId")!=null)
		{
			dynamicExtensionsURL = dynamicExtensionsURL + "?" + WebUIManager.CONATINER_IDENTIFIER_PARAMETER_NAME +"=" +request.getParameter("containerId");
			dynamicExtensionsURL = dynamicExtensionsURL + "&" + WebUIManager.getCallbackURLParamName() + "=" + request.getContextPath() + AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DEFN;
		}
		else
		{
		//append callback parameter
			dynamicExtensionsURL = dynamicExtensionsURL + "?" + WebUIManager.getCallbackURLParamName() + "=" + request.getContextPath() + AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DEFN;
		}
		return dynamicExtensionsURL;
	}
}
