package edu.wustl.catissuecore.bizlogic;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;

public interface ICollectionProtocolBizLogic extends ICaTissueDefaultBizLogic {

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
	public List<Object> retrieveCP(final Long id) throws BizLogicException;

	public List<CollectionProtocol> retrieve(final String className,
			final String colName, final Object colValue)
			throws BizLogicException;

	public CollectionProtocol retrieveB(final String className, final Long id)
			throws BizLogicException;

	public String getShortTitle(final Long cpId) throws BizLogicException;

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
	public Collection<Site> getRelatedSites(final DAO dao, final Long cpId)
			throws BizLogicException, DAOException;

	public boolean isReadDeniedTobeChecked();

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
			final StorageContainer storageContainer) throws BizLogicException;

	/**
	 * Gives List of NameValueBean of CP for given cpIds array.
	 *
	 * @param cpIds
	 *            - Long array of cpIds
	 * @return NameValueBean - List of NameValueBean
	 */
	public List<NameValueBean> getCPNameValueList(final long[] cpIds)
			throws BizLogicException;

	/**
	 * Finds a {@link CollectionProtocol} by its IRB ID and IRB Site identified by its CTEP code.
	 * @param irbId
	 * @param siteCtep
	 * @return
	 * @throws BizLogicException 
	 */
	public CollectionProtocol findByIRBInfo(String irbId, String siteCtep) throws BizLogicException;

	public abstract boolean isAuthorized(SessionDataBean sessionDataBean, CollectionProtocol cp, String privilegeName)
			throws BizLogicException;

	public Collection<CollectionProtocol> findByIRBInfo(String irbId) throws BizLogicException;

}