package com.krishagni.catissueplus.core.de.domain;


import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Filter {
	
	public enum Op {
		EQ ("="),		
		NE ("!="),
		GE (">="),
		GT (">"),
		LE ("<="),
		LT ("<"),
		CONTAINS ("contains"),
		STARTS_WITH ("starts with"),
		ENDS_WITH ("ends with"),
		IN ("in"),
		NOT_IN ("not in"),
		EXISTS ("exists"),
		NOT_EXISTS ("not exists"),
		ANY("any"),
		BETWEEN("between");

		private String symbol;

		private Op(String symbol) {
			this.symbol = symbol;
		}

		public String symbol() {
			return symbol;
		}

		public boolean isUnary() {
			return this == EXISTS || this == NOT_EXISTS || this == ANY;
		}
	};
	
	private int id;

	private String field;
	
	private Op op;
	
	private String[] values;
	
	private String expr;
	
	private String desc;
	
	private boolean parameterized;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Op getOp() {
		return op;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isParameterized() {
		return parameterized;
	}

	public void setParameterized(boolean parameterized) {
		this.parameterized = parameterized;
	}

	public void setEqValues(List<Object> values) {
		if (!isParameterizableConditionValue()) {
			return;
		}

		addEqualCondition(values);
	}

	public void setRangeValues(List<Object> values) {
		if (!isParameterizableConditionValue()) {
			return;
		}

		addRangeCondition(values);
	}

	private boolean isParameterizableConditionValue() {
		if (!isParameterized()) {
			return false;
		}

		if (StringUtils.isNotBlank(getExpr())) {
			return true;
		}

		switch (getOp()) {
			case NE:
			case GT:
			case LT:
			case CONTAINS:
			case STARTS_WITH:
			case ENDS_WITH:
			case NOT_IN:
			case NOT_EXISTS:
				return false;

			default:
				return true;
		}
	}

	private void addEqualCondition(List<Object> values) {
		String[] cond = values.stream().filter(val -> val != null).map(val -> val.toString()).toArray(String[]::new);
		if (cond.length == 0) {
			return;
		}

		setOp(Op.IN);
		setValues(cond);
	}

	private void addRangeCondition(List<Object> values) {
		if (StringUtils.isBlank(getExpr())) {
			addFieldRangeCondition(values);
		} else {
			addExprRangeCondition(values);
		}
	}

	private void addFieldRangeCondition(List<Object> values) {
		Object minValue = values.get(0);
		Object maxValue = values.size() > 1 ? values.get(1) : null;

		if (minValue == null && maxValue != null) {
			setOp(Op.LE);
			setValues(new String[] {maxValue.toString()});
		} else if (minValue != null && maxValue == null) {
			setOp(Op.GE);
			setValues(new String[] {minValue.toString()});
		} else if (minValue != null && maxValue != null) {
			setOp(Op.BETWEEN);
			setValues(new String[] {minValue.toString(), maxValue.toString()});
		}
	}

	private void addExprRangeCondition(List<Object> values) {
		Object minValue = values.get(0);
		Object maxValue = values.size() > 1 ? values.get(1) : null;

		String expr = getExpr();
		if (minValue == null && maxValue != null) {
			setExpr(getLhs(expr) + " <= " + maxValue.toString());
		} else if (minValue != null && maxValue == null) {
			setExpr(getLhs(expr) + " >= " + minValue.toString());
		} else if (minValue != null && maxValue != null) {
			setExpr(getLhs(expr) + " between (" + minValue.toString() + ", " + maxValue.toString() + ")");
		}
	}

	private String getLhs(String expr) {
		String[] lhsRhs = expr.split("[<=>!]|\\sany\\s*$|\\sexists\\s*$|\\snot exists\\s*$|\\sbetween\\s");
		return lhsRhs[0];
	}
}