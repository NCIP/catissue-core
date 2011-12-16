package edu.wustl.catissuecore.domain.client;

import gov.nih.nci.system.applicationservice.ApplicationService;

/**
 * @author Ion C. Olaru
 *         Date: 12/16/11 -11:16 AM
 */
public class ProxyHelperImpl extends gov.nih.nci.system.client.proxy.ProxyHelperImpl {
    @Override
    protected Object convertObjectToProxy(ApplicationService as, Object obj) {
/*
        System.out.println(">>>>>>>>>> " + obj.getClass());
        System.out.println(">>>" + obj);
        System.out.println(">>>>>>>>>>");
*/
        return obj;
    }
}
