package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class BarcodeLabelToken extends AbstractSpecimenLabelToken {
	
	public BarcodeLabelToken() {
		this.name = "BARCODE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		return specimen.getBarcode();
	}
}
