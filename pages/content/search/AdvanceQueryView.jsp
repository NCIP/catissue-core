<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.SearchUtil"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<head>

	<link rel="StyleSheet" href="tree.css" type="css/tree.css">
	<script type="text/javascript" src="jss/tree.js"></script>
	<LINK REL="StyleSheet" HREF="css/menu.css">
	<script language="javascript" src="jss/menu.js"></script>
	<script type="text/javascript">
	<%
		Vector treeList = (Vector)request.getAttribute(Constants.TREE_VECTOR);
	%>
	var tree = [<%int k;%><%for (k=0;k < (treeList.size()-1);k++){%>"<%=treeList.get(k)%>",<%}%>"<%=treeList.get(k)%>"];
	
	function enableCommonItems(selectedNode,itemId,linkName)
	{
		var editItem = document.getElementById('<%=Constants.EDIT%>');
		editItem.className="formTitle";
		var editLink = linkName + selectedNode +"&itemId="+itemId;
		var s = "<a HREF='" + editLink + "' target='searchPageFrame'><font color='white'>Edit</font></a>"
		editItem.innerHTML = "" + s;
			
			
		var andItem = document.getElementById('and');
		andItem.className="linkChange";
		var link = "AdvanceQueryView.do?operator=EXIST&itemId="+itemId;
		andItem.innerHTML ="&nbsp;<img src='images/point.gif' alt='pAND' />&nbsp;<a HREF='"+link+"'><%=Constants.ADVANCED_QUERY_AND%></a>"
			
		var orItem = document.getElementById('or');
		orItem.className="linkChange";
		var link = "AdvanceQueryView.do?operator=OR&itemId="+itemId;
		orItem.innerHTML ="&nbsp;&nbsp;<img src='images/graydot.gif' alt='OR' />&nbsp;&nbsp;<a HREF='"+link+"'><%=Constants.ADVANCED_QUERY_OR%></a>"
		
	}				
	function disableAll()
	{
		var item = document.getElementById('<%=Constants.CP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<%=Constants.MENU_COLLECTION_PROTOCOL%>";
		
		/*item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp; <%=Constants.MENU_DISTRIBUTION_PROTOCOL%>";*/
		
		item = document.getElementById('<%=Constants.P%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Participant.GIF' alt='Participant' /> &nbsp;<%=Constants.PARTICIPANT%>";
		
		item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML = "&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp; <%=Constants.SPECIMEN%>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='Specimen Collection Group' /> &nbsp;<%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%>";
		
		/*item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Distribution.GIF' alt='Distribution' /> &nbsp; <%=Constants.DISTRIBUTION%>";*/
		
		item = document.getElementById('<%=Constants.EDIT%>');
		item.className = "formTitle";
		item.innerHTML = "Edit";
				
		var deleteItem = document.getElementById('<%=Constants.DELETE%>');
		deleteItem.className = "formTitle";
		deleteItem.innerHTML = "Delete";
		
		var andItem = document.getElementById('and');
		andItem.className="linkChange";
		//var link = "AdvanceQueryView.do?operator=AND&itemId="+itemId;
		andItem.innerHTML ="&nbsp;<img src='images/point.gif' alt='And' />&nbsp;<%=Constants.ADVANCED_QUERY_AND%>";
			
		var orItem = document.getElementById('or');
		orItem.className="linkChange";
		//var link = "AdvanceQueryView.do?operator=OR&itemId="+itemId;
		orItem.innerHTML ="&nbsp;&nbsp;<img src='images/graydot.gif' alt='OR' />&nbsp;&nbsp;<%=Constants.ADVANCED_QUERY_OR%>";
				
		/*item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp; <a HREF='<%=SearchUtil.getLink("Specimen")%>' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='Specimen Collection Group' /> &nbsp;<a HREF='<%=SearchUtil.getLink("SpecimenCollectionGroup")%>' target='searchPageFrame'><%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%></a>";
		
		item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Distribution.GIF' alt='Distribution' /> &nbsp; <a HREF='#'><%=Constants.DISTRIBUTION%></a>";
		
		item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp; <a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";*/
	}

	function checkNum(nodeCount)//checkName,itemId,
	{
		disableAll();
		var participantCount = 0;
		var chk = "";
		var cProtocolCount = 0;
		//var cProtocolCount = 0;
		var sCGroupCount = 0;
		//var distributionCount = 0;
		var specimenCount = 0;
		//var j=0;
		//var selectedNode = new Array(nodeCount);
		var selectedNode="";
		var itemId = 0;
								
		for(var i = 1; i <= nodeCount; i++)
		{
			var participantItem = '<%=Constants.PARTICIPANT%>'+"_"+i;
			if(document.getElementById(participantItem) != null)
			{
				if(document.getElementById(participantItem).checked==true)
				{
					participantCount = participantCount + 1;
					selectedNode=selectedNode+","+i;
					itemId = i;
					chk = participantItem
				}
			}
			
			var cProtocolItem = '<%=Constants.COLLECTION_PROTOCOL%>'+"_"+i;
			if(document.getElementById(cProtocolItem) != null)
			{
				if(document.getElementById(cProtocolItem).checked==true)
				{
					cProtocolCount = cProtocolCount + 1;
					selectedNode=selectedNode+","+i;
					itemId = i;
					chk = cProtocolItem;
				}
			}
			
			/*var DProtocolItem = '<%=Constants.DISTRIBUTION_PROTOCOL%>'+"_"+i;
			//DProtocolCount = checkStatus(item3, count3,selectedNode);
			if(document.getElementById(DProtocolItem) != null)
			{
				if(document.getElementById(dProtocolItem).checked==true)
				{
					DProtocolCount = DProtocolCount + 1;
					selectedNode=selectedNode+","+i;
				}
			}*/
			
			var sCGroupItem = '<%=Constants.SPECIMEN_COLLECTION_GROUP%>'+"_"+i;
			if(document.getElementById(sCGroupItem) != null)
			{
				if(document.getElementById(sCGroupItem).checked==true)
				{
					sCGroupCount = sCGroupCount + 1;
					selectedNode=selectedNode+","+i;
					itemId = i;
					chk = sCGroupItem;
				}
			}
			
			/*var DistributionItem = '<%=Constants.DISTRIBUTION%>'+"_"+i;
			//count5 = checkStatus(item5, count5,selectedNode,i);
			if(document.getElementById(DistributionItem) != null)
			{
				if(document.getElementById(distributionItem).checked==true)
				{
					distributionCount = distributionCount + 1;
					selectedNode=selectedNode+","+i;
				}
			}*/
			var specimenItem = '<%=Constants.SPECIMEN%>'+"_"+i;
			//count6 = checkStatus(item6, count6,selectedNode,i);
			if(document.getElementById(specimenItem) != null)
			{
				if(document.getElementById(specimenItem).checked==true)
				{
					specimenCount = specimenCount + 1;
					selectedNode=selectedNode+","+i;
					itemId = i;
					chk = specimenItem;
				}
			}
		}
		
		//var sum = participantCount + cProtocolCount + DProtocolCount + sCGroupCount + distributionCount + specimenCount;
		var sum = participantCount + cProtocolCount +  sCGroupCount +  specimenCount;
		if (selectedNode=="")
		{
			selectedNode="-1";
		}
		if( (participantCount == sum) && (participantCount == 1) ) 
		{
			/*var PItem = document.getElementById('<%=Constants.P%>');
			PItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("Participant")%>"+ selectedNode;
			PItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";*/
			
			showAddLinks("Participant",selectedNode);

			/*DPItem = document.getElementById('<%=Constants.DP%>');
			DPItem.className="linkChange";
			//var link = "<%=SearchUtil.getLink("DistributionProtocol")%>"+ selectedNode;
			DPItem.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp;<a HREF='#'><%=Constants.MENU_DISTRIBUTION_PROTOCOL%></a>";*/
			
			var linkName = "<%=SearchUtil.getLink("Participant")%>";
			enableCommonItems(selectedNode,itemId,linkName);
		}
		else if( (cProtocolCount == sum) && (cProtocolCount == 1) )
		{
			//alert("clicked coll prot");
			
			/*var CPItem = document.getElementById('<%=Constants.CP%>');
			CPItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("CollectionProtocol")%>"+ selectedNode;
			CPItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.MENU_COLLECTION_PROTOCOL%></a>";*/
			
			/*var scGItem = document.getElementById('<%=Constants.SCG%>');
			scGItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("SpecimenCollectionGroup")%>"+ selectedNode;
			scGItem.innerHTML ="&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='SpecimenCollectionGroup' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%></a>";*/
			
			showAddLinks("CollectionProtocol",selectedNode);
						
			var linkName = "<%=SearchUtil.getLink("CollectionProtocol")%>";
			enableCommonItems(selectedNode,itemId,linkName);
		}
		else if( (sCGroupCount == sum) && (sCGroupCount == 1))
		{
			var sItem = document.getElementById('<%=Constants.S%>');
			sItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("Specimen")%>"+ selectedNode;
			sItem.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
			
			showAddLinks("SpecimenCollectionGroup",selectedNode);
			
			var linkName = "<%=SearchUtil.getLink("SpecimenCollectionGroup")%>";
			enableCommonItems(selectedNode,itemId,linkName);
		}
		else if( (specimenCount == sum) && (specimenCount == 1))
		{
		
			/*var sItem = document.getElementById('<%=Constants.S%>');
            sItem.className="linkChange";
            var link = "<%=SearchUtil.getLink("Specimen")%>"+ selectedNode;
            sItem.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";*/
			showAddLinks("Specimen",selectedNode);
			var linkName = "<%=SearchUtil.getLink("Specimen")%>";
			enableCommonItems(selectedNode,itemId,linkName);
		}
		else 
			disableAll();
			
		if(sum == 0)
		{
			disableAll();
			/*var pItem = document.getElementById('<%=Constants.P%>');
			pItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("Participant")%>"+ selectedNode;
			pItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";*/
			showAddLinks("None",selectedNode);
		}
		else if(sum>0)
		{
			var deleteItem = document.getElementById('<%=Constants.DELETE%>');
			deleteItem.className="formTitle";
			var deleteLink = "AdvanceSearch.do?delete=true&itemId="+selectedNode;
			deleteItem.innerHTML = "&nbsp;<a HREF='" + deleteLink + "'><font color='white'>Delete</font></a>";
		}
		
	}	
	
	//To activate or deactivate links depending upon the objectName of the node selected.
	function showAddLinks(objectName, selectedNode)
	{
		var link;
		switch (objectName)
		{
		case "None":
			var pItem = document.getElementById('<%=Constants.P%>');
			pItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("Participant")%>"+ selectedNode;
			pItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		
		case "Participant" :
			var cpItem = document.getElementById('<%=Constants.CP%>');
			cpItem.className="linkChange";
			link = "<%=SearchUtil.getLink("CollectionProtocol")%>"+ selectedNode;
			cpItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.MENU_COLLECTION_PROTOCOL%></a>";
			
		case "CollectionProtocol":
			var scGItem = document.getElementById('<%=Constants.SCG%>');
			scGItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("SpecimenCollectionGroup")%>"+ selectedNode;
			scGItem.innerHTML ="&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='SpecimenCollectionGroup' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%></a>";
			
		case "SpecimenCollectionGroup":
		case "Specimen":
			var sItem = document.getElementById('<%=Constants.S%>');
			sItem.className="linkChange";
			var link = "<%=SearchUtil.getLink("Specimen")%>"+ selectedNode;
			sItem.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
		}
	}
	
	function changeClass(element,styleName)
	{
		element.className = styleName;
	}
	--></script>
</head>
<html>
<body>

	<table cellpadding='0' cellspacing='0' border='0' width='600'>
		<tr>
			<td class='formTitle' height='20' width='60%'>
				<img src="images/arrow.GIF" alt="Rules" />Rules
			</td>
			
			
			<td class='formTitle'  height='20' width='10%' onmouseover="expand(this);" onmouseout="collapse(this);" align='centre'><%=Constants.ADVANCED_QUERY_ADD%> 
				<table class='menuNormal' border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td>
							<div class='menuNormal' width='140'>
								<table class='menuNormal' width='140' border='0' cellpadding='3' cellspacing='1'>
									<tr height='20'  vAlign="middle">
										<td colspan=2 class='linkChange' id='P' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/Participant.GIF" alt="Participant" /> &nbsp; 
												<a HREF='<%=SearchUtil.getLink("Participant")%>' target='searchPageFrame'>
												<%=Constants.PARTICIPANT%>
											</a>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='CP' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
												&nbsp;<img src="images/CollectionProtocol.GIF" alt="CollectionProtocol" />  &nbsp;	<%=Constants.MENU_COLLECTION_PROTOCOL%>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='SCG' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/SpecimenCollectionGroup.GIF" alt="Specimen Collection Group" /> &nbsp; <%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%>
										</td>
									</tr>
									
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='S' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/Specimen.GIF" alt="Specimen" /> &nbsp; <%=Constants.SPECIMEN%>
										</td>
									</tr>
									<!--tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='D' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
										&nbsp;<img src="images/Distribution.GIF" alt="Distribution" /> &nbsp; <%=Constants.DISTRIBUTION%>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='DP' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
										&nbsp;<img src="images/DistributionProtocol.GIF" alt="DistributionProtocol" /> &nbsp; <%=Constants.MENU_DISTRIBUTION_PROTOCOL%>
										</td>
									</tr-->
								</table>
							</div>
						</td>
					</tr>
			</table>
			</td>
		<td class='formTitle' id='edit' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');"><%=Constants.ADVANCED_QUERY_EDIT%></td>
		
		<td class='formTitle' id='delete' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');"><%=Constants.ADVANCED_QUERY_DELETE%></td>
		
		<td class='formTitle'  height='20' width='10%' onmouseover="expand(this);" onmouseout="collapse(this);" align='centre'><%=Constants.ADVANCED_QUERY_OPERATOR%> 
			<table class='menuNormal' border='0' cellpadding='0' cellspacing='0'>
				<tr>
					<td>
						<div class='menuNormal' width='140'>
							<table class='menuNormal' width='100' border='0' cellpadding='3' cellspacing='1'>
								<tr height='20'  vAlign="middle">
									<td colspan=2 class='linkChange' id='and' noWrap  height='20' vAlign="middle" 
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/point.gif" alt="AND" /> &nbsp; 
											<%=Constants.ADVANCED_QUERY_AND%>
									</td>
								</tr>
								<tr height='20'  vAlign="middle">
									<td colspan=2 class='linkChange' id='or' noWrap  height='20' vAlign="middle" 
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;&nbsp;<img src="images/graydot.gif" alt="OR" /> &nbsp;&nbsp;
											<%=Constants.ADVANCED_QUERY_OR%>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
			
	</tr>

			<div class="tree">
				<script type="text/javascript">
					createTree(tree);
					checkNum(tree.length);
					
				</script>
			</div>
			



</table>

</body>
</html>

