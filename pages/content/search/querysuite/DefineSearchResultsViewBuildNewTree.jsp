
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.tree.QueryTreeNodeData"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
<script src="jss/queryModule.js"></script>
<script language="JavaScript" type="javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="javascript" src="dhtml_comp/jss/dhtmlXTree.js"></script>
<script>


function showTree(selectedCategories, flag)
{
	 var selLength = selectedCategories.length;     
	 var updatedList = new Array();
	 tree.setImagePath("dhtml_comp/imgs/");
	 var i, j=0;				
	 
	 if(flag)
	 {
		 for(i=0; i <selLength; i++)
		 {
			if(selectedCategories.options[i].selected)
			{
				tree.insertNewChild(0,itemID ,selectedCategories.options[i].text,0,"","","","");					
				itemID++;
			 }	
			 else
			 {
			 	var newOpt = new Option(selectedCategories.options[i].text, selectedCategories.options[i].text); 
			 	updatedList[j]=newOpt;			
			 	j++;
	 	
			 }
	
		 }
		 
		for(i=0; i <selLength; i++)
		{
			selectedCategories.options[i]=null; 		
		}

		 for(i=0; i<j; i++)
		 {
			selectedCategories.options[i] = updatedList[i];
			
		 }		 
	
	 }
	 else
	 {
	 	
	 	var selectedItemText= tree.getSelectedItemText().split(",");
	 	var selectedItem = tree.getSelectedItemId().split(",");	
	 	var removeItemLength = selectedItem.length;
	 	
	  	var k=0;
	 	for(k=0; k<removeItemLength ; k++)
	 	{
		 	var newOpt = new Option(selectedItemText[k], selectedItemText[k]); 
		 	selLength = selectedCategories.length;    	
		 	selectedCategories.options[selLength]= newOpt;	 	
		 	tree.deleteItem(selectedItem[k] , false);
	 	
	 	}
	 }	    
	  
}

</script>
<html>
<body>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<table border="0" width="100%" id="table3" height="100%" cellspacing="0" cellpadding="0">
	<tr  height="5%" valign="center" width="100%">
		<td width="25%" height="10%" colspan="2" valign="center"  class="queryModuleTabSelected">
			<bean:message key="query.step1.defineTree"/>
		</td>
		<td colspan="3" valign="center" height="10%" class="queryModuleTab" >
			<bean:message key="query.step2.defineSpreadsheet"/>
		</td>
	</tr>
	<tr valign="top" height="5" >
		<td valign="top" colspan="5" class="defineResultsViewMessage" height="10%" width="100%"><bean:message key="query.defineResultsViewMessage"/></td>
	</tr>
	<!--tr valign="top" height="5" >
		<td height="77%"  valign="top" colspan="5" class="defineResultsViewMessage" height="2%" width="100%"><html:button property="Button"><bean:message key="query.buildNewTree"/></html:button></td>
	</tr-->
	<tr valign="top" height="5%" >										
			
					
						<td valign="top" width="25%" height="76%">
						<%@ include file="/pages/content/search/querysuite/ChooseSearchCategory.jsp" %>
						</td>
						
						<td align="center" valign="middle">
							<html:button property="addEntities" onclick="showTree(document.forms[0].selectCategoryList, true)">							
							<bean:message key="query.addEntities"/>
							</html:button>
						
						 <br/><br/>
						
							<html:button property="removeEntities" onclick="showTree(document.forms[0].selectCategoryList, false)">
							<bean:message key="query.removeEntities"/>
							</html:button>
						</td>
						
						<td valign="top" width="25%" height="76%" >
						<%@ include file="/pages/content/search/querysuite/BuildCustomTree.jsp" %>
						</td>
				
				</tr>
				
		
			</table> 
		</td>
	</tr>


</table>
</body>
</html>