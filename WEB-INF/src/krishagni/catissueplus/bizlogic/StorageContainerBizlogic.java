
package krishagni.catissueplus.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.StorageContainerViewDTO;
import krishagni.catissueplus.dto.StoragePositionDTO;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class StorageContainerBizlogic
{

	private static final boolean IS_CP_UNQ = true;
	private static final boolean IS_SPCLASS_UNQ = true;
	private static final boolean IS_CP_NONUNQ = false;
	private static final boolean IS_SPCLASS_NONUNQ = false;
	private static final boolean IS_SPTYPE_UNQ = true;
	private static final boolean IS_SPTYPE_NONUNQ = false;
	private static final int CONTAINERS_MAX_LIMIT = Integer.parseInt(XMLPropertyHandler
			.getValue(Constants.CONTAINERS_MAX_LIMIT));
	private static final String INVALID_STORAGE_CONTAINER_NAME="";

	/**
	 * Gives container details DTO for given container name.
	 * @param containerName
	 * @param dao 
	 * @return
	 * @throws ApplicationException
	 */
	public StorageContainerViewDTO getContainerDetailsForDataView(String containerName,
			HibernateDAO dao) throws ApplicationException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, containerName));

		//Step1: sql to get label schemes, dimensions labels,capacity id by joining type and container
		List list = dao.executeNamedQuery("getStorageContainerDetailsByContainerName", params);

		//Step2: populate storageContainerViewDTO from the retrieved data 
		StorageContainerViewDTO storageContainerViewDTO = null;

		if (null != list && !list.isEmpty())
		{
			//retrieve data from list
			Long containerId = (Long) ((Object[]) list.get(0))[0];
			String oneDimensionLabellingScheme = (String) ((Object[]) list.get(0))[1];
			String twoDimensionLabellingScheme = (String) ((Object[]) list.get(0))[2];
			String oneDimensionLabel = (String) ((Object[]) list.get(0))[3];
			String twoDimensionLabel = (String) ((Object[]) list.get(0))[4];
			Integer oneDimensionCapacity = (Integer) ((Object[]) list.get(0))[5];
			Integer twoDimensionCapacity = (Integer) ((Object[]) list.get(0))[6];

			//get storage position details
			StoragePositionDTO[][] availablePositions = getStoragePositionDetails(containerId,
					oneDimensionCapacity + 1, twoDimensionCapacity + 1, dao);

			//set DTO fields
			storageContainerViewDTO = new StorageContainerViewDTO();
			storageContainerViewDTO.setContainerId(containerId);
			storageContainerViewDTO.setOneDimensionLabellingScheme(oneDimensionLabellingScheme);
			storageContainerViewDTO.setTwoDimensionLabellingScheme(twoDimensionLabellingScheme);
			storageContainerViewDTO.setOneDimensionLabel(oneDimensionLabel);
			storageContainerViewDTO.setTwoDimensionLabel(twoDimensionLabel);
			storageContainerViewDTO.setOneDimensionCapacity(oneDimensionCapacity);
			storageContainerViewDTO.setTwoDimensionCapacity(twoDimensionCapacity);
			storageContainerViewDTO.setStoragePositionDTOCollection(availablePositions);
		}

		return storageContainerViewDTO;
	}

	/**
	 * Gives position details of the given container
	 * @param containerId
	 * @param dimX
	 * @param dimY
	 * @param dao
	 * @return
	 * @throws BizLogicException
	 * @throws DAOException 
	 */
	public static StoragePositionDTO[][] getStoragePositionDetails(Long containerId, int dimX,
			int dimY, HibernateDAO dao) throws BizLogicException, DAOException
	{
		final StoragePositionDTO[][] positions = new StoragePositionDTO[dimX][dimY];

		//populateContainerWithEmptyPositions(dimX, dimY, positions);//create container position array with all positions empty

		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, containerId));

		//get stored specimen details
		List list = dao.executeNamedQuery("getSpecimenPositionDetailsByContainerId", params);

		if (list != null)
		{
			setPositionDTO(positions, list);//set positions of occupied specimens
		}

		//get stored child container details
		List list2 = dao.executeNamedQuery("getContainerPositionDetailsByContainerId", params);

		if (list2 != null)
		{
			setPositionDTO(positions, list2);//set positions of occupied child containers
		}

		return positions;
	}

	/**
	 * Sets position for occupied specimens and child containers
	 * @param positions
	 * @param resultSet
	 * @param isListOfSpecimen
	 */
	private static void setPositionDTO(StoragePositionDTO[][] positions, List resultSet)
	{
		int countX, countY;
		for (int i = 0; i < resultSet.size(); i++)
		{
			final Object[] columnList = (Object[]) resultSet.get(i);
			countX = (Integer) columnList[0];
			countY = (Integer) columnList[1];

			StoragePositionDTO dtoObj = new StoragePositionDTO();
			dtoObj.setPosDimensionX(countX);
			dtoObj.setPosDimensionY(countY);
			dtoObj.setObjLabel((String) columnList[2]);

			positions[countX][countY] = dtoObj;
		}
	}


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
	 * @param positions - boolean array of position.
	 * @param list - list of objects
	 */
	private void setPositions(boolean[][] positions, List resultSet)
	{
		if (resultSet != null)
		{
			int countX, countY;
			for (int i = 0; i < resultSet.size(); i++)
			{
				final Object[] columnList = (Object[]) resultSet.get(i);
				if ((columnList != null) && (columnList.length == 2))
				{
					countX = (Integer) columnList[0];
					countY = (Integer) columnList[1];
					positions[countX][countY] = false;
				}
			}
		}
	}


	/**
	 * Checking specimen.
	 * @param containerName container name
	 * @param pos1 position dim 1
	 * @param pos2 position dim 2
	 * @return Boolean
	 * @throws ApplicationException
	 */
	public boolean isPositionAvailable(String containerName, int pos1, int pos2,
			HibernateDAO hibernateDao) throws ApplicationException
	{
		Capacity capacity =  getCapacityFromContainerName(containerName, hibernateDao);
		if(capacity.getOneDimensionCapacity()< pos1 ||capacity.getTwoDimensionCapacity() < pos2){
			throw new ApplicationException(null, null, "Please enter valid storage position.");
		}
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, containerName));
		params.put("1", new NamedQueryParam(DBTypes.LONG, pos1));
		params.put("2", new NamedQueryParam(DBTypes.LONG, pos2));

		Collection positionList = hibernateDao.executeNamedQuery("isPositionAvailable", params);
		if (positionList.isEmpty())
		{
			positionList = hibernateDao.executeNamedQuery("isPositionAvailableContainer", params);

		}
		return positionList.isEmpty();
	}


	private static List<String> extractLabellingSchemes(List labellingSchemesList)
	{
		List<String> labellingList = new ArrayList<String>();
		if (labellingSchemesList != null && !labellingSchemesList.isEmpty())
		{
			Object[] objArr = (Object[]) labellingSchemesList.get(0);
			labellingList.add(objArr[0].toString());
			labellingList.add(objArr[1].toString());
		}
		return labellingList;
	}

	public Long getContainerIdFromName(String containerName, HibernateDAO hibernateDao)
			throws ApplicationException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, containerName));
		List idList = hibernateDao.executeNamedQuery("getStorageContainerIdByName", substParams);
		if (idList.isEmpty())
		{
			throw new ApplicationException(null, null, String.format(
					INVALID_STORAGE_CONTAINER_NAME, containerName));
		}
		return (Long) idList.get(0);
	}

	public Capacity getCapacityFromContainerName(String containerName, HibernateDAO hibernateDao)
			throws ApplicationException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, containerName));
		List idList = hibernateDao.executeNamedQuery("getCapacityByName", substParams);
		if (idList.isEmpty())
		{
			throw new ApplicationException(null, null, INVALID_STORAGE_CONTAINER_NAME);
		}
		return (Capacity) idList.get(0);
	}


}
