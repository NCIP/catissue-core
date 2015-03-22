package com.krishagni.catissueplus.core.de.ui;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.AbstractControlFactory;

public class StorageContainerControlFactory extends AbstractControlFactory {
	public static StorageContainerControlFactory getInstance() {
		return new StorageContainerControlFactory();
	}
	
	@Override
	public String getType() {
		return "storageContainer";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		StorageContainerControl ctrl = new StorageContainerControl();
		super.setControlProps(ctrl, ele, row, xPos);
		return ctrl;
	}
}
