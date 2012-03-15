//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/

function eXcell_dec(cell){if (cell){this.cell = cell;this.grid = this.cell.parentNode.grid};this.getValue = function(){return parseFloat(this.cell.innerHTML.replace(/,/g,""))};this.setValue = function(val){var format = "0,000.00";if(val=="0"){this.setCValue(format.replace(/.*(0\.[0]+)/,"$1"),val);return};var z = format.substr(format.indexOf(".")+1).length
 val = Math.round(val*Math.pow(10,z)).toString();var out = "";var cnt=0;var fl = false;for(var i=val.length-1;i>=0;i--){cnt++;out = val.charAt(i)+out;if(!fl && cnt==z){out = "."+out;cnt=0;fl = true};if(fl && cnt==3 && i!=0 && val.charAt(i-1)!='-'){out = ","+out;cnt=0}};this.setCValue(out,val)}};eXcell_dec.prototype = new eXcell_ed;//(c)dhtmlx ltd. www.dhtmlx.com
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/