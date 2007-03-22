/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.wustl.catissuecore.bizlogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathConstants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;

public class AnnotationUtil
{

    /**
     * @param staticEntityId
     * @param dynamicEntityId
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public static Long addAssociation(Long staticEntityId, Long dynamicEntityId)
            throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        EntityManagerInterface entityManager = EntityManager.getInstance();
        EntityInterface staticEntity = entityManager
                .getEntityByIdentifier(staticEntityId);
        EntityInterface dynamicEntity = (entityManager
                .getContainerByIdentifier(staticEntityId.toString()))
                .getEntity();
        dynamicEntity.addEntityGroupInterface((EntityGroupInterface) staticEntity.getEntityGroupCollection().iterator().next());
        String roleName = staticEntityId.toString().concat("_").concat(
                dynamicEntityId.toString());
        RoleInterface sourceRole = getRole(AssociationType.ASSOCIATION,
                roleName, Cardinality.ZERO, Cardinality.ONE);
        RoleInterface targetRole = getRole(AssociationType.ASSOCIATION,
                roleName, Cardinality.ZERO, Cardinality.MANY);
        AssociationInterface association = getAssociation(dynamicEntity,
                AssociationDirection.SRC_DESTINATION, roleName, sourceRole,
                targetRole);
        staticEntity.addAssociation(association);
        EntityManager.getInstance().persistEntity(staticEntity);
        staticEntity = EntityManager.getInstance().getEntityByIdentifier(
                staticEntity.getId());
        association = (AssociationInterface) EntityManager.getInstance()
                .getAssociation(staticEntity.getName(), roleName);
        association = (AssociationInterface) EntityManager.getInstance()
                .getAssociation(staticEntity.getName(), roleName);

        return association.getId();
    }

    /**
     * @param targetEntity
     * @param associationDirection
     * @param assoName
     * @param sourceRole
     * @param targetRole
     * @return
     */
    public static AssociationInterface getAssociation(
            EntityInterface targetEntity,
            AssociationDirection associationDirection, String assoName,
            RoleInterface sourceRole, RoleInterface targetRole)
    {
        AssociationInterface association = DomainObjectFactory.getInstance()
                .createAssociation();
        association.setTargetEntity(targetEntity);
        association.setAssociationDirection(associationDirection);
        association.setName(assoName);
        association.setSourceRole(sourceRole);
        association.setTargetRole(targetRole);
        return association;
    }

    /**
     * @param associationType associationType
     * @param name name
     * @param minCard  minCard
     * @param maxCard maxCard
     * @return  RoleInterface
     */
    public static RoleInterface getRole(AssociationType associationType,
            String name, Cardinality minCard, Cardinality maxCard)
    {
        RoleInterface role = DomainObjectFactory.getInstance().createRole();
        role.setAssociationsType(associationType);
        role.setName(name);
        role.setMinimumCardinality(minCard);
        role.setMaximumCardinality(maxCard);
        return role;
    }

    public static void addPathsForQuery(Long staticEntityId,
            Long dynamicEntityId, Long deAssociationID)
    {
        Long maxPathId = getMaxId("path_id", "path");
        maxPathId += 1;
        insertNewPaths(maxPathId, staticEntityId, dynamicEntityId,
                deAssociationID);
    }

    /**
     * @param maxPathId
     * @param staticEntityId
     * @param dynamicEntityId
     * @param deAssociationID
     */
    private static void insertNewPaths(Long maxPathId, Long staticEntityId,
            Long dynamicEntityId, Long deAssociationID)
    {
        StringBuffer query = new StringBuffer();
        Long intraModelAssociationId = getMaxId("ASSOCIATION_ID",
                "INTRA_MODEL_ASSOCIATION");
        intraModelAssociationId += 1;
        Session session;
        try
        {
            session = DBUtil.currentSession();
            Connection conn = session.connection();

            String associationQuery = "insert into ASSOCIATION (ASSOCIATION_ID, ASSOCIATION_TYPE) values ("
                    + intraModelAssociationId
                    + ","
                    + PathConstants.INTRA_MODEL_ASSOCIATION_TYPE + ")";
            String intraModelQuery = "insert into INTRA_MODEL_ASSOCIATION (ASSOCIATION_ID, DE_ASSOCIATION_ID) values ("
                    + intraModelAssociationId + "," + deAssociationID + ")";
            String directPathQuery = "insert into PATH (PATH_ID, FIRST_ENTITY_ID,INTERMEDIATE_PATH, LAST_ENTITY_ID) values ("
                    + maxPathId
                    + ","
                    + staticEntityId
                    + ","
                    + deAssociationID.toString() + "," + dynamicEntityId + ")";

            List<String> list = new ArrayList<String>();
            list.add(associationQuery);
            list.add(intraModelQuery);
            list.add(directPathQuery);

            executeQuery(conn, list);
            maxPathId += 1;
            addIndirectPaths(maxPathId, staticEntityId, dynamicEntityId,
                    intraModelAssociationId, conn);
            EntityCache.getInstance().refreshCache();
            //TODO PathFinder.getInstance().
            conn.commit();

        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param maxPathId
     * @param staticEntityId
     * @param dynamicEntityId
     * @param intraModelAssociationId
     * @param conn
     * @throws SQLException
     */
    private static void addIndirectPaths(Long maxPathId, Long staticEntityId,
            Long dynamicEntityId, Long intraModelAssociationId, Connection conn)
            throws SQLException
    {
        ResultSet resultSet = getIndirectPaths(conn, staticEntityId);
        String query = "insert into path (PATH_ID, FIRST_ENTITY_ID,INTERMEDIATE_PATH, LAST_ENTITY_ID) values (?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(query);
        while (resultSet.next())
        {

            Long firstEntityId = resultSet.getLong(1);
            String path = resultSet.getString(2);
            path = path.concat("_").concat(intraModelAssociationId.toString());

            statement.setLong(1, maxPathId);
            maxPathId++;
            statement.setLong(2, firstEntityId);
            statement.setString(3, path);
            statement.setLong(4, dynamicEntityId);
            statement.execute();
            statement.clearParameters();
        }
        resultSet.close();
        statement.close();

    }

    /**
     * @param conn
     * @param staticEntityId
     * @return
     * @throws SQLException
     */
    private static ResultSet getIndirectPaths(Connection conn,
            Long staticEntityId) throws SQLException
    {
        String query = "select FIRST_ENTITY_ID,INTERMEDIATE_PATH from path where LAST_ENTITY_ID="
                + staticEntityId;
        java.sql.PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    /**
     * @param conn
     * @param queryList
     * @throws SQLException
     */
    private static void executeQuery(Connection conn, List<String> queryList)
            throws SQLException
    {
        for (String query : queryList)
        {
            java.sql.PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
        }

    }

    /**
     * @param columnName
     * @param tableName
     * @return
     */
    private static Long getMaxId(String columnName, String tableName)
    {
        String query = "select max(" + columnName + ") from " + tableName;
        HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance()
                .getDAO(Constants.HIBERNATE_DAO);
        try
        {
            Session session = DBUtil.currentSession();
            Connection conn = session.connection();
            java.sql.PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long maxId = resultSet.getLong(0);
            return maxId;
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
