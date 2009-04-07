<%@ page import="edu.wustl.catissuecore.actionForm.CategorySearchForm"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<html>
<head>

<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
</head>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script  src="dhtml_comp/jss/dhtmlXCommon.js"></script>
	<script  src="dhtml_comp/jss/dhtmlXGrid.js"></script>		
	<script  src="dhtml_comp/jss/dhtmlXGrid.js"></script>		
	<script  src="dhtml_comp/jss/dhtmlXGridCell.js"></script>	
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmlXTree.js"></script>
    <script type="text/javascript" src="jss/ajax.js"></script> 
	<script src="jss/queryModule.js"></script>

 <script>
var columns ;
var colWidth;
var colTypes 
<%List columnList = (List) request.getSession().getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
List dataList = (List) request.getSession().getAttribute(Constants.SPREADSHEET_DATA_LIST);
Long trees = (Long)request.getSession().getAttribute("noOfTrees");
int noOfTrees = trees.intValue();%>
	function checkAll(element)
	{
		var state=element.checked;
		rowCount = mygrid.getRowsNum();
		//alert("rowCount : "+ rowCount);
		for(i=1;i<=rowCount;i++)
		{
			var cl = mygrid.cells(i,0);
			if(cl.isCheckbox())
			cl.setChecked(state);
		}
	}

					//function to update hidden fields as per check box selections.
		function updateHiddenFields()
		{
			var isChecked = "false";
			var checkedRows = mygrid.getCheckedRows(0);
			if(checkedRows.length > 0)
			{
	        	isChecked = "true";
				var cb = checkedRows.split(",");
				rowCount = mygrid.getRowsNum();
				for(i=1;i<=rowCount;i++)
				{
					var cl = mygrid.cells(i,0);
					if(cl.isChecked())
					{
						var cbvalue = document.getElementById(""+(i-1));
						cbvalue.value="1";
						cbvalue.disabled=false;
					}
					else
					{
						var cbvalue = document.getElementById(""+(i-1));
						cbvalue.value="0";
						cbvalue.disabled=true;
					}
				}
			}
			else
			{
				isChecked = "false";
			}
			return isChecked;
		}	

// ------------------------------  FUNCTION SECTION END
<%if (columnList != null && columnList.size()!= 0 && dataList != null && dataList.size() != 0)
{%>
var myData = [<%int i;%><%for (i=0;i<(dataList.size()-1);i++){%>
<%List row = (List)dataList.get(i);
  	int j;%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=AppUtility.toNewGridFormat(row.get(j))%>,<%}%><%=AppUtility.toNewGridFormat(row.get(j))%><%="\""%>,<%}%>
<%List row = (List)dataList.get(i);
  	int j;%>
<%="\""%><%for (j=0;j < (row.size()-1);j++){%><%=AppUtility.toNewGridFormat(row.get(j))%>,<%}%><%=AppUtility.toNewGridFormat(row.get(j))%><%="\""%>
];


var columns = <%="\""%><%int col;%><%for(col=0;col<(columnList.size()-1);col++){%><%=columnList.get(col)%>,<%}%><%=columnList.get(col)%><%="\""%>;

var colWidth = <%="\""%><%for(col=0;col<(columnList.size()-1);col++){%><%=100%>,<%}%><%=100%><%="\""%>;
var colTypes = <%="\""%><%=Variables.prepareColTypes(dataList,true)%><%="\""%>;

var colDataTypes = colTypes;

while(colDataTypes.indexOf("str") !=-1)
colDataTypes=colDataTypes.replace(/str/,"ro");
<% } %>
</script>


<script>
var trees = new Array()
var treeNo = 0;

function initGridView()
{
	<% if (columnList != null && columnList.size()!= 0 && dataList != null && dataList.size() != 0)
	{ %>
		mygrid = new dhtmlXGridObject('gridbox');
	mygrid.setImagePath("dhtml_comp/imgs/");
	mygrid.setHeader(columns);
	//mygrid.setEditable("FALSE");
	mygrid.enableAutoHeigth(false);

	mygrid.setInitWidths(colWidth);

	mygrid.setColTypes(colDataTypes);
	mygrid.enableMultiselect(true);
//	mygrid.chNoState = "0";

	//mygrid.setColAlign("left,left")
	mygrid.setColSorting(colTypes);

//	mygrid.enableAutoHeigth(true);
	mygrid.init();

/*
	mygrid.loadXML("dhtmlxgrid/grid.xml");
	
//		clears the dummy data and refreshes the grid.
	mygrid.clearAll();
*/
	for(var row=0;row<myData.length;row++)
	{
		data = "0,"+myData[row];
		mygrid.addRow(row+1,data,row+1);
	}
	//fix for grid display on IE for first time.
	mygrid.setSizes();
		


		
		
		//////////
		<%  
		for(int i=0;i<noOfTrees;i++) 
		{
			String divId = "treebox"+i;
			String treeDataId = "treeData_"+i;
		%>
	
			trees[treeNo]=new dhtmlXTreeObject(<%=divId%>,"100%","100%",0);
			trees[treeNo].setImagePath("dhtml_comp/imgs/");
			trees[treeNo].setOnClickHandler(treeNodeClicled);
			<%
					Vector treeData = (Vector)request.getSession().getAttribute(treeDataId);
						if(treeData != null && treeData.size() != 0)
						{
							Iterator itr  = treeData.iterator();
							String nodeColapseCode = "";
							while(itr.hasNext())
							{
								QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
								String parentId = "0";	
								if(!data.getParentIdentifier().equals("0"))
								{
									parentId = data.getParentIdentifier().toString();		
								}
								String nodeId = data.getIdentifier().toString();
								String img = "results.gif";
								if(nodeId.endsWith(Constants.LABEL_TREE_NODE))
								{
									 img = "ic_folder.gif";
								}
								if (parentId.equals("0"))
								{
									nodeColapseCode += "tree.closeAllItems('" + nodeId + "');";
								}
			%>
			trees[treeNo].insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
			trees[treeNo].setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
			
			<%	
							}
		
						}	%>
treeNo = treeNo + 1;						
		<%}
	%>	
		////////////
<%} %>
}
</script>

<% if (columnList != null && columnList.size()!= 0 && dataList != null && dataList.size() != 0)
{ %>
<body onload="initGridView()">
	<% }  else{ %>
	<body>	
<% } %>
<html:errors />
<%
	String formAction = Constants.DefineSearchResultsViewJSPAction;
%>
<html:form method="GET" action="<%=formAction%>">
<html:hidden property="currentPage" value=""/>
<html:hidden property="stringToCreateQueryObject" value="" />
<table bordercolor="#000000" border="0" width="100%" cellspacing="2" cellpadding="2"  height="100%">
	<tr>
	<td>
	<table border="1" width="100%" cellspacing="0" cellpadding="0" height="100%" id="table1">
	<tr>
		<td>
		<table border="0" width="100%" cellspacing="0" cellpadding="0" height="100%" bordercolor="#000000" id="table1" >
		<tr  class="trStyle">
			<td width="25%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.addLimits"/>
			</td>

			<td width="25%" height="5%" class="queryModuleTabMenuItem" >
				<bean:message key="query.defineSearchResultsViews"/>
			</td>

			<td width="25%" height="5%" class="queryTabMenuItemSelected">
				<bean:message key="query.viewSearchResults"/>
			</td> 
		</tr>
		<tr>
			<td height="500" width="100%" colspan="4">

				<table border="0"  height="100%" width="100%" cellpadding="1" cellspacing="3">
					<tr>
						<td valign="top" width="20%" height="100%">
							<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" >
								<tr>
									<td valign="top">
										<%  for(int i=0;i<noOfTrees;i++) {
										String divId = "treebox"+i;
										int divHeight= 600/noOfTrees;
										%>
											<div id="<%=divId%>"  style="background-color:white;overflow:auto;width:200;height:400;">
												
											</div>
										<% } %>
									</td>
								</tr>									
							</table>
						</td>
						<td valign="top" height="100%">
							<table border="1" width="100%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" id="table11">
								<tr>
								<td valign="top" colspan="2">
									<div id="gridbox"  height="100%" style="background-color:white;">
									<% if (dataList != null && dataList.size() == 0)
										{ %>
									<bean:message key="simpleQuery.noRecordsFound"/>
									<% } %>
									</div>
								</td>
							</tr>
					<tr bgcolor="#FFFFFF"  height="40">
					<td valign="bottom" height="40">
					<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="" height="100%" bordercolorlight="#000000" >
					<tr valign="center">
					 <td width="2%" valign="center">&nbsp;</td>
						<td width="2%" ><html:button property="Button"><bean:message key="query.addToDataList"/></html:button></td>
					 <td width="2%" valign="center">&nbsp;</td>
						<td ><html:button property="Button"><bean:message key="query.export"/></html:button></td>
					</tr>
				</table>
				
			</td>
		</tr>
						</table>
				</td>
		</tr>
					
				</table>
				</td>
					</tr>
					<tr bgcolor="#DFE9F3">
					<td colspan="4" valign="bottom" height="30">
					<table border="0" width="100%" cellspacing="0" cellpadding="0" bgcolor="#EAEAEA" height="100%" bordercolorlight="#000000" >
					<tr height="35" valign="center">
					 <td width="2%" valign="center">&nbsp;</td>
					 <td valign="center" width="75%"><html:button property="saveButton" onclick="saveClientQueryToServer('save');"><bean:message key="query.saveButton"/></html:button></td>				   	 <td align="right" valign="center"><html:button property="Button" onclick="defineSearchResultsView()"><bean:message key="query.previousButton" /></html:button></td>
					 <td align="right" valign="center"><html:button property="Button" onclick=""><bean:message key="query.nextButton"/></html:button>
					 </td>
					 <td width="2%">&nbsp;</td>
					</tr>
				</table>				
			</td>
		   </tr>
		</table>
	  </td>
	</tr>
  </table>
</td></tr>
</table>
</html:form>
</body>
</html> 