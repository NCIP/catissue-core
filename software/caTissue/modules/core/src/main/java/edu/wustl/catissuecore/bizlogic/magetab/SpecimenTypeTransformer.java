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

}
