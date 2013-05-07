<html>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.tree.QueryTreeNodeData"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.caties.util.CaTIESConstants"%>
<%@ page import="java.util.*"%>

<%

	String divHeight = "100%";
	String divWidth = "278";
	String partId ="";
	String conflictStatus = (String)request.getParameter(Constants.CONFLICT_STATUS);
	String reportQueueId = (String)request.getParameter(Constants.REPORT_ID);
	boolean buttonDisable=true;
	boolean buttonEnable=false;


%>

<head>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
	<title>DHTML Tree samples. dhtmlXTree - Action handlers</title>
	<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
	<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<body>
	<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<div id="treeboxbox_tree" style="background-color:#f5f5f5; overflow:auto;width:<%=divWidth%>;" height=<%=divHeight%>/>
				</td>
			</tr>
	</table>
	<script language="javascript">
	
			function setButtons(obj1,conflictStat)
			{
			
					if(obj1 == "<%=Constants.PARTICIPANT%>")
					{	
						var selSCGId=parent.document.getElementById("useSelSCG");
						selSCGId.disabled="<%=buttonDisable%>";

						if(conflictStat!="<%=CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT%>")	
						{
							var selPartId=parent.document.getElementById("useSelPart");
							if(selPartId.disabled == true)
							{
								selPartId.disabled=false;
							}
						}	
					
					}

					if(obj1 == "<%=Constants.SPECIMEN_COLLECTION_GROUP%>")
					{
						if(conflictStat!="<%=CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT%>")	
						{
							var selPartId=parent.document.getElementById("useSelPart");
							selPartId.disabled="<%=buttonDisable%>";
						}
						
						var selSCGId=parent.document.getElementById("useSelSCG");
						if(selSCGId.disabled == true)
						{
							selSCGId.disabled=false;
						}
					}
				
			}

			//This function is called when any of the node is selected in tree 
			function tonclick(id)
			{
				
				
				var index = id.indexOf(":");
				var isFuture = "";
				if(index != -1)
				{
					isFuture = id.substring(index+1);
					var str = id.substring(0,index);
				}
				else
				{
				var str = id;
				}
				var name = tree.getItemText(id);
				var i = str.indexOf('_');
				var obj1 = str.substring(0,i);
				var id1 = str.substring(i+1);
				var conflictStat = "<%=conflictStatus%>";
				var reportId = "<%=reportQueueId%>";

				
					
				if(conflictStat == "<%=CaTIESConstants.STATUS_SCG_CONFLICT%>")
				{
						window.parent.frames[2].location = "ConflictSCGAction.do?id="+id1+"&reportQueueId="+reportId;
				}
				else
				{
					setButtons(obj1,conflictStat);
					if(obj1 == "<%=Constants.PARTICIPANT%>")
					{	
						
						window.parent.frames[2].location = "ConflictParticipantDataDetails.do?id="+id1;
					}
					else
					{
										
						if(obj1 == "<%=Constants.SPECIMEN_COLLECTION_GROUP%>")
						{
							window.parent.frames[2].location = "ConflictSCGDataDetails.do?id="+id1;
						}
					
					}
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
						String parentId = "0";	
						String id = data.getIdentifier().toString();

						String ParentId1 = data.getParentIdentifier().toString();
					

						if(!data.getParentIdentifier().equals("0"))
						{
							parentId = data.getParentObjectName() + "_"+ data.getParentIdentifier().toString();
							
						}
						
						String nodeId = data.getObjectName() + "_"+id;
						
						String img = "Participant.GIF";
						if(data.getObjectName().equals(Constants.PARTICIPANT) && conflictStatus.equals(CaTIESConstants.STATUS_SCG_PARTIAL_CONFLICT))
						{
							partId = nodeId;
							
						}
						
						if(data.getObjectName().equals(Constants.PARTICIPANT))
						{
							
						}
						if(data.getObjectName().equals(Constants.COLLECTION_PROTOCOL))
						{
							img = "CollectionProtocol.GIF";
						}

						if(data.getObjectName().equals(Constants.SPECIMEN_COLLECTION_GROUP))
						{
							img = "SpecimenCollectionGroup.GIF";
						}
						
						
			%>
					tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getDisplayName()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
					tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
					tree.setItemText("<%=nodeId%>","<%=data.getDisplayName()%>");
			<%	
					}
				}
			%>
			tree.closeAllItems("0");
			//This will be called in case of scg partial conflict
			tonclick("<%=partId%>");

			
	</script>
</body>
</html>