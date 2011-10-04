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
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return true;
	}

}
