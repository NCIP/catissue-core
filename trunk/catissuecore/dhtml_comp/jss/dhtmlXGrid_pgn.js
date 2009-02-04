/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 
 
dhtmlXGridObject.prototype.enablePaging = function(fl,pageSize,pagesInGrp,parentObj,showRecInfo,recInfoParentObj){
 if(typeof(parentObj)=="string"){
 parentObj = document.getElementById(parentObj);
}
 if(typeof(recInfoParentObj)=="string"){
 recInfoParentObj = document.getElementById(recInfoParentObj);
}
 this.pagingOn = fl;
 this.showRecInfo = showRecInfo;
 this.rowsBufferOutSize = pageSize;
 this.pagingBlock = document.createElement("DIV");
 this.pagingBlock.className = "pagingBlock";
 this.recordInfoBlock = document.createElement("SPAN");
 this.recordInfoBlock.className = "recordsInfoBlock";
 if(parentObj)
 parentObj.appendChild(this.pagingBlock)
 if(recInfoParentObj)
 recInfoParentObj.appendChild(this.recordInfoBlock)
 this.currentPage = 1;
 this.pagesPrefix = this.pagesPrefix||"Result Page: ";
 this.pagesDevider = "&nbsp;|&nbsp;"
 this.pagesInGroup = pagesInGrp;
 this.recordsInfoStr = this.recordsInfoStr||"Results <b>[from]-[to]</b> of [about]<b>[total]</b>";
 this.noRecordStr = this.noRecordStr||"No records found";
}

 
dhtmlXGridObject.prototype.createPagingBlock = function(){
 
 this.pagingBlock.innerHTML = "";
 var self = this;
 var pagesNum = Math.ceil((this.rowsBuffer[0].length+this.rowsCol.length)/this.rowsBufferOutSize);
 if(pagesNum==0){
 this.createRecordsInfo();
 return false;
}
 
 
 if(!this.pagesInGroup || this.pagesInGroup>(pagesNum*2-2)){
 this.pagesInGroup = pagesNum;
}
 
 var startOfGroup =(this.currentPage -(this.currentPage-1)%this.pagesInGroup)||1
 var lastPage = startOfGroup+this.pagesInGroup - 1
 if(this.recordsNoMore){
 lastPage = Math.min(pagesNum,lastPage);
}
 
 
 var prefObj = document.createElement("SPAN");
 prefObj.innerHTML = this.pagesPrefix;
 this.pagingBlock.appendChild(prefObj);
 
 if(startOfGroup!=1){
 var pageMark = document.createElement("SPAN");
 pageMark.innerHTML = "&lt;&nbsp;";
 this.pagingBlock.appendChild(pageMark)
 pageMark.className = "pagingPage";
 pageMark.cp = startOfGroup-1;
 pageMark.onclick = function(){
 self.changePage(this.cp)
}
}
 
 for(var i=startOfGroup;i<=lastPage;i++){
 var pageMark = document.createElement("SPAN");
 pageMark.innerHTML = i;
 
 this.pagingBlock.appendChild(pageMark)
 if(i!=lastPage || !this.recordsNoMore || lastPage!=pagesNum){
 var divider = document.createElement("SPAN");
 divider.innerHTML = this.pagesDevider;
 this.pagingBlock.appendChild(divider)

}
 if(this.currentPage==i){
 pageMark.className = "pagingCurrentPage";
}else{
 pageMark.className = "pagingPage";
 pageMark.cp = i;
 pageMark.onclick = function(){
 self.changePage(this.cp)
}
}
}
 
 if(!this.recordsNoMore || lastPage!=pagesNum){
 var pageMark = document.createElement("SPAN");
 pageMark.innerHTML = "&gt;";
 this.pagingBlock.appendChild(pageMark)
 pageMark.className = "pagingPage";
 pageMark.cp = i;
 pageMark.onclick = function(){
 self.changePage(this.cp)
}
}
 if(this.showRecInfo){
 if(this.recordInfoBlock.parentNode==null){
 this.pagingBlock.appendChild(document.createTextNode(" "))
 this.pagingBlock.appendChild(this.recordInfoBlock)
}
 this.createRecordsInfo();
}
 this.createRecordsInfo();
 if(this._onPaging)
 this._onPaging(pagesNum);
}

 
dhtmlXGridObject.prototype.createRecordsInfo = function(){
 if(this.showRecInfo){
 if(this.recordInfoBlock.parentNode==null){
 this.pagingBlock.appendChild(document.createTextNode(" "))
 this.pagingBlock.appendChild(this.recordInfoBlock)
}
}
 else{
 return false;
}
 
 this.recordInfoBlock.innerHTML = "";
 var rowsNum = this.getRowsNum()
 if(rowsNum==0){
 var outStr = this.noRecordStr;
}else{
 var from =((this.currentPage-1)*this.rowsBufferOutSize)+1;
 var to = Math.min((this.currentPage*this.rowsBufferOutSize),rowsNum)
 var outStr = this.recordsInfoStr.replace(/\[from\]/,from);
 outStr = outStr.replace(/\[to\]/,to);
 outStr = outStr.replace(/\[total\]/,rowsNum);
 if(this.recordsNoMore){
 outStr = outStr.replace(/\[about\]/,"");
}else{
 outStr = outStr.replace(/\[about\]/,"known ");
}
}
 this.recordInfoBlock.innerHTML = outStr;
 
}

 
dhtmlXGridObject.prototype.changePage = function(pageNum){
 if(pageNum<1){
 return false;
}
 if(pageNum)
 this.currentPage = parseInt(pageNum);
 this.createPagingBlock();

 
 var totalRows = this.obj._rowslength();

 for(var i=0;i<totalRows;i++){
 var tmpPar = this.obj._rows(0).parentNode
 tmpPar.removeChild(this.obj._rows(0));
}
 
 var startRowInd = this.currentPage*this.rowsBufferOutSize - this.rowsBufferOutSize
 for(var i=startRowInd;i<(parseInt(startRowInd)+parseInt(this.rowsBufferOutSize));i++){
 var row = this.getRowFromCollection(i);
 if(row){
 
 this.obj.firstChild.appendChild(row)
}else{
 if(startRowInd==i){
 this.changePage(this.currentPage-1)
}
 break;
}
}
 this.setSizes();
 if(this._onPageChanged && startRowInd<i)
 this._onPageChanged(this.currentPage,startRowInd,i-1);
}
 
dhtmlXGridObject.prototype.getRowFromCollection = function(ind){
 var row = this.rowsCol[ind];
 if(!row){
 
 this.addRowsFromBuffer()
 if(this.getRowsNum()>ind){
 row = this.getRowFromCollection(ind)
}
}
 return row;
}

 
dhtmlXGridObject.prototype.setPagePrefix = function(str){
 this.pagesPrefix = str;
}
 
dhtmlXGridObject.prototype.getPagePrefix = function(){
 return this.pagesPrefix;
}
 
dhtmlXGridObject.prototype.setRecordsInfoTemplate = function(str){
 this.recordsInfoStr = str;
}
 
dhtmlXGridObject.prototype.getRecordsInfoTemplate = function(){
 return this.recordsInfoStr;
}
 
dhtmlXGridObject.prototype.setNoRecordsString = function(str){
 this.noRecordStr = str;
}
 
dhtmlXGridObject.prototype.getNoRecordsString = function(){
 return this.noRecordStr;
}
 
dhtmlXGridObject.prototype.enableRecordsInfo = function(fl){
 this.showRecInfo = fl;
}
 
dhtmlXGridObject.prototype.setRecordsInfoParent = function(obj){
 if(!obj)
 if(this.recordInfoBlock.parentNode)
 this.recordInfoBlock.parentNode.removeChild(this.recordInfoBlock)
 else
 obj.appendChild(this.recordInfoBlock);
}
 
dhtmlXGridObject.prototype.setPagingBlockParent = function(obj){
 obj.appendChild(this.pagingBlock);
}
 
dhtmlXGridObject.prototype.setOnPageChanged = function(func){
 this.dhx_attachEvent("_onPageChanged",func);
}
 
dhtmlXGridObject.prototype.setOnPaging = function(func){
 this.dhx_attachEvent("_onPaging",func);
}


 
dhtmlXGridObject.prototype.setPagingWTMode = function(navButtons,navLabel,pageSelect,perPageSelect,labels){
 this._WTDef=[navButtons,navLabel,pageSelect,perPageSelect];
 this._WTlabels=labels||["Results","Records from "," to ","Page ","rows per page"];
}

 
dhtmlXGridObject.prototype.enablePagingWT = function(fl,pageSize,pagesInGrp,parentObjId){
 if(!this._WTDef)this.setPagingWTMode(true,true,true,true);

 var self = this;
 this.enablePaging(fl,pageSize,pagesInGrp);
 
 this.setOnPageChanged(function(page,startRowInd,lastRowInd){
 if(this._WTDef[2]){
 self.aToolBar.enableItem("right");
 self.aToolBar.enableItem("rightabs");
 self.aToolBar.enableItem("left");
 self.aToolBar.enableItem("leftabs");
 if(self.currentPage==self.totalPages){
 self.aToolBar.disableItem("right");
 self.aToolBar.disableItem("rightabs");
}else{
 if(self.currentPage==1){
 self.aToolBar.disableItem("left");
 self.aToolBar.disableItem("leftabs");
}
}
}
 if(this._WTDef[2]){
 var sButton = self.aToolBar.getItem("pages");
 sButton.setSelected(page.toString())
}
 if(this._WTDef[1]){
 var iButton = self.aToolBar.getItem("results");
 iButton.setText(this._WTlabels[1]+(startRowInd+1)+this._WTlabels[2]+(lastRowInd+1));
}
});
 this.setOnPaging(function(pNum){
 if(this._WTDef[2]){
 var pButton = self.aToolBar.getItem("pages")
 pButton.clearOptions();
 for(var i=0;i<pNum;i++){
 pButton.addOption((i+1),this._WTlabels[3]+(i+1))
}
}
 if(this._WTDef[3]){
 var ppButton = self.aToolBar.getItem("perpagenum")
 ppButton.setSelected(self.rowsBufferOutSize.toString())
}
 self.totalPages = pNum;
});
 
 if(!parentObjId.setOnClickHandler)
 this.aToolBar=new dhtmlXToolbarObject(parentObjId,'100%',22,"Grid Output");
 else
 this.aToolBar=parentObjId;
 var f1=function(val){
 switch(this.id){
 case "leftabs":
 self.changePage(1);
 break;
 case "left":
 self.changePage(self.currentPage-1);
 break;
 case "rightabs":
 self.changePage(self.totalPages);
 break;
 case "right":
 self.changePage(self.currentPage+1);
 break;
 case "perpagenum":
 self.rowsBufferOutSize = val;
 self.changePage();
 break;
 case "pages":
 self.changePage(val);
 break;
}
};
 this.aToolBar.showBar();

 
 if(this._WTDef[0]){
 this.aToolBar.addItem(new dhtmlXImageButtonObject(this.imgURL+'ar_left_abs.gif',18,18,f1,'leftabs','To First Page'))
 this.aToolBar.addItem(new dhtmlXImageButtonObject(this.imgURL+'ar_left.gif',18,18,f1,'left','Previous Page'))
}
 if(this._WTDef[1])
 this.aToolBar.addItem(new dhtmlXImageTextButtonObject(this.imgURL+'results.gif',this._WTlabels[0],150,18,0,'results','Found Records'))
 if(this._WTDef[0]){
 this.aToolBar.addItem(new dhtmlXImageButtonObject(this.imgURL+'ar_right.gif',18,18,f1,'right','Previous Page'))
 this.aToolBar.addItem(new dhtmlXImageButtonObject(this.imgURL+'ar_right_abs.gif',18,18,f1,'rightabs','To Last Page'))
}
 if(this._WTDef[2])
 this.aToolBar.addItem(new dhtmlXSelectButtonObject("pages","null","null",f1,120,18,"toolbar_select"));
 if(this._WTDef[3])
 this.aToolBar.addItem(new dhtmlXSelectButtonObject("perpagenum","5,10,15,20,25,30","5"+this._WTlabels[4]+",10"+this._WTlabels[4]+",15"+this._WTlabels[4]+",20"+this._WTlabels[4]+",25"+this._WTlabels[4]+",30"+this._WTlabels[4]+"",f1,120,18,"toolbar_select"));

 
 this.aToolBar.disableItem("results");
 return this.aToolBar;
}

