
package edu.wustl.catissuecore.util.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonConstants;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is the Setter class for hash map.
 * CatissueCoreServeletContextListener.java class call validateDefaultValues() method, this method will
 * validate default Values from Configuration file i.e caTissueCore_Properties.xml
 * for enumerated dropdowns and populate defaultValue map which will have key value pair
 * if validation fails for some value then empty String will be mapped to corresponding key.

 * @author virender_mehta
 * @Reviewer: Sachin Lale
 * Bug ID: defaultValueConfiguration_BugID
 * Patch ID:defaultValueConfiguration_BugID_3
 * See also:defaultValueConfiguration_BugID_1,2
 */
public class DefaultValueManager
{

	private static Logger logger = Logger.getCommonLogger(DefaultValueManager.class);
	private static HashMap defaultValueMap = new HashMap();

	/**
	 * This method is getter method with parameter as Key and it will return value mappend to the key.
	 * @param defaultConstant
	 * @return dafaultValueMap.get(defaultConstant);
	 */
	public static Object getDefaultValue(String defaultConstant)
	{

		return defaultValueMap.get(defaultConstant);
	}

	/**
	 * This method is setter method.
	 * It will map value with the key
	 * @param key The key
	 * @param value The value
	 */
	private static void setDefaultValue(String key, String value)
	{
		defaultValueMap.put(key, value);
	}

	/**
	 * Description: Validate and Configure default value for enumerated dropdowns.
	 */
	public static void validateAndInitDefaultValueMap()
	{
		String specimenClassName = null;
		List permissibleValueList = new ArrayList();
		for (final String[] defaultValueKey : Constants.defaultValueKeys)
		{
			final String defaultValue = XMLPropertyHandler.getValue(defaultValueKey[0]);
			NameValueBean defaultValueBean = null;
			//converting defaultValue into NameValue object.
			if (defaultValue != null)
			{
				defaultValueBean = new NameValueBean(defaultValue, defaultValue);
			}
			if (defaultValueKey[0].equalsIgnoreCase(Constants.IS_BARCODE_EDITABLE))
			{
				logger.error("......barcode.isEditable....." + defaultValue + "default keys : "
						+ defaultValueKey[0]);
				DefaultValueManager.setDefaultValue(defaultValueKey[0], defaultValue);
			}
			else
			{
				if ((defaultValueKey[0]).equals(Constants.DEFAULT_SPECIMEN))
				{
					specimenClassName = defaultValue;
				}
				if ((defaultValueKey[0]).equals(Constants.DEFAULT_SPECIMEN_TYPE))
				{
					//Get the Specimen Type List.
					permissibleValueList = AppUtility.getSpecimenTypes(specimenClassName);
				}
				if ((defaultValueKey[0]).equals(Constants.DEFAULT_CLINICAL_DIAGNOSIS))
				{
					final String sourceObjectName = PermissibleValueImpl.class.getName();
					final String[] selectColumnName = {"value"};
					// String[] whereColumnName ={ "value" };
					// String[] whereColumnCondition =  { "=" };
					// Object[] whereColumnValue ={Constants.SPECIMEN_TYPE_NOT_SPECIFIED};

					final String[] whereColumnName = {"value", "cde.publicId"};
					final String[] whereColumnCondition = {"=", "="};
					final Object[] whereColumnValue = {Constants.NOT_SPECIFIED,
							"Clinical_Diagnosis_PID"};

					final String joinCondition = CommonConstants.AND_JOIN_CONDITION;
					new ArrayList();
					final IBizLogic bizLogic = new DefaultBizLogic();
					try
					{
						final Iterator<String> iterator = bizLogic.retrieve(sourceObjectName,
								selectColumnName, whereColumnName, whereColumnCondition,
								whereColumnValue, joinCondition).iterator();

						if (iterator.hasNext())
						{
							final String clinicaDiagnosisvalue = iterator.next();
							permissibleValueList.add(new NameValueBean(clinicaDiagnosisvalue,
									clinicaDiagnosisvalue));
						}
					}
					catch (final Exception e)
					{
						logger.debug(e.getMessage(), e);
						e.printStackTrace();
					}
				}
				else
				{
					permissibleValueList = AppUtility.getListFromCDE(defaultValueKey[1]);
				}
				//added for bug 10750
				if ((defaultValueKey[0]).equals(Constants.DEFAULT_PRINTER_LOCATION))
				{
					permissibleValueList = new ArrayList<NameValueBean>();
					if (Variables.printerLocationList != null
							&& Variables.printerLocationList.size() > 0)
					{
						defaultValueBean = Variables.printerLocationList.get(0);
						permissibleValueList.add(defaultValueBean);
					}

				}
				//added for bug 10750
				if ((defaultValueKey[0]).equals(Constants.DEFAULT_PRINTER_TYPE))
				{
					permissibleValueList = new ArrayList<NameValueBean>();
					if (Variables.printerTypeList != null && Variables.printerTypeList.size() > 0)
					{
						defaultValueBean = Variables.printerTypeList.get(0);
						permissibleValueList.add(defaultValueBean);
					}

				}

				/*If List contain default value then key,Value pair is set in default value map
				else empty string is set for that key*/
				if (permissibleValueList != null && permissibleValueList.contains(defaultValueBean))
				{
					DefaultValueManager.setDefaultValue(defaultValueKey[0], defaultValueBean
							.getValue());
				}
				else
				{
					DefaultValueManager.setDefaultValue(defaultValueKey[0], "");
					logger.error("Default Value set for '" + defaultValueKey[0]
							+ "' is not in the CDEList");
				}
			}
		}
	}
}
