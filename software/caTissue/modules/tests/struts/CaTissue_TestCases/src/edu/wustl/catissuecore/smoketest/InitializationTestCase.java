package edu.wustl.catissuecore.smoketest;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.jboss.naming.NamingContextFactory;
import org.jboss.tm.TransactionManagerService;
import org.jboss.tm.TxManager;
import org.jnp.server.NamingBeanImpl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import edu.wustl.catissuecore.smoketest.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.smoketest.util.DataSourceFinder;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.audit.AuditManager;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains test case for application initialization same as
 * jboss startup.
 * @author sachin_lale
 *
 */
public class InitializationTestCase extends CaTissueSuiteSmokeBaseTest
{

	public InitializationTestCase(String name)
	{
		super(name);
	}
	public InitializationTestCase()
	{
		super();
	}

	private static boolean initComplete= false;

	/**
	 * Create new Data source object.
	 * @return {@link DataSource}
	 * @author Himanshu Aseeja
	 */
	private DataSource getDataSource()
	{

		DataSource dataSource = null;
		DataSourceFinder.setAllValues();

		if(DataSourceFinder.databaseType.equals("mysql"))
		{
			MysqlDataSource mysqlDataSource  = new MysqlDataSource();
		    mysqlDataSource  = new MysqlDataSource();
		    mysqlDataSource.setDatabaseName(DataSourceFinder.databaseName);
		    mysqlDataSource.setServerName(DataSourceFinder.databaseHost);
    	    mysqlDataSource.setPort(DataSourceFinder.port);
		    mysqlDataSource.setUser(DataSourceFinder.databaseUser);
		    mysqlDataSource.setPassword(DataSourceFinder.databasePassword);
		    return mysqlDataSource;
		}
		else if(DataSourceFinder.databaseType.equals("oracle"))
		{
			OracleDataSource oracleDataSource = null;
			try
			{
				oracleDataSource= new OracleDataSource();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			oracleDataSource.setDatabaseName(DataSourceFinder.databaseName);
			oracleDataSource.setServerName(DataSourceFinder.databaseHost);
			oracleDataSource.setPortNumber(DataSourceFinder.port);
			oracleDataSource.setURL("jdbc:oracle:thin:@"+oracleDataSource.getServerName()+":"+oracleDataSource.getPortNumber()+":"+oracleDataSource.getDatabaseName());
			oracleDataSource.setUser(DataSourceFinder.databaseUser);
			oracleDataSource.setPassword(DataSourceFinder.databasePassword);
			return oracleDataSource;
		}
		return dataSource;
	}

	/**
	 * Initiailize and start JNP server.
	 */
	private void initCaTissueSuite()
	{
		try
		{
//			Logger.configure("");
			// Create a Properties object and set properties appropriately
			System.setProperty("java.naming.factory.initial", NamingContextFactory.class.getName());
			System.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			// start JNDI server
			System.out.println("befor NamingBeanImpl");
			NamingBeanImpl server = new NamingBeanImpl();
			server.start();
			System.out.println("after NamingBeanImpl");
			// Create the initial context from the properties we just created
			System.out.println("befor InitialContext");
			Context initialContext = new InitialContext();
			System.out.println("after InitialContext");
			// create data source and bind to JNDI
			DataSource caTissueDS = getDataSource();

			DataSource dynamicExtnDS = getDataSource();
			initialContext.createSubcontext(CaTissueSuiteTestUtil.CATISSUE_DATASOURCE_JNDI_NAME);
			initialContext.createSubcontext(CaTissueSuiteTestUtil.DE_DATASOURCE_JNDI_NAME);
			initialContext.createSubcontext(CaTissueSuiteTestUtil.TX_MANAGER_DATASOURCE_JNDI_NAME);
			initialContext.rebind(CaTissueSuiteTestUtil.CATISSUE_DATASOURCE_JNDI_NAME, caTissueDS);
			initialContext.rebind(CaTissueSuiteTestUtil.DE_DATASOURCE_JNDI_NAME, dynamicExtnDS);

			TxManager.getInstance();
			Reference ref = new Reference("org.jboss.tm.TxManager",
					TransactionManagerService.class.getName(), null);
			initialContext.rebind(CaTissueSuiteTestUtil.TX_MANAGER_DATASOURCE_JNDI_NAME, ref);
		}
		catch (Exception ex)
		{
			Logger.out.error("ApplicationInitializeTestCase.testAndInitialize()"+ex.getMessage());
			fail("Application Initilization fail"+ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * test Application Initialization same as Jboss startup.
	 * Create a thread to start JNP server for JNDI data source initialization
	 * Call CaTissueCoreServletCOntextLiatners method
	 *
	 */
	public void testApplicationInitialization()
	{
			try
			{
				String applicationHome = "./modules/caTissue/conf/";
				String testHome = ".";
				String CDEconfigPath = "./deploytempCatissuecore/catissuecore";
				File file = new File("./modules/caTissue/conf/catissuecore-properties/caTissueCore_Properties.xml");
				if(!file.exists())
				{
					applicationHome = System.getProperty("user.dir")+"/software/catissue/src/conf";
					testHome = System.getProperty("user.dir")+"/software/catissue/test/struts";
					CDEconfigPath = applicationHome;
				}
				System.out.println("System.getProperty(user.dir  " + System.getProperty("user.dir"));
				System.out.println("System.getProperty(user.dir  " + System.getProperty("user.dir"));
				String applicationPropertiesHome = applicationHome+"/catissuecore-properties";

				initCaTissueSuite();
				System.out.println("applicationPropertiesHome  "+applicationPropertiesHome);
				//System.setProperties(props);
				System.setProperty("gov.nih.nci.security.configFile",
						testHome+"/CaTissue_TestCases/ApplicationSecurityConfig.xml");
				System.setProperty("app.propertiesFile",
						applicationPropertiesHome+"/caTissueCore_Properties.xml");
				System.setProperty("java.security.auth.login.config",
						testHome+"/CaTissue_TestCases/test_login.conf");
				System.setProperty("app.domainAuthFilePath",applicationPropertiesHome+"/IDPAuthentication.xml");
				//Files added for BulkOperationTestCases
				if (!initComplete)
				{
					initComplete = true;

//					Class.forName(DBUtil.class.getName());

					CatissueCoreServletContextListener init =
						new CatissueCoreServletContextListener();
					ApplicationProperties.initBundle("ApplicationResources");
					String path = System.getProperty("app.propertiesFile");
					System.out.println("Path : "+ path);
			    	XMLPropertyHandler.init(path);
			    	File propetiesDirPath = new File(path);
			    	String propertiesDirPath = propetiesDirPath.getParent();
			    	if (propertiesDirPath.startsWith("file:\\"))
			    	{
			    		int beginIndex = "file:\\".length();
			    		propertiesDirPath=
			    			propertiesDirPath.substring(beginIndex);
			    	}
			    	String applicationName = ApplicationProperties.getValue("app.name");
			        String applicationVersion = ApplicationProperties.getValue("app.version");
					int maximumTreeNodeLimit = Integer.parseInt(XMLPropertyHandler.getValue
							(Constants.MAXIMUM_TREE_NODE_LIMIT));
					Variables.maximumTreeNodeLimit = maximumTreeNodeLimit;
					CommonServiceLocator.getInstance().setAppHome(CDEconfigPath);
					ErrorKey.init("~");
					AuditManager.init();
					init.initCatissueParams();
					DefaultValueManager.validateAndInitDefaultValueMap();
				}
			}
			catch(Exception ex)
			{
				System.out.println("String appHome= "+CommonServiceLocator.getInstance().getAppHome());
				Logger.out.error("ApplicationInitializeTestCase.initTestData()"+ex);
				ex.printStackTrace();
				fail("Application Initiilization failed "+ex.getMessage());
			}
	}
}
