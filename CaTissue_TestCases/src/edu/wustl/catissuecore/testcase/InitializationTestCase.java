package edu.wustl.catissuecore.testcase;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.sql.DataSource;

import org.jboss.naming.NamingContextFactory;
import org.jboss.tm.TransactionManagerService;
import org.jboss.tm.TxManager;
import org.jnp.server.NamingBeanImpl;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;

import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.DataSourceFinder;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
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
public class InitializationTestCase extends CaTissueSuiteBaseTest
{
	private static boolean initComplete= false;

	/**
	 * Create new Data source object.
	 * @return {@link DataSource}
	 * @author Himanshu Aseeja
	 */
	private DataSource getDataSource()
	{
		
		DataSourceFinder.setAllValues();
		DataSource dataSource = null;
		
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
			NamingBeanImpl server = new NamingBeanImpl();
			server.start();
			// Create the initial context from the properties we just created
			Context initialContext = new InitialContext();

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
		}
	}

	/**
	 * test Application Initialization same as Jboss startup.
	 * Create a thread to start JNP server for JNDI data source initialization
	 * Call CaTissueCoreServletCOntextLiatners method
	 *
	 */
	@Test
	public void testApplicationInitialization()
	{
			try
			{
				String applicationHome = ".";

				initCaTissueSuite();

				//System.setProperties(props);
				System.setProperty("gov.nih.nci.security.configFile",
						applicationHome+"/CaTissue_TestCases/ApplicationSecurityConfig.xml");
				System.setProperty("app.propertiesFile",
						applicationHome+"/catissuecore-properties/caTissueCore_Properties.xml");
				System.setProperty("java.security.auth.login.config",
						applicationHome+"/CaTissue_TestCases/test_login.conf");
				System.setProperty("app.domainAuthFilePath",applicationHome+"/catissuecore-properties/AuthenticationDomain.xml");
				//Files added for BulkOperationTestCases
				System.setProperty("bulk.operation.add.csv.file",
						applicationHome+"/CaTissue_TestCases/createMolecularSpecimenEvent.csv");
				System.setProperty("bulk.operation.add.xml.file",
						applicationHome+"/CaTissue_TestCases/createMolecularSpecimenEvent.xml");
				if (!initComplete)
				{
					initComplete = true;

//					Class.forName(DBUtil.class.getName());

					CatissueCoreServletContextListener init =
						new CatissueCoreServletContextListener();
					ApplicationProperties.initBundle("ApplicationResources");
					String path = System.getProperty("app.propertiesFile");
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
					CommonServiceLocator.getInstance().setAppHome(System.getProperty("user.dir"));
					ErrorKey.init("~");
					init.initCatissueParams();
				}
			}
			catch(Exception ex)
			{
				Logger.out.error("ApplicationInitializeTestCase.initTestData()"+ex);
				fail("Application Initiilization failed "+ex.getMessage());
			}
	}
}
