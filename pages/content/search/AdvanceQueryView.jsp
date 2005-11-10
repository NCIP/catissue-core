<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.SearchUtil"%>

<head>

	<link rel="StyleSheet" href="tree.css" type="css/tree.css">
	<script type="text/javascript" src="jss/tree.js"></script>
	<LINK REL="StyleSheet" HREF="css/menu.css">
	<script language="javascript" src="jss/menu.js"></script>
	<script type="text/javascript">
	<%
		Vector Treelist = (Vector)request.getAttribute(Constants.TREE_VECTOR);
	%>
	var Tree = [<%int k;%><%for (k=0;k < (Treelist.size()-1);k++){%>"<%=Treelist.get(k)%>",<%}%>"<%=Treelist.get(k)%>"];
	<% SearchUtil util = new SearchUtil(); %>
		
	/*function CheckNum(checkName,itemName,nodeCount){
	
		
		alert("node count"+nodeCount);
		if(document.getElementById(checkName).checked==true){
			//DisableAll();
			if(itemName == '<%//=Constants.PARTICIPANT%>'){
				//itemCount++;
				//DisableAll();
			var item = document.getElementById('<%//=Constants.P%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("Participant")%>' target='searchPageFrame'><%//=Constants.PARTICIPANT%></a>";
			EnableItem(nodeCount);
			}
			if(itemName == '<%//=Constants.COLLECTION_PROTOCOL%>'){
				//itemCount++;
				//DisableAll();
				EnableItem(nodeCount);
			var item = document.getElementById('<%//=Constants.CP%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("CollectionProtocol")%>' target='searchPageFrame'><%//=Constants.COLLECTION_PROTOCOL%></a>";
			
			}
			if(itemName == '<%//=Constants.DISTRIBUTION_PROTOCOL%>'){
				itemCount++;
			}
			if((itemName == '<%//=Constants.SPECIMEN_COLLECTION_GROUP%>') || (itemName == '<%//=Constants.DISTRIBUTION%>') ){
				itemCount++;
			}
			if(itemName == '<%//=Constants.SPECIMEN%>'){
				//itemCount++;
				//DisableAll();
				EnableItem(nodeCount);
			var item = document.getElementById('<%//=Constants.S%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("Specimen")%>' target='searchPageFrame'><%//=Constants.SPECIMEN%></a>";
			
			}
		}
		else
			itemCount--;
			
	//if(itemCount > 2){
		//DisableAll();
	//}
	//else{
	alert("itemCount"+itemCount);
	alert("check box name"+checkName);
	alert("item name"+itemName);
		if(itemCount == 1){
			DisableAll();
			var item = document.getElementById('<%//=Constants.P%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("Participant")%>' target='searchPageFrame'><%//=Constants.PARTICIPANT%></a>";
		}
		//Poornima:Added CP link and S link
		if(itemCount == 2){
			DisableAll();
			var item = document.getElementById('<%//=Constants.CP%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("CollectionProtocol")%>' target='searchPageFrame'><%//=Constants.COLLECTION_PROTOCOL%></a>";
		}
			
		if(itemCount == 3){
			DisableAll();
			var item = document.getElementById('<%//=Constants.S%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%//=util.getLink("Specimen")%>' target='searchPageFrame'><%//=Constants.SPECIMEN%></a>";
		}
		else
			EnableItem(nodeCount);
		//}
	}*/	

	function DisableAll()
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
		
	}
	function EnableAll()
	{
	
		var item = document.getElementById('<%=Constants.CP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='<%=util.getLink("CollectionProtocol")%>' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
		
		item = document.getElementById('<%=Constants.P%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Participant.GIF' alt='Participant' /> &nbsp;<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		
		item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp; <a HREF='<%=util.getLink("Specimen")%>' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='Specimen Collection Group' /> &nbsp;<a HREF='<%=util.getLink("SpecimenCollectionGroup")%>' target='searchPageFrame'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
		
		item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/Distribution.GIF' alt='Distribution' /> &nbsp; <a HREF='#'><%=Constants.DISTRIBUTION%></a>";
		
		item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp; <a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
	}
	
	/*function checkStatus(item, counter,selectedNode,i)
	{
		if(document.getElementById(item) != null)
		{
			if(document.getElementById(item).checked==true)
			{
				counter = counter + 1;
				selectedNode=i;
			}
		}
		return counter; 
	}*/
	
//	function EnableItem(nodeCount){
	function CheckNum(checkName,itemName,nodeCount)
	{
		DisableAll();
		var ParticipantCount = 0;
		var CProtocolCount = 0;
		var DProtocolCount = 0;
		var SCGroupCount = 0;
		var DistributionCount = 0;
		var SpecimenCount = 0;
		//var j=0;
		//var selectedNode = new Array(nodeCount);
		var selectedNode=0;
		//alert("inside checknum");					
		/*alert("node count"+nodeCount);
		alert("itemCount"+itemCount);
		alert("check box name"+checkName);
		alert("item name"+itemName);*/
								
		for(var i = 1; i <= nodeCount; i++)
		{
			var ParticipantItem = '<%=Constants.PARTICIPANT%>'+"_"+i;
			//count1 = checkStatus(item1, count1,selectedNode,i);
			if(document.getElementById(ParticipantItem) != null)
			{
				if(document.getElementById(ParticipantItem).checked==true)
				{
					ParticipantCount = ParticipantCount + 1;
					selectedNode=i;
				}
			}
			
			var CProtocolItem = '<%=Constants.COLLECTION_PROTOCOL%>'+"_"+i;
			//alert("item2 in loop"+item2);
			//count2 = checkStatus(item2, count2,selectedNode,i);
			if(document.getElementById(CProtocolItem) != null)
			{
				if(document.getElementById(CProtocolItem).checked==true)
				{
					CProtocolCount = CProtocolCount + 1;
					selectedNode=i;
				}
			}
			
			var DProtocolItem = '<%=Constants.DISTRIBUTION_PROTOCOL%>'+"_"+i;
			//DProtocolCount = checkStatus(item3, count3,selectedNode);
			if(document.getElementById(DProtocolItem) != null)
			{
				if(document.getElementById(DProtocolItem).checked==true)
				{
					DProtocolCount = DProtocolCount + 1;
					selectedNode=i;
				}
			}
			
			var SCGroupItem = '<%=Constants.SPECIMEN_COLLECTION_GROUP%>'+"_"+i;
			//count4 = checkStatus(item4, count4,selectedNode,i);
			if(document.getElementById(SCGroupItem) != null)
			{
				if(document.getElementById(SCGroupItem).checked==true)
				{
					SCGroupCount = SCGroupCount + 1;
					selectedNode=i;
				}
			}
			
			var DistributionItem = '<%=Constants.DISTRIBUTION%>'+"_"+i;
			//count5 = checkStatus(item5, count5,selectedNode,i);
			if(document.getElementById(DistributionItem) != null)
			{
				if(document.getElementById(DistributionItem).checked==true)
				{
					DistributionCount = DistributionCount + 1;
					selectedNode=i;
				}
			}
			var SpecimenItem = '<%=Constants.SPECIMEN%>'+"_"+i;
			//count6 = checkStatus(item6, count6,selectedNode,i);
			if(document.getElementById(SpecimenItem) != null)
			{
				if(document.getElementById(SpecimenItem).checked==true)
				{
					SpecimenCount = SpecimenCount + 1;
					selectedNode=i;
				}
			}
		}
		
		var sum = ParticipantCount + CProtocolCount + DProtocolCount + SCGroupCount + DistributionCount + SpecimenCount;
		
		//alert("count1 "+count1+" count2 "+count2 );
		//alert("sum "+sum);
		
		if(ParticipantCount == sum) 
		{
			/*var PItem = document.getElementById('<%=Constants.P%>');
			PItem.className="linkChange";
			var link = "<%=util.getLink("Participant")%>"+ selectedNode;
			PItem.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";*/
			
			//alert("participant rule");
			var CPItem = document.getElementById('<%=Constants.CP%>');
			CPItem.className="linkChange";
			var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode;
			CPItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
		
			DPItem = document.getElementById('<%=Constants.DP%>');
			DPItem.className="linkChange";
			//var link = "<%=util.getLink("DistributionProtocol")%>"+ selectedNode;
			DPItem.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp;<a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
			
			var deleteItem = document.getElementById('<%=Constants.DELETE%>');
			deleteItem.innerHTML = "&nbsp;<a HREF='#'>Delete</a>";
		}
		else if(CProtocolCount == sum)
		{
			//alert("clicked coll prot");
			
			var CPItem = document.getElementById('<%=Constants.CP%>');
			CPItem.className="linkChange";
			var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode;
			CPItem.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
			
			var SCGItem = document.getElementById('<%=Constants.SCG%>');
			SCGItem.className="linkChange";
			var link = "<%=util.getLink("SpecimenCollectionGroup")%>"+ selectedNode;
			SCGItem.innerHTML ="&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='SpecimenCollectionGroup' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
			
			
		}
		else if(SCGroupCount == sum)
		{
			//alert("clicked specimen group");
			var SItem = document.getElementById('<%=Constants.S%>');
			SItem.className="linkChange";
			var link = "<%=util.getLink("Specimen")%>"+ selectedNode;
			//alert(link);
			SItem.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
			
		}		
		else
			DisableAll();
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
		<td class='formTitle' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');">Edit</td>
		<td class='formTitle' id='delete' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');">Delete</td>
	</tr>

	
		<div class="tree">
			<script type="text/javascript">
				createTree(Tree);
			</script>
		</div>
	
	</table>	
	</body>
</html>

