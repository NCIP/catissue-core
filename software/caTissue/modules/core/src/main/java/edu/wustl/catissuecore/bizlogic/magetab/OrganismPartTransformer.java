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

}
