package com.krishagni.catissueplus.core.de.domain;


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
}
