//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXEditor.prototype.initDhtmlxToolbar = function() {this.tb = this.base.attachToolbar();this.tb.setSkin(this.skin);if (this.skin == "dhx_skyblue"){this.tb.base.style.borderLeft = "none";this.tb.base.style.borderRight = "none";this.tb.base.style.top = "-1px"};this.setSizes();this.tb.setIconsPath(this.iconsPath+"dhxeditor_"+this.skin+"/");this._availFonts = new Array("Arial", "Arial Narrow", "Comic Sans MS", "Courier", "Georgia", "Impact", "Tahoma", "Times New Roman", "Verdana");this._initFont = this._availFonts[0];this._xmlFonts = "";for (var q=0;q<this._availFonts.length;q++){var fnt = String(this._availFonts[q]).replace(/\s/g,"_");this._xmlFonts += '<item type="button" id="applyFontFamily:'+fnt+'"><itemText><![CDATA[<img src="'+this.tb.imagePath+'font_'+String(fnt).toLowerCase()+'.gif" border="0" style="width:110px;height:16px;">]]></itemText></item>'};this._availSizes = {"1":"8pt", "2":"10pt", "3":"12pt", "4":"14pt", "5":"18pt", "6":"24pt", "7":"36pt"};this._xmlSizes = "";for (var a in this._availSizes){this._xmlSizes += '<item type="button" id="applyFontSize:'+a+':'+this._availSizes[a]+'" text="'+this._availSizes[a]+'"/>'};this.tbXML = '<toolbar>'+
 
 '<item id="applyH1" type="buttonTwoState" img="h1.gif" imgdis="h4_dis.gif" title="H1"/>'+
 '<item id="applyH2" type="buttonTwoState" img="h2.gif" imgdis="h4_dis.gif" title="H2"/>'+
 '<item id="applyH3" type="buttonTwoState" img="h3.gif" imgdis="h4_dis.gif" title="H3"/>'+
 '<item id="applyH4" type="buttonTwoState" img="h4.gif" imgdis="h4_dis.gif" title="H4"/>'+
 '<item id="separ01" type="separator"/>'+
 
 '<item id="applyBold" type="buttonTwoState" img="bold.gif" imgdis="bold_dis.gif" title="Bold Text"/>'+
 '<item id="applyItalic" type="buttonTwoState" img="italic.gif" imgdis="italic_dis.gif" title="Italic Text"/>'+
 '<item id="applyUnderscore" type="buttonTwoState" img="underline.gif" imgdis="underline_dis.gif" title="Underscore Text"/>'+
 '<item id="applyStrikethrough" type="buttonTwoState" img="strike.gif" imgdis="strike_dis.gif" title="Strikethrough Text"/>'+
 '<item id="separ02" type="separator"/>'+
 
 '<item id="alignLeft" type="buttonTwoState" img="align_left.gif" imgdis="align_left_dis.gif" title="Left Alignment"/>'+
 '<item id="alignCenter" type="buttonTwoState" img="align_center.gif" imgdis="align_center_dis.gif" title="Center Alignment"/>'+
 '<item id="alignRight" type="buttonTwoState" img="align_right.gif" imgdis="align_right_dis.gif" title="Right Alignment"/>'+
 '<item id="alignJustify" type="buttonTwoState" img="align_justify.gif" title="Justified Alignment"/>'+
 '<item id="separ03" type="separator"/>'+
 
 '<item id="applySub" type="buttonTwoState" img="script_sub.gif" imgdis="script_sub.gif" title="Subscript"/>'+
 '<item id="applySuper" type="buttonTwoState" img="script_super.gif" imgdis="script_super_dis.gif" title="Superscript"/>'+
 '<item id="separ04" type="separator"/>'+
 
 '<item id="createNumList" type="button" img="list_number.gif" imgdis="list_number_dis.gif" title="Number List"/>'+
 '<item id="createBulList" type="button" img="list_bullet.gif" imgdis="list_bullet_dis.gif" title="Bullet List"/>'+
 '<item id="separ05" type="separator"/>'+
 
 '<item id="increaseIndent" type="button" img="indent_inc.gif" imgdis="indent_inc_dis.gif" title="Increase Indent"/>'+
 '<item id="decreaseIndent" type="button" img="indent_dec.gif" imgdis="indent_dec_dis.gif" title="Decrease Indent"/>'+
 '<item id="separ06" type="separator"/>'+
 '<item id="clearFormatting" type="button" img="clear.gif" title="Clear Formatting"/>'+
 '</toolbar>';this.tb.loadXMLString(this.tbXML);this._checkAlign = function(alignSelected) {this.tb.setItemState("alignCenter", false);this.tb.setItemState("alignRight", false);this.tb.setItemState("alignJustify", false);this.tb.setItemState("alignLeft", false);if (alignSelected)this.tb.setItemState(alignSelected, true)};this._checkH = function(h) {this.tb.setItemState("applyH1", false);this.tb.setItemState("applyH2", false);this.tb.setItemState("applyH3", false);this.tb.setItemState("applyH4", false);if (h)this.tb.setItemState(h, true)};this._doOnFocusChanged = function(state) {if(!state.h1&&!state.h2&&!state.h3&&!state.h4){var bold = (String(state.fontWeight).search(/bold/i) != -1) || (Number(state.fontWeight) >= 700);this.tb.setItemState("applyBold", bold)}else this.tb.setItemState("applyBold", false);var alignId = "alignLeft";if (String(state.textAlign).search(/center/) != -1) {alignId = "alignCenter"};if (String(state.textAlign).search(/right/) != -1) {alignId = "alignRight"};if (String(state.textAlign).search(/justify/) != -1) {alignId = "alignJustify"};this.tb.setItemState(alignId, true);this._checkAlign(alignId);this.tb.setItemState("applyH1", state.h1);this.tb.setItemState("applyH2", state.h2);this.tb.setItemState("applyH3", state.h3);this.tb.setItemState("applyH4", state.h4);if (window._KHTMLrv){state.sub = (state.vAlign == "sub");state.sup = (state.vAlign == "super")};this.tb.setItemState("applyItalic", (state.fontStyle == "italic"));this.tb.setItemState("applyStrikethrough", state.del);this.tb.setItemState("applySub", state.sub);this.tb.setItemState("applySuper", state.sup);this.tb.setItemState("applyUnderscore", state.u)};this._doOnToolbarClick = function(id) {var action = String(id).split(":");if (this[action[0]] != null){if (typeof(this[action[0]])== "function") {this[action[0]](action[1])}}};this._doOnStateChange = function(itemId, state) {this[itemId]();switch (itemId) {case "alignLeft":
 case "alignCenter":
 case "alignRight":
 case "alignJustify":
 this._checkAlign(itemId);break;case "applyH1":
 case "applyH2":
 case "applyH3":
 case "applyH4":
 this._checkH(itemId);break}};this._doOnBeforeStateChange = function(itemId, state) {if ((itemId == "alignLeft" || itemId == "alignCenter" || itemId == "alignRight" || itemId == "alignJustify")&& state == true) {return false};return true};var that = this;this.tb.attachEvent("onClick", function(id){that._doOnToolbarClick(id)});this.tb.attachEvent("onStateChange", function(id,st){that._doOnStateChange(id,st)});this.tb.attachEvent("onBeforeStateChange", function(id,st){return that._doOnBeforeStateChange(id,st)});this.applyBold = function(){this.runCommand("Bold")};this.applyItalic = function(){this.runCommand("Italic")};this.applyUnderscore = function(){this.runCommand("Underline")};this.applyStrikethrough = function(){this.runCommand("StrikeThrough")};this.alignLeft = function(){this.runCommand("JustifyLeft")};this.alignRight = function(){this.runCommand("JustifyRight")};this.alignCenter = function(){this.runCommand("JustifyCenter")};this.alignJustify = function(){this.runCommand("JustifyFull")};this.applySub = function(){this.runCommand("Subscript")};this.applySuper = function(){this.runCommand("Superscript")};this.applyH1 = function(){this.runCommand("FormatBlock","<H1>")};this.applyH2 = function(){this.runCommand("FormatBlock","<H2>")};this.applyH3 = function(){this.runCommand("FormatBlock","<H3>")};this.applyH4 = function(){this.runCommand("FormatBlock","<H4>")};this.createNumList = function(){this.runCommand("InsertOrderedList")};this.createBulList = function(){this.runCommand("InsertUnorderedList")};this.increaseIndent = function(){this.runCommand("Indent")};this.decreaseIndent = function(){this.runCommand("Outdent")};this.clearFormatting = function() {this.runCommand("RemoveFormat");this.tb.setItemState("applyBold", false);this.tb.setItemState("applyItalic", false);this.tb.setItemState("applyStrikethrough", false);this.tb.setItemState("applySub", false);this.tb.setItemState("applySuper", false);this.tb.setItemState("applyUnderscore", false)};this.getParentByTag = function(node, tag_name){tag_name = tag_name.toLowerCase()
 var p = node
 do{if(tag_name == '' || p.nodeName.toLowerCase()== tag_name) return p
 }while(p = p.parentNode)return node
 };this.isStyleProperty = function(node, tag_name, name, value){tag_name = tag_name.toLowerCase();var n = node;do{if((n.nodeName.toLowerCase()== tag_name)&&(n.style[name]==value)) return true
 }while(n = n.parentNode)return false
 };this.setStyleProperty = function(el,prop){this.style[prop] = false;var n = this.getParentByTag(el,prop);if(n&&(n.tagName.toLowerCase()==prop)) this.style[prop] = true;if(prop == "del")if(this.getParentByTag(el,"strike")&&(this.getParentByTag(el,"strike").tagName.toLowerCase()=="strike")) this.style.del = true}};
//v.2.5 build 91111

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/