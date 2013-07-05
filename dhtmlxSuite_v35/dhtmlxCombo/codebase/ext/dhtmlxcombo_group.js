//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXCombo.prototype.attachChildCombo=function(a,c){if(!this._child_combos)this._child_combos=[];this._has_childen=1;this._child_combos[this._child_combos.length]=a;a.show(0);var b=this,e=arguments.length;this.attachEvent("onChange",function(){for(var d=0;d<b._child_combos.length;d++)b._child_combos[d]==a&&(a.show(1),a.callEvent("onMasterChange",[b.getActualValue(),b]));if(b.getActualValue()=="")b.showSubCombo(b,0);else if(a._xml){if(e==1)c=a._xml;a._xml=b.deleteParentVariable(c);a._xml+=(a._xml.indexOf("?")!=
-1?"&":"?")+"parent="+encodeURIComponent(b.getActualValue())}else c&&(a.clearAll(!0),a.loadXML(c+(c.indexOf("?")!=-1?"&":"?")+"parent="+encodeURIComponent(b.getActualValue())))})};
dhtmlXCombo.prototype.setAutoSubCombo=function(a,c){arguments.length==1&&(c="subcombo");if(this._parentCombo)b=new dhtmlXCombo(this._parentCombo.DOMParent,c,this._parentCombo.DOMelem.style.width),b._parentCombo=this._parentCombo;else{var b=new dhtmlXCombo(this.DOMParent,c,this.DOMelem.style.width);b._parentCombo=this}if(this._filter)b._filter=1;if(this._xml&&(b._xml=arguments.length>0?a:this._xml,a=b._xml,b._autoxml=this._autoxml,this._xmlCache))b._xmlCache=[];this.attachChildCombo(b,a);return b};
dhtmlXCombo.prototype.detachChildCombo=function(a){for(var c=0;c<this._child_combos.length;c++)this._child_combos.splice(c,1);a.show(1)};dhtmlXCombo.prototype.showSubCombo=function(a,c){if(a._child_combos)for(var b=0;b<a._child_combos.length;b++)a._child_combos[b].show(c),a.showSubCombo(a._child_combos[b],0)};dhtmlXCombo.prototype.deleteParentVariable=function(a){return a=a.replace(/parent\=[^&]*/g,"").replace(/\?\&/,"?")};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/