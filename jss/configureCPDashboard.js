var dashboardItemGrid, dashboardItemGridCombo, dashboardItemRowCounter = 1;
var associationIds = "";
function doCPDashboardInitGrid() {
dashboardItemGrid = new dhtmlXGridObject('cpDashboard_container');
	dashboardItemGrid.setImagePath("dhtmlxSuite_v35/dhtmlxGrid/codebase/imgs/");
	dashboardItemGrid.setSkin("dhx_skyblue");
	dashboardItemGrid
			.setHeader(",Sequence Order,Dashboard Item,Display Label,Delete");
	dashboardItemGrid
			.setColumnIds("AssocID,seqOrder,Dashboard Item,Display Label,Delete");
	dashboardItemGrid.setColTypes("ro,ro,co,ed,ch");
	dashboardItemGrid.setInitWidths("0,0,490,490,*");
	dashboardItemGrid.setColAlign("left,left,left,left,center");
	dashboardItemGrid.setColSorting(",int,str,str,");

	dashboardItemGrid.init();
	dashboardItemGridCombo = dashboardItemGrid.getCombo(2);
	dhtmlxAjax.get('AjaxSearchCPLabel.do?operation=fetchLabels',
			initdashboardItemGridCombo);

}
function initdashboardItemGridCombo(loader) {

	var jsonObject = eval('(' + loader.xmlDoc.responseText + ')');
	for ( var i = 0; i < jsonObject.row.length; i++) {
		dashboardItemGridCombo.put(jsonObject.row[i].id, jsonObject.row[i].field);
	}
	if (document.getElementById("dashboardLabelJsonValue").value == "") {
		document.getElementById("dashboardLabelJsonValue").value = '{"row":[{}]}';
	}

	initdashboardItemGridForEdit();
}

function initdashboardItemGridForEdit() {
    
    var disableEdit = isdefault;
    
    var jsonObject = eval('(' + document
			.getElementById("dashboardLabelJsonValue").value + ')');

	if (jsonObject.row.length == 0) {
		addCSLevelFormRow();
	}
	for ( var i = 0; i < jsonObject.row.length; i++) {
		addCSLevelFormRow();
		var counter = dashboardItemRowCounter - 1;

		dashboardItemGrid.cellById(counter, 0)
				.setValue(jsonObject.row[i].assocId);
		dashboardItemGrid.cellById(counter, 1).setValue(
				jsonObject.row[i].seqOrder);
		dashboardItemGrid.cellById(counter, 2)
				.setValue(jsonObject.row[i].labelId);
dashboardItemGrid.cellById(counter, 2).setDisabled(disableEdit);
		dashboardItemGrid.cellById(counter, 3).setValue(
				jsonObject.row[i].userDefinedLabel);
dashboardItemGrid.cellById(counter, 3).setDisabled(disableEdit);
		dashboardItemGrid.cellById(counter, 4).setValue("false");


	}
document.getElementById("addSpecimenReq").disabled = (disableEdit == "true");
document.getElementById("deleteStudyForm").disabled = (disableEdit == "true");
if(disableEdit == "true") 
{
 document.getElementById("mesgSpan").style.display = "none";
} 
else
{
document.getElementById("mesgSpan").style.display = "block";
}
 
dashboardItemGrid.selectRowById(0);
}

function addCSLevelFormRow() {
	dashboardItemGrid.addRow(dashboardItemRowCounter, "," + dashboardItemRowCounter
			+ ",,,");
	dashboardItemGrid.cellById(dashboardItemRowCounter, 4).setValue("false");
	dashboardItemGrid.selectRowById(dashboardItemRowCounter);
	dashboardItemRowCounter++;
}

function checkValue() {
	var checkedRows = dashboardItemGrid.getCheckedRows(4);
	var ids = checkedRows.split(",");
	for ( var i = 0; i < ids.length; i++) {
	     var assocId = dashboardItemGrid.cellById(ids[i], 0).getValue();
		 if(associationIds == "")
			 associationIds = associationIds + assocId;
		 else
			 associationIds = associationIds +"," + assocId;
		dashboardItemGrid.deleteRow(ids[i]);
	}
}
function setCSLevelFormData() {
	
	var isSavedashboard = true; 
	
	var dataArrayString = "";
  	if(document.getElementById("saveDashboard") != null)
  	{
  		isSavedashboard = document.getElementById("saveDashboard").checked;
  	}
	if(isSavedashboard)
  {
	 var count = dashboardItemGrid.getRowsNum();
	 if (count == 1) {
		var rowID = dashboardItemGrid.getRowId(0);
		var labelId = dashboardItemGrid.cellById(rowID, 2).getValue();
		var userDefinedLabel = dashboardItemGrid.cellById(rowID, 3).getValue();
		if (labelId == "" && userDefinedLabel == "") {
			count = 0;
		}
	 }
	for ( var i = 0; i < count; i++) {
		var rowID = dashboardItemGrid.getRowId(i);

		var assocId = dashboardItemGrid.cellById(rowID, 0).getValue();
		var seqOrder = dashboardItemGrid.cellById(rowID, 1).getValue();
		var labelId = dashboardItemGrid.cellById(rowID, 2).getValue();
		var userDefinedLabel = dashboardItemGrid.cellById(rowID, 3).getValue();
		if (seqOrder == "") {
			seqOrder = 0;
		}
		if (labelId != "") {
			var jsonString = '{' + '"assocId":"' + assocId + '",'
					+ '"seqOrder":"' + seqOrder + '",' + '"labelId":"'
					+ labelId + '",' + '"userDefinedLabel":"'
					+ userDefinedLabel + '"' + '}';

			dataArrayString = dataArrayString + jsonString + ',';
		}
	}
  }

	var mainJsonString = '{"row":[' + dataArrayString + '],"deletedAssocIds":"'+ associationIds +'"}';

	document.getElementById("dashboardLabelJsonValue").value = mainJsonString;// JSON.stringify(jsonObject);

}

function enableDisableGrid(checkbox)
{
	var cbStatus = !(checkbox.checked);
        document.getElementById("addSpecimenReq").disabled = cbStatus ;
        document.getElementById("deleteStudyForm").disabled = cbStatus ; 
         if(cbStatus == true ) //checkbox is not checked
      {
           document.getElementById("mesgSpan").style.display = "none";
      } 
     else
     {
          document.getElementById("mesgSpan").style.display = "block";
     }
	
        var count = dashboardItemGrid.getRowsNum();
	for ( var i = 0; i < count; i++) {
		var rowID = dashboardItemGrid.getRowId(i);
		dashboardItemGrid.cellById(rowID, 2).setDisabled(cbStatus);
 		dashboardItemGrid.cellById(rowID, 3).setDisabled(cbStatus);
         }
}