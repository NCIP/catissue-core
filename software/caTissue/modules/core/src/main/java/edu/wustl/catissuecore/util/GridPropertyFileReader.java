package edu.wustl.catissuecore.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.wustl.catissuecore.gridgrouper.GridGrouperConstant;
import edu.wustl.catissuecore.gridgrouper.GridGrouperUtil;

public class GridPropertyFileReader {
	//private static String CAGRID_VERSION = "1.3";
	private static Logger LOG = Logger.getLogger(GridGrouperUtil.class);
	
	public static Properties serviceUrls() {
		Properties defaultProps = configuredProperties();
		String targetGrid=defaultProps.getProperty(GridGrouperConstant.GG_TARGET_GRID);
		//TargetGrid targetGrid = TargetGrid.byName(tg);
		String jbossHome = defaultProps.getProperty(GridGrouperConstant.JBOSS_HOME);
		String serviceUrlsFile = jbossHome + "/certificates/"+targetGrid+"/service_urls-"+targetGrid+".properties";
		
		InputStream in = null;
		Properties serviceUrls = new Properties();
		try {
			in = new FileInputStream(serviceUrlsFile);
			serviceUrls.load(in);
		} catch (IOException e) {
			LOG.error(GridGrouperConstant.GG_PROPERTIES_LOAD_ERROR, e);
			serviceUrls = null;		
		}
		return serviceUrls;
	}
	
	public static Properties configuredProperties() {
		Properties defaultProps = new Properties();
		InputStream in = null;
		in = GridCertificateSynchronization.class.getClassLoader().getResourceAsStream(GridGrouperConstant.GG_PROPERTIES_FILE);
		try {
			defaultProps.load(in);
		} catch (IOException e) {
			LOG.error(GridGrouperConstant.GG_PROPERTIES_LOAD_ERROR, e);
			defaultProps = null;

		}
		return defaultProps;
	}
	
}
