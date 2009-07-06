
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This class  contains the default  implementation for Specimen Barcode generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * logger object
	 */
	private static final transient Logger logger = Logger.getCommonLogger(DefaultSpecimenBarcodeGenerator.class);
	/**
	 * Current Barcode.
	 */
	protected Long currentBarcode;
	
	/**
	 * Default Constructor.
	 */
	public DefaultSpecimenBarcodeGenerator() throws ApplicationException
	{
		super();
		init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init() throws ApplicationException
	{
		String databaseConstant = null;
		try
		{
			if (Constants.ORACLE_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getDataBaseType()))
			{
				databaseConstant = Constants.ORACLE_MAX_BARCODE_COL;
			}
			else if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance()
					.getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType()))
			{
				databaseConstant = Constants.MSSQLSERVER_MAX_BARCODE_COL;
			}
			else
			{
				databaseConstant = Constants.MYSQL_MAX_BARCODE_COL;
			}

			StringBuffer sql = new StringBuffer("select MAX(" + databaseConstant
					+ ") from CATISSUE_SPECIMEN");
			// Modify query for mssqlserver DB.
			if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
					Constants.APPLICATION_NAME).getDataBaseType()))
			{
				sql.append(Constants.MSSQLSERVER_QRY_DT_CONVERSION_FOR_BARCODE_APPEND_STR);
			}
			currentBarcode = AppUtility.getLastAvailableValue(sql.toString());
		}
		catch (ApplicationException ex)
		{
			logger.debug(ex.getMessage(), ex);
			throw new ApplicationException(ex.getErrorKey(),ex,ex.getMessage());
		}
	}

	/**
	 * @param parentObject parent sp obj
	 * @param specimenObject sp obj
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		String parentSpecimenBarcode = (String) ((Specimen) parentObject).getBarcode();
		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		aliquotCount = returnAliquotCount(parentObject, aliquotCount);
		if (parentSpecimenBarcode != null)
		{
			specimenObject.setBarcode(parentSpecimenBarcode + "_" + (aliquotCount));
		}
	}

	/**
	 * @param parentObject Parent Object
	 * @param aliquotCount aliquot count
	 * @return aliquotCount
	 */
	protected long returnAliquotCount(AbstractSpecimen parentObject, long aliquotCount)
	{
		Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			Specimen spec = (Specimen) itr.next();
			if (spec.getLineage().equals(Constants.DERIVED_SPECIMEN) || spec.getBarcode() == null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		return aliquotCount;
	}

	/**
	 * @param parentObject Parent Sp object
	 * @param specimenObject Specimen obj
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		currentBarcode = currentBarcode + 1;
		specimenObject.setBarcode(currentBarcode.toString());
	}

	/**
	 * Setting barcode.
	 * @param obj Specimen object
	 */
	public synchronized void setBarcode(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			currentBarcode = currentBarcode + 1;
			objSpecimen.setBarcode(currentBarcode.toString());
		}
		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			setNextAvailableAliquotSpecimenBarcode(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			setNextAvailableDeriveSpecimenBarcode(objSpecimen.getParentSpecimen(), objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection<AbstractSpecimen> specimenCollection = objSpecimen
					.getChildSpecimenCollection();
			Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setBarcode(objChildSpecimen);
			}
		}

	}

	/**
	 * Setting barcode.
	 * @param objSpecimenList Specimen object list
	 */
	public synchronized void setBarcode(List objSpecimenList)
	{

		List<Specimen> specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			Specimen objSpecimen = (Specimen) specimenList.get(index);
			setBarcode(objSpecimen);
		}
	}
}