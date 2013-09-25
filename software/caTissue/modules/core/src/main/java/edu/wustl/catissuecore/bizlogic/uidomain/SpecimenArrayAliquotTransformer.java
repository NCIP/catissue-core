/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.uidomain;

import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;

@InputUIRepOfDomain(SpecimenArrayAliquotForm.class)
public class SpecimenArrayAliquotTransformer extends AbstractSpecimenArrayTransformer<SpecimenArrayAliquotForm> {

    @Override
    public void overwriteDomainObject(SpecimenArray domainObject, SpecimenArrayAliquotForm uiRepOfDomain) {
        super.overwriteDomainObject(domainObject, uiRepOfDomain);
        domainObject.setId(Long.valueOf(uiRepOfDomain.getSpecimenArrayId()));
    }

}
