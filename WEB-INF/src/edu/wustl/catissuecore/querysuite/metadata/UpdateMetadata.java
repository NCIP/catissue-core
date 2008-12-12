package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wustl.common.util.global.Constants;

/**
 * @author vijay_pande
 * 1. For AddAssociation immediately execute executeInsertSQL method since identifier should get updated
 * 2. Verify that all required MAP/List are set properly to generate SQL
 * 3. First generate SQl statements, verify them and then only execute them on DB
 * 4. For delete entity first delete all attributes for that entity. DeleteEntity will generate SQL statements to delete association
 * 5. Always first execute all the delete SQLs then update SQLs and then execute Insert SQLs
 * 6. Map to delete path is populated in DeleteAssociation class itself
 * 7. SQLs of AddRaceMetadata are not redirected to the SQL file. They are directly ran on DB
 */
public class UpdateMetadata
{
	private static Connection connection = null;
	private static Statement stmt = null;
	// The Name of the server for the database. For example : localhost
	static String DATABASE_SERVER_NAME;
	// The Port number of the server for the database.
	static String DATABASE_SERVER_PORT_NUMBER;
	// The Type of Database. Use one of the two values 'MySQL', 'Oracle', MsSqlServer.
	static String DATABASE_TYPE;
	//	Name of the Database.
	static String DATABASE_NAME;
	// Database User name
	static String DATABASE_USERNAME;
	// Database Password
	static String DATABASE_PASSWORD;
	// The database Driver 
	static String DATABASE_DRIVER;
	//Oracle Version
	static String ORACLE_TNS_NAME;
	
	static String DB_SPECIFIC_COMPARE_OPERATOR;
	
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
	{
		try
		{
			configureDBConnection(args);
			connection = getConnection();
			connection.setAutoCommit(true);
			stmt = connection.createStatement();
			UpdateMetadataUtil.isExecuteStatement = true;
			DB_SPECIFIC_COMPARE_OPERATOR = UpdateMetadataUtil.getDBCompareModifier();
					
			deleteMeatadata();
			List<String> updateSQL = updateSQLForDistributionProtocol();
			UpdateMetadataUtil.executeSQLs(updateSQL, connection.createStatement(), false);
			
			addMetadata();
			updateMetadata();
			addCurratedPath();
			deletePermissibleValue();
			addPermissibleValue();
			cleanUpMetadata();
		}
		finally
		{
			if(UpdateMetadataUtil.metadataSQLFile!=null)
			{
				UpdateMetadataUtil.metadataSQLFile.close();
			}
			if(UpdateMetadataUtil.failureWriter!=null)
			{
				UpdateMetadataUtil.failureWriter.close();
			}
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
	
	private static void cleanUpMetadata() throws SQLException, IOException
	{
		CleanUpMetadata cleanUpMetadata = new CleanUpMetadata(connection);
		List<String> deleteSQLList = cleanUpMetadata.cleanMetadata();
		UpdateMetadataUtil.executeSQLs(deleteSQLList, connection.createStatement(), true);
	}

	private static void addPermissibleValue() throws SQLException, IOException
	{
		AddPermissibleValue addPermissibleValue = new AddPermissibleValue(connection);
		addPermissibleValue.addPermissibleValue();
	}

	private static void deletePermissibleValue() throws SQLException, IOException
	{
		DeletePermissibleValue deletePermissibleValue = new DeletePermissibleValue(connection);
		List<String> deleteSQL = deletePermissibleValue.deletePermissibleValue();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);
		
	}

	private static void addCurratedPath() throws IOException, SQLException
	{
		AddCuratedPath addCurratedPath = new AddCuratedPath(connection);
		addCurratedPath.addCurratedPath();
	}

	/**
	 * Configuration
	 * @param args
	 */
	private static void configureDBConnection(String[] args)
	{
		if(args.length < 7)
		{
			throw new RuntimeException("In sufficient number of arguments");
		}
			DATABASE_SERVER_NAME = args[0];
			DATABASE_SERVER_PORT_NUMBER = args[1];
			DATABASE_TYPE = args[2];
			DATABASE_NAME = args[3];
			DATABASE_USERNAME = args[4];
			DATABASE_PASSWORD = args[5];
			DATABASE_DRIVER = args[6];
	}
	
    /**
	 * This method will create a database connection using configuration info.
	 * @return Connection : Database connection object
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Connection connection = null;
		// Load the JDBC driver
		Class.forName(DATABASE_DRIVER);
		// Create a connection to the database
		String url = "";
		if ("MySQL".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:mysql://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + "/"
					+ DATABASE_NAME; // a JDBC url
		}
		if ("Oracle".equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:oracle:thin:@" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER
					+ ":" + DATABASE_NAME;
		}
		if (Constants.MSSQLSERVER_DATABASE.equalsIgnoreCase(DATABASE_TYPE))
		{
			url = "jdbc:sqlserver://" + DATABASE_SERVER_NAME + ":" + DATABASE_SERVER_PORT_NUMBER + ";"
					+ "databaseName=" + DATABASE_NAME + ";";
			System.out.println("UpdateMetadata.getConnection() URL : " + url);
		}
		connection = DriverManager.getConnection(url, DATABASE_USERNAME, DATABASE_PASSWORD);
		return connection;
	}
	private static void updateMetadata() throws SQLException, IOException
	{
		/**  update statements  start **/
		List<String> updateSQL = getUpdateSQL();
		UpdateMetadataUtil.executeSQLs(updateSQL, connection.createStatement(), false);
		
		AddPath pathObject = new AddPath();
		List<String> insertPathSQL = pathObject.getInsertPathStatements(stmt, connection,false);
		UpdateMetadataUtil.executeSQLs(insertPathSQL, connection.createStatement(), false);
		
		//Copy site to abstract SCG path to site-SCG, can not add this to getUpdateSQL() method because of path_id conflict
		ResultSet rs = null;
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select max(PATH_ID) from path");
		if(rs.next())
		{
			Long pathId=rs.getLong(1)+1;
			rs = stmt.executeQuery("select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Site') and LAST_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup')");
			if(rs.next())
			{
				String intermediatePath = rs.getString(1);
				UpdateMetadataUtil.executeInsertSQL("insert into path values("+pathId+", (select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Site'),'"+intermediatePath+"',(select IDENTIFIER from dyextn_abstract_metadata where name"+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.SpecimenCollectionGroup'))", connection.createStatement());
			}
		}
		stmt.close();
		
		/**  update statements  end **/	
	}

	private static void deleteMeatadata() throws IOException, SQLException
	{
		/**  delete path statements  start **/
		DeleteAssociation deleteAssociation = new DeleteAssociation(connection);
		List<String> deletePathSQL = deleteAssociation.getSQLToDeleteAssociation();
		UpdateMetadataUtil.executeSQLs(deletePathSQL, connection.createStatement(), true);
		/**  delete path statements  end **/
		
		/**  delete attribute statements  start **/
		DeleteAttribute deleteAttribute = new DeleteAttribute(connection);
		List<String> deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);
		/**  delete attribute statements  end **/
		
		/**  delete permissible value attribute statements  start **/
		DeletePermissibleValueAttribute  deletePermissibleValueAttribute= new DeletePermissibleValueAttribute(connection);
		List<String> deletePermissibleValueAttributeSQL = deletePermissibleValueAttribute.deletePermissibleValue();
		UpdateMetadataUtil.executeSQLs(deletePermissibleValueAttributeSQL, connection.createStatement(), true);
		/**  delete permissible value attribute statements  end **/
		
		
		/**  delete entity statements  start **/
		
		/**  delete attribute statements  for delete entity to delete start**/
		deleteAttribute = new DeleteAttribute(connection);
		populateAttributesToDeleteMap(deleteAttribute);
		populateAttributeDatatypeMap(deleteAttribute);
		populateEntityList(deleteAttribute);
		
		deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);
			
		List<String> entityNameList = new ArrayList<String>();
		entityNameList.add("edu.wustl.catissuecore.domain.QuantityInCount");
		deleteAttribute.setEntityNameList(entityNameList);
		
		HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
		attributeDatatypeMap.put("id", "long");
		attributeDatatypeMap.put("value", "integer");
		deleteAttribute.setAttributeDatatypeMap(attributeDatatypeMap);
		
		deleteSQL = deleteAttribute.deleteAttribute();
		UpdateMetadataUtil.executeSQLs(deleteSQL, connection.createStatement(), true);

		/**  delete attribute statements  for delete entity to delete end **/
		
		DeleteEntity deleteEntity = new DeleteEntity(connection);
		List<String> deleteEntitySQL=null;
		
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.QuantityInMicrogram");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.QuantityInGram");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.QuantityInMilliliter");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.QuantityInCount");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.Quantity");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		
		deleteEntity.setEntityNameToDelete("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");
		deleteEntitySQL = deleteEntity.deleteEntity();
		UpdateMetadataUtil.executeSQLs(deleteEntitySQL, connection.createStatement(), true);
		/**  delete entity statements  end **/	
	}

	private static void addMetadata() throws SQLException, IOException
	{
		/**  Add Race metadata statements  start **/
		AddRaceMetadata addRaceMetadata = new AddRaceMetadata(connection);
		addRaceMetadata.addRaceMetadata();
		/**  Add Race metadata statements  end **/
				
		AddAbstractPositionMetadata addAbstractPositionMetaData = new AddAbstractPositionMetadata(connection);
		addAbstractPositionMetaData.addContainerMetadata();
		
		AddAbstractSpecimenMetaData addAbstractSpecimenMetaData = new AddAbstractSpecimenMetaData(connection);
		addAbstractSpecimenMetaData.addAbstractPositionMetaData();
		
		AddSpecimenRequirementMetaData addSpecimenRequirementMetaData = new AddSpecimenRequirementMetaData(connection);
		addSpecimenRequirementMetaData.addSpecimenRequirementMetaData();
		
		AddSpecimenClassAttributeMetadata addSpecimenClassAttributeMetadata = new AddSpecimenClassAttributeMetadata(connection);
		addSpecimenClassAttributeMetadata.addSpecimenClass();
		
		AddSAndTMetadata addSAndTMetadata = new AddSAndTMetadata(connection);
		addSAndTMetadata.addSAndTMetadata();
		
		AddSubClassesMetaData addSubClassesMetaData = new AddSubClassesMetaData(connection);
		addSubClassesMetaData.addSubClassesMetadata();
		
		AddAssociation addAssociation = new AddAssociation(connection);
		addAssociation.addAssociation();
		
		AddAttribute addAttribute = new AddAttribute(connection);
		addAttribute.addAttribute();	
	}

	private static List<String> getUpdateSQL() throws SQLException, IOException
	{
		List<String> dbUpdateSQL = new ArrayList<String>();
		ResultSet rs;
		
		stmt = connection.createStatement();
		UpdateMetadataUtil.executeInsertSQL("update path set FIRST_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') where FIRST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen') and LAST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.SpecimenCharacteristics')", connection.createStatement());
		UpdateMetadataUtil.executeInsertSQL("update  path set LAST_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') where FIRST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen') and LAST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')", connection.createStatement());
		UpdateMetadataUtil.executeInsertSQL("update  path set FIRST_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') where FIRST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.Specimen') and LAST_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen')", connection.createStatement());

		dbUpdateSQL.addAll(getInsertPathStatements());

		dbUpdateSQL.add("update dyextn_entity set INHERITANCE_STRATEGY = 3 where PARENT_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')");
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select DE_ASSOCIATION_ID from intra_model_association where ASSOCIATION_ID in (select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID in (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') and LAST_ENTITY_ID in (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.SpecimenCharacteristics'))");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			ResultSet rs1 = stmt.executeQuery("Select TARGET_ROLE_ID from dyextn_association where IDENTIFIER = "+identifier);
			if(rs1.next())
			{
				Long roleId = rs1.getLong(1);
				dbUpdateSQL.add("update dyextn_role set ASSOCIATION_TYPE = 'ASSOCIATION', NAME = 'edu.wustl.catissuecore.domain.AbstractSpecimen' where IDENTIFIER ="+roleId);
			}
		}
		
		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'CATISSUE_CELL_SPECIMEN' where IDENTIFIER = (select IDENTIFIER from dyextn_table_properties  where ABSTRACT_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.CellSpecimen'))");
		
		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'CATISSUE_FLUID_SPECIMEN' where IDENTIFIER = (select IDENTIFIER from dyextn_table_properties  where ABSTRACT_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.FluidSpecimen'))");
		
		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'CATISSUE_MOLECULAR_SPECIMEN' where IDENTIFIER = (select IDENTIFIER from dyextn_table_properties  where ABSTRACT_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.MolecularSpecimen'))");
		
		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'CATISSUE_TISSUE_SPECIMEN' where IDENTIFIER = (select IDENTIFIER from dyextn_table_properties  where ABSTRACT_ENTITY_ID = (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.TissueSpecimen'))");
		
		dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'pathologicalStatus') and ENTIY_ID in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')");

		dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'type') and ENTIY_ID in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')");

		dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'lineage')");

		dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'initialQuantity') and ENTIY_ID in (Select identifier from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')");

		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'type'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'specimenType' where IDENTIFIER ="+identifier);
		}
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'available'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'isAvailable' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.CellSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'available'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'isAvailable' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.FluidSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'available'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'isAvailable' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.MolecularSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'available'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'isAvailable' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select IDENTIFIER from dyextn_abstract_metadata where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.TissueSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'available'");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_abstract_metadata set NAME = 'isAvailable' where IDENTIFIER ="+identifier);
		}
		stmt.close();
		
		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'SPECIMEN_TYPE' where IDENTIFIER in (Select IDENTIFIER from dyextn_column_properties where PRIMITIVE_ATTRIBUTE_ID in (Select IDENTIFIER from dyextn_primitive_attribute where IDENTIFIER in (Select IDENTIFIER from dyextn_abstract_metadata  where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'specimenType')))");

		dbUpdateSQL.add("update dyextn_database_properties set NAME = 'INITIAL_QUANTITY' where IDENTIFIER in (Select IDENTIFIER from dyextn_column_properties where PRIMITIVE_ATTRIBUTE_ID in (Select IDENTIFIER from dyextn_primitive_attribute where IDENTIFIER in (Select IDENTIFIER from dyextn_abstract_metadata  where IDENTIFIER in (Select IDENTIFIER from dyextn_attribute where ENTIY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen')) and NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'initialQuantity')))");

		dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select IDENTIFIER from dyextn_association  where TARGET_ENTITY_ID in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.SpecimenCharacteristics') and SOURCE_ROLE_ID in (Select identifier from dyextn_role where NAME = 'edu.wustl.catissuecore.domain.Specimen'))");
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select DE_ASSOCIATION_ID from intra_model_association where ASSOCIATION_ID in (select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID in (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') and LAST_ENTITY_ID in (select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen'))");
		while(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_association set TARGET_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER="+identifier);
			
			dbUpdateSQL.add("update dyextn_attribute set ENTIY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER="+identifier);
		}
		stmt.close();

		dbUpdateSQL.add("update dyextn_entity set PARENT_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup') where IDENTIFIER in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.CollectionProtocolEvent')");
		
		dbUpdateSQL.add("update dyextn_entity set PARENT_ENTITY_ID = (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+" 'edu.wustl.catissuecore.domain.AbstractSpecimen') where IDENTIFIER in (Select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.Specimen')");
		
		//CSM changes related to query
		dbUpdateSQL.add("update dyextn_role set association_type = 'ASSOCIATION' where identifier in (294,324,322,320,318,316,328,330,332,334,336,312,310,308,306,304,302,314,20)");
		dbUpdateSQL.add("UPDATE dyextn_primitive_attribute SET IS_IDENTIFIED =1 where IDENTIFIER in (839,855,853,851,850,847,846,947,394,681,682,683,684,646,647,648,649,22,102,114,175,180,188,194,201,243,271,315,351,412,422,432,444,455,465,479,493,504,515,531,544,557,569,580,591,602,614,625,635,667,693,759,767,917,928,1239,1240,1251,1285,1873,1876,1918)");
		
		//Delete initial curated path between OrderDetails and TissueSpecimen which is invalid
		dbUpdateSQL.add("delete from path where FIRST_ENTITY_ID in (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.OrderDetails') and LAST_ENTITY_ID= (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.TissueSpecimen')");
		dbUpdateSQL.add("insert into dyextn_tagged_value select Max(IDENTIFIER)+1, 'IS_BIRTH_DATE','true',847 from dyextn_tagged_value");
		
		return dbUpdateSQL;
	}
	
	private static List<String> updateSQLForDistributionProtocol() throws SQLException
	{
		List<String> dbUpdateSQL = new ArrayList<String>();
		ResultSet rs;
		
		//DP related
		dbUpdateSQL.add("update dyextn_database_properties set NAME='CATISSUE_DISTRIBUTION_SPEC_REQ' where NAME = 'CATISSUE_SPECIMEN_REQUIREMENT'");

		dbUpdateSQL.add("update dyextn_abstract_metadata set NAME='edu.wustl.catissuecore.domain.DistributionSpecimenRequirement' where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.SpecimenRequirement'");

		stmt = connection.createStatement();
		rs = stmt.executeQuery("select TARGET_ROLE_ID from dyextn_association where IDENTIFIER = (select DE_ASSOCIATION_ID from intra_model_association where ASSOCIATION_ID = (select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID=(select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionProtocol') and LAST_ENTITY_ID=(select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement')))");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_role set Name='distributionSpecimenRequirementCollection', MIN_CARDINALITY=0 where identifier="+identifier);
		}
		
		stmt = connection.createStatement();
		rs = stmt.executeQuery("select SOURCE_ROLE_ID from dyextn_association where IDENTIFIER = (select DE_ASSOCIATION_ID from intra_model_association where ASSOCIATION_ID = (select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID=(select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionProtocol') and LAST_ENTITY_ID=(select IDENTIFIER from dyextn_abstract_metadata where name "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement')))");
		if(rs.next())
		{
			Long identifier = rs.getLong(1);
			dbUpdateSQL.add("update dyextn_role set Name='distributionProtocol', MIN_CARDINALITY=0, MAX_CARDINALITY=1 where identifier="+identifier);
		}

		dbUpdateSQL.add("delete from path where FIRST_ENTITY_ID in (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement')	and LAST_ENTITY_ID= (select identifier from dyextn_abstract_metadata where NAME"+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.QuantityInMicrogram')");

		dbUpdateSQL.add("delete from path where FIRST_ENTITY_ID in (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement')	and LAST_ENTITY_ID= (select identifier from dyextn_abstract_metadata where NAME"+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.QuantityInGram')");

		dbUpdateSQL.add("delete from path where FIRST_ENTITY_ID in (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement') and LAST_ENTITY_ID= (select identifier from dyextn_abstract_metadata where NAME"+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.QuantityInMilliliter')");

		dbUpdateSQL.add("delete from path where FIRST_ENTITY_ID in (select identifier from dyextn_abstract_metadata where NAME "+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.DistributionSpecimenRequirement') and LAST_ENTITY_ID= (select identifier from dyextn_abstract_metadata where NAME"+DB_SPECIFIC_COMPARE_OPERATOR+"'edu.wustl.catissuecore.domain.QuantityInCount')");

		dbUpdateSQL.add("update dyextn_constraint_properties set SOURCE_ENTITY_KEY='DISTRIBUTION_PROTOCOL_ID', TARGET_ENTITY_KEY=NULL where SOURCE_ENTITY_KEY='SPECIMEN_REQUIREMENT_ID' and TARGET_ENTITY_KEY='DISTRIBUTION_PROTOCOL_ID'");

		dbUpdateSQL.add("update dyextn_constraint_properties set SOURCE_ENTITY_KEY=NULL, TARGET_ENTITY_KEY='DISTRIBUTION_PROTOCOL_ID' where SOURCE_ENTITY_KEY='DISTRIBUTION_PROTOCOL_ID' and TARGET_ENTITY_KEY='SPECIMEN_REQUIREMENT_ID'");
		
		return dbUpdateSQL;
		
	}
	
	private static List<String> getInsertPathStatements() throws SQLException
	{
		List<String> insertPathSQL =new ArrayList<String>();
		AddPath.initData();
		AddPath pathObject = new AddPath();
		insertPathSQL = pathObject.getInsertPathStatements(stmt, connection,true);
		return insertPathSQL;
	}

	private static void populateAttributesToDeleteMap(DeleteAttribute deleteAttribute)
	{
		HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
		List<String> attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("id");
		attributeToDelete.add("value");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.QuantityInMicrogram",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.QuantityInGram",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.QuantityInMilliliter",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.QuantityInCount",attributeToDelete);
			
		attributeToDelete =  new ArrayList<String>();
		attributeToDelete.add("id");
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.Quantity",attributeToDelete);
		entityAttributesToDelete.put("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup",attributeToDelete);
		deleteAttribute.setEntityAttributesToDelete(entityAttributesToDelete);
	}
	
	private static void populateAttributeDatatypeMap(DeleteAttribute deleteAttribute) 
	{
		HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
		attributeDatatypeMap.put("id", "long");
		attributeDatatypeMap.put("value", "double");
		
		deleteAttribute.setAttributeDatatypeMap(attributeDatatypeMap);
	}

	private static void populateEntityList(DeleteAttribute deleteAttribute) 
	{
		List<String> entityNameList = new ArrayList<String>();
		entityNameList.add("edu.wustl.catissuecore.domain.QuantityInMicrogram");
		entityNameList.add("edu.wustl.catissuecore.domain.QuantityInGram");
		entityNameList.add("edu.wustl.catissuecore.domain.QuantityInMilliliter");
		
		entityNameList.add("edu.wustl.catissuecore.domain.Quantity");
		entityNameList.add("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");
		deleteAttribute.setEntityNameList(entityNameList);
	}
}
