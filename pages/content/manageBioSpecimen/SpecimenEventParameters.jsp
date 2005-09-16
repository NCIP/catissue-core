<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<link href="runtime/styles/xp/grid.css" rel="stylesheet" type="text/css" ></link>
<script src="runtime/lib/grid.js"></script>

<!-- grid format -->
	<style>
		.active-controls-grid {height: 100%; font: menu;}
		
		.active-column-0 {width:  80px;}
		.active-column-1 {width: 200px;}
		.active-column-2 {text-align: right;}
		.active-column-3 {text-align: right;}
		.active-column-4 {text-align: right;}
		
		.active-grid-column {border-right: 1px solid threedlightshadow;}
		.active-grid-row {border-bottom: 1px solid threedlightshadow;}
	</style>
<!--  grid data -->
<head>
<%
	String[] columnList = Constants.EVENT_PARAMETERS_COLUMNS;
	List dataList = (List) request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String specimenIdentifier = (String)request.getParameter("specimenIdentifier");
if(dataList!=null && dataList.size() != 0)
{
%>

<script>
var myData = [<%int xx;%><%for (xx=0;xx<(dataList.size()-1);xx++){%>
<%
	List row = (List)dataList.get(xx);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"],<%}%>
<%
	List row = (List)dataList.get(xx);
  	int j;
%>
[<%for (j=0;j < (row.size()-1);j++){%>"<%=row.get(j)%>",<%}%>"<%=row.get(j)%>"]
];

var columns = [<%int k;%><%for (k=0;k < (columnList.length-1);k++){%>"<%=columnList[k]%>",<%}%>"<%=columnList[k]%>"];
</script>

<%}%>

<script language="JavaScript">

	function onParameterChange(element)
	{
		var action = "";
		var iFrame = document.getElementById("newEventFrame");
		var addNew = document.getElementById("sepAdd");
		//addNew.target="_blank";
		
		if(element.value == "Cell Specimen Review")
			action = "/catissuecore/CellSpecimenReviewParameters.do?operation=add&pageOf=pageOfCellSpecimenReviewParameters";
		else if(element.value == "Check In Check Out")
			action = "/catissuecore/CheckInCheckOutEventParameters.do?operation=add&pageOf=pageOfCheckInCheckOutEventParameters";
		else if(element.value == "Collection")
			action = "/catissuecore/CollectionEventParameters.do?operation=add&pageOf=pageOfCollectionEventParameters";
		else if(element.value == "Disposal")
			action = "/catissuecore/DisposalEventParameters.do?operation=add&pageOf=pageOfDisposalEventParameters";
		else if(element.value == "Embedded")
			action = "/catissuecore/EmbeddedEventParameters.do?operation=add&pageOf=pageOfEmbeddedEventParameters";
		else if(element.value == "Fixed")
			action = "/catissuecore/FixedEventParameters.do?operation=add&pageOf=pageOfFixedEventParameters";
		else if(element.value == "Fluid Specimen Review")
			action = "/catissuecore/FluidSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfFluidSpecimenReviewParameters";
		else if(element.value == "Frozen")
			action = "/catissuecore/FrozenEventParameters.do?operation=add&pageOf=pageOfFrozenEventParameters";
		else if(element.value == "Molecular Specimen Review")
			action = "/catissuecore/MolecularSpecimenReviewParameters.do?operation=add&pageOf=pageOfMolecularSpecimenReviewParameters";
		else if(element.value == "Procedure")
			action = "/catissuecore/ProcedureEventParameters.do?operation=add&pageOf=pageOfProcedureEventParameters";
		else if(element.value == "Received")
			action = "/catissuecore/ReceivedEventParameters.do?operation=add&pageOf=pageOfReceivedEventParameters";
		else if(element.value == "Spun")
			action = "/catissuecore/SpunEventParameters.do?operation=add&pageOf=pageOfSpunEventParameters";
		else if(element.value == "Thaw")
			action = "/catissuecore/ThawEventParameters.do?operation=add&pageOf=pageOfThawEventParameters";
		else if(element.value == "Tissue Specimen Review")
			action = "/catissuecore/TissueSpecimenReviewEventParameters.do?operation=add&pageOf=pageOfTissueSpecimenReviewParameters";
		else if(element.value == "Transfer")
		{
			action = "/catissuecore/TransferEventParameters.do?operation=add&pageOf=pageOfTransferEventParameters";
			//action = action + "&fromPositionData=" + fromPositionData;			
			//action = action + "&posOne=" + posOne;
			//action = action + "&posTwo=" + posTwo;
			//action = action + "&storContId=" + storContId;			
			
		}	
		
		var specimenIdentifier = "<%=specimenIdentifier%>";
		action = action + "&specimenId=" + specimenIdentifier;
		addNew.href = action;
		iFrame.src = action;
		
		if(element.value == "<%=Constants.SELECT_OPTION%>")
		{
			iFrame.src = "";
			addNew.href = "#";
			addNew.target="_parent";
		}
	}
</script>

</head>

<html:errors />

<html:form action="<%=Constants.SPECIMEN_ADD_ACTION%>">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="500">
<%
	if(dataList!=null && dataList.size() != 0)
	{
%>
<tr>
 <td>
  	 <table summary="" cellpadding="3" cellspacing="0" border="0" width="500">

   	 	<tr>
			<td>
				<div STYLE="overflow: auto; width:550; height: 200; padding:0px; margin: 0px; border: 1px solid" id="eventGrid">
				<script>
				
					//	create ActiveWidgets Grid javascript object.
					var obj = new Active.Controls.Grid;
					
					//	set number of rows/columns.
					obj.setRowProperty("count", <%=dataList.size()%>);
					obj.setColumnProperty("count", <%=columnList.length-1%>);
					
					//	provide cells and headers text
					obj.setDataProperty("text", function(i, j){return myData[i][j]});
					obj.setColumnProperty("text", function(i){return columns[i]});
					obj.setDataProperty("value", function(i){return myData[i][0]});
					
					//	set headers width/height.
					obj.setRowHeaderWidth("28px");
					obj.setColumnHeaderHeight("20px");
			
					var row = new Active.Templates.Row;
					row.setEvent("ondblclick", function(){this.action("myAction")}); 
					
					obj.setTemplate("row", row);
			   		obj.setAction("myAction", 
						function(src){var frame = document.getElementById("newEventFrame"); frame.src = 'SearchObject.do?pageOf=' + myData[this.getSelectionProperty("index")][<%=columnList.length-1%>] + '&operation=search&systemIdentifier=' + src.getDataProperty("value")}); 
			
					//	write grid html to the page.
					document.write(obj);
				</script>
				</div>
			</td>
		</tr>
	</table>
 </td>
</tr>
<% } %>

<tr>
	<td>&nbsp;</td>
</tr>

<tr>
	<td>
		<html:select property="specimenEventParameter" styleClass="formFieldSized15" styleId="className" size="1" disabled="false" onchange="onParameterChange(this)">
			<html:options name="<%=Constants.EVENT_PARAMETERS_LIST%>" labelName="<%=Constants.EVENT_PARAMETERS_LIST%>"/>
		</html:select>
		<a id="sepAdd" href="#">
  			<bean:message key="app.addNew" />
   		</a>
	</td>
</tr>

<tr>
	<td>&nbsp;</td>
</tr>

<tr>
	<td>
		<iframe name="newEventFrame" id="newEventFrame" src="" width="550" height="400" scrolling="auto">
		</iframe>
	</td>
</tr>

</table>
</html:form>