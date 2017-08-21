package com.krishagni.catissueplus.core.de.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.de.domain.Filter.Op;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode.LogicalOp;
import com.krishagni.catissueplus.core.de.domain.QueryExpressionNode.Parenthesis;
import com.krishagni.catissueplus.core.de.domain.SelectField.Function;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.domain.nui.LookupControl;

public class AqlBuilder {
	
	private AqlBuilder() {
		
	}
	
	public static AqlBuilder getInstance() {
		return new AqlBuilder();
	}
	
	public String getQuery(Object[] selectList, Filter[] filters, QueryExpressionNode[] queryExprNodes) {
		return getQuery(selectList, filters, null, queryExprNodes);
	}
	
	public String getQuery(Object[] selectList, Filter[] filters, Filter[] conjunctionFilters, QueryExpressionNode[] queryExprNodes) {
		Map<Integer, Filter> filterMap = new HashMap<Integer, Filter>();
		for (Filter filter : filters) {
			filterMap.put(filter.getId(), filter);
		}
		
		String selectClause = buildSelectClause(filterMap, selectList);
		String whereClause = buildWhereClause(filterMap, queryExprNodes);
		if (conjunctionFilters != null && conjunctionFilters.length > 0) {
			whereClause = "(" + whereClause + ") and (";
			for (int i = 0; i < conjunctionFilters.length; ++i) {
				if (i > 0) {
					whereClause += " and ";
				}
				
				whereClause += buildFilterExpr(conjunctionFilters[i]);
				++i;
			}
			
			whereClause += ")";
		}

		String query = "";
		if (StringUtils.isNotBlank(selectClause)) {
			query = "select " + selectClause + " where ";
		}

		return query + whereClause;
	}
	
	private String buildSelectClause(Map<Integer, Filter> filterMap, Object[] selectList) {
		if (selectList == null || selectList.length == 0) {
			return "";
		}

		StringBuilder select = new StringBuilder();
		for (Object field : selectList) {
			SelectField aggField = null;
			if (field instanceof String) {
				aggField = new SelectField();
				aggField.setName((String) field);
			} else if (field instanceof Map) {
				aggField = new ObjectMapper().convertValue(field, SelectField.class);
			} else if (field instanceof SelectField) {
				aggField = (SelectField) field;
			}

			if (aggField == null) {
				continue;
			}

			String fieldName = aggField.getName();
			if (aggField.getAggFns() == null || aggField.getAggFns().isEmpty()) {
				select.append(getFieldExpr(filterMap, fieldName, true)).append(", ");
			} else {
				String fieldExpr = getFieldExpr(filterMap, fieldName, false);
					
				StringBuilder fnExpr = new StringBuilder("");
				for (Function fn : aggField.getAggFns()) {
					if (fnExpr.length() > 0) {
						fnExpr.append(", ");
					}
						
					if (fn.getName().equals("count")) {
						fnExpr.append("count(distinct ");
					} else {
						fnExpr.append(fn.getName()).append("(");
					}
						
					fnExpr.append(fieldExpr).append(") as \"").append(fn.getDesc()).append(" \"");
				}
					
				select.append(fnExpr.toString()).append(", ");
			}
		}
		
		int endIdx = select.length() - 2;		
		return select.substring(0, endIdx < 0 ? 0 : endIdx);
	}
	
	private String getFieldExpr(Map<Integer, Filter> filterMap, String fieldName, boolean includeDesc) {
		if (!fieldName.startsWith("$temporal.")) {
			return fieldName;
		}

		Integer filterId = Integer.parseInt(fieldName.substring("$temporal.".length()));
		Filter filter = filterMap.get(filterId);

		String expr = getLhs(filter.getExpr());
		if (includeDesc) {
			expr += " as \"" + filter.getDesc() + "\"";
		}

		return expr;
	}	
	
	private String buildWhereClause(Map<Integer, Filter> filterMap, QueryExpressionNode[] queryExprNodes) {		
		StringBuilder whereClause = new StringBuilder();
		
		for (QueryExpressionNode node : queryExprNodes) {
			switch (node.getNodeType()) {
			  case FILTER:
				  int filterId;
				  if (node.getValue() instanceof Double) {
					  filterId = ((Double)node.getValue()).intValue();
				  } else {
					  filterId = (Integer)node.getValue();
				  }
				  
				  Filter filter = filterMap.get(filterId);
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
		if (filter.getExpr() != null) {
			return filter.getExpr();
		}
		
		String field = filter.getField();
		String[] fieldParts = field.split("\\.");
		
		if (fieldParts.length <= 1) {
			throw new RuntimeException("Invalid field name"); // need to replace with better exception type
		}
				
		StringBuilder filterExpr = new StringBuilder();
		filterExpr.append(field).append(" ").append(filter.getOp().symbol()).append(" ");
		if (filter.getOp().isUnary()) {
			return filterExpr.toString();
		}

		Container form = null;
		String ctrlName = null;
		Control ctrl = null;
		if (fieldParts[1].equals("extensions") || fieldParts[1].equals("customFields")) {
			if (fieldParts.length < 4) {
				return "";
			}
			
			form = getContainer(fieldParts[2]);
			ctrlName = StringUtils.join(fieldParts, ".", 3, fieldParts.length);
		} else {
			form = getContainer(fieldParts[0]);
			ctrlName = StringUtils.join(fieldParts, ".", 1, fieldParts.length);
		}
		
		ctrl = form.getControlByUdn(ctrlName, "\\.");				

		DataType type = ctrl.getDataType();
		if (ctrl instanceof LookupControl) {
			type = ((LookupControl)ctrl).getValueType();
		}
		
		String[] values = (String[])Arrays.copyOf(filter.getValues(), filter.getValues().length);		
		quoteStrings(type, values);
		
		String value = values[0];
		if (filter.getOp() == Op.IN || filter.getOp() == Op.NOT_IN) {
			value = "(" + join(values) + ")";
		} else if (filter.getOp() == Op.BETWEEN) {
			value =  "(" + values[0] + ", " + values[1] + ")";
		}
		
		return filterExpr.append(value).toString();
	}
	
	private void quoteStrings(DataType type, String[] values) {
		if (type != DataType.STRING && type != DataType.DATE) {
			return;
		}
		
		for (int i = 0; i < values.length; ++i) {
			values[i] = "\"" + values[i] + "\"";   
		}		
	}
	
	private String join(String[] values) {
		StringBuilder result = new StringBuilder();
		for (String val : values) {
			result.append(val).append(", ");
		}
        
		int endIdx = result.length() - 2;
		return result.substring(0, endIdx < 0 ? 0 : endIdx);
	}
	
	private String getLhs(String temporalExpr) {
		String[] parts = temporalExpr.split("[<=>!]|\\sany\\s*$|\\sexists\\s*$|\\snot exists\\s*$|\\sbetween\\s");
		return parts[0];
	}
	
	public Container getContainer(String formName){
		return Container.getContainer(formName);
	}
}
