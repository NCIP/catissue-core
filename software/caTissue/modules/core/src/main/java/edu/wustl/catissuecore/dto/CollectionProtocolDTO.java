/**
 */

package edu.wustl.catissuecore.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.CPGridGrouperPrivilege;
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

	/** List of grid privileges **/
	private List<CPGridGrouperPrivilege> gridPrivilegeList = new ArrayList<CPGridGrouperPrivilege>();
	
	/**
	 * @return CollectionProtocol
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return this.collectionProtocol;
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
		return this.rowIdBeanMap;
	}

	/**
	 * @param rowIdBeanMap : rowIdBeanMap
	 */
	public void setRowIdBeanMap(Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap)
	{
		this.rowIdBeanMap = rowIdBeanMap;
	}

	public List<CPGridGrouperPrivilege> getGridPrivilegeList() {
		return gridPrivilegeList;
	}

	public void setGridPrivilegeList(List<CPGridGrouperPrivilege> gridPrivilegeList) {
		this.gridPrivilegeList = gridPrivilegeList;
	}

}
