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

import edu.wustl.catissuecore.domain.Institution;
import gov.nih.nci.coppa.po.Organization;

public class COPPAInstitutionTransformer implements COPPATransformer {

	public Institution transform(Organization org) throws Exception {
		Institution remoteInstituion = new Institution();

		remoteInstituion.setRemoteId(new Long(org.getIdentifier()
				.getExtension()).longValue());
		remoteInstituion.setName(org.getName().getPart().get(0).getValue());
		remoteInstituion.setDirtyEditFlag(false);
		remoteInstituion.setRemoteManagedFlag(true);
		return remoteInstituion;
	}

	@Override
	public Object transform(Object obj) throws Exception {
		return (transform((Organization) obj));
	}

}
