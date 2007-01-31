
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.tree.QueryTreeNodeData"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXTree.css">
<script src="jss/queryModule.js"></script>
<script language="JavaScript" type="javascript" src="dhtml_comp/jss/dhtmXTreeCommon.js"></script>
<script language="JavaScript" type="javascript" src="dhtml_comp/jss/dhtmlXTree.js"></script>
<script>


function showTree(selectedCategories)
{
	 var selLength = selectedCategories.length;     
	 tree.setImagePath("dhtml_comp/imgs/");
	 var i;				
	 for(i=0; i <selLength; i++)
	 {
		if(selectedCategories.options[i].selected)
		{
			tree.insertNewChild(0,selectedCategories.options[i].text,selectedCategories.options[i].text,0,"","","","");	
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
						
						<td height="1%"  valign="center" height="2%" width="100%">
							<html:button property="addEntities" onclick="showTree(document.forms[0].selectCategoryList)">							
							<bean:message key="query.addEntities"/>
							</html:button>
						</td>
						
						<td height="1%"  valign="center" height="2%" width="100%">
							<html:button property="removeEntities" onclick="showTree()">
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