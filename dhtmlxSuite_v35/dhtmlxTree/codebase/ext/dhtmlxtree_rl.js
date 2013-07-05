//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.enableRTL=function(a){var b=convertStringToBoolean(a);if(b&&!this.rtlMode||!b&&this.rtlMode)this.rtlMode=b,this._switchToRTL(this.rtlMode)};
dhtmlXTreeObject.prototype._switchToRTL=function(a){a?(this.allTree.className=this._ltr_line=this.lineArray,this._ltr_min=this.minusArray,this._ltr_plus=this.plusArray,this.lineArray="line2_rtl.gif,line3_rtl.gif,line4_rtl.gif,blank.gif,blank.gif,line1_rtl.gif".split(","),this.minusArray=["minus2_rtl.gif","minus3_rtl.gif","minus4_rtl.gif","minus.gif","minus5_rtl.gif"],this.plusArray=["plus2_rtl.gif","plus3_rtl.gif","plus4_rtl.gif","plus.gif","plus5_rtl.gif"],this.allTree.className="containerTableStyleRTL"):
(this.allTree.className="containerTableStyle",this.lineArray=this._ltr_line,this.minusArray=this._ltr_min,this.plusArray=this._ltr_plus);this.htmlNode.childsCount&&this._redrawFrom(this,this.htmlNode)};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/