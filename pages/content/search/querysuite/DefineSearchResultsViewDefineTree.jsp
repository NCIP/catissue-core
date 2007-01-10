
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
	Map m = (Map) request.getAttribute("treesMap");
%>
<script>

function showTree(parentsNode)
{
	document.getElementById("treebox").innerHTML ="";
	  <% 
        for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
           String element = (String) iter.next();
           Vector treeData1 = (Vector)m.get(element);  %>
			if(parentsNode == "<%= element %>")
			{
			   	tree=new dhtmlXTreeObject("treebox","100%","100%",0);
				tree.setImagePath("dhtml_comp/imgs/");
				<% 
					if(treeData1 != null && treeData1.size() != 0)
					{
						Iterator itr  = treeData1.iterator();
						while(itr.hasNext())
						{
							QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
							String parentId = "0";	
							if(!data.getParentIdentifier().equals("0"))
							{
								parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();		
							}
							String nodeId = data.getObjectName() + "_"+data.getIdentifier().toString();
							String img = "plus.GIF";
							if(data.getObjectName().equals(Constants.SPECIMEN_COLLECTION_GROUP))
							{
								img = "plus.GIF";
							}				
							%>
							tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"","","","");
							tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
							<%	
						}
					}
				%>	
			} 
		<% 
	}
 %>	
}
function showDefaultTree()
{
	<%
		 for (Iterator KeysIter = m.keySet().iterator(); KeysIter.hasNext();)
		{
			   String element = (String) KeysIter.next();
			   Vector treeData1 = (Vector)m.get(element);  %>
				tree=new dhtmlXTreeObject("treebox","100%","100%",0);
				tree.setImagePath("dhtml_comp/imgs/");
				<% 
					if(treeData1 != null && treeData1.size() != 0)
					{				
						Iterator itr  = treeData1.iterator();
						while(itr.hasNext())
						{
							QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
							String parentId = "0";	
							if(!data.getParentIdentifier().equals("0"))
							{
								parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();		
							}
							String nodeId = data.getObjectName() + "_"+data.getIdentifier().toString();
							String img = "plus.GIF";
							if(data.getObjectName().equals(Constants.SPECIMEN_COLLECTION_GROUP))
							{
								img = "plus.GIF";
							}				
							%>
							tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"","","","");
							tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
							<%	
						}
					}
			break;
		} 
	%>			
}
function BuildNewTree()
{
	alert("dfds");
	document.forms['categorySearchForm'].nextOperation.value = "BuildNewTree";
	document.forms['categorySearchForm'].action='DefineSearchResultsView.do';
	document.forms['categorySearchForm'].submit();
}
</script>
<html>
<input type="hidden" name="nextOperation" value=""/>
<body onLoad="showDefaultTree()">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<table border="0" width="100%" id="table3" height="100%" cellspacing="0" cellpadding="0">
	<tr  height="5%" valign="center" width="100%">
		<td width="25%" height="5%" colspan="2" valign="center"  class="queryModuleTabSelected">
			<bean:message key="query.step1.defineTree"/>
		</td>
		<td colspan="3" valign="center" height="5%" class="queryModuleTab" >
			<bean:message key="query.step2.defineSpreadsheet"/>
		</td>
	</tr>
	<tr valign="top" height="5" >
		<td valign="top" colspan="5" class="defineResultsViewMessage" height="1%" width="100%"><bean:message key="query.defineResultsViewMessage"/></td>
	</tr>
	<tr valign="top" height="5" >
		<td height="1%"  valign="top" colspan="5" class="defineResultsViewMessage" height="2%" width="100%"><html:button property="buildNewTree" onclick="BuildNewTree()"><bean:message key="query.buildNewTree"/></html:button></td>
	</tr>
	<tr valign="top" height="5%" >										
		<td height="5%"  valign="top" colspan="5" class="defineResultsViewMessage">
			<table border="0" width="100%" height="100%">
				<tr valign="top">
					<td align="left"  height="1%" class="headerStyle">
						<bean:message key="query.systemDefinedTrees"/>
					</td>
					<td valign="top" height="1%" class="headerStyle">
						<bean:message key="query.treePreview"/>
					</td>										
				</tr>
				<tr>
					<td valign="top" width="25%" style="border:solid 1px;" height="400">
						<table>																											
							<%			
								boolean isFirst = true;
								Iterator iter = m.keySet().iterator();
								while (iter.hasNext())
								{
									String parentNode = (String)iter.next();
									Vector treeData = (Vector)m.get(parentNode);
							%>
							<tr>
								<td>
									<% 
									if (isFirst)
										{ 
											%>
													<input type="radio" name="selected" onclick="showTree('<%= parentNode%>')" CHECKED><font face="Arial"><font size="2"><%= parentNode%></font>
											<%  isFirst = false;
										} else {
									%>
									<input type="radio" name="selected"  value="<%= parentNode%>" onclick="showTree('<%= parentNode%>')"><font face="Arial"><font size="2"><%= parentNode%></font>
									<% 
										}
									%>
								</td>
							</tr>
							<%
							}
							%>
					</table>
					</td>
					<td width="500" style="border:solid 1px;" valign="top">
						<div id="treebox" style="width:400; height:250; overflow:auto;"/>
					</td>
				</tr>
			</table> 
		</td>
	</tr>


</table>
</body>
</html>