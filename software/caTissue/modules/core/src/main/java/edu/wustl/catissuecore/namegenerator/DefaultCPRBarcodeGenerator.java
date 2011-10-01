
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;

/**
 * This  class which contains the default CPR barcode implementation.
 * @author vijay_pande
 *
 */
public class DefaultCPRBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * Current barcode.
	 */
	protected Long currentBarcode;

	/**
	 * Data source Name.
	 */
	//String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultCPRBarcodeGenerator() throws ApplicationException
	{
		super();
		this.init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Database Name and then set function name that will convert
	 * barcode from integer to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{

		this.currentBarcode = Long.valueOf(0);
		final String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_COLL_PROT_REG";
		this.currentBarcode = AppUtility.getLastAvailableValue(sql);

	}

	/**
	 * Setting Barcode.
	 * @param obj CPR object
	 */
	public void setBarcode(Object obj)
	{
		final CollectionProtocolRegistration objCPR = (CollectionProtocolRegistration) obj;
		//TODO :Write a logic to generate barcode.
		final String barcode = "";
		objCPR.setBarcode(AppUtility.handleEmptyStrings(barcode));
	}

	/**
	 * Setting barcode.
	 * @param cprList CPR object list.
	 */
	public void setBarcode(List<Object> cprList)
	{
		for (int i = 0; i < cprList.size(); i++)
		{
			final CollectionProtocolRegistration objCPR = (CollectionProtocolRegistration) cprList
					.get(i);
			this.setBarcode(objCPR);
		}
	}
}
