/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 

 
 dhtmlXGridObject.prototype.enableDragAndDrop=function(mode){
 if(mode=="temporary_disabled"){
 this.dADTempOff=false;
 mode=true;}
 else
 this.dADTempOff=true;

 this.dragAndDropOff=convertStringToBoolean(mode);
};

 
dhtmlXGridObject.prototype.setDragBehavior=function(mode){
 this.dadmodec=this.dadmodefix=0;
 switch(mode){
 case "child": this.dadmode=0;break;
 case "sibling": this.dadmode=1;break;
 case "complex": this.dadmode=2;break;
}};

 
dhtmlXGridObject.prototype._createDragNode=function(htmlObject){
 if(!this.dADTempOff)return null;
 htmlObject.parentObject=new Object();
 htmlObject.parentObject.treeNod=this;

 var z=new Array();
 
 
 z=(this.getSelectedId()||"").split(",");
 var exst=false;
 for(var i=0;i<z.length;i++)
 if(z[i]==htmlObject.parentNode.idd)exst=true;
 if(!exst)
 
 
 z[this.selMultiRows?z.length:0]=htmlObject.parentNode.idd;


 this._dragged=new Array();
 for(var i=0;i<z.length;i++)
 if(this.rowsAr[z[i]]){
 this._dragged[this._dragged.length]=this.rowsAr[z[i]];
 this.rowsAr[z[i]].treeNod=this;
}

 htmlObject.parentObject.parentNode=htmlObject.parentNode;

 var dragSpan=document.createElement('div');
 dragSpan.innerHTML=this.rowToDragElement(htmlObject.parentNode.idd);
 dragSpan.style.position="absolute";
 dragSpan.className="dragSpanDiv";
 return dragSpan;
}



 
dhtmlXGridObject.prototype._createSdrgc=function(){
 this._sdrgc=document.createElement("DIV");
 this._sdrgc.innerHTML="&nbsp;";
 this._sdrgc.className="gridDragLine";
 this.objBox.appendChild(this._sdrgc);
}











 
function dragContext(a,b,c,d,e,f,j,h){
 this.source=a||"grid";
 this.target=b||"grid";
 this.mode=c||"move";
 this.dropmode=d||"child";
 this.sid=e||0;
 this.tid=f||window.unknown;
 this.sobj=j||null;
 this.tobj=h||null;
 return this;
}
 
dragContext.prototype.close=function(){
 this.sobj=null;
 this.tobj=null;
}
 
dragContext.prototype.copy=function(){
 return new dragContext(this.source,this.target,this.mode,this.dropmode,this.sid,this.tid,this.sobj,this.tobj);
 this.sobj=null;
 this.tobj=null;
}
 
dragContext.prototype.set=function(a,b){
 this[a]=b;
 return this;
}
 
dragContext.prototype.uid=function(a,b){
 this.nid=this.sid;
 while(this.tobj.rowsAr[this.nid])
 this.nid=this.nid+((new Date()).valueOf());

 return this;
}
 
dragContext.prototype.data=function(){
 if(this.sobj==this.tobj)
 return this.sobj._getRowArray(this.sobj.rowsAr[this.sid]);
 if(this.source=="tree")
 return this.tobj.treeToGridElement(this.sobj,this.sid,this.tid);
 else
 return this.tobj.gridToGrid(this.sid,this.sobj,this.tobj);
}
 
dragContext.prototype.pid=function(){
 if(this.tid==window.unknown)return window.unknown;
 if(this.target=="treeGrid")
 if(this.dropmode=="child")
 return this.tid;
 else
 return this.tobj.rowsAr[this.tid].parent_id;
}
 
dragContext.prototype.ind=function(){
 if(this.tid==window.unknown)return 0;
 if((this.dropmode=="child")&&(this.target=="treeGrid"))
 this.tobj.openItem(this.tid);
 var ind=this.tobj.rowsCol._dhx_find(this.tobj.rowsAr[this.tid]);

 return(ind+((this.target=="treeGrid")?this.tobj._countBranchLength(ind):1));
}
 
dragContext.prototype.img=function(){
 if(this.target!="grid")
 return this.sobj.getItemImage(this.sid);
 else return null;
}

 
dragContext.prototype.slist=function(){
 var res=new Array();
 for(var i=0;i<this.sid.length;i++)
 res[res.length]=this.sid[i][(this.source=="tree")?"id":"idd"];

 return res.join(",");
}


 
dhtmlXGridObject.prototype._drag=function(sourceHtmlObject,dhtmlObject,targetHtmlObject,innerFlag){
 
 if(this._autoOpenTimer)window.clearTimeout(this._autoOpenTimer);

 
 var r1=targetHtmlObject.parentNode;
 var r2=sourceHtmlObject.parentObject;
 
 if(!r1.grid){r1.grid=this;this.dadmodefix=0;}

 var c=new dragContext(0,0,0,(r1.grid.dadmodec?"sibling":"child"));

 if(r2 && r2.childNodes)
 c.set("source","tree").set("sobj",r2.treeNod).set("sid",c.sobj._dragged);
 else{
 if(r2.treeNod.isTreeGrid())c.set("source","treeGrid");
 c.set("sobj",r2.treeNod).set("sid",c.sobj._dragged);
}


 if(r1.grid.isTreeGrid())
 c.set("target","treeGrid");
 c.set("tobj",r1.grid).set("tid",r1.idd);

 
 
 
 if((c.tobj.dadmode==2)&&(c.tobj.dadmodec==1)&&(c.tobj.dadmodefix<0))
 if(c.tobj.obj._rows(0).idd!=c.tid)c.tid=r1.previousSibling.idd;
 else c.tid=window.unknown;
 
 


 if(c.sobj.dpcpy)c.set("mode","copy");
 c.tobj._clearMove();

 if((c.tobj.dragFunc)&&(!c.tobj.dragFunc(c.slist(),c.tid,c.sobj,c.tobj)))return;

 
 var result=new Array();
 if(typeof(c.sid)=="object"){
 var nc=c.copy();
 for(var i=0;i<c.sid.length;i++){
 nc.tobj._dragRoutine(nc.set("sid",c.sid[i][(c.source=="tree"?"id":"idd")]));
 result[result.lenght]=nc.nid;
}
 nc.close();
}
 else
 c.tobj._dragRoutine(c);

 
 if(c.tobj.dropFunc)
 c.tobj.dropFunc(c.slist(),c.tid,result.join(","),c.sobj,c.tobj);

 c.close();
}


 
dhtmlXGridObject.prototype._dragRoutine=function(c){
 c.uid().tobj.addRow(c.nid,c.data(),c.ind(),c.pid(),c.img());

 if(c.source=="tree"){
 var sn=c.sobj._globalIdStorageFind(c.sid);
 if(sn.childsCount){
 var nc=c.copy().set("tid",c.nid).set("dropmode",c.target=="grid"?"sibling":"child");
 for(var j=0;j<sn.childsCount;j++){
 c.tobj._dragRoutine(nc.set("sid",sn.childNodes[j].id));
 if(c.mode=="move")j--;
}
 nc.close();
}
}
 else{
 c.tobj._copyUserData(c);
 if((c.source=="treeGrid")){
 var snc=c.sobj.loadedKidsHash.get(c.sid);
 if((snc)&&(snc.length)){
 var nc=c.copy().set("tid",c.nid);
 if(c.target=="grid")
 nc.set("dropmode","sibling");
 else{
 nc.tobj.openItem(c.tid);
 nc.set("dropmode","child");
}
 for(var j=0;j<snc.length;j++){
 c.tobj._dragRoutine(nc.set("sid",snc[j].idd));
 if(c.mode=="move")j--;
}
 nc.close();
}
}
}

 if(c.mode=="move"){
 c.sobj[(c.source=="tree")?"deleteItem":"deleteRow"](c.sid);
 if((c.sobj==c.tobj)&&(!c.tobj.rowsAr[c.sid]))c.tobj.changeRowId(c.nid,c.sid);
}
}


 
dhtmlXGridObject.prototype.gridToGrid = function(rowId,sgrid,tgrid){
 var z=new Array();
 for(var i=0;i<sgrid.hdr.rows[0].cells.length;i++)
 z[i]=sgrid.cells(rowId,i).getValue();
 return z;
}

 
dhtmlXGridObject.prototype.checkParentLine=function(node,id){
 if((!id)||(!node))return false;
 if(node.idd==id)return true;
 else return this.checkParentLine(this.getRowById(node.parent_id),id);
}

 
dhtmlXGridObject.prototype._dragIn=function(htmlObject,shtmlObject,x,y){
 if(!this.dADTempOff)return 0;
 var tree=this.isTreeGrid();

 if(htmlObject.parentNode==shtmlObject.parentNode)
 return 0;

 if((!tree)&&((htmlObject.parentNode.nextSibling)&&(htmlObject.parentNode.nextSibling==shtmlObject.parentNode)))
 return 0;

 if((tree)&&((this.checkParentLine(htmlObject.parentNode,shtmlObject.parentNode.idd))))
 return 0;
 var obj=shtmlObject.parentObject?shtmlObject.parentObject:shtmlObject.parentNode;
 if((this.dragInFunc)&&(!this.dragInFunc(obj.idd||obj.id,htmlObject.parentNode.idd,obj.grid||obj.treeNod,htmlObject.parentNode.grid)))
 return 0;

 this._setMove(htmlObject,x,y);

 if((tree)&&(htmlObject.parentNode.expand!="")){
 this._autoOpenTimer=window.setTimeout(new callerFunction(this._autoOpenItem,this),1000);
 this._autoOpenId=htmlObject.parentNode.idd;
}
 else
 if(this._autoOpenTimer)window.clearTimeout(this._autoOpenTimer);

 return htmlObject;
}
 
dhtmlXGridObject.prototype._autoOpenItem=function(e,gridObject){
 gridObject.openItem(gridObject._autoOpenId);
}

 
dhtmlXGridObject.prototype._dragOut=function(htmlObject){
 this._clearMove();
 if(this._autoOpenTimer)window.clearTimeout(this._autoOpenTimer);
}
 
dhtmlXGridObject.prototype._setMove=function(htmlObject,x,y){
 var a1=getAbsoluteTop(htmlObject);
 var a2=getAbsoluteTop(this.objBox);

 if(this.dadmode==2)
{

 var z=y-a1+this.objBox.scrollTop+(document.body.scrollTop||document.documentElement.scrollTop)-2-htmlObject.offsetHeight/2;
 if((Math.abs(z)-htmlObject.offsetHeight/6)>0)
{
 this.dadmodec=1;
 
 if(z<0)this.dadmodefix=-1;else this.dadmodefix=1;
}
 else this.dadmodec=0;
}
 else
 this.dadmodec=this.dadmode;


 
 if((a1-a2-parseInt(this.objBox.scrollTop))>(parseInt(this.objBox.offsetHeight)-50))
 this.objBox.scrollTop=parseInt(this.objBox.scrollTop)+20;
 
 if((a1-a2)<(parseInt(this.objBox.scrollTop)+30))
 this.objBox.scrollTop=parseInt(this.objBox.scrollTop)-20;

 if(this.dadmodec){
 if(!this._sdrgc)this._createSdrgc();
 this._sdrgc.style.display="block";
 this._sdrgc.style.top=a1-a2+((this.dadmodefix>=0)?htmlObject.offsetHeight:0)+"px";
}
 else{
 this._llSelD=htmlObject;
 if(htmlObject.parentNode.tagName=="TR")
 for(var i=0;i<htmlObject.parentNode.childNodes.length;i++)
{
 var z= htmlObject.parentNode.childNodes[i];
 z._bgCol=z.style.backgroundColor;
 z.style.backgroundColor="#FFCCCC";
}
}
}
 
dhtmlXGridObject.prototype._clearMove=function(){
 if(this._sdrgc)this._sdrgc.style.display="none";
 if((this._llSelD)&&(this._llSelD.parentNode.tagName=="TR"))
 for(var i=0;i<this._llSelD.parentNode.childNodes.length;i++)
 this._llSelD.parentNode.childNodes[i].style.backgroundColor=this._llSelD._bgCol;

 this._llSelD=null;
}


 
dhtmlXGridObject.prototype.rowToDragElement=function(gridRowId){
 var out=this.cells(gridRowId,0).getValue();
 return out;
}








 
dhtmlXGridObject.prototype._copyUserData = function(c){
 var z1 = c.sobj.UserData[c.sid];
 var z2 = new Hashtable();
 if(z1){
 z2.keys = z2.keys.concat(z1.keys);
 z2.values = z2.values.concat(z1.values);
}

 c.tobj.UserData[c.tid]=z2;
}



 
dhtmlXGridObject.prototype.moveRow=function(rowId,mode,targetId,targetGrid){
 switch(mode){
 case "row_sibling":
 this.moveRowTo(rowId,targetId,"move","sibling",this,targetGrid);
 break;
 case "up":
 this.moveRowUp(rowId);
 break;
 case "down":
 this.moveRowDown(rowId);
 break;
}
}



 
dhtmlXGridObject.prototype.setDragHandler=function(func){if(typeof(func)=="function")this.dragFunc=func;else this.dragFunc=eval(func);}


 
 

 
dhtmlXGridObject.prototype._nonTrivialNode=function(tree,targetObject,beforeNode,itemObject,z2)
{
 if((tree.dragFunc)&&(!z2))
 if(!tree.dragFunc(itemObject.parentNode.idd,targetObject.id,(beforeNode?beforeNode.id:null),this,tree))return false;

 var gridRowId = itemObject.idd;
 var treeNodeId = gridRowId+(new Date()).getMilliseconds();

 var img=(this.isTreeGrid()?this.getItemImage(gridRowId):"")
 var newone=tree._attachChildNode(targetObject,treeNodeId,this.gridToTreeElement(tree,treeNodeId,gridRowId),"",img,img,img,"","",beforeNode);
 if(this.loadedKidsHash){
 var akids=this.loadedKidsHash.get(gridRowId);
 if(akids)
 for(var i=0;i<akids.length;i++){
 this._nonTrivialNode(tree,newone,0,akids[i],1);
 if(!this.dpcpy)i--;
}
}

 if(!this.dpcpy)this.deleteRow(gridRowId);

 if((tree.dropFunc)&&(!z2))
 tree.dropFunc(treeNodeId,targetObject.id,(beforeNode?beforeNode.id:null),this,tree);
}

 
dhtmlXGridObject.prototype.gridToTreeElement = function(treeObj,treeNodeId,gridRowId){
 return this.cells(gridRowId,0).getValue();
}

 
dhtmlXGridObject.prototype.treeToGridElement = function(treeObj,treeNodeId,gridRowId){
 var w=new Array();
 var z=this.cellType._dhx_find("tree");
 if(z==-1)z=0;
 for(var i=0;i<this.getColumnCount();i++)
 w[w.length]=(i!=z)?(treeObj.getUserData(treeNodeId,this.getColumnId(i))||""):treeObj.getItemText(treeNodeId);
 return w;
}

 
dhtmlXGridObject.prototype.moveRowTo=function(srowId,trowId,mode,dropmode,sourceGrid,targetGrid){
 var c=new dragContext("grid","grid",mode,dropmode||"sibling",srowId,trowId,sourceGrid||grid,targetGrid||grid);
 c.tobj._dragRoutine(c);
 c.close();
}




 
dhtmlXGridObject.prototype.setDropHandler=function(func){if(typeof(func)=="function")this.dropFunc=func;else this.dropFunc=eval(func);};


 
dhtmlXGridObject.prototype.setDragInHandler=function(func){if(typeof(func)=="function")this.dragInFunc=func;else this.dragInFunc=eval(func);};



 
dhtmlXGridObject.prototype.enableMercyDrag=function(mode){this.dpcpy=convertStringToBoolean(mode);};
 
 


