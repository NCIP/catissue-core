package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;


/**
 * This  class which contains the default SCG barcode implementation.
 * @author vijay_pande
 *
 */
public class DefaultSCGBarcodeGenerator implements BarcodeGenerator
{
	/**
	 * Current barcode
	 */
	protected Long currentBarcode;
	/**
	 * Datasource Name
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor
	 */
	public DefaultSCGBarcodeGenerator()
	{
		super();
		init();
	}
	
	/**
	 * This is a init() function it is called from the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * barcode from int to String
	 */
	protected void init()
	{
		Connection conn = null;
		try 
		{
			
			currentBarcode = new Long(0);
			String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_SPECIMEN_COLL_GROUP";
			conn = getConnection();
			ResultSet resultSet = conn.createStatement().executeQuery(sql);
			
			if(resultSet.next())
				currentBarcode = new Long (resultSet.getLong(1));
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
	private Connection getConnection() throws NamingException, SQLException 
	{
		Connection conn;
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
		conn = ds.getConnection();
		return conn;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.BarcodeGenerator#setBarcode(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setBarcode(Object obj )
	{
		SpecimenCollectionGroup objSpecimenCollectionGroup = (SpecimenCollectionGroup)obj;
		//TODO :Write a logic to generate barcode.
		String barcode = "";
		objSpecimenCollectionGroup.setBarcode(barcode);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.BarcodeGenerator#setBarcode(java.util.List)
	 */
	public void setBarcode(List specimenCollectionGroupList) {
		
		for(int i=0; i< specimenCollectionGroupList.size(); i++)
		{
			SpecimenCollectionGroup objSpecimenCollectionGroup = (SpecimenCollectionGroup)specimenCollectionGroupList.get(i);
			setBarcode(objSpecimenCollectionGroup);
			
		}	
	}	
}
