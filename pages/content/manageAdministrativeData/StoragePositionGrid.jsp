<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.storage.StorageContainerGridObject,edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>
<script language="JavaScript" type="text/javascript"
	src="jss/caTissueSuite.js"></script>
<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script type="text/javascript" src="jss/wz_tooltip.js"></script>
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
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

function setTextBoxValue(elementId1,elementValue1,elementId2,elementValue2)
{
	var id1 = parent.window.document.getElementById(elementId1);	
	id1.value = elementValue1;
	var id2 = parent.window.document.getElementById(elementId2);	
	id2.value = elementValue2;
	parent.window.dhxWins.window("containerPositionPopUp").close();
}

function closeFramedWindow()
{
	alert("closeFrameWindow");
	parent.window.dhxWins.window("containerPositionPopUp").close();
}

</script>

<%
	StorageContainerGridObject storageContainerGridObject 
			= (StorageContainerGridObject)request.getAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT);
			System.out.println("grid object :: "+storageContainerGridObject);
			String temp ="";
    String verStrTemp ="";
    String verTempOne ="";
    String verTempTwo ="";
    String filler = " ";
	String oneDimLabellingScheme="";
	String twoDimLabellingScheme="";
	String oneDimLabel="";
	String twoDimLabel="";
	int oneDimensionCapacity=0,twoDimensionCapacity=0;
	int rowSpanValue=0,colSpanValue=0;
	String pos1= "";
    String pos2= "";
	boolean [][]availablePositions =null;
	
	if(null!=storageContainerGridObject)
	{
	Integer startNumber = null;
	availablePositions = storageContainerGridObject.getAvailablePositions();
	
	oneDimLabellingScheme = storageContainerGridObject.getOneDimensionLabellingScheme();
	twoDimLabellingScheme = storageContainerGridObject.getTwoDimensionLabellingScheme();
	
	oneDimLabel = storageContainerGridObject.getOneDimensionLabel();
	twoDimLabel = storageContainerGridObject.getTwoDimensionLabel();
    
    
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
    
	rowSpanValue = storageContainerGridObject.getOneDimensionCapacity().intValue();
	colSpanValue = storageContainerGridObject.getTwoDimensionCapacity().intValue();
	oneDimensionCapacity = storageContainerGridObject.getOneDimensionCapacity().intValue();
	twoDimensionCapacity = storageContainerGridObject.getTwoDimensionCapacity().intValue();
	
	String containerStyle = (String)session.getAttribute(Constants.CONTAINER_STYLE);

	String containerStyleId = (String)session.getAttribute(Constants.CONTAINER_STYLEID);

	String xDimStyleId = (String)session.getAttribute(Constants.XDIM_STYLEID);
	String yDimStyleId = (String)session.getAttribute(Constants.YDIM_STYLEID);
     pos1= (String) request.getAttribute(Constants.POS1);
     pos2= (String)request.getAttribute(Constants.POS2);
	}	

%>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" /> 

<%@ include file="/pages/content/common/ActionErrors.jsp" %>
<!-- target of anchor to skip menus -->
<table summary="" cellpadding="0" cellspacing="0" border="0"  width="100%" >
	<tr>
	<%if(null!=storageContainerGridObject){%>
		<td  width="5"class="black_ar_t">&nbsp;</td>
		<td class="black_ar"><b>&nbsp;<%=verTempTwo%></b></td>
		 
	</tr>
	<tr>
		<td   width="5" class="black_ar_t" align="center"><b><%=verTempOne%><b></td>
		<td class="black_ar_t">
		<table style="border:3px;"  cellpadding="0" cellspacing="1" border="0" >
				 
				<tr>
					<td colspan="2">
					<table style="table-layout: fixed;" cellspacing="1" cellpadding="2" width="100%">
					<tr>
						<td class="subtdPosGrid">&nbsp;</td>
						
						<%
						for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=twoDimensionCapacity;i++)
						{
							if(twoDimensionCapacity == 1)
							{
							%>
								<td align="center"  class="subtdPosGrid">
								<%=edu.wustl.catissuecore.util.global.AppUtility.getPositionValue(twoDimLabellingScheme,i)%>
								</td>
							<%
							}
							else
							{
							%>
								<td align="center"  class="subtdPosGrid">
								<%=edu.wustl.catissuecore.util.global.AppUtility.getPositionValue(twoDimLabellingScheme,i)%>
								</td>
							<%}
						}%>				
					</tr>
					
					<% 
						for (int i = Constants.STORAGE_CONTAINER_FIRST_ROW;i<=oneDimensionCapacity;i++)
						{%>
							<tr>
							<%
								if(i!=oneDimensionCapacity+1) 
								{%>
									<td align="center" class="subtdPosGrid">
									<%=edu.wustl.catissuecore.util.global.AppUtility.getPositionValue(oneDimLabellingScheme,i)%>
									</td>
								<%}
					   
								for (int j = Constants.STORAGE_CONTAINER_FIRST_COLUMN;j<=twoDimensionCapacity;j++)
								{
									// Default cell boundary
                   						if(i == oneDimensionCapacity+1)
										{
											if(j ==1)
											{%>   
												<td align="center" class="subtd">
												<%=edu.wustl.catissuecore.util.global.AppUtility.getPositionValue(oneDimLabellingScheme,i)%>
												</td>
											<%}
											else
											{%>   
												<td  align="center" >&nbsp;</td>
											<%}
										}
										else 
										{
											if(j ==twoDimensionCapacity+1)
											{%>   
												<td  align="center" >&nbsp;</td>
											<%}
											else
											{
												if (!availablePositions[i][j])
												{%>
													<td bgcolor="#F25252;" align="center"></td>
												<%}
												else
												{
													String setParentWindowContainer = null;
													setParentWindowContainer = "javascript:setTextBoxValue('"+pos1+"','"+  AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(),i) + "','"+pos2+"','"+  AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(),j) + "');\"" ;
													%>
													<td width="8%" style="cursor:pointer" onMouseOver="this.bgColor='#008000'" onMouseOut="this.bgColor='#83F2B9'" bgColor=#83F2B9 align="center" onclick="<%=setParentWindowContainer%>"><a href="<%=setParentWindowContainer%>"></a></td>
												<%}%>					   	  
										<%}%>
								<%}%>
						<%}
					}%>
					  </tr>
					
					</table>
					</td>
				</tr>
		</table>
		</td>
	</tr>
	<%}%>
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