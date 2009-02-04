/*
 * This class holds data for the Multiple Specimen Storage Location JSP.
 * Created on Nov 7, 2006
 * @author mandar_deshmukh
 * 
 */

package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/*
 * This class holds data for the Multiple Specimen Storage Location JSP.
 * Created on Nov 7, 2006
 * @author mandar_deshmukh
 * 
 */
public class MultipleSpecimenStorageLocationForm extends AbstractActionForm
{

	//Map to hold multiple specimens.
	private Map specimenMap = new HashMap();

	private List specimenList = new ArrayList();
	//used to display components on ui
	private Map specimenOnUIMap = new HashMap();

	private String submitForm = "";

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#getFormId()
	 */
	public int getFormId()
	{
		return Constants.MULTIPLE_SPECIMEN_STOGAGE_LOCATION_FORM_ID;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#reset()
	 */
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is to be mapped.
	 */
	public void setValue(String key, Object value)
	{
		if (isMutable())
			specimenOnUIMap.put(key, value);
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return specimenOnUIMap.get(key);
	}

	/**
	 * Default empty constructor.
	 */
	public MultipleSpecimenStorageLocationForm()
	{
		// TODO Auto-generated constructor stub
		reset();
	}

	/**
	 * @return Returns the specimenMap.
	 */
	public Map getSpecimenMap()
	{
		return specimenMap;
	}

	/**
	 * @param specimenMap The specimenMap to set.
	 */
	public void setSpecimenMap(Map specimenMap)
	{
		this.specimenMap = specimenMap;
	}

	/**
	 * @return Returns the specimenOnUIMap.
	 */
	public Map getSpecimenOnUIMap()
	{
		return specimenOnUIMap;
	}

	/**
	 * @param specimenOnUIMap The specimenOnUIMap to set.
	 */
	public void setSpecimenOnUIMap(Map specimenOnUIMap)
	{
		this.specimenOnUIMap = specimenOnUIMap;
	}

	public void populateSpecimenOnUIMap(HttpServletRequest request)
	{
		if (specimenMap != null)
		{
			Iterator specimenKeys = specimenMap.keySet().iterator();
			int cnt = 1;
			while (specimenKeys.hasNext())
			{
				Specimen specimen = (Specimen) specimenKeys.next();
				//hold order of specimen
				specimenList.add(specimen);
				String labelKey = "Specimen:" + cnt + "_Label";
				String typeKey = "Specimen:" + cnt + "_Type";
				String barKey = "Specimen:" + cnt + "_Barcode";
				String storageContainerKey = "Specimen:" + cnt + "_StorageContainer";
				String positionOneKey = "Specimen:" + cnt + "_PositionOne";
				String positionTwoKey = "Specimen:" + cnt + "_PositionTwo";

				//key for location map
				String classKey = "Specimen:" + cnt + "_ClassName";
				String collectionProtocolKey = "Specimen:" + cnt + "_CollectionProtocol";
				specimenOnUIMap.put(classKey, specimen.getClassName());
				specimenOnUIMap.put(collectionProtocolKey, specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
						.getCollectionProtocol().getId().toString());

				specimenOnUIMap.put(labelKey, specimen.getLabel());
				specimenOnUIMap.put(typeKey, specimen.getType());
				specimenOnUIMap.put(barKey, specimen.getBarcode());
				if (specimen.getStorageContainer() != null)
					specimenOnUIMap.put(storageContainerKey, specimen.getStorageContainer().getId().toString());
				if (specimen.getPositionDimensionOne() != null)
					specimenOnUIMap.put(positionOneKey, specimen.getPositionDimensionOne().toString());
				if (specimen.getPositionDimensionTwo() != null)
					specimenOnUIMap.put(positionTwoKey, specimen.getPositionDimensionTwo().toString());
				addDerivedSpecimens(specimen, cnt);
				cnt++;
			}
			String specimenCountKey = "Specimen_Count";
			specimenOnUIMap.put(specimenCountKey, new Integer(specimenMap.size()));
		}
		else
		{
			String specimenCountKey = "Specimen_Count";
			specimenOnUIMap.put(specimenCountKey, new Integer(0));
		}
		Logger.out.debug("specimenOnUIMap after population : \n------------\n" + specimenOnUIMap + "\n-------\n");
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_SPECIMEN_ORDER_LIST, specimenList);
		Logger.out.debug("\n---------------\n List set in session.\n---------------\n");
	}

	private void addDerivedSpecimens(Specimen mainSpecimen, int specimenID)
	{
		List derivedList = (List) specimenMap.get(mainSpecimen);
		String parentKey = "Specimen:" + specimenID + "_";
		if (derivedList != null && !derivedList.isEmpty())
		{
			for (int cnt = 1; cnt <= derivedList.size(); cnt++)
			{
				String derivedPrefix = parentKey + "DerivedSpecimen:";
				Specimen specimen = (Specimen) derivedList.get(cnt - 1);
				String labelKey = derivedPrefix + cnt + "_Label";
				String typeKey = derivedPrefix + cnt + "_Type";
				String barKey = derivedPrefix + cnt + "_Barcode";
				String storageContainerKey = derivedPrefix + cnt + "_StorageContainer";
				String positionOneKey = derivedPrefix + cnt + "_PositionOne";
				String positionTwoKey = derivedPrefix + cnt + "_PositionTwo";
				//key for location map
				String classKey = derivedPrefix + cnt + "_ClassName";
				String collectionProtocolKey = derivedPrefix + cnt + "_CollectionProtocol";
				specimenOnUIMap.put(classKey, specimen.getClassName());
				specimenOnUIMap.put(collectionProtocolKey, specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
						.getCollectionProtocol().getId().toString());

				specimenOnUIMap.put(labelKey, specimen.getLabel());
				specimenOnUIMap.put(typeKey, specimen.getType());
				specimenOnUIMap.put(barKey, specimen.getBarcode());
				if (specimen.getStorageContainer() != null)
					specimenOnUIMap.put(storageContainerKey, specimen.getStorageContainer().getId().toString());
				if (specimen.getPositionDimensionOne() != null)
					specimenOnUIMap.put(positionOneKey, specimen.getPositionDimensionOne().toString());
				if (specimen.getPositionDimensionTwo() != null)
					specimenOnUIMap.put(positionTwoKey, specimen.getPositionDimensionTwo().toString());
			}
			specimenOnUIMap.put(parentKey + "DeriveCount", new Integer(derivedList.size()));
		}
		else
		{
			specimenOnUIMap.put(parentKey + "DeriveCount", new Integer(0));
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		if (request.getParameter("formSubmitted")!=null)
		{
			Iterator keyIterator = specimenOnUIMap.keySet().iterator();

			while (keyIterator.hasNext())
			{
				Validator validator = new Validator();
				String key = (String) keyIterator.next();

				if (key.endsWith("_Label"))
				{
					String value = (String) specimenOnUIMap.get(key);

					if (validator.isEmpty(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
								.getValue("specimen.label")));
					}
				}
				else if (key.endsWith("_PositionOne") || key.endsWith("_PositionTwo") || key.indexOf("positionDimension") != -1)
				{
					String value = (String) specimenOnUIMap.get(key);
					if (value != null && !value.trim().equals("") && !validator.isDouble(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("specimen.positionInStorageContainer")));
						break;
					}
				}
			}
		}
		return errors;
	}
}