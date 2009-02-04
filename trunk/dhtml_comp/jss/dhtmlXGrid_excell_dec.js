/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
function eXcell_dec(cell){
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 
 this.getValue = function(){
 return parseFloat(this.cell.innerHTML.replace(/,/g,""));
}
 this.setValue = function(val){
 var format = "0,000.00";
 if(val=="0"){
 this.cell.innerHTML = format.replace(/.*(0\.[0]+)/,"$1");
 return;
}
 var z = format.substr(format.indexOf(".")+1).length
 val = Math.round(val*Math.pow(10,z)).toString();
 var out = "";
 var cnt=0;
 var fl = false;
 for(var i=val.length-1;i>=0;i--){
 cnt++;
 out = val.charAt(i)+out;
 if(!fl && cnt==z){
 out = "."+out;
 cnt=0;
 fl = true;
}
 if(fl && cnt==3 && i!=0 && val.charAt(i-1)!='-'){
 out = ","+out;
 cnt=0;
}
}
 this.cell.innerHTML = out;
}
}
eXcell_dec.prototype = new eXcell_ed;

