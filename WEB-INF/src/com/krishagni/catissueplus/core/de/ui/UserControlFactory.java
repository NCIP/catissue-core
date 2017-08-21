package com.krishagni.catissueplus.core.de.ui;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.AbstractControlFactory;

public class UserControlFactory extends AbstractControlFactory {
	public static UserControlFactory getInstance() {
		return new UserControlFactory();
	}
	
	@Override
	public String getType() {
		return "userField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		UserControl ctrl = new UserControl();
		super.setControlProps(ctrl, ele, row, xPos);
		return ctrl;
	}
}
