package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class SexTransformer extends AbstractCharacteristicTransformer {
	
	public SexTransformer(CharacteristicTransformerConfig config) {
		super("Sex", "Sex", "Gender", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		return specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant().getGender();
	}

}
