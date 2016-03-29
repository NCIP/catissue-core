package com.krishagni.openspecimen.rde.services.impl;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.common.domain.LabelTmplTokenRegistrar;
import com.krishagni.openspecimen.rde.tokens.impl.CohortBarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.CpSiteCodeBarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.EventCodeBarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.PpidBarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.SrCodeBarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.VisitYear2BarcodeToken;
import com.krishagni.openspecimen.rde.tokens.impl.VisitYearBarcodeToken;

public class PluginInitializer implements InitializingBean {
	
	private LabelTmplTokenRegistrar visitNameTokenRegistrar;
	
	private LabelTmplTokenRegistrar spmnLabelTokenRegistrar;

	public void setVisitNameTokenRegistrar(LabelTmplTokenRegistrar visitNameTokenRegistrar) {
		this.visitNameTokenRegistrar = visitNameTokenRegistrar;
	}

	public void setSpmnLabelTokenRegistrar(LabelTmplTokenRegistrar spmnLabelTokenRegistrar) {
		this.spmnLabelTokenRegistrar = spmnLabelTokenRegistrar;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		visitNameTokenRegistrar.register(new PpidBarcodeToken());
		visitNameTokenRegistrar.register(new EventCodeBarcodeToken());
		visitNameTokenRegistrar.register(new CpSiteCodeBarcodeToken());
		visitNameTokenRegistrar.register(new CohortBarcodeToken());
		visitNameTokenRegistrar.register(new VisitYearBarcodeToken());
		visitNameTokenRegistrar.register(new VisitYear2BarcodeToken());
		
		spmnLabelTokenRegistrar.register(new SrCodeBarcodeToken());
	}
}
