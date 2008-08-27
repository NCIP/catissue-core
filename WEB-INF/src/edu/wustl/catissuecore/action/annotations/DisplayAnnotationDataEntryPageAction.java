/*
 * @author
 *
 */
package edu.wustl.catissuecore.action.annotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapRecord;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DisplayAnnotationDataEntryPageAction extends BaseAction
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		request.setAttribute("entityId",request.getParameter("entityId"));
        request.setAttribute("entityRecordId",request.getParameter("entityRecordId"));
        request.setAttribute(Constants.ID,request.getParameter(Constants.ID));
        request.setAttribute("staticEntityName",request.getParameter("staticEntityName"));
        
        SessionDataBean sessionDataBean = getSessionData(request);
        
        List cpIdsList = new ArrayList();
		cpIdsList = edu.wustl.common.util.Utility.getCPIdsList(request.getParameter("staticEntityName"), Long.valueOf(request.getParameter("entityRecordId")), sessionDataBean, cpIdsList);
		
		PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(sessionDataBean.getUserName());
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.COLLECTION_PROTOCOL_CLASS_NAME).append("_");
		for (Object cpId : cpIdsList)
		{
			boolean hasPrivilege = ((privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), Permissions.REGISTRATION)) ||
							(privilegeCache.hasPrivilege(sb.toString()+cpId.toString(), Permissions.SPECIMEN_PROCESSING)));				
			if(!hasPrivilege)
			{
				ActionErrors errors = new ActionErrors();
		        ActionError error = new ActionError("access.view.action.denied");
		        errors.add(ActionErrors.GLOBAL_ERROR, error);
		        saveErrors(request, errors);
		        return mapping.findForward(Constants.ACCESS_DENIED);
				// throw new DAOException("Access denied ! User does not have privilege to view/edit this information.");
			}
		}
		
        return mapping.findForward(request.getParameter("pageOf"));
	}

	/**
	 * @param request
	 * @throws CacheException
	 */
	private void updateCache(HttpServletRequest request) throws CacheException
	{
		String parentEntityId = request.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
		String parentEntityRecordId = request.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID);
		String entityIdForCondition =  request.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_ID);
		String entityRecordIdForCondition =  request.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_RECORD_ID);
		//Set into Cache
		//CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager.getInstance();
		HttpSession session = request.getSession();
		if(session !=null)
		{
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID, parentEntityId);
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID, parentEntityRecordId);
			session.setAttribute(AnnotationConstants.ENTITY_ID_IN_CONDITION, entityIdForCondition);
			session.setAttribute(AnnotationConstants.ENTITY_RECORDID_IN_CONDITION, entityRecordIdForCondition);
		}
	}

//	/**
//	 * @param selected_static_entityid
//	 * @return
//	 * @throws CacheException
//	 */
//	private Object getObjectFromCache(String key) throws CacheException
//	{
//		if(key!=null)
//		{
//			CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager.getInstance();
//			if(cacheManager!=null)
//			{
//				return cacheManager.getObjectFromCache(key);
//			}
//		}
//		return null;
//	}

	/**
	 * @param request
	 * @throws CacheException
	 * @throws BizLogicException
	 */
	private void processResponseFromDynamicExtensions(HttpServletRequest request) throws CacheException, BizLogicException
	{
		System.out.println("Request query string = " + request.getQueryString());
		String operationStatus = request.getParameter(WebUIManager.getOperationStatusParameterName());
		if((operationStatus!=null)&&(operationStatus.trim().equals(WebUIManagerConstants.SUCCESS)))
		{
			String dynExtRecordId = request.getParameter(WebUIManager.getRecordIdentifierParameterName());
			Logger.out.info("Dynamic Entity Record Id [" + dynExtRecordId + "]");
			insertEntityMapRecord(request,dynExtRecordId);
		}
	}

	/**
	 * @param request
	 * @param dynExtRecordId
	 * @throws CacheException
	 * @throws BizLogicException
	 */
	private void insertEntityMapRecord(HttpServletRequest request, String dynExtRecordId) throws CacheException, BizLogicException
	{
		EntityMapRecord entityMapRecord = getEntityMapRecord(request,dynExtRecordId);
		if(entityMapRecord!=null)
		{
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			try
            {
                annotationBizLogic.insertEntityRecord(entityMapRecord);
            }
            catch (DAOException e)
            {
                // TODO ERROR HANDLING STILL REMAINING
                e.printStackTrace();
            }
		}
	}

	/**
	 * @param request
	 * @param dynExtRecordId
	 * @return
	 * @throws CacheException
	 */
	private EntityMapRecord getEntityMapRecord(HttpServletRequest request, String dynExtRecordId) throws CacheException
	{
		EntityMapRecord entityMapRecord = null;
		String staticEntityRecordId = (String)request.getSession().getAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
		Long entityMapId = (Long)request.getSession().getAttribute(AnnotationConstants.SELECTED_ENTITY_MAP_ID);
		if((entityMapId!=null)&&(staticEntityRecordId!=null)&&(dynExtRecordId!=null))
		{
			entityMapRecord = new EntityMapRecord();
			entityMapRecord.getFormContext().getEntityMap().setId(entityMapId);
			entityMapRecord.setStaticEntityRecordId(Utility.toLong(staticEntityRecordId));
			entityMapRecord.setDynamicEntityRecordId(Utility.toLong(dynExtRecordId));
			SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
			if(sessionDataBean!=null)
			{
				entityMapRecord.setCreatedBy(sessionDataBean.getLastName()+","+sessionDataBean.getFirstName());
				entityMapRecord.setCreatedDate(new Date());
			}
			entityMapRecord.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
		}
		return entityMapRecord;
	}

	/**
	 * @param request
	 * @param entityRecordIdForCondition
	 * @param entityIdForCondition
	 * @param parentEntityId
	 * @param annotationDataEntryForm
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private void initializeDataEntryForm(HttpServletRequest request, String staticEntityId,String staticEntityRecordId, String entityIdForCondition, String entityRecordIdForCondition, AnnotationDataEntryForm annotationDataEntryForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//Set annotations list
		if(staticEntityId!=null)
		{
			List annotationList = getAnnotationList(staticEntityId,entityIdForCondition,entityRecordIdForCondition);
			annotationDataEntryForm.setAnnotationsList(annotationList);
			annotationDataEntryForm.setParentEntityId(staticEntityId);
		}

		//Set defined annotations information
		String definedAnnotationsDataXML = getDefinedAnnotationsDataXML(request,Utility.toLong(staticEntityId),Utility.toLong(staticEntityRecordId));
		annotationDataEntryForm.setDefinedAnnotationsDataXML(definedAnnotationsDataXML);
	}

	/**
	 * @param request
	 * @param entityId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private String getDefinedAnnotationsDataXML(HttpServletRequest request, Long staticEntityId,Long staticEntityRecordId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer definedAnnotationsXML = new StringBuffer();
		//"<?xml version='1.0' encoding='UTF-8'?><rows><row id='1' class='formField'><cell>0</cell><cell>001</cell><cell>Preeti</cell><cell>12-2-1990</cell><cell>Preeti</cell></row></rows>";
		definedAnnotationsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		if(staticEntityId!=null)
		{
			List<EntityMapRecord> entityMapRecords  = getEntityMapRecords(staticEntityId,staticEntityRecordId);
			if(entityMapRecords!=null)
			{
				definedAnnotationsXML.append("<rows>");
				Iterator<EntityMapRecord> iterator =entityMapRecords.iterator();
				EntityMapRecord entityMapRecord = null;
				int index = 1;
				while(iterator.hasNext())
				{
					entityMapRecord  = iterator.next();
					definedAnnotationsXML.append(getXMLForEntityMapRecord(request,entityMapRecord,index++));
				}
				definedAnnotationsXML.append("</rows>");
			}
		}
		return definedAnnotationsXML.toString();
	}

	/**
	 * @param request
	 * @param entityMapRecord
	 * @param index
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private String getXMLForEntityMapRecord(HttpServletRequest request, EntityMapRecord entityMapRecord, int index) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer entityMapRecordXML = new StringBuffer();
		if(entityMapRecord!=null)
		{
			NameValueBean dynamicEntity = getDynamicEntity(entityMapRecord.getFormContext().getEntityMap().getId());
			if(dynamicEntity!=null)
			{
				String strURLForEditRecord = getURLForEditEntityMapRecord(request,dynamicEntity.getName(),entityMapRecord.getDynamicEntityRecordId());
				entityMapRecordXML.append("<row id='" + index + "' >");
				entityMapRecordXML.append("<cell>" + "0" +  "</cell>");
				//entityMapRecordXML.append("<cell>" + entityMapRecord.getId() +  "</cell>");
				entityMapRecordXML.append("<cell>" + dynamicEntity.getValue()+"^"+strURLForEditRecord+"</cell>");
				entityMapRecordXML.append("<cell>" + Utility.parseDateToString(entityMapRecord.getCreatedDate(),Constants.TIMESTAMP_PATTERN) + "</cell>");
				entityMapRecordXML.append("<cell>" + entityMapRecord.getCreatedBy() + "</cell>");
				entityMapRecordXML.append("<cell>" + "Edit" +"^"+ strURLForEditRecord+"</cell>");
				entityMapRecordXML.append("</row>");
			}
		}
		return entityMapRecordXML.toString();
	}

	/**
	 * @param request
	 * @param id
	 * @return
	 */
	private String getURLForEditEntityMapRecord(HttpServletRequest request, String containerId, Long dynExtensionEntityRecordId)
	{
		String urlForEditRecord =request.getContextPath() + "/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="+containerId +"&amp;recordId="+dynExtensionEntityRecordId + "^_self";
		return urlForEditRecord;
	}

	/**
	 * @param entityMapId
	 * @return Name Value bean containing entity container id and name
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private NameValueBean getDynamicEntity(Long entityMapId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean dynamicEntity  = null;
		if(entityMapId!=null)
		{
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			EntityMap entityMap  = annotationBizLogic.getEntityMap(entityMapId);
			if(entityMap!=null)
			{
				dynamicEntity = new NameValueBean(entityMap.getContainerId(),getDEContainerName(entityMap.getContainerId()));
			}
		
		}
		return dynamicEntity;
	}

	/**
	 * @param entityId
	 * @return
	 */
	private List<EntityMapRecord> getEntityMapRecords(Long staticEntityId,Long staticEntityRecordId)
	{
		List<EntityMapRecord> entityMapRecords = null;
		if(staticEntityId!=null)
		{
			List entityMapIds = getListOfEntityMapIdsForSE(staticEntityId);
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			entityMapRecords = annotationBizLogic.getEntityMapRecordList(entityMapIds, staticEntityRecordId);
		}
		return entityMapRecords;
	}

	/**
	 * @param staticEntityId
	 * @return
	 */
	private List<Long>  getListOfEntityMapIdsForSE(Long staticEntityId)
	{
		List<Long> entityMapIds = new ArrayList<Long>();
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		List<EntityMap> entityMapsForStaticEntity = annotationBizLogic.getListOfDynamicEntities(staticEntityId);
		if(entityMapsForStaticEntity!=null)
		{
			EntityMap entityMap = null;
			Iterator<EntityMap> iter = entityMapsForStaticEntity.iterator();
			while(iter.hasNext())
			{
				entityMap = iter.next();
				if(entityMap!=null)
				{
					entityMapIds.add(entityMap.getId());
				}
			}
		}
		return entityMapIds;
	}

	/**
	 * @param entityId
	 * @param entityRecordIdForCondition
	 * @param entityIdForCondition
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private List getAnnotationList(String entityId, String entityIdForCondition, String entityRecordIdForCondition) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<NameValueBean> annotationsList = new ArrayList<NameValueBean>();
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		List dynEntitiesList = null;
		if((entityIdForCondition==null)||(entityIdForCondition.trim().equals("")))
		{
			dynEntitiesList = annotationBizLogic.getListOfDynamicEntitiesIds(Utility.toLong(entityId));
		}
		else
		{
			dynEntitiesList = annotationBizLogic.getListOfDynamicEntitiesIds(Utility.toLong(entityId),Utility.toLong(entityIdForCondition),Utility.toLong(entityRecordIdForCondition));
		}
		if(dynEntitiesList!=null)
		{
			Iterator<Long> dynEntitiesIterator = dynEntitiesList.iterator();
			NameValueBean annotationBean = null;
			while(dynEntitiesIterator.hasNext())
			{
				annotationBean = getNameValueBeanForDE(dynEntitiesIterator.next());
				if(annotationBean!=null)
				{
					annotationsList.add(annotationBean);
				}
			}
		}
		return annotationsList;
	}

	/**
	 * @param long1
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private NameValueBean getNameValueBeanForDE(Long deContainerId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean nameValueBean = null;
		String deEntityName = getDEContainerName(deContainerId);
		if((deContainerId!=null)&&(deEntityName!=null))
		{
			nameValueBean = new NameValueBean(deEntityName,deContainerId);
		}
		return nameValueBean;
	}

	/**
	 * @param deContainerId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private String getDEContainerName(Long deContainerId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String containerName = null;
		if(deContainerId!=null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			ContainerInterface container = DynamicExtensionsUtility.getContainerByIdentifier(deContainerId.toString());
			if(container!=null)
			{
				containerName = container.getCaption();
			}
		}
		return containerName;
	}
}
