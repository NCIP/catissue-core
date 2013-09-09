package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import krishagni.catissueplus.mobile.dto.StoragePositionDTO;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;


public class StorageContainerForSpecimenBizLogic extends AbstractSCSelectionBizLogic
{
	
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(StorageContainerForContainerBizLogic.class);
	private static final boolean IS_CP_UNQ = true;
	private static final boolean IS_SPCLASS_UNQ = true;
	private static final boolean IS_CP_NONUNQ = false;
	private static final boolean IS_SPCLASS_NONUNQ = false;
	private static final boolean IS_SPTYPE_UNQ = true;
	private static final boolean IS_SPTYPE_NONUNQ = false;
	
	/**
	 * @param parameterList - consists of cpId, specimen class
	 * @param sessionData - SessionDataBean object
	 * @param jdbcDAO - JDBCDAO object
	 * @return TreeMap of Allocated Containers
	 * @throws BizLogicException throws BizLogicException
	 * @throws DAOException throws DAOException
	 */
	public TreeMap<NameValueBean, Map<NameValueBean, List<NameValueBean>>> 
			getAllocatedContainerMapForSpecimen(final List<Object> parameterList, 
			final SessionDataBean sessionData, final DAO dao,String contName)
			throws BizLogicException
	{
		try
		{
			parameterList.add(sessionData);
			final String[] queries = this.getStorageContainerForSpecimenQuery(parameterList,contName);
			final List<?> containerList = this.getStorageContainerList(null,queries);
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
	 * @param parameterList - consists of cpId, specimen class
	 * @param sessionData - SessionDataBean object
	 * @param jdbcDAO - JDBCDAO object
	 * @return TreeMap of Allocated Containers
	 * @throws BizLogicException throws BizLogicException
	 * @throws DAOException throws DAOException
	 */
	public LinkedHashMap	getAutoAllocatedContainerListForSpecimen(final List<Object> parameterList, 
			final SessionDataBean sessionData, final DAO dao,String contName)
			throws BizLogicException
	{
		try
		{
			parameterList.add(sessionData);
			final String[] queries = this.getStorageContainerForSpecimenQuery(parameterList,contName);
			final List<?> containerList = this.getStorageContainerList(null,queries);
			LinkedHashMap containerNameListForAutoOption=null;
			if(containerList!=null && !containerList.isEmpty())
			{
				containerNameListForAutoOption=new LinkedHashMap();
				java.util.Iterator<?> listIter=containerList.iterator();
				while(listIter.hasNext())
				{
					ArrayList contInfoList=(ArrayList) listIter.next();
					NameValueBean nvb=new NameValueBean();
					nvb.setName(contInfoList.get(1));
					nvb.setValue(contInfoList.get(0));
					containerNameListForAutoOption.put(nvb,nvb);
				}
			}
			return containerNameListForAutoOption;
		}
		catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = ErrorKey.getErrorKey(daoExp.getErrorKeyName());
			throw new BizLogicException(errorKey, daoExp, daoExp.getMsgValues());
		}
	}

/**
	 * This Api return list of available continues specimenPositon as per specimenCount
	 * @param containerName
	 * @param pos1
	 * @param pos2
	 * @param specimenCount
	 * @param dao
	 * @return
	 * @throws BizLogicException
	 */
	public List<SpecimenPosition> getAvailablePositionFromContainerForSpecimen(String containerName,String pos1,String pos2,int specimenCount,DAO dao) throws BizLogicException{
		List<SpecimenPosition> posList = new ArrayList<SpecimenPosition>();
		String query = getStorageContainerFromNameForSpecimenQuery(containerName);
		final String[] queries = {query};
		try
		{	
			final List<?> containerList = this.getStorageContainerList(null,queries);
			Iterator<?> itr = containerList.iterator();
			if(itr.hasNext()){
				final ArrayList<?> container = (ArrayList<?>) itr.next();
				Long containerId = Long.parseLong(container.get(0).toString());
				int positionDimensionOne = Integer.parseInt(container.get(2).toString());
				int positionDimensionTwo = Integer.parseInt(container.get(3).toString());
				final boolean[][] storagePositions = StorageContainerUtil. getAvailablePositionsForContainer(containerId.toString(),
						positionDimensionOne+1, positionDimensionTwo+1, dao);
				List labellingList=StorageContainerUtil.getLabellingSchemeByContainerId(containerId.toString());
				String oneDimensionLabellingScheme=(String) labellingList.get(0);//((ArrayList)labellingList.get(0)).get(0);
				String twoDimensionLabellingScheme=(String) labellingList.get(1);//((ArrayList)labellingList.get(0)).get(1);
				Integer pos1Integer = "".equals(pos1) || pos1 == null ?1: AppUtility.getPositionValueInInteger(oneDimensionLabellingScheme, pos1);
				Integer pos2Integer = "".equals(pos2) || pos2 == null ?1:AppUtility.getPositionValueInInteger(twoDimensionLabellingScheme, pos2);
				if(pos1Integer <=0 || pos1Integer > storagePositions.length || pos2Integer<=0 || pos2Integer > storagePositions[0].length){
					throw new BizLogicException(null, null, "Invalid Storage Position.");
				}
				boolean isfirstIteration = true;
				boolean isRequiredPositionAvaliable = false;
				List<int[]> positionList = new ArrayList<int[]>();
				for(int i = pos1Integer; i< storagePositions.length;i++){
					for(int j=1;j<storagePositions[i].length;j++){
						if(i==pos1Integer && isfirstIteration){
							j = pos2Integer;
						}
						isfirstIteration = false;
						if(storagePositions[i][j]){
							int[] posArr = {i,j};
							positionList.add(posArr);
							if(positionList.size()== specimenCount){
								isRequiredPositionAvaliable= true;
								break;
							}
						}else{
							positionList.clear();
						}
					}
					if(isRequiredPositionAvaliable){
						break;
					}
					
				}
				posList = createSpecimenPositionList(positionList,containerId);
			}
			
		}
		catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = ErrorKey.getErrorKey(daoExp.getErrorKeyName());
			throw new BizLogicException(errorKey, daoExp, daoExp.getMsgValues());
		}
		catch(NumberFormatException exc){
			throw new BizLogicException(null, null, "Invalid Storage Position.");
		}
		if(posList.isEmpty()){
			throw new BizLogicException(null, null, "Insuficient StoragePosition");
		}
		return posList;
	}
	
	public StoragePositionDTO[][] getPositionDetailsFromContainer(String containerName,DAO dao)  throws BizLogicException{
		List<SpecimenPosition> posList = new ArrayList<SpecimenPosition>();
		String query = getStorageContainerFromNameForSpecimenQuery(containerName);
		final String[] queries = {query};
		StoragePositionDTO[][] storagePositions = null;
		try
		{	
			final List<?> containerList = this.getStorageContainerList(null,queries);
			Iterator<?> itr = containerList.iterator();
			if(itr.hasNext()){
				final ArrayList<?> container = (ArrayList<?>) itr.next();
				Long containerId = Long.parseLong(container.get(0).toString());
				int positionDimensionOne = Integer.parseInt(container.get(2).toString());
				int positionDimensionTwo = Integer.parseInt(container.get(3).toString());
				storagePositions = StorageContainerUtil.getPositionDetailFormContainer(containerId.toString(),
						positionDimensionOne+1, positionDimensionTwo+1, dao);
			}
		}catch (final ApplicationException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			final ErrorKey errorKey = ErrorKey.getErrorKey(daoExp.getErrorKeyName());
			throw new BizLogicException(errorKey, daoExp, daoExp.getMsgValues());
		}
		return storagePositions;
	}
	
	
	public  List<SpecimenPosition> createSpecimenPositionList(List<int[]> positionList,Long containerId){
		List<SpecimenPosition> posList = new ArrayList<SpecimenPosition>();
		for(int i=0;i<positionList.size();i++){
			int[] posArr = positionList.get(i);
			StorageContainer sContainer = new StorageContainer();
			SpecimenPosition specimenPosition= new SpecimenPosition();
			specimenPosition.setPositionDimensionOne(posArr[0]);
			specimenPosition.setPositionDimensionTwo(posArr[1]);
			sContainer.setId(containerId);
			specimenPosition.setStorageContainer(sContainer);
			posList.add(specimenPosition);
		}
		return posList;
	}

	/**
	 * Gets the query array for Specimen Storage Containers
	 * @param cpId
	 * @param spClass
	 * @param aliquotCount
	 * @param sessionData
	 * @return
	 */
	protected String[] getStorageContainerForSpecimenQuery(final List<Object> parameterList,String contName)
	{
		if(!Validator.isEmpty(contName))
		{
			contName = contName.toLowerCase();
		}
		// Containers allowing Only this CP, Only this Specimen Class and only this Specimen Type
		final String query0 = this.createSCQuery(parameterList, IS_CP_UNQ, IS_SPCLASS_UNQ,IS_SPTYPE_UNQ,contName);
		// Containers only this CP restriction and just this Specimen Class and any specimen type
		final String query1 = this.createSCQuery(parameterList, IS_CP_UNQ, IS_SPCLASS_UNQ,IS_SPTYPE_NONUNQ,contName);
		// Containers allowing Only this CP but other Specimen Classes and SpecimenType also
		final String query2 = this.createSCQuery(parameterList, IS_CP_UNQ, IS_SPCLASS_NONUNQ,IS_SPTYPE_NONUNQ,contName);
		// Containers allowing Other CPs also but just this Specimen Class and Specimen Type
		final String query3 = this.createSCQuery(parameterList, IS_CP_NONUNQ, IS_SPCLASS_UNQ,IS_SPTYPE_UNQ,contName);
		// Containers allowing any CP just this Specimen Class and any specimen type
		final String query4 = this.createSCQuery(parameterList, IS_CP_NONUNQ, IS_SPCLASS_UNQ,IS_SPTYPE_NONUNQ,contName);
		// Containers allowing Others CPs also, other Specimen Classes and Specimen Type too
		final String query5 = this.createSCQuery(parameterList, IS_CP_NONUNQ, IS_SPCLASS_NONUNQ,IS_SPTYPE_NONUNQ,contName);
		// Containers no CP restriction and just this Specimen Class and Specimen Type
		final String query6 = this.getNoCPRestrictionQuery(parameterList, null, IS_SPCLASS_UNQ,IS_SPTYPE_UNQ,contName);
		// Containers no CP restriction and just this Specimen Class and any specimen type
		final String query7 = this.getNoCPRestrictionQuery(parameterList, null, IS_SPCLASS_UNQ,IS_SPTYPE_NONUNQ,contName);
		//Containers with no CP restrictions, other Specimen Classes and Specimen Type too
		final String query8 = this.getNoCPRestrictionQuery(parameterList, null, IS_SPCLASS_NONUNQ, IS_SPTYPE_NONUNQ,contName);
		//Containers allowing any CP,any Specimen Class and any Specimen Type
		final String query9 = this.createSCQuery(parameterList,	null, null, null, contName);
		
		return new String[]{query0, query1, query2, query3, query4, query5, query6, query7, query8, query9};
	}
	
	protected String getStorageContainerFromNameForSpecimenQuery(String containerName){
		StringBuffer query = new StringBuffer();
		query.append("SELECT VIEW1.IDENTIFIER,VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY,VIEW1.TWO_DIMENSION_CAPACITY,VIEW1.CAPACITY-COUNT(*)  AVAILABLE_SLOTS ");
		query.append(" FROM ( SELECT D.IDENTIFIER,D.NAME,F.ONE_DIMENSION_CAPACITY, F.TWO_DIMENSION_CAPACITY, (F.ONE_DIMENSION_CAPACITY * F.TWO_DIMENSION_CAPACITY)  CAPACITY ");
		query.append("FROM CATISSUE_CAPACITY F JOIN CATISSUE_CONTAINER D  ON F.IDENTIFIER = D.CAPACITY_ID ");
		query.append("  LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON D.IDENTIFIER = K.CONTAINER_ID ");
		query.append("  LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER C ON D.IDENTIFIER = C.IDENTIFIER "); 
		query.append(" LEFT OUTER JOIN CATISSUE_SITE L ON C.SITE_ID = L.IDENTIFIER ");
		query.append(" WHERE D.NAME = '"+containerName+"'" );
		query.append(") VIEW1  ");
		query.append(" GROUP BY IDENTIFIER, VIEW1.NAME, ");
		query.append(" VIEW1.ONE_DIMENSION_CAPACITY, ");
		query.append(" VIEW1.TWO_DIMENSION_CAPACITY, ");
		query.append(" VIEW1.CAPACITY ");
		
		return query.toString();
	}

	/**
	 * Forms a Query to get the Storage Container list
	 * @param cpId - Collection Protocol Id
	 * @param spClass - Specimen Class
	 * @param aliquotCount - Number of aliquotes that the fetched containers 
	 * should minimally support. A value of <b>0</b> specifies that there's
	 * no such restriction
	 * @param siteId - Site Id
	 * @return query string
	 */
	private String createSCQuery(final List<Object> parameterList,
			final Boolean isCPUnique, final Boolean isSPClassUnique,final Boolean isSPTypeUnique,String contName)
	{
		final long cpId = (Long)parameterList.get(0);
		final String spClass = (String)parameterList.get(1);
		final int aliquotCount = (Integer)parameterList.get(2);
		final String spType = (String)parameterList.get(3);
		final SessionDataBean sessionData = (SessionDataBean)parameterList.get(4);
		final Long userId = sessionData.getUserId();
		final boolean isAdmin = sessionData.isAdmin();
		final StringBuilder scQuery = new StringBuilder();
		scQuery
				.append("SELECT VIEW1.IDENTIFIER,VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY,VIEW1.TWO_DIMENSION_CAPACITY,VIEW1.CAPACITY-COUNT(*)  AVAILABLE_SLOTS ");
		scQuery
				.append(" FROM"
						+ " (SELECT D.IDENTIFIER,D.NAME,F.ONE_DIMENSION_CAPACITY, F.TWO_DIMENSION_CAPACITY,(F.ONE_DIMENSION_CAPACITY * F.TWO_DIMENSION_CAPACITY)  CAPACITY");
		scQuery
				.append(" FROM CATISSUE_CAPACITY F JOIN CATISSUE_CONTAINER D  ON F.IDENTIFIER = D.CAPACITY_ID");
		scQuery
				.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON D.IDENTIFIER = K.CONTAINER_ID ");
		scQuery.append(" LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER C ON D.IDENTIFIER = C.IDENTIFIER ");
		scQuery.append(" LEFT OUTER JOIN CATISSUE_SITE L ON C.SITE_ID = L.IDENTIFIER ");
		if (isCPUnique != null) //DO not join on CP if there is no restriction on CP. i.e isCPUnique=null 
		{
			scQuery
					.append(" LEFT OUTER JOIN CATISSUE_ST_CONT_COLL_PROT_REL A ON A.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
		}
		if (isSPClassUnique != null) //DO not join on SP CLS if there is no restriction on SP CLS. i.e isSPClassUnique=null
		{
			scQuery
					.append(" LEFT OUTER JOIN CATISSUE_STOR_CONT_SPEC_CLASS B ON B.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
		}
		if (isSPTypeUnique != null)//DO not join on SP Type if there is no restriction on SP Type. i.e isSPTypeUnique=null
		{
			scQuery.append(" LEFT OUTER JOIN CATISSUE_STOR_CONT_SPEC_TYPE SPT ON SPT.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
		}
		scQuery.append(" WHERE ");
		if (isCPUnique != null)
		{
			scQuery.append(" A.COLLECTION_PROTOCOL_ID = ");
			scQuery.append(cpId);
			scQuery.append(" AND ");
		}
		if (isSPClassUnique != null)
		{
			scQuery.append("  B.SPECIMEN_CLASS = '");
			scQuery.append(spClass);
			scQuery.append("'");
			scQuery.append(" AND ");
		}
		if (isSPTypeUnique != null)
		{
			scQuery.append("  SPT.SPECIMEN_TYPE = '");
			scQuery.append(spType);
			scQuery.append("'");
			scQuery.append(" AND ");
		}
		if (!isAdmin)
		{
			scQuery.append(" C.SITE_ID IN (SELECT M.SITE_ID FROM  ");
			scQuery.append(" CATISSUE_SITE_USERS M WHERE M.USER_ID = ");
			scQuery.append(userId);
			scQuery.append(" ) ");
			scQuery.append(" AND ");
		}
		scQuery.append("  L.ACTIVITY_STATUS = 'Active' AND D.ACTIVITY_STATUS='Active' AND D.CONT_FULL=0 "); //Added cont_full condition by Preeti
		if(contName!=null && !contName.equals(""))
		{
			scQuery.append(" AND ");
			scQuery.append(" lower(D.NAME) Like '%");
			scQuery.append(contName);
			scQuery.append("%' ");
		}
		scQuery.append(") VIEW1  ");
		scQuery.append(" GROUP BY IDENTIFIER, VIEW1.NAME, ");
		scQuery.append(" VIEW1.ONE_DIMENSION_CAPACITY, ");
		scQuery.append(" VIEW1.TWO_DIMENSION_CAPACITY, ");
		scQuery.append(" VIEW1.CAPACITY ");
		if (aliquotCount > 0)
		{
			scQuery.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) >=  ");
			scQuery.append(aliquotCount);
		}
		else
		{
			scQuery.append(" HAVING (VIEW1.CAPACITY - COUNT(*)) > 0  ");
		}
		scQuery.append(this.getStorageContainerCPQuery(isCPUnique));
		scQuery.append(this.getStorageContainerSPClassQuery(isSPClassUnique));
		scQuery.append(this.getStorageContainerSPTypeQuery(isSPTypeUnique));
		scQuery.append(" ORDER BY VIEW1.IDENTIFIER ");
		logger.debug(String.format("%s:%s:%s", this.getClass().getSimpleName(),
				"createSCQuery() query ", scQuery));
		return scQuery.toString();
	}

	/**
	 * Gets the restriction query for Containers for Collection Protocol
	 * @param isUnique - Specifies the kind of restriction where:
	 * <li><strong>true</strong> implies that the container should allow only one CP</li>
	 * <li><strong>false</strong> implies that the container allows more than one CPs</li>
	 * @return the query string
	 */
	private String getStorageContainerCPQuery(final Boolean isUnique)
	{
		String scCpQuery;
		final String SC_CP_TABLE_NAME = "CATISSUE_ST_CONT_COLL_PROT_REL";
		if (isUnique == null) //No restrictions on CP. Any CP condition
		{
			final StringBuilder scCPQuery = new StringBuilder();
			scCPQuery.append(" AND VIEW1.IDENTIFIER NOT IN ");
			scCPQuery.append(" ( ");
			scCPQuery.append(" SELECT t2.STORAGE_CONTAINER_ID FROM " + SC_CP_TABLE_NAME + " t2 ");
			scCPQuery.append(" ) ");
			scCpQuery = scCPQuery.toString();
		}
		else
		{
			scCpQuery = this.getSCBaseRestrictionQuery(SC_CP_TABLE_NAME, isUnique);
		}
		return scCpQuery;
	}

	/**
	 * Generates the base container restriction string 
	 * This allows for selection of Containers that allow Single/Multiple CPs,
	 * as well as Containers that allow Single/Multiple Specimen Classes
	 * @param tableName - the table name to apply the restriction on
	 * @param isUnique - specifies the multiplicity of the restriction
	 * @return the base query string
	 */
	private String getSCBaseRestrictionQuery(final String tableName, final boolean isUnique)
	{
		final StringBuilder scBaseRestQuery = new StringBuilder();
		scBaseRestQuery.append(" AND  ");
		scBaseRestQuery.append(" (( ");
		scBaseRestQuery.append(" SELECT COUNT(*) ");
		scBaseRestQuery.append(" FROM ");
		scBaseRestQuery.append(tableName);
		scBaseRestQuery.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER )");
		if (isUnique)
		{
			scBaseRestQuery.append(" = 1 ");
		}
		else
		{
			scBaseRestQuery.append(" >1 ");
		}
		scBaseRestQuery.append(" ) ");
		return scBaseRestQuery.toString();
	}

	/**
	 * Gets the restriction query for Containers for Specimen Class
	 * @param isUnique - Specifies the kind of restriction where:
	 * <li><strong>true</strong> implies that the container should allow only one type of Specimen</li>
	 * <li><strong>false</strong> implies that the container allows more than type of Specimens</li>
	 * @return the query string
	 */
	private String getStorageContainerSPClassQuery(final Boolean isUnique)
	{
		String scCPClass;
		final String SC_SP_TABLE_NAME = "CATISSUE_STOR_CONT_SPEC_CLASS";
		if (isUnique == null) //No restrictions on CP. Any CP condition
		{
			final StringBuilder scSPQuery = new StringBuilder();
			scSPQuery.append(" AND ");
			scSPQuery.append(" ( ");
			scSPQuery.append(" SELECT COUNT(*) FROM ");
			scSPQuery.append(SC_SP_TABLE_NAME);
			scSPQuery.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER ");
			scSPQuery.append(" ) ");
			scSPQuery.append(" =4 "); //No restriction on specimen class means it can store any of the 4 specimen classes
			scCPClass = scSPQuery.toString();
		}
		else
		{
			scCPClass = this.getSCBaseRestrictionQuery("CATISSUE_STOR_CONT_SPEC_CLASS", isUnique);
		}
		return scCPClass;
	}
	/**
	 * Gets the restriction query for Containers for Specimen Type
	 * @param isUnique - Specifies the kind of restriction where:
	 * <li><strong>true</strong> implies that the container should allow only one type of Specimen</li>
	 * <li><strong>false</strong> implies that the container allows more than type of Specimens</li>
	 * @return the query string
	 */
	private String getStorageContainerSPTypeQuery(Boolean isUnique)
	{
		final String SC_SPCLS_TABLE_NAME = "CATISSUE_STOR_CONT_SPEC_TYPE";
		String scCPType;
		if (isUnique == null)
		{
			final StringBuilder sbSPQuery = new StringBuilder();
			sbSPQuery.append(" AND ");
			sbSPQuery.append(" ( ");
			sbSPQuery.append(" SELECT COUNT(*) FROM ");
			sbSPQuery.append(SC_SPCLS_TABLE_NAME);
			sbSPQuery.append(" AA WHERE AA.STORAGE_CONTAINER_ID = VIEW1.IDENTIFIER ");
			sbSPQuery.append(" ) ");
			sbSPQuery.append(" >1 ");//No restriction on specimen type means it can store any of the 46 specimen types
			scCPType = sbSPQuery.toString();
		}
		else
		{
			scCPType = this.getSCBaseRestrictionQuery("CATISSUE_STOR_CONT_SPEC_TYPE", isUnique);
		}
		return scCPType;
	}
	/**
	 * 
	 */
	private String getNoCPRestrictionQuery(final List<Object> parameterList,
			final Boolean isCPUnique, final Boolean isSPClassUnique,final Boolean isSPTypeUnique,String contName)
	{
		final String spClass = (String)parameterList.get(1);
		final String spType = (String)parameterList.get(3);
		final SessionDataBean sessionData = (SessionDataBean)parameterList.get(4);
		final Long userId = sessionData.getUserId();
		final boolean isAdmin = sessionData.isAdmin();
		final StringBuilder scQuery = new StringBuilder();
		String spClassCount = "=";
		String spTypeCount = "=";
		if(!isSPClassUnique)
		{
			spClassCount=">";
		}
		if(!isSPTypeUnique)
		{
			spTypeCount=">";
		}
		StringBuffer adminQuery = new StringBuffer(); 
		if (!isAdmin)
		{
			adminQuery.append(" AND C.SITE_ID IN (SELECT M.SITE_ID FROM  ");
			adminQuery.append(" CATISSUE_SITE_USERS M WHERE M.USER_ID = ");
			adminQuery.append(userId);
			adminQuery.append(" ) ");
		}
		StringBuffer contNameQuery=new StringBuffer();
		if(contName!=null && !contName.equals(""))
		{
			contNameQuery.append(" AND ");
			contNameQuery.append(" lower(d.NAME) Like '%");
			contNameQuery.append(contName);
			contNameQuery.append("%' ");
		}
		scQuery.append("SELECT identifier,name,one_dimension_capacity,two_dimension_capacity,available_slots FROM " +
				"(SELECT  storage_container_id,count(*) AS lord FROM catissue_stor_cont_spec_type GROUP BY storage_container_id " +
				"HAVING count(*) "+spTypeCount+" 1) cscspt, " +
				"(SELECT storage_container_id, count(*) den FROM catissue_stor_cont_spec_class cscpc GROUP BY storage_container_id " +
				"HAVING count(*) "+spClassCount+" 1) cscpc, " +
				"(SELECT d.identifier, d.NAME, f.one_dimension_capacity, f.two_dimension_capacity, (f.one_dimension_capacity * f.two_dimension_capacity) capacity, count(*) cnt," +
				"((f.one_dimension_capacity * f.two_dimension_capacity)-count(*)) available_slots FROM catissue_capacity f JOIN " +
				"catissue_container d ON f.identifier = d.capacity_id " +
				"LEFT OUTER JOIN catissue_specimen_position k ON d.identifier = k.container_id " +
				"JOIN catissue_storage_container c ON d.identifier = c.identifier " +
				"JOIN catissue_site l ON c.site_id = l.identifier " +
				"JOIN catissue_stor_cont_spec_class b ON b.storage_container_id = c.identifier " +
				"JOIN catissue_stor_cont_spec_type spt ON spt.storage_container_id = c.identifier WHERE d.identifier NOT IN " +
				"(SELECT t2.storage_container_id FROM catissue_st_cont_coll_prot_rel t2) " +
				"AND b.specimen_class = '" +spClass+"'" +
				"AND spt.specimen_type = '" +spType+"'" + adminQuery.toString() +
				"AND l.activity_status = 'Active' " +
				"AND d.activity_status = 'Active' AND d.cont_full = 0 " + contNameQuery.toString() +
				"GROUP BY d.identifier, d.NAME, f.one_dimension_capacity," +
				"f.two_dimension_capacity,(f.one_dimension_capacity * f.two_dimension_capacity)) view1 " +
				"WHERE  cscspt.storage_container_id = view1.identifier AND cscpc.storage_container_id = view1.identifier " +
				"and available_slots > 0 order by identifier");

		
		logger.debug(String.format("%s:%s:%s", this.getClass().getSimpleName(),
				"createSCQuery() query ", scQuery));
		return scQuery.toString();
	
	}
	
}
