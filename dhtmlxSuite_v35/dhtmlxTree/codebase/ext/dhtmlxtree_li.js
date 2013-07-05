//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXTreeObject.prototype.isLocked=function(a){this._locker||this._init_lock();return this._locker[a]==!0};
dhtmlXTreeObject.prototype._lockItem=function(a,b,c){this._locker||this._init_lock();if(b){if(this._locker[a.id]==!0)return;this._locker[a.id]=!0;a.bIm0=a.images[0];a.bIm1=a.images[1];a.bIm2=a.images[2];a.images[0]=this.lico0;a.images[1]=this.lico1;a.images[2]=this.lico2;var d=a.span.parentNode,e=d.previousSibling;this.dragger.removeDraggableItem(d);this.dragger.removeDraggableItem(e)}else{if(this._locker[a.id]!=!0)return;this._locker[a.id]=!1;a.images[0]=a.bIm0;a.images[1]=a.bIm1;a.images[2]=a.bIm2;
d=a.span.parentNode;e=d.previousSibling;this.dragger.addDraggableItem(d,this);this.dragger.addDraggableItem(e,this)}c||this._correctPlus(a)};dhtmlXTreeObject.prototype.lockItem=function(a,b){this._locker||this._init_lock();this._lockOn=!1;var c=this._globalIdStorageFind(a);this._lockOn=!0;this._lockItem(c,convertStringToBoolean(b))};dhtmlXTreeObject.prototype.setLockedIcons=function(a,b,c){this._locker||this._init_lock();this.lico0=a;this.lico1=b;this.lico2=c};
dhtmlXTreeObject.prototype._init_lock=function(){this._locker=[];this._locker_count="0";this._lockOn=!0;this._globalIdStorageFindA=this._globalIdStorageFind;this._globalIdStorageFind=this._lockIdFind;if(this._serializeItem)this._serializeItemA=this._serializeItem,this._serializeItem=this._serializeLockItem,this._serializeTreeA=this.serializeTree,this.serializeTree=this._serializeLockTree;this.setLockedIcons(this.imageArray[0],this.imageArray[1],this.imageArray[2])};
dhtmlXTreeObject.prototype._lockIdFind=function(a,b,c){return!this.skipLock&&!c&&this._lockOn==!0&&this._locker[a]==!0?null:this._globalIdStorageFindA(a,b,c)};dhtmlXTreeObject.prototype._serializeLockItem=function(a){return this._locker[a.id]==!0?"":this._serializeItemA(a)};dhtmlXTreeObject.prototype._serializeLockTree=function(){var a=this._serializeTreeA();return a.replace(/<item[^>]+locked\=\"1\"[^>]+\/>/g,"")};dhtmlXTreeObject.prototype._moveNodeToA=dhtmlXTreeObject.prototype._moveNodeTo;
dhtmlXTreeObject.prototype._moveNodeTo=function(a,b,c){return b.treeNod.isLocked&&b.treeNod.isLocked(b.id)?!1:this._moveNodeToA(a,b,c)};dhtmlXTreeObject.prototype.lockTree=function(a){if(convertStringToBoolean(a))this._initTreeLocker();else if(this._TreeLocker)this._TreeLocker.parentNode.removeChild(this._TreeLocker),this._TreeLocker=null};
dhtmlXTreeObject.prototype._initTreeLocker=function(){if(!this._TreeLocker){this.parentObject.style.overflow="hidden";if(this.parentObject.style.position!="absolute")this.parentObject.style.position="relative";var a=document.createElement("div");a.style.position="absolute";a.style.left="0px";a.style.top="0px";a.className="dhx_tree_opacity";a.style.width=this.allTree.offsetWidth+"px";a.style.backgroundColor="#FFFFFF";a.style.height=this.allTree.offsetHeight+"px";this._TreeLocker=a;this.parentObject.appendChild(this._TreeLocker)}};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/