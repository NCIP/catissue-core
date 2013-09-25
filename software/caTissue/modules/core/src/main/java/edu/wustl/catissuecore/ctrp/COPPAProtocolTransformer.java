/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.ctrp;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import gov.nih.nci.coppa.services.pa.StudyProtocol;

public class COPPAProtocolTransformer implements COPPATransformer {

	public CollectionProtocol transform(StudyProtocol studyProtocol) throws Exception {
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setTitle(COPPAUtil.getTitle(studyProtocol));
		collectionProtocol.setShortTitle((COPPAUtil.getShortTitle(studyProtocol)));
		collectionProtocol.setStartDate((COPPAUtil.getStartDate(studyProtocol)));
		
		
		return collectionProtocol;
	}

	@Override
	public Object transform(Object obj) throws Exception {
		return (transform((StudyProtocol) obj));
	}

}
