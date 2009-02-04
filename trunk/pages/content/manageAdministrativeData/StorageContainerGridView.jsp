<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject"%>
<%@ page import="java.util.*"%>

<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<%
	String siteName = (String) request.getAttribute("siteName");   
%>
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

function setCustomListBoxValue(elementId,elementValue)
{
	var id = parent.opener.document.getElementById(elementId);
	
	if(elementId.match("_2"))
	{
		id.value = elementValue;
	}
	else
	{
		id.value = elementValue;
		parent.opener.onCustomListBoxChange(id);
	}
}

function setContainerName()
{
	var siteName = "<%=siteName%>";
	parent.opener.setContainerName(siteName);	
}
function setTextBoxValue(elementId,elementValue)
{
	var id = parent.opener.document.getElementById(elementId);	
	id.value = elementValue;
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
	int [][] childContainerIds = (int [][])request.getAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS);
    String [][] childContainerType = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_TYPE);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	
	String enablePageStr = (String)request.getAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE);
	boolean enablePage = false;
	if (enablePageStr!=null && enablePageStr.equals(Constants.TRUE))
		enablePage = true;
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
	
	String containerStyle = (String)session.getAttribute(Constants.CONTAINER_STYLE);
	System.out.println(containerStyle);
	String containerStyleId = (String)session.getAttribute(Constants.CONTAINER_STYLEID);
	System.out.println(containerStyleId);
	String xDimStyleId = (String)session.getAttribute(Constants.XDIM_STYLEID);
	String yDimStyleId = (String)session.getAttribute(Constants.YDIM_STYLEID);
	
	String selectedContainerName= (String) session.getAttribute(Constants.SELECTED_CONTAINER_NAME);
    String containerId= (String) session.getAttribute(Constants.CONTAINERID);
    String pos1= (String) session.getAttribute(Constants.POS1);
    String pos2= (String)session.getAttribute(Constants.POS2);
		

	//Mandar: 29Aug06 : For container details
	List collectionProtocolList = (List)request.getAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST );
	List specimenClassList = (List)request.getAttribute(Constants.MAP_SPECIMEN_CLASS_LIST );
	
	String specimenClass = null;
	String collectionGroup = null;
	String specimenMapKey = null;
	String specimenCallBackFunction = null;
	String nodeName = null;
 
   if (pageOf.equals(Constants.PAGEOF_MULTIPLE_SPECIMEN)) {
	   specimenClass = (String) session.getAttribute(Constants.SPECIMEN_CLASS);
	   collectionGroup = (String) session.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP);
	   specimenMapKey = (String) session.getAttribute(Constants.SPECIMEN_ATTRIBUTE_KEY);
	   specimenCallBackFunction =  (String) session.getAttribute(Constants.SPECIMEN_CALL_BACK_FUNCTION);
   }

%>

<html:errors/>

</br>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
<!-- ROW FOR CONTAINER DETAILS -->
<TR>
	<TD>
<%
	//System.out.println("CP No. : " +collectionProtocolList.size());
	//System.out.println("SC No. : " +specimenClassList.size());
	int rowSpan = getRowSpan(collectionProtocolList, 3);
//int rowSpan = (int)((collectionProtocolList.size()%3)== 0 ? collectionProtocolList.size()/3 : (collectionProtocolList.size()/3)+1 );
%>
		<TABLE border=1 class="borderForMap">
			<TR>
				<TD ROWSPAN=<%=rowSpan %> class="formLabelNoBackGround"><b><bean:message key="map.collectionprotocol"/></b></TD>
<%	
			for(int colcnt=0;colcnt<collectionProtocolList.size();colcnt++)
			{
				String data =(String) collectionProtocolList.get(colcnt );
				if(colcnt != 0)
				{
					if((colcnt)%3 == 0)
					{
%>
					</TR>
					<tr>
<%
					}
				}
 %> 				
					<td class="formLabelNoBackGround"><%=data %></td>
<%
			}
			int colSpan = (int)(3-(collectionProtocolList.size()%3));
 %>
				<td colspan="<%=colSpan %>" class="formLabelNoBackGround">&nbsp;</td>
			</tr>
<!-- Specimen class -->
<%
 	//rowSpan =(int)((specimenClassList.size()%3)== 0 ? specimenClassList.size()/3 : (specimenClassList.size()/3)+1 );
	rowSpan = getRowSpan(specimenClassList, 3);
%>

			<TR>
				<TD ROWSPAN=<%=rowSpan %> class="formLabelNoBackGround"><b><bean:message key="map.specimenclass"/></b></TD>
<%	
			for(int colcnt=0;colcnt<specimenClassList.size();colcnt++)
			{
				String data =(String) specimenClassList.get(colcnt );
				if(colcnt != 0)
				{
					if((colcnt)%3 == 0)
					{
%>
					</TR>
					<tr>
<%
					}
				}
 %> 				
					<td class="formLabelNoBackGround"><%=data %></td>
<%
			}
			colSpan = (int)(3-(specimenClassList.size()%3));
 %>
				<td colspan="<%=colSpan %>" class="formLabelNoBackGround">&nbsp;</td>
			</tr>

		</TABLE>
	</TD>
</TR>
<!-- CONTAINER DETAILS END -->

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
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerIds[i][j]
                    			+ "&" + Constants.PAGEOF + "=" + pageOf;
							}
							else
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerIds[i][j]
                    			+ "&" + Constants.STORAGE_CONTAINER_TYPE + "=" + storageContainerType
								+ "&" + Constants.PAGEOF + "=" + pageOf;
							}
							if (fullStatus[i][j] == 1)
							{%>
							<td class="mapTdred" noWrap="true">
								<a href="<%=openStorageContainer%>"> 
								 <%=childContainerType[i][j]%><!-- : <%=childContainerIds[i][j]%> -->
								</a>
 							</td>
					   	  <%}
							else{%>
							<td class="mapTdspe" noWrap="true">
							<%=childContainerType[i][j]%><!--: <%=childContainerIds[i][j]%> -->
							</td>
							<%}%>
						<%}
						else
						{
							String setParentWindowContainer = null;
							if (pageOf.equals(Constants.PAGEOF_MULTIPLE_SPECIMEN)) 
							{
								setParentWindowContainer = "javascript:" + specimenCallBackFunction + "('" + specimenMapKey + "' , '" + storageContainerGridObject.getId() + "' , '" + storageContainerGridObject.getName() + 
								"' , '" + i + "' , '" + j + "' );closeFramedWindow()" ;
							} 							
							else if (pageOf.equals(Constants.PAGEOF_SPECIMEN))
							{
								setParentWindowContainer = "javascript:setTextBoxValue('" + containerId + "','"+  storageContainerGridObject.getId() + "');" + 
															"javascript:setTextBoxValue('" + selectedContainerName + "','"+  storageContainerGridObject.getName() + "');" + 
															"javascript:setTextBoxValue('" + pos1 + "','"+  i + "');" + 
															"javascript:setTextBoxValue('" + pos2 + "','"+  j + "');" +
															"javascript:setParentWindowValue('positionInStorageContainer','"+ storageContainerGridObject.getType() + " : " + storageContainerGridObject.getId() + " Pos (" + i + "," + j + ")');" ;								
								String storageContainer= (String) session.getAttribute("storageContainer");								
								
								if(storageContainer != null)
								{
									setParentWindowContainer = setParentWindowContainer + "javascript:setContainerName();";
								}
															
								setParentWindowContainer = setParentWindowContainer + "javascript:closeFramedWindow()";
							}	
							else if (pageOf.equals(Constants.PAGEOF_ALIQUOT))
							{
								setParentWindowContainer = "javascript:setTextBoxValue('" + containerStyleId + "','"
								+ storageContainerGridObject.getId() + "');"+"javascript:setTextBoxValue('" + containerStyle + "','"
								+ storageContainerGridObject.getName() + "');"
								+"javascript:setTextBoxValue('" + xDimStyleId + "','"
								+ i + "');"
								+ "javascript:setTextBoxValue('" + yDimStyleId + "','"
								+ j + "');closeFramedWindow()";
							}							
							else
							{
								setParentWindowContainer = "javascript:setParentWindowValue('positionInParentContainer','"
															+ storageContainerGridObject.getType() + " : " 
															+ storageContainerGridObject.getId()
															+ " Pos (" + i + "," + j + ")');"
															+ "javascript:setTextBoxValue('" + containerStyleId + "','"
															+ storageContainerGridObject.getId() + "');"+"javascript:setTextBoxValue('" + xDimStyleId + "','"
															+ i + "');"
															+ "javascript:setTextBoxValue('" + yDimStyleId + "','"
															+ j + "');"
															+ "javascript:setParentWindowValue('startNumber','"
															+ startNumber.intValue() + "');closeFramedWindow()";
							}
						%>
						<td class="mapTdred" noWrap="true">
							<%if (enablePage){%>
						 	<a href="<%=setParentWindowContainer%>">
						 		<%=Constants.UNUSED%>
							</a>
							<%}else{%>
								<%=Constants.UNUSED%>
							<%}%>
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
<%!
// method to return the rowspan value for the cell.
private int getRowSpan(List dataList, int columnNumber)
{
int rowSpan = 0;
rowSpan = (int)((dataList.size()%columnNumber)== 0 ? dataList.size()/columnNumber : (dataList.size()/columnNumber)+1 );
return rowSpan;
}


%>