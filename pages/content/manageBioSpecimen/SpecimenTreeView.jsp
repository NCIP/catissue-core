<html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
		String participantId=(String)request.getAttribute(Constants.CP_SEARCH_PARTICIPANT_ID);
		String cpId=(String)request.getAttribute(Constants.CP_SEARCH_CP_ID);
		String access = null;
		access = (String)session.getAttribute("Access");
		String divHeight = "170";
		if(access != null && access.equals("Denied"))
		{
			divHeight = "280";		
		}

%>
<head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
		<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
</head>

<body>
	<table>
		<tr>	
			<td>&nbsp;&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder">
				Details :
			</td>
		</tr>	
		<tr>
		<td>&nbsp;&nbsp;</td>
			<td align="left">
				<div id="treeboxbox_tree" style="width:161; height:<%=divHeight%>;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"/>
			</td>
			
		</tr>
		<tr>
		<td>&nbsp;&nbsp;</td>
		<td>
		
		<input type="hidden" name="participantId" value="<%=participantId%>"/>
		<input type="hidden" name="cpId" value="<%=cpId%>"/>
		</td></tr>		
	</table>

	<script language="javascript">
			function tonclick(id)
			{
				var str = id;
				var name = tree.getItemText(id);
				var i = str.indexOf('_');
				var obj1 = str.substring(0,i);
				var id1 = str.substring(i+1);
				if(obj1 == "<%=Constants.SPECIMEN_COLLECTION_GROUP%>")
				{
					<%if(access != null && access.equals("Denied"))
					{%>
					window.parent.frames[2].location = "CPQuerySpecimenCollectionGroupForTech.do?pageOf=pageOfSpecimenCollectionGroupCPQuery&operation=edit&id="+id1+"&name="+name;
					<%}else {%>
					window.parent.frames[2].location = "QuerySpecimenCollectionGroupSearch.do?pageOf=pageOfSpecimenCollectionGroupCPQueryEdit&operation=edit&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+<%=participantId%>+"&<%=Constants.CP_SEARCH_CP_ID%>="+<%=cpId%>;
					<%}%>
				}
				else
				{
					window.parent.frames[2].location = "QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&operation=edit&id="+id1+"&<%=Constants.CP_SEARCH_PARTICIPANT_ID%>="+<%=participantId%>+"&<%=Constants.CP_SEARCH_CP_ID%>="+<%=cpId%>;
				}
			};
													
			tree=new dhtmlXTreeObject("treeboxbox_tree","100%","100%",0);


			tree.setImagePath("dhtml_comp/imgs/");
			tree.setOnClickHandler(tonclick);
			<% Vector treeData = (Vector)request.getAttribute("treeData");
				if(treeData != null && treeData.size() != 0)
				{
					Iterator itr  = treeData.iterator();
					while(itr.hasNext())
					{
						QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
						String parentId = "0";	
						if(!data.getParentIdentifier().equals("0"))
						{
							parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();
		
						}
						String nodeId = data.getObjectName() + "_"+data.getIdentifier().toString();
						String img = "Specimen.GIF";
						if(data.getObjectName().equals(Constants.SPECIMEN_COLLECTION_GROUP))
						{
							img = "SpecimenCollectionGroup.GIF";
						}

				
			%>
					tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
					tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
			<%	
					}
				}

			%>
			tree.closeAllItems("0");
			<%if(request.getParameter("nodeId") != null)
			{
				String nodeId = request.getParameter("nodeId");
				
			%>
			var parentId = tree.getParentId("<%=nodeId%>");
			tree.openItem(parentId);
			tree.selectItem("<%=nodeId%>",false);
			tree.openItem("<%=nodeId%>");
			
			<%}%>	
										
	
			
	</script>


</body>

</html>