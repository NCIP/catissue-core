<%@ page import="java.util.*"%>

<head>

	<link rel="StyleSheet" href="tree.css" type="css/tree.css">
	<script type="text/javascript" src="jss/tree.js"></script>
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
<div class="tree">
<script type="text/javascript">
<!--
	createTree(Tree);
//-->
</script>
</div>

<br /><br />

<input type = button value="Delete">


</body>
</html>

