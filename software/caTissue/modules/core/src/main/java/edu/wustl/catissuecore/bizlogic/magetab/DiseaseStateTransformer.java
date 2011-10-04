package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class DiseaseStateTransformer extends AbstractCharacteristicTransformer {
	
	public DiseaseStateTransformer(CharacteristicTransformerConfig config) {
		super("DiseaseState", "Disease State","Clinical Diagnosis", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		return specimen.getSpecimenCollectionGroup().getClinicalDiagnosis();
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
