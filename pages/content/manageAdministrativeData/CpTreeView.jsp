<html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
		
		String access = null;
		access = (String)session.getAttribute("Access");
		String divHeight = "150";
		if(access != null && access.equals("Denied"))
		{
			divHeight = "280";		
		}
		String operation = (String)request.getAttribute("operation");
		String operationType = (String)request.getAttribute("operationType");

%>
<head>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
<link rel="STYLESHEET" type="text/css"
	href="dhtml_comp/css/dhtmlXTree.css">
<script language="JavaScript" type="text/javascript"
	src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="text/javascript"
	src="dhtml_comp/js/dhtmlXTree.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script src="jss/caTissueSuite.js" language="JavaScript" type="text/javascript"></script>
</head>

<body>
<table border="0" cellpadding="0" cellspacing="0">


	<tr>	
		 <td class="formLabelAllBorder"  style="background-color: #ffffff; border: 1px solid Silver; overflow: auto;border-left:1px solid #61a1e3;border-right:1px solid #61a1e3;border-top:1px solid #61a1e3;" colspan="2" width="170">
			<b>Collection Protocol Details :</b>
		</td>
		<td align="left" colspan="2">
		  <tr>
			<td align="left" colspan="2">
			<div id="treeboxbox_tree"
				style="width: 180px; height: 368px; background-color: #ffffff; border: 1px solid Silver; overflow: auto;border-left:1px solid #61a1e3;	border-right:1px solid #61a1e3;	border-bottom:1px solid #61a1e3;border-top:1px solid #61a1e3;" />
			</td>
		  </tr>
		</td>
	</tr>
</table>

<script language="javascript">
			//This function is called when any of the node is selected in tree 
			function tonclick(id)
			{
		        var str=id;		
				var name = tree.getItemText(id);
				var i = str.indexOf('_');
				var alias = str.substring(0,i);
				var uniqId = str.substring(i+1);
				if(uniqId.indexOf('class') != -1)
				{
					i = uniqId.lastIndexOf('_');
					uniqId = uniqId.substring(i+1);
				}		
				
			   if(alias == "New")
				{
					window.parent.frames['SpecimenRequirementView'].location="CreateSpecimenTemplate.do?operation=edit&pageOf=specimenRequirement&key="+uniqId+"&nodeId="+id+"&operationType=<%=operationType%>";
				}
				else if(alias == "ViewSummary")
				{
				
					window.parent.frames['SpecimenRequirementView'].location="GenericSpecimenSummary.do?Event_Id="+uniqId+"&nodeId="+id;
				}
				else if(alias == "cpName")
				{
					window.parent.frames['SpecimenRequirementView'].location="CollectionProtocol.do?operation=${requestScope.operation}&pageOf=pageOfCollectionProtocol&invokeFunction=cp";
				}
				
				else
				{
					window.parent.frames['SpecimenRequirementView'].location="ProtocolEventsDetails.do?operation=${requestScope.operation}&pageOf=defineEvents&key="+uniqId+"&nodeId="+id+"&operationType=<%=operationType%>";
				}
			}; 
									
			// Creating the tree object								
			tree=new dhtmlXTreeObject("treeboxbox_tree","100%","100%",0);
			tree.setImagePath("dhtml_comp/imgs/");
			tree.setOnClickHandler(tonclick);
			<%-- in this tree for root node parent node id is "0" --%>
			<%-- creating the nodes of the tree --%>
			<% Vector treeData = (Vector)request.getAttribute("treeData");
				if(treeData != null && treeData.size() != 0)
				{
					Iterator itr  = treeData.iterator();
					while(itr.hasNext())
					{
						QueryTreeNodeData data = (QueryTreeNodeData) itr.next();
						String tooltipText = data.getToolTipText();
						String parentId = "0";	
						String id = data.getIdentifier().toString();
						if(!data.getParentIdentifier().equals("0"))
						{
							parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();		
						}
						String nodeId = data.getObjectName() + "_"+id;
						String img = "Specimen.GIF";
						
						String diaplayName = data.getDisplayName();
						String name=null;
						StringTokenizer stringTokenizer =new StringTokenizer(diaplayName, "_");
						if(stringTokenizer!=null)
						{	
							while(stringTokenizer.hasMoreTokens())
							{
								name = stringTokenizer.nextToken();
							}
						}
						
						if(name.startsWith("S"))
						{
							img = "Specimen.GIF";
						}
						else if(name.startsWith("A"))
						{
							img = "aliquot_specimen.gif";
						}
						else if(name.startsWith("D"))
						{
							img = "derived_specimen.gif";
						}
						else
						{
							img = "cp_event.gif";
						}
						if((data.getObjectName()).equals("cpName"))
						{
							img ="CollectionProtocol.GIF";
                        }
						
			%>

					tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
					tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
					tree.setItemText("<%=nodeId%>","<%=data.getDisplayName()%>","<%=tooltipText%>");
			<%	
					}
				}
			%>
			
			<%if( request.getSession().getAttribute("nodeId") != null)
			{
				String nodeId =(String)request.getSession().getAttribute("nodeId");
				if(request.getSession().getAttribute("clickedNode")!=null)//nodeIdToBeShownSelected
				{
				String clickedNode  = (String)request.getSession().getAttribute("clickedNode");
				%>

				
						var str="<%=clickedNode%>";
						var i = str.indexOf('_');
						var alias =str.substring(0,i);
						if(alias == "New")
					{
						var str2="<%=nodeId%>";
						var e1=str.substring(i+1,i+3);
						if(str.indexOf(e1)>0 && str2.indexOf(e1)>0)
						{
							tree.selectItem("<%=clickedNode%>",false);
							tree.openItem("<%=nodeId%>");
						}
						else
						{
							tree.selectItem("<%=clickedNode%>",false);
							tree.openItem(parentId);
						}
					}
					else
					{
						tree.selectItem("<%=clickedNode%>",false);
						tree.openItem("<%=clickedNode%>");
					}
			<% } else 
				{
			%>
				
					tree.selectItem("<%=nodeId%>",false);
					tree.openItem("<%=nodeId%>");
			<%	}
			   }
			%>	
	</script>
</body>
</html>