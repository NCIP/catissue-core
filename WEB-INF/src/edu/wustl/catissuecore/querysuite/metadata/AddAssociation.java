package edu.wustl.catissuecore.querysuite.metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.wustl.common.util.dbManager.DBUtil;
/**
 * 
 * @author deepti_shelar
 *
 */
public class AddAssociation{
	private static Connection connection = null;

	private static Statement stmt = null;

	public static void main(String[] args)
			throws Exception{
		connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		stmt = connection.createStatement();
		
		AddAssociation addAssociation = new AddAssociation();
		String entityName = "edu.wustl.catissuecore.domain.User";
		String targetEntityName = "edu.wustl.catissuecore.domain.Site";
//		addAssociation(entityName,targetEntityName,"user_site","CONTAINTMENT","siteCollection",true,"'USER_ID'","'SITE_ID'");
//		addAssociation(targetEntityName,entityName,"site_user","ASSOCIATION","userCollection",false,"'SITE_ID'","'USER_ID'");
		
		entityName = "edu.wustl.catissuecore.domain.User";
		targetEntityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		List<String> deleteSQL = addAssociation.addAssociation(entityName,targetEntityName,"user_collectionProtocol","CONTAINTMENT","userCollectionProtocolCollection",true,"'USER_ID'","'SITE_ID'");
		deleteSQL = addAssociation.addAssociation(targetEntityName,entityName,"collectionProtocol_user","ASSOCIATION","userCollection",false,"'SITE_ID'","'USER_ID'");
		
		entityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		targetEntityName = "edu.wustl.catissuecore.domain.Site";
		deleteSQL = addAssociation.addAssociation(entityName,targetEntityName,"collectionProtocol_site","CONTAINTMENT","siteCollection",true,"NULL","'SITE_ID'");
		
	}

	private List<String> addAssociation(String entityName,String associatedEntityName,String associationName,String associationType, String roleName,boolean isSwap, String SOURCE_ENTITY_KEY, String TARGET_ENTITY_KEY) throws SQLException
	{
		List<String> deleteSQL= new ArrayList<String>();
		
		String sql = "select max(identifier) from dyextn_abstract_metadata";
		ResultSet rs = stmt.executeQuery(sql);

		int nextIdOfAbstractMetadata = 0;
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfAbstractMetadata = maxId + 1;
		}
		int nextIdOfDERole = 0;
		sql = "select max(identifier) from dyextn_role";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDERole = maxId + 1;
		}
		int nextIdOfDBProperties = 0;

		sql = "select max(identifier) from dyextn_database_properties";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdOfDBProperties = maxId + 1;
		}
		int nextIDintraModelAssociation = 0;
		sql = "select max(ASSOCIATION_ID) from intra_model_association";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIDintraModelAssociation = maxId + 1;
		}
		
		int nextIdPath = 0;
		sql = "select max(PATH_ID) from path";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}

		int entityId = 0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
				+ entityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			entityId = rs.getInt(1);

		}
		if (entityId == 0) {
			System.out.println("Entity not found of name ");
		}
		
		int associatedEntityId =0;
		
		sql = "select identifier from dyextn_abstract_metadata where name like '"
			+ associatedEntityName + "'";
		rs = stmt.executeQuery(sql);
		if (rs.next()) {
			associatedEntityId = rs.getInt(1);

		}
		if (associatedEntityId == 0) {
			System.out.println("Entity not found of name ");
		}
		
		sql = "insert into dyextn_abstract_metadata values ("
				+ nextIdOfAbstractMetadata
				+ ",null,null,null,'"+associationName+"',null)";
		deleteSQL.add(sql);
		
		sql = "INSERT INTO DYEXTN_BASE_ABSTRACT_ATTRIBUTE values("+ nextIdOfAbstractMetadata + ")";
		deleteSQL.add(sql);

		sql = "insert into dyextn_attribute values ("
				+ nextIdOfAbstractMetadata + "," + entityId + ")";
		deleteSQL.add(sql);
		int roleId = nextIdOfDERole;
		if(isSwap)
		{
			roleId = nextIdOfDERole +1;
			sql = "insert into dyextn_role values (" + nextIdOfDERole
					+ ",'"+associationType+"',2,0,'"+roleName+"')";
			deleteSQL.add(sql);
			nextIdOfDERole++;
		
			
			//participantAnnotation
			sql = "insert into dyextn_role values (" + roleId
					+ ",'ASSOCIATION',1,0,'"+roleName+"')";
			deleteSQL.add(sql);
		}
		
		if (!isSwap) {
			int lastIdOfDERole =nextIdOfDERole -2;
			int idOfDERole =nextIdOfDERole -1;
			sql = "insert into dyextn_association values ("
					+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
					+ associatedEntityId + "," + lastIdOfDERole + "," + idOfDERole + ",1)";
		} else {
			sql = "insert into dyextn_association values ("
				+ nextIdOfAbstractMetadata + ",'BI_DIRECTIONAL',"
				+ associatedEntityId + "," + roleId + "," + nextIdOfDERole + ",1)";
		}
		deleteSQL.add(sql);
		
		sql = "insert into dyextn_database_properties values ("
				+ nextIdOfDBProperties
				+ ",'"+associationName+"')";
		deleteSQL.add(sql);
		
		if(isSwap)
		{
		sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ","+SOURCE_ENTITY_KEY+","+TARGET_ENTITY_KEY+","
				+ nextIdOfAbstractMetadata + ")";
		}else
		{
			sql = "insert into dyextn_constraint_properties (IDENTIFIER,SOURCE_ENTITY_KEY,TARGET_ENTITY_KEY,ASSOCIATION_ID) values("
				+ nextIdOfDBProperties + ","+SOURCE_ENTITY_KEY+","+TARGET_ENTITY_KEY+","
				+ nextIdOfAbstractMetadata + ")";
			
		}
		deleteSQL.add(sql);
						
		sql = "insert into association values("
			+ nextIDintraModelAssociation + ",2)";
		deleteSQL.add(sql);
	
		sql = "insert into intra_model_association values("
				+ nextIDintraModelAssociation + "," + nextIdOfAbstractMetadata + ")";
		deleteSQL.add(sql);
		
		sql = "insert into path values (" + nextIdPath + "," + entityId + ","
		+ nextIDintraModelAssociation + "," + associatedEntityId + ")";
		
		deleteSQL.add(sql);
		nextIdPath++;
		return deleteSQL;
	}
}
