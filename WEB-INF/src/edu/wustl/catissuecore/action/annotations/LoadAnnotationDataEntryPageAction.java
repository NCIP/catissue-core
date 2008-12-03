/*
 * @author
 *
 */
package edu.wustl.catissuecore.action.annotations;

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
{

    /* (non-Javadoc)
     * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward executeAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        AnnotationDataEntryForm annotationDataEntryForm = (AnnotationDataEntryForm) form;
        String staticEntityId = null,staticEntityName=null, staticEntityRecordId = null, entityIdForCondition = null, entityRecordIdForCondition = null;
        if (request

                .getParameter(WebUIManager.getOperationStatusParameterName()) != null)
        {
            //Return from dynamic extensions           
            staticEntityId = (String) request.getSession().getAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID);
            String dynEntContainerId = request.getParameter("containerId");
            processResponseFromDynamicExtensions(request, staticEntityId, dynEntContainerId);
            staticEntityRecordId = (String) request.getSession().getAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
            if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
            {
                annotationDataEntryForm
                        .setSelectedStaticEntityId(staticEntityId);
            }
            if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
            {
                annotationDataEntryForm
                        .setSelectedStaticEntityRecordId(staticEntityRecordId);
            }
            staticEntityName = request.getParameter(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

            if(staticEntityName != null )
                request.getSession().setAttribute(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,staticEntityName);

            if(staticEntityName == null )
                staticEntityName=(String)request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

            entityIdForCondition = (String) request.getSession().getAttribute(AnnotationConstants.ENTITY_ID_IN_CONDITION);
            entityRecordIdForCondition = (String) request.getSession().getAttribute(AnnotationConstants.ENTITY_RECORDID_IN_CONDITION);
        }
        else
        {
            staticEntityName =(String) request.getParameter(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

            if(staticEntityName != null )
                request.getSession().setAttribute(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,staticEntityName);

            if(staticEntityName == null )
                staticEntityName=(String)request.getSession().getAttribute(edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME);

            staticEntityId = request
                    .getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
            staticEntityRecordId = request
                    .getParameter(AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID);
            if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
            {
                annotationDataEntryForm
                        .setSelectedStaticEntityId(staticEntityId);
            }
            if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
            {
                annotationDataEntryForm
                        .setSelectedStaticEntityRecordId(staticEntityRecordId);
            }
            if (annotationDataEntryForm.getOperation() != null
                    && annotationDataEntryForm.getOperation().equalsIgnoreCase(
                            "deleteRecords"))
            {
                staticEntityId = annotationDataEntryForm
                        .getSelectedStaticEntityId();
                staticEntityRecordId = annotationDataEntryForm
                        .getSelectedStaticEntityRecordId();
                deleteRecords(annotationDataEntryForm.getSelectedRecords());

            }

            entityIdForCondition = request
                    .getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_ID);
            entityRecordIdForCondition = request
                    .getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_RECORD_ID);
            updateCache(request);
        }
        Logger.out.info("Updating for Entity Id " + staticEntityId);
        initializeDataEntryForm(request, staticEntityId, staticEntityRecordId,
                entityIdForCondition, entityRecordIdForCondition,staticEntityName,
                annotationDataEntryForm);
        String pageOf = request.getParameter(Constants.PAGEOF);
        return mapping.findForward(Constants.SUCCESS);
    }

    /**
     *
     * @param selectedRecords
     */
    private void deleteRecords(String selectedRecords)
    {
        String[] recordArray = selectedRecords.split(",");
        AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
        List<Long> list = new ArrayList(recordArray.length);
        for (String record : recordArray)
        {
        	 Object object = null;
            try
            {
                object = annotationBizLogic.retrieve(EntityMapRecord.class.getName(), new Long(record));

                if (object != null )
                {
                    EntityMapRecord entityMapRecord = (EntityMapRecord) object;
                            
                    Object entityMapObject = annotationBizLogic.retrieve(
                            EntityMap.class.getName(), entityMapRecord.getFormContext().getEntityMap().getId());
                    EntityMap entityMap = entityMapObject == null
                            ? null
                            : (EntityMap) entityMapObject;
                    if (entityMap != null)
                    {
                        List recordList = new ArrayList();
                        recordList.add(entityMapRecord
                                .getDynamicEntityRecordId());
                        annotationBizLogic.deleteAnnotationRecords(entityMap
                                .getContainerId(), recordList);
                        annotationBizLogic
                                .delete(
                                        entityMapRecord,
                                        edu.wustl.catissuecore.util.global.Constants.HIBERNATE_DAO);
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * @param request
     * @throws CacheException
     */
    private void updateCache(HttpServletRequest request) throws CacheException
    {
        String parentEntityId = request
                .getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
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
        
        if (session  != null)
        {
        	session.setAttribute(
                    AnnotationConstants.SELECTED_STATIC_ENTITYID,
                    parentEntityId);
        	session.setAttribute(
                    AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID,
                    parentEntityRecordId);
        	session.setAttribute(
                    AnnotationConstants.ENTITY_ID_IN_CONDITION,
                    entityIdForCondition);
        	session.setAttribute(
                    AnnotationConstants.ENTITY_RECORDID_IN_CONDITION,
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
     * @throws CacheException
     * @throws BizLogicException
     */
    private void processResponseFromDynamicExtensions(HttpServletRequest request, String staticEntityId, String dynEntContainerId) throws CacheException, BizLogicException
    {
        String operationStatus = request.getParameter(WebUIManager
                .getOperationStatusParameterName());
        if ((operationStatus != null)
                && (operationStatus.trim()
                        .equals(WebUIManagerConstants.SUCCESS)))
        {
            String dynExtRecordId = request.getParameter(WebUIManager
                    .getRecordIdentifierParameterName());
            Logger.out
                    .info("Dynamic Entity Record Id [" + dynExtRecordId + "]");
            insertEntityMapRecord(request, dynExtRecordId, staticEntityId, dynEntContainerId);
        }
    }

    /**
     * @param request
     * @param dynExtRecordId
     * @throws CacheException
     * @throws BizLogicException
     */
    private void insertEntityMapRecord(HttpServletRequest request,
            String dynExtRecordId, String staticEntityId, String dynEntContainerId) throws CacheException, BizLogicException
    {
        EntityMapRecord entityMapRecord = getEntityMapRecord(request,
                dynExtRecordId, staticEntityId, dynEntContainerId);
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
                e.printStackTrace();
            }
        }
        else
        {//Edit
        	AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();            
            annotationBizLogic.updateEntityRecord(entityMapRecord);           
        }
    }

    /**
     * @param request
     * @param dynExtRecordId
     * @return
     * @throws CacheException
     */
    private EntityMapRecord getEntityMapRecord(HttpServletRequest request,
            String dynExtRecordId, String staticEntityId, String dynEntContainerId) throws CacheException
    {
        EntityMapRecord entityMapRecord = null;
        String staticEntityRecordId = (String) request.getSession().getAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
        Long entityMapId = (Long) request.getSession().getAttribute(AnnotationConstants.SELECTED_ENTITY_MAP_ID);
        if ((entityMapId != null) && (staticEntityRecordId != null)
                && (dynExtRecordId != null) && !(dynExtRecordId.equals("")))
        {
            entityMapRecord = new EntityMapRecord();
            //entityMapRecord.setEntityMapId(entityMapId);
            FormContext formContext = getFormContext(entityMapId);
            entityMapRecord.setFormContext(formContext);
            entityMapRecord.setStaticEntityRecordId(Utility.toLong(staticEntityRecordId));
            entityMapRecord.setDynamicEntityRecordId(Utility.toLong(dynExtRecordId));
            SessionDataBean sessionDataBean = (SessionDataBean) request
                    .getSession().getAttribute(Constants.SESSION_DATA);
            if (sessionDataBean != null)
            {
                entityMapRecord.setCreatedBy(sessionDataBean.getLastName()
                        + "," + sessionDataBean.getFirstName());
                entityMapRecord.setCreatedDate(new Date());
            }
            entityMapRecord.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
        }
        else
        {
        	if(dynEntContainerId != null && !dynEntContainerId.equals(""))
        	{
	        	AnnotationBizLogic bizLogic = new AnnotationBizLogic();
	        	Collection<EntityMap> entityMapColl = new HashSet<EntityMap>();
				try {
					entityMapColl = bizLogic.getEntityMapsForContainer(new Long(dynEntContainerId));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	for(EntityMap entityMap : entityMapColl)
	        	{
	        		FormContext formContext = getFormContext(entityMap.getId());
	        		Collection<EntityMapRecord> recordColl = formContext.getEntityMapRecordCollection();
	        		for(EntityMapRecord eMR : recordColl)
	        		{
	        			if(eMR.getDynamicEntityRecordId().longValue() == Utility.toLong(dynExtRecordId).longValue())
	        			{
	        				 SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
			                 if (sessionDataBean != null)
			                 {
			                	 eMR.setCreatedBy(sessionDataBean.getLastName() + "," + sessionDataBean.getFirstName());
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
     *
     * @param entityMapId
     * @return
     */
    private FormContext getFormContext( Long entityMapId )
    {
        AnnotationBizLogic bizLogic = new AnnotationBizLogic();
        Object object = bizLogic.getEntityMap(entityMapId);
        if(object != null)
        {
            EntityMap entityMap=(EntityMap) object;
            Iterator formIt= entityMap.getFormContextCollection().iterator();
            while(formIt.hasNext())
            {
                    FormContext formContext=(FormContext)  formIt.next();
                    if((formContext.getNoOfEntries() == null || formContext.getNoOfEntries().equals(""))&&(formContext.getStudyFormLabel() == null || formContext.getStudyFormLabel().equals("")))
                        return formContext;
            }
        }
        return null;
    }

    /**
     * It sets the data entry grid for edit
     * @param request
     * @param entityRecordIdForCondition
     * @param entityIdForCondition
     * @param parentEntityId
     * @param annotationDataEntryForm
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private void initializeDataEntryForm(HttpServletRequest request,
            String staticEntityId, String staticEntityRecordId,
            String entityIdForCondition, String entityRecordIdForCondition,String staticEntityName,
            AnnotationDataEntryForm annotationDataEntryForm)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        //Set annotations list
        if (staticEntityId != null)
        {
            List annotationList = getAnnotationList(staticEntityId,
                    entityIdForCondition, entityRecordIdForCondition,staticEntityName,staticEntityRecordId);
            annotationDataEntryForm.setAnnotationsList(annotationList);
            annotationDataEntryForm.setParentEntityId(staticEntityId);
        }

        //Set defined annotations information
        String definedAnnotationsDataXML = getDefinedAnnotationsDataXML(
                request, Utility.toLong(staticEntityId), Utility.toLong(staticEntityRecordId));
        annotationDataEntryForm
                .setDefinedAnnotationsDataXML(definedAnnotationsDataXML);
    }

    /**
     * @param request
     * @param entityId
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private String getDefinedAnnotationsDataXML(HttpServletRequest request,
            Long staticEntityId, Long staticEntityRecordId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        StringBuffer definedAnnotationsXML = new StringBuffer();
        //"<?xml version='1.0' encoding='UTF-8'?><rows><row id='1' class='formField'><cell>0</cell><cell>001</cell><cell>Preeti</cell><cell>12-2-1990</cell><cell>Preeti</cell></row></rows>";
        definedAnnotationsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
        List dataList = new ArrayList();
        if (staticEntityId != null)
        {
            List<EntityMapRecord> entityMapRecords = getEntityMapRecords(
                    staticEntityId, staticEntityRecordId);
            if (entityMapRecords != null)
            {
                definedAnnotationsXML.append("<rows>");
                Iterator<EntityMapRecord> iterator = entityMapRecords
                        .iterator();
                EntityMapRecord entityMapRecord = null;
                int index = 1;
                while (iterator.hasNext())
                {
                    List innerList = new ArrayList();
                    entityMapRecord = iterator.next();
                    definedAnnotationsXML.append(getXMLForEntityMapRecord(
                            request, entityMapRecord, index++,innerList));
                    dataList.add(innerList);
                }
                definedAnnotationsXML.append("</rows>");
            }
            request.setAttribute(edu.wustl.catissuecore.util.global.Constants.SPREADSHEET_DATA_RECORD,dataList);
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
    private String getXMLForEntityMapRecord(HttpServletRequest request,
            EntityMapRecord entityMapRecord, int index,List innerList)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {

        StringBuffer entityMapRecordXML = new StringBuffer();
        if (entityMapRecord != null)
        {
            NameValueBean dynamicEntity = getDynamicEntity(entityMapRecord.getFormContext().getEntityMap().getId());

            if (dynamicEntity != null)
            {
                String strURLForEditRecord = getURLForEditEntityMapRecord(
                        request, dynamicEntity.getName(), entityMapRecord
                                );
                entityMapRecordXML.append("<row id='"
                        + entityMapRecord.getId().toString() + "' >");
                //entityMapRecordXML.append("<cell>" + "0" + "</cell>");
                //innerList.add("0");
                //entityMapRecordXML.append("<cell>" + entityMapRecord.getId() +  "</cell>");
                entityMapRecordXML.append("<cell>" + dynamicEntity.getValue()
                        + "^" + strURLForEditRecord + "</cell>");
                innerList.add(makeURL(dynamicEntity.getValue(),strURLForEditRecord));
                entityMapRecordXML.append("<cell>"
                        + Utility.parseDateToString(entityMapRecord
                                .getCreatedDate(),
                                Constants.DATE_PATTERN_MM_DD_YYYY) + "</cell>");
                innerList.add(Utility.parseDateToString(entityMapRecord
                        .getCreatedDate(),
                        Constants.DATE_PATTERN_MM_DD_YYYY));
                String creator = entityMapRecord.getCreatedBy();
                if(creator==null || creator.equals("null"))
                    creator ="";
                entityMapRecordXML.append("<cell>"
                        + creator + "</cell>");
                innerList.add(creator);
                entityMapRecordXML.append("<cell>" + "Edit" + "^"
                        + strURLForEditRecord + "</cell>");
                innerList.add(entityMapRecord.getId().toString());
                entityMapRecordXML.append("</row>");
            }
        }
        return entityMapRecordXML.toString();
    }

    private String makeURL(String containercaption, String dynExtentionsEditEntityURL)
    {
        String url="";
        url="<a  href="+"'"+dynExtentionsEditEntityURL+"'>"+containercaption+"</a>";
        return url;
    }

    /**
     * @param request
     * @param id
     * @return
     */
    private String getURLForEditEntityMapRecord(HttpServletRequest request,
            String containerId, EntityMapRecord entityMapRecord)
    {
        String urlForEditRecord = "";
        try
        {
            EntityMap entityMap = (EntityMap)(new AnnotationBizLogic().retrieve(EntityMap.class.getName(), entityMapRecord.getFormContext().getEntityMap().getId()));


        urlForEditRecord = request.getContextPath()
                + "/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="
                + containerId + "&amp;recordId=" + entityMapRecord.getDynamicEntityRecordId()+"&amp;selectedStaticEntityId="+ entityMap.getStaticEntityId()+
                "&amp;selectedStaticEntityRecordId="+entityMapRecord.getStaticEntityRecordId() ;//+"_self";//"^_self";
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return urlForEditRecord;

    }

    /**
     * @param entityMapId
     * @return Name Value bean containing entity container id and name
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private NameValueBean getDynamicEntity(Long entityMapId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
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
                    dynamicEntity = new NameValueBean(entityMap
                            .getContainerId(), getDEContainerName(entityMap
                            .getContainerId()));
                }
            }
        }
        return dynamicEntity;
    }

    /**
     * @param entityId
     * @return
     */
    private List<EntityMapRecord> getEntityMapRecords(Long staticEntityId,
            Long staticEntityRecordId)
    {
        List<EntityMapRecord> entityMapRecords = null;
        if (staticEntityId != null)
        {
            List entityMapIds = getListOfEntityMapIdsForSE(staticEntityId);
            AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
            entityMapRecords = annotationBizLogic.getEntityMapRecordList(
                    entityMapIds, staticEntityRecordId);
        }
        return entityMapRecords;
    }

    /**
     * @param staticEntityId
     * @return
     */
    private List<Long> getListOfEntityMapIdsForSE(Long staticEntityId)
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
     * @param entityId
     * @param entityRecordIdForCondition
     * @param entityIdForCondition
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private List getAnnotationList(String entityId,
            String entityIdForCondition, String entityRecordIdForCondition,String staticEntityName,String staticEntityRecordId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        List<NameValueBean> annotationsList = new ArrayList<NameValueBean>();
        AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
        List dynEntitiesList = null;
        List allEntitiesList = new ArrayList();
        List cpIdList = new ArrayList();
        List allCategoryEntityList =  new ArrayList();
        if(staticEntityName != null && staticEntityRecordId != null )
        {
            ICPCondition annoCondn=getConditionInvoker(staticEntityName,staticEntityRecordId);
            if(annoCondn!=null)
                cpIdList=annoCondn.getCollectionProtocolList( new Long(staticEntityRecordId));
        }
       /* if(cpIdList==null || cpIdList.isEmpty())
            dynEntitiesList=  annotationBizLogic.getListOfDynamicEntitiesIds(Utility.toLong(entityId));
        else*/
        {
            dynEntitiesList = annotationBizLogic.getListOfDynamicEntities(Utility.toLong(entityId));
            dynEntitiesList = annotationBizLogic.getAnnotationIdsBasedOnCondition(dynEntitiesList,cpIdList);
            allEntitiesList = checkForAbstractEntity(dynEntitiesList);
	        allCategoryEntityList = checkForAbstractCategoryEntity(dynEntitiesList);
           
        }
      //  getConditionalDEId(dynEntitiesList,cpIdList);
        if (allEntitiesList != null)
        {
            Iterator<Long> dynEntitiesIterator = allEntitiesList.iterator();
           NameValueBean annotationBean = null;
            while (dynEntitiesIterator.hasNext())
            {
                annotationBean = getNameValueBeanForDE(dynEntitiesIterator
                        .next());
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
                annotationBean = getNameValueBeanForCategoryEntity(dynEntitiesIterator
                        .next());
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
    * @param dynEntitiesList
    * @throws DynamicExtensionsApplicationException
    * @throws DynamicExtensionsSystemException
    */
   private List checkForAbstractEntity(List dynEntitiesList)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
	   
        List entitesList = new ArrayList();
        if (dynEntitiesList != null)
        {
            Iterator<Long> dynEntitiesIterator = dynEntitiesList.iterator();
            EntityManagerInterface entityManager = EntityManager.getInstance();
            while (dynEntitiesIterator.hasNext())
            {
                Long deContainerId=dynEntitiesIterator.next();
                deContainerId =entityManager.checkContainerForAbstractEntity(deContainerId,false);
                if(deContainerId != null)
                      entitesList.add(deContainerId);
            }
        }
        return entitesList;
    }
	/**
	 * @param dynEntitiesList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
   	private List checkForAbstractCategoryEntity(List dynEntitiesList)
   			throws DynamicExtensionsSystemException,
   			DynamicExtensionsApplicationException
   	{
   	   	List entitesList = new ArrayList();
	   	if (dynEntitiesList != null)
	   	{
	   		Iterator<Long> dynEntitiesIterator = dynEntitiesList.iterator();
	   		EntityManagerInterface entityManager = EntityManager.getInstance();
	   		while (dynEntitiesIterator.hasNext())
	   		{
	   			Long deContainerId=dynEntitiesIterator.next();
	   			deContainerId =entityManager.checkContainerForAbstractCategoryEntity(deContainerId);
	   			if(deContainerId != null)
	   				entitesList.add(deContainerId);
	   		}
	   	}
	   	return entitesList;
   	}


    /**
     * @param long1
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private NameValueBean getNameValueBeanForCategoryEntity(Long deContainerId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        NameValueBean nameValueBean = null;
        String deEntityName;
		try 
		{
			deEntityName = getDEContainerCategoryName(deContainerId);
		} 
		catch (DAOException e) {
			
			throw new DynamicExtensionsApplicationException(e.getMessage());
		}
        if ((deContainerId != null) && (deEntityName != null))
        {
        	deEntityName = "Form--"+deEntityName;
            nameValueBean = new NameValueBean(deEntityName, deContainerId);
        }
        return nameValueBean;
    }
    /**
     * @param deContainerId
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    private NameValueBean getNameValueBeanForDE(Long deContainerId)
    throws DynamicExtensionsSystemException,
    DynamicExtensionsApplicationException
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
     * @param deContainerId
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    private String getDEContainerName(Long deContainerId)
            throws DynamicExtensionsSystemException,
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
     * @param deContainerId
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     * @throws DAOException 
     */
    private String getDEContainerCategoryName(Long deContainerId)
    		throws DynamicExtensionsSystemException,
    		DynamicExtensionsApplicationException, DAOException
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
     * @param staticEntityName
     * @param entityInstanceId
     * @return
     */
    private ICPCondition getConditionInvoker(String staticEntityName,String entityInstanceId)
    {
        //read StaticInformation.xml and get ConditionInvoker
        try
        {
           AnnotationUtil util = new AnnotationUtil();

           util.populateStaticEntityList("StaticEntityInformation.xml",
                            staticEntityName);
            String conditionInvoker = (String) util.map.get("conditionInvoker");

            ICPCondition annoCondn =(ICPCondition) Class.forName(conditionInvoker).newInstance();
         /*
            Class[] parameterTypes = new Class[]{Long.class};
            Object[] parameterValues = new Object[]{new Long(entityInstanceId)};

            Method getBizLogicMethod = annotaionConditionClass.getMethod("getCollectionProtocolList", parameterTypes);
            annoCondn = (ICPCondition)getBizLogicMethod.invoke(annotaionConditionClass,parameterValues);*/
               return annoCondn;

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
        return null;

    }
}
