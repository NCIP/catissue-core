package edu.wustl.catissuecore.domain.util;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import org.globus.gsi.GlobusCredential;

/**
 * @author Ion C. Olaru
 */
public class Service {

    public static final CaTissueWritableAppService getService(GlobusCredential gc) throws Exception {
        return (CaTissueWritableAppService)ApplicationServiceProvider.getApplicationService(gc);
    }

    public static final CaTissueWritableAppService getService(String username, String password) throws Exception {
        CaTissueWritableAppService service = null;
        try {
            service = (CaTissueWritableAppService) ApplicationServiceProvider.getApplicationService(username, password);
        } catch (Exception e) {
            if (service == null) {
                try {
                    service = (CaTissueWritableAppService)ApplicationServiceProvider.getApplicationService(username, password);
                } catch (Exception e1) {
                    throw new Exception(e1);
                }
            }
        }
        return service;
    }

}
