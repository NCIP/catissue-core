/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
function eXcell_cntr(cell){
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.edit = function(){}
 this.getValue = function(){
 return this.cell.parentNode.rowIndex;
}
 this.setValue = function(val){
 this.cell.style.paddingRight = "2px";
 var cell=this.cell;
 window.setTimeout(function(){
 cell.innerHTML = cell.parentNode.rowIndex;
 cell=null;
},100);
}
}
eXcell_cntr.prototype = new eXcell;

