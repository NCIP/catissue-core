<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject,java.lang.String"%>

<script>

function setParentWindowValue(elementName,elementValue) { 
	for (var i=0;i < top.opener.document.forms[0].elements.length;i++)
    {
    	if (top.opener.document.forms[0].elements[i].name == elementName)
		{
			top.opener.document.forms[0].elements[i].value = elementValue;
		}
    }
}

function closeFramedWindow()
{
	top.window.close();
}
</script>

<%
	StorageContainerGridObject storageContainerGridObject 
			= (StorageContainerGridObject)request.getAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT);
	boolean [][]fullStatus = (boolean [][])request.getAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS);
%>

<html:errors/>

</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<tr>
		<td>
		<table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
					<table summary="Enter summary of data here" cellpadding="3" 
							cellspacing="0" border="0" class="dataTable" width="100%">
					<% for (int i=0;i<storageContainerGridObject.getOneDimensionCapacity().intValue();i++){%>
						<tr class="dataRowLight">	
					<% for (int j=0;j<storageContainerGridObject.getTwoDimensionCapacity().intValue();j++){
						if (fullStatus[i][j] == true){
					   %>
						<td class="dataCellText">
							<img src="images/redbox.gif" width="40" height="40" border="0">
 						</td>
						<%}
						else
						{ 
							String setParentWindowContainer = "javascript:setParentWindowValue('parentContainerId','"+
															  + storageContainerGridObject.getSystemIdentifier()
															  + "');closeFramedWindow()";
						%>
						<td class="dataCellText">
						 	<a href="<%=setParentWindowContainer%>">
								<img src="images/bluebox.gif" width="40" height="40" border="0">
							</a>
						</td>
					  <%}%>
					<%}%>
					  </tr>
					<%}%>
					</table>
					</td>
				</tr>
		</table>
		</td>
	</tr>
</table>