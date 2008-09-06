/**
 * @Class TestDBUnit.java
 * @Author abhijit_naik
 * @Created on Aug 6, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.io.FileInputStream;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.dbunit.util.DecodeUtility;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;


/**
 * @author abhijit_naik
 *
 */
public abstract class CaTissueBaseDBUnitTestCase extends JdbcBasedDBTestCase
{

	/**
	 * 
	 */
	protected static final String DEFAULT_OBJECTS_XML_FILE = "defaultObjects.xml";
	private Properties props = null;
	protected static final DatabaseOperation CATISSUE_INSERT = new CatissueDBUnitOperation();
	private Map<String, List<AbstractDomainObject>> objectMap = null;
	private String objectxmlFile=DEFAULT_OBJECTS_XML_FILE;
	private Long userId = Long.valueOf(1);
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
		System.out.println("sssssssssss constructor is called...");
		initTestData();
		System.out.println("After init testdata() is called...");
		String filename = getObjectFile();
		if(filename!=null)
		{
			objectxmlFile = filename;
		}
		
		initializeObjectMap();
		System.out.println("After initializeObjectMAp is called...");
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
		return CATISSUE_INSERT;
	   
	 }
	abstract public String getObjectFile();
	
	private void initializeObjectMap()
	{
		try
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
		}catch(Exception e){
			e.printStackTrace();
			fail("Test failed in initialize object map");
		}
	}
	public List<AbstractDomainObject> getObjectList(Class classObject)
	{
		return objectMap.get(classObject.getName());
	}
	protected DatabaseOperation getTearDownOperation()
	  throws Exception {
	   return DatabaseOperation.NONE;
	}

	public void testDummyCase()
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
			SessionDataBean sessionDataBean = getSessionDataBean();
			System.out.println("............"+sessionDataBean);
			IBizLogic bizLogic =BizLogicFactory.getInstance()
						.getBizLogic(object.getClass().getName());
			System.out.println("............"+object);
			bizLogic.insert(object,sessionDataBean, Constants.HIBERNATE_DAO);
			System.out.println(".......inserted.....");
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

		   Session session = null;
		   try
		   {
				IBizLogic bizLogic =BizLogicFactory.getInstance()
							.getBizLogic(object.getClass().getName());
				Long oldObjectId = ((AbstractDomainObject) object).getId();
				SessionDataBean sessionDataBean = getSessionDataBean();
				session = DBUtil.getCleanSession();

				Object oldObject = session.get(object.getClass().getName(), oldObjectId);
				bizLogic.update(object,oldObject, Constants.HIBERNATE_DAO,sessionDataBean);

		   }
		   catch(UserNotAuthorizedException authorizedException)
		   {
			   throw new BizLogicException(authorizedException.getMessage(),authorizedException);
		   }
		   catch(HibernateException exception)
		   {
			   throw new BizLogicException(exception.getMessage(),exception);
		   }
		   finally
		   {
			   try
			   {
				   session.close();
			   }
			   catch(HibernateException exception)
			   {
				   System.out.println(exception.getMessage());
			   }
		   }
	   }

	
	public Map<String, List<AbstractDomainObject>> getObjectMap()
	{
		return objectMap;
	}

	
	private void setObjectMap(Map<String, List<AbstractDomainObject>> objectMap)
	{
		this.objectMap = objectMap;
	}

	
	public Long getUserId()
	{
		return userId;
	}

	
	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
	
	public SessionDataBean getSessionDataBean()
	{
		SessionDataBean sessionDataBean = null;;
		try
		{
			DefaultBizLogic bizLogic = new DefaultBizLogic();
			User user = (User)bizLogic.retrieve(User.class.getName(),userId);
			sessionDataBean = new SessionDataBean();
			sessionDataBean.setAdmin(true);
			sessionDataBean.setCsmUserId(String.valueOf(user.getCsmUserId()));
			sessionDataBean.setFirstName(user.getFirstName());
			sessionDataBean.setIpAddress("localhost");
			sessionDataBean.setLastName(user.getLastName());
			sessionDataBean.setUserId(userId);
			sessionDataBean.setUserName(user.getEmailAddress());
		}
		catch(DAOException e){
			e.printStackTrace();
			fail("failed to retrieve user id " + userId);
		}
		
		return sessionDataBean;
	}
}
