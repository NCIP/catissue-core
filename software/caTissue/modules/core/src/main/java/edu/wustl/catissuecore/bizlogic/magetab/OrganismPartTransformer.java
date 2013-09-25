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

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;

public class OrganismPartTransformer extends AbstractCharacteristicTransformer {
	
	public OrganismPartTransformer(CharacteristicTransformerConfig config) {
		super("OrganismPart", "Organism Part", "Tissue Site", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		SpecimenCharacteristics characteristics = specimen.getSpecimenCharacteristics();
		if (characteristics != null) {
			return characteristics.getTissueSite();
		} else {
			return "";
		}
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}

}
