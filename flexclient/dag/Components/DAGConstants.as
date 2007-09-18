package Components
{
	public class DAGConstants
	{
		public static  var VIEW_ONLY_NODE:String ="ViewOnlyNode";
		public static  var CONSTRAINT_VIEW_NODE:String="ConstraintViewNode";
		public static  var CONSTRAINT_ONLY_NODE:String="ConstraintOnlyNode";
		public static  var EDIT:String="Edit";
		public static  var DELETE:String="Delete";
		public static  var ADD_TO_VIEW:String="AddToView";
		public static  var DELETE_FROM_VIEW:String="DeleteFromView";
		public static  var ADD_LIMIT_VIEW:String="AddLimit";
		public static  var RESULT_VIEW:String="Result";
				
		//Error Codes 
		public static  var SUCCESS:int=0;
		public static  var EMPTY_DAG:int=1;
		public static  var MULTIPLE_ROOT:int=2;
		public static  var CYCLIC_GRAPH:int=3;
		public static  var SQL_EXCEPTION:int=4;
		public static  var DAO_EXCEPTION:int=5;
		public static  var DYNAMIC_EXTENSION_EXCEPTION:int=6;
		public static  var NO_PATHS_PRESENT:int=7;
		public static  var NO_RESULT_PRESENT:int=8;
		public static var  CLASS_NOT_FOUND:int=9;
		
		//messages 
		public static var EMPTY_LIMIT_ERROR_MESSAGE:String = "<li><font color='red'>Please enter at least one condition to add a limit to limit set.</font></li>";
		public static var GENERIC_MESSAGE:String="<li><font color='red'>Error occured while executing query.Please report this problem to the Adminstrator.</font></li>";
		public static var MULTIPLE_ROOT_MESSAGE:String="<li><font color='red'>Expression graph should be a connected graph and without multiple roots.</font></li>"
		public static var NO_RESULT_PRESENT_MESSAGE:String="<li><font color='blue' family='arial,helvetica,verdana,sans-serif'>Zero records found for this query.</font></li>"
		public static var EDIT_LIMITS_MESSAGE:String = "<li><font color='blue'>Limit succesfully edited.</font></li>";
		public static var DELETE_LIMITS_MESSAGE:String = "<li><font color='blue'>Limit succesfully deleted.</font></li>";
	}
}