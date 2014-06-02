package com.krishagni.catissueplus.core.de.domain;


public class QueryExpressionNode { 

	public enum NodeType {
	    FILTER, 
	    OPERATOR, 
	    PARENTHESIS
	};
	
	public enum LogicalOp { 
		AND ("and"), 
		OR ("or"),
		PAND ("pand"),
		NTHCHILD("nthchild"),
		NOT ("not");
		
		private String symbol;
		
		private LogicalOp(String symbol) {
			this.symbol = symbol;
		}
		
		public String symbol() {
			return symbol;
		}		
	};

	public enum Parenthesis {
		LEFT  ("("), 
		RIGHT (")");
		
		private String symbol;
		
		private Parenthesis(String symbol) {
			this.symbol = symbol;
		}
		
		public String symbol() {
			return symbol;
		}				
	};
	
	private NodeType nodeType;

	//below Object can be either of the types
	//(LogicalOperator if nodeType = OPERATOR)
	//(Parentheses if nodeType = PARENTHESES )
	//(int if nodeType = FILTER)
	private Object value;

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
