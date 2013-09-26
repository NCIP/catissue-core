/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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

    public static final CaTissueWritableAppService getService(String url, String username, String password) throws Exception {
        CaTissueWritableAppService service = null;
        try {
            service = (CaTissueWritableAppService) ApplicationServiceProvider.getApplicationServiceFromUrl(url, username, password);
        } catch (Exception e) {
            if (service == null) {
                try {
                    service = (CaTissueWritableAppService)ApplicationServiceProvider.getApplicationServiceFromUrl(url, username, password);
                } catch (Exception e1) {
                    throw new Exception(e1);
                }
            }
        }
        return service;
    }

}
