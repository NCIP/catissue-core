/*
 * Created on Jan 17, 2007
 * @author
 *
 */

package edu.wustl.catissuecore.action.annotations;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoadDynamicExtentionsDataEntryPageAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(LoadDynamicExtentionsDataEntryPageAction.class);
	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		AnnotationDataEntryForm annotationDataEntryForm = (AnnotationDataEntryForm) form;
		updateCache(request, annotationDataEntryForm);

		//Set as request attribute
		String dynExtDataEntryURL = getDynamicExtensionsDataEntryURL(request, annotationDataEntryForm);
		request.setAttribute(AnnotationConstants.DYNAMIC_EXTN_DATA_ENTRY_URL_ATTRIB, dynExtDataEntryURL);
		request.setAttribute(Constants.OPERATION, "DefineDynExtDataForAnnotations");
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param request
	 * @param annotationDataEntryForm 
	 * @throws CacheException 
	 * @throws DynamicExtensionsApplicationException
	 * @throws NumberFormatException 
	 * @throws BizLogicException BizLogic Exception
	 */
	private void updateCache(HttpServletRequest request, AnnotationDataEntryForm annotationDataEntryForm) throws CacheException, NumberFormatException, DynamicExtensionsSystemException, BizLogicException
	{
		String staticEntityId = annotationDataEntryForm.getParentEntityId();
		String dynEntContainerId = annotationDataEntryForm.getSelectedAnnotation();

		//Set into Cache
		//CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager.getInstance();
		HttpSession session = request.getSession();
		Long entityMapId = getEntityMapId(AppUtility.toLong(staticEntityId),AppUtility.toLong(dynEntContainerId));
		session.setAttribute(AnnotationConstants.SELECTED_ENTITY_MAP_ID, entityMapId);
		session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID,annotationDataEntryForm.getSelectedStaticEntityId());
		session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID,annotationDataEntryForm.getSelectedStaticEntityRecordId());
	}

	/**
	 * @param annotationDataEntryForm
	 * @throws BizLogicException BizLogic Exception
	 * @throws DynamicExtensionsApplicationException 
	 */
	private Long getEntityMapId(Long staticEntityId, Long dynamicEntityContainerId) throws DynamicExtensionsSystemException, BizLogicException
	{
		if (staticEntityId != null)
		{
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			List<EntityMap> entityMapsForStaticEntity = annotationBizLogic.getListOfDynamicEntities(staticEntityId);
			EntityMap entityMap = getEntityMapForSelectedDE(dynamicEntityContainerId, entityMapsForStaticEntity);
			if (entityMap != null)
			{
				return entityMap.getId();
			}
		}
		return null;
	}

	/**
	 * @param deContainerId
	 * @param entityMapsForStaticEntity 
	 */
	private EntityMap getEntityMapForSelectedDE(Long deContainerId, List<EntityMap> entityMapsForStaticEntity)
	{
		if ((deContainerId != null) && (entityMapsForStaticEntity != null))
		{
			EntityMap entityMap = null;
			Iterator<EntityMap> entityMapIter = entityMapsForStaticEntity.iterator();
			while (entityMapIter.hasNext())
			{
				entityMap = entityMapIter.next();
				if ((entityMap != null) && (entityMap.getContainerId() != null))
				{
					if (entityMap.getContainerId().longValue() == deContainerId.longValue())
					//If matches the specified DE container id	 
					{
						return entityMap;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private String getDynamicExtensionsDataEntryURL(HttpServletRequest request, AnnotationDataEntryForm annotationDataEntryForm)
	{
		String dynExtDataEntryURL = request.getContextPath() + WebUIManager.getLoadDataEntryFormActionURL();
		 SessionDataBean sessionbean = (SessionDataBean)request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.SESSION_DATA);
         String userId= sessionbean.getUserId().toString();
		String isAuthenticatedUser = "false";
		if (userId != null)
		{
			isAuthenticatedUser = "true";
		}
		
		//Append container id
		logger.info("Load data entry page for Dynamic Extension Entity [" + annotationDataEntryForm.getSelectedAnnotation() + "]");
		dynExtDataEntryURL = dynExtDataEntryURL + "&" + WebUIManager.CONATINER_IDENTIFIER_PARAMETER_NAME + "="
				+ annotationDataEntryForm.getSelectedAnnotation();
		if (request.getParameter("recordId") != null)
		{
			logger.info("Loading details of record id [" + request.getParameter("recordId") + "]");
			dynExtDataEntryURL = dynExtDataEntryURL + "&" + WebUIManager.RECORD_IDENTIFIER_PARAMETER_NAME + "=" + request.getParameter("recordId");
		}
		String operation = request.getParameter("operation");
		String selectedAnnotation = request.getParameter("selectedAnnotation");
		String callbackURL=AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DATA_ENTRY+"?"+"editOperation="+operation+"@selectedAnnotation="+selectedAnnotation;
		//append callback url
		dynExtDataEntryURL = dynExtDataEntryURL + "&" + WebUIManager.getCallbackURLParamName() + "=" + request.getContextPath()
				+callbackURL  + "&isAuthenticatedUser=" + isAuthenticatedUser;
		return dynExtDataEntryURL;
	}

}
