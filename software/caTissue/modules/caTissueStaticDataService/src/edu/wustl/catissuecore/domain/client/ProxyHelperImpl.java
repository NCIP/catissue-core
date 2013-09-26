/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
