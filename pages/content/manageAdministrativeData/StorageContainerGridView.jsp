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
    String [][] childContainerType = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_TYPE);
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
	
	String oneDimLabel = (String)request.getAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL);
	String twoDimLabel = (String)request.getAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL);
    
    String temp ="";
    String verStrTemp ="";
    String verTempOne ="";
    String verTempTwo ="";
    String filler = " ";
    for( int j=0; j<oneDimLabel.length();j++)
    {
        temp =oneDimLabel.substring(j,j+1);
        verStrTemp=temp.concat(filler); 
        verTempOne=verTempOne.concat(verStrTemp);
        
    } 
    temp ="";
    verStrTemp ="";
    
    for( int j=0; j<twoDimLabel.length();j++)
    {
        temp =twoDimLabel.substring(j,j+1);
        verStrTemp=temp.concat(filler); 
        verTempTwo=verTempTwo.concat(verStrTemp);
        
    } 
    
    
    
    
	int rowSpanValue = storageContainerGridObject.getOneDimensionCapacity().intValue();
	int colSpanValue = storageContainerGridObject.getTwoDimensionCapacity().intValue();
	System.out.println("rowSpanValue : "+ rowSpanValue + " || colSpanValue : " + colSpanValue);  
%>

<html:errors/>

</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<tr>
		<td>
		<table summary="" cellpadding="0" cellspacing="0" border="0" class="borderForMap">
				<tr>
					<td>
					<table summary="Enter summary of data here" cellpadding="3" 
							cellspacing="0" border="0" class="dataTableMap" width="100%">
						  <tr>
						    <td colspan="2" rowspan="2" width="6">&nbsp;</td>
						    <td colspan="<%=colSpanValue%>" align="left"><b> <%=verTempTwo%>&rarr;</b></td>
						  </tr>
							
						<tr class="dataRowLight">	
					<% for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=storageContainerGridObject.getTwoDimensionCapacity().intValue();i++){
                        if(storageContainerGridObject.getTwoDimensionCapacity().intValue() == 1)
                        {
                            %>
    						<td align="center" class="numberMapCol" width="45"><%=i%></td>
    						<td width="*">&nbsp;</td>
    						
    					    <%
                        }
                        else
                        {
					%>
						<td align="center" class="numberMapCol"><%=i%></td>
					<%}}%>				
						</tr>	
					<% for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=storageContainerGridObject.getOneDimensionCapacity().intValue()+1;i++){%>
						<tr class="dataRowLight">
						<%
	                        
							if(i == 1)
							{
						%>
						   <td rowspan="<%=rowSpanValue+1%>" valign ="top" width="6"><b> <%=verTempOne%>&darr;</b></td>
						   
						<%
							}
                        	if(i!=storageContainerGridObject.getOneDimensionCapacity().intValue()+1) 
                            {
						%>
							<td class="numberMapRow" width="3" align="right"><%=i%></td>
					   <% 
                            }
					   
					for (int j = Constants.STORAGE_CONTAINER_FIRST_COLUMN;j<=storageContainerGridObject.getTwoDimensionCapacity().intValue()+1;j++)
                    {
					String styleClassName = "dataCellText"; // Default cell boundary
                    
                    if(i == storageContainerGridObject.getOneDimensionCapacity().intValue()+1)
                    {
                        if(j ==1)
                        {
                        %>   
                            <td width="6" height="40">&nbsp;</td>
       					<%
                            
                        }
                        else
                        {
                         %>   
                            <td width="*" height="40">&nbsp;</td>
       					<%  
                        }
                    
                        
                    }
                    else 
                    {
                        if(j ==storageContainerGridObject.getTwoDimensionCapacity().intValue()+1)
                        {
                            %>   
                            <td width="25" height="40">&nbsp;</td>
       						<% 
                        }
                        else
                        {
                       
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
							<td class="mapTdred" noWrap="true">
								<a href="<%=openStorageContainer%>"> 
								 <%=childContainerType[i][j]%> : <%=childContainerSystemIdentifiers[i][j]%>
								</a>
 							</td>
					   	  <%}
							else{%>
							<td class="mapTdspe" noWrap="true">
							<%=childContainerType[i][j]%> : <%=childContainerSystemIdentifiers[i][j]%>
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
						<td class="mapTdred" noWrap="true">
						 	<a href="<%=setParentWindowContainer%>">
						 		<%=Constants.UNUSED%>
							</a>
						</td>
					  <%}%>
					<%}}}%>
					  </tr>
					<%}%>
					
					</table>
					</td>
				</tr>
		</table>
		</td>
	</tr>
</table>