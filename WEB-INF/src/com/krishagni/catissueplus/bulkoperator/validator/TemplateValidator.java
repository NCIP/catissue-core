/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.metadata.Attribute;
import com.krishagni.catissueplus.bulkoperator.metadata.AttributeDiscriminator;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.HookingInformation;
import com.krishagni.catissueplus.bulkoperator.processor.DynCategoryBulkOperationProcessor;
import com.krishagni.catissueplus.bulkoperator.processor.StaticBulkObjectBuilder;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class TemplateValidator {

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(TemplateValidator.class);
	/**
	 * errorList of ArrayList format containing error messages in String format.
	 */
	private transient final List<String> errorList = new ArrayList<String>();
	private transient int globalRecordsCount = 0;
	private transient final boolean isDiscriminator = false;

	/**
	 * Validate Xml And Csv.
	 *
	 * @param bulkOperationMetaData
	 *            BulkOperationMetaData.
	 * @param operationName
	 *            String.
	 * @param bulkOperationProcessor
	 *            BulkOperationProcessor.
	 * @param csvColumnNames
	 *            Map of String, String.
	 * @throws Exception
	 *             Exception.
	 */
	public Set<String> validateXmlAndCsv(BulkOperationClass bulkOperationClass,
			String operationName,CsvReader csvReader)
			throws BulkOperationException {

		List<String> columnNamesList = Arrays.asList(csvReader.getColumnNames());


		if (errorList.isEmpty()) {
			try {
				if (BulkOperationConstants.ENTITY_TYPE
						.equals(bulkOperationClass.getType())) {
					validateBulkOperationClass(bulkOperationClass, columnNamesList, 0);
					StaticBulkObjectBuilder staticProcessor = new StaticBulkObjectBuilder(
							bulkOperationClass);
					Object domainObject = bulkOperationClass.getClassObject()
							.getConstructor().newInstance((Object[])null);
					staticProcessor.processObject(domainObject,
							bulkOperationClass, csvReader, "", true, 0);
				}
				else if (BulkOperationConstants.CATEGORY_TYPE.equalsIgnoreCase(bulkOperationClass.getType())) {
					HashMap<String, Object> dynExtObject = new HashMap<String, Object>();
					DynCategoryBulkOperationProcessor deProcessor = new DynCategoryBulkOperationProcessor(
							bulkOperationClass);
					deProcessor.processObject(dynExtObject,
							bulkOperationClass, csvReader,
							"", true, 0);
					HookingInformation hookingInformationFromTag = bulkOperationClass.getHookingInformation();
					validateHookingInformation(csvReader,
							hookingInformationFromTag);
				}
			} catch (BulkOperationException bulkExp) {
				logger.debug(bulkExp.getMessage(), bulkExp);
				throw new BulkOperationException(bulkExp.getErrorKey(),
						bulkExp, bulkExp.getMsgValues());
			} catch (Exception exp) {
				logger.debug(exp.getMessage(), exp);
				ErrorKey errorKey = ErrorKey
						.getErrorKey("bulk.operation.issues");
				throw new BulkOperationException(errorKey, exp, exp
						.getMessage());
			}
		}
		return new HashSet<String>(errorList);
	}

	private void validateHookingInformation(CsvReader csvReader,
			HookingInformation hookingInformationFromTag) {
		Collection<Attribute> attributes = hookingInformationFromTag
				.getAttributeCollection();
		final List<String> columnList = Arrays.asList(csvReader.getColumnNames());
		for (Attribute attribute : attributes) {
			if (!columnList.contains(attribute.getCsvColumnName())) {
				logger.error("Column name " + attribute.getCsvColumnName()
						+ " does not exist in CSV.");
				errorList.add("Column name " + attribute.getCsvColumnName()
						+ " does not exist in CSV.");

			}
		}
	}

	/**
	 * Validate Bulk Operation Main Class..
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass
	 * @param operationName
	 *            String
	 * @param csvColumnNames
	 *            Map of String, String.
	 * @throws Exception
	 *             Exception.
	 */
	private void validateBulkOperationClass(
			BulkOperationClass bulkOperationClass, List<String> csvColumnNames,
			int maxRowNumbers) throws BulkOperationException {
		try {
			bulkOperationClass.getClassObject();
			if (bulkOperationClass.getMaxNoOfRecords() >= 1
					&& "*".equals(bulkOperationClass.getCardinality())) {
				maxRowNumbers = bulkOperationClass.getMaxNoOfRecords();
				if (globalRecordsCount == 0) {
					globalRecordsCount = maxRowNumbers;
				}
			}
		} catch (NullPointerException exp) {
			logger
					.debug(
							"The keyword 'className' "+bulkOperationClass.getClassName()+" is either missing or incorrectly "
									+ "written in the XML for the main class tag.",
							exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey("bulk.error.xml.missing.name");
			throw new BulkOperationException(errorkey, exp, "className "+bulkOperationClass.getClassName());
		} catch (Exception exp) {
			logger
					.debug(
							"The 'className' "+bulkOperationClass.getClassName() +" value mentioned is incorrect for the main XML tag.",
							exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey("bulk.error.xml.incorrect.name");
			throw new BulkOperationException(errorkey, exp, "className "+bulkOperationClass.getClassName());
		}
		try {
			validateXMLTagAttibutes(bulkOperationClass,
					globalRecordsCount);
			if (globalRecordsCount != 0 && maxRowNumbers != 0) {
				for (int i = globalRecordsCount; i >= 1; i--) {
					validateAssociations(bulkOperationClass, csvColumnNames, i);
				}
			} else {
				validateAssociations(bulkOperationClass, csvColumnNames, 0);
			}
		} catch (BulkOperationException exp) {
			logger.debug(exp.getMessage(), exp);
			throw new BulkOperationException(exp.getErrorKey(), exp, exp
					.getMsgValues());
		}
	}

	/**
	 * Validate XML Tag Attributes.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param operationName
	 *            String.
	 */
	private void validateXMLTagAttibutes(BulkOperationClass bulkOperationClass,int maxRowNumbers)
			throws BulkOperationException {
		Integer maxNoOfRecords = bulkOperationClass.getMaxNoOfRecords();
		if (maxNoOfRecords <= 0) {
			logger.debug("The 'maxRecords' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. "
					+ "It should greater than or equal to 1.");
			errorList.add("The 'maxRecords' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. "
					+ "It should greater than or equal to 1.");
		} else if (maxNoOfRecords > 1
				&& bulkOperationClass.getCardinality() == "1") {
			logger
					.debug("The 'maxRecords' value mentioned for "
							+ bulkOperationClass.getClassName()
							+ " is incorrect. "
							+ "The value should be '1' if its cardinality value is set to '1'.");
			errorList
					.add("The 'maxRecords' value mentioned for "
							+ bulkOperationClass.getClassName()
							+ " is incorrect. "
							+ "The value should be '1' if its cardinality value is set to '1'.");
		}
	}

	/**
	 * Validate Associations.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param operationName
	 *            String.
	 * @param csvColumnNames
	 *            Hash table of String, String.
	 * @throws BulkOperationException
	 *             BulkOperationException.
	 */
	private void validateAssociations(BulkOperationClass bulkOperationClass,
			List<String> csvColumnNames, int maxRowNumbers)
			throws BulkOperationException {
		if (bulkOperationClass.getAttributeCollection() != null
				&& !bulkOperationClass.getAttributeCollection().isEmpty()) {
			Collection<Attribute> attributesClassList = bulkOperationClass
					.getAttributeCollection();
			validateAttributes(attributesClassList, bulkOperationClass,
					csvColumnNames, maxRowNumbers);
		}
		if (bulkOperationClass.getContainmentAssociationCollection() != null
				&& !bulkOperationClass.getContainmentAssociationCollection()
						.isEmpty()) {
			Collection<BulkOperationClass> containmentClassList = bulkOperationClass
					.getContainmentAssociationCollection();
			validateContainmentReference(containmentClassList, csvColumnNames,
					maxRowNumbers);
		}
		if (bulkOperationClass.getReferenceAssociationCollection() != null
				&& !bulkOperationClass.getReferenceAssociationCollection()
						.isEmpty()) {
			Collection<BulkOperationClass> referenceClassList = bulkOperationClass
					.getReferenceAssociationCollection();
			validateContainmentReference(referenceClassList, csvColumnNames,
					maxRowNumbers);
		}
	}

	/**
	 * Validate Containment and Reference.
	 *
	 * @param operationName
	 *            String.
	 * @param classList
	 *            Collection of BulkOperationClass.
	 * @param csvColumnNames
	 *            Hash table of String, String values.
	 * @throws BulkOperationException
	 *             BulkOperationException.
	 */
	private void validateContainmentReference(
			Collection<BulkOperationClass> classList,
			List<String> csvColumnNames, int maxRowNumbers)
			throws BulkOperationException {
		for (BulkOperationClass innerClass : classList) {
			try {
				innerClass.getClassObject();
				validateBulkOperationClass(innerClass, csvColumnNames,
						maxRowNumbers);
				validateAssociations(innerClass,csvColumnNames,maxRowNumbers);
			} catch (NullPointerException exp) {
				logger.debug(
						"The keyword 'className' "+innerClass.getClassName()+" is either missing or incorrectly "
								+ "written for a XML inner class tag.", exp);
				ErrorKey errorKey = ErrorKey
						.getErrorKey("bulk.error.xml.missing.name");
				throw new BulkOperationException(errorKey, exp, "className "+innerClass.getClassName());
			} catch (Exception exp) {
				logger
						.debug(
								"The 'className' value "+innerClass.getClassName()+" mentioned is incorrect in the XML class tag.",
								exp);
				ErrorKey errorKey = ErrorKey
						.getErrorKey("bulk.error.xml.incorrect.name");
				throw new BulkOperationException(errorKey, exp, "className "+innerClass.getClassName());
			}
		}
	}

	/**
	 * Validate Attributes.
	 *
	 * @param attributesClassList
	 *            Collection of Attributes.
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param csvColumnNames
	 *            Hash table of String, String.
	 * @throws BulkOperationException
	 *             BulkOperationException.
	 */
	private void validateAttributes(Collection<Attribute> attributesClassList,
			BulkOperationClass bulkOperationClass, List<String> csvColumnNames,
			int maxRowNumbers) throws BulkOperationException {
		for (Attribute attribute : attributesClassList) {
			try {
				Class classObject = null;
				String belongsTo = attribute.getBelongsTo();
				if (belongsTo != null && !"".equals(belongsTo)) {
					try {
						classObject = Class.forName(belongsTo);
					} catch (Exception exp) {
						logger.debug(
								"The 'belongsTo' attribute in discriminator tag in "
										+ bulkOperationClass.getClassName()
										+ " has incorrect class name.", exp);
						errorList
								.add("The 'belongsTo' attribute in discriminator tag in "
										+ bulkOperationClass.getClassName()
										+ " has incorrect class name.");
						classObject = bulkOperationClass.getClassObject();
					}
				} else {
					classObject = bulkOperationClass.getClassObject();
				}
				validateDiscriminator(bulkOperationClass, attribute);
				Field field = null;
				String attributeName = attribute.getName();
				if (attributeName == null) {
					checkForNullData(bulkOperationClass, "attribute Name");
				} else if ("".equals(attributeName.trim())) {
					logger.debug("The 'attributeName' value mentioned for "
							+ bulkOperationClass.getClassName()
							+ " is incorrect.");
					errorList.add("The 'attributeName' value mentioned for "
							+ bulkOperationClass.getClassName()
							+ " is incorrect.");
				} else {
					field = getDeclaredField(classObject, attributeName);
					if (field == null
							&& (!"java.lang.String".equals(bulkOperationClass
									.getClassName())
									&& !"java.lang.Integer"
											.equals(bulkOperationClass
													.getClassName())
									&& !"java.lang.Boolean"
											.equals(bulkOperationClass
													.getClassName())
									&& !"java.lang.Double"
											.equals(bulkOperationClass
													.getClassName())
									&& !"java.lang.Float"
											.equals(bulkOperationClass
													.getClassName()) && !"java.lang.Long"
									.equals(bulkOperationClass.getClassName()))) {
						logger.debug("The keyword '" + attributeName
								+ "' in attribute collection tag of "
								+ bulkOperationClass.getClassName()
								+ " class tag in incorrect.");
						errorList.add("The keyword '" + attributeName
								+ "' in attribute collection tag of "
								+ bulkOperationClass.getClassName()
								+ " class tag in incorrect.");
					}
				}
				validateColumnName(bulkOperationClass, attribute,
						csvColumnNames, maxRowNumbers);
				// validateDataType(bulkOperationClass, attribute, field,
				// attributeName);
				// validateUpdateBasedOn(bulkOperationClass, attribute);
			} catch (Exception exp) {
				logger.debug(exp.getMessage(), exp);
				errorList.add(exp.getMessage());
			}
		}
	}

	private void validateDiscriminator(BulkOperationClass bulkOperationClass,
			Attribute attribute) {
		Collection<AttributeDiscriminator> attributeDisColl = attribute
				.getDiscriminatorCollection();
	   for (AttributeDiscriminator discriminator : attributeDisColl) {
			validateDiscriminatorName(bulkOperationClass, discriminator);
			validateDiscriminatorValue(bulkOperationClass, discriminator);
		}
	}

	/**
	 * @param bulkOperationClass
	 * @param discriminator
	 */
	private void validateDiscriminatorName(
			BulkOperationClass bulkOperationClass,
			AttributeDiscriminator discriminator) {
		String name = discriminator.getName();
		if (name == null) {
			logger.debug("The keyword 'name' is either missing or incorrectly "
					+ "written for discriminator tag in "
					+ bulkOperationClass.getClassName()
					+ " class tag in the XML.");
			errorList
					.add("The keyword 'name' is either missing or incorrectly "
							+ "written for discriminator tag in "
							+ bulkOperationClass.getClassName()
							+ " class tag in the XML.");
		} else if ("".equals(name.trim())) {
			logger
					.debug("The value for 'name' attribute in discriminator tag in "
							+ bulkOperationClass.getClassName()
							+ " in XML cannot be empty.");
			errorList
					.add("The value for 'name' attribute in discriminator tag in "
							+ bulkOperationClass.getClassName()
							+ " in XML cannot be empty.");
		}
	}

	/**
	 * @param bulkOperationClass
	 * @param discriminator
	 */
	private void validateDiscriminatorValue(
			BulkOperationClass bulkOperationClass,
			AttributeDiscriminator discriminator) {
		String value = discriminator.getValue();
		if (value == null) {
			logger
					.debug("The keyword 'value' is either missing or incorrectly "
							+ "written for discriminator tag in "
							+ bulkOperationClass.getClassName()
							+ " class tag in the XML.");
			errorList
					.add("The keyword 'value' is either missing or incorrectly "
							+ "written for discriminator tag in "
							+ bulkOperationClass.getClassName()
							+ " class tag in the XML.");
		} else if ("".equals(value.trim())) {
			logger.debug("The 'value' attribute in discriminator tag in "
					+ bulkOperationClass.getClassName()
					+ " in XML cannot be empty.");
			errorList.add("The 'value' attribute in discriminator tag in "
					+ bulkOperationClass.getClassName()
					+ " in XML cannot be empty.");
		} else {
			try {
				Class.forName(value);
			} catch (Exception exp) {
				logger.debug("The 'value' attribute in discriminator tag in "
						+ bulkOperationClass.getClassName()
						+ " has incorrect class name.", exp);
				errorList.add("The 'value' attribute in discriminator tag in "
						+ bulkOperationClass.getClassName()
						+ " has incorrect class name.");
			}
		}
	}

	/**
	 * Validate UpdateBasedOn value.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param attribute
	 *            Attribute.
	 */
	/**
	 * Validate Data Type.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param attribute
	 *            Attribute.
	 * @param field
	 *            Field.
	 * @param attributeName
	 *            String.
	 */

	/**
	 * Validate Column Name.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 * @param attribute
	 *            Attribute.
	 * @param csvColumnNames
	 *            Hash table of String, String.
	 */
	private void validateColumnName(BulkOperationClass bulkOperationClass,
			Attribute attribute, List<String> csvColumnNames, int maxRowNumbers) {
		String csvColumnName = attribute.getCsvColumnName();
		if (csvColumnName == null) {
			checkForNullData(bulkOperationClass, "csvColumnName");
		}
	}

	/**
	 * Get Declared Field.
	 *
	 * @param classObject
	 *            Class.
	 * @param attributeName
	 *            String.
	 * @return Field Field.
	 */
	private Field getDeclaredField(Class classObject, String attributeName) {
		Field field = null;
		boolean flag = false;
		do {
			try {
				if (flag) {
					classObject = classObject.getSuperclass();
				}
				field = classObject.getDeclaredField(attributeName);
				flag = true;
			} catch (Exception exp) {
				flag = true;
			}
		} while (classObject.getSuperclass() != null && field == null);
		return field;
	}

	/**
	 * Validate Cardinality.
	 *
	 * @param bulkOperationClass
	 *            BulkOperationClass.
	 */
	private void validateCardinality(BulkOperationClass bulkOperationClass) {
		String cardinality = bulkOperationClass.getCardinality();
		if (cardinality == null) {
			checkForNullData(bulkOperationClass, "cardinality");
		} else if (("".equals(cardinality.trim()))
				|| (!"*".equals(cardinality.trim()) && !"1".equals(cardinality
						.trim()))) {
			logger.debug("The 'cardinality' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. "
					+ "Valid values are '1' and '*' only.");
			errorList.add("The 'cardinality' value mentioned for "
					+ bulkOperationClass.getClassName() + " is incorrect. "
					+ "Valid values are '1' and '*' only.");
		}
	}

	/**
	 * @param bulkOperationClass
	 */
	private void checkForNullData(BulkOperationClass bulkOperationClass,
			String attributeType) {
		logger.debug("The keyword '" + attributeType
				+ "' is either missing or incorrectly "
				+ "written in the XML for " + bulkOperationClass.getClassName()
				+ " class tag.");
		errorList.add("The keyword '" + attributeType
				+ "' is either missing or incorrectly "
				+ "written in the XML for " + bulkOperationClass.getClassName()
				+ " class tag.");
	}
}