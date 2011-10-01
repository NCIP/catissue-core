
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This class  contains the default  implementation for Specimen Barcode generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenBarcodeGenerator implements BarcodeGenerator
{

	/**
	 * Current Barcode.
	 */
	protected Long currentBarcode;

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSpecimenBarcodeGenerator() throws ApplicationException
	{
		super();
		this.init();
	}

	/**
	 * This is a init() function it is called from the default constructor of Base class.
	 * When getInstance of base class called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		String databaseConstant = null;

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

		final StringBuffer sql = new StringBuffer("select MAX(" + databaseConstant
				+ ") from CATISSUE_SPECIMEN");
		// Modify query for mssqlserver DB.
		if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
				Constants.APPLICATION_NAME).getDataBaseType()))
		{
			sql.append(Constants.MSSQLSERVER_QRY_DT_CONVERSION_FOR_BARCODE_APPEND_STR);
		}
		this.currentBarcode = AppUtility.getLastAvailableValue(sql.toString());

	}

	/**
	 * @param parentObject parent sp obj
	 * @param specimenObject sp obj
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(AbstractSpecimen parentObject,
			Specimen specimenObject)
	{
		final String parentSpecimenBarcode = ((Specimen) parentObject).getBarcode();
		Collection childSpecimenColl=parentObject.getChildSpecimenCollection();
		if(childSpecimenColl!=null)
		{
			long aliquotCount = childSpecimenColl.size();
			aliquotCount = this.returnAliquotCount(parentObject, aliquotCount);
			if (parentSpecimenBarcode != null)
			{
				specimenObject.setBarcode(AppUtility.handleEmptyStrings(parentSpecimenBarcode + "_" + (aliquotCount)));
			}
		}
	}

	/**
	 * @param parentObject Parent Object
	 * @param aliquotCount aliquot count
	 * @return aliquotCount
	 */
	protected long returnAliquotCount(AbstractSpecimen parentObject, long aliquotCount)
	{
		Collection childSpecimenColl=parentObject.getChildSpecimenCollection();
		if(childSpecimenColl!=null)
		{
			final Iterator<AbstractSpecimen> itr = childSpecimenColl.iterator();
			while (itr.hasNext())
			{
				final Specimen spec = (Specimen) itr.next();
				if (spec.getLineage().equals(Constants.DERIVED_SPECIMEN) || spec.getBarcode() == null)
				{
					aliquotCount--;
				}
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
		this.currentBarcode = this.currentBarcode + 1;
		specimenObject.setBarcode(AppUtility.handleEmptyStrings(this.currentBarcode.toString()));
	}

	/**
	 * Setting barcode.
	 * @param obj Specimen object
	 */
	public synchronized void setBarcode(Object obj)
	{
		final Specimen objSpecimen = (Specimen) obj;
		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			String barcode = this.getBarcode(objSpecimen);
			if(!Validator.isEmpty(barcode)) {
				objSpecimen.setBarcode(AppUtility.handleEmptyStrings(barcode));
			}
		}
		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			this.setNextAvailableAliquotSpecimenBarcode(objSpecimen.getParentSpecimen(),
					objSpecimen);
		}

		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			this
			.setNextAvailableDeriveSpecimenBarcode(objSpecimen.getParentSpecimen(),
					objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection()!=null && objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			final Collection<AbstractSpecimen> specimenCollection = objSpecimen
			.getChildSpecimenCollection();
			final Iterator<AbstractSpecimen> specCollItr = specimenCollection.iterator();
			while (specCollItr.hasNext())
			{
				final Specimen objChildSpecimen = (Specimen) specCollItr.next();
				this.setBarcode(objChildSpecimen);
			}
		}

	}

	/**
	 * This method will be called to get the barcode.
	 * @return
	 */
	protected String getBarcode(Specimen objSpecimen)
	{
		this.currentBarcode = this.currentBarcode + 1;
		return this.currentBarcode.toString();
	}

	/**
	 * Setting barcode.
	 * @param objSpecimenList Specimen object list
	 */
	public synchronized void setBarcode(List objSpecimenList)
	{

		final List<Specimen> specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			final Specimen objSpecimen = specimenList.get(index);
			this.setBarcode(objSpecimen);
		}
	}
}