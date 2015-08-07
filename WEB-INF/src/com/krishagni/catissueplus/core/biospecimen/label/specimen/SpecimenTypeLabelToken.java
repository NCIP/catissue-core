package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.AbbreviationConfig;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {

    private AbbreviationConfig abbreviationConfig;
 
    public SpecimenTypeLabelToken() {
        this.name = "SP_TYPE";
    }

    public void setAbbreviationMap(AbbreviationConfig abbreviationConfig) {
        this.abbreviationConfig = abbreviationConfig;
    }

    @Override
    public String getLabel(Specimen specimen) {
        String abbreviationValue = abbreviationConfig.getAbbreviation(SPECIMEN_TYPE, specimen.getSpecimenType());
        if (!StringUtils.isBlank(abbreviationValue)) {
            return abbreviationValue;
        } else {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getPathologicalStatus(),
                SPECIMEN_TYPE.replace("_", " "));
        }
    }

    private static final String SPECIMEN_TYPE = "Specimen_Type";
}
