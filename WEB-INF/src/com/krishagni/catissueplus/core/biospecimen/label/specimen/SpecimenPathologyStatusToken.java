package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.AbbreviationMap;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken {

    private AbbreviationMap abbreviationMap;

    public void setAbbreviationMap(AbbreviationMap abbreviationMap) {
        this.abbreviationMap = abbreviationMap;
    }

    public SpecimenPathologyStatusToken() {
        this.name = "SP_PATH_STATUS";
    }

    @Override
    public String getLabel(Specimen specimen) {

        Map<String, Map<String, String>> allAbbrValuesMap = abbreviationMap.getAbbreviationMap();

        Map<String, String> spPathStatusAbbrMap = allAbbrValuesMap.get(SPECIMEN_PATH_STATUS);

        if (!spPathStatusAbbrMap.containsKey(specimen.getPathologicalStatus())) {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getPathologicalStatus(),
                SPECIMEN_PATH_STATUS.replace("_", " "));
        } else {
            return spPathStatusAbbrMap.get(specimen.getPathologicalStatus());
        }
    }

    private static final String SPECIMEN_PATH_STATUS = "Specimen_Pathology_Status";
}
