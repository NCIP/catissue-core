package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.AbbreviationConfig;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken {

    private AbbreviationConfig abbreviationConfig;

    public void setAbbreviationConfig(AbbreviationConfig abbreviationConfig) {
        this.abbreviationConfig = abbreviationConfig;
    }

    public SpecimenPathologyStatusToken() {
        this.name = "SP_PATH_STATUS";
    }

    @Override
    public String getLabel(Specimen specimen) {

        String abbreviationValue = abbreviationConfig.getAbbreviation(SPECIMEN_PATH_STATUS, specimen.getPathologicalStatus()); 
        if (!StringUtils.isBlank(abbreviationValue)) {
            return abbreviationValue;
        } else {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getPathologicalStatus(),
                SPECIMEN_PATH_STATUS.replace("_", " "));
        }
    }

    private static final String SPECIMEN_PATH_STATUS = "Specimen_Pathology_Status";
}
