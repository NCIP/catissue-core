//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/




function dhtmlXTabBar(parentObject,mode,height){mode=mode||"top"
 dhtmlxEventable(this);this._hrefs = {};this._s={};this._c={};this._s.mode=mode;this._s.scrolls=true;this._s.line_height=(parseInt(height)||20)+3;this._s.skin_line = 1;this._s.tab_margin = 0;this._s.expand = 0;this._s._bMode=(mode=="right"||mode=="bottom")?1:0;this._s._vMode=(mode=="right"||mode=="left")?1:0;this._dx=this._s._vMode?"height":"width";this._dy=this._s._vMode?"width":"height";switch(mode){case "top":
 this._py="top";this._px="left";this._pxc="right";break;case "bottom":
 this._py="bottom";this._px="left";this._pxc="right";break;case "right":
 this._py="right";this._px="top";this._pxc="bottom";break;case "left":
 this._py="left";this._px="top";this._pxc="bottom";break};this._active= null;this._tabs = {};this._content = {};this._href={};this._rows=[];this._s._tabSize=150;this._styles={"default":{left:3, right:3, select_shift:3, select_top:2, margin:1, offset:5, tab_color:"#F4F3EE", data_color:"#F0F8FF" },
 "winbiscarf":{left:18, right:18, select_shift:3, select_top:2, margin:1, offset:5},
 "winscarf":{left:18, right:4, select_shift:3, select_top:2, margin:5, offset:5},
 "modern":{left:5, right:5, select_shift:3, select_top:2, margin:1, offset:5, tab_color:"#F4F3EE", data_color:"#F0F8FF" },
 "silver":{left:7, right:7, select_shift:3, select_top:2, margin:1, offset:5, tab_color:"#F4F3EE", data_color:"#F0F8FF" },
 "dark_blue":{left:2, right:2, select_shift:3, select_top:2, margin:1, offset:5 },
 "glassy_blue":{left:2, right:3, select_shift:3, select_top:2, margin:1, offset:5 },
 
 "dhx_black":{left:2, right:2, select_shift:3, select_top:0, margin:1, offset:5},
 "dhx_blue":{left:2, right:2, select_shift:3, select_top:0, margin:1, offset:5, tab_color:"#F4F3EE", data_color:"#F0F8FF" },
 "dhx_skyblue":{left:3, right:3, select_shift:1, select_top:0, margin:-1, offset:5 , data_color:"white", hover:true }};if (typeof(parentObject)!="object")
 parentObject = document.getElementById(parentObject);this.entBox=parentObject;this.entBox.className+=" dhx_tabbar_zone_"+this._s.mode;if (dhtmlx.image_path)this.setImagePath(dhtmlx.image_path);this.setStyle("default");this.skin = "";this._createSelf();if (_isIE)this.preventIECashing(true);return this};dhtmlXTabBar.prototype={_get_size:function(name,alter){var size = this.entBox.getAttribute(name) || this.entBox.style[name] || (window.getComputedStyle?window.getComputedStyle(this.entBox,null)[name]:(this.entBox.currentStyle?this.entBox.currentStyle[name]:0))
 if ((size||"").indexOf("%")!=-1)
 this.enableAutoReSize(true,true);if (!size||size.indexOf("%")!=-1||size=="auto")
 size=alter+"px";return size},
 
 setStyle:function(name){this.setSkin(name)},
 _getSkin:function(tab){return this._a},
 
 setSkin:function(name){name=name.toLowerCase();if (!this._styles[name])name="default";this._a=this._styles[name];this.skin=name;if (this._tabAll)this._tabAll.className='dhx_tabbar_zone dhx_tabbar_zone_'+this.skin;if (name.indexOf("dhx_")==0) 
 this._s.skin_line=0;if (name.indexOf("dhx_sky")==0) {this._s.line_height=26;this._setRowSizes();if (this._s.expand)this._s.tab_margin = -1;this._s.skin_line_x=true;this._s.skin_line=-3;var r = this._s._rendered;if (r)for (var i=0;i < r.length;i++){r[i].parentNode.removeChild(r[i])};var d1 = document.createElement("DIV");d1.className="dhx_tabbar_lineA";this._tabAll.appendChild(d1);var d2 = document.createElement("DIV");d2.className="dhx_tabbar_lineB";this._tabAll.appendChild(d2);var d3 = document.createElement("DIV");d3.className="dhx_tabbar_lineC";this._tabAll.appendChild(d3);var d4 = document.createElement("DIV");d4.className="dhx_tabbar_lineD";this._tabAll.appendChild(d4);this._getCoverLine();this._s._rendered = [d1,d2,d3,d4];if (this._s.expand){this._conZone.style.borderWidth="0px 0px 0px 0px";this._tabZone.firstChild.style.borderWidth="0px 0px 0px 0px";d3.style.borderWidth="0px 0px 0px 0px";d4.style.left="0px";d3.style.right="0px";d1.style.borderWidth="0px 0px 0px 0px";if (this._s.mode=="top")this._lineA.style.borderWidth="1px 0px 0px 0px";d2.style.left = "1px"
 };var f = function(){this._lineA.style[this._dx]="1px";var _quirks=(_isIE && document.compatMode == "BackCompat");var w = this._tabAll[this._s._vMode?"offsetHeight":"offsetWidth"]+(_quirks?2:0);if (this._lastActive)w=Math.max(w,this._lastActive.parentNode[this._s._vMode?"scrollHeight":"scrollWidth"]);if (w<6)return;d1.style[this._py]=parseInt(this._conZone.style[this._py])-3+"px";d1.style[this._dx]=w-2+"px";d2.style[this._py]=parseInt(this._conZone.style[this._py])-3+"px";d2.style[this._dx]=w-(_quirks?6:4)+(this._s.expand?2:0)+"px";d3.style[this._dy]=parseInt(this._tabZone.style[this._dy])-3+"px";this._lineA.style[this._dx]=w-2+"px"};f.call(this);var bf = this._checkScroll;this._checkScroll=function(){f.apply(this,arguments);bf.apply(this,arguments)};var bs = this._scrollTo;this._scrollTo=function(){bs.apply(this,arguments);f.apply(this,arguments)}};if (this._a.data_color && this._conZone)this._conZone.style.backgroundColor=this._a.data_color},

 
 enableAutoReSize:function(){var self=this;dhtmlxEvent(window,"resize",function(){window.setTimeout(function(){if (self && self._setSizes)self._setSizes()},1) 
 })
 },
 _createSelf:function(){this._tabAll=document.createElement("DIV");this._tabZone=document.createElement("DIV");this._conZone=document.createElement("DIV");this.entBox.appendChild(this._tabAll);this._tabAll.appendChild(this._tabZone);this._tabAll.appendChild(this._conZone);this._tabAll.className='dhx_tabbar_zone dhx_tabbar_zone_'+this.skin;if (this._s._vMode)this._tabAll.className+='V';if (this._s._bMode)this._tabAll.className+='B';this._tabZone.className='dhx_tablist_zone';this._conZone.className='dhx_tabcontent_zone';if (this._a.data_color)this._conZone.style.backgroundColor=this._a.data_color;this._tabZone.onselectstart = function(){return false};this._tabZone.onclick = this._onClickHandler;this._tabZone.onmouseover = this._onMouseOverHandler;this._tabZone[_isFF?"onmouseout":"onmouseleave"] = this._onMouseOutHandler;this._tabZone.tabbar=this;this._createRow()},
 _createRow:function(){var z=document.createElement("DIV");z.className='dhx_tabbar_row';z.tabCount=0;this._tabZone.appendChild(z);this._rows[this._rows.length]=z;this._setRowSizes()},
 _removeRow:function(row){row.parentNode.removeChild(row);var z=[];for (var i=0;i<this._rows.length;i++)if (this._rows[i]!=row)z[z.length]=this._rows[i];this._rows=z},
 _setSizes:function(x,y){var dim=["clientHeight","clientWidth"];if (this._dx!="width")dim.reverse();var _quirks=(_isIE && document.compatMode == "BackCompat");var outerBorder=(this._conZone.offsetWidth-this._conZone.clientWidth);var _h=y||(this.entBox[dim[0]]+(_quirks?outerBorder:0));var _w=x||(this.entBox[dim[1]]+(_quirks?outerBorder:0));var _t=this._rows.length*(this._s.line_height-(this._s.skin_line_x?4:2))+(this._s.skin_line_x?2:0);this._tabZone.style[this._dy]=_t+"px";this._conZone.style[this._dy]=Math.max(0,_h-_t-2-(this._s.skin_line_x?3:0)-this._s.tab_margin)+"px";this._conZone.style[this._dx]=Math.max(0,_w - (this._s.expand?0:2))+"px";this._tabZone.style[this._py]=this._s.tab_margin+"px";this._conZone.style[this._py]=_t+this._s.tab_margin-this._s.skin_line+"px";this._checkScroll();var id = this.getActiveTab();if (id)this.cells(id).activate()},
 _checkScroll:function(){if (this._s._vMode || !this._s.scrolls)return;for (var i=0;i<this._rows.length;i++)if ((this._rows[i].scrollWidth-this._rows[i].offsetWidth)>2)
 this._showScroll(i);else this._hideScroll(i)},

 _showScroll:function(i){if (this._rows[i]._scroll)return;var a=this._rows[i]._scroll=[];var top = Math.max(0,this._s.line_height-23);a[0]=document.createElement("DIV");a[0].style.cssText="background-image:url("+this.imgUrl+this.skin+"/"+this.skin+"_scroll_left.gif);width:20px;height:21px;position:absolute;left:0px;z-index:990;top:"+top+"px;";a[0].className="dhx_tab_scroll_left";this._rows[i].appendChild(a[0]);a[1]=document.createElement("DIV");a[1].style.cssText="background-image:url("+this.imgUrl+this.skin+"/"+this.skin+"_scroll_right.gif);width:20px;height:21px;position:absolute;right:0px;z-index:990;top:"+top+"px;";a[1].className="dhx_tab_scroll_right";this._rows[i].appendChild(a[1])},
 _hideScroll:function(i){if (!this._rows[i]._scroll)return;this._rows[i].removeChild(this._rows[i]._scroll[0])
 this._rows[i].removeChild(this._rows[i]._scroll[1])
 this._rows[i]._scroll=null},

 _setRowSizes:function(){for (var i=0;i<this._rows.length;i++){this._rows[i].style[this._dy]=this._s.line_height+"px";this._rows[i].style[this._py]=i*(this._s.line_height-(this._s.skin_line_x?4:2))-((_isIE && !window.postMessage && this._s._bMode)?0:0)+"px";this._rows[i].style.zIndex=10+i};this._setSizes()},
 _setTabSizes:function(row){var pos=this._a.offset;var px = this._vMode?this._pxc:this._px;for (var i=0;i < row.tabCount;i++){var tab=row.childNodes[i];if (tab.style.display=="none")continue;tab.style[px]=pos-(this._lastActive==tab?this._a.select_shift:0)+"px";pos+=tab._size+this._a.margin}},

 
 addTab:function(id, text, size, position, row){if (!this.skin && dhtmlx.skin)this.setSkin(dhtmlx.skin);row=row||0;for (var i=this._rows.length;i<=row;i++)this._createRow();var z=this._rows[row].tabCount;if ((!position)&&(position!==0)) position=z;var tab=this._createTab(id, text, (size=="*"?10:size||this._s._tabSize));this._addTab(this._rows[row],tab,size,position);this._tabs[id]=tab;this.cells(id).hide();this._checkScroll()},

 
 removeTab:function(id,mode){var tab=this._tabs[id];if (!tab)return;this.cells(id)._dhxContDestruct();this._content[id].parentNode.removeChild(this._content[id]);this._content[id]=null;this._goToAny(tab,mode);var row=tab.parentNode;row.removeChild(tab);row.tabCount--;if ((row.tabCount==0)&&(this._rows.length>1))
 this._removeRow(row);else
 this._setTabSizes(row);delete this._tabs[id];if (this._lastActive==tab)this._lastActive=null;this._setRowSizes()},
 _goToAny:function(tab,mode){if (this._lastActive==tab){if (mode)this.goToNextTab();else this._lastActive=null}},
 _createTab:function(id,text,size){var tab=document.createElement("DIV");tab.className='dhx_tab_element dhx_tab_element_inactive';var thml="<span>"+text+"</span><div></div><div></div><div></div>";if (this._close)thml+="<IMG style='"+this._pxc+":4px;"+this._py+":4px;position:absolute;' src='"+this.imgUrl+this.skin+"/close.png' >"
 tab.innerHTML=thml;tab.setAttribute("tab_id",id);tab._size=parseInt(size);tab.style[this._dx]=parseInt(size)+"px";tab.style[this._dy]=this._s.line_height+"px";tab.style[this._py]=this._a.select_top+"px";tab.skin=this.skin;if (this._a.tab_color)tab.style.backgroundColor=this._a.tab_color;if (this._c[id])tab.childNodes[0].style.cssText=this._c[id].color;this._img_all(tab);if (this._close){var self=this;tab.childNodes[4].onclick=function(e){var id=this.parentNode.getAttribute("tab_id");if (self.callEvent("onTabClose",[id])) self.removeTab(id,true);(e||event).cancelBubble=true}};return tab},
 _img_all:function(tab){var a=this._getSkin(tab);var pf=tab._active?1:4;this._img(tab.childNodes[1],pf,this._px,a.left);this._img(tab.childNodes[2],pf+2,this._pxc,a.right);this._img(tab.childNodes[3],pf+1,this._px,parseInt(tab.style[this._dx])-(a.left+a.right),a.left)},
 _get_img_pos:function(ind){if (this._s._bMode && ind<7)ind=Math.abs(ind-6);ind=-5-ind*45;if (this._s._vMode)return ind+"px 0px";else return "0px "+ind+"px"},
 _img:function(tag,y,pos,a,b){tag.style.backgroundImage="url("+this.imgUrl+this.skin+"/"+this.skin+"_"+this._s.mode+".gif)";tag.style.backgroundPosition=this._get_img_pos(y);tag.style[this._py]="0px";if (pos){tag.style[this._dx]=a+"px";tag.style[pos]=(b||0)+"px"}},
 _addTab:function(row,tab,size,position){var pos=this._a.offset;if (row.tabCount){var last=row.childNodes[row.tabCount-1];var pos=parseInt(last.style[this._s.align?this._pxc:this._px])+parseInt(last._size)+this._a.margin};var next=row.childNodes[position];if (next)row.insertBefore(tab,next)
 else
 row.appendChild(tab);row.tabCount++;if (size=="*"){tab.style.whiteSpace="nowrap";this.adjustTabSize(tab)};tab.style[this._s.align?this._pxc:this._px]=pos+"px";if (position!=row.tabCount-1)this._setTabSizes(row)},
 adjustTabSize:function(tab,size){var a=this._getSkin(a);if (!size){tab.style.fontWeight="bold";size=tab.scrollWidth+10+(this._close?20:0);tab.style.fontWeight=""};tab.style[this._dx]=size+"px";tab._size=size;this._img(tab.childNodes[3],5,this._px,size-(a.left+a.right),a.left)},
 _onMouseOverHandler:function(e){var target = this.tabbar._getTabTarget(e?e.target:event.srcElement);this.tabbar._showHover(target)},
 _onMouseOutHandler:function(e){this.tabbar._showHover()},
 _showHover:function(tab){if (tab==this._lastHower)return;if (this._lastHower && this._lastHower != this._lastActive){var a=this._getSkin(this._lastHower);this._lastHower.className=this._lastHower.className.replace(/[ ]*dhx_tab_hover/gi,"");if (a.hover)this._img_all(this._lastHower);else
 this._img(this._lastHower.childNodes[3],5,this._px,parseInt(this._lastHower.style[this._dx])-(a.left+a.right),a.left);this._lastHower=null};if (tab && ( tab == this._lastActive || tab._disabled)) return;this._lastHower=tab;if(tab){var a=this._getSkin(tab);tab.className+=" dhx_tab_hover";if (a.hover){this._img(tab.childNodes[1],7,this._px,a.left);this._img(tab.childNodes[2],8,this._pxc,a.right)};this._img(tab.childNodes[3],0,this._px,parseInt(tab.style[this._dx])-(a.left+a.right),a.left)}},
 _getTabTarget:function(t){if (!t)return null;while ((!t.className)||(t.className.indexOf("dhx_tab_element")==-1)){if ((t.className)&&(t.className.indexOf("dhx_tabbar_zone")!=-1)) return null;t=t.parentNode;if (!t)return null};return t},
 _onClickHandler:function(e){var src=e?e.target:event.srcElement;var target = this.tabbar._getTabTarget(src);if (target && !target._disabled)this.tabbar._setTabActive(target);else {var tag=null;if (src.className=="dhx_tab_scroll_left"){src.parentNode.scrollLeft=Math.max(0,src.parentNode.scrollLeft-src.parentNode.offsetWidth/2);tag=src;this.tabbar._setTabTop(this.tabbar._lastActive)}else if (src.className=="dhx_tab_scroll_right"){src.parentNode.scrollLeft+=src.parentNode.offsetWidth/2;tag=src.previousSibling;this.tabbar._setTabTop(this.tabbar._lastActive)};if (tag){tag.style.left=tag.parentNode.scrollLeft+"px";if (!_isIE || window.XMLHttpRequest)tag.nextSibling.style.right=tag.parentNode.scrollLeft*(-1)+"px";return false}}},
 _deactivateTab:function(){this._setTabInActive(this._lastActive);this._lastActive=null},
 _setTabInActive:function(tab,mode){if (!tab || tab!=this._lastActive)return true;var a = this._getSkin(tab);var id = tab.getAttribute("tab_id");var px = this._s.align?this._pxc:this._px;tab.className=tab.className.replace("_active","_inactive");tab.style[this._py]=a.select_top+"px";tab.style[px]=parseInt(tab.style[px])+a.select_shift+"px";tab.style[this._dx]=tab._size+"px";tab._active=false;if (a.tab_color)tab.style.backgroundColor=a.tab_color;if (this._c[id])tab.childNodes[0].style.cssText=this._c[id].color;this._img_all(tab);this.cells(id).hide()},
 _setTabActive:function(tab,mode){if (!tab || tab==this._lastActive)return true;var id = tab.getAttribute("tab_id");var last_id = this._lastActive?this._lastActive.getAttribute("tab_id"):null;var a = this._getSkin(tab);if (!mode)if (!this.callEvent("onSelect",[id,last_id])) return;var px = this._s.align?this._pxc:this._px;this._setTabInActive(this._lastActive);tab.className=tab.className.replace("_inactive","_active");tab.style[this._py]="0px";tab.style[px]=parseInt(tab.style[px])-a.select_shift+"px";tab.style[this._dx]=tab._size+a.select_shift*2+"px";tab._active=true;if (a.data_color)tab.style.backgroundColor=a.data_color;if (this._c[id])tab.childNodes[0].style.cssText=this._c[id].scolor;this._img_all(tab);this._setTabTop(tab);this._lastActive=tab;this.cells(id).show();this._scrollTo(tab);return true},
 _scrollTo:function(tab){if (!this._s.scrolls)return;if (tab.offsetLeft<tab.parentNode.scrollLeft || (tab.offsetLeft+tab.offsetWidth)>(tab.parentNode.scrollLeft+tab.parentNode.offsetWidth)){tab.parentNode.scrollLeft = tab.offsetLeft;var tag = tab.parentNode._scroll;if (tag && tag[0]){tag[0].style.left=tag[0].parentNode.scrollLeft+"px";if (!_isIE || window.XMLHttpRequest)tag[1].style.right=tag[1].parentNode.scrollLeft*(-1)+"px"}}},
 _setTabTop:function(tab){var t = this._rows.length-1;for (var i=0;i<this._rows.length;i++)if (this._rows[i]==tab.parentNode){var row=this._rows[i];if (i!=t){this._rows[i]=this._rows[t]
 this._rows[t]=row};var line=this._getCoverLine();row.appendChild(line);line.style[this._dx]="1px";var wh=(this._s._vMode?Math.max(this._tabZone.offsetHeight,row.scrollHeight):Math.max(this._tabZone.offsetWidth,row.scrollWidth));if (wh>0)line.style[this._dx]=wh+"px";tab.style.zIndex=(line._index++);break};this._setRowSizes()},
 _getCoverLine:function(){if (!this._lineA){this._lineA=document.createElement("div");this._lineA.className="dhx_tablist_line";this._lineA.style[this._py]=this._s.line_height-3-(this._s.skin_line_x?1:0)+"px";this._lineA.style[this._dx]="100%";this._lineA._index=1};this._lineA.style.zIndex=(this._lineA._index++);return this._lineA},
 cells:function(id){if (!this._tabs[id])return null;if (!this._content[id]){var d=document.createElement("DIV");d.style.cssText="width:100%;height:100%;visibility:hidden;overflow:hidden;position:absolute;top:0px;left:0px;";d.setAttribute("tab_id",id);d.skin = this.skin;this._conZone.appendChild(d);(new dhtmlXContainer(d)).setContent(d);this._content[id]=d;var self=this;d.show = function(){if (self._s.hide)this.style.display="";this.style.visibility="visible";this.style.zIndex="1";if (self._awdj || self._ahdj){var dim = this._getContentDim();if (self._awdj)self.entBox.style.width=dim[0]+2+"px";if (self._ahdj)self._tabAll.style.height = self.entBox.style.height=dim[1]+self._rows.length*(self._s.line_height-2)+(self._s.expand?0:2)+2+"px";self._setSizes();self._setTabTop(self._lastActive)};this._activate()};d.hide = function(){if (self._s.hide){this.style.visibility="visible";this.style.display="none"}else
 this.style.visibility="hidden";this.style.zIndex=-1;if (self._hrfmode=="iframe")this.attachURL("javascript:false")};d._activate=function(){if (this._delay)this._attachContent.apply(this,this._delay);this.activate();if (self._hrfmode!="iframe")this._delay=null};d.activate=function(){this.adjustContent(this.parentNode,0,0,false,0);d.updateNestedObjects()};d._doOnAttachStatusBar = d.activate;d._doOnAttachMenu = d.activate;d._doOnAttachToolbar = d.activate;d._getContentDim=function(){return [this.mainCont.scrollWidth,this.mainCont.scrollHeight]};d._doOnAttachURL=function(){self.callEvent("onXLE",[]);self.callEvent("onTabContentLoaded",[this.getAttribute("tab_id")])};d._doOnBeforeAttachURL=function(){self.callEvent("onXLS",[])};d.adjustContent(d.parentNode,0,0,false,0)};return this._content[id]},

 
 forceLoad:function(id,href){this.setContentHref(id,href||this._hrefs[id]);this.cells(id)._activate()},
 
 
 

 
 enableAutoSize:function(autoWidth,autoHeight){this._ahdj=convertStringToBoolean(autoHeight);this._awdj=convertStringToBoolean(autoWidth)},
 

 
 clearAll:function(){for (var id in this._tabs)this.removeTab(id,false);var line=this._getCoverLine();if (line.parentNode)line.parentNode.removeChild(line)},

 
 enableTabCloseButton:function(mode){this._close = convertStringToBoolean(mode)}, 
 
 
 preventIECashing:function(mode){this.no_cashe = convertStringToBoolean(mode);if (this.XMLLoader)this.XMLLoader.rSeed=this.no_cashe},

 
 setTabActive:function(id,mode){this._setTabActive(this._tabs[id],mode===false)},

 
 loadXMLString:function(xmlString,call){this.XMLLoader=new dtmlXMLLoaderObject(this._parseXML,this,true,this.no_cashe);this.XMLLoader.waitCall=call||0;this.XMLLoader.loadXMLString(xmlString)},

 
 loadXML:function(url,call){this.callEvent("onXLS",[]);this.XMLLoader=new dtmlXMLLoaderObject(this._parseXML,this,true,this.no_cashe);this.XMLLoader.waitCall=call||0;this.XMLLoader.loadXML(url)},
 _parseXML:function(that,a,b,c,obj){that.clearAll();var selected="";if (!obj)obj=that.XMLLoader;var atop=obj.getXMLTopNode("tabbar");var arows = obj.doXPath("//row",atop);var acs=atop.getAttribute("tabstyle");if (acs)that.setStyle(acs);that._hrfmode=atop.getAttribute("hrefmode")||that._hrfmode;that._a.margin =parseInt((atop.getAttribute("margin")||that._a.margin),10);acs=atop.getAttribute("align");if (acs)that._s.align = (acs=="right"||acs=="bottom");that._a.offset = parseInt((atop.getAttribute("offset")||that._a.offset),10);acs=atop.getAttribute("skinColors");if (acs)that.setSkinColors(acs.split(",")[0],acs.split(",")[1]);for (var i=0;i<arows.length;i++){var atabs = obj.doXPath("./tab",arows[i]);for (var j=0;j<atabs.length;j++){var width=atabs[j].getAttribute("width");var name=that._getXMLContent(atabs[j]);var id=atabs[j].getAttribute("id");that.addTab(id,name,width,"",i);if (atabs[j].getAttribute("selected")) selected=id;if (that._hrfmode)that.setContentHref(id,atabs[j].getAttribute("href"));else
 for (var k=0;k<atabs[j].childNodes.length;k++){var cont=atabs[j].childNodes[k];if (cont.tagName=="content"){if (cont.getAttribute("id"))
 that.setContent(id,cont.getAttribute("id"));else
 that.setContentHTML(id,that._getXMLContent(cont))}}}};if (selected)that.setTabActive(selected);that.callEvent("onXLE",[])},
 adjustOuterSize:function(){this._setSizes()},
 _getXMLContent:function(node){var text="";for (var i=0;i<node.childNodes.length;i++){var z=node.childNodes[i];text+=(z.nodeValue===null?"":z.nodeValue)};return text},

 
 enableContentZone:function(mode){this._conZone.style.display=((convertStringToBoolean(mode))?"":"none");this._setSizes()},

 enableForceHiding:function(mode){this._s.hide=convertStringToBoolean(mode)},


 
 setSize:function(x,y){this.entBox.style.width=x+"px";this.entBox.style.height=y+"px";this._setSizes()},

 
 setSkinColors:function(a,b){if (a)this._a.tab_color=a;if (b)this._a.data_color=b;this._conZone.style.backgroundColor=b},

 
 setCustomStyle:function(id,color,scolor,style){var str="";this._c[id]={color:(";"+(color?("color:"+color+";"):"")+(style||"")),
 scolor:(";"+(scolor?("color:"+scolor+";"):"")+(style||""))
 };if (this._tabs[id])this._tabs[id].childNodes[0].style.cssText=((this._tabs[id]==this._lastActive)?this._c[id].scolor:this._c[id].color)},

 
 setImagePath:function(url){this.imgUrl=url},
 getNext:function(tab,alt){alt=alt||"nextSibling";var next=tab[alt];if (next && next.className.indexOf("dhx_tab_element")==-1) next=null;if (!next && tab.parentNode[alt])next=tab.parentNode[alt].childNodes[0];return next||tab},

 
 goToNextTab:function(tab){do {tab=this.getNext(tab||this._lastActive)}while (!this._setTabActive(tab))},

 
 goToPrevTab:function(tab){do {tab=this.getNext((tab||this._lastActive),"previousSibling")}while (!this._setTabActive(tab))},

 
 disableTab:function(id){this._tabs[id]._disabled=true;this._tabs[id].style.color="silver"},

 
 enableTab:function(id){this._tabs[id]._disabled=false;this._tabs[id].style.color=""},

 
 showTab:function(id){var tab=this._tabs[id];tab.style.display="";this._setTabSizes(tab.parentNode)},

 
 hideTab:function(id){var tab=this._tabs[id];tab.style.display="none";if (tab == this._lastActive)this.goToNextTab();this._setTabSizes(tab.parentNode)},

 
 getActiveTab:function(){if (!this._lastActive)return null;return this._lastActive.getAttribute("tab_id")},

 
 setLabel:function(id,text){this._tabs[id].firstChild.innerHTML=text},

 getLabel:function(id){return this._tabs[id].firstChild.innerHTML}, 

 
 setOffset:function(n){this._a.offset=n*1},

 enableScroll:function(mode){this._s.scrolls=convertStringToBoolean(mode)},

 
 setMargin:function(n){this._a.margin=n*1},

 
 setAlign:function(n){this._s.align=(n=="bottom"||n=="right")},

 tabWindow:function(tab_id){return (this._content[tab_id]?this._content[tab_id]._frame.contentWindow:null)},
 
 setContentHTML:function(id,value){this.cells(id).attachHTMLString(value)},

 
 setContent:function(id,value){this.cells(id).attachObject(value);this.cells(id).activate()},

 
 setHrefMode:function(mode){this._hrfmode=mode}, 

 
 setContentHref:function(id,href){this._hrefs[id]=href;switch (this._hrfmode){case "iframes":
 this.cells(id).attachURL(href);break;case "iframe":
 case "iframes-on-demand":
 this.cells(id)._delay=["url",href,false];break;case "ajax":
 var cell=this.cells(id);var that=this;cell._delay=["urlajax",href,true];cell.attachHTMLStringA=cell.attachHTMLString;cell.attachHTMLString=function(str,xml){if (xml)str=that._getXMLContent(xml.doXPath("//content")[0]);return this.attachHTMLStringA(str)};break;case "ajax-html":
 this.cells(id)._delay=["urlajax",href,true];break};if (this._tabs[id]==this._lastActive)this.cells(id).show(true)},



 normalize:function(limit,full){limit=limit||this._tabZone.offsetWidth;function correct_size(tab,i){tabs[i]._size=tabs[i]._size+prev_size-size;tab.adjustTabSize(tabs[i],tabs[i]._size)};var tabs=[];for (var j=0;j<this._rows.length;j++)for (var i=0;i<this._rows[j].tabCount;i++)tabs.push(this._rows[j].removeChild(this._rows[j].childNodes[0]));this._tabZone.innerHTML="";this._rows=[];var t = this._lastActive;this._lastActive=null;this._createRow();var row=0;var size=this._a.offset;var prev_size=Infinity;var last_tab=null;var i=0;for (i;i<tabs.length;i++)if ((size + tabs[i]._size + this._a.margin)< limit){this._rows[row].appendChild(tabs[i]);this._rows[row].tabCount++;size+=tabs[i]._size + this._a.margin}else {if (full && size<prev_size)correct_size(this,i-1);this._createRow();i--;row++;prev_size=size;size=this._a.offset;continue};if (full && size<prev_size && prev_size!=Infinity)correct_size(this,i-1);for (var j=0;j < this._rows.length;j++)this._setTabSizes(this._rows[j]);this._setSizes();if (this._lastActive=t)this._setTabTop(this._lastActive)},

 showInnerScroll:function(){for (var i in this._tabs)this.cells(i).dhxcont.mainCont.style.overflow="auto"},
 
 
 getNumberOfTabs:function (){var tc = 0;for(var i=0;i<this._rows.length;i++)tc+=this._rows[i].tabCount;return tc}};if (!window.dhtmlXContainer){window.dhtmlXContainer=function(obj) {var that = this;this.obj = obj;this.dhxcont = null;this.setContent = function(data) {this.dhxcont = data;this.dhxcont.innerHTML = "<div id='dhxMainCont' class='dhxcont_main_content'></div>"+
 "<div id='dhxContBlocker' class='dhxcont_content_blocker' style='display: none;'></div>";this.dhxcont.mainCont = this.dhxcont.childNodes[0];this.obj.dhxcont = this.dhxcont};this.obj._genStr = function(w) {var s = "";var z = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";for (var q=0;q<w;q++){s = s + z.charAt(Math.round(Math.random() * z.length))};return s};this.obj.setMinContentSize = function(w, h) {this._minDataSizeW = w;this._minDataSizeH = h};this.obj.moveContentTo = function(cont) {cont.updateNestedObjects()};this.obj.adjustContent = function(parentObj, offsetTop, marginTop, notCalcWidth, offsetBottom) {this.dhxcont.style.top = offsetTop+"px";this.dhxcont.style.left = "0px";if (notCalcWidth == true){}else {this.dhxcont.style.width = parentObj.clientWidth+"px"};var px = parentObj.clientHeight-offsetTop;if (px < 0){px = 0};this.dhxcont.style.height = px+(marginTop!=null?marginTop:0)+"px";if (notCalcWidth == true){}else {if (this.dhxcont.offsetWidth > parentObj.clientWidth){this.dhxcont.style.width = Math.max(0,parentObj.clientWidth*2-this.dhxcont.offsetWidth)+"px"}};if (this.dhxcont.offsetHeight > parentObj.clientHeight - offsetTop){var px = (parentObj.clientHeight-offsetTop)*2-this.dhxcont.offsetHeight;if (px < 0){px = 0};this.dhxcont.style.height = px+"px"};if (offsetBottom){if (!isNaN(offsetBottom)) this.dhxcont.style.height = parseInt(this.dhxcont.style.height)-offsetBottom+"px"};if (this._minDataSizeH != null){if (parseInt(this.dhxcont.style.height)< this._minDataSizeH) {this.dhxcont.style.height = this._minDataSizeH+"px"}};if (this._minDataSizeW != null){if (parseInt(this.dhxcont.style.width)< this._minDataSizeW) {this.dhxcont.style.width = this._minDataSizeW+"px"}};if (notCalcWidth == true){}else {this.dhxcont.mainCont.style.width = this.dhxcont.clientWidth+"px"};var menuOffset = (this.menu!=null?(!this.menuHidden?this.menuHeight:0):0);var toolbarOffset = (this.toolbar!=null?(!this.toolbarHidden?this.toolbarHeight:0):0);var statusOffset = (this.sb!=null?(!this.sbHidden?this.sbHeight:0):0);this.dhxcont.mainCont.style.height = this.dhxcont.clientHeight-menuOffset-toolbarOffset-statusOffset+"px"};this.obj.updateNestedObjects = function() {};this.obj.attachObject = function(obj, autoSize) {if (typeof(obj)== "string") {obj = document.getElementById(obj)};if (autoSize){obj.style.visibility = "hidden";obj.style.display = "";var objW = obj.offsetWidth;var objH = obj.offsetHeight};this._attachContent("obj", obj);if (autoSize && this._isWindow){obj.style.visibility = "visible";this._adjustToContent(objW, objH)}};this.obj.appendObject = function(obj) {if (typeof(obj)== "string") {obj = document.getElementById(obj)};this._attachContent("obj", obj, true)};this.obj.attachHTMLString = function(str) {this._attachContent("str", str);var z=str.match(/<script[^>]*>[^\f]*?<\/script>/g)||[];for (var i=0;i<z.length;i++){var s=z[i].replace(/<([\/]{0,1})script[^>]*>/g,"")
 if (window.execScript)window.execScript(s);else window.eval(s)}};this.obj.attachURL = function(url, ajax) {this._attachContent((ajax==true?"urlajax":"url"), url, false)};this.obj._attachContent = function(type, obj, append) {if (append !== true){while (that.dhxcont.mainCont.childNodes.length > 0){that.dhxcont.mainCont.removeChild(that.dhxcont.mainCont.childNodes[0])}};if (type == "url"){var fr = document.createElement("IFRAME");fr.frameBorder = 0;fr.border = 0;fr.style.width = "100%";fr.style.height = "100%";that.dhxcont.mainCont.appendChild(fr);fr.src = obj;this._frame = fr;if (this._doOnAttachURL)this._doOnAttachURL(true)}else if (type == "urlajax"){var t = this;var xmlParser=function(){t.attachHTMLString(this.xmlDoc.responseText,this);if (t._doOnAttachURL)t._doOnAttachURL(false);this.destructor()};var xmlLoader = new dtmlXMLLoaderObject(xmlParser, window);xmlLoader.loadXML(obj);if (t._doOnBeforeAttachURL)t._doOnBeforeAttachURL(false)}else if (type == "obj"){that.dhxcont._frame = null;that.dhxcont.mainCont.appendChild(obj);that.dhxcont.mainCont.style.overflow = (append===true?"auto":"hidden");obj.style.display = ""}else if (type == "str"){that.dhxcont._frame = null;that.dhxcont.mainCont.innerHTML = obj}};this.obj._dhxContDestruct = function() {}}};(function(){dhtmlx.extend_api("dhtmlXTabBar",{_init:function(obj){return [obj.parent,obj.mode,obj.height]},
 tabs:"tabs",
 skin:"setSkin",
 offset:"setOffset",
 margin:"setMargin",
 image_path:"setImagePath",
 href_mode:"setHrefMode",
 align:"setAlign",
 xml:"loadXML",
 close_button:"enableTabCloseButton",
 scroll:"enableScroll",
 forced:"enableForceHiding",
 content_zone:"enableContentZone",
 size_by_content:"enableAutoSize",
 auto_size:"enableAutoReSize"
 },{tabs:function(arr){for (var i=0;i<arr.length;i++){var t=arr[i];this.addTab(t.id,t.label, t.width, t.index, t.row);if (t.active)this.setTabActive(t.id)}}})})();
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/