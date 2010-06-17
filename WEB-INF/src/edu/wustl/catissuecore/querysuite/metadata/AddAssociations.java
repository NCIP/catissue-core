
package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * AddAssociations.
 *
 */
public class AddAssociations
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger LOGGER = Logger.getCommonLogger(AddAssociations.class);

	/**
	 * Connection instance.
	 */
	private Connection connection = null;

	/**
	 * SQL for dyextn_constraint_properties table
	 */
	private static final String INSERT_CONST_PROP = "insert into dyextn_constraint_properties "
			+ "(IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values(";
	/**
	 * SQL for DYEXTN_COLUMN_PROPERTIES table
	 */
	private static final String INS_COL_PROP = "insert into DYEXTN_COLUMN_PROPERTIES "
			+ "(IDENTIFIER,CNSTR_KEY_PROP_ID) values(";
	/**
	 * SQL for dyextn_database_properties table
	 */
	private static final String INSERT_DB_PROP = "insert into dyextn_database_properties values (";
	/**
	 * SQL for dyextn_role table
	 */
	private static final String INSERT_DYEXTN_ROLE = "insert into dyextn_role"
			+ "(IDENTIFIER,ASSOCIATION_TYPE,MAX_CARDINALITY,MIN_CARDINALITY,NAME) values (";
	/**
	 * SQL for dyextn_association table
	 */
	private static final String INS_ASSOC = "insert into dyextn_association values (";

	/**
	 * Constructor.
	 * @param connection Connection object.
	 */
	AddAssociations(final Connection connection)
	{
		this.connection = connection;
	}

	/**
	 * This method adds Association.
	 * @param entityName entity Name
	 * @param associatedEntityName associated Entity Name
	 * @param associationName association Name
	 * @param associationType association Type
	 * @param roleName role Name
	 * @param isSwap is Swap
	 * @param roleNameTable role Name Table
	 * @param srcAssociationId source Association Id
	 * @param targetAssociationId target Association Id
	 * @param maxCardinality max Cardinality
	 * @param isSystemGenerated is System Generated
	 * @param direction direction
	 * @param mmflag
	 * @param entityGrpName
	 * @throws SQLException SQL Exception
	 * @throws IOException IO Exception
	 */
	public void addAssociation(final String entityName, final String associatedEntityName,
			final String associationName, final String associationType, String roleName, boolean isSwap,
			final String roleNameTable, String srcAssociationId, String targetAssociationId,
			int maxCardinality, int isSystemGenerated, String direction, boolean mmflag,
			String... entityGrpName) throws SQLException, IOException
	{
		final Long nextIdOfAbstractMetadata = getNextId("dyextn_abstract_metadata", "identifier");
		Long nextIdOfDERole;
		if(!isSwap && srcAssociationId == null)
		{
			nextIdOfDERole = getRoleId("dyextn_role", "identifier");
		}
		else
		{
			nextIdOfDERole = getNextId("dyextn_role", "identifier");
		}
		final Long nextIdOfDBProperties = getNextId("dyextn_database_properties", "identifier");
		final Long nextIDintraModelAssociation = getNextId("intra_model_association", "ASSOCIATION_ID");
		final Long nextIdPath = getNextId("path", "PATH_ID");

		String sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		final Long entityId = getIdentifier(sql);
		if (entityId == 0)
		{
			LOGGER.info("Entity not found of name ");
		}

		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ associatedEntityName + "'";
		Long associatedEntityId = getIdentifier(sql);
		if (associatedEntityId == 0)
		{
			LOGGER.info("Entity not found of name ");
		}

		insertAssociationData(associationName, nextIdOfAbstractMetadata, entityId);

		Long roleId = nextIdOfDERole;
		if (isSwap)
		{
			roleId = nextIdOfDERole + 1;
			sql = INSERT_DYEXTN_ROLE + nextIdOfDERole + ",'" + associationType + "',"
					+ maxCardinality + ",0,'" + roleName + "')";
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql, "dyextn_role");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

			if (isSystemGenerated == 0)
			{
				sql = INSERT_DYEXTN_ROLE + roleId + ",'ASSOCIATION',2,0,'" + roleNameTable + "')";
			}
			else
			{
				sql = INSERT_DYEXTN_ROLE + roleId + ",'ASSOCIATION',1,0,'" + roleNameTable + "')";
			}
			if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
			{
				sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql, "dyextn_role");
			}
			UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		}

		if (isSwap)
		{
			if (isSystemGenerated == 0)
			{
				sql = INS_ASSOC + nextIdOfAbstractMetadata + ",'" + direction + "',"
						+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",0,0)";
			}
			else
			{
				sql = INS_ASSOC + nextIdOfAbstractMetadata + ",'" + direction + "',"
						+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",1,0)";
			}
		}
		else
		{
			final Long lastIdOfDERole = nextIdOfDERole - 2;
			final Long idOfDERole = nextIdOfDERole - 1;

				sql = INS_ASSOC + nextIdOfAbstractMetadata + ",'" + direction + "',"
						+ associatedEntityId + "," + lastIdOfDERole + "," + idOfDERole + ","
						+ isSystemGenerated + ",0)";
		}
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		sql = INSERT_DB_PROP + nextIdOfDBProperties + ",'" + associationName + "')";
		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
		{
			sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
					"dyextn_database_properties");
		}
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

		final String constraintPropertiesSql = getSQLForConstraintProperties(isSwap, srcAssociationId,
				targetAssociationId, nextIdOfAbstractMetadata, nextIdOfDBProperties);
		UpdateMetadataUtil.executeInsertSQL(constraintPropertiesSql, this.connection.createStatement());

		//--changes due to constraint properties : Deepali---------------
		final String attidsql = "select attribute_id from dyextn_entiy_composite_key_rel "
				+ "where entity_id ='" + entityId + "'";

		final Long attrid = getIdentifier(attidsql);

		///for associated attribute id
		final String assoattidsql = "select attribute_id from dyextn_entiy_composite_key_rel "
				+ "where entity_id ='" + associatedEntityId + "'";
		Long assoattid = getIdentifier(assoattidsql);

		String constraintidSQL = "select identifier from dyextn_constraint_properties where association_id ='"
				+ nextIdOfAbstractMetadata + "'";
		Long constraintid = getIdentifier(constraintidSQL);

		long nextIddeconskeypro = getNextId("DYEXTN_CONSTRAINTKEY_PROP", "identifier");
		String sqldeconskeyprop = null;
		String sqlassconskeyprop = null;
		long consKeyId1 = nextIddeconskeypro;
		long consKeyId2 = nextIddeconskeypro;
		if (isSwap)
		{
			if (mmflag)
			{
				sqlassconskeyprop = "insert into DYEXTN_CONSTRAINTKEY_PROP" +
						"(IDENTIFIER , PRIMARY_ATTRIBUTE_ID ,SRC_CONSTRAINT_KEY_ID)"
						+ " values ("
						+ nextIddeconskeypro
						+ ","
						+ attrid
						+ ","
						+ constraintid
						+ ")";
				consKeyId1 = nextIddeconskeypro;
				nextIddeconskeypro = nextIddeconskeypro + 1;
			}
			sqldeconskeyprop = "insert into DYEXTN_CONSTRAINTKEY_PROP(IDENTIFIER,"
					+ "PRIMARY_ATTRIBUTE_ID,TGT_CONSTRAINT_KEY_ID) values ("
					+ nextIddeconskeypro + "," + assoattid + "," + constraintid + ")";
		}
		else
		{
			if(srcAssociationId == null)
			{
				sqldeconskeyprop = "insert into DYEXTN_CONSTRAINTKEY_PROP(IDENTIFIER,"
					+ "PRIMARY_ATTRIBUTE_ID,TGT_CONSTRAINT_KEY_ID) values ("
					+ nextIddeconskeypro + "," + assoattid + "," + constraintid + ")";
			}
			else
			{
				sqldeconskeyprop = "insert into DYEXTN_CONSTRAINTKEY_PROP(IDENTIFIER,"
						+ "PRIMARY_ATTRIBUTE_ID,SRC_CONSTRAINT_KEY_ID) values ("
						+ nextIddeconskeypro + "," + attrid + "," + constraintid + ")";
			}

			if (mmflag)
			{
				nextIddeconskeypro = nextIddeconskeypro + 1; //getNextId("DYEXTN_CONSTRAINTKEY_PROP", "identifier", jdbcdao);

				sqlassconskeyprop = "insert into DYEXTN_CONSTRAINTKEY_PROP" +
						"(IDENTIFIER , PRIMARY_ATTRIBUTE_ID ,TGT_CONSTRAINT_KEY_ID)"
						+ " values ("
						+ nextIddeconskeypro
						+ ","
						+ assoattid
						+ ","
						+ constraintid
						+ ")";
				consKeyId2 = nextIddeconskeypro;
			}
		}
		UpdateMetadataUtil.executeInsertSQL(sqldeconskeyprop, this.connection.createStatement());
		if (mmflag)
		{
			UpdateMetadataUtil.executeInsertSQL(sqlassconskeyprop, this.connection.createStatement());
		}

		long nextIdDBPropforCol = getNextId("dyextn_database_properties", "identifier");
		String sqlDBPropforCol = null;
		if (mmflag)
		{
			if (isSwap)
			{
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + targetAssociationId
						+ "')";
				UpdateMetadataUtil.executeInsertSQL(sqlDBPropforCol, this.connection
						.createStatement());
				String sqlColPropforConsKey = INS_COL_PROP + nextIdDBPropforCol + ","
						+ consKeyId1 + ")";

				UpdateMetadataUtil.executeInsertSQL(sqlColPropforConsKey, this.connection
						.createStatement());
				nextIdDBPropforCol = nextIdDBPropforCol + 1;
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + srcAssociationId
						+ "')";
				UpdateMetadataUtil.executeInsertSQL(sqlDBPropforCol, this.connection
						.createStatement());
				String sqlCOlPropForConsKey = INS_COL_PROP + nextIdDBPropforCol + ","
						+ consKeyId2 + ")";

				UpdateMetadataUtil.executeInsertSQL(sqlCOlPropForConsKey, this.connection
						.createStatement());
			}
			else
			{
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + srcAssociationId
						+ "')";

				UpdateMetadataUtil.executeInsertSQL(sqlDBPropforCol, this.connection
						.createStatement());
				String sqlColPropforConsKey = INS_COL_PROP + nextIdDBPropforCol + ","
						+ consKeyId1 + ")";

				UpdateMetadataUtil.executeInsertSQL(sqlColPropforConsKey, this.connection
						.createStatement());
				nextIdDBPropforCol = nextIdDBPropforCol + 1;
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + targetAssociationId
						+ "')";
				UpdateMetadataUtil.executeInsertSQL(sqlDBPropforCol, this.connection
						.createStatement());

				String sqlCOlPropForConsKey = INS_COL_PROP + nextIdDBPropforCol + ","
						+ consKeyId2 + ")";
				UpdateMetadataUtil.executeInsertSQL(sqlCOlPropForConsKey, this.connection
						.createStatement());
			}
		}
		else
		{
			if(!isSwap && srcAssociationId == null)
			{
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + targetAssociationId + "')";
			}
			else
			{
				sqlDBPropforCol = INSERT_DB_PROP + nextIdDBPropforCol + ",'" + srcAssociationId + "')";
			}
			UpdateMetadataUtil.executeInsertSQL(sqlDBPropforCol, this.connection.createStatement());
			String sqlColPropforConsKey = INS_COL_PROP + nextIdDBPropforCol + ","
					+ consKeyId1 + ")";

			UpdateMetadataUtil.executeInsertSQL(sqlColPropforConsKey, this.connection.createStatement());
		}

		//-------------------------------
		sql = "insert into association values(" + nextIDintraModelAssociation + ",2)";
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());

		sql = "insert into intra_model_association values(" + nextIDintraModelAssociation + ","
				+ nextIdOfAbstractMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
		sql = "insert into path values (" + nextIdPath + "," + entityId + ","
				+ nextIDintraModelAssociation + "," + associatedEntityId + ")";

		UpdateMetadataUtil.executeInsertSQL(sql, this.connection.createStatement());
	}

	/**
	 *
	 * @param associationName
	 * @param nextIdMetadata
	 * @param entityId
	 * @throws SQLException
	 */
	private void insertAssociationData(String associationName, Long nextIdMetadata, Long entityId)
			throws SQLException
	{
		final Statement stmt = this.connection.createStatement();
		String sql = "insert into dyextn_abstract_metadata values (" + nextIdMetadata
				+ ",null,null,null,'" + associationName + "',null)";

		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(UpdateMetadata.DATABASE_TYPE))
		{
			sql = UpdateMetadataUtil.getIndentityInsertStmtForMsSqlServer(sql,
					"dyextn_abstract_metadata");
		}

		stmt.executeUpdate(sql);

		sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE values(" + nextIdMetadata + ")";
		stmt.executeUpdate(sql);

		sql = "insert into dyextn_attribute values (" + nextIdMetadata + "," + entityId + ")";
		stmt.executeUpdate(sql);

		stmt.close();

	}

	/**
	 *
	 * @param isSwap
	 * @param srcAssociationId
	 * @param targetAssociationId
	 * @param nextIdOfAbstractMetadata
	 * @param nextIdOfDBProperties
	 * @return
	 */
	private String getSQLForConstraintProperties(boolean isSwap, String srcAssociationId,
			String targetAssociationId, Long nextIdOfAbstractMetadata, Long nextIdOfDBProperties)
	{
		String sql = "";
		if (isSwap)
		{
			sql = insertConstraintPropIfSwap(srcAssociationId, targetAssociationId,
					nextIdOfAbstractMetadata, nextIdOfDBProperties);
		}
		else
		{
			sql = insertConstraintPropIfNotSwap(srcAssociationId, targetAssociationId,
					nextIdOfAbstractMetadata, nextIdOfDBProperties);
		}

		return sql;
	}

	/**
	 *
	 * @param srcAssociationId
	 * @param targetId
	 * @param nextIdMetadata
	 * @param nextIdDbProp
	 * @return
	 */
	private String insertConstraintPropIfSwap(String srcAssociationId, String targetId,
			long nextIdMetadata, long nextIdDbProp)
	{
		String sql;
		if (targetId == null)
		{
			sql = INSERT_CONST_PROP + nextIdDbProp + ",null,'" + srcAssociationId + "',"
					+ nextIdMetadata + ")";
		}
		else
		{
			sql = INSERT_CONST_PROP + nextIdDbProp + ",'" + targetId + "','" + srcAssociationId
					+ "'," + nextIdMetadata + ")";
		}
		return sql;
	}

	/**
	 *
	 * @param srcAssociationId
	 * @param targetId
	 * @param nextIdMetadata
	 * @param nextIdDbProp
	 * @return
	 */
	private String insertConstraintPropIfNotSwap(String srcAssociationId, String targetId,
			long nextIdMetadata, long nextIdDbProp)
	{
		String sql;
		if (targetId == null)
		{
			sql = INSERT_CONST_PROP + nextIdDbProp + ",'" + srcAssociationId + "',null,"
					+ nextIdMetadata + ")";
		}
		else if (srcAssociationId == null)
		{
			sql = INSERT_CONST_PROP + nextIdDbProp + ",null,'" + targetId + "',"
					+ nextIdMetadata + ")";
		}
		else
		{
			sql = INSERT_CONST_PROP + nextIdDbProp + ",'" + srcAssociationId + "','" + targetId
					+ "'," + nextIdMetadata + ")";
		}

		return sql;
	}

	/**
	 *
	 * @param tablename
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	private Long getNextId(String tablename, String column) throws SQLException
	{
		final Statement stmt = this.connection.createStatement();
		String sql = "select max(" + column + ") from " + tablename;
		ResultSet resultSet = stmt.executeQuery(sql);
		long nextId = 0;
		if (resultSet.next())
		{
			long maxId = resultSet.getLong(1);
			nextId = maxId + 1;
		}
		resultSet.close();
		stmt.close();
		return nextId;
	}

	/**
	 *
	 * @param tablename
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	private Long getRoleId(String tablename, String column) throws SQLException
	{
		final Statement stmt = this.connection.createStatement();
		String sql = "select target_role_id from dyextn_association where identifier in (select de_association_id from intra_model_association where association_id = (select intermediate_path from path where first_entity_id = (select identifier from dyextn_abstract_metadata where name = 'edu.wustl.catissuecore.domain.SpecimenArrayContent') and last_entity_id = (select identifier from dyextn_abstract_metadata where name = 'edu.wustl.catissuecore.domain.SpecimenArray')))";
		ResultSet resultSet = stmt.executeQuery(sql);
		long nextId = 0;
		if (resultSet.next())
		{
			long maxId = resultSet.getLong(1);
			nextId = maxId + 1;
		}
		resultSet.close();
		stmt.close();
		return nextId;
	}

	/**
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private Long getIdentifier(String sql) throws SQLException
	{
		Long identifier = Long.valueOf(0);
		final Statement stmt = this.connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);
		if (resultSet.next())
		{
			identifier = resultSet.getLong(1);
		}
		resultSet.close();
		stmt.close();
		return identifier;
	}
}
