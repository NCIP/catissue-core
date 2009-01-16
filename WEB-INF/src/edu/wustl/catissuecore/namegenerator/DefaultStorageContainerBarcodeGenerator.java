
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
 * This  class which contains the default StorageContainer barcode implementation.
 * @author falguni_sachde
 *
 */
public class DefaultStorageContainerBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * Current label.
	 */
	protected Long currentBarcode;
	/**
	 * Datasource Name.
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";

	/**
	 * Default Constructor.
	 */
	public DefaultStorageContainerBarcodeGenerator()
	{
		super();
		init();
	}

	/**
	 * This is a init() function it is called from the
	 * default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		Connection conn = null;
		try
		{

			currentBarcode = new Long(0);
			String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_STORAGE_CONTAINER";
			conn = getConnection();
			ResultSet resultSet = conn.createStatement().executeQuery(sql);

			if (resultSet.next())
			{
				currentBarcode = new Long(resultSet.getLong(1));
			}
		}
		catch (Exception daoException)
		{
			daoException.printStackTrace();

		}
		finally
		{
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (SQLException exception)
				{

					exception.printStackTrace();
				}
			}
		}

	}

	/**
	 * @return connection
	 * @throws NamingException NamingException
	 * @throws SQLException SQLException
	 */
	private Connection getConnection() throws NamingException, SQLException
	{
		Connection conn;
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(DATASOURCE_JNDI_NAME);
		conn = ds.getConnection();
		return conn;
	}

	/**
	 * Set barcode.
	 * @param obj SC obj
	 */
	public void setBarcode(Object obj)
	{
		StorageContainer objStorageContainer = (StorageContainer) obj;
		//TODO :Write a logic to generate barcode.
		String barcode = "";
		objStorageContainer.setBarcode(barcode);

	}

	/**
	 * Set barcode.
	 * @param storageContainerList SC objList
	 */
	public void setBarcode(List storageContainerList)
	{

		for (int i = 0; i < storageContainerList.size(); i++)
		{
			StorageContainer objStorageContainer = (StorageContainer) storageContainerList.get(i);
			setBarcode(objStorageContainer);

		}

	}
}
