/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.deintegration.DEIntegration;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * @author sandeep_chinta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class AnnotationBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * logger object.
	 */
	private static final Logger logger = Logger.getCommonLogger(AnnotationBizLogic.class);

	/**
	 *  public constructor.
	 */
	public AnnotationBizLogic()
	{
		super();
	}

	/**
	 * @param staticEntityId staticEntityId.
	 * @return List of all dynamic entities Objects from a given static entity
	 * eg: returns all dynamic entity objects from a Participant,Specimen etc
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public List getListOfDynamicEntities(long staticEntityId)
			throws DynamicExtensionsSystemException
	{
		/**
		 * get all associated DE entities with static entity and get its container id
		 */
		DEIntegration integration = new DEIntegration();
		Collection containerList = integration
				.getCategoriesContainerIdFromHookEntity(staticEntityId);
		Collection containerList1 = integration
				.getDynamicEntitiesContainerIdFromHookEntity(staticEntityId);
		List list = new ArrayList(containerList);
		list.addAll(containerList1);
		return list;

	}

	/**
	 * This method called to delete deleteAnnotationRecords.
	 * @param containerId : containerId.
	 * @param recordIdList : recordIdList.
	 * @throws BizLogicException : BizLogicException.
	 */
	/*public void deleteAnnotationRecords(Long containerId, List<Long> recordIdList)
			throws BizLogicException
	{
		final EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
		try
		{
			entityManagerInterface.deleteRecords(containerId, recordIdList);
		}
		catch (final Exception e)
		{
			AnnotationBizLogic.logger.error(e.getMessage(), e);
		}
	}*/

	/**
	 * Deletes an object from the database.
	 * @param obj The object to be deleted.
	 * @param dao dao
	 * @throws BizLogicException
	 */
	@Override
	protected void delete(Object obj, DAO dao)
	{
		try
		{
			dao.delete(obj);
		}
		catch (final DAOException e)
		{
			AnnotationBizLogic.logger.error(e.getMessage(), e);
		}
	}

	/**
	 * This method gets Annotation Ids Based On Condition.
	 * @param dynEntitiesList dynEntities List
	 * @param cpIdList cpId List
	 * @return dynEntitiesIdList
	 * @throws BizLogicException BizLogic Exception
	 */
	public List getAnnotationIdsBasedOnCondition(List dynEntitiesList, List cpIdList)
			throws BizLogicException
	{
		final List dynEntitiesIdList = new ArrayList();
		if (dynEntitiesList != null && !dynEntitiesList.isEmpty())
		{
			final Iterator dynEntitiesIterator = dynEntitiesList.iterator();
			while (dynEntitiesIterator.hasNext())
			{
				NameValueBean nvBean = (NameValueBean) dynEntitiesIterator.next();
				String containerId = nvBean.getValue();

				StudyFormContext studyFormContext = getStudyFormContext(Long.valueOf(containerId));
				if (studyFormContext != null)
				{
					if (!studyFormContext.getHideForm())
					{
						//dynEntitiesIdList.add(Long.valueOf(containerId));
						Collection<CollectionProtocol> coll = studyFormContext
								.getCollectionProtocolCollection();
						if (coll != null && !coll.isEmpty())
						{
							for (CollectionProtocol cp : coll)
							{
								if (cpIdList.contains(cp.getId()))
								{
									dynEntitiesIdList.add(Long.valueOf(containerId));
								}
							}
						}
						else
						{
							dynEntitiesIdList.add(Long.valueOf(containerId));
						}
					}
					/*else if("NONE".equals(studyFormContext.getEntityMapCondition()))
					{
						Collection coll = studyFormContext.getCollectionProtocolCollection();
						if(coll != null && !coll.isEmpty())
						{
							dynEntitiesIdList.add(Long.valueOf(containerId));
						}
					}*/
				}
				else
				{
					dynEntitiesIdList.add(Long.valueOf(containerId));
				}
			}
		}

		return dynEntitiesIdList;
	}

	/**
	 * Get Study Form Context.
	 * @param containerId Long
	 * @throws BizLogicException BizLogicException
	 * @return StudyFormContext
	 */
	public StudyFormContext getStudyFormContext(Long containerId) throws BizLogicException
	{
		List<StudyFormContext> sfcList = this.retrieve(StudyFormContext.class.getName(),
				"containerId", containerId);
		if (sfcList != null && !sfcList.isEmpty())
		{
			return sfcList.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * this method returns DynamicRecord From association id.
	 * @param recEntryId Long
	 * @param containerId Long
	 * @param recEntryEntityId Long
	 * @return Collection
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 * @throws SQLException SQLException
	 * @throws DAOException DAOException
	 */
	public Collection getDynamicRecordIdFromStaticId(Long recEntryId, Long containerId,
			Long recEntryEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, SQLException, DAOException
	{
		DEIntegration integrate = new DEIntegration();
		Collection recList = integrate.getDynamicRecordFromStaticId(recEntryId.toString(),
				containerId, recEntryEntityId.toString());

		return recList;
	}

	/**
	 * This method is called to associate records.
	 * @param containerId
	 * @param staticEntyRecId
	 * @param dynaEntyRecId
	 * @param staticEntityId
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public void associateRecords(Long containerId, Long staticEntyRecId, Long dynaEntyRecId,
			Long staticEntityId) throws DynamicExtensionsSystemException, BizLogicException,
			DAOException
	{
		DEIntegration integrate = new DEIntegration();
		integrate.associateRecords(containerId, staticEntyRecId, dynaEntyRecId, staticEntityId);
	}

	/**
	 *
	 * @param request
	 * @param dynExtRecordId
	 * @param dynEntContainerId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	/*public Long getRecordEntryId(HttpServletRequest request, String dynExtRecordId,
			String dynEntContainerId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, ApplicationException
	{
		String staticEntityId = (String) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_ENTITYID);

		DEIntegration integrate = new DEIntegration();
		Long recordEntryId = integrate.getHookEntityRecordId(staticEntityId, dynEntContainerId,
				dynExtRecordId);

		return recordEntryId;
	}*/
	/**
	 * Create Record Entry.
	 * @param staticEntityName String
	 * @param selectedStaticEntityRecordId String
	 * @return AbstractRecordEntry
	 */
	public AbstractRecordEntry createRecordEntry(String staticEntityName,
			String selectedStaticEntityRecordId)
	{
		AbstractRecordEntry abstractRecordEntry = null;
		if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			abstractRecordEntry = new ParticipantRecordEntry();
			Participant participant = new Participant();
			participant.setId(Long.valueOf(selectedStaticEntityRecordId));
			((ParticipantRecordEntry) abstractRecordEntry).setParticipant(participant);

		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY))
		{
			abstractRecordEntry = new SpecimenRecordEntry();
			Specimen specimen = new Specimen();
			specimen.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SpecimenRecordEntry) abstractRecordEntry).setSpecimen(specimen);
		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			abstractRecordEntry = new SCGRecordEntry();
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SCGRecordEntry) abstractRecordEntry).setSpecimenCollectionGroup(scg);
		}
		return abstractRecordEntry;
	}

	/**
	 * Get Study Form Context.
	 * @param dynEntContainerId String
	 * @return StudyFormContext
	 * @throws BizLogicException BizLogicException
	 * @throws NumberFormatException NumberFormatException
	 */
	public StudyFormContext getStudyFormContext(String dynEntContainerId) throws BizLogicException,
			NumberFormatException
	{
		StudyFormContext studyFormContext = this.getStudyFormContext(Long
				.valueOf(dynEntContainerId));
		if (studyFormContext == null)
		{
			studyFormContext = new StudyFormContext();
			studyFormContext.setHideForm(false);
			studyFormContext.setContainerId(Long.valueOf(dynEntContainerId));
			this.insert(studyFormContext);
		}
		return studyFormContext;
	}

	/**
	 * Insert Abstract Record Entry.
	 * @param recordEntry AbstractRecordEntry
	 * @return AbstractRecordEntry
	 * @throws BizLogicException BizLogicException
	 */
	public AbstractRecordEntry insertAbstractRecordEntry(AbstractRecordEntry recordEntry)
			throws BizLogicException
	{
		this.insert(recordEntry);
		return recordEntry;
	}

	/**
	 * Get Recory Entry Type Name From Static Object.
	 * @param staticObject Object
	 * @return String
	 * @throws ApplicationException ApplicationException
	 */
	public String getRecoryEntryTypeNameFromStaticObject(Object staticObject)
			throws ApplicationException
	{
		String recordEntryType = null;
		if (staticObject instanceof Participant)
		{
			recordEntryType = AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY;
		}
		else if (staticObject instanceof SpecimenCollectionGroup)
		{
			recordEntryType = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
		}
		else if (staticObject instanceof Specimen)
		{
			recordEntryType = AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY;
		}
		else
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("errors.item.invalid");
			throw new ApplicationException(errorkey, null, "CaTissue Hook Entity");
		}
		return recordEntryType;
	}

	/**
	 * Get Static Type Object.
	 * @param staticObject Object
	 * @return String
	 * @throws ApplicationException ApplicationException
	 */
	public String getStaticTypeObject(Object staticObject) throws ApplicationException
	{
		String staticObjectType = null;
		if (staticObject instanceof Participant)
		{
			staticObjectType = AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID;
		}
		else if (staticObject instanceof SpecimenCollectionGroup)
		{
			staticObjectType = AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID;
		}
		else if (staticObject instanceof Specimen)
		{
			staticObjectType = AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID;
		}
		else
		{
			ErrorKey errorkey = ErrorKey.getErrorKey("errors.item.invalid");
			throw new ApplicationException(errorkey, null, "CaTissue Hook Entity");
		}
		return staticObjectType;
	}

	/**
	 * This method will create a hooke object of the given staticEntityId & associate it with
	 * the given dynExtRecordId for form with container id dynEntContainerId.
	 * @param dynExtRecordId dynamic record id.
	 * @param dynEntContainerId root container id of the form.
	 * @param staticEntityName name of the static entity.
	 * @param selectedStaticEntityRecordId static entity record id with which to hook.
	 * @param staticEntityId metadata entity id of the static entity.
	 * @param sessionDataBean session data bean.
	 * @throws BizLogicException exception.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	public Long createHookEntityObject(String dynExtRecordId, String dynEntContainerId,
			String staticEntityName, String selectedStaticEntityRecordId, String staticEntityId,
			final SessionDataBean sessionDataBean) throws BizLogicException,
			DynamicExtensionsSystemException, DAOException
	{

		AbstractRecordEntry abstractRecordEntry = createRecordEntry(staticEntityName,
				selectedStaticEntityRecordId);
		abstractRecordEntry.setActivityStatus("Active");
		abstractRecordEntry.setModifiedDate(new Date());

		if (sessionDataBean != null)
		{
			abstractRecordEntry.setModifiedBy(sessionDataBean.getLastName() + ","
					+ sessionDataBean.getFirstName());
		}
		StudyFormContext studyFormContext = getStudyFormContext(dynEntContainerId);
		abstractRecordEntry.setFormContext(studyFormContext);
		abstractRecordEntry = insertAbstractRecordEntry(abstractRecordEntry);
		Long recordEntryId = abstractRecordEntry.getId();

		associateRecords(Long.valueOf(dynEntContainerId), recordEntryId, Long
				.valueOf(dynExtRecordId), Long.valueOf(staticEntityId));
		return recordEntryId;
	}

	/**
	 * This method will find the static entity with which the given form is associated.
	 * & it will return the name & id of that entity in the nameValueBean.
	 * @param rootContainerId container id of the form.
	 * @param formName name of the form.
	 * @return bean with name as static entity name & value as its entity id.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws ApplicationException
	 */
	public NameValueBean getHookEntityNameValueBeanForCategory(Long rootContainerId, String formName)
			throws DynamicExtensionsSystemException, ApplicationException
	{

		Long tgtEntityId = EntityManager.getInstance().getCategoryRootEntityByContainerId(
				rootContainerId).getId();
		NameValueBean bean = getHookEntiyNameValueBean(tgtEntityId, formName);
		return bean;
	}

	/**
	 * This method will find the static entity with which the given entity is associated.
	 * & it will return the name & id of that entity in the nameValueBean.
	 * @param tgtEntityId Entity id.
	 * @param formName name of the form.
	 * @return bean with name as static entity name & value as its entity id.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws ApplicationException exception.
	 */
	public NameValueBean getHookEntiyNameValueBean(Long tgtEntityId, String formName)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Long participantRecEntryId = AnnotationUtil
				.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
		Long specimenRecEntryId = AnnotationUtil
				.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
		Long scgRecEntryId = AnnotationUtil
				.getEntityId(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);
		NameValueBean bean = new NameValueBean();
		if (!entityManager.getAssociationIds(participantRecEntryId, tgtEntityId).isEmpty())
		{
			bean.setName(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
			bean.setValue(participantRecEntryId);
		}
		else if (!entityManager.getAssociationIds(specimenRecEntryId, tgtEntityId).isEmpty())
		{
			bean.setName(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
			bean.setValue(specimenRecEntryId);
		}
		else if (!entityManager.getAssociationIds(scgRecEntryId, tgtEntityId).isEmpty())
		{
			bean.setName(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY);
			bean.setValue(scgRecEntryId);
		}
		else
		{
			throw new ApplicationException(ErrorKey.getErrorKeyObject("bo.error.no.hookentity"), null,
					formName);
		}
		return bean;
	}

	/**
	 * It will return the id of the specimen whose label is equal to given specimenLabel.
	 * @param specimenLabel label of specimen whose id is needed.
	 * @return id of the specimen.
	 * @throws ApplicationException
	 */
	public Long getSpecimenByLabel(String specimenLabel) throws ApplicationException
	{
		Long specimenId = null;
		if (specimenLabel != null)
		{
			final String hql = "select specimen.id from " + Specimen.class.getName()
					+ " as specimen where specimen.label = ? and specimen.activityStatus <> '"
					+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' ";

			List<ColumnValueBean> columnValueBean = new ArrayList<ColumnValueBean>();
			columnValueBean.add(new ColumnValueBean("label",specimenLabel));
			final List<Long> list = this.executeQuery(hql,columnValueBean);
			if (list == null || list.isEmpty())
			{
				throw new BizLogicException(ErrorKey.getErrorKey("invalid.label.specimen"), null,
						specimenLabel);

			}
			specimenId = list.get(0);
		}
		return specimenId;
	}

	/**
	 * It will return the id of the specimen whose barcode is equal to given specimenBarcode.
	 * @param specimenBarcode barcode of specimen whose id is needed.
	 * @return id of the specimen.
	 * @throws ApplicationException
	 */
	public Long getSpecimenByBarcode(String specimenBarcode) throws ApplicationException
	{
		Long specimenId = null;
		if (specimenBarcode != null)
		{
			final String hql = "select specimen.id from " + Specimen.class.getName()
					+ " as specimen where specimen.barcode = ? and specimen.activityStatus <> '"
					+ Status.ACTIVITY_STATUS_DISABLED.toString() + "' ";

			List<ColumnValueBean> columnValueBean = new ArrayList<ColumnValueBean>();
			columnValueBean.add(new ColumnValueBean("barcode",specimenBarcode));
			final List<Long> list = this.executeQuery(hql,columnValueBean);
			if (list == null || list.isEmpty())
			{
				throw new BizLogicException(ErrorKey.getErrorKey("invalid.barcode.specimen"), null,
						specimenBarcode);
			}
			specimenId = list.get(0);
		}
		return specimenId;
	}

	/**
	 * This method will check whether the specimen with given id exists or not.
	 * @param specimenId specimen Id.
	 * @return true if specimen with given id exists.
	 * @throws BizLogicException exception.
	 */
	public boolean isSpecimenExists(String specimenId) throws BizLogicException
	{
		final String hql = "select specimen.id from " + Specimen.class.getName()
				+ " as specimen where specimen.id = " + specimenId
				+ " and specimen.activityStatus <> '" + Status.ACTIVITY_STATUS_DISABLED.toString()
				+ "' ";

		final List<Long> list = this.executeQuery(hql);
		if (list == null || list.isEmpty())
		{
			throw new BizLogicException(ErrorKey.getErrorKey("invalid.id.specimen"), null,
					specimenId);
		}
		return true;
	}

}