package edu.wustl.catissuecore.flex.dag;

public class DAGConstant {
	public static final String QUERY_OBJECT = "queryObject";
	public static final String CONSTRAINT_VIEW_NODE ="ConstraintViewNode";
	public static final String VIEW_ONLY_NODE="ViewOnlyNode";
	public static final String CONSTRAINT_ONLY_NODE ="ConstraintOnlyNode";
	public static final String HTML_STR="HTMLSTR";
	public static final String EXPRESSION="EXPRESSION";
	public static final String ADD_LIMIT="Add";
	public static final String EDIT_LIMIT="Edit";
	public static final String DAG_NODE_LIST = "dagNodeList";
	public static final String CUSTOM_FORMULA_NODE_LIST = "customFormulaNodeList";
	public static final String SINGLE_NODE_CUSTOM_FORMULA_NODE_LIST ="singleNodeCFList";
	public static final String REPAINT_OPERATION = "rePaint";
	public static final String ISREPAINT = "isRepaint";
	public static final String ISREPAINT_TRUE = "true";
	public static final String ISREPAINT_FALSE = "false";
	public static final String EDIT_OPERATION = "edit";
    public static final String REPAINT_EDIT = "rePaintEdit";
    public static final String REPAINT_CREATE = "rePaintCreate";
    public static final String TQUIMap = "TQUIMap";
	
	//Error Codes 
	public static  final int SUCCESS=0;
	public static  final int EMPTY_DAG=1;
	public static  final int MULTIPLE_ROOT=2;
	public static  final int NO_RESULT_PRESENT=3;
	public static  final int SQL_EXCEPTION=4;
	public static  final int DAO_EXCEPTION=5;
	public static  final int CLASS_NOT_FOUND=6;
	public static  final int NO_PATHS_PRESENT=7;
	public static  final int DYNAMIC_EXTENSION_EXCEPTION=8;
	public static  final int CYCLIC_GRAPH=9;
	
	
	
}
