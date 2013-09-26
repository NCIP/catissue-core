/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Map;

public interface IAddOrderDetailsForm {
    /**
     * @return name of distribution protocol
     */
    public abstract String getDistrbutionProtocol();

    /**
     * @return object of OrderForm
     */
    public abstract OrderForm getOrderForm();

    /**
     * @return map values
     */
    public abstract Map getValues();

}