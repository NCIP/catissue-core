package edu.wustl.catissuecore.flex.dag;

import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;

public class DAGNodeBuilder {
	
	 private IClientQueryBuilderInterface m_queryObject;
	 
	/**
	 * @param expressionId
	 */
	public DAGNode createNode(IExpressionId expressionId,IClientQueryBuilderInterface queryObject,boolean isOutputView)
	{
		// TODO required to create m_queryObject object b4 this call
		setQueryObject(queryObject);
		IExpression expression = m_queryObject.getQuery().getConstraints().getExpression(expressionId);
        IQueryEntity constraintEntity = expression.getQueryEntity();
        DAGNode dagNode = new DAGNode();
		dagNode.setNodeName(edu.wustl.cab2b.common.util.Utility.getOnlyEntityName(constraintEntity.getDynamicExtensionsEntity()));
		dagNode.setExpressionId(expression.getExpressionId().getInt());
		if(isOutputView)
		{
			dagNode.setNodeType(DAGConstant.VIEW_ONLY_NODE);
		}
		else
		{
			dagNode.setToolTip(expression);
		}

		//m_queryObject.addExressionIdToVisibleList(expressionId); //Requiref or assocaiation
		return dagNode;
        		
	}

	 public void setQueryObject(IClientQueryBuilderInterface queryObject) {
	        m_queryObject = queryObject;
	    }

}
