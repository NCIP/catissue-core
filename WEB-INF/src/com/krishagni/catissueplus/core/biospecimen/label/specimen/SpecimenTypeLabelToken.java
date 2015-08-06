package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.AbbreviationMap;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken {

    private AbbreviationMap abbreviationMap;
 
    public SpecimenTypeLabelToken() {
        this.name = "SP_TYPE";
    }

    public void setAbbreviationMap(AbbreviationMap abbreviationMap) {
        this.abbreviationMap = abbreviationMap;
    }

    @Override
    public String getLabel(Specimen specimen) {
        Map<String, Map<String, String>> allAbbrValuesMap = abbreviationMap.getAbbreviationMap();

        Map<String, String> spTypeMap = allAbbrValuesMap.get(SPECIMEN_TYPE);

        if (!spTypeMap.containsKey(specimen.getSpecimenType())) {
            throw OpenSpecimenException.userError(
                AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getSpecimenType(), SPECIMEN_TYPE.replace("_", " "));
        } else {
            return spTypeMap.get(specimen.getSpecimenType());
        }
    }

    private static final String SPECIMEN_TYPE = "Specimen_Type";
}
