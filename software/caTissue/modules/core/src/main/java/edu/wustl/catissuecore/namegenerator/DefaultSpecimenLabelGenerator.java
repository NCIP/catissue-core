
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.daofactory.DAOConfigFactory;

// TODO: Auto-generated Javadoc
/**
 * This  class which contains the default  implementation for Specimen label generation.
 *
 * @author falguni_sachde
 */
public class DefaultSpecimenLabelGenerator implements LabelGenerator
{

	/** Current label. */
	protected transient Long currentLabel;

	/**
	 * Default Constructor.
	 *
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
	 *
	 * @throws ApplicationException Application Exception
	 */
	protected void init() throws ApplicationException
	{
		String databaseConstant;

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
	 * Sets the next available aliquot specimenlabel.
	 *
	 * @param parentObject parent object
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		final String parentSpecimenLabel = parentObject.getLabel();
		Collection childCollection=parentObject.getChildSpecimenCollection();
		if(childCollection!=null)
		{
			long aliquotCount = childCollection.size();
			aliquotCount = this.aliquotCount(parentObject, aliquotCount);
			specimenObject.setLabel(parentSpecimenLabel + "_" + (aliquotCount));
		}
	}

	/**
	 * Aliquot count.
	 *
	 * @param parentObject parent object
	 * @param aliquotCount aliquot count
	 *
	 * @return aliquotCount
	 */
	protected long aliquotCount(AbstractSpecimen parentObject, long aliquotCount)
	{
		long count = aliquotCount;
		Collection<AbstractSpecimen> childSpecimens = parentObject.getChildSpecimenCollection();
		if(childSpecimens!=null)
		{
			for (AbstractSpecimen spec : childSpecimens)
			{
				if (spec.getLineage().equals(Constants.DERIVED_SPECIMEN) || spec.getLabel() == null)
				{
					count--;
				}
			}
			count++;
		}
		return count;
	}

	/**
	 * Sets the next available derive specimenlabel.
	 *
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
	 *
	 * @param obj Specimen object
	 *
	 * @throws LabelGenException the label gen exception
	 */
	public synchronized void setLabel(Object obj) throws LabelGenException
	{

		final Specimen objSpecimen = (Specimen) obj;
		final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
		if (objSpecimen.getLabel() != null)
		{
			return;
		}

		if(iscollStatusPending(objSpecimen))
		{
			throw new LabelGenException("Specimen status is not "+Constants.COLLECTION_STATUS_COLLECTED);
		}

		setSpecimenLabel(objSpecimen, parentSpecimen);

		if (objSpecimen.getChildSpecimenCollection()!=null && objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			final Collection<AbstractSpecimen> specimenCollection = objSpecimen
			.getChildSpecimenCollection();
			final Iterator<AbstractSpecimen> specCollItr = specimenCollection.iterator();
			while (specCollItr.hasNext())
			{
				final Specimen objChildSpecimen = (Specimen) specCollItr.next();
				this.setLabel(objChildSpecimen);
			}
		}
	}

	/**
	 * Iscoll status pending.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if iscoll status pending
	 */
	private boolean iscollStatusPending(final Specimen objSpecimen)
	{
		return objSpecimen.getCollectionStatus() == null || !Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus());
	}

	/**
	 * Sets the specimen label.
	 *
	 * @param objSpecimen the obj specimen
	 * @param parentSpecimen the parent specimen
	 */
	private void setSpecimenLabel(final Specimen objSpecimen, final Specimen parentSpecimen)
	{
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
	}

	/**
	 * Setting Label.
	 *
	 * @param objSpecimenList Specimen object list
	 *
	 * @throws LabelGenException the label gen exception
	 */
	public synchronized void setLabel(List objSpecimenList) throws LabelGenException
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
	 *
	 * @param obj Specimen obj
	 *
	 * @return label
	 *
	 * @throws LabelGenException the label gen exception
	 */
	public String getLabel(Object obj) throws LabelGenException
	{
		final Specimen objSpecimen = (Specimen) obj;
		this.setLabel(objSpecimen);
		return objSpecimen.getLabel();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.Collection)
	 */
	public void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		Iterator<AbstractDomainObject> iterator = object.iterator();
		while (iterator.hasNext())
		{
			final Specimen newSpecimen = (Specimen) iterator.next();
			this.setLabel(newSpecimen);
		}
	}

	public void setLabel(Object object, boolean ignoreCollectedStatus) throws LabelGenException
	{
		// TODO Auto-generated method stub

	}
}