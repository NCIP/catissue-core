
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
 * This  class which contains the default  implementation for Specimen label generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenLabelGenerator implements LabelGenerator
{

	/**
	 * Logger object.
	 */
	private static final transient Logger LOGGER = Logger
			.getCommonLogger(DefaultSpecimenLabelGenerator.class);
	/**
	 * Current label.
	 */
	protected Long currentLabel;

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSpecimenLabelGenerator() throws ApplicationException
	{

		this.init();
		// TODO Auto-generated catch block
	}

	/**
	 * This is a init() function it is called from the
	 * default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
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
			databaseConstant = Constants.ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION;
		}
		else if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance()
				.getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType()))
		{
			// Modify query for mssqlserver DB.
			databaseConstant = Constants.MSSQLSERVER_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION;
		}
		else
		{
			databaseConstant = Constants.MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION;
		}

		final StringBuffer sql = new StringBuffer("select MAX(" + databaseConstant
				+ ") from CATISSUE_SPECIMEN");
		if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(
				Constants.APPLICATION_NAME).getDataBaseType()))
		{
			sql.append(Constants.MSSQLSERVER_QRY_DT_CONVERSION_FOR_LABEL_APPEND_STR);
		}
		this.currentLabel = AppUtility.getLastAvailableValue(sql.toString());

	}

	/**
	 * @param parentObject parent object
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		final String parentSpecimenLabel = parentObject.getLabel();
		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		aliquotCount = this.aliquotCount(parentObject, aliquotCount);
		specimenObject.setLabel(parentSpecimenLabel + "_" + (aliquotCount));
	}

	/**
	 * @param parentObject parent object
	 * @param aliquotCount aliquot count
	 * @return aliquotCount
	 */
	protected long aliquotCount(AbstractSpecimen parentObject, long aliquotCount)
	{
		final Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			final Specimen spec = (Specimen) itr.next();
			if (spec.getLineage().equals(Constants.DERIVED_SPECIMEN) || spec.getLabel() == null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		return aliquotCount;
	}

	/**
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableDeriveSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		this.currentLabel = this.currentLabel + 1;
		specimenObject.setLabel(this.currentLabel.toString());
	}

	/**
	 * Setting label.
	 * @param obj Specimen object
	 */
	public synchronized void setLabel(Object obj)
	{

		final Specimen objSpecimen = (Specimen) obj;
		final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
		if (objSpecimen.getLabel() != null)
		{
			return;
		}

		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			this.currentLabel++;
			objSpecimen.setLabel(this.currentLabel.toString());
		}
		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			this.setNextAvailableAliquotSpecimenlabel(parentSpecimen, objSpecimen);
		}
		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			this.setNextAvailableDeriveSpecimenlabel(parentSpecimen, objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			final Collection<AbstractSpecimen> specimenCollection = objSpecimen
					.getChildSpecimenCollection();
			final Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				final Specimen objChildSpecimen = (Specimen) it.next();
				this.setLabel(objChildSpecimen);
			}
		}
	}

	/**
	 * Setting Label.
	 * @param objSpecimenList Specimen object list
	 */
	public synchronized void setLabel(List objSpecimenList)
	{

		final List<Specimen> specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			final Specimen objSpecimen = specimenList.get(index);
			this.setLabel(objSpecimen);
		}

	}

	/**
	 * Returns label for the given domain object.
	 * @param obj Specimen obj
	 * @return label
	 */
	public String getLabel(Object obj)
	{
		final Specimen objSpecimen = (Specimen) obj;
		this.setLabel(objSpecimen);
		return (objSpecimen.getLabel());
	}
}