
package krishagni.catissueplus.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.Exception.CatissueException;
import krishagni.catissueplus.Exception.SpecimenErrorCodeEnum;
import krishagni.catissueplus.dto.AliquotContainerDetailsDTO;
import krishagni.catissueplus.dto.ContainerInputDetailsDTO;
import krishagni.catissueplus.dto.LabellingSchemeDTO;
import krishagni.catissueplus.dto.StorageContainerViewDTO;
import krishagni.catissueplus.dto.StoragePositionDTO;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
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

    /**
     * Gives container details DTO for given container name.
     * @param containerName
     * @param dao 
     * @return
     * @throws ApplicationException
     */
    public StorageContainerViewDTO getContainerDetailsForDataView(String containerName, HibernateDAO dao)
            throws ApplicationException
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
        else
        {
        	ErrorKey errorKey = ErrorKey.getErrorKey("invalid.container.name.message");
        	throw new BizLogicException(errorKey,null,"");
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
    public static StoragePositionDTO[][] getStoragePositionDetails(Long containerId, int dimX, int dimY,
            HibernateDAO dao) throws BizLogicException, DAOException
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

    public List<AliquotContainerDetailsDTO> getStorageContainerList(ContainerInputDetailsDTO containerInputDetails,
            final String storageType, HibernateDAO hibernateDao, int numOfContainers, List<AliquotContainerDetailsDTO> containers) throws ApplicationException
    {
//        List<AliquotContainerDetailsDTO> containers = new ArrayList<AliquotContainerDetailsDTO>();
        JDBCDAO jdbcDao = null;
        try
        {
            jdbcDao = AppUtility.openJDBCSession();

            final String[] queries = this.getStorageContainerForSpecimenQuery(containerInputDetails, "");

            for (int i = 0; i < queries.length; i++)
            {
                List<AliquotContainerDetailsDTO> resultList = getContainerListByQuery(storageType, jdbcDao, queries[i],
                        (containers.size() - numOfContainers));
                //dao.executeQuery(queries[i]);
                if (resultList == null || resultList.size() == 0)
                {
                    continue;
                }

                int cnt = 0;

                while (containers.size() < numOfContainers && resultList.size() > cnt)
                {
                    AliquotContainerDetailsDTO aliquotContainerDetailsDTO = resultList.get(cnt);
                    if (!containers.contains(resultList.get(cnt))
                            && aliquotContainerDetailsDTO.emptyPositionCount >= containerInputDetails.aliquotCount)
                    {
                        containers.add(aliquotContainerDetailsDTO);
                        //		remainingContainersNeeded--;
                    }
                    cnt++;
                }

                if (containers.size() == numOfContainers)
                {
                    break;
                }
            }
        }
        finally
        {
            AppUtility.closeJDBCSession(jdbcDao);
        }
        return containers;
    }

    private List<AliquotContainerDetailsDTO> getContainerListByQuery(String storageType, final JDBCDAO dao,
            final String query, int maxRecords) throws DAOException
    {
        List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = new ArrayList<AliquotContainerDetailsDTO>();
        final List resultList;
        /*if (!"Manual".equals(storageType))
        {
        	resultList = dao.executeQuery(query, 0, maxRecords, null);
        }
        else*/
        {
            resultList = dao.executeQuery(query);
        }

        for (int cnt = 0; cnt < resultList.size(); cnt++)
        {
            AliquotContainerDetailsDTO aliquotContainerDetailsDTO = new AliquotContainerDetailsDTO();
            List retList = (List) resultList.get(cnt);
            aliquotContainerDetailsDTO.containerId = Long.parseLong(retList.get(0).toString());
            aliquotContainerDetailsDTO.containerName = (String) retList.get(1);
            aliquotContainerDetailsDTO.dimension1 = Integer.parseInt(retList.get(2).toString());
            aliquotContainerDetailsDTO.dimension2 = Integer.parseInt(retList.get(3).toString());
            aliquotContainerDetailsDTO.emptyPositionCount = Long.parseLong(retList.get(4).toString());
            aliquotContainerDetailsDTOList.add(aliquotContainerDetailsDTO);

        }

        return aliquotContainerDetailsDTOList;
    }

    protected String[] getStorageContainerForSpecimenQuery(ContainerInputDetailsDTO containerInputDetails,
            String contName)
    {
        // Containers allowing Only this CP, Only this Specimen Class and only this Specimen Type
        final String query0 = this.createSCQuery(containerInputDetails, IS_CP_UNQ, IS_SPCLASS_UNQ, IS_SPTYPE_UNQ,
                contName);
        // Containers only this CP restriction and just this Specimen Class and any specimen type
        final String query1 = this.createSCQuery(containerInputDetails, IS_CP_UNQ, IS_SPCLASS_UNQ, IS_SPTYPE_NONUNQ,
                contName);
        // Containers allowing Only this CP but other Specimen Classes and SpecimenType also
        final String query2 = this.createSCQuery(containerInputDetails, IS_CP_UNQ, IS_SPCLASS_NONUNQ, IS_SPTYPE_NONUNQ,
                contName);
        // Containers allowing Other CPs also but just this Specimen Class and Specimen Type
        final String query3 = this.createSCQuery(containerInputDetails, IS_CP_NONUNQ, IS_SPCLASS_UNQ, IS_SPTYPE_UNQ,
                contName);
        // Containers allowing any CP just this Specimen Class and any specimen type
        final String query4 = this.createSCQuery(containerInputDetails, IS_CP_NONUNQ, IS_SPCLASS_UNQ, IS_SPTYPE_NONUNQ,
                contName);
        // Containers allowing Others CPs also, other Specimen Classes and Specimen Type too
        final String query5 = this.createSCQuery(containerInputDetails, IS_CP_NONUNQ, IS_SPCLASS_NONUNQ,
                IS_SPTYPE_NONUNQ, contName);
        // Containers no CP restriction and just this Specimen Class and Specimen Type
        final String query6 = this.getNoCPRestrictionQuery(containerInputDetails, null, IS_SPCLASS_UNQ, IS_SPTYPE_UNQ,
                contName);
        // Containers no CP restriction and just this Specimen Class and any specimen type
        final String query7 = this.getNoCPRestrictionQuery(containerInputDetails, null, IS_SPCLASS_UNQ,
                IS_SPTYPE_NONUNQ, contName);
        //Containers with no CP restrictions, other Specimen Classes and Specimen Type too
        final String query8 = this.getNoCPRestrictionQuery(containerInputDetails, null, IS_SPCLASS_NONUNQ,
                IS_SPTYPE_NONUNQ, contName);
        //Containers allowing any CP,any Specimen Class and any Specimen Type
        final String query9 = this.createSCQuery(containerInputDetails, null, null, null, contName);

        return new String[]{query0, query1, query2, query3, query4, query5, query6, query7, query8, query9};
    }

    private String getNoCPRestrictionQuery(ContainerInputDetailsDTO containerInputDetails, final Boolean isCPUnique,
            final Boolean isSPClassUnique, final Boolean isSPTypeUnique, String contName)
    {
        final long cpId = containerInputDetails.cpId;
        final String spClass = containerInputDetails.specimenClass;
        final int aliquotCount = containerInputDetails.aliquotCount;
        final String spType = containerInputDetails.specimenType;
        final Long userId = containerInputDetails.userId;
        final boolean isAdmin = containerInputDetails.isAdmin;
        final StringBuilder scQuery = new StringBuilder();
        String spClassCount = "=";
        String spTypeCount = "=";
        if (!isSPClassUnique)
        {
            spClassCount = ">";
        }
        if (!isSPTypeUnique)
        {
            spTypeCount = ">";
        }
        StringBuffer adminQuery = new StringBuffer();
        if (!isAdmin)
        {
            adminQuery.append(" AND C.SITE_ID IN (SELECT M.SITE_ID FROM  ");
            adminQuery.append(" CATISSUE_SITE_USERS M WHERE M.USER_ID = ");
            adminQuery.append(userId);
            adminQuery.append(" ) ");
        }
        StringBuffer contNameQuery = new StringBuffer();
        if (contName != null && !contName.equals(""))
        {
            contNameQuery.append(" AND ");
            contNameQuery.append(" d.NAME Like '%");
            contNameQuery.append(contName);
            contNameQuery.append("%' ");
        }
        scQuery.append("SELECT identifier,name,one_dimension_capacity,two_dimension_capacity,available_slots FROM "
                + "(SELECT  storage_container_id,count(*) AS lord FROM catissue_stor_cont_spec_type GROUP BY storage_container_id "
                + "HAVING count(*) "
                + spTypeCount
                + " 1) cscspt, "
                + "(SELECT storage_container_id, count(*) den FROM catissue_stor_cont_spec_class cscpc GROUP BY storage_container_id "
                + "HAVING count(*) "
                + spClassCount
                + " 1) cscpc, "
                + "(SELECT d.identifier, d.NAME, f.one_dimension_capacity, f.two_dimension_capacity, (f.one_dimension_capacity * f.two_dimension_capacity) capacity, count(*) cnt,"
                + "((f.one_dimension_capacity * f.two_dimension_capacity)-count(*)) available_slots FROM catissue_capacity f JOIN "
                + "catissue_container d ON f.identifier = d.capacity_id "
                + "LEFT OUTER JOIN catissue_specimen_position k ON d.identifier = k.container_id "
                + "JOIN catissue_storage_container c ON d.identifier = c.identifier "
                + "JOIN catissue_site l ON c.site_id = l.identifier "
                + "JOIN catissue_stor_cont_spec_class b ON b.storage_container_id = c.identifier "
                + "JOIN catissue_stor_cont_spec_type spt ON spt.storage_container_id = c.identifier WHERE d.identifier NOT IN "
                + "(SELECT t2.storage_container_id FROM catissue_st_cont_coll_prot_rel t2) "
                + "AND b.specimen_class = '"
                + spClass
                + "'"
                + "AND spt.specimen_type = '"
                + spType
                + "'"
                + adminQuery.toString()
                + "AND l.activity_status = 'Active' "
                + "AND d.activity_status = 'Active' AND d.cont_full = 0 "
                + contNameQuery.toString()
                + "GROUP BY d.identifier, d.NAME, f.one_dimension_capacity,"
                + "f.two_dimension_capacity,(f.one_dimension_capacity * f.two_dimension_capacity)) view1 "
                + "WHERE  cscspt.storage_container_id = view1.identifier AND cscpc.storage_container_id = view1.identifier "
                + "and available_slots > 0 order by identifier");

        return scQuery.toString();

    }

    private String createSCQuery(ContainerInputDetailsDTO containerInputDetails, final Boolean isCPUnique,
            final Boolean isSPClassUnique, final Boolean isSPTypeUnique, String contName)
    {
        final long cpId = containerInputDetails.cpId;
        final String spClass = containerInputDetails.specimenClass;
        final int aliquotCount = containerInputDetails.aliquotCount;
        final String spType = containerInputDetails.specimenType;
        final Long userId = containerInputDetails.userId;
        final boolean isAdmin = containerInputDetails.isAdmin;
        final StringBuilder scQuery = new StringBuilder();
        scQuery.append("SELECT VIEW1.IDENTIFIER,VIEW1.NAME,VIEW1.ONE_DIMENSION_CAPACITY,VIEW1.TWO_DIMENSION_CAPACITY,VIEW1.CAPACITY-COUNT(*)  AVAILABLE_SLOTS ");
        scQuery.append(" FROM"
                + " (SELECT D.IDENTIFIER,D.NAME,F.ONE_DIMENSION_CAPACITY, F.TWO_DIMENSION_CAPACITY,(F.ONE_DIMENSION_CAPACITY * F.TWO_DIMENSION_CAPACITY)  CAPACITY");
        scQuery.append(" FROM CATISSUE_CAPACITY F JOIN CATISSUE_CONTAINER D  ON F.IDENTIFIER = D.CAPACITY_ID");
        scQuery.append(" LEFT OUTER JOIN CATISSUE_SPECIMEN_POSITION K ON D.IDENTIFIER = K.CONTAINER_ID ");
        scQuery.append(" LEFT OUTER JOIN CATISSUE_STORAGE_CONTAINER C ON D.IDENTIFIER = C.IDENTIFIER ");
        scQuery.append(" LEFT OUTER JOIN CATISSUE_SITE L ON C.SITE_ID = L.IDENTIFIER ");
        if (isCPUnique != null) //DO not join on CP if there is no restriction on CP. i.e isCPUnique=null 
        {
            scQuery.append(" LEFT OUTER JOIN CATISSUE_ST_CONT_COLL_PROT_REL A ON A.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
        }
        if (isSPClassUnique != null) //DO not join on SP CLS if there is no restriction on SP CLS. i.e isSPClassUnique=null
        {
            scQuery.append(" LEFT OUTER JOIN CATISSUE_STOR_CONT_SPEC_CLASS B ON B.STORAGE_CONTAINER_ID = C.IDENTIFIER ");
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
        if (contName != null && !contName.equals(""))
        {
            scQuery.append(" AND ");
            scQuery.append(" D.NAME Like '%");
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
        return scQuery.toString();
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

    public void setAvailablePositionsForContainer(AliquotContainerDetailsDTO aliquotContainerDetailsDTO,
            String startPosX, String startPosY, int emptyPosCount, HibernateDAO hibernateDao, HashSet<String> allocatedPositions)
            throws ApplicationException
    {
        int dimX = aliquotContainerDetailsDTO.dimension1 + 1;
        int dimY = aliquotContainerDetailsDTO.dimension2 + 1;
        final boolean[][] positions = new boolean[dimX][dimY];
        try
        {

            for (int i = 1; i < dimX; i++)
            {
                for (int j = 1; j < dimY; j++)
                {
                    positions[i][j] = true;
                }
            }

            Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
            substParams.put("0", new NamedQueryParam(DBTypes.STRING, aliquotContainerDetailsDTO.containerName));
            List allocatedPositionList = hibernateDao.executeNamedQuery("getAllocatedSpecimenPosition", substParams);
            setPositions(positions, allocatedPositionList);
            allocatedPositionList = hibernateDao.executeNamedQuery("getAllocatedContainerPosition", substParams);
            setPositions(positions, allocatedPositionList);
            LabellingSchemeDTO labellingSchemeDTO = getLabellingSchemeByContainerName(
                    aliquotContainerDetailsDTO.containerName, hibernateDao);
            int startPosxNum = Validator.isEmpty(startPosX) ? 0 : AppUtility.getPositionValueInInteger(
                    labellingSchemeDTO.getDimensionOne(), startPosX);
            int startPosyNum = Validator.isEmpty(startPosY) ? 0 : AppUtility.getPositionValueInInteger(
                    labellingSchemeDTO.getDimensionOne(), startPosY);
            for (int i = startPosxNum; i < dimX; i++)
            {
                for (int j = startPosyNum; j < dimY; j++)
                {
                    if (positions[i][j] )
                    {
                    	String pos1 = AppUtility.getPositionValue(labellingSchemeDTO.getDimensionOne(), i);
                    	String pos2 = AppUtility.getPositionValue(labellingSchemeDTO.getDimensionTwo(), j);
                    	String contValue = getStorageValueKey(aliquotContainerDetailsDTO.containerName, null, pos1, pos2, hibernateDao);
                    	if(allocatedPositions != null && !allocatedPositions.contains(contValue))
                    	{
	                        aliquotContainerDetailsDTO.position1.add(pos1);
	                        aliquotContainerDetailsDTO.position2.add(pos2);
                    	}
                    	else if(allocatedPositions == null)
                    	{
                    		aliquotContainerDetailsDTO.position1.add(pos1);
	                        aliquotContainerDetailsDTO.position2.add(pos2);
                    	}
                        if (aliquotContainerDetailsDTO.position2.size() == emptyPosCount)
                        {
                            return;
                        }
                    }
                }
                startPosyNum=0;
            }

        
	        if (aliquotContainerDetailsDTO.position2.size() != emptyPosCount)
	        {
	        	for (int i = 0; i < dimX; i++)
	            {
	                for (int j = 0; j < dimY; j++)
	                {
	                    if (positions[i][j] )
	                    {
	                    	String pos1 = AppUtility.getPositionValue(labellingSchemeDTO.getDimensionOne(), i);
	                    	String pos2 = AppUtility.getPositionValue(labellingSchemeDTO.getDimensionTwo(), j);
	                    	String contValue = getStorageValueKey(aliquotContainerDetailsDTO.containerName, null, pos1, pos2, hibernateDao);
	                    	if(allocatedPositions != null && !allocatedPositions.contains(contValue))
	                    	{
		                        aliquotContainerDetailsDTO.position1.add(pos1);
		                        aliquotContainerDetailsDTO.position2.add(pos2);
	                    	}
	                    	else if(allocatedPositions == null)
	                    	{
	                    		aliquotContainerDetailsDTO.position1.add(pos1);
		                        aliquotContainerDetailsDTO.position2.add(pos2);
	                    	}
	                        if (aliquotContainerDetailsDTO.position2.size() == emptyPosCount)
	                        {
	                            return;
	                        }
	                    }
	                }
	            }
	        }
        }
        catch (final DAOException daoEx)
        {
            throw new BizLogicException(daoEx);
        }
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

    public SpecimenPosition getPositionIfAvailableFromContainer(String containerName, String pos1, String pos2,
    		String prevPos1, String prevPos2, HibernateDAO hibernateDao, HashSet<String> allocatedPositions) throws ApplicationException
    {
        SpecimenPosition specimenPosition;
        Long containerId = getContainerIdFromName(containerName, hibernateDao);

        LabellingSchemeDTO labellingSchemeDTO = getLabellingSchemeByContainerName(containerName, hibernateDao);
        if (Validator.isEmpty(pos1) || Validator.isEmpty(pos2))
        {
            specimenPosition = getFirstAvailablePositionInContainer(containerName,prevPos1,prevPos2, labellingSchemeDTO, hibernateDao,allocatedPositions);
        }
        else
        {
            int pos1Integer = AppUtility.getPositionValueInInteger(labellingSchemeDTO.getDimensionOne(), pos1);
            int pos2Integer = AppUtility.getPositionValueInInteger(labellingSchemeDTO.getDimensionTwo(), pos2);
            String contValue = getStorageValueKey(containerName, null, pos1, pos2, hibernateDao);
            if((allocatedPositions != null && allocatedPositions.contains(contValue)) || !isPositionAvailable(containerName, pos1Integer, pos2Integer, hibernateDao))
            {
                throw new CatissueException(String.format(
                        SpecimenErrorCodeEnum.POSITION_NOT_AVAILABLE.getDescription(), containerName, pos1, pos2),
                        SpecimenErrorCodeEnum.POSITION_NOT_AVAILABLE.getCode());

                //				throw new ApplicationException(null, null, String.format(
                //						Constants.CONTAINER_ERROR_MSG_FOR_ALIQUOT,
                //						containerName, pos1, pos2));
            }

            specimenPosition = new SpecimenPosition();
            specimenPosition.setPositionDimensionOneString(pos1);
            specimenPosition.setPositionDimensionTwoString(pos2);
            specimenPosition.setPositionDimensionOne(pos1Integer);
            specimenPosition.setPositionDimensionTwo(pos2Integer);

        }
        if(specimenPosition==null){
            throw new CatissueException(String.format(
                    SpecimenErrorCodeEnum.POSITION_ALREADY_OCCUPIED.getDescription(), containerName, pos1, pos2),
                    SpecimenErrorCodeEnum.POSITION_ALREADY_OCCUPIED.getCode());
        }
        StorageContainer sContainer = new StorageContainer();
        sContainer.setId(containerId);
        sContainer.setName(containerName);
        specimenPosition.setStorageContainer(sContainer);

        return specimenPosition;
    }

    /**
     * Checking specimen.
     * @param containerName container name
     * @param pos1 position dim 1
     * @param pos2 position dim 2
     * @return Boolean
     * @throws ApplicationException
     */
    public boolean isPositionAvailable(String containerName, int pos1, int pos2, HibernateDAO hibernateDao)
            throws ApplicationException
    {
        Capacity capacity = getCapacityFromContainerName(containerName, hibernateDao);
        if (capacity.getOneDimensionCapacity() < pos1 || capacity.getTwoDimensionCapacity() < pos2 || pos1 == 0
                || pos1 == -1 || pos2 == 0 || pos2 == -1)
        {
            throw new CatissueException(String.format(SpecimenErrorCodeEnum.INVALID_STORAGE_POSITION.getDescription(),
                    containerName, pos1, pos2), SpecimenErrorCodeEnum.INVALID_STORAGE_POSITION.getCode());
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

    public static LabellingSchemeDTO getLabellingSchemeByContainerName(String containerName, HibernateDAO hibernateDao)
            throws ApplicationException
    {
        Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
        substParams.put("0", new NamedQueryParam(DBTypes.STRING, containerName));
        LabellingSchemeDTO labellingSchemeDTO = new LabellingSchemeDTO();
        List labellingSchemeList = hibernateDao.executeNamedQuery("getStorageContainerLabellingSchemesByName",
                substParams);
        if (!labellingSchemeList.isEmpty())
        {
            Object[] objArr = (Object[]) labellingSchemeList.get(0);
            labellingSchemeDTO.setDimensionOne(objArr[0].toString());
            labellingSchemeDTO.setDimensionTwo(objArr[1].toString());
        }
        return labellingSchemeDTO;
    }
    
    public static LabellingSchemeDTO getLabellingSchemeByContainerId(String containerId,HibernateDAO hibernateDao) throws DAOException
	{
		// Create a map of substitution parameters.
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.STRING, containerId));
		LabellingSchemeDTO labellingSchemeDTO = new LabellingSchemeDTO();
			List labellingSchemeList=hibernateDao.executeNamedQuery("getStorageContainerLabellingSchemesById", substParams);
		if (!labellingSchemeList.isEmpty())
        {
            Object[] objArr = (Object[]) labellingSchemeList.get(0);
            labellingSchemeDTO.setDimensionOne(objArr[0].toString());
            labellingSchemeDTO.setDimensionTwo(objArr[1].toString());
        }
        return labellingSchemeDTO;
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

    public Long getContainerIdFromName(String containerName, HibernateDAO hibernateDao) throws ApplicationException
    {
        Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
        substParams.put("0", new NamedQueryParam(DBTypes.STRING, containerName));
        List idList = hibernateDao.executeNamedQuery("getStorageContainerIdByName", substParams);
        if (idList.isEmpty())
        {
            //            throw new ApplicationException(null, null, String.format(
            //                    Constants.INVALID_STORAGE_CONTAINER_NAME, containerName));
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_STORAGE_CONTAINER_NAME.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_STORAGE_CONTAINER_NAME.getCode());
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
            throw new CatissueException(SpecimenErrorCodeEnum.INVALID_STORAGE_CONTAINER_NAME.getDescription(),
                    SpecimenErrorCodeEnum.INVALID_STORAGE_CONTAINER_NAME.getCode());
        }
        return (Capacity) idList.get(0);
    }

    /**
     * This function returns the first available position in a container which can be allocated.
     * If container is full, returns null
     * @param prevPos2 
     * @param prevPos1 
     * @param allocatedPositions 
     * @param container : Container for which available position is to be searched
     * @return Position
     * @throws ApplicationException 
     */
    public SpecimenPosition getFirstAvailablePositionInContainer(String containerName,
    		String prevPos1, String prevPos2, LabellingSchemeDTO labellingSchemeDTO, HibernateDAO hibernateDao, HashSet<String> allocatedPositions) throws ApplicationException
    {
        SpecimenPosition position = null;
        try
        {
            Integer xPos;
            Integer yPos;
            final Capacity scCapacity = getCapacityFromContainerName(containerName, hibernateDao);
            AliquotContainerDetailsDTO aliquotContainerDetailsDTO = new AliquotContainerDetailsDTO();
            aliquotContainerDetailsDTO.containerName = containerName;
            aliquotContainerDetailsDTO.dimension1 = scCapacity.getOneDimensionCapacity();
            aliquotContainerDetailsDTO.dimension2 = scCapacity.getTwoDimensionCapacity();
            aliquotContainerDetailsDTO.emptyPositionCount = 1l;
            setAvailablePositionsForContainer(aliquotContainerDetailsDTO, prevPos1, prevPos2, 1, hibernateDao,allocatedPositions);

            if (!aliquotContainerDetailsDTO.position1.isEmpty())
            {
                String pos1 = aliquotContainerDetailsDTO.position1.get(0);
                String pos2 = aliquotContainerDetailsDTO.position2.get(0);
                position = new SpecimenPosition();
                position.setPositionDimensionOneString(pos1);
                position.setPositionDimensionTwoString(pos2);
                position.setPositionDimensionOne(AppUtility.getPositionValueInInteger(
                        labellingSchemeDTO.getDimensionOne(), pos1));
                position.setPositionDimensionTwo(AppUtility.getPositionValueInInteger(
                        labellingSchemeDTO.getDimensionTwo(), pos2));

            }

        }
        catch (final DAOException daoEx)
        {
            throw new BizLogicException(daoEx);
        }
        return position;
    }

    public static String getStorageValueKey(String containerName, String containerID,
			String containerPos1, String containerPos2,HibernateDAO hibernateDao) throws ApplicationException
	{
		final StringBuffer storageValue = new StringBuffer();
		if (containerName != null)
		{
			storageValue.append(containerName);
			storageValue.append(':');
			storageValue.append(containerPos1);//convertSpecimenPositionsToString(containerName, 1, containerPos1,hibernateDao));
			storageValue.append(" ,");
			storageValue.append(containerPos2);//convertSpecimenPositionsToString(containerName, 2, containerPos2,hibernateDao));
//			storageValue.append(':');
//			storageValue.append(containerPos1);
//			storageValue.append(" ,");
//			storageValue.append(containerPos2);
		}
		else if (containerID != null)
		{
			storageValue.append(containerID);
			storageValue.append(':');
//			storageValue.append(containerPos1);//convertPositionsToIntegerUsingContId(containerID, 1, containerPos1.toString(),hibernateDao));
//			storageValue.append(" ,");
//			storageValue.append(convertPositionsToIntegerUsingContId(containerID, 2, containerPos2.toString(),hibernateDao));
//			storageValue.append(':');
			storageValue.append(containerPos1);
			storageValue.append(" ,");
			storageValue.append(containerPos2);
		}
		return storageValue.toString();
	}
    

}
