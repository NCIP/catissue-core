package com.krishagni.catissueplus.core.de.domain;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.de.domain.Filter.Op;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode.LogicalOp;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode.Parenthesis;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.emory.mathcs.backport.java.util.Arrays;

public class AqlBuilder {
	
	private AqlBuilder() {
		
	}
	
	public static AqlBuilder getInstance() {
		return new AqlBuilder();
	}
	
	public String getQuery(String[] selectList, Filter[] filters, QueryExpressionNode[] queryExprNodes) {
		String selectClause = buildSelectClause(selectList);
		String whereClause = buildWhereClause(filters, queryExprNodes);
		return "select " + selectClause + " where " + whereClause;		
	}
	
	private String buildSelectClause(String[] selectList) {
		StringBuilder select = new StringBuilder();
		for (String field : selectList) {
			select.append(field).append(", ");
		}
		
		int endIdx = select.length() - 2;		
		return select.substring(0, endIdx < 0 ? 0 : endIdx);
	}
	
	private String buildWhereClause(Filter[] filters, QueryExpressionNode[] queryExprNodes) {
		Map<Integer, Filter> filterMap = new HashMap<Integer, Filter>();
		for (Filter filter : filters) {
			filterMap.put(filter.getId(), filter);
		}
		
		StringBuilder whereClause = new StringBuilder();
		
		for (QueryExpressionNode node : queryExprNodes) {
			switch (node.getNodeType()) {
			  case FILTER:
				  Filter filter = filterMap.get((Integer)node.getValue());
				  String filterExpr = buildFilterExpr(filter);
				  whereClause.append(filterExpr);				  				  
				  break;
				  
			  case OPERATOR:
				  LogicalOp op = null;
				  if (node.getValue() instanceof String) {
					  op = LogicalOp.valueOf((String)node.getValue());
				  } else if (node.getValue() instanceof LogicalOp) {
					  op = (LogicalOp)node.getValue();
				  } 
				  whereClause.append(op.symbol());
				  break;
				  
			  case PARENTHESIS:
				  Parenthesis paren = null;
				  if (node.getValue() instanceof String) {
					  paren = Parenthesis.valueOf((String)node.getValue());
				  } else if (node.getValue() instanceof Parenthesis) {
					  paren = (Parenthesis)node.getValue();
				  }				  
				  whereClause.append(paren.symbol());
				  break;				  				
			}
			
			whereClause.append(" ");
		}
		
		return whereClause.toString();
	}
	
	private String buildFilterExpr(Filter filter) {		
		String field = filter.getField();
		int dotIdx = field.indexOf(".");
		if (dotIdx == -1) {
			throw new RuntimeException("Invalid field name"); // need to replace with better exception type
		}
				
		StringBuilder filterExpr = new StringBuilder();
		filterExpr.append(field).append(" ").append(filter.getOp().symbol()).append(" ");
		if (filter.getOp() == Op.EXISTS || filter.getOp() == Op.NOT_EXISTS) {
			return filterExpr.toString();
		}
		
		String formName = field.substring(0, dotIdx);
		Container form = Container.getContainer(formName);
		Control ctrl = form.getControlByUdn(field.substring(dotIdx + 1));
				
		String[] values = (String[])Arrays.copyOf(filter.getValues(), filter.getValues().length);
		if (ctrl.getDataType() == DataType.STRING || ctrl.getDataType() == DataType.DATE) {
			for (int i = 0; i < values.length; ++i) {
				values[i] = "\"" + values[i] + "\"";   
			}
		} 
		
		String value = values[0];
		if (filter.getOp() == Op.IN || filter.getOp() == Op.NOT_IN) {
			value = join(values);
		} 
		
		return filterExpr.append(value).toString();
	}
	
	private String join(String[] values) {
		StringBuilder result = new StringBuilder();
		for (String val : values) {
			result.append(val).append(", ");
		}
		
		int endIdx = result.length() - 2;
		return result.substring(0, endIdx < 0 ? 0 : endIdx);
	}
}
