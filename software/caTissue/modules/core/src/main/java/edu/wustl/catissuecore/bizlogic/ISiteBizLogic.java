package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public interface ISiteBizLogic {

	/**
	 * @param siteId
	 *            : siteId
	 * @return Collection
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public abstract Collection<CollectionProtocol> getRelatedCPs(Long siteId,
			DAO dao) throws BizLogicException;

	/**
	 * Gives the Site Object related to given container
	 * 
	 * @param containerId
	 *            - Long
	 * @return Site object
	 * @throws BizLogicException
	 *             throws BizLogicException
	 */
	public abstract Site getRelatedSite(Long containerId, DAO dao)
			throws BizLogicException;

	/**
	 * @param displayNameFields
	 *            - displayNameFields.
	 * @param valueField
	 *            - valueField
	 * @param activityStatusArr
	 *            - activityStatusArr
	 * @param userId
	 *            - userId
	 * @return List of site list
	 * @throws BizLogicException
	 *             throws BizLogicException
	 */
	public abstract List getSiteList(String[] displayNameFields,
			String valueField, String[] activityStatusArr, Long userId)
			throws BizLogicException;

	/**
	 * @param sourceObjectName
	 *            - sourceObjectName.
	 * @param displayNameFields
	 *            - displayNameFields
	 * @param valueField
	 *            - valueField
	 * @param activityStatusArr
	 *            - activityStatusArr
	 * @param isToExcludeDisabled
	 *            - isToExcludeDisabled
	 * @return List of objects
	 * @throws BizLogicException
	 *             throws BizLogicException
	 */
	public abstract List getRepositorySiteList(String sourceObjectName,
			String[] displayNameFields, String valueField,
			String[] activityStatusArr, boolean isToExcludeDisabled)
			throws BizLogicException;

	/**
	 * @param storageContainer
	 *            - StorageContainer object.
	 * @param site
	 *            Site object
	 * @param dao
	 *            DAO object
	 * @throws BizLogicException
	 *             throws BizLogicException
	 */
	public abstract void setSiteForSubContainers(
			StorageContainer storageContainer, Site site, DAO dao)
			throws BizLogicException;

	/**
	 * @param dao
	 *            Object of DAO
	 * @param containerId
	 *            id of container whose site is to be retrieved
	 * @return Site object belongs to container with given id
	 * @throws BizLogicException
	 *             Exception occure while DB handling
	 */
	public abstract Site getSite(DAO dao, Long containerId)
			throws BizLogicException;

	/**
	 * Retrieve activity status of the Site.
	 * 
	 * @param dao
	 * @param containerId
	 * @return String string
	 * @throws DAOException
	 */
	public abstract String getSiteId(DAO dao, Long containerId)
			throws BizLogicException;

	/**
	 * Retrieve the id of the Site on the basisi of site name.
	 * 
	 * @param dao
	 * @param siteName
	 * @return Long id
	 * @throws DAOException
	 */
	public abstract Long retriveSiteIdByName(DAO dao, String siteName)
			throws BizLogicException;

	/**
	 * @param dao
	 *            DAO object.
	 * @param containerId
	 *            - Long.
	 * @param errMessage
	 *            - String.
	 * @throws BizLogicException
	 *             throws BizLogicException
	 */
	public abstract void checkClosedSite(DAO dao, Long containerId,
			String errMessage) throws BizLogicException;

	/**
	 * Gives the Site Object related to given container whose name is given.
	 * 
	 * @param containerName
	 *            - String
	 * @return Site - site object
	 */
	public abstract Site getRelatedSiteForManual(String containerName, DAO dao)
			throws BizLogicException;

	public abstract Site getSiteByCtepId(String ctepId)
			throws BizLogicException;

	public abstract Site getSiteById(long id) throws BizLogicException;

}