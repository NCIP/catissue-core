/*
 * @author
 *
 */
package edu.wustl.catissuecore.action.annotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import edu.common.dynamicextensions.domain.integration.EntityMapRecord;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.entitymanager.DynamicExtensionsQueryBuilderConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.annotations.ICPCondition;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 *
 * Load data for AnnotationDataEntryPage.jsp.
 * Loads data of linked dynamic entities and also annotation data that has been added
 * Also processes response from DE data entry action and stores relative data to DB
 */
public class LoadAnnotationDataEntryPageAction extends BaseAction
		implements
			DynamicExtensionsQueryBuilderConstantsInterface
{

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionfwd = null;
		AnnotationDataEntryForm annotationDataEntryForm = (AnnotationDataEntryForm) form;
		String staticEntityId = null, staticEntityName = null, staticEntityRecordId = null;

		String operation = request.getParameter(AnnotationConstants.OPERATION);
		if (operation != null && !operation.equals(AnnotationConstants.NULL)
				&& (operation.equalsIgnoreCase(AnnotationConstants.EDIT_SELECTED_ANNOTATION)))
		{
			//If operation not null -> Operation		
			String selectedFormId = null;

			selectedFormId = request.getParameter(AnnotationConstants.SELECTED_ANNOTATION);
			staticEntityId = request.getParameter(AnnotationConstants.STATIC_ENTITY_ID);
			staticEntityRecordId = request
					.getParameter(AnnotationConstants.STATIC_ENTITY_RECORD_ID);

			String definedAnnotationsDataXML = processEditOperation(request, Utility.toLong(staticEntityId), Utility
					.toLong(staticEntityRecordId), selectedFormId);
			annotationDataEntryForm.setDefinedAnnotationsDataXML(definedAnnotationsDataXML);

			actionfwd = mapping.findForward(AnnotationConstants.PAGE_OF_EDIT_ANNOTATION);
		}
		else
		{
			if (request.getParameter(WebUIManager.getOperationStatusParameterName()) != null)
			{
				//Return from dynamic extensions           
				staticEntityId = (String) request.getSession().getAttribute(
						AnnotationConstants.SELECTED_STATIC_ENTITYID);
				String dynEntContainerId = request.getParameter("containerId");
				processResponseFromDynamicExtensions(request, dynEntContainerId);
				staticEntityRecordId = (String) request.getSession().getAttribute(
						AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
				if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityId(staticEntityId);
				}
				if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityRecordId(staticEntityRecordId);
				}
				staticEntityName = request
						.getParameter(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

				if (staticEntityName != null)
				{
					request.getSession().setAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,
							staticEntityName);
				}
				if (staticEntityName == null)
				{
					staticEntityName = (String) request.getSession().getAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);
				}
			}
			else
			{
				staticEntityName = (String) request
						.getParameter(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

				if (staticEntityName != null)
				{
					request.getSession().setAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,
							staticEntityName);
				}
				if (staticEntityName == null)
				{
					staticEntityName = (String) request.getSession().getAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);
				}
				staticEntityId = request.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
				staticEntityRecordId = request
						.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID);
				if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityId(staticEntityId);
				}
				if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityRecordId(staticEntityRecordId);
				}
				if (annotationDataEntryForm.getOperation() != null
						&& annotationDataEntryForm.getOperation().equalsIgnoreCase("deleteRecords"))
				{
					staticEntityId = annotationDataEntryForm.getSelectedStaticEntityId();
					staticEntityRecordId = annotationDataEntryForm
							.getSelectedStaticEntityRecordId();
					deleteRecords(annotationDataEntryForm.getSelectedRecords());

				}
				updateCache(request);
			}
			String editOperation=request.getParameter("editOperation");
			if(editOperation!=null && editOperation.trim().length()>0 && (editOperation.contains(AnnotationConstants.EDIT_SELECTED_ANNOTATION)))
			{
				String[] parameters = editOperation.split("@");
				String selectedFormId = (parameters[1].split("="))[1].toString();
				String definedAnnotationsDataXML = processEditOperation(request, Utility.toLong(staticEntityId), Utility
						.toLong(staticEntityRecordId), selectedFormId);
				annotationDataEntryForm.setDefinedAnnotationsDataXML(definedAnnotationsDataXML);
				actionfwd = mapping.findForward(AnnotationConstants.PAGE_OF_EDIT_ANNOTATION);
			}
			else
			{
				Logger.out.info("Updating for Entity Id " + staticEntityId);
				initializeDataEntryForm(request, staticEntityId, staticEntityRecordId,
					staticEntityName,
					annotationDataEntryForm);
//				String pageOf = request.getParameter(Constants.PAGEOF);
				actionfwd = mapping.findForward(Constants.SUCCESS);
			}
		}
		return actionfwd;
	}

	/**
	 * @param selectedRecords
	 */
	private void deleteRecords(String selectedRecords)
	{
		String[] recordArray = selectedRecords.split(",");
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		for (String record : recordArray)
		{
			Object object = null;
			try
			{
				object = annotationBizLogic.retrieve(EntityMapRecord.class.getName(), Long.valueOf(
						record));

				if (object != null)
				{
					EntityMapRecord entityMapRecord = (EntityMapRecord) object;

					Object entityMapObject = annotationBizLogic.retrieve(EntityMap.class.getName(),
							entityMapRecord.getFormContext().getEntityMap().getId());
					EntityMap entityMap = entityMapObject == null
							? null
							: (EntityMap) entityMapObject;
					if (entityMap != null)
					{
						List recordList = new ArrayList();
						recordList.add(entityMapRecord.getDynamicEntityRecordId());
						annotationBizLogic.deleteAnnotationRecords(entityMap.getContainerId(),
								recordList);
						annotationBizLogic.delete(entityMapRecord,
								edu.wustl.catissuecore.util.global.Constants.HIBERNATE_DAO);
					}
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				Logger.out.error(e.getMessage(), e);
			}
		}

	}

	/**
	 * @param request
	 * @throws CacheException fails to retrieve object from cache
	 */
	private void updateCache(HttpServletRequest request) throws CacheException
	{
		String parentEntityId = request.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
		String parentEntityRecordId = request
				.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID);
		String entityIdForCondition = request
				.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_ID);
		String entityRecordIdForCondition = request
				.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_RECORD_ID);
		//Set into Cache
		//        CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
		//                .getInstance();
		HttpSession session = request.getSession();

		if (session != null)
		{
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID, parentEntityId);
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID,
					parentEntityRecordId);
			session.setAttribute(AnnotationConstants.ENTITY_ID_IN_CONDITION, entityIdForCondition);
			session.setAttribute(AnnotationConstants.ENTITY_RECORDID_IN_CONDITION,
					entityRecordIdForCondition);
		}
	}

	//    /**
	//     * @param selected_static_entityid
	//     * @return
	//     * @throws CacheException
	//     */
	//    private Object getObjectFromCache(String key) throws CacheException
	//    {
	//        if (key != null)
	//        {
	//            CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
	//                    .getInstance();
	//            if (cacheManager != null)
	//            {
	//                return cacheManager.getObjectFromCache(key);
	//            }
	//        }
	//        return null;
	//    }

	/**
	 * @param request
	 * @param dynEntContainerId Dynamic Extensions Container Id
	 * @throws CacheException fails to retrieve object from cache
	 * @throws BizLogicException fails to retrieve bizLogic Object
	 * @throws DynamicExtensionsApplicationException 
	 */
	private void processResponseFromDynamicExtensions(HttpServletRequest request,
			String dynEntContainerId) throws CacheException,
			BizLogicException, DynamicExtensionsSystemException
	{
		String operationStatus = request.getParameter(WebUIManager
				.getOperationStatusParameterName());
		if ((operationStatus != null)
				&& (operationStatus.trim().equals(WebUIManagerConstants.SUCCESS)))
		{
			String dynExtRecordId = request.getParameter(WebUIManager
					.getRecordIdentifierParameterName());
			Logger.out.info("Dynamic Entity Record Id [" + dynExtRecordId + "]");
			insertEntityMapRecord(request, dynExtRecordId, dynEntContainerId);
		}
	}

	/**
	 * @param request 
	 * @param dynExtRecordId Dynamic Extension record Id
	 * @param dynEntContainerId Dynamic Extension Container Id
	 * @throws CacheException fails to retrieve object from cache
	 * @throws BizLogicException fails to retrieve bizLogic Object
	 * @throws DynamicExtensionsApplicationException 
	 */
	private void insertEntityMapRecord(HttpServletRequest request, String dynExtRecordId,
		String dynEntContainerId) throws CacheException,
			BizLogicException, DynamicExtensionsSystemException
	{
		EntityMapRecord entityMapRecord = getEntityMapRecord(request, dynExtRecordId,
				dynEntContainerId);
		if (entityMapRecord != null && entityMapRecord.getId() == null)
		{
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			try
			{
				annotationBizLogic.insertEntityRecord(entityMapRecord);
			}
			catch (DAOException e)
			{
				// TODO ERROR HANDLING STILL REMAINING
				Logger.out.debug("Got exception while creating entity map record....");
				Logger.out.error(e.getMessage(), e);
			}
		}
		else
		{//Edit
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			annotationBizLogic.updateEntityRecord(entityMapRecord);
		}
	}

	/**
	 * @param request request object
	 * @param dynExtRecordId Dynamic Extension record Id
	 * @return EntityMapRecord for the Dynamic Extension record Id
	 * @throws CacheException fails to retrieve object from cache
	 * @throws DynamicExtensionsApplicationException 
	 */
	private EntityMapRecord getEntityMapRecord(HttpServletRequest request, String dynExtRecordId,
		String dynEntContainerId) throws CacheException, DynamicExtensionsSystemException
	{
		EntityMapRecord entityMapRecord = null;
		String staticEntityRecordId = (String) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
		Long entityMapId = (Long) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_ENTITY_MAP_ID);
		if ((entityMapId != null) && (staticEntityRecordId != null) && (dynExtRecordId != null)
				&& !(dynExtRecordId.equals("")))
		{
			entityMapRecord = new EntityMapRecord();
			//entityMapRecord.setEntityMapId(entityMapId);
			FormContext formContext = getFormContext(entityMapId);
			entityMapRecord.setFormContext(formContext);
			entityMapRecord.setStaticEntityRecordId(Utility.toLong(staticEntityRecordId));
			entityMapRecord.setDynamicEntityRecordId(Utility.toLong(dynExtRecordId));
			SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			if (sessionDataBean != null)
			{
				entityMapRecord.setCreatedBy(sessionDataBean.getLastName() + ","
						+ sessionDataBean.getFirstName());
				entityMapRecord.setCreatedDate(new Date());
			}
			entityMapRecord.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
		}
		else
		{
			if (dynEntContainerId != null && !dynEntContainerId.equals(""))
			{
				AnnotationBizLogic bizLogic = new AnnotationBizLogic();
				Collection<EntityMap> entityMapColl = new HashSet<EntityMap>();
				try
				{
					entityMapColl = bizLogic.getEntityMapsForContainer(Long.valueOf(dynEntContainerId));
				}
				catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					Logger.out.error(e.getMessage(), e);
				}
				catch (DAOException e)
				{
					// TODO Auto-generated catch block
					Logger.out.error(e.getMessage(), e);
				}
				for (EntityMap entityMap : entityMapColl)
				{					
					FormContext formContext = getFormContext(entityMap.getId());
					Collection<EntityMapRecord> recordColl = Utility.getEntityMapRecords(formContext.getId());
					for (EntityMapRecord eMR : recordColl)
					{
						if (eMR.getDynamicEntityRecordId().longValue() == Utility.toLong(
								dynExtRecordId).longValue())
						{
							SessionDataBean sessionDataBean = (SessionDataBean) request
									.getSession().getAttribute(Constants.SESSION_DATA);
							if (sessionDataBean != null)
							{
								eMR.setCreatedBy(sessionDataBean.getLastName() + ","
										+ sessionDataBean.getFirstName());
								eMR.setCreatedDate(new Date());
								entityMapRecord = eMR;
								break;
							}
						}
					}
					break;
				}
			}
		}
		return entityMapRecord;
	}

	/**
	 * It gives the FormContext Object based on EntityMap
	 * @param entityMapId identifier of entityMap
	 * @return FormContext object for the particular entityMapId
	 * @throws DynamicExtensionsApplicationException 
	 */
	private FormContext getFormContext(Long entityMapId) throws DynamicExtensionsSystemException
	{
		AnnotationBizLogic bizLogic = new AnnotationBizLogic();
		Object object = bizLogic.getEntityMap(entityMapId);
		FormContext formContextObject = null;
		if (object != null)
		{
			EntityMap entityMap = (EntityMap) object;
			Collection<FormContext> formContextMetadata = Utility.getFormContexts(entityMap.getId());

			Iterator<FormContext> formContextIter = formContextMetadata.iterator();
			while (formContextIter.hasNext())
			{
				FormContext formContext = formContextIter.next();
				if ((formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
						.equals(""))
						&& (formContext.getStudyFormLabel() == null || formContext
								.getStudyFormLabel().equals("")))
				{
					formContextObject = formContext;
					break;
				}
			}
		}
		return formContextObject;
	}

	/**
	 * It sets the data entry grid for edit
	 * @param request
	 * @param parentEntityId Entity Id of parent entity
	 * @param annotationDataEntryForm Action form Bean
	 * @throws DynamicExtensionsApplicationException fails to retrieve Annotations
	 * @throws DynamicExtensionsSystemException fails to retrieve Annotations
	 */
	private void initializeDataEntryForm(HttpServletRequest request, String staticEntityId,
			String staticEntityRecordId, String staticEntityName,
			AnnotationDataEntryForm annotationDataEntryForm)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//Set annotations list
		if (staticEntityId != null)
		{
			List annotationList = getAnnotationList(staticEntityId, staticEntityName, staticEntityRecordId);
			annotationDataEntryForm.setAnnotationsList(annotationList);
			annotationDataEntryForm.setParentEntityId(staticEntityId);
			String definedAnnotationXML = getDefinedAnnotationsXML(request, annotationList, Utility
					.toLong(staticEntityId), Utility.toLong(staticEntityRecordId));
			annotationDataEntryForm.setDefinedAnnotationEntitiesXML(definedAnnotationXML);
		}

		//Set defined annotations information
		//		String definedAnnotationsDataXML = getDefinedAnnotationsDataXML(request, Utility
		//				.toLong(staticEntityId), Utility.toLong(staticEntityRecordId));
		String definedAnnotationsDataXML = getDefinedAnnotationsDataXML(request, Utility
				.toLong(staticEntityId), Utility.toLong(staticEntityRecordId));
		annotationDataEntryForm.setDefinedAnnotationsDataXML(definedAnnotationsDataXML);
	}

	/**
	 * @param request
	 * @param staticEntityId Identifier of static Entity
	 * @param staticEntityRecordId Identifier of static Entity record
	 * @return annotations XML
	 * @throws DynamicExtensionsApplicationException fails to get EntityMapRecord
	 * @throws DynamicExtensionsSystemException
	 */
	private String getDefinedAnnotationsDataXML(HttpServletRequest request, Long staticEntityId,
			Long staticEntityRecordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		StringBuffer definedAnnotationsXML = new StringBuffer();
		//"<?xml version='1.0' encoding='UTF-8'?><rows><row id='1' class='formField'><cell>0</cell><cell>001</cell><cell>Preeti</cell><cell>12-2-1990</cell><cell>Preeti</cell></row></rows>";
		definedAnnotationsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		List dataList = new ArrayList();
		if (staticEntityId != null)
		{
			List<EntityMapRecord> entityMapRecords = getEntityMapRecords(staticEntityId,
					staticEntityRecordId);
			if (entityMapRecords != null)
			{
				definedAnnotationsXML.append("<rows>");
				Iterator<EntityMapRecord> iterator = entityMapRecords.iterator();
				EntityMapRecord entityMapRecord = null;
				while (iterator.hasNext())
				{
					List innerList = new ArrayList();
					entityMapRecord = iterator.next();
					definedAnnotationsXML.append(getXMLForEntityMapRecord(request, entityMapRecord,
							innerList));
					dataList.add(innerList);
				}
				definedAnnotationsXML.append("</rows>");
			}
			request.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPREADSHEET_DATA_RECORD, dataList);
		}
		return definedAnnotationsXML.toString();
	}

	/**
	 * @param request
	 * @param entityMapRecord Object of entityMapRecord
	 * @param innerList contains XML nodes
	 * @return XML
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private String getXMLForEntityMapRecord(HttpServletRequest request,
			EntityMapRecord entityMapRecord, List innerList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		StringBuffer entityMapRecordXML = new StringBuffer();
		if (entityMapRecord != null)
		{
			NameValueBean dynamicEntity = getDynamicEntity(entityMapRecord.getFormContext()
					.getEntityMap().getId());

			if (dynamicEntity != null)
			{
				String strURLForEditRecord = getURLForEditEntityMapRecord(request, dynamicEntity
						.getName(), entityMapRecord);
				entityMapRecordXML.append("<row id='" + entityMapRecord.getId().toString() + "' >");
				//entityMapRecordXML.append("<cell>" + "0" + "</cell>");
				//innerList.add("0");
				//entityMapRecordXML.append("<cell>" + entityMapRecord.getId() +  "</cell>");
				entityMapRecordXML.append("<cell>" + dynamicEntity.getValue() + "^"
						+ strURLForEditRecord + "</cell>");
				innerList.add(makeURL(dynamicEntity.getValue(), strURLForEditRecord));
				entityMapRecordXML.append("<cell>"
						+ Utility.parseDateToString(entityMapRecord.getCreatedDate(),
								Constants.DATE_PATTERN_MM_DD_YYYY) + "</cell>");
				innerList.add(Utility.parseDateToString(entityMapRecord.getCreatedDate(),
						Constants.DATE_PATTERN_MM_DD_YYYY));
				String creator = entityMapRecord.getCreatedBy();
				if (creator == null || creator.equals("null"))
				{
					creator = "";
				}
				entityMapRecordXML.append("<cell>" + creator + "</cell>");
				innerList.add(creator);
				entityMapRecordXML
						.append("<cell>" + AnnotationConstants.EDIT + "^" + strURLForEditRecord + "</cell>");
				innerList.add(entityMapRecord.getId().toString());
				entityMapRecordXML.append("</row>");
			}
		}
		return entityMapRecordXML.toString();
	}

	/**
	 * @param containercaption name of container
	 * @param dynExtentionsEditEntityURL URL for editing Dynamic Enxtension data Entry Forms
	 * @return
	 */
	private String makeURL(String containercaption, String dynExtentionsEditEntityURL)
	{
		String url = "";
		url = "<a  href=" + "'" + dynExtentionsEditEntityURL + "'>" + containercaption + "</a>";
		return url;
	}

	/**
	 * @param request
	 * @param containerId identifier of container
	 * @return URL for edit annotation
	 */
	private String getURLForEditEntityMapRecord(HttpServletRequest request, String containerId,
			EntityMapRecord entityMapRecord)
	{
		String urlForEditRecord = "";
		try
		{
			EntityMap entityMap = (EntityMap) (new AnnotationBizLogic().retrieve(EntityMap.class
					.getName(), entityMapRecord.getFormContext().getEntityMap().getId()));

			urlForEditRecord = request.getContextPath()
					+ "/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation=" + containerId
					+ "&amp;recordId=" + entityMapRecord.getDynamicEntityRecordId()
					+ "&amp;selectedStaticEntityId=" + entityMap.getStaticEntityId()
					+ "&amp;selectedStaticEntityRecordId="
					+ entityMapRecord.getStaticEntityRecordId();//+"_self";//"^_self";
		}
		catch (DAOException e)
		{
			Logger.out.error(e.getMessage(), e);
		}
		return urlForEditRecord;

	}

	/**
	 * @param entityMapId
	 * @return Name-Value bean containing entity container id and name
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private NameValueBean getDynamicEntity(Long entityMapId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean dynamicEntity = null;
		if (entityMapId != null)
		{
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			Object object = annotationBizLogic.getEntityMap(entityMapId);
			if (object != null)
			{
				EntityMap entityMap = (EntityMap) object;
				if (entityMap != null)
				{
					dynamicEntity = new NameValueBean(entityMap.getContainerId(),
							getDEContainerName(entityMap.getContainerId()));
				}
			}
		}
		return dynamicEntity;
	}

	/**
	 * @param staticEntityId Identifier of static entity
	 * @param staticEntityRecordId Identifier of static record
	 * @return collection of entityMapRecords
	 * @throws DynamicExtensionsApplicationException 
	 */
	private List<EntityMapRecord> getEntityMapRecords(Long staticEntityId, Long staticEntityRecordId) throws DynamicExtensionsSystemException
	{
		List<EntityMapRecord> entityMapRecords = null;
		if (staticEntityId != null)
		{
			List entityMapIds = getListOfEntityMapIdsForSE(staticEntityId);
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			entityMapRecords = annotationBizLogic.getEntityMapRecordList(entityMapIds,
					staticEntityRecordId);
		}
		return entityMapRecords;
	}

	/**
	 * @param staticEntityId Identifier of static entity
	 * @return collection of EntityMap for static entity
	 * @throws DynamicExtensionsApplicationException 
	 */
	private List<Long> getListOfEntityMapIdsForSE(Long staticEntityId) throws DynamicExtensionsSystemException
	{
		List<Long> entityMapIds = new ArrayList<Long>();
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		List<EntityMap> entityMapsForStaticEntity = annotationBizLogic
				.getListOfDynamicEntities(staticEntityId);
		if (entityMapsForStaticEntity != null)
		{
			EntityMap entityMap = null;
			Iterator<EntityMap> iter = entityMapsForStaticEntity.iterator();
			while (iter.hasNext())
			{
				entityMap = iter.next();
				if (entityMap != null)
				{
					entityMapIds.add(entityMap.getId());
				}
			}
		}
		return entityMapIds;
	}

	/**
	 * @param entityId identifier of entity
	 * @param staticEntityName Name of static entity
	 * @param staticEntityRecordId Identifier of static record
	 * @return list of Annotations
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private List getAnnotationList(String entityId,
			String staticEntityName, String staticEntityRecordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<NameValueBean> annotationsList = new ArrayList<NameValueBean>();
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		List dynEntitiesList = new ArrayList();
		List dynamicEntitiesList = new ArrayList();
		List allEntitiesList = new ArrayList();
		List cpIdList = new ArrayList();
		List allCategoryEntityList = new ArrayList();
		if (staticEntityName != null && staticEntityRecordId != null)
		{
			ICPCondition annoCondn = getConditionInvoker(staticEntityName);
			if (annoCondn != null)
			{
				cpIdList = annoCondn.getCollectionProtocolList(Long.valueOf(staticEntityRecordId));
			}
		}
		/* if(cpIdList==null || cpIdList.isEmpty())
		     dynEntitiesList=  annotationBizLogic.getListOfDynamicEntitiesIds(Utility.toLong(entityId));
		 else*/
//		{
			boolean showForAll=false;
			dynEntitiesList = annotationBizLogic.getListOfDynamicEntities(Utility.toLong(entityId));
			dynamicEntitiesList = annotationBizLogic.getAnnotationIdsBasedOnCondition(dynEntitiesList,
					cpIdList,showForAll);
			if (dynamicEntitiesList == null || dynamicEntitiesList.size()<=0 || dynamicEntitiesList.isEmpty())
			{
				showForAll=true;
				dynamicEntitiesList = annotationBizLogic.getAnnotationIdsBasedOnCondition(dynEntitiesList,
						cpIdList,showForAll);
			}
			allEntitiesList = checkForAbstractEntity(dynamicEntitiesList);
			allCategoryEntityList = checkForAbstractCategoryEntity(dynamicEntitiesList);
//		}
		//  getConditionalDEId(dynEntitiesList,cpIdList);
		if (allEntitiesList != null)
		{
			Iterator<Long> dynEntitiesIterator = allEntitiesList.iterator();
			NameValueBean annotationBean = null;
			while (dynEntitiesIterator.hasNext())
			{
				annotationBean = getNameValueBeanForDE(dynEntitiesIterator.next());
				if (annotationBean != null)
				{
					annotationsList.add(annotationBean);
				}
			}
		}
		if (allCategoryEntityList != null)
		{
			Iterator<Long> dynEntitiesIterator = allCategoryEntityList.iterator();
			NameValueBean annotationBean = null;
			while (dynEntitiesIterator.hasNext())
			{
				annotationBean = getNameValueBeanForCategoryEntity(dynEntitiesIterator.next());
				if (annotationBean != null)
				{
					annotationsList.add(annotationBean);
				}
			}
		}
		return annotationsList;
	}

	/**
	 * check for the abstract entity
	 * @param dynEntitiesList Collection of dynamic entities
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private List checkForAbstractEntity(List dynEntitiesList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		List entitesList = new ArrayList();
		if (dynEntitiesList != null)
		{
			Iterator<Long> dynEntitiesIterator = dynEntitiesList.iterator();
			EntityManagerInterface entityManager = EntityManager.getInstance();
			while (dynEntitiesIterator.hasNext())
			{
				Long deContainerId = dynEntitiesIterator.next();
				deContainerId = entityManager.checkContainerForAbstractEntity(deContainerId, false);
				if (deContainerId != null)
				{
					entitesList.add(deContainerId);
				}
			}
		}
		return entitesList;
	}

	/**
	 * @param dynEntitiesList Collection if dynamic entities
	 * @return collection of entities
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private List checkForAbstractCategoryEntity(List dynEntitiesList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List entitesList = new ArrayList();
		if (dynEntitiesList != null)
		{
			Iterator<Long> dynEntitiesIterator = dynEntitiesList.iterator();
			EntityManagerInterface entityManager = EntityManager.getInstance();
			while (dynEntitiesIterator.hasNext())
			{
				Long deContainerId = dynEntitiesIterator.next();
				deContainerId = entityManager
						.checkContainerForAbstractCategoryEntity(deContainerId);
				if (deContainerId != null)
				{
					entitesList.add(deContainerId);
				}
			}
		}
		return entitesList;
	}

	/**
	 * @param deContainerId Identifier of container
	 * @return bean for category Entity
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private NameValueBean getNameValueBeanForCategoryEntity(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean nameValueBean = null;
		String deEntityName = null;
		try
		{
			deEntityName = getDEContainerCategoryName(deContainerId);
		}
		catch (DAOException e)
		{

			Logger.out.error(e.getMessage(), e);
		}
		if ((deContainerId != null) && (deEntityName != null))
		{
			deEntityName = "Form--" + deEntityName;
			nameValueBean = new NameValueBean(deEntityName, deContainerId);
		}
		return nameValueBean;
	}

	/**
	 * @param deContainerId Identifier of container
	 * @return bean for dynamic entity
	 * @throws DynamicExtensionsSystemException fails to get container name for dynamic entity
	 * @throws DynamicExtensionsApplicationException
	 */
	private NameValueBean getNameValueBeanForDE(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean nameValueBean = null;
		String deEntityName = getDEContainerName(deContainerId);
		if ((deContainerId != null) && (deEntityName != null))
		{
			nameValueBean = new NameValueBean(deEntityName, deContainerId);
		}
		return nameValueBean;
	}

	/**
	 * @param deContainerId Identifier of container
	 * @return container name for dynamic entity
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private String getDEContainerName(Long deContainerId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		String containerName = null;
		if (deContainerId != null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			containerName = entityManager.getContainerCaption(deContainerId);
		}
		return containerName;
	}

	/**
	 * @param deContainerId Identifier of container
	 * @return container name for category entity
	 * @throws DynamicExtensionsSystemException fails to get container
	 * @throws DynamicExtensionsApplicationException
	 * @throws DAOException 
	 */
	private String getDEContainerCategoryName(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			DAOException
	{
		String containerName = null;
		if (deContainerId != null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Long categoryEntityId = entityManager.getEntityIdByContainerId(deContainerId);
			containerName = entityManager.getCategoryCaption(categoryEntityId);
		}
		return containerName;
	}

	/**
	 *
	 * @param staticEntityName Name of static entity
	 * @return ICPCondition object
	 */
	private ICPCondition getConditionInvoker(String staticEntityName)
	{
		//read StaticInformation.xml and get ConditionInvoker
		ICPCondition annoCondn = null;
		try
		{
			AnnotationUtil util = new AnnotationUtil();
			
			util.populateStaticEntityList("StaticEntityInformation.xml", staticEntityName);
			String conditionInvoker = (String) util.map.get("conditionInvoker");

			annoCondn = (ICPCondition) Class.forName(conditionInvoker).newInstance();
			/*
			   Class[] parameterTypes = new Class[]{Long.class};
			   Object[] parameterValues = new Object[]{new Long(entityInstanceId)};

			   Method getBizLogicMethod = annotaionConditionClass.getMethod("getCollectionProtocolList", parameterTypes);
			   annoCondn = (ICPCondition)getBizLogicMethod.invoke(annotaionConditionClass,parameterValues);*/
//			   return annoCondn;

		}
		catch (DataTypeFactoryInitializationException e)
		{
			Logger.out.debug(e.getMessage(), e);
		}
		catch (ClassNotFoundException e)
		{
			Logger.out.debug(e.getMessage(), e);
		}
		catch (InstantiationException e)
		{
			Logger.out.debug(e.getMessage(), e);
		}

		catch (IllegalAccessException e)
		{
			Logger.out.debug(e.getMessage(), e);
		}
		return annoCondn;

	}

	/**
	 * @param request object
	 * @param annotationsList list of Annotations
	 * @param staticEntityId identifier of static entity
	 * @param staticEntityRecordId recordId of static entity
	 * @return XML string
	 * @throws DynamicExtensionsSystemException fails to get entityGroup name
	 * @throws DynamicExtensionsApplicationException
	 */
	private String getDefinedAnnotationsXML(HttpServletRequest request, List annotationsList,
			Long staticEntityId, Long staticEntityRecordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		List<Long> annotationIds=new ArrayList<Long>();
		StringBuffer definedAnnotationsXML = new StringBuffer();
		definedAnnotationsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		definedAnnotationsXML.append("<rows>");
		Iterator<NameValueBean> iterator = annotationsList.iterator();
		List dataList = new ArrayList();
		String addURL = null;
		NameValueBean entityBean = null;
		List<EntityMapRecord> entityMapRecords = null;

		if (staticEntityId != null)
		{
			entityMapRecords = getEntityMapRecords(staticEntityId, staticEntityRecordId);
		}
		while (iterator.hasNext())
		{
			List innerList = new ArrayList();
			String entityGroupName = null;
			int numRecord = 0;
			entityBean = iterator.next();
			List<Long> recordIds = null;
			if (entityBean != null)
			{
				String entityName = entityBean.getName();
				Long entityId = Long.parseLong(entityBean.getValue());

				entityGroupName = entityManager.getEntityGroupNameByEntityName(entityName);
				addURL = "<a href='#' name='"
						+ entityId
						+ "' onClick='loadDynExtDataEntryPage(event);' style='cursor:pointer;' id='selectedAnnotation'>Add</a>";

				if (entityGroupName == null)
				{
					entityGroupName = edu.wustl.catissuecore.util.global.Constants.DOUBLE_QUOTES;
				}
				String entName=getFormattedStringForCapitalization(entityName);
				recordIds = getRecordCountForEntity(entityId.toString(), entityMapRecords);
				numRecord = recordIds.size();
				definedAnnotationsXML.append("<row id='" + entityId.toString() + "' >");
				definedAnnotationsXML.append("<cell>" + entityGroupName + "</cell>");
				innerList.add(entityGroupName);
				definedAnnotationsXML.append("<cell>" + entityName + "</cell>");
				innerList.add(entName);
				definedAnnotationsXML.append("<cell>" + numRecord + "</cell>");
				innerList.add(numRecord);
				definedAnnotationsXML.append("<cell>" + "Add" + AnnotationConstants.HTML_SPACE
						+ "Edit" + "</cell>");
				if (numRecord <= 0)
				{
					innerList.add(addURL);
				}
				if (numRecord > 0)
				{
					String editURL = null;
					if (numRecord == 1)
					{
						editURL = "<a href='/catissuecore/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="
								+ entityId
								+ "&selectedStaticEntityId="
								+ staticEntityId
								+ "&selectedStaticEntityRecordId="
								+ staticEntityRecordId
								+ "&recordId=" + recordIds.get(0) + "'>Edit</a>";
					}
					else if (numRecord > 1)
					{
						String onClick = "onClick=editSelectedAnnotation('formId=" + entityId
								+ ":staticEntityId=" + staticEntityId
								+ ":staticEntityRecordId=" + staticEntityRecordId + "'); ";
						editURL = "<a href='#' name='" + entityId + "' " + onClick
								+ " style='cursor:pointer;' id='selectedAnnotation'>Edit</a>";

					}
					String url = addURL + AnnotationConstants.HTML_SPACE + editURL;
					innerList.add(url);
				}
				definedAnnotationsXML.append("</row>");
				annotationIds.add(entityId);
			}
			dataList.add(innerList);
		}
		request.setAttribute(AnnotationConstants.ANNOTATION_LIST_FROM_XML, dataList);
		definedAnnotationsXML.append("</rows>");
		return definedAnnotationsXML.toString();
	}

	/**
	 * get Formatted String For Capitalization.
	 * @param entityName name of entity to get formatted String
	 * @return
	 */
	public static String getFormattedStringForCapitalization(String entityName)
	{
		return Utility.getDisplayLabel(entityName.trim());
	}
	
	/**
	 * @param entityId id of annotation
	 * @param entityMapRecords to get EntityRecord information
	 * @return List of recordIds for entity
	 * @throws DynamicExtensionsSystemException fails to get DynamicEntity based on entityMapRecords
	 * @throws DynamicExtensionsApplicationException fails to get DynamicEntity based on entityMapRecords
	 */
	public List<Long> getRecordCountForEntity(String entityId,
			List<EntityMapRecord> entityMapRecords) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List<Long> recordIds = new ArrayList<Long>();
		if (entityMapRecords != null)
		{
			Iterator<EntityMapRecord> iter = entityMapRecords.iterator();
			EntityMapRecord entityMapRecord = null;
			while (iter.hasNext())
			{
				entityMapRecord = iter.next();
				if (entityMapRecord != null)
				{

					NameValueBean dynamicEntity = getDynamicEntity(entityMapRecord.getFormContext()
							.getEntityMap().getId());
					if (dynamicEntity != null && dynamicEntity.getName().equalsIgnoreCase(entityId) 
											  && !recordIds.contains(entityMapRecord.getDynamicEntityRecordId()))
					{
						recordIds.add(entityMapRecord.getDynamicEntityRecordId());
					}
				}
			}
		}
		return recordIds;
	}

	/**
	 * @param request object for processing request
	 * @param response object to send the response back
	 * @throws IOException fail to get PrintWriter object
	 * @throws DynamicExtensionsSystemException fails to get number of records for a particular entity
	 * @throws DynamicExtensionsApplicationException fails to get number of records for a particular entity
	 */
	private String processEditOperation(HttpServletRequest request, Long staticEntityId, Long staticEntityRecordId, String selectedFormId)
			throws IOException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List<EntityMapRecord> entityMapRecords = null;
		String editURL = null;
		if (staticEntityId != null)
		{
			entityMapRecords = getEntityMapRecords(staticEntityId, staticEntityRecordId);
		}
		List<Long> recordIds = getRecordCountForEntity(selectedFormId, entityMapRecords);

		StringBuffer definedAnnotationDataXML = new StringBuffer();
		definedAnnotationDataXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		definedAnnotationDataXML.append("<rows>");
		List dataList = new ArrayList();

		for (Long recordId : recordIds)
		{
			for (EntityMapRecord entMapRecord : entityMapRecords)
			{
				if (entMapRecord != null && entMapRecord.getDynamicEntityRecordId() == recordId)
				{

					NameValueBean dynamicEntity = getDynamicEntity(entMapRecord
								.getFormContext().getEntityMap().getId());

					if (dynamicEntity != null)
					{
						List innerList = new ArrayList();
						String creator = entMapRecord.getCreatedBy();
						if (creator == null
									|| creator
											.equals(edu.wustl.catissuecore.util.global.Constants.NULL))
						{
							creator = edu.wustl.catissuecore.util.global.Constants.DOUBLE_QUOTES;
						}
						else if (creator != null && creator.contains(","))
						{
							creator = creator.replace(",", "-");
						}
						editURL = "<a href='/catissuecore/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="
									+ selectedFormId
									+ "&selectedStaticEntityId="
									+ staticEntityId
									+ "&selectedStaticEntityRecordId="
									+ staticEntityRecordId
									+ "&recordId="
									+ recordId
									+ "&operation=editSelectedAnnotation"
									+ "'>"
									+ AnnotationConstants.EDIT  + "</a>";
						definedAnnotationDataXML
								.append("<row id='" + recordId.toString() + "' >");
						definedAnnotationDataXML.append("<cell>" + recordId + "</cell>");
						innerList.add(Integer.parseInt(recordId.toString()));
						definedAnnotationDataXML.append("<cell>"
								+ Utility.parseDateToString(entMapRecord.getCreatedDate(),
										Constants.DATE_PATTERN_MM_DD_YYYY) + "</cell>");
						innerList.add(Utility.parseDateToString(entMapRecord.getCreatedDate(),
								Constants.DATE_PATTERN_MM_DD_YYYY));
						definedAnnotationDataXML.append("<cell>" + creator + "</cell>");
						innerList.add(creator);
						definedAnnotationDataXML.append("<cell>" +AnnotationConstants.EDIT + "^" + editURL
								+ "</cell>");
						innerList.add(editURL);
						dataList.add(innerList);
						request.setAttribute(AnnotationConstants.ENTITY_NAME, dynamicEntity.getValue());
						request.setAttribute(AnnotationConstants.REQST_PARAM_ENTITY_ID, dynamicEntity.getName());
					}
				}
			}
		}
		request.setAttribute(AnnotationConstants.STATIC_ENTITY_ID, staticEntityId);
		request.setAttribute(AnnotationConstants.STATIC_ENTITY_RECORD_ID, staticEntityRecordId);
		request.setAttribute(AnnotationConstants.RECORDS_IDs, recordIds);
		definedAnnotationDataXML.append("</row>");
		request.setAttribute(edu.wustl.catissuecore.util.global.Constants.SPREADSHEET_DATA_RECORD, dataList);
		return definedAnnotationDataXML.toString();
	}
}