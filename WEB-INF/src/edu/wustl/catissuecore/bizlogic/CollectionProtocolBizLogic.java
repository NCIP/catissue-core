/**
 * <p>Title: CollectionProtocolBizLogic Class>
 * <p>Description:	CollectionProtocolBizLogic is used to add CollectionProtocol information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 09, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.catissuecore.TaskTimeCalculater;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.CollectionProtocolAuthorization;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.Roles;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exceptionformatter.DefaultExceptionFormatter;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.security.beans.SecurityDataBean;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeManager;

// TODO: Auto-generated Javadoc
/**
 * CollectionProtocolBizLogic is used to add CollectionProtocol information into
 * the database using Hibernate.
 *
 * @author Mandar Deshmukh
 */
public class CollectionProtocolBizLogic extends SpecimenProtocolBizLogic implements Roles
{

    /**
     * Logger object.
     */
    private static final transient Logger LOGGER = Logger.getCommonLogger(CollectionProtocolBizLogic.class);

    private static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
    private static final String PROTOCOL_COORDINATOR = "Protocol Coordinator";
    private static final String ERROR_ITEM_REQUIRED = "errors.item.required";

    /**
     * Saves the CollectionProtocol object in the database.
     *
     * @param obj
     *            The CollectionProtocol object to be saved.
     * @param session
     *            The session in which the object is saved.
     * @throws BizLogicException
     */
    @Override
    protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
        CollectionProtocol collectionProtocol = null;
        Map<String, SiteUserRolePrivilegeBean> rowIdMap = null;
        if (obj instanceof CollectionProtocolDTO)
        {
            final CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
            collectionProtocol = CpDto.getCollectionProtocol();
            rowIdMap = CpDto.getRowIdBeanMap();
        }
        else
        {
            collectionProtocol = (CollectionProtocol) obj;
        }

        checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), PRINCIPAL_INVESTIGATOR);
        validateCollectionProtocol(dao, collectionProtocol);
        insertCollectionProtocol(dao, sessionDataBean, collectionProtocol, rowIdMap);
    }

    /**
     * Saves the CollectionProtocol object in the database.
     *
     * @param dao
     *            .
     * @param sessionDataBean
     * @param collProtocol
     * @param rowIdMap
     * @throws BizLogicException
     */
    private void insertCollectionProtocol(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collProtocol, final Map<String, SiteUserRolePrivilegeBean> rowIdMap)
            throws BizLogicException
    {
        try
        {
            setPrincipalInvestigator(dao, collProtocol);
            setCoordinatorCollection(dao, collProtocol);
            dao.insert(collProtocol);
            insertCPEvents(dao, sessionDataBean, collProtocol);
            insertchildCollectionProtocol(dao, sessionDataBean, collProtocol, rowIdMap);
            final HashSet<CollectionProtocol> protectionObjects = new HashSet<CollectionProtocol>();
            protectionObjects.add(collProtocol);

            final CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
            collectionProtocolAuthorization.authenticate(collProtocol, protectionObjects, rowIdMap);
        }
        catch (final ApplicationException exception)
        {
            CollectionProtocolBizLogic.LOGGER.error(exception.getMessage(), exception);
            // ErrorKey errorKey = ErrorKey.getErrorKey("dao.error");
            throw getBizLogicException(exception, exception.getErrorKeyName(), exception.getMsgValues());
        }
    }

    /**
     * validate the collection protocol object.
     *
     * @param dao
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void validateCollectionProtocol(final DAO dao, final CollectionProtocol collectionProtocol)
            throws BizLogicException
    {
        final Collection<CollectionProtocol> childCPCollection = collectionProtocol
                .getChildCollectionProtocolCollection();
        final Iterator<CollectionProtocol> cpIterator = childCPCollection.iterator();
        while (cpIterator.hasNext())
        {
            final CollectionProtocol protocol = cpIterator.next();
            checkStatus(dao, protocol.getPrincipalInvestigator(), PRINCIPAL_INVESTIGATOR);
        }
    }

    /**
     * This function used to insert collection protocol events and specimens.
     * for the collectionProtocol object.
     *
     * @param dao
     *            used to insert events and specimens into database.
     * @param sessionDataBean
     *            Contains session information
     * @param collectionProtocol
     *            collection protocol for which events & specimens to be added
     *
     * @throws BizLogicException
     *             If fails to insert events or its specimens
     */
    private void insertCPEvents(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collectionProtocol) throws BizLogicException
    {

        final Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
        final RequirementSpecimenBizLogic bizLogic = new RequirementSpecimenBizLogic();
        while (it.hasNext())
        {
            final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
            collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
            final Collection<SpecimenRequirement> reqSpecimenCollection = collectionProtocolEvent
                    .getSpecimenRequirementCollection();

            insertCollectionProtocolEvent(dao, collectionProtocolEvent);

            // check for null added for Bug #8533
            // Patch: 8533_4
            if (reqSpecimenCollection != null)
            {
                insertSpecimens(bizLogic, dao, reqSpecimenCollection, sessionDataBean, collectionProtocolEvent);
            }
        }
    }

    /**
     * This function will insert child Collection Protocol.
     *
     * @param dao
     * @param sessionDataBean
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void insertchildCollectionProtocol(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collectionProtocol, final Map<String, SiteUserRolePrivilegeBean> rowIdMap)
            throws BizLogicException
    {

        final Collection<CollectionProtocol> childCPCollection = collectionProtocol
                .getChildCollectionProtocolCollection();
        final Iterator<CollectionProtocol> cpIterator = childCPCollection.iterator();
        while (cpIterator.hasNext())
        {
            final CollectionProtocol protocol = cpIterator.next();
            protocol.setParentCollectionProtocol(collectionProtocol);
            insertCollectionProtocol(dao, sessionDataBean, protocol, rowIdMap);
        }

    }

    /**
     * @param bizLogic
     *            used to call business logic of Specimen.
     * @param dao
     *            Data access object to insert Specimen Collection groups and
     *            specimens.
     * @param collectionRequirementGroup
     * @param sessionDataBean
     *            object containing session information which is required for
     *            authorization.
     * @throws BizLogicException
     */
    private void insertSpecimens(final RequirementSpecimenBizLogic bizLogic, final DAO dao,
            final Collection<SpecimenRequirement> reqSpecimenCollection, final SessionDataBean sessionDataBean,
            final CollectionProtocolEvent collectionProtocolEvent) throws BizLogicException
    {
        final TaskTimeCalculater specimenInsert = TaskTimeCalculater.startTask("Insert specimen for CP",
                CollectionProtocolBizLogic.class);
        final Iterator<SpecimenRequirement> specIter = reqSpecimenCollection.iterator();
        final Collection specimenMap = new LinkedHashSet();
        while (specIter.hasNext())
        {
            final SpecimenRequirement SpecimenRequirement = specIter.next();
            SpecimenRequirement.setCollectionProtocolEvent(collectionProtocolEvent);
            if (SpecimenRequirement.getParentSpecimen() == null)
            {
                specimenMap.add(SpecimenRequirement);

            }
            else
            {
                addToParentSpecimen(SpecimenRequirement);
            }
        }
        // bizLogic.setCpbased(true);
        bizLogic.insertMultiple(specimenMap, dao, sessionDataBean);
        TaskTimeCalculater.endTask(specimenInsert);
    }

    /**
     * This function adds specimen object to its parent's childrenCollection if
     * not already added.
     *
     * @param SpecimenRequirement
     *            The object to be added to it's parent childrenCollection
     */
    private void addToParentSpecimen(final SpecimenRequirement SpecimenRequirement)
    {
        Collection<AbstractSpecimen> childrenCollection = SpecimenRequirement.getParentSpecimen()
                .getChildSpecimenCollection();
        if (childrenCollection == null)
        {
            childrenCollection = new LinkedHashSet<AbstractSpecimen>();
            // childrenCollection = new HashSet<AbstractSpecimen>();
        }
        if (!childrenCollection.contains(SpecimenRequirement))
        {
            childrenCollection.add(SpecimenRequirement);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * edu.wustl.common.bizlogic.DefaultBizLogic#postInsert(java.lang.Object,
     * edu.wustl.dao.DAO, edu.wustl.common.beans.SessionDataBean)
     */
    @Override
    public void postInsert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
        if (obj instanceof CollectionProtocolDTO)
        {
            final CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
            CpDto.getCollectionProtocol();
        }

        super.postInsert(obj, dao, sessionDataBean);
        // Commented by Geeta for removing teh CP
        // ParticipantRegistrationCacheManager
        // participantRegistrationCacheManager = new
        // ParticipantRegistrationCacheManager();
        // participantRegistrationCacheManager.addNewCP(collectionProtocol.getId(),
        // collectionProtocol.getTitle(), collectionProtocol.getShortTitle());

    }

    /**
     * Updates the persistent object in the database.
     *
     * @param obj
     *            The object to be updated.
     * @param session
     *            The session in which the object is saved.
     * @throws BizLogicException
     */
    @Override
    protected void update(final DAO dao, final Object obj, final Object oldObj,
            final SessionDataBean sessionDataBean) throws BizLogicException
    {
        CollectionProtocol collectionProtocol = null;
        Map<String, SiteUserRolePrivilegeBean> rowIdMap = null;
        if (obj instanceof CollectionProtocolDTO)
        {
            final CollectionProtocolDTO cpDto = (CollectionProtocolDTO) obj;
            collectionProtocol = cpDto.getCollectionProtocol();
            rowIdMap = cpDto.getRowIdBeanMap();
            final HashSet<CollectionProtocol> protectionObjects = new HashSet<CollectionProtocol>();
            protectionObjects.add(collectionProtocol);
        }
        else
        {
            collectionProtocol = (CollectionProtocol) obj;
        }
        isCollectionProtocolLabelUnique(collectionProtocol);
        modifyCPObject(dao, sessionDataBean, collectionProtocol);

        final Vector<SecurityDataBean> authorizationData = new Vector<SecurityDataBean>();
        if (rowIdMap != null)
        {
            try
            {
                // Vector authorizationData = new Vector();
                final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();

                final CollectionProtocolAuthorization cpAuthorization = new CollectionProtocolAuthorization();
                cpAuthorization.insertCpUserPrivilegs(collectionProtocol, authorizationData, rowIdMap);
                // insertCPSitePrivileges(user, authorizationData,
                // userRowIdMap);
                // collectionProtocol.getAssignedProtocolUserCollection().add(collectionProtocol.getPrincipalInvestigator());
                cpAuthorization.inserPIPrivileges(collectionProtocol, authorizationData);
                cpAuthorization.insertCoordinatorPrivileges(collectionProtocol, authorizationData);
                privilegeManager.insertAuthorizationData(authorizationData, null, null, collectionProtocol
                        .getObjectId());
                // new
                // CollectionProtocolAuthorization().insertCpUserPrivilegs(collectionProtocol,
                // authorizationData, rowIdMap);
            }
            catch (final SMException e)
            {
                CollectionProtocolBizLogic.LOGGER.error(e.getMessage(), e);
                throw new BizLogicException(null, null, "Error in checking has privilege");
            }
            catch (final ApplicationException e)
            {
                CollectionProtocolBizLogic.LOGGER.error(e.getLogMessage(), e);
                throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
            }
        }
    }

    /**
     * @param dao
     * @param sessionDataBean
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void modifyCPObject(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collectionProtocol) throws BizLogicException
    {
        final CollectionProtocol collectionProtocolOld;
        DAO cleanDAO = null;
        try
        {
            cleanDAO = openDAOSession(sessionDataBean);
            collectionProtocolOld = getOldCollectionProtocol(cleanDAO, collectionProtocol.getId());

            if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(collectionProtocol.getActivityStatus())
                    & !Status.ACTIVITY_STATUS_CLOSED.toString().equals(collectionProtocol.getActivityStatus()))
            {
                setCPEventCollection(collectionProtocol);
                editCPObj(dao, collectionProtocol, collectionProtocolOld);
            }
            else
            {
                setCPRCollection(collectionProtocol);
                if (collectionProtocolOld.getCollectionProtocolRegistrationCollection().size() == 0)
                {
                    final Collection<ConsentTier> oldConsentTierCollection = collectionProtocolOld
                            .getConsentTierCollection();
                    final Collection<ConsentTier> newConsentTierCollection = collectionProtocol
                            .getConsentTierCollection();
                    checkConsents(dao, oldConsentTierCollection, newConsentTierCollection);
                }
                editEvents(dao, sessionDataBean, collectionProtocol, collectionProtocolOld);
                editCPObj(dao, collectionProtocol, collectionProtocolOld);
            }

        }
        catch (final BizLogicException e)
        {
            CollectionProtocolBizLogic.LOGGER.error(e.getMessage(), e);
            throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
        }
        finally
        {
            closeDAOSession(cleanDAO);
        }

    }

    /**
     * @param dao
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void setCPRCollection(final CollectionProtocol collectionProtocol) throws BizLogicException
    {
        final String cprHQL = "select elements(cp.collectionProtocolRegistrationCollection)"
                + " from edu.wustl.catissuecore.domain.CollectionProtocol as cp" + " where cp.id="
                + collectionProtocol.getId();
        final Collection cprCollection = executeHQL(cprHQL);
        collectionProtocol.setCollectionProtocolRegistrationCollection(cprCollection);
    }

    /**
     * @param dao
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void setCPEventCollection(final CollectionProtocol collectionProtocol) throws BizLogicException
    {
        final String cpEventHQL = "select elements(cp.collectionProtocolEventCollection)"
                + " from edu.wustl.catissuecore.domain.CollectionProtocol as cp" + " where cp.id="
                + collectionProtocol.getId();
        final Collection cpEventCollection = executeHQL(cpEventHQL);
        collectionProtocol.setCollectionProtocolEventCollection(cpEventCollection);
    }

    /**
     * @param dao
     * @param cprHQL
     * @return
     * @throws BizLogicException
     */
    private Collection executeHQL(final String cprHQL) throws BizLogicException
    {
        final Collection cprCollection = new LinkedHashSet();
        final DefaultBizLogic bizLogic = new DefaultBizLogic();

        final List list = bizLogic.executeQuery(cprHQL);
        cprCollection.addAll(list);

        return cprCollection;
    }

    /**
     * @param dao
     *            DAO Object
     * @param collectionProtocol
     *            Transient Object
     * @param collectionProtocolOld
     *            Persistent object
     * @param collectionProtocol
     *            Transient CP Object
     * @param collectionProtocolOld
     *            Persistent CP Object
     * @throws BizLogicException
     *             Database related exception
     */
    private void editCPObj(final DAO dao, final CollectionProtocol collectionProtocol,
            final CollectionProtocol collectionProtocolOld) throws BizLogicException
    {
        try
        {
            if (!collectionProtocol.getPrincipalInvestigator().getId().equals(
                    collectionProtocolOld.getPrincipalInvestigator().getId()))
            {
                checkStatus(dao, collectionProtocol.getPrincipalInvestigator(), PRINCIPAL_INVESTIGATOR);
            }
            checkCoordinatorStatus(dao, collectionProtocol, collectionProtocolOld);
            checkForChangedStatus(collectionProtocol, collectionProtocolOld);
            dao.update(collectionProtocol, collectionProtocolOld);
            this.disableRelatedObjects(dao, collectionProtocol);
            updatePIAndCoordinatorGroup(dao, collectionProtocol, collectionProtocolOld);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
    }

    /**
     * @param dao
     *            DAO object.
     * @param oldConsentTierCollection
     *            old Consent tier collection
     * @param newConsentTierCollection
     *            New Consent tier collection
     * @throws BizLogicException
     *             BizLogicException
     */
    private void checkConsents(final DAO dao, final Collection<ConsentTier> oldConsentTierCollection,
            final Collection<ConsentTier> newConsentTierCollection) throws BizLogicException
    {
        final Iterator<ConsentTier> itr = oldConsentTierCollection.iterator();
        while (itr.hasNext())
        {
            final ConsentTier cTier = itr.next();
            final Object obj = getCorrespondingOldObject(newConsentTierCollection, cTier.getId());
            if (obj == null)
            {
                deleteConsents(dao, cTier);
            }
        }
    }

    /**
     * @param dao
     *            DAO Object
     * @param cTier
     *            ConsentTier object
     * @throws BizLogicException
     *             DAO Exception
     */
    private void deleteConsents(final DAO dao, final ConsentTier cTier) throws BizLogicException
    {
        try
        {
            final ConsentTier consentTier = (ConsentTier) dao.retrieveById(ConsentTier.class.getName(), cTier
                    .getId());
            dao.delete(consentTier);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
    }

    /**
     * @param dao
     *            DAO Object
     * @param sessionDataBean
     *            Session data
     * @param collectionProtocol
     *            Transient CP object
     * @param collectionProtocolOld
     *            Old CP Object
     * @throws BizLogicException
     *             Database related exception
     */
    private void editEvents(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collectionProtocol, final CollectionProtocol collectionProtocolOld)
            throws BizLogicException
    {
        final Collection oldCollectionProtocolEventCollection = collectionProtocolOld
                .getCollectionProtocolEventCollection();
        updateCollectionProtocolEvents(dao, sessionDataBean, collectionProtocol,
                oldCollectionProtocolEventCollection);
    }

    /**
     * updatePIAndCoordinatorGroup.
     *
     * @param dao
     *            dao
     * @param collectionProtocol
     *            collectionProtocol
     * @param collectionProtocolOld
     *            collectionProtocolOld
     * @throws BizLogicException
     *             BizLogicException
     */
    private void updatePIAndCoordinatorGroup(final DAO dao, final CollectionProtocol collectionProtocol,
            final CollectionProtocol collectionProtocolOld) throws BizLogicException
    {
        try
        {
            final CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
            collectionProtocolAuthorization.updatePIAndCoordinatorGroup(dao, collectionProtocolOld, true);

            final Long csmUserId = collectionProtocolAuthorization.getCSMUserId(dao, collectionProtocol
                    .getPrincipalInvestigator());
            if (csmUserId != null)
            {
                collectionProtocol.getPrincipalInvestigator().setCsmUserId(csmUserId);
                LOGGER.debug("PI ....." + collectionProtocol.getPrincipalInvestigator().getCsmUserId());
                collectionProtocolAuthorization.updatePIAndCoordinatorGroup(dao, collectionProtocol, false);
            }
        }
        catch (final ApplicationException smExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(smExp.getMessage(), smExp);
            throw getBizLogicException(smExp, smExp.getErrorKeyName(), smExp.getMsgValues());

        }
    }

    /**
     * @param collectionProtocol
     * @return
     * @throws BizLogicException
     */
    private CollectionProtocol getOldCollectionProtocol(final DAO dao, final Long identifier)
            throws BizLogicException
    {
        CollectionProtocol collectionProtocolOld;
        try
        {
            collectionProtocolOld = (CollectionProtocol) dao.retrieveById(CollectionProtocol.class.getName(),
                    identifier);
        }
        catch (final DAOException e)
        {
            CollectionProtocolBizLogic.LOGGER.error(e.getMessage(), e);
            throw getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
        }
        return collectionProtocolOld;
    }

    /**
     * @param dao
     * @param collectionProtocol
     * @param collectionProtocolOld
     * @throws BizLogicException
     */
    private void checkCoordinatorStatus(final DAO dao, final CollectionProtocol collectionProtocol,
            final CollectionProtocol collectionProtocolOld) throws BizLogicException
    {
        final Iterator<User> iterator = collectionProtocol.getCoordinatorCollection().iterator();
        while (iterator.hasNext())
        {
            final User coordinator = iterator.next();
            if (coordinator.getId().equals(collectionProtocol.getPrincipalInvestigator().getId()))
            {
                iterator.remove();

            }
            else
            {
                final CollectionProtocolAuthorization collectionProtocolAuthorization = new CollectionProtocolAuthorization();
                if (!collectionProtocolAuthorization.hasCoordinator(coordinator, collectionProtocolOld))
                {
                    checkStatus(dao, coordinator, PROTOCOL_COORDINATOR);
                }
            }
        }
    }

    /**
     * @param dao
     * @param collectionProtocol
     * @throws BizLogicException
     */
    private void disableRelatedObjects(final DAO dao, final CollectionProtocol collectionProtocol)
            throws BizLogicException
    {
        // Disable related Objects
        LOGGER.debug("collectionProtocol.getActivityStatus() " + collectionProtocol.getActivityStatus());
        if (collectionProtocol.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
        {
            LOGGER.debug("collectionProtocol.getActivityStatus() " + collectionProtocol.getActivityStatus());
            final Long collectionProtocolIDArr[] = { collectionProtocol.getId() };

            final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
            final CollectionProtocolRegistrationBizLogic bizLogic = (CollectionProtocolRegistrationBizLogic) factory
                    .getBizLogic(Constants.COLLECTION_PROTOCOL_REGISTRATION_FORM_ID);
            bizLogic.disableRelatedObjectsForCollectionProtocol(dao, collectionProtocolIDArr);
        }
    }

    /**
     * @param dao
     *            DAO Object
     * @param sessionDataBean
     *            Session data
     * @param collectionProtocol
     *            Transient CP object
     * @param collectionProtocolOld
     *            Old CP Object
     * @throws BizLogicException
     *             Database related exception
     */
    private void updateCollectionProtocolEvents(final DAO dao, final SessionDataBean sessionDataBean,
            final CollectionProtocol collectionProtocol,
            final Collection<CollectionProtocolEvent> oldCPEventCollection) throws BizLogicException
    {
        try
        {
            final RequirementSpecimenBizLogic reqSpBiz = new RequirementSpecimenBizLogic();
            final Iterator it = collectionProtocol.getCollectionProtocolEventCollection().iterator();
            while (it.hasNext())
            {
                final CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) it.next();
                collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
                CollectionProtocolEvent oldCollectionProtocolEvent = null;
                if (collectionProtocolEvent.getId() == null || collectionProtocolEvent.getId() <= 0)
                {
                    insertCollectionProtocolEvent(dao, collectionProtocolEvent);
                }
                else
                {
                    oldCollectionProtocolEvent = (CollectionProtocolEvent) getCorrespondingOldObject(
                            oldCPEventCollection, collectionProtocolEvent.getId());
                    dao.update(collectionProtocolEvent, oldCollectionProtocolEvent);

                }
                // Audit of collectionProtocolEvent
                reqSpBiz
                        .updateSpecimens(dao, sessionDataBean, oldCollectionProtocolEvent, collectionProtocolEvent);
            }
            final Collection<CollectionProtocolEvent> newCPEventCollection = collectionProtocol
                    .getCollectionProtocolEventCollection();
            checkEventDelete(dao, reqSpBiz, newCPEventCollection, oldCPEventCollection);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());

        }

    }

    /**
     * This method check for Event to delete or not.
     *
     * @param dao
     *            DAO Object
     * @param reqSpBiz
     *            Object of requirement Sp Bizlogic
     * @param cpEventCollection
     *            CP Event Collection
     * @param oldCpEventCollection
     *            Old Cp Event Collection
     * @throws BizLogicException
     *             Database related exception
     */
    private void checkEventDelete(final DAO dao, final RequirementSpecimenBizLogic reqSpBiz,
            final Collection<CollectionProtocolEvent> cpEventCollection,
            final Collection<CollectionProtocolEvent> oldCpEventCollection) throws BizLogicException
    {
        CollectionProtocolEvent oldCpEvent = null;
        final Iterator<CollectionProtocolEvent> itr = oldCpEventCollection.iterator();
        while (itr.hasNext())
        {
            oldCpEvent = itr.next();
            final Object cpe = getCorrespondingOldObject(cpEventCollection, oldCpEvent.getId());
            if (cpe == null)
            {
                deleteEvent(dao, oldCpEvent, reqSpBiz);
            }
        }
    }

    /**
     * This method will delete the event and the req spe under that event
     *
     * @param dao
     *            DAO object
     * @param oldCpEvent
     *            Persistent CP Event
     * @param reqSpBiz
     *            Requirement Specimen biz logic object
     * @throws BizLogicException
     *             Database related exception
     */
    private void deleteEvent(final DAO dao, final CollectionProtocolEvent oldCpEvent,
            final RequirementSpecimenBizLogic reqSpBiz) throws BizLogicException
    {
        try
        {
            final CollectionProtocolEvent cpEvent = (CollectionProtocolEvent) dao.retrieveById(
                    CollectionProtocolEvent.class.getName(), oldCpEvent.getId());
            SpecimenRequirement oldSpReq = null;
            final Collection<SpecimenRequirement> spReqColl = cpEvent.getSpecimenRequirementCollection();
            final Iterator<SpecimenRequirement> spReqItr = spReqColl.iterator();
            while (spReqItr.hasNext())
            {
                oldSpReq = spReqItr.next();
                if (Constants.NEW_SPECIMEN.equals(oldSpReq.getLineage()))
                {
                    reqSpBiz.deleteRequirementSpecimen(dao, oldSpReq);
                }
            }
            dao.delete(cpEvent);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
    }

    /**
     *
     * @param dao
     *            DAO object
     * @param collectionProtocolEvent
     *            CPE Object
     * @throws BizLogicException
     *             Database related exception
     */
    private void insertCollectionProtocolEvent(final DAO dao, final CollectionProtocolEvent collectionProtocolEvent)
            throws BizLogicException
    {
        try
        {
            dao.insert(collectionProtocolEvent);
            // final AuditManager auditManager =
            // this.getAuditManager(sessionDataBean);
            // auditManager.insertAudit(dao, collectionProtocolEvent);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }

    }

    // This method sets the Principal Investigator.
    private void setPrincipalInvestigator(final DAO dao, final CollectionProtocol collectionProtocol)
            throws BizLogicException
    {
        try
        {
            // List list = dao.retrieve(User.class.getName(), "id",
            // collectionProtocol.getPrincipalInvestigator().getId());
            // if (list.size() != 0)
            final Object obj = dao.retrieveById(User.class.getName(), collectionProtocol
                    .getPrincipalInvestigator().getId());
            if (obj != null)
            {
                final User investigator = (User) obj;// list.get(0);
                collectionProtocol.setPrincipalInvestigator(investigator);
            }
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
    }

    // This method sets the User Collection.
    private void setCoordinatorCollection(final DAO dao, final CollectionProtocol collectionProtocol)
            throws BizLogicException
    {
        final Long piID = collectionProtocol.getPrincipalInvestigator().getId();
        LOGGER.debug("Coordinator Size " + collectionProtocol.getCoordinatorCollection().size());
        final Collection coordinatorColl = new HashSet();
        try
        {
            final Iterator it = collectionProtocol.getCoordinatorCollection().iterator();
            while (it.hasNext())
            {
                final User aUser = (User) it.next();
                if (!aUser.getId().equals(piID))
                {
                    LOGGER.debug("Coordinator ID :" + aUser.getId());
                    Object obj;

                    obj = dao.retrieveById(User.class.getName(), aUser.getId());

                    if (obj != null)
                    {
                        final User coordinator = (User) obj;// list.get(0);

                        // checkStatus(dao, coordinator, "coordinator");
                        final String activityStatus = coordinator.getActivityStatus();
                        if (activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
                        {
                            final String message = "Coordinator " + coordinator.getLastName() + " "
                                    + coordinator.getFirstName() + " ";
                            throw getBizLogicException(null, "error.object.closed", message);
                        }

                        coordinatorColl.add(coordinator);
                        // coordinator.getCollectionProtocolCollection().add(collectionProtocol);
                        // collectionProtocol.getAssignedProtocolUserCollection().add(coordinator);
                    }
                }
            }
            collectionProtocol.setCoordinatorCollection(coordinatorColl);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
    }

    /*
     * public void setPrivilege(DAO dao, String privilegeName, Class objectType,
     * Long[] objectIds, Long userId, String roleId, boolean assignToUser,
     * boolean assignOperation) throws SMException, DAOException {
     * super.setPrivilege(dao, privilegeName, objectType, objectIds, userId,
     * roleId, assignToUser, assignOperation);
     *
     * // CollectionProtocolRegistrationBizLogic bizLogic =
     * (CollectionProtocolRegistrationBizLogic
     * )BizLogicFactory.getBizLogic(Constants
     * .COLLECTION_PROTOCOL_REGISTRATION_FORM_ID); //
     * bizLogic.assignPrivilegeToRelatedObjectsForCP
     * (dao,privilegeName,objectIds,userId, roleId, assignToUser); }
     */

    /**
     * Executes hql Query and returns the results.
     *
     * @param hql
     *            String hql
     * @throws BizLogicException
     *             DAOException
     * @throws ClassNotFoundException
     *             ClassNotFoundException
     */
    private List executeHqlQuery(final String hql) throws BizLogicException
    {

        return executeQuery(hql);
    }

    protected boolean isSpecimenExists(final DAO dao, final Long cpId) throws BizLogicException

    {

        final String hql = " select" + " elements(scg.specimenCollection) " + "from "
                + " edu.wustl.catissuecore.domain.CollectionProtocol as cp"
                + ", edu.wustl.catissuecore.domain.CollectionProtocolRegistration as cpr"
                + ", edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg"
                + ", edu.wustl.catissuecore.domain.Specimen as s" + " where cp.id = " + cpId + "  and"
                + " cp.id = cpr.collectionProtocol.id and" + " cpr.id = scg.collectionProtocolRegistration.id and"
                + " scg.id = s.specimenCollectionGroup.id and " + " s.activityStatus = '"
                + Status.ACTIVITY_STATUS_ACTIVE.toString() + "'";

        final List specimenList = executeHqlQuery(hql);
        if ((specimenList != null) && (specimenList).size() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * Overriding the parent class's method to validate the enumerated attribute
     * values
     */
    @Override
    protected boolean validate(final Object obj, final DAO dao, final String operation) throws BizLogicException
    {

        // Added by Ashish
        // setAllValues(obj);
        // END

        Map<String, SiteUserRolePrivilegeBean> rowIdMap = null;
        CollectionProtocol protocol = null;
        if (obj instanceof CollectionProtocolDTO)
        {
            final CollectionProtocolDTO CpDto = (CollectionProtocolDTO) obj;
            protocol = CpDto.getCollectionProtocol();
            rowIdMap = CpDto.getRowIdBeanMap();

            final Set<Long> piAndCoOrdIds = new HashSet<Long>();

            piAndCoOrdIds.add(protocol.getPrincipalInvestigator().getId());

            final Collection<User> coordinatorCollection = protocol.getCoordinatorCollection();

            if (coordinatorCollection != null && !coordinatorCollection.isEmpty())
            {
                for (final User user : coordinatorCollection)
                {
                    piAndCoOrdIds.add(user.getId());
                }
            }

            // if (rowIdMap != null)
            // {
            // final Object[] mapKeys = rowIdMap.keySet().toArray();
            // for (final Object mapKey : mapKeys)
            // {
            // final String key = mapKey.toString();
            // final SiteUserRolePrivilegeBean bean = rowIdMap.get(key);
            // if (bean.isCustChecked() &&
            // piAndCoOrdIds.contains(bean.getUser().getId()))
            // {
            // // throw new
            // DAOException(ApplicationProperties.getValue("cp.cannotChangePrivilegesOfPIOrCoord"));
            // }
            // }
            // }
        }
        else
        {
            protocol = (CollectionProtocol) obj;
        }
        final Collection eventCollection = protocol.getCollectionProtocolEventCollection();

        /**
         * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
         * Search, previoulsy it was failing since there was default class level
         * initialization on domain object. For example in User object, it was
         * initialized as protected String lastName=""; So we removed default
         * class level initialization on domain object and are initializing in
         * method setAllValues() of domain object. But in case of Api Search,
         * default values will not get set since setAllValues() method of
         * domainObject will not get called. To avoid null pointer exception, we
         * are setting the default values same as we were setting in
         * setAllValues() method of domainObject.
         */
        ApiSearchUtil.setSpecimenProtocolDefault(protocol);
        // End:- Change for API Search

        // Added by Ashish
        final Validator validator = new Validator();
        String message = "";
        if (protocol == null)
        {
            throw getBizLogicException(null, "domain.object.null.err.msg", "Collection Protocol");
        }

        if (Validator.isEmpty(protocol.getTitle()))
        {
            message = ApplicationProperties.getValue("collectionprotocol.protocoltitle");
            throw getBizLogicException(null, ERROR_ITEM_REQUIRED, message);
        }
        if (Validator.isEmpty(protocol.getShortTitle()))
        {
            message = ApplicationProperties.getValue("collectionprotocol.shorttitle");
            throw getBizLogicException(null, ERROR_ITEM_REQUIRED, message);
        }

        // if (validator.isEmpty(protocol.getIrbIdentifier()))
        // {
        // message = ApplicationProperties.getValue("collectionprotocol.irbid");
        // throw new
        // DAOException(ApplicationProperties.getValue("errors.item.required",message));
        // }

        if (protocol.getStartDate() == null)
        {
            message = ApplicationProperties.getValue("collectionprotocol.startdate");
            throw getBizLogicException(null, ERROR_ITEM_REQUIRED, message);
        }
        else
        {
            validator.validateDate(protocol.getStartDate().toString(), false);

        }
        if (protocol.getEndDate() != null)
        {
            validator.validateDate(protocol.getEndDate().toString(), false);
        }
        if ((protocol.getPrincipalInvestigator().getId() == null || protocol.getPrincipalInvestigator().getId() == -1)
                && (protocol.getPrincipalInvestigator().getLoginName() == null || Validator.isEmpty(protocol
                        .getPrincipalInvestigator().getLoginName())))
        {
            throw getBizLogicException(null, ERROR_ITEM_REQUIRED, PRINCIPAL_INVESTIGATOR);
        }
        else if (protocol.getPrincipalInvestigator().getId() == null
                && (protocol.getPrincipalInvestigator().getLoginName() != null || !Validator.isEmpty(protocol
                        .getPrincipalInvestigator().getLoginName())))
        {
            final List list = getUserByLoginName(dao, protocol.getPrincipalInvestigator().getLoginName());

            /*
             * List list = null; list =dao.executeQuery(
             * "select id,activityStatus from edu.wustl.catissuecore.domain.User where loginName='"
             * +protocol.getPrincipalInvestigator() .getLoginName()+"'");
             */
            if (list == null || list.isEmpty())
            {
                throw getBizLogicException(null, "errors.entity.notExists", PRINCIPAL_INVESTIGATOR);
            }
            else if (list != null && !list.isEmpty())
            {
                final Object[] object = (Object[]) list.get(0);
                final User user = new User();
                user.setId((Long) object[0]);
                user.setActivityStatus((String) object[1]);
                // check for closed User
                checkStatus(dao, user, "User");
                protocol.setPrincipalInvestigator(user);
            }
        }
        validatePI(dao, protocol);

        final Collection protocolCoordinatorCollection = protocol.getCoordinatorCollection();
        // if(protocolCoordinatorCollection == null ||
        // protocolCoordinatorCollection.isEmpty())
        // {
        // //message =
        // ApplicationProperties.getValue("collectionprotocol.specimenstatus");
        // throw new
        // DAOException(ApplicationProperties.getValue("errors.one.item.required","Protocol Coordinator"));
        // }
        if (protocolCoordinatorCollection != null && !protocolCoordinatorCollection.isEmpty())
        {
            final Iterator protocolCoordinatorItr = protocolCoordinatorCollection.iterator();
            while (protocolCoordinatorItr.hasNext())
            {
                final User protocolCoordinator = (User) protocolCoordinatorItr.next();
                if (protocolCoordinator.getId() == null
                        && (protocolCoordinator.getLoginName() != null || !Validator.isEmpty(protocolCoordinator
                                .getLoginName())))
                {
                    final List list = getUserByLoginName(dao, protocolCoordinator.getLoginName());
                    if (list == null || list.isEmpty())
                    {
                        throw getBizLogicException(null, "errors.entity.notExists", PROTOCOL_COORDINATOR);
                    }
                    else if (list != null && !list.isEmpty())
                    {
                        final Object[] object = (Object[]) list.get(0);
                        protocolCoordinator.setId((Long) object[0]);
                        protocolCoordinator.setActivityStatus((String) object[1]);
                        // check for closed User
                        checkStatus(dao, protocolCoordinator, "User");
                    }
                }
                if (protocolCoordinator.getId() == protocol.getPrincipalInvestigator().getId())
                {
                    throw getBizLogicException(null, "errors.pi.coordinator.same", "");
                }
            }
        }

        if (eventCollection != null && eventCollection.size() != 0)
        {
            final CDEManager manager = CDEManager.getCDEManager();

            if (manager == null)
            {

                throw getBizLogicException(null, "cde.init.issue", "");
            }

            final List specimenClassList = manager
                    .getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);

            CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_TISSUE_SITE, null);

            CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);
            final List clinicalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
                    Constants.CDE_NAME_CLINICAL_STATUS, null);

            final Iterator eventIterator = eventCollection.iterator();

            while (eventIterator.hasNext())
            {
                final CollectionProtocolEvent event = (CollectionProtocolEvent) eventIterator.next();

                if (event == null)
                {
                    throw getBizLogicException(null, "collectionProtocol.eventsEmpty.errMsg", "");
                }
                else
                {
                    if (!Validator.isEnumeratedValue(clinicalStatusList, event.getClinicalStatus()))
                    {
                        throw getBizLogicException(null, "collectionProtocol.clinicalStatus.errMsg", "");
                    }

                    // Added for Api Search
                    if (event.getStudyCalendarEventPoint() == null)
                    {
                        message = ApplicationProperties.getValue("collectionprotocol.studycalendartitle");
                        throw getBizLogicException(null, ERROR_ITEM_REQUIRED, message);
                    }
                    // Added by Vijay for API
                    // testAddCollectionProtocolWithWrongCollectionPointLabel
                    if (Validator.isEmpty(event.getCollectionPointLabel()))
                    {
                        message = ApplicationProperties.getValue("collectionprotocol.collectionpointlabel");

                        throw getBizLogicException(null, ERROR_ITEM_REQUIRED, message);
                    }

                    final Collection<SpecimenRequirement> reqCollection = event.getSpecimenRequirementCollection();

                    if (reqCollection != null && !reqCollection.isEmpty())
                    {
                        final Iterator<SpecimenRequirement> reqIterator = reqCollection.iterator();

                        while (reqIterator.hasNext())
                        {

                            final SpecimenRequirement SpecimenRequirement = reqIterator.next();
                            // if (SpecimenRequirement == null)
                            // {
                            // //check removed for Bug #8533
                            // //Patch: 8533_2
                            // //throw new DAOException(ApplicationProperties
                            // // .getValue("protocol.spReqEmpty.errMsg"));
                            // }
                            ApiSearchUtil.setReqSpecimenDefault(SpecimenRequirement);
                            final String specimenClass = SpecimenRequirement.getClassName();
                            if (!Validator.isEnumeratedValue(specimenClassList, specimenClass))
                            {
                                throw getBizLogicException(null, "protocol.class.errMsg", "");
                            }
                            if (!Validator.isEnumeratedValue(AppUtility.getSpecimenTypes(specimenClass),
                                    SpecimenRequirement.getSpecimenType()))
                            {
                                throw getBizLogicException(null, "protocol.type.errMsg", "");
                            }
                        }

                    }
                }
            }
        }
        // else
        // {
        // //commented by vaishali... it' not necessary condition
        // //throw new DAOException(ApplicationProperties
        // // .getValue("collectionProtocol.eventsEmpty.errMsg"));
        // }

        if (operation.equals(Constants.ADD))
        {

            validateCPTitle(protocol);
            if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(protocol.getActivityStatus()))
            {

                throw getBizLogicException(null, "activityStatus.active.errMsg", "");
            }
        }
        else
        {
            if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, protocol.getActivityStatus()))
            {
                throw getBizLogicException(null, "activityStatus.errMsg", "");
            }
        }
        if (protocol.getActivityStatus() != null
                && protocol.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))
        {

            final boolean isSpecimenExist = isSpecimenExists(dao, protocol.getId());
            if (isSpecimenExist)
            {
                throw getBizLogicException(null, "collectionprotocol.specimen.exists", "");
            }

        }

        return true;
    }

    /**
     * This method checks to see if the PI of the collection protocol is an
     * active user.
     *
     * @param dao
     * @param protocol
     * @throws BizLogicException
     */
    private void validatePI(final DAO dao, final CollectionProtocol protocol) throws BizLogicException
    {
        try
        {
            final ColumnValueBean columnValueBean=new ColumnValueBean( protocol
                    .getPrincipalInvestigator().getId());
            columnValueBean.setColumnName(Constants.ID);

            final List<Object> principalInvestigatorStatus =((HibernateDAO)dao).retrieveAttribute(User.class,columnValueBean , "activityStatus");
            if (principalInvestigatorStatus == null || principalInvestigatorStatus.size()==0)
            {
                throw getBizLogicException(null, "errors.item.notExists", PRINCIPAL_INVESTIGATOR);
            }
            else if (!"Active".equals(principalInvestigatorStatus.get(0)))
            {
                throw getBizLogicException(null, "errors.item.notActive", PRINCIPAL_INVESTIGATOR);
            }
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(null, "errors.item.notExists", PRINCIPAL_INVESTIGATOR);
        }
    }

    /**
     * @param protocol
     *            Collection protocol
     * @throws BizLogicException
     */
    private void validateCPTitle(final CollectionProtocol protocol) throws BizLogicException
    {
        final JDBCDAO jdbcDao = openJDBCSession();

        final String queryStr = "select title from catissue_specimen_protocol where title = ?";
        final ColumnValueBean columnValueBean = new ColumnValueBean(protocol.getTitle());
        final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
        columnValueBeanList.add(columnValueBean);
        try
        {

            final List<String> titleList = jdbcDao.executeQuery(queryStr, null, columnValueBeanList);
            if (!titleList.isEmpty())
            {
                LOGGER.debug("Collection Protocol with the same Title already exists");
                throw getBizLogicException(null, "collprot.title.exists", "");
            }
        }
        catch (final DAOException e1)
        {
            CollectionProtocolBizLogic.LOGGER.error(e1.getMessage(), e1);
            throw getBizLogicException(e1, e1.getErrorKeyName(), e1.getMsgValues());
        }
        finally
        {
            closeJDBCSession(jdbcDao);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * edu.wustl.common.bizlogic.DefaultBizLogic#postUpdate(edu.wustl.dao.DAO,
     * java.lang.Object, java.lang.Object,
     * edu.wustl.common.beans.SessionDataBean)
     */
    @Override
    public void postUpdate(final DAO dao, final Object currentObj, final Object oldObj,
            final SessionDataBean sessionDataBean) throws BizLogicException
    {
        if (currentObj instanceof CollectionProtocolDTO)
        {
            final CollectionProtocolDTO cpDto = (CollectionProtocolDTO) currentObj;
            cpDto.getCollectionProtocol();
        }
        // else
        // {
        // }
        // Commented by Geeta for removing teh CP
        /*
         * ParticipantRegistrationCacheManager
         * participantRegistrationCacheManager = new
         * ParticipantRegistrationCacheManager();
         * participantRegistrationCacheManager
         * .updateCPTitle(collectionProtocol.getId
         * (),collectionProtocol.getTitle());
         *
         *
         * participantRegistrationCacheManager.updateCPShortTitle(collectionProtocol
         * .getId(), collectionProtocol.getShortTitle());
         *
         * if (collectionProtocol.getActivityStatus().equals(
         * Status.ACTIVITY_STATUS_DISABLED.toString())) {
         * participantRegistrationCacheManager
         * .removeCP(collectionProtocol.getId()); }
         */
        super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
    }

    // mandar : 31-Jan-07 ----------- consents tracking
    /**
     * @param collectionProtocol
     */
    private void verifyConsentsWaived(final CollectionProtocol collectionProtocol)
    {
        // check for consentswaived
        if (collectionProtocol.getConsentsWaived() == null)
        {
            collectionProtocol.setConsentsWaived(Boolean.FALSE);
        }
    }

    /**
     * Patch Id : FutureSCG_7 Description : method to validate the CPE against
     * uniqueness
     */
    protected void isCollectionProtocolLabelUnique(final CollectionProtocol collectionProtocol)
            throws BizLogicException
    {
        if (collectionProtocol != null)
        {
            final HashSet<String> cpLabelsSet = new HashSet<String>();
            final Collection<CollectionProtocolEvent> collectionProtocolEventCollection = collectionProtocol
                    .getCollectionProtocolEventCollection();
            if (!collectionProtocolEventCollection.isEmpty())
            {
                final Iterator iterator = collectionProtocolEventCollection.iterator();
                while (iterator.hasNext())
                {
                    final CollectionProtocolEvent event = (CollectionProtocolEvent) iterator.next();
                    final String label = event.getCollectionPointLabel();
                    if (cpLabelsSet.contains(label))
                    {
                        String arguments[] = null;
                        arguments = new String[] { "Collection Protocol Event", "Collection point label" };
                        final String errMsg = new DefaultExceptionFormatter().getErrorMessage(
                                "Err.ConstraintViolation", arguments);
                        LOGGER.debug("Unique Constraint Violated: " + errMsg);
                        // throw new DAOException(errMsg);
                    }
                    else
                    {
                        cpLabelsSet.add(label);
                    }
                }
            }
        }
    }

    /**
     * this function retrieves collection protocol and all nested child objects
     * and populates bean objects.
     *
     * @param id
     *            id of CP
     * @return list with collection protocol object and hashmap of collection
     *         protocol events
     * @throws BizLogicException
     */
    /**
     * this function retrieves collection protocol and all nested child objects
     * and populates bean objects.
     *
     * @param id
     *            id of CP
     * @return list with collection protocol object and hashmap of collection
     *         protocol events
     * @throws BizLogicException
     *             TO DO: Move to CollectionProtocolUtil
     */
    public List retrieveCP(final Long id) throws BizLogicException
    {

        final DAO dao = openDAOSession(null);
        try
        {
            final Object object = dao.retrieveById(CollectionProtocol.class.getName(), id);
            if (object == null)
            {

                throw getBizLogicException(null, "cp.retrive.err", id.toString());
            }
            final CollectionProtocol collectionProtocol = (CollectionProtocol) object;
            final CollectionProtocolBean collectionProtocolBean = CollectionProtocolUtil
                    .getCollectionProtocolBean(collectionProtocol);
            final Collection<CollectionProtocolEvent> collectionProtocolEventColl = collectionProtocol
                    .getCollectionProtocolEventCollection();
            final List<CollectionProtocolEvent> cpEventList = new LinkedList<CollectionProtocolEvent>(
                    collectionProtocolEventColl);
            CollectionProtocolUtil.getSortedCPEventList(cpEventList);
            final LinkedHashMap<String, CollectionProtocolEventBean> eventMap = CollectionProtocolUtil
                    .getCollectionProtocolEventMap(cpEventList, dao);

            final List<Object> cpList = new ArrayList<Object>();
            cpList.add(collectionProtocolBean);
            cpList.add(eventMap);
            return cpList;
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        finally
        {
            closeDAOSession(dao);
        }
    }

    @Override
    public List<CollectionProtocol> retrieve(final String className, final String colName, final Object colValue)
            throws BizLogicException
    {

        List<CollectionProtocol> cpList = null;
        final DAO dao = openDAOSession(null);
        try
        {
            cpList = dao.retrieve(className, colName, colValue);
            if (isCPListEmpty(cpList))
            {
                throw getBizLogicException(null, "cp.incorrect.param", colName + ":" + colValue);
            }

            final Iterator<CollectionProtocol> iterator = cpList.iterator();
            while (iterator.hasNext())
            {
                loadCP(iterator.next());
            }

        }
        catch (final DAOException daoExp)
        {
            LOGGER.debug(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        finally
        {
            try
            {
                dao.commit();
                closeDAOSession(dao);
            }
            catch (final DAOException daoExp)
            {
                CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
                throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
            }

        }
        return cpList;
    }

    /**
     * @param cpList
     * @return
     */
    private boolean isCPListEmpty(final List<CollectionProtocol> cpList)
    {
        return cpList == null || cpList.isEmpty();
    }

    public CollectionProtocol retrieveB(final String className, final Long id) throws BizLogicException
    {
        CollectionProtocol collectionProtocol = null;
        final DAO dao = openDAOSession(null);
        try
        {
            final Object object = dao.retrieveById(className, id);
            if (object == null)
            {
                throw getBizLogicException(null, "cp.retrive.err", id.toString());
            }

            collectionProtocol = (CollectionProtocol) object;
            loadCP(collectionProtocol);

        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
        }
        finally
        {
            try
            {
                dao.commit();
                closeDAOSession(dao);
            }
            catch (final DAOException daoExp)
            {
                CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
                throw getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
            }

        }
        return collectionProtocol;

    }

    /**
     * populate the CP with associations which otherwise will be loaded lazily
     *
     * @param collectionProtocol
     */
    private void loadCP(final CollectionProtocol collectionProtocol)
    {
        final Collection<CollectionProtocolEvent> collectionEventCollection = collectionProtocol
                .getCollectionProtocolEventCollection();
        final Iterator<CollectionProtocolEvent> cpIterator = collectionEventCollection.iterator();
        while (cpIterator.hasNext())
        {
            final CollectionProtocolEvent collectionProtocolEvent = cpIterator.next();
            final Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
                    .getSpecimenRequirementCollection();
            final Iterator<SpecimenRequirement> specimenIterator = specimenCollection.iterator();
            while (specimenIterator.hasNext())
            {
                specimenIterator.next();
            }
        }
        collectionProtocol.getCollectionProtocolRegistrationCollection();

    }

    /**
     * Custom method for Collection Protocol case
     *
     * @param dao
     * @param domainObject
     * @param sessionDataBean
     * @return
     */
    @Override
    public String getObjectId(final DAO dao, final Object domainObject)
    {
        CollectionProtocolDTO cpDTO = null;
        Map<String, SiteUserRolePrivilegeBean> cpRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
        final Collection<Site> siteCollection = new ArrayList<Site>();

        if (domainObject instanceof CollectionProtocolDTO)
        {
            cpDTO = (CollectionProtocolDTO) domainObject;
            cpDTO.getCollectionProtocol();
            cpRowIdMap = cpDTO.getRowIdBeanMap();
        }

        if (cpRowIdMap != null)
        {
            final Object[] mapKeys = cpRowIdMap.keySet().toArray();
            for (final Object mapKey : mapKeys)
            {
                final String key = mapKey.toString();
                final SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = cpRowIdMap.get(key);

                siteCollection.add(siteUserRolePrivilegeBean.getSiteList().get(0));
            }
        }

        final StringBuffer sb = new StringBuffer();
        boolean hasProtocolAdministrationPrivilege = false;

        if (siteCollection != null && !siteCollection.isEmpty())
        {
            sb.append(Constants.SITE_CLASS_NAME);
            for (final Site site : siteCollection)
            {
                if (site.getId() != null)
                {
                    sb.append(Constants.UNDERSCORE).append(site.getId());
                    hasProtocolAdministrationPrivilege = true;
                }
            }
        }

        if (hasProtocolAdministrationPrivilege)
        {
            return sb.toString();
        }

        return null;
    }

    /**
     * To get PrivilegeName for authorization check from
     * 'PermissionMapDetails.xml' (non-Javadoc)
     *
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
     */
    @Override
    protected String getPrivilegeKey(final Object domainObject)
    {
        return Constants.ADD_EDIT_CP;
    }

    public String getShortTitle(final Long cpId) throws BizLogicException
    {
        String shortTitle = null;

        final Object object = this.retrieveAttribute(CollectionProtocol.class, cpId, Constants.shortTitle);

        if (object == null)
        {
            throw getBizLogicException(null, "cp.shr.title.incorrect", cpId.toString());
        }

        shortTitle = (String) object;
        return shortTitle;
    }

    /**
     * Over-ridden for the case of Site Admin user should be able to create
     * Collection Protocol for Site to which he belongs (non-Javadoc)
     *
     * @throws BizLogicException
     * @see edu.wustl.common.bizlogic.DefaultBizLogic#isAuthorized(edu.wustl.common.dao.DAO,
     *      java.lang.Object, edu.wustl.common.beans.SessionDataBean)
     */
    @Override
    public boolean isAuthorized(final DAO dao, final Object domainObject, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {

        boolean isAuthorized = false;

        isAuthorized = checkCP(domainObject, sessionDataBean);
        if (isAuthorized)
        {
            return true;
        }

        final String privilegeName = getPrivilegeName(domainObject);
        final String protectionElementName = getObjectId(dao, domainObject);

        isAuthorized = AppUtility.returnIsAuthorized(sessionDataBean, privilegeName, protectionElementName);

        return isAuthorized;
    }

    /**
     * @param domainObject
     *            .
     * @param sessionDataBean
     * @return
     * @throws BizLogicException
     */
    private boolean checkCP(final Object domainObject, final SessionDataBean sessionDataBean)
            throws BizLogicException
    {
        CollectionProtocolDTO cpDTO = null;
        Map<String, SiteUserRolePrivilegeBean> cpRowIdMap = new HashMap<String, SiteUserRolePrivilegeBean>();
        if (sessionDataBean != null && sessionDataBean.isAdmin())
        {
            return true;
        }

        /*
         * if (domainObject instanceof CollectionProtocol) { }
         */
        if (domainObject instanceof CollectionProtocolDTO)
        {
            cpDTO = (CollectionProtocolDTO) domainObject;
            cpDTO.getCollectionProtocol();
            cpRowIdMap = cpDTO.getRowIdBeanMap();

            if (cpRowIdMap == null)
            {
                throw getBizLogicException(null, "cp.siteIsRequired", "");
            }
            else
            {
                final Object[] mapKeys = cpRowIdMap.keySet().toArray();
                for (final Object mapKey : mapKeys)
                {
                    final String key = mapKey.toString();
                    final SiteUserRolePrivilegeBean bean = cpRowIdMap.get(key);

                    if (bean.getSiteList() == null || bean.getSiteList().isEmpty())
                    {
                        throw getBizLogicException(null, "cp.siteIsRequired", "");
                    }
                }

            }
        }
        return false;
    }

    /**
     * Get Related Sites.
     *
     * @param dao
     *            DAO
     * @param cpId
     *            Long
     * @return Collection of Site
     * @throws DAOException
     *             DAOException
     * @throws BizLogicException
     *             BizLogicException
     */
    public Collection<Site> getRelatedSites(final DAO dao, final Long cpId) throws BizLogicException, DAOException
    {
        Collection<Site> siteCollection = null;
        if (cpId == null || cpId == 0)
        {
            return null;
        }
        // Query changed for bug #16295
        final String query = "select cp.siteCollection.id "
                + " from edu.wustl.catissuecore.domain.CollectionProtocol cp " + " where cp.id = " + cpId;
        final List<Long> list = executeQuery(query);
        if (list != null && !list.isEmpty())
        {
            siteCollection = new HashSet<Site>();
            for (final Long siteId : list)
            {
                final Site site = new Site();
                site.setId(siteId);
                siteCollection.add(site);
            }
        }
        return siteCollection;
    }

    @Override
    public boolean isReadDeniedTobeChecked()
    {
        return true;
    }

    @Override
    public String getReadDeniedPrivilegeName()
    {
        return Permissions.READ_DENIED;
    }

    /**
     * @param id
     *            Identifier of the StorageContainer related to which the
     *            collectionProtocol titles are to be retrieved.
     * @return List of collectionProtocol title.
     * @throws BizLogicException
     *             throws BizLogicException
     */
    public static List getCollectionProtocolList(final String id) throws ApplicationException
    {
        final String sql = " SELECT SP.TITLE TITLE FROM CATISSUE_SPECIMEN_PROTOCOL SP, CATISSUE_ST_CONT_COLL_PROT_REL SC "
                + " WHERE SP.IDENTIFIER = SC.COLLECTION_PROTOCOL_ID AND SC.STORAGE_CONTAINER_ID = ? ";

        final ColumnValueBean columnValueBean = new ColumnValueBean(id);
        final List<ColumnValueBean> columnValueBeanList = new ArrayList<ColumnValueBean>();
        columnValueBeanList.add(columnValueBean);

        final List resultList = AppUtility.executeSQLQuery(sql, columnValueBeanList);
        final Iterator iterator = resultList.iterator();
        final List returnList = new ArrayList();
        while (iterator.hasNext())
        {
            final List list = (List) iterator.next();
            final String data = (String) list.get(0);
            returnList.add(data);
        }
        if (returnList.isEmpty())
        {
            returnList.add(new String(Constants.ALL));
        }
        return returnList;
    }

    /**
     * To check weather the Container to display can holds the given
     * CollectionProtocol.
     *
     * @param collectionProtocolId
     *            The collectionProtocol Id.
     * @param storageContainer
     *            The StorageContainer reference to be displayed on the page.
     * @return true if the given container can hold the CollectionProtocol.
     * @throws BizLogicException
     *             throws BizLogicException
     */
    public boolean canHoldCollectionProtocol(final long collectionProtocolId,
            final StorageContainer storageContainer) throws BizLogicException
    {
        boolean canHold = true;
        final Collection collectionProtocols = (Collection) this.retrieveAttribute(StorageContainer.class
                .getName(), storageContainer.getId(), "elements(collectionProtocolCollection)");
        if (!collectionProtocols.isEmpty())
        {
            final Iterator itr = collectionProtocols.iterator();
            canHold = false;
            while (itr.hasNext())
            {
                final CollectionProtocol cp = (CollectionProtocol) itr.next();
                if (cp.getId().longValue() == collectionProtocolId)
                {
                    return true;
                }
            }
        }
        return canHold;
    }

    /**
     * Gives List of NameValueBean of CP for given cpIds array.
     *
     * @param cpIds
     *            - Long array of cpIds
     * @return NameValueBean - List of NameValueBean
     */
    public List<NameValueBean> getCPNameValueList(final long[] cpIds) throws BizLogicException
    {
        DAO dao = null;
        final List<NameValueBean> cpNameValueList = new ArrayList<NameValueBean>();
        try
        {
            dao = openDAOSession(null);
            NameValueBean cpNameValueBean;
            for (final long cpId : cpIds)
            {
                if (cpId != -1)
                {
                    CollectionProtocol protocol = new CollectionProtocol();

                    protocol = (CollectionProtocol) dao.retrieveById(CollectionProtocol.class.getName(), cpId);

                    final String cpShortTitle = protocol.getShortTitle();
                    cpNameValueBean = new NameValueBean(cpShortTitle, cpId);
                    cpNameValueList.add(cpNameValueBean);
                }
            }
        }
        catch (final DAOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        finally
        {
            closeDAOSession(dao);
        }

        return cpNameValueList;
    }

    /**
     * Method to get the id and activity status of a user based on the login
     * name.
     *
     * @param dao
     * @param loginName
     * @return
     */
    private List getUserByLoginName(final DAO dao, final String loginName) throws BizLogicException
    {
        List list = null;
        try
        {
            final String column = "loginName";
            final String sourceObjectName = User.class.getName();
            final String[] selectColumnName = { "id", "activityStatus" };
            final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
            queryWhereClause.addCondition(new EqualClause(column, loginName));
            list = dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
        }
        catch (final DAOException daoExp)
        {
            CollectionProtocolBizLogic.LOGGER.error(daoExp.getMessage(), daoExp);
            /*
             * throw this.getBizLogicException(null, "errors.entity.notExists",
             * "Coordinator");
             */
            throw new BizLogicException(daoExp.getErrorKey(), daoExp, daoExp.getMessage());
        }
        return list;
    }
}
