<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<title>Grid Operation Testing</title>
	<script src="runtime/lib/aw.js"></script>
	<link href="runtime/styles/xp/aw.css" rel="stylesheet"></link>
	<script src="jss/mygrid.js"></script>
	<link href="css/mygrid.css" rel="stylesheet"></link>

	<style>
    .aw-grid-cell {border: 1px solid threedlightshadow;}
	.aw-grid-row {border-bottom: 1px solid threedlightshadow;}
    </style>
</head>
<body onload='onBodyLoad()'>
<div id="contextmenu" class="skin0" onMouseover="highlight(event)" onMouseout="lowlight(event)" onClick="jumptomain(event)" display:none>
		<div class="menuitems" url="javascript:alert('Not Implemented');">Edit</div>
		<div class="menuitems" url="javascript:alert('Not Implemented');">Copy</div>
		<div class="menuitems" url="javascript:alert('Not Implemented');">Paste</div>
		<div class="menuitems" url="javascript:alert('Not Implemented');">Delete</div>
		<div class="menuitems" url="javascript:onSelectAll();">Select All Cells</div>
</div>

<script>
var myCells = [
["Specimen 1,1", "Specimen 1,2", "Specimen 1,3"],
["Specimen 2,1", "Specimen 2,2", "Specimen 2,3"],
["Specimen 3,1", "Specimen 3,2", "Specimen 3,3"],
["Specimen 4,1", "Specimen 4,2", "Specimen 4,3"],
["Specimen 5,1", "Specimen 5,2", "Specimen 5,3"]
];

var myHeaders = ["1", "2", "3"];

// create grid object
var obj = new AW.UI.Grid;
var prevSelectedCols = new Array();
var prevSelectedRows = new Array();
var index = 0;
var i = 0;

// assign cells and headers text

obj.setCellText(myCells);
obj.setHeaderText(myHeaders);

//	enable row selectors
obj.setSelectorVisible(true);
obj.setSelectorText(function(i){return this.getRowPosition(i)+1});

// set number of columns / rows

/* 			var template = new AW.Templates.Input
template.setSize(100, 20);
template.getContent("box/text").setAttribute("MAXLENGTH", 5);
obj.setCellTemplate(template);
obj.setRowHeight(40);
obj.getRowTemplate().setClass("text", "wrap");
// obj.setCellTemplate(new AW.Templates.Text);
 */
// obj.setCellData(usFormat);
// obj.setCellText(ukFormat);

obj.setColumnCount(myHeaders.length);
obj.setRowCount(myCells.length);


obj.getCellTemplate().setEvent("oncontextmenu", function(e)
{
   //alert(e.button);
   e.cancelBubble = false;
   e.returnValue = false;
   showmenu(e);
   if ((prevSelectedCols.length > 1) || (prevSelectedRows.length > 1))
   {
      for(i = 0; i < prevSelectedCols.length; i ++ )
      {
         setCellColor(prevSelectedCols[i], prevSelectedRows[i]);
         // alert(data);
      }
   } else {
          resetAllCells();
          obj.setSelectedColumns([]);  // deselect any selected columns...
          obj.setSelectedRows([]);  // d
          //this.setStyle("background-color", "#316ac5");
   }
 }
);

//var rowheader = new Active.Templates.Item;
//rowheader.setEvent("onmousedown", function(event){
//            rowheader.setStyle("color", "red");
//            obj.refresh();
//}); 

//obj.setTemplate("left/item", rowheader);  

function setCellColor(col, row)
{
   // obj.getCellTemplate(col, row).element().select();
   obj.getCellTemplate(col, row).setStyle("background-color", "#316ac5");
}

function resetCellColor(col, row)
{
   obj.getCellTemplate(col, row).setStyle("background-color", "");
}

function resetAllCells() {
      if ((prevSelectedCols.length > 0) || (prevSelectedRows.length > 0))
      {
         for(i = 0; i < prevSelectedCols.length ; i ++ )
         {
            resetCellColor(prevSelectedCols[i], prevSelectedRows[i]);
         }
    }
}

function setAllCells() {
      if ((prevSelectedCols.length > 0) || (prevSelectedRows.length > 0))
      {
         for(i = 0; i < prevSelectedCols.length ; i ++ )
         {
            setCellColor(prevSelectedCols[i], prevSelectedRows[i]);
         }
    }
}

obj.onCellClicked = function(event, col, row)
{
   obj.setCellEditable(false);
   index = 0;
   // alert(event.type + '  ' + event.which);
     if (event.button == 0)
     {
      if ((prevSelectedCols.length > 0) || (prevSelectedRows.length > 0))
      {
//         for(i = 0; i < prevSelectedCols.length ; i ++ )
//         {
//            resetCellColor(prevSelectedCols[i], prevSelectedRows[i]);
//         }
         resetAllCells();
         prevSelectedCols = new Array();
         prevSelectedRows = new Array();
         setCellColor(col, row);
      }

      prevSelectedCols[index] = col;
      prevSelectedRows[index] = row;
      index ++ ;
//      window.open("demo_multiselect_3107.html", "quanandconcwin" , "height=300,width=400,menubar=no,scrollbars=no,
//       toolbar=no,status=no,resizable=yes");
    }
}

obj.onCellCtrlClicked = function(event, col, row)
{
   setAllCells();
   prevSelectedCols[index] = col;
   prevSelectedRows[index] = row;
   setCellColor(prevSelectedCols[index], prevSelectedRows[index]);
   index ++ ;
}

obj.onCellDoubleClicked = function(event, col, row)
{
   obj.setCellEditable(true);
}

obj.onCellEditableChanging = function(value)
{   
    var columnArray = obj.getSelectedColumns();
    var rowArray = obj.getSelectedRows();
   
    if (value) {
         resetCellColor(columnArray[0], rowArray[0]);
         obj.getCellTemplate(columnArray[0], rowArray[0]).element().focus();
    }
}
obj.onHeaderClicked = function(event,col) {
    resetAllCells();
    this.setSelectedColumns([]);  // deselect any selected columns...
    this.setSelectedRows([]);  // d
//    alert(obj.getCellSelected());

    prevSelectedCols = new Array();
    prevSelectedRows = new Array();
    index = 0;
    
    for (i=0; i<myCells.length; i++) {
            setCellColor(col, i);
            prevSelectedCols[index] = col;
            prevSelectedRows[index] = i;
            index++;
    }
    
    var columnArray = obj.getSelectedColumns();
    var rowArray = obj.getSelectedRows();
    resetCellColor(columnArray[0], rowArray[0]);

    return true;
}

function onSelectAll() {

    var j = 0;
    prevSelectedCols = new Array();
    prevSelectedRows = new Array();
    index = 0;
    for (i=0; i<myCells.length; i++) {
        for (j=0; j<myCells[i].length; j++) {
            setCellColor(j, i);
            prevSelectedCols[index] = j;
            prevSelectedRows[index] = i;
            index++;
        }
    }
    obj.setSelectedColumns([]);  // deselect any selected columns...
    obj.setSelectedRows([]);  // d
    
//    obj.setSelectedColumns(1);  // deselect any selected columns...
//    obj.setSelectedRows(1);  // d
//    alert(obj.getSelectedModel());
/*    var firingobj = document.getElementBy;
	firingobj.style.backgroundColor=""
	firingobj.style.color="black"
	window.status=''
*/	
    //lowlight(e);
}

obj.onSelectorClicked = function(event, row){
    obj.setSelectedRows(row);
    resetAllCells();
    this.setSelectedColumns([]);  // deselect any selected columns...
    this.setSelectedRows([]);  // d
//    alert(obj.getCellSelected());

    prevSelectedCols = new Array();
    prevSelectedRows = new Array();
    index = 0;
    
    for (i=0; i<myCells[row].length; i++) {
            setCellColor(i,row);
            prevSelectedCols[index] = i;
            prevSelectedRows[index] = row;
            index++;
    }
    //alert(row);
}

obj.onTopSelectorClicked = function(event, row){
    onSelectAll();
};
document.write(obj);
</script>
</BODY>
</HTML>