/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.metadata.Attribute;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.DateValue;
import com.krishagni.catissueplus.bulkoperator.metadata.HookingInformation;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;

import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public abstract class AbstractBulkObjectBuilder {
	
	private static final Logger logger = Logger.getCommonLogger(AbstractBulkObjectBuilder.class);
	
	protected BulkOperationClass bulkOperationClass = null;
	
	private static CustomDateConverter converter=new CustomDateConverter();
	
	static {
		  ConvertUtils.register(converter, java.util.Date.class);
	};
	
	public AbstractBulkObjectBuilder(BulkOperationClass bulkOperationClass) {
		this.bulkOperationClass = bulkOperationClass;
	}
	
	public final BulkOperationClass getBulkOperationClass() {
		return bulkOperationClass;
	}

	protected Object getEntityObject(CsvReader csvReader) throws BulkOperationException {
		Object staticObject = bulkOperationClass.getClassDiscriminator(csvReader,"");
		if (staticObject == null) {
			staticObject = bulkOperationClass.getNewInstance();
		}
		return staticObject;
	}

	abstract Object processObject(Map<String, String> csvData) throws BulkOperationException;

	/**
	 * 
	 * @param mainObj
	 * @param migrationClass
	 * @param columnSuffix
	 * @param validate
	 * @throws BulkOperationException
	 */
	public void processObject(Object mainObj, BulkOperationClass migrationClass,CsvReader csvReader,
			String columnSuffix, boolean validate, int csvRowNumber)
			throws BulkOperationException {
		if (migrationClass.getAttributeCollection() != null
				&& !migrationClass.getAttributeCollection().isEmpty()) {
			processAttributes(mainObj, migrationClass, csvReader, columnSuffix,
					validate);
		}

		if (migrationClass.getContainmentAssociationCollection() != null
				&& !migrationClass.getContainmentAssociationCollection()
						.isEmpty()) {
			processContainments(mainObj, migrationClass, csvReader, columnSuffix,
					validate, csvRowNumber);
		}

		if (migrationClass.getReferenceAssociationCollection() != null
				&& !migrationClass.getReferenceAssociationCollection()
						.isEmpty()) {
			processAssociations(mainObj, migrationClass, csvReader, columnSuffix,
					validate, csvRowNumber);
		}
	}

	/**
	 * 
	 * @param mainObj
	 * @param mainMigrationClass
	 * @param csvData
	 * @param columnSuffix
	 * @param validate
	 * @throws BulkOperationException
	 */
	protected void processContainments(Object mainObj, BulkOperationClass mainMigrationClass, CsvReader csvReader,
			String columnSuffix, boolean validate, int csvRowNumber)
			throws BulkOperationException {
		try {
			Iterator<BulkOperationClass> containmentItert = mainMigrationClass
					.getContainmentAssociationCollection().iterator();
			while (containmentItert.hasNext()) {
				BulkOperationClass containmentMigrationClass = containmentItert
						.next();
				String cardinality = containmentMigrationClass.getCardinality();
				if (cardinality != null && cardinality.equals("*")
						&& !cardinality.equals("")) {
					Collection containmentObjectCollection = (Collection) mainMigrationClass
							.invokeGetterMethod(containmentMigrationClass
									.getRoleName(), null, mainObj, (Object[])null);
					if (containmentObjectCollection == null) {
						containmentObjectCollection = new LinkedHashSet();
					}
					List sortedList = new ArrayList(containmentObjectCollection);
					containmentObjectCollection = new LinkedHashSet(sortedList);
					int maxNoOfRecords = containmentMigrationClass
							.getMaxNoOfRecords().intValue();
					for (int i = 1; i <= maxNoOfRecords; i++) {
						
						if (validate||BulkOperationUtility
								.checkIfAtLeastOneColumnHasAValueForInnerContainmentForStatic(csvRowNumber,
										containmentMigrationClass,columnSuffix+ "#" + i, csvReader))
						{
						 Object containmentObject =null;
						if(!validate)
						{
							containmentObject = containmentMigrationClass
									.getClassDiscriminator(csvReader,
											columnSuffix + "#" + i);// getNewInstance();
						}			
							if (containmentObject == null) {
								if (BulkOperationConstants.JAVA_LANG_STRING_DATATYPE
										.equals(containmentMigrationClass
												.getClassName())) {
									containmentObject = new StringBuffer();
								} else {
									containmentObject = containmentMigrationClass
											.getNewInstance();
								}
							}
							
							processObject(containmentObject,
									containmentMigrationClass, csvReader,
									columnSuffix + "#" + i, validate,
									csvRowNumber);
							if (BulkOperationConstants.JAVA_LANG_STRING_DATATYPE
									.equals(containmentMigrationClass
											.getClassName())) {
								containmentObjectCollection
										.add(containmentObject.toString());
							} else {
								containmentObjectCollection
										.add(containmentObject);
							}
							String roleName = containmentMigrationClass
									.getParentRoleName();
							if (!Validator.isEmpty(roleName)) {
								BeanUtils.setProperty(containmentObject,roleName,mainObj);
							}
						}
					}
					String roleName = containmentMigrationClass.getRoleName();
					
					setContainmentObjectProperty(containmentMigrationClass, mainObj, roleName, containmentObjectCollection);
				} else if (cardinality != null && cardinality.equals("1")
						&& !cardinality.equals("")) {
					
					if ( validate||BulkOperationUtility.checkIfAtLeastOneColumnHasAValueForInnerContainmentForStatic(csvRowNumber,
							containmentMigrationClass,columnSuffix, csvReader)) {
						Object containmentObject = mainMigrationClass
								.invokeGetterMethod(containmentMigrationClass
										.getRoleName(), null, mainObj, (Object[]) null);
						if (containmentObject == null) {
							containmentObject = containmentMigrationClass
									.getClassDiscriminator(csvReader,
											columnSuffix);
						}
						if (containmentObject == null) {
							Class klass = containmentMigrationClass
									.getClassObject();
							Constructor constructor = klass
									.getConstructor(null);
							containmentObject = constructor.newInstance();
						}
						processObject(containmentObject,
								containmentMigrationClass, csvReader,
								columnSuffix, validate, csvRowNumber);
						String roleName = containmentMigrationClass
								.getRoleName();
						
						BeanUtils.setProperty(mainObj,roleName,containmentObject);
						
						String parentRoleName = containmentMigrationClass.getParentRoleName();
						if (!Validator.isEmpty(parentRoleName)) {
							BeanUtils.setProperty(containmentObject,parentRoleName,mainObj);
						}
					}
				}
			}
		} catch (BulkOperationException bulkExp) {
			logger.error(bulkExp.getMessage(), bulkExp);
			throw new BulkOperationException(bulkExp.getErrorKey(), bulkExp,
					bulkExp.getMsgValues());
		} catch (Exception exp) {
			logger.error(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
	}
	
    private void setContainmentObjectProperty(BulkOperationClass containmentMigrationClass, Object mainObject, String name, 
            Collection valueToSet) throws Exception {
	    /*
	     * This method is introduced to handle List type Containment assignment
	     * Do not use getField() or getDeclaredField() to simplify the below processing
	     * #1 getField() doesn't fetch private fields of class
	     * #2 getDeclaredField() can fetch private fields of class, but can't fetch fields that are inherited from parent class
	     */
	    
	    String getterMethod = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	    Method method = mainObject.getClass().getMethod(getterMethod,(Class<?>[]) null);
	    
	    if (List.class.getName().equals(method.getReturnType().getName())) { 
	            valueToSet = new ArrayList(valueToSet);
	    } 
	    
	    BeanUtils.setProperty(mainObject,name,valueToSet);
    }

	/**
	 * 
	 * @param mainObj
	 * @param mainMigrationClass
	 * @param csvData
	 * @param columnSuffix
	 * @param validate
	 * @throws BulkOperationException
	 */
	private void processAssociations(Object mainObj,
			BulkOperationClass mainMigrationClass,CsvReader csvReader,
			String columnSuffix, boolean validate, int csvRowNumber)
			throws BulkOperationException {
		try {
			Iterator<BulkOperationClass> associationItert = mainMigrationClass
					.getReferenceAssociationCollection().iterator();
			while (associationItert.hasNext()) {
				BulkOperationClass associationMigrationClass = associationItert
						.next();
				String cardinality = associationMigrationClass.getCardinality();
				if (cardinality != null && cardinality.equals("*")
						&& !cardinality.equals("") && mainObj != null) {
					Collection associationObjectCollection = (Collection) mainMigrationClass
							.invokeGetterMethod(associationMigrationClass
									.getRoleName(), null, mainObj,(Object[]) null);
					if (associationObjectCollection == null) {
						associationObjectCollection = new LinkedHashSet<Object>();
					}
					List sortedList = new ArrayList(associationObjectCollection);
					associationObjectCollection = new LinkedHashSet(sortedList);
					int maxNoOfRecords = associationMigrationClass
							.getMaxNoOfRecords().intValue();
					for (int i = 1; i <= maxNoOfRecords; i++) {
						if ( validate||BulkOperationUtility
								.checkIfAtLeastOneColumnHasAValueForInnerContainmentForStatic(csvRowNumber,
										associationMigrationClass,columnSuffix+ "#" + i, csvReader)) {
							Object referenceObject =null;
							if(!validate)
							{
							 referenceObject = associationMigrationClass
									.getClassDiscriminator(csvReader,
											columnSuffix + "#" + i);
							}
							if (referenceObject == null) {
								if (BulkOperationConstants.JAVA_LANG_STRING_DATATYPE
										.equals(associationMigrationClass
												.getClassName())) {
									referenceObject = new StringBuffer();
								} else {
									referenceObject = associationMigrationClass
											.getNewInstance();
								}
							}
							processObject(referenceObject,
									associationMigrationClass, csvReader,
									columnSuffix + "#" + i, validate,
									csvRowNumber);
							if (BulkOperationConstants.JAVA_LANG_STRING_DATATYPE
									.equals(associationMigrationClass
											.getClassName())) {
								associationObjectCollection.add(referenceObject
										.toString());
							} else {
								associationObjectCollection
										.add(referenceObject);
							}
							String roleName = associationMigrationClass
									.getParentRoleName();
							if (!Validator.isEmpty(roleName)) {
								BeanUtils.setProperty(mainObj,roleName,referenceObject);
							}
						}
					}
					String roleName = associationMigrationClass.getRoleName();
					
					BeanUtils.setProperty(mainObj, roleName,associationObjectCollection);
					
				} else if (cardinality != null && cardinality.equals("1")
						&& !cardinality.equals("")) {
					List<String> attributeList = BulkOperationUtility
							.getAttributeList(associationMigrationClass,
									columnSuffix);
					if (validate||BulkOperationUtility.checkIfAtLeastOneColumnHasAValueForInnerContainmentForStatic(csvRowNumber,
							associationMigrationClass,columnSuffix, csvReader)) {
						Object associatedObject = mainMigrationClass.invokeGetterMethod(associationMigrationClass
										.getRoleName(), null, mainObj, (Object[])null);
						if (associatedObject == null) {
							associatedObject = associationMigrationClass
									.getClassDiscriminator(csvReader,
											columnSuffix);
						}
						if (associatedObject == null) {
							Class klass = associationMigrationClass
									.getClassObject();
							Constructor constructor = klass
									.getConstructor(null);
							associatedObject = constructor.newInstance();
						}
						processObject(associatedObject,
								associationMigrationClass, csvReader,
								columnSuffix, validate, csvRowNumber);
						String roleName = associationMigrationClass
								.getRoleName();
						
						BeanUtils.setProperty(mainObj, roleName,associatedObject);
						String parentRoleName = associationMigrationClass
						.getParentRoleName();
						if (!Validator.isEmpty(parentRoleName)) {
							BeanUtils.setProperty(associatedObject,parentRoleName,mainObj);
						}
					}
				}
			}
		} catch (BulkOperationException bulkExp) {
			logger.error(bulkExp.getMessage(), bulkExp);
			throw new BulkOperationException(bulkExp.getErrorKey(), bulkExp,
					bulkExp.getMsgValues());
		} catch (Exception exp) {
			logger.error(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
	}

	/**
	 * 
	 * @param mainObj
	 * @param mainMigrationClass
	 * @param csvData2
	 * @param columnSuffix
	 * @param validate
	 * @throws BulkOperationException
	 */
	private void processAttributes(Object mainObj,
			BulkOperationClass mainMigrationClass,CsvReader csvReader,
			String columnSuffix, boolean validate)
			throws BulkOperationException {
		try {
			Iterator<Attribute> attributeItertor = mainMigrationClass
					.getAttributeCollection().iterator();
			while (attributeItertor.hasNext()) {
				Attribute attribute = attributeItertor.next();

				if (validate
						&& !Arrays.asList(csvReader.getColumnNames()).contains(
								attribute.getCsvColumnName() + columnSuffix)
						&& attribute.getUpdateBasedOn())
				{
					BulkOperationUtility.throwExceptionForColumnNameNotFound(
							mainMigrationClass, validate, attribute);
				} else if(!validate)
				 {
					if ("java.lang.String".equals(mainMigrationClass
							.getClassName())) {
						if (!Validator.isEmpty(csvReader.getColumn(attribute.getCsvColumnName()
								+ columnSuffix))) {
							String csvDataValue =csvReader.getColumn(attribute.getCsvColumnName()+ columnSuffix);
							
							Object attributeValue = attribute
									.getValueOfDataType(csvDataValue, validate,
											attribute.getCsvColumnName()
													+ columnSuffix, mainMigrationClass
													.getClassName());
							((StringBuffer) mainObj).append(attributeValue);
						}
					} else if ("java.lang.Long".equals(mainMigrationClass
							.getClassName())
							|| "java.lang.Double".equals(mainMigrationClass
									.getClassName())
							|| "java.lang.Integer".equals(mainMigrationClass
									.getClassName())
							|| "java.lang.Boolean".equals(mainMigrationClass
									.getClassName())
							|| "java.lang.Float".equals(mainMigrationClass
									.getClassName())) {
						if (!Validator.isEmpty(csvReader.getColumn(attribute.getCsvColumnName()
								+ columnSuffix))) {
							csvReader.getColumn(attribute.getCsvColumnName()
									+ columnSuffix);
							mainObj = csvReader;
						}
					} else if (String.valueOf(mainObj.getClass()).contains(
							attribute.getBelongsTo())) {
						setValueToObject(mainObj, mainMigrationClass, csvReader,
								columnSuffix, validate, attribute);
					}// else if ends
				}// null check if - else ends
			}// while ends
		} catch (BulkOperationException bulkExp) {
			logger.error(bulkExp.getMessage(), bulkExp);
			throw new BulkOperationException(bulkExp.getErrorKey(), bulkExp,
					bulkExp.getMsgValues());
		} catch (Exception exp) {
			logger.error(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
	}

	protected void setValueToObject(Object mainObj,
			BulkOperationClass mainMigrationClass,CsvReader csvReader,
			String columnSuffix, boolean validate, Attribute attribute) throws BulkOperationException {
		String csvDataValue=null;
		if(csvReader.getColumn(attribute.getCsvColumnName()+ columnSuffix)!=null)
		{
			csvDataValue=csvReader.getColumn(attribute.getCsvColumnName()+ columnSuffix);
		}
		if(Validator.isEmpty(csvDataValue) && attribute.getDefaultValue()!=null)
		{
			csvDataValue=attribute.getDefaultValue();
		}
		
		if (!Validator.isEmpty(csvDataValue)) {
			try {
				 
				if(attribute.getFormat()!=null)
				{
					DateValue value = new DateValue(csvDataValue, attribute.getFormat());
					BeanUtils.copyProperty(mainObj, attribute.getName(),value);
				}
				else
				{
					BeanUtils.copyProperty(mainObj, attribute.getName(),csvDataValue);
				}
				
			} catch (IllegalAccessException exp) {
				logger.error(exp.getMessage(), exp);
				ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
				throw new BulkOperationException(errorkey, exp, exp.getMessage());
			} catch (InvocationTargetException exp) {
				logger.error(exp.getMessage(), exp);
				ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
				throw new BulkOperationException(errorkey, exp, exp.getMessage());
			}
		}
	}
	
	protected void getinformationForHookingData(CsvReader csvReader, HookingInformation hookingInformation) {		
		for (Attribute attr : hookingInformation.getAttributeCollection()) {
			if (!Validator.isEmpty(csvReader.getColumn(attr.getCsvColumnName()))) {
				String csvDataValue = csvReader.getColumn(attr.getCsvColumnName());
				hookingInformation.getDataHookingInformation().put(attr.getName(), csvDataValue);
			} else{
				hookingInformation.getDataHookingInformation().remove(attr.getName());
			}
		}
		hookingInformation.setEntityType();
	}
}
