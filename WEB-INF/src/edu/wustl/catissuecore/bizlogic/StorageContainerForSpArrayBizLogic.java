package edu.wustl.catissuecore.bizlogic;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;


public class StorageContainerForSpArrayBizLogic extends AbstractSCSelectionBizLogic
{
	
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(StorageContainerForSpArrayBizLogic.class);
	/**
	 * Gets allocated container map for specimen array.
	 * @param sp_arr_type_id
	 *            specimen array type id
	 * @param noOfAliqoutes
	 *            No. of aliquots
	 * @param sessionData - SessionDataBean object
	 * @param exceedingMaxLimit String
	 * @return container map
	 * @throws BizLogicException --
	 *             throws DAO Exception
	 * @see edu.wustl.common.dao.JDBCDAOImpl
	 */
	public TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>>
	getAllocatedContainerMapForSpecimenArray(long sp_arr_type_id,
			SessionDataBean sessionData, DAO dao)
			throws BizLogicException
	{
		try
		{
			final String[] queries = this.getStorageContainerForSpecimenArrQuery(sp_arr_type_id,
					sessionData);
			final List<?> containerList = this.getStorageContainerList(null,queries);
			return (TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>>)
			this.getAllocDetailsForContainers(containerList, dao);
		}
		catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			ErrorKey errorKey = ErrorKey.getErrorKey(daoExp.getErrorKeyName());
			throw new BizLogicException(errorKey, daoExp, daoExp.getMsgValues());
		}
	}
	
	/**
	 * Gets the query for Specimen Array.
	 * @param sp_arr_type_id Specimen Array Identifier
	 * @param sessionData SessionDataBean object
	 * @return String[]
	 */
	protected String[] getStorageContainerForSpecimenArrQuery(long sp_arr_type_id,
			SessionDataBean sessionData)
	{
		String includeAllIdQueryStr = " OR t4.SPECIMEN_ARRAY_TYPE_ID = '"
				+ Constants.ARRAY_TYPE_ALL_ID + "'";

		if (!(new Validator().isValidOption(String.valueOf(sp_arr_type_id))))
		{
			includeAllIdQueryStr = "";
		}
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("SELECT VIEW1.IDENTIFIER, VIEW1.NAME, VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY from ");
		stringBuilder.append("(");
		stringBuilder
				.append(" SELECT t1.IDENTIFIER,t1.name,c.one_dimension_capacity , c.two_dimension_capacity, (c.ONE_DIMENSION_CAPACITY * c.TWO_DIMENSION_CAPACITY)  CAPACITY ");
		stringBuilder
				.append(" FROM CATISSUE_CONTAINER t1 JOIN CATISSUE_CAPACITY c on t1.CAPACITY_ID=c.IDENTIFIER ");
		stringBuilder.append(" LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER t2 on t2.IDENTIFIER=t1.IDENTIFIER ");
		stringBuilder
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON t1.IDENTIFIER = K.CONTAINER_ID ");
		stringBuilder
				.append(" LEFT OUTER JOIN CATISSUE_CONTAINER_POSITION L ON t1.IDENTIFIER = L.PARENT_CONTAINER_ID ");
		stringBuilder.append(" LEFT OUTER join catissue_site S on S.IDENTIFIER = t2.SITE_ID ");
		stringBuilder.append(" WHERE t2.IDENTIFIER IN ");
		stringBuilder
				.append(" (SELECT t4.STORAGE_CONTAINER_ID from CATISSUE_CONT_HOLDS_SPARRTYPE t4  where t4.SPECIMEN_ARRAY_TYPE_ID = '"
						+ sp_arr_type_id + "'");
		stringBuilder.append(includeAllIdQueryStr);
		stringBuilder.append(")");
		if (!sessionData.isAdmin())
		{
			stringBuilder.append(" AND t2.SITE_ID in (SELECT SITE_ID from CATISSUE_SITE_USERS where USER_ID="
					+ sessionData.getUserId() + ")");
		}
		stringBuilder.append("AND t1.ACTIVITY_STATUS='" + Status.ACTIVITY_STATUS_ACTIVE
				+ "' and S.ACTIVITY_STATUS='Active' and t1.CONT_FULL=0) VIEW1 ");
		stringBuilder
				.append(" GROUP BY VIEW1.IDENTIFIER, VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY, VIEW1.TWO_DIMENSION_CAPACITY ,VIEW1.CAPACITY ");
		stringBuilder.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >  0");
		stringBuilder.append(" order by IDENTIFIER");
		return new String[]{stringBuilder.toString()};
	}
}
