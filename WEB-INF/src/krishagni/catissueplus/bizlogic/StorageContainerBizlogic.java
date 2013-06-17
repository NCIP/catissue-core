
package krishagni.catissueplus.bizlogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.StorageContainerViewDTO;
import krishagni.catissueplus.dto.StoragePositionDTO;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

public class StorageContainerBizlogic
{

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

}
