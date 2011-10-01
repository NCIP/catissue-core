
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;

/**
 * @author deepti_shelar
 *
 */
public class DeleteEntity
{

	/**
	 * Connection instance.
	 */
	private Connection connection = null;
	/**
	 * Statement instance.
	 */
	private static Statement stmt = null;

	/**
	 * Specify entity Name To Delete.
	 */
	private String entityNameToDelete;
	/**
	 * Specify entity To Delete.
	 */
	private final Entity entityToDelete;

	/*	public static void main(String[] args) throws Exception
		{
			Connection connection = DBUtil.getConnection();
			connection.setAutoCommit(true);

			DeleteEntity deleteEntity = new DeleteEntity(connection);

			deleteEntity.populateEntityToDeletetList();
			deleteEntity.updateEntityToDelete();

			List<String> deleteSQL = new ArrayList<String>();

			List<String> deleteAttribute = deleteEntity.deleteAttribute();
			deleteSQL.addAll(deleteAttribute);

			List<String> deleteEntitySQL = deleteEntity.deleteEntity();
			deleteSQL.addAll(deleteEntitySQL);
			UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);

			connection.close();
		}
	*/
	/**
	 * This method deletes Entity.
	 * @throws SQLException SQL Exception
	 * @return deleteSQL
	 */
	public List<String> deleteEntity() throws SQLException
	{
		List<String> deleteSQL = new ArrayList<String>();
		if (this.entityNameToDelete == null)
		{
			System.out.println("entityNameToDelete is NULL");
		}
		this.updateEntityToDelete();

		deleteSQL = this.getDeleteEntitySQL();
		return deleteSQL;
	}

	/**
	 * This method gets Delete Entity SQL.
	 * @return deleteSQL
	 * @throws SQLException SQL Exception
	 */
	private List<String> getDeleteEntitySQL() throws SQLException
	{
		final List<String> deleteSQL = new ArrayList<String>();
		String sql;

		sql = "delete from dyextn_table_properties where identifier = "
				+ this.entityToDelete.getTableProperties().getId();
		deleteSQL.add(sql);

		sql = "delete from dyextn_database_properties where identifier = "
				+ this.entityToDelete.getTableProperties().getId();
		deleteSQL.add(sql);

		this.deleteAssociation(deleteSQL, this.entityToDelete.getId());

		sql = "delete from intra_model_association" + " where DE_ASSOCIATION_ID"
				+ " in (select identifier from DYEXTN_ASSOCIATION where TARGET_ENTITY_ID ="
				+ this.entityToDelete.getId() + ")";
		deleteSQL.add(sql);

		sql = "delete from DYEXTN_TAGGED_VALUE where ABSTRACT_METADATA_ID ="
				+ this.entityToDelete.getId();
		deleteSQL.add(sql);

		sql = "delete from DYEXTN_SEMANTIC_PROPERTY where ABSTRACT_METADATA_ID ="
				+ this.entityToDelete.getId();
		deleteSQL.add(sql);

		sql = "delete from dyextn_entity where identifier = " + this.entityToDelete.getId();
		deleteSQL.add(sql);

		sql = "select identifier from dyextn_constraint_properties where ABSTRACT_ENTITY_ID="
			+ this.entityToDelete.getId();

		Long constraintId = getConstraintKeyId(sql);
		sql = "delete from dyextn_column_properties where cnstr_key_prop_id in "
				+ "(select identifier from dyextn_constraintkey_prop where "
				+ "src_constraint_key_id=" + constraintId + " or tgt_constraint_key_id="
				+ constraintId + ")";
		deleteSQL.add(sql);

		sql = "delete from dyextn_constraintkey_prop where src_constraint_key_id=" + constraintId
				+ " or tgt_constraint_key_id=" + constraintId;
		deleteSQL.add(sql);

		sql = "delete from dyextn_constraint_properties where ABSTRACT_ENTITY_ID="
				+ this.entityToDelete.getId();
		deleteSQL.add(sql);

		sql = "delete from dyextn_abstract_entity where id = " + this.entityToDelete.getId();
		deleteSQL.add(sql);

		sql = "delete from dyextn_abstract_metadata where identifier = "
				+ this.entityToDelete.getId();
		deleteSQL.add(sql);

		return deleteSQL;
	}

	private Long getConstraintKeyId(String sql) throws SQLException
	{
		Long identifier = null;
		stmt = this.connection.createStatement();
		final ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			identifier = rs.getLong(1);
		}

		return identifier;
	}

	/**
	 * This method gets Delete Entity SQL.
	 * @return deleteSQL.
	 * @throws SQLException SQL Exception
	 */
	private List<String> deleteAttribute() throws SQLException
	{
		final DeleteAttribute deleteAttribute = new DeleteAttribute(this.connection);
		final List<String> entityNameList = new ArrayList<String>();
		entityNameList.add("edu.wustl.catissuecore.domain.Quantity");
		deleteAttribute.setEntityNameList(entityNameList);

		final HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
		final List<String> attributesToDelete = new ArrayList<String>();
		attributesToDelete.add("value");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Quantity", attributesToDelete);
		deleteAttribute.setEntityAttributesToDelete(entityAttributesToDelete);

		final HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
		attributeDatatypeMap.put("value", "double");
		deleteAttribute.setAttributeDatatypeMap(attributeDatatypeMap);

		final List<String> deleteSQL = deleteAttribute.deleteAttribute();
		return deleteSQL;
	}

	/**
	 * This method deletes Association.
	 * @param deleteSQL deleteSQL list
	 * @param id identifier
	 * @throws SQLException SQL Exception
	 */
	private void deleteAssociation(List<String> deleteSQL, Long id) throws SQLException
	{
		int sourceEntityId;
		int targetEntityId;
		final List<Long> roleIdMap = new ArrayList<Long>();

		final DeleteAssociation deleteAssociation = new DeleteAssociation(this.connection);

		String sql = "Select FIRST_ENTITY_ID, LAST_ENTITY_ID from path where LAST_ENTITY_ID=" + id
				+ " or FIRST_ENTITY_ID=" + id;
		final ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
		{

			sourceEntityId = rs.getInt(1);
			targetEntityId = rs.getInt(2);

			roleIdMap.addAll(deleteAssociation.getSourceSQL(deleteSQL, sourceEntityId,
					targetEntityId));
			roleIdMap.addAll(deleteAssociation.getSourceSQL(deleteSQL, targetEntityId,
					sourceEntityId));
		}

		for (final Long srcRoleId : roleIdMap)
		{
			sql = "delete from dyextn_role where IDENTIFIER=" + srcRoleId;
			deleteSQL.add(sql);
		}
	}

	/**
	 * This method populates Entity To Deletet List.
	 */
	private void populateEntityToDeletetList()
	{
		this.entityNameToDelete = "edu.wustl.catissuecore.domain.Quantity";
	}

	/**
	 * This method updates Entity To Delete.
	 * @throws SQLException SQLException
	 */
	private void updateEntityToDelete() throws SQLException
	{
		String sql;
		stmt = this.connection.createStatement();
		ResultSet rs;

		sql = "select identifier,name from dyextn_abstract_metadata where NAME "
				+ UpdateMetadataUtil.getDBCompareModifier() + "'" + this.entityNameToDelete + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			this.entityToDelete.setId(rs.getLong(1));
			this.entityToDelete.setName(rs.getString(2));
		}
		final TableProperties tableProperties = new TableProperties();
		sql = "select identifier from dyextn_table_properties where ABSTRACT_ENTITY_ID="
				+ this.entityToDelete.getId();
		rs = stmt.executeQuery(sql);

		if (rs.next())
		{
			tableProperties.setId(rs.getLong(1));
		}
		this.entityToDelete.setTableProperties(tableProperties);
	}

	/**
	 * This method set Entity Name To Delete.
	 * @param entityNameToDelete entity Name To Delete
	 */
	public void setEntityNameToDelete(String entityNameToDelete)
	{
		this.entityNameToDelete = entityNameToDelete;
	}

	/**
	 * Constructor.
	 * @param connection Connection object.
	 * @throws SQLException SQL Exception
	 */
	public DeleteEntity(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		DeleteEntity.stmt = connection.createStatement();
		this.entityToDelete = new Entity();
	}
}
