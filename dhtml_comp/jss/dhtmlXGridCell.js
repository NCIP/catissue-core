/*
Copyright Scand LLC http://www.scbr.com
This version of Software is under GNU GPL. For non-GNU GPL usage please contact info@scbr.com to obtain Commercial/Enterprise license (Professional Edition included)
*/ 

 
function dhtmlXGridCellObject(obj){
 
 this.destructor=function(){
 this.cell.obj=null;
 this.cell=null;
 this.grid=null;
 this.base=null;
 return null;
}
 this.cell = obj;
 
 this.getValue = function(){
 if((this.cell.firstChild)&&(this.cell.firstChild.tagName=="TEXTAREA"))
 return this.obj.firstChild.value;
 else
 return this.cell.innerHTML._dhx_trim();
}

 
 this.getMathValue = function(){
 if(this.cell._val)
 return this.cell._val;
 else return this.getValue();
}
 
 this.getFont = function(){
 arOut = new Array(3);
 if(this.cell.style.fontFamily)
 arOut[0] = this.cell.style.fontFamily
 if(this.cell.style.fontWeight=='bold' || this.cell.parentNode.style.fontWeight=='bold')
 arOut[1] = 'bold';
 if(this.cell.style.fontStyle=='italic' || this.cell.parentNode.style.fontWeight=='italic')
 arOut[1]+= 'italic';
 if(this.cell.style.fontSize)
 arOut[2] = this.cell.style.fontSize
 else
 arOut[2] = "";
 return arOut.join("-")
}
 
 this.getTextColor = function(){
 if(this.cell.style.color)
 return this.cell.style.color
 else
 return "#000000";
}
 
 this.getBgColor = function(){
 if(this.cell.bgColor)
 return this.cell.bgColor
 else
 return "#FFFFFF";
}
 
 this.getHorAlign = function(){
 if(this.cell.style.textAlign)
 return this.cell.style.textAlign;
 else if(this.cell.align)
 return this.cell.align
 else
 return "left";
}
 
 this.getWidth = function(){
 return this.cell.scrollWidth;
}

 
 this.setFont = function(val){
 fntAr = val.split("-");
 this.cell.style.fontFamily = fntAr[0];
 this.cell.style.fontSize = fntAr[fntAr.length-1]
 if(fntAr.length==3){
 if(/bold/.test(fntAr[1]))
 this.cell.style.fontWeight = "bold";
 if(/italic/.test(fntAr[1]))
 this.cell.style.fontStyle = "italic";
 if(/underline/.test(fntAr[1]))
 this.cell.style.textDecoration = "underline";

}

}
 
 this.setTextColor = function(val){
 this.cell.style.color = val;
}
 
 this.setBgColor = function(val){
 if(val=="")
 val = null;
 this.cell.bgColor = val;
}
 
 this.setHorAlign = function(val){
 if(val.length==1){
 if(val=='c')
 this.cell.style.textAlign = 'center'
 else if(val=='l')
 this.cell.style.textAlign = 'left';
 else
 this.cell.style.textAlign = 'right';
}else
 this.cell.style.textAlign = val
}
 
 this.wasChanged = function(){
 if(this.cell.wasChanged)
 return true;
 else
 return false;
}
 
 this.isCheckbox = function(){
 var ch = this.cell.firstChild;
 if(ch && ch.tagName=='INPUT'){
 type = ch.type;
 if(type=='radio' || type=='checkbox')
 return true;
 else
 return false;
}else
 return false;
}
 
 this.isChecked = function(){
 if(this.isCheckbox()){
 return this.cell.firstChild.checked;
}
}
 
 this.isDisabled = function(){
 if(this.isCheckbox()){
 return this.cell.firstChild.disabled;
}
}
 
 this.setChecked = function(fl){
 if(this.isCheckbox()){
 if(fl!='true' && fl!=1)
 fl = false;
 this.cell.firstChild.checked = fl;
}
}
 
 this.setDisabled = function(fl){
 if(this.isCheckbox()){
 if(fl!='true' && fl!=1)
 fl = false;
 this.cell.firstChild.disabled = fl;
 if(this.disabledF)this.disabledF(fl);
}
}
}

 
dhtmlXGridCellObject.prototype.setValue = function(val){
 if((typeof(val)!="number")&& val.toString()._dhx_trim()==""){
 val="&nbsp;"
 this.cell._clearCell=true;
}
 this.cell.innerHTML = val;

}

 
dhtmlXGridCellObject.prototype.setLabel = function(val){
 this.cell.innerHTML = val;
}

 
dhtmlXGridCellObject.prototype.getMath = function(val){
 if(this._val)return this.val;
 else
 return this.getValue();
}

 
function eXcell(){
 this.obj = null;
 
 this.val = null;
 
 this.changeState = function(){return false}
 
 this.edit = function(){this.val = this.getValue()}
 
 this.detach = function(){return false}
 
 this.getPosition = function(oNode){
 var oCurrentNode=oNode;
 var iLeft=0;
 var iTop=0;
 while(oCurrentNode.tagName!="BODY"){
 iLeft+=oCurrentNode.offsetLeft;
 iTop+=oCurrentNode.offsetTop;
 oCurrentNode=oCurrentNode.offsetParent;
}
 return new Array(iLeft,iTop);
}
}
eXcell.prototype = new dhtmlXGridCellObject;

 
function eXcell_ed(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
}catch(er){}
 this.edit = function(){
 this.val = this.getValue();
 this.obj = document.createElement(_isKHTML?"INPUT":"TEXTAREA");
 this.obj.style.height =(this.cell.offsetHeight-(this.grid.multiLine?5:4))+"px";
 this.obj.className="dhx_combo_edit";
 this.obj.wrap = "soft";
 this.obj.style.textAlign = this.cell.align;
 this.obj.onclick = function(e){(e||event).cancelBubble = true}
 this.obj.value = this.val
 this.cell.innerHTML = "";
 this.cell.appendChild(this.obj);
 this.obj.onselectstart=function(e){if(!e)e=event;e.cancelBubble=true;return true;};
 this.obj.focus()
 this.obj.focus()
}
 this.getValue = function(){
 
 if((this.cell.firstChild)&&(this.cell.firstChild.tagName=="TEXTAREA"))
 return this.cell.firstChild.value;
 else
 return this.cell.innerHTML.toString()._dhx_trim();
}

 this.detach = function(){
 this.setValue(this.obj.value);
 return this.val!=this.getValue();
}

}
eXcell_ed.prototype = new eXcell;


 
function eXcell_ch(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.cell.obj = this;
}catch(er){}

 this.disabledF=function(fl){
 if((fl==true)||(fl==1))
 this.cell.innerHTML=this.cell.innerHTML.replace("item_chk0.","item_chk0_dis.").replace("item_chk1.","item_chk1_dis.");
 else
 this.cell.innerHTML=this.cell.innerHTML.replace("item_chk0_dis.","item_chk0.").replace("item_chk1_dis.","item_chk1.");
}

 this.changeState = function(){
 
 if(!this.grid.isEditable)return;
 
 if(typeof(this.grid.onEditCell)=="string"){
 if(eval(this.grid.onEditCell+"(0,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")!=false){
 this.val = this.getValue()
 if(this.val=="1")
 this.setValue("<checkbox state='false'>")
 else
 this.setValue("<checkbox state='true'>")
 
 eval(this.grid.onEditCell+"(1,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")
 if(this.grid.onCheckbox)this.grid.onCheckbox(this.cell.parentNode.idd,(this.val!='1'),this.cell._cellIndex);
}else{
 this.grid.editor=null;
}
}else{
 if(this.grid.onEditCell(0,this.cell.parentNode.idd,this.cell._cellIndex)!=false){
 this.val = this.getValue()
 if(this.val=="1")
 this.setValue("<checkbox state='false'>")
 else
 this.setValue("<checkbox state='true'>")
 
 this.grid.onEditCell(1,this.cell.parentNode.idd,this.cell._cellIndex)
 if(typeof(this.grid.onCheckbox)=='function')
 this.grid.onCheckbox(this.cell.parentNode.idd,this.cell._cellIndex,(this.val!='1'))
}else{
 this.editor=null;
}
}
 
 
 
}
 this.getValue = function(){
 try{
 return this.cell.chstate.toString();
}catch(er){
 return null;
}
}

 this.isCheckbox = function(){
 return true;
}
 this.isChecked = function(){
 if(this.getValue()=="1")
 return true;
 else
 return false;
}
 this.setChecked = function(fl){
 this.setValue(fl.toString())
}
 this.detach = function(){
 return this.val!=this.getValue();
}
}
eXcell_ch.prototype = new eXcell;
eXcell_ch.prototype.setValue = function(val){
 this.cell.style.verticalAlign = "middle";
 
 val=(val||"").toString();
 if(val.indexOf("1")!=-1 || val.indexOf("true")!=-1){
 val = "1";
 this.cell.chstate = "1";
}else{
 val = "0";
 this.cell.chstate = "0"
}
 var obj = this;
 this.cell.innerHTML = "<img src='"+this.grid.imgURL+"item_chk"+val+".gif' onclick='this.parentNode.obj.changeState()'>";
}

 
function eXcell_ra(cell){
 this.base = eXcell_ch;
 this.base(cell)
 this.grid = cell.parentNode.grid;

 this.disabledF=function(fl){
 if((fl==true)||(fl==1))
 this.cell.innerHTML=this.cell.innerHTML.replace("radio_chk0.","radio_chk0_dis.").replace("radio_chk1.","radio_chk1_dis.");
 else
 this.cell.innerHTML=this.cell.innerHTML.replace("radio_chk0_dis.","radio_chk0.").replace("radio_chk1_dis.","radio_chk1.");
}

 this.changeState = function(){
 if(!this.grid.isEditable)return;
 
 
 if(typeof(this.grid.onEditCell)=="string"){
 if(eval(this.grid.onEditCell+"(0,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")!=false){
 this.val = this.getValue()
 if(this.val=="1")
 this.setValue("<checkbox state='false'>")
 else
 this.setValue("<checkbox state='true'>")
 
 eval(this.grid.onEditCell+"(1,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")
 if(this.grid.onCheckbox)this.grid.onCheckbox(this.cell.parentNode.idd,(this.val!='1'),this.cell._cellIndex);
 for(var i=0;i<this.grid.getRowsNum();i++){
 if(this.grid.cells2(i,this.cell._cellIndex).isChecked()&& this.grid.cells2(i,this.cell._cellIndex).cell!=this.cell)
 this.grid.cells2(i,this.cell._cellIndex).setValue("<checkbox state='false'>")
}
}else{
 this.grid.editor=null;
}
}else{
 if(this.grid.onEditCell(0,this.cell.parentNode.idd,this.cell._cellIndex)!=false){
 this.val = this.getValue()
 if(this.val=="1")
 this.setValue("<checkbox state='false'>")
 else
 this.setValue("<checkbox state='true'>")
 
 this.grid.onEditCell(1,this.cell.parentNode.idd,this.cell._cellIndex)
 if(typeof(this.grid.onCheckbox)=='function')
 this.grid.onCheckbox(this.cell.parentNode.idd,this.cell._cellIndex,(this.val!='1'))
 for(var i=0;i<this.grid.getRowsNum();i++){
 if(this.grid.cells2(i,this.cell._cellIndex).isChecked()&& this.grid.cells2(i,this.cell._cellIndex).cell!=this.cell)
{
 this.grid.cells2(i,this.cell._cellIndex).setValue("<checkbox state='false'>")
 this.grid.onEditCell(1,this.grid.rowsCol[i].idd,this.cell._cellIndex);
}
}
}else{
 this.editor=null;
}
}
 
}

}
eXcell_ra.prototype = new eXcell_ch;
eXcell_ra.prototype.setValue = function(val){
 this.cell.style.verticalAlign = "middle";
 if((val||"").indexOf("1")!=-1 ||(val||"").indexOf("true")!=-1){
 val = "1";
 this.cell.chstate = "1";
}else{
 val = "0";
 this.cell.chstate = "0"
}
 var obj = this;
 this.cell.innerHTML = "<img src='"+this.grid.imgURL+"radio_chk"+val+".gif' onclick='this.parentNode.obj.changeState()'>";
}
 
function eXcell_txt(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
}catch(er){}
 this.edit = function(){
 this.val = this.getValue()
 this.obj = document.createElement("TEXTAREA");
 this.obj.className="dhx_textarea";

 this.obj.onclick = function(e){(e||event).cancelBubble = true}
 var arPos = this.grid.getPosition(this.cell);
 if(!this.cell._clearCell)
 this.obj.value = this.cell.innerHTML.replace(/<br[^>]*>/gi,"\n");
 
 
 document.body.appendChild(this.obj);
 this.obj.style.left = arPos[0]-this.grid.objBox.scrollLeft+"px";
 this.obj.style.top = arPos[1]+this.cell.offsetHeight-this.grid.objBox.scrollTop+"px";
 if(this.cell.scrollWidth<200)
 this.obj.style.width = "200px";
 else
 this.obj.style.width = this.cell.scrollWidth+"px";
 this.obj.style.display = "";
 this.obj.style.textAlign = this.cell.align;
 this.obj.focus();
 this.obj.focus()
}
 this.detach = function(){
 if(this.obj.value==""){
 this.cell._clearCell=true;
}
 else this.cell._clearCell=false;
 this.setValue(this.obj.value);
 
 document.body.removeChild(this.obj);

 return this.val!=this.getValue();
}
 this.getValue = function(){
 if((this.cell.firstChild)&&(this.cell.firstChild.tagName=="TEXTAREA"))
 return this.obj.firstChild.value;
 else
 return this.cell.innerHTML.replace(/<br[^>]*>/gi,"\n")._dhx_trim();
}
}
eXcell_txt.prototype = new eXcell;

eXcell_txt.prototype.setValue = function(val){
 if(!val || val.toString()._dhx_trim()==""){
 val="&nbsp;"
 this.cell._clearCell=true;
}
 this.cell.innerHTML = val.replace(/\n/g,"<br/>");
}


 
function eXcell_co(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.combo = this.grid.getCombo(this.cell._cellIndex);
 this.editable = true
}catch(er){}
 this.shiftNext=function(){

 var z=this.list.options[this.list.selectedIndex+1];
 if(z)z.selected=true;
 this.obj.value=this.list.value;

 return true;
}
 this.shiftPrev=function(){

 var z=this.list.options[this.list.selectedIndex-1];
 if(z)z.selected=true;

 this.obj.value=this.list.value;
 
 return true;
}

 this.edit = function(){
 this.val = this.getValue();
 this.text = this.cell.innerHTML._dhx_trim();
 var arPos = this.grid.getPosition(this.cell)

 this.obj = document.createElement("TEXTAREA");
 this.obj.className="dhx_combo_edit";
 this.obj.style.height=(this.cell.offsetHeight-4)+"px";

 this.obj.wrap = "soft";
 this.obj.style.textAlign = this.cell.align;
 this.obj.onclick = function(e){(e||event).cancelBubble = true}
 this.obj.value = this.text

 this.list = document.createElement("SELECT");
 this.list.editor_obj = this;
 this.list.className='dhx_combo_select';
 this.list.style.width=this.cell.offsetWidth+"px";
 this.list.style.left = arPos[0]-this.grid.objBox.scrollLeft+"px";
 this.list.style.top = arPos[1]+this.cell.offsetHeight-this.grid.objBox.scrollTop+"px";
 this.list.size="6";
 this.list.onclick = function(e){
 var ev = e||window.event;
 var cell = ev.target||ev.srcElement
 
 if(cell.tagName=="OPTION")cell=cell.parentNode;
 cell.editor_obj.setValue(cell.value);
 cell.editor_obj.editable=false;
 cell.editor_obj.detach();
}
 var comboKeys = this.combo.getKeys();
 var fl=false
 var selOptId=0;
 for(var i=0;i<comboKeys.length;i++){
 var val = this.combo.get(comboKeys[i])
 this.list.options[this.list.options.length]=new Option(val,comboKeys[i]);
 if(comboKeys[i]==this.val){
 selOptId=this.list.options.length-1;
 fl=true;
}
}

 if(fl==false){
 this.list.options[this.list.options.length]=new Option(this.text,this.val===null?"":this.val);
 selOptId=this.list.options.length-1;
}
 document.body.appendChild(this.list)
 this.cstate=1;
 if(this.editable){
 this.cell.innerHTML = "";
}
 else{
 this.obj.style.width="1px";
 this.obj.style.height="1px";
}
 this.cell.appendChild(this.obj);
 this.list.options[selOptId].selected=true;
 this.obj.focus();
 this.obj.focus();
 if(!this.editable)
 this.obj.style.visibility="hidden";
}

 this.getValue = function(){
 return((this.cell.combo_value==window.undefined)?"":this.cell.combo_value);
}
 this.getText = function(){
 return this.cell.innerHTML;
}
 this.detach = function(){
 if(this.val!=this.getValue()){
 this.cell.wasChanged = true;
}

 if(this.list.parentNode!=null){
 if(this.editable)
 if(this.obj.value._dhx_trim()!=this.text){
 this.setValue(this.obj.value)
}else{
 this.setValue(this.val)
}
 else
 this.setValue(this.list.value)
}
 
 if(typeof(this.grid.onEditCell)=="string")
 eval(this.grid.onEditCell+"(2,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")
 else if(typeof(this.grid.onEditCell)=='function'){
 this.grid.onEditCell(2,this.cell.parentNode.idd,this.cell._cellIndex)
}
 
 if(this.list.parentNode)
 this.list.parentNode.removeChild(this.list);
 if(this.obj.parentNode)
 this.obj.parentNode.removeChild(this.obj);

 return this.val!=this.getValue();
}
}
eXcell_co.prototype = new eXcell;
eXcell_co.prototype.setValue = function(val){
 if((val||"").toString()._dhx_trim()=="")
 val=null

 if(val!==null)
 this.cell.innerHTML = this.grid.getCombo(this.cell._cellIndex).get(val)|| val;
 else
 this.cell.innerHTML="&nbsp;";

 this.cell.combo_value = val;
}
 
function eXcell_coro(cell){
 this.base = eXcell_co;
 this.base(cell)
 this.editable = false;
}
eXcell_coro.prototype = new eXcell_co;

 
function eXcell_cp(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
}catch(er){}
 this.edit = function(){
 this.val = this.getValue()
 this.obj = document.createElement("SPAN");
 this.obj.style.border = "1px solid black";
 this.obj.style.position = "absolute";
 var arPos = this.grid.getPosition(this.cell);
 this.colorPanel(4,this.obj)
 document.body.appendChild(this.obj);
 this.obj.style.left = arPos[0]-this.grid.objBox.scrollLeft+"px";
 this.obj.style.top = arPos[1]+this.cell.offsetHeight-this.grid.objBox.scrollTop+"px";
}
 this.toolDNum = function(value){
 if(value.length==1)
 value = '0'+value;
 return value;
}
 this.colorPanel = function(index,parent){
 var tbl = document.createElement("TABLE");
 parent.appendChild(tbl)
 tbl.cellSpacing = 0;
 tbl.editor_obj = this;
 tbl.style.cursor = "default";
 tbl.style.cursor = "table-layout:fixed";
 tbl.onclick = function(e){
 var ev = e||window.event
 var cell = ev.target||ev.srcElement;
 var ed = cell.parentNode.parentNode.parentNode.editor_obj
 ed.setValue(cell.style.backgroundColor)
 ed.detach()
}
 var cnt = 256/index;
 for(var j=0;j<=(256/cnt);j++){
 var r = tbl.insertRow(j);
 for(var i=0;i<=(256/cnt);i++){
 for(var n=0;n<=(256/cnt);n++){
 R = new Number(cnt*j)-(j==0?0:1)
 G = new Number(cnt*i)-(i==0?0:1)
 B = new Number(cnt*n)-(n==0?0:1)
 var rgb = this.toolDNum(R.toString(16))+""+this.toolDNum(G.toString(16))+""+this.toolDNum(B.toString(16));
 var c = r.insertCell(i);
 c.width = "10px";
 c.innerHTML = "&nbsp;";
 c.title = rgb.toUpperCase()
 c.style.backgroundColor = "#"+rgb
 if(this.val!=null && "#"+rgb.toUpperCase()==this.val.toUpperCase()){
 c.style.border = "2px solid white"
}
}
}
}
}
 this.getValue = function(){
 return this.cell.firstChild.style?this.cell.firstChild.style.backgroundColor:"";
}
 this.getRed = function(){
 return Number(parseInt(this.getValue().substr(1,2),16))
}
 this.getGreen = function(){
 return Number(parseInt(this.getValue().substr(3,2),16))
}
 this.getBlue = function(){
 return Number(parseInt(this.getValue().substr(5,2),16))
}
 this.detach = function(){
 
 if(typeof(this.grid.onEditCell)=="string")
 eval(this.grid.onEditCell+"(2,'"+this.cell.parentNode.idd+"',"+this.cell._cellIndex+");")
 else{
 this.grid.onEditCell(2,this.cell.parentNode.idd,this.cell._cellIndex)
}
 if(this.obj.offsetParent!=null)
 document.body.removeChild(this.obj);
 
 return this.val!=this.getValue();
}
}
eXcell_cp.prototype = new eXcell;
eXcell_cp.prototype.setValue = function(val){
 this.cell.innerHTML = "<div style='width:100%;height:"+(this.cell.offsetHeight-2)+";background-color:"+(val||"")+";border:0px;'>&nbsp;</div>";
}


 
 
function eXcell_img(cell){
 try{
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
}catch(er){}
 this.getValue = function(){
 if(this.cell.firstChild.tagName=="IMG")
 return this.cell.firstChild.src+(this.cell.titFl!=null?"^"+this.cell.tit:"");
 else if(this.cell.firstChild.tagName=="A"){
 var out = this.cell.firstChild.firstChild.src+(this.cell.titFl!=null?"^"+this.cell.tit:"");
 out+="^"+this.cell.lnk;
 if(this.cell.trg)
 out+="^"+this.cell.trg
 return out;
}
}
 this.getTitle = function(){
 return this.cell.tit
}
}
eXcell_img.prototype = new eXcell;
eXcell_img.prototype.setValue = function(val){
 var title = val;
 if(val.indexOf("^")!=-1){
 var ar = val.split("^");
 val = ar[0]
 title = ar[1];
 
 if(ar.length>2){
 this.cell.lnk = ar[2]
 if(ar[3])
 this.cell.trg = ar[3]
}
 this.cell.titFl = "1";
}
 this.cell.innerHTML = "<img src='"+(val||"")._dhx_trim()+"' border='0'>";
 if(this.cell.lnk){
 this.cell.innerHTML = "<a href='"+this.cell.lnk+"' target='"+this.cell.trg+"'>"+this.cell.innerHTML+"</a>"
}
 this.cell.tit = title;
}

 
function eXcell_price(cell){
 this.base = eXcell_ed;
 this.base(cell)
 this.getValue = function(){
 if(this.cell.childNodes.length>1)
 return this.cell.childNodes[1].innerHTML.toString()._dhx_trim()
 else
 return "0";
}
}
eXcell_price.prototype = new eXcell_ed;
eXcell_price.prototype.setValue = function(val){
 if(isNaN(Number(val))){
 if(!(val||"")||(val||"")._dhx_trim()!="")
 val = 0;
 val = this.val || 0;
}
 if(val>0){
 var color = "green";
 this.cell.innerHTML = "<span>$</span><span style='padding-right:2px;color:"+color+";'>"+val+"</span>";
}else{
 this.cell.innerHTML = "<div align='center' style='color:red;'>&nbsp;</div>";
}

}

 
function eXcell_dyn(cell){
 this.base = eXcell_ed;
 this.base(cell)
 this.getValue = function(){
 return this.cell.firstChild.childNodes[1].innerHTML.toString()._dhx_trim()
}

}

eXcell_dyn.prototype = new eXcell_ed;
eXcell_dyn.prototype.setValue = function(val){
 if(!val || isNaN(Number(val))){
 val = 0;
}
 if(val>0){
 var color = "green";
 var img = "dyn_up.gif";
}else if(val==0){
 var color = "black";
 var img = "dyn_.gif";
}else{
 var color = "red";
 var img = "dyn_down.gif";
}
 this.cell.innerHTML = "<div style='position:relative;padding-right:2px;width:100%;'><img src='"+this.grid.imgURL+""+img+"' height='15' style='position:absolute;top:0px;left:0px;'><span style='width:100%;color:"+color+";'>"+val+"</span></div>";
}

 

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
 
 this.cell.innerHTML = "<a "+valsAr[1]+" onclick='(isIE()?event:arguments[0]).cancelBubble = true;'>"+valsAr[0]+"</a>"
}
}

eXcell_link.prototype = new eXcell;
eXcell_link.prototype.getTitle=function(){
 return this.cell.firstChild.getAttribute("href");
}

 
function eXcell_ro(cell){
 this.cell = cell;
 this.grid = this.cell.parentNode.grid;
 this.edit = function(){}
}
eXcell_ro.prototype = new eXcell;


 
function dhtmlXGridComboObject(){
 this.keys = new Array();
 this.values = new Array();
 
 this.put = function(key,value){
 for(var i=0;i<this.keys.length;i++){
 if(this.keys[i]==key){
 this.values[i]=value;
 return true;
}
}
 this.values[this.values.length] = value;
 this.keys[this.keys.length] = key;
}
 
 this.get = function(key){
 for(var i=0;i<this.keys.length;i++){
 if(this.keys[i]==key){
 return this.values[i];
}
}
 return null;
}
 
 this.clear = function(){
 
 this.keys = new Array();
 this.values = new Array();
}
 
 this.remove = function(key){
 for(var i=0;i<this.keys.length;i++){
 if(this.keys[i]==key){
 this.keys._dhx_removeAt(i);
 this.values._dhx_removeAt(i);
 return true;
}
}
}
 
 this.size = function(){
 var j=0;
 for(var i=0;i<this.keys.length;i++){
 if(this.keys[i]!=null)
 j++;
}
 return j;
}
 
 this.getKeys = function(){
 var keyAr = new Array(0);
 for(var i=0;i<this.keys.length;i++){
 if(this.keys[i]!=null)
 keyAr[keyAr.length] = this.keys[i];
}
 return keyAr;
}

 
 this.save = function(){
 this._save=new Array();
 for(var i=0;i<this.keys.length;i++)
 this._save[i]=[this.keys[i],this.values[i]];
}


 
 this.restore = function(){
 if(this._save){
 this.keys[i]=new Array();
 this.values[i]=new Array();
 for(var i=0;i<this._save.length;i++){
 this.keys[i]=this._save[i][0];
 this.values[i]=this._save[i][1];
}
}
}
 return this;
}
function Hashtable(){
 this.keys = new Array();
 this.values = new Array();
 return this;
}
Hashtable.prototype = new dhtmlXGridComboObject;





