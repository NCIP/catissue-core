<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.SearchUtil"%>
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
	<% 
		SearchUtil util = new SearchUtil(); 
	%>
		
	function disableAll()
	{
		var item = document.getElementById('<%=Constants.CP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<%=Constants.COLLECTION_PROTOCOL%>";
		
		item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp; <%=Constants.DISTRIBUTION_PROTOCOL%>";
		
		item = document.getElementById('<%=Constants.P%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Participant.GIF' alt='Participant' /> &nbsp;<%=Constants.PARTICIPANT%>";
		
		item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML = "&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp; <%=Constants.SPECIMEN%>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='Specimen Collection Group' /> &nbsp;<%=Constants.SPECIMEN_COLLECTION_GROUP%>";
		
		item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Distribution.GIF' alt='Distribution' /> &nbsp; <%=Constants.DISTRIBUTION%>";
		
		item = document.getElementById('<%=Constants.EDIT%>');
		item.className = "linkChange";
		item.innerHTML = "Edit";
		
		var deleteItem = document.getElementById('<%=Constants.DELETE%>');
		deleteItem.className = "linkChange";
		deleteItem.innerHTML = "Delete";
		
	}

	function checkNum(checkName,itemId,nodeCount,documentForm)
	{
		disableAll();
		var participantCount = 0;
		var cprotocolCount = 0;
		var dProtocolCount = 0;
		var scGroupCount = 0;
		var distributionCount = 0;
		var specimenCount = 0;
		var selectedNode=0;
								
		for(var i = 1; i <= nodeCount; i++)
		{
			var participantItem = '<%=Constants.PARTICIPANT%>'+"_"+i;
			if(document.getElementById(participantItem) != null)
			{
				if(document.getElementById(participantItem).checked==true)
				{
					participantCount = participantCount + 1;
					selectedNode=i;
				}
			}
			
			var cProtocolItem = '<%=Constants.COLLECTION_PROTOCOL%>'+"_"+i;
			if(document.getElementById(cProtocolItem) != null)
			{
				if(document.getElementById(cProtocolItem).checked==true)
				{
					cprotocolCount = cprotocolCount + 1;
					selectedNode=i;
				}
			}
			
			var dProtocolItem = '<%=Constants.DISTRIBUTION_PROTOCOL%>'+"_"+i;
			if(document.getElementById(dProtocolItem) != null)
			{
				if(document.getElementById(dProtocolItem).checked==true)
				{
					dProtocolCount = dProtocolCount + 1;
					selectedNode=i;
				}
			}
			
			var sCGroupItem = '<%=Constants.SPECIMEN_COLLECTION_GROUP%>'+"_"+i;
			if(document.getElementById(sCGroupItem) != null)
			{
				if(document.getElementById(sCGroupItem).checked==true)
				{
					scGroupCount = scGroupCount + 1;
					selectedNode=i;
				}
			}
			
			var distributionItem = '<%=Constants.DISTRIBUTION%>'+"_"+i;
			if(document.getElementById(distributionItem) != null)
			{
				if(document.getElementById(distributionItem).checked==true)
				{
					distributionCount = distributionCount + 1;
					selectedNode=i;
				}
			}
			var specimenItem = '<%=Constants.SPECIMEN%>'+"_"+i;
			if(document.getElementById(specimenItem) != null)
			{
				if(document.getElementById(specimenItem).checked==true)
				{
					specimenCount = specimenCount + 1;
					selectedNode=i;
				}
			}
		}
		
		var sum = participantCount + cprotocolCount + dProtocolCount + scGroupCount + distributionCount + specimenCount;
		
		if(participantCount == sum) 
		{
			/*var PItem = document.getElementById('<%=Constants.P%>');
			PItem.className="linkChange";
			var link = "<%=util.getLink("Participant")%>"+ selectedNode;
			PItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";*/
			
			var cpItem = document.getElementById('<%=Constants.CP%>');
			cpItem.className="linkChange";
			var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode;
			cpItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
		
			dpItem = document.getElementById('<%=Constants.DP%>');
			dpItem.className="linkChange";
			//var link = "<%=util.getLink("DistributionProtocol")%>"+ selectedNode;
			dpItem.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp;<a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
			
			var editItem = document.getElementById('<%=Constants.EDIT%>');
			var editLink = "<%=util.getLink("Participant")%>"+ selectedNode +"&itemId="+itemId;
			var s = "<a HREF='" + editLink + "' target='searchPageFrame'>Edit</a>"
			editItem.innerHTML = "" + s;
			
			var deleteItem = document.getElementById('<%=Constants.DELETE%>');
			var deleteLink = "AdvanceSearch.do?delete=true&itemId="+itemId;
			deleteItem.innerHTML = "&nbsp;<a HREF='" + deleteLink + "'>Delete</a>";
			
		}
		else if(cprotocolCount == sum)
		{
		
			/*var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode; 
			var CPItem = document.getElementById('<%=Constants.CP%>');
			CPItem.className="linkChange";
			CPItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";*/
			
			var scGItem = document.getElementById('<%=Constants.SCG%>');
			scGItem.className="linkChange";
			var link = "<%=util.getLink("SpecimenCollectionGroup")%>"+ selectedNode;
			scGItem.innerHTML ="&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='SpecimenCollectionGroup' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
			
			var editItem = document.getElementById('<%=Constants.EDIT%>');
			var editLink = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode +"&itemId="+itemId;
			var s = "<a HREF='" + editLink + "' target='searchPageFrame'>Edit</a>"
			editItem.innerHTML = "" + s;
			
			var deleteItem = document.getElementById('<%=Constants.DELETE%>');
			var deleteLink = "AdvanceSearch.do?delete=true&itemId="+itemId;
			deleteItem.innerHTML = "&nbsp;<a HREF='" + deleteLink + "'>Delete</a>";
			
		}
		else if(scGroupCount == sum)
		{
			var sItem = document.getElementById('<%=Constants.S%>');
			sItem.className="linkChange";
			var link = "<%=util.getLink("Specimen")%>"+ selectedNode;
			sItem.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
			
			var editItem = document.getElementById('<%=Constants.EDIT%>');
			var editLink = "<%=util.getLink("SpecimenCollectionGroup")%>"+ selectedNode +"&itemId="+itemId;
			var s = "<a HREF='" + editLink + "' target='searchPageFrame'>Edit</a>"
			editItem.innerHTML = "" + s;
			
			var deleteItem = document.getElementById('<%=Constants.DELETE%>');
			var deleteLink = "AdvanceSearch.do?delete=true&itemId="+itemId;
			deleteItem.innerHTML = "&nbsp;<a HREF='" + deleteLink + "'>Delete</a>";
			
		}
		else
			disableAll();
			
		if(sum == 0)
		{
			disableAll();
			var pItem = document.getElementById('<%=Constants.P%>');
			pItem.className="linkChange";
			var link = "<%=util.getLink("Participant")%>"+ selectedNode;
			pItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		}	
	}	
	
	function changeClass(element,styleName)
	{
		element.className = styleName;
	}
	</script>
</head>
<html>
<body>
	
	<table cellpadding='0' cellspacing='0' border='0' width='600'>
		<tr>
			<td class='formTitle' height='20' width='72%'>
				<img src="images/arrow.GIF" alt="Rules" />Rules
			</td>
			
			
			<td class='formTitle'  height='20' width='8%' onmouseover="expand(this);" onmouseout="collapse(this);" align='centre'>Add 
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
												<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'>
												<%=Constants.PARTICIPANT%>
											</a>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='CP' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
												&nbsp;<img src="images/CollectionProtocol.GIF" alt="CollectionProtocol" />  &nbsp;	<%=Constants.COLLECTION_PROTOCOL%>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='S' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/Specimen.GIF" alt="Specimen" /> &nbsp; <%=Constants.SPECIMEN%>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
										<td colspan=2 class='linkChange' id='SCG' noWrap  height='20' vAlign="middle"
										onmouseover="changeMenuStyle(this,'linkChangeOnMouseOver')"
										 onmouseout="changeMenuStyle(this,'linkChange')">
											&nbsp;<img src="images/SpecimenCollectionGroup.GIF" alt="Specimen Collection Group" /> &nbsp; <%=Constants.SPECIMEN_COLLECTION_GROUP%>
										</td>
									</tr>
									<tr height='20' vAlign="middle">
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
										&nbsp;<img src="images/DistributionProtocol.GIF" alt="DistributionProtocol" /> &nbsp; <%=Constants.DISTRIBUTION_PROTOCOL%>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
			</table>
			</td>
		<td class='formTitle' id='edit' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');">Edit</td>
		<td class='formTitle' id='delete' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');">Delete</td>
	</tr>

	
		<div class="tree">
			<script type="text/javascript">
				createTree(tree);
			</script>
		</div>
	</table>	
	</body>
</html>

