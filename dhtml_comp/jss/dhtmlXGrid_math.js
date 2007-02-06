/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
function eXcell_math(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
}catch(er){}
 
 this.edit = function(){
 this.val = this.getValue();
 if((this.cell._val.indexOf("=")==0)&&(!this.grid._mathEdit))return false;
 this.obj = document.createElement("TEXTAREA");
 this.obj.style.width = "100%";
 this.obj.style.height =(this.cell.offsetHeight-4)+"px";
 this.obj.style.border = "0px";
 this.obj.style.margin = "0px";
 this.obj.style.padding = "0px";
 this.obj.style.overflow = "hidden";
 this.obj.style.fontSize = "12px";
 this.obj.style.fontFamily = "Arial";
 this.obj.wrap = "soft";
 this.obj.style.textAlign = this.cell.align;
 this.obj.onclick = function(e){(e||event).cancelBubble = true}

 this.obj.value = this.cell._val;
 this.cell.innerHTML = "";
 this.cell.appendChild(this.obj);
 this.obj.onselectstart=function(e){if(!e)e=event;e.cancelBubble=true;return true;};
 this.obj.focus()
 this.obj.focus()
}
 
 this.getValue = function(){
 
 if(this.grid._strangeParams[this.cell._cellIndex]){
 var d=this.cell;
 var z=eval("new eXcell_"+(this.grid._strangeParams[this.cell._cellIndex])+"(d)");
 return z.getValue();
}else return this.cell.innerHTML.toString()._dhx_trim();

}
 
 this.setValueA = function(val){
 if(this.grid._strangeParams[this.cell._cellIndex]){
 var z=eval("new eXcell_"+this.grid._strangeParams[this.cell._cellIndex]+"(this.cell)");
 z.setValue(val==window.undefied?"":val);
}
 else
 this.setCValue(this.grid._calcSCL(cell)||"0");
}
 
 this.setValue = function(val){
 this.cell._val=val;
 this.cell._code=this.grid._compileSCL(val,this.cell);
 this.setValueA(this.grid._calcSCL(cell));
 this.grid._checkSCL(this.cell);
}
 
 this.detach = function(){if((this.cell._val.indexOf("=")==0)&&(!this.grid._mathEdit))return false;
 this.setValue(this.obj.value);
 return this.val!=this.getValue();
}
}
eXcell_math.prototype = new eXcell;




dhtmlXGridCellObject.prototype.setValueA=dhtmlXGridCellObject.prototype.setValue;
eXcell_price.prototype.setValueA=eXcell_price.prototype.setValue;
eXcell_dyn.prototype.setValueA=eXcell_dyn.prototype.setValue;
eXcell_ch.prototype.setValueA=eXcell_ch.prototype.setValue;
eXcell_ra.prototype.setValueA=eXcell_ra.prototype.setValue;
eXcell_cp.prototype.setValueA=eXcell_cp.prototype.setValue;
eXcell_co.prototype.setValueA=eXcell_co.prototype.setValue;
 
 
eXcell_edn.prototype.setValueA=eXcell_edn.prototype.setValue;
 
 

 
eXcell_math.prototype._NsetValue=function(val){
 this.setValueA(val);
 this.grid._checkSCL(this.cell);
}


dhtmlXGridCellObject.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_price.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_dyn.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_ch.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_ra.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_cp.prototype.setValue=eXcell_math.prototype._NsetValue;
eXcell_co.prototype.setValue=eXcell_math.prototype._NsetValue;
 
 
eXcell_edn.prototype.setValue=eXcell_math.prototype._NsetValue;
 
 

 
dhtmlXGridObject.prototype._checkSCL=function(cell){
 if(!this.math_off)
{
 if(cell._SCL){
 for(var i=0;i<cell._SCL.length;i++)
 if(cell._SCL[i]){
 if(this._strangeParams[cell._SCL[i]._cellIndex]){
 var z=eval("new eXcell_"+this._strangeParams[cell._SCL[i]._cellIndex]+"(cell._SCL[i])");
 val=this._calcSCL(cell._SCL[i]);
 z.setValue(val==window.undefied?"":val);
}
 else
 cell._SCL[i].innerHTML=this._calcSCL(cell._SCL[i]);
 this._checkSCL(cell._SCL[i]);
}
}


 if(cell.parentNode.parent_id)
{
 var pRow=this.getRowById(cell.parentNode.parent_id);
 if(!pRow)return;
 if(pRow.childNodes[cell._cellIndex]._sumArr)
{
 if(this._strangeParams[cell._cellIndex]){
 var z=eval("new eXcell_"+this._strangeParams[cell._cellIndex]+"(pRow.childNodes[cell._cellIndex])");
 var val=this._calcSCL(pRow.childNodes[cell._cellIndex]);
 z.setValue(val==window.undefied?"":val);
}
 else
 pRow.childNodes[cell._cellIndex].innerHTML=this._calcSCL(pRow.childNodes[cell._cellIndex]);
 this._checkSCL(pRow.childNodes[cell._cellIndex]);

}

}
}
 else this.math_req=true;
}

 
dhtmlXGridObject.prototype.setMathRound=function(digits){
 this._roundD=digits;
}

 
dhtmlXGridObject.prototype.enableMathEditing=function(status){
 this._mathEdit=convertStringToBoolean(status);
}

 
dhtmlXGridObject.prototype.enableMathSerialization=function(status){
 this._mathSerialization=convertStringToBoolean(status);
}

 
dhtmlXGridObject.prototype._calcSCL=function(cell){
 if(!cell._code)return "";
 try{
 var agrid=this;
 if(!this._roundD){
 var z=eval(cell._code);
 return z;}
 else
{
 var z=eval(cell._code);
 z=z.toString().split(".");
 return(z[0]+"."+((z[1]||"0")+"000000000").substring(0,this._roundD));
}
}
 catch(e){
 return("#SCL");
}
}

dhtmlXGridObject.prototype._countTotal=function(row,cel){
 var a=this.loadedKidsHash.get(row);
 if(!a)return;
 var b=0;
 for(var i=0;i<a.length;i++)
 b=b*1+this.cells3(a[i],cel).getValue()*1;
 return b;
}

 
dhtmlXGridObject.prototype._compileSCL=function(code,cell){
 if(!code)return "";
 if(code.indexOf("=")!=0){
 this._reLink(new Array(),cell);
 return code;
}
 code=code.replace("=","");
 if(code.indexOf("sum")!=-1){
 code=code.replace("sum","(agrid._countTotal('"+cell.parentNode.idd+"',"+cell._cellIndex+"))");
 
 cell.parentNode._sumArr=true;
 cell._sumArr=true;
 return code;
}
 if(code.indexOf("[[")!=-1){
 var test = /(\[\[([^\,]*)\,([^\]]*)]\])/g;
 var agrid=this;
 var linked=new Array();
 code=code.replace(test,
 function($0,$1,$2,$3){
 if($2=="-")
 $2=cell.parentNode.idd;
 if($2.indexOf("#")==0)
 $2=agrid.getRowId($2.replace("#",""));
 linked[linked.length]=[$2,$3];
 return "(agrid.cells(\""+$2+"\","+$3+").getValue()*1)";
}
);
}
 else{
 var test = /c([0-9]+)/g;
 var agrid=this;
 var id=cell.parentNode.idd;
 var linked=new Array();
 code=code.replace(test,
 function($0,$1,$2,$3){
 linked[linked.length]=[id,$1];
 return "(agrid.cells(\""+id+"\","+$1+").getValue()*1)";
}
);
}
 this._reLink(linked,cell);
 return code;
}

 
dhtmlXGridObject.prototype._laterLink=function(){
 var a=window._SCL_later;
 window._SCL_later=new Array();
 window._SCL_later_timer=null;

 for(var i=0;i<a.length;i++){
(a[i][2])._reLink(a[i][0],a[i][1]);
 if((a[i][2])._strangeParams[a[i][1]._cellIndex]){
 var z=eval("new eXcell_"+(a[i][2])._strangeParams[a[i][1]._cellIndex]+"(a[i][1])");
 var val=(a[i][2])._calcSCL(a[i][1]);
 z.setValue(val==window.undefied?"":val);
}
 else
 a[i][1].innerHTML=(a[i][2])._calcSCL(a[i][1]);
(a[i][2])._checkSCL(a[i][1]);
}
}


 
dhtmlXGridObject.prototype._reLink=function(ar,cell){
 if(cell._alink){
 for(var i=0;i<cell._alink.length;i++)
(cell._alink[i][0])._SCL[cell._alink[i][1]]=null;
}

 cell._alink=new Array();
 for(var i=0;i<ar.length;i++)
{
 var a=this.getRowById(ar[i][0]);
 if(!a){
 if(!window._SCL_later)window._SCL_later=new Array();
 window._SCL_later.grid=this;
 window._SCL_later[window._SCL_later.length]=[ar,cell,this];
 if(!window._SCL_later_timer)
 window._SCL_later_timer=window.setTimeout(this._laterLink,300);
 return 0;
}
 var b=a.childNodes[ar[i][1]];
 if(!b._SCL)b._SCL=new Array();
 cell._alink[i]=[b,b._SCL.length];
 b._SCL[b._SCL.length]=cell;
}
}

if(_isKHTML){
 
(function(){
 var default_replace = String.prototype.replace;
 String.prototype.replace = function(search,replace){
 
 if(typeof replace != "function"){
 return default_replace.apply(this,arguments)
}
 var str = ""+this;
 var callback = replace;
 
 if(!(search instanceof RegExp)){
 var idx = str.indexOf(search);
 return(
 idx == -1 ? str :
 default_replace.apply(str,[search,callback(search,idx,str)])
)
}
 var reg = search;
 var result = [];
 var lastidx = reg.lastIndex;
 var re;
 while((re = reg.exec(str))!= null){
 var idx = re.index;
 var args = re.concat(idx,str);
 result.push(
 str.slice(lastidx,idx),
 callback.apply(null,args).toString()
);
 if(!reg.global){
 lastidx+= RegExp.lastMatch.length;
 break
}else{
 lastidx = reg.lastIndex;
}
}
 result.push(str.slice(lastidx));
 return result.join("")
}
})();
}

