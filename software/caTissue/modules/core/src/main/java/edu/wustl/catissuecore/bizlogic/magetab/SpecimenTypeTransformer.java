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

public class SpecimenTypeTransformer extends AbstractCharacteristicTransformer {
	
	public SpecimenTypeTransformer(CharacteristicTransformerConfig config) {
		super("SpecimenType", "Specimen Type","Specimen Type", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		return specimen.getSpecimenType();
	}

	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
