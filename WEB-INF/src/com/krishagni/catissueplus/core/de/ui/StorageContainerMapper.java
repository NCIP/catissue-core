package com.krishagni.catissueplus.core.de.ui;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.wustl.dynamicextensions.formdesigner.mapper.DefaultControlMapper;
import edu.wustl.dynamicextensions.formdesigner.mapper.Properties;

public class StorageContainerMapper extends DefaultControlMapper {
	@Override
	public Control propertiesToControl(Properties controlProps, Container container) throws Exception {
		StorageContainerControl control = new StorageContainerControl();
		setCommonProperties(controlProps, control);
		return control;
	}

	@Override
	public Properties controlToProperties(Control control, Container container) {
		Properties controlProps = new Properties();
		controlProps.setProperty("type", "fancyControl");
		controlProps.setProperty("fancyControlType", "storageContainer");
		getCommonProperties(controlProps, control);
		return controlProps;
	}
}
