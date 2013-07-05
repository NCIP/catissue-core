//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.enableKeyboardNavigation=function(a){if(this._enblkbrd=convertStringToBoolean(a)){if(_isFF){var b=window.getComputedStyle(this.parentObject,null).position;if(b!="absolute"&&b!="relative")this.parentObject.style.position="relative"}this._navKeys=[["up",38],["down",40],["open",39],["close",37],["call",13],["edit",113]];var c=this,b=document.createElement("INPUT");b.className="a_dhx_hidden_input";b.autocomplete="off";if(window._KHTMLrv)b.style.color="white";this.parentObject.appendChild(b);
this.parentObject[_isOpera?"onkeypress":"onkeydown"]=function(a){if(c.callEvent("onKeyPress",[(a||window.event).keyCode,a||window.event]))return c._onKeyDown(a||window.event)};this.parentObject.onclick=function(){(_isFF||_isIE)&&b.select();(window._KHTMLrv||_isOpera)&&b.focus()}}else this.parentObject.onkeydown=null};
dhtmlXTreeObject.prototype._onKeyDown=function(a){if(window.globalActiveDHTMLGridObject&&globalActiveDHTMLGridObject.isActive)return!0;for(var b=this,c=0;c<this._navKeys.length;c++)if(this._navKeys[c][1]==a.keyCode)return this["_onkey_"+this._navKeys[c][0]].apply(this,[this.getSelectedItemId()]),a.preventDefault&&a.preventDefault(),(a||event).cancelBubble=!0,!1;return this._textSearch?this._searchItemByKey(a):!0};
dhtmlXTreeObject.prototype._onkey_up=function(a){var b=this._globalIdStorageFind(a);if(b){var c=this._getPrevVisibleNode(b);c.id!=this.rootId&&(this.focusItem(c.id),this.selectItem(c.id,!1))}};dhtmlXTreeObject.prototype._onkey_down=function(a){var b=this._globalIdStorageFind(a);if(b){var c=this._getNextVisibleNode(b);c.id!=this.rootId&&(this.focusItem(c.id),this.selectItem(c.id,!1))}};dhtmlXTreeObject.prototype._onkey_open=function(a){this.openItem(a)};dhtmlXTreeObject.prototype._onkey_close=function(a){this.closeItem(a)};
dhtmlXTreeObject.prototype._onkey_call=function(a){this.stopEdit?(this.stopEdit(),this.parentObject.lastChild.focus(),this.parentObject.lastChild.focus(),this.selectItem(a,!0)):this.selectItem(this.getSelectedItemId(),!0)};dhtmlXTreeObject.prototype._onkey_edit=function(a){this.editItem&&this.editItem(a)};
dhtmlXTreeObject.prototype._getNextVisibleNode=function(a,b){return!b&&this._getOpenState(a)>0?a.childNodes[0]:a.tr&&a.tr.nextSibling&&a.tr.nextSibling.nodem?a.tr.nextSibling.nodem:a.parentObject?this._getNextVisibleNode(a.parentObject,1):a};dhtmlXTreeObject.prototype._getPrevVisibleNode=function(a){return a.tr&&a.tr.previousSibling&&a.tr.previousSibling.nodem?this._lastVisibleChild(a.tr.previousSibling.nodem):a.parentObject?a.parentObject:a};
dhtmlXTreeObject.prototype._lastVisibleChild=function(a){return this._getOpenState(a)>0?this._lastVisibleChild(a.childNodes[a.childsCount-1]):a};
dhtmlXTreeObject.prototype._searchItemByKey=function(a){if(a.keyCode==8)return this._textSearchString="",!0;var b=String.fromCharCode(a.keyCode).toUpperCase();return b.match(/[A-Z,a-z,0-9\ ]/)?(this._textSearchString+=b,this._textSearchInProgress=!0,(this.getSelectedItemText()||"").match(RegExp("^"+this._textSearchString,"i"))||this.findItem("^"+this._textSearchString,0),this._textSearchInProgress=!1,a.preventDefault&&a.preventDefault(),(a||event).cancelBubble=!0,!1):!0};
dhtmlXTreeObject.prototype.assignKeys=function(a){this._navKeys=a};dhtmlXTreeObject.prototype.enableKeySearch=function(a){if(this._textSearch=convertStringToBoolean(a)){this._textSearchString="";var b=this;this._markItem2=this._markItem;this._markItem=function(a){if(!b._textSearchInProgress)b._textSearchString="";b._markItem2(a)}}};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/