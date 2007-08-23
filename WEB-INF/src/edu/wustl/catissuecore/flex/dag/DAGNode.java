package edu.wustl.catissuecore.flex.dag;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;

import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;

public class DAGNode implements Externalizable {
	private String nodeName;
	private int expressionId;
	private String toolTip;
	private String operatorBetweenAttrAndAssociation;
	
	public DAGNode()
	{
		setOperatorBetweenAttrAndAssociation(ClientConstants.OPERATOR_AND);
	}
	

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	/**
	 * @param expression
	 *            Expression to set
	 */
	public void setExpressionId(int expressionId) {
//		this.expression = expression;
		this.expressionId = expressionId;
	
	}
	
	public int getExpressionId() {
		//this.expressionId = expression.getExpressionId();
		return expressionId;
	}
	
	public void  setToolTip(IExpression expression) {
		StringBuffer sb = new StringBuffer();
		IRule rule = null;
		if (!((IExpression) expression).containsRule()) {
			return;
		} else {
			rule = (IRule) expression.getOperand(0);
		}
		int totalConditions = rule.size();
			
		sb.append("Condition(s) on  ").append("\n");
		for (int i = 0; i < totalConditions; i++) {
			ICondition condition = rule.getCondition(i);
			sb.append((i + 1)).append(") ");
			String formattedAttributeName = CommonUtils.getFormattedString(condition.getAttribute()
					.getName());
			sb.append(formattedAttributeName).append(" ");
			List<String> values = condition.getValues();
			RelationalOperator operator = condition.getRelationalOperator();
			sb.append(edu.wustl.cab2b.client.ui.query.Utility
							.displayStringForRelationalOperator(operator)).append(" ");
			int size = values.size();
			if (size > 0)// Special case for 'Not Equals and Equals
			{
				if (size == 1) {
					sb.append(values.get(0));
				} else {
					sb.append("(");
					if (values.get(0).indexOf(",") != -1) {
						sb.append("\"");
						sb.append(values.get(0));
						sb.append("\"");
					} else {
						sb.append(values.get(0));
					}
					for (int j = 1; j < size; j++) {
						sb.append(", ");
						if (values.get(j).indexOf(",") != -1) {
							sb.append("\"");
							sb.append(values.get(j));
							sb.append("\"");
						} else {
							sb.append(values.get(j));
						}
					}
					sb.append(")");
				}
			}
			sb.append("\n");
		}
		this.toolTip=sb.toString();
	}
	
	public String getToolTip() {
		return toolTip;
	}
	
	public String getOperatorBetweenAttrAndAssociation() {
		return operatorBetweenAttrAndAssociation;
	}

	public void setOperatorBetweenAttrAndAssociation(
			String operatorBetweenAttrAndAssociation) {
		this.operatorBetweenAttrAndAssociation = operatorBetweenAttrAndAssociation;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		nodeName=in.readUTF();
		toolTip = in.readUTF();
		expressionId=in.readInt();
		operatorBetweenAttrAndAssociation=in.readUTF();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(nodeName);
		out.writeUTF(toolTip);
		out.writeInt(expressionId);
		out.writeUTF(operatorBetweenAttrAndAssociation);
	}


	

}
