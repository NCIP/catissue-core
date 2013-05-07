/*
Copyright Scand LLC http://www.scbr.com
To use this component please contact info@scbr.com to obtain license

*/
 
 dhtmlXGridObject.prototype.enableDynamicLoading = function(url,limit){
 this._dload = url;
 this._dInc=12;
 this.limit=limit;
 this.multiLine=false;
 this.obj.className+=" row20px";
 this._dloadSize=Math.floor(parseInt(this.entBox.style.height)/20)+2;

 this._fastAddRowSpacer(0,this.limit*20);
 this._askRealRows();
}

 dhtmlXGridObject.prototype._askRealRows=function(){
 
 var gi=Math.floor(this.objBox.scrollTop/20);
 
 
 if(gi>(this.limit-this._dloadSize))gi=this.limit-this._dloadSize;

 var size=gi+this._dloadSize;
 if(size>this.limit)size=this.rowsCol.length;

 for(var j=gi;j<size;j++)
 if((!this.rowsCol[j])||(this.rowsCol[j]._rLoad)){
 var loader = new dtmlXMLLoaderObject(this._askRealRows2,this);
 loader.loadXML(this._dload+((this._dload.indexOf("?")!=-1)?"&":"?")+"posStart="+gi+"&count="+(size-gi));

 return;
}
}
 dhtmlXGridObject.prototype._askRealRows2=function(obj,xml,c,d,e){
 var top=e.getXMLTopNode("rows");
 var rows=e.doXPath("//rows/row",top);

 var j=parseInt(top.getAttribute("pos"));
 for(var i=0;i<rows.length;i++){

 if((!obj.rowsCol[i+j])||(obj.rowsCol[i+j]._sRow))
 obj._splitRowAt(i+j);

 if(obj.rowsCol[i+j]._rLoad){
 

 var cellsCol = e.doXPath("./cell",rows[i]);
 for(var k=0;k<cellsCol.length;k++){
 obj.cells3(obj.rowsCol[i+j],k).setValue(cellsCol[k].firstChild?cellsCol[k].firstChild.data:"");
}
 obj.rowsCol[i+j]._rLoad=false;
 obj.changeRowId(obj.rowsCol[i+j].idd,rows[i].getAttribute("id"));
}
}
}

 dhtmlXGridObject.prototype._splitRowAt=function(ind){
 var id='temp_dLoad_'+this._dInc;
 this._dInc++;
 var z=this.rowsCol[ind];
 if(!z)
{
 

 var ind2=this._findSParent(ind);
 var delta=ind2[1]-(ind-ind2[0])*20;
 this._fixHeight(this.rowsCol[ind2[0]],delta);


 var z2=this._fastAddRow(id,ind,true,ind2[0])
 z2._sRow=true;

 this._fixHeight(z2,-1*((ind2[1]-(ind-ind2[0])*20)-20));
 return this._splitRowAt(ind);
}
 else
 if(z._sRow){
 


(this._fastAddRow(id,ind,true))._rLoad=true;
 this.rowsCol[ind+1]=z;
 this._fixHeight(z,20);
 if(ind==0)this.setSizes();
}
}
 dhtmlXGridObject.prototype._findSParent=function(ind){
 for(var i=ind-1;i>=0;i--){
 if(this.rowsCol[i])return [i,(parseInt(this.rowsCol[i].style.height))];
}
}
 dhtmlXGridObject.prototype._fixHeight=function(z,delta){
 var x=parseInt(z.style.height||20)-delta;
 if(x==20){z._sRow=false;z._rLoad=true;}

 z.style.height=x+"px";
 var n=z.childNodes.length;
 for(var i=0;i<n;i++)
 z.childNodes[i].style.height=x+"px";
}

 dhtmlXGridObject.prototype._fastAddRowSpacer=function(ind,height){

 var id='temp_dLoad_'+this._dInc;
 this._dInc++;

 var z=this._fastAddRow(id,ind);
 z.style.height=height+"px";
 var n=z.childNodes.length;
 for(var i=0;i<n;i++)
 z.childNodes[i].style.height=height+"px";

 z._sRow=true;
}



 dhtmlXGridObject.prototype._fastAddRow=function(id,ind,nonshift,ind2){
 var n=this.hdr.rows[0].cells.length;
 var z=document.createElement("TR");

 z.idd = id;
 z.grid = this;
 if(this._cssEven){
 if(ind%2==1)z.className+=" "+this._cssUnEven;
 else z.className+=" "+this._cssEven;
}
 for(var i=0;i<n;i++){
 var c=document.createElement("TD");
 c.innerHTML="&nbsp;";

 if(this._enbCid)
 c.id="c_"+r.idd+"_"+i;
 c._cellIndex = i;
 if(this.dragAndDropOff)this.dragger.addDraggableItem(c,this);
 c.align = this.cellAlign[i];
 c.style.verticalAlign = this.cellVAlign[i];
 c.bgColor = this.columnColor[i] || ""
 if((this._hrrar)&&(this._hrrar[i]))
 c.style.display="none";

 z.appendChild(c);
}





 if((ind2)&&(this.rowsCol[ind2].nextSibling))
 this.rowsCol[ind2].parentNode.insertBefore(z,this.rowsCol[ind2].nextSibling);
 else
{
 if((ind==this.limit)||(this.obj.rows.length==0)||(!this.rowsCol[ind])){
 if(_isKHTML)
 this.obj.appendChild(z);
 else{
 if(!this.obj.firstChild)
 this.obj.appendChild(document.createElement("TBODY"));
 this.obj.childNodes[0].appendChild(z);
}
}
 else
 this.rowsCol[ind2||ind].parentNode.insertBefore(z,this.rowsCol[ind]);
}


 this.rowsAr[id] = z;
 if(!nonshift)
 this.rowsCol._dhx_insertAt(ind,z);
 else
 this.rowsCol[ind]=z;

 return z;

};

