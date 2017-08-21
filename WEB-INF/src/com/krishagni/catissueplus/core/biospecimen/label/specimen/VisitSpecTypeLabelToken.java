package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.LabelTmplToken;

public class VisitSpecTypeLabelToken extends AbstractSpecimenLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public VisitSpecTypeLabelToken() {
		this.name = "VISIT_SP_TYPE_UID";
	}

	@Override
	public String getLabel(Specimen specimen) {
		String visitName = specimen.getVisit().getName();
		String key = visitName + "_" + specimen.getSpecimenType();
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, key);

		return uniqueId == 1L ? LabelTmplToken.EMPTY_VALUE : uniqueId.toString();
	}

}
