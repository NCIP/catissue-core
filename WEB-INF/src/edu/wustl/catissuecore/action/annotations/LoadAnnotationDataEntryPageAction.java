/*
 * @author
 */

package edu.wustl.catissuecore.action.annotations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.common.dynamicextensions.entitymanager.DynamicExtensionsQueryBuilderConstantsInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.catissuecore.actionForm.AnnotationDataEntryForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.annotations.ICPCondition;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;

/**
 * @author preeti_munot To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments Load data for AnnotationDataEntryPage.jsp. Loads data of
 *         linked dynamic entities and also annotation data that has been added
 *         Also processes response from DE data entry action and stores relative
 *         data to DB
 */
public class LoadAnnotationDataEntryPageAction extends BaseAction
		implements
			DynamicExtensionsQueryBuilderConstantsInterface
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger( LoadAnnotationDataEntryPageAction.class );

	/**
	 * @param mapping - mapping
	 * @param form - ActionForm
	 * @param request - HttpServletRequest object
	 * @param response - HttpServletResponse
	 * @return ActionForward
	 * @throws Exception - Exception
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ActionForward actionfwd = null;
		final AnnotationDataEntryForm annotationDataEntryForm = (AnnotationDataEntryForm) form;
		String staticEntityId = null, staticEntityName = null, staticEntityRecordId = null;

		final String operation = request.getParameter( AnnotationConstants.OPERATION );
		if (operation != null && !operation.equals( AnnotationConstants.NULL )
				&& ( operation.equalsIgnoreCase( AnnotationConstants.EDIT_SELECTED_ANNOTATION ) ))
		{
			// If operation not null -> Operation
			String selectedFormId = request.getParameter( AnnotationConstants.SELECTED_ANNOTATION );
			staticEntityId = request.getParameter( AnnotationConstants.STATIC_ENTITY_ID );
			staticEntityRecordId = request
					.getParameter( AnnotationConstants.STATIC_ENTITY_RECORD_ID );

			final String definedAnnotationsDataXML = this.processEditOperation( request, AppUtility
					.toLong( staticEntityId ), AppUtility.toLong( staticEntityRecordId ),
					selectedFormId );
			annotationDataEntryForm.setDefinedAnnotationsDataXML( definedAnnotationsDataXML );

			actionfwd = mapping.findForward( AnnotationConstants.PAGE_OF_EDIT_ANNOTATION );
		}
		else
		{
			if (request.getParameter( WebUIManager.getOperationStatusParameterName() ) != null)
			{
				// Return from dynamic extensions
				staticEntityId = (String) request.getSession().getAttribute(
						AnnotationConstants.SELECTED_STATIC_ENTITYID );
				final String dynEntContainerId = request.getParameter( "containerId" );
				this.processResponseFromDynamicExtensions( request, dynEntContainerId );
				staticEntityRecordId = (String) request.getSession().getAttribute(
						AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID );
				if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityId( staticEntityId );
				}
				if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityRecordId( staticEntityRecordId );
				}
				staticEntityName = request
						.getParameter( edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME );

				if (staticEntityName != null)
				{
					request.getSession().setAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,
							staticEntityName );
				}
				if (staticEntityName == null)
				{
					staticEntityName = (String) request.getSession().getAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME );
				}
			}
			else
			{
				staticEntityName = request
						.getParameter( edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME );

				if (staticEntityName != null)
				{
					request.getSession().setAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME,
							staticEntityName );
				}
				if (staticEntityName == null)
				{
					staticEntityName = (String) request.getSession().getAttribute(
							edu.wustl.catissuecore.util.global.Constants.STATIC_ENTITY_NAME );
				}
				staticEntityId = request.getParameter( AnnotationConstants.REQST_PARAM_ENTITY_ID );
				staticEntityRecordId = request
						.getParameter( AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID );
				if (annotationDataEntryForm.getSelectedStaticEntityId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityId( staticEntityId );
				}
				if (annotationDataEntryForm.getSelectedStaticEntityRecordId() == null)
				{
					annotationDataEntryForm.setSelectedStaticEntityRecordId( staticEntityRecordId );
				}
				if (annotationDataEntryForm.getOperation() != null
						&& annotationDataEntryForm.getOperation()
								.equalsIgnoreCase( "deleteRecords" ))
				{
					staticEntityId = annotationDataEntryForm.getSelectedStaticEntityId();
					staticEntityRecordId = annotationDataEntryForm
							.getSelectedStaticEntityRecordId();
					this.deleteRecords( annotationDataEntryForm.getSelectedRecords() );

				}
				this.updateCache( request );
			}
			final String editOperation = request.getParameter( "editOperation" );
			if (editOperation != null && editOperation.trim().length() > 0
					&& ( editOperation.contains( AnnotationConstants.EDIT_SELECTED_ANNOTATION ) ))
			{
				final String[] parameters = editOperation.split( "@" );
				final String selectedFormId = ( parameters[1].split( "=" ) )[1].toString();
				final String definedAnnotationsDataXML = this.processEditOperation( request,
						AppUtility.toLong( staticEntityId ), AppUtility
								.toLong( staticEntityRecordId ), selectedFormId );
				annotationDataEntryForm.setDefinedAnnotationsDataXML( definedAnnotationsDataXML );
				actionfwd = mapping.findForward( AnnotationConstants.PAGE_OF_EDIT_ANNOTATION );
			}
			else
			{
				this.logger.info( "Updating for Entity Id " + staticEntityId );
				this.initializeDataEntryForm( request, staticEntityId, staticEntityRecordId,
						staticEntityName, annotationDataEntryForm );
				// String pageOf = request.getParameter(Constants.PAGE_OF);
				actionfwd = mapping.findForward( Constants.SUCCESS );
			}
		}
		return actionfwd;
	}

	/**
	 * @param selectedRecords : selectedRecords
	 * @throws Exception
	 */
	private void deleteRecords(String selectedRecords) throws Exception
	{
		/*final String[] recordArray = selectedRecords.split( "," );
		final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();*/
		// TODO: Check if the method is in use or not - Deepali
		logger.info("--------------------------------------");
		logger.info("NEED TO ADD CODE: LoadAnnotationDataEntryPageAction:deleteRecords");
		throw new Exception("Code not added for deletion after DE integration restructure changes");

		/*for (final String record : recordArray)
		{
			Object object = null;
			try
			{
				object = annotationBizLogic.retrieve( EntityMapRecord.class.getName(), Long
						.valueOf( record ) );

				if (object != null)
				{
					final EntityMapRecord entityMapRecord = (EntityMapRecord) object;

					final Object entityMapObject = annotationBizLogic.retrieve( EntityMap.class
							.getName(), entityMapRecord.getFormContext().getEntityMap().getId() );
					final EntityMap entityMap = entityMapObject == null
							? null
							: (EntityMap) entityMapObject;
					if (entityMap != null)
					{
						final List recordList = new ArrayList();
						recordList.add( entityMapRecord.getDynamicEntityRecordId() );
						annotationBizLogic.deleteAnnotationRecords( entityMap.getContainerId(),
								recordList );
						annotationBizLogic.delete( entityMapRecord, 0 );
					}
				}
			}
			catch (final BizLogicException e)
			{
				this.logger.error( e.getMessage(), e );
			}
		}*/

	}

	/**
	*
	 * @param request : request
	 */
	private void updateCache(HttpServletRequest request)
	{
		// Set into Cache
		// CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
		// .getInstance();
		final HttpSession session = request.getSession();

		if (session != null)
		{
			final String parentEntityId = request
					.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_ID);
			final String parentEntityRecordId = request
					.getParameter(AnnotationConstants.REQST_PARAM_ENTITY_RECORD_ID);
			final String entityIdForCondition = request
					.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_ID);
			final String entityRecordIdForCondition = request
					.getParameter(AnnotationConstants.REQST_PARAM_CONDITION_ENTITY_RECORD_ID);
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID, parentEntityId);
			session.setAttribute(AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID,
					parentEntityRecordId);
			session.setAttribute(AnnotationConstants.ENTITY_ID_IN_CONDITION, entityIdForCondition);
			session.setAttribute(AnnotationConstants.ENTITY_RECORDID_IN_CONDITION,
					entityRecordIdForCondition);
		}
	}

	// /**
	// * @param selected_static_entityid
	// * @return
	// * @throws CacheException
	// */
	// private Object getObjectFromCache(String key) throws CacheException
	// {
	// if (key != null)
	// {
	// CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
	// .getInstance();
	// if (cacheManager != null)
	// {
	// return cacheManager.getObjectFromCache(key);
	// }
	// }
	// return null;
	// }

	/**
	 * processResponseFromDynamicExtensions.
	 * @param request : request
	 * @param dynEntContainerId : dynEntContainerId
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws ApplicationException : ApplicationException
	 * @throws DynamicExtensionsApplicationException
	 * @throws NumberFormatException
	 */
	private void processResponseFromDynamicExtensions(HttpServletRequest request,
			String dynEntContainerId) throws DynamicExtensionsSystemException,
			ApplicationException, NumberFormatException, DynamicExtensionsApplicationException
	{
		final String operationStatus = request.getParameter( WebUIManager
				.getOperationStatusParameterName() );
		if (( operationStatus != null )
				&& ( operationStatus.trim().equals( WebUIManagerConstants.SUCCESS ) ))
		{
			final String dynExtRecordId = request.getParameter( WebUIManager
					.getRecordIdentifierParameterName() );
			this.logger.info( "Dynamic Entity Record Id [" + dynExtRecordId + "]" );
			//this.insertEntityMapRecord( request, dynExtRecordId, dynEntContainerId );
			this.insertRecordEntry(request, dynExtRecordId, dynEntContainerId);
		}
	}


	/**
	 * @param request : request
	 * @param staticEntityId : staticEntityId
	 * @param staticEntityRecordId : staticEntityRecordId
	 * @param staticEntityName : staticEntityName
	 * @param annotationDataEntryForm : annotationDataEntryForm
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws BizLogicException : BizLogicException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws CacheException
	 * @throws NumberFormatException
	 */
	private void initializeDataEntryForm(HttpServletRequest request, String staticEntityId,
			String staticEntityRecordId, String staticEntityName,
			AnnotationDataEntryForm annotationDataEntryForm)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			BizLogicException, NumberFormatException, CacheException, DAOException, SQLException
	{
		// Set annotations list
		if (staticEntityId != null)
		{
			final List annotationList = this.getAnnotationList( staticEntityId, staticEntityName,
					staticEntityRecordId );
			annotationDataEntryForm.setAnnotationsList( annotationList );
			annotationDataEntryForm.setParentEntityId( staticEntityId );
			final String definedAnnotationXML = this.getDefinedAnnotationsXML( request,
					annotationList, AppUtility.toLong( staticEntityId ), AppUtility
							.toLong( staticEntityRecordId ) );
			annotationDataEntryForm.setDefinedAnnotationEntitiesXML( definedAnnotationXML );
		}

		// Set defined annotations information
		// String definedAnnotationsDataXML =
		// getDefinedAnnotationsDataXML(request, Utility
		// .toLong(staticEntityId), Utility.toLong(staticEntityRecordId));
		final String definedAnnotationsDataXML = this.getDefinedAnnotationsDataXML( request,
				AppUtility.toLong( staticEntityId ), AppUtility.toLong( staticEntityRecordId ) );
		annotationDataEntryForm.setDefinedAnnotationsDataXML( definedAnnotationsDataXML );
	}

	/**
	 * @param request : request
	 * @param staticEntityId
	 *            Identifier of static Entity
	 * @param staticEntityRecordId
	 *            Identifier of static Entity record
	 * @return annotations XML
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 *             fails to get EntityMapRecord
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	private String getDefinedAnnotationsDataXML(HttpServletRequest request, Long staticEntityId,
			Long staticEntityRecordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, BizLogicException
	{
		final StringBuffer definedAnnotationsXML = new StringBuffer();
		// "<?xml version='1.0' encoding='UTF-8'?><rows><row id='1'
		//class='formField'><cell>0</cell><cell>001</cell><cell>Preeti
		//</cell><cell>12-2-1990</cell><cell>Preeti</cell></row></rows>"
		// ;
		definedAnnotationsXML.append( "<?xml version='1.0' encoding='UTF-8'?>" );
		final List dataList = new ArrayList();
		if (staticEntityId != null)
		{
			/*final List < EntityMapRecord > entityMapRecords = this.getEntityMapRecords(
					staticEntityId, staticEntityRecordId );*/
			final List<AbstractRecordEntry> entityMapRecords = this.getRecordEntryList(request,
					staticEntityRecordId);
			if (entityMapRecords != null)
			{
				definedAnnotationsXML.append( "<rows>" );
				final Iterator < AbstractRecordEntry > iterator = entityMapRecords.iterator();
				AbstractRecordEntry entityMapRecord = null;
				while (iterator.hasNext())
				{
					final List innerList = new ArrayList();
					entityMapRecord = iterator.next();
					definedAnnotationsXML.append( this.getXMLForEntityMapRecord( request,
							entityMapRecord, innerList ) );
					dataList.add( innerList );
				}
				definedAnnotationsXML.append( "</rows>" );
			}
			request.setAttribute(
					edu.wustl.catissuecore.util.global.Constants.SPREADSHEET_DATA_RECORD, dataList );
		}
		return definedAnnotationsXML.toString();
	}

	/**
	 * @param request : request
	 * @param entityMapRecord
	 *            Object of entityMapRecord
	 * @param innerList
	 *            contains XML nodes
	 * @return XML
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 */
	private String getXMLForEntityMapRecord(HttpServletRequest request,
			AbstractRecordEntry entityMapRecord, List innerList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		final StringBuffer entityMapRecordXML = new StringBuffer();
		if (entityMapRecord != null)
		{
			final Long containerId = entityMapRecord.getFormContext().getContainerId();

			final NameValueBean dynamicEntity = this.getDynamicEntity(containerId);

			if (dynamicEntity != null)
			{
				final String datePattern = CommonServiceLocator.getInstance().getDatePattern();
				final String strURLForEditRecord = this.getURLForEditEntityMapRecord( request,
						dynamicEntity.getName(), entityMapRecord );
				entityMapRecordXML
						.append( "<row id='" + entityMapRecord.getId().toString() + "' >" );
				// entityMapRecordXML.append(CELL_START + "0" + AnnotationConstants.CELL_END);
				// innerList.add("0");
				// entityMapRecordXML.append(CELL_START + entityMapRecord.getId()
				// + AnnotationConstants.CELL_END);
				entityMapRecordXML.append( AnnotationConstants.CELL_START );
				entityMapRecordXML.append( dynamicEntity.getValue() );
				entityMapRecordXML.append( '^' );
				entityMapRecordXML.append( strURLForEditRecord );
				entityMapRecordXML.append( AnnotationConstants.CELL_END );
				entityMapRecordXML.append( innerList.add( this.makeURL( dynamicEntity.getValue(), strURLForEditRecord ) ));
				entityMapRecordXML.append( AnnotationConstants.CELL_START );
				entityMapRecordXML.append( Utility.parseDateToString( entityMapRecord.getModifiedDate(), datePattern ));
				entityMapRecordXML.append(  AnnotationConstants.CELL_END );
				innerList.add( Utility.parseDateToString( entityMapRecord.getModifiedDate(),
						datePattern ) );
				innerList.add("");
				String creator = entityMapRecord.getModifiedBy();
				if (creator == null || creator.equals( "null" ))
				{
					creator = "";
				}
				entityMapRecordXML.append( AnnotationConstants.CELL_START );
				entityMapRecordXML.append( creator );
				entityMapRecordXML.append( AnnotationConstants.CELL_END );
				innerList.add( creator );
				entityMapRecordXML.append( AnnotationConstants.CELL_START );
				entityMapRecordXML.append( AnnotationConstants.EDIT );
				entityMapRecordXML.append( '^' );
				entityMapRecordXML.append( strURLForEditRecord );
				entityMapRecordXML.append( AnnotationConstants.CELL_END );
				innerList.add( entityMapRecord.getId().toString() );
				entityMapRecordXML.append( "</row>" );
			}
		}
		return entityMapRecordXML.toString();
	}

	/**
	 * @param containercaption
	 *            name of container
	 * @param dynExtentionsEditEntityURL
	 *            URL for editing Dynamic Extension data Entry Forms
	 * @return String : String
	 */
	private String makeURL(String containercaption, String dynExtentionsEditEntityURL)
	{
		String url = "<a  href=" + "'" + dynExtentionsEditEntityURL + "'>" + containercaption
				+ "</a>";
		return url;
	}

	/**
	 *
	 * @param request : request
	 * @param containerId : containerId
	 * @param entityMapRecord : entityMapRecord
	 * @return String : String
	 */
	private String getURLForEditEntityMapRecord(HttpServletRequest request, String containerId,
			AbstractRecordEntry recordEntry)
	{
		String urlForEditRecord = "";
		try
		{
			final Long recEntryEntityId = AnnotationUtil.getHookEntityId(recordEntry);
			//Long containerId = recordEntry.getFormContext().getContainerId();

			final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			Collection deRecIdList = annotationBizLogic.getDynamicRecordIdFromStaticId(recordEntry
					.getId(), Long.valueOf(containerId), recEntryEntityId);
			final String deRecId = deRecIdList.iterator().next().toString();
			/*final EntityMap entityMap = (EntityMap) ( new AnnotationBizLogic().retrieve(
					EntityMap.class.getName(), entityMapRecord.getFormContext().getEntityMap()
							.getId() ) );*/

			urlForEditRecord = request.getContextPath()
					+ "/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation=" + containerId
					+ "&amp;recordId=" + deRecId
					+ "&amp;selectedStaticEntityId=" + recEntryEntityId
					+ "&amp;selectedStaticEntityRecordId="
					+ AnnotationUtil.getStaticEntityRecordId(recordEntry);// +"_self";//
		}
		catch (final Exception e)
		{
			this.logger.error( e.getMessage(), e );
		}
		return urlForEditRecord;

	}

	/**
	 * @param containerId : containerId
	 * @return NameValueBean :Name-Value bean containing entity container id and name
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 */
	private NameValueBean getDynamicEntity(Long containerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean dynamicEntity = null;
		if(containerId != null)
		{
			dynamicEntity = new NameValueBean(containerId, this.getDEContainerName(containerId));
		}
		return dynamicEntity;
	}


	/**
	 * @param entityId
	 *            identifier of entity
	 * @param staticEntityName
	 *            Name of static entity
	 * @param staticEntityRecordId
	 *            Identifier of static record
	 * @return list of Annotations
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws BizLogicException
	 *             BizLogic Exception
	 */
	private List getAnnotationList(String entityId, String staticEntityName,
			String staticEntityRecordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, BizLogicException
	{
		final List < NameValueBean > annotationsList = new ArrayList < NameValueBean >();
		final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		//List dynEntitiesList = new ArrayList();
		// List dynamicEntitiesList = new ArrayList();
		//List allEntitiesList = new ArrayList();
		List cpIdList = new ArrayList();
		//List allCategoryEntityList = new ArrayList();
		if (staticEntityName != null && staticEntityRecordId != null)
		{
			String associatedStaticEntity = AnnotationUtil
					.getAssociatedStaticEntityForRecordEntry(staticEntityName);
			final ICPCondition annoCondn = this.getConditionInvoker( associatedStaticEntity );
			if (annoCondn != null)
			{
				cpIdList = annoCondn
						.getCollectionProtocolList( Long.valueOf( staticEntityRecordId ) );
			}
		}
		List dynEntitiesList = annotationBizLogic.getListOfDynamicEntities( Utility.toLong( entityId ) );
		dynEntitiesList = annotationBizLogic.getAnnotationIdsBasedOnCondition( dynEntitiesList,
				cpIdList );
		List allEntitiesList = this.checkForAbstractEntity( dynEntitiesList );
		List allCategoryEntityList = this.checkForAbstractCategoryEntity( dynEntitiesList );

		if (allEntitiesList != null)
		{
			final Iterator < Long > dynEntitiesIterator = allEntitiesList.iterator();
			NameValueBean annotationBean = null;
			while (dynEntitiesIterator.hasNext())
			{
				annotationBean = this.getNameValueBeanForDE( dynEntitiesIterator.next() );
				if (annotationBean != null)
				{
					annotationsList.add( annotationBean );
				}
			}
		}
		if (allCategoryEntityList != null)
		{
			final Iterator < Long > dynEntitiesIterator = allCategoryEntityList.iterator();
			NameValueBean annotationBean = null;
			while (dynEntitiesIterator.hasNext())
			{
				annotationBean = this
						.getNameValueBeanForCategoryEntity( dynEntitiesIterator.next() );
				if (annotationBean != null)
				{
					annotationsList.add( annotationBean );
				}
			}
		}
		return annotationsList;
	}

	/**
	 * check for the abstract entity.
	 * @param dynEntitiesList
	 *            Collection of dynamic entities
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @return List : List
	 */
	private List checkForAbstractEntity(List dynEntitiesList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		final List entitesList = new ArrayList();
		if (dynEntitiesList != null)
		{
			final Iterator < Long > dynEntitiesIterator = dynEntitiesList.iterator();
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			while (dynEntitiesIterator.hasNext())
			{
				Long deContainerId = dynEntitiesIterator.next();
				deContainerId = entityManager
						.checkContainerForAbstractEntity( deContainerId, false );
				if (deContainerId != null)
				{
					entitesList.add( deContainerId );
				}
			}
		}
		return entitesList;
	}

	/**
	 * @param dynEntitiesList
	 *            Collection if dynamic entities
	 * @return collection of entities
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 */
	private List checkForAbstractCategoryEntity(List dynEntitiesList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		final List entitesList = new ArrayList();
		if (dynEntitiesList != null)
		{
			final Iterator < Long > dynEntitiesIterator = dynEntitiesList.iterator();
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			while (dynEntitiesIterator.hasNext())
			{
				Long deContainerId = dynEntitiesIterator.next();
				deContainerId = entityManager
						.checkContainerForAbstractCategoryEntity( deContainerId );
				if (deContainerId != null)
				{
					entitesList.add( deContainerId );
				}
			}
		}
		return entitesList;
	}

	/**
	 * @param deContainerId
	 *            Identifier of container
	 * @return bean for category Entity
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsApplicationException
	 */
	private NameValueBean getNameValueBeanForCategoryEntity(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean nameValueBean = null;
		String deEntityName = null;
		try
		{
			deEntityName = this.getDEContainerCategoryName( deContainerId );
		}
		catch (final DAOException e)
		{
			this.logger.error( e.getMessage(), e );
		}
		if (( deContainerId != null ) && ( deEntityName != null ))
		{
			deEntityName = "Form--" + deEntityName;
			nameValueBean = new NameValueBean( deEntityName , deContainerId );
		}
		return nameValueBean;
	}

	/**
	 * @param deContainerId
	 *            Identifier of container
	 * @return bean for dynamic entity
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 */
	private NameValueBean getNameValueBeanForDE(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		NameValueBean nameValueBean = null;
		final String deEntityName = this.getDEContainerName( deContainerId );
		if (( deContainerId != null ) && ( deEntityName != null ))
		{
			nameValueBean = new NameValueBean( deEntityName , deContainerId );
		}
		return nameValueBean;
	}

	/**
	 * @param deContainerId
	 *            Identifier of container
	 * @return container name for dynamic entity
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 */
	private String getDEContainerName(Long deContainerId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		String containerName = null;
		if (deContainerId != null)
		{
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			containerName = entityManager.getContainerCaption( deContainerId );
		}
		return containerName;
	}

	/**
	 * @param deContainerId
	 *            Identifier of container
	 * @return container name for category entity
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws DAOException : DAOException
	 */
	private String getDEContainerCategoryName(Long deContainerId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			DAOException
	{
		String containerName = null;
		if (deContainerId != null)
		{
			final EntityManagerInterface entityManager = EntityManager.getInstance();
			final Long categoryEntityId = entityManager.getEntityIdByContainerId( deContainerId );
			containerName = entityManager.getCategoryCaption( categoryEntityId );
		}
		return containerName;
	}

	/**
	 * @param staticEntityName
	 *            Name of static entity
	 * @return ICPCondition object
	 */
	private ICPCondition getConditionInvoker(String staticEntityName)
	{
		// read StaticInformation.xml and get ConditionInvoker
		ICPCondition annoCondn = null;
		try
		{
			final AnnotationUtil util = new AnnotationUtil();
			util.populateStaticEntityList("StaticEntityInformation.xml", staticEntityName);
			final String conditionInvoker = (String) util.map.get("conditionInvoker");

			annoCondn = (ICPCondition) Class.forName( conditionInvoker ).newInstance();
			/*
			 * Class[] parameterTypes = new Class[]{Long.class}; Object[]
			 * parameterValues = new Object[]{new Long(entityInstanceId)};
			 * Method getBizLogicMethod =
			 * annotaionConditionClass.getMethod("getCollectionProtocolList",
			 * parameterTypes); annoCondn =
			 * (ICPCondition)getBizLogicMethod.invoke
			 * (annotaionConditionClass,parameterValues);
			 */
			// return annoCondn;
		}
		catch (final DataTypeFactoryInitializationException e)
		{
			this.logger.error( e.getMessage(), e );
		}
		catch (final ClassNotFoundException e)
		{
			this.logger.error( e.getMessage(), e );
		}
		catch (final InstantiationException e)
		{
			this.logger.error( e.getMessage(), e );
		}

		catch (final IllegalAccessException e)
		{
			this.logger.error( e.getMessage(), e );
		}
		return annoCondn;

	}

	/**
	 * @param request
	 *            object
	 * @param annotationsList
	 *            list of Annotations
	 * @param staticEntityId
	 *            identifier of static entity
	 * @param staticEntityRecordId
	 *            recordId of static entity
	 * @return XML string
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws BizLogicException
	 *             BizLogic Exception
	 * @throws SQLException
	 * @throws DAOException
	 * @throws CacheException
	 */
	private String getDefinedAnnotationsXML(HttpServletRequest request, List annotationsList,
			Long staticEntityId, Long staticEntityRecordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			BizLogicException, CacheException, DAOException, SQLException
	{
		final EntityManagerInterface entityManager = EntityManager.getInstance();
		final List < Long > annotationIds = new ArrayList < Long >();
		final StringBuffer definedAnnotationsXML = new StringBuffer();
		definedAnnotationsXML.append( "<?xml version='1.0' encoding='UTF-8'?><rows>" );
		final Iterator < NameValueBean > iterator = annotationsList.iterator();
		final List dataList = new ArrayList();
		NameValueBean entityBean = null;
		//List < EntityMapRecord > entityMapRecords = null;
		List < AbstractRecordEntry > entityMapRecords = null;

		if (staticEntityId != null)
		{
			//entityMapRecords = this.getEntityMapRecords( staticEntityId, staticEntityRecordId );
			entityMapRecords = this.getRecordEntryList(request, staticEntityRecordId );
		}
		while (iterator.hasNext())
		{
			final List innerList = new ArrayList();
			entityBean = iterator.next();
			if (entityBean != null)
			{
				final String entityName = entityBean.getName();
				final Long entityId = Long.parseLong( entityBean.getValue() );

				// entityGroupName =
				// entityManager.getEntityGroupNameByEntityName(entityName);
				String entityGroupName = entityManager.getEntityGroupNameByEntityName( entityName, Long
						.valueOf( entityId ) );
				String addURL = "<a href='#' name='"
						+ entityId
						+ "' onClick='loadDynExtDataEntryPage(event);' style='cursor:pointer;' id='selectedAnnotation'>Add</a>";

				if (entityGroupName == null)
				{
					entityGroupName = edu.wustl.catissuecore.util.global.Constants.DOUBLE_QUOTES;
				}
				final String entName = getFormattedStringForCapitalization(entityName);
				List<Long> recordIds = this.getRecordCountForEntity(entityId.toString(),
						entityMapRecords);
				int numRecord = recordIds.size();
				definedAnnotationsXML.append( "<row id='" + entityId.toString() + "' >" );
				definedAnnotationsXML.append( AnnotationConstants.CELL_START );
				definedAnnotationsXML.append( entityGroupName );
				definedAnnotationsXML.append( AnnotationConstants.CELL_END );
				innerList.add( entityGroupName );
				definedAnnotationsXML.append( AnnotationConstants.CELL_START );
				definedAnnotationsXML.append( entityName );
				definedAnnotationsXML.append( AnnotationConstants.CELL_END );
				innerList.add( entName );
				definedAnnotationsXML.append( AnnotationConstants.CELL_START );
				definedAnnotationsXML.append( numRecord );
				definedAnnotationsXML.append( AnnotationConstants.CELL_END );
				innerList.add( numRecord );
				definedAnnotationsXML.append( AnnotationConstants.CELL_START );
				definedAnnotationsXML.append( "Add" );
				definedAnnotationsXML.append( AnnotationConstants.HTML_SPACE );
				definedAnnotationsXML.append( "Edit" );
				definedAnnotationsXML.append( AnnotationConstants.CELL_END );
				if (numRecord <= 0)
				{
					innerList.add( addURL );
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
								+ "&recordId=" + recordIds.get( 0 ) + "'>Edit</a>";
					}
					else if (numRecord > 1)
					{
						final String onClick = "onClick=editSelectedAnnotation('formId=" + entityId
								+ ":staticEntityId=" + staticEntityId + ":staticEntityRecordId="
								+ staticEntityRecordId + "'); ";
						editURL = "<a href='#' name='" + entityId + "' " + onClick
								+ " style='cursor:pointer;' id='selectedAnnotation'>Edit</a>";

					}
					final String url = addURL + AnnotationConstants.HTML_SPACE + editURL;
					innerList.add( url );
				}
				definedAnnotationsXML.append( "</row>" );
				annotationIds.add( entityId );
			}
			dataList.add( innerList );
		}
		request.setAttribute( AnnotationConstants.ANNOTATION_LIST_FROM_XML, dataList );
		definedAnnotationsXML.append( "</rows>" );
		return definedAnnotationsXML.toString();
	}

	/**
	 * get Formatted String For Capitalization.
	 * @param entityName
	 *            name of entity to get formatted String
	 * @return String : String
	 */
	public static String getFormattedStringForCapitalization(String entityName)
	{
		return Utility.getDisplayLabel( entityName.trim() );
	}

	/**
	 * @param entityId
	 *            id of annotation
	 * @param entityMapRecords
	 *            to get EntityRecord information
	 * @return List of recordIds for entity
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : fails to get DynamicEntity based on entityMapRecords
	 * @throws SQLException
	 * @throws DAOException
	 * @throws CacheException
	 */
	public List < Long > getRecordCountForEntity(String entityId,
			List < AbstractRecordEntry > entityMapRecords) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, CacheException, DAOException, SQLException
	{
		final List < Long > recordIds = new ArrayList < Long >();
		if (entityMapRecords != null)
		{
			final Iterator < AbstractRecordEntry > iter = entityMapRecords.iterator();
			//AbstractRecordEntry recordEntry = null;
			AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();

			while (iter.hasNext())
			{
				AbstractRecordEntry recordEntry = iter.next();

				if (recordEntry != null)
				{
					Long containerId = recordEntry.getFormContext().getContainerId();
					final NameValueBean dynamicEntity = this.getDynamicEntity(containerId);
					Long recEntryEntityId = AnnotationUtil.getHookEntityId(recordEntry);
					Collection recIdList = annotationBizLogic.getDynamicRecordIdFromStaticId(
							recordEntry.getId(), containerId, recEntryEntityId);
					String recId = recIdList.iterator().next().toString();
					if (dynamicEntity != null
							&& dynamicEntity.getName().equalsIgnoreCase(entityId)
							&& !recordIds.contains(recId))
					{
						recordIds.add(Long.valueOf(recId));
					}
				}
			}
		}
		return recordIds;
	}

	/**
	 * processEditOperation.
	 * @param request : request
	 * @param staticEntityId : staticEntityId
	 * @param staticEntityRecordId : staticEntityRecordId
	 * @param selectedFormId : selectedFormId
	 * @return String : String
	 * @throws IOException : IOException
	 * @throws DynamicExtensionsSystemException : DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException : DynamicExtensionsApplicationException
	 * @throws BizLogicException : BizLogicException.
	 * @throws SQLException
	 * @throws DAOException
	 * @throws CacheException
	 * @throws NumberFormatException
	 */
	private String processEditOperation(HttpServletRequest request, Long staticEntityId,
			Long staticEntityRecordId, String selectedFormId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			BizLogicException, NumberFormatException, CacheException, DAOException, SQLException
	{
		final String datePattern = CommonServiceLocator.getInstance().getDatePattern();
		//List < EntityMapRecord > entityMapRecords = null;
		List < AbstractRecordEntry > entityMapRecords = null;
		if (staticEntityId != null)
		{
			//entityMapRecords = this.getEntityMapRecords( staticEntityId, staticEntityRecordId );
			entityMapRecords = this.getRecordEntryList(request, staticEntityRecordId);
		}
		final List < Long > recordIds = this.getRecordCountForEntity( selectedFormId,
				entityMapRecords );

		final StringBuffer definedAnnotationDataXML = new StringBuffer();
		definedAnnotationDataXML.append( "<?xml version='1.0' encoding='UTF-8'?><rows>" );
		final List dataList = new ArrayList();

		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
		for (final Long recordId : recordIds)
		{
			for (final AbstractRecordEntry entMapRecord : entityMapRecords)
			{
				Collection deEntityRecIdColl = annotationBizLogic.getDynamicRecordIdFromStaticId(
						entMapRecord.getId(), Long.valueOf(selectedFormId), staticEntityId);
				Long deEntityRecId = null;
				if (deEntityRecIdColl != null && !deEntityRecIdColl.isEmpty())
				{
					deEntityRecId = Long.valueOf(deEntityRecIdColl.iterator().next().toString());
				}
				//Long deEntityRecId = Long.valueOf(deEntityRecIdColl.iterator().next().toString());
				if (entMapRecord != null && recordId.equals(deEntityRecId))
				{
					final NameValueBean dynamicEntity = this.getDynamicEntity(Long.valueOf(selectedFormId));

					if (dynamicEntity != null)
					{
						final List innerList = new ArrayList();
						String creator = entMapRecord.getModifiedBy();
						if (creator == null
								|| creator
										.equals( edu.wustl.catissuecore.util.global.Constants.NULL ))
						{
							creator = edu.wustl.catissuecore.util.global.Constants.DOUBLE_QUOTES;
						}
						else if (creator != null && creator.contains( "," ))
						{
							creator = creator.replace( ",", "-" );
						}
						String editURL = "<a href='/catissuecore/LoadDynamicExtentionsDataEntryPage.do?selectedAnnotation="
								+ selectedFormId
								+ "&selectedStaticEntityId="
								+ staticEntityId
								+ "&selectedStaticEntityRecordId="
								+ staticEntityRecordId
								+ "&recordId="
								+ recordId
								+ "&operation=editSelectedAnnotation"
								+ "'>" + AnnotationConstants.EDIT + "</a>";
						definedAnnotationDataXML.append( "<row id='" + recordId.toString() + "' >" );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_START );
						definedAnnotationDataXML.append( recordId );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_END );
						innerList.add( Integer.parseInt( recordId.toString() ) );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_START );
						definedAnnotationDataXML.append( Utility.parseDateToString( entMapRecord.getModifiedDate(),
										datePattern ));
						definedAnnotationDataXML.append( AnnotationConstants.CELL_END );
						innerList.add( Utility.parseDateToString( entMapRecord.getModifiedDate(),
								datePattern ) );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_START );
						definedAnnotationDataXML.append( creator );
						definedAnnotationDataXML.append(  AnnotationConstants.CELL_END );
						innerList.add( creator );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_START );
						definedAnnotationDataXML.append( AnnotationConstants.EDIT );
						definedAnnotationDataXML.append( '^' );
						definedAnnotationDataXML.append( editURL );
						definedAnnotationDataXML.append( AnnotationConstants.CELL_END );
						innerList.add( editURL );
						dataList.add( innerList );
						request.setAttribute( AnnotationConstants.ENTITY_NAME, dynamicEntity
								.getValue() );
						request.setAttribute( AnnotationConstants.REQST_PARAM_ENTITY_ID,
								dynamicEntity.getName() );
					}
				}
			}
		}
		request.setAttribute( AnnotationConstants.STATIC_ENTITY_ID, staticEntityId );
		request.setAttribute( AnnotationConstants.STATIC_ENTITY_RECORD_ID, staticEntityRecordId );
		request.setAttribute( AnnotationConstants.RECORDS_IDs, recordIds );
		definedAnnotationDataXML.append( "</row>" );
		request.setAttribute( edu.wustl.catissuecore.util.global.Constants.SPREADSHEET_DATA_RECORD,
				dataList );
		return definedAnnotationDataXML.toString();
	}

	/**
	 *
	 * @param request
	 * @param dynExtRecordId
	 * @param dynEntContainerId
	 * @throws NumberFormatException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws ApplicationException
	 */
	private void insertRecordEntry(HttpServletRequest request, String dynExtRecordId,
			String dynEntContainerId) throws NumberFormatException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException, ApplicationException
	{
		AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();

		// TODO: Set request parameter for identifying Add/Edit case
		Long recEntryId = annotationBizLogic.getRecordEntryId(request,
				dynExtRecordId, dynEntContainerId);

		if(recEntryId == null)
		{
			String staticEntityName = (String) request.getSession().getAttribute("staticEntityName");
			String selectedStaticEntityRecordId = (String) request.getSession().getAttribute(
					AnnotationConstants.SELECTED_STATIC_ENTITY_RECORDID);
			AbstractRecordEntry abstractRecordEntry = createRecordEntry(staticEntityName,
					selectedStaticEntityRecordId);
			abstractRecordEntry.setActivityStatus("Active");
			abstractRecordEntry.setModifiedDate(new Date());

			final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			if (sessionDataBean != null)
			{
				abstractRecordEntry.setModifiedBy(sessionDataBean.getLastName() + ","
						+ sessionDataBean.getFirstName());
			}

			StudyFormContext studyFormContext = annotationBizLogic.getStudyFormContext(Long
					.valueOf(dynEntContainerId));
			if(studyFormContext == null)
			{
				studyFormContext = new StudyFormContext();
				studyFormContext.setHideForm(false);
				studyFormContext.setContainerId(Long
						.valueOf(dynEntContainerId));
				annotationBizLogic.insert(studyFormContext);
			}
			abstractRecordEntry.setFormContext(studyFormContext);

			annotationBizLogic.insert(abstractRecordEntry);
			AnnotationBizLogic annoBizLogic = new AnnotationBizLogic();
			Long recordEntryId = abstractRecordEntry.getId();

			String staticEntityId = (String) request.getSession().getAttribute(
					AnnotationConstants.SELECTED_STATIC_ENTITYID);

			annoBizLogic.associateRecords(Long.valueOf(dynEntContainerId), recordEntryId, Long
					.valueOf(dynExtRecordId), Long.valueOf(staticEntityId));
		}
	}

	private AbstractRecordEntry createRecordEntry(String staticEntityName,
			String selectedStaticEntityRecordId)
	{
		AbstractRecordEntry abstractRecordEntry = null;
		if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			abstractRecordEntry = new ParticipantRecordEntry();
			Participant participant = new Participant();
			participant.setId(Long.valueOf(selectedStaticEntityRecordId));
			((ParticipantRecordEntry)abstractRecordEntry).setParticipant(participant);

		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY))
		{
			abstractRecordEntry = new SpecimenRecordEntry();
			Specimen specimen = new Specimen();
			specimen.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SpecimenRecordEntry)abstractRecordEntry).setSpecimen(specimen);
		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			abstractRecordEntry = new SCGRecordEntry();
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			scg.setId(Long.valueOf(selectedStaticEntityRecordId));
			((SCGRecordEntry)abstractRecordEntry).setSpecimenCollectionGroup(scg);
		}

		return abstractRecordEntry;
	}

	/**
	 *
	 * @param request
	 * @param staticEntityRecordId
	 * @return
	 * @throws BizLogicException
	 */
	private List<AbstractRecordEntry> getRecordEntryList(HttpServletRequest request,
			Long staticEntityRecordId) throws BizLogicException
	{
		DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		List<AbstractRecordEntry> recordEntryList = null;

		String staticEntityName = (String) request.getSession().getAttribute("staticEntityName");

		if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			recordEntryList = defaultBizLogic.retrieve(ParticipantRecordEntry.class.getName(),
					"participant.id", staticEntityRecordId);
		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY))
		{
			recordEntryList = defaultBizLogic.retrieve(SpecimenRecordEntry.class.getName(),
					"specimen.id", staticEntityRecordId);
		}
		else if (staticEntityName.equals(AnnotationConstants.ENTITY_NAME_SCG_REC_ENTRY))
		{
			recordEntryList = defaultBizLogic.retrieve(SCGRecordEntry.class.getName(),
					"specimenCollectionGroup.id", staticEntityRecordId);
		}

		return recordEntryList;
	}


}
