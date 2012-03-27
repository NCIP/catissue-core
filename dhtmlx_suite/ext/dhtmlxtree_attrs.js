//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.parserExtension={_parseExtension:function(p,a,pid) {this._idpull[a.id]._attrs=a}};dhtmlXTreeObject.prototype.getAttribute=function(id,name){this._globalIdStorageFind(id)
 var t=this._idpull[id]._attrs;return t?t[name]:window.undefined};dhtmlXTreeObject.prototype.setAttribute=function(id,name,value){this._globalIdStorageFind(id)
 var t=(this._idpull[id]._attrs)||{};t[name]=value;this._idpull[id]._attrs=t};
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/