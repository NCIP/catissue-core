<%@ page import="edu.wustl.catissuecore.actionForm.CategorySearchForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmlXTree.js"></script>
<script src="jss/queryModule.js"></script>
<script type="text/javascript" src="jss/ajax.js"></script> 
<script>
function moveNodeUp()
{

	var selectedNode = tree.getSelectedItemId().split(",");	
	var length= selectedNode.length;
	var i=0;	
	for(i=0; i<length; i++)
	{
		tree.moveItem(selectedNode[i], "up");
	}
}

function moveNodeDown()
{

	var selectedNode = tree.getSelectedItemId().split(",");		
	var length= selectedNode.length;
	var i=0;	
	for(i=0; i<length; i++)
	{
		tree.moveItem(selectedNode[i], "down");
	}
}

function moveNodeLeft()
{
	var selectedNode = tree.getSelectedItemId().split(",");	
	var length= selectedNode.length;
	var i=0;	
	for(i=0; i<length; i++)
		tree.moveItem(selectedNode[i], "left");
}

function moveNodeRight()
{

	var selectedNode = tree.getSelectedItemId();	
	
	var subItems = tree.getAllSubItems(0).split(",");
	
	var subItemsLength = subItems.length;
	
	var i=0;	
	for(i=0; i<subItemsLength ; i++)
	{
		if(subItems[i]==selectedNode)
		{
				tree.moveItem(selectedNode, "item_child", subItems[i-1]);
				
		}
	}	
}

function onAutogenerate()
{

	var selectedNode = tree.getSelectedItemId();	
	
}

</script>

</head>
<html:errors />
<html:form method="GET" action="SearchCategory.do">
	
	<table border="1" width="20%" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" height="100%" bordercolorlight="#000000" id="table11">
	<tr>
		<td colspan="7" height="5%">
				<bean:message key="query.customTree"/>
		</td>
	
	</tr>
		<tr>
			<td valign="top" colspan="7" > 
				<table border="0" width="208" valign="top" cellspacing="0" height="100%">		
					<td width="500" style="border:solid 1px;" valign="top">
						<div id="treebox" style="width:400; height:500; overflow:auto;"/>
					</td>						
				</table>
			</td>
		</tr>														
		<tr height="5%">
		<td width="25%" valign="center" >
				<bean:message key="query.treeHierarchy"/>
		</td>
		
		<td width="25%" valign="center" >
				<html:button property="Up" onclick="moveNodeUp()">							
					<bean:message key="query.moveNodeUp"/>
				</html:button>			
		</td>

		<td width="25%"  valign="center" >
				<html:button property="Down" onclick="moveNodeDown()">							
					<bean:message key="query.moveNodeDown"/>
				</html:button>			
		</td>

		<td width="25%" valign="center" >
				<html:button property="Left" onclick="moveNodeLeft()">							
					<bean:message key="query.moveNodeLeft"/>
				</html:button>			
		</td>

		<td width="25%" valign="center"  >
				<html:button property="Right" onclick="moveNodeRight()">							
					<bean:message key="query.moveNodeRight"/>
				</html:button>			
		</td>
		
		<td width="25%" valign="center" >
			<bean:message key="query.or"/>
		</td>
		
		<td width="25%" valign="center"  >
				<html:button property="Right" onclick="onAutogenerate()">							
					<bean:message key="query.autogenerate"/>
				</html:button>		
		</td>

		
		</tr>	
		
	</table>
</html:form>
</html>
<script> 
	var tree=new dhtmlXTreeObject("treebox","100%","100%",0);
	var itemID =1;
	tree.enableTreeLines(false);
	tree.enableMultiselection(true);
</script>