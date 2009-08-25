/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.action.annotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.annotations.AnnotationUtil;
import edu.wustl.catissuecore.annotations.PathObject;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * @author preeti_munot
 *
 */
/**
 * This class is responsible for loading the annotation information
 */
public class LoadAnnotationDefinitionAction extends SecureAction
{

	private transient final Logger logger = Logger
			.getCommonLogger(LoadAnnotationDefinitionAction.class);

	/**
	 * @param mapping - mapping
	 * @param form - ActionForm
	 * @param request - HttpServletRequest object
	 * @param response - HttpServletResponse
	 * @return ActionForward
	 * @throws Exception - Exception
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		ActionForward actionfwd = null;
		// try
		// {
		final AnnotationForm annotationForm = (AnnotationForm) form;
		annotationForm.setSelectedStaticEntityId(null);

		// Added by Ravindra to disallow Non Super Admin users to add Local Extension
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		if (!sessionDataBean.isAdmin())
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("access.execute.action.denied");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);

			return mapping.findForward(Constants.ACCESS_DENIED);
		}

		//Ajax Code
		if (request.getParameter(AnnotationConstants.AJAX_OPERATION) != null
				&& !request.getParameter(AnnotationConstants.AJAX_OPERATION).equals("null"))
		{
			//If operation not null -> ajaxOperation
			this.processAjaxOperation(request, response);
			//loadAnnotations(annotationForm);
		}
		else if (request.getParameter(WebUIManager.getOperationStatusParameterName()) != null)
		{
			//Return from dynamic extensions
			this.processResponseFromDynamicExtensions(request);
			this.loadAnnotations(annotationForm, request);
			request.setAttribute(Constants.OPERATION, Constants.LOAD_INTEGRATION_PAGE);
			actionfwd = mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			this.loadAnnotations(annotationForm, request);
			actionfwd = mapping.findForward(Constants.SUCCESS);
		}

		//this is while edit operation for conditions
		if (request.getParameter(Constants.LINK) != null)
		{
			final String link = request.getParameter(Constants.LINK);
			request.setAttribute(Constants.LINK, link);

			final String containerId = request.getParameter(Constants.CONTAINERID);
			request
					.setAttribute(Constants.CONTAINERID, request
							.getParameter(Constants.CONTAINERID));
			if (containerId != null)
			{
				annotationForm.setSelectedStaticEntityId(request
						.getParameter(Constants.SELECTED_ENTITY_ID));

				final EntityManagerInterface entityManager = EntityManager.getInstance();
				final String containerCaption = entityManager.getContainerCaption(new Long(
						containerId));
				request.setAttribute(Constants.CONTAINER_NAME, containerCaption);

				this.getCPConditions(annotationForm, containerId, request);
				actionfwd = mapping.findForward(Constants.SUCCESS);
			}
		}
		/*   }
		 catch (Exception e)
		 {
		 e.printStackTrace();
		 Logger.out.error(e);
		 }*/

		return actionfwd;
	}

	/**
	 * @param annotationForm - AnnotationForm
	 * @param containerId - container Id
	 * @param request - HttpServletRequest object
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private void getCPConditions(AnnotationForm annotationForm, String containerId,
			HttpServletRequest request) throws DynamicExtensionsSystemException
	{
		if (containerId != null)
		{
			final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
					.getInstance();
			final List entitymapList = this.getEntityMapsForDE(new Long(containerId));
			if (entitymapList != null && !entitymapList.isEmpty())
			{
				String[] whereColumnValue = null;
				int i = 0;
				EntityMap entityMap = new EntityMap();
				entityMap = (EntityMap) entitymapList.get(0);
				final Collection<FormContext> formContexts = AppUtility.getFormContexts(entityMap
						.getId());
				final Iterator<FormContext> formContextIterator = formContexts.iterator();
				while (formContextIterator.hasNext())
				{
					final FormContext formContext = formContextIterator.next();
					final Collection<EntityMapCondition> entityMapConditions = AppUtility
							.getEntityMapConditions(formContext.getId());
					if ((formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
							.equals(""))
							&& (formContext.getStudyFormLabel() == null || formContext
									.getStudyFormLabel().equals("")))
					{
						if (entityMapConditions != null && !entityMapConditions.isEmpty())
						{
							final Collection entityMapConditionColl = entityMapConditions;
							whereColumnValue = new String[entityMapConditionColl.size()];
							final Iterator entityMapCondIterator = entityMapConditionColl
									.iterator();
							while (entityMapCondIterator.hasNext())
							{
								final EntityMapCondition entityMapCondition = (EntityMapCondition) entityMapCondIterator
										.next();
								if (entityMapCondition.getTypeId().toString().equals(
										catissueCoreCacheManager.getObjectFromCache(
												AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID)
												.toString()))
								{
									whereColumnValue[i++] = entityMapCondition.getStaticRecordId()
											.toString();
								}
							}
						}
					}
				}
				if (whereColumnValue == null || whereColumnValue.length == 0)
				{
					whereColumnValue = new String[]{Constants.ALL};
				}
				annotationForm.setConditionVal(whereColumnValue);
			}
		}
	}

	/**
	 * @param request - HttpServletRequest
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws ApplicationException  - ApplicationException
	 */
	private void processResponseFromDynamicExtensions(HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			ApplicationException
	{
		final String operationStatus = request.getParameter(WebUIManager
				.getOperationStatusParameterName());
		if ((operationStatus != null)
				&& (operationStatus.trim().equals(WebUIManagerConstants.SUCCESS)))
		{
			final String dynExtContainerId = request.getParameter(WebUIManager
					.getContainerIdentifierParameterName());
			final String staticEntityId = this.getStaticEntityIdForLinking(request);

			final String[] staticRecordId = this.getStaticRecordIdForLinking(request);

			this.logger.info("Need to link static entity [" + staticEntityId + "] to dyn ent ["
					+ dynExtContainerId + "]");
			this.linkEntities(request, staticEntityId, dynExtContainerId, staticRecordId);
		}
	}

	/**
	 * @param request - HttpServletRequest
	 * @param staticEntityId - staticEntityId
	 * @param staticRecordIds - staticRecordIds
	 * @param dynExtContainerId - dynExtContainerId
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws ApplicationException - ApplicationException
	 */
	private void linkEntities(HttpServletRequest request, String staticEntityId,
			String dynExtContainerId, String[] staticRecordIds)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			ApplicationException
	{
		if (dynExtContainerId != null)
		{
			DAO dao = null;
			final String deletedAssociationIds = request
					.getParameter(WebUIManagerConstants.DELETED_ASSOCIATION_IDS);
			String[] deletedAssociationIdArray = null;
			if (deletedAssociationIds != null && deletedAssociationIds.length() > 0)
			{
				deletedAssociationIdArray = deletedAssociationIds.split("_");
			}
			final DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List<EntityMap> entityMapList = null;
			try
			{
				entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(), "containerId",
						Long.parseLong(dynExtContainerId));
				if (entityMapList == null || entityMapList.size() == 0)
				{//If entity map is not present then Add case
					final EntityMap entityMap = this.getEntityMap(request, staticEntityId,
							dynExtContainerId, staticRecordIds);
					final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
					annotationBizLogic.insertEntityMap(entityMap);
				}
				else
				{//if entity map is present then Edit case
					//Getting the static entity id

					final EntityMap baseLevelEntityMap = entityMapList.get(0);
					staticEntityId = baseLevelEntityMap.getStaticEntityId().toString();

					//Retrieving the container
					try
					{
						final String appName = DynamicExtensionDAO.getInstance().getAppName();
						dao = DAOConfigFactory.getInstance().getDAOFactory(appName).getDAO();
						dao.openSession(null);

					}
					catch (final HibernateException excep)
					{
						this.logger.debug(excep.getMessage(), excep);
						excep.printStackTrace();
						throw new ApplicationException(ErrorKey.getErrorKey("hibernate.exception"),
								excep, "LoadAnnotationDefinitionAction.java");
					}

					ContainerInterface dynamicContainer = null;
					EntityInterface staticEntity = null;
					try
					{
						staticEntity = (EntityInterface) dao.retrieveById(Entity.class.getName(),
								new Long(staticEntityId));
						dynamicContainer = (Container) dao.retrieveById(Container.class.getName(),
								new Long(dynExtContainerId));

						final AssociationInterface association = edu.wustl.catissuecore.bizlogic.AnnotationUtil
								.getAssociationForEntity(staticEntity, dynamicContainer
										.getAbstractEntity());
						//	Get entitygroup that is used by caB2B for path finder purpose.
						//				Commented this line since performance issue for Bug 6433
						//EntityGroupInterface entityGroupInterface = edu.wustl.cab2b.common.util.Utility.getEntityGroup(staticEntity);
						//List<EntityInterface> processedEntityList = new ArrayList<EntityInterface>();

						//edu.wustl.catissuecore.bizlogic.AnnotationUtil.addCatissueGroup(dynamicContainer.getEntity(), entityGroupInterface, processedEntityList);
						//				staticEntity = EntityManager.getInstance().persistEntityMetadataForAnnotation(
						//						staticEntity, true, false, association);

						final Set<PathObject> processedPathList = new HashSet<PathObject>();
						//Adding paths from second level as first level paths between static entity and top level dynamic entity have already been added
						this.addQueryPathsForEntityHierarchy((EntityInterface) dynamicContainer
								.getAbstractEntity(), staticEntity, association.getId(),
								staticEntity.getId(), processedPathList);

						edu.wustl.catissuecore.bizlogic.AnnotationUtil.addEntitiesToCache(false,
								(EntityInterface) dynamicContainer.getAbstractEntity(),
								staticEntity);
					}
					catch (final BizLogicException excep)
					{
						this.logger.debug(excep.getMessage(), excep);
						excep.printStackTrace();
						throw AppUtility.getApplicationException(excep, excep.getErrorKeyName(),
								excep.getMsgValues());
					}
					finally
					{
						dao.closeSession();
					}
				}
			}
			catch (final NumberFormatException excep)
			{
				this.logger.debug(excep.getMessage(), excep);
				excep.printStackTrace();
				throw AppUtility.getApplicationException(excep, "number.format.exp",
						"LoadAnnotationDefinitionAction.java");
			}
			catch (final DAOException excep)
			{
				this.logger.debug(excep.getMessage(), excep);
				excep.printStackTrace();
				throw AppUtility.getApplicationException(excep, excep.getErrorKeyName(), excep
						.getMsgValues());
			}
			finally
			{
				//Added by Rajesh to remove deleted associations from query tables.
				this.removeQueryPathsForEntityHierarchy(deletedAssociationIdArray);
			}
		}
	}

	/**
	 * @param deletedAssociationIdArray - deletedAssociationIdArray
	 * @throws ApplicationException - ApplicationException
	 */
	private void removeQueryPathsForEntityHierarchy(String[] deletedAssociationIdArray)
			throws ApplicationException
	{
		if (deletedAssociationIdArray != null && deletedAssociationIdArray.length > 0)
		{
			JDBCDAO jdbcDAO = null;
			jdbcDAO = AppUtility.openJDBCSession();

			PreparedStatement statement = null;
			try
			{
				final String deleteQuery = "delete from intra_model_association where de_association_id = ?";
				statement = jdbcDAO.getPreparedStatement(deleteQuery);
				for (final String id : deletedAssociationIdArray)
				{
					statement.setLong(1, new Long(id));
					statement.executeUpdate();
					statement.clearParameters();
				}
				jdbcDAO.commit();
			}
			catch (final SQLException excep)
			{
				this.logger.debug(excep.getMessage(), excep);
				throw new ApplicationException(ErrorKey.getErrorKey("SQL.exp"), excep,
						"LoadAnnotationDefinitionAction.java");
			}
			finally
			{
				AppUtility.closeJDBCSession(jdbcDAO);
				try
				{
					statement.close();
				}
				catch (final SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param dynamicEntity - dynamicEntity
	 * @param staticEntity - staticEntity
	 * @param associationId - associationId
	 * @param staticEntityId - staticEntityId
	 * @param processedPathList - processedPathList
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws BizLogicException - BizLogicException
	 */
	private void addQueryPathsForEntityHierarchy(EntityInterface dynamicEntity,
			EntityInterface staticEntity, Long associationId, Long staticEntityId,
			Set<PathObject> processedPathList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, BizLogicException
	{
		final PathObject pathObject = new PathObject();
		pathObject.setSourceEntity(staticEntity);
		pathObject.setTargetEntity(dynamicEntity);

		if (processedPathList.contains(pathObject))
		{
			return;
		}
		else
		{
			processedPathList.add(pathObject);
		}

		final Long start = new Long(System.currentTimeMillis());
		final boolean ispathAdded = this.isPathAdded(staticEntity.getId(), dynamicEntity.getId());
		if (!ispathAdded)
		{
			if (dynamicEntity.getId() != null)
			{
				edu.wustl.catissuecore.bizlogic.AnnotationUtil.addPathsForQuery(staticEntity
						.getId(), dynamicEntity, staticEntityId, associationId);
			}
		}
		final Collection<AssociationInterface> associationCollection = dynamicEntity
				.getAllAssociations();
		for (final AssociationInterface association : associationCollection)
		{
			System.out.println("PERSISTING PATH");
			this.addQueryPathsForEntityHierarchy(association.getTargetEntity(), dynamicEntity,
					association.getId(), staticEntityId, processedPathList);
		}
		final Long end = new Long(System.currentTimeMillis());
		System.out.println("Time required to add complete paths is" + (end - start) / 1000
				+ "seconds");
	}

	/**
	 * @param staticEntityId - staticEntityId
	 * @param dynamicEntityId - dynamicEntityId
	 * @return boolean
	 */
	private boolean isPathAdded(Long staticEntityId, Long dynamicEntityId/*, Long deAssociationId*/)
	{
		boolean ispathAdded = false;
		JDBCDAO jdbcDAO = null;

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			jdbcDAO = AppUtility.openJDBCSession();
			final String checkForPathQuery = "select path_id from path where FIRST_ENTITY_ID = ? and LAST_ENTITY_ID = ?";
			preparedStatement = jdbcDAO.getPreparedStatement(checkForPathQuery);
			preparedStatement.setLong(1, staticEntityId);

			preparedStatement.setLong(2, dynamicEntityId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null)
			{
				while (resultSet.next())
				{
					ispathAdded = true;
					break;
				}
			}

			System.out.println("ispathAdded  ----- " + ispathAdded);

		}
		catch (final SQLException e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		catch (final ApplicationException e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				jdbcDAO.closeStatement(resultSet);
				AppUtility.closeJDBCSession(jdbcDAO);
			}
			catch (final ApplicationException e)
			{
				this.logger.error(e.getLogMessage(), e);
				e.printStackTrace();
			}
		}
		return ispathAdded;
	}

	/**
	 * @param request - HttpServletRequest
	 * @param staticEntityId - staticEntityId
	 * @param dynExtContainerId - dynExtContainerId
	 * @param conditions - conditions
	 * @return EntityMap
	 */
	private EntityMap getEntityMap(HttpServletRequest request, String staticEntityId,
			String dynExtContainerId, String[] conditions)
	{
		final AnnotationUtil util = new AnnotationUtil();
		Collection formContextCollection = new HashSet();

		final EntityMap entityMapObj = new EntityMap();
		entityMapObj.setContainerId(CommonUtilities.toLong(dynExtContainerId));
		entityMapObj.setStaticEntityId(CommonUtilities.toLong(staticEntityId));
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		if (sessionDataBean != null)
		{
			entityMapObj.setCreatedBy(sessionDataBean.getLastName() + ", "
					+ sessionDataBean.getFirstName());
			entityMapObj.setCreatedDate(new Date());
		}
		entityMapObj.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);

		if (conditions != null)
		{
			formContextCollection = util.getFormContextCollection(conditions, entityMapObj);
		}

		entityMapObj.setFormContextCollection(formContextCollection);
		//    entityMapObj.setEntityMapConditionCollection(entityMapConditionCollection);

		return entityMapObj;
	}

	/**
	 * @param request - HttpServletRequest
	 * @return String
	 */
	private String getStaticEntityIdForLinking(HttpServletRequest request)
	{
		final String staticEntityId = (String) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_ENTITYID);
		request.getSession().removeAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID);

		return staticEntityId;
	}

	/**
	 * @param request - HttpServletRequest
	 * @return String[]
	 */
	private String[] getStaticRecordIdForLinking(HttpServletRequest request)
	{
		final String[] staticRecordId = (String[]) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_RECORDID);
		request.getSession().removeAttribute(AnnotationConstants.SELECTED_STATIC_RECORDID);
		return staticRecordId;
	}

	/**
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @throws IOException - IOException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DAOException - DAOException
	 */
	private void processAjaxOperation(HttpServletRequest request, HttpServletResponse response)
			throws IOException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, DAOException
	{
		final String operation = request.getParameter(AnnotationConstants.AJAX_OPERATION);
		if ((operation != null)
				&& (operation.equalsIgnoreCase(AnnotationConstants.AJAX_OPERATION_SELECT_GROUP)))
		{
			final String groupId = request
					.getParameter(AnnotationConstants.AJAX_OPERATION_SELECTED_GROUPID);
			this.getEntitiesForGroupAsXML(groupId, request);
			this.getResponseString(request, response);

			// sendResponse(entitiesXML, response);
		}
	}

	/**
	 * Response string modidied as LoadXML creates problem in MAC safari
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @throws IOException - IOException
	 */
	private void getResponseString(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		final List entityList = (List) request.getSession().getAttribute(
				Constants.SPREADSHEET_DATA_ENTITY);
		final Iterator entityIt = entityList.iterator();
		//String responseString="";
		final StringBuilder responseString = new StringBuilder();
		while (entityIt.hasNext())
		{
			final List innerList = (List) entityIt.next();
			if (innerList != null && !innerList.isEmpty())
			{
				for (int i = 0; i < innerList.size(); i++)
				{
					responseString.append(innerList.get(i));
					if ((i + 1) < innerList.size())
					{
						responseString.append(",");
					}
				}
			}
			responseString.append("@");
		}
		this.sendResponse(responseString.toString(), response);
	}

	/**
	 * @param containerId - containerId
	 * @return String
	 */
	private String getDynamicExtentionsEditURL(Long containerId)
	{
		//TODO change ths with new api
		final String dynamicExtensionsEditEntityURL = "BuildDynamicEntity.do?containerId="
				+ containerId;// + "^_self";
		return dynamicExtensionsEditEntityURL;
	}

	/**
	 * @param containerId - containerId
	 * @param staticEntityId - staticEntityId
	 * @return String
	 */
	private String getDynamicExtentionsEditCondnURL(Long containerId, Long staticEntityId)
	{
		//TODO change ths with new api
		final String dynamicExtensionsEditEntityURL = "DefineAnnotations.do?link=editCondn&amp;containerId="
				+ containerId + "&amp;selectedStaticEntityId=" + staticEntityId;
		// + "^_self";
		return dynamicExtensionsEditEntityURL;
	}

	/**
	 * @param groupId - groupId
	 * @param request - HttpServletRequest
	 * @return String
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DAOException - DAOException
	 */
	private String getEntitiesForGroupAsXML(String groupId, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			DAOException
	{
		final List dataList = new ArrayList();

		final StringBuffer entitiesXML = new StringBuffer();
		entitiesXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		if (groupId != null)
		{
			final Long lGroupId = new Long(groupId);
			final Collection<NameValueBean> entityContainerCollection = EntityManager.getInstance()
					.getMainContainer(lGroupId);

			if (entityContainerCollection != null)
			{
				entitiesXML.append("<rows>");
				final Iterator<NameValueBean> containerCollnIter = entityContainerCollection
						.iterator();
				int entityIndex = 1;
				while (containerCollnIter.hasNext())
				{
					final List innerList = new ArrayList();
					final NameValueBean container = containerCollnIter.next();
					entitiesXML.append(this.getEntityXMLString(container, entityIndex, innerList,
							request));
					entityIndex++;
					dataList.add(innerList);
				}
				entitiesXML.append("</rows>");
			}
			request.getSession().setAttribute(Constants.SPREADSHEET_DATA_ENTITY, dataList);

		}
		return entitiesXML.toString();
	}

	/**
	 * @param container - container
	 * @param entityIndex - entityIndex
	 * @param innerList - innerList
	 * @param request - HttpServletRequest
	 * @return String
	 * @throws DAOException - DAOException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 */
	private String getEntityXMLString(NameValueBean container, int entityIndex, List innerList,
			HttpServletRequest request) throws DAOException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		final StringBuffer entityXML = new StringBuffer();
		if (container != null)
		{
			int index = 1;
			String editDynExtEntityURL = this.getDynamicExtentionsEditURL(new Long(container
					.getValue()));

			final List<EntityMap> entityMapList = this.getEntityMapsForDE(new Long(container
					.getValue()));
			if (entityMapList != null)
			{
				final Iterator<EntityMap> entityMapIterator = entityMapList.iterator();
				while (entityMapIterator.hasNext())
				{
					final EntityMap entityMapObj = entityMapIterator.next();
					final String editDynExtCondnURL = this.getDynamicExtentionsEditCondnURL(
							new Long(container.getValue()), entityMapObj.getStaticEntityId());
					editDynExtEntityURL = editDynExtEntityURL + "&staticEntityId="
							+ entityMapObj.getStaticEntityId();
					entityXML.append(this.getXMLForEntityMap(container.getName(), entityMapObj,
							entityIndex + index, editDynExtEntityURL, editDynExtCondnURL,
							innerList, request));
					index++;
				}
			}
		}
		return entityXML.toString();
	}

	/**
	 * @param containercaption - containercaption
	 * @param entityMapObj - entityMapObj
	 * @param rowId - rowId
	 * @param dynExtentionsEditEntityURL - dynExtentionsEditEntityURL
	 * @param editDynExtCondnURL - editDynExtCondnURL
	 * @param innerList - innerList
	 * @param request - HttpServletRequest
	 * @return StringBuffer
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 */
	private StringBuffer getXMLForEntityMap(String containercaption, EntityMap entityMapObj,
			int rowId, String dynExtentionsEditEntityURL, String editDynExtCondnURL,
			List innerList, HttpServletRequest request) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		final StringBuffer entityMapXML = new StringBuffer();
		entityMapXML.append("<row id='" + rowId + "'>");
		//entityMapXML.append("<cell>0</cell>");
		// innerList.add(rowId);
		entityMapXML.append("<cell>" + containercaption + "^" + dynExtentionsEditEntityURL
				+ "</cell>");
		String url = this.makeURL(containercaption, dynExtentionsEditEntityURL);
		innerList.add(url);
		if (entityMapObj != null)
		{
			final String staticEntityName = this.getEntityName(entityMapObj.getStaticEntityId(),
					request);
			entityMapXML.append("<cell>" + staticEntityName + "</cell>");
			innerList.add(staticEntityName);
			entityMapXML.append("<cell>"
					+ CommonUtilities.parseDateToString(entityMapObj.getCreatedDate(),
							CommonServiceLocator.getInstance().getDatePattern()) + "</cell>");
			innerList.add(CommonUtilities.parseDateToString(entityMapObj.getCreatedDate(),
					CommonServiceLocator.getInstance().getDatePattern()));
			entityMapXML.append("<cell>" + entityMapObj.getCreatedBy() + "</cell>");
			String name = entityMapObj.getCreatedBy();
			if (name == null)
			{
				name = ",";
			}
			if (name != null && name.contains(","))
			{
				name = name.replace(',', ' ');
				innerList.add(name);
			}
			else
			{
				innerList.add(entityMapObj.getCreatedBy());
				/* entityMapXML.append("<cell>" + entityMapObj.getLinkStatus()
				 + "</cell>");*/
			}
		}
		entityMapXML.append("<cell>" + edu.wustl.catissuecore.util.global.Constants.EDIT_CONDN
				+ "^" + editDynExtCondnURL + "</cell>");
		url = this.makeURL(edu.wustl.catissuecore.util.global.Constants.EDIT_CONDN,
				editDynExtCondnURL);
		innerList.add(url);
		entityMapXML.append("</row>");
		return entityMapXML;
	}

	/**
	 * @param containercaption - containercaption
	 * @param dynExtentionsEditEntityURL - dynExtentionsEditEntityURL
	 * @return String
	 */
	private String makeURL(String containercaption, String dynExtentionsEditEntityURL)
	{
		String url = "";
		url = "<a href=" + "'" + dynExtentionsEditEntityURL + "'>" + containercaption + "</a>";
		return url;
	}

	/**
	 * @param dynEntityContainerId - dynEntityContainerId
	 * @return List of EntityMap
	 */
	private List<EntityMap> getEntityMapsForDE(Long dynEntityContainerId)
	{
		List<EntityMap> entityMapList = null;
		if (dynEntityContainerId != null)
		{
			final AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
			entityMapList = annotationBizLogic.getListOfStaticEntities(dynEntityContainerId);
			/*List staticEntityIdsList = annotationBizLogic.getListOfStaticEntities(dynEntityContainerId);
			 entityMapList = getEntityMapList(staticEntityIdsList);*/
		}
		return entityMapList;
	}

	//	/**
	//	 * @param staticEntityIdsList
	//	 * @return
	//	 */
	//	private List<EntityMap> getEntityMapList(List<Long> staticEntityIdsList)
	//	{
	//		List<EntityMap> entityMapList = new ArrayList<EntityMap>();
	//		EntityMap entityMapObj = null;
	//		if (staticEntityIdsList != null)
	//		{
	//			Iterator<Long> staticEntityIdIterator = staticEntityIdsList.iterator();
	//			while (staticEntityIdIterator.hasNext())
	//			{
	//				entityMapObj = new EntityMap();
	//				entityMapObj.setStaticEntityId(staticEntityIdIterator.next());
	//				entityMapList.add(entityMapObj);
	//			}
	//		}
	//		return entityMapList;
	//	}

	/**
	 * @param entityId - entityId
	 * @return String
	 * @param request - HttpServletRequest
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private String getEntityName(Long entityId, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String entityName = "";
		if (entityId != null)
		{
			final List staticEntityList = (List) request.getSession().getAttribute(
					AnnotationConstants.STATIC_ENTITY_LIST);
			if (staticEntityList != null && !staticEntityList.isEmpty())
			{
				final Iterator listIterator = staticEntityList.iterator();
				while (listIterator.hasNext())
				{
					final NameValueBean nameValueBean = (NameValueBean) listIterator.next();
					if (nameValueBean.getValue().equalsIgnoreCase(entityId.toString()))
					{
						entityName = nameValueBean.getName();
					}
				}
			}

		}
		return entityName;
	}

	/**
	 * @param responseXML - responseXML
	 * @param response - HttpServletResponse
	 * @throws IOException - IOException
	 */
	private void sendResponse(String responseXML, HttpServletResponse response) throws IOException
	{
		final PrintWriter out = response.getWriter();
		out.write(responseXML);

		// out.print(responseXML);
	}

	/**
	 * @param request - HttpServletRequest
	 * @param annotationForm - AnnotationForm
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private void loadAnnotations(AnnotationForm annotationForm, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException

	{
		List systemEntitiesList = new ArrayList();
		if (annotationForm != null)
		{
			//load list of system entities
			if (request.getSession().getAttribute(AnnotationConstants.STATIC_ENTITY_LIST) == null)
			{
				systemEntitiesList = AnnotationUtil.getSystemEntityList();
				request.getSession().setAttribute(AnnotationConstants.STATIC_ENTITY_LIST,
						systemEntitiesList);
			}
			else
			{
				systemEntitiesList = (List) request.getSession().getAttribute(
						AnnotationConstants.STATIC_ENTITY_LIST);
			}

			annotationForm.setSystemEntitiesList(systemEntitiesList);
			//Load list of groups
			this.loadGroupList(annotationForm, request);

			final List conditionalInstancesList = this.populateConditionalInstanceList();
			annotationForm.setConditionalInstancesList(conditionalInstancesList);
			annotationForm.setConditionVal(new String[]{Constants.ALL});

		}
	}

	/**
	 * @return List
	 */
	private List populateConditionalInstanceList()
	{
		List conditionalInstancesList = new ArrayList();

		try
		{
			final DefaultBizLogic bizLogic = new DefaultBizLogic();
			final String[] displayNames = {"shortTitle", "title"};
			conditionalInstancesList = bizLogic.getList(CollectionProtocol.class.getName(),
					displayNames, "id", true);
			conditionalInstancesList = this.modifyName(conditionalInstancesList);
			conditionalInstancesList.remove(0);
			conditionalInstancesList.add(0, new NameValueBean(
					edu.wustl.catissuecore.util.global.Constants.HOLDS_ANY, Constants.ALL));
		}
		catch (final BizLogicException e)
		{
			this.logger.debug(e.getMessage(), e);
		}
		return conditionalInstancesList;
	}

	/**
	 * @param conditionalInstancesList - conditionalInstancesList
	 * @return List of NameValueBean
	 */
	private List modifyName(List conditionalInstancesList)
	{
		final List list = new ArrayList();
		for (final Object x : conditionalInstancesList)
		{

			final NameValueBean bean = (NameValueBean) x;
			final String[] split = bean.getName().split(",");
			if (split != null && split.length > 1)
			{
				String name;
				name = split[0] + " (" + split[1].trim() + ")";
				bean.setName(name);

			}
			list.add(bean);

		}
		return list;
	}

	/**
	 * @param annotationForm - AnnotationForm
	 * @param request - HttpServletRequest object
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 */
	private void loadGroupList(AnnotationForm annotationForm, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//List of groups
		final Collection<NameValueBean> annotationGroupsList = this.getAnnotationGroups();
		final String groupsXML = this.getGroupsXML(annotationGroupsList, request);
		annotationForm.setAnnotationGroupsXML(groupsXML);
	}

	/**
	 * @param annotationGroupsList - collection of NameValueBean
	 * @param request - HttpServletRequest object
	 * @return string
	 */
	private String getGroupsXML(Collection<NameValueBean> annotationGroupsList,
			HttpServletRequest request)
	{
		final StringBuffer groupsXML = new StringBuffer();
		final List dataList = new ArrayList();
		if (annotationGroupsList != null)
		{
			groupsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
			groupsXML.append("<rows>");
			NameValueBean groupBean = null;
			final Iterator<NameValueBean> iterator = annotationGroupsList.iterator();
			while (iterator.hasNext())
			{
				groupBean = iterator.next();
				final List innerList = new ArrayList();
				if (groupBean != null)
				{
					groupsXML.append("<row id='" + groupBean.getValue() + "' >");
					//groupsXML.append("<cell>0</cell>");
					groupsXML.append("<cell>" + groupBean.getName() + "</cell>");
					innerList.add(groupBean.getValue());
					innerList.add(groupBean.getName());
					groupsXML.append("</row>");
					dataList.add(innerList);
				}
			}
			groupsXML.append("</rows>");
		}
		request.setAttribute(Constants.SPREADSHEET_DATA_GROUP, dataList);
		return groupsXML.toString();
	}

	/**
	 * @return collection of  NameValueBean
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 */
	private Collection<NameValueBean> getAnnotationGroups()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		final EntityManagerInterface entityManager = EntityManager.getInstance();
		final Collection<NameValueBean> entityGroups = entityManager.getAllEntityGroupBeans();
		return entityGroups;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax.servlet.http.HttpServletRequest)
	 */
	/*protected boolean isAuthorizedToExecute(HttpServletRequest request) throws Exception
	{

		return super.isAuthorizedToExecute(request);

	}*/

	/**
	 * @param request - HttpServletRequest
	 * @return SessionDataBean
	 */
	@Override
	protected SessionDataBean getSessionData(HttpServletRequest request)
	{

		return super.getSessionData(request);

	}

}
