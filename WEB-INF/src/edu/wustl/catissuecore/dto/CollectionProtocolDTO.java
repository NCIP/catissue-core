/**
 */

package edu.wustl.catissuecore.dto;

import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;

/**
 * @author supriya_dankh
 * This class is used to transfer data related to collection protocol
 *  as well as all the details related to privileges of users
 * of a collection protocol that is being inserted or updated.
 */
public class CollectionProtocolDTO
{
	/**
	 * collectionProtocol.
	 */
	private CollectionProtocol collectionProtocol;
	/**
	 * rowIdBeanMap.
	 */
	Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
	/**
	 * @return CollectionProtocol
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}
	/**
	 * @param collectionProtocol : collectionProtocol
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}
	/**
	 * @return Map
	 */
	public Map<String, SiteUserRolePrivilegeBean> getRowIdBeanMap()
	{
		return rowIdBeanMap;
	}
	/**
	 * @param rowIdBeanMap : rowIdBeanMap
	 */
	public void setRowIdBeanMap(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap)
	{
		this.rowIdBeanMap = rowIdBeanMap;
	}

}
