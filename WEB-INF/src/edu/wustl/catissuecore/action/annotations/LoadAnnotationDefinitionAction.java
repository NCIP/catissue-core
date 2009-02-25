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
import java.sql.Connection;
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

import net.sf.ehcache.CacheException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
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
		// try
		// {
		AnnotationForm annotationForm = (AnnotationForm) form;
		annotationForm.setSelectedStaticEntityId(null);

		// Added by Ravindra to disallow Non Super Admin users to add Local Extension
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		if (!sessionDataBean.isAdmin())
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("access.execute.action.denied");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);

			return mapping.findForward(Constants.ACCESS_DENIED);
		}

		//Ajax Code
		if (request.getParameter(AnnotationConstants.AJAX_OPERATION) != null
				&& !request.getParameter(AnnotationConstants.AJAX_OPERATION).equals("null"))
		{
			//If operation not null -> ajaxOperation
			processAjaxOperation(request, response);
			//loadAnnotations(annotationForm);
		}
		else if (request.getParameter(WebUIManager.getOperationStatusParameterName()) != null)
		{
			//Return from dynamic extensions
			processResponseFromDynamicExtensions(request);
			loadAnnotations(annotationForm, request);
			request.setAttribute(Constants.OPERATION, Constants.LOAD_INTEGRATION_PAGE);
			actionfwd = mapping.findForward(Constants.SUCCESS);
		}
		else
		{
			loadAnnotations(annotationForm, request);
			actionfwd = mapping.findForward(Constants.SUCCESS);
		}

		//this is while edit operation for conditions
		if (request.getParameter(Constants.LINK) != null)
		{
			String link = request.getParameter(Constants.LINK);
			request.setAttribute(Constants.LINK, link);

			String containerId = request.getParameter(Constants.CONTAINERID);
			request
					.setAttribute(Constants.CONTAINERID, request
							.getParameter(Constants.CONTAINERID));
			if (containerId != null)
			{
				annotationForm.setSelectedStaticEntityId(request
						.getParameter(Constants.SELECTED_ENTITY_ID));

				EntityManagerInterface entityManager = EntityManager.getInstance();
				String containerCaption = entityManager.getContainerCaption(new Long(containerId));
				request.setAttribute(Constants.CONTAINER_NAME, containerCaption);

				getCPConditions(annotationForm, containerId, request);
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
	 *
	 * @param annotationForm
	 * @param containerId
	 * @throws CacheException
	 * @throws IllegalStateException
	 */
	private void getCPConditions(AnnotationForm annotationForm, String containerId,
			HttpServletRequest request) throws IllegalStateException, CacheException
	{
		if (containerId != null)
		{
			CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
					.getInstance();
			List entitymapList = getEntityMapsForDE(new Long(containerId));
			if (entitymapList != null && !entitymapList.isEmpty())
			{
				String[] whereColumnValue = null;
				int i = 0;
				EntityMap entityMap = new EntityMap();
				entityMap = (EntityMap) entitymapList.get(0);
				Collection formContextConditionColl = entityMap.getFormContextCollection();
				Iterator it = formContextConditionColl.iterator();

				while (it.hasNext())
				{
					FormContext formContext = (FormContext) it.next();
					if ((formContext.getNoOfEntries() == null || formContext.getNoOfEntries()
							.equals(""))
							&& (formContext.getStudyFormLabel() == null || formContext
									.getStudyFormLabel().equals("")))
						if (formContext.getEntityMapConditionCollection() != null
								&& !formContext.getEntityMapConditionCollection().isEmpty())
						{

							Collection entityMapConditionColl = formContext
									.getEntityMapConditionCollection();
							whereColumnValue = new String[entityMapConditionColl.size()];
							Iterator entityMapCondIterator = entityMapConditionColl.iterator();
							while (entityMapCondIterator.hasNext())
							{
								EntityMapCondition entityMapCondition = (EntityMapCondition) entityMapCondIterator
										.next();
								if (entityMapCondition.getTypeId().toString().equals(
										catissueCoreCacheManager.getObjectFromCache(
												AnnotationConstants.COLLECTION_PROTOCOL_ENTITY_ID)
												.toString()))
									whereColumnValue[i++] = entityMapCondition.getStaticRecordId()
											.toString();
							}
						}
				}
				annotationForm.setConditionVal(whereColumnValue);
			}
		}
	}

	/**
	 * @param request
	 * @throws UserNotAuthorizedException
	 * @throws BizLogicException
	 * @throws CacheException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private void processResponseFromDynamicExtensions(HttpServletRequest request)
			throws BizLogicException, UserNotAuthorizedException, CacheException, DAOException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String operationStatus = request.getParameter(WebUIManager
				.getOperationStatusParameterName());
		if ((operationStatus != null)
				&& (operationStatus.trim().equals(WebUIManagerConstants.SUCCESS)))
		{
			String dynExtContainerId = request.getParameter(WebUIManager
					.getContainerIdentifierParameterName());
			String staticEntityId = getStaticEntityIdForLinking(request);

			String[] staticRecordId = getStaticRecordIdForLinking(request);

			Logger.out.info("Need to link static entity [" + staticEntityId + "] to dyn ent ["
					+ dynExtContainerId + "]");
			linkEntities(request, staticEntityId, dynExtContainerId, staticRecordId);
		}
	}

	/**
	 * @param request
	 * @param staticEntityId
	 * @param dynExtContainerId
	 * @throws UserNotAuthorizedException
	 * @throws BizLogicException
	 * @throws CacheException
	 * @throws DAOException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private void linkEntities(HttpServletRequest request, String staticEntityId,
			String dynExtContainerId, String[] staticRecordIds) throws BizLogicException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (dynExtContainerId != null)
		{
			String deletedAssociationIds = request
					.getParameter(WebUIManagerConstants.DELETED_ASSOCIATION_IDS);
			String[] deletedAssociationIdArray = null;
			if (deletedAssociationIds != null && deletedAssociationIds.length() > 0)
			{
				deletedAssociationIdArray = deletedAssociationIds.split("_");
			}
			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			List<EntityMap> entityMapList = null;
			try
			{
				entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(), "containerId",
						Long.parseLong(dynExtContainerId));
				if (entityMapList == null || entityMapList.size() == 0)
				{//If entity map is not present then Add case
					EntityMap entityMap = getEntityMap(request, staticEntityId, dynExtContainerId,
							staticRecordIds);
					AnnotationBizLogic annotationBizLogic = new AnnotationBizLogic();
					annotationBizLogic.insertEntityMap(entityMap);
				}
				else
				{//if entity map is present then Edit case
					//Getting the static entity id

					EntityMap baseLevelEntityMap = entityMapList.get(0);
					staticEntityId = baseLevelEntityMap.getStaticEntityId().toString();

					//Retrieving the container
					Session session = null;
					try
					{
						session = DBUtil.currentSession();
					}
					catch (HibernateException e1)
					{
						// TODO Auto-generated catch block
						Logger.out.debug(e1.getMessage(), e1);
						throw new BizLogicException("", e1);
					}

					ContainerInterface dynamicContainer = null;
					EntityInterface staticEntity = null;
					try
					{
						staticEntity = (EntityInterface) session.load(Entity.class, new Long(
								staticEntityId));
						dynamicContainer = (Container) session.load(Container.class, new Long(
								dynExtContainerId));

						AssociationInterface association = edu.wustl.catissuecore.bizlogic.AnnotationUtil
								.getAssociationForEntity(staticEntity, dynamicContainer
										.getAbstractEntity());
						//	Get entitygroup that is used by caB2B for path finder purpose.
						//				Commented this line since performance issue for Bug 6433
						//EntityGroupInterface entityGroupInterface = edu.wustl.cab2b.common.util.Utility.getEntityGroup(staticEntity);
						//List<EntityInterface> processedEntityList = new ArrayList<EntityInterface>();

						//edu.wustl.catissuecore.bizlogic.AnnotationUtil.addCatissueGroup(dynamicContainer.getEntity(), entityGroupInterface, processedEntityList);
						//				staticEntity = EntityManager.getInstance().persistEntityMetadataForAnnotation(
						//						staticEntity, true, false, association);

						Set<PathObject> processedPathList = new HashSet<PathObject>();
						//Adding paths from second level as first level paths between static entity and top level dynamic entity have already been added
						addQueryPathsForEntityHierarchy((EntityInterface) dynamicContainer
								.getAbstractEntity(), staticEntity, association.getId(),
								staticEntity.getId(), processedPathList);

						edu.wustl.catissuecore.bizlogic.AnnotationUtil.addEntitiesToCache(false,
								(EntityInterface) dynamicContainer.getAbstractEntity(),
								staticEntity);
					}
					catch (HibernateException e1)
					{
						// TODO Auto-generated catch block
						Logger.out.debug(e1.getMessage(), e1);
						throw new BizLogicException("", e1);
					}
					finally
					{
						try
						{
							DBUtil.closeSession();
						}
						catch (HibernateException e)
						{
							// TODO Auto-generated catch block
							Logger.out.debug(e.getMessage(), e);
							throw new BizLogicException("", e);
						}
					}
				}
			}
			catch (NumberFormatException e2)
			{
				// TODO Auto-generated catch block
				Logger.out.debug(e2.getMessage(), e2);
				throw new BizLogicException("", e2);
			}
			catch (DAOException e2)
			{
				// TODO Auto-generated catch block
				Logger.out.debug(e2.getMessage(), e2);
				throw new BizLogicException("", e2);
			}
			finally
			{
				//Added by Rajesh to remove deleted associations from query tables.
				removeQueryPathsForEntityHierarchy(deletedAssociationIdArray);
			}
		}
	}

	/**
	 *
	 *
	 */
	private void removeQueryPathsForEntityHierarchy(String[] deletedAssociationIdArray)
			throws BizLogicException
	{
		if (deletedAssociationIdArray != null && deletedAssociationIdArray.length > 0)
		{
			Connection conn = null;
			try
			{
				conn = DBUtil.getConnection();
			}
			catch (HibernateException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new BizLogicException("", e1);
			}
			PreparedStatement statement = null;
			try
			{
				String deleteQuery = "delete from intra_model_association where de_association_id = ?";
				statement = conn.prepareStatement(deleteQuery);
				for (String id : deletedAssociationIdArray)
				{
					statement.setLong(1, new Long(id));
					statement.executeUpdate();
					statement.clearParameters();
				}
				conn.commit();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BizLogicException("", e);
			}
			finally
			{
				try
				{
					statement.close();
					DBUtil.closeConnection();
				}
				catch (HibernateException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new BizLogicException("", e);
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new BizLogicException("", e);
				}

			}
		}
	}

	/**
	 * @param dynamicEntity
	 * @param entityGroupInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws BizLogicException
	 */
	private void addQueryPathsForEntityHierarchy(EntityInterface dynamicEntity,
			EntityInterface staticEntity, Long associationId, Long staticEntityId,
			Set<PathObject> processedPathList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, BizLogicException
	{
		PathObject pathObject = new PathObject();
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

		Long start = new Long(System.currentTimeMillis());
		boolean ispathAdded = isPathAdded(staticEntity.getId(), dynamicEntity.getId());
		if (!ispathAdded)
		{
			if (dynamicEntity.getId() != null)
			{
				edu.wustl.catissuecore.bizlogic.AnnotationUtil.addPathsForQuery(staticEntity
						.getId(), dynamicEntity.getId(), staticEntityId, associationId);
			}
		}
		Collection<AssociationInterface> associationCollection = dynamicEntity
				.getAssociationCollection();
		for (AssociationInterface association : associationCollection)
		{
			System.out.println("PERSISTING PATH");
			addQueryPathsForEntityHierarchy(association.getTargetEntity(), dynamicEntity,
					association.getId(), staticEntityId, processedPathList);
		}
		Long end = new Long(System.currentTimeMillis());
		System.out.println("Time required to add complete paths is" + (end - start) / 1000
				+ "seconds");
	}

	/**
	 * @param staticEntityId
	 * @param dynamicEntityId
	 * @return
	 */
	private boolean isPathAdded(Long staticEntityId, Long dynamicEntityId/*, Long deAssociationId*/)
	{
		boolean ispathAdded = false;
		Connection conn = null;

		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			String checkForPathQuery = "select path_id from path where FIRST_ENTITY_ID = ? and LAST_ENTITY_ID = ?";
			preparedStatement = conn.prepareStatement(checkForPathQuery);
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
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{

			try
			{
				resultSet.close();
				preparedStatement.close();
				DBUtil.closeConnection();
			}
			catch (HibernateException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return ispathAdded;
	}

	/**
	 * @param request
	 * @param staticEntityId
	 * @param dynExtContainerId
	 * @return
	 * @throws CacheException
	 */
	private EntityMap getEntityMap(HttpServletRequest request, String staticEntityId,
			String dynExtContainerId, String[] conditions) throws CacheException
	{
		AnnotationUtil util = new AnnotationUtil();
		Collection formContextCollection = new HashSet();

		EntityMap entityMapObj = new EntityMap();
		entityMapObj.setContainerId(Utility.toLong(dynExtContainerId));
		entityMapObj.setStaticEntityId(Utility.toLong(staticEntityId));
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		if (sessionDataBean != null)
		{
			entityMapObj.setCreatedBy(sessionDataBean.getLastName() + ", "
					+ sessionDataBean.getFirstName());
			entityMapObj.setCreatedDate(new Date());
		}
		entityMapObj.setLinkStatus(AnnotationConstants.STATUS_ATTACHED);

		if (conditions != null)
			formContextCollection = util.getFormContextCollection(conditions, entityMapObj);

		entityMapObj.setFormContextCollection(formContextCollection);
		//    entityMapObj.setEntityMapConditionCollection(entityMapConditionCollection);

		return entityMapObj;
	}

	/**
	 * @return
	 * @throws CacheException
	 */
	private String getStaticEntityIdForLinking(HttpServletRequest request) throws CacheException
	{
		String staticEntityId = (String) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_ENTITYID);
		request.getSession().removeAttribute(AnnotationConstants.SELECTED_STATIC_ENTITYID);

		return staticEntityId;
	}

	/**
	 *
	 * @return
	 * @throws CacheException
	 */
	private String[] getStaticRecordIdForLinking(HttpServletRequest request) throws CacheException
	{
		String[] staticRecordId = (String[]) request.getSession().getAttribute(
				AnnotationConstants.SELECTED_STATIC_RECORDID);
		request.getSession().removeAttribute(AnnotationConstants.SELECTED_STATIC_RECORDID);
		return staticRecordId;
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws CacheException
	 */
	private void processAjaxOperation(HttpServletRequest request, HttpServletResponse response)
			throws IOException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, DAOException, CacheException
	{
		String operation = request.getParameter(AnnotationConstants.AJAX_OPERATION);
		if ((operation != null)
				&& (operation.equalsIgnoreCase(AnnotationConstants.AJAX_OPERATION_SELECT_GROUP)))
		{
			String groupId = request
					.getParameter(AnnotationConstants.AJAX_OPERATION_SELECTED_GROUPID);
			String entitiesXML = getEntitiesForGroupAsXML(groupId, request);
			getResponseString(request, response);

			// sendResponse(entitiesXML, response);
		}
	}

	/**
	 * Response string modidied as LoadXML creates problem in MAC safari
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getResponseString(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		List entityList = (List) request.getSession().getAttribute(
				Constants.SPREADSHEET_DATA_ENTITY);
		Iterator entityIt = entityList.iterator();
		//String responseString="";
		StringBuilder responseString = new StringBuilder();
		while (entityIt.hasNext())
		{
			List innerList = (List) entityIt.next();
			if (innerList != null && !innerList.isEmpty())
			{
				for (int i = 0; i < innerList.size(); i++)
				{
					responseString.append(innerList.get(i));
					if ((i + 1) < innerList.size())
						responseString.append(",");
				}
			}
			responseString.append("@");
		}
		sendResponse(responseString.toString(), response);
	}

	/**
	 * @param containerId
	 * @return
	 */
	private String getDynamicExtentionsEditURL(Long containerId)
	{
		//TODO change ths with new api
		String dynamicExtensionsEditEntityURL = "BuildDynamicEntity.do?containerId=" + containerId;// + "^_self";
		return dynamicExtensionsEditEntityURL;
	}

	private String getDynamicExtentionsEditCondnURL(Long containerId, Long staticEntityId)
	{
		//TODO change ths with new api
		String dynamicExtensionsEditEntityURL = "DefineAnnotations.do?link=editCondn&amp;containerId="
				+ containerId + "&amp;selectedStaticEntityId=" + staticEntityId;
		// + "^_self";
		return dynamicExtensionsEditEntityURL;
	}

	/**
	 * @param groupId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws CacheException
	 */
	private String getEntitiesForGroupAsXML(String groupId, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			DAOException, CacheException
	{
		List dataList = new ArrayList();

		StringBuffer entitiesXML = new StringBuffer();
		entitiesXML.append("<?xml version='1.0' encoding='UTF-8'?>");
		if (groupId != null)
		{
			Long lGroupId = new Long(groupId);
			Collection<NameValueBean> entityContainerCollection = EntityManager.getInstance()
					.getMainContainer(lGroupId);

			if (entityContainerCollection != null)
			{
				entitiesXML.append("<rows>");
				Iterator<NameValueBean> containerCollnIter = entityContainerCollection.iterator();
				int entityIndex = 1;
				while (containerCollnIter.hasNext())
				{
					List innerList = new ArrayList();
					NameValueBean container = containerCollnIter.next();
					entitiesXML.append(getEntityXMLString(container, entityIndex, innerList,
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
	 * @param container
	 * @param entityIndex
	 * @return
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws CacheException
	 */
	private String getEntityXMLString(NameValueBean container, int entityIndex, List innerList,
			HttpServletRequest request) throws DAOException, DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, CacheException
	{
		StringBuffer entityXML = new StringBuffer();
		if (container != null)
		{
			int index = 1;
			String editDynExtEntityURL = getDynamicExtentionsEditURL(new Long(container.getValue()));

			List<EntityMap> entityMapList = getEntityMapsForDE(new Long(container.getValue()));
			if (entityMapList != null)
			{
				Iterator<EntityMap> entityMapIterator = entityMapList.iterator();
				while (entityMapIterator.hasNext())
				{
					EntityMap entityMapObj = entityMapIterator.next();
					String editDynExtCondnURL = getDynamicExtentionsEditCondnURL(new Long(container
							.getValue()), entityMapObj.getStaticEntityId());
					editDynExtEntityURL = editDynExtEntityURL + "&staticEntityId="
							+ entityMapObj.getStaticEntityId();
					entityXML.append(getXMLForEntityMap(container.getName(), entityMapObj,
							entityIndex + index, editDynExtEntityURL, editDynExtCondnURL,
							innerList, request));
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
	 * @throws CacheException
	 */
	private StringBuffer getXMLForEntityMap(String containercaption, EntityMap entityMapObj,
			int rowId, String dynExtentionsEditEntityURL, String editDynExtCondnURL,
			List innerList, HttpServletRequest request) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, CacheException
	{
		StringBuffer entityMapXML = new StringBuffer();
		entityMapXML.append("<row id='" + rowId + "'>");
		//entityMapXML.append("<cell>0</cell>");
		// innerList.add(rowId);
		entityMapXML.append("<cell>" + containercaption + "^" + dynExtentionsEditEntityURL
				+ "</cell>");
		String url = makeURL(containercaption, dynExtentionsEditEntityURL);
		innerList.add(url);
		if (entityMapObj != null)
		{
			String staticEntityName = getEntityName(entityMapObj.getStaticEntityId(), request);
			entityMapXML.append("<cell>" + staticEntityName + "</cell>");
			innerList.add(staticEntityName);
			entityMapXML.append("<cell>"
					+ Utility
							.parseDateToString(entityMapObj.getCreatedDate(), Variables.dateFormat)
					+ "</cell>");
			innerList.add(Utility.parseDateToString(entityMapObj.getCreatedDate(),
					Variables.dateFormat));
			entityMapXML.append("<cell>" + entityMapObj.getCreatedBy() + "</cell>");
			String name = entityMapObj.getCreatedBy();
			if (name == null)
				name = ",";
			if (name != null && name.contains(","))
			{
				name = name.replace(',', ' ');
				innerList.add(name);
			}
			else
				innerList.add(entityMapObj.getCreatedBy());
			/* entityMapXML.append("<cell>" + entityMapObj.getLinkStatus()
			 + "</cell>");*/
		}
		entityMapXML.append("<cell>" + edu.wustl.catissuecore.util.global.Constants.EDIT_CONDN
				+ "^" + editDynExtCondnURL + "</cell>");
		url = makeURL(edu.wustl.catissuecore.util.global.Constants.EDIT_CONDN, editDynExtCondnURL);
		innerList.add(url);
		entityMapXML.append("</row>");
		return entityMapXML;
	}

	private String makeURL(String containercaption, String dynExtentionsEditEntityURL)
	{
		String url = "";
		url = "<a href=" + "'" + dynExtentionsEditEntityURL + "'>" + containercaption + "</a>";
		return url;
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
	 * @param entityId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws CacheException
	 */
	private String getEntityName(Long entityId, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			CacheException
	{
		String entityName = "";
		if (entityId != null)
		{
			List staticEntityList = (List) request.getSession().getAttribute(
					AnnotationConstants.STATIC_ENTITY_LIST);
			if (staticEntityList != null && !staticEntityList.isEmpty())
			{
				Iterator listIterator = staticEntityList.iterator();
				while (listIterator.hasNext())
				{
					NameValueBean nameValueBean = (NameValueBean) listIterator.next();
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
	 * @throws IOException
	 *
	 */
	private void sendResponse(String responseXML, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		out.write(responseXML);

		// out.print(responseXML);
	}

	/**
	 * @param annotationForm
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws CacheException
	 */
	private void loadAnnotations(AnnotationForm annotationForm, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			CacheException
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
			loadGroupList(annotationForm, request);

			List conditionalInstancesList = populateConditionalInstanceList();
			annotationForm.setConditionalInstancesList(conditionalInstancesList);
			annotationForm.setConditionVal(new String[]{"-1"});

		}
	}

	/**
	 *
	 * @return
	 */
	private List populateConditionalInstanceList()
	{
		List conditionalInstancesList = new ArrayList();

		try
		{
			DefaultBizLogic bizLogic = new DefaultBizLogic();
			String[] displayNames = {"shortTitle", "title"};
			conditionalInstancesList = bizLogic.getList(CollectionProtocol.class.getName(),
					displayNames, "id", true);
			conditionalInstancesList = modifyName(conditionalInstancesList);
			conditionalInstancesList.remove(0);
			conditionalInstancesList.add(0, new NameValueBean(
					edu.wustl.catissuecore.util.global.Constants.HOLDS_ANY, "-1"));
		}
		catch (DAOException e)
		{
			Logger.out.debug(e.getMessage(), e);
		}
		return conditionalInstancesList;
	}

	/**
	 *
	 * @param conditionalInstancesList
	 * @return
	 */
	private List modifyName(List conditionalInstancesList)
	{
		List list = new ArrayList();
		for (Object x : conditionalInstancesList)
		{

			NameValueBean bean = (NameValueBean) x;
			String[] split = bean.getName().split(",");
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
	 * @param annotationForm
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 *
	 */
	private void loadGroupList(AnnotationForm annotationForm, HttpServletRequest request)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//List of groups
		Collection<NameValueBean> annotationGroupsList = getAnnotationGroups();
		String groupsXML = getGroupsXML(annotationGroupsList, request);
		annotationForm.setAnnotationGroupsXML(groupsXML);
	}

	/**
	 * @param annotationGroupsList
	 * @return
	 */
	private String getGroupsXML(Collection<NameValueBean> annotationGroupsList,
			HttpServletRequest request)
	{
		StringBuffer groupsXML = new StringBuffer();
		List dataList = new ArrayList();
		if (annotationGroupsList != null)
		{
			groupsXML.append("<?xml version='1.0' encoding='UTF-8'?>");
			groupsXML.append("<rows>");
			NameValueBean groupBean = null;
			Iterator<NameValueBean> iterator = annotationGroupsList.iterator();
			while (iterator.hasNext())
			{
				groupBean = iterator.next();
				List innerList = new ArrayList();
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
	 * @param annotationForm
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	private Collection<NameValueBean> getAnnotationGroups()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<NameValueBean> entityGroups = entityManager.getAllEntityGroupBeans();
		return entityGroups;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isAuthorizedToExecute(HttpServletRequest request) throws Exception
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
