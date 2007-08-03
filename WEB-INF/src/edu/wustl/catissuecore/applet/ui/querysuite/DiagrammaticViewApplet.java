
package edu.wustl.catissuecore.applet.ui.querysuite;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IPathFinder;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImages;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.ui.BaseApplet;
import edu.wustl.catissuecore.applet.util.CommonAppletUtil;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraph;
import edu.wustl.common.querysuite.queryobject.impl.Rule;
import edu.wustl.common.util.logger.Logger;

/**
 * This is applet class for Dag view. This renders the rule objects according to the user inputs for add/edit limits section.
 *  
 * @author deepti_shelar
 *
 */
public class DiagrammaticViewApplet extends BaseApplet
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * panel to be rendered on applet
	 */
	private MainDagPanel panel;
	/**
	 * ClientQueryBuilder object to build the query.
	 */
	private ClientQueryBuilder queryObject;
	/**
	 * IExpression object to edit the expression.
	 */
	private IExpression expression;

	/**
	 * init method for applet. We call a dag panel of cab2b here and also add the query obj to it.
	 */

	protected void doInit()
	{
		super.doInit();
		Logger.configure();
		/*URL codeBase = getCodeBase();
		String path = codeBase.getPath();
		path = path + AppletConstants.RESOURCE_BUNDLE_PATH;
		ApplicationProperties.initBundle(path);*/
		IQuery query = getQueryObjectFromServer();
		System.out.println("Query from server    "+query);
		queryObject = new ClientQueryBuilder();
		if(query != null)
		{
			queryObject.setQuery(query);
		}
		Map<DagImages, Image> imagePathsMap = getImagePathsMap();
		UpdateAddLimitUI updateAddLimitUI = new UpdateAddLimitUI(this);
		IPathFinder pathFinder = new PathFinderAppletServerCommunicator(serverURL);
		boolean isForView = false;
		String isForViewStr = getParameter("isForView");
		if(isForViewStr.equalsIgnoreCase("true"))
		{
			isForView = true;
		}
		panel = new MainDagPanel(updateAddLimitUI, imagePathsMap, pathFinder,isForView);
		panel.setQueryObject(queryObject);
		this.getContentPane().add(panel);
		this.setSize(3500, 2000);
		this.setVisible(true);
	}
	protected void postInit() {
		//Initializing frame reference to parent frame. 
		//This reference is used for showing ambiguity resolver
		//CommonUtils.FrameReference =  findParentFrame();
		panel.updateGraph();
		System.out.println();
	}

	/**
	 * This method returns parent frame of this applet
	 * @return Frame
	 */
	private Frame findParentFrame(){ 
		Container c = this; 

		while(c != null){ 
			if (c instanceof Frame) 
				return (Frame)c; 

			c = c.getParent(); 
		} 
		return null; 
	} 

	/**
	 * This method is called from applet through javascript. 
	 * It further calls ViewSearchResultsAction class to get the results,of the query generated.
	 * @return String Error message if any is shown to user through javascript.
	 */
	public String getSearchResults()
	{
		String errorMessage = "";
		boolean isRulePresentInDag = false;
		BaseAppletModel appletModel = new BaseAppletModel();
		Map<String,IQuery> inputMap = new HashMap<String,IQuery>();
		IQuery query = queryObject.getQuery();
		IConstraints constraints = query.getConstraints();
		Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
		while(expressionIds.hasMoreElements())
		{
			IExpressionId id = expressionIds.nextElement();
			if(((Expression)constraints.getExpression(id)).containsRule())
			{
				isRulePresentInDag = true;
				break;
			}
		}
		if(isRulePresentInDag)
		{
			inputMap.put(AppletConstants.QUERY_OBJECT, query);
			appletModel.setData(inputMap);
			String session_id = getParameter(AppletConstants.SESSION_ID);
			String urlString = serverURL + AppletConstants.GET_SEARCH_RESULTS + ";jsessionid=" + session_id + "?"
			+ AppletConstants.APPLET_ACTION_PARAM_NAME + "=" + AppletConstants.INIT_DATA + "";
			try
			{
				AppletModelInterface outputModel = AppletServerCommunicator.doAppletServerCommunication(urlString, appletModel);
				Map outputMap = outputModel.getData();
				errorMessage = (String)outputMap.get(AppletConstants.ERROR_MESSAGE);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			errorMessage = AppletConstants.EMPTY_DAG_ERROR_MESSAGE;
		}
		return errorMessage;
	}

	/**
	 * This method is called from a javascript when user clicks on Add Limit button of AddLimits.jsp.
	 * This again calls a action class which returns a map holding the details to create a rule and then we add this rule to query obj.
	 * and update the graph. 
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @throws MultipleRootsException MultipleRootsException
	 */
	public void addExpression(String strToCreateQueryObject, String entityName) throws MultipleRootsException
	{
		String errorMessage = "";
		if (strToCreateQueryObject.equalsIgnoreCase(""))
		{
			errorMessage = AppletConstants.EMPTY_LIMIT_ERROR_MESSAGE;
			showValidationMessagesToUser(errorMessage);
		}
		else
		{
			Map outputMap = callAddToLimiteSetAction(strToCreateQueryObject, entityName);
			List<AttributeInterface> attributes = (List) outputMap.get(AppletConstants.ATTRIBUTES);
			List<String> attributeOperators = (List) outputMap.get(AppletConstants.ATTRIBUTE_OPERATORS);
			List<List<String>> conditionValues = (List<List<String>>) outputMap.get(AppletConstants.ATTR_VALUES);
			errorMessage = (String)outputMap.get(AppletConstants.ERROR_MESSAGE);
			if(errorMessage != null && errorMessage.equalsIgnoreCase(""))
			{
				IExpressionId expressionId = queryObject.addRule(attributes, attributeOperators, conditionValues);
				panel.updateGraph(expressionId);	
				showValidationMessagesToUser(errorMessage);
			}
			else
			{
				showValidationMessagesToUser(errorMessage);
			}
		}
	}
	/**
	 * Calls javascript function to show validation messages
	 * @param errorMessage string message
	 */
	void showValidationMessagesToUser(String errorMessage)
	{
		Object[] paramArray = {errorMessage};
		CommonAppletUtil.callJavaScriptFunction(this, AppletConstants.SHOW_VALIDATION_MESSAGES, paramArray);
	}
	/**
	 * This method is called from a javascript when user clicks on Edit Limit button of AddLimits.jsp.
	 * This again calls a action class which returns a map which holds the details to create a rule and then we get the expression which is to be edited , remove all conditions of the associated rule and
	 * add new conditions to the same rule to query obj.
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @throws MultipleRootsException MultipleRootsException
	 */
	public void editExpression(String strToCreateQueryObject, String entityName) throws MultipleRootsException
	{
		String errorMessage = "";
		if (strToCreateQueryObject.equalsIgnoreCase(""))
		{
			errorMessage = AppletConstants.EMPTY_LIMIT_ERROR_MESSAGE;
			showValidationMessagesToUser(errorMessage);
		}
		else
		{
			Map outputMap = callAddToLimiteSetAction(strToCreateQueryObject, entityName);
			List<AttributeInterface> attributes = (List) outputMap.get(AppletConstants.ATTRIBUTES);
			List<String> attributeOperators = (List) outputMap.get(AppletConstants.ATTRIBUTE_OPERATORS);
			List<List<String>> conditionValues = (List<List<String>>) outputMap.get(AppletConstants.ATTR_VALUES);
			errorMessage = (String)outputMap.get(AppletConstants.ERROR_MESSAGE);
			if(!errorMessage.equalsIgnoreCase(""))
			{
				showValidationMessagesToUser(errorMessage);
			}
			else
			{
				Rule rule = ((Rule) (expression.getOperand(0)));
				rule.removeAllConditions();
				List<ICondition> conditionsList = queryObject.getConditions(attributes, attributeOperators,conditionValues);
				for (ICondition condition : conditionsList)
				{
					rule.addCondition(condition);
				}
				errorMessage = AppletConstants.EDIT_LIMITS;
				System.out.println(errorMessage);
				showValidationMessagesToUser(errorMessage);
			}
		}
	}

	/**
	 * This calls a action class which returns a map which holds the details to create a rule and then we add this rule to query obj.
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @return Map map of rule details.
	 */
	public Map callAddToLimiteSetAction(String strToCreateQueryObject, String entityName)
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		Map inputMap = new HashMap();
		inputMap.put(AppletConstants.STR_TO_CREATE_QUERY_OBJ, strToCreateQueryObject);
		inputMap.put(AppletConstants.ENTITY_NAME, entityName);
		IQuery query = queryObject.getQuery();
		inputMap.put(AppletConstants.QUERY_OBJECT, query);
		appletModel.setData(inputMap);
		try
		{
			String session_id = getParameter(AppletConstants.SESSION_ID);
			String urlString = serverURL + AppletConstants.ADD_TO_LIMIT_ACTION + ";jsessionid=" + session_id + "?"
			+ AppletConstants.APPLET_ACTION_PARAM_NAME + "=" + AppletConstants.INIT_DATA + "";
			AppletModelInterface outputModel = AppletServerCommunicator.doAppletServerCommunication(urlString, appletModel);
			Map outputMap = outputModel.getData();
			return outputMap;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method returns the map of all the required imgaes with its path.
	 * @return Map<DagImages, Image> map of images
	 */
	private Map<DagImages, Image> getImagePathsMap()
	{
		HashMap<DagImages, Image> imagePathsMap = new HashMap<DagImages, Image>();
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/arrow_icon_mo.gif"));
		imagePathsMap.put(DagImages.ArrowSelectMOIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/arrow_icon.gif"));
		imagePathsMap.put(DagImages.ArrowSelectIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/paper_grid.png"));
		imagePathsMap.put(DagImages.DocumentPaperIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/port.gif"));
		imagePathsMap.put(DagImages.PortImageIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/select_icon_mo.gif"));
		imagePathsMap.put(DagImages.selectMOIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/select_icon.gif"));
		imagePathsMap.put(DagImages.SelectIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/parenthesis_icon_mo.gif"));
		imagePathsMap.put(DagImages.ParenthesisMOIcon, icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/parenthesis_icon.gif"));
		imagePathsMap.put(DagImages.ParenthesisIcon, icon.getImage());
		return imagePathsMap;
	}
	/**
	 * Calls server from applet and gets the required data back.
	 * @param url of the server class
	 * @param inputMap input data 
	 * @return output data 
	 */
	Map doAppletServletCommunication(String url , Map inputMap,String methodName)
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		appletModel.setData(inputMap);
		try
		{
			String session_id = getParameter(AppletConstants.SESSION_ID);
			String urlString = serverURL + url + ";jsessionid=" + session_id + "?"
			+ AppletConstants.APPLET_ACTION_PARAM_NAME + "=" + methodName + "";
			AppletModelInterface outputModel = AppletServerCommunicator.doAppletServerCommunication(urlString, appletModel);
			Map outputMap = outputModel.getData();
			return outputMap;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This expression is the one on which user has edited limits.
	 * @return the expression
	 */
	public IExpression getExpression()
	{
		return expression;
	}

	/**
	 * Expression with the changed limits.
	 * @param expression the expression to set
	 */
	public void setExpression(IExpression expression)
	{
		this.expression = expression;
	}
	/**
	 * Calls server to get the query object.This object was already stored in session when user had created it. 
	 * @return IQuery query object
	 */
	public IQuery getQueryObjectFromServer()
	{
		Map inputDataMap = new HashMap();
		Map outputMap = doAppletServletCommunication(AppletConstants.GET_DAG_VIEW_DATA, inputDataMap,AppletConstants.INIT_DATA);
		IQuery query = (IQuery)outputMap.get(AppletConstants.QUERY_OBJECT);
		return query;
	}
	/**
	 * This method is called from a javascript when user clicks on Add To View button of define search results jsp.
	 * It adds the selected entityExpression to the graph and updates the graph. 
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @throws MultipleRootsException 
	 * @throws MultipleRootsException MultipleRootsException
	 */
	public void addNodeToView(String nodesStr) throws MultipleRootsException
	{
		if (!nodesStr.equalsIgnoreCase(""))
		{
			if(nodesStr.indexOf("~")!= -1)
			{
				String[] nodes = nodesStr.split("~");
				Map<String,String[]> inputDataMap = new HashMap<String,String[]>();
				inputDataMap.put(AppletConstants.ENTITY_STR, nodes);
				Map outputMap = doAppletServletCommunication(AppletConstants.GET_DAG_VIEW_DATA, inputDataMap,AppletConstants.GET_DATA);
				int len = nodes.length;
				System.out.println(len);
				for(int i=0; i<len; i++)
				{
					String node = nodes[i];
					EntityInterface entity = (EntityInterface)outputMap.get(node);
					IExpressionId id = queryObject.addExpression(entity);
					panel.updateGraphForViewExpression(id);
				}
			}
		}
	}
	/**
	 * This method is called from a javascript when user clicks on Next button of AddLimits.jsp.
	 * This again calls a action class to set the query object in session.
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @throws MultipleRootsException 
	 * @throws MultipleRootsException MultipleRootsException
	 */
	public String defineResultsView() throws MultipleRootsException
	{
		Map<String,IQuery> inputDataMap = new HashMap<String,IQuery>();
		String errorMessage = "";
		IQuery query = queryObject.getQuery();
		int roots = ((JoinGraph)(query.getConstraints().getJoinGraph())).getAllRoots().size();
		if(roots > 1)
		{
			errorMessage = AppletConstants.MULTIPLE_ROOTS_EXCEPTION;
		//	showValidationMessagesToUser(errorMessage);
		}
		else
		{
			inputDataMap.put(AppletConstants.QUERY_OBJECT, query);
			doAppletServletCommunication(AppletConstants.GET_DAG_VIEW_DATA, inputDataMap,AppletConstants.SET_DATA);
		}
		return errorMessage;
	}
}
