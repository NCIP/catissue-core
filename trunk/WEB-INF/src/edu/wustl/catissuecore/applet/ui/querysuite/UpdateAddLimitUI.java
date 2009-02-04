
package edu.wustl.catissuecore.applet.ui.querysuite;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.IUpdateAddLimitUIInterface;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.catissuecore.bizlogic.querysuite.GenerateHtmlForAddLimitsBizLogic;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.impl.Rule;

/**
 * This class allows user interaction from applet to server.
 * This basically allows user to edit the limits on expressions.
 * @author deepti_shelar
 *
 */
public class UpdateAddLimitUI implements IUpdateAddLimitUIInterface
{
	/**
	 * The applet class's reference.
	 */
	private DiagrammaticViewApplet dagApplet = null;

	/**
	 * Constructor with applets reference to be set.
	 * @param dagApplet
	 */
	public UpdateAddLimitUI(DiagrammaticViewApplet dagApplet)
	{
		this.dagApplet = dagApplet;
	}

	/**
	 * This method is called when user clicks on clear limits option.
	 * @param IExpression expression to be changed
	 */
	public void clearAddLimitUI()
	{
	}

	/**
	 * This method is called when user clicks on edit limits option.
	 * @param IExpression expression to be changed
	 */
	public void editAddLimitUI(IExpression expression)
	{
		dagApplet.setExpression(expression);
		EntityInterface entity = expression.getConstraintEntity().getDynamicExtensionsEntity();
		GenerateHtmlForAddLimitsBizLogic generateHTMLBizLogic = new GenerateHtmlForAddLimitsBizLogic();
		Rule rule = ((Rule) (expression.getOperand(0)));
		List<ICondition> conditions = rule.getConditions();
		String html = generateHTMLBizLogic.generateHTML(entity, conditions);
		Object[] paramArray = {html};
		CommonAppletUtil.callJavaScriptFunction(dagApplet, AppletConstants.SHOW_ENTITY_INFO, paramArray);
	}
}
