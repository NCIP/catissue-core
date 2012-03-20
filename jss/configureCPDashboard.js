var dashboardItemGrid, dashboardItemGridCombo, dashboardItemRowCounter = 1;
function doCPDashboardInitGrid() {
	dashboardItemGrid = new dhtmlXGridObject('cpDashboard_container');
	dashboardItemGrid.setImagePath("dhtmlx_suite/imgs/");
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
		dashboardItemGrid.cellById(counter, 3).setValue(
				jsonObject.row[i].userDefinedLabel);
		dashboardItemGrid.cellById(counter, 4).setValue("false");
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
		dashboardItemGrid.deleteRow(ids[i]);
	}
}
function setCSLevelFormData() {
	var count = dashboardItemGrid.getRowsNum();
	var dataArrayString = "";

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

	var mainJsonString = '{"row":[' + dataArrayString + ']}';

	document.getElementById("dashboardLabelJsonValue").value = mainJsonString;// JSON.stringify(jsonObject);

}