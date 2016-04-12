package com.krishagni.catissueplus.core.administrative.label.container;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;

public class SiteCodeLabelToken extends AbstractContainerLabelToken {

    public SiteCodeLabelToken() {
        this.name = "SITE_CODE";
    }

    @Override
    public String getLabel(StorageContainer container) {
        String code = container.getSite().getCode();
        if (StringUtils.isBlank(code)) {
            code = StringUtils.EMPTY;
        }

        return code;
    }
}
