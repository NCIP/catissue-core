package com.krishagni.catissueplus.core.de.ui;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.domain.nui.AbstractLookupControl;

public class UserControl extends AbstractLookupControl {
	private static final long serialVersionUID = 1L;
	
	private static final String LU_TABLE = "USER_VIEW";
	
	private static final String ALT_KEY = "email_address";
	
	private static final Properties LU_PV_SOURCE_PROPS = initPvSourceProps();

	@Override
	public String getCtrlType() {
		return "userField";
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("apiUrl", "rest/ng/users");
		props.put("dataType", getDataType());
	}
	
	public void serializeToXml(Writer writer, Properties props) {
		super.serializeToXml("userField", writer, props);
	}

	@Override
	public String getTableName() {		
		return LU_TABLE;
	}

	@Override
	public Properties getPvSourceProps() {
		return LU_PV_SOURCE_PROPS;
	}
	
	@Override
	public String getAltKeyColumn() {		
		return ALT_KEY;
	}
	
	private static Properties initPvSourceProps() {
		Properties props = new Properties();
		props.put("apiUrl", "rest/ng/users");
		props.put("searchTermName", "searchString");
		props.put("resultFormat", "{{firstName}} {{lastName}}");
		props.put("respField", "users");
		
		return props;
	}
}