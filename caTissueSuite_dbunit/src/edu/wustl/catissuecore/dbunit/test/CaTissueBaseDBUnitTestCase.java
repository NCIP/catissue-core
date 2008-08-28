/**
 * @Class TestDBUnit.java
 * @Author abhijit_naik
 * @Created on Aug 6, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dbunit.JdbcBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.dbunit.util.DecodeUtility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author abhijit_naik
 *
 */
public abstract class CaTissueBaseDBUnitTestCase extends JdbcBasedDBTestCase
{

	private Properties props = null;
	protected static final DatabaseOperation CATISSUE_INSERT = new CatissueDBUnitOperation();
	private Map<String, List<AbstractDomainObject>> objectMap = null;
	private String objectxmlFile="defaultObjects.xml";
	/* (non-Javadoc)
	 * @see org.dbunit.DatabaseTestCase#getDataSet()
	 */
	protected IDataSet getDataSet() throws Exception
	{

		// TODO Auto-generated method stub
		return new FlatXmlDataSet( new FileInputStream("TestDataSet.xml"));
	}

	public CaTissueBaseDBUnitTestCase()
	{
		super();
		initTestData();
	}

	/**
	 * 
	 */
	private void initTestData()
	{
		try
		{
			InitCatissueAppParameters initCatissueAppParam = new InitCatissueAppParameters();
			props = new Properties();
			initCatissueAppParam.initTestData(props);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			fail("Test case failed"+ ex.getMessage());
		}
	}

	protected DatabaseOperation getSetUpOperation() 
	  throws Exception {
//		initTestData();
		String filename = getObjectFile();
		if(filename!=null)
		{
			objectxmlFile = filename;
		}
		
		initializeObjectMap();
		return CATISSUE_INSERT;
	   
	 }
	abstract public String getObjectFile();
	private void initializeObjectMap() throws Exception
	{
		Collection objectColl = DecodeUtility.getObjectListFromFile(objectxmlFile);
		Iterator objectIterator = objectColl.iterator();
		objectMap = new HashMap<String, List<AbstractDomainObject>>();
		while(objectIterator.hasNext())
		{

			AbstractDomainObject object = (AbstractDomainObject) objectIterator.next();
			String keyClassName = object.getClass().getName();
			if (!objectMap.containsKey(keyClassName))
			{

				objectMap.put(keyClassName, new ArrayList<AbstractDomainObject>());
			}
			objectMap.get(keyClassName).add(object);
			
		}
	}
	public List<AbstractDomainObject> getObjectList(Class classObject)
	{
		return objectMap.get(classObject.getName());
	}
	protected DatabaseOperation getTearDownOperation()
	  throws Exception {
		isTrearedDown = true;
	   return DatabaseOperation.NONE;
	}

	public void testDummyCate()
	{
		
		assertTrue("Dummy test case", true);
	}
	/* (non-Javadoc)
	 * @see org.dbunit.JdbcBasedDBTestCase#getConnectionUrl()
	 */
	protected String getConnectionUrl()
	{
		// TODO Auto-generated method stub
		//return "jdbc:mysql://localhost:3307/catissuecore";

		return props.getProperty("database.jdbc.url");
	}

	/* (non-Javadoc)
	 * @see org.dbunit.JdbcBasedDBTestCase#getDriverClass()
	 */
	protected String getDriverClass()
	{
		// TODO Auto-generated method stub
		return "org.gjt.mm.mysql.Driver";
		//return props.getProperty("database.driver");
	}
	 protected String getPassword()
	   {

	      //return "root";
		 return props.getProperty("database.password");
	   }

	   /**
	    * Returns the username for the connection.<br>
	    * Subclasses may override this method to provide a custom username.<br>
	    * Default implementations returns null.
	    */
	   protected String getUsername()
	   {
	      //return "root";
		  return props.getProperty("database.user");
	   }
	   protected void insert(Object object) throws BizLogicException
	   {
		try
		{
			IBizLogic bizLogic =BizLogicFactory.getInstance()
						.getBizLogic(object.getClass().getName());
			bizLogic.insert(object, Constants.HIBERNATE_DAO);
		}
		catch(UserNotAuthorizedException authorizedException)
		{
			throw new BizLogicException(authorizedException.getMessage(),authorizedException);
		}
	   }
	   protected List search(Class clazz) throws BizLogicException
	   {
		try
		{
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			return defaultBizLogic.retrieve(clazz.getName());
		}
		catch(DAOException daoEx)
		{
			throw new BizLogicException(daoEx.getMessage(),daoEx);
		}
	   }

	   protected void update(Object object) throws BizLogicException
	   {

		   HibernateDAO dao = null;
		   try
		   {
				IBizLogic bizLogic =BizLogicFactory.getInstance()
							.getBizLogic(object.getClass().getName());
				Long oldObjectId = ((AbstractDomainObject) object).getId();
				dao=(HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				dao.openSession(null);

				Object oldObject = dao.retrieve(object.getClass().getName(), oldObjectId);
				bizLogic.update(object,oldObject, Constants.HIBERNATE_DAO,null);

		   }
		   catch(UserNotAuthorizedException authorizedException)
		   {
			   throw new BizLogicException(authorizedException.getMessage(),authorizedException);
		   }
		   catch(DAOException exception)
		   {
			   throw new BizLogicException(exception.getMessage(),exception);
		   }
		   finally
		   {
			   try
			   {
				   dao.closeSession();
			   }
			   catch(DAOException exception)
			   {
				   System.out.println(exception.getMessage());
			   }
		   }
	   }

	
	public Map<String, AbstractDomainObject> getObjectMap()
	{
		return objectMap;
	}

	
	private void setObjectMap(Map<String, AbstractDomainObject> objectMap)
	{
		this.objectMap = objectMap;
	}
}
