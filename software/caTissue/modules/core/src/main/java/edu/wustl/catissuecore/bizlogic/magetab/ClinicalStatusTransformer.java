package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class ClinicalStatusTransformer extends AbstractCharacteristicTransformer {

	public ClinicalStatusTransformer() {
		super("Clinical Status", "Clinical Status", "Clinical Status");
	}
	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
		// TODO Auto-generated method stub
		return specimen.getSpecimenCollectionGroup().getClinicalStatus();
	}

	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}
}
