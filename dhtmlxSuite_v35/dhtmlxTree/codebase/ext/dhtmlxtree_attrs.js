//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.parserExtension={_parseExtension:function(a,b){this._idpull[b.id]._attrs=b}};dhtmlXTreeObject.prototype.getAttribute=function(a,b){this._globalIdStorageFind(a);var c=this._idpull[a]._attrs;return c?c[b]:window.undefined};dhtmlXTreeObject.prototype.setAttribute=function(a,b,c){this._globalIdStorageFind(a);var d=this._idpull[a]._attrs||{};d[b]=c;this._idpull[a]._attrs=d};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/