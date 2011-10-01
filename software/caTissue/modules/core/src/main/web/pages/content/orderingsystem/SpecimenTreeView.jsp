<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.tree.SpecimenTreeNode"%>

<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmlXTree.js"></script>
<script language="JavaScript" type="text/javascript" src="dhtml_comp/js/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/OrderingSystem.js"></script>

<html>
<%
	Vector dataList = (Vector) request.getAttribute(Constants.TREE_DATA_LIST);
%>
<head>
<script>
 //Global object for dhtmlXTreeObject.
 var tree;
 
 	//Function to obtain the userdata from selected tree node 	
	function obtainUserData()
	{			
		var strUserData = "";		
		strUserData = tree.getSelectedItemId();
		var len = strUserData.length;
		data = strUserData.substring(0,len-2);
		chkDataClassType(data);
	}
	
	//Function to check the specimenclass and type of the selected node to set it in the dropdown.
	function chkDataClassType(data)
	{
		var strNode = "";
		<%
		for (Iterator iter = dataList.iterator(); iter.hasNext();) 
        {	
        %>
	        <%
	        	SpecimenTreeNode treeNode = (SpecimenTreeNode) iter.next();
	        	Long propertyValue = treeNode.getIdentifier();
	        %>
    		strNode = "<%= treeNode.getValue() %>";
        	if(strNode == data)
        	{
        		//Check if userdata is parent node
        		<% String nodeId = treeNode.getValue() + "_"+ treeNode.getIdentifier().toString(); %>
				var parentObj = tree.getParentId("<%= nodeId %>");     		        			
        		if(parentObj != 0)
        		{
					//Obtain from nodes in the vector        		
	        		var nodetype = "<%= treeNode.getType() %>";
	        		var nodeClass = "<%= treeNode.getSpecimenClass() %>";
	
	        		//Obtain from request object
	        		var strNodeType = "<%= request.getAttribute(Constants.SPECIMEN_TYPE) %>"; 
	        		var strNodeClass = "<%= request.getAttribute(Constants.SPECIMEN_CLASS) %>";
					
					//Check If selected node is not a parent node
	        		if(nodetype == strNodeType && nodeClass == strNodeClass)
	        		{       				
	        			var propertyName = "<%= request.getAttribute(Constants.PROPERTY_NAME) %>";
	        			var propertyValue = "<%= propertyValue %>";
	        			setDropDownValue(propertyName,propertyValue,data);
	        		}
	        	}
        	}//End If
        <%        	
        } //End for
		%>
	}
	
	//Set the value in the dropdown
	function setDropDownValue(propertyName,propertyValue,data)
	{				
		for (var i=0;i < opener.document.forms[0].elements.length;i++)
	    {						
	    	if (opener.document.forms[0].elements[i].name == propertyName)
			{										
					for(var j=0;j<opener.document.forms[0].elements[i].options.length;j++)
					{
						if(opener.document.forms[0].elements[i].options[j].value == propertyValue)
						{										
							opener.document.forms[0].elements[i].selectedIndex = j;															
							break;																					
						}
					}
			}			
	    }
	    window.close();
	}

	//Function to display the Specimen Tree
	function displayTree()
	{		
		document.getElementById("treebox").innerHTML ="";

		tree=new dhtmlXTreeObject("treebox","100%","100%",0);
		tree.setImagePath("dhtml_comp/imgs/");		
		//Call function obtainUserData() when any node is double clicked.
		tree.setOnDblClickHandler(obtainUserData);	
		<% 
		//Obtain from request object
		String strNodeType = (String)request.getAttribute(Constants.SPECIMEN_TYPE);
		String strNodeClass = (String)request.getAttribute(Constants.SPECIMEN_CLASS);
       
        for (Iterator iter = dataList.iterator(); iter.hasNext();) 
        {
				if(dataList != null && dataList.size() != 0)
				{									
						SpecimenTreeNode data = (SpecimenTreeNode) iter.next();
						
						String parentId = "0";
						String img = "Specimen.GIF";
						String nodeColor="gray";
						
						//If the node is not a parent node
						if(!data.getParentIdentifier().equals("0"))
						{
							parentId = data.getParentValue() + "_"+ data.getParentIdentifier().toString();		
							img = "Distribution.GIF";
						}
						String nodeId = data.getValue() + "_"+ data.getIdentifier().toString();		
						if(data.getType().equals(strNodeType) && data.getSpecimenClass().equals(strNodeClass) && !data.getParentIdentifier().equals("0"))
						{
							nodeColor="";
						}							
		%>
						tree.insertNewChild("<%=parentId%>","<%=nodeId%>","<%=data.getValue()%>",0,"<%=img%>","<%=img%>","<%=img%>","");
						tree.setUserData("<%=nodeId%>","<%=nodeId%>","<%=data%>");	
						tree.setItemColor("<%=nodeId%>","<%=nodeColor%>","<%=nodeColor%>");
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