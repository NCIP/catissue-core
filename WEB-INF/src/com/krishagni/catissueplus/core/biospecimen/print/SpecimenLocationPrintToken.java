package com.krishagni.catissueplus.core.biospecimen.print;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;
import com.krishagni.catissueplus.core.common.util.MessageUtil;

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
			return MessageUtil.getInstance().getMessage("specimen_virtual");
		}
		
		return new StringBuilder(position.getContainer().getName()).append(" (")
			.append(position.getPosTwo()).append(" x ")
			.append(position.getPosOne()).append(")")
			.toString();
	}
}
