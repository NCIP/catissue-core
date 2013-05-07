//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/




 dhtmlXTreeObject.prototype.enableLoadingItem=function(text) {this.attachEvent("onXLS",this._showFakeItem);this.attachEvent("onXLE",this._hideFakeItem);this._tfi_text=text||"Loading..."};dhtmlXTreeObject.prototype._showFakeItem=function(tree,id) {if ((id===null)||(this._globalIdStorageFind("fake_load_xml_"+id))) return;var temp = this.XMLsource;this.XMLsource=null;this.insertNewItem(id,"fake_load_xml_"+id,this._tfi_text);this.XMLsource=temp};dhtmlXTreeObject.prototype._hideFakeItem=function(tree,id) {if (id===null)return;this.deleteItem("fake_load_xml_"+id)};
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/