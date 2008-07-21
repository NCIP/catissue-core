package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.wustl.common.util.dbManager.DBUtil;

public class AddSpecimenToSpecimenPositionAssociation
{
	private Connection connection = null;

	private static Statement stmt = null;
	
	public static void main(String args[])throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		
		AddSpecimenToSpecimenPositionAssociation addSpecimenToSpecimenPositionAssociation = new AddSpecimenToSpecimenPositionAssociation(connection);
		addSpecimenToSpecimenPositionAssociation.addAssociation();
		
	}
	
	public void addAssociation() throws SQLException, IOException
	{
		String entityName = "edu.wustl.catissuecore.domain.Specimen";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		addAssociation(entityName,targetEntityName,"specimen_specimenPosition","specimen",true,"specimenPosition","SPECIMEN_ID");
		addAssociation(targetEntityName,entityName,"specimenPosition_specimen","specimenPosition",false,"","SPECIMEN_ID");
	}

	public void addAssociation(String entityName, String associatedEntityName,String associationName, String roleName,boolean isSwap,String roleNameTable,String associationId)throws SQLException, IOException
	{
		String sql = "select max(identifier) from dyextn_abstract_metadata";
		ResultSet rs = stmt.executeQuery(sql);
		
		int nextIdOfAbstractMetadata = 0;
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfAbstractMetadata = maxId + 1;
		}
		int nextIdOfDERole = 0;
		sql = "select max(identifier) from dyextn_role";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfDERole = maxId + 1;
		}
		int nextIdOfDBProperties = 0;
		
		sql = "select max(identifier) from dyextn_database_properties";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdOfDBProperties = maxId + 1;
		}
		int nextIDintraModelAssociation = 0;
		sql = "select max(ASSOCIATION_ID) from intra_model_association";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIDintraModelAssociation = maxId + 1;
		}
		
		int nextIdPath = 0;
		sql = "select max(PATH_ID) from path";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}
		
		int entityId = 0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			entityId = rs.getInt(1);
		}
		if (entityId == 0)
		{
			System.out.println("Entity not found of name ");
		}
		
		int associatedEntityId =0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
			+ associatedEntityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			associatedEntityId = rs.getInt(1);
		}
		if (associatedEntityId == 0)
		{
			System.out.println("Entity not found of name ");
		}
		
		sql = "insert into dyextn_abstract_metadata values ("
				+ nextIdOfAbstractMetadata
				+ ",null,null,null,'"+associationName+"',null)";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
		sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE values("+ nextIdOfAbstractMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		
		sql = "insert into dyextn_attribute values ("
				+ nextIdOfAbstractMetadata + "," + entityId + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		int roleId = nextIdOfDERole;
		if(isSwap)
		{
			roleId = nextIdOfDERole +1;
			sql = "insert into dyextn_role values (" + nextIdOfDERole
					+ ",'ASSOCIATION',1,0,'"+roleName+"')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			
				sql = "insert into dyextn_role values (" + roleId
					+ ",'ASSOCIATION',1,0,'"+roleNameTable+"')";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		}		
		if (!isSwap) 
		{
			int lastIdOfDERole =nextIdOfDERole -2;
			int idOfDERole =nextIdOfDERole -1;
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ associatedEntityId + "," + lastIdOfDERole + "," + idOfDERole + ",0)";
		} 
		else
		{
			sql = "insert into dyextn_association values ("
				+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
				+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",1)";
		}
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		sql = "insert into dyextn_database_properties values ("
				+ nextIdOfDBProperties
				+ ",'"+associationName+"')";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		if(isSwap)
		{
			sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ",null,'"+associationId+"',"
				+ nextIdOfAbstractMetadata + ")";
		}
		else
		{
			sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ",'"+associationId+"',null,"
				+ nextIdOfAbstractMetadata + ")";
		}
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
						
		sql = "insert into association values("
			+ nextIDintraModelAssociation + ",2)";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());	
		
		sql = "insert into intra_model_association values("
				+ nextIDintraModelAssociation + "," + nextIdOfAbstractMetadata + ")";
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
		sql = "insert into path values (" + nextIdPath + "," + entityId + ","
		+ nextIDintraModelAssociation + "," + associatedEntityId + ")";
		
		UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
	}

	public AddSpecimenToSpecimenPositionAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
	
}
