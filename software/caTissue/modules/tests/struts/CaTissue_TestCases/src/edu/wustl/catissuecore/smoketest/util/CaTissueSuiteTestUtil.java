package edu.wustl.catissuecore.smoketest.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.flex.FlexInterface;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.dao.util.HibernateMetaDataFactory;
/**
 * Utility class.
 * @author sachin_lale
 *
 */
public class CaTissueSuiteTestUtil
{
	/**
	 * Default constructor.
	 */
	public CaTissueSuiteTestUtil()
	{

	}
	/**
	 * JNDI name for catissuecore.
	 */
	public static final String CATISSUE_DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * JNDI name for dynamic extension.
	 */
	public static final String DE_DATASOURCE_JNDI_NAME = "java:/dynamicextensions";
	/**
	 * JNDI name for transaction manager.
	 */
	public static final String TX_MANAGER_DATASOURCE_JNDI_NAME = "java:/TransactionManager";
	/**
	 * JNDI name for session data bean after user logged in.
	 */
	public static SessionDataBean USER_SESSION_DATA_BEAN;
	/**
	 * JNDI name for context url.
	 */
	public static final String CONTEXT_URL = "http://localhost:8080/catissuecore";
	
	public static final String FREEZER_STORAGETYPE="FREEZER_STORAGETYPE";
	public static final String BOX_STORAGETYPE="BOX_STORAGETYPE";
	
	public static FlexInterface flexInterface;
	
	public static FlexInterface getFlexInterface(HttpSession session) throws Exception
	{
		if(flexInterface==null)
		{
			flexInterface = new FlexInterface();		
		}
		if(USER_SESSION_DATA_BEAN!=null)
		{
			Field sessionField = FlexInterface.class.getDeclaredField("session");
			sessionField.setAccessible(true);
			
			sessionField.set(flexInterface, session);
			flexInterface.setSessionInitializationFalse();
		}
		return flexInterface;
	}
	
	 /**
	 * If query is from grid then create the target object
	 * and set the attributes by iterating over attributes defined in HBM
	 * @param pclass
	 * @param entityName
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public static List createObjectList(String entityName,List rows) throws Exception
	{
		HibernateMetaData hibernateMetaData = HibernateMetaDataFactory.getHibernateMetaData(CommonServiceLocator.getInstance().getAppName());
		PersistentClass pclass = hibernateMetaData.getPersistentClass(entityName);

		List retrunList = new ArrayList();
		List<Field> fieldList = new ArrayList<Field>();
		PersistentClass targetClass = pclass;
		pclass =targetClass;
		/**
		 * Get the field list of Target class
		 */
		getFieldList(pclass,fieldList);
		/**
		 * Loop over all the super classes of target class
		 */
		while(pclass.getSuperclass() != null)
		{
			pclass = pclass.getSuperclass();
			/**
			 * Get the field list of Super class
			 */
			getFieldList(pclass,fieldList);
		}
		/**
		 * get the identifier field
		 */
		String identifier = pclass.getIdentifierProperty().getName();
		Field idField = pclass.getMappedClass().getDeclaredField(identifier);
		idField.setAccessible(true);
		fieldList.add(idField);
		/**
		 * Call caCOREAppServiceDelegator to create domain object instanes
		 */
		retrunList = new CaCoreAppServicesDelegator().createTargetObjectList(entityName, fieldList, rows);
		return retrunList;
	}
	
	public static void getFieldList(PersistentClass pclass,List<Field> fieldList) throws Exception
	{
		Iterator<Property>properties = pclass.getPropertyIterator();
		while(properties.hasNext())
		{
			Property prop =  properties.next();
			if (!prop.getType().isAssociationType())
			{
				String fieldName = prop.getName();
				Field field = pclass.getMappedClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				fieldList.add(field);
			}
		}
	}	
}
