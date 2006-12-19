/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
dhtmlXGridObject.prototype.useCSV = function(path){
 if(!this._csv_loadXML){
 this._csv_loadXML=this.loadXML;
 this._csv_loadXMLString=this.loadXMLString;
 this.loadXML=this.loadCSVFile;
 this.loadXMLString=this.loadCSVFile;
}
}


 
dhtmlXGridObject.prototype.loadCSVFile = function(path){
 this.xmlLoader = new dtmlXMLLoaderObject(this._onCSVFileLoad,this);
 this.xmlLoader.loadXML(path);
}
dhtmlXGridObject.prototype._onCSVFileLoad=function(obj,b,c,d,xml){
 var z=this.xmlDoc.responseText;
 obj.loadCSVString(z);
}


 
dhtmlXGridObject.prototype.setCSVDelimiter = function(str){
 this._csvDelim=(str||this._csvDelim);
 this._csvDelimX=this._csvDelim.charCodeAt(0);
 var trans=[0,1,2,3,4,5,6,7,8,9,"A","B","C","D","E","F"];
 this._csvDelimX="\\x"+trans[Math.floor(this._csvDelimX/16)]+""+trans[(this._csvDelimX%16)];
}

 
dhtmlXGridObject.prototype.loadCSVString = function(str){
 if(!this._csvDelimX)this.setCSVDelimiter();
 var r1=new RegExp("^([^"+this._csvDelimX+"]+)"+this._csvDelimX);
 var r2=new RegExp("\n([^"+this._csvDelimX+"]+)"+this._csvDelimX,"g");
 var r3=new RegExp(""+this._csvDelimX+"","g");
 str=str.replace(r1,"<row id='$1'><cell><![CDATA[");
 str=str.replace(r2,"]]></cell></row><row id='$1'><cell><![CDATA[");
 str=str.replace(r3,"]]></cell><cell><![CDATA[");
 str="<?xml version='1.0'?><rows>"+str+"]]></cell></row></rows>";
 this.xmlLoader = new dtmlXMLLoaderObject(this.doLoadDetails,window,true,this.no_cashe);
 this.xmlLoader.loadXMLString(str);
}

 
dhtmlXGridObject.prototype.serializeToCSV = function(name){
 this.editStop()
 if(this._mathSerialization)
 this._agetm="getMathValue";
 else this._agetm="getValue";

 var out="";
 
 var i=0;
 var leni=(this._dload)?this.rowsBuffer[0].length:this.rowsCol.length;
 for(i;i<leni;i++){
 var r = this.rowsCol[i];
 var temp=this._serializeRowToCVS(r)
 out+= temp;
 if(temp!="")out+= "\n";
}

 return out;
}

 
dhtmlXGridObject.prototype._serializeRowToCVS = function(r){
 var out = "";
 out+= r.idd;

 
 var changeFl=false;
 for(var jj=0;jj<r.childNodes.length;jj++)
 if((!this._srClmn)||(this._srClmn[jj])){
 var cvx=r.childNodes[jj];
 out+= this._csvDelim;

 var zx=this.cells(r.idd,cvx._cellIndex);
 if(zx.cell)
 zxVal=zx[this._agetm]();
 else zxVal="";


 if((this._chAttr)&&(zx.wasChanged()))
 changeFl=true;

 out+=((zxVal===null)?"":zxVal)
 
 if((this._ecspn)&&(cvx.colSpan)){
 cvx=cvx.colSpan-1;
 for(var u=0;u<cvx;u++)
 out+= this._csvDelim;
}
 

}
 if((this._onlChAttr)&&(!changeFl))return "";
 return out;
}

dhtmlXGridObject.prototype.toClipBoard=function(val){
 if(window.clipboardData)
 window.clipboardData.setData("Text",val);
 else
(new Clipboard()).copy(val);

}
dhtmlXGridObject.prototype.fromClipBoard=function(){
 if(window.clipboardData)
 return window.clipboardData.getData("Text");
 else
 return(new Clipboard()).paste();
}

 
dhtmlXGridObject.prototype.cellToClipboard = function(rowId,cellInd){
 if((!rowId)||(!cellInd)){
 if(!this.selectedRows[0])return;
 rowId=this.selectedRows[0].idd;
 cellInd=this.cell._cellIndex;
}
 this.toClipBoard(this.cells(rowId,cellInd).getValue());
}

 
dhtmlXGridObject.prototype.updateCellFromClipboard = function(rowId,cellInd){
 if((!rowId)||(!cellInd)){
 if(!this.selectedRows[0])return;
 rowId=this.selectedRows[0].idd;
 cellInd=this.cell._cellIndex;
}
 this.cells(rowId,cellInd).setValue(this.fromClipBoard());
}

 
dhtmlXGridObject.prototype.rowToClipboard = function(rowId){
 var out="";
 if(this._mathSerialization)
 this._agetm="getMathValue";
 else this._agetm="getValue";
 if(rowId)
 out=this._serializeRowToCVS(this.getRowById(rowId));
 else
 for(var i=0;i<this.selectedRows.length;i++){
 if(out)out+="\n";
 out+=this._serializeRowToCVS(this.selectedRows[i]);
}
 this.toClipBoard(out);
}

 
dhtmlXGridObject.prototype.updateRowFromClipboard = function(rowId){
 var csv=this.fromClipBoard();
 if(!csv)return;
 if(rowId)
 var r=this.getRowById(rowId);
 else
 var r=this.selectedRows[0];
 if(!r)return;
 csv=csv.split(this._csvDelim);
 for(var i=1;i<csv.length;i++)
 this.cells3(r,i-1).setValue(csv[i]);
}

 
dhtmlXGridObject.prototype.addRowFromClipboard = function(){
 var csv=this.fromClipBoard();
 if(!csv)return;
 var z=csv.split("\n");
 for(var i=0;i<z.length;i++)
 if(z[i]){
 csv=z[i].split(this._csvDelim);
 this.addRow(csv[0],csv.slice(1));
}
}

 
dhtmlXGridObject.prototype.gridToClipboard = function(){
 this.toClipBoard(this.serializeToCSV());
}

 
dhtmlXGridObject.prototype.gridFromClipboard = function(){
 var csv=this.fromClipBoard();
 if(!csv)return;
 this.loadCSVString(csv);
}


 
dhtmlXGridObject.prototype.loadObject = function(obj){
}


 
dhtmlXGridObject.prototype.loadJSONFile = function(path){
}


 
dhtmlXGridObject.prototype.serializeToObject = function(){
}

 
dhtmlXGridObject.prototype.serializeToJSON = function(){
}





