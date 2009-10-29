
package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 * This  class which contains the default  implementation for Specimen label generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenLabelGeneratorForWashu extends DefaultSpecimenLabelGenerator
{

	/**
	 * Logger object.
	 */
	private static final transient Logger LOGGER = Logger
			.getCommonLogger(DefaultSpecimenLabelGeneratorForWashu.class);
	/**
	 * Current label.
	 */
	protected Long currentLabel;
	/**
	 * temp label.
	 */
	protected Long tmpLabel;

	/**
	 * count.
	 */
	protected long count = 0;

	/**
	 * Default Constructor.
	 * @throws ApplicationException Application Exception
	 */
	public DefaultSpecimenLabelGeneratorForWashu() throws ApplicationException
	{
		try
		{
			this.init();
			final String sql = "select MAX(identifier) from CATISSUE_SPECIMEN";
			this.tmpLabel = AppUtility.getLastAvailableValue(sql);
		}
		catch (final Exception ex)
		{
			DefaultSpecimenLabelGeneratorForWashu.LOGGER.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Map for Tree base specimen entry.
	 */
	Map<Object, Long> labelCountTreeMap = new HashMap<Object, Long>();
	/**
	 * Temp for label.
	 */
	Map<Object, Long> tmpLabelCountTreeMap = new HashMap<Object, Long>();

	/**
	 * @param parentObject parent sp obj
	 * @param specimenObject sp obj
	 * @param prefix prefix
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject, String prefix)
	{

		if (Constants.COLLECTION_STATUS_COLLECTED.equals(specimenObject.getCollectionStatus()))
		{
			this.labelCountTreeMap = this.setAliquotLabel(parentObject, specimenObject, prefix,
					this.labelCountTreeMap);
		}
		else
		{
			this.tmpLabelCountTreeMap = this.setAliquotLabel(parentObject, specimenObject, prefix,
					this.tmpLabelCountTreeMap);
		}
	}

	/**
	 * @param parentObject parent specimen object
	 * @param specimenObject specimen object
	 * @param prefix prefix
	 * @param labelTreeMap map that stores count
	 * @return  labelTreeMap
	 */
	private Map<Object, Long> setAliquotLabel(Specimen parentObject, Specimen specimenObject,
			String prefix, Map<Object, Long> labelTreeMap)
	{
		final String parentSpecimenLabel = parentObject.getLabel();
		long aliquotChildCount = 0;

		if (labelTreeMap.containsKey(parentObject.getLabel()))
		{
			aliquotChildCount = Long
					.parseLong(labelTreeMap.get(parentObject.getLabel()).toString());
		}
		else
		{
			aliquotChildCount = parentObject.getChildSpecimenCollection().size();
		}

		//	specimenObject.setLabel(prefix + parentSpecimenLabel + "_" + (++aliquotChildCount) );
		specimenObject.setLabel(parentSpecimenLabel + "_" + (++aliquotChildCount));
		labelTreeMap.put(parentObject.getLabel(), aliquotChildCount);

		return labelTreeMap;
	}

	/**
	 * @param parentObject parent obj
	 * @param specimenObject sp obj
	 * @param labelCtr  label
	 * @param prefix prefix
	 * @return labelCtr
	 */
	synchronized Long setNextAvailableDeriveSpecimenlabel(Specimen parentObject,
			Specimen specimenObject, Long labelCtr, String prefix)
	{
		final Long labelControl = labelCtr + 1;

		if ((Constants.COLLECTION_STATUS_COLLECTED).equals(specimenObject.getCollectionStatus()))
		{
			specimenObject.setLabel(labelControl.toString());
		}
		else
		{
			specimenObject.setLabel(prefix + "_" + labelControl.toString());
		}

		this.labelCountTreeMap.put(specimenObject, 0L);
		return labelControl;
	}

	/**
	 * Setting Label.
	 * @param obj Specimen object list
	 */
	@Override
	public synchronized void setLabel(Object obj)
	{
		final Specimen objSpecimen = (Specimen) obj;
		if ((Constants.COLLECTION_STATUS_COLLECTED).equals(objSpecimen.getCollectionStatus()))
		{
			this.tmpLabel = this.generateLabel(objSpecimen, this.tmpLabel, "");
		}
		else
		{
			if (objSpecimen.getLabel() == null)
			{
				this.tmpLabel = this.generateLabel(objSpecimen, this.tmpLabel, objSpecimen
						.getSpecimenType());
			}
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
	 * @param objSpecimen sp obj.
	 * @param labelCtr label
	 * @param prefix prefix
	 * @return labelCtr
	 */
	private Long generateLabel(Specimen objSpecimen, Long labelCtr, String prefix)
	{
		if (!this.labelCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			if (objSpecimen.getLabel() == null)
			{
				labelCtr = labelCtr + 1;
				if ((Constants.COLLECTION_STATUS_COLLECTED).equals(objSpecimen
						.getCollectionStatus()))
				{
					objSpecimen.setLabel(labelCtr.toString());
				}
				else
				{
					objSpecimen.setLabel(prefix + "_" + labelCtr.toString());
				}
			}

			this.labelCountTreeMap.put(objSpecimen.getLabel(), 0L);
			this.tmpLabelCountTreeMap.put(objSpecimen.getLabel(), 0L);
		}

		else if (!this.labelCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			this.setNextAvailableAliquotSpecimenlabel((Specimen) objSpecimen.getParentSpecimen(),
					objSpecimen, prefix);
		}

		else if (!this.labelCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			if (objSpecimen.getLabel() == null)
			{
				labelCtr = this.setNextAvailableDeriveSpecimenlabel((Specimen) objSpecimen
						.getParentSpecimen(), objSpecimen, labelCtr, prefix);
			}
		}

		return labelCtr;
	}

	/**
	 * Setting Label.
	 * @param objSpecimenList Specimen object list
	 */
	@Override
	public synchronized void setLabel(List objSpecimenList)
	{

		final List specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			final Specimen objSpecimen = (Specimen) specimenList.get(index);
			this.setLabel(objSpecimen);
		}

	}

	/**
	 *	Returns label for the given domain object.
	 * @param obj Specimen object
	 * @return label
	 */
	@Override
	public String getLabel(Object obj)
	{
		final Specimen objSpecimen = (Specimen) obj;
		this.setLabel(objSpecimen);

		return (objSpecimen.getLabel());
	}
}