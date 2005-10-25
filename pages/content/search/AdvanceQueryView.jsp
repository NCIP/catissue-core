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
	
	
	function CheckNum(checkName,itemName,nodeCount){
	
		if(document.getElementById(checkName).checked==true){
			DisableAll();
			if(itemName == '<%=Constants.PARTICIPANT%>'){
				itemCount++;
			}
			if(itemName == '<%=Constants.COLLECTION_PROTOCOL%>'){
				itemCount++;
			}
			if(itemName == '<%=Constants.DISTRIBUTION_PROTOCOL%>'){
				itemCount++;
			}
			if((itemName == '<%=Constants.SPECIMEN_COLLECTION_GROUP%>') || (itemName == '<%=Constants.DISTRIBUTION%>') ){
				itemCount++;
			}
			if(itemName == '<%=Constants.SPECIMEN%>')
				itemCount++;
		}
		else
			itemCount--;
			
	//if(itemCount > 2){
		//DisableAll();
	//}
	//else{
		if(itemCount == 1){
			DisableAll();
			var item = document.getElementById('<%=Constants.P%>');
			item.className="linkChange";
			item.innerHTML = "<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		}
		else
			EnableItem(nodeCount);
		//}
	}	

	function DisableAll(){
		var item = document.getElementById('<%=Constants.CP%>');
		item.className="linkChange";
		item.innerHTML = "<%=Constants.COLLECTION_PROTOCOL%>";
		
		item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = '<%=Constants.DISTRIBUTION_PROTOCOL%>';
		
		item = document.getElementById('<%=Constants.P%>');
		item.className="linkChange";
		item.innerHTML = "<%=Constants.PARTICIPANT%>";
		
		item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML = "<%=Constants.SPECIMEN%>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "<%=Constants.SPECIMEN_COLLECTION_GROUP%>";
		
		item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "<%=Constants.DISTRIBUTION%>";
		
		
	}
	
	function EnableAll(){
	
		var item = document.getElementById('<%=Constants.CP%>');
		item.className="linkChange";
		item.innerHTML = "<a HREF='#'><%=Constants.COLLECTION_PROTOCOL%></a>";;
		
		item = document.getElementById('<%=Constants.P%>');
		item.className="linkChange";
		item.innerHTML = "<a HREF='<%=util.getLink("Participant")%>' target='searchPageFrame'><%=Constants.PARTICIPANT%></a>";
		
		item = document.getElementById('<%=Constants.S%>');
		item.className="linkChange"
		item.innerHTML = "<a HREF='#'><%=Constants.SPECIMEN%></a>";
		
		item = document.getElementById('<%=Constants.SCG%>');
		item.className="linkChange";
		item.innerHTML = "<a HREF='#'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
		
		item = document.getElementById('<%=Constants.D%>');
		item.className="linkChange";
		item.innerHTML = "<a HREF='#'><%=Constants.DISTRIBUTION%></a>";
		
		item = document.getElementById('<%=Constants.DP%>');
		item.className="linkChange";
		item.innerHTML = "<a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
	}
	
	function EnableItem(nodeCount){
		var count1 = 0;
		var count2 = 0;
		var count3 = 0;
		var count4 = 0;
		var count5 = 0;
							
		for(var i = 1; i <= nodeCount; i++){
			
			var item1 = '<%=Constants.PARTICIPANT%>'+"_"+i;
			
			if((document.getElementById(item1) != null) && (document.getElementById(item1).checked==true)){
				var item = document.getElementById('<%=Constants.CP%>');
				item.className="linkChange";
				item.innerHTML ="<a HREF='#'><%=Constants.COLLECTION_PROTOCOL%></a>";
			
				item = document.getElementById('<%=Constants.DP%>');
				item.className="linkChange";
				item.innerHTML = "<a HREF='#'><%=Constants.DISTRIBUTION_PROTOCOL%></a>";
				count1++;
				
			}
			
			var item2 = '<%=Constants.COLLECTION_PROTOCOL%>'+"_"+i;
			if((document.getElementById(item2)!= null) && (document.getElementById(item2).checked==true)){
				var item = document.getElementById('<%=Constants.SCG%>');
				item.className="linkChange";
				item.innerHTML ="<a HREF='#'><%=Constants.SPECIMEN_COLLECTION_GROUP%></a>";
				count2++;
				
			}
			
			var item3 = '<%=Constants.DISTRIBUTION_PROTOCOL%>'+"_"+i;
			if((document.getElementById(item3)!= null) && (document.getElementById(item3).checked==true)){
				var item = document.getElementById('<%=Constants.D%>');
				item.className="linkChange";
				item.innerHTML ="<a HREF='#'><%=Constants.DISTRIBUTION%></a>";
				count3++;
			}
			
			var item4 = '<%=Constants.SPECIMEN_COLLECTION_GROUP%>'+"_"+i;
			var item5 = '<%=Constants.DISTRIBUTION%>'+"_"+i;
			if(((document.getElementById(item4)!= null) && (document.getElementById(item4).checked==true)) ||((document.getElementById(item5)!= null) && (document.getElementById(item5).checked==true))){
				var item = document.getElementById('<%=Constants.S%>');
				item.className="linkChange";
				item.innerHTML ="<a HREF='#'><%=Constants.SPECIMEN%></a>";
				count4++;
			}
			
			var item6 = '<%=Constants.SPECIMEN%>'+"_"+i;
			if((document.getElementById(item6)!= null) && (document.getElementById(item6).checked==true)){
				var item = document.getElementById('<%=Constants.S%>');
				item.className="linkChange";
				item.innerHTML ="<a HREF='#'><%=Constants.SPECIMEN%></a>";
				count5++;
			}
			
		}
		var sum = count1 + count2 + count3 + count4 + count5;
		if( (count1 == sum) || (count2 == sum) || (count3 == sum) || (count4 == sum) ||(count5 == sum) )
			return;
		else
			DisableAll();
		
	}	
	</script>
</head>



<html>
<body>
	
	<table cellpadding='0' cellspacing='0' border='0' width='85%'>
		<tr>
			<td class='formTitle' height='20' width='70%'>
				<img src="images/arrow.GIF" alt="Rules" />Rules
			</td>
			<td class='menuNormal'  height='20' width='10%' onmouseover="expand(this);" onmouseout="collapse(this);" align='centre'>Add
				<table class='menuNormal' border='0' cellpadding='0' cellspacing='0'>
					<tr>
						<td>
							<div class='menuNormal' width='85'>
								<table class='menuNormal' width='85' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td>
											<img src="images/Participant.GIF" alt="Participant" /> 
										</td>
										<td class='linkChange' id='P'>
											
											
											<a HREF='<%=util.getLink("Participant")%>' target="searchPageFrame">
												<%=Constants.PARTICIPANT%>
											</a>
											
									</td>
									</tr>
									<tr>
										<td>
											<img src="images/CollectionProtocol.GIF" alt="CollectionProtocol" /> 
										</td>
										<td class='linkChange' id='CP'>	
											<%=Constants.COLLECTION_PROTOCOL%>
											<!-- a HREF='#' class='menuitem'>
												CollectionProtocol 
											</a-->
										</td>
									</tr>
									<tr>
										<td>
											<img src="images/Specimen.GIF" alt="Specimen" /> 
										</td>
										<td class='linkChange' id='S'>
											<%=Constants.SPECIMEN%>
											<!--font size='2'>
											<a HREF='#' class='menuitem'>
												Specimen 
											</a>
											</font--> 
										</td>
									</tr>
									<tr>
									<td>
										<img src="images/SpecimenCollectionGroup.GIF" alt="Specimen Collection Group" /> 
									</td>
										<td class='linkChange' id='SCG'>
											<%=Constants.SPECIMEN_COLLECTION_GROUP%>
											<!--font size='2'>
											<a HREF='#' class='menuitem'>
												SpecimenCollectionGroup 
											</a>
											</font-->
										</td>
									</tr>
									<tr>
									<td>
										<img src="images/Distribution.GIF" alt="Distribution" /> 
									</td>
										<td class='linkChange' id='D'>
											<%=Constants.DISTRIBUTION%>
											<!--font size='2'>
											<a HREF='#' class='menuitem'>
												Distribution 
											</a>
											</font-->
										</td>
									</tr>
									<tr>
									<td>
										<img src="images/DistributionProtocol.GIF" alt="DistributionProtocol" /> 
									</td>
										<td class='linkChange' id='DP'>
											<%=Constants.DISTRIBUTION_PROTOCOL%>
											<!--font size='2'>
											<a HREF='#' class='menuitem'>
												DistributionProtocol 
											</a>
											</font-->
										</td>
									</tr>
									
								</table>
							</div>
						</td>
					</tr>
				
			</table>
		
		<td class='formTitle' height='20' width='10%'>Edit</td>
		<td class='formTitle' height='20' width='10%'>Delete</td>
	</tr>

	
		<div class="tree">
			<script type="text/javascript">
				createTree(Tree);
			</script>
		</div>
	
	</table>	
	</body>
</html>

