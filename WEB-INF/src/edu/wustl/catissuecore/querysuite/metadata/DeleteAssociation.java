package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.wustl.common.util.dbManager.DBUtil;

/**
 * 
 * @author deepti_shelar
 *
 */
public class DeleteAssociation 
{
	private Connection connection = null;

	private Statement stmt = null;

	private static List<String> entityNameListDelete = new ArrayList<String>();
	private static List<Entity> entityToDelete = new ArrayList<Entity>();
	static Map<String,String> twoEntityMap = new HashMap<String, String>();
	public static void main(String[] args) throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		
		DeleteAssociation deleteAssociation = new DeleteAssociation(connection);
		List<String> deleteSQL = new ArrayList<String>();
		
		deleteAssociation.populateEntityToDeletetList();
		deleteAssociation.updateEntityToDeleteList();
		deleteAssociation.populateAssociationToDelete();
		Set<String> keySet = twoEntityMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext())
		{
			String srcName = iterator.next();
			deleteSQL.addAll(deleteAssociation.deleteAssociation(srcName,twoEntityMap.get(srcName)));	
		}		
		connection.close();
	}

	private void populateAssociationToDelete()
	{
		
		twoEntityMap.put("edu.wustl.catissuecore.domain.SpecimenArray","edu.wustl.catissuecore.domain.StorageContainer");
		
	}


	public List<String> deleteAssociation(String srcName, String targetName) throws SQLException, IOException
	{
		int SOURCE_ENTITY_ID = UpdateMetadataUtil.getEntityIdByName(srcName, connection.createStatement());
		int TARGET_ENTITY_ID = UpdateMetadataUtil.getEntityIdByName(targetName, connection.createStatement());
		
		List<String> deleteSQL = deleteAssociation(SOURCE_ENTITY_ID, TARGET_ENTITY_ID);
			
		return deleteSQL;
	}
	
	public List<String> deleteAssociation(int SOURCE_ENTITY_ID, int TARGET_ENTITY_ID) throws SQLException
	{
		List<String> deleteSQL = new ArrayList<String>();
		String sql;
		
		List<Long> roleIdMap = getSourceSQL(deleteSQL, SOURCE_ENTITY_ID, TARGET_ENTITY_ID);
		roleIdMap.addAll(getSourceSQL(deleteSQL, TARGET_ENTITY_ID, SOURCE_ENTITY_ID));
		
		for(Long srcRoleId : roleIdMap)
		{
			if(srcRoleId!=null)
			{
				sql = "delete from dyextn_role where IDENTIFIER="+srcRoleId;
				deleteSQL.add(sql);
			}
		}

		return deleteSQL;
	}

	public List<Long> getSourceSQL(List<String> deleteSQL, int SOURCE_ENTITY_ID, int TARGET_ENTITY_ID) throws SQLException
	{
		String sql;
		Long srcRoleId = null;
		Long targetRoleId =null;
		Long deAssociationId =null;
		
		List<Long> roleIdMap = new ArrayList<Long>();
		
		sql = "select INTERMEDIATE_PATH from  path where FIRST_ENTITY_ID ="+SOURCE_ENTITY_ID+" AND LAST_ENTITY_ID = "+TARGET_ENTITY_ID;
		stmt = connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		try
		{
			while(rs.next())
			{
				String  intermediatePathId = rs.getString(1);
				
				sql = "delete from path where INTERMEDIATE_PATH ='"+intermediatePathId+"'";
				deleteSQL.add(sql);
				
				StringTokenizer st = new StringTokenizer(intermediatePathId, "_");
				while(st.hasMoreTokens())
				{
					String pathId=st.nextToken();
					sql = "delete from path where INTERMEDIATE_PATH ='"+pathId+"'";
					deleteSQL.add(sql);
					sql = "select DE_ASSOCIATION_ID from intra_model_association where ASSOCIATION_ID="+pathId;
					Statement stmt2 = connection.createStatement();
					ResultSet rs2 = stmt2.executeQuery(sql);
					if(rs2.next())
					{
						deAssociationId = rs2.getLong(1);
						sql = "select DIRECTION,SOURCE_ROLE_ID,TARGET_ROLE_ID from dyextn_association where identifier = "+deAssociationId;
						Statement stmt3 = connection.createStatement();
						ResultSet rs3 = stmt3.executeQuery(sql);
						if(rs3.next())
						{
							if(rs3.getString(1).equals("BI_DIRECTIONAL"))
							{
								srcRoleId = rs3.getLong(2);
								targetRoleId = rs3.getLong(3);
								roleIdMap.add(srcRoleId);
								roleIdMap.add(targetRoleId);
							}
						}
						rs3.close();
						stmt3.close();
					}
					rs2.close();
					stmt2.close();
					
					sql = "select identifier from dyextn_constraint_properties where ASSOCIATION_ID = "+deAssociationId;
					Statement stmt1 = connection.createStatement();
					ResultSet rs1 = stmt1.executeQuery(sql);
					if(rs1.next())
					{
						Long constraintId = rs1.getLong(1);
						sql = "delete from dyextn_constraint_properties where identifier = "+constraintId;
						deleteSQL.add(sql);
						
						sql = "delete from dyextn_database_properties where identifier = "+constraintId;
						deleteSQL.add(sql);
						
					}
					rs1.close();
					stmt1.close();
					
					sql = "delete from intra_model_association where ASSOCIATION_ID ="+pathId;
					deleteSQL.add(sql);
					
					
					sql = "delete from association where ASSOCIATION_ID ="+pathId;
					deleteSQL.add(sql);
					
					sql = "delete from dyextn_association where IDENTIFIER="+deAssociationId;
					deleteSQL.add(sql);
					
					sql ="delete from dyextn_attribute where identifier ="+deAssociationId;
					deleteSQL.add(sql);
					
					sql = "delete from DYEXTN_BASE_ABSTRACT_ATTRIBUTE where identifier="+deAssociationId;
					deleteSQL.add(sql);
					
					sql = "delete from dyextn_tagged_value where ABSTRACT_METADATA_ID="+deAssociationId;
					deleteSQL.add(sql);
					
					sql = "delete from dyextn_semantic_property where ABSTRACT_METADATA_ID="+deAssociationId;
					deleteSQL.add(sql);
					
					sql = "delete from dyextn_primitive_attribute where identifier ="+deAssociationId;
					deleteSQL.add(sql);
						
					sql = "delete from dyextn_rule_parameter where  RULE_ID =(select IDENTIFIER from dyextn_rule where ATTRIBUTE_ID ="+deAssociationId+")";
					deleteSQL.add(sql);
						
					sql = "delete from dyextn_rule where ATTRIBUTE_ID ="+deAssociationId;
					deleteSQL.add(sql);
						
					sql = "delete from dyextn_abstract_metadata where identifier="+deAssociationId;
					deleteSQL.add(sql);
				}
				
			}
			if(srcRoleId!=null)
			{
				sql = "delete from dyextn_role where IDENTIFIER="+srcRoleId;
				deleteSQL.add(sql);
			}
			
			if(targetRoleId!=null)
			{
				sql = "delete from dyextn_role where IDENTIFIER="+targetRoleId;
				deleteSQL.add(sql);
			}		
		}
		finally
		{
			rs.close();
			stmt.close();
		}
		return roleIdMap;
	}

	private void populateEntityToDeletetList()
	{
		entityNameListDelete = new ArrayList<String>();
		entityNameListDelete.add("edu.wustl.catissuecore.domain.Quantity");
		entityNameListDelete.add("edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup");
	}
	
	private void updateEntityToDeleteList() throws SQLException
	{
		String sql;
		stmt = connection.createStatement();
		ResultSet rs;
		for(String entityName : entityNameListDelete)
		{
			Entity entity = new Entity();
			sql = "select identifier,name from dyextn_abstract_metadata where NAME='"+entityName+"'";
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				entity.setId(rs.getLong(1));
				entity.setName(rs.getString(2));
			}
			TableProperties tableProperties=new TableProperties();
			sql = "select identifier from dyextn_table_properties where ABSTRACT_ENTITY_ID="+entity.getId();
			rs = stmt.executeQuery(sql);
			
			if(rs.next())
			{
				tableProperties.setId(rs.getLong(1));
			}
			rs.close();
			entity.setTableProperties(tableProperties);
			
			entityToDelete.add(entity);
		}
	}

	public DeleteAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
	
	public List<String> getSQLToDeleteAssociation() throws IOException, SQLException  
	{
		List<String> deleteSQL = new ArrayList<String>();
		
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen", "edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.Quantity"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.SpecimenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.CollectionEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.FrozenEventParameters"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Container","edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer","edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer","edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray","edu.wustl.catissuecore.domain.StorageContainer"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Container","edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray","edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArray","edu.wustl.catissuecore.domain.SpecimenArray"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Container","edu.wustl.catissuecore.domain.Container"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.StorageContainer","edu.wustl.catissuecore.domain.StorageContainer"));

		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArrayContent","edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArrayContent","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArrayContent","edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.SpecimenArrayContent","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem","edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem","edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.DistributedItem","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.Specimen","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.CellSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.TissueSpecimen","edu.wustl.catissuecore.domain.SpecimenCharacteristics"));
		
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.MolecularSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.CellSpecimen","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.FluidSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.MolecularSpecimen","edu.wustl.catissuecore.domain.TissueSpecimen"));
		deleteSQL.addAll(deleteAssociation("edu.wustl.catissuecore.domain.FluidSpecimen","edu.wustl.catissuecore.domain.TissueSpecimen"));
		
		return deleteSQL;
	}
}
