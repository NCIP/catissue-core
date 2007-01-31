<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.tree.SpecimenTreeNode"%>

<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<html>
<%
	Vector dataList = (Vector) request.getAttribute(Constants.TREE_DATA_LIST);
%>
<head>
<script>
	function displayTree()
	{		
		document.getElementById("treebox").innerHTML ="";
		tree=new dhtmlXTreeObject("treebox","100%","100%",0);
		tree.setImagePath("dhtml_comp/imgs/");
		//tree.enableCheckBoxes(true);		
		<% 
        for (Iterator iter = dataList.iterator(); iter.hasNext();) 
        {
					if(dataList != null && dataList.size() != 0)
					{									
							SpecimenTreeNode data = (SpecimenTreeNode) iter.next();
							String parentId = "0";
							String img = "Specimen.GIF";
							//If the node is not a parent node
							if(!data.getParentIdentifier().equals("0"))
							{
								parentId = data.getParentValue() + "_"+ data.getParentIdentifier().toString();		
								img = "Distribution.GIF";
							}
							String nodeId = data.getValue() + "_"+ data.getIdentifier().toString();									
		%>
							tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getValue()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
							tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
		<%	
					}//End If 
		   }//End for
	 	 %>	
	}//End Function
</script>
</head>

<body onload="displayTree();">
	<table>
		<tr><td>
			<div id="treebox" style="width:400; height:250; overflow:auto;"/>
		</td></tr>
	</table>
</body>

</html>