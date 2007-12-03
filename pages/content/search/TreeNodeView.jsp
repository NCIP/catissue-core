<html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.tree.StorageContainerTreeNode"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 	String reload =request.getParameter(Constants.RELOAD);
	String pageOf = request.getParameter(Constants.PAGEOF);
	String storageContainerType = null;
	String treeNodeIDToBeReloaded= null;
	String storageContainerID = null;
	String position = null;
	String propertyName = null, cdeName=null;
	String height = "585",width="500";
	if(reload!=null && reload.equals("true"))
    	{
    		treeNodeIDToBeReloaded=request.getParameter(Constants.TREE_NODE_ID);
		}
	if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION) || pageOf.equals(Constants.PAGEOF_SPECIMEN)|| pageOf.equals(Constants.PAGEOF_ALIQUOT))
	{
		storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
		storageContainerID = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
		position = (String)request.getAttribute(Constants.STORAGE_CONTAINER_POSITION);
	}
	else if (pageOf.equals(Constants.PAGEOF_TISSUE_SITE))
	{
		propertyName = request.getParameter(Constants.PROPERTY_NAME);
		cdeName = request.getParameter(Constants.CDE_NAME);
	}
%>

<head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<body>
<script language="javascript">
platform = navigator.platform.toLowerCase();
	if (platform.indexOf("mac") != -1)
	{
		<%=width%>="335"; <%=height%>="500";
	}
	else
	{
		<%=width%>="100%"; <%=height%>="100%";
	}
</script>
<table border="0" cellpadding="0" cellspacing="0">
			<tr>	
				<td class="formLabelAllBorder" colspan="2" width="170">
				</td>
			</tr>	
			<tr>
				<td align="left" colspan="2">
					<div id="treeboxbox_tree" style="width:<%=width%>; height:<%=height%>;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" width="170">
				</td>
			</tr>		
	</table>

	<script language="javascript">
	
		//This function is called when any of the node is selected in the tree 
		function tonclick(id)
			{				
				var nodeId=tree.getUserData(id,'nodeId');
			    var activityStatus=tree.getUserData(id,'activityStatus');
			    var parentId=tree.getUserData(id,'parentId');
				if( parentId != "0")
				{					
					if("<%=pageOf%>" == "<%=Constants.PAGEOF_SPECIMEN%>")
					{
						window.parent.<%=Constants.DATA_VIEW_FRAME%>.location="<%=Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION%>?<%=Constants.SYSTEM_IDENTIFIER%>="+nodeId+"&<%=Constants.PAGEOF%>=<%=pageOf%>&<%=Constants.ACTIVITY_STATUS%>="+activityStatus+"";
					}	
					else
					{
						window.parent.<%=Constants.DATA_VIEW_FRAME%>.location="<%=Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION%>?<%=Constants.SYSTEM_IDENTIFIER%>="+nodeId+"&<%=Constants.STORAGE_CONTAINER_TYPE%>=<%=storageContainerType%>&<%=Constants.PAGEOF%>=<%=pageOf%>&<%=Constants.ACTIVITY_STATUS%>="+activityStatus;
					}
				}
				else
				{
					window.parent.<%=Constants.DATA_VIEW_FRAME%>.location="<%=Constants.BLANK_SCREEN_ACTION%>";
				}

			};	
			
			<%--Creating nodes for the DHTML tree --%>
			<%-- parent node id for root node is "0" --%>
			tree=new dhtmlXTreeObject("treeboxbox_tree","100%","100%",0);
			tree.setImagePath("dhtml_comp/imgs/");
			tree.setOnClickHandler(tonclick);

			<%		 	//Iterating over the tree-vector and inserting into the dhtmlx-tree one by one
						Vector treeData = (Vector)request.getAttribute("treeData");
						if(treeData != null && treeData.size() != 0)
							{
								Iterator itr  = treeData.iterator();
								while(itr.hasNext())
								{
									StorageContainerTreeNode data= (StorageContainerTreeNode)itr.next();
									String nodeId = data.getValue();
									String parentId ="0";
									if(data.getParentNode()!=null) 
										parentId=data.getParentNode().toString();
									String DisplayName=data.getValue();
									String img = "bluebox.gif";
			%>
									tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=DisplayName%>",0,"<%=img%>","<%=img%>","<%=img%>","");
									tree.setUserData("<%=nodeId%>",'nodeId',"<%=data.getIdentifier()%>");	
									tree.setUserData("<%=nodeId%>",'activityStatus',"<%=data.getActivityStatus()%>");
									tree.setUserData("<%=nodeId%>",'parentId',"<%=parentId%>");
					<%	
								}
							}
					%>		
			<% if(reload!=null && reload.equals("true"))
			   { %>
			   tree.closeAllItems("0");
			   tree.selectItem("<%=treeNodeIDToBeReloaded%>",false);
				
			<% }
			else
				{%>
			tree.closeAllItems("0");
			<% }%>
	</script>
</body>
</html>