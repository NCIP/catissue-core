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
