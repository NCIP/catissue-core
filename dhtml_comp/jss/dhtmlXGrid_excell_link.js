/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 

function eXcell_link(cell){
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.edit = function(){}
 this.getValue = function(){
 if(this.cell.firstChild.getAttribute)
 return this.cell.firstChild.innerHTML+"^"+this.cell.firstChild.getAttribute("href")
 else
 return "";
}
 this.setValue = function(val){
 var valsAr = val.split("^");
 if(valsAr.length==1)
 valsAr[1] = "";
 else{
 if(valsAr.length>1){
 valsAr[1] = "href='"+valsAr[1]+"'";
 if(valsAr.length==3)
 valsAr[1]+= " target='"+valsAr[2]+"'";
 else
 valsAr[1]+= " target='_blank'";
}
}
 
 this.setCValue("<a "+valsAr[1]+" onclick='(isIE()?event:arguments[0]).cancelBubble = true;'>"+valsAr[0]+"</a>",valsAr);
}
}

eXcell_link.prototype = new eXcell;
eXcell_link.prototype.getTitle=function(){
 return this.cell.firstChild.getAttribute("href");
}


