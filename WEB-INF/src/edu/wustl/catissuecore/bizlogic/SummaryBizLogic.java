/**
 * @author mandar_deshmukh
 * This class is created to get all the details required on summary page.
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

public class SummaryBizLogic extends CatissueDefaultBizLogic
{

	/**
	 * @author mandar_deshmukh
	 */
	public SummaryBizLogic()
	{
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SummaryBizLogic.class);

	/**
	 * JDBCDAO object
	 */
	private static JDBCDAO jdbcDAO = null;

	/**
	 * Returns Map which has all the details of Summary Page
	 * @return Map<String, Object>
	 * @throws BizLogicException - throws BizLogicException
	 */
	public Map<String, Object> getTotalSummaryDetails() throws BizLogicException
	{

		Map<String, Object> summaryDataMap = null;
		try
		{
			// Database connection established
			jdbcDAO = this.openJDBCSession();

			summaryDataMap = new HashMap<String, Object>();
			summaryDataMap.put("TotalSpecimenCount", this.getTotalSpecimenCount());
			summaryDataMap.put("TissueCount", this.getSpecimenTypeCount(Constants.TISSUE));
			summaryDataMap.put("CellCount", this.getSpecimenTypeCount(Constants.CELL));
			summaryDataMap.put("MoleculeCount", this.getSpecimenTypeCount(Constants.MOLECULE));
			summaryDataMap.put("FluidCount", this.getSpecimenTypeCount(Constants.FLUID));
			summaryDataMap.put("TissueTypeDetails", this
					.getSpecimenTypeDetailsCount(Constants.TISSUE));
			summaryDataMap.put("CellTypeDetails", this.getSpecimenTypeDetailsCount(Constants.CELL));
			summaryDataMap.put("MoleculeTypeDetails", this
					.getSpecimenTypeDetailsCount(Constants.MOLECULE));
			summaryDataMap.put("FluidTypeDetails", this
					.getSpecimenTypeDetailsCount(Constants.FLUID));
			summaryDataMap.put("TissueQuantity", this.getSpecimenTypeQuantity(Constants.TISSUE));
			summaryDataMap.put("CellQuantity", this.getSpecimenTypeQuantity(Constants.CELL));
			summaryDataMap
					.put("MoleculeQuantity", this.getSpecimenTypeQuantity(Constants.MOLECULE));
			summaryDataMap.put("FluidQuantity", this.getSpecimenTypeQuantity(Constants.FLUID));

			//20Jan09 : --------- New Data 
			summaryDataMap.put(Constants.SP_PATHSTAT, this.getPathStatsWiseCount());
			summaryDataMap.put(Constants.SP_TSITE, this.getTSiteWiseCount());
			summaryDataMap.put(Constants.P_BYCD, this.getPbyCD());
			summaryDataMap.put(Constants.P_BYCS, this.getPbyCS());
			summaryDataMap.put(Constants.TOTAL_PART_COUNT, this.getTotalParticipantCount());

			summaryDataMap.put(Constants.TOTAL_CP_COUNT, this.getTotalCPCount());
			summaryDataMap.put(Constants.TOTAL_DP_COUNT, this.getTotalDPCount());
			summaryDataMap.put(Constants.REPO_SITE_COUNT, this
					.getTotalSiteCount(Constants.REPO_SITE));
			summaryDataMap
					.put(Constants.LAB_SITE_COUNT, this.getTotalSiteCount(Constants.LAB_SITE));
			summaryDataMap.put(Constants.COLL_SITE_COUNT, this
					.getTotalSiteCount(Constants.COL_SITE));
			summaryDataMap.put(Constants.TOTAL_USER_COUNT, this.getTotalUserCount());

			summaryDataMap.put(Constants.USER_DATA, this.getUserData());

		}
		catch (final ClassNotFoundException e)
		{
			logger.error(e.getMessage(), e);
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
		}
		finally
		{
			this.closeJDBCSession(jdbcDAO);
		}
		return summaryDataMap;
	}

	/**
	 * Returns the count of speciman of the type passed.
	 * @param specimanType - String of Specimen Type, JDDCDAO object
	 * @return String - Specimen type count
	 * @throws DAOException throws DAOException
	 */
	private String getSpecimenTypeCount(final String specimanType) throws DAOException,
			ClassNotFoundException
	{
		String pValDNme = "0";
		final String sql = "select count(*) from CATISSUE_SPECIMEN specimen join catissue_abstract_specimen absspec "
				+ " on specimen.identifier=absspec.identifier where absspec.SPECIMEN_CLASS = '"
				+ specimanType
				+ "' and specimen.COLLECTION_STATUS = 'Collected' and specimen"
				+ DISABLED;
		try
		{
			final List list = jdbcDAO.executeQuery(sql);

			if (!list.isEmpty())
			{
				final List rowList = (List) list.get(0);
				pValDNme = (String) rowList.get(0);
			}
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
		}
		return pValDNme;
	}

	/**
	 * String object DESC_ORDER.
	 */
	private static final String DESC_ORDER = " order by 2 desc";

	/**
	 * Returns the Specimen Sub-Type and its available Quantity.
	 * @param specimenType Class whose details are to be retrieved
	 * @return Vector of type and count name value bean
	 * @throws DAOException
	 */
	private List getSpecimenTypeDetailsCount(final String specimenType) throws BizLogicException

	{
		final String sql = "select absspec.SPECIMEN_TYPE,COUNT(*) from CATISSUE_SPECIMEN specimen "
				+ "join catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier "
				+ "and specimen.COLLECTION_STATUS='Collected' and specimen" + DISABLED
				+ " and absspec.SPECIMEN_CLASS = '" + specimenType
				+ "'group by absspec.SPECIMEN_TYPE " + DESC_ORDER;
		final List<NameValueBean> nameValuePairs = this.getNameValuePairs(sql);
		return nameValuePairs;
	}

	/**
	 * Returns the count of speciman of the type passed.
	 * @param specimanType- specimanType
	 * @return sum of SpecimenTypeQuantity
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getSpecimenTypeQuantity(final String specimanType) throws DAOException,
			ClassNotFoundException
	{
		String pValDNme = "0";
		final String sql = "select sum(AVAILABLE_QUANTITY) from CATISSUE_SPECIMEN specimen join"
				+ " catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier"
				+ " where absspec.SPECIMEN_CLASS='" + specimanType + "' and specimen" + DISABLED;
		try
		{
			final List list = jdbcDAO.executeQuery(sql);

			if (!list.isEmpty())
			{
				final List rowList = (List) list.get(0);
				pValDNme = (String) rowList.get(0);
			}
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
		}
		return pValDNme;
	}

	/**
	 * String object DISABLED.
	 */
	private static final String DISABLED = ".ACTIVITY_STATUS not in ( 'Disabled')";

	/***
	 * Returns the Total Specimen Count of caTissue.
	 * @return Total specimen count value
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalSpecimenCount() throws DAOException, ClassNotFoundException
	{

		final String sql = "select count(*) from CATISSUE_SPECIMEN specimen join "
				+ "catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier "
				+ "where specimen.COLLECTION_STATUS='Collected' and specimen" + DISABLED;
		return this.getCount(sql);
	}

	/***
	 * Returns the Total Participant Count of caTissue.
	 * @return String - Total participant count
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalParticipantCount() throws DAOException, ClassNotFoundException
	{
		final String sql = "SELECT COUNT(*) FROM CATISSUE_PARTICIPANT A WHERE A" + DISABLED;
		return this.getCount(sql);
	}

	/***
	 * Returns the Total CP Count of caTissue.
	 * @return String -Total user count
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalUserCount() throws DAOException, ClassNotFoundException
	{
		final String sql = "SELECT COUNT(*) FROM CATISSUE_USER A WHERE A" + DISABLED;
		return this.getCount(sql);
	}

	/***
	 * Returns the Total CP Count of caTissue.
	 * @return String - Total CP count.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalCPCount() throws DAOException, ClassNotFoundException
	{
		final String sql = "SELECT COUNT(*) FROM CATISSUE_SPECIMEN_PROTOCOL A, CATISSUE_COLLECTION_PROTOCOL B WHERE A.IDENTIFIER = B.IDENTIFIER AND A"
				+ DISABLED;
		return this.getCount(sql);
	}

	/***
	 * Returns the Total DP Count of caTissue.
	 * @return String - Total distribution protocol count
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalDPCount() throws DAOException, ClassNotFoundException
	{
		final String sql = "SELECT COUNT(*) FROM CATISSUE_SPECIMEN_PROTOCOL A, CATISSUE_DISTRIBUTION_PROTOCOL B WHERE A.IDENTIFIER = B.IDENTIFIER AND A"
				+ DISABLED;
		return this.getCount(sql);
	}

	/***
	 * Returns the Total Site Count as per site type of caTissue
	 * @param siteType - site type
	 * @return String - Total Site count
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getTotalSiteCount(final String siteType) throws DAOException,
			ClassNotFoundException
	{
		final String sql = "SELECT COUNT(*) FROM CATISSUE_SITE A WHERE A.TYPE LIKE '" + siteType
				+ "' AND A" + DISABLED;
		return this.getCount(sql);
	}

	/** 
	 * @return List of user data.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List getUserData() throws DAOException, ClassNotFoundException
	{
		final List<List> userData = new ArrayList<List>();
		final String sql = "SELECT concat(FIRST_NAME,concat(',',LAST_NAME)), EMAIL_ADDRESS,B.PHONE_NUMBER FROM CATISSUE_USER A, CATISSUE_ADDRESS B WHERE CSM_USER_ID  IN (SELECT USER_ID FROM CSM_USER_GROUP WHERE GROUP_ID=1) AND A.IDENTIFIER>1 AND A.ADDRESS_ID=B.IDENTIFIER  AND A"
				+ DISABLED;
		try
		{
			final List list = jdbcDAO.executeQuery(sql);
			if (!list.isEmpty())
			{
				for (int cnt = 0; cnt < list.size(); cnt++)
				{
					final List rowList = (List) list.get(cnt);
					userData.add(rowList);
				}
			}
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
		}
		return userData;

	}

	/**
	 * @param sql - sql
	 * @return count value
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private String getCount(final String sql) throws DAOException, ClassNotFoundException
	{
		String pValDNme = "0";
		try
		{
			final List list = jdbcDAO.executeQuery(sql);
			if (!list.isEmpty())
			{
				final List rowList = (List) list.get(0);
				pValDNme = (String) rowList.get(0);
			}
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
		}
		return pValDNme;

	}

	/**
	 * @return  Returns the List of specimen count based on the pathology status.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List getPathStatsWiseCount() throws BizLogicException
	{
		final String sql = "select PATHOLOGICAL_STATUS,count(*) from catissue_abstract_specimen abs,"
				+ " catissue_specimen sp where abs.identifier = sp.identifier and sp.COLLECTION_STATUS "
				+ "like 'Collected' and sp"
				+ DISABLED
				+ " group by PATHOLOGICAL_STATUS"
				+ DESC_ORDER;

		return this.executeSQL(sql);
	}

	/**
	 * @return  Returns the List of specimen count based on the Tissue Site.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List getTSiteWiseCount() throws BizLogicException
	{
		final String sql = "SELECT TISSUE_SITE, COUNT(B.IDENTIFIER) FROM CATISSUE_SPECIMEN_CHAR A, "
				+ "CATISSUE_ABSTRACT_SPECIMEN B, CATISSUE_SPECIMEN C WHERE "
				+ "A.IDENTIFIER = B.SPECIMEN_CHARACTERISTICS_ID AND B.IDENTIFIER = C.IDENTIFIER AND "
				+ "C.ACTIVITY_STATUS NOT IN ('Disabled') AND C.COLLECTION_STATUS LIKE 'Collected' GROUP BY A.TISSUE_SITE"
				+ DESC_ORDER;

		return this.executeSQL(sql);
	}

	/**
	 * @return Returns the List of participant count based on the clinical diagnosis.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List getPbyCD() throws BizLogicException
	{
		final String sql = "SELECT CLINICAL_DIAGNOSIS, COUNT(distinct C.PARTICIPANT_ID) FROM CATISSUE_ABS_SPECI_COLL_GROUP A,"
				+ " CATISSUE_SPECIMEN_COLL_GROUP B, CATISSUE_COLL_PROT_REG C, CATISSUE_PARTICIPANT D WHERE"
				+ " A.IDENTIFIER = B.IDENTIFIER AND B.COLLECTION_PROTOCOL_REG_ID = C.IDENTIFIER AND "
				+ "D.IDENTIFIER = C.PARTICIPANT_ID AND D"
				+ DISABLED
				+ " AND "
				+ "C"
				+ DISABLED
				+ " GROUP BY CLINICAL_DIAGNOSIS" + DESC_ORDER;

		return this.executeSQL(sql);
	}

	/**
	 * @return Returns the List of participant count based on the clinical status.
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List getPbyCS() throws BizLogicException
	{
		final String sql = "SELECT CLINICAL_STATUS, COUNT(distinct C.PARTICIPANT_ID) FROM CATISSUE_ABS_SPECI_COLL_GROUP A,"
				+ " CATISSUE_SPECIMEN_COLL_GROUP B, CATISSUE_COLL_PROT_REG C, CATISSUE_PARTICIPANT D WHERE "
				+ "A.IDENTIFIER = B.IDENTIFIER AND B.COLLECTION_PROTOCOL_REG_ID = C.IDENTIFIER AND "
				+ "D.IDENTIFIER = C.PARTICIPANT_ID AND D"
				+ DISABLED
				+ " AND "
				+ "C"
				+ DISABLED
				+ " GROUP BY CLINICAL_STATUS" + DESC_ORDER;

		return this.executeSQL(sql);
	}

	/**
	 * @param sql - sql.
	 * @return List of objects
	 * @throws DAOException throws DAOException
	 * @throws ClassNotFoundException throws ClassNotFoundException
	 */
	private List executeSQL(final String sql) throws BizLogicException
	{
		final List<NameValueBean> nameValuePairs = this.getNameValuePairs(sql);
		return nameValuePairs;
	}

	/**
	 * This method will be called to get list of name value pairs.
	 * @param sql
	 * @return NameValueBeans
	 * @throws BizLogicException 
	 */
	private List<NameValueBean> getNameValuePairs(final String sql) throws BizLogicException
	{

		final List<NameValueBean> nameValuePairs = new ArrayList<NameValueBean>();
		try
		{
			final List list = jdbcDAO.executeQuery(sql);
			if (!list.isEmpty())
			{
				// Creating name value beans.
				for (int i = 0; i < list.size(); i++)
				{
					final List detailList = (List) list.get(i);
					final NameValueBean nameValueBean = new NameValueBean();
					nameValueBean.setName(detailList.get(0));
					nameValueBean.setValue(detailList.get(1));
					logger.debug(i + " : " + nameValueBean.toString());
					nameValuePairs.add(nameValueBean);
				}
			}
		}
		catch (final DAOException e)
		{
			logger.error(e.getMessage(), e);
			throw new BizLogicException(e.getErrorKey(), e, e.getMsgValues());
		}
		return nameValuePairs;
	}

}
