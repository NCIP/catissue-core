<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject"%>

<script>
function setParentWindowValue(elementName,elementValue)
{ 
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
	int [][]fullStatus = (int [][])request.getAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS);
	int [][] childContainerSystemIdentifiers = (int [][])request.getAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String storageContainerType = null;
	Integer startNumber = null;
	Long positionOne = (Long)request.getAttribute(Constants.POS_ONE);
	Long positionTwo = (Long)request.getAttribute(Constants.POS_TWO);	
	if (pageOf.equals(Constants.PAGEOF_STORAGE_LOCATION))
	{
		storageContainerType = (String)request.getAttribute(Constants.STORAGE_CONTAINER_TYPE);
		startNumber = (Integer)request.getAttribute(Constants.START_NUMBER);
	}
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
					String styleClassName = "dataCellText"; // Default cell boundary
					if(((null != positionOne) && (null != positionTwo) &&
          				(-1 != positionOne.intValue()) && (-1 != positionTwo.intValue())) && // means nothing entered by the user
					   (i == positionOne.intValue()) && (j == positionTwo.intValue())) // means values entered by user
					{
						styleClassName = "dataSelectedCellText"; // Show the selected cell
						                   // with red boundary
					}
						if (fullStatus[i][j] != 0)
						{
							String openStorageContainer = null;
							if (pageOf.equals(Constants.PAGEOF_SPECIMEN))
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerSystemIdentifiers[i][j]
                    			+ "&" + Constants.PAGEOF + "=" + pageOf;
							}
							else
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerSystemIdentifiers[i][j]
                    			+ "&" + Constants.STORAGE_CONTAINER_TYPE + "=" + storageContainerType
								+ "&" + Constants.PAGEOF + "=" + pageOf;
							}
							if (fullStatus[i][j] == 1)
							{%>
							<td class="<%=styleClassName%>">
								<a href="<%=openStorageContainer%>">
									<img src="images/redbox.gif" width="40" height="40" border="0">
								</a>
 							</td>
					   	  <%}
							else{%>
							<td class="<%=styleClassName%>">
								<img src="images/specimen.jpg" width="40" height="40" border="0">
 							</td>
							<%}%>
						<%}
						else
						{ 
							String setParentWindowContainer = null;
							if (pageOf.equals(Constants.PAGEOF_SPECIMEN))
							{
								setParentWindowContainer = "javascript:setParentWindowValue('positionDimensionOne','"+
															  + i + "');"+"javascript:setParentWindowValue('positionDimensionTwo','"+
															  + j + "');"+"javascript:setParentWindowValue('storageContainer','"+
															  + storageContainerGridObject.getSystemIdentifier() + "');"+
																"javascript:setParentWindowValue('positionInStorageContainer','"
															  + storageContainerGridObject.getType() + " : " 
															  + storageContainerGridObject.getSystemIdentifier()
															  + " Pos (" + i + "," + j + ")');"+"closeFramedWindow()";
							}
							else
							{
								setParentWindowContainer = "javascript:setParentWindowValue('positionInParentContainer','"
															  + storageContainerGridObject.getType() + " : " 
															  + storageContainerGridObject.getSystemIdentifier()
															  + " Pos (" + i + "," + j + ")');"
															  + "javascript:setParentWindowValue('positionDimensionOne','"+
															  + i + "');"+"javascript:setParentWindowValue('positionDimensionTwo','"+
															  + j + "');"
															  + "javascript:setParentWindowValue('parentContainerId','"+
															  + storageContainerGridObject.getSystemIdentifier() + "');"
															  + "javascript:setParentWindowValue('startNumber','"+
															  + startNumber.intValue() + "');closeFramedWindow()";
							}
						%>
						<td class="<%=styleClassName%>">
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