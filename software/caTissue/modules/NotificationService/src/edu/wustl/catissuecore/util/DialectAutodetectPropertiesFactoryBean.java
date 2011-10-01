/**
 * 
 */
package edu.wustl.catissuecore.util;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ReflectionUtils;

/**
 * @author Denis G. Krylov
 * 
 */
public class DialectAutodetectPropertiesFactoryBean implements FactoryBean {

	private static final Log log = LogFactory
			.getLog(DialectAutodetectPropertiesFactoryBean.class);

	private String dialectName = "org.hibernate.dialect.Oracle9iDialect";
	private Properties properties;
	private DataSource dataSource;

	public DialectAutodetectPropertiesFactoryBean() {
		properties = new Properties();
	}

	public Object getObject() throws Exception {
		autoDetectDialect();
		if (dialectName != null) {
			properties.setProperty("hibernate.dialect", dialectName);
		}
		return properties;
	}

	private void autoDetectDialect() {
		if (dataSource != null) {
			try {
				Field mcf = ReflectionUtils
						.findField(
								Thread.currentThread()
										.getContextClassLoader()
										.loadClass(
												"org.jboss.resource.adapter.jdbc.WrapperDataSource"),
								"mcf");
				ReflectionUtils.makeAccessible(mcf);
				Object baseWrapperManagedConnectionFactory = mcf
						.get(dataSource);
				String connectionURL = (String) PropertyUtils.getProperty(
						baseWrapperManagedConnectionFactory, "connectionURL");
				if (connectionURL.contains("mysql")) {
					dialectName = "org.hibernate.dialect.MySQLDialect";
				}
			} catch (Exception e) {
				log.error("Unable to auto-detect the database vendor; defaulting dialect to "
						+ dialectName);
				log.error(e, e);
			}
		}
	}

	public Class getObjectType() {
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	// //// CONFIGURATION

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getDialectName() {
		return dialectName;
	}

	public void setDialectName(String dialectName) {
		this.dialectName = dialectName;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
