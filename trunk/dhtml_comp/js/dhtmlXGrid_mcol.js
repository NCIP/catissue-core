/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/ 


 
dhtmlXGridObject.prototype.insertColumn=function(ind,header,type,width,sort,align,valign,hidden,columnColor){
 ind=parseInt(ind);
 if(ind>this._cCount)ind=this._cCount;
 if(!this._cMod)this._cMod=this._cCount;
 this._processAllArrays(this._cCount,ind-1,[(header||"&nbsp;"),(width||100),(type||"ed"),(align||"left"),(valign||""),(sort||"na"),(columnColor||""),(hidden||false),this._cMod,(width||100),1]);
 this._processAllRows("_addColInRow",ind);

 this.setHeaderCol(ind,(header||"&nbsp;"));
 this.hdr.rows[0].cells[ind]
 this._cCount++;
 this._cMod++;
 this.setSizes();
}
 
dhtmlXGridObject.prototype.deleteColumn=function(ind){
 ind=parseInt(ind);
 if(this._cCount==0)return;
 if(!this._cMod)this._cMod=this._cCount;
 if(ind>=this._cCount)return;
 this._processAllArrays(ind,this._cCount-1,[null,null,null,null,null,null,null,null,null,null,null]);
 this._processAllRows("_deleteColInRow",ind);
 this._cCount--;
 this.setSizes();

}

 
dhtmlXGridObject.prototype._processAllRows = function(method,oldInd,newInd){
 var z=this.obj.rows.length;
 for(var i=0;i<z;i++)
 this[method](this.obj.rows[i],oldInd,newInd,i);

 var z=this.hdr.rows.length;
 for(var i=0;i<z;i++)
 this[method](this.hdr.rows[i],oldInd,newInd,i);

 var z=this.rowsCol.length;
 for(var i=0;i<z;i++)
 if((this.rowsCol[i])&&(!this.rowsCol[i].parentNode.tagName))
 this[method](this.rowsCol[i],oldInd,newInd,-1);

 var z=this.rowsBuffer[1].length;
 for(var i=0;i<z;i++)
 if((this.rowsBuffer[1][i])&&(this.rowsBuffer[1][i].tagName=="TR"))
 this[method](this.rowsBuffer[1][i],oldInd,newInd,-1);
}

 
dhtmlXGridObject.prototype._processAllArrays = function(oldInd,newInd,vals){
 var ars=["hdrLabels","cellWidth","cellType","cellAlign","cellVAlign","fldSort","columnColor","_hrrar","_c_order"];
 if(this.cellWidthPX.length)ars[ars.length]="cellWidthPX";
 else ars[ars.length]="cellWidthPC";
 if(this._mCols)ars[ars.length]="_mCols";
 var mode=(oldInd<=newInd);

 if(!this._c_order){
 this._c_order=new Array();
 var l=this._cCount;
 for(var i=0;i<l;i++)
 this._c_order[i]=i;
}

 for(var i=0;i<ars.length;i++)
{
 var t=this[ars[i]];
 if(t){
 if(mode){
 var val=t[oldInd];
 for(var j=oldInd;j<newInd;j++)
 t[j]=t[j+1];
 t[newInd]=val;
}else{
 var val=t[oldInd];
 for(var j=oldInd;j>(newInd+1);j--)
 t[j]=t[j-1];
 t[newInd+1]=val;
}

 if(vals)
 t[newInd+(mode?0:1)]=vals[i];
}
}
}


 
dhtmlXGridObject.prototype.moveColumn = function(oldInd,newInd){
 oldInd=parseInt(oldInd);newInd=parseInt(newInd);
 if(newInd<oldInd)var tInd=newInd+1;
 else var tInd=newInd;

 if((this.onBColM)&&(!this.onBColM(oldInd,tInd)))return false;
 if(oldInd==tInd)return;

 if(this.onAColM)this.onAColM(oldInd,newInd-((newInd<oldInd)?1:0));
 
 this.editStop();
 this._processAllRows("_moveColInRow",oldInd,newInd);
 this._processAllArrays(oldInd,newInd);

 
 if(this.fldSorted)
 this.setSortImgPos(this.fldSorted._cellIndex);

 

 if(this.onAColM)this.onAColM(oldInd,tInd);
};


 
dhtmlXGridObject.prototype._swapColumns = function(cols){
 var z=new Array();
 for(var i=0;i<this._cCount;i++){
 var n=cols[this._c_order[i]];
 if(typeof(n)=="undefined")n="";
 z[i]=n;
}
 return z;
}

 
dhtmlXGridObject.prototype._moveColInRow = function(row,oldInd,newInd){


 var c=row.childNodes[oldInd];
 var ci=row.childNodes[newInd+1];
 if(ci)
 row.insertBefore(c,ci);
 else
 row.appendChild(c);

 for(var i=0;i<row.childNodes.length;i++)
 row.childNodes[i]._cellIndex=row.childNodes[i]._cellIndexS=i;

};
 
dhtmlXGridObject.prototype._addColInRow = function(row,ind,old,mod){
 var c=row.childNodes[ind];
 var z=document.createElement((mod)?"TD":"TH");
 z.style.width=this.cellWidthPX[ind];
 if(c)
 row.insertBefore(z,c);
 else
 row.appendChild(z);

 for(var i=ind;i<row.childNodes.length;i++)
 row.childNodes[i]._cellIndex=row.childNodes[i]._cellIndexS=i;

 if(row.idd){
 this.cells3(row,ind).setValue("");
 z.style.align=this.cellAlign[ind];
 z.style.verticalAlign=this.cellVAlign[ind];
 z.bgColor=this.columnColor[ind];

}
};
 
dhtmlXGridObject.prototype._deleteColInRow = function(row,ind){
 var c=row.childNodes[ind];
 if(c)
 row.removeChild(c);

 for(var i=ind;i<row.childNodes.length;i++)
 row.childNodes[i]._cellIndex=row.childNodes[i]._cellIndexS=i;
};

 
dhtmlXGridObject.prototype.enableColumnMove = function(mode,columns){
 this._mCol=convertStringToBoolean(mode);
 if(typeof(columns)!="undefined")
 this._mCols=columns.split(",");
 if(!this._mmevTrue){
 dhtmlxEvent(this.hdr,"mousedown",this._startColumnMove);
 dhtmlxEvent(document.body,"mousemove",this._onColumnMove);
 dhtmlxEvent(document.body,"mouseup",this._stopColumnMove);
 this._mmevTrue=true;
}
};

dhtmlXGridObject.prototype._startColumnMove = function(e){
 e=e||event;
 var el = e.target||e.srcElement;
 var grid=globalActiveDHTMLGridObject;

 if(!grid){
 var zel=el.parentNode.parentNode.parentNode;
 grid=(zel.grid||zel.parentNode.grid);
}

 el = grid.getFirstParentOfType(el,"TD")
 if(el.style.cursor!="default")return true;
 if((grid)&&(!grid._colInMove)){
 if((!grid._mCols)||(grid._mCols[el._cellIndex]=="true"))
 grid._colInMove=el._cellIndex+1;
}
 return true;
};
dhtmlXGridObject.prototype._onColumnMove = function(e){
 e=e||event;
 var grid=globalActiveDHTMLGridObject;
 if((grid)&&(grid._colInMove)){
 if(typeof(grid._colInMove)!="object"){
 var z=document.createElement("DIV");
 z._aIndex=(grid._colInMove-1);
 z._bIndex=null;
 z.innerHTML=grid.getHeaderCol(z._aIndex);
 z.className="dhx_dragColDiv";
 z.style.position="absolute";
 document.body.appendChild(z);
 grid._colInMove=z;
}
 grid._colInMove.style.left=e.clientX+8+"px";
 grid._colInMove.style.top=e.clientY+8+"px";

 var el = e.target||e.srcElement;
 while((el)&&(typeof(el._cellIndexS)=="undefined"))
 el=el.parentNode;

 if(grid._colInMove._oldHe){
 grid._colInMove._oldHe.className=grid._colInMove._oldHe.className.replace(/columnTarget(L|R)/g,"");
 grid._colInMove._oldHe=null;
 grid._colInMove._bIndex=null;
}
 if(el){
 if(grid.hdr.rows[1]._childIndexes)
 var he=grid.hdr.rows[1].cells[grid.hdr.rows[1]._childIndexes[el._cellIndexS]];
 else
 var he=grid.hdr.rows[1].cells[el._cellIndexS];
 var z=e.clientX-(getAbsoluteLeft(he)-grid.hdrBox.scrollLeft);
 window.status= z/he.offsetWidth+"||"+z+"||"+he.offsetWidth;
 if(z/he.offsetWidth>0.5){
 he.className+=" columnTargetR";
 grid._colInMove._bIndex=el._cellIndexS;
}
 else{
 he.className+=" columnTargetL";
 grid._colInMove._bIndex=el._cellIndexS-1;
}
 grid._colInMove._oldHe=he;
}
}
 return true;
};
dhtmlXGridObject.prototype._stopColumnMove = function(e){
 e=e||event;
 var grid=globalActiveDHTMLGridObject;
 if((grid)&&(grid._colInMove)){
 if(typeof(grid._colInMove)=="object"){
 grid._colInMove.parentNode.removeChild(grid._colInMove);
 if(grid._colInMove._bIndex!=null)
 grid.moveColumn(grid._colInMove._aIndex,grid._colInMove._bIndex);

 if(grid._colInMove._oldHe)
 grid._colInMove._oldHe.className=grid._colInMove._oldHe.className.replace(/columnTarget(L|R)/g,"");
 grid._colInMove._oldHe=null;
 grid._colInMove.grid=null;
}
 grid._colInMove=0;
}
 return true;
};


 
 dhtmlXGridObject.prototype.setOnBeforeColumnMove=function(func){
 this.dhx_attachEvent("onBColM",func);
};

 
 dhtmlXGridObject.prototype.setOnAfterColumnMove=function(func){
 this.dhx_attachEvent("onAColM",func);
};





