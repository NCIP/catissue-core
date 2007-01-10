/*
Copyright Scand LLC http://www.scbr.com
To use this component not under GNU GPL please contact info@scbr.com to obtain license (Professional Edition included)

*/ 

 

var globalActiveDHTMLGridObject;
String.prototype._dhx_trim = function(){
 return this.replace(/&nbsp;/g," ").replace(/(^[ \t]*)|([ \t]*$)/g,"");
}
Array.prototype._dhx_find = function(pattern){
 for(var i=0;i<this.length;i++){
 if(pattern==this[i])
 return i;
}
 return -1;
}
Array.prototype._dhx_delAt = function(ind){
 if(Number(ind)<0 || this.length==0)
 return false;
 for(var i=ind;i<this.length;i++){
 this[i]=this[i+1];
}
 this.length--;
}
Array.prototype._dhx_insertAt = function(ind,value){
 this[this.length] = null;
 for(var i=this.length-1;i>=ind;i--){
 this[i] = this[i-1]
}
 this[ind] = value
}
Array.prototype._dhx_removeAt = function(ind){
 for(var i=ind;i<this.length;i++){
 this[i] = this[i+1]
}
 this.length--;
}

Array.prototype._dhx_swapItems = function(ind1,ind2){
 var tmp = this[ind1];
 this[ind1] = this[ind2]
 this[ind2] = tmp;
}

 
function dhtmlXGridObject(id){
 if(id){
 if(typeof(id)=='object'){
 this.entBox = id
 this.entBox.id = "cgrid2_"+(new Date()).getTime();
}else
 this.entBox = document.getElementById(id);
}else{
 this.entBox = document.createElement("DIV");
 this.entBox.id = "cgrid2_"+(new Date()).getTime();
}


 this._tttag=this._tttag||"rows";
 this._cttag=this._cttag||"cell";
 this._rttag=this._rttag||"row";

 var self = this;

 this._wcorr=0;
 this.nm = this.entBox.nm || "grid";
 this.cell = null;
 this.row = null;
 this.editor=null;
 this._f2kE=true;this._dclE=true;
 this.combos=new Array(0);
 this.defVal=new Array(0);
 this.rowsAr = new Array(0);
 this.rowsCol = new Array(0);
 
 this._maskArr=new Array(0);
 this.selectedRows = new Array(0);
 this.rowsBuffer = new Array(new Array(0),new Array(0));
 this.loadedKidsHash = null;
 this.UserData = new Array(0)

 

 this.styleSheet = document.styleSheets;
 this.entBox.className = "gridbox";
 this.entBox.style.width = this.entBox.getAttribute("width")||(window.getComputedStyle?window.getComputedStyle(this.entBox,null)["width"]:(this.entBox.currentStyle?this.entBox.currentStyle["width"]:0))|| "100%";
 this.entBox.style.height = this.entBox.getAttribute("height")||(window.getComputedStyle?window.getComputedStyle(this.entBox,null)["height"]:(this.entBox.currentStyle?this.entBox.currentStyle["height"]:0))|| "100%";

 
 this.entBox.style.cursor = 'default';
 this.entBox.onselectstart = function(){return false};
 this.obj = document.createElement("TABLE");
 this.obj.cellSpacing = 0;
 this.obj.cellPadding = 0;
 this.obj.style.width = "100%";
 this.obj.style.tableLayout = "fixed";
 this.obj.className = "obj";

 this.obj._rows=function(i){return this.rows[i+1];}
 this.obj._rowslength=function(){return this.rows.length-1;}

 this.hdr = document.createElement("TABLE");
 this.hdr.style.border="1px solid gray";
 this.hdr.cellSpacing = 0;
 this.hdr.cellPadding = 0;
 if((!_isOpera)||(_OperaRv>=9))
 this.hdr.style.tableLayout = "fixed";
 this.hdr.className = "hdr";
 this.hdr.width = "100%";

 this.xHdr = document.createElement("TABLE");
 this.xHdr.cellPadding = 0;
 this.xHdr.cellSpacing = 0;
 var r = this.xHdr.insertRow(0)
 var c = r.insertCell(0);
 r.insertCell(1).innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
 c.appendChild(this.hdr)
 this.objBuf = document.createElement("DIV");
 this.objBuf.style.borderBottom = "1px solid white"
 this.objBuf.appendChild(this.obj);
 this.entCnt = document.createElement("TABLE");
 this.entCnt.insertRow(0).insertCell(0)
 this.entCnt.insertRow(1).insertCell(0);

 this.entCnt.cellPadding = 0;
 this.entCnt.cellSpacing = 0;
 this.entCnt.width = "100%";
 this.entCnt.height = "100%";

 this.entCnt.style.tableLayout = "fixed";

 this.objBox = document.createElement("DIV");
 this.objBox.style.width = "100%";
 this.objBox.style.height = this.entBox.style.height;
 this.objBox.style.overflow = "auto";
 this.objBox.style.position = "relative";
 this.objBox.appendChild(this.objBuf);
 this.objBox.className = "objbox";


 this.hdrBox = document.createElement("DIV");
 this.hdrBox.style.width = "100%"
 if(((_isOpera)&&(_OperaRv<9))||((_isMacOS)&&(_isFF)))
 this.hdrSizeA=25;else this.hdrSizeA=100;

 this.hdrBox.style.height=this.hdrSizeA+"px";
 if(_isIE)
 this.hdrBox.style.overflowX="hidden";
 else
 this.hdrBox.style.overflow = "hidden";

 this.hdrBox.style.position = "relative";
 this.hdrBox.appendChild(this.xHdr);

 this.preloadImagesAr = new Array(0)

 this.sortImg = document.createElement("IMG")
 this.sortImg.style.display = "none";
 this.hdrBox.insertBefore(this.sortImg,this.xHdr)
 this.entCnt.rows[0].cells[0].vAlign="top";
 this.entCnt.rows[0].cells[0].appendChild(this.hdrBox);
 this.entCnt.rows[1].cells[0].appendChild(this.objBox);


 this.entBox.appendChild(this.entCnt);
 
 this.entBox.grid = this;
 this.objBox.grid = this;
 this.hdrBox.grid = this;
 this.obj.grid = this;
 this.hdr.grid = this;
 
 this.cellWidthPX = new Array(0);
 this.cellWidthPC = new Array(0);
 this.cellWidthType = this.entBox.cellwidthtype || "px";

 this.delim = this.entBox.delimiter || ",";
 this.hdrLabels =(this.entBox.hdrlabels || "").split(",");
 this.columnIds =(this.entBox.columnids || "").split(",");
 this.columnColor =(this.entBox.columncolor || "").split(",");
 this.cellType =(this.entBox.cellstype || "").split(",");
 this.cellAlign =(this.entBox.cellsalign || "").split(",");
 this.initCellWidth =(this.entBox.cellswidth || "").split(",");
 this.fldSort =(this.entBox.fieldstosort || "").split(",")
 this.imgURL = this.entBox.imagesurl || "gridCfx/";
 this.isActive = false;
 this.isEditable = true;
 this.raNoState = this.entBox.ranostate || "N";
 this.chNoState = this.entBox.chnostate || "N";
 this.selBasedOn =(this.entBox.selbasedon || "cell").toLowerCase()
 this.selMultiRows = this.entBox.selmultirows || false;
 this.multiLine = this.entBox.multiline || false;
 this.noHeader = this.entBox.noheader || false;
 this.dynScroll = this.entBox.dynscroll || false;
 this.dynScrollPageSize = 0;
 this.dynScrollPos = 0;
 this.xmlFileUrl = this.entBox.xmlfileurl || "";
 this.recordsNoMore = this.entBox.infinitloading || true;;
 this.useImagesInHeader = false;
 this.pagingOn = false;
 
 this.rowsBufferOutSize = 0;
 
 if(this.entBox.oncheckbox)
 this.onCheckbox = eval(this.entBox.oncheckbox);
 this.onEditCell = this.entBox.oneditcell || function(){return true;};
 this.onRowSelect = this.entBox.onrowselect || function(){return true;};
 this.onEnter = this.entBox.onenter || function(){return true;};

 if(window.addEventListener)window.addEventListener("unload",function(){try{self.destructor();}catch(e){}},false);
 if(window.attachEvent)window.attachEvent("onunload",function(){try{self.destructor();}catch(e){}});

 
 
 this.loadXML = function(url,afterCall){
 if(this._dload){this._dload=url;this._askRealRows();return true;};
 if(this._xmlaR)this.setXMLAutoLoading(url);


 if(url.indexOf("?")!=-1)
 var s = "&";
 else
 var s = "?";
 var obj = this;
 if(this.onXLS)this.onXLS(this);

 if(afterCall)this.XMLLoader.waitCall=afterCall;
 this.xmlLoader.loadXML(url+""+s+"rowsLoaded="+this.getRowsNum()+"&lastid="+this.getRowId(this.getRowsNum()-1)+"&sn="+Date.parse(new Date()));
 
 
 
}

 
 this.setSkin = function(name){
 this.entBox.className = "gridbox gridbox_"+name;
 switch(name){
 case "xp": this._srdh=22;break;
}
}


 
 this.doLoadDetails = function(obj){
 var root = self.xmlLoader.getXMLTopNode(self._tttag)
 if(root.tagName!="DIV")
 if(!self.xmlLoader.xmlDoc.nodeName){
 self.parseXML(self.xmlLoader.xmlDoc.responseXML)
}else{
 self.parseXML(self.xmlLoader.xmlDoc)
}
 
 if(self.pagingOn)
 self.createPagingBlock()
}
 this.xmlLoader = new dtmlXMLLoaderObject(this.doLoadDetails,window,true,this.no_cashe);
 this.dragger=new dhtmlDragAndDropObject();

 
 
 this._doOnScroll = function(e,mode){
 if(this._onSCRL)this._onSCRL(this.objBox.scrollLeft,this.objBox.scrollTop);
 this.doOnScroll(e,mode);
}
 
 this.doOnScroll = function(e,mode){
 this.hdrBox.scrollLeft = this.objBox.scrollLeft;
 this.setSortImgPos(null,true);
 if(mode)return;
 
 
 if(!this.pagingOn && this.objBox.scrollTop+this.hdrSizeA+this.objBox.offsetHeight>this.objBox.scrollHeight){
 if(this._xml_ready && this.addRowsFromBuffer())
 this.objBox.scrollTop = this.objBox.scrollHeight -(this.hdrSizeA+1+this.objBox.offsetHeight)
}

 if(this._dload){
 if(this._dLoadTimer)window.clearTimeout(this._dLoadTimer);
 this._dLoadTimer=window.setTimeout(function(){self._askRealRows();},500);
}

}
 
 this.attachToObject = function(obj){
 obj.appendChild(this.entBox)
 
}
 
 this.init = function(fl){

 this.editStop()
 
 this.lastClicked = null;
 this.resized = null;
 this.fldSorted = null;
 this.gridWidth = 0;
 this.gridHeight = 0;
 
 this.cellWidthPX = new Array(0);
 this.cellWidthPC = new Array(0);
 if(this.hdr.rows.length>0){
 this.clearAll();
 this.hdr.rows[1].parentNode.removeChild(this.hdr.rows[1]);
 this.hdr.rows[0].parentNode.removeChild(this.hdr.rows[0]);

}
 if(this.cellType._dhx_find("tree")!=-1){
 this.loadedKidsHash = new Hashtable();
 this.loadedKidsHash.put("hashOfParents",new Hashtable())
}

 var hdrRow = this.hdr.insertRow(0);
 for(var i=0;i<this.hdrLabels.length;i++){
 hdrRow.appendChild(document.createElement("TH"));
 hdrRow.childNodes[i]._cellIndex=i;
}
 if(_isIE)hdrRow.style.position="absolute";
 else hdrRow.style.height='auto';
 var hdrRow = this.hdr.insertRow(_isKHTML?2:1);

 hdrRow._childIndexes=new Array();
 var col_ex=0;
 for(var i=0;i<this.hdrLabels.length;i++){
 hdrRow._childIndexes[i]=i-col_ex;

 hdrRow.insertCell(i-col_ex);

 hdrRow.childNodes[i-col_ex]._cellIndex=i;
 hdrRow.childNodes[i-col_ex]._cellIndexS=i;
 this.setHeaderCol(i,this.hdrLabels[i]);
}
 
 if(!this.obj.firstChild)
 this.obj.appendChild(document.createElement("TBODY"));

 var tar=this.obj.firstChild;
 tar.appendChild(document.createElement("TR"));
 tar=tar.firstChild;
 if(_isIE)tar.style.position="absolute";
 else tar.style.height='auto';

 for(var i=0;i<this.hdrLabels.length;i++)
 tar.appendChild(document.createElement("TH"));


 this.setColumnIds()
 if(this.multiLine==-1)
 this.multiLine = true;
 if(this.multiLine != true)
 this.obj.className+=" row20px";

 
 
 
 this.sortImg.style.position = "absolute";
 this.sortImg.style.display = "none";
 this.sortImg.src = this.imgURL+"sort_desc.gif";
 this.sortImg.defLeft = 0;
 
 
 this.entCnt.rows[0].style.display = '' 
 if(this.noHeader){
 this.entCnt.rows[0].style.display = 'none';
}else{
 this.noHeader = false
}



 this.setSizes();
 if(fl)
 this.parseXML()
 this.obj.scrollTop = 0

 if(this.dragAndDropOff)this.dragger.addDragLanding(this.entBox,this);
 if(this._initDrF)this._initD();

};
 
 this.setSizes = function(fl){
 if((!this.noHeader)&&((!this.hdr.rows[0])||(!this.hdrBox.offsetWidth)))return;

 if(fl && this.gridWidth==this.entBox.offsetWidth && this.gridHeight==this.entBox.offsetHeight){
 return false
}else if(fl){
 this.gridWidth = this.entBox.offsetWidth
 this.gridHeight = this.entBox.offsetHeight
}

 if((!this.hdrBox.offsetHeight)&&(this.hdrBox.offsetHeight>0))
 this.entCnt.rows[0].cells[0].height = this.hdrBox.offsetHeight+"px";

 var gridWidth = parseInt(this.entBox.offsetWidth);
 var gridHeight = parseInt(this.entBox.offsetHeight);

 if(((!this._ahgr)&&(this.objBox.scrollHeight>this.objBox.offsetHeight))||((this._ahgrM)&&(this._ahgrM<this.objBox.scrollHeight)))
 gridWidth-=(this._scrFix||(_isFF?19:16));


 var len = this.hdr.rows[0].cells.length

 for(var i=0;i<this.hdr.rows[0].cells.length;i++){
 if(this.cellWidthType=='px' && this.cellWidthPX.length < len){
 this.cellWidthPX[i] = this.initCellWidth[i] - this._wcorr;
}else if(this.cellWidthType=='%' && this.cellWidthPC.length < len){
 this.cellWidthPC[i] = this.initCellWidth[i];
}
 if(this.cellWidthPC.length!=0){
 this.cellWidthPX[i] = parseInt(gridWidth*this.cellWidthPC[i]/100);
}
}




 this.chngCellWidth();

 var summ = 0;
 for(var i=0;i<this.cellWidthPX.length;i++)
 summ+= parseInt(this.cellWidthPX[i])
 if(_isOpera)summ-=1;
 this.objBuf.style.width = summ+"px";
 this.objBuf.childNodes[0].style.width = summ+"px";
 
 
 this.doOnScroll(0,1);

 
 

 this.hdr.style.border="0px solid gray";
 if((_isMacOS)&&(_isFF))
 var zheight=20;
 else
 var zheight=this.hdr.offsetHeight;
 

 
 if(this._ahgr)
 if(this.objBox.scrollHeight){
 if(_isIE)
 var z2=this.objBox.scrollHeight;
 else
 var z2=this.objBox.childNodes[0].scrollHeight;

 if(this._ahgrM)
 z2=(z2>this._ahgrM?this._ahgrM:z2)*1;

 gridHeight=z2+zheight+((this.objBox.offsetWidth<this.objBox.scrollWidth)?(_isFF?20:18):1);
 this.entBox.style.height=gridHeight+"px";
}

 if(!this.noHeader)
 this.entCnt.rows[1].cells[0].childNodes[0].style.top =(zheight-this.hdrBox.offsetHeight+(_isFF?0:(1-(this.entBox.offsetWidth-this.entBox.clientWidth))))+"px";
 
 this.entCnt.rows[1].cells[0].childNodes[0].style.height =(((gridHeight - zheight-1)<0 && _isIE)?20:(gridHeight - zheight-1))+"px";

 if(this._dload)
 this._dloadSize=Math.floor(parseInt(this.entBox.style.height)/20)+2;

};

 
 this.chngCellWidth = function(){
 for(var i=0;i<this.cellWidthPX.length;i++){
 this.hdr.rows[0].cells[i].style.width = this.cellWidthPX[i]+"px";
 this.obj.rows[0].childNodes[i].style.width = this.cellWidthPX[i]+"px";
}
}
 
 this.setDelimiter = function(delim){
 this.delim = delim;
}
 
 this.setInitWidthsP = function(wp){
 this.cellWidthType = "%";
 this.initCellWidth = wp.split(this.delim.replace(/px/gi,""));
 var el=window;
 var self=this;
 if(el.addEventListener){
 if((_isFF)&&(_FFrv<1.8))
 el.addEventListener("resize",function(){
 if(!self.entBox)return;
 var z=self.entBox.style.width;
 self.entBox.style.width="1px";

 window.setTimeout(function(){self.entBox.style.width=z;self.setSizes();},10);
},false);
 else
 el.addEventListener("resize",function(){if(self.setSizes)self.setSizes();},false);
}
 else if(el.attachEvent)
 el.attachEvent("onresize",function(){
 if(self._resize_timer)window.clearTimeout(self._resize_timer);
 if(self.setSizes)
 self._resize_timer=window.setTimeout(function(){self.setSizes();},500);
});

}
 
 this.setInitWidths = function(wp){
 this.cellWidthType = "px";
 this.initCellWidth = wp.split(this.delim);
 if(_isFF){
 for(var i=0;i<this.initCellWidth.length;i++)
 this.initCellWidth[i]=parseInt(this.initCellWidth[i])-2;
}

}

 
 this.enableMultiline = function(state){
 this.multiLine = convertStringToBoolean(state);
}


 
 this.enableMultiselect = function(state){
 this.selMultiRows = convertStringToBoolean(state);
}

 
 this.setImagePath = function(path){
 this.imgURL = path;
}

 
 this.changeCursorState = function(ev){

 var el = ev.target||ev.srcElement;
 if(el.tagName!="TD")
 el = this.getFirstParentOfType(el,"TD")
 if((el.tagName=="TD")&&(this._drsclmn)&&(!this._drsclmn[el._cellIndex]))return;

 if((el.offsetWidth -(ev.offsetX||(parseInt(this.getPosition(el,this.hdrBox))-ev.layerX)*-1))<10){
 el.style.cursor = "E-resize";
}else
 el.style.cursor = "default";
 if(_isOpera)this.hdrBox.scrollLeft = this.objBox.scrollLeft;
}
 
 this.startColResize = function(ev){
 this.resized = null;
 var el = ev.target||ev.srcElement;
 if(el.tagName!="TD")
 el = this.getFirstParentOfType(el,"TD")
 var x = ev.clientX;
 var tabW = this.hdr.offsetWidth;
 var startW = parseInt(el.offsetWidth)
 if(el.tagName=="TD" && el.style.cursor!="default"){
 if((this._drsclmn)&&(!this._drsclmn[el._cellIndex]))return;
 this.entBox.onmousemove = function(e){this.grid.doColResize(e||window.event,el,startW,x,tabW)}
 document.body.onmouseup = new Function("","document.getElementById('"+this.entBox.id+"').grid.stopColResize()");
}
}
 
 this.stopColResize = function(){
 this.entBox.onmousemove = "";
 document.body.onmouseup = "";
 this.setSizes();
 this.doOnScroll(0,1)
 if(this.onRSE)this.onRSE(this);
}
 
 this.doColResize = function(ev,el,startW,x,tabW){
 el.style.cursor = "E-resize";
 this.resized = el;
 var fcolW = startW+(ev.clientX-x);
 var wtabW = tabW+(ev.clientX-x)
 if((this.onRSI)&&(!this.onRSI(el._cellIndex,fcolW,this)))return;

 var gridWidth = parseInt(this.entBox.offsetWidth);
 if(this.objBox.scrollHeight>this.objBox.offsetHeight)gridWidth-=(this._scrFix||(_isFF?19:16));

 this._setColumnSizeR(el._cellIndex,fcolW,gridWidth);
 this.doOnScroll(0,1);

 if(_isOpera)this.setSizes();
 
 this.objBuf.childNodes[0].style.width = "";


}

 
 this._setColumnSizeR=function(ind,fcolW,gridWidth){
 if(fcolW>(this._drsclmW?(this._drsclmW[ind]||10):10)){

 this.obj.firstChild.firstChild.childNodes[ind].style.width = fcolW+"px";
 this.hdr.rows[0].childNodes[ind].style.width = fcolW+"px";

 if(this.cellWidthType=='px'){
 this.cellWidthPX[ind]=fcolW;
}else{
 var pcWidth = Math.round(fcolW/gridWidth*100)
 this.cellWidthPC[ind]=pcWidth;
}
}
}
 
 this.setSortImgState=function(state,ind,direction){
 if(!convertStringToBoolean(state)){
 this.sortImg.style.display = "none";
 return;
}

 if(direction=="ASC")
 this.sortImg.src = this.imgURL+"sort_asc.gif";
 else
 this.sortImg.src = this.imgURL+"sort_desc.gif";
 this.sortImg.style.display="";
 this.fldSorted=this.hdr.rows[0].cells[ind];
 this.setSortImgPos(ind);
}

 
 this.setSortImgPos = function(ind,mode){
 if(!ind)
 var el = this.fldSorted;
 else
 var el = this.hdr.rows[0].cells[ind];
 if(el!=null){
 var pos = this.getPosition(el,this.hdrBox)
 var wdth = el.offsetWidth;
 this.sortImg.style.left = Number(pos[0]+wdth-13)+"px";
 this.sortImg.defLeft = parseInt(this.sortImg.style.left)
 this.sortImg.style.top = Number(pos[1]+5)+"px";
 if((!this.useImagesInHeader)&&(!mode))
 this.sortImg.style.display = "inline";
 this.sortImg.style.left = this.sortImg.defLeft+"px";
}
}

 
 this.setActive = function(fl){
 if(arguments.length==0)
 var fl = true;
 if(fl==true){
 
 globalActiveDHTMLGridObject = this;
 this.isActive = true;
}else{
 this.isActive = false;
}
};
 
 this._doClick = function(ev){
 var selMethod = 0;
 var el = this.getFirstParentOfType(_isIE?ev.srcElement:ev.target,"TD");
 var fl = true;
 if(this.selMultiRows!=false){
 if(ev.shiftKey && this.row!=null){
 selMethod = 1;
}
 if(ev.ctrlKey){
 selMethod = 2;
}

}
 this.doClick(el,fl,selMethod)
};


 
 this._doContClick=function(ev){
 var el = this.getFirstParentOfType(_isIE?ev.srcElement:ev.target,"TD");
 if((!el)||(el.parentNode.idd===undefined))return true;

 if((ev.button==2)&&(this._ctmndx)){
 if((this.onBCM)&&(!this.onBCM(el.parentNode.idd,el._cellIndex,this)))return true;
 el.contextMenuId=el.parentNode.idd+"_"+el._cellIndex;
 el.contextMenu=this._ctmndx;
 el.a=this._ctmndx._contextStart;
 if(_isIE)
 ev.srcElement.oncontextmenu = function(){event.cancelBubble=true;return false;};
 el.a(el,ev);
 el.a=null;
}
 return true;
}

 
 this.doClick = function(el,fl,selMethod){
 this.setActive(true);
 if(!selMethod)
 selMethod = 0;
 if(this.cell!=null)
 this.cell.className = this.cell.className.replace(/cellselected/g,"");
 if(el.tagName=="TD" &&(this.rowsCol._dhx_find(this.rowsAr[el.parentNode.idd])!=-1 || this.rowsBuffer[0]._dhx_find(el.parentNode.idd)!=-1)){
 if(this.onSSC)var initial=this.getSelectedId();
 if(selMethod==0){
 this.clearSelection();
}else if(selMethod==1){
 var elRowIndex = this.rowsCol._dhx_find(el.parentNode)
 var lcRowIndex = this.rowsCol._dhx_find(this.lastClicked)
 if(elRowIndex>lcRowIndex){
 var strt = lcRowIndex;
 var end = elRowIndex;
}else{
 var strt = elRowIndex;
 var end = lcRowIndex;
}
 this.clearSelection();
 for(var i=0;i<this.rowsCol.length;i++){
 if((i>=strt && i<=end)&&(this.rowsCol[i])&&(!this.rowsCol[i]._sRow)){
 this.rowsCol[i].className+=" rowselected";
 this.selectedRows[this.selectedRows.length] = this.rowsCol[i]
}
}

}else if(selMethod==2){
 if(el.parentNode.className.indexOf("rowselected")!= -1){
 el.parentNode.className=el.parentNode.className.replace("rowselected","");
 this.selectedRows._dhx_removeAt(this.selectedRows._dhx_find(el.parentNode))
 var skipRowSelection = true;
}
}
 this.editStop()
 this.cell = el;
 if(this.row != el.parentNode){
 this.row = el.parentNode;
 if(fl)
{
 var rid = this.row.idd
 var func = this.onRowSelect
 setTimeout(function(){func(rid,false);},100)
}
}

 if((!skipRowSelection)&&(!this.row._sRow)){
 this.row.className+= " rowselected"
 if(this.selectedRows._dhx_find(this.row)==-1)
 this.selectedRows[this.selectedRows.length] = this.row;
}
 if(this.selBasedOn=="cell"){
 if(this.cell.parentNode.className.indexOf("rowselected")!=-1)
 this.cell.className = this.cell.className.replace(/cellselected/g,"")+" cellselected";
}

 if(selMethod!=1)
 this.lastClicked = el.parentNode;

 if(this.onSSC){
 var afinal=this.getSelectedId();
 if(initial!=afinal)this.onSSC(afinal);
}
}
 this.isActive = true;
 this.moveToVisible(this.cell)
}
 
 this.selectCell = function(r,cInd,fl,preserve,edit){
 if(!fl)
 fl = false;
 if(typeof(r)!="object")
 r = this.rowsCol[r]

 var c = r.childNodes[cInd];
 if(preserve)
 this.doClick(c,fl,3)
 else
 this.doClick(c,fl)
 if(edit)this.editCell();
}
 
 this.moveToVisible = function(cell_obj){
 try{
 var distance = cell_obj.offsetLeft+cell_obj.offsetWidth+20;

 if(distance>(this.objBox.offsetWidth+this.objBox.scrollLeft)){
 var scrollLeft = distance - this.objBox.offsetWidth;
}else if(cell_obj.offsetLeft<this.objBox.scrollLeft){
 var scrollLeft = cell_obj.offsetLeft-5
}
 if(scrollLeft)
 this.objBox.scrollLeft = scrollLeft;

 var distance = cell_obj.offsetTop+cell_obj.offsetHeight+20;
 if(distance>(this.objBox.offsetHeight+this.objBox.scrollTop)){
 var scrollTop = distance - this.objBox.offsetHeight;
}else if(cell_obj.offsetTop<this.objBox.scrollTop){
 var scrollTop = cell_obj.offsetTop-5
}
 if(scrollTop)
 this.objBox.scrollTop = scrollTop;
}catch(er){
}
}
 
 this.editCell = function(){
 this.editStop();
 if((this.isEditable!=true)||(!this.cell))
 return false;
 var c = this.cell;
 
 if(c.parentNode._locked)return false;
 

 eval("this.editor = new eXcell_"+this.cellType[this.cell._cellIndex]+"(c)");

 
 if(this.editor!=null){
 if(this.editor.isDisabled()){this.editor=null;return false;}
 c.className+=" editable";

 if(typeof(this.onEditCell)=="string"){
 if(eval(this.onEditCell+"(0,'"+this.row.idd+"',"+this.cell._cellIndex+");")!=false){
 this.editor.edit()
 this._Opera_stop=(new Date).valueOf();
 eval(this.onEditCell+"(1,'"+this.row.idd+"',"+this.cell._cellIndex+");")
}else{
 this.editor=null;
}
}else{
 if(this.onEditCell(0,this.row.idd,this.cell._cellIndex)!=false){
 this._Opera_stop=(new Date).valueOf();
 this.editor.edit()
 this.onEditCell(1,this.row.idd,this.cell._cellIndex)
}else{
 this.editor=null;
}
}
}
}
 
 this.editStop = function(){
 if(_isOpera)
 if(this._Opera_stop){
 if((this._Opera_stop*1+50)>(new Date).valueOf())return;
 this._Opera_stop=null;
}

 if(this.editor && this.editor!=null){
 this.cell.className=this.cell.className.replace("editable","");
 this.cell.wasChanged = this.editor.detach();
 this.editor=null;
 if(typeof(this.onEditCell)=="string")
 eval(this.onEditCell+"(2,'"+this.row.idd+"',"+this.cell._cellIndex+");")
 else
 this.onEditCell(2,this.row.idd,this.cell._cellIndex);
}
}
 
 this.doKey = function(ev){
 if(!ev)return true;
 if((ev.target||ev.srcElement).value!==window.undefined){
 var zx=(ev.target||ev.srcElement);
 if((!zx.parentNode)||(zx.parentNode.className.indexOf("editable")==-1))
 return true;
}
 if((globalActiveDHTMLGridObject)&&(this!=globalActiveDHTMLGridObject))
 return globalActiveDHTMLGridObject.doKey(ev);
 if(this.isActive==false){
 
 return true;
}

 if(this._htkebl)return true;
 try{
 var type = this.cellType[this.cell._cellIndex]
 
 if(ev.keyCode==13 &&(ev.ctrlKey || ev.shiftKey)){
 var rowInd = this.rowsCol._dhx_find(this.row)
 if(window.event.ctrlKey && rowInd!=this.rowsCol.length-1){
 if(this.row.rowIndex==this.obj._rowslength()-1 && this.dynScroll && this.dynScroll!='false')
 this.doDynScroll("dn")
 this.selectCell(this.rowsCol[rowInd+1],this.cell._cellIndex,true);
}else if(ev.shiftKey && rowInd!=0){
 if(this.row.rowIndex==0 && this.dynScroll && this.dynScroll!='false')
 this.doDynScroll("up")
 this.selectCell(this.rowsCol[rowInd-1],this.cell._cellIndex,true);
}
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 if(ev.keyCode==13 && !ev.ctrlKey && !ev.shiftKey){
 this.editStop();
 if(typeof(this.onEnter)=="string")
 eval("window."+this.onEnter+"('"+this.row.idd+"',"+this.cell._cellIndex+")")
 else
 this.onEnter(this.row.idd,this.cell._cellIndex);
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 
 if(ev.keyCode==9 && !ev.shiftKey){
 this.editStop();
 var aind=this.cell._cellIndex;
 var arow=this.row;

 aind++;

 if(aind>=this.obj.rows[0].childNodes.length){
 aind=0;
 arow=this.rowsCol[this.rowsCol._dhx_find(this.row)+1];
 if(!arow){
 aind=this.row.childNodes.length-1;
 return true;}
}
 this.selectCell(arow||this.row,aind,((arow)&&(this.row!=arow)));
 this.editCell()
 _isIE?ev.returnValue=false:ev.preventDefault();
}else if(ev.keyCode==9 && ev.shiftKey){
 this.editStop();
 var aind=this.cell._cellIndex-1;
 var arow=this.row;

 if(aind<0)
{
 aind=this.obj.rows[0].childNodes.length-1;

 arow=this.rowsCol[this.rowsCol._dhx_find(this.row)-1];
 if(!arow){aind=0;
 return true;}
}
 this.selectCell(arow||this.row,aind,((arow)&&(this.row!=arow)));
 this.editCell()
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 
 if(ev.keyCode==40 || ev.keyCode==38){

 if(this.editor && this.editor.combo){
 if(ev.keyCode==40)this.editor.shiftNext();
 if(ev.keyCode==38)this.editor.shiftPrev();
 return false;
}
 else{
 var rowInd = this.row.rowIndex;
 if(ev.keyCode==38 && rowInd!=1){
 
 
 this.selectCell(this.obj._rows(rowInd-2),this.cell._cellIndex,true);
}else if(this.pagingOn && ev.keyCode==38 && rowInd==1 && this.currentPage!=1){
 this.changePage(this.currentPage-1)
 this.selectCell(this.obj.rows[this.obj.rows.length-1],this.cell._cellIndex,true);
}else if(ev.keyCode==40 && rowInd!=this.rowsCol.length && rowInd!=this.obj.rows.length-1){
 
 
 this.selectCell(this.obj._rows(rowInd),this.cell._cellIndex,true);
}else if(this.pagingOn && ev.keyCode==40 &&(this.row!=this.rowsCol[this.rowsCol.length-1] || this.rowsBuffer[0].length>0 || !this.recordsNoMore)){
 this.changePage(this.currentPage+1)
 this.selectCell(this.obj._rows(0),this.cell._cellIndex,true);
}
}
 _isIE?ev.returnValue=false:ev.preventDefault();

}
 
 if((ev.keyCode==113)&&(this._f2kE)){
 this.editCell();
 return false;
}
 
 if(ev.keyCode==32){
 var c = this.cell
 eval("var ed = new eXcell_"+this.cellType[c._cellIndex]+"(c)");
 
 if(ed.changeState()!=false)
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 
 if(ev.keyCode==27 && this.oe!=false){
 this.editStop();
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 
 if(ev.keyCode==33 || ev.keyCode==34){
 if(this.pagingOn){
 if(ev.keyCode==33){
 this.changePage(this.currentPage-1)
}else{
 this.changePage(this.currentPage+1)
}
}
 this.selectCell(this.getRowIndex(this.row.idd)+this.rowsBufferOutSize*(ev.keyCode!=33?1:-1),this.cell._cellIndex,true);

 
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 
 if(!this.editor)
{
 if(ev.keyCode==37 && this.cellType._dhx_find("tree")!=-1){
 this.collapseKids(this.row)
 _isIE?ev.returnValue=false:ev.preventDefault();
}
 if(ev.keyCode==39 && this.cellType._dhx_find("tree")!=-1){
 this.expandKids(this.row)
 _isIE?ev.returnValue=false:ev.preventDefault();
}
}
 return true;
}catch(er){return true;}


}
 
 this.getRow = function(cell){
 if(!cell)
 cell = window.event.srcElement;
 if(cell.tagName!='TD')
 cell = cell.parentElement;
 r = cell.parentElement;
 if(this.cellType[cell._cellIndex]=='lk')
 eval(this.onLink+"('"+this.getRowId(r.rowIndex)+"',"+cell._cellIndex+")");
 this.selectCell(r,cell._cellIndex,true)
}
 
 this.selectRow = function(r,fl,preserve){
 if(typeof(r)!='object')
 r = this.rowsCol[r]
 this.selectCell(r,0,fl,preserve)
};
 
 this.sortRows = function(col,type,order){
 while(this.addRowsFromBuffer(true)){
}
 
 if(this.cellType._dhx_find("tree")!=-1){
 return this.sortTreeRows(col,type,order)
}
 var self=this;
 var arrTS=new Array();
 var atype = this.cellType[col];
 for(var i=0;i<this.rowsCol.length;i++)
 arrTS[this.rowsCol[i].idd]=this.cells3(this.rowsCol[i],col).getValue();

 this._sortRows(col,type,order,arrTS);
}

 
 this._sortRows = function(col,type,order,arrTS){

 if(type=='str'){
 this.rowsCol.sort(function(a,b){
 if(order=="asc")
 return arrTS[a.idd]>arrTS[b.idd]?1:-1
 else
 return arrTS[a.idd]<arrTS[b.idd]?1:-1
});
}else if(type=='int'){
 this.rowsCol.sort(function(a,b){
 var aVal = parseFloat(arrTS[a.idd])||-99999999999999
 var bVal = parseFloat(arrTS[b.idd])||-99999999999999
 if(order=="asc")
 return aVal-bVal
 else
 return bVal-aVal

});
}else if(type=='date'){
 this.rowsCol.sort(function(a,b){
 var aVal = Date.parse(new Date(arrTS[a.idd])||new Date("01/01/1900"))
 var bVal = Date.parse(new Date(arrTS[b.idd])||new Date("01/01/1900"))
 if(order=="asc")
 return aVal-bVal
 else
 return bVal-aVal

});
}
 if(this.dynScroll && this.dynScroll!='false'){
 alert("not implemented yet")
}else if(this.pagingOn){
 this.changePage(this.currentPage);
 if(this.onGridReconstructed)this.onGridReconstructed();
}else{
 var tb = this.obj.firstChild;
 if(tb.tagName == 'TR')tb = this.obj;


 for(var i=0;i<this.rowsCol.length;i++){
 if(this.rowsCol[i]!=this.obj._rows(i))
 tb.insertBefore(this.rowsCol[i],this.obj._rows(i))
 
}
}
 
 if(this.onGridReconstructed)this.onGridReconstructed();
}


 
 this.setXMLAutoLoading = function(filePath,bufferSize){
 if(arguments.length==0)return(this._xmlaR=true);
 this.recordsNoMore = false;
 this.xmlFileUrl = filePath;
 this.rowsBufferOutSize = bufferSize||this.rowsBufferOutSize==0?40:this.rowsBufferOutSize;
}

 
 this.enableBuffering = function(bufferSize){
 this.rowsBufferOutSize = bufferSize||this.rowsBufferOutSize==0?40:this.rowsBufferOutSize;;
}




 
 this.addRowsFromBuffer = function(stopBeforeServerCall){
 if(this.rowsBuffer[0].length==0){
 if(!this.recordsNoMore && !stopBeforeServerCall){
 if((this.xmlFileUrl!="")&&(!this._startXMLLoading)){
 this._startXMLLoading=true;
 this.loadXML(this.xmlFileUrl)
}
}else
 return false;
}
 var cnt = Math.min(this.rowsBufferOutSize,this.rowsBuffer[0].length)


 
 for(var i=0;i<cnt;i++){
 

 if(this.rowsBuffer[1][0].tagName == "TR"){
 this._insertRowAt(this.rowsBuffer[1][0],-1,this.pagingOn);
}else{
 var rowNode = this.rowsBuffer[1][0]
 this._insertRowAt(this.createRowFromXMLTag(rowNode),-1,this.pagingOn);
}
 this.rowsBuffer[0]._dhx_removeAt(0);
 this.rowsBuffer[1]._dhx_removeAt(0);
}

 return this.rowsBuffer[0].length!=0;
}
 
 this.createRowFromXMLTag = function(rowNode){
 if(rowNode.tagName=="TR")
 return rowNode;
 var tree=this.cellType._dhx_find("tree");
 var rId = rowNode.getAttribute("id")
 var pId=0;
 var cellsCol = rowNode.childNodes;
 var strAr = new Array(0);
 var jj=0;
 for(var j=0;j<cellsCol.length;j++){
 if(cellsCol[j].tagName=='cell'){
 if(jj!=tree)
 strAr[strAr.length] = cellsCol[j].firstChild?cellsCol[j].firstChild.data:"";
 else
 strAr[strAr.length] = rowNode.parentNode.getAttribute("id")||0+"^"+(cellsCol[j].firstChild?cellsCol[j].firstChild.data:"")+"^"+(rowNode.getAttribute("xmlkids")?"1":"0")+"^"+(cellsCol[j].getAttribute("image")||"leaf.gif");
 jj++;
}

}
 
 var r= this._fillRow(this._prepareRow(rId),strAr);
 
 
 if(rowNode.getAttribute("selected")==true){
 this.setSelectedRow(rId,false,false,rowNode.getAttribute("call")==true)
}
 
 if(rowNode.getAttribute("expand")=="1"){
 r.expand = "";
}

 this.rowsAr[rId] = r;
 return r;
}

 
 this.setMultiselect = function(fl){
 this.selMultiRows = convertStringToBoolean(fl);
}

 
 this.wasDblClicked = function(ev){
 var el = this.getFirstParentOfType(_isIE?ev.srcElement:ev.target,"TD");
 if(el){
 var rowId = el.parentNode.idd;
 return((this.onRowDblClicked)?this.onRowDblClicked(rowId,el._cellIndex):true);
}
}

 
 this._onHeaderClick = function(e){
 var that=this.grid;
 var el = that.getFirstParentOfType(_isIE?event.srcElement:e.target,"TD");

 if((this.grid.onHeaderClick)&&(!this.grid.onHeaderClick(el._cellIndexS)))return false;
 if(this.grid.resized==null)
 that.sortField(el._cellIndexS)
}

 
 this.deleteSelectedItem = function(){
 var num = this.selectedRows.length 
 if(num==0)
 return;
 var tmpAr = this.selectedRows;
 this.selectedRows = new Array(0)
 for(var i=num-1;i>=0;i--){
 var node = tmpAr[i]

 if(!this.deleteRow(node.idd,node)){
 this.selectedRows[this.selectedRows.length] = node;
}else{
 if(node==this.row){
 var ind = i;
}
}
 
}
 if(ind){
 try{
 if(ind+1>this.rowsCol.length)
 ind--;
 this.selectCell(ind,0,true)
}catch(er){
 this.row = null
 this.cell = null
}
}
}

 
 this.getSelectedId = function(){
 var selAr = new Array(0);
 for(var i=0;i<this.selectedRows.length;i++){
 selAr[selAr.length]=this.selectedRows[i].idd
}

 
 if(selAr.length==0)
 return null;
 else
 return selAr.join(this.delim);
}
 
 this.getSelectedCellIndex = function(){
 if(this.cell!=null)
 return this.cell._cellIndex;
 else
 return -1;
}
 
 this.getColWidth = function(ind){
 return parseInt(this.cellWidthPX[ind])+((_isFF)?2:0);
}

 
 this.setColWidth = function(ind,value){
 if(this.cellWidthType=='px')
 this.cellWidthPX[ind]=parseInt(value);
 else
 this.cellWidthPC[ind]=parseInt(value);
 this.setSizes();
}


 
 this.getRowById = function(id){
 var row = this.rowsAr[id]
 if(row)
 return row;
 else
 if(this._dload){
 var ind = this.rowsBuffer[0]._dhx_find(id);
 if(ind>=0){
 this._askRealRows(ind);
 return this.getRowById(id);
}
}
 else if(this.pagingOn){
 var ind = this.rowsBuffer[0]._dhx_find(id);
 if(ind>=0){
 var r = this.createRowFromXMLTag(this.rowsBuffer[1][ind]);
 this.rowsBuffer[1][ind] = r;
 return r;
}else{
 return null;
}
}
 return null;
}
 
 this.getRowByIndex = function(ind){
 if(this.rowsCol.length<=ind){
 if((this.rowsCol.length+this.rowsBuffer[0].length)<=ind)
 return null;
 else{
 var indInBuf = ind-this.rowsCol.length-1;
 var r = this.createRowFromXMLTag(this.rowsBuffer[1][indInBuf]);
 return r;
}
}else{
 return this.rowsCol[ind]
}
}

 
 this.getRowIndex = function(row_id){
 var ind = this.rowsCol._dhx_find(this.getRowById(row_id));
 if(ind!=-1)
 return ind;
 else{
 ind = this.rowsBuffer[0]._dhx_find(row_id)
 if(ind!=-1)
 return ind+this.rowsCol.length;
 return -1;
}
}
 
 this.getRowId = function(ind){
 var z=this.rowsCol[parseInt(ind)];
 if(z)return z.idd;
 return(this.rowsBuffer[0][this._dload?ind:(ind-this.rowsCol.length-1)]||null);
}
 
 this.setRowId = function(ind,row_id){
 var r = this.rowsCol[ind]
 this.changeRowId(r.idd,row_id)
}
 
 this.changeRowId = function(oldRowId,newRowId){
 var row = this.rowsAr[oldRowId]
 row.idd = newRowId;
 if(this.UserData[oldRowId]){
 this.UserData[newRowId] = this.UserData[oldRowId]
 this.UserData[oldRowId] = null;
}
 if(this.loadedKidsHash){
 var oldHash=this.loadedKidsHash.get(oldRowId);
 if(oldHash!=null){
 for(var z=0;z<oldHash.length;z++)
 oldHash[z].parent_id=newRowId;
 this.loadedKidsHash.put(newRowId,oldHash);
 this.loadedKidsHash.remove(oldRowId);
}
 var parentsHash = this.loadedKidsHash.get("hashOfParents")
 if(parentsHash!=null){
 if(parentsHash.get(oldRowId)!=null){
 parentsHash.put(newRowId,row);
 parentsHash.remove(oldRowId);
 this.loadedKidsHash.put("hashOfParents",parentsHash)
}
}
}

 this.rowsAr[oldRowId] = null;
 this.rowsAr[newRowId] = row;
}
 
 this.setColumnIds = function(ids){
 if(ids)
 this.columnIds = ids.split(",")
 if(this.hdr.rows[0].cells.length>=this.columnIds.length){
 for(var i=0;i<this.columnIds.length;i++){
 this.hdr.rows[0].cells[i].column_id = this.columnIds[i];
}
}
}
 
 this.getColIndexById = function(id){
 for(var i=0;i<this.hdr.rows[0].cells.length;i++){
 if(this.hdr.rows[0].cells[i].column_id==id)
 return i;
}
}
 
 this.getColumnId = function(cin){
 return this.hdr.rows[0].cells[cin].column_id
}

 
 this.getHeaderCol = function(cin){
 var z=this.hdr.rows[1]
 return z.cells[z._childIndexes[Number(cin)]].innerHTML;
}

 
 this.setRowTextBold = function(row_id){
 this.getRowById(row_id).style.fontWeight = "bold";
}
 
 this.setRowTextStyle = function(row_id,styleString){
 var r = this.getRowById(row_id)
 for(var i=0;i<r.childNodes.length;i++){
 var pfix="";


 if(_isIE)
 r.childNodes[i].style.cssText = pfix+"width:"+r.childNodes[i].style.width+";"+styleString;
 else
 r.childNodes[i].style.cssText = pfix+"width:"+r.childNodes[i].style.width+";"+styleString;
}

}
 
 this.setCellTextStyle = function(row_id,ind,styleString){
 var r = this.getRowById(row_id)
 if(!r)return;
 if(ind<r.childNodes.length)
{
 var pfix="";

 if(_isIE)
 r.childNodes[ind].style.cssText = pfix+"width:"+r.childNodes[ind].style.width+";"+styleString;
 else
 r.childNodes[ind].style.cssText = pfix+"width:"+r.childNodes[ind].style.width+";"+styleString;
}

}

 
 this.setRowTextNormal = function(row_id){
 this.getRowById(row_id).style.fontWeight = "normal";
}
 
 this.isItemExists = function(row_id){
 if(this.getRowById(row_id)!=null)
 return true
 else
 return false
}

 
 this.getRowsNum = function(){
 if(this._dload)
 return this.rowsBuffer[0].length||this.rowsCol.length;
 return this.rowsCol.length+this.rowsBuffer[0].length;
}
 
 this.getColumnCount = function(){
 return this.hdr.rows[0].cells.length;
}

 
 this.moveRowUp = function(row_id){
 var r = this.getRowById(row_id)
 var rInd = this.rowsCol._dhx_find(r)
 if(this.isTreeGrid()){
 if(this.rowsCol[rInd].parent_id!=this.rowsCol[rInd-1].parent_id)return;
 this.collapseKids(r);
}

 this.rowsCol._dhx_swapItems(rInd,rInd-1)

 if(r.previousSibling){
 this.obj.firstChild.insertBefore(r,r.previousSibling)
 this.setSizes();
}
}
 
 this.moveRowDown = function(row_id){
 var r = this.getRowById(row_id)
 var rInd = this.rowsCol._dhx_find(r)
 if(this.isTreeGrid())
 if(this.rowsCol[rInd].parent_id!=this.rowsCol[rInd+1].parent_id)return;

 this.rowsCol._dhx_swapItems(rInd,rInd+1)

 if(r.nextSibling){
 if(r.nextSibling.nextSibling)
 this.obj.firstChild.insertBefore(r,r.nextSibling.nextSibling)
 else
 this.obj.firstChild.appendChild(r)
 this.setSizes();
}
}
 
 this.cells = function(row_id,col){
 if(arguments.length==0){
 var c = this.cell;
 return eval("new eXcell_"+this.cellType[this.cell._cellIndex]+"(c)");
}else{
 var c = this.getRowById(row_id);
 var cell=(c._childIndexes?c.childNodes[c._childIndexes[col]]:c.childNodes[col]);
 if(!c)return null;
 return eval("new eXcell_"+this.cellType[cell._cellIndex]+"(cell)");
}
}
 
 this.cells2 = function(row_index,col){
 var c = this.rowsCol[parseInt(row_index)];
 var cell=(c._childIndexes?c.childNodes[c._childIndexes[col]]:c.childNodes[col]);
 return eval("new eXcell_"+this.cellType[cell._cellIndex]+"(cell)");
}

 
 this.cells3 = function(row,col){
 var cell=(row._childIndexes?row.childNodes[row._childIndexes[col]]:row.childNodes[col]);
 return eval("new eXcell_"+this.cellType[cell._cellIndex]+"(cell)");
}
 
 this.cells4 = function(cell){
 return eval("new eXcell_"+this.cellType[cell._cellIndex]+"(cell)");
}
 
 this.getCombo = function(col_ind){
 if(this.cellType[col_ind].indexOf('co')==0){
 if(!this.combos[col_ind]){
 this.combos[col_ind] = new dhtmlXGridComboObject();
}
 return this.combos[col_ind];
}else{

 return null;
}
}
 
 this.setUserData = function(row_id,name,value){
 try{
 if(row_id=="")
 row_id = "gridglobaluserdata";
 if(!this.UserData[row_id])
 this.UserData[row_id] = new Hashtable()
 this.UserData[row_id].put(name,value)
}catch(er){
 alert("UserData Error:"+er.description)
}
}
 
 this.getUserData = function(row_id,name){
 if(row_id=="")
 row_id = "gridglobaluserdata";
 var z=this.UserData[row_id];
 return(z?z.get(name):"");
}

 
 this.setEditable = function(fl){
 if(fl!='true' && fl!=1 && fl!=true)
 ifl = true;
 else
 ifl = false;
 for(var j=0;j<this.cellType.length;j++){
 if(this.cellType[j].indexOf('ra')==0 || this.cellType[j]=='ch'){
 for(var i=0;i<this.rowsCol.length;i++){
 var z=this.rowsCol[i].cells[j];
 if((z.childNodes.length>0)&&(z.firstChild.nodeType==1)){
 this.rowsCol[i].cells[j].firstChild.disabled = ifl;
}
}
}
}
 this.isEditable = !ifl;
}
 
 this.setSelectedRow = function(row_id,multiFL,show,call){
 if(!call)
 call = false;
 this.selectCell(this.getRowById(row_id),0,call,multiFL);
 if(arguments.length>2 && show==true){
 this.moveToVisible(this.getRowById(row_id).cells[0])
}
}
 
 this.clearSelection = function(){
 this.editStop()
 for(var i=0;i<this.selectedRows.length;i++){
 this.selectedRows[i].className=this.selectedRows[i].className.replace(/rowselected/g,"");
}

 
 this.selectedRows = new Array(0)
 this.row = null;
 if(this.cell!=null){
 this.cell.className = this.cell.className.replace(/cellselected/g,"");
 this.cell = null;
}
}
 
 this.copyRowContent = function(from_row_id,to_row_id){
 var frRow = this.getRowById(from_row_id)

 if(!this.isTreeGrid())
 for(i=0;i<frRow.cells.length;i++){
 this.cells(to_row_id,i).setValue(this.cells(from_row_id,i).getValue())
}
 else
 this._copyTreeGridRowContent(frRow,from_row_id,to_row_id);

 
 if(!isIE())
 this.getRowById(from_row_id).cells[0].height = frRow.cells[0].offsetHeight
}
 
 this.setHeaderCol = function(col,label){
 var z=this.hdr.rows[1];
 if(!this.useImagesInHeader){
 var hdrHTML = "<div class='hdrcell'>"
 if(label.indexOf('img:[')!=-1){
 var imUrl = label.replace(/.*\[([^>]+)\].*/,"$1");
 label = label.substr(label.indexOf("]")+1,label.length)
 hdrHTML+="<img width='18px' height='18px' align='absmiddle' src='"+imUrl+"' hspace='4'>"
}
 hdrHTML+=label;
 hdrHTML+="</div>";
 z.cells[z._childIndexes[col]].innerHTML = hdrHTML;
 
}else{
 z.cells[z._childIndexes[col]].style.textAlign = "left";
 z.cells[z._childIndexes[col]].innerHTML = "<img src='"+this.imgURL+""+label+"' onerror='this.src = \""+this.imgURL+"imageloaderror.gif\"'>";
 
 var a = new Image();
 a.src = this.imgURL+""+label.replace(/(\.[a-z]+)/,".desc$1");
 this.preloadImagesAr[this.preloadImagesAr.length] = a;
 var b = new Image();
 b.src = this.imgURL+""+label.replace(/(\.[a-z]+)/,".asc$1");
 this.preloadImagesAr[this.preloadImagesAr.length] = b;
}
 

}
 
 this.clearAll = function(){
 this.editStop();

 if(this._dload){
 this.objBox.scrollTop=0;
 this.limit=this._limitC||0;
 this._initDrF=true;
}

 var len = this.rowsCol.length;
 
 if(this.loadedKidsHash!=null){
 this.loadedKidsHash.clear();
 this.loadedKidsHash.put("hashOfParents",new Hashtable());
}
 
 len = this.obj._rowslength();
 for(var i=len-1;i>=0;i--){
 this.obj.firstChild.removeChild(this.obj._rows(i))
}
 
 this.row = null;
 this.cell = null;
 
 this.rowsCol = new Array(0)
 this.rowsAr = new Array(0);
 this.rowsBuffer = new Array(new Array(0),new Array(0));
 this.UserData = new Array(0)

 if(this.pagingOn){
 this.changePage(1);
 
}

 this.setSizes();
 
}


 
 this.sortField = function(ind,repeatFl){
 if((this.onCLMS)&&(!this.onCLMS(ind,this)))return;
 if(this.getRowsNum()==0)
 return false;
 var el = this.hdr.rows[0].cells[ind];
 if(!el)return;
 if(el.tagName == "TH" &&(this.fldSort.length-1)>=el._cellIndex && this.fldSort[el._cellIndex]!='na'){
 if((((this.sortImg.src.indexOf("_desc.gif")==-1)&&(!repeatFl))||((this.sortImg.style.filter!="")&&(repeatFl)))&&(this.fldSorted==el)){
 var sortType = "desc";
 this.sortImg.src = this.imgURL+"sort_desc.gif";
}else{
 var sortType = "asc";
 this.sortImg.src = this.imgURL+"sort_asc.gif";
}
 
 if(this.useImagesInHeader){
 var cel=this.hdr.rows[1].cells[el._cellIndex].firstChild;
 if(this.fldSorted!=null){
 var celT=this.hdr.rows[1].cells[this.fldSorted._cellIndex].firstChild;
 celT.src = celT.src.replace(/\.[ascde]+\./,".");
}
 cel.src = cel.src.replace(/(\.[a-z]+)/,"."+sortType+"$1")
}
 
 this.sortRows(el._cellIndex,this.fldSort[el._cellIndex],sortType)
 this.fldSorted = el;
 var real_el=this.hdr.rows[1]._childIndexes[el._cellIndex];
 this.setSortImgPos(this.hdr.rows[1].childNodes[real_el]._cellIndex);
}
}



 
 this.enableHeaderImages = function(fl){
 this.useImagesInHeader = fl;
}

 
 this.setHeader = function(hdrStr,splitSign){
 var arLab = hdrStr.split(this.delim);
 var arWdth = new Array(0);
 var arTyp = new Array(0);
 var arAlg = new Array(0);
 var arVAlg = new Array(0);
 var arSrt = new Array(0);
 for(var i=0;i<arLab.length;i++){
 arWdth[arWdth.length] = Math.round(100/arLab.length);
 arTyp[arTyp.length] = "ed";
 arAlg[arAlg.length] = "left";
 arVAlg[arVAlg.length] = "";
 arSrt[arSrt.length] = "na";
}
 this.splitSign = splitSign||"-";
 this.hdrLabels = arLab;
 this.cellWidth = arWdth;
 this.cellType = arTyp;
 this.cellAlign = arAlg;
 this.cellVAlign = arVAlg;
 this.fldSort = arSrt;
}


 
 this.getColType = function(cell_index){
 return this.cellType[cell_index];
}

 
 this.getColTypeById = function(col_id){
 return this.cellType[this.getColIndexById(col_id)];
}

 
 this.setColTypes = function(typeStr){
 this.cellType = typeStr.split(this.delim)
 this._strangeParams=new Array();
 for(var i=0;i<this.cellType.length;i++)
 if((this.cellType[i].indexOf("[")!=-1))
{
 var z=this.cellType[i].split(/[\[\]]+/g);
 this.cellType[i]=z[0];
 this.defVal[i]=z[1];
 if(z[1].indexOf("=")==0){
 this.cellType[i]="math";
 this._strangeParams[i]=z[0];
}
}
}
 
 this.setColSorting = function(sortStr){
 this.fldSort = sortStr.split(this.delim)

}
 
 this.setColAlign = function(alStr){
 this.cellAlign = alStr.split(this.delim)
}
 
 this.setColVAlign = function(alStr){
 this.cellVAlign = alStr.split(this.delim)
}

 
 this.setMultiLine = function(fl){
 if(fl==true)
 this.multiLine = -1;
}
 
 this.setNoHeader = function(fl){
 if(fl==true)
 this.noHeader = true;
}
 
 this.showRow = function(rowID){
 this.moveToVisible(this.getRowById(rowID).cells[0])
}

 
 this.setStyle = function(ss_header,ss_grid,ss_selCell,ss_selRow){
 this.ssModifier = [ss_header,ss_grid,ss_selCell,ss_selCell,ss_selRow];
 var prefs=["#"+this.entBox.id+" table.hdr td","#"+this.entBox.id+" table.obj td","#"+this.entBox.id+" table.obj tr.rowselected td.cellselected","#"+this.entBox.id+" table.obj td.cellselected","#"+this.entBox.id+" table.obj tr.rowselected td"];

 for(var i=0;i<prefs.length;i++)
 if(_isIE)
 this.styleSheet[0].addRule(prefs[i],this.ssModifier[i]);
 else
 this.styleSheet[0].insertRule(prefs[i]+"{"+this.ssModifier[i]+"}",0);
}
 
 this.setColumnColor = function(clr){
 this.columnColor = clr.split(this.delim)
}

 
 this.enableAlterCss = function(cssE,cssU){
 if(cssE||cssU)
 this.setOnGridReconstructedHandler(function(){
 this._fixAlterCss();
});


 this._cssEven = cssE;
 this._cssUnEven = cssU;
}

 
 this._fixAlterCss = function(ind){
 ind=ind||0;
 var j=ind;
 for(var i=ind;i<this.rowsCol.length;i++){
 if(!this.rowsCol[i])continue;
 if(this.rowsCol[i].style.display!="none"){
 if(this.rowsCol[i].className.indexOf("rowselected")!=-1){
 if(j%2==1)
 this.rowsCol[i].className=this._cssUnEven+" rowselected";
 else
 this.rowsCol[i].className=this._cssEven+" rowselected";
}
 else{
 if(j%2==1)
 this.rowsCol[i].className=this._cssUnEven;
 else
 this.rowsCol[i].className=this._cssEven;
}
 j++;
}
}
}


 
 this.doDynScroll = function(fl){
 if(!this.dynScroll || this.dynScroll=='false')
 return false;
 
 
 this.setDynScrollPageSize();
 

 var tmpAr = new Array(0)
 if(fl && fl=='up'){
 this.dynScrollPos = Math.max(this.dynScrollPos-this.dynScrollPageSize,0);
}else if(fl && fl=='dn' && this.dynScrollPos+this.dynScrollPageSize<this.rowsCol.length){
 if(this.dynScrollPos+this.dynScrollPageSize+this.rowsBufferOutSize>this.rowsCol.length){
 this.addRowsFromBuffer()
}
 this.dynScrollPos+=this.dynScrollPageSize
}
 var start = Math.max(this.dynScrollPos-this.dynScrollPageSize,0);
 for(var i = start;i<this.rowsCol.length;i++){
 if(i>=this.dynScrollPos && i<this.dynScrollPos+this.dynScrollPageSize){
 tmpAr[tmpAr.length] = this.rowsCol[i];
}
 this.rowsCol[i].removeNode(true);
}
 for(var i=0;i<tmpAr.length;i++){
 this.obj.childNodes[0].appendChild(tmpAr[i]);
 if(this.obj.offsetHeight>this.objBox.offsetHeight)
 this.dynScrollPos-=(this.dynScrollPageSize-i)
}
 this.setSizes()


}
 
 this.setDynScrollPageSize = function(){
 if(this.dynScroll && this.dynScroll!='false'){
 var rowsH = 0;
 try{
 var rowH = this.obj._rows(0).scrollHeight;
}catch(er){
 var rowH = 20
}
 for(var i=0;i<1000;i++){
 rowsH = i*rowH;
 if(this.objBox.offsetHeight<rowsH)
 break
}
 this.dynScrollPageSize = i+2;
 this.rowsBufferOutSize = this.dynScrollPageSize*4
}
}



 
this.dhx_attachEvent=function(original,catcher){
 if((!this[original])||(!this[original].dhx_addEvent)){
 var z=new this.dhx_eventCatcher(this);
 z.dhx_addEvent(this[original]);
 this[original]=z;
}
 this[original].dhx_addEvent(catcher);
}
 
this.dhx_eventCatcher=function(obj){
 var dhx_catch=new Array();
 var m_obj=obj;
 var z=function(){
 if(dhx_catch)
 var res=true;

 for(var i=0;i<dhx_catch.length;i++)
 if(!dhx_catch[i].apply(m_obj,arguments))res=false;
 return res;
}
 z.dhx_addEvent=function(ev){
 if(typeof(ev)!="function")
 ev=eval(ev);
 if(ev)
 dhx_catch[dhx_catch.length]=ev;
}
 return z;
}

 
 
 
 this.setOnRowSelectHandler = function(func){
 this.dhx_attachEvent("onRowSelect",func);
}


 
 this.setOnScrollHandler = function(func){
 this.dhx_attachEvent("_onSCRL",func);
}

 
 this.setOnEditCellHandler = function(func){
 this.dhx_attachEvent("onEditCell",func);
}
 
 this.setOnCheckHandler = function(func){
 this.dhx_attachEvent("onCheckbox",func);
}

 
 this.setOnEnterPressedHandler = function(func){
 this.dhx_attachEvent("onEnter",func);
}

 
 this.setOnBeforeRowDeletedHandler = function(func){
 this.dhx_attachEvent("onBeforeRowDeleted",func);
}
 
 this.setOnRowAddedHandler = function(func){
 this.dhx_attachEvent("onRowAdded",func);
}

 
 this.setOnGridReconstructedHandler = function(func){
 this.dhx_attachEvent("onGridReconstructed",func);
}
 




 
 this.getPosition = function(oNode,pNode){

 if(!pNode)
 var pNode = document.body

 var oCurrentNode=oNode;
 var iLeft=0;
 var iTop=0;
 while((oCurrentNode)&&(oCurrentNode!=pNode)){
 iLeft+=oCurrentNode.offsetLeft;
 iTop+=oCurrentNode.offsetTop;
 oCurrentNode=oCurrentNode.offsetParent;

}
 if(((_isKHTML)||(_isOpera))&&(pNode == document.body)){
 iLeft+=document.body.offsetLeft;
 iTop+=document.body.offsetTop;
}

 return new Array(iLeft,iTop);
}
 
 this.getFirstParentOfType = function(obj,tag){
 while(obj.tagName!=tag && obj.tagName!="BODY"){
 obj = obj.parentNode;
}
 return obj;
}

 
 
 this.setColumnCount = function(cnt){alert('setColumnCount method deprecated')}
 
 this.showContent = function(){alert('showContent method deprecated')}

 
 this.objBox.onscroll = new Function("","this.grid._doOnScroll()")
 if((!_isOpera)||(_OperaRv>8.5))
{
 this.hdr.onmousemove = new Function("e","this.grid.changeCursorState(e||window.event)");
 this.hdr.onmousedown = new Function("e","this.grid.startColResize(e||window.event)");
}
 this.obj.onmousemove = this._drawTooltip;
 this.obj.onclick = new Function("e","this.grid._doClick(e||window.event);if(this.grid._sclE)this.grid.editCell(e||window.event)");
 this.entBox.onmousedown = new Function("e","return this.grid._doContClick(e||window.event);");
 this.obj.ondblclick = new Function("e","if(!this.grid.wasDblClicked(e||window.event)){return false};if(this.grid._dclE)this.grid.editCell(e||window.event)");
 this.hdr.onclick = this._onHeaderClick;

 
 if(!document.body._dhtmlxgrid_onkeydown){
 if(document.addEventListener)document.addEventListener("keydown",new Function("e","if(globalActiveDHTMLGridObject)return globalActiveDHTMLGridObject.doKey(e||window.event);return true;"),false);
 else if(document.attachEvent)document.attachEvent("onkeydown",new Function("e","if(globalActiveDHTMLGridObject)return globalActiveDHTMLGridObject.doKey(e||window.event);return true;"));
 document.body._dhtmlxgrid_onkeydown=true;
}

 
 
 this.entBox.onbeforeactivate = new Function("","this.grid.setActive()");
 this.entBox.onbeforedeactivate = new Function("","this.grid.isActive=-1");
 
 this.doOnRowAdded = function(row){};
 return this;
}


 
 dhtmlXGridObject.prototype.isTreeGrid= function(){
 return(this.cellType._dhx_find("tree")!=-1);
}

 
 dhtmlXGridObject.prototype.addRow = function(new_id,text,ind){
 var r = this._addRow(new_id,text,ind);
 if(typeof(this.onRowAdded)=='function'){
 this.onRowAdded(new_id);
}
 if(this.pagingOn){
 this.changePage(this.currentPage)
}
 this.setSizes();
 return r;
}


 
 dhtmlXGridObject.prototype._prepareRow=function(new_id){
 var r=document.createElement("TR");
 r.idd = new_id;
 r.grid = this;

 for(var i=0;i<this.hdr.rows[0].cells.length;i++){
 var c = document.createElement("TD");
 
 if(this._enbCid)c.id="c_"+r.idd+"_"+i;
 
 c._cellIndex = i;
 if(this.dragAndDropOff)this.dragger.addDraggableItem(c,this);
 c.align = this.cellAlign[i];
 c.style.verticalAlign = this.cellVAlign[i];
 
 c.bgColor = this.columnColor[i] || "";




 r.appendChild(c);
}
 return r;
}
 
 dhtmlXGridObject.prototype._fillRow=function(r,text){
 if(!this._parsing_)this.editStop();

 this.math_off=true;
 this.math_req=false;

 if(typeof(text)!='object')
 text =(text||"").split(this.delim);
 for(var i=0;i<r.childNodes.length;i++){
 if((i<text.length)||(this.defVal[i])){
 var val = text[i]
 if((this.defVal[i])&&((val=="")||(val===window.undefined)))
 val = this.defVal[i];


 if(this._dload)
 this.editor = this.cells3(r,r.childNodes[i]._cellIndex);
 else
 this.editor = this.cells4(r.childNodes[i]);

 
 this.editor.setValue(val)
 this.editor = this.editor.destructor();
}else{
 var val = "&nbsp;";
 r.childNodes[i].innerHTML = val;
 r.childNodes[i]._clearCell=true;
}
}
 this.math_off=false;
 if((this.math_req)&&(!this._parsing_)){
 for(var i=0;i<this.hdr.rows[0].cells.length;i++)
 this._checkSCL(r.childNodes[i]);
 this.math_req=false;
}
 return r;
}
 
 dhtmlXGridObject.prototype._insertRowAt=function(r,ind,skip){
 if(ind<0)ind=this.rowsCol.length;

 if((arguments.length<2)||(ind===window.undefined))
 ind = this.rowsCol.length 
 else{
 if(ind>this.rowsCol.length)
 ind = this.rowsCol.length;
}

 if(!skip)
 if((ind==(this.obj.rows.length-1))||(!this.rowsCol[ind]))
 if(_isKHTML)
 this.obj.appendChild(r);
 else{
 this.obj.firstChild.appendChild(r);
}
 else
{
 this.rowsCol[ind].parentNode.insertBefore(r,this.rowsCol[ind]);
}


 this.rowsAr[r.idd] = r;
 this.rowsCol._dhx_insertAt(ind,r);

 if(this._cssEven){
 if(ind%2==1)r.className+=" "+this._cssUnEven;
 else r.className+=" "+this._cssEven;

 if(ind!=(this.rowsCol.length-1))
 this._fixAlterCss(ind+1);
}

 
 this.doOnRowAdded(r);

 
 if((this.math_req)&&(!this._parsing_)){
 for(var i=0;i<this.hdr.rows[0].cells.length;i++)
 this._checkSCL(r.childNodes[i]);
 this.math_req=false;
}

 return r;
}

 
 dhtmlXGridObject.prototype._addRow = function(new_id,text,ind){
 var row = this._fillRow(this._prepareRow(new_id),text);
 if(ind>this.rowsCol.length && ind<(this.rowsCol.length+this.rowsBuffer[0].length)){
 var inBufInd = ind - this.rowsCol.length;
 this.rowsBuffer[0]._dhx_insertAt(inBufInd,new_id);
 this.rowsBuffer[1]._dhx_insertAt(inBufInd,row);
 return row;
}
 return this._insertRowAt(row,ind);
}

 
dhtmlXGridObject.prototype.setRowHidden=function(id,state){
 var f=convertStringToBoolean(state);
 
 
 
 var row= this.getRowById(id)
 if(!row)
 return;

 if(row.expand==="")
 this.collapseKids(row);

 if((state)&&(row.style.display!="none")){
 row.style.display="none";
 var z=this.selectedRows._dhx_find(row);
 if(z!=-1){
 row.className=row.className.replace("rowselected","");
 for(var i=0;i<row.childNodes.length;i++)
 row.childNodes[i].className=row.childNodes[i].className.replace(/cellselected/g,"");
 this.selectedRows._dhx_removeAt(z);
}
 if(this.onGridReconstructed)
 this.onGridReconstructed();
}

 if((!state)&&(row.style.display=="none")){
 row.style.display="";
 if(this.onGridReconstructed)this.onGridReconstructed();
}

}


 
dhtmlXGridObject.prototype.enableRowsHover = function(mode,cssClass){
 this._hvrCss=cssClass;
 if(convertStringToBoolean(mode)){
 if(!this._elmnh){
 this.obj._honmousemove=this.obj.onmousemove;
 this.obj.onmousemove=this._setRowHover;
 if(_isIE)
 this.obj.onmouseleave=this._unsetRowHover;
 else
 this.obj.onmouseout=this._unsetRowHover;

 this._elmnh=true;
}
}else{
 if(this._elmnh){
 this.obj.onmousemove=this.obj._honmousemove;
 if(_isIE)
 this.obj.onmouseleave=null;
 else
 this.obj.onmouseout=null;

 this._elmnh=false;
}
}
};

 
dhtmlXGridObject.prototype.enableEditEvents = function(click,dblclick,f2Key){
 this._sclE = convertStringToBoolean(click);
 this._dclE = convertStringToBoolean(dblclick);
 this._f2kE = convertStringToBoolean(f2Key);
}


 
dhtmlXGridObject.prototype.enableLightMouseNavigation = function(mode){
 if(convertStringToBoolean(mode)){
 if(!this._elmn){
 this.entBox._onclick=this.entBox.onclick;
 this.entBox.onclick = function(){return true;};

 this.obj.onclick=function(e){
 this.grid.editStop();
 var c = this.grid.getFirstParentOfType(e?e.target:event.srcElement,'TD');
 this.grid.doClick(c);
 this.ondblclick(e);
}

 this.obj._onmousemove=this.obj.onmousemove;
 this.obj.onmousemove=this._autoMoveSelect;
 this._elmn=true;
}
}else{
 if(this._elmn){
 this.entBox.onclick = this.entBox._onclick;
 this.obj.onclick=function(){return true};
 this.obj.onmousemove=this.obj._onmousemove;
 this._elmn=false;
}
}
}


 
dhtmlXGridObject.prototype._unsetRowHover = function(e,c){
 if(c)that=this;else that=this.grid;

 if((that._lahRw)&&(that._lahRw!=c)){
 for(var i=0;i<that._lahRw.childNodes.length;i++)
 that._lahRw.childNodes[i].className=that._lahRw.childNodes[i].className.replace(that._hvrCss,"");
 that._lahRw=null;
}
}

 
dhtmlXGridObject.prototype._setRowHover = function(e){
 var c = this.grid.getFirstParentOfType(e?e.target:event.srcElement,'TD');
 if(c){
 this.grid._unsetRowHover(0,c);
 c=c.parentNode;
 for(var i=0;i<c.childNodes.length;i++)
 c.childNodes[i].className+=" "+this.grid._hvrCss;
 this.grid._lahRw=c;
}
 this._honmousemove(e);
}

 
dhtmlXGridObject.prototype._autoMoveSelect = function(e){
 
 if(!this.grid.editor)
{
 var c = this.grid.getFirstParentOfType(e?e.target:event.srcElement,'TD');
 this.grid.doClick(c,true,0);
}
 this._onmousemove(e);
}


 
dhtmlXGridObject.prototype.destructor=function(){
 var a;
 this.xmlLoader=this.xmlLoader.destructor();
 for(var i=0;i<this.rowsCol.length;i++)
 if(this.rowsCol[i])this.rowsCol[i].grid=null;
 for(i in this.rowsAr)
 if(this.rowsAr[i])this.rowsAr[i]=null;

 this.rowsCol=new Array();
 this.rowsAr=new Array();
 this.entBox.innerHTML="";
 this.entBox.onclick = function(){};
 this.entBox.onmousedown = function(){};
 this.entBox.onbeforeactivate = function(){};
 this.entBox.onbeforedeactivate = function(){};
 this.entBox.onbeforedeactivate = function(){};

 for(a in this){
 if((this[a])&&(this[a].m_obj))
 this[a].m_obj=null;
 this[a]=null;
}


 if(this==globalActiveDHTMLGridObject)
 globalActiveDHTMLGridObject=null;
 return null;
}





 
 dhtmlXGridObject.prototype.getSortingState=function(){
 var z=new Array();
 if(this.fldSorted){
 z[0]=this.fldSorted._cellIndex;
 z[1]=(this.sortImg.src.indexOf("sort_desc.gif")!=-1)?"DES":"ASC";
}
 return z;
};

 
 dhtmlXGridObject.prototype.enableAutoHeigth=function(mode,maxHeight){
 this._ahgr=convertStringToBoolean(mode);
 this._ahgrM=maxHeight||null;
};

 
 dhtmlXGridObject.prototype.enableKeyboardSupport=function(mode){
 this._htkebl=!convertStringToBoolean(mode);
};


 
 dhtmlXGridObject.prototype.enableContextMenu=function(menu){
 this._ctmndx=menu;
};
 
 dhtmlXGridObject.prototype.setOnBeforeContextMenu=function(func){
 this.dhx_attachEvent("onBCM",func);
};



 
 dhtmlXGridObject.prototype.setScrollbarWidthCorrection=function(width){
 this._scrFix=parseInt(width);
};

 
 dhtmlXGridObject.prototype.enableTooltips=function(list){
 this._enbTts=list.split(",");
 for(var i=0;i<this._enbTts.length;i++)
 this._enbTts[i]=convertStringToBoolean(this._enbTts[i]);
};


 
 dhtmlXGridObject.prototype.enableResizing=function(list){
 this._drsclmn=list.split(",");
 for(var i=0;i<this._drsclmn.length;i++)
 this._drsclmn[i]=convertStringToBoolean(this._drsclmn[i]);
};

 
 dhtmlXGridObject.prototype.setColumnMinWidth=function(width,ind){
 if(arguments.length==2){
 if(!this._drsclmW)this._drsclmW=new Array();
 this._drsclmW[ind]=width;
}
 else
 this._drsclmW=width.split(",");
};


 
 
 dhtmlXGridObject.prototype.enableCellIds=function(mode){
 this._enbCid=convertStringToBoolean(mode);
};
 



 
 
 dhtmlXGridObject.prototype.lockRow=function(rowId,mode){
 var z=this.getRowById(rowId);
 if(z){
 z._locked=convertStringToBoolean(mode);
 if((this.cell)&&(this.cell.parentNode.idd==rowId))
 this.editStop();
}
};
 

 
 dhtmlXGridObject.prototype._getRowArray=function(row){
 var text=new Array();
 for(var ii=0;ii<row.childNodes.length;ii++)
 text[ii]=this.cells3(row,ii).getValue();
 return text;
}


 
 dhtmlXGridObject.prototype.parseXML = function(xml,startIndex){
 this._xml_ready=true;
 var pid=null;
 var zpid=null;
 if(!xml)
 try{
 var xmlDoc = eval(this.entBox.id+"_xml").XMLDocument;
}catch(er){
 var xmlDoc = this.loadXML(this.xmlFileUrl)
}
 else{
 if(typeof(xml)=="object"){
 var xmlDoc = xml;
}else{
 if(xml.indexOf(".")!=-1){
 if(this.xmlFileUrl=="")
 this.xmlFileUrl = xml
 var xmlDoc = this.loadXML(xml)
 return;
}else
 var xmlDoc = eval(xml).XMLDocument;
}
}





 var ar = new Array();
 var idAr = new Array();



 var rowsCol = this.xmlLoader.doXPath("//rows/row",xmlDoc);
 if(rowsCol.length==0){
 this.recordsNoMore = true;
 var pid=0;
}
 else{
 pid=(rowsCol[0].parentNode.getAttribute("parent")||null);
 zpid=this.getRowById(pid);
 if(zpid)zpid._xml_await=false;
 else pid=null;
 startIndex=this.getRowIndex(pid)+1;
}

 
 var gudCol = this.xmlLoader.doXPath("//rows/userdata",xmlDoc);
 if(gudCol.length>0){
 this.UserData["gridglobaluserdata"] = new Hashtable();
 for(var j=0;j<gudCol.length;j++){
 this.UserData["gridglobaluserdata"].put(gudCol[j].getAttribute("name"),gudCol[j].firstChild?gudCol[j].firstChild.data:"");
}
}

 
 var tree=this.cellType._dhx_find("tree");
 if(tree==-1)tree=this.cellType._dhx_find("3d");
 if(this._innerParse(rowsCol,startIndex,tree,pid)==-1)return;

 if(zpid)this.expandKids(zpid);

 if(this.dynScroll && this.dynScroll!='false'){
 this.doDynScroll()
}

 if(tree!=-1){
 var oCol = this.xmlLoader.doXPath("//row[@open]",xmlDoc);
 for(var i=0;i<oCol.length;i++)
 this.openItem(oCol[i].getAttribute("id"));
}

 this.setSizes();
 if(_isOpera){
 this.obj.style.border=1;
 this.obj.style.border=0;
}
 this._startXMLLoading=false;



 if(this.onXLE)
 this.onXLE(this,rowsCol.length);
}
 
 dhtmlXGridObject.prototype._postRowProcessing=function(r,xml){
 var rId = xml.getAttribute("id")
 var xstyle = xml.getAttribute("style");

 
 var udCol = this.xmlLoader.doXPath("./userdata",xml);
 if(udCol.length>0){
 this.UserData[rId] = new Hashtable();
 for(var j=0;j<udCol.length;j++){
 this.UserData[rId].put(udCol[j].getAttribute("name"),udCol[j].firstChild?udCol[j].firstChild.data:"");
}
}

 
 var css1=xml.getAttribute("class");
 if(css1)r.className+=" "+css1;
 

 
 if(xml.getAttribute("locked"))
{
 r._locked=true;
}
 


 
 if(xml.getAttribute("selected")==true){
 this.setSelectedRow(rId,this.selMultiRows,false,xml.getAttribute("call")==true)
}
 
 if(xml.getAttribute("expand")=="1"){
 r.expand = "";
}

 if(xstyle)this.setRowTextStyle(rId,xstyle);

}
 
 dhtmlXGridObject.prototype._fillRowFromXML=function(r,xml,tree,pId){
 var cellsCol = this.xmlLoader.doXPath("./cell",xml);
 var strAr = new Array(0);

 for(var j=0;j<cellsCol.length;j++){
 if(j!=tree)
 strAr[strAr.length] = cellsCol[j].firstChild?cellsCol[j].firstChild.data:"";
 else
 strAr[strAr.length] = pId+"^"+(cellsCol[j].firstChild?cellsCol[j].firstChild.data:"")+"^"+(xml.getAttribute("xmlkids")?"1":"0")+"^"+(cellsCol[j].getAttribute("image")||"leaf.gif");
}

 for(var j=0;j<cellsCol.length;j++){
 css1=cellsCol[j].getAttribute("class");
 if(css1)r.childNodes[j].className+=" "+css1;
}

 this._fillRow(r,strAr);

 return r;
}


 
 dhtmlXGridObject.prototype._innerParse=function(rowsCol,startIndex,tree,pId,i){
 i=i||0;var imax=i+this._ads_count;
 var r=null;
 for(var i;i<rowsCol.length;i++){

 if((pId)||(i<this.rowsBufferOutSize || this.rowsBufferOutSize==0)){

 this._parsing_=true;
 var rId = rowsCol[i].getAttribute("id");
 r=this._fillRowFromXML(this._prepareRow(rId),rowsCol[i],tree,pId);

 if(startIndex){
 r = this._insertRowAt(r,startIndex);
 startIndex++;
}else{
 r = this._insertRowAt(r);
}

 this._postRowProcessing(r,rowsCol[i]);
 this._parsing_=false;
}else{
 var len = this.rowsBuffer[0].length
 this.rowsBuffer[1][len] = rowsCol[i];
 this.rowsBuffer[0][len] = rowsCol[i].getAttribute("id")
}

 if(tree!=-1){
 var rowsCol2 = this.xmlLoader.doXPath("./row",rowsCol[i]);
 if(rowsCol2.length!=0)
 startIndex=this._innerParse(rowsCol2,startIndex,tree,rId);
}



}
 
 if(this.pagingOn && this.rowsBuffer[0].length>0){
 this.changePage(this.currentPage)
}

 if((r)&&(this._checkSCL))
 for(var i=0;i<this.hdr.rows[0].cells.length;i++)
 this._checkSCL(r.childNodes[i]);
 return startIndex;
}


 
 dhtmlXGridObject.prototype.getCheckedRows=function(col_ind){
 var d=new Array();
 for(var i=0;i<this.rowsCol.length;i++){
 if(this.cells3(this.rowsCol[i],col_ind).getValue()!="0")
 d[d.length]=this.rowsCol[i].idd;
}
 return d.join(",");
}
 
 dhtmlXGridObject.prototype._drawTooltip=function(e){
 var c = this.grid.getFirstParentOfType(e?e.target:event.srcElement,'TD');
 if((this.grid.editor)&&(this.grid.editor.cell==c))return true;

 var r = c.parentNode;
 if(r.idd==window.unknown)return true;
 if((this.grid._enbTts)&&(!this.grid._enbTts[c._cellIndex])){
(e?e.target:event.srcElement).title='';
 return true;}

 var ced = this.grid.cells(r.idd,c._cellIndex);

 if(ced)
(e?e.target:event.srcElement).title=ced.getTitle?ced.getTitle():(ced.getValue()||"").toString().replace(/<[^>]*>/gi,"");

 return true;
};

 
 dhtmlXGridObject.prototype.enableCellWidthCorrection=function(size){
 if(_isFF)this._wcorr=parseInt(size);
}


 
dhtmlXGridObject.prototype.getAllItemIds = function(separator){
 var ar = new Array(0)
 for(i=0;i<this.rowsCol.length;i++){
 ar[ar.length]=this.rowsCol[i].idd
}
 for(i=0;i<this.rowsBuffer[0].length;i++){
 ar[ar.length]=this.rowsBuffer[0][i]
}
 return ar.join(separator||",")
}

 
dhtmlXGridObject.prototype.deleteRow = function(row_id,node){
 
 if(!node)
 node = this.getRowById(row_id)
 if(!this.rowsAr[row_id])
 return;
 this.editStop();
 if(typeof(this.onBeforeRowDeleted)=="function" && this.onBeforeRowDeleted(row_id)==false)
 return false;

 if(node!=null){
 if(this.cellType._dhx_find("tree")!=-1)
 this._removeTrGrRow(node);
 if(node.parentNode){
 node.parentNode.removeChild(node);
}
 var ind=this.rowsCol._dhx_find(node);
 if(ind!=-1)
 this.rowsCol._dhx_removeAt(ind);
 else{
 ind = this.rowsBuffer[0]._dhx_find(row_id)
 if(ind>=0){
 this.rowsBuffer[0]._dhx_removeAt(ind)
 this.rowsBuffer[1]._dhx_removeAt(ind)
}

}
 node = null;
}

 for(var i=0;i<this.selectedRows.length;i++)
 if(this.selectedRows[i].idd==row_id)
 this.selectedRows._dhx_removeAt(i);

 this.rowsAr[row_id] = null;
 if(this.onGridReconstructed)
 this.onGridReconstructed();
 if(this.pagingOn){
 this.changePage();
}
 this.setSizes();
 return true;
}



 
dhtmlXGridObject.prototype.preventIECashing=function(mode){
 this.no_cashe = convertStringToBoolean(mode);
 this.XMLLoader.rSeed=this.no_cashe;
}





