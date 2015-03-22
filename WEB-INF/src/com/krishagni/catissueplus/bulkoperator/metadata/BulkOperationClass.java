/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.proxy.HibernateProxy;

import com.krishagni.catissueplus.bulkoperator.csv.CsvReader;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class BulkOperationClass
{

	/**
	 *
	 */
	private static Logger logger = Logger.getCommonLogger(BulkOperationClass.class);

	private String className;
	private String relationShipType;
	private String cardinality="1";
	private String roleName;
	private String parentRoleName;
	private String templateName;
	private Integer maxNoOfRecords=1;
	private Integer batchSize;
	private Long id;
	private Class klass;
	private String type=BulkOperationConstants.ENTITY_TYPE;
	private String isOneToManyAssociation="";
	private Collection<BulkOperationClass> referenceAssociationCollection = new ArrayList<BulkOperationClass>();
	private Collection<BulkOperationClass> containmentAssociationCollection = new ArrayList<BulkOperationClass>();
	private Collection<BulkOperationClass> dynExtEntityAssociationCollection = new ArrayList<BulkOperationClass>();
	private Collection<BulkOperationClass> dynExtCategoryAssociationCollection = new ArrayList<BulkOperationClass>();
	private Collection<Attribute> attributeCollection = new ArrayList<Attribute>();
	private HookingInformation hookingInformation;
	private String entityGroupName;
	
	
	public void setHookingInformation(HookingInformation hookingInformation)
	{
		this.hookingInformation = hookingInformation;
	}

	public  HookingInformation getHookingInformation()
	{
		return hookingInformation;
	}
 
	public Collection<Attribute> getAttributeCollection()
	{
		return attributeCollection;
	}

	public void setAttributeCollection(Collection<Attribute> attributeCollection)
	{
		this.attributeCollection = attributeCollection;
	}
	
	public void addAttribute(Attribute attribute) {
	    this.attributeCollection.add(attribute);
	}

	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Collection<BulkOperationClass> getReferenceAssociationCollection()
	{
		return referenceAssociationCollection;
	}

	/**
	 *
	 * @return
	 */
	public Collection<BulkOperationClass> getDynExtEntityAssociationCollection()
	{
		return dynExtEntityAssociationCollection;
	}

	/**
	 *
	 * @param associationCollection
	 */
	public void setDynExtEntityAssociationCollection(
			Collection<BulkOperationClass> associationCollection)
	{
		dynExtEntityAssociationCollection = associationCollection;
	}
  
	public void addDynExtEntityAssociation(BulkOperationClass dynExtEntityAssociation) {
	    this.dynExtEntityAssociationCollection.add(dynExtEntityAssociation);
	}
	/**
	 * @return the batchSize
	 */
	public Integer getBatchSize()
	{
		return batchSize;
	}

	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(Integer batchSize)
	{
		this.batchSize = batchSize;
	}

	public void setReferenceAssociationCollection(
			Collection<BulkOperationClass> referenceAssociationCollection)
	{
		this.referenceAssociationCollection = referenceAssociationCollection;
	}
	
	public void addReferenceAssociation(BulkOperationClass referenceAssociation) {
	    this.referenceAssociationCollection.add(referenceAssociation);
	}

	public Collection<BulkOperationClass> getContainmentAssociationCollection()
	{
		return containmentAssociationCollection;
	}

	public void setContainmentAssociationCollection(
			Collection<BulkOperationClass> containmentAssociationCollection)
	{
		this.containmentAssociationCollection = containmentAssociationCollection;
	}
	
	public void addContainmentAssociation(BulkOperationClass containmentAssociation) {
	    this.containmentAssociationCollection.add(containmentAssociation);
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getRelationShipType()
	{
		return relationShipType;
	}

	public void setRelationShipType(String relationShipType)
	{
		this.relationShipType = relationShipType;
	}

	public String getCardinality()
	{
		return cardinality;
	}

	public void setCardinality(String cardinality)
	{
		this.cardinality = cardinality;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getParentRoleName()
	{
		return parentRoleName;
	}

	public void setParentRoleName(String parentRoleName)
	{
		this.parentRoleName = parentRoleName;
	}

	public Integer getMaxNoOfRecords()
	{
		return maxNoOfRecords;
	}

	public void setMaxNoOfRecords(Integer maxNoOfRecords)
	{
		this.maxNoOfRecords = maxNoOfRecords;
	}

	public Collection<BulkOperationClass> getDynExtCategoryAssociationCollection()
	{
		return dynExtCategoryAssociationCollection;
	}

	public void setDynExtCategoryAssociationCollection(
			Collection<BulkOperationClass> dynExtCategoryAssociationCollection)
	{
		this.dynExtCategoryAssociationCollection = dynExtCategoryAssociationCollection;
	}
	public void addDynExtCategoryAssociation(BulkOperationClass dynExtCategoryAssociation) {
	    this.dynExtCategoryAssociationCollection.add(dynExtCategoryAssociation);
	}
	public boolean isUpdateOperation()
	{
		boolean isUpdateOperation = false;
		Collection<Attribute> attributes = getAttributeCollection();
		Iterator<Attribute> attributeItertor = attributes.iterator();
		while (attributeItertor.hasNext())
		{
			Attribute attribute = attributeItertor.next();
			if (attribute.getUpdateBasedOn())
			{
				isUpdateOperation = true;
			}
		}
		return isUpdateOperation;
	}

	public Object getClassDiscriminator(CsvReader csvReader, String columnSuffix)
			throws BulkOperationException
	{
		Object object = null;
		try
		{
			Collection<Attribute> attributes = getAttributeCollection();
			Iterator<Attribute> attributeItertor = attributes.iterator();
			while (attributeItertor.hasNext())
			{
				Attribute attribute = attributeItertor.next();
				Collection<AttributeDiscriminator> attributeDiscriminatorColl = attribute
						.getDiscriminatorCollection();
				if (attributeDiscriminatorColl != null && !attributeDiscriminatorColl.isEmpty())
				{
					Iterator<AttributeDiscriminator> attributeDiscriminatorItertor = attributeDiscriminatorColl
							.iterator();
					while (attributeDiscriminatorItertor.hasNext())
					{
						AttributeDiscriminator attributeDiscriminator = attributeDiscriminatorItertor
								.next();
						String value = csvReader.getColumn(attribute.getCsvColumnName() + columnSuffix);
						if (attributeDiscriminator.getName().equalsIgnoreCase(value))
						{
							String discriminatorValue = attributeDiscriminator.getValue();
							object = Class.forName(discriminatorValue).newInstance();
							break;
						}
					}
					break;
				}
			}
		}
		catch (Exception exp)
		{
			logger.debug("Error in Discriminator Object Instantiation." + exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.discriminator");
			throw new BulkOperationException(errorkey, exp, "");
		}
		return object;
	}

	/**
	 *
	 * @param bulkOperationclass
	 * @return
	 * @throws BulkOperationException
	 */
	public boolean checkForDynEntityAssociationCollectionTag(
			BulkOperationClass bulkOperationclass) throws BulkOperationException
	{
		boolean isDynExtEntityPresent = false;
		try
		{
			Collection<BulkOperationClass> dynEntityAssociationCollection = getDynExtEntityAssociationCollection();
			if (dynEntityAssociationCollection != null
					&& !dynEntityAssociationCollection.isEmpty())
			{
				isDynExtEntityPresent = true;
			}
		}
		catch (Exception exp)
		{
			logger.debug("Error in Checking For DynExtEntityAssociationCollection Tag. "
					+ exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.checking.deassociation");
			throw new BulkOperationException(errorkey, exp, "");
		}
		return isDynExtEntityPresent;
	}

	public boolean checkForDynExtCategoryAssociationCollectionTag(BulkOperationClass bulkOperationclass)
			throws BulkOperationException
	{
		boolean isCategoryObjectPresent = false;
		try
		{
			Collection<BulkOperationClass> categoryAssociationCollection = getDynExtCategoryAssociationCollection();
			if (categoryAssociationCollection != null && !categoryAssociationCollection.isEmpty())
			{
				isCategoryObjectPresent = true;
			}
		}
		catch (Exception exp)
		{
			logger.debug("Error in Checking For categoryAssociationCollection Tag. "
					+ exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.checking.deassociation");
			throw new BulkOperationException(errorkey, exp, "");
		}
		return isCategoryObjectPresent;
	}

	public Class getClassObject() throws BulkOperationException
	{
		try
		{
			if (klass == null)
			{
				klass = Class.forName(className);
			}
		}
		catch (ClassNotFoundException exp)
		{
			logger.debug(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
		return klass;
	}

	public Object getNewInstance() throws BulkOperationException
	{
		Object returnObject = null;
		try
		{
			returnObject = Class.forName(className).newInstance();
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
		return returnObject;
	}

	public Object invokeGetterMethod(String roleName, Class[] parameterTypes,
			Object objectOnWhichMethodToInvoke, Object... args) throws BulkOperationException
	{
		Object returnObject = null;
		try
		{
			String functionName = BulkOperationUtility.getGetterFunctionName(roleName);
			returnObject = Class.forName(className).getMethod(functionName, parameterTypes).invoke(
					objectOnWhichMethodToInvoke, args);
			if (returnObject instanceof HibernateProxy)
			{
//				returnObject = HibernateMetaData.getProxyObjectImpl(returnObject);
			}
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
		return returnObject;
	}

	public void invokeSetterMethod(String roleName, Class[] parameterTypes,
			Object objectOnWhichMethodToInvoke, Object... args) throws BulkOperationException
	{
		String functionName = BulkOperationUtility.getSetterFunctionName(roleName);
		try
		{
			objectOnWhichMethodToInvoke.getClass().getMethod(functionName, parameterTypes).invoke(
					objectOnWhichMethodToInvoke, args);
		}
		catch (NoSuchMethodException e)
		{
			try
			{
				Class.forName(className).getMethod(functionName, parameterTypes[0].getSuperclass())
						.invoke(objectOnWhichMethodToInvoke, args);
			}
			catch (NoSuchMethodException e1)
			{
				try
				{
					Class.forName(className).getMethod(functionName,
							parameterTypes[0].getSuperclass().getSuperclass()).invoke(
							objectOnWhichMethodToInvoke, args);
				}
				catch (Exception e2)
				{
					logger.debug(e2.getMessage(), e2);
					ErrorKey errorkey = ErrorKey
							.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
					throw new BulkOperationException(errorkey, e, e2.getMessage());
				}
			}
			catch (Exception e4)
			{
				logger.debug(e4.getMessage(), e4);
				ErrorKey errorkey = ErrorKey
						.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
				throw new BulkOperationException(errorkey, e, e4.getMessage());
			}
		}
		catch (Exception exp)
		{
			logger.debug(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey
					.getErrorKey(BulkOperationConstants.COMMON_ISSUES_ERROR_KEY);
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
	}

	public Long invokeGetIdMethod(Object objectOnWhichMethodToInvoke) throws BulkOperationException
	{
		Long identifier = null;
		try
		{
			identifier = (Long) Class.forName(className).getMethod("getId", (Class<?>[])null).invoke(
					objectOnWhichMethodToInvoke, (Object[])null);
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw new BulkOperationException(exp.getMessage(), exp);
		}
		return identifier;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getIsOneToManyAssociation() {
		return isOneToManyAssociation;
	}

	public void setIsOneToManyAssociation(String isOneToManyAssociation) {
		this.isOneToManyAssociation = isOneToManyAssociation;
	}
	public String getEntityGroupName()
	{
		return entityGroupName;
	}
	public void setEntityGroupName(String entityGroupName)
	{
		this.entityGroupName = entityGroupName;
	}
}