/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheException;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.EntityMapRecord;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;

/**
 * @author sandeep_chinta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class AnnotationBizLogic extends DefaultBizLogic
{

    /**
     *
     * @param staticEntityId
     * @return List of all dynamic entities id from a given static entity
     * eg: returns all dynamic entity id from a Participant,Specimen etc
     */
    public List getListOfDynamicEntitiesIds(long staticEntityId)
    {
        List<EntityMap> dynamicList = new ArrayList<EntityMap>();

        String[] selectColumnName = {"id"};
        String[] whereColumnName = {"staticEntityId"};
        String[] whereColumnCondition = {"="};
        Object[] whereColumnValue = {new Long(staticEntityId)};
        String joinCondition = null;
        List list = new ArrayList();
        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), "staticEntityId",
                    new Long(staticEntityId));
            if (dynamicList != null && !dynamicList.isEmpty())
            {
                for (EntityMap entityMap : dynamicList)
                {
                    list.add(entityMap.getContainerId());
                }
            }
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @param staticEntityId
     * @return List of all dynamic entities Objects from a given static entity
     * eg: returns all dynamic entity objects from a Participant,Specimen etc
     */
    public List getListOfDynamicEntities(long staticEntityId)
    {
        List dynamicList = new ArrayList();
        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), "staticEntityId",
                    new Long(staticEntityId));
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }



    /**
     *
     * @param staticEntityId
     * @param typeId
     * @param staticRecordId
     * @return List of all dynamic entities id from a given static entity based on its protocol linkage
     * eg: returns all dynamic entity id from a Participant,Specimen etc which is linked to Protocol 1, Protocol 2 etc
     */
    public List getListOfDynamicEntitiesIds(long staticEntityId, long typeId,
            long staticRecordId)
    {
        List dynamicList = new ArrayList();

        String[] selectColumnName = {"containerId"};
        String[] whereColumnName = {"staticEntityId", "typeId",
                "staticRecordId"};
        ;
        String[] whereColumnCondition = {"=", "=", "="};
        Object[] whereColumnValue = {new Long(staticEntityId),
                new Long(typeId), new Long(staticRecordId)};
        String joinCondition = Constants.AND_JOIN_CONDITION;

        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), selectColumnName,
                    whereColumnName, whereColumnCondition, whereColumnValue,
                    joinCondition);
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }

    /**
     *
     * @param entityRecord
     * Updates the Entity Record object in database
     * @throws BizLogicException
     */
    public void updateEntityRecord(EntityMapRecord entityRecord) throws BizLogicException
    {

        try
        {
            update(entityRecord, Constants.HIBERNATE_DAO);
        }
        catch (BizLogicException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);

        }
        catch (UserNotAuthorizedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }
    }

    /**
     *
     * @param entityRecord
     * Inserts a new EntityRecord record in Database
     * @throws DAOException
     * @throws BizLogicException
     */
    public void insertEntityRecord(EntityMapRecord entityRecord) throws DAOException, BizLogicException
    {
        try
        {
            insert(entityRecord, Constants.HIBERNATE_DAO);
            Long entityMapId = entityRecord.getFormContext().getEntityMap().getId();
            Long staticEntityRecordId = entityRecord.getStaticEntityRecordId();
            Long dynExtRecordId = entityRecord.getDynamicEntityRecordId();
            associateRecords(entityMapId,new Long (staticEntityRecordId), new Long(dynExtRecordId));
        }
        catch (BizLogicException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }
        catch (UserNotAuthorizedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }
    }
    /**
     * @param entityMapId
     * @param long1
     * @param long2
     * @throws DAOException
     */
    private void associateRecords(Long entityMapId, Long staticEntityRecordId, Long dynamicEntityRecordId) throws DAOException
    {
        List entityMapList = new DefaultBizLogic().retrieve(EntityMap.class.getName(),edu.wustl.catissuecore.util.global.Constants.ID,entityMapId);
        EntityManagerInterface entityManager = EntityManager.getInstance();
        if (entityMapList != null && !entityMapList.isEmpty())
        {
            try
            {
                EntityMap entityMap = (EntityMap) entityMapList.get(0);
                Long dynamicEntityId = entityManager.getEntityIdByContainerId(entityMap.getContainerId());
                EntityInterface dynamicEntity = EntityCache.getInstance().getEntityById(dynamicEntityId);
                EntityInterface staticEntity = EntityCache.getInstance().getEntityById(entityMap.getStaticEntityId());
                Collection<AssociationInterface> associationCollection = staticEntity.getAssociationCollection();
                AssociationInterface associationInterface = null;
                for (AssociationInterface association : associationCollection)
                {
                    if (association.getTargetEntity().equals(dynamicEntity))
                    {
                        associationInterface = association;
                        break;
                    }
                }
                entityManager.associateEntityRecords(associationInterface,staticEntityRecordId,dynamicEntityRecordId);
            }
            catch (DynamicExtensionsSystemException e)
            {
                throw new DAOException("Can not associate static and dynamic records",e);
            }
        }
    }

    /**
     *
     * @param entityMap
     * Updates the Entity Map object in database
     * @throws BizLogicException
     */
    public void updateEntityMap(EntityMap entityMap) throws BizLogicException
    {

        try
        {
            update(entityMap,Constants.HIBERNATE_DAO);
            //update(entityMap,null, Constants.HIBERNATE_DAO,null);
        }
        catch (BizLogicException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }
        catch (UserNotAuthorizedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }
    }

    /**
     *
     * @param entityMap
     * Inserts a new EntityMap record in Database
     * @throws BizLogicException
     */
    public void insertEntityMap(EntityMap entityMap) throws BizLogicException
    {
        try
        {
            insert(entityMap, Constants.HIBERNATE_DAO);
            Long staticEntityId = entityMap.getStaticEntityId();
            Long dynamicEntityId = entityMap.getContainerId();
            Long deAssociationID = AnnotationUtil.addAssociation(staticEntityId, dynamicEntityId);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.saveannotation"), e);
        }

    }

        /**
     *
     * @param dynamicEntityContainerId
     * @return List of Static Entity Id from a given Dynamic Entity Id
     *
     */
    public List getListOfStaticEntitiesIds(long dynamicEntityContainerId)
    {
        List dynamicList = new ArrayList();

        String[] selectColumnName = {"staticEntityId"};
        String[] whereColumnName = {"containerId"};
        ;
        String[] whereColumnCondition = {"="};
        Object[] whereColumnValue = {new Long(dynamicEntityContainerId)};
        String joinCondition = null;

        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), selectColumnName,
                    whereColumnName, whereColumnCondition, whereColumnValue,
                    joinCondition);
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }

    /**
     *
     * @param dynamicEntityContainerId
     * @return List of Static Entity Objects from a given Dynamic Entity Id
     *
     */
    public List getListOfStaticEntities(long dynamicEntityContainerId)
    {
        List dynamicList = new ArrayList();

        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), "containerId",
                    new Long(dynamicEntityContainerId));
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }

    /**
     *
     * @param entityMapId
     * @return EntityMap object for its given id
     */
    public List getEntityMap(long entityMapId)
    {
        List dynamicList = new ArrayList();

        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), "id", new Long(
                    entityMapId));
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }

    public List getEntityMapRecordList(List entityMapids, long staticRecordId)
    {
        List dynamicList = new ArrayList();

        String[] selectColumnName = null;
        String[] whereColumnName = {"staticEntityRecordId", "formContext.entityMap.id"};
        String[] whereColumnCondition = {"=", "="};
        String joinCondition = Constants.AND_JOIN_CONDITION;

        Iterator iter = entityMapids.iterator();
        while (iter.hasNext())
        {
            Long entityMapId = (Long) iter.next();
            if (entityMapId != null)
            {
                Object[] whereColumnValue = {new Long(staticRecordId),
                        entityMapId};
                try
                {
                    List list = retrieve(EntityMapRecord.class.getName(),
                            selectColumnName, whereColumnName,
                            whereColumnCondition, whereColumnValue,
                            joinCondition);
                    if (list != null)
                    {
                        dynamicList.addAll(list);
                    }
                }
                catch (DAOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return dynamicList;
    }

    public void deleteEntityMapRecord(long entityMapId,
            long dynamicEntityRecordId) throws BizLogicException
    {
        List dynamicList = new ArrayList();

        String[] selectColumnName = null;
        String[] whereColumnName = {"formContext.entityMap.id", "dynamicEntityRecordId"};
        ;
        String[] whereColumnCondition = {"=", "="};
        Object[] whereColumnValue = {new Long(entityMapId),
                new Long(dynamicEntityRecordId)};
        String joinCondition = Constants.AND_JOIN_CONDITION;

        try
        {
            dynamicList = retrieve(EntityMapRecord.class.getName(),
                    selectColumnName, whereColumnName, whereColumnCondition,
                    whereColumnValue, joinCondition);
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (dynamicList != null && !dynamicList.isEmpty())
        {

            try
            {
                EntityMapRecord entityRecord = (EntityMapRecord) dynamicList.get(0);
                entityRecord.setLinkStatus(Constants.ACTIVITY_STATUS_DISABLED);
                update(entityRecord, Constants.HIBERNATE_DAO);
            }
            catch (UserNotAuthorizedException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new BizLogicException(ApplicationProperties
                        .getValue("app.annotatations.errors.deleteRecord"), e1);
            }
            catch (BizLogicException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new BizLogicException(ApplicationProperties
                        .getValue("app.annotatations.errors.deleteRecord"), e1);
            }
        }
    }
/**
 *
 * @param containerId
 * @param recordIdList
 * @throws BizLogicException
 */
    public void deleteAnnotationRecords(Long containerId,
            List<Long> recordIdList) throws BizLogicException
    {
        EntityManagerInterface entityManagerInterface = EntityManager
                .getInstance();
        try
        {
            entityManagerInterface.deleteRecords(containerId, recordIdList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.deleteRecord"), e);
        }
    }

    /**
     * Deletes an object from the database.
     * @param obj The object to be deleted.
     * @throws DAOException
     * @throws UserNotAuthorizedException TODO
     */
    protected void delete(Object obj, DAO dao) throws DAOException,
            UserNotAuthorizedException
    {
        dao.delete(obj);
    }

    /**
     * @param dynEntitiesList
     * @param cpIdList
     * @return
     */
    public List getAnnotationIdsBasedOnCondition(List dynEntitiesList,
            List cpIdList)
    {
        List dynEntitiesIdList = new ArrayList();
        if (dynEntitiesList != null && !dynEntitiesList.isEmpty())
        {
            Iterator dynEntitiesIterator = dynEntitiesList.iterator();
            while (dynEntitiesIterator.hasNext())
            {
                EntityMap entityMap = (EntityMap) dynEntitiesIterator.next();
                Iterator  formIterator= entityMap.getFormContextCollection().iterator();
                while(formIterator.hasNext())
                {
                    FormContext formContext = (FormContext) formIterator.next();
                    if((formContext.getNoOfEntries() == null || formContext.getNoOfEntries().equals(""))&&(formContext.getStudyFormLabel() == null || formContext.getStudyFormLabel().equals("")))
                    {
                        if (formContext.getEntityMapConditionCollection() != null
                        && !formContext.getEntityMapConditionCollection()
                                .isEmpty())
                        {
                            boolean check = checkStaticRecId(formContext
                                    .getEntityMapConditionCollection(), cpIdList);
                            if (check)
                                dynEntitiesIdList.add(entityMap.getContainerId());
                        }
                        else
                            dynEntitiesIdList.add(entityMap.getContainerId());
                    }
                }
            }
        }
        return dynEntitiesIdList;
    }

    /**
     * @param entityMapConditionCollection
     * @param cpIdList
     * @return
     * @throws CacheException
     */
    private boolean checkStaticRecId(Collection entityMapConditionCollection,
            List cpIdList)
    {
        Iterator entityMapCondIterator = entityMapConditionCollection
                .iterator();
        try
        {
            CatissueCoreCacheManager cache = CatissueCoreCacheManager.getInstance();
            if (cpIdList != null && !cpIdList.isEmpty())
                while (entityMapCondIterator.hasNext())
                {
                    EntityMapCondition entityMapCond = (EntityMapCondition) entityMapCondIterator
                            .next();
                    if (entityMapCond.getTypeId().toString().equals(cache.getObjectFromCache(AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID).toString()) && cpIdList.contains(entityMapCond.getStaticRecordId()))
                        return true;
                }
        }
        catch(Exception e){}
        return false;
    }

    /**
     *
     * @param entityMapId
     * @return EntityMap object for its given id
     */
    public List getEntityMapOnContainer(long containerId)
    {
        List dynamicList = new ArrayList();

        try
        {
            dynamicList = retrieve(EntityMap.class.getName(), "containerId", new Long(
                    containerId));
        }
        catch (DAOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dynamicList;
    }
    /**
     *
     * @param entityMapCondition
     * @throws BizLogicException
     */
    public void insertEntityMapCondition(EntityMapCondition entityMapCondition) throws BizLogicException
    {
        try
        {
            insert(entityMapCondition, Constants.HIBERNATE_DAO);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BizLogicException(ApplicationProperties
                    .getValue("app.annotatations.errors.updateannotation"), e);
        }

    }


}
