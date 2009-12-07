
package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * AddPath.
 * @author deepali_ahirrao
 */
public class AddDEIntegrationPath
{
	/**
	 * specify superClass And SubClasses Map.
	 */
	private Map<String, List<String>> superSubClassMap = new HashMap<String, List<String>>();
	/**
	 * specify superClass And Associations Map.
	 */
	private Map<String, List<String>> superClassAssoMap = new HashMap<String, List<String>>();

	/**
	 * specify superClass name and Description Map.
	 */
	private Map<String, String> superClassDescMap = new HashMap<String, String>();
	/**
	 * specify identifier.
	 */
	private int identifier = 0;

	private final Map<String, String> abstractMetaDataIds = new HashMap<String, String>();

	/**
	 * Table name.
	 */
	private static final String ABSTRACT_METADATA = "DYEXTN_ABSTRACT_METADATA";

	/**
	 * Initialize Data.
	 */
	public void initData(Map<String, List<String>> superSubClassMap,
			Map<String, List<String>> superClassAssoMap, Map<String, String> superClassDescMap)
	{
		this.superSubClassMap = superSubClassMap;
		this.superClassAssoMap = superClassAssoMap;
		this.superClassDescMap = superClassDescMap;
	}

	/**
	 * This method parses the maps, calls the getInsertPaths()
	 * method and returns the insert statements.
	 * @param jdbcdao jdbcdao
	 * @return Insert Path Statements.
	 * @throws SQLException SQL Exception
	 * @throws DAOException  DAOException
	 */
	public List<String> parseMaps(JDBCDAO jdbcdao)
			throws SQLException, DAOException
	{
		final List<String> insertPathSQL = new ArrayList<String>();
		ResultSet resultSet;

		resultSet = jdbcdao.getQueryResultSet("SELECT max(PATH_ID) FROM PATH");
		if (resultSet.next())
		{
			this.identifier = resultSet.getInt(1) + 1;
		}
		String sql;
		final Set<String> keySet = superClassAssoMap.keySet();
		final Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			final String key = iterator.next();
			sql = getSqlForDesc(key);
			resultSet = jdbcdao.getQueryResultSet(sql);
			if (resultSet.next())
			{
				resultSet = getInsertPaths(jdbcdao, insertPathSQL, resultSet, key);
			}
			else
			{
				System.out.println("Entity with name : " + key + " not found");
			}
		}
		jdbcdao.closeStatement(resultSet);
		return insertPathSQL;
	}

	/**
	 * Get insert path statements.
	 * @param jdbcdao jdbcdao
	 * @param insertPathSQL list of insert statements
	 * @param resultSet ResultSet object
	 * @param key key
	 * @return List of insert statements
	 * @throws SQLException SQLException
	 * @throws DAOException DAOException
	 */
	private ResultSet getInsertPaths(JDBCDAO jdbcdao,
			final List<String> insertPathSQL, ResultSet resultSet,
			final String key) throws SQLException, DAOException
	{
		String sql;
		String entityId;
		String assocEntityId;
		entityId = String.valueOf(resultSet.getLong(1));
		final List<String> associationsList = superClassAssoMap.get(key);
		for (final String associatedEntityName : associationsList)
		{
			sql = "SELECT IDENTIFIER FROM " + ABSTRACT_METADATA + " WHERE NAME = " + "'"
					+ associatedEntityName + "'";
			resultSet = jdbcdao.getQueryResultSet(sql);
			if (resultSet.next())
			{
				assocEntityId = String.valueOf(resultSet.getLong(1));
				abstractMetaDataIds.put(associatedEntityName, assocEntityId);

				sql = "SELECT INTERMEDIATE_PATH FROM PATH WHERE FIRST_ENTITY_ID = "
						+ entityId + " and LAST_ENTITY_ID = " + assocEntityId;
				resultSet = jdbcdao.getQueryResultSet(sql);
				while (resultSet.next())
				{
					final String intermediatePathId = resultSet.getString(1);
					if(intermediatePathId.indexOf('_') == -1)
					{
						final List<String> subClassList = superSubClassMap.get(key);
						for (final String subClassEntity : subClassList)
						{
							String subClassEntityId;
							sql = "SELECT IDENTIFIER FROM " + ABSTRACT_METADATA + " WHERE NAME = "
									+ "'" + subClassEntity + "'";
							final ResultSet rs1 = jdbcdao.getQueryResultSet(sql);
							if (rs1.next())
							{
								subClassEntityId = String.valueOf(rs1.getLong(1));
								abstractMetaDataIds.put(subClassEntity, subClassEntityId);
								if (key.equals(associatedEntityName))
								{
									insertPathSQL.add("INSERT INTO PATH VALUES("
											+ this.identifier++ + "," + subClassEntityId + ","
											+ intermediatePathId + "," + subClassEntityId + ")");
								}
								else
								{
									insertPathSQL.add("INSERT INTO PATH VALUES("
											+ this.identifier++ + "," + subClassEntityId + ","
											+ intermediatePathId + "," + assocEntityId + ")");
								}
								jdbcdao.closeStatement(rs1);
							}
						}
					}
				}
				if (!(key.equals(associatedEntityName)))
				{
					insertPaths(jdbcdao, insertPathSQL, entityId, assocEntityId, key);
				}
			}
		}
		return resultSet;
	}

	/**
	 * @param key key
	 * @return The query
	 */
	private String getSqlForDesc(final String key)
	{
		String sql;
		final String description = superClassDescMap.get(key);
		if (description != null)
		{
			sql = "SELECT IDENTIFIER FROM " + ABSTRACT_METADATA + " WHERE NAME = " + "'" + key
					+ "' and DESCRIPTION = '" + description + "'";
		}
		else
		{
			sql = "SELECT IDENTIFIER FROM " + ABSTRACT_METADATA + " WHERE NAME = " + "'" + key
					+ "'";
		}
		return sql;
	}

	/**
	 * @param jdbcdao JDBCDAO object
	 * @param insertPathSQL list of insert path statements
	 * @param entityId entity id
	 * @param associatedId associated entity id
	 * @param key key
	 * @throws SQLException SQLException
	 * @throws DAOException DAOException
	 */
	private void insertPaths(JDBCDAO jdbcdao, final List<String> insertPathSQL, String entityId,
			String associatedId, final String key) throws SQLException, DAOException
	{
		ResultSet resultSet;
		String sql;
		sql = "SELECT INTERMEDIATE_PATH" + " FROM PATH WHERE FIRST_ENTITY_ID = " + associatedId
				+ " AND LAST_ENTITY_ID = " + entityId;
		resultSet = jdbcdao.getQueryResultSet(sql);
		while (resultSet.next())
		{
			final String intermediateId = resultSet.getString(1);
			if (intermediateId.indexOf('_') == -1)
			{
				final List<String> subClassList = superSubClassMap
						.get(key);
				for (final String subClassEntity : subClassList)
				{
					String subClassEntityId;
					sql = "SELECT IDENTIFIER" + " FROM "+ABSTRACT_METADATA
							+ " WHERE NAME = "
							+ "'"
							+ subClassEntity + "'";
					final ResultSet rs1 = jdbcdao.getQueryResultSet(sql);
					if (rs1.next())
					{
						subClassEntityId = String.valueOf(rs1.getLong(1));
						abstractMetaDataIds.put(subClassEntity, subClassEntityId);
						insertPathSQL.add("INSERT INTO PATH VALUES("
								+ this.identifier++ + "," + associatedId
								+ "," + intermediateId + "," + subClassEntityId
								+ ")");
					}
					jdbcdao.closeStatement(rs1);
				}
			}
		}
		jdbcdao.closeStatement(resultSet);
	}


	public Map<String, String> getAbstractMetaDataIds()
	{
		return abstractMetaDataIds;
	}


}
