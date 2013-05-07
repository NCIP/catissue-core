/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 
 
function eXcell_mro(cell){
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.edit = function(){}
}
eXcell_mro.prototype = new eXcell;
eXcell_mro.prototype.getValue = function(){
 return this.cell.childNodes[0].innerHTML._dhx_trim();
}
eXcell_mro.prototype.setValue = function(val){
 if(!this.cell.childNodes.length){
 this.cell.style.whiteSpace='normal';
 this.cell.innerHTML="<div style='height:100%;white-space:nowrap;overflow:hidden;'></div>";
}

 if(!val || val.toString()._dhx_trim()=="")
 val="&nbsp;"
 this.cell.childNodes[0].innerHTML = val;
}



