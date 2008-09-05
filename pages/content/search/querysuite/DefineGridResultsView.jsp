<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CategorySearchForm"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<html>
<head>

<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
</head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
	<script  src="dhtml_comp/jss/dhtmlXCommon.js"></script>
	<script src="jss/script.js"></script>
	<%
	String callAction=Constants.CONFIGURE_GRID_VIEW_ACTION;
	CategorySearchForm form = (CategorySearchForm)request.getAttribute("categorySearchForm");
	String currentSelectedNodeInTree = form.getCurrentSelectedNodeInTree();
	String showSelected = "false";
	List selectedColumnNameValueBeanList = form.getSelectedColumnNameValueBeanList();
%>
	

<html:form method="GET" action="<%=callAction%>">
<html:hidden property="operation" value=""/>
<body onload="initTreeView()">
<table border="0" width="400" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="90%" bordercolorlight="#000000" >
	<tr >
		<td width="1px" >&nbsp;</td>
		<td valign="top"  width="100"></td>
	</tr>

	<tr >
		<td width="1px">&nbsp;	</td>
		<td valign="top" colspan="8" width="100%" >
		<bean:message key="query.defineGridResultsView.message"/>
		</td>
	</tr>
	
		
	<tr>
		<td width="1px">&nbsp;	</td>
		<td valign="top"  width="90%" height="90%">
			<div id="treeBox" style="background-color:white;overflow:auto;height:270;width:260;border-left:solid 1px;border-right:solid 1px;border-top:solid 1px;border-bottom:solid 1px;"></div>
		</td>
		<td width="1%"> &nbsp; </td>
		   <td align="center" valign="center" width="">
			<html:button styleClass="actionButton" property="shiftRight"styleId ="shiftRight" onclick="moveOptionsRight(this.form.columnNames, this.form.selectedColumnNames);">
				<bean:message key="buttons.addToView"/>
			</html:button>
			<br/><br/>
			<html:button styleClass="actionButton" property="shiftLeft" styleId ="shiftLeft" onclick="moveOptionsLeft(this.form.selectedColumnNames, this.form.columnNames);" >
				<bean:message key="buttons.deleteFromView"/>
			</html:button>  
		</td>
		<td width="1%"> &nbsp; </td>
		<td class="" valign="top" width="60" height="85%">
<!-- Mandar : 434 : for tooltip -->
			<html:select property="selectedColumnNames" styleClass="" size="16" multiple="true">
				<html:options collection="selectedColumnNameValueBeanList" labelProperty="name" property="value"/>
			</html:select>
		</td>
		<td width="1%"> &nbsp; </td>
		<td align="center" valign="center">
			<html:button styleClass="actionButton" property="shiftUp" styleClass="actionButton" styleId ="shiftUp" onclick="moveUp(this.form.selectedColumnNames);">
				<bean:message key="buttons.up"/>
			</html:button>  <br/><br/>
			
			<html:button styleClass="actionButton" property="shiftDown" styleClass="actionButton" styleId ="shiftDown" onclick="moveDown(this.form.selectedColumnNames)" >
				<bean:message key="buttons.down"/>
			</html:button> 
		</td>
</tr>
<tr><td> &nbsp;
</td>
</tr>					
<tr>
<td colspan="6" align="left" valign="top">
  	<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr height="1px">
			<td align="center" height="2%">
					<html:button styleClass="actionButton" property="configureButton" onclick = "onSubmit(this.form.selectedColumnNames,'back');" >
							<bean:message key="query.back.button"/>
					</html:button>
			</td>	 
			<td align="center" height="2%">
					<html:button styleClass="actionButton" property="redefineButton" onclick = "onSubmit(this.form.selectedColumnNames,'restore');" >
						<bean:message key="query.restoreDefault.button"/>
					</html:button>
			</td>
			<td align="center" height="2%">
					<html:button styleClass="actionButton" property="configButton" onclick = "onSubmit(this.form.selectedColumnNames,'finish');" >
						<bean:message key="query.finish.button"/>
					</html:button>
			</td>
			<td width="40%">
			</td>
			</tr>
		</table>
	</td>
			</tr>
</table>
</body>
</html:form>

<SCRIPT LANGUAGE="JavaScript">

    function addOption(theSel, theText, theValue)
    {
	    var newOpt = new Option(theText, theValue);
	    var selLength = theSel.length;
	    var exists="false";
	    for(var i=0;i<selLength; i++)
	    {
			if(theSel.options[i].value==theValue)// || theSel.options[i].indexof("class") != 0 || theSel.options[i].indexog("root")!=0)
				{
					exists="true";
					break;
				}
	    }
	    if(exists=="false")
		    theSel.options[selLength] = newOpt;
	}
	
   
    function moveOptionsRight(theSelFrom, theSelTo)
    {
		var list=tree.getAllChecked(); 
		var selectedAttrs = list.split(",");
	    var selLength = selectedAttrs.length;
	    var selectedText = new Array();
	    var selectedValues = new Array();
	    var selectedCount = 0;
	    if(list=="")
		{
			alert("Please select column name.");
		} else
		{
			var i;
		  
			// Find the selected Options in reverse order
			// and delete them from the 'from' Select.
			for(i=selLength-1; i>=0; i--)
			{
				var selectedOption = selectedAttrs[i];
				if(selectedOption.indexOf("root") != 0)
				{
					if(selectedOption.indexOf("class") != 0)
					{
						var nodetext = tree.getItemText(selectedOption);
						var parentId = tree.getParentId(selectedOption);
						var parentNodeText = tree.getItemText(parentId);
						var displaySelectedColumn = parentNodeText + " : " + nodetext;
						selectedText[selectedCount] = displaySelectedColumn;
						selectedValues[selectedCount] = selectedOption;
						//deleteOption(theSelFrom, i);
						selectedCount++;
					}
				}
			}
		  
			// Add the selected text/values in reverse order.
			// This will add the Options to the 'to' Select
			// in the same order as they were in the 'from' Select.
			for(i=selectedCount-1; i>=0; i--)
			{
				addOption(theSelTo, selectedText[i], selectedValues[i]);
			}
		}
    }
   	function moveOptionsLeft(theSelFrom, theSelTo)
	{
		var selLength = theSelFrom.length;
		var selectedCount = 0;
		var i;
		for(i=selLength-1; i>=0; i--)
		{
		    if(theSelFrom.options[i].selected)
		    {
		    	selectedCount++;
	  			deleteOption(theSelFrom, i);
    		}
		}
		if(selectedCount==0)
			alert("Please select column name.");
	}
	function deleteOption(theSel, theIndex)
    { 
	    var selLength = theSel.length;
	    if(selLength>0)
	    {
			var selItem = theSel.options[theIndex].value;
			tree.setCheck(selItem,false);
			theSel.options[theIndex] = null;
		
	    }
   	}
	function moveUpAllSelected(theSelFrom)
	{
		var selLength = theSelFrom.length;
		var selectedCount = 0;
		var i;
		for(i=selLength-1; i>=0; i--)
		{
		    if(theSelFrom.options[i].selected)
		    {
		    	selectedCount++;
				moveUpOneByOne(theSelFrom,i);
			}
		}
	}
	function moveUpOneByOne(obj,index)
	{
	  var currernt;
	  var reverse;
	  var currerntValue;
	  var reverseValue;
	  //obj.options[obj.options.selectedIndex].
	  if(index > 0)
	  {
	    current = obj.options[index].text;
	    currentValue = obj.options[index].value;
	    reverse = obj.options[index-1].text;
	    reverseValue = obj.options[index-1].value;
	    obj.options[index].text = reverse;
	    obj.options[index].value = reverseValue;
	    obj.options[index-1].text = current;
	    obj.options[index-1].value = currentValue;
	    self.focus();
	    index--;
	  }
	}
    function typeChange(namesArray,valuesArray)
    { 
	    var columnsList = "columnNames";
	    ele = document.getElementById(columnsList);
	    //To Clear the Combo Box
	    ele.options.length = 0;
				
	    //ele.options[0] = new Option('-- Select --','-1');
	    var j=0;
	    //Populating the corresponding Combo Box
	    for(i=0;i<namesArray.length;i++)
	    {
	    	ele.options[j++] = new Option(namesArray[i],valuesArray[i]);
	    }
    }


	function moveUp(obj)
	{
	  var currernt;
	  var reverse;
	  var currerntValue;
	  var reverseValue;
	  
	  if(obj.options[obj.options.selectedIndex].index > 0)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    currentValue = obj.options[obj.options.selectedIndex].value;
	    reverse = obj.options[obj.options[obj.options.selectedIndex].index-1].text;
	    reverseValue = obj.options[obj.options[obj.options.selectedIndex].index-1].value;
	    obj.options[obj.options.selectedIndex].text = reverse;
	    obj.options[obj.options.selectedIndex].value = reverseValue;
	    obj.options[obj.options[obj.options.selectedIndex].index-1].text = current;
	    obj.options[obj.options[obj.options.selectedIndex].index-1].value = currentValue;
	    self.focus();
	    obj.options.selectedIndex--;
	  }
	}
	
	function moveDown(obj)
	{
	  var currernt;
	  var reverse;
	  var currerntValue;
	  var reverseValue;
	  if(obj.options[obj.options.selectedIndex].index != obj.length-1)
	  {
	    current = obj.options[obj.options.selectedIndex].text;
	    currentValue = obj.options[obj.options.selectedIndex].value;
	    reverse = obj.options[obj.options[obj.options.selectedIndex].index+1].text;
	    reverseValue = obj.options[obj.options[obj.options.selectedIndex].index+1].value;
	    obj.options[obj.options.selectedIndex].text = reverse;
	    obj.options[obj.options.selectedIndex].value = reverseValue;
	    obj.options[obj.options[obj.options.selectedIndex].index+1].text = current;
	    obj.options[obj.options[obj.options.selectedIndex].index+1].value = currentValue;
	    self.focus();
	    obj.options.selectedIndex++;
	  }
	}	
    function selectOptions(element)
	{
		for(i=0;i<element.length;i++) 
		{
			element.options[i].selected=true;
		}
	}
    function onSubmit(theSelTo,operation)
	{		
		
		
		if(operation == 'finish')
		{
			if(theSelTo.length==0)
			{
				alert("We need to add atleast one column to define view");
				return ;
			}
			selectOptions(document.forms[0].selectedColumnNames);
		}
		document.forms[0].operation.value = operation;
		document.forms[0].action =  "ConfigureGridView.do";	
		document.forms[0].submit();		
	}
	
</script>
	<script>
	var tree;	 
function initTreeView()
{
		tree=new dhtmlXTreeObject("treeBox","100%","100%",0);
		tree.setImagePath("dhtml_comp/imgs/");
		tree.setOnClickHandler();	
		tree.enableCheckBoxes(1);
	    tree.enableThreeStateCheckboxes(true);
				
<%
Vector treeData = (Vector)request.getSession().getAttribute(Constants.TREE_DATA);
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
			if(currentSelectedNodeInTree != null && currentSelectedNodeInTree.equals(nodeId))
			{
				showSelected = "true";
			}
			String img = "results.gif";
			//if (parentId.equals("0"))
			//{
				//nodeColapseCode += "tree.closeAllItems('" + nodeId + "');";
			//}
%>
tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
tree.setItemText("<%=nodeId%>","<%=data.getDisplayName()%>","<%=data.getDisplayName()%>");
if("<%=showSelected%>" == "true")
{
	tree.setCheck("<%=currentSelectedNodeInTree%>",true);
	tree.openItem("<%=currentSelectedNodeInTree%>");
} 
 <% if(selectedColumnNameValueBeanList!=null)
  {
	for(int i=0;i<selectedColumnNameValueBeanList.size();i++) {
	NameValueBean nameValueBean = (NameValueBean)selectedColumnNameValueBeanList.get(i);
	String name = nameValueBean.getName();
	String value = nameValueBean.getValue();
	if(nodeId.equalsIgnoreCase(value))
	 {
%>
		tree.setCheck("<%=value%>",true);
		tree.openItem("<%=value%>");
<% } //end of if
 }//end of for
 } //end of if
 %>




<%	
		}// end of while
  } // end of if
%>
					
}
function shiftRight()
{
	var list=tree.getAllChecked(); 
	alert(list);
}
</script>					