package com.krishagni.catissueplus.core.biospecimen.label;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AbbreviationErrorCode implements ErrorCode {
    ABBR_VALUE_NOT_FOUND;

    @Override
    public String code() {
        return this.name();
    }

}
