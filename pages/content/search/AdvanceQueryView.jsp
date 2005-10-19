<%@ page import="java.util.*"%>

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
	
	/*function TreeContent(list){
		alert('im in treecontent');
		
		var Tree = new Array;
		for(var i = 0; i < list.size(); i++){
        	Tree[i] = list.get(i);
        }
       createTree(Tree); 
	}*/
		
	</script>
</head>



<html>
<body>

	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
		<tr>
			<td class='formTitle' height='20' colspan='4'>Rules</td>
		</tr>

		<tr>
			<td>
				<div class="tree">
					<script type="text/javascript">
						createTree(Tree);
					</script>
				</div>
			</td>
		</tr>
	</table>	
</body>
</html>

