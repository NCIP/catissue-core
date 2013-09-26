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

import java.util.List;

public interface IAddArrayableOrderDetailsForm extends IAddOrderDetailsForm {

    /**
     * @return List of defineArrayObj
     */
    public abstract List getDefineArrayObj();
}
