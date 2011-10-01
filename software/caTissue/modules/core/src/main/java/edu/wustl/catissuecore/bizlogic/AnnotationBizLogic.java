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
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;


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
	 * @throws DynamicExtensionsCacheException
	 */
	public List getListOfDynamicEntities(long staticEntityId) throws DynamicExtensionsSystemException, DynamicExtensionsCacheException
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
				if(studyFormContext != null)
				{
					if(!studyFormContext.getHideForm())
					{
						//dynEntitiesIdList.add(Long.valueOf(containerId));
						Collection<CollectionProtocol> coll = studyFormContext
								.getCollectionProtocolCollection();
						if(coll != null && !coll.isEmpty())
						{
							for(CollectionProtocol cp : coll)
							{
								if(cpIdList.contains(cp.getId()))
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
		if(sfcList != null && !sfcList.isEmpty())
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
		Collection recList = integrate.getDynamicRecordFromStaticId(recEntryId.toString(), containerId,
				recEntryEntityId.toString());

		return recList;
	}

	/**
	 * This method is called to associate records.
	 * @param containerId
	 * @param staticEntyRecId
	 * @param dynaEntyRecId
	 * @param staticEntityId
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws ApplicationException
	 */
	public void associateRecords(Long containerId, Long staticEntyRecId, Long dynaEntyRecId,
			Long staticEntityId) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, ApplicationException
	{
		DEIntegration integrate = new DEIntegration();
		integrate.associateRecords(containerId, staticEntyRecId, dynaEntyRecId, staticEntityId, null);
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
			InstanceFactory<ParticipantRecordEntry> partInstFact = DomainInstanceFactory.getInstanceFactory(ParticipantRecordEntry.class);
			abstractRecordEntry = partInstFact.createObject();//new ParticipantRecordEntry();
			InstanceFactory<Participant> instFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
			Participant participant =instFact.createObject(); //new Participant();
			participant.setId(Long.valueOf(selectedStaticEntityRecordId));
			((ParticipantRecordEntry)abstractRecordEntry).setParticipant(participant);

		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY))
		{
			InstanceFactory<SpecimenRecordEntry> specInstFact = DomainInstanceFactory.getInstanceFactory(SpecimenRecordEntry.class);
			abstractRecordEntry =specInstFact.createObject();// new SpecimenRecordEntry();
			InstanceFactory<Specimen> instFact = DomainInstanceFactory.getInstanceFactory(Specimen.class);
			Specimen specimen = instFact.createObject();//new Specimen();
			specimen.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SpecimenRecordEntry)abstractRecordEntry).setSpecimen(specimen);
		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			InstanceFactory<SCGRecordEntry> recordEntryInstFact = DomainInstanceFactory.getInstanceFactory(SCGRecordEntry.class);
			abstractRecordEntry = recordEntryInstFact.createObject();//new SCGRecordEntry();
			InstanceFactory<SpecimenCollectionGroup> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCollectionGroup.class);
			SpecimenCollectionGroup scg = instFact.createObject();//new SpecimenCollectionGroup();
			scg.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SCGRecordEntry)abstractRecordEntry).setSpecimenCollectionGroup(scg);
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
	public StudyFormContext getStudyFormContext(String dynEntContainerId)
	throws BizLogicException, NumberFormatException
	{
		StudyFormContext studyFormContext = this.getStudyFormContext(Long
				.valueOf(dynEntContainerId));
		if (studyFormContext == null)
		{
			InstanceFactory<StudyFormContext> studyFormInstFact = DomainInstanceFactory.getInstanceFactory(StudyFormContext.class);
			studyFormContext = studyFormInstFact.createObject();//new StudyFormContext();
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
		if(staticObject instanceof Participant)
		{
			recordEntryType = AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY;
		}
		else if(staticObject instanceof SpecimenCollectionGroup)
		{
			recordEntryType = AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY;
		}
		else if(staticObject instanceof Specimen)
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
		if(staticObject instanceof Participant)
		{
			staticObjectType = AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID;
		}
		else if(staticObject instanceof SpecimenCollectionGroup)
		{
			staticObjectType = AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID;
		}
		else if(staticObject instanceof Specimen)
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
}