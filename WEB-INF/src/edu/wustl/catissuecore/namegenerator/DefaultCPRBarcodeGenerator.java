package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;


/**
 * This  class which contains the default CPR barcode implementation.
 * @author vijay_pande
 *
 */
public class DefaultCPRBarcodeGenerator implements BarcodeGenerator
{
	/**
	 * Creating Logger instance.
	 */
	private static final  Logger logger =Logger.getCommonLogger(DefaultCPRBarcodeGenerator.class);

	/**
	 * Current barcode.
	 */
	protected Long currentBarcode;

	/**
	 * Datasource Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor.
	 * @throws ApplicationException 
	 */
	public DefaultCPRBarcodeGenerator() throws ApplicationException
	{
		super();
		init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * barcode from int to String
	 */
	protected void init() throws ApplicationException
	{
		try
		{
			currentBarcode = Long.valueOf(0);
			String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_COLL_PROT_REG";
			currentBarcode=AppUtility.getLastAvailableValue(sql);
		}
		catch (Exception daoException)
		{
			logger.error(daoException.getMessage(), daoException);
		}
	}


	
	/**
	 * Setting Barcode.
	 * @param obj CPR object
	 */
	public void setBarcode(Object obj)
	{
		CollectionProtocolRegistration objCPR= (CollectionProtocolRegistration)obj;
		//TODO :Write a logic to generate barcode.
		String barcode = "";
		objCPR.setBarcode(barcode);
	}

	/**
	 * Setting barcode.
	 * @param cprList CPR obj list.
	 */
	public void setBarcode(List<Object> cprList)
	{
		for(int i=0; i< cprList.size(); i++)
		{
			CollectionProtocolRegistration objCPR = (CollectionProtocolRegistration)cprList.get(i);
			setBarcode(objCPR);
		}
	}
}
