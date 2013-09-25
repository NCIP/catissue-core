/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class GsIdTransformer extends AbstractCharacteristicTransformer {
	
	public GsIdTransformer() {
		super("Global Specimen Identifier", "Global Specimen Identifier", "Global Specimen Identifier");
	}

	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
		if (specimen.getGlobalSpecimenIdentifier()==null) return "";
		return specimen.getGlobalSpecimenIdentifier();
	}

	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}
}
