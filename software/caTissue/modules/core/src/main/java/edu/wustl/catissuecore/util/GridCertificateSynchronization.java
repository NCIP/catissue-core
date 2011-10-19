package edu.wustl.catissuecore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.wustl.catissuecore.gridgrouper.GridGrouperConstant;
import edu.wustl.catissuecore.gridgrouper.GridGrouperUtil;
import gov.nih.nci.cacoresdk.util.GridAuthenticationClient;


public class GridCertificateSynchronization {

	private static Logger LOG = Logger.getLogger(GridGrouperUtil.class);
	//private static String CAGRID_VERSION = "1.3";
	
	public GridCertificateSynchronization () {
		
	}
		
	/**************************************
	 * sync certs ... 
	 */
	public void sync() {
		Properties defaultProps = GridPropertyFileReader.configuredProperties();
		String targetGrid=defaultProps.getProperty(GridGrouperConstant.GG_TARGET_GRID);
		//TargetGrid targetGrid = TargetGrid.byName(tg);
		String jbossHome = defaultProps.getProperty(GridGrouperConstant.JBOSS_HOME);

		String certificateDirName = jbossHome + "/certificates/"+targetGrid.toString()+"/certificates";
		String syncDescFile = jbossHome + "/certificates/"+targetGrid+"/sync-description-"+targetGrid+".xml";
		try {
			installRootCerts(certificateDirName);
			syncTrust(syncDescFile);
			
		} catch (Exception e) {
			LOG.error(GridGrouperConstant.GLOBUS_INIT_ERROR, e);
		}
	}

	private static void syncTrust(String syncDescFile) {
		//System.out.println("***********"+GSID_SYNC_DESC_FILE);
		LOG.debug(GridGrouperConstant.GG_SYNCHRONIZE_START_MSG);
		try
		{
			GridAuthenticationClient
				.synchronizeOnce(syncDescFile);
		}catch(Exception e)
		{
			LOG.debug(GridGrouperConstant.GG_SYNCHRONIZE_ERROR_MSG,e);
		}
		LOG.debug(GridGrouperConstant.GG_SYNCHRONIZE_COMPLETE_MSG);
	}
	
	private static void installRootCerts(String certificateDirName) throws Exception {

		String targetDirectoryName = System.getProperty("user.home")+"/.globus/certificates";
		File sourceDir = new File(certificateDirName);
		File targetDir = new File(targetDirectoryName);
		copyDirectory(sourceDir, targetDir);	
	}

	private static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
    	//System.out.println("COPY ING " + sourceLocation.getAbsolutePath() +   "   " + targetLocation.getAbsolutePath());
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }




	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		GridCertificateSynchronization s = new GridCertificateSynchronization();
		s.sync();
	}
		
}
