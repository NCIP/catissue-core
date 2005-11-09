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
		Vector Treelist = (Vector)request.getAttribute("vector");
	%>
	var Tree = [<%int k;%><%for (k=0;k < (Treelist.size()-1);k++){%>"<%=Treelist.get(k)%>",<%}%>"<%=Treelist.get(k)%>"];
	//alert('tree'+Tree);
	//window.onload=TreeContent(Treelist);
	<% SearchUtil util = new SearchUtil(); %>
	/*function TreeContent(list){
		alert('im in treecontent');
		
		var Tree = new Array;
		for(var i = 0; i < list.size(); i++){
        	Tree[i] = list.get(i);
        }
       createTree(Tree); 
	}*/
	
	var itemCount = 1;
	
	
	/*function CheckNum(checkName,itemName,nodeCount){
	
		
		alert("node count"+nodeCount);
		if(document.getElementById(checkName).checked==true){
			//DisableAll();
			if(itemName == '<%=Constants.PARTICIPANT%>'){
				//itemCount++;
				//DisableAll();
			var item = document.getElementById('<%=Constants.P%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
			EnableItem(nodeCount);
			}
			if(itemName == '<%=Constants.COLLECTION_PROTOCOL%>'){
				//itemCount++;
				//DisableAll();
				EnableItem(nodeCount);
			var item = document.getElementById('<%=Constants.CP%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("CollectionProtocol")%>' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
			
			}
			if(itemName == '<%=Constants.DISTRIBUTION_PROTOCOL%>'){
				itemCount++;
			}
			if((itemName == '<%=Constants.SPECIMEN_COLLECTION_GROUP%>') || (itemName == '<%=Constants.DISTRIBUTION%>') ){
				itemCount++;
			}
			if(itemName == '<%=Constants.SPECIMEN%>'){
				//itemCount++;
				//DisableAll();
				EnableItem(nodeCount);
			var item = document.getElementById('<%=Constants.S%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("Specimen")%>' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
			
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
			var item = document.getElementById('<%=Constants.P%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		}
		//Poornima:Added CP link and S link
		if(itemCount == 2){
			DisableAll();
			var item = document.getElementById('<%=Constants.CP%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("CollectionProtocol")%>' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
		}
			
		if(itemCount == 3){
			DisableAll();
			var item = document.getElementById('<%=Constants.S%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("Specimen")%>' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
		}
		else
			EnableItem(nodeCount);
		//}
	}*/	

	function DisableAll(){
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
	function EnableAll(){
	
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
		var count1 = 0;
		var count2 = 0;
		var count3 = 0;
		var count4 = 0;
		var count5 = 0;
		var count6 = 0;
		//var j=0;
		//var selectedNode = new Array(nodeCount);
		var selectedNode=0;
							
		/*alert("node count"+nodeCount);
		alert("itemCount"+itemCount);
		alert("check box name"+checkName);
		alert("item name"+itemName);*/
								
		for(var i = 1; i <= nodeCount; i++)
		{
			var item1 = '<%=Constants.PARTICIPANT%>'+"_"+i;
			//count1 = checkStatus(item1, count1,selectedNode,i);
			if(document.getElementById(item1) != null)
			{
				if(document.getElementById(item1).checked==true)
				{
					count1 = count1 + 1;
					selectedNode=i;
				}
			}
			
			var item2 = '<%=Constants.COLLECTION_PROTOCOL%>'+"_"+i;
			//alert("item2 in loop"+item2);
			//count2 = checkStatus(item2, count2,selectedNode,i);
			if(document.getElementById(item2) != null)
			{
				if(document.getElementById(item2).checked==true)
				{
					count2 = count2 + 1;
					selectedNode=i;
				}
			}
			
			var item3 = '<%=Constants.DISTRIBUTION_PROTOCOL%>'+"_"+i;
			//count3 = checkStatus(item3, count3,selectedNode);
			if(document.getElementById(item3) != null)
			{
				if(document.getElementById(item3).checked==true)
				{
					count3 = count3 + 1;
					selectedNode=i;
				}
			}
			
			var item4 = '<%=Constants.SPECIMEN_COLLECTION_GROUP%>'+"_"+i;
			//count4 = checkStatus(item4, count4,selectedNode,i);
			if(document.getElementById(item4) != null)
			{
				if(document.getElementById(item4).checked==true)
				{
					count4 = count4 + 1;
					selectedNode=i;
				}
			}
			
			var item5 = '<%=Constants.DISTRIBUTION%>'+"_"+i;
			//count5 = checkStatus(item5, count5,selectedNode,i);
			if(document.getElementById(item5) != null)
			{
				if(document.getElementById(item5).checked==true)
				{
					count5 = count5 + 1;
					selectedNode=i;
				}
			}
			var item6 = '<%=Constants.SPECIMEN%>'+"_"+i;
			//count6 = checkStatus(item6, count6,selectedNode,i);
			if(document.getElementById(item6) != null)
			{
				if(document.getElementById(item6).checked==true)
				{
					count6 = count6 + 1;
					selectedNode=i;
				}
			}
		}
		
		var sum = count1 + count2 + count3 + count4 + count5 + count6;
		
		//alert("count1 "+count1+" count2 "+count2 );
		//alert("sum "+sum);
		
		if(count1 == sum) 
		{
			var item = document.getElementById('<%=Constants.P%>');
			item.className="linkChange";
			var link = "<%=util.getLink("Participant")%>"+ selectedNode;
			item.innerHTML ="&nbsp;<img src='images/Participant.GIF' alt='Participant' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
			
			//alert("participant rule");
			var item = document.getElementById('<%=Constants.CP%>');
			item.className="linkChange";
			var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode;
			item.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
		
			item = document.getElementById('<%=Constants.DP%>');
			item.className="linkChange";
			//var link = "<%=util.getLink("DistributionProtocol")%>"+ selectedNode;
			item.innerHTML = "&nbsp;<img src='images/DistributionProtocol.GIF' alt='DistributionProtocol' /> &nbsp;<a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
		}
		else if(count2 == sum)
		{
			//alert("clicked coll prot");
			
			var item = document.getElementById('<%=Constants.CP%>');
			item.className="linkChange";
			var link = "<%=util.getLink("CollectionProtocol")%>"+ selectedNode;
			item.innerHTML ="&nbsp;<img src='images/CollectionProtocol.GIF' alt='CollectionProtocol' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.COLLECTION_PROTOCOL%></a>";
			
			var item = document.getElementById('<%=Constants.SCG%>');
			item.className="linkChange";
			var link = "<%=util.getLink("SpecimenCollectionGroup")%>"+ selectedNode;
			item.innerHTML ="&nbsp;<img src='images/SpecimenCollectionGroup.GIF' alt='SpecimenCollectionGroup' />  &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
			
			
		}
		else if(count4 == sum)
		{
			//alert("clicked coll prot");
			var item = document.getElementById('<%=Constants.S%>');
			item.className="linkChange";
			var link = "<%=util.getLink("Specimen")%>"+ selectedNode;
			//alert(link);
			item.innerHTML ="&nbsp;<img src='images/Specimen.GIF' alt='Specimen' /> &nbsp;<a HREF='"+link+"' target='searchPageFrame'><%=Constants.SPECIMEN%></a>";
			
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
		<td class='formTitle' height='20' width='10%' onmouseover="changeClass(this,'menuHover');" onmouseout="changeClass(this,'formTitle');">Delete</td>
	</tr>

	
		<div class="tree">
			<script type="text/javascript">
				createTree(Tree);
			</script>
		</div>
	
	</table>	
	</body>
</html>

