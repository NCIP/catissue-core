/**
 * 
 */

package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.CreateQueryObjectBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.util.QueryUtility;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author chetan_patil
 * @created Sep 14, 2007, 9:53:15 AM
 */
public class ExecuteQueryAction extends BaseAction
{

	protected ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = Constants.FAILURE;

		CreateQueryObjectBizLogic bizLogic = null;
		HttpSession session = request.getSession();
		IParameterizedQuery parameterizedQuery = (IParameterizedQuery) session.getAttribute(AppletConstants.QUERY_OBJECT);
		String conditionstr = request.getParameter("conditionList");
		Map<String, String[]> conditions = null;
		if (conditionstr != null) 
		{
			bizLogic = new CreateQueryObjectBizLogic();
			conditions = bizLogic.createConditionsMap(conditionstr);
			processInputData(conditions, parameterizedQuery);
		}
		
		String errorMessage = executeQuery(request, parameterizedQuery);
		if (errorMessage == null)
		{
			target = Constants.SUCCESS;
		}
		else if(errorMessage.equalsIgnoreCase(Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS))
		{
			target = Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS;
			return actionMapping.findForward(target);
		}
		else
		{
			ActionErrors errors = new ActionErrors();
			ActionError error = new ActionError("errors.item", errorMessage);
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			saveErrors(request, errors);
		}
		
		return actionMapping.findForward(target);
	}

	private void processInputData(Map<String, String[]> conditions,
			IParameterizedQuery query) {
		Map<IExpressionId, Map<AttributeInterface, ICondition>> expressionConditionMap = QueryUtility.getSelectedConditions(query);
		
		Set<Map.Entry<IExpressionId, Map<AttributeInterface, ICondition>>> mapEntries = expressionConditionMap
				.entrySet();
		for (Map.Entry<IExpressionId, Map<AttributeInterface, ICondition>> entry : mapEntries) {
			IExpressionId id = entry.getKey();
			Map<AttributeInterface, ICondition> map = entry.getValue();
			Set<Map.Entry<AttributeInterface, ICondition>> attrEntries = map
					.entrySet();
			for (Map.Entry<AttributeInterface, ICondition> attrEntry : attrEntries) {
				AttributeInterface attribute = attrEntry.getKey();
				String expressionidattrid = id.getInt() + "_"
						+ attribute.getId();
				if (conditions.containsKey(expressionidattrid)) {
					ICondition condition = attrEntry.getValue();
					String[] params = conditions.get(expressionidattrid);
					ArrayList<String> conditionValueList = new ArrayList<String>();
					for (int i = 1; i < params.length; i++) {
						if (params[i] != null)
							conditionValueList.add(params[i]);
					}
					condition.setValues(conditionValueList);
					condition.setRelationalOperator(RelationalOperator
							.getOperatorForStringRepresentation(params[0]));
				}
			}
		}
	}

	private String executeQuery(HttpServletRequest request, IParameterizedQuery parameterizedQuery)
	{
		String errorMessage = null;

		int errorCode = QueryModuleUtil.searchQuery(request , parameterizedQuery,null);
		switch (errorCode)
		{
			case QueryModuleUtil.EMPTY_DAG :
				errorMessage = ApplicationProperties.getValue("query.empty.dag");
				break;
			case QueryModuleUtil.MULTIPLE_ROOT :
				errorMessage = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
				break;
			case QueryModuleUtil.NO_RESULT_PRESENT :
				errorMessage = ApplicationProperties.getValue("query.zero.records.present");
				break;
			case QueryModuleUtil.SQL_EXCEPTION :
			case QueryModuleUtil.DAO_EXCEPTION :
			case QueryModuleUtil.CLASS_NOT_FOUND :
				errorMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
			case QueryModuleUtil.RESULTS_MORE_THAN_LIMIT :
				errorMessage = Constants.TREE_NODE_LIMIT_EXCEEDED_RECORDS;
		}

		return errorMessage;
	}

}
