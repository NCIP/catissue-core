<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject"%>
<%@ page import="java.util.*"%>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<%
	String siteName = (String) request.getAttribute("siteName");   
%>
<script>

<%
String pageOf = (String)request.getAttribute(Constants.PAGE_OF);
String [][] childContainerName = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_NAME);
%>
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

function setTextBoxValue(elementId,elementValue)
{
	var id = parent.opener.document.getElementById(elementId);	
	id.value = elementValue;
}

function closeFramedWindow()
{
	top.window.close();
}


function refresh_tree(nodeId)
{	
	window.parent.frames["SCTreeView"].location="<%=Constants.STORAGE_CONTAINER_TREE_ACTION%>?<%=Constants.PAGE_OF%>=<%=pageOf%>&<%=Constants.RELOAD%>=true&<%=Constants.TREE_NODE_ID%>="+nodeId;
}

function containerChanged()
{
	window.parent.containerChangedTrue();	
}
function containerInfoTab()
{	 
	  var isContainerChanged=window.parent.isChanged();
	  if(isContainerChanged == 'no')
		{
		   
			window.parent.frames['StorageContainerView'].location="StorageContainer.do?operation=edit&pageOf=pageOfStorageContainer&containerIdentifier=${requestScope.storageContainerIdentifier}";
		}
     else
		{
			
			window.parent.frames['StorageContainerView'].location="SearchObject.do?pageOf=pageOfTreeSC&operation=search&id=${requestScope.storageContainerIdentifier}";
		}
}	
</script>

<%
	StorageContainerGridObject storageContainerGridObject 
			= (StorageContainerGridObject)request.getAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT);
	int [][]fullStatus = (int [][])request.getAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS);
	int [][] childContainerIds = (int [][])request.getAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS);
    String [][] childContainerType = (String [][])request.getAttribute(Constants.CHILD_CONTAINER_TYPE);
	
	
	String enablePageStr = (String)request.getAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE);
	boolean enablePage = false;
	if (enablePageStr!=null && enablePageStr.equals(Constants.TRUE))
		enablePage = true;
	String storageContainerType = null;
	Integer startNumber = null;
	Long positionOne = (Long)request.getAttribute(Constants.POS_ONE);
	Long positionTwo = (Long)request.getAttribute(Constants.POS_TWO);	
	if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
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
	List specimenTypeList = (List)request.getAttribute(Constants.MAP_SPECIMEN_TYPE_LIST );
	
	String specimenClass = null;
	String collectionGroup = null;
	String specimenMapKey = null;
	String specimenCallBackFunction = null;
	String nodeName = null;
 
   if (pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)) {
	   specimenClass = (String) session.getAttribute(Constants.SPECIMEN_CLASS);
	   collectionGroup = (String) session.getAttribute(Constants.SPECIMEN_COLLECTION_GROUP);
	   specimenMapKey = (String) session.getAttribute(Constants.SPECIMEN_ATTRIBUTE_KEY);
	   specimenCallBackFunction =  (String) session.getAttribute(Constants.SPECIMEN_CALL_BACK_FUNCTION);
   }

%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 
<%@ include file="/pages/content/common/ActionErrors.jsp" %>


<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%">
<%
	//System.out.println("CP No. : " +collectionProtocolList.size());
	//System.out.println("SC No. : " +specimenClassList.size());
	int rowSpan = getRowSpan(collectionProtocolList, 3);
//int rowSpan = (int)((collectionProtocolList.size()%3)== 0 ? collectionProtocolList.size()/3 : (collectionProtocolList.size()/3)+1 );
%>

		<tr>
		 <td width="5" valign="bottom" >&nbsp;</td>
           <td >
		     <table  border="0" cellpadding="0" cellspacing="0" width="100%">
                 <tr>
                    <td valign="bottom" ><a href="#"><img src="images/uIEnhancementImages/sc_info1.gif" alt="Container Info" width="111" height="20" border="0" onclick="containerInfoTab()"/></a></td>
                    <td   valign="bottom"><a href="#"><img src="images/uIEnhancementImages/cp_containerMap.gif" alt="View Map" width="111" height="20" border="0" /></a></td>
                     <td width="99%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                   </tr>
              </table>
			 </td>
          </tr>
	
	<tr>
	 <td width="5" valign="bottom">&nbsp;</td>
		<td class="cp_tabtable" width="100%">
		 <table  border="0" cellpadding="3" cellspacing="0" width="100%"> 
					 <tr>		
                <td align="left" colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
 <%
		int colspanForCPLabel;
	if(collectionProtocolList.size()>specimenClassList.size())
		colspanForCPLabel = collectionProtocolList.size();
    else 
	    colspanForCPLabel = specimenClassList.size();
%>                 
		<tr>
          <td  align="left" class="tr_bg_blue1"><span class="blue_ar_b"> Storage Container Restrictions</span></td>
          </tr>
                  <tr>
					<td>
					  <table  border="0" cellspacing="0" cellpadding="0" width="100%"> 
						   	<td width="150">
								<table  border="0" cellspacing="2"  cellpadding="3" width="100%"> 
									<tr>
							<td  width="150" class="tabletd1">Collection Protocol</td>
							</tr>
								</table>
							</td>
							 <td>
								<table  border="0" cellspacing="2"  cellpadding="3" > 
									<tr>
<%	
			for(int colcnt=0;colcnt<collectionProtocolList.size();colcnt++)
			{
				String data =(String) collectionProtocolList.get(colcnt );
				
%>
                    <td  class="tabletd1"><%=data %></td>
<%
			}
%>			
							</tr>
								</table>
							</td>
							</tr>
						</table>
					</td>
                  </tr>
                  <tr>
				    <td>
						 <table  border="0" cellspacing="0" cellpadding="0" width="100%"> 
						   <tr>
							<td width="150">
								<table  border="0" cellspacing="2"  cellpadding="3" width="100%"> 
									<tr>
											<td width="150" class="tabletd1">Specimen Class</td>
									</tr>
								</table>
							</td>
							 <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
<%	
			for(int colcnt=0;colcnt<specimenClassList.size();colcnt++)
			{
				String data =(String) specimenClassList.get(colcnt );
				
%>
								<td  class="tabletd1" ><%=data %></td>
<%
			}
%>
								</tr>
								</table>
							</td>
							</tr>
						</table>
						</td>
                  </tr>

				  <tr>
					<td>
					  <table  border="0" cellspacing="0" cellpadding="0">
						 <tr>
							<td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
											<td width="140" class="tabletd1">Specimen Type</td>
												<%	
													for(int colcnt=0;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
												%>
														<td  class="tabletd1" ><%=data %></td>
												<%
														if(colcnt >=10)
														{
															break;
														}
													}
												%>
								    </tr>
								</table>
							 </td>
						</tr>
				
						<tr>
							<td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=11;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=20)
														{
															break;
														}			
													}
										%>
								  </tr>
								</table>
							</td>
					   </tr>
					   <tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
											<%	
														for(int colcnt=21;colcnt<specimenTypeList.size();colcnt++)
														{
															String data =(String) specimenTypeList.get(colcnt );
											%>
															<td  class="tabletd1" ><%=data %></td>
											<%
															if(colcnt >=30)
															{
																break;
															}			
														}
											%>
					
								</tr>
								</table>
							</td>
						</tr>
						<tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=31;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=40)
														{
															break;
														}			
													}
										%>
					
								</tr>
								</table>
							</td>
						</tr>
						<tr>
						   <td>
								<table  border="0" cellspacing="2"  cellpadding="3"> 
									<tr>
										<%	
													for(int colcnt=41;colcnt<specimenTypeList.size();colcnt++)
													{
														String data =(String) specimenTypeList.get(colcnt );
														
										%>
														<td  class="tabletd1" ><%=data %></td>
										<%
														if(colcnt >=50)
														{
															break;
														}			
													}
										%>
					
									</tr>
								</table>
							</td>
						</tr>
					</tr>
				</tr>
			</table>
						</td>
                </table></td>
              </tr>
			   <tr>
                <td class="bottomtd" colspan="2"></td>
              </tr>
			  <tr>
		
		<td  width="5"class="black_ar_t" >&nbsp;</td>
		<td class="black_ar" ><b>&nbsp;<%=verTempTwo%>&rarr;</b></td>
		 
	</tr>
				<tr>
					<td width="5" class="black_ar_t" align="center"><b><%=verTempOne%>&darr;<b></td>
					<td class="black_ar_t">
					<table  border="0" cellspacing="1" cellpadding="3">
						  	<tr><td width="5%">&nbsp;</td>	
					<% 
					    String pageOfSpecimen=(String)request.getAttribute(Constants.CONTENT_OF_CONTAINNER);
						for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=storageContainerGridObject.getTwoDimensionCapacity().intValue();i++){
                        if(storageContainerGridObject.getTwoDimensionCapacity().intValue() == 1)
                        {
                            %>
    						<td align="center"  class="subtd"><%=i%></td>
    						
    						
    					    <%
                        }
                        else
                        {
					%>
						<td align="center"  class="subtd"><%=i%></td>
					<%}}%>				
						</tr>	
					<% for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=storageContainerGridObject.getOneDimensionCapacity().intValue()+1;i++){%>
						<tr>
						<%
	                        
							if(i == 1)
							{
						%>
						   
						   
						<%
							}
                        	if(i!=storageContainerGridObject.getOneDimensionCapacity().intValue()+1) 
                            {
						%>
							<td align="center" class="subtd"><%=i%></td>
					   <% 
                            }
					   
					for (int j = Constants.STORAGE_CONTAINER_FIRST_COLUMN;j<=storageContainerGridObject.getTwoDimensionCapacity().intValue()+1;j++)
                    {
					                    
                    if(i == storageContainerGridObject.getOneDimensionCapacity().intValue()+1)
                    {
                        if(j ==1)
                        {
                        %>   
                            <td  align="center">&nbsp;</td>
       					<%
                            
                        }
                        else
                        {
                         %>   
                            <td  align="center" >&nbsp;</td>
       					<%  
                        }
                    
                        
                    }
                    else 
                    {
                        if(j ==storageContainerGridObject.getTwoDimensionCapacity().intValue()+1)
                        {
                            %>   
                            <td  align="center" >&nbsp;</td>
       						<% 
                        }
                        else
                        {
                       
						if (fullStatus[i][j] != 0)
						{
							String openStorageContainer = null;
							if (pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerIds[i][j]
                    			+ "&" + Constants.PAGE_OF + "=" + pageOf;
							}
							else
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
                    			+ "?" + Constants.SYSTEM_IDENTIFIER + "=" + childContainerIds[i][j]
                    			+ "&" + Constants.STORAGE_CONTAINER_TYPE + "=" + storageContainerType
								+ "&" + Constants.PAGE_OF + "=" + pageOf;
							}
							if (fullStatus[i][j] == 1)
							{
								 String containerName = childContainerType[i][j];
								 int containerNameSize=childContainerType[i][j].length();
								 if(containerNameSize >= 20)
									 containerName = containerName.substring(11,20)+"...";
								 else
									 containerName =containerName.substring(11,containerNameSize);
							%>
							<!--;refresh_tree('<%=childContainerName[i][j]%>')-->
							<td  align="center" class="tabletdformap" noWrap="true"><a href="<%=openStorageContainer%>" onclick="containerChanged()" class="view" onmouseover="Tip(' <%=childContainerType[i][j]%>')"><img src="images/uIEnhancementImages/used_container.gif" alt="Unused" width="32" height="32" border="0" ><br><%=containerName%></a></td>
					   	  <%}
							else{

								String containerName =childContainerType[i][j];
								 int containerNameSize=childContainerType[i][j].length();
								if(pageOfSpecimen!=null && pageOfSpecimen.equals(Constants.ALIAS_SPECIMEN))
									{
									 if(containerNameSize >= 20)
										 containerName = containerName.substring(11,20)+"...";
									 else
										 containerName =containerName.substring(11,containerNameSize);
									}
									if(pageOfSpecimen!=null && pageOfSpecimen.equals(Constants.ALIAS_SPECIMEN_ARRAY))
									{
										if(containerNameSize >= 18)
										 containerName = containerName.substring(8,18)+"...";
									 else
										 containerName =containerName.substring(8,containerNameSize);
									}

							%>
							<td  align="center" class="tabletdformap" noWrap="true"><%
							
								if(pageOfSpecimen!=null && pageOfSpecimen.equals(Constants.ALIAS_SPECIMEN))
								{%>
								<a  class="view" href="QuerySpecimenSearch.do?<%=Constants.PAGE_OF%>=pageOfNewSpecimenCPQuery&<%=Constants.SYSTEM_IDENTIFIER%>=<%=childContainerIds[i][j]%>" onmouseover="Tip('<%=childContainerType[i][j]%>')" ><img src="images/uIEnhancementImages/specimen.gif" alt="Unused" width="32" height="32"  border="0"><br><%=containerName%><!--: <%=childContainerIds[i][j]%> -->
								</a>
								
								<%}
								if(pageOfSpecimen!=null && pageOfSpecimen.equals(Constants.ALIAS_SPECIMEN_ARRAY))
								{%>
								<a class="view" href="QuerySpecimenArraySearch.do?<%=Constants.PAGE_OF%>=pageOfSpecimenArray&<%=Constants.SYSTEM_IDENTIFIER%>=<%=childContainerIds[i][j]%> " onmouseover="Tip('<%=childContainerType[i][j]%>')" ><img src="images/uIEnhancementImages/specimen_array.gif" alt="Unused" width="32" height="32"  border="0"><br><%=containerName%><!--: <%=childContainerIds[i][j]%> -->
								</a>
								</td>
								<% }
								}%>
						<%}
						else
						{
							String setParentWindowContainer = null;
							if (pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN)) 
							{
								setParentWindowContainer = "javascript:" + specimenCallBackFunction + "('" + specimenMapKey + "' , '" + storageContainerGridObject.getId() + "' , '" + storageContainerGridObject.getName() + 
								"' , '" + i + "' , '" + j + "' );closeFramedWindow()" ;
							} 							
							else if (pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
							{
								setParentWindowContainer = "javascript:setTextBoxValue('" + containerId + "','"+  storageContainerGridObject.getId() + "');" + 
															"javascript:setTextBoxValue('" + selectedContainerName + "','"+  storageContainerGridObject.getName() + "');" + 
															"javascript:setTextBoxValue('" + pos1 + "','"+  i + "');" + 
															"javascript:setTextBoxValue('" + pos2 + "','"+  j + "');" +
															"javascript:setParentWindowValue('positionInStorageContainer','"+ storageContainerGridObject.getType() + " : " + storageContainerGridObject.getId() + " Pos (" + i + "," + j + ")');" ;								
								
								setParentWindowContainer = setParentWindowContainer + "javascript:closeFramedWindow()";
							}	
							else if (pageOf.equals(Constants.PAGE_OF_ALIQUOT))
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
						<td align="center" class="tabletdformap" width="150" noWrap="true" ><img src="images/uIEnhancementImages/empty_container.gif" alt="Unused" width="32" height="32" border="0" onmouseover="Tip('Unused')"></td>
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