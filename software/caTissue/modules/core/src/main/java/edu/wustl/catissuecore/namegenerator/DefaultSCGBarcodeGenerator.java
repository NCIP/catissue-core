
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This  class which contains the default SCG barcode implementation.
 * @author vijay_pande
 *
 */
public class DefaultSCGBarcodeGenerator implements BarcodeGenerator
{
	/**
	 * Logger instance.
	 */
	private transient final Logger logger
		= Logger.getCommonLogger(DefaultSCGBarcodeGenerator.class);
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
	 */
	public DefaultSCGBarcodeGenerator()
	{
		super();
		try
		{

			this.init();
		}
		catch (final ApplicationException e)
		{
			this.logger.error(e.getMessage(),e) ;
		}

	}

	/**
	 * This is a init() function it is called from
	 * the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * barcode from int to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		this.currentBarcode = new Long(0);
		final String sql = "select max(IDENTIFIER) as MAX_NAME from CATISSUE_SPECIMEN_COLL_GROUP";
		this.currentBarcode = AppUtility.getLastAvailableValue(sql);
	}

	/**
	 * Setting barcode.
	 * @param obj SCG object
	 */
	public void setBarcode(Object obj)
	{
		final SpecimenCollectionGroup objSpecimenCollectionGroup = (SpecimenCollectionGroup) obj;
		//TODO :Write a logic to generate barcode.
		objSpecimenCollectionGroup.setBarcode(null);
	}

	/**
	 * Setting barcode.
	 * @param scgList SCG object list
	 */
	public void setBarcode(List<Object> scgList)
	{
		for (int i = 0; i < scgList.size(); i++)
		{
			final SpecimenCollectionGroup scgObj = (SpecimenCollectionGroup) scgList.get(i);
			this.setBarcode(scgObj);
		}
	}
}
