package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



/**
 * @author vijay_pande
 * Class to add curated path between two entities if an intermediate entity exist common to source as well as target entity.
 */
public class AddCuratedPath
{
	private static Connection connection;
	private List<String> entityList;
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException
	{
		Connection connection=DBUtil.getConnection();
		
//		Class.forName("com.mysql.jdbc.Driver");
//		String url = "jdbc:mysql://localhost:3307/upgrade";
//		Connection connection = DriverManager.getConnection(url, "root", "pspl");
		
		AddCuratedPath addCurratedPath = new AddCuratedPath(connection);
		addCurratedPath.addCurratedPath();
	}
	
	public void addCurratedPath() throws IOException, SQLException
	{
		populateMapForPath();
		
		StringTokenizer st;
		String sourceEntity;
		String targetEntity;
		String intermediatePath;
		
		int sourceEntityId=0;
		int targetEntityId=0;
		int mainEntityId=0;
		
		int nextIdPath = 0;
		String sql = "select max(PATH_ID) from path";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next())
		{
			int maxId = rs.getInt(1);
			nextIdPath = maxId + 1;
		}
		stmt.close();
		for(String key : entityList)
		{
			intermediatePath="";
			st = new StringTokenizer(key, ",");
			sourceEntity = st.nextToken();
			
			sourceEntityId = UpdateMetadataUtil.getEntityIdByName(sourceEntity, connection.createStatement());
			mainEntityId = sourceEntityId;
			while(st.hasMoreTokens())
			{
				targetEntity = st.nextToken();
				targetEntityId = UpdateMetadataUtil.getEntityIdByName(targetEntity, connection.createStatement());
				
				String tempPath = getIntermediatePath(sourceEntityId, targetEntityId);
				if(intermediatePath.equals(""))
				{
					intermediatePath = tempPath;
				}
				else
				{
					intermediatePath = intermediatePath+"_"+tempPath;
				}
				
				sourceEntity = targetEntity;
				sourceEntityId = targetEntityId;
			}
			
			sql = "insert into path values("+nextIdPath+","+mainEntityId+",'"+intermediatePath+"',"+targetEntityId+")";
			UpdateMetadataUtil.executeInsertSQL(sql, connection.createStatement());
			nextIdPath++;
		}
	}
	
	private String getIntermediatePath(int sourceEntityId, int intermediateEntityId) throws SQLException
	{
		String inetrmediatePath = null;
		ResultSet rs;
		Statement stmt = connection.createStatement();
		String sql = "select INTERMEDIATE_PATH from path where FIRST_ENTITY_ID ='"+sourceEntityId+"'  and LAST_ENTITY_ID = '"+intermediateEntityId+"'";
		rs = stmt.executeQuery(sql);
		if(rs.next())
		{
			inetrmediatePath = rs.getString(1);
		}
		stmt.close();
		return inetrmediatePath;
	}

	private void populateMapForPath()
	{
		entityList = new ArrayList<String>();
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport");
		
		entityList.add("edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.Participant");
		
		entityList.add("edu.wustl.catissuecore.domain.StorageContainer,edu.wustl.catissuecore.domain.SpecimenPosition,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.StorageContainer,edu.wustl.catissuecore.domain.SpecimenPosition,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.StorageContainer,edu.wustl.catissuecore.domain.SpecimenPosition,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.StorageContainer,edu.wustl.catissuecore.domain.SpecimenPosition,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.StorageContainer,edu.wustl.catissuecore.domain.SpecimenPosition,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.Site,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.Site,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Site,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Site,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.Site,edu.wustl.catissuecore.domain.SpecimenCollectionGroup,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.OrderDetails,edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderDetails,edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderDetails,edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderDetails,edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderDetails,edu.wustl.catissuecore.domain.Distribution,edu.wustl.catissuecore.domain.TissueSpecimen");
		
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolRegistration,edu.wustl.catissuecore.domain.Participant");
		
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolEvent,edu.wustl.catissuecore.domain.SpecimenRequirement");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolEvent,edu.wustl.catissuecore.domain.CellSpecimenRequirement");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolEvent,edu.wustl.catissuecore.domain.FluidSpecimenRequirement");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolEvent,edu.wustl.catissuecore.domain.MolecularSpecimenRequirement");
		entityList.add("edu.wustl.catissuecore.domain.CollectionProtocol,edu.wustl.catissuecore.domain.CollectionProtocolEvent,edu.wustl.catissuecore.domain.TissueSpecimenRequirement");
		
		entityList.add("edu.wustl.catissuecore.domain.OrderItem,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.Specimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderItem,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.CellSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderItem,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.FluidSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderItem,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.MolecularSpecimen");
		entityList.add("edu.wustl.catissuecore.domain.OrderItem,edu.wustl.catissuecore.domain.DistributedItem,edu.wustl.catissuecore.domain.TissueSpecimen");
	}
	
	public AddCuratedPath(Connection connection)
	{
		super();
		this.connection = connection;
	}	
}
