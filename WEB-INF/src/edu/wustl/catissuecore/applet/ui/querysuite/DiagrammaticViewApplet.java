package edu.wustl.catissuecore.applet.ui.querysuite;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.client.ui.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils.DagImageConstants;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.AppletServerCommunicator;
import edu.wustl.catissuecore.applet.model.AppletModelInterface;
import edu.wustl.catissuecore.applet.model.BaseAppletModel;
import edu.wustl.catissuecore.applet.ui.BaseApplet;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
/**
 * This is applet class for Dag view. This renders the rule objects according to the user inputs for add limits section.
 *  
 * @author deepti_shelar
 *
 */
public class DiagrammaticViewApplet extends BaseApplet   
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * panel to be rendered on applet
	 */
	MainDagPanel panel;
	/**
	 * IClientQueryBuilderInterface object to build the query.
	 */
	IClientQueryBuilderInterface queryObject ;
	/**
	 * init method for applet. We call a dag panel of cab2b here and also add the query obj to it.
	 */
	protected void doInit()  
	{
		super.doInit();
		queryObject =  new ClientQueryBuilder();
		Map<DagImageConstants, Image> imagePathsMap = getImagePathsMap();
		UpdateAddLimitUI updateAddLimitUI = new UpdateAddLimitUI();
		PathFinderAppletServerCommunicator pathFinder = new PathFinderAppletServerCommunicator();
		panel = new MainDagPanel(updateAddLimitUI,imagePathsMap, pathFinder);
		panel.setQueryObject(queryObject);
		this.getContentPane().add(panel);
		this.setSize(3500, 2000);
		this.setVisible(true);
	}
	public void getSearchResults()
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		Map inputMap = new HashMap(); 
		IQuery query = queryObject.getQuery();
		inputMap.put("queryObject",query);
		appletModel.setData(inputMap);
		String session_id = getParameter("session_id");
		String urlString = serverURL + AppletConstants.GET_SEARCH_RESULTS + ";jsessionid="+session_id+"?" + AppletConstants.APPLET_ACTION_PARAM_NAME + "=initData";
		try
		{
			AppletModelInterface outputModel = AppletServerCommunicator.doAppletServerCommunication(urlString, appletModel);
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
	/**
	 * This method is called from a javascript when user clicks on Add Limit button of AddLimits.jsp.
	 * This again calls a action class which returns a map which holds the details to create a rule and then we add this rule to query obj.
	 * and update the graph. 
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @throws MultipleRootsException MultipleRootsException
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 */
	public void addExpression(String strToCreateQueryObject, String entityName) throws MultipleRootsException, DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (!strToCreateQueryObject.equalsIgnoreCase(""))
		{
			Map outputMap  = callAddToLimiteSetAction(strToCreateQueryObject,entityName);
			List attributes = (List)outputMap.get("Attributes");
			List attributeOperators = (List)outputMap.get("AttributeOperators");
			List firstAttributeValues = (List)outputMap.get("FirstAttributeValues");
			List secondAttributeValues = (List)outputMap.get("SecondAttributeValues");
			IExpressionId expressionId = queryObject.addRule(attributes, attributeOperators, firstAttributeValues, secondAttributeValues);
			panel.updateGraph(expressionId);
		}
	}
	/**
	 * This again calls a action class which returns a map which holds the details to create a rule and then we add this rule to query obj.
	 * @param strToCreateQueryObject String to create query obj
	 * @param entityName name of the entity
	 * @return Map map of rule details.
	 */
	public Map callAddToLimiteSetAction(String strToCreateQueryObject, String entityName)
	{
		BaseAppletModel appletModel = new BaseAppletModel();
		Map inputMap = new HashMap(); 
		inputMap.put("strToCreateQueryObject",strToCreateQueryObject);
		inputMap.put("entityName",entityName);
		appletModel.setData(inputMap);
		try
		{
			String session_id = getParameter("session_id");
			String urlString = serverURL + AppletConstants.ADD_TO_LIMIT_ACTION + ";jsessionid="+session_id+"?" + AppletConstants.APPLET_ACTION_PARAM_NAME + "=initData";
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

	
	private Map<DagImageConstants, Image> getImagePathsMap()
	{
		HashMap<DagImageConstants, Image> imagePathsMap = new HashMap<DagImageConstants,Image>();
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/arrow_icon_mo.gif"));
		imagePathsMap.put(DagImageConstants.ArrowSelectMOIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/arrow_icon.gif"));
		imagePathsMap.put(DagImageConstants.ArrowSelectIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/paper_grid.png"));
		imagePathsMap.put(DagImageConstants.DocumentPaperIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/port.gif"));
		imagePathsMap.put(DagImageConstants.PortImageIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/select_icon_mo.gif"));
		imagePathsMap.put(DagImageConstants.selectMOIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/select_icon.gif"));
		imagePathsMap.put(DagImageConstants.SelectIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/parenthesis_icon_mo.gif"));
		imagePathsMap.put(DagImageConstants.ParenthesisMOIcon,icon.getImage());
		icon = new ImageIcon(getClass().getClassLoader().getResource("images/parenthesis_icon.gif"));
		imagePathsMap.put(DagImageConstants.ParenthesisIcon,icon.getImage());
		return imagePathsMap;
	}   
	
}
