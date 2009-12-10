package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;


public class StorageContainerForContainerBizLogic 
				extends AbstractSCSelectionBizLogic
{
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(StorageContainerForContainerBizLogic.class);
	
	
	/**
	 * @param type_id - long .
	 * @param sessionDataBean - SessionDataBean object
  	 * @param storageType Auto, Manual, Virtual
	 * @return TreeMap of allocated containers
	 * @throws BizLogicException throws BizLogicException
	 */
	public TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>>
		getAllocatedContainerMapForContainer(final long type_id,final SessionDataBean sessionDataBean,
				final DAO dao, final String storageType)
			throws BizLogicException
	{
		try
		{
			final String[] queries = this.getStorageContainerForContainerQuery(type_id,sessionDataBean);
			final List<?> containerList = this.getStorageContainerList(storageType, queries);
			return (TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>>)
			this.getAllocDetailsForContainers(containerList, dao);
		}
		catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = ErrorKey.getErrorKey(daoExp.getErrorKeyName());
			throw new BizLogicException(errorKey, daoExp, daoExp.getMsgValues());
		}
		
	}
	/**
	 * Gets the query array for Container
 	 * @param type_id - long.
	 * @param sessionDataBean - SessionDataBean object
	 */
	protected String[] getStorageContainerForContainerQuery(long type_id, final SessionDataBean sessionData)
	{
		final StringBuilder scQuery = new StringBuilder();
		scQuery
				.append("SELECT VIEW1.IDENTIFIER, VIEW1.NAME, VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY FROM  ");
		scQuery
				.append("(SELECT cont.IDENTIFIER, cont.NAME, cap.ONE_DIMENSION_CAPACITY, cap.TWO_DIMENSION_CAPACITY, (cap.ONE_DIMENSION_CAPACITY * cap.TWO_DIMENSION_CAPACITY)  CAPACITY ");
		scQuery
				.append("	FROM CATISSUE_CAPACITY cap JOIN CATISSUE_CONTAINER cont   on cap.IDENTIFIER = cont.CAPACITY_ID  ");
		scQuery
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON cont.IDENTIFIER = K.CONTAINER_ID ");
		scQuery
				.append("  LEFT OUTER JOIN CATISSUE_CONTAINER_POSITION L ON cont.IDENTIFIER = L.PARENT_CONTAINER_ID ");
		scQuery.append(" WHERE cont.IDENTIFIER IN ");
		scQuery.append(" (SELECT t4.STORAGE_CONTAINER_ID   FROM CATISSUE_ST_CONT_ST_TYPE_REL t4 ");
		scQuery.append(" WHERE (t4.STORAGE_TYPE_ID = '" + type_id);
		scQuery.append("' OR t4.STORAGE_TYPE_ID='1') and t4.STORAGE_CONTAINER_ID in ");
		scQuery.append(" (select SC.IDENTIFIER from CATISSUE_STORAGE_CONTAINER SC ");
		scQuery
				.append(" join CATISSUE_SITE S on sc.site_id=S.IDENTIFIER and S.ACTIVITY_STATUS!='Closed' ");
		if (!sessionData.isAdmin())
		{
			scQuery.append(" and S.IDENTIFIER in(SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		scQuery.append(")) ");
		scQuery.append(" AND cont.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE);
		scQuery.append("' and cont.CONT_FULL=0 ) VIEW1 ");
		scQuery
				.append(" GROUP BY VIEW1.IDENTIFIER, VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY ,VIEW1.CAPACITY ");
		scQuery.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >  0 ");
		scQuery.append(" ORDER BY VIEW1.IDENTIFIER");
		return new String[]{scQuery.toString()};
	}
	
}
