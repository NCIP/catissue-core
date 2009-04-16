package edu.wustl.catissuecore.querysuite.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DataElement;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class UpdateMetadataUtil
{
	private static Logger logger = Logger.getCommonLogger(UpdateMetadataUtil.class);
	public static BufferedWriter metadataSQLFile;
	public static String fileName="./metadata.sql";
	public static BufferedWriter failureWriter;
	public static String errorFileName="./SQLerror.txt";
	public static Boolean isExecuteStatement = false;

	public static int executeInsertSQL(String sql, Statement stmt) throws IOException, SQLException 
	{
		int b = 0;
		if(metadataSQLFile == null)
		{
			metadataSQLFile = new BufferedWriter(new FileWriter(new File(fileName)));
		}
		try
		{
			metadataSQLFile.write(sql+";\n");
			if(isExecuteStatement)
			{
				stmt.executeUpdate(sql);
			}
		}
		catch (SQLException e)
		{
			logger.debug(e.getMessage(), e);
			if(failureWriter == null)
			{
				failureWriter = new BufferedWriter(new FileWriter(new File(errorFileName)));
			}
			failureWriter.write("\nException: "+e.getMessage()+"\n");
			failureWriter.write(sql+";");
		}
		finally
		{
			stmt.close();
		}
		return b;
	}

	public static int getEntityIdByName(String entityName, Statement stmt) throws IOException, SQLException
	{
		ResultSet rs;
		int entityId = 0;
		String sql = "select identifier from dyextn_abstract_metadata where name like '"
			+ entityName + "'";
		try
		{

			rs = stmt.executeQuery(sql);
			if (rs.next()) 
			{
				entityId = rs.getInt(1);
			}
			if (entityId == 0) 
			{
				System.out.println("Entity not found of name "+entityName);
			}
		}
		catch (SQLException e)
		{
			logger.debug(e.getMessage(), e);
			if(failureWriter == null)
			{
				failureWriter = new BufferedWriter(new FileWriter(new File(errorFileName)));
			}
			failureWriter.write(sql+";");
			failureWriter.write("\nException: "+e.getMessage()+"\n");
		}
		finally
		{
			stmt.close();
		}
		return entityId;
	}

	public static void executeSQLs(List<String> deleteSQL, Statement stmt, boolean isDelete) throws IOException, SQLException 
	{
		try
		{
			if(metadataSQLFile == null)
			{
				metadataSQLFile = new BufferedWriter(new FileWriter(new File(fileName)));
			}
			for(String sql: deleteSQL)
			{
				try
				{
					metadataSQLFile.write(sql+";\n");
					if(isExecuteStatement)
					{
						if(isDelete)
						{
							stmt.execute(sql);
						}
						else
						{
							stmt.executeUpdate(sql);
						}
					}
				}
				catch (SQLException e)
				{
					logger.debug(e.getMessage(), e);
					if(failureWriter == null)
					{
						failureWriter = new BufferedWriter(new FileWriter(new File(errorFileName)));
					}
					failureWriter.write(sql+";");
					failureWriter.write("\nException: "+e.getMessage()+"\n");
				}
			}
		}
		finally
		{
			stmt.close();
		}
	}
	
	public static HashMap<Long, List<AttributeInterface>> populateEntityAttributeMap(Connection connection,Map<String, Long> entityIDMap) throws SQLException 
	{
		HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
		List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
		Statement stmt = null;
		String sql;
		Set<String> keySet = entityIDMap.keySet();
		Long identifier;
		for(String  key : keySet)
		{
			attributeList = new ArrayList<AttributeInterface>();
			identifier = entityIDMap.get(key);
			sql= "select identifier,name from dyextn_abstract_metadata where identifier in (select identifier from dyextn_attribute where ENTIY_ID="+identifier+")";
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				AttributeInterface attributeInterface=new Attribute();
				attributeInterface.setId(rs.getLong(1));
				attributeInterface.setName(rs.getString(2));
				ColumnProperties  columnProperties=new ColumnProperties();
				sql= "select identifier from dyextn_column_properties where PRIMITIVE_ATTRIBUTE_ID="+attributeInterface.getId();
				stmt = connection.createStatement();
				ResultSet rs1 = stmt.executeQuery(sql);
				if(rs1.next())
				{
					columnProperties.setId(rs1.getLong(1));
				}
				
				stmt.close();
				rs1.close();
				attributeInterface.setColumnProperties(columnProperties);
				
				AttributeTypeInformationInterface  attributeTypeInfo=new StringAttributeTypeInformation();
				DataElement dataElement = new UserDefinedDE();
				sql= "select identifier from dyextn_attribute_type_info where PRIMITIVE_ATTRIBUTE_ID="+attributeInterface.getId();
				stmt = connection.createStatement();
				ResultSet rs2 = stmt.executeQuery(sql);
				if(rs2.next())
				{
					dataElement.setId(rs2.getLong(1));
				}
				attributeTypeInfo.setDataElement(dataElement);
				attributeInterface.setAttributeTypeInformation(attributeTypeInfo);
				stmt.close();
				rs2.close();
				attributeList.add(attributeInterface);				
			}
			rs.close();
			entityIDAttributeListMap.put(identifier, attributeList);
		}
		return entityIDAttributeListMap;
	}
	
	public static void commonDeleteStatements(AttributeInterface attribute,
			List<String> deleteSQL) 
	{
		String sql;
		
		
		sql = "delete from dyextn_attribute_type_info where identifier = "+attribute.getAttributeTypeInformation().getDataElement().getId();
		deleteSQL.add(sql);
		
		sql = "delete from DYEXTN_CADSR_VALUE_DOMAIN_INFO where PRIMITIVE_ATTRIBUTE_ID = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_primitive_attribute where identifier = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_attribute where identifier = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from DYEXTN_SEMANTIC_PROPERTY where ABSTRACT_METADATA_ID = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from DYEXTN_BASE_ABSTRACT_ATTRIBUTE where identifier = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from DYEXTN_TAGGED_VALUE where ABSTRACT_METADATA_ID = "+attribute.getId();
		deleteSQL.add(sql);
		
		sql = "delete from dyextn_abstract_metadata where identifier = "+attribute.getId();
		deleteSQL.add(sql);
	}
	
	
	public static String getDBCompareModifier()
	{
		if(UpdateMetadata.DATABASE_TYPE.equalsIgnoreCase(Constants.MSSQLSERVER_DATABASE))
		{
			return " like ";
		}
		else
		{
			return " = ";
		}
	}
	
	/**
	 * Method for setting identity insert ON before inserting rows with value to an identity column in MsSqlServer
	 * @param tableName
	 * @return
	 */
	public static String getIdentityOnStatement(String tableName)
	{
		String sql =  "SET IDENTITY_INSERT "+ tableName+" ON;";
		return sql;
	}
	/**
	 * Method for setting identity insert OFF after inserting rows with value to an identity column in MsSqlServer
	 * @param tableName
	 * @return
	 */
	public static String getIdentityOffStatement(String tableName)
	{
		String sql = " SET IDENTITY_INSERT "+ tableName+" OFF;";
		return sql;
	}
	
	
	/**
	 * Method that appends setting identity insert ON & OFF statements which facilitates inserting 
	 * values into a column which is declared as identity in MsSqlServer 
	 * @param sql
	 * @param tableName
	 * @return
	 */
	public static String getIndentityInsertStmtForMsSqlServer(String sql, String tableName)
	{
		sql = getIdentityOnStatement(tableName)+sql+getIdentityOffStatement(tableName);
		return sql;
	}
}
