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
		STARTS_WITH ("starts_with"),
		ENDS_WITH ("ends_with"),
		IN ("in"),
		NOT_IN ("not_in"),
		EXISTS ("exists"),
		NOT_EXISTS ("not_exists");
		
		private String symbol;
		
		private Op(String symbol) {
			this.symbol = symbol;
		}
		
		public String symbol() {
			return symbol;
		}		
	};
	
	private int id;

	private String field;
	
	private Op op;
	
	private String[] values;

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
}
