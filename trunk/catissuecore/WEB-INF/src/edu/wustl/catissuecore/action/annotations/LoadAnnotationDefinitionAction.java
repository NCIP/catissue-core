/*
 * Created on Jan 5, 2007
 * @author
 *
 */

package edu.wustl.catissuecore.action.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.EntityMap;
import edu.wustl.catissuecore.domain.EntityMapCondition;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 *
 */
/**
 * This class is responsible for loading the annotation information
 */
public class LoadAnnotationDefinitionAction extends SecureAction
{

    /* (non-Javadoc)
     * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ActionForward actionfwd = null;
        try
        {
            AnnotationForm annotationForm = (AnnotationForm) form;
            annotationForm.setSelectedStaticEntityId(null);            
            //annotationForm.setConditionVal(null);            
            //Ajax Code
            if (request.getParameter(AnnotationConstants.AJAX_OPERATION) != null)
            {
                //If operation not null -> ajaxOperation
                processAjaxOperation(request, response);
            }
            else if (request.getParameter(WebUIManager
                    .getOperationStatusParameterName()) != null)
            {
                //Return from dynamic extensions
                processResponseFromDynamicExtensions(request);
                loadAnnotations(annotationForm);
                request.setAttribute("operation", "loadIntegrationPage");
                actionfwd = mapping.findForward(Constants.SUCCESS);
            }
            else
            {
                loadAnnotations(annotationForm);
                actionfwd = mapping.findForward(Constants.SUCCESS);
            }

        /*    if (request.getParameter("link") != null
                    && request.getParameter("link").equals(Constants.EDIT))
            {                
                    String XMLstr = getAssociatedDE(annotationForm
                            .getSelectedStaticEntityId());
                    sendResponse(XMLstr, response);
                    annotationForm.setAnnotationGroupsXML(XMLstr);                
                    //actionfwd = mapping.findForward("editAnnotation");
                    actionfwd = mapping.findForward(Constants.SUCCESS);
            }
           */ 

            if (request.getParameter("link") != null
                    && request.getParameter("link").equals("editCondn"))
            {
                request.setAttribute("link",request.getParameter("link"));
                String containerId=request.getParameter("containerId");
                request.setAttribute("containerId",request.getParameter("containerId"));
                if(containerId != null)
                {
                    annotationForm.setSelectedStaticEntityId(request.getParameter("selectedStaticEntityId"));
                    
                    EntityManagerInterface entityManager = EntityManager.getInstance();
                    String  containerCaption = entityManager.getContainerCaption(new Long(containerId));
                    request.setAttribute("containerName",containerCaption);
                    
                    getCPConditions(annotationForm,containerId);
                }
                actionfwd = mapping.findForward(Constants.SUCCESS);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.out.error(e);
        }
        return actionfwd;
    }
    
    private void getCPConditions(AnnotationForm annotationForm,String containerId)
    {
        if(containerId != null)
        {
            List entitymapList=getEntityMapsForDE(new Long(containerId));
            if(entitymapList!= null && !entitymapList.isEmpty())
            {
                int i=0;
                EntityMap entityMap=  new EntityMap();
                entityMap =(EntityMap) entitymapList.get(0);
                Collection entityMapConditionColl = entityMap.getEntityMapConditionCollection();
                Iterator it=  entityMapConditionColl.iterator();
                String[] whereColumnValue = new String[entityMapConditionColl.size()];
                while(it.hasNext())
                {
                    EntityMapCondition  entityMapCondition = (EntityMapCondition) it.next();
                    whereColumnValue[i++]=entityMapCondition.getStaticRecordId().toString();         
                               
                }
                if(whereColumnValue== null || whereColumnValue.length==0)
                    whereColumnValue = new String[]{Constants.ALL};
                 /* DefaultBizLogic bizLogic= new DefaultBizLogic();
                    List conditionalInstancesList = bizLogic.getList(SpecimenProtocol.class
                            .getName(), displayNames, "id",whereColumnName,whereColumnCondition,whereColumnValue,Constants.OR_JOIN_CONDITION, null,false);
                  */
                  annotationForm.setConditionVal(whereColumnValue);
                
                
            }
        }
    }
    
/*
    private String getAssociatedDE(String staticEntityId)
    {
        StringBuffer entitiesXML = new StringBuffer();
        entitiesXML.append("<?xml version='1.0' encoding='UTF-8'?>");
        entitiesXML.append("<rows>");
        if (staticEntityId != null)
        {
            //Long lGroupId = new Long(groupId);
            try
            {

                AnnotationBizLogic bizLogic = new AnnotationBizLogic();
                List dynEntitiesList = null;
                dynEntitiesList = bizLogic
                        .getListOfDynamicEntitiesIds(new Long(staticEntityId));

                if (dynEntitiesList != null)
                {
                    Iterator<Long> dynEntitiesIterator = dynEntitiesList
                            .iterator();
                    int entityIndex = 1;
                    NameValueBean annotationBean = null;
                    
                    while (dynEntitiesIterator.hasNext())
                    {
                        EntityManagerInterface entityManager = EntityManager
                                .getInstance();
                        ContainerInterface container = entityManager
                                .getContainerByIdentifier(dynEntitiesIterator
                                        .next()
                                        + "");
                        if (container != null)
                        {
                            NameValueBean bean = new NameValueBean(container
                                    .getCaption(), container.getId().toString());
                            entitiesXML.append(getEntityXMLString(bean,
                                    entityIndex));
                            entityIndex++;
                        }

                    }
                }
               
            }
            catch (DynamicExtensionsSystemException e)
            {
            }
            catch (DynamicExtensionsApplicationException e)
            {
            }
            catch (DAOException e)
            {
            }
            
            entitiesXML.append("</rows>");
        }
        return entitiesXML.toString();
    }
*/
    /**
     * @param request
     * @throws UserNotAuthorizedException 
     * @throws BizLogicException 
     */
    private void processResponseFromDynamicExtensions(HttpServletRequest request)
            throws BizLogicException, UserNotAuthorizedException
    {
        String operationStatus = request.getParameter(WebUIManager
                .getOperationStatusParameterName());
        if ((operationStatus != null)
                && (operationStatus.trim()
                        .equals(WebUIManagerConstants.SUCCESS)))
        {
            String dynExtContainerId = request.getParameter(WebUIManager
                    .getContainerIdentifierParameterName());
            String staticEntityId = getStaticEntityIdForLinking();

            String[] staticRecordId = getStaticRecordIdForLinking();

            Logger.out.info("Need to link static entity [" + staticEntityId
                    + "] to dyn ent [" + dynExtContainerId + "]");
            linkEntities(request, staticEntityId, dynExtContainerId,
                    staticRecordId);
        }
    }

    /**
     * @param request 
     * @param staticEntityId
     * @param dynExtContainerId
     * @throws UserNotAuthorizedException 
     * @throws BizLogicException 
     */
    private void linkEntities(HttpServletRequest request,
            String staticEntityId, String dynExtContainerId,
            String[] staticRecordIds) throws BizLogicException,
            UserNotAuthorizedException
    {
        if ((staticEntityId != null) && (dynExtContainerId != null))
        {
            EntityMap entityMap = getEntityMap(request, staticEntityId,
                    dynExtContainerId, staticRecordIds);
            AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
            annotationBizLogic.insertEntityMap(entityMap);
        }
    }

    /**
     * @param request 
     * @param staticEntityId
     * @param dynExtContainerId
     * @return
     */
    private EntityMap getEntityMap(HttpServletRequest request,
            String staticEntityId, String dynExtContainerId, String[] conditions)
    {
        AnnotationUtil util = new AnnotationUtil();
        Collection entityMapConditionCollection = new HashSet();

        EntityMap entityMapObj = new EntityMap();
        entityMapObj.setContainerId(Utility.toLong(dynExtContainerId));
        entityMapObj.setStaticEntityId(Utility.toLong(staticEntityId));
        SessionDataBean sessionDataBean = (SessionDataBean) request
                .getSession().getAttribute(Constants.SESSION_DATA);
        if (sessionDataBean != null)
        {
            entityMapObj.setCreatedBy(sessionDataBean.getLastName() + ", "
                    + sessionDataBean.getFirstName());
            entityMapObj.setCreatedDate(new Date());
        }
        entityMapObj.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);
        

        if (conditions != null)
            entityMapConditionCollection =util.getEntityMapConditionsCollection(
                    conditions, entityMapObj);
        entityMapObj
                .setEntityMapConditionCollection(entityMapConditionCollection);

        return entityMapObj;
    }

   
  
    /**
     * @return
     */
    private String getStaticEntityIdForLinking()
    {
        String staticEntityId = null;
        CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
                .getInstance();
        if (cacheManager != null)
        {
            staticEntityId = (String) cacheManager
                    .getObjectFromCache(AnnotationConstants.SELECTED_STATIC_ENTITYID);
            cacheManager
                    .removeObjectFromCache(AnnotationConstants.SELECTED_STATIC_ENTITYID);
        }
        return staticEntityId;
    }

    private String[] getStaticRecordIdForLinking()
    {
        String[] staticRecordId = null;
        CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
                .getInstance();
        if (cacheManager != null)
        {
            staticRecordId = (String[]) cacheManager
                    .getObjectFromCache(AnnotationConstants.SELECTED_STATIC_RECORDID);
            cacheManager
                    .removeObjectFromCache(AnnotationConstants.SELECTED_STATIC_RECORDID);
        }
        return staticRecordId;
    }

    /**
     * @param request
     * @param response
     * @throws IOException 
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     * @throws DAOException 
     */
    private void processAjaxOperation(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException, DAOException
    {
        String operation = request
                .getParameter(AnnotationConstants.AJAX_OPERATION);
        if ((operation != null)
                && (operation
                        .equalsIgnoreCase(AnnotationConstants.AJAX_OPERATION_SELECT_GROUP)))
        {
            String groupId = request
                    .getParameter(AnnotationConstants.AJAX_OPERATION_SELECTED_GROUPID);
            String entitiesXML = getEntitiesForGroupAsXML(groupId);
            sendResponse(entitiesXML, response);
        }
    }

    /**
     * @param containerId 
     * @return
     */
    private String getDynamicExtentionsEditURL(Long containerId)
    {
        //TODO change ths with new api
        String dynamicExtensionsEditEntityURL = "BuildDynamicEntity.do?containerId="
                + containerId + "^_self";
        return dynamicExtensionsEditEntityURL;
    }

    private String getDynamicExtentionsEditCondnURL(Long containerId,Long staticEntityId)
    {
        //TODO change ths with new api
        String dynamicExtensionsEditEntityURL = "DefineAnnotations.do?link=editCondn&amp;containerId="
                + containerId +"&amp;selectedStaticEntityId="+staticEntityId+ "^_self";
        return dynamicExtensionsEditEntityURL;
    }

    /**
     * @param groupId
     * @return
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     * @throws DAOException 
     */
    private String getEntitiesForGroupAsXML(String groupId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException, DAOException
    {
        StringBuffer entitiesXML = new StringBuffer();
        entitiesXML.append("<?xml version='1.0' encoding='UTF-8'?>");
        if (groupId != null)
        {
            Long lGroupId = new Long(groupId);
            Collection<NameValueBean> entityContainerCollection = EntityManager
                    .getInstance().getMainContainer(lGroupId);

            if (entityContainerCollection != null)
            {
                entitiesXML.append("<rows>");
                Iterator<NameValueBean> containerCollnIter = entityContainerCollection
                        .iterator();
                int entityIndex = 1;
                while (containerCollnIter.hasNext())
                {
                    NameValueBean container = containerCollnIter.next();
                    entitiesXML.append(getEntityXMLString(container,
                            entityIndex));
                    entityIndex++;
                }
                entitiesXML.append("</rows>");
            }
        }
        return entitiesXML.toString();
    }

    /**
     * @param container
     * @param entityIndex 
     * @return
     * @throws DAOException 
     * @throws DynamicExtensionsSystemException 
     * @throws DynamicExtensionsApplicationException 
     */
    private String getEntityXMLString(NameValueBean container, int entityIndex)
            throws DAOException, DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        StringBuffer entityXML = new StringBuffer();
        if (container != null)
        {
            int index = 1;
            String editDynExtEntityURL = getDynamicExtentionsEditURL(new Long(
                    container.getValue()));
            
            List<EntityMap> entityMapList = getEntityMapsForDE(new Long(
                    container.getValue()));
            if (entityMapList != null)
            {
                Iterator<EntityMap> entityMapIterator = entityMapList
                        .iterator();
                while (entityMapIterator.hasNext())
                {
                    EntityMap entityMapObj = entityMapIterator.next();
                    String editDynExtCondnURL = getDynamicExtentionsEditCondnURL(new Long(
                            container.getValue()),entityMapObj.getStaticEntityId());
                    
                    entityXML.append(getXMLForEntityMap(container.getName(),
                            entityMapObj, entityIndex + index,
                            editDynExtEntityURL, editDynExtCondnURL));
                    index++;
                }
            }
        }
        return entityXML.toString();
    }

    /**
     * @param caption
     * @param entityMapObj
     * @param dynExtentionsEditEntityURL 
     * @param i
     * @return
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     */
    private StringBuffer getXMLForEntityMap(String containercaption,
            EntityMap entityMapObj, int rowId,
            String dynExtentionsEditEntityURL, String editDynExtCondnURL)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        StringBuffer entityMapXML = new StringBuffer();
        entityMapXML.append("<row id='" + rowId + "'>");
        //entityMapXML.append("<cell>0</cell>");
        entityMapXML.append("<cell>" + containercaption + "^"
                + dynExtentionsEditEntityURL + "</cell>");
        if (entityMapObj != null)
        {
            String staticEntityName = getEntityName(entityMapObj
                    .getStaticEntityId());
            entityMapXML.append("<cell>" + staticEntityName + "</cell>");
            entityMapXML.append("<cell>"
                    + Utility.parseDateToString(entityMapObj.getCreatedDate(),
                            Constants.DATE_PATTERN_MM_DD_YYYY) + "</cell>");
            entityMapXML.append("<cell>" + entityMapObj.getCreatedBy()
                    + "</cell>");
           /* entityMapXML.append("<cell>" + entityMapObj.getLinkStatus()
                    + "</cell>");*/
        }
        entityMapXML.append("<cell>"
                + edu.wustl.catissuecore.util.global.Constants.EDIT_CONDN + "^"
                + editDynExtCondnURL + "</cell>");

        entityMapXML.append("</row>");
        return entityMapXML;
    }

    /**
     * @param id
     * @return
     */
    private List<EntityMap> getEntityMapsForDE(Long dynEntityContainerId)
    {
        List<EntityMap> entityMapList = null;
        if (dynEntityContainerId != null)
        {
            AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
            entityMapList = annotationBizLogic
                    .getListOfStaticEntities(dynEntityContainerId);
            /*List staticEntityIdsList = annotationBizLogic.getListOfStaticEntities(dynEntityContainerId);
             entityMapList = getEntityMapList(staticEntityIdsList);*/
        }
        return entityMapList;
    }

    /**
     * @param staticEntityIdsList
     * @return
     */
    private List<EntityMap> getEntityMapList(List<Long> staticEntityIdsList)
    {
        List<EntityMap> entityMapList = new ArrayList<EntityMap>();
        EntityMap entityMapObj = null;
        if (staticEntityIdsList != null)
        {
            Iterator<Long> staticEntityIdIterator = staticEntityIdsList
                    .iterator();
            while (staticEntityIdIterator.hasNext())
            {
                entityMapObj = new EntityMap();
                entityMapObj.setStaticEntityId(staticEntityIdIterator.next());
                entityMapList.add(entityMapObj);
            }
        }
        return entityMapList;
    }

    /**
     * @param entityId
     * @return
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     */
    private String getEntityName(Long entityId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        String entityName = "";
        if (entityId != null)
        {
            EntityManagerInterface entityManager = EntityManager.getInstance();
            CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
                    .getInstance();
            List staticEntityList = (List) cacheManager
                    .getObjectFromCache(AnnotationConstants.STATIC_ENTITY_LIST);
            if (staticEntityList != null && !staticEntityList.isEmpty())
            {
                Iterator listIterator = staticEntityList.iterator();
                while (listIterator.hasNext())
                {
                    NameValueBean nameValueBean = (NameValueBean) listIterator
                            .next();
                    if (nameValueBean.getValue().equalsIgnoreCase(
                            entityId.toString()))
                    {
                        entityName = nameValueBean.getName();
                    }
                }
            }

        }
        return entityName;
    }

    /**
     * @param name
     * @return
     */
    private String resolveEntityName(String entityName)
    {
        if (AnnotationConstants.ENTITY_NAME_PARTICIPANT.equals(entityName))
        {
            return AnnotationConstants.PARTICIPANT;
        }
        else if (AnnotationConstants.ENTITY_NAME_SPECIMEN.equals(entityName))
        {
            return AnnotationConstants.SPECIMEN;
        }
        else if (AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP
                .equals(entityName))
        {
            return AnnotationConstants.SPECIMEN_COLLN_GROUP;
        }
        /*   else if (AnnotationConstants.ENTITY_NAME_COLLECTION_PROTOCOL_REGISTRATION
         .equals(entityName))
         {
         return AnnotationConstants.COLLECTION_PROTOCOL_REGISTRATION;
         }*/
        return "";
    }

    /**
     * @throws IOException 
     * 
     */
    private void sendResponse(String responseXML, HttpServletResponse response)
            throws IOException
    {
        PrintWriter out = response.getWriter();
        out.write(responseXML);
    }

    /**
     * @param annotationForm
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     */
    private void loadAnnotations(AnnotationForm annotationForm)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        if (annotationForm != null)
        {
            //load list of system entities
            List systemEntitiesList = AnnotationUtil.getSystemEntityList();
            annotationForm.setSystemEntitiesList(systemEntitiesList);
            //Load list of groups
            loadGroupList(annotationForm);

            List conditionalInstancesList = populateConditionalInstanceList();
            annotationForm
                    .setConditionalInstancesList(conditionalInstancesList);
            annotationForm
            .setConditionVal(new String[]{Constants.ALL});


        }
    }

    private List populateConditionalInstanceList()
    {
        List conditionalInstancesList = new ArrayList();

        //conditionalInstancesList.add(new NameValueBean(Constants.SELECT_OPTION,
        //      Constants.SELECT_OPTION_VALUE));
        /*if (request.getParameter("selectedId") != null)
         {
         String selectedId = request.getParameter("selectedId");*/
        try
        {
            /* if (selectedId.equals("4"))
             {*/
            DefaultBizLogic bizLogic = new DefaultBizLogic();
            String[] displayNames = {"title"};
            conditionalInstancesList = bizLogic.getList(SpecimenProtocol.class
                    .getName(), displayNames, "id", true);

         /*   if (conditionalInstancesList != null
                    && conditionalInstancesList.size() > 0)*/
                conditionalInstancesList.remove(0);
            /*if (conditionalInstancesList != null
                    && conditionalInstancesList.size() >= 1)
            {*/
                conditionalInstancesList.add(0, new NameValueBean(
                        edu.wustl.catissuecore.util.global.Constants.HOLDS_ANY , Constants.ALL));
            //}

            //    }
        }
        catch (DAOException e)
        {
        }

        // }
        return conditionalInstancesList;
    }




    
    

    /**
     * @param annotationForm 
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     * 
     */
    private void loadGroupList(AnnotationForm annotationForm)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        //List of groups
        Collection<NameValueBean> annotationGroupsList = getAnnotationGroups();
        String groupsXML = getGroupsXML(annotationGroupsList);
        annotationForm.setAnnotationGroupsXML(groupsXML);
    }

    /**
     * @param annotationGroupsList
     * @return
     */
    private String getGroupsXML(Collection<NameValueBean> annotationGroupsList)
    {
        StringBuffer groupsXML = new StringBuffer();
        if (annotationGroupsList != null)
        {
            groupsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
            groupsXML.append("<rows>");
            NameValueBean groupBean = null;
            Iterator<NameValueBean> iterator = annotationGroupsList.iterator();
            while (iterator.hasNext())
            {
                groupBean = iterator.next();
                if (groupBean != null)
                {
                    groupsXML
                            .append("<row id='" + groupBean.getValue() + "' >");
                    //groupsXML.append("<cell>0</cell>");
                    groupsXML
                            .append("<cell>" + groupBean.getName() + "</cell>");
                    groupsXML.append("</row>");
                }
            }
            groupsXML.append("</rows>");
        }
        return groupsXML.toString();
    }

    /**
     * @param annotationForm
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     */
    private Collection<NameValueBean> getAnnotationGroups()
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        EntityManagerInterface entityManager = EntityManager.getInstance();
        Collection<NameValueBean> entityGroups = entityManager
                .getAllEntityGroupBeans();
        return entityGroups;
    }
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax.servlet.http.HttpServletRequest)
     */
    protected boolean isAuthorizedToExecute(HttpServletRequest request)
            throws Exception
    {
        
            return super.isAuthorizedToExecute(request);
        
    }
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.BaseAction#getSessionData(javax.servlet.http.HttpServletRequest)
     */
    protected SessionDataBean getSessionData(HttpServletRequest request)
    {
       
            return super.getSessionData(request);
      
    }

}
