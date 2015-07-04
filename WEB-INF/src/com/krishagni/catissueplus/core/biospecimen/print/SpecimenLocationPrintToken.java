package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class SpecimenLocationPrintToken extends AbstractLabelTmplToken implements LabelTmplToken {

	@Override
	public String getName() {
		return "specimen_location";
	}

	@Override
	public String getReplacement(Object object) {
		Specimen specimen = (Specimen)object;
		StorageContainerPosition position = specimen.getPosition();
		if (position == null) {
			return "Virtual"; // TODO: pick from locale
		}
		
		return new StringBuilder(position.getContainer().getName())
			.append(" (")
			.append(position.getPosOne())
			.append(" x ")
			.append(position.getPosTwo())
			.append(")")
			.toString();
	}
}
