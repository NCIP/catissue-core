package com.krishagni.catissueplus.core.de.ui;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.domain.nui.ColumnDef;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.LookupControl;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class UserControl extends Control implements LookupControl {
	private static final long serialVersionUID = 1L;
	
	private static final String LU_TABLE = "USER_VIEW";
	
	private static final String LU_KEY_COLUMN = "IDENTIFIER";
	
	private static final String LU_VALUE_COLUMN = "NAME";
	
	private static final Properties LU_PV_SOURCE_PROPS = initPvSourceProps();
	
	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getIntegerColType()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		return new BigDecimal(value).longValue();
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("type", "userField");
		props.put("apiUrl", "rest/ng/users");
		props.put("dataType", getDataType());
	}

	@Override
	public String getTableName() {		
		return LU_TABLE;
	}

	@Override
	public String getParentKey() {
		return getDbColumnName();
	}

	@Override
	public String getLookupKey() {
		return LU_KEY_COLUMN;
	}

	@Override
	public String getValueColumn() {
		return LU_VALUE_COLUMN;
	}

	@Override
	public DataType getValueType() {
		return DataType.STRING;
	}

	@Override
	public Properties getPvSourceProps() {
		return LU_PV_SOURCE_PROPS;
	}
	
	private static Properties initPvSourceProps() {
		Properties props = new Properties();
		props.put("apiUrl", "rest/ng/users");
		props.put("searchTermName", "searchString");
		props.put("resultFormat", "{{lastName}}, {{firstName}}");
		props.put("respField", "users");
		
		return props;
	}
}