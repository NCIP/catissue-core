//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/

dhtmlXGridObject.prototype.setRowspan=function(rowID,colInd,length){var c=this[this._bfs_cells?"_bfs_cells":"cells"](rowID,colInd).cell;var r=this.rowsAr[rowID];if (c.rowSpan && c.rowSpan!=1){var ur=r.nextSibling;for (var i=1;i<c.rowSpan;i++){var tc=ur.childNodes[ur._childIndexes[c._cellIndex+1]]
 var ti=document.createElement("TD");ti.innerHTML="&nbsp;";ti._cellIndex=c._cellIndex;ti._clearCell=true;if (tc)tc.parentNode.insertBefore(ti,tc);else
 tc.parentNode.appendChild(ti);this._shiftIndexes(ur,c._cellIndex,-1);ur=ur.nextSibling}};c.rowSpan=length;if (!this._h2)r=r.nextSibling||this.rowsCol[this.rowsCol._dhx_find(r)+1];else
 r=this.rowsAr[ this._h2.get[r.idd].parent.childs[this._h2.get[r.idd].index+1].id ];var kids=[];for (var i=1;i<length;i++){var ct=null;if (this._fake && !this._realfake)ct=this._bfs_cells3(r,colInd).cell;else
 ct=this.cells3(r,colInd).cell;this._shiftIndexes(r,c._cellIndex,1);if (ct)ct.parentNode.removeChild(ct);kids.push(r);if (!this._h2)r=r.nextSibling||this.rowsCol[this.rowsCol._dhx_find(r)+1];else {var r=this._h2.get[r.idd].parent.childs[this._h2.get[r.idd].index+1];if (r)r=this.rowsAr[ r.id ]}};this.rowsAr[rowID]._rowSpan=this.rowsAr[rowID]._rowSpan||{};this.rowsAr[rowID]._rowSpan[colInd]=kids;if (this._fake && !this._realfake && colInd<this._fake._cCount)this._fake.setRowspan(rowID,colInd,length)
};dhtmlXGridObject.prototype._shiftIndexes=function(r,pos,ind){if (!r._childIndexes){r._childIndexes=new Array();for (var z=0;z<r.childNodes.length;z++)r._childIndexes[z]=z};for (var z=0;z<r._childIndexes.length;z++)if (z>pos)r._childIndexes[z]=r._childIndexes[z]-ind};dhtmlXGridObject.prototype.enableRowspan=function(){this._erspan=true;this.enableRowspan=function(){};this.attachEvent("onAfterSorting",function(){if (this._dload)return;for (var i=1;i<this.obj.rows.length;i++)if (this.obj.rows[i]._rowSpan){var master=this.obj.rows[i];for (var kname in master._rowSpan){var row=master;var kids=row._rowSpan[kname];for (var j=0;j < kids.length;j++){if(row.nextSibling)row.parentNode.insertBefore(kids[j],row.nextSibling);else 
 row.parentNode.appendChild(kids[j]);if (this._fake){var frow=this._fake.rowsAr[row.idd];var fkid=this._fake.rowsAr[kids[j].idd];if(frow.nextSibling)frow.parentNode.insertBefore(fkid,frow.nextSibling);else 
 frow.parentNode.appendChild(fkid);this._correctRowHeight(row.idd)};row=row.nextSibling}}};this.rowsCol=new dhtmlxArray();for (var i=1;i<this.obj.rows.length;i++)this.rowsCol.push(this.obj.rows[i])}) 
 
 this.attachEvent("onXLE",function(a,b,c,xml){var spans=this.xmlLoader.doXPath("//cell[@rowspan]",xml);for (var i=0;i<spans.length;i++){var p=spans[i].parentNode;var rid=p.getAttribute("id");var len=spans[i].getAttribute("rowspan");var ind=0;for (var j=0;j < p.childNodes.length;j++){if (p.childNodes[j].tagName=="cell"){if (p.childNodes[j] == spans[i])break;else
 ind++}};this.setRowspan(rid,ind,len) 
 }})
};
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/