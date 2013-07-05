//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.sortTree=function(c,e,f){var b=this._globalIdStorageFind(c);if(!b)return!1;this._reorderBranch(b,e.toString().toLowerCase()=="asc",convertStringToBoolean(f))};dhtmlXTreeObject.prototype.setCustomSortFunction=function(c){this._csfunca=c};
dhtmlXTreeObject.prototype._reorderBranch=function(c,e,f){var b=[],d=c.childsCount;if(d){for(var g=c.childNodes[0].tr.parentNode,a=0;a<d;a++)b[a]=c.childNodes[a],g.removeChild(b[a].tr);var h=this;e==1?this._csfunca?b.sort(function(a,b){return h._csfunca(a.id,b.id)}):b.sort(function(a,b){return a.span.innerHTML.toUpperCase()>b.span.innerHTML.toUpperCase()?1:a.span.innerHTML.toUpperCase()==b.span.innerHTML.toUpperCase()?0:-1}):this._csfunca?b.sort(function(a,b){return h._csfunca(b.id,a.id)}):b.sort(function(a,
b){return a.span.innerHTML.toUpperCase()<b.span.innerHTML.toUpperCase()?1:a.span.innerHTML.toUpperCase()==b.span.innerHTML.toUpperCase()?0:-1});for(a=0;a<d;a++)g.appendChild(b[a].tr),c.childNodes[a]=b[a],f&&b[a].unParsed?b[a].unParsed.set("order",e?1:-1):f&&b[a].childsCount&&this._reorderBranch(b[a],e,f);for(a=0;a<d;a++)this._correctPlus(b[a]),this._correctLine(b[a])}};
dhtmlXTreeObject.prototype._reorderXMLBranch=function(c){var e=c.getAttribute("order");if(e!="none"){var f=e==1,b=c.childNodes.length;if(b){for(var d=[],g=0,a=0;a<b;a++)c.childNodes[a].nodeType==1&&(d[g]=c.childNodes[a],g++);for(a=b-1;a!=0;a--)c.removeChild(c.childNodes[a]);f?d.sort(function(a,b){return a.getAttribute("text")>b.getAttribute("text")?1:a.getAttribute("text")==b.getAttribute("text")?0:-1}):d.sort(function(a,b){return a.getAttribute("text")<b.getAttribute("text")?1:a.getAttribute("text")==
b.getAttribute("text")?0:-1});for(a=0;a<g;a++)d[a].setAttribute("order",e),c.appendChild(d[a]);c.setAttribute("order","none")}}};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/