//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.enableLoadingItem=function(b){this.attachEvent("onXLS",this._showFakeItem);this.attachEvent("onXLE",this._hideFakeItem);this._tfi_text=b||"Loading..."};dhtmlXTreeObject.prototype._showFakeItem=function(b,a){if(!(a===null||this._globalIdStorageFind("fake_load_xml_"+a))){var c=this.XMLsource;this.XMLsource=null;this.insertNewItem(a,"fake_load_xml_"+a,this._tfi_text);this.XMLsource=c}};
dhtmlXTreeObject.prototype._hideFakeItem=function(b,a){a!==null&&this.deleteItem("fake_load_xml_"+a)};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/