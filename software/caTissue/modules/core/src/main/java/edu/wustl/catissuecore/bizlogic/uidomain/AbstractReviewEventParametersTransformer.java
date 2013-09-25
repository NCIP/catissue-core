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

import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.catissuecore.domain.ReviewEventParameters;

public abstract class AbstractReviewEventParametersTransformer<U extends SpecimenEventParametersForm, D extends ReviewEventParameters>
extends
AbstractSpecimenEventParametersTransformer<U, D> {

}
