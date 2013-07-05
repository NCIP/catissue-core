//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXCombo_imageOption=function(){this.init()};dhtmlXCombo_imageOption.prototype=new dhtmlXCombo_defaultOption;dhtmlXCombo_imageOption.prototype.setValue=function(a){this.value=a.value||"";this.text=a.text||"";this.css=a.css||"";this.img_src=a.img_src||this.getDefImage()};
dhtmlXCombo_imageOption.prototype.render=function(){if(!this.content){this.content=document.createElement("DIV");this.content._self=this;this.content.style.cssText="width:100%; overflow:hidden; "+this.css;var a="";this.img_src!=""&&(a+='<img style="float:left;" src="'+this.img_src+'" />');a+='<div style="float:left">'+this.text+"</div>";this.content.innerHTML=a}return this.content};dhtmlXCombo_imageOption.prototype.data=function(){return[this.value,this.text,this.img_src]};
dhtmlXCombo_imageOption.prototype.DrawHeader=function(a,b,c){var d=document.createElement("DIV");d.style.width=c+"px";d.className="dhx_combo_box";d._self=a;a.DOMelem=d;this._DrawHeaderImage(a,b,c);this._DrawHeaderInput(a,b,c-19);this._DrawHeaderButton(a,b,c);a.DOMParent.appendChild(a.DOMelem)};dhtmlXCombo_imageOption.prototype._DrawHeaderImage=function(a){var b=document.createElement("img");b.className="dhx_combo_option_img";b.style.visibility="hidden";a.DOMelem.appendChild(b);a.DOMelem_image=b};
dhtmlXCombo_imageOption.prototype.RedrawHeader=function(a,b){a.DOMelem_image.style.visibility=b?"hidden":"visible";a.DOMelem_image.src=b?"":this.img_src};dhtmlXCombo_imageOption.prototype.getDefImage=function(){return""};dhtmlXCombo.prototype.setDefaultImage=function(a){dhtmlXCombo_imageOption.prototype.getDefImage=function(){return a}};dhtmlXCombo_optionTypes.image=dhtmlXCombo_imageOption;dhtmlXCombo_checkboxOption=function(){this.init()};dhtmlXCombo_checkboxOption.prototype=new dhtmlXCombo_defaultOption;
dhtmlXCombo_checkboxOption.prototype.setValue=function(a){this.value=a.value||"";this.text=a.text||"";this.css=a.css||"";this.checked=a.checked||0};
dhtmlXCombo_checkboxOption.prototype.render=function(){if(!this.content){this.content=document.createElement("DIV");this.content._self=this;this.content.style.cssText="width:100%; overflow:hidden; "+this.css;var a="";a+=this.checked?'<input style="float:left;" type="checkbox" checked   />':'<input style="float:left;" type="checkbox" />';a+='<div style="float:left">'+this.text+"</div>";this.content.innerHTML=a;this.content.firstChild.onclick=function(a){this.parentNode.parentNode.combo.DOMelem_input.focus();
(a||event).cancelBubble=!0;if(!this.parentNode.parentNode.combo.callEvent("onCheck",[this.parentNode._self.value,this.checked]))return this.checked=!this.checked,!1}}return this.content};dhtmlXCombo_checkboxOption.prototype.data=function(){return[this.value,this.text,this.render().firstChild.checked]};
dhtmlXCombo_checkboxOption.prototype.DrawHeader=function(a,b,c){a.DOMelem=document.createElement("DIV");a.DOMelem.style.width=c+"px";a.DOMelem.className="dhx_combo_box";a.DOMelem._self=a;this._DrawHeaderCheckbox(a,b,c);this._DrawHeaderInput(a,b,c-19);this._DrawHeaderButton(a,b,c);a.DOMParent.appendChild(a.DOMelem)};
dhtmlXCombo_checkboxOption.prototype._DrawHeaderCheckbox=function(a){var b=document.createElement("input");b.type="checkbox";b.className="dhx_combo_option_img";b.style.visibility="hidden";b.onclick=function(c){var d=a.getIndexByValue(a.getActualValue());d!=-1&&(a.setChecked(d,b.checked),a.callEvent("onCheck",[a.getActualValue(),a.optionsArr[d].content.firstChild.checked]));(c||event).cancelBubble=!0};a.DOMelem.appendChild(b);a.DOMelem_checkbox=b};
dhtmlXCombo_checkboxOption.prototype.RedrawHeader=function(a,b){a.DOMelem_checkbox.style.visibility=b?"hidden":"";a.DOMelem_checkbox.checked=b?!1:this.content.firstChild.checked};dhtmlXCombo_optionTypes.checkbox=dhtmlXCombo_checkboxOption;dhtmlXCombo.prototype.getChecked=function(){for(var a=[],b=0;b<this.optionsArr.length;b++)this.optionsArr[b].data()[2]&&a.push(this.optionsArr[b].value);return a};
dhtmlXCombo.prototype.setChecked=function(a,b){this.optionsArr[a].content.firstChild.checked=b!==!1;this._selOption==this.optionsArr[a]&&this._selOption.RedrawHeader(this)};dhtmlXCombo.prototype.setCheckedByValue=function(a,b){return this.setChecked(this.getIndexByValue(a),b)};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/