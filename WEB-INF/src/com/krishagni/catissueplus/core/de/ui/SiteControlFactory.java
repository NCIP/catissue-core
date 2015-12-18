package com.krishagni.catissueplus.core.de.ui;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.AbstractControlFactory;

public class SiteControlFactory extends AbstractControlFactory {
	public static SiteControlFactory getInstance() {
		return new SiteControlFactory();
	}
	
	@Override
	public String getType() {
		return "siteField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		SiteControl ctrl = new SiteControl();
		super.setControlProps(ctrl, ele, row, xPos);
		return ctrl;
	}

}
