
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
	 * Logger object 
	 */
	private static final transient Logger logger = Logger.getCommonLogger(DefaultSpecimenLabelGeneratorForWashu.class);
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
	 */
	public DefaultSpecimenLabelGeneratorForWashu() throws ApplicationException
	{
		try
		{
			init();
			String sql = "select MAX(identifier) from CATISSUE_SPECIMEN";
			tmpLabel=AppUtility.getLastAvailableValue(sql);
		}
		catch (Exception ex)
		{
			logger.debug(ex.getMessage(), ex);
		}
	}

	/**
	 * Map for Tree base specimen entry.
	 */
	Map<Object,Long> labelCountTreeMap = new HashMap<Object, Long>();
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
			labelCountTreeMap = setAliquotLabel(parentObject, specimenObject, prefix,
					labelCountTreeMap);
		}
		else
		{
			tmpLabelCountTreeMap = setAliquotLabel(parentObject, specimenObject, prefix,
					tmpLabelCountTreeMap);
		}
	}

	/**
	 * @param parentObject parent sp obj
	 * @param specimenObject sp obj
	 * @param prefix prefix
	 * @param labelTreeMap map that stores count
	 * @return  labelTreeMap
	 */
	private Map<Object, Long>setAliquotLabel(Specimen parentObject, Specimen specimenObject, String prefix,
			Map<Object, Long> labelTreeMap)
	{
		String parentSpecimenLabel = (String) parentObject.getLabel();
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
		Long labelControl=labelCtr + 1;
	
		if ((Constants.COLLECTION_STATUS_COLLECTED).equals(specimenObject.getCollectionStatus()))
		{
			specimenObject.setLabel(labelControl.toString());
		}
		else
		{
			specimenObject.setLabel(prefix + "_" + labelControl.toString());
		}

		labelCountTreeMap.put(specimenObject, 0L);
		return labelControl;
	}

	/**
	 * Setting Label.
	 * @param obj Specimen object list
	 */
	public synchronized void setLabel(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		if ((Constants.COLLECTION_STATUS_COLLECTED).equals(objSpecimen.getCollectionStatus()))
		{
			tmpLabel = generateLabel(objSpecimen, tmpLabel, "");
		}
		else
		{
			if (objSpecimen.getLabel() == null)
			{
				tmpLabel = generateLabel(objSpecimen, tmpLabel, objSpecimen.getSpecimenType());
			}
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection<AbstractSpecimen> specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setLabel(objChildSpecimen);
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
		if (!labelCountTreeMap.containsKey(objSpecimen)
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

			labelCountTreeMap.put(objSpecimen.getLabel(), 0L);
			tmpLabelCountTreeMap.put(objSpecimen.getLabel(), 0L);
		}

		else if (!labelCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			setNextAvailableAliquotSpecimenlabel((Specimen) objSpecimen.getParentSpecimen(),
					objSpecimen, prefix);
		}

		else if (!labelCountTreeMap.containsKey(objSpecimen)
				&& objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			if (objSpecimen.getLabel() == null)
			{
				labelCtr = setNextAvailableDeriveSpecimenlabel((Specimen) objSpecimen
						.getParentSpecimen(), objSpecimen, labelCtr, prefix);
			}
		}

		return labelCtr;
	}

	/**
	 * Setting Label.
	 * @param objSpecimenList Specimen object list
	 */
	public synchronized void setLabel(List objSpecimenList)
	{

		List specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			Specimen objSpecimen = (Specimen) specimenList.get(index);
			setLabel(objSpecimen);
		}

	}

	/**
	 *	Returns label for the given domain object.
	 * @param obj Specimen object
	 * @return label
	 */
	public String getLabel(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		setLabel(objSpecimen);

		return (objSpecimen.getLabel());
	}
}