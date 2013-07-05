/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
/*
2012 August 23
*/



/* DHX DEPEND FROM FILE 'assert.js'*/


if (!window.dhtmlx) 
	dhtmlx={};

//check some rule, show message as error if rule is not correct
dhtmlx.assert = function(test, message){
	if (!test)	dhtmlx.error(message);
};
dhtmlx.assert_enabled=function(){ return false; };

//register names of event, which can be triggered by the object
dhtmlx.assert_event = function(obj, evs){
	if (!obj._event_check){
		obj._event_check = {};
		obj._event_check_size = {};
	}
		
	for (var a in evs){
		obj._event_check[a.toLowerCase()]=evs[a];
		var count=-1; for (var t in evs[a]) count++;
		obj._event_check_size[a.toLowerCase()]=count;
	}
};
dhtmlx.assert_method_info=function(obj, name, descr, rules){
	var args = [];
	for (var i=0; i < rules.length; i++) {
		args.push(rules[i][0]+" : "+rules[i][1]+"\n   "+rules[i][2].describe()+(rules[i][3]?"; optional":""));
	}
	return obj.name+"."+name+"\n"+descr+"\n Arguments:\n - "+args.join("\n - ");
};
dhtmlx.assert_method = function(obj, config){
	for (var key in config)
		dhtmlx.assert_method_process(obj, key, config[key].descr, config[key].args, (config[key].min||99), config[key].skip);
};
dhtmlx.assert_method_process = function (obj, name, descr, rules, min, skip){
	var old = obj[name];
	if (!skip)
		obj[name] = function(){
			if (arguments.length !=	rules.length && arguments.length < min) 
				dhtmlx.log("warn","Incorrect count of parameters\n"+obj[name].describe()+"\n\nExpecting "+rules.length+" but have only "+arguments.length);
			else
				for (var i=0; i<rules.length; i++)
					if (!rules[i][3] && !rules[i][2](arguments[i]))
						dhtmlx.log("warn","Incorrect method call\n"+obj[name].describe()+"\n\nActual value of "+(i+1)+" parameter: {"+(typeof arguments[i])+"} "+arguments[i]);
			
			return old.apply(this, arguments);
		};
	obj[name].describe = function(){	return dhtmlx.assert_method_info(obj, name, descr, rules);	};
};
dhtmlx.assert_event_call = function(obj, name, args){
	if (obj._event_check){
		if (!obj._event_check[name])
			dhtmlx.log("warn","Not expected event call :"+name);
		else if (dhtmlx.isNotDefined(args))
			dhtmlx.log("warn","Event without parameters :"+name);
		else if (obj._event_check_size[name] != args.length)
			dhtmlx.log("warn","Incorrect event call, expected "+obj._event_check_size[name]+" parameter(s), but have "+args.length +" parameter(s), for "+name+" event");
	}		
};
dhtmlx.assert_event_attach = function(obj, name){
	if (obj._event_check && !obj._event_check[name]) 
			dhtmlx.log("warn","Unknown event name: "+name);
};
//register names of properties, which can be used in object's configuration
dhtmlx.assert_property = function(obj, evs){
	if (!obj._settings_check)
		obj._settings_check={};
	dhtmlx.extend(obj._settings_check, evs);		
};
//check all options in collection, against list of allowed properties
dhtmlx.assert_check = function(data,coll){
	if (typeof data == "object"){
		for (var key in data){
			dhtmlx.assert_settings(key,data[key],coll);
		}
	}
};
//check if type and value of property is the same as in scheme
dhtmlx.assert_settings = function(mode,value,coll){
	coll = coll || this._settings_check;

	//if value is not in collection of defined ones
	if (coll){
		if (!coll[mode])	//not registered property
			return dhtmlx.log("warn","Unknown propery: "+mode);
			
		var descr = "";
		var error = "";
		var check = false;
		for (var i=0; i<coll[mode].length; i++){
			var rule = coll[mode][i];
			if (typeof rule == "string")
				continue;
			if (typeof rule == "function")
				check = check || rule(value);
			else if (typeof rule == "object" && typeof rule[1] == "function"){
				check = check || rule[1](value);
				if (check && rule[2])
					dhtmlx["assert_check"](value, rule[2]); //temporary fix , for sources generator
			}
			if (check) break;
		}
		if (!check )
			dhtmlx.log("warn","Invalid configuration\n"+dhtmlx.assert_info(mode,coll)+"\nActual value: {"+(typeof value)+"} "+value);
	}
};

dhtmlx.assert_info=function(name, set){ 
	var ruleset = set[name];
	var descr = "";
	var expected = [];
	for (var i=0; i<ruleset.length; i++){
		if (typeof rule == "string")
			descr = ruleset[i];
		else if (ruleset[i].describe)
			expected.push(ruleset[i].describe());
		else if (ruleset[i][1] && ruleset[i][1].describe)
			expected.push(ruleset[i][1].describe());
	}
	return "Property: "+name+", "+descr+" \nExpected value: \n - "+expected.join("\n - ");
};


if (dhtmlx.assert_enabled()){
	
	dhtmlx.assert_rule_color=function(check){
		if (typeof check != "string") return false;
		if (check.indexOf("#")!==0) return false;
		if (check.substr(1).replace(/[0-9A-F]/gi,"")!=="") return false;
		return true;
	};
	dhtmlx.assert_rule_color.describe = function(){
		return "{String} Value must start from # and contain hexadecimal code of color";
	};
	
	dhtmlx.assert_rule_template=function(check){
		if (typeof check == "function") return true;
		if (typeof check == "string") return true;
		return false;
	};
	dhtmlx.assert_rule_template.describe = function(){
		return "{Function},{String} Value must be a function which accepts data object and return text string, or a sting with optional template markers";
	};
	
	dhtmlx.assert_rule_boolean=function(check){
		if (typeof check == "boolean") return true;
		return false;
	};
	dhtmlx.assert_rule_boolean.describe = function(){
		return "{Boolean} true or false";
	};
	
	dhtmlx.assert_rule_object=function(check, sub){
		if (typeof check == "object") return true;
		return false;
	};
	dhtmlx.assert_rule_object.describe = function(){
		return "{Object} Configuration object";
	};
	
	
	dhtmlx.assert_rule_string=function(check){
		if (typeof check == "string") return true;
		return false;
	};
	dhtmlx.assert_rule_string.describe = function(){
		return "{String} Plain string";
	};
	
	
	dhtmlx.assert_rule_htmlpt=function(check){
		return !!dhtmlx.toNode(check);
	};
	dhtmlx.assert_rule_htmlpt.describe = function(){
		return "{Object},{String} HTML node or ID of HTML Node";
	};
	
	dhtmlx.assert_rule_notdocumented=function(check){
		return false;
	};
	dhtmlx.assert_rule_notdocumented.describe = function(){
		return "This options wasn't documented";
	};
	
	dhtmlx.assert_rule_key=function(obj){
		var t = function (check){
			return obj[check];
		};
		t.describe=function(){
			var opts = [];
			for(var key in obj)
				opts.push(key);
			return  "{String} can take one of next values: "+opts.join(", ");
		};
		return t;
	};
	
	dhtmlx.assert_rule_dimension=function(check){
		if (check*1 == check && !isNaN(check) && check >= 0) return true;
		return false;
	};
	dhtmlx.assert_rule_dimension.describe=function(){
		return "{Integer} value must be a positive number";
	};
	
	dhtmlx.assert_rule_number=function(check){
		if (typeof check == "number") return true;
		return false;
	};
	dhtmlx.assert_rule_number.describe=function(){
		return "{Integer} value must be a number";
	};
	
	dhtmlx.assert_rule_function=function(check){
		if (typeof check == "function") return true;
		return false;
	};
	dhtmlx.assert_rule_function.describe=function(){
		return "{Function} value must be a custom function";
	};
	
	dhtmlx.assert_rule_any=function(check){
		return true;
	};
	dhtmlx.assert_rule_any.describe=function(){
		return "Any value";
	};
	
	dhtmlx.assert_rule_mix=function(a,b){
		var t = function(check){
			if (a(check)||b(check)) return true;
			return false;
		};
		t.describe = function(){
			return a.describe();
		};
		return t;
	};

}


/* DHX DEPEND FROM FILE 'dhtmlx.js'*/


/*DHX:Depend assert.js*/

/*
	Common helpers
*/
dhtmlx.version="3.0";
dhtmlx.codebase="./";

//coding helpers

dhtmlx.copy = function(source){
	var f = dhtmlx.copy._function;
	f.prototype = source;
	return new f();
};
dhtmlx.copy._function = function(){};

//copies methods and properties from source to the target
dhtmlx.extend = function(target, source){
	for (var method in source)
		target[method] = source[method];
		
	//applying asserts
	if (dhtmlx.assert_enabled() && source._assert){
		target._assert();
		target._assert=null;
	}
	
	dhtmlx.assert(target,"Invalid nesting target");
	dhtmlx.assert(source,"Invalid nesting source");
	//if source object has init code - call init against target
	if (source._init)	
		target._init();
				
	return target;	
};
dhtmlx.proto_extend = function(){
	var origins = arguments;
	var compilation = origins[0];
	var construct = [];
	
	for (var i=origins.length-1; i>0; i--) {
		if (typeof origins[i]== "function")
			origins[i]=origins[i].prototype;
		for (var key in origins[i]){
			if (key == "_init") 
				construct.push(origins[i][key]);
			else if (!compilation[key])
				compilation[key] = origins[i][key];
		}
	};
	
	if (origins[0]._init)
		construct.push(origins[0]._init);
	
	compilation._init = function(){
		for (var i=0; i<construct.length; i++)
			construct[i].apply(this, arguments);
	};
	compilation.base = origins[1];
	var result = function(config){
		this._init(config);
		if (this._parseSettings)
			this._parseSettings(config, this.defaults);
	};
	result.prototype = compilation;
	
	compilation = origins = null;
	return result;
};
//creates function with specified "this" pointer
dhtmlx.bind=function(functor, object){ 
	return function(){ return functor.apply(object,arguments); };  
};

//loads module from external js file
dhtmlx.require=function(module){
	if (!dhtmlx._modules[module]){
		dhtmlx.assert(dhtmlx.ajax,"load module is required");
		
		//load and exec the required module
		dhtmlx.exec( dhtmlx.ajax().sync().get(dhtmlx.codebase+module).responseText );
		dhtmlx._modules[module]=true;	
	}
};
dhtmlx._modules = {};	//hash of already loaded modules

//evaluate javascript code in the global scoope
dhtmlx.exec=function(code){
	if (window.execScript)	//special handling for IE
		window.execScript(code);
	else window.eval(code);
};

/*
	creates method in the target object which will transfer call to the source object
	if event parameter was provided , each call of method will generate onBefore and onAfter events
*/
dhtmlx.methodPush=function(object,method,event){
	return function(){
		var res = false;
		//if (!event || this.callEvent("onBefore"+event,arguments)){ //not used anymore, probably can be removed
			res=object[method].apply(object,arguments);
		//	if (event) this.callEvent("onAfter"+event,arguments);
		//}
		return res;	//result of wrapped method
	};
};
//check === undefined
dhtmlx.isNotDefined=function(a){
	return typeof a == "undefined";
};
//delay call to after-render time
dhtmlx.delay=function(method, obj, params, delay){
	setTimeout(function(){
		var ret = method.apply(obj,params);
		method = obj = params = null;
		return ret;
	},delay||1);
};

//common helpers

//generates unique ID (unique per window, nog GUID)
dhtmlx.uid = function(){
	if (!this._seed) this._seed=(new Date).valueOf();	//init seed with timestemp
	this._seed++;
	return this._seed;
};
//resolve ID as html object
dhtmlx.toNode = function(node){
	if (typeof node == "string") return document.getElementById(node);
	return node;
};
//adds extra methods for the array
dhtmlx.toArray = function(array){ 
	return dhtmlx.extend((array||[]),dhtmlx.PowerArray);
};
//resolve function name
dhtmlx.toFunctor=function(str){ 
	return (typeof(str)=="string") ? eval(str) : str; 
};

//dom helpers

//hash of attached events
dhtmlx._events = {};
//attach event to the DOM element
dhtmlx.event=function(node,event,handler,master){
	node = dhtmlx.toNode(node);
	
	var id = dhtmlx.uid();
	dhtmlx._events[id]=[node,event,handler];	//store event info, for detaching
	
	if (master) 
		handler=dhtmlx.bind(handler,master);	
		
	//use IE's of FF's way of event's attaching
	if (node.addEventListener)
		node.addEventListener(event, handler, false);
	else if (node.attachEvent)
		node.attachEvent("on"+event, handler);

	return id;	//return id of newly created event, can be used in eventRemove
};

//remove previously attached event
dhtmlx.eventRemove=function(id){
	
	if (!id) return;
	dhtmlx.assert(this._events[id],"Removing non-existing event");
		
	var ev = dhtmlx._events[id];
	//browser specific event removing
	if (ev[0].removeEventListener)
		ev[0].removeEventListener(ev[1],ev[2],false);
	else if (ev[0].detachEvent)
		ev[0].detachEvent("on"+ev[1],ev[2]);
		
	delete this._events[id];	//delete all traces
};


//debugger helpers
//anything starting from error or log will be removed during code compression

//add message in the log
dhtmlx.log = function(type,message,details){
	if (window.console && console.log){
		type=type.toLowerCase();
		if (window.console[type])
			window.console[type](message||"unknown error");
		else
			window.console.log(type +": "+message);
		if (details) 
			window.console.log(details);
	}	
};
//register rendering time from call point 
dhtmlx.log_full_time = function(name){
	dhtmlx._start_time_log = new Date();
	dhtmlx.log("Info","Timing start ["+name+"]");
	window.setTimeout(function(){
		var time = new Date();
		dhtmlx.log("Info","Timing end ["+name+"]:"+(time.valueOf()-dhtmlx._start_time_log.valueOf())/1000+"s");
	},1);
};
//register execution time from call point
dhtmlx.log_time = function(name){
	var fname = "_start_time_log"+name;
	if (!dhtmlx[fname]){
		dhtmlx[fname] = new Date();
		dhtmlx.log("Info","Timing start ["+name+"]");
	} else {
		var time = new Date();
		dhtmlx.log("Info","Timing end ["+name+"]:"+(time.valueOf()-dhtmlx[fname].valueOf())/1000+"s");
		dhtmlx[fname] = null;
	}
};
//log message with type=error
dhtmlx.error = function(message,details){
	dhtmlx.log("error",message,details);
};
//event system
dhtmlx.EventSystem={
	_init:function(){
		this._events = {};		//hash of event handlers, name => handler
		this._handlers = {};	//hash of event handlers, ID => handler
		this._map = {};
	},
	//temporary block event triggering
	block : function(){
		this._events._block = true;
	},
	//re-enable event triggering
	unblock : function(){
		this._events._block = false;
	},
	mapEvent:function(map){
		dhtmlx.extend(this._map, map);
	},
	//trigger event
	callEvent:function(type,params){
		if (this._events._block) return true;
		
		type = type.toLowerCase();
		dhtmlx.assert_event_call(this, type, params);
		
		var event_stack =this._events[type.toLowerCase()];	//all events for provided name
		var return_value = true;

		if (dhtmlx.debug)	//can slowdown a lot
			dhtmlx.log("info","["+this.name+"] event:"+type,params);
		
		if (event_stack)
			for(var i=0; i<event_stack.length; i++)
				/*
					Call events one by one
					If any event return false - result of whole event will be false
					Handlers which are not returning anything - counted as positive
				*/
				if (event_stack[i].apply(this,(params||[]))===false) return_value=false;
				
		if (this._map[type] && !this._map[type].callEvent(type,params))
			return_value =	false;
			
		return return_value;
	},
	//assign handler for some named event
	attachEvent:function(type,functor,id){
		type=type.toLowerCase();
		dhtmlx.assert_event_attach(this, type);
		
		id=id||dhtmlx.uid(); //ID can be used for detachEvent
		functor = dhtmlx.toFunctor(functor);	//functor can be a name of method

		var event_stack=this._events[type]||dhtmlx.toArray();
		//save new event handler
		event_stack.push(functor);
		this._events[type]=event_stack;
		this._handlers[id]={ f:functor,t:type };
		
		return id;
	},
	//remove event handler
	detachEvent:function(id){
		if(this._handlers[id]){
			var type=this._handlers[id].t;
			var functor=this._handlers[id].f;
			
			//remove from all collections
			var event_stack=this._events[type];
			event_stack.remove(functor);
			delete this._handlers[id];
		}
	} 
};

//array helper
//can be used by dhtmlx.toArray()
dhtmlx.PowerArray={
	//remove element at specified position
	removeAt:function(pos,len){
		if (pos>=0) this.splice(pos,(len||1));
	},
	//find element in collection and remove it 
	remove:function(value){
		this.removeAt(this.find(value));
	},	
	//add element to collection at specific position
	insertAt:function(data,pos){
		if (!pos && pos!==0) 	//add to the end by default
			this.push(data);
		else {	
			var b = this.splice(pos,(this.length-pos));
  			this[pos] = data;
  			this.push.apply(this,b); //reconstruct array without loosing this pointer
  		}
  	},  	
  	//return index of element, -1 if it doesn't exists
  	find:function(data){ 
  		for (i=0; i<this.length; i++) 
  			if (data==this[i]) return i; 	
  		return -1; 
  	},
  	//execute some method for each element of array
  	each:function(functor,master){
		for (var i=0; i < this.length; i++)
			functor.call((master||this),this[i]);
	},
	//create new array from source, by using results of functor 
	map:function(functor,master){
		for (var i=0; i < this.length; i++)
			this[i]=functor.call((master||this),this[i]);
		return this;
	}
};

dhtmlx.env = {};

//environment detection
if (navigator.userAgent.indexOf('Opera') != -1)
	dhtmlx._isOpera=true;
else{
	//very rough detection, but it is enough for current goals
	dhtmlx._isIE=!!document.all;
	dhtmlx._isFF=!document.all;
	dhtmlx._isWebKit=(navigator.userAgent.indexOf("KHTML")!=-1);
	if (navigator.appVersion.indexOf("MSIE 8.0")!= -1 && document.compatMode != "BackCompat") 
		dhtmlx._isIE=8;
	if (navigator.appVersion.indexOf("MSIE 9.0")!= -1 && document.compatMode != "BackCompat") 
		dhtmlx._isIE=9;
}

dhtmlx.env = {};

// dhtmlx.env.transform 
// dhtmlx.env.transition
(function(){
	dhtmlx.env.transform = false;
	dhtmlx.env.transition = false;
	var options = {};
	options.names = ['transform', 'transition'];
	options.transform = ['transform', 'WebkitTransform', 'MozTransform', 'oTransform','msTransform'];
	options.transition = ['transition', 'WebkitTransition', 'MozTransition', 'oTransition'];
	
	var d = document.createElement("DIV");
	var property;
	for(var i=0; i<options.names.length; i++) {
		while (p = options[options.names[i]].pop()) {
			if(typeof d.style[p] != 'undefined')
				dhtmlx.env[options.names[i]] = true;
		}
	}
})();
dhtmlx.env.transform_prefix = (function(){
		var prefix;
		if(dhtmlx._isOpera)
			prefix = '-o-';
		else {
			prefix = ''; // default option
			if(dhtmlx._isFF) 
				prefix = '-moz-';
			if(dhtmlx._isWebKit) 
					prefix = '-webkit-';
		}
		return prefix;
})();
dhtmlx.env.svg = (function(){
		return document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1");
})();

//store maximum used z-index
dhtmlx.zIndex={ drag : 10000 };

//html helpers
dhtmlx.html={
	create:function(name,attrs,html){
		attrs = attrs || {};
		var node = document.createElement(name);
		for (var attr_name in attrs)
			node.setAttribute(attr_name, attrs[attr_name]);
		if (attrs.style)
			node.style.cssText = attrs.style;
		if (attrs["class"])
			node.className = attrs["class"];
		if (html)
			node.innerHTML=html;
		return node;
	},
	//return node value, different logic for different html elements
	getValue:function(node){
		node = dhtmlx.toNode(node);
		if (!node) return "";
		return dhtmlx.isNotDefined(node.value)?node.innerHTML:node.value;
	},
	//remove html node, can process an array of nodes at once
	remove:function(node){
		if (node instanceof Array)
			for (var i=0; i < node.length; i++)
				this.remove(node[i]);
		else
			if (node && node.parentNode)
				node.parentNode.removeChild(node);
	},
	//insert new node before sibling, or at the end if sibling doesn't exist
	insertBefore: function(node,before,rescue){
		if (!node) return;
		if (before)
			before.parentNode.insertBefore(node, before);
		else
			rescue.appendChild(node);
	},
	//return custom ID from html element 
	//will check all parents starting from event's target
	locate:function(e,id){
		e=e||event;
		var trg=e.target||e.srcElement;
		while (trg){
			if (trg.getAttribute){	//text nodes has not getAttribute
				var test = trg.getAttribute(id);
				if (test) return test;
			}
			trg=trg.parentNode;
		}	
		return null;
	},
	//returns position of html element on the page
	offset:function(elem) {
		if (elem.getBoundingClientRect) { //HTML5 method
			var box = elem.getBoundingClientRect();
			var body = document.body;
			var docElem = document.documentElement;
			var scrollTop = window.pageYOffset || docElem.scrollTop || body.scrollTop;
			var scrollLeft = window.pageXOffset || docElem.scrollLeft || body.scrollLeft;
			var clientTop = docElem.clientTop || body.clientTop || 0;
			var clientLeft = docElem.clientLeft || body.clientLeft || 0;
			var top  = box.top +  scrollTop - clientTop;
			var left = box.left + scrollLeft - clientLeft;
			return { y: Math.round(top), x: Math.round(left) };
		} else { //fallback to naive approach
			var top=0, left=0;
			while(elem) {
				top = top + parseInt(elem.offsetTop,10);
				left = left + parseInt(elem.offsetLeft,10);
				elem = elem.offsetParent;
			}
			return {y: top, x: left};
		}
	},
	//returns position of event
	pos:function(ev){
		ev = ev || event;
        if(ev.pageX || ev.pageY)	//FF, KHTML
            return {x:ev.pageX, y:ev.pageY};
        //IE
        var d  =  ((dhtmlx._isIE)&&(document.compatMode != "BackCompat"))?document.documentElement:document.body;
        return {
                x:ev.clientX + d.scrollLeft - d.clientLeft,
                y:ev.clientY + d.scrollTop  - d.clientTop
        };
	},
	//prevent event action
	preventEvent:function(e){
		if (e && e.preventDefault) e.preventDefault();
		dhtmlx.html.stopEvent(e);
	},
	//stop event bubbling
	stopEvent:function(e){
		(e||event).cancelBubble=true;
		return false;
	},
	//add css class to the node
	addCss:function(node,name){
        node.className+=" "+name;
    },
    //remove css class from the node
    removeCss:function(node,name){
        node.className=node.className.replace(RegExp(name,"g"),"");
    }
};

//autodetect codebase folder
(function(){
	var temp = document.getElementsByTagName("SCRIPT");	//current script, most probably
	dhtmlx.assert(temp.length,"Can't locate codebase");
	if (temp.length){
		//full path to script
		temp = (temp[temp.length-1].getAttribute("src")||"").split("/");
		//get folder name
		temp.splice(temp.length-1, 1);
		dhtmlx.codebase = temp.slice(0, temp.length).join("/")+"/";
	}
})();

if (!dhtmlx.ui)
	dhtmlx.ui={};


/* DHX DEPEND FROM FILE 'template.js'*/


/*
	Template - handles html templates
*/

/*DHX:Depend dhtmlx.js*/

dhtmlx.Template={
	_cache:{
	},
	empty:function(){	
		return "";	
	},
	setter:function(value){
		return dhtmlx.Template.fromHTML(value);
	},
	obj_setter:function(value){
		var f = dhtmlx.Template.setter(value);
		var obj = this;
		return function(){
			return f.apply(obj, arguments);
		};
	},
	fromHTML:function(str){
		if (typeof str == "function") return str;
		if (this._cache[str])
			return this._cache[str];
			
	//supported idioms
	// {obj} => value
	// {obj.attr} => named attribute or value of sub-tag in case of xml
	// {obj.attr?some:other} conditional output
	// {-obj => sub-template
		str=(str||"").toString();		
		str=str.replace(/[\r\n]+/g,"\\n");
		str=str.replace(/\{obj\.([^}?]+)\?([^:]*):([^}]*)\}/g,"\"+(obj.$1?\"$2\":\"$3\")+\"");
		str=str.replace(/\{common\.([^}\(]*)\}/g,"\"+common.$1+\"");
		str=str.replace(/\{common\.([^\}\(]*)\(\)\}/g,"\"+(common.$1?common.$1(obj):\"\")+\"");
		str=str.replace(/\{obj\.([^}]*)\}/g,"\"+obj.$1+\"");
		str=str.replace(/#([a-z0-9_]+)#/gi,"\"+obj.$1+\"");
		str=str.replace(/\{obj\}/g,"\"+obj+\"");
		str=str.replace(/\{-obj/g,"{obj");
		str=str.replace(/\{-common/g,"{common");
		str="return \""+str+"\";";
		return this._cache[str]= Function("obj","common",str);
	}
};

dhtmlx.Type={
	/*
		adds new template-type
		obj - object to which template will be added
		data - properties of template
	*/
	add:function(obj, data){ 
		//auto switch to prototype, if name of class was provided
		if (!obj.types && obj.prototype.types)
			obj = obj.prototype;
		//if (typeof data == "string")
		//	data = { template:data };
			
		if (dhtmlx.assert_enabled())
			this.assert_event(data);
		
		var name = data.name||"default";
		
		//predefined templates - autoprocessing
		this._template(data);
		this._template(data,"edit");
		this._template(data,"loading");
		
		obj.types[name]=dhtmlx.extend(dhtmlx.extend({},(obj.types[name]||this._default)),data);	
		return name;
	},
	//default template value - basically empty box with 5px margin
	_default:{
		css:"default",
		template:function(){ return ""; },
		template_edit:function(){ return ""; },
		template_loading:function(){ return "..."; },
		width:150,
		height:80,
		margin:5,
		padding:0
	},
	//template creation helper
	_template:function(obj,name){ 
		name = "template"+(name?("_"+name):"");
		var data = obj[name];
		//if template is a string - check is it plain string or reference to external content
		if (data && (typeof data == "string")){
			if (data.indexOf("->")!=-1){
				data = data.split("->");
				switch(data[0]){
					case "html": 	//load from some container on the page
						data = dhtmlx.html.getValue(data[1]).replace(/\"/g,"\\\"");
						break;
					case "http": 	//load from external file
						data = new dhtmlx.ajax().sync().get(data[1],{uid:(new Date()).valueOf()}).responseText;
						break;
					default:
						//do nothing, will use template as is
						break;
				}
			}
			obj[name] = dhtmlx.Template.fromHTML(data);
		}
	}
};



/* DHX DEPEND FROM FILE 'config.js'*/


/*
	Behavior:Settings
	
	@export
		customize
		config
*/

/*DHX:Depend template.js*/
/*DHX:Depend dhtmlx.js*/

dhtmlx.Settings={
	_init:function(){
		/* 
			property can be accessed as this.config.some
			in same time for inner call it have sense to use _settings
			because it will be minified in final version
		*/
		this._settings = this.config= {}; 
	},
	define:function(property, value){
		if (typeof property == "object")
			return this._parseSeetingColl(property);
		return this._define(property, value);
	},
	_define:function(property,value){
		dhtmlx.assert_settings.call(this,property,value);
		
		//method with name {prop}_setter will be used as property setter
		//setter is optional
		var setter = this[property+"_setter"];
		return this._settings[property]=setter?setter.call(this,value):value;
	},
	//process configuration object
	_parseSeetingColl:function(coll){
		if (coll){
			for (var a in coll)				//for each setting
				this._define(a,coll[a]);		//set value through config
		}
	},
	//helper for object initialization
	_parseSettings:function(obj,initial){
		//initial - set of default values
		var settings = dhtmlx.extend({},initial);
		//code below will copy all properties over default one
		if (typeof obj == "object" && !obj.tagName)
			dhtmlx.extend(settings,obj);	
		//call config for each setting
		this._parseSeetingColl(settings);
	},
	_mergeSettings:function(config, defaults){
		for (var key in defaults)
			switch(typeof config[key]){
				case "object": 
					config[key] = this._mergeSettings((config[key]||{}), defaults[key]);
					break;
				case "undefined":
					config[key] = defaults[key];
					break;
				default:	//do nothing
					break;
			}
		return config;
	},
	//helper for html container init
	_parseContainer:function(obj,name,fallback){
		/*
			parameter can be a config object, in such case real container will be obj.container
			or it can be html object or ID of html object
		*/
		if (typeof obj == "object" && !obj.tagName) 
			obj=obj.container;
		this._obj = dhtmlx.toNode(obj);
		if (!this._obj && fallback)
			this._obj = fallback(obj);
			
		dhtmlx.assert(this._obj, "Incorrect html container");
		
		this._obj.className+=" "+name;
		this._obj.onselectstart=function(){return false;};	//block selection by default
		this._dataobj = this._obj;//separate reference for rendering modules
	},
	//apply template-type
	_set_type:function(name){
		//parameter can be a hash of settings
		if (typeof name == "object")
			return this.type_setter(name);
		
		dhtmlx.assert(this.types, "RenderStack :: Types are not defined");
		dhtmlx.assert(this.types[name],"RenderStack :: Inccorect type name",name);
		//or parameter can be a name of existing template-type	
		this.type=dhtmlx.extend({},this.types[name]);
		this.customize();	//init configs
	},
	customize:function(obj){
		//apply new properties
		if (obj) dhtmlx.extend(this.type,obj);
		
		//init tempaltes for item start and item end
		this.type._item_start = dhtmlx.Template.fromHTML(this.template_item_start(this.type));
		this.type._item_end = this.template_item_end(this.type);
		
		//repaint self
		this.render();
	},
	//config.type - creates new template-type, based on configuration object
	type_setter:function(value){
		this._set_type(typeof value == "object"?dhtmlx.Type.add(this,value):value);
		return value;
	},
	//config.template - creates new template-type with defined template string
	template_setter:function(value){
		return this.type_setter({template:value});
	},
	//config.css - css name for top level container
	css_setter:function(value){
		this._obj.className += " "+value;
		return value;
	}
};


/* DHX DEPEND FROM FILE 'destructor.js'*/


/*
	Behavior:Destruction
	
	@export
		destructor
*/

/*DHX:Depend dhtmlx.js*/

dhtmlx.Destruction = {
	_init:function(){
		//register self in global list of destructors
		dhtmlx.destructors.push(this);
	},
	//will be called automatically on unload, can be called manually
	//simplifies job of GC
	destructor:function(){
		this.destructor=function(){}; //destructor can be called only once
		
		//html collection
		this._htmlmap  = null;
		this._htmlrows = null;
		
		//temp html element, used by toHTML
		if (this._html)
			document.body.appendChild(this._html);	//need to attach, for IE's GC

		this._html = null;
		if (this._obj) {
			this._obj.innerHTML="";
			this._obj._htmlmap = null;
		}
		this._obj = this._dataobj=null;
		this.data = null;
		this._events = this._handlers = {};
	}
};
//global list of destructors
dhtmlx.destructors = [];
dhtmlx.event(window,"unload",function(){
	//call all registered destructors
	for (var i=0; i<dhtmlx.destructors.length; i++)
		dhtmlx.destructors[i].destructor();
	dhtmlx.destructors = [];
	
	//detach all known DOM events
	for (var a in dhtmlx._events){
		var ev = dhtmlx._events[a];
		if (ev[0].removeEventListener)
			ev[0].removeEventListener(ev[1],ev[2],false);
		else if (ev[0].detachEvent)
			ev[0].detachEvent("on"+ev[1],ev[2]);
		delete dhtmlx._events[a];
	}
});


/* DHX DEPEND FROM FILE 'pager.js'*/


/*
	UI:paging control
*/

/*DHX:Depend template.js*/

dhtmlx.ui.pager=function(container){
	this.name = "Pager";
	
	if (dhtmlx.assert_enabled()) this._assert();
	
	dhtmlx.extend(this, dhtmlx.Settings);
	this._parseContainer(container,"dhx_pager");
	
	dhtmlx.extend(this, dhtmlx.EventSystem);
	dhtmlx.extend(this, dhtmlx.SingleRender);
	dhtmlx.extend(this, dhtmlx.MouseEvents);
	
	this._parseSettings(container,{
		size:10,	//items on page
		page:-1,	//current page
		group:5,	//pages in group
		count:0,	//total count of items
		type:"default"
	});
	
	this.data = this._settings;
	this.refresh();
};

dhtmlx.ui.pager.prototype={
	_id:"dhx_p_id",
	on_click:{
		//on paging button click
		"dhx_pager_item":function(e,id){
			this.select(id);
		}
	},
	select:function(id){
		//id - id of button, number for page buttons
		switch(id){
			case "next":
				id = this._settings.page+1;
				break;
			case "prev":
				id = this._settings.page-1;
				break;
			case "first":
				id = 0;
				break;
			case "last":
				id = this._settings.limit-1;
				break;
			default:
				//use incoming id
				break;
		}
		if (id<0) id=0;
		if (id>=this.data.limit) id=this.data.limit-1;
		if (this.callEvent("onBeforePageChange",[this._settings.page,id])){
			this.data.page = id*1; //must be int
			this.refresh();
			this.callEvent("onAfterPageChange",[id]);	
		}
	},
	types:{
		"default":{ 
			template:dhtmlx.Template.fromHTML("{common.pages()}"),
			//list of page numbers
			pages:function(obj){
				var html="";
				//skip rendering if paging is not fully initialized
				if (obj.page == -1) return "";
				//current page taken as center of view, calculate bounds of group
				obj.min = obj.page-Math.round((obj.group-1)/2);
				obj.max = obj.min + obj.group-1;
				if (obj.min<0){
					obj.max+=obj.min*(-1);
					obj.min=0;
				}
				if (obj.max>=obj.limit){
					obj.min -= Math.min(obj.min,obj.max-obj.limit+1);
					obj.max = obj.limit-1;
				}
				//generate HTML code of buttons
				for (var i=(obj.min||0); i<=obj.max; i++)
					html+=this.button({id:i, index:(i+1), selected:(i == obj.page ?"_selected":"")});
				return html;
			},
			page:function(obj){
				return obj.page+1;
			},
			//go-to-first page button
			first:function(){
				return this.button({ id:"first", index:" &lt;&lt; ", selected:""});
			},
			//go-to-last page button
			last:function(){
				return this.button({ id:"last", index:" &gt;&gt; ", selected:""});
			},
			//go-to-prev page button
			prev:function(){
				return this.button({ id:"prev", index:"&lt;", selected:""});
			},
			//go-to-next page button
			next:function(){
				return this.button({ id:"next", index:"&gt;", selected:""});
			},
			button:dhtmlx.Template.fromHTML("<div dhx_p_id='{obj.id}' class='dhx_pager_item{obj.selected}'>{obj.index}</div>")
			
		}
	},
	//update settings and repaint self
	refresh:function(){
		var s = this._settings;
		//max page number
		s.limit = Math.ceil(s.count/s.size);
		
		//correct page if it is out of limits
		if (s.limit && s.limit != s.old_limit)
			s.page = Math.min(s.limit-1, s.page);
		
		var id = s.page;
		if (id!=-1 && (id!=s.old_page) || (s.limit != s.old_limit)){ 
			//refresh self only if current page or total limit was changed
			this.render();
			this.callEvent("onRefresh",[]);
			s.old_limit = s.limit;	//save for onchange check in next iteration
			s.old_page = s.page;
		}
	},
	template_item_start:dhtmlx.Template.fromHTML("<div>"),
	template_item_end:dhtmlx.Template.fromHTML("</div>")
};


/* DHX DEPEND FROM FILE 'single_render.js'*/


/*
	REnders single item. 
	Can be used for elements without datastore, or with complex custom rendering logic
	
	@export
		render
*/

/*DHX:Depend template.js*/

dhtmlx.SingleRender={
	_init:function(){
	},
	//convert item to the HTML text
	_toHTML:function(obj){
			/*
				this one doesn't support per-item-$template
				it has not sense, because we have only single item per object
			*/
			return this.type._item_start(obj,this.type)+this.type.template(obj,this.type)+this.type._item_end;
	},
	//render self, by templating data object
	render:function(){
		if (!this.callEvent || this.callEvent("onBeforeRender",[this.data])){
			if (this.data)
				this._dataobj.innerHTML = this._toHTML(this.data);
			if (this.callEvent) this.callEvent("onAfterRender",[]);
		}
	}
};


/* DHX DEPEND FROM FILE 'tooltip.js'*/


/*
	UI: Tooltip
	
	@export
		show
		hide
*/

/*DHX:Depend tooltip.css*/
/*DHX:Depend template.js*/
/*DHX:Depend single_render.js*/

dhtmlx.ui.Tooltip=function(container){
	this.name = "Tooltip";
	this.version = "3.0";
	
	if (dhtmlx.assert_enabled()) this._assert();

	if (typeof container == "string"){
		container = { template:container };
	}
		
	dhtmlx.extend(this, dhtmlx.Settings);
	dhtmlx.extend(this, dhtmlx.SingleRender);
	this._parseSettings(container,{
		type:"default",
		dy:0,
		dx:20
	});
	
	//create  container for future tooltip
	this._dataobj = this._obj = document.createElement("DIV");
	this._obj.className="dhx_tooltip";
	dhtmlx.html.insertBefore(this._obj,document.body.firstChild);
};
dhtmlx.ui.Tooltip.prototype = {
	//show tooptip
	//pos - object, pos.x - left, pox.y - top
	show:function(data,pos){
		if (this._disabled) return;
		//render sefl only if new data was provided
		if (this.data!=data){
			this.data=data;
			this.render(data);
		}
		//show at specified position
		this._obj.style.top = pos.y+this._settings.dy+"px";
		this._obj.style.left = pos.x+this._settings.dx+"px";
		this._obj.style.display="block";
	},
	//hide tooltip
	hide:function(){
		this.data=null; //nulify, to be sure that on next show it will be fresh-rendered
		this._obj.style.display="none";
	},
	disable:function(){
		this._disabled = true;	
	},
	enable:function(){
		this._disabled = false;
	},
	types:{
		"default":dhtmlx.Template.fromHTML("{obj.id}")
	},
	template_item_start:dhtmlx.Template.empty,
	template_item_end:dhtmlx.Template.empty
};



/* DHX DEPEND FROM FILE 'autotooltip.js'*/


/*
	Behavior: AutoTooltip - links tooltip to data driven item
*/

/*DHX:Depend tooltip.js*/

dhtmlx.AutoTooltip = {
	tooltip_setter:function(value){
		var t = new dhtmlx.ui.Tooltip(value);
		this.attachEvent("onMouseMove",function(id,e){	//show tooltip on mousemove
			t.show(this.get(id),dhtmlx.html.pos(e));
		});
		this.attachEvent("onMouseOut",function(id,e){	//hide tooltip on mouseout
			t.hide();
		});
		this.attachEvent("onMouseMoving",function(id,e){	//hide tooltip just after moving start
			t.hide();
		});
		return t;
	}
};


/* DHX DEPEND FROM FILE 'compatibility.js'*/


/*
	Collection of compatibility hacks
*/

/*DHX:Depend dhtmlx.js*/

dhtmlx.compat=function(name, obj){
	//check if name hash present, and applies it when necessary
	if (dhtmlx.compat[name])
		dhtmlx.compat[name](obj);
};


(function(){
	if (!window.dhtmlxError){
		//dhtmlxcommon is not included
		
		//create fake error tracker for connectors
		var dummy = function(){};
		window.dhtmlxError={ catchError:dummy, throwError:dummy };
		//helpers instead of ones from dhtmlxcommon
		window.convertStringToBoolean=function(value){
			return !!value;
		};
		window.dhtmlxEventable = function(node){
			dhtmlx.extend(node,dhtmlx.EventSystem);
		};
		//imitate ajax layer of dhtmlxcommon
		var loader = {
			getXMLTopNode:function(name){
				
			},
			doXPath:function(path){
				return dhtmlx.DataDriver.xml.xpath(this.xml,path);
			},
			xmlDoc:{
				responseXML:true
			}
		};
		//wrap ajax methods of dataprocessor
		dhtmlx.compat.dataProcessor=function(obj){
			//FIXME
			//this is pretty ugly solution - we replace whole method , so changes in dataprocessor need to be reflected here
			
			var sendData = "_sendData";
			var in_progress = "_in_progress";
			var tMode = "_tMode";
			var waitMode = "_waitMode";
			
			obj[sendData]=function(a1,rowId){
		    	if (!a1) return; //nothing to send
		    	if (rowId)
					this[in_progress][rowId]=(new Date()).valueOf();
			    
				if (!this.callEvent("onBeforeDataSending",rowId?[rowId,this.getState(rowId)]:[])) return false;				
				
				var a2 = this;
		        var a3=this.serverProcessor;
				if (this[tMode]!="POST")
					//use dhtmlx.ajax instead of old ajax layer
					dhtmlx.ajax().get(a3+((a3.indexOf("?")!=-1)?"&":"?")+this.serialize(a1,rowId),"",function(t,x,xml){
						loader.xml = dhtmlx.DataDriver.xml.checkResponse(t,x);
						a2.afterUpdate(a2, null, null, null, loader);
					});
				else
		        	dhtmlx.ajax().post(a3,this.serialize(a1,rowId),function(t,x,xml){
		        		loader.xml = dhtmlx.DataDriver.xml.checkResponse(t,x);
		        		a2.afterUpdate(a2, null, null, null, loader);
		    		});
		
				this[waitMode]++;
		    };
		};
	}
	
})();


/* DHX DEPEND FROM FILE 'dataprocessor_hook.js'*/


/*
	Behaviour:DataProcessor - translates inner events in dataprocessor calls
	
	@export
		changeId
		setItemStyle
		setUserData
		getUserData
*/

/*DHX:Depend compatibility.js*/
/*DHX:Depend dhtmlx.js*/

dhtmlx.DataProcessor={
	//called from DP as part of dp.init
	_dp_init:function(dp){
		//map methods
		var varname = "_methods";
		dp[varname]=["setItemStyle","","changeId","remove"];
		//after item adding - trigger DP
		this.attachEvent("onAfterAdd",function(id){
			dp.setUpdated(id,true,"inserted");
		});
		this.data.attachEvent("onStoreLoad",dhtmlx.bind(function(driver, data){
			if (driver.getUserData)
				driver.getUserData(data,this._userdata);
		},this));
		
		//after item deleting - trigger DP
		this.attachEvent("onBeforeDelete",function(id){
	        var z=dp.getState(id);
			if (z=="inserted") {  dp.setUpdated(id,false);		return true; }
			if (z=="deleted")  return false;
	    	if (z=="true_deleted")  return true;
	    	
			dp.setUpdated(id,true,"deleted");
	      	return false;
		});
		//after editing - trigger DP
		this.attachEvent("onAfterEditStop",function(id){
			dp.setUpdated(id,true,"updated");
		});
		this.attachEvent("onBindUpdate",function(data){
			window.setTimeout(function(){
				dp.setUpdated(data.id,true,"updated");	
			},1);
		});
		
		varname = "_getRowData";
		//serialize item's data in URL
		dp[varname]=function(id,pref){
			var ev=this.obj.data.get(id);
			var data = {};
			for (var a in ev){
				if (a.indexOf("_")===0) continue;
					data[a]=ev[a];
			}
			
			return data;
		};
		varname = "_clearUpdateFlag";
		dp[varname]=function(){};
		this._userdata = {};
		
		dp.attachEvent("insertCallback", this._dp_callback);
		dp.attachEvent("updateCallback", this._dp_callback);
		dp.attachEvent("deleteCallback", function(upd, id) {
			this.obj.setUserData(id, this.action_param, "true_deleted");
			this.obj.remove(id);
		});
				
		//enable compatibility layer - it will allow to use DP without dhtmlxcommon
		dhtmlx.compat("dataProcessor",dp);
	},
	_dp_callback:function(upd,id){
		this.obj.data.set(id,dhtmlx.DataDriver.xml.getDetails(upd.firstChild));
		this.obj.data.refresh(id);
	},
	//marks item in question with specific styles, not purposed for public usage
	setItemStyle:function(id,style){
		var node = this._locateHTML(id);
		if (node) node.style.cssText+=";"+style; //style is not persistent
	},
	//change ID of item
	changeId:function(oldid, newid){
		this.data.changeId(oldid, newid);
		this.refresh();
	},
	//sets property value, not purposed for public usage
	setUserData:function(id,name,value){
		if (id)
			this.data.get(id)[name]=value;
		else
			this._userdata[name]=value;
	},
	//gets property value, not purposed for public usage
	getUserData:function(id,name){
		return id?this.data.get(id)[name]:this._userdata[name];
	}
};
(function(){
	var temp = "_dp_init";
	dhtmlx.DataProcessor[temp]=dhtmlx.DataProcessor._dp_init;
})();



/* DHX DEPEND FROM FILE 'compatibility_drag.js'*/


/*
	Compatibility hack for DND
	Allows dnd between dhtmlx.dnd and dhtmlxcommon based dnd
	When dnd items - related events will be correctly triggered. 
	onDrag event must define final moving logic, if it is absent - item will NOT be moved automatically
	
	to activate this functionality , next command need to be called
		dhtmlx.compat("dnd");
*/

/*DHX:Depend compatibility.js*/

dhtmlx.compat.dnd = function(){
	//if dhtmlxcommon.js included on the page
	if (window.dhtmlDragAndDropObject){
		var _dragged = "_dragged"; //fake for code compression utility, do not change!
		
		//wrap methods of dhtmlxcommon to inform dhtmlx.dnd logic
		var old_ocl = dhtmlDragAndDropObject.prototype.checkLanding;
		dhtmlDragAndDropObject.prototype.checkLanding=function(node,e,skip){
			old_ocl.apply(this,arguments);
			if (!skip){ 
				var c = dhtmlx.DragControl._drag_context = dhtmlx.DragControl._drag_context||{};
				if (!c.from)
					c.from = this.dragStartObject;
				dhtmlx.DragControl._checkLand(node,e,true);
			}
		};
		
		var old_odp = dhtmlDragAndDropObject.prototype.stopDrag;
		dhtmlDragAndDropObject.prototype.stopDrag=function(e,dot,skip){
			if (!skip){
				if (dhtmlx.DragControl._last){
					dhtmlx.DragControl._active = dragger.dragStartNode;
					dhtmlx.DragControl._stopDrag(e,true);
				}
			}
			old_odp.apply(this,arguments);
		};
		
		
		//pre-create dnd object from dhtmlxcommon
		var dragger = new dhtmlDragAndDropObject();
		
		//wrap drag start process
		var old_start = dhtmlx.DragControl._startDrag;
		dhtmlx.DragControl._startDrag=function(){ 
			old_start.apply(this,arguments);	
			//build list of IDs and fake objects for dhtlmxcommon support
			var c = dhtmlx.DragControl._drag_context;
			if (!c) return;
			var source = [];
			var tsource = [];
			for (var i=0; i < c.source.length; i++){
				source[i]={idd:c.source[i]};
				tsource.push(c.source[i]);
			}
			
			dragger.dragStartNode = {	
				parentNode:{}, 
				parentObject:{ 
					idd:source, 
					id:(tsource.length == 1?tsource[0]:tsource),
					treeNod:{
						object:c.from
					}
				}
			};
			
			//prevent code compression of "_dragged"
			dragger.dragStartNode.parentObject.treeNod[_dragged]=source;
			dragger.dragStartObject = c.from;
		};
		//wrap drop landing checker
		var old_check = dhtmlx.DragControl._checkLand;
		dhtmlx.DragControl._checkLand = function(node,e,skip){
			old_check.apply(this,arguments);
			if (!this._last && !skip){
				//we are in middle of nowhere, check old drop landings
				node = dragger.checkLanding(node,e,true);
			}
		};
		
		//wrap drop routine
		var old_drop = dhtmlx.DragControl._stopDrag;
		dhtmlx.DragControl._stopDrag=function(e,skip){
			old_drop.apply(this,arguments);
			if (dragger.lastLanding && !skip)
				dragger.stopDrag(e,false,true);
		};
		//extend getMaster, so they will be able to recognize master objects from dhtmlxcommon.js
		var old_mst = 	dhtmlx.DragControl.getMaster;
		dhtmlx.DragControl.getMaster = function(t){
			var master = null;
			if (t)
				master = old_mst.apply(this,arguments);
			if (!master){
				master = dragger.dragStartObject;
				var src = [];
				var from = master[_dragged];
				for (var i=0; i < from.length; i++) {
					src.push(from[i].idd||from[i].id);
				}
				dhtmlx.DragControl._drag_context.source = src;
			}
			return master;
		};
		
	}
};


/* DHX DEPEND FROM FILE 'move.js'*/


/*
	Behavior:DataMove - allows to move and copy elements, heavily relays on DataStore.move
	@export
		copy
		move
*/
dhtmlx.DataMove={
	_init:function(){
		dhtmlx.assert(this.data, "DataMove :: Component doesn't have DataStore");
	},
	//creates a copy of the item
	copy:function(sid,tindex,tobj,tid){
		var data = this.get(sid);
		if (!data){
			dhtmlx.log("Warning","Incorrect ID in DataMove::copy");
			return;
		}
		
		//make data conversion between objects
		if (tobj){
			dhtmlx.assert(tobj.externalData,"DataMove :: External object doesn't support operation");	
			data = tobj.externalData(data);
		}
		tobj = tobj||this;
		//adds new element same as original
		return tobj.add(tobj.externalData(data,tid),tindex);
	},
	//move item to the new position
	move:function(sid,tindex,tobj,tid){
		//can process an arrya - it allows to use it from onDrag 
		if (sid instanceof Array){
			for (var i=0; i < sid.length; i++) {
				//increase index for each next item in the set, so order of insertion will be equal to order in the array
				var new_index = (tobj||this).indexById(this.move(sid[i], tindex, tobj, dhtmlx.uid()));
				if (sid[i+1])
					tindex = new_index+(this.indexById(sid[i+1])<new_index?0:1);
				
			}
			return;
		}
		
		nid = sid; //id after moving
		if (tindex<0){
			dhtmlx.log("Info","DataMove::move - moving outside of bounds is ignored");
			return;
		}
		
		var data = this.get(sid);
		if (!data){
			dhtmlx.log("Warning","Incorrect ID in DataMove::move");
			return;
		}
		
		if (!tobj || tobj == this)
			this.data.move(this.indexById(sid),tindex);	//move inside the same object
		else {
			dhtmlx.assert(tobj.externalData, "DataMove :: External object doesn't support operation");
			//copy to the new object
			nid=tobj.add(tobj.externalData(data,tid),tindex);
			this.remove(sid);//delete in old object
		}
		return nid;	//return ID of item after moving
	},
	//move item on one position up
	moveUp:function(id,step){
		return this.move(id,this.indexById(id)-(step||1));
	},
	//move item on one position down
	moveDown:function(id,step){
		return this.moveUp(id, (step||1)*-1);
	},
	//move item to the first position
	moveTop:function(id){
		return this.move(id,0);
	},
	//move item to the last position
	moveBottom:function(id){
		return this.move(id,this.data.dataCount()-1);
	},
	/*
		this is a stub for future functionality
		currently it just makes a copy of data object, which is enough for current situation
	*/
	externalData:function(data,id){
		//FIXME - will not work for multi-level data
		var newdata = dhtmlx.extend({},data);
		newdata.id = id||dhtmlx.uid();
		
		newdata.$selected=newdata.$template=null;
		return newdata;
	}
};


/* DHX DEPEND FROM FILE 'dnd.js'*/


/*
	Behavior:DND - low-level dnd handling
	@export
		getContext
		addDrop
		addDrag
		
	DND master can define next handlers
		onCreateDrag
		onDragIng
		onDragOut
		onDrag
		onDrop
	all are optional
*/

/*DHX:Depend dhtmlx.js*/

dhtmlx.DragControl={
	//has of known dnd masters
	_drag_masters : dhtmlx.toArray(["dummy"]),
	/*
		register drop area
		@param node 			html node or ID
		@param ctrl 			options dnd master
		@param master_mode 		true if you have complex drag-area rules
	*/
	addDrop:function(node,ctrl,master_mode){
		node = dhtmlx.toNode(node);
		node.dhx_drop=this._getCtrl(ctrl);
		if (master_mode) node.dhx_master=true;
	},
	//return index of master in collection
	//it done in such way to prevent dnd master duplication
	//probably useless, used only by addDrop and addDrag methods
	_getCtrl:function(ctrl){
		ctrl = ctrl||dhtmlx.DragControl;
		var index = this._drag_masters.find(ctrl);
		if (index<0){
			index = this._drag_masters.length;
			this._drag_masters.push(ctrl);
		}
		return index;
	},
	/*
		register drag area
		@param node 	html node or ID
		@param ctrl 	options dnd master
	*/
	addDrag:function(node,ctrl){
	    node = dhtmlx.toNode(node);
	    node.dhx_drag=this._getCtrl(ctrl);
		dhtmlx.event(node,"mousedown",this._preStart,node);
	},
	//logic of drag - start, we are not creating drag immediately, instead of that we hears mouse moving
	_preStart:function(e){
		if (dhtmlx.DragControl._active){
			dhtmlx.DragControl._preStartFalse();
			dhtmlx.DragControl.destroyDrag();
		}
		dhtmlx.DragControl._active=this;
		dhtmlx.DragControl._start_pos={x:e.pageX, y:e.pageY};
		dhtmlx.DragControl._dhx_drag_mm = dhtmlx.event(document.body,"mousemove",dhtmlx.DragControl._startDrag);
		dhtmlx.DragControl._dhx_drag_mu = dhtmlx.event(document.body,"mouseup",dhtmlx.DragControl._preStartFalse);
		//http://code.google.com/p/chromium/issues/detail?id=14204
		dhtmlx.DragControl._dhx_drag_sc = dhtmlx.event(this,"scroll",dhtmlx.DragControl._preStartFalse);

		e.cancelBubble=true;
		return false;
	},
	//if mouse was released before moving - this is not a dnd, remove event handlers
	_preStartFalse:function(e){
		dhtmlx.DragControl._dhx_drag_mm = dhtmlx.eventRemove(dhtmlx.DragControl._dhx_drag_mm);
		dhtmlx.DragControl._dhx_drag_mu = dhtmlx.eventRemove(dhtmlx.DragControl._dhx_drag_mu);		
		dhtmlx.DragControl._dhx_drag_sc = dhtmlx.eventRemove(dhtmlx.DragControl._dhx_drag_sc);		
	},
	//mouse was moved without button released - dnd started, update event handlers
	_startDrag:function(e){
		//prevent unwanted dnd
		var pos = {x:e.pageX, y:e.pageY};
		if (Math.abs(pos.x-dhtmlx.DragControl._start_pos.x)<5 && Math.abs(pos.y-dhtmlx.DragControl._start_pos.y)<5)
			return;

		dhtmlx.DragControl._preStartFalse();
		if (!dhtmlx.DragControl.createDrag(e)) return;
		
		dhtmlx.DragControl.sendSignal("start"); //useless for now
		dhtmlx.DragControl._dhx_drag_mm = dhtmlx.event(document.body,"mousemove",dhtmlx.DragControl._moveDrag);
		dhtmlx.DragControl._dhx_drag_mu = dhtmlx.event(document.body,"mouseup",dhtmlx.DragControl._stopDrag);
		dhtmlx.DragControl._moveDrag(e);
	},
	//mouse was released while dnd is active - process target
	_stopDrag:function(e){
		dhtmlx.DragControl._dhx_drag_mm = dhtmlx.eventRemove(dhtmlx.DragControl._dhx_drag_mm);
		dhtmlx.DragControl._dhx_drag_mu = dhtmlx.eventRemove(dhtmlx.DragControl._dhx_drag_mu);
		if (dhtmlx.DragControl._last){	//if some drop target was confirmed
			dhtmlx.DragControl.onDrop(dhtmlx.DragControl._active,dhtmlx.DragControl._last,this._landing,e);
			dhtmlx.DragControl.onDragOut(dhtmlx.DragControl._active,dhtmlx.DragControl._last,null,e);
		}
		dhtmlx.DragControl.destroyDrag();
		dhtmlx.DragControl.sendSignal("stop");	//useless for now
	},
	//dnd is active and mouse position was changed
	_moveDrag:function(e){
		var pos = dhtmlx.html.pos(e);
		//adjust drag marker position
		dhtmlx.DragControl._html.style.top=pos.y+dhtmlx.DragControl.top +"px";
		dhtmlx.DragControl._html.style.left=pos.x+dhtmlx.DragControl.left+"px";
		
		if (dhtmlx.DragControl._skip)
			dhtmlx.DragControl._skip=false;
		else
			dhtmlx.DragControl._checkLand((e.srcElement||e.target),e);
		
		e.cancelBubble=true;
		return false;		
	},
	//check if item under mouse can be used as drop landing
	_checkLand:function(node,e){ 
		while (node && node.tagName!="BODY"){
			if (node.dhx_drop){	//if drop area registered
				if (this._last && (this._last!=node || node.dhx_master))	//if this area with complex dnd master
					this.onDragOut(this._active,this._last,node,e);			//inform master about possible mouse-out
				if (!this._last || this._last!=node || node.dhx_master){	//if this is new are or area with complex dnd master
				    this._last=null;										//inform master about possible mouse-in
					this._landing=this.onDragIn(dhtmlx.DragControl._active,node,e);
					if (this._landing)	//landing was rejected
						this._last=node;
					return;				
				} 
				return;
			}
			node=node.parentNode;
		}
		if (this._last)	//mouse was moved out of previous landing, and without finding new one 
			this._last = this._landing = this.onDragOut(this._active,this._last,null,e);
	},
	//mostly useless for now, can be used to add cross-frame dnd
	sendSignal:function(signal){
		dhtmlx.DragControl.active=(signal=="start");
	},
	
	//return master for html area
	getMaster:function(t){
		return this._drag_masters[t.dhx_drag||t.dhx_drop];
	},
	//return dhd-context object
	getContext:function(t){
		return this._drag_context;
	},
	//called when dnd is initiated, must create drag representation
	createDrag:function(e){ 
		var a=dhtmlx.DragControl._active;
		var master = this._drag_masters[a.dhx_drag];
		var drag_container;
		
		//if custom method is defined - use it
		if (master.onDragCreate){
			drag_container=master.onDragCreate(a,e);
			drag_container.style.position='absolute';
			drag_container.style.zIndex=dhtmlx.zIndex.drag;
			drag_container.onmousemove=dhtmlx.DragControl._skip_mark;
		} else {
		//overvise use default one
			var text = dhtmlx.DragControl.onDrag(a,e);
			if (!text) return false;
			var drag_container = document.createElement("DIV");
			drag_container.innerHTML=text;
			drag_container.className="dhx_drag_zone";
			drag_container.onmousemove=dhtmlx.DragControl._skip_mark;
			document.body.appendChild(drag_container);
		}
		dhtmlx.DragControl._html=drag_container;
		return true;
	},
	//helper, prevents unwanted mouse-out events
	_skip_mark:function(){
		dhtmlx.DragControl._skip=true;
	},
	//after dnd end, remove all traces and used html elements
	destroyDrag:function(){
		var a=dhtmlx.DragControl._active;
		var master = this._drag_masters[a.dhx_drag];
		
		if (master && master.onDragDestroy)
			master.onDragDestroy(a,dhtmlx.DragControl._html);
		else dhtmlx.html.remove(dhtmlx.DragControl._html);
		
		dhtmlx.DragControl._landing=dhtmlx.DragControl._active=dhtmlx.DragControl._last=dhtmlx.DragControl._html=null;
	},
	top:5,	 //relative position of drag marker to mouse cursor
	left:5,
	//called when mouse was moved in drop area
	onDragIn:function(s,t,e){
		var m=this._drag_masters[t.dhx_drop];
		if (m.onDragIn && m!=this) return m.onDragIn(s,t,e);
		t.className=t.className+" dhx_drop_zone";
		return t;
	},
	//called when mouse was moved out drop area
	onDragOut:function(s,t,n,e){
		var m=this._drag_masters[t.dhx_drop];
		if (m.onDragOut && m!=this) return m.onDragOut(s,t,n,e);
		t.className=t.className.replace("dhx_drop_zone","");
		return null;
	},
	//called when mouse was released over drop area
	onDrop:function(s,t,d,e){
		var m=this._drag_masters[t.dhx_drop];
		dhtmlx.DragControl._drag_context.from = dhtmlx.DragControl.getMaster(s);
		if (m.onDrop && m!=this) return m.onDrop(s,t,d,e);
		t.appendChild(s);
	},
	//called when dnd just started
	onDrag:function(s,e){
		var m=this._drag_masters[s.dhx_drag];
		if (m.onDrag && m!=this) return m.onDrag(s,e);
		dhtmlx.DragControl._drag_context = {source:s, from:s};
		return "<div style='"+s.style.cssText+"'>"+s.innerHTML+"</div>";
	}	
};


/* DHX DEPEND FROM FILE 'drag.js'*/


/*
	Behavior:DragItem - adds ability to move items by dnd
	
	dnd context can have next properties
		from - source object
		to - target object
		source - id of dragged item(s)
		target - id of drop target, null for drop on empty space
		start - id from which DND was started
*/

/*DHX:Depend dnd.js*/		/*DHX:Depend move.js*/		/*DHX:Depend compatibility_drag.js*/ 	
/*DHX:Depend dhtmlx.js*/



dhtmlx.DragItem={
	_init:function(){
		dhtmlx.assert(this.move,"DragItem :: Component doesn't have DataMove interface");
		dhtmlx.assert(this.locate,"DragItem :: Component doesn't have RenderStack interface");
		dhtmlx.assert(dhtmlx.DragControl,"DragItem :: DragControl is not included");
		
		if (!this._settings || this._settings.drag)
			dhtmlx.DragItem._initHandlers(this);
		else if (this._settings){
			//define setter, which may be triggered by config call
			this.drag_setter=function(value){
				if (value){
					this._initHandlers(this);
					delete this.drag_setter;	//prevent double initialization
				}
				return value;
			};
		}
		//if custom dnd marking logic is defined - attach extra handlers
		if (this.dragMarker){
			this.attachEvent("onBeforeDragIn",this.dragMarker);
			this.attachEvent("onDragOut",this.dragMarker);
		}
			
	},
	//helper - defines component's container as active zone for dragging and for dropping
	_initHandlers:function(obj){
		dhtmlx.DragControl.addDrop(obj._obj,obj,true);
		dhtmlx.DragControl.addDrag(obj._obj,obj);	
	},
	/*
		s - source html element
		t - target html element
		d - drop-on html element ( can be not equal to the target )
		e - native html event 
	*/
	//called when drag moved over possible target
	onDragIn:function(s,t,e){
		var id = this.locate(e) || null;
		var context = dhtmlx.DragControl._drag_context;
		var to = dhtmlx.DragControl.getMaster(s);
		//previous target
		var html = (this._locateHTML(id)||this._obj);
		//prevent double processing of same target
		if (html == dhtmlx.DragControl._landing) return html;
		context.target = id;
		context.to = to;
		
		if (!this.callEvent("onBeforeDragIn",[context,e])){
			context.id = null;
			return null;
		}
		
		dhtmlx.html.addCss(html,"dhx_drag_over"); //mark target
		return html;
	},
	//called when drag moved out from possible target
	onDragOut:function(s,t,n,e){ 
		var id = this.locate(e) || null;
        if (n != this._dataobj)
            id = null;
		//previous target
		var html = (this._locateHTML(id)||(n?dhtmlx.DragControl.getMaster(n)._obj:window.undefined));
		if (html == dhtmlx.DragControl._landing) return null;
		//unmark previous target
		var context = dhtmlx.DragControl._drag_context;
		dhtmlx.html.removeCss(dhtmlx.DragControl._landing,"dhx_drag_over");
		context.target = context.to = null;
		this.callEvent("onDragOut",[context,e]);
		return null;
	},
	//called when drag moved on target and button is released
	onDrop:function(s,t,d,e){ 
		var context = dhtmlx.DragControl._drag_context;
		
		//finalize context details
		context.to = this;
		context.index = context.target?this.indexById(context.target):this.dataCount();
		context.new_id = dhtmlx.uid();
		if (!this.callEvent("onBeforeDrop",[context,e])) return;
		//moving
		if (context.from==context.to){
			this.move(context.source,context.index);	//inside the same component
		} else {
			if (context.from)	//from different component
				context.from.move(context.source,context.index,context.to,context.new_id);
			else
				dhtmlx.error("Unsopported d-n-d combination");
		}
		this.callEvent("onAfterDrop",[context,e]);
	},
	//called when drag action started
	onDrag:function(s,e){
		var id = this.locate(e);
		var list = [id];
		if (id){
			if (this.getSelected){ //has selection model
				var selection = this.getSelected();	//if dragged item is one of selected - drag all selected
				if (dhtmlx.PowerArray.find.call(selection,id)!=-1)
					list = selection;
			}
			//save initial dnd params
			var context = dhtmlx.DragControl._drag_context= { source:list, start:id };
			context.from = this;
			
			if (this.callEvent("onBeforeDrag",[context,e]))
				return context.html||this._toHTML(this.get(id));	//set drag representation
		}
		return null;
	}
	//returns dnd context object
	/*getDragContext:function(){
		return dhtmlx.DragControl._drag_context;
	}*/
};


/* DHX DEPEND FROM FILE 'edit.js'*/


/*
	Behavior:EditAbility - enables item operation for the items
	
	@export
		edit
		stopEdit
*/
dhtmlx.EditAbility={
	_init: function(id){
		this._edit_id = null;		//id of active item 
		this._edit_bind = null;		//array of input-to-property bindings

		dhtmlx.assert(this.data,"EditAbility :: Component doesn't have DataStore");
		dhtmlx.assert(this._locateHTML,"EditAbility :: Component doesn't have RenderStack");
				
		this.attachEvent("onEditKeyPress",function(code, ctrl, shift){
			if (code == 13 && !shift)
				this.stopEdit();
			else if (code == 27) 
				this.stopEdit(true);
		});
		this.attachEvent("onBeforeRender", function(){
			this.stopEdit();
		});
    	
	},
	//returns id of item in edit state, or null if none
	isEdit:function(){
		return this._edit_id;
	},
	//switch item to the edit state
	edit:function(id){
		//edit operation can be blocked from editStop - when previously active editor can't be closed			
		if (this.stopEdit(false, id)){
			if (!this.callEvent("onBeforeEditStart",[id])) 
				return;			
			var data = this.data.get(id);			
			//object with custom templates is not editable
			if (data.$template) return;
			
			//item must have have "edit" template
 			data.$template="edit";	
			this.data.refresh(id);
			this._edit_id = id;
			
			//parse templates and save input-property mapping
			this._save_binding(id);
			this._edit_bind(true,data);	//fill inputs with data
			
			this.callEvent("onAfterEditStart",[id]);	
		}
	},
	//close currently active editor
	stopEdit:function(mode, if_not_id){
		if (!this._edit_id) return true;
		if (this._edit_id == if_not_id) return false;

		var values = {};
		if (!mode) this._edit_bind(false,values);
		else values = null;

		if (!this.callEvent("onBeforeEditStop",[this._edit_id, values]))
			return false;
			
		var data=this.data.get(this._edit_id);
		data.$template=null;	//set default template
		
		//load data from inputs
		//if mode is set - close editor without saving
		if (!mode) this._edit_bind(false,data);
		var id = this._edit_id;
		this._edit_bind=this._edit_id=null;
		
		this.data.refresh(id);
		
		this.callEvent("onAfterEditStop",[id, values]);
		return true;
	},
	//parse template and save inputs which need to be mapped to the properties
	_save_binding:function(id){
		var cont = this._locateHTML(id);
		var code = "";			//code of prop-to-inp method
		var back_code = "";		//code of inp-to-prop method
		var bind_elements = [];	//binded inputs
		if (cont){
			var elements = cont.getElementsByTagName("*");		//all sub-tags
			var bind = "";
			for (var i=0; i < elements.length; i++) {
				if(elements[i].nodeType==1 && (bind = elements[i].getAttribute("bind"))){	//if bind present
					//code for element accessing 
					code+="els["+bind_elements.length+"].value="+bind+";";
					back_code+=bind+"=els["+bind_elements.length+"].value;";
					bind_elements.push(elements[i]);
					//clear block-selection for the input
					elements[i].className+=" dhx_allow_selection";
					elements[i].onselectstart=this._block_native;
				}
			}
			elements = null;
		}
		//create accessing methods, for later usage
		code = Function("obj","els",code);
		back_code = Function("obj","els",back_code);
		this._edit_bind = function(mode,obj){
			if (mode){	//property to input
				code(obj,bind_elements);	
				if (bind_elements.length && bind_elements[0].select) //focust first html input, if possible
					bind_elements[0].select();						 
			}
			else 		//input to propery
				back_code(obj,bind_elements);
		};
	},
	//helper - blocks event bubbling, used to stop click event on editor level
	_block_native:function(e){ (e||event).cancelBubble=true; return true; }
};


/* DHX DEPEND FROM FILE 'key.js'*/


/*
	Behavior:KeyEvents - hears keyboard 
*/
dhtmlx.KeyEvents = {
	_init:function(){
		//attach handler to the main container
		dhtmlx.event(this._obj,"keypress",this._onKeyPress,this);
	},
	//called on each key press , when focus is inside of related component
	_onKeyPress:function(e){
		e=e||event;
		var code = e.which||e.keyCode; //FIXME  better solution is required
		this.callEvent((this._edit_id?"onEditKeyPress":"onKeyPress"),[code,e.ctrlKey,e.shiftKey,e]);
	}
};


/* DHX DEPEND FROM FILE 'mouse.js'*/


/*
	Behavior:MouseEvents - provides inner evnets for  mouse actions
*/
dhtmlx.MouseEvents={
	_init: function(){
		//attach dom events if related collection is defined
		if (this.on_click){
			dhtmlx.event(this._obj,"click",this._onClick,this);
			dhtmlx.event(this._obj,"contextmenu",this._onContext,this);
		}
		if (this.on_dblclick)
			dhtmlx.event(this._obj,"dblclick",this._onDblClick,this);
		if (this.on_mouse_move){
			dhtmlx.event(this._obj,"mousemove",this._onMouse,this);
			dhtmlx.event(this._obj,(dhtmlx._isIE?"mouseleave":"mouseout"),this._onMouse,this);
		}

	},
	//inner onclick object handler
	_onClick: function(e) {
		return this._mouseEvent(e,this.on_click,"ItemClick");
	},
	//inner ondblclick object handler
	_onDblClick: function(e) {
		return this._mouseEvent(e,this.on_dblclick,"ItemDblClick");
	},
	//process oncontextmenu events
	_onContext: function(e) {
		var id = dhtmlx.html.locate(e, this._id);
		if (id && !this.callEvent("onBeforeContextMenu", [id,e]))
			return dhtmlx.html.preventEvent(e);
	},
	/*
		event throttler - ignore events which occurs too fast
		during mouse moving there are a lot of event firing - we need no so much
		also, mouseout can fire when moving inside the same html container - we need to ignore such fake calls
	*/
	_onMouse:function(e){
		if (dhtmlx._isIE)	//make a copy of event, will be used in timed call
			e = document.createEventObject(event);
			
		if (this._mouse_move_timer)	//clear old event timer
			window.clearTimeout(this._mouse_move_timer);
				
		//this event just inform about moving operation, we don't care about details
		this.callEvent("onMouseMoving",[e]);
		//set new event timer
		this._mouse_move_timer = window.setTimeout(dhtmlx.bind(function(){
			//called only when we have at least 100ms after previous event
			if (e.type == "mousemove")
				this._onMouseMove(e);
			else
				this._onMouseOut(e);
		},this),500);
	},
	//inner mousemove object handler
	_onMouseMove: function(e) {
		if (!this._mouseEvent(e,this.on_mouse_move,"MouseMove"))
			this.callEvent("onMouseOut",[e||event]);
	},
	//inner mouseout object handler
	_onMouseOut: function(e) {
		this.callEvent("onMouseOut",[e||event]);
	},
	//common logic for click and dbl-click processing
	_mouseEvent:function(e,hash,name){
		e=e||event;
		var trg=e.target||e.srcElement;
		var css = "";
		var id = null;
		var found = false;
		//loop through all parents
		while (trg && trg.parentNode){
			if (!found && trg.getAttribute){													//if element with ID mark is not detected yet
				id = trg.getAttribute(this._id);							//check id of current one
				if (id){
					if (trg.getAttribute("userdata"))
						this.callEvent("onLocateData",[id,trg]);
					if (!this.callEvent("on"+name,[id,e,trg])) return;		//it will be triggered only for first detected ID, in case of nested elements
					found = true;											//set found flag
				}
			}
			css=trg.className;
			if (css){		//check if pre-defined reaction for element's css name exists
				css = css.split(" ");
				css = css[0]||css[1]; //FIXME:bad solution, workaround css classes which are starting from whitespace
				if (hash[css])
					return  hash[css].call(this,e,id||dhx.html.locate(e, this._id),trg);
			}
			trg=trg.parentNode;
		}		
		return found;	//returns true if item was located and event was triggered
	}
};


/* DHX DEPEND FROM FILE 'selection.js'*/


/*
	Behavior:SelectionModel - manage selection states
	@export
		select
		unselect
		selectAll
		unselectAll
		isSelected
		getSelected
*/
dhtmlx.SelectionModel={
	_init:function(){
		//collection of selected IDs
		this._selected = dhtmlx.toArray();
		dhtmlx.assert(this.data, "SelectionModel :: Component doesn't have DataStore");
         	
		//remove selection from deleted items
		this.data.attachEvent("onStoreUpdated",dhtmlx.bind(this._data_updated,this));
		this.data.attachEvent("onStoreLoad", dhtmlx.bind(this._data_loaded,this));
		this.data.attachEvent("onAfterFilter", dhtmlx.bind(this._data_filtered,this));
		this.data.attachEvent("onIdChange", dhtmlx.bind(this._id_changed,this));
	},
	_id_changed:function(oldid, newid){
		for (var i = this._selected.length - 1; i >= 0; i--)
			if (this._selected[i]==oldid)
				this._selected[i]=newid;
	},
	_data_filtered:function(){
		for (var i = this._selected.length - 1; i >= 0; i--){
			if (this.data.indexById(this._selected[i]) < 0)
				var id = this._selected[i];
				var item = this.item(id);
				if (item)
					delete item.$selected;
				this._selected.splice(i,1);
				this.callEvent("onSelectChange",[id]);
		}	
	},
	//helper - linked to onStoreUpdated
	_data_updated:function(id,obj,type){
		if (type == "delete")				//remove selection from deleted items
			this._selected.remove(id);
		else if (!this.data.dataCount() && !this.data._filter_order){	//remove selection for clearAll
			this._selected = dhtmlx.toArray();
		}
	},
	_data_loaded:function(){
		if (this._settings.select)
			this.data.each(function(obj){
				if (obj.$selected) this.select(obj.id);
			}, this);
	},
	//helper - changes state of selection for some item
	_select_mark:function(id,state,refresh){
		if (!refresh && !this.callEvent("onBeforeSelect",[id,state])) return false;
		
		this.data.item(id).$selected=state;	//set custom mark on item
		if (refresh)
			refresh.push(id);				//if we in the mass-select mode - collect all changed IDs
		else{
			if (state)
				this._selected.push(id);		//then add to list of selected items
		else
				this._selected.remove(id);
			this._refresh_selection(id);	//othervise trigger repainting
		}
			
		return true;
	},
	//select some item
	select:function(id,non_modal,continue_old){
		//if id not provide - works as selectAll
		if (!id) return this.selectAll();

		//allow an array of ids as parameter
		if (id instanceof Array){
			for (var i=0; i < id.length; i++)
				this.select(id[i], non_modal, continue_old);
			return;
		}

		if (!this.data.exists(id)){
			dhtmlx.error("Incorrect id in select command: "+id);
			return;
		}
		
		//block selection mode
		if (continue_old && this._selected.length)
			return this.selectAll(this._selected[this._selected.length-1],id);
		//single selection mode
		if (!non_modal && (this._selected.length!=1 || this._selected[0]!=id)){
			this._silent_selection = true; //prevent unnecessary onSelectChange event
			this.unselectAll();
			this._silent_selection = false;
		}
		if (this.isSelected(id)){
			if (non_modal) this.unselect(id);	//ctrl-selection of already selected item
			return;
		}
		
		if (this._select_mark(id,true)){	//if not blocked from event
			this.callEvent("onAfterSelect",[id]);
		}
	},
	//unselect some item
	unselect:function(id){
		//if id is not provided  - unselect all items
		if (!id) return this.unselectAll();
		if (!this.isSelected(id)) return;
		
		this._select_mark(id,false);
	},
	//select all items, or all in defined range
	selectAll:function(from,to){
		var range;
		var refresh=[];
		
		if (from||to)
			range = this.data.getRange(from||null,to||null);	//get limited set if bounds defined
		else
			range = this.data.getRange();			//get all items in other case
												//in case of paging - it will be current page only
		range.each(function(obj){ 
			var d = this.data.item(obj.id);
			if (!d.$selected){	
				this._selected.push(obj.id);	
				this._select_mark(obj.id,true,refresh);
			}
			return obj.id; 
		},this);
		//repaint self
		this._refresh_selection(refresh);
	},
	//remove selection from all items
	unselectAll:function(){
		var refresh=[];
		
		this._selected.each(function(id){
			this._select_mark(id,false,refresh);	//unmark selected only
		},this);
		
		this._selected=dhtmlx.toArray();
		this._refresh_selection(refresh);	//repaint self
	},
	//returns true if item is selected
	isSelected:function(id){
		return this._selected.find(id)!=-1;
	},
	/*
		returns ID of selected items or array of IDs
		to make result predictable - as_array can be used, 
			with such flag command will always return an array 
			empty array in case when no item was selected
	*/
	getSelected:function(as_array){	
		switch(this._selected.length){
			case 0: return as_array?[]:"";
			case 1: return as_array?[this._selected[0]]:this._selected[0];
			default: return ([].concat(this._selected)); //isolation
		}
	},
	//detects which repainting mode need to be used
	_is_mass_selection:function(obj){
		 // crappy heuristic, but will do the job
		return obj.length>100 || obj.length > this.data.dataCount/2;
	},
	_refresh_selection:function(refresh){
		if (typeof refresh != "object") refresh = [refresh];
		if (!refresh.length) return;	//nothing to repaint
		
		if (this._is_mass_selection(refresh))	
			this.data.refresh();	//many items was selected - repaint whole view
		else
			for (var i=0; i < refresh.length; i++)	//repaint only selected
				this.render(refresh[i],this.data.item(refresh[i]),"update");
			
		if (!this._silent_selection)	
		this.callEvent("onSelectChange",[refresh]);
	}
};


/* DHX DEPEND FROM FILE 'render.js'*/


/*
	Renders collection of items
	Behavior uses plain strategy which suits only for relative small datasets
	
	@export
		locate
		show
		render
*/
dhtmlx.RenderStack={
	_init:function(){
		dhtmlx.assert(this.data,"RenderStack :: Component doesn't have DataStore");
        dhtmlx.assert(dhtmlx.Template,"dhtmlx.Template :: dhtmlx.Template is not accessible");

		//used for temporary HTML elements
		//automatically nulified during destruction
		this._html = document.createElement("DIV");

	},
	//convert single item to HTML text (templating)
	_toHTML:function(obj){
			//check if related template exist
			dhtmlx.assert((!obj.$template || this.type["template_"+obj.$template]),"RenderStack :: Unknown template: "+obj.$template);
                        
			/*mm: fix allows to call the event for all objects (PropertySheet)*/	
			//if (obj.$template) //custom template
				this.callEvent("onItemRender",[obj]);
			/*
				$template property of item, can contain a name of custom template
			*/	
			return this.type._item_start(obj,this.type)+(obj.$template?this.type["template_"+obj.$template]:this.type.template)(obj,this.type)+this.type._item_end;
	},
	//convert item to HTML object (templating)
	_toHTMLObject:function(obj){
		this._html.innerHTML = this._toHTML(obj);
		return this._html.firstChild;
	},
	//return html container by its ID
	//can return undefined if container doesn't exists
	_locateHTML:function(search_id){
		if (this._htmlmap)
			return this._htmlmap[search_id];
			
		//fill map if it doesn't created yet
		this._htmlmap={};
		
		var t = this._dataobj.childNodes;
		for (var i=0; i < t.length; i++){
			var id = t[i].getAttribute(this._id); //get item's
			if (id) 
				this._htmlmap[id]=t[i];
		}
		//call locator again, when map is filled
		return this._locateHTML(search_id);
	},
	//return id of item from html event
	locate:function(e){ return dhtmlx.html.locate(e,this._id); },
	//change scrolling state of top level container, so related item will be in visible part
	show:function(id){
		var html = this._locateHTML(id);
		if (html)
			this._dataobj.scrollTop = html.offsetTop-this._dataobj.offsetTop;
	},
	//update view after data update
	//method calls low-level rendering for related items
	//when called without parameters - all view refreshed
	render:function(id,data,type,after){
		if (id){
			var cont = this._locateHTML(id); //get html element of updated item
			switch(type){
				case "update":
					//in case of update - replace existing html with updated one
					if (!cont) return;
					var t = this._htmlmap[id] = this._toHTMLObject(data);
					dhtmlx.html.insertBefore(t, cont); 
					dhtmlx.html.remove(cont);
					break;
				case "delete":
					//in case of delete - remove related html
					if (!cont) return;
					dhtmlx.html.remove(cont);
					delete this._htmlmap[id];
					break;
				case "add":
					//in case of add - put new html at necessary position
					var t = this._htmlmap[id] = this._toHTMLObject(data);
					dhtmlx.html.insertBefore(t, this._locateHTML(this.data.next(id)), this._dataobj);
					break;
				case "move":
					//in case of move , simulate add - delete sequence
					//it will affect only rendering 
					this.render(id,data,"delete");
					this.render(id,data,"add");
					break;
				default:
					dhtmlx.error("Unknown render command: "+type);
					break;
			}
		} else {
			//full reset
			if (this.callEvent("onBeforeRender",[this.data])){
				//getRange - returns all elements
				this._dataobj.innerHTML = this.data.getRange().map(this._toHTML,this).join("");
				this._htmlmap = null; //clear map, it will be filled at first _locateHTML
			}
		}
		this.callEvent("onAfterRender",[]);
	},
	//pager attachs handler to onBeforeRender, to limit visible set of data 
	//data.min and data.max affect result of data.getRange()
	pager_setter:function(value){ 
		this.attachEvent("onBeforeRender",function(){
			var s = this._settings.pager._settings;
			//initial value of pager = -1, waiting for real value
			if (s.page == -1) return false;	
			
			this.data.min = s.page*s.size;	//affect data.getRange
			this.data.max = (s.page+1)*s.size-1;
			return true;
		});
	
		var pager = new dhtmlx.ui.pager(value);
		//update functor
		var update = dhtmlx.bind(function(){
			this.data.refresh();
		},this);
		
		//when values of pager are changed - repaint view
		pager.attachEvent("onRefresh",update);
		//when something changed in DataStore - update configuration of pager
		//during refresh - pager can call onRefresh method which will cause repaint of view
		this.data.attachEvent("onStoreUpdated",function(data){
			var count = this.dataCount();
			if (count != pager._settings.count){
				pager._settings.count = count;
				//set first page
				//it is called first time after data loading
				//until this time pager is not rendered
				if (pager._settings.page == -1)
					pager._settings.page = 0;
					
				pager.refresh();
			}
		});
		return pager;
	},
	//can be used only to trigger auto-height
	height_setter:function(value){
		if (value=="auto"){
			//react on resize of window and self-repainting
			this.attachEvent("onAfterRender",this._correct_height);
			dhtmlx.event(window,"resize",dhtmlx.bind(this._correct_height,this));
		}
		return value;
	},
	//update height of container to remove inner scrolls
	_correct_height:function(){
		//disable scrolls - if we are using auto-height , they are not necessary
		this._dataobj.style.overflow="hidden";
		//set min. size, so we can fetch real scroll height
		this._dataobj.style.height = "1px";
		
		var t = this._dataobj.scrollHeight;
		this._dataobj.style.height = t+"px";
		// FF has strange issue with height caculation 
		// it incorrectly detects scroll height when only small part of item is invisible
		if (dhtmlx._isFF){ 
			var t2 = this._dataobj.scrollHeight;
			if (t2!=t)
				this._dataobj.style.height = t2+"px";
		}
		this._obj.style.height = this._dataobj.style.height;
	},
	//get size of single item
	_getDimension:function(){
		var t = this.type;
		var d = (t.border||0)+(t.padding||0)*2+(t.margin||0)*2;
		//obj.x  - widht, obj.y - height
		return {x : t.width+d, y: t.height+d };
	},
	//x_count propery sets width of container, so N items can be placed on single line
	x_count_setter:function(value){
		var dim = this._getDimension();
		this._dataobj.style.width = dim.x*value+(this._settings.height != "auto"?18:0)+"px";
		return value;
	},
	//x_count propery sets height of container, so N items a visible in one column
	//column will have scroll if real count of lines is greater than N
	y_count_setter:function(value){
		var dim = this._getDimension();
		this._dataobj.style.height = dim.y*value+"px";
		return value;
	}
};


/* DHX DEPEND FROM FILE 'virtual_render.js'*/


/*
	Renders collection of items
	Always shows y-scroll
	Can be used with huge datasets
	
	@export
		show
		render
*/

/*DHX:Depend render.js*/ 

dhtmlx.VirtualRenderStack={
	_init:function(){
		dhtmlx.assert(this.render,"VirtualRenderStack :: Object must use RenderStack first");
        	
        this._htmlmap={}; //init map of rendered elements
        //in this mode y-scroll is always visible
        //it simplifies calculations a lot
        this._dataobj.style.overflowY="scroll";
        
        //we need to repaint area each time when view resized or scrolling state is changed
        dhtmlx.event(this._dataobj,"scroll",dhtmlx.bind(this._render_visible_rows,this));
        dhtmlx.event(window,"resize",dhtmlx.bind(function(){ this.render(); },this));

		//here we store IDs of elemenst which doesn't loadede yet, but need to be rendered
		this.data._unrendered_area=[];
		this.data.getIndexRange=this._getIndexRange;
	},
	//return html object by item's ID. Can return null for not-rendering element
	_locateHTML:function(search_id){
		//collection was filled in _render_visible_rows
		return this._htmlmap[search_id];
	},
	//adjust scrolls to make item visible
	show:function(id){
		range = this._getVisibleRange();
		var ind = this.data.indexById(id);
		//we can't use DOM method for not-rendered-yet items, so fallback to pure math
		var dy = Math.floor(ind/range._dx)*range._y;
		this._dataobj.scrollTop = dy;
	},	
	_getIndexRange:function(from,to){
		if (to !== 0)
			to=Math.min((to||Infinity),this.dataCount()-1);
		
		var ret=dhtmlx.toArray(); //result of method is rich-array
		for (var i=(from||0); i <= to; i++){
			var item = this.item(this.order[i]);
			if(this.order.length>i){
                if (!item){
                    this.order[i]=dhtmlx.uid();
                    item = { id:this.order[i], $template:"loading" };
                    this._unrendered_area.push(this.order[i]);	//store item ID for later loading
                } else if (item.$template == "loading")
                    this._unrendered_area.push(this.order[i]);
                ret.push(item);
            }

		}
		return ret;
	},	
	//repain self after changes in DOM
	//for add, delete, move operations - render is delayed, to minify performance impact
	render:function(id,data,type,after){ 
		if (id){
			var cont = this._locateHTML(id);	//old html element
			switch(type){
				case "update":
					if (!cont) return;
					//replace old with new
					var t = this._htmlmap[id] = this._toHTMLObject(data);
					dhtmlx.html.insertBefore(t, cont); 
					dhtmlx.html.remove(cont);
					break;
				default: // "move", "add", "delete"
					/*
						for all above operations, full repainting is necessary
						but from practical point of view, we need only one repainting per thread
						code below initiates double-thread-rendering trick
					*/
					this._render_delayed();
					break;
			}
		} else {
			//full repainting
			if (this.callEvent("onBeforeRender",[this.data])){
				this._htmlmap = {}; 					//nulify links to already rendered elements
				this._render_visible_rows(null, true);	
				// clear delayed-rendering, because we already have repaint view
				this._wait_for_render = false;			
				this.callEvent("onAfterRender",[]);
			}
		}
	},
	//implement double-thread-rendering pattern
	_render_delayed:function(){
		//this flag can be reset from outside, to prevent actual rendering 
		if (this._wait_for_render) return;
		this._wait_for_render = true;	
		
		window.setTimeout(dhtmlx.bind(function(){
			this.render();
		},this),1);
	},
	//create empty placeholders, which will take space before rendering
	_create_placeholder:function(height){
		var node = document.createElement("DIV");
			node.style.cssText = "height:"+height+"px; width:100%; overflow:hidden;";
		return node;
	},
	/*
		Methods get coordinatest of visible area and checks that all related items are rendered
		If, during rendering, some not-loaded items was detected - extra data loading is initiated.
		reset - flag, which forces clearing of previously rendered elements
	*/
	_render_visible_rows:function(e,reset){
		this.data._unrendered_area=[]; //clear results of previous calls
		
		var viewport = this._getVisibleRange();	//details of visible view
		if (!this._dataobj.firstChild || reset){	//create initial placeholder - for all view space
			this._dataobj.innerHTML="";
			this._dataobj.appendChild(this._create_placeholder(viewport._max));
			//register placeholder in collection
			this._htmlrows = [this._dataobj.firstChild];
		}
		
		/*
			virtual rendering breaks all view on rows, because we know widht of item
			we can calculate how much items can be placed on single row, and knowledge 
			of that, allows to calculate count of such rows
			
			each time after scrolling, code iterate through visible rows and render items 
			in them, if they are not rendered yet
			
			both rendered rows and placeholders are registered in _htmlrows collection
		*/

		//position of first visible row
		var t = viewport._from;
		//max can be 0, in case of 1 row per page
		var max_row = (this.data.max || this.data.max === 0)?this.data.max:Infinity;
		
		while(t<=viewport._height){	//loop for all visible rows
			//skip already rendered rows
			while(this._htmlrows[t] && this._htmlrows[t]._filled && t<=viewport._height){
				t++; 
			}
			//go out if all is rendered
			if (t>viewport._height) break;
			
			//locate nearest placeholder
			var holder = t;
			while (!this._htmlrows[holder]) holder--;
			var holder_row = this._htmlrows[holder];
			
			//render elements in the row			
			var base = t*viewport._dx+(this.data.min||0);	//index of rendered item
			if (base > max_row) break;	//check that row is in virtual bounds, defined by paging
			var nextpoint =  Math.min(base+viewport._dx-1,max_row);
			var node = this._create_placeholder(viewport._y);
			//all items in rendered row
			var range = this.data.getIndexRange(base, nextpoint);
			if (!range.length) break; 
			
			node.innerHTML=range.map(this._toHTML,this).join(""); 	//actual rendering
			for (var i=0; i < range.length; i++)					//register all new elements for later usage in _locateHTML
				this._htmlmap[this.data.idByIndex(base+i)]=node.childNodes[i];
			
			//correct placeholders
			var h = parseInt(holder_row.style.height,10);
			var delta = (t-holder)*viewport._y;
			var delta2 = (h-delta-viewport._y);
			
			//add new row to the DOOM
			dhtmlx.html.insertBefore(node,delta?holder_row.nextSibling:holder_row,this._dataobj);
			this._htmlrows[t]=node;
			node._filled = true;
			
			/*
				if new row is at start of placeholder - decrease placeholder's height
				else if new row takes whole placeholder - remove placeholder from DOM
				else 
					we are inserting row in the middle of existing placeholder
					decrease height of existing one, and add one more, 
					before the newly added row
			*/
			if (delta <= 0 && delta2>0){
				holder_row.style.height = delta2+"px";
				this._htmlrows[t+1] = holder_row;
			} else {
				if (delta<0)
					dhtmlx.html.remove(holder_row);
				else
					holder_row.style.height = delta+"px";
				if (delta2>0){ 
					var new_space = this._htmlrows[t+1] = this._create_placeholder(delta2);
					dhtmlx.html.insertBefore(new_space,node.nextSibling,this._dataobj);
				}
			}
			
			
			t++;
		}
		//when all done, check for non-loaded items
		if (this.data._unrendered_area.length){
			//we have some data to load
			//detect borders
			var from = this.indexById(this.data._unrendered_area[0]);
			var to = this.indexById(this.data._unrendered_area.pop())+1;
			if (to>from){
				//initiate data loading
				if (!this.callEvent("onDataRequest",[from, to-from])) return false;
				dhtmlx.assert(this.data.feed,"Data feed is missed");
				this.data.feed.call(this,from,to-from);
			}
		}
		if (dhtmlx._isIE){
				var viewport2 = this._getVisibleRange();
				if (viewport2._from != viewport._from)
					this._render_visible_rows();
		}
	},
	//calculates visible view
	_getVisibleRange:function(){
		var top = this._dataobj.scrollTop;
		var width = Math.max(this._dataobj.scrollWidth,this._dataobj.offsetWidth)-18; 	// opera returns zero scrollwidth for the empty object
		var height = this._dataobj.offsetHeight;									// 18 - scroll
		//size of single item
		var t = this.type;
		var dim = this._getDimension();

		var dx = Math.floor(width/dim.x)||1; //at least single item per row
		
		var min = Math.floor(top/dim.y);				//index of first visible row
		var dy = Math.ceil((height+top)/dim.y)-1;		//index of last visible row
		//total count of items, paging can affect this math
		var count = this.data.max?(this.data.max-this.data.min):this.data.dataCount();
		var max = Math.ceil(count/dx)*dim.y;			//size of view in rows
		
		return { _from:min, _height:dy, _top:top, _max:max, _y:dim.y, _dx:dx};
	}
};


/* DHX DEPEND FROM FILE 'load.js'*/


/* 
	ajax operations 
	
	can be used for direct loading as
		dhtmlx.ajax(ulr, callback)
	or
		dhtmlx.ajax().item(url)
		dhtmlx.ajax().post(url)

*/

/*DHX:Depend dhtmlx.js*/

dhtmlx.ajax = function(url,call,master){
	//if parameters was provided - made fast call
	if (arguments.length!==0){
		var http_request = new dhtmlx.ajax();
		if (master) http_request.master=master;
		http_request.get(url,null,call);
	}
	if (!this.getXHR) return new dhtmlx.ajax(); //allow to create new instance without direct new declaration
	
	return this;
};
dhtmlx.ajax.prototype={
	//creates xmlHTTP object
	getXHR:function(){
		if (dhtmlx._isIE)
		 return new ActiveXObject("Microsoft.xmlHTTP");
		else 
		 return new XMLHttpRequest();
	},
	/*
		send data to the server
		params - hash of properties which will be added to the url
		call - callback, can be an array of functions
	*/
	send:function(url,params,call){
		var x=this.getXHR();
		if (typeof call == "function")
		 call = [call];
		//add extra params to the url
		if (typeof params == "object"){
			var t=[];
			for (var a in params){
				var value = params[a];
				if (value === null || value === dhtmlx.undefined)
					value = "";
				t.push(a+"="+encodeURIComponent(value));// utf-8 escaping
		 	}
			params=t.join("&");
		}
		if (params && !this.post){
			url=url+(url.indexOf("?")!=-1 ? "&" : "?")+params;
			params=null;
		}
		
		x.open(this.post?"POST":"GET",url,!this._sync);
		if (this.post)
		 x.setRequestHeader('Content-type','application/x-www-form-urlencoded');
		 
		//async mode, define loading callback
		//if (!this._sync){
		 var self=this;
		 x.onreadystatechange= function(){
			if (!x.readyState || x.readyState == 4){
				//dhtmlx.log_full_time("data_loading");	//log rendering time
				if (call && self) 
					for (var i=0; i < call.length; i++)	//there can be multiple callbacks
					 if (call[i])
						call[i].call((self.master||self),x.responseText,x.responseXML,x);
				self.master=null;
				call=self=null;	//anti-leak
			}
		 };
		//}
		
		x.send(params||null);
		return x; //return XHR, which can be used in case of sync. mode
	},
	//GET request
	get:function(url,params,call){
		this.post=false;
		return this.send(url,params,call);
	},
	//POST request
	post:function(url,params,call){
		this.post=true;
		return this.send(url,params,call);
	}, 
	sync:function(){
		this._sync = true;
		return this;
	}
};


dhtmlx.AtomDataLoader={
	_init:function(config){
		//prepare data store
		this.data = {}; 
		if (config){
			this._settings.datatype = config.datatype||"json";
			this._after_init.push(this._load_when_ready);
		}
	},
	_load_when_ready:function(){
		this._ready_for_data = true;
		
		if (this._settings.url)
			this.url_setter(this._settings.url);
		if (this._settings.data)
			this.data_setter(this._settings.data);
	},
	url_setter:function(value){
		if (!this._ready_for_data) return value;
		this.load(value, this._settings.datatype);	
		return value;
	},
	data_setter:function(value){
		if (!this._ready_for_data) return value;
		this.parse(value, this._settings.datatype);
		return true;
	},
	//loads data from external URL
	load:function(url,call){
		this.callEvent("onXLS",[]);
		if (typeof call == "string"){	//second parameter can be a loading type or callback
			this.data.driver = dhtmlx.DataDriver[call];
			call = arguments[2];
		}
		else
			this.data.driver = dhtmlx.DataDriver["xml"];
		//load data by async ajax call
		dhtmlx.ajax(url,[this._onLoad,call],this);
	},
	//loads data from object
	parse:function(data,type){
		this.callEvent("onXLS",[]);
		this.data.driver = dhtmlx.DataDriver[type||"xml"];
		this._onLoad(data,null);
	},
	//default after loading callback
	_onLoad:function(text,xml,loader){
		var driver = this.data.driver;
		var top = driver.getRecords(driver.toObject(text,xml))[0];
		this.data=(driver?driver.getDetails(top):text);
		this.callEvent("onXLE",[]);
	},
	_check_data_feed:function(data){
		if (!this._settings.dataFeed || this._ignore_feed || !data) return true;
		var url = this._settings.dataFeed;
		if (typeof url == "function")
			return url.call(this, (data.id||data), data);
		url = url+(url.indexOf("?")==-1?"?":"&")+"action=get&id="+encodeURIComponent(data.id||data);
		this.callEvent("onXLS",[]);
		dhtmlx.ajax(url, function(text,xml){
			this._ignore_feed=true;
			this.setValues(dhtmlx.DataDriver.json.toObject(text)[0]);
			this._ignore_feed=false;
			this.callEvent("onXLE",[]);
		}, this);
		return false;
	}
};

/*
	Abstraction layer for different data types
*/

dhtmlx.DataDriver={};
dhtmlx.DataDriver.json={
	//convert json string to json object if necessary
	toObject:function(data){
		if (!data) data="[]";
		if (typeof data == "string"){
		 eval ("dhtmlx.temp="+data);
		 return dhtmlx.temp;
		}
		return data;
	},
	//get array of records
	getRecords:function(data){
		if (data && !(data instanceof Array))
		 return [data];
		return data;
	},
	//get hash of properties for single record
	getDetails:function(data){
		return data;
	},
	//get count of data and position at which new data need to be inserted
	getInfo:function(data){
		return { 
		 _size:(data.total_count||0),
		 _from:(data.pos||0),
		 _key:(data.dhx_security)
		};
	}
};

dhtmlx.DataDriver.json_ext={
	//convert json string to json object if necessary
	toObject:function(data){
		if (!data) data="[]";
		if (typeof data == "string"){
			var temp;
			eval ("temp="+data);
			dhtmlx.temp = [];
			var header  = temp.header;
			for (var i = 0; i < temp.data.length; i++) {
				var item = {};
				for (var j = 0; j < header.length; j++) {
					if (typeof(temp.data[i][j]) != "undefined")
						item[header[j]] = temp.data[i][j];
				}
				dhtmlx.temp.push(item);
			}
			return dhtmlx.temp;
		}
		return data;
	},
	//get array of records
	getRecords:function(data){
		if (data && !(data instanceof Array))
		 return [data];
		return data;
	},
	//get hash of properties for single record
	getDetails:function(data){
		return data;
	},
	//get count of data and position at which new data need to be inserted
	getInfo:function(data){
		return {
		 _size:(data.total_count||0),
		 _from:(data.pos||0)
		};
	}
};

dhtmlx.DataDriver.html={
	/*
		incoming data can be
		 - collection of nodes
		 - ID of parent container
		 - HTML text
	*/
	toObject:function(data){
		if (typeof data == "string"){
		 var t=null;
		 if (data.indexOf("<")==-1)	//if no tags inside - probably its an ID
			t = dhtmlx.toNode(data);
		 if (!t){
			t=document.createElement("DIV");
			t.innerHTML = data;
		 }
		 
		 return t.getElementsByTagName(this.tag);
		}
		return data;
	},
	//get array of records
	getRecords:function(data){
		if (data.tagName)
		 return data.childNodes;
		return data;
	},
	//get hash of properties for single record
	getDetails:function(data){
		return dhtmlx.DataDriver.xml.tagToObject(data);
	},
	//dyn loading is not supported by HTML data source
	getInfo:function(data){
		return { 
		 _size:0,
		 _from:0
		};
	},
	tag: "LI"
};

dhtmlx.DataDriver.jsarray={
	//eval jsarray string to jsarray object if necessary
	toObject:function(data){
		if (typeof data == "string"){
		 eval ("dhtmlx.temp="+data);
		 return dhtmlx.temp;
		}
		return data;
	},
	//get array of records
	getRecords:function(data){
		return data;
	},
	//get hash of properties for single record, in case of array they will have names as "data{index}"
	getDetails:function(data){
		var result = {};
		for (var i=0; i < data.length; i++) 
		 result["data"+i]=data[i];
		 
		return result;
	},
	//dyn loading is not supported by js-array data source
	getInfo:function(data){
		return { 
		 _size:0,
		 _from:0
		};
	}
};

dhtmlx.DataDriver.csv={
	//incoming data always a string
	toObject:function(data){
		return data;
	},
	//get array of records
	getRecords:function(data){
		return data.split(this.row);
	},
	//get hash of properties for single record, data named as "data{index}"
	getDetails:function(data){
		data = this.stringToArray(data);
		var result = {};
		for (var i=0; i < data.length; i++) 
		 result["data"+i]=data[i];
		 
		return result;
	},
	//dyn loading is not supported by csv data source
	getInfo:function(data){
		return { 
		 _size:0,
		 _from:0
		};
	},
	//split string in array, takes string surrounding quotes in account
	stringToArray:function(data){
		data = data.split(this.cell);
		for (var i=0; i < data.length; i++)
		 data[i] = data[i].replace(/^[ \t\n\r]*(\"|)/g,"").replace(/(\"|)[ \t\n\r]*$/g,"");
		return data;
	},
	row:"\n",	//default row separator
	cell:","	//default cell separator
};

dhtmlx.DataDriver.xml={
	//convert xml string to xml object if necessary
	toObject:function(text,xml){
		if (xml && (xml=this.checkResponse(text,xml)))	//checkResponse - fix incorrect content type and extra whitespaces errors
		 return xml;
		if (typeof text == "string"){
		 return this.fromString(text);
		}
		return text;
	},
	//get array of records
	getRecords:function(data){
		return this.xpath(data,this.records);
	},
	records:"/*/item",
	//get hash of properties for single record
	getDetails:function(data){
		return this.tagToObject(data,{});
	},
	//get count of data and position at which new data_loading need to be inserted
	getInfo:function(data){
		return { 
		 _size:(data.documentElement.getAttribute("total_count")||0),
		 _from:(data.documentElement.getAttribute("pos")||0),
		 _key:(data.documentElement.getAttribute("dhx_security"))
		};
	},
	//xpath helper
	xpath:function(xml,path){
		if (window.XPathResult){	//FF, KHTML, Opera
		 var node=xml;
		 if(xml.nodeName.indexOf("document")==-1)
		 xml=xml.ownerDocument;
		 var res = [];
		 var col = xml.evaluate(path, node, null, XPathResult.ANY_TYPE, null);
		 var temp = col.iterateNext();
		 while (temp){ 
			res.push(temp);
			temp = col.iterateNext();
		}
		return res;
		}	
		else {
			var test = true;
			try {
				if (typeof(xml.selectNodes)=="undefined")
					test = false;
			} catch(e){ /*IE7 and below can't operate with xml object*/ }
			//IE
			if (test)
				return xml.selectNodes(path);
			else {
				//Google hate us, there is no interface to do XPath
				//use naive approach
				var name = path.split("/").pop();
				return xml.getElementsByTagName(name);
			}
		}
	},
	//convert xml tag to js object, all subtags and attributes are mapped to the properties of result object
	tagToObject:function(tag,z){
		z=z||{};
		var flag=false;
		
		//map attributes
		var a=tag.attributes;
		if(a && a.length){
			for (var i=0; i<a.length; i++)
		 		z[a[i].name]=a[i].value;
		 	flag = true;
	 	}
		//map subtags
		
		var b=tag.childNodes;
		var state = {};
		for (var i=0; i<b.length; i++){
			if (b[i].nodeType==1){
				var name = b[i].tagName;
				if (typeof z[name] != "undefined"){
					if (!(z[name] instanceof Array))
						z[name]=[z[name]];
					z[name].push(this.tagToObject(b[i],{}));
				}
				else
					z[b[i].tagName]=this.tagToObject(b[i],{});	//sub-object for complex subtags
				flag=true;
			}
		}
		
		if (!flag)
			return this.nodeValue(tag);
		//each object will have its text content as "value" property
		z.value = this.nodeValue(tag);
		return z;
	},
	//get value of xml node 
	nodeValue:function(node){
		if (node.firstChild)
		 return node.firstChild.data;	//FIXME - long text nodes in FF not supported for now
		return "";
	},
	//convert XML string to XML object
	fromString:function(xmlString){
		if (window.DOMParser && !dhtmlx._isIE)		// FF, KHTML, Opera
		 return (new DOMParser()).parseFromString(xmlString,"text/xml");
		if (window.ActiveXObject){	// IE, utf-8 only 
		 var temp=new ActiveXObject("Microsoft.xmlDOM");
		 temp.loadXML(xmlString);
		 return temp;
		}
		dhtmlx.error("Load from xml string is not supported");
	},
	//check is XML correct and try to reparse it if its invalid
	checkResponse:function(text,xml){ 
		if (xml && ( xml.firstChild && xml.firstChild.tagName != "parsererror") )
			return xml;
		//parsing as string resolves incorrect content type
		//regexp removes whitespaces before xml declaration, which is vital for FF
		var a=this.fromString(text.replace(/^[\s]+/,""));
		if (a) return a;
		
		dhtmlx.error("xml can't be parsed",text);
	}
};




/* DHX DEPEND FROM FILE 'datastore.js'*/


/*DHX:Depend load.js*/
/*DHX:Depend dhtmlx.js*/

/*
	Behavior:DataLoader - load data in the component
	
	@export
		load
		parse
*/
dhtmlx.DataLoader={
	_init:function(config){
		//prepare data store
		config = config || "";
		this.name = "DataStore";
		this.data = (config.datastore)||(new dhtmlx.DataStore());
		this._readyHandler = this.data.attachEvent("onStoreLoad",dhtmlx.bind(this._call_onready,this));
	},
	//loads data from external URL
	load:function(url,call){
		dhtmlx.AtomDataLoader.load.apply(this, arguments);
		//prepare data feed for dyn. loading
		if (!this.data.feed)
		 this.data.feed = function(from,count){
			//allow only single request at same time
			if (this._load_count)
				return this._load_count=[from,count];	//save last ignored request
			else
				this._load_count=true;
				
			this.load(url+((url.indexOf("?")==-1)?"?":"&")+"posStart="+from+"&count="+count,function(){
				//after loading check if we have some ignored requests
				var temp = this._load_count;
				this._load_count = false;
				if (typeof temp =="object")
					this.data.feed.apply(this, temp);	//load last ignored request
			});
		};
	},
	//default after loading callback
	_onLoad:function(text,xml,loader){
		this.data._parse(this.data.driver.toObject(text,xml));
		this.callEvent("onXLE",[]);
		if(this._readyHandler){
			this.data.detachEvent(this._readyHandler);
			this._readyHandler = null;
		}
	},
	dataFeed_setter:function(value){
		this.data.attachEvent("onBeforeFilter", dhtmlx.bind(function(text, value){
			if (this._settings.dataFeed){
				var filter = {};
				if (!text && !filter) return;
				if (typeof text == "function"){
					if (!value) return;
					text(value, filter);
				} else 
					filter = { text:value };

				this.clearAll();
				var url = this._settings.dataFeed;
				if (typeof url == "function")
					return url.call(this, value, filter);
				var urldata = [];
				for (var key in filter)
					urldata.push("dhx_filter["+key+"]="+encodeURIComponent(filter[key]));
				this.load(url+(url.indexOf("?")<0?"?":"&")+urldata.join("&"), this._settings.datatype);
				return false;
			}
		},this));
		return value;
	},
	_call_onready:function(){
		if (this._settings.ready){
			var code = dhtmlx.toFunctor(this._settings.ready);
			if (code && code.call) code.apply(this, arguments);
		}
	}
};


/*
	DataStore is not a behavior, it standalone object, which represents collection of data.
	Call provideAPI to map data API

	@export
		exists
		idByIndex
		indexById
		get
		set
		refresh
		dataCount
		sort
		filter
		next
		previous
		clearAll
		first
		last
*/
dhtmlx.DataStore = function(){
	this.name = "DataStore";
	
	dhtmlx.extend(this, dhtmlx.EventSystem);
	
	this.setDriver("xml");	//default data source is an XML
	this.pull = {};						//hash of IDs
	this.order = dhtmlx.toArray();		//order of IDs
};

dhtmlx.DataStore.prototype={
	//defines type of used data driver
	//data driver is an abstraction other different data formats - xml, json, csv, etc.
	setDriver:function(type){
		dhtmlx.assert(dhtmlx.DataDriver[type],"incorrect DataDriver");
		this.driver = dhtmlx.DataDriver[type];
	},
	//process incoming raw data
	_parse:function(data){
		this.callEvent("onParse", [this.driver, data]);
		if (this._filter_order)
			this.filter();
			
		//get size and position of data
		var info = this.driver.getInfo(data);
		if (info._key)
			dhtmlx.security_key = info._key;
		//get array of records

		var recs = this.driver.getRecords(data);
		var from = (info._from||0)*1;
		
		if (from === 0 && this.order[0]) //update mode
			from = this.order.length;
		
		var j=0;
		for (var i=0; i<recs.length; i++){
			//get has of details for each record
			var temp = this.driver.getDetails(recs[i]);
			var id = this.id(temp); 	//generate ID for the record
			if (!this.pull[id]){		//if such ID already exists - update instead of insert
				this.order[j+from]=id;	
				j++;
			}
			this.pull[id]=temp;
			//if (this._format)	this._format(temp);
			
			if (this.extraParser)
				this.extraParser(temp);
			if (this._scheme){ 
				if (this._scheme.$init)
					this._scheme.$update(temp);
				else if (this._scheme.$update)
					this._scheme.$update(temp);
			}
		}

		//for all not loaded data
		for (var i=0; i < info._size; i++)
			if (!this.order[i]){
				var id = dhtmlx.uid();
				var temp = {id:id, $template:"loading"};	//create fake records
				this.pull[id]=temp;
				this.order[i]=id;
			}

		this.callEvent("onStoreLoad",[this.driver, data]);
		//repaint self after data loading
		this.refresh();
	},
	//generate id for data object
	id:function(data){
		return data.id||(data.id=dhtmlx.uid());
	},
	changeId:function(old, newid){
		dhtmlx.assert(this.pull[old],"Can't change id, for non existing item: "+old);
		this.pull[newid] = this.pull[old];
		this.pull[newid].id = newid;
		this.order[this.order.find(old)]=newid;
		if (this._filter_order)
			this._filter_order[this._filter_order.find(old)]=newid;
		this.callEvent("onIdChange", [old, newid]);
		if (this._render_change_id)
			this._render_change_id(old, newid);
	},
	get:function(id){
		return this.item(id);
	},
	set:function(id, data){
		return this.update(id, data);
	},
	//get data from hash by id
	item:function(id){
		return this.pull[id];
	},
	//assigns data by id
	update:function(id,data){
		if (this._scheme && this._scheme.$update)
			this._scheme.$update(data);
		if (this.callEvent("onBeforeUpdate", [id, data]) === false) return false;
		this.pull[id]=data;
		this.refresh(id);
	},
	//sends repainting signal
	refresh:function(id){
		if (this._skip_refresh) return; 
		
		if (id)
			this.callEvent("onStoreUpdated",[id, this.pull[id], "update"]);
		else
			this.callEvent("onStoreUpdated",[null,null,null]);
	},
	silent:function(code){
		this._skip_refresh = true;
		code.call(this);
		this._skip_refresh = false;
	},
	//converts range IDs to array of all IDs between them
	getRange:function(from,to){		
		//if some point is not defined - use first or last id
		//BEWARE - do not use empty or null ID
		if (from)
			from = this.indexById(from);
		else 
			from = this.startOffset||0;
		if (to)
			to = this.indexById(to);
		else {
			to = Math.min((this.endOffset||Infinity),(this.dataCount()-1));
			if (to<0) to = 0; //we have not data in the store
		}

		if (from>to){ //can be in case of backward shift-selection
			var a=to; to=from; from=a;
		}
				
		return this.getIndexRange(from,to);
	},
	//converts range of indexes to array of all IDs between them
	getIndexRange:function(from,to){
		to=Math.min((to||Infinity),this.dataCount()-1);
		
		var ret=dhtmlx.toArray(); //result of method is rich-array
		for (var i=(from||0); i <= to; i++)
			ret.push(this.item(this.order[i]));
		return ret;
	},
	//returns total count of elements
	dataCount:function(){
		return this.order.length;
	},
	//returns truy if item with such ID exists
	exists:function(id){
		return !!(this.pull[id]);
	},
	//nextmethod is not visible on component level, check DataMove.move
	//moves item from source index to the target index
	move:function(sindex,tindex){
		if (sindex<0 || tindex<0){
			dhtmlx.error("DataStore::move","Incorrect indexes");
			return;
		}
		
		var id = this.idByIndex(sindex);
		var obj = this.item(id);
		
		this.order.removeAt(sindex);	//remove at old position
		//if (sindex<tindex) tindex--;	//correct shift, caused by element removing
		this.order.insertAt(id,Math.min(this.order.length, tindex));	//insert at new position
		
		//repaint signal
		this.callEvent("onStoreUpdated",[id,obj,"move"]);
	},
	scheme:function(config){
		/*
			some.scheme({
				order:1,
				name:"dummy",
				title:""
			})
		*/
		this._scheme = config;
		
	},
	sync:function(source, filter, silent){
		if (typeof filter != "function"){
			silent = filter;
			filter = null;
		}
		
		if (dhtmlx.debug_bind){
			this.debug_sync_master = source; 
			dhtmlx.log("[sync] "+this.debug_bind_master.name+"@"+this.debug_bind_master._settings.id+" <= "+this.debug_sync_master.name+"@"+this.debug_sync_master._settings.id);
		}
		
		var topsource = source;
		if (source.name != "DataStore")
			source = source.data;

		var sync_logic = dhtmlx.bind(function(id, data, mode){
			if (mode != "update" || filter) 
				id = null;

			if (!id){
				this.order = dhtmlx.toArray([].concat(source.order));
				this._filter_order = null;
				this.pull = source.pull;
				
				if (filter)
					this.silent(filter);
				
				if (this._on_sync)
					this._on_sync();
			}

			if (dhtmlx.debug_bind)
				dhtmlx.log("[sync:request] "+this.debug_sync_master.name+"@"+this.debug_sync_master._settings.id + " <= "+this.debug_bind_master.name+"@"+this.debug_bind_master._settings.id);
			if (!silent) 
				this.refresh(id);
			else
				silent = false;
		}, this);
		
		source.attachEvent("onStoreUpdated", sync_logic);
		this.feed = function(from, count){
			topsource.loadNext(count, from);
		};
		sync_logic();
	},
	//adds item to the store
	add:function(obj,index){
		
		if (this._scheme){
			obj = obj||{};
			for (var key in this._scheme)
				obj[key] = obj[key]||this._scheme[key];
			if (this._scheme){ 
				if (this._scheme.$init)
					this._scheme.$update(obj);
				else if (this._scheme.$update)
					this._scheme.$update(obj);
			}
		}
		
		//generate id for the item
		var id = this.id(obj);
		
		//by default item is added to the end of the list
		var data_size = this.dataCount();
		
		if (dhtmlx.isNotDefined(index) || index < 0)
			index = data_size; 
		//check to prevent too big indexes			
		if (index > data_size){
			dhtmlx.log("Warning","DataStore:add","Index of out of bounds");
			index = Math.min(this.order.length,index);
		}
		if (this.callEvent("onBeforeAdd", [id, obj, index]) === false) return false;

		if (this.exists(id)) return dhtmlx.error("Not unique ID");
		
		this.pull[id]=obj;
		this.order.insertAt(id,index);
		if (this._filter_order){	//adding during filtering
			//we can't know the location of new item in full dataset, making suggestion
			//put at end by default
			var original_index = this._filter_order.length;
			//put at start only if adding to the start and some data exists
			if (!index && this.order.length)
				original_index = 0;
			
			this._filter_order.insertAt(id,original_index);
		}
		this.callEvent("onafterAdd",[id,index]);
		//repaint signal
		this.callEvent("onStoreUpdated",[id,obj,"add"]);
		return id;
	},
	
	//removes element from datastore
	remove:function(id){
		//id can be an array of IDs - result of getSelect, for example
		if (id instanceof Array){
			for (var i=0; i < id.length; i++)
				this.remove(id[i]);
			return;
		}
		if (this.callEvent("onBeforeDelete",[id]) === false) return false;
		if (!this.exists(id)) return dhtmlx.error("Not existing ID",id);
		var obj = this.item(id);	//save for later event
		//clear from collections
		this.order.remove(id);
		if (this._filter_order) 
			this._filter_order.remove(id);
			
		delete this.pull[id];
		this.callEvent("onafterdelete",[id]);
		//repaint signal
		this.callEvent("onStoreUpdated",[id,obj,"delete"]);
	},
	//deletes all records in datastore
	clearAll:function(){
		//instead of deleting one by one - just reset inner collections
		this.pull = {};
		this.order = dhtmlx.toArray();
		this.feed = null;
		this._filter_order = null;
		this.callEvent("onClearAll",[]);
		this.refresh();
	},
	//converts id to index
	idByIndex:function(index){
		if (index>=this.order.length || index<0)
			dhtmlx.log("Warning","DataStore::idByIndex Incorrect index");
			
		return this.order[index];
	},
	//converts index to id
	indexById:function(id){
		var res = this.order.find(id);	//slower than idByIndex
		
		//if (!this.pull[id])
		//	dhtmlx.log("Warning","DataStore::indexById Non-existing ID: "+ id);
			
		return res;
	},
	//returns ID of next element
	next:function(id,step){
		return this.order[this.indexById(id)+(step||1)];
	},
	//returns ID of first element
	first:function(){
		return this.order[0];
	},
	//returns ID of last element
	last:function(){
		return this.order[this.order.length-1];
	},
	//returns ID of previous element
	previous:function(id,step){
		return this.order[this.indexById(id)-(step||1)];
	},
	/*
		sort data in collection
			by - settings of sorting
		
		or
		
			by - sorting function
			dir - "asc" or "desc"
			
		or
		
			by - property
			dir - "asc" or "desc"
			as - type of sortings
		
		Sorting function will accept 2 parameters and must return 1,0,-1, based on desired order
	*/
	sort:function(by, dir, as){
		var sort = by;	
		if (typeof by == "function")
			sort = {as:by, dir:dir};
		else if (typeof by == "string")
			sort = {by:by, dir:dir, as:as};		
		
		
		var parameters = [sort.by, sort.dir, sort.as];
		if (!this.callEvent("onbeforesort",parameters)) return;	
		
		if (this.order.length){
			var sorter = dhtmlx.sort.create(sort);
			//get array of IDs
			var neworder = this.getRange(this.first(), this.last());
			neworder.sort(sorter);
			this.order = neworder.map(function(obj){ return this.id(obj); },this);
		}
		
		//repaint self
		this.refresh();
		
		this.callEvent("onaftersort",parameters);
	},
	/*
		Filter datasource
		
		text - property, by which filter
		value - filter mask
		
		or
		
		text  - filter method
		
		Filter method will receive data object and must return true or false
	*/
	filter:function(text,value){
		if (!this.callEvent("onBeforeFilter", [text, value])) return;
		
		//remove previous filtering , if any
		if (this._filter_order){
			this.order = this._filter_order;
			delete this._filter_order;
		}
		
		if (!this.order.length) return;
		
		//if text not define -just unfilter previous state and exit
		if (text){
			var filter = text;
			value = value||"";
			if (typeof text == "string"){
				text = dhtmlx.Template.fromHTML(text);
				value = value.toString().toLowerCase();
				filter = function(obj,value){	//default filter - string start from, case in-sensitive
					return text(obj).toLowerCase().indexOf(value)!=-1;
				};
			}
			
					
			var neworder = dhtmlx.toArray();
			for (var i=0; i < this.order.length; i++){
				var id = this.order[i];
				if (filter(this.item(id),value))
					neworder.push(id);
			}
			//set new order of items, store original
			this._filter_order = this.order;
			this.order = neworder;
		}
		//repaint self
		this.refresh();
		
		this.callEvent("onAfterFilter", []);
	},
	/*
		Iterate through collection
	*/
	each:function(method,master){
		for (var i=0; i<this.order.length; i++)
			method.call((master||this), this.item(this.order[i]));
	},
	/*
		map inner methods to some distant object
	*/
	provideApi:function(target,eventable){
		this.debug_bind_master = target;
			
		if (eventable){
			this.mapEvent({
				onbeforesort:	target,
				onaftersort:	target,
				onbeforeadd:	target,
				onafteradd:		target,
				onbeforedelete:	target,
				onafterdelete:	target,
				onbeforeupdate: target/*,
				onafterfilter:	target,
				onbeforefilter:	target*/
			});
		}
			
		var list = ["get","set","sort","add","remove","exists","idByIndex","indexById","item","update","refresh","dataCount","filter","next","previous","clearAll","first","last","serialize"];
		for (var i=0; i < list.length; i++)
			target[list[i]]=dhtmlx.methodPush(this,list[i]);
			
		if (dhtmlx.assert_enabled())		
			this.assert_event(target);
	},
	/*
		serializes data to a json object
	*/
	serialize: function(){
		var ids = this.order;
		var result = [];
		for(var i=0; i< ids.length;i++)
			result.push(this.pull[ids[i]]); 
		return result;
	}
};

dhtmlx.sort = {
	create:function(config){
		return dhtmlx.sort.dir(config.dir, dhtmlx.sort.by(config.by, config.as));
	},
	as:{
		"int":function(a,b){
			a = a*1; b=b*1;
			return a>b?1:(a<b?-1:0);
		},
		"string_strict":function(a,b){
			a = a.toString(); b=b.toString();
			return a>b?1:(a<b?-1:0);
		},
		"string":function(a,b){
			a = a.toString().toLowerCase(); b=b.toString().toLowerCase();
			return a>b?1:(a<b?-1:0);
		}
	},
	by:function(prop, method){
		if (!prop)
			return method;
		if (typeof method != "function")
			method = dhtmlx.sort.as[method||"string"];
		prop = dhtmlx.Template.fromHTML(prop);
		return function(a,b){
			return method(prop(a),prop(b));
		};
	},
	dir:function(prop, method){
		if (prop == "asc")
			return method;
		return function(a,b){
			return method(a,b)*-1;
		};
	}
};





/* DHX DEPEND FROM FILE 'compatibility_layout.js'*/


/*DHX:Depend dhtmlx.js*/
/*DHX:Depend compatibility.js*/

if (!dhtmlx.attaches)
	dhtmlx.attaches = {};
	
dhtmlx.attaches.attachAbstract=function(name, conf){
	var obj = document.createElement("DIV");
	obj.id = "CustomObject_"+dhtmlx.uid();
	obj.style.width = "100%";
	obj.style.height = "100%";
	obj.cmp = "grid";
	document.body.appendChild(obj);
	this.attachObject(obj.id);
	
	conf.container = obj.id;
	
	var that = this.vs[this.av];
	that.grid = new window[name](conf);
	
	that.gridId = obj.id;
	that.gridObj = obj;
	
		
	that.grid.setSizes = function(){
		if (this.resize) this.resize();
		else this.render();
	};
	
	var method_name="_viewRestore";
	return this.vs[this[method_name]()].grid;
};
dhtmlx.attaches.attachDataView = function(conf){
	return this.attachAbstract("dhtmlXDataView",conf);
};
dhtmlx.attaches.attachChart = function(conf){
	return this.attachAbstract("dhtmlXChart",conf);
};

dhtmlx.compat.layout = function(){};



/* DHX INITIAL FILE 'G:\dhtmlx.out\Professional\dhtmlxCore/sources//dataview.js'*/


/*
	UI:DataView
*/

/*DHX:Depend dataview.css*/
/*DHX:Depend types*/
/*DHX:Depend ../imgs/dataview*/

/*DHX:Depend compatibility_layout.js*/
/*DHX:Depend compatibility_drag.js*/

/*DHX:Depend datastore.js*/
/*DHX:Depend load.js*/ 		/*DHX:Depend virtual_render.js*/ 		/*DHX:Depend selection.js*/
/*DHX:Depend mouse.js*/ 	/*DHX:Depend key.js*/ 					/*DHX:Depend edit.js*/ 
/*DHX:Depend drag.js*/		/*DHX:Depend dataprocessor_hook.js*/ 	/*DHX:Depend autotooltip.js*/ 
/*DHX:Depend pager.js*/		/*DHX:Depend destructor.js*/			/*DHX:Depend dhtmlx.js*/
/*DHX:Depend config.js*/




//container - can be a HTML container or it's ID
dhtmlXDataView = function(container){
	//next data is only for debug purposes
	this.name = "DataView";	//name of component
	this.version = "3.0";	//version of component
	
	if (dhtmlx.assert_enabled()) this._assert();

	//enable configuration
	dhtmlx.extend(this, dhtmlx.Settings);
	this._parseContainer(container,"dhx_dataview");	//assigns parent container
	
	
	
	//behaviors
	dhtmlx.extend(this, dhtmlx.AtomDataLoader);
	dhtmlx.extend(this, dhtmlx.DataLoader);	//includes creation of DataStore
	dhtmlx.extend(this, dhtmlx.EventSystem);
	dhtmlx.extend(this, dhtmlx.RenderStack);
	dhtmlx.extend(this, dhtmlx.SelectionModel);
	dhtmlx.extend(this, dhtmlx.MouseEvents);
	dhtmlx.extend(this, dhtmlx.KeyEvents);
	dhtmlx.extend(this, dhtmlx.EditAbility);
	dhtmlx.extend(this, dhtmlx.DataMove);
	dhtmlx.extend(this, dhtmlx.DragItem);
	dhtmlx.extend(this, dhtmlx.DataProcessor);
	dhtmlx.extend(this, dhtmlx.AutoTooltip);
	dhtmlx.extend(this, dhtmlx.Destruction);
	
	
	//render self , each time when data is updated
	this.data.attachEvent("onStoreUpdated",dhtmlx.bind(function(){
		this.render.apply(this,arguments);
	},this));

	//default settings
	this._parseSettings(container,{
		drag:false,
		edit:false,
		select:"multiselect", //multiselection is enabled by default
		type:"default"
	});
	
	//in case of auto-height we use plain rendering
	if (this._settings.height!="auto"&&!this._settings.renderAll)
		dhtmlx.extend(this, dhtmlx.VirtualRenderStack);	//extends RenderStack behavior
	
	//map API of DataStore on self
	this.data.provideApi(this,true);
};
dhtmlXDataView.prototype={
	bind:function(){
		dhx.BaseBind.legacyBind.apply(this, arguments);
	},
	sync:function(){
		dhx.BaseBind.legacySync.apply(this, arguments);
	},
	/*
		Called each time when dragIn or dragOut situation occurs
		context - drag context object
		ev - native event
	*/
	dragMarker:function(context,ev){
		//get HTML element by item ID
		//can be null - when item is not rendered yet
		var el = this._locateHTML(context.target);
		
		//ficon and some other types share common bg marker
		if (this.type.drag_marker){
			if (this._drag_marker){
				//clear old drag marker position
				this._drag_marker.style.backgroundImage="";
				this._drag_marker.style.backgroundRepeat="";
			}
			
			// if item already rendered
			if (el) {
				//show drag marker
				el.style.backgroundImage="url("+(dhtmlx.image_path||"")+this.type.drag_marker+")";
				el.style.backgroundRepeat="no-repeat";
				this._drag_marker = el;
			}
		}
		
		//auto-scroll during d-n-d, only if related option is enabled
		if (el && this._settings.auto_scroll){
			//maybe it can be moved to the drag behavior
			var dy = el.offsetTop;
			var dh = el.offsetHeight;
			var py = this._obj.scrollTop;
			var ph = this._obj.offsetHeight;
			//scroll up or down is mouse already pointing on top|bottom visible item
			if (dy-dh >= 0 && dy-dh*0.75 < py)
				py = Math.max(dy-dh, 0);
			else if (dy+dh/0.75 > py+ph)
				py = py+dh;
			
			this._obj.scrollTop = py;
		}
		return true;
	},
	//attribute , which will be used for ID storing
	_id:"dhx_f_id",
	//css class to action map, for onclick event
	on_click:{
		dhx_dataview_item:function(e,id){ 
			//click on item
			if (this.stopEdit(false,id)){
				if (this._settings.select){
					if (this._settings.select=="multiselect")
						this.select(id, e.ctrlKey, e.shiftKey); 	//multiselection
					else
						this.select(id);
				}
			}
		}	
	},
	//css class to action map, for dblclick event
	on_dblclick:{
		dhx_dataview_item:function(e,id){ 
			//dblclick on item
			if (this._settings.edit)
				this.edit(id);	//edit it!
		}
	},
	//css class to action map, for mousemove event
	on_mouse_move:{
	},
	types:{
		"default":{
			css:"default",
			//normal state of item
			template:dhtmlx.Template.fromHTML("<div style='padding:10px; white-space:nowrap; overflow:hidden;'>{obj.text}</div>"),
			//template for edit state of item
			template_edit:dhtmlx.Template.fromHTML("<div style='padding:10px; white-space:nowrap; overflow:hidden;'><textarea style='width:100%; height:100%;' bind='obj.text'></textarea></div>"),
			//in case of dyn. loading - temporary spacer
			template_loading:dhtmlx.Template.fromHTML("<div style='padding:10px; white-space:nowrap; overflow:hidden;'>Loading...</div>"),
			width:210,
			height:115,
			margin:0,
			padding:10,
			border:1
		}
	},
	template_item_start:dhtmlx.Template.fromHTML("<div dhx_f_id='{-obj.id}' class='dhx_dataview_item dhx_dataview_{obj.css}_item{-obj.$selected?_selected:}' style='width:{obj.width}px; height:{obj.height}px; padding:{obj.padding}px; margin:{obj.margin}px; float:left; overflow:hidden;'>"),
	template_item_end:dhtmlx.Template.fromHTML("</div>")
};

dhtmlx.compat("layout");