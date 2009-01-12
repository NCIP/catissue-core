package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.StorageContainer;


/**
 * This class contains the default  Storage container label  implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerLabelGenerator implements LabelGenerator
{
	/**
	 * Current label 
	 */
	protected Long currentLabel ;
	/**
	 * Datasource Name
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor
	 */
	public DefaultStorageContainerLabelGenerator()
	{
		super();
		init();
	}
	
	/**
	 * This is a init() function it is called from the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		Connection conn = null;
		try 
		{
			
			currentLabel = new Long(0);
			String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
			conn = getConnection();
			ResultSet resultSet = conn.createStatement().executeQuery(sql);
			
			if(resultSet.next())
			currentLabel = new Long (resultSet.getLong(1));
		}
		catch (Exception daoException) 
		{
			daoException.printStackTrace();
			
		} finally
		{
			if (conn != null)
			{
				try {
					conn.close();
				} catch (SQLException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * @return connection
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Connection getConnection() throws NamingException, SQLException {
		Connection conn;
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
		conn = ds.getConnection();
		return conn;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setLabel(Object obj )
	{
		StorageContainer objStorageContainer = (StorageContainer)obj;
		currentLabel= currentLabel+1;
		String containerName = "";
		String maxSiteName = objStorageContainer.getSite().getName();
		String maxTypeName =  objStorageContainer.getStorageType().getName();
		if (maxSiteName.length() > 40)
		{
			maxSiteName = maxSiteName.substring(0, 39);
		}
		if (maxTypeName.length() > 40)
		{
			maxTypeName = maxTypeName.substring(0, 39);
		}

		containerName = maxSiteName + "_" + maxTypeName + "_" + String.valueOf(currentLabel);
		
		objStorageContainer.setName(containerName);	
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.List)
	 */
	public void setLabel(List storageContainerList) {
		
		for(int i=0; i< storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer)storageContainerList.get(i);
			setLabel(objStorageContainer);
			
		}	
		
	}	

	/**
	 * Returns label for the given domain object
	 */
	public String getLabel(Object obj) 
	{
		StorageContainer objStorageContainer = (StorageContainer)obj;
		setLabel(objStorageContainer);
		
		return (objStorageContainer.getName());
	}	
}
