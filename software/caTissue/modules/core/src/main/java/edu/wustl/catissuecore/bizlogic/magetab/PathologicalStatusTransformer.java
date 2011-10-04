package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class PathologicalStatusTransformer extends AbstractCharacteristicTransformer {
	
	public PathologicalStatusTransformer(CharacteristicTransformerConfig config) {
		super("PathologicalStatus", "Pathological Status","Pathological Status", config);
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		return specimen.getPathologicalStatus();
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}

}
