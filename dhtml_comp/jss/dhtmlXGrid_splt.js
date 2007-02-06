/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
dhtmlXGridObject.prototype.splitAt=function(ind){
 var z=document.createElement("DIV");
 this.entBox.appendChild(z);
 this.entBox.style.position="relative";
 
 this._fake=new dhtmlXGridObject(z);
 this._fake._fake=this;
 this._fake._realfake=true;
 this._fake.imgURL=this.imgURL;
 this._fake.fldSort=new Array();
 this._fake.loadedKidsHash=this.loadedKidsHash;

 var b_ha=[[],[],[],[],[],[],[]];
 var b_ar=["hdrLabels","initCellWidth","cellType","cellAlign","cellVAlign","fldSort","columnColor"];
 var b_fu=["setHeader","setInitWidths","setColTypes","setColAlign","setColVAlign","setColSorting","setColumnColor"];
 var ev_fu=["onCheckbox","onEditCell","onCLMS","onRowDblClicked","onHeaderClick"];

 for(var i=0;i<ev_fu.length;i++)
 this._fake[ev_fu[i]]=this[ev_fu[i]];
 if(this._elmn)
 this._fake.enableLightMouseNavigation(true);

 var width=0;

 for(var i=0;i<ind;i++){
 for(var j=0;j<b_ar.length;j++){
 if(this[b_ar[j]])
 b_ha[j][i]=this[b_ar[j]][i];
}
 if(_isFF)b_ha[1][i]+=2;
 width+=parseInt(this[b_ar[1]][i]);
 this.setColumnHidden(i,true);
}
 


 for(var j=0;j<b_ar.length;j++){
 var str=b_ha[j].join(",");
 if(str!="");
 this._fake[b_fu[j]](str);
}



 
 var pa=this.entBox.childNodes[0];
 pa.style.left=width+"px";
 pa.style.top=0+"px";
 pa.style.position="absolute";
 pa.style.width=this.entBox.offsetWidth-width-(_isFF?0:(this.entBox.offsetWidth-this.entBox.clientWidth))+"px";

 z.style.width=width+"px";
 z.style.height=this.objBuf.offsetHeight;
 z.style.position="absolute";
 z.style.top="0px";
 z.style.left="0px";
 z.style.zIndex=11;

 if(this._ecspn)this._fake._ecspn=true;

 
 this._fake.init();
 this._fake.objBox.style.overflow="hidden";
 this._fake.objBox.style.overflowX="scroll";
 this._fake._srdh=this._srdh||20;


 
 


 var zname="cells";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 if(arguments[1]<ind)
 return this._fake["cells"].apply(this._fake,arguments);
 else
 return this["_bfs_"+"cells"].apply(this,arguments);
}

 var zname="cells2";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 if(arguments[1]<ind)
 return this._fake["cells2"].apply(this._fake,arguments);
 else
 return this["_bfs_"+"cells2"].apply(this,arguments);
}

 var zname="cells3";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 if(arguments[1]<ind){
 arguments[0]=arguments[0].idd;
 return this._fake["cells"].apply(this._fake,arguments);
}
 else
 return this["_bfs_"+"cells3"].apply(this,arguments);
}

 

 var zname="changeRowId";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 this["_bfs_changeRowId"].apply(this,arguments);
 this._fake["changeRowId"].apply(this._fake,arguments);
}

 if(this.collapseKids){
 
 this._fake["_bfs_collapseKids"]=this.collapseKids;
 this._fake["collapseKids"]=function(){
 this.loadedKidsHash=this._fake.loadedKidsHash;
 var z=this._fake["collapseKids"].apply(this._fake,arguments);
 this._fake.rowsAr[arguments[0].idd].expand=this.rowsAr[arguments[0].idd].expand;
 return z;
}
 this._fake["_bfs_expandKids"]=this.expandKids;
 this._fake["expandKids"]=function(){
 this.loadedKidsHash=this._fake.loadedKidsHash;
 var z=this._fake["expandKids"].apply(this._fake,arguments);
 this._fake.rowsAr[arguments[0].idd].expand=this.rowsAr[arguments[0].idd].expand;
 return z;
}
 this._fake["_removeTrGrRow"]=function(){
 
 
 return;
}

 this._fake._collapseFake=function(row,i){
 var frow=this.rowsAr[row.idd];
 frow.parentNode.removeChild(frow);
 this.rowsCol._dhx_removeAt(i)
 for(var i=0;i<ind;i++){
 row.childNodes[i].innerHTML=frow.childNodes[i].innerHTML;
}
}
 this._fake._expandCorrect=function(row){
 var curRow=this.rowsAr[row.idd];
 curRow.expand="";
 var treeCell = curRow.childNodes[this.cellType._dhx_find("tree")];
 treeCell.innerHTML = treeCell.innerHTML.replace(/\/(plus|blank)\.gif/,"/minus.gif");
}


 this._sortTreeRowsA = this._fake._sortTreeRowsA = this._sortTreeRows;
 this._fake._sortTreeRows = this._sortTreeRows = function(){
 this._sortTreeRowsA.apply(this,arguments);
 this._fake._sortTreeRowsA.apply(this._fake,arguments);
 this._fake.setSortImgState(false);
}


 


}

 var zname="_insertRowAt";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 var x=arguments[0].cloneNode(true);
 while(x.childNodes.length>ind)
 x.removeChild(x.childNodes[x.childNodes.length-1]);
 var z=0;
 var zm=ind;
 for(var i=0;i<zm;i++){
 x.childNodes[i].style.display="";
 x.childNodes[i]._cellIndex=i;
 x.childNodes[i].combo_value=arguments[0].childNodes[i].combo_value;
 x.childNodes[i]._clearCell=arguments[0].childNodes[i]._clearCell;

 if(x.childNodes[i].colSpan>1){
 this._childIndexes=this._fake._childIndexes;
 for(var z=1;z<x.childNodes[i].colSpan;z++)
 x.removeChild(x.childNodes[i+z]);
 zm--;
}
}

 var r=this["_bfs__insertRowAt"].apply(this,arguments);
 x.idd=arguments[0].idd;
 x.grid=this._fake;
 arguments[0]=x;

 var r2=this._fake["_insertRowAt"].apply(this._fake,arguments);
 if(r._fhd){
 r2.parentNode.removeChild(r2);
 this._fake.rowsCol._dhx_removeAt(this._fake.rowsCol._dhx_find(r2));
 r._fhd=false;
}
 return r;
}

 var zname="setSizes";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 this["_bfs_setSizes"].apply(this,arguments);
 z.style.height=this.entBox.offsetHeight+"px";
 if(this._fake.hdr.offsetHeight!=this.hdr.offsetHeight){
 this._fake.hdr.style.height=this.hdr.offsetHeight+"px";
 this._fake["setSizes"].apply(this._fake,arguments);
}

 if(((this.obj.offsetWidth+1*((this.objBox.offsetHeight<=this.objBox.scrollHeight)?(_isFF?20:18):0))<=this.objBox.offsetWidth)&&(this._fake.obj.offsetWidth<=this._fake.objBox.offsetWidth))
{
 this._fake.objBox.style.overflowX="hidden";
 this.objBox.style.overflowX="hidden";
}
 else
{
 this._fake.objBox.style.overflowX="scroll";
 this.objBox.style.overflowX="scroll";
}

 this._fake.objBox.scrollTop=this.objBox.scrollTop;
 this._fake["_bfs_setSizes"].apply(this._fake,arguments);
 this._fake.entCnt.rows[1].cells[0].childNodes[0].style.top = this.entCnt.rows[1].cells[0].childNodes[0].style.top;
}
 this._fake["_bfs_"+zname]=this._fake[zname];
 this._fake[zname]=function(){
 this["_bfs_setSizes"].apply(this,arguments);
 if(this._fake.hdr.offsetHeight!=this.hdr.offsetHeight){
 this._fake.hdr.style.height=this.hdr.offsetHeight+"px";
 this._fake["setSizes"].apply(this._fake,arguments);
}

 this.entCnt.rows[1].cells[0].childNodes[0].style.top = this._fake.entCnt.rows[1].cells[0].childNodes[0].style.top;

 if(((this._fake.obj.offsetWidth+1*((this._fake.objBox.offsetHeight<=this._fake.objBox.scrollHeight)?(_isFF?20:18):0))<=this._fake.objBox.offsetWidth)&&(this.obj.offsetWidth<=this.objBox.offsetWidth))
{
 this.objBox.style.overflowX="hidden";
 this._fake.objBox.style.overflowX="hidden";
}
 else{
 this.objBox.style.overflowX="scroll";
 this._fake.objBox.style.overflowX="scroll";
}


}

 var zname="_doOnScroll";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 this["_bfs__doOnScroll"].apply(this,arguments);
 this._fake.objBox.scrollTop=this.objBox.scrollTop;
 this._fake["_doOnScroll"].apply(this._fake,arguments);
}
 

 


 var zname="doClick";
 this["_bfs_"+zname]=this[zname];
 this[zname]=function(){
 this["_bfs_doClick"].apply(this,arguments);
 if(arguments[0].tagName=="TD"){
 var fl=(arguments[0]._cellIndex>=ind);
 arguments[0]=this._fake.rowsAr[arguments[0].parentNode.idd].childNodes[fl?0:arguments[0]._cellIndex];
 this._fake["_bfs_doClick"].apply(this._fake,arguments);
 if(fl){
 arguments[0].className="";
 globalActiveDHTMLGridObject=this;
}
 else{
 this.objBox.scrollTop=this._fake.objBox.scrollTop;
}
}
}
 this._fake["_bfs_"+zname]=this._fake[zname];
 this._fake[zname]=function(){
 this["_bfs_doClick"].apply(this,arguments);
 if(arguments[0].tagName=="TD"){
 var fl=(arguments[0]._cellIndex<ind);
 arguments[0]=this._fake.rowsAr[arguments[0].parentNode.idd].childNodes[fl?ind:arguments[0]._cellIndex];
 this._fake["_bfs_doClick"].apply(this._fake,arguments);
 if(fl){
 arguments[0].className="";
 globalActiveDHTMLGridObject=this;
}
}
}


this.moveRowUpA = this.moveRowUp;
this.moveRowUp = function(row_id){
 this._fake.moveRowUp(row_id);
 this.moveRowUpA(row_id);
}
this.moveRowDownA = this.moveRowDown;
this.moveRowDown = function(row_id){
 this._fake.moveRowDown(row_id);
 this.moveRowDownA(row_id);
}



this._fake.doColResizeA = this._fake.doColResize;
this._fake.doColResize = function(){
 a=-1;
 var z=this.doColResizeA.apply(this,arguments);
 if(arguments[1]._cellIndex==(ind-1))
 a=this.obj.scrollWidth-this.objBox.scrollLeft;
 else
 if(this.obj.offsetWidth<this.entBox.offsetWidth)
 a=this.obj.offsetWidth;

 if(a>-1)
{
 this.entBox.style.width=a;
 this.objBuf.style.width=a;
 var pa=this._fake.entBox.childNodes[0];
 pa.style.left=a-(_isFF?2:0)+"px";
 pa.style.width=this._fake.entBox.offsetWidth-a+"px";
}
 return z;
}

 this._fake.startColResizeA = this._fake.startColResize;
 this._fake.startColResize = function(ev){
 var z=this.startColResizeA(ev);
 if(this.entBox.onmousemove){
 var m=this.entBox.parentNode;m._aggrid=m.grid;m.grid=this;
 this.entBox.parentNode.onmousemove=this.entBox.onmousemove;
 this.entBox.onmousemove=null;
}
 return z;
}

 this._fake.stopColResizeA = this._fake.stopColResize;
 this._fake.stopColResize = function(ev){
 if(this.entBox.parentNode.onmousemove){
 var m=this.entBox.parentNode;m.grid=m._aggrid;m._aggrid=null;
 this.entBox.onmousemove=this.entBox.parentNode.onmousemove;
 this.entBox.parentNode.onmousemove=null;
}
 return this.stopColResizeA(ev);
}



this.doKeyA = this.doKey;
this._fake.doKeyA = this._fake.doKey;
this._fake.doKey=this.doKey=function(ev){
 if(!ev)return true;
 if(this._htkebl)return true;
 switch(ev.keyCode){
 case 9:
 if(!ev.shiftKey){
 if(this._realfake){
 if((this.cell)&&(this.cell._cellIndex==(ind-1))){
 if(ev.preventDefault)
 ev.preventDefault();
 this._fake.selectCell(this.rowsCol._dhx_find(this.cell.parentNode),ind,false,false,true);
 return false;
}
 else
 var z=this.doKeyA(ev);
 globalActiveDHTMLGridObject=this;
 return z;
}
 else{
 if((this.cell)&&(this.cell._cellIndex==(this.rowsCol[0].childNodes.length-1))){
 if(ev.preventDefault)
 ev.preventDefault();
 var z=this._fake.rowsCol[this.rowsCol._dhx_find(this.cell.parentNode)+1];
 if(z)this._fake.selectCell(z,0,false,false,true);
 return false;
}
 else
 return this.doKeyA(ev);
}
}
 else{
 if(this._realfake){
 if((this.cell)&&(this.cell._cellIndex==0)){
 if(ev.preventDefault)
 ev.preventDefault();
 var z=this._fake.rowsCol[this.rowsCol._dhx_find(this.cell.parentNode)-1];
 if(z)this._fake.selectCell(z,this._fake.rowsCol[0].childNodes.length-1,false,false,true);
 return false;
}
 else
 return this.doKeyA(ev);
}
 else{
 if((this.cell)&&(this.cell._cellIndex==ind)){
 if(ev.preventDefault)
 ev.preventDefault();
 this._fake.selectCell(this.rowsCol._dhx_find(this.cell.parentNode),ind-1,false,false,true);
 return false;
}
 else
 return this.doKeyA(ev);
}
}
 break;
}

 return this.doKeyA(ev);
}

this.deleteRowA = this.deleteRow;
this.deleteRow=function(row_id,node){
 
 this.deleteRowA(row_id,node);
 this._fake.deleteRow(row_id);
}

this.clearAllA = this.clearAll;
this.clearAll=function(){
 this.clearAllA();
 this._fake.clearAll();
}

this._sortRowsA = this._sortRows;
this._fake._sortRowsA = this._fake._sortRows;
this._sortRows=this._fake._sortRows=function(col,type,order,ar){
 this._sortRowsA(col,type,order,ar);
 this._fake._sortRowsA(col,type,order,ar);
 this._fake.setSortImgState(false);
}

if(this.changePage){
 



 this.changePageA=this.changePage;
 this.changePage=function(pageNum){

 this.changePageA(pageNum);
 var zsize=this.rowsBufferOutSize;
 var startRowInd = this.currentPage*zsize - zsize

 
 var totalRows = this._fake.obj._rowslength();

 for(var i=0;i<totalRows;i++){
 var tmpPar = this._fake.obj._rows(0).parentNode
 tmpPar.removeChild(this._fake.obj._rows(0));
}

 for(var i=startRowInd;i<(startRowInd+zsize);i++){
 var row = this.getRowFromCollection(i);
 if(!row)continue;
 if(!this._fake.rowsAr[row.idd])
{
 var x=row.cloneNode(true);
 while(x.childNodes.length>ind)
 x.removeChild(x.childNodes[x.childNodes.length-1]);
 for(var j=0;j<ind;j++){
 x.childNodes[j].style.display="";
 x.childNodes[j]._cellIndex=j;
}

 x.idd=this.obj._rows(i).idd;
 x.grid=this._fake;
 this._fake["_insertRowAt"](x,-1);
}

 this._fake.obj.rows[0].parentNode.appendChild(this._fake.rowsAr[row.idd]);
}
}


 if(this.pagingOn){
 
 this._sortRows=function(){
 this._sortRowsA.apply(this,arguments);
 this._fake.setSortImgState(false);
}
 this._fake.sortRows=function(){
 this._fake.sortRows.apply(this._fake,arguments);
 this._fake.setSortImgState(false);
}
}

}





if(this._fastAddRowSpacer){
 this._fastAddRowSpacerA=this._fastAddRowSpacer;
 this._fastAddRowSpacer=function(ind,height){
 this._fake._fastAddRowSpacer(ind,height);
 return this._fastAddRowSpacerA(ind,height);
}


 this._splitRowAtA=this._splitRowAt;
 this._splitRowAt=function(){
 this._fake._splitRowAt.apply(this._fake,arguments);
 this._splitRowAt=this._splitRowAtA;
 var z=this._splitRowAtA.apply(this,arguments);
 this._splitRowAt=this._splitRowAtB;
 return z;
}
 this._splitRowAtB=this._splitRowAt;

 this._fake._askRealRows=function(){};

 this.loadXMLA=this.loadXML;
 this.loadXML=function(){
 if(this._dload){
 this._fake._dload = true;
 this._fake._dInc=12;
 this._fake._dl_start=new Array();
 this._fake.limit=this.limit;

 this._fake.multiLine=false;
 this._fake._dloadSize=Math.floor(parseInt(this.entBox.style.height)/20)+2;
 this._fake.obj.className+=" row20px";
 this._fake._dpref=this._dpref;
 this._dom_limit=this._fake._dom_limit=999999999;
}
 return this.loadXMLA.apply(this,arguments);
}

 this._addFromBufferSR=function(j){
 if((!this.rowsCol[j])||(this.rowsCol[j]._sRow))
 this._splitRowAt(j);

 if(this.rowsBuffer[1][j].tagName=="row"){
 var cellsCol = this.xmlLoader.doXPath("./cell",this.rowsBuffer[1][j]);
 for(var k=0;k<cellsCol.length;k++){
 this.cells3(this.rowsCol[j],k).setValue(cellsCol[k].firstChild?cellsCol[k].firstChild.data:"");
}
 this.changeRowId(this.rowsCol[j].idd,this.rowsBuffer[1][j].getAttribute("id"));
 this._fake.changeRowId(this._fake.rowsCol[j].idd,this.rowsBuffer[1][j].getAttribute("id"));
}

 this.rowsCol[j]._rLoad=false;
 this._fake.rowsCol[j]._rLoad=false;
 this.rowsBuffer[1][j]=null;
}
}


 this._fake.combos=this.combos;
 this.setSizes();
}


