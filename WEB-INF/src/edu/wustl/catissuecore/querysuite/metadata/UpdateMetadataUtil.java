package edu.wustl.catissuecore.querysuite.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class UpdateMetadataUtil
{
	public static BufferedWriter metadataSQLFile;
	public static String fileName="./UpdateMetadataReport/metadata.sql";
	public static BufferedWriter failureWriter;
	public static String errorFileName="./UpdateMetadataReport/SQLerror.txt";
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
			System.out.println(sql+";");
			metadataSQLFile.write(sql+";\n");
			if(isExecuteStatement)
			{
				stmt.executeUpdate(sql);
			}
		}
		catch (SQLException e)
		{
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
					System.out.println(sql+";");
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
	
}
