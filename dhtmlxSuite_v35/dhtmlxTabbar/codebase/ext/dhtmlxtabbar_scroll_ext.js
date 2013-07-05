dhtmlXTabBar.prototype.enableScrollerTab = function(){
	var tabbar = this;
	this.attachEvent("onBeforeShowScroll",function(i){
		this._beforeScrollOffect =  this._a.offset;
		this.setOffset(20);
		this._setTabSizes(this._rows[i]);
		window.setTimeout(function(){
			if(!tabbar._tabs["fakeScrollTab"]){
				tabbar._addFakeTab();
			}
		},1);
	});
	this.attachEvent("onBeforeHideScroll",function(i){
		this.removeTab("fakeScrollTab");
		this.setOffset(this._beforeScrollOffect||0);
		this._setTabSizes(this._rows[i]);
	});
	this.attachEvent("onSelect",function(id){
		if(id=="fakeScrollTab")
			window.setTimeout(function(){
				tabbar.goToPrevTab();
				tabbar._clearFakeTabStyles();
			},1);
		return true
	});

	var addTab = this.addTab;
	this._addFakeTab = function(){
		var isButton = this._close;

		if(isButton)
			this.enableTabCloseButton(false);
		addTab.call(this,"fakeScrollTab","",20);
		this.disableTab("fakeScrollTab");
		if(isButton)
			this.enableTabCloseButton(true);
		this._clearFakeTabStyles();
	};
	this.addTab = function(id, text, size, position, row){
		if(this._tabs["fakeScrollTab"])
			this.removeTab("fakeScrollTab");
		addTab.apply(this,arguments);
		row=row||0;
		if(this._rows[row]._scroll)
			this._addFakeTab();
	};
	this._clearFakeTabStyles = function(){
		var tab = this._tabs["fakeScrollTab"];
		for(var i=1;i<4;i++){
			tab.childNodes[i].style.backgroundImage = "none";
		}
		tab.style.backgroundColor = "transparent";
	}
};


