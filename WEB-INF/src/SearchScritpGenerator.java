import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.util.global.HibernateProperties;
import edu.wustl.common.util.dbManager.DAOException;

/*
 * Created on Sep 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SearchScritpGenerator
{
	int colIndex = 1;
	BufferedWriter writer;
	private Connection connection = null;
	public SearchScritpGenerator()throws Exception
	{
		HibernateProperties.initBundle("hibernate.properties");
		Class.forName(HibernateProperties.getValue("hibernate.connection.driver_class"));
		
		String database = HibernateProperties
								.getValue("hibernate.connection.url");
		String loginName = HibernateProperties
		        				.getValue("hibernate.connection.username");
		String password = HibernateProperties
								.getValue("hibernate.connection.password");
		
		//Creates a connection.
		connection = DriverManager.getConnection(database, loginName, password);
		
		writer = new BufferedWriter(new FileWriter("QueryData-Out.txt"));
	}
	
	
	public void update() throws Exception
	{
		DatabaseMetaData aDatabaseMetaData = connection.getMetaData();
		String type[] = {"TABLE"};
		
		ResultSet rs = aDatabaseMetaData.getTables(null, "catissucore","" , type);

		List tableNameList = getTableNameList(rs);
		System.out.println(tableNameList);
		
		int index = 1;
		Iterator it = tableNameList.iterator();
		while(it.hasNext())
		{
			String tableName = (String)it.next();
			generateTableScript(tableName, index);
			
			List colList = getColumnNameList(tableName);
			generateColScript(colList, index);
			index++;
			//System.out.println(colNameList);
		}
		
		writer.close();
	}
	
	private void generateTableScript(String tableName, int index) throws Exception
	{
		String names[] = generateDisplayName(tableName, true);
		StringBuffer buff = new StringBuffer("insert into CATISSUE_QUERY_INTERFACE_TABLE_DATA  ( TABLE_ID, TABLE_NAME, DISPLAY_NAME, ALIAS_NAME) values ( ");
		buff.append(index+", ");
		buff.append("'"+tableName+"', ");
		buff.append("'"+names[0]+"', ");
		buff.append("'"+names[1]+"');");
		
		System.out.println(buff);
		writer.write(buff.toString()+"\n");
		
	}
	
	private void generateColScript(List colList, int tableIndex) throws Exception
	{
		Iterator it = colList.iterator();
		while(it.hasNext())
		{
			String col[] = (String[])it.next();
			
			String names[] = generateDisplayName(col[0], false);
			
			// 1, 'IDENTIFIER' , 'Storage Type Identifier' , 'bigint' );
			StringBuffer buff = new StringBuffer("insert into CATISSUE_QUERY_INTERFACE_COLUMN_DATA ( IDENTIFIER, TABLE_ID, COLUMN_NAME , DISPLAY_NAME , ATTRIBUTE_TYPE ) values ( ");
			buff.append(colIndex+", ");
			buff.append(tableIndex+", ");
			buff.append("'"+col[0]+"', ");
			buff.append("'"+names[0]+"', ");
			buff.append("'"+col[1]+"');");
			colIndex++;
			System.out.println(buff);
			writer.write(buff.toString()+"\n");
		}
	}
	
	private String[] generateDisplayName(String tableName, boolean isTable)
	{
		StringBuffer displayName = new StringBuffer();
		StringBuffer aliasName = new StringBuffer();
		
		StringTokenizer tok = new StringTokenizer(tableName,"_");
		//SkipFirst Token
		if(isTable)
			tok.nextToken();
		while(tok.hasMoreTokens())
		{
			String token = tok.nextToken().toLowerCase();
			String firstChar = (token.charAt(0)+"").toUpperCase();
			displayName.append(firstChar+token.substring(1)+" ");
			
			aliasName.append(firstChar+token.substring(1));
		}
			
		return new String[]{displayName.toString().trim(),aliasName.toString().trim()};
	}
	
	private List getTableNameList(ResultSet rs) throws Exception 
	{
		List tableNameList = new ArrayList();
		
		List list =	executeQuery(rs);
		
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			List innerList = (List)it.next();
			
			String tableName = ((String)innerList.get(2)).toUpperCase();
			
			if(tableName.startsWith("CATISSUE_"))
			{
				tableNameList.add(tableName);
			}
		}
		
		return tableNameList;
	}
	
	private List getColumnNameList(String tableName) throws Exception 
	{
		List colList = new ArrayList();
		
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("Select * from "+tableName+" where 1=2");
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int count = rsmd.getColumnCount();
		
		for (int i = 1; i <= count; i++)
		{
			String colName = rsmd.getColumnName(i);
			String colTypeName = rsmd.getColumnTypeName(i);
			
			if(colTypeName.startsWith("DATE"))
				colTypeName = "DATE";
			else if(colTypeName.startsWith("TEXT") || colTypeName.startsWith("VARCHAR"))
				colTypeName = "TEXT";
			else
				colTypeName = "NUMERIC";
			
			//System.out.println(colName+" " +colTypeName);
			
			colList.add(new String[]{colName,colTypeName});
		}
		
		return colList;
	}
	
	public List executeQuery(ResultSet resultSet) throws ClassNotFoundException, DAOException
    {
        List list = null;
        try
        {
            list = new ArrayList();
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (resultSet.next())
            {
                int i = 1;
                List aList= new ArrayList();
                while (i <= columnCount)
                {
                	if(resultSet.getObject(i) != null)
                	{
                		aList.add(new String(resultSet.getObject(i).toString()));
                	}
                	else
                	{
                	    aList.add("");
                	}
                	i++;
                }
                
                list.add(aList);
            }
        }
        catch(SQLException sqlExp)
        {
            throw new DAOException(sqlExp.getMessage(), sqlExp);
        }
        
        return list;
    }
	
	
	
	public static void main(String[] args) throws Exception
	{
		SearchScritpGenerator aSearchScritpGenerator = new SearchScritpGenerator();
		aSearchScritpGenerator.update();
	}
}
