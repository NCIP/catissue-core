// script.aculo.us controls.js v1.7.0, Fri Jan 19 19:16:36 CET 2007

// Copyright (c) 2005, 2006 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005, 2006 Ivan Krstic (http://blogs.law.harvard.edu/ivan)
//           (c) 2005, 2006 Jon Tirsen (http://www.tirsen.com)
// Contributors:
//  Richard Livsey
//  Rahul Bhargava
//  Rob Wills
// 
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

// Autocompleter.Base handles all the autocompletion functionality 
// that's independent of the data source for autocompletion. This
// includes drawing the autocompletion menu, observing keyboard
// and mouse events, and similar.
//
// Specific autocompleters need to provide, at the very least, 
// a getUpdatedChoices function that will be invoked every time
// the text inside the monitored textbox changes. This method 
// should get the text for which to provide autocompletion by
// invoking this.getToken(), NOT by directly accessing
// this.element.value. This is to allow incremental tokenized
// autocompletion. Specific auto-completion logic (AJAX, etc)
// belongs in getUpdatedChoices.
//
// Tokenized incremental autocompletion is enabled automatically
// when an autocompleter is instantiated with the 'tokens' option
// in the options parameter, e.g.:
// new Ajax.Autocompleter('id','upd', '/url/', { tokens: ',' });
// will incrementally autocomplete with a comma as the token.
// Additionally, ',' in the above example can be replaced with
// a token array, e.g. { tokens: [',', '\n'] } which
// enables autocompletion on multiple tokens. This is most 
// useful when one of the tokens is \n (a newline), as it 
// allows smart autocompletion after linebreaks.

if(typeof Effect == 'undefined')
  throw("controls.js requires including script.aculo.us' effects.js library");






var globalKeyPress;  
var elemStartsWithEntry;
var Autocompleter = {}
Autocompleter.Base = function() {};
Autocompleter.Base.prototype = {
  baseInitialize: function(element, update, options) {
    this.element     = $(element); 
    this.update      = $(update);  
    this.hasFocus    = false; 
    this.changed     = false; 
    this.active      = false; 
    this.index       = 0;     
    this.entryCount  = 0;

    if(this.setOptions)
      this.setOptions(options);
    else
      this.options = options || {};

    this.options.paramName    = this.options.paramName || this.element.name;
    this.options.tokens       = this.options.tokens || [];
    this.options.frequency    = this.options.frequency || 0.4;
    this.options.minChars     = this.options.minChars || 1;
    this.options.onShow       = this.options.onShow || 
      function(element, update){ 
        if(!update.style.position || update.style.position=='absolute') {
          update.style.position = 'absolute';
          Position.clone(element, update, {
            setHeight: false, 
            offsetTop: element.offsetHeight
          });
        }
        Effect.Appear(update,{duration:0.15});
      };
    this.options.onHide = this.options.onHide || 
      function(element, update){ new Effect.Fade(update,{duration:0.15}) };

    if(typeof(this.options.tokens) == 'string') 
      this.options.tokens = new Array(this.options.tokens);

    this.observer = null;
    
    this.element.setAttribute('autocomplete','off');

    Element.hide(this.update);

    Event.observe(this.element, "blur", this.onBlur.bindAsEventListener(this));
    Event.observe(this.element, "keypress", this.onKeyPress.bindAsEventListener(this));
    Event.observe(this.update, "blur", this.onBlur.bindAsEventListener(this)); 
    Event.observe(this.element, "focus", this.onFocus.bindAsEventListener(this)); 
 	Event.observe(this.update, "focus", this.onFocus.bindAsEventListener(this)); 
	Event.observe(this.update, "keypress", this.onKeyPress.bindAsEventListener(this)); 
  },

  show: function() {
    if(Element.getStyle(this.update, 'display')=='none') this.options.onShow(this.element, this.update);
    if(!this.iefix && 
      (navigator.appVersion.indexOf('MSIE')>0) &&
      (navigator.userAgent.indexOf('Opera')<0) &&
      (Element.getStyle(this.update, 'position')=='absolute')) {
      new Insertion.After(this.update, 
       '<iframe id="' + this.update.id + '_iefix" '+
       'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
       'src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
      this.iefix = $(this.update.id+'_iefix');
    }
    if(this.iefix) setTimeout(this.fixIEOverlapping.bind(this), 50);
  },
  
  fixIEOverlapping: function() {
    Position.clone(this.update, this.iefix, {setTop:(!this.update.style.height)});
    this.iefix.style.zIndex = 1;
    this.update.style.zIndex = 2;
    Element.show(this.iefix);
  },

  hide: function() {
    this.stopIndicator();
    if(Element.getStyle(this.update, 'display')!='none') this.options.onHide(this.element, this.update);
    if(this.iefix) Element.hide(this.iefix);
  },

  startIndicator: function() {
    if(this.options.indicator) Element.show(this.options.indicator);
  },

  stopIndicator: function() {
    if(this.options.indicator) Element.hide(this.options.indicator);
  },

  onKeyPress: function(event) {
  
    globalKeyPress = event.keyCode;	
    if(this.active)
      switch(event.keyCode) {
	      
       case Event.KEY_TAB:
       case Event.KEY_RETURN:
         this.selectEntry();
         Event.stop(event);
       case Event.KEY_ESC:
         this.hide();
         this.active = false;
         Event.stop(event);
         return;
       case Event.KEY_LEFT:
       case Event.KEY_RIGHT:
         return;
       case Event.KEY_UP:
         this.markPrevious();
         this.render();
         if(navigator.appVersion.indexOf('AppleWebKit')>0) Event.stop(event);
         return;
       case Event.KEY_DOWN:
         this.markNext();
         this.render();
         if(navigator.appVersion.indexOf('AppleWebKit')>0) Event.stop(event);
         return;
      }
     else 
       if(event.keyCode==Event.KEY_TAB || event.keyCode==Event.KEY_RETURN || 
         (navigator.appVersion.indexOf('AppleWebKit') > 0 && event.keyCode == 0)) return;

    this.changed = true;
    this.hasFocus = true;

    if(this.observer) clearTimeout(this.observer);
      this.observer = 
        setTimeout(this.onObserverEvent.bind(this), this.options.frequency*1000);
  },

  activate: function() {
    clearTimeout(this.hideTimeout); 
    this.changed = false;
    this.hasFocus = true;
    this.getUpdatedChoices();
  },

  onHover: function(event) {
    var element = Event.findElement(event, 'LI');
    if(this.index != element.autocompleteIndex) 
    {
        this.index = element.autocompleteIndex;
        this.render();
    }
    Event.stop(event);
  },
  
  onClick: function(event) {
    
    var element = Event.findElement(event, 'LI');
    this.index = element.autocompleteIndex;
    this.selectEntry();
    this.hide();
  },
  
  onBlur: function(event) {
    // needed to make click events working
    
    this.hideTimeout = setTimeout(this.hide.bind(this), 250); 
    this.hasFocus = false;
    this.active = false;     
  }, 
  
  onFocus: function(event) { 
 	        if (this.hideTimeout) clearTimeout(this.hideTimeout); 
 	        this.changed = false; 
 		    this.hasFocus = true; 
 		    //this.getUpdatedChoices(); 
 		  }, 
  
  render: function() {
    if(this.entryCount > 0) {
      for (var i = 0; i < this.entryCount; i++)
        this.index==i ? 
          Element.addClassName(this.getEntry(i),"selected") : 
          Element.removeClassName(this.getEntry(i),"selected");
		  
		    // added this
      var item=this.getEntry(this.index);
       
      //get default size , done for scrolling -- Santosh
      var size = this.entryCount;
      var scrollSize = 15;
	  if(size>scrollSize)
	  {
	     item.parentNode.style.height = "21em";
		 item.parentNode.style.overflow = "auto";
	  }
      		  
		  if (this.index == i) {
          var element = this.getEntry(i);
          element.scrollIntoView(false);
        } 
        
      if(this.hasFocus) { 
        this.show();
        this.active = true;
      }
    } else {
      this.active = false;
      this.hide();
    }
  },
  
  markPrevious: function() {
    if(this.index > 0) this.index--
      else this.index = this.entryCount-1;
    this.getEntry(this.index).scrollIntoView(true);
  },
  
  markNext: function() {
    if(this.index < this.entryCount-1) this.index++
      else this.index = 0;
    this.getEntry(this.index).scrollIntoView(false);
  },
  
  getEntry: function(index) {
    return this.update.firstChild.childNodes[index];
  },
  
  getCurrentEntry: function() {
    return this.getEntry(this.index);
  },
  
  selectEntry: function() {
    this.active = false;
    this.updateElement(this.getCurrentEntry());
  },

  updateElement: function(selectedElement) {
    if (this.options.updateElement) {
      this.options.updateElement(selectedElement);
      return;
    }
    var value = '';
    if (this.options.select) {
      var nodes = document.getElementsByClassName(this.options.select, selectedElement) || [];
      if(nodes.length>0) value = Element.collectTextNodes(nodes[0], this.options.select);
    } else
      value = Element.collectTextNodesIgnoreClass(selectedElement, 'informal');
    
    var lastTokenPos = this.findLastToken();
    if (lastTokenPos != -1) {
      var newValue = this.element.value.substr(0, lastTokenPos + 1);
      var whitespace = this.element.value.substr(lastTokenPos + 1).match(/^\s+/);
      if (whitespace)
        newValue += whitespace[0];
      this.element.value = newValue + value;
    } else {
      this.element.value = value;
    }
    this.element.focus();
    
    if (this.options.afterUpdateElement)
      this.options.afterUpdateElement(this.element, selectedElement);
  },

  updateChoices: function(choices) {
    if(!this.changed && this.hasFocus) {
      this.update.innerHTML = choices;
      Element.cleanWhitespace(this.update);
      Element.cleanWhitespace(this.update.down());

      if(this.update.firstChild && this.update.down().childNodes) {
        this.entryCount = 
          this.update.down().childNodes.length;
        for (var i = 0; i < this.entryCount; i++) {
          var entry = this.getEntry(i);
          entry.autocompleteIndex = i;
          this.addObservers(entry);
        }
      } else { 
        this.entryCount = 0;
      }

      this.stopIndicator();
      this.index = 0;  
    
	//  globalKeyPress = 8 indicates that it is a backspace key and do not autocomplete -- Santosh
	// To autocomplete element should start with entry 	 
	 	 
      if(this.entryCount==1 && this.options.autoSelect && globalKeyPress != 8 && elemStartsWithEntry) {
        this.selectEntry();
        this.hide();
      } else {
        this.render();
      }
    }
  },

  addObservers: function(element) {
    Event.observe(element, "mouseover", this.onHover.bindAsEventListener(this));
    Event.observe(element, "click", this.onClick.bindAsEventListener(this));
  },

  onObserverEvent: function() {
    this.changed = false;   
    if(this.getToken().length>=this.options.minChars) {
      this.startIndicator();
      this.getUpdatedChoices();
    } else {
      this.active = false;
      this.hide();
    }
  },

  getToken: function() {
    var tokenPos = this.findLastToken();
    if (tokenPos != -1)
      var ret = this.element.value.substr(tokenPos + 1).replace(/^\s+/,'').replace(/\s+$/,'');
    else
      var ret = this.element.value;

    return /\n/.test(ret) ? '' : ret;
  },

  findLastToken: function() {
    var lastTokenPos = -1;

    for (var i=0; i<this.options.tokens.length; i++) {
      var thisTokenPos = this.element.value.lastIndexOf(this.options.tokens[i]);
      if (thisTokenPos > lastTokenPos)
        lastTokenPos = thisTokenPos;
    }
    return lastTokenPos;
  }
}

Ajax.Autocompleter = Class.create();
Object.extend(Object.extend(Ajax.Autocompleter.prototype, Autocompleter.Base.prototype), {
  initialize: function(element, update, url, options) {
    this.baseInitialize(element, update, options);
    this.options.asynchronous  = true;
    this.options.onComplete    = this.onComplete.bind(this);
    this.options.defaultParams = this.options.parameters || null;
    this.url                   = url;
  },

  getUpdatedChoices: function() {
  alert("Get updated choic");
    entry = encodeURIComponent(this.options.paramName) + '=' + 
      encodeURIComponent(this.getToken());

    this.options.parameters = this.options.callback ?
      this.options.callback(this.element, entry) : entry;

    if(this.options.defaultParams) 
      this.options.parameters += '&' + this.options.defaultParams;

    new Ajax.Request(this.url, this.options);
  },

  onComplete: function(request) {
    this.updateChoices(request.responseText);
  }

});

// The local array autocompleter. Used when you'd prefer to
// inject an array of autocompletion options into the page, rather
// than sending out Ajax queries, which can be quite slow sometimes.
//
// The constructor takes four parameters. The first two are, as usual,
// the id of the monitored textbox, and id of the autocompletion menu.
// The third is the array you want to autocomplete from, and the fourth
// is the options block.
//
// Extra local autocompletion options:
// - choices - How many autocompletion choices to offer
//
// - partialSearch - If false, the autocompleter will match entered
//                    text only at the beginning of strings in the 
//                    autocomplete array. Defaults to true, which will
//                    match text at the beginning of any *word* in the
//                    strings in the autocomplete array. If you want to
//                    search anywhere in the string, additionally set
//                    the option fullSearch to true (default: off).
//
// - fullSsearch - Search anywhere in autocomplete array strings.
//
// - partialChars - How many characters to enter before triggering
//                   a partial match (unlike minChars, which defines
//                   how many characters are required to do any match
//                   at all). Defaults to 2.
//
// - ignoreCase - Whether to ignore case when autocompleting.
//                 Defaults to true.
//
// It's possible to pass in a custom function as the 'selector' 
// option, if you prefer to write your own autocompletion logic.
// In that case, the other options above will not apply unless
// you support them.
var setChoices = false;

Autocompleter.Local = Class.create();
Autocompleter.Local.prototype = Object.extend(new Autocompleter.Base(), {
  initialize: function(element, update, array, options) {
    this.baseInitialize(element, update, options);
    this.options.array = array;
  },

  getUpdatedChoices: function() {
    this.updateChoices(this.options.selector(this));
  },

  
  setOptions: function(options) {
  
    this.options = Object.extend({
      choices: 10,
      partialSearch: true,
      partialChars: 2,
      ignoreCase: true,
      fullSearch: false,
      selector: function(instance) {
        var ret       = []; // Beginning matches
        var partial   = []; // Inside matches
        var entry     = instance.getToken();
        var count     = 0;
        var intialToken = instance.getToken(); 
		elemStartsWithEntry = false; 
		 
        for (var i = 0; i < instance.options.array.length &&  
          ret.length < instance.options.choices ; i++) { 

          var elem = instance.options.array[i];
		  entry     = instance.getToken();
        /*  var foundPos = instance.options.ignoreCase ? 
            elem.toLowerCase().indexOf(entry.toLowerCase()) : 
            elem.indexOf(entry);*/ // Commented and added code as per requirement -- Santosh
			
				 var elem1 = elem.toLowerCase();
				 var initialEntry = entry.toLowerCase();
				 initialEntry = trim(initialEntry);
				  var foundPos = -1;
			if(initialEntry=="*"||initialEntry=="?")
            {
                foundPos=0;
				instance.options.choices = instance.options.array.length;
				entry="";
				setChoices = true;
            }			
			else
            {			
			     if(setChoices)
				 {
			         instance.options.choices = 10; 
				 }
				 var spacePresent = false;
				 if(initialEntry.indexOf(" ")!=-1)
				 {
				      spacePresent = true;
				 }
				 
				 foundPos = elem.toLowerCase().indexOf(entry.toLowerCase());
				 
				 if(foundPos==-1 && spacePresent)
				 {
				   var entry1 = "(" + initialEntry.replace(/ /g,")+[^#]*( ") +")"+"+";
				   try
				   {
				   var regexp = new RegExp(entry1);
				   var foundPos = elem1.match(regexp,"gi");
				   
				   if(foundPos!=null && foundPos.length > 0)
				   {
				     entry = foundPos[0];
					 foundPos = elem.toLowerCase().indexOf(entry.toLowerCase());
					 entry = elem.substr(foundPos,entry.length);
				   }
				    
					}
					catch(e)
					{
					}
				 } 
				 
				 if(foundPos == null)
				 {
				     foundPos = -1;
				 }	
							
            }
			
		if(foundPos != -1)	
		{
		
		 if(elem.toLowerCase().indexOf(intialToken.toLowerCase()) == 0)
         {
		   elemStartsWithEntry = true;
         }		 
		}	
          while (foundPos != -1) {
            if (foundPos == 0) { 
              ret.push("<li><strong>" + elem.substr(0, entry.length) + "</strong>" + 
                elem.substr(entry.length) + "</li>");
              break;
            } else if (entry.length >= instance.options.partialChars && 
              instance.options.partialSearch && foundPos != -1) {
              if (instance.options.fullSearch || /\s/.test(elem.substr(foundPos-1,1))) {
                partial.push("<li>" + elem.substr(0, foundPos) + "<strong>" +
                  elem.substr(foundPos, entry.length) + "</strong>" + elem.substr(
                  foundPos + entry.length) + "</li>");
                break;
              }
            }

            foundPos = instance.options.ignoreCase ? 
              elem.toLowerCase().indexOf(entry.toLowerCase(), foundPos + 1) : 
              elem.indexOf(entry, foundPos + 1);

          }
        }
        if (partial.length)
          ret = ret.concat(partial.slice(0, instance.options.choices - ret.length))
        return "<ul>" + ret.join('') + "</ul>";
      }
    }, options || {});
  }
});

/*
	The combobox control.  This is very simililar to the local array
	autocompleter but with the addition that if a specified control
	(like an image) is clicked it will show a default list.
	Otherwise it behaves exactly like the local array autocompleter.

	The constructor is the same as the local array autocompleter.
	
	To specify a different array for the 'show default' control,
	add the option 'defaultArray', and pass to it a valid javascript array.
	This control won't work so well if the two arrays you pass in are 
	wildly different.  Generally you need to have the defaultArray be a 
	subset of the main array.
	
	I've commented in-line to help explain some of the choices.
	
	One last thing:  a 'super' would be *really* nice for stuff like this.
	There is a lot of dup code because I can't call super; or super.onBlur().
	I guess I could name the methods something different, and we can do that
	to keep the code DRY, but I left the dupe lines in to make it clear as to
	what is going on.
	
	An example (in html) is:
	<script type="text/javascript">
		//*horrible* baby names (first names!  try and guess which ones are for 
		// boys and girls.  ugh)
		var all_options = ['Ambrosia','Attica','Autumn Night','Avellana','Bow Hunter',
		'Brielle','Brooklynn','Cash','Cinsere','Crisiant','Cyndee','Delphine','Donte',
		'Drudwen','Dusk','Electra','Emmaleigh','Enfys','Enobi','Eurwen','Faerin',
		'Faleiry','Francessca','Franka','Fritha','Gage','Gaiety','Gennavieve','Gibson'];
		// the worst.  Ok, couldn't cull it down enough, so any 5
		var default_options = ['Avellana','Enobi','Eurwen','Faleiry','Gage'];

	</script> 
	TEST:<input type="text" id="name-field"/>
	<image id='name-arrow' src="/images/de/combo_box_arrow.png"/>
	<div class="auto_complete" id="lookup_auto_complete"></div> 
	<%= javascript_tag("new Autocompleter.Combobox('name-field',
	'lookup_auto_complete', 'name-arrow',
 	all_options,{partialChars:1, ignoreCase:true, fullSearch:true, frequency:0.1, 
							 choices:6, defaultArray:default_options, tokens: ',' });") %>
*/

Autocompleter.Combobox = Class.create();
Object.extend(Object.extend(Autocompleter.Combobox.prototype, Autocompleter.Local.prototype), {
    	initialize: function(element, update, selectDefault, array, options) {
		this.baseInitialize(element, update, options);
		this.selectDefault = selectDefault;
		//this keeps track of whether or not the click is currently over the 'selectDefault'.  See
		// below for more detail.
		this.overSelectDefault = false;
		this.clickedSelectDefault = 0;
		
		this.options.array           = this.options.array || array;
		this.options.defaultArray    = this.options.defaultArray || this.options.array;
		this.options.defaultChoices  = this.options.defaultChoices || this.options.defaultArray.length;
		
		Event.observe(this.selectDefault, 'click', this.click.bindAsEventListener(this));		

		// these events are all in place to coordinate between te onBlur event for the input
		// field and the click event for the selectDefault.  See below for more detail.
    Event.observe(this.selectDefault, "mouseover", this.onMouseover.bindAsEventListener(this));
    Event.observe(this.selectDefault, "mouseout", this.onMouseout.bindAsEventListener(this));
    Event.observe(this.element, "keypress", this.resetClick.bindAsEventListener(this));
	},

/*
The one issue I had (and hence the extra Event.observers and this.overSelectDefault and this.clickedSelectDefault)
was that if you had the input field highlighted and clicked _anywhere_ outside the input field, the
'onBlur' method would be called.  This makes sense except in the case where you hit the 'selectDefault'
element, in which case you don't want to hide the dropdown but show it!

I first tried to see if the events were called in any particular order (deterministic).  I didn't
think this would work but I thought it was worth a shot.  The answer depends on the browser, but
most are non-deterministic.  Because they aren't called in a pre-defined order, that also meant
that you can't set a state flag in one (like 'I just clicked the selectDefault element!'), and expect
to check it in the other event ('hide me IF the selectDefault was not clicked!').

The other hope was that the event could give me information about either which element was
*active* when the event was triggered (this is different than asking which element is tied to the
event, which is given to you by prototype's Event.element), or the X,Y coordinates of the event to
see if I could compare that to the location of the selectDefault element (prototype's Position.within).

Neither of those worked.  Firefox has an attribute for events that tell you what element you were on
when the event was triggered.  IE supposedly has something similar, but opera and safari do not.  So
that won't work.  And, the onBlur event doesn't set the X and Y coordinates (or any of the numerous
position attributes) in either firefox or safari.  doah!

So the end solution was to set a flag on a mouseover and mouseout on the selectDefault element. 
The idea is that this will occur before a click and there will be time to set the flag before
the 'onClick' and 'onBlur' events are triggered simultaneously.  This works really well, but leaves one
last catch: sometimes when you click the selectDefault you *want* the default menu to disappear.  Hence
the reason to keep track of the state of the selectDefault element (previously clicked or not clicked).
*/

	onMouseover: function(event){
		this.overSelectDefault = true;
	},

	onMouseout: function(event){
		this.overSelectDefault = false;
	},
	
	resetClick :function(event) {
		this.clickedSelectDefault = 0;
	},
	
	onBlur: function(event, force) {
		if (this.overSelectDefault) // Santosh - removed force
		{
			//no need to blur
			return;
		}
		// this is where you can call super.onBlur();
	  
    // needed to make click events working  Because of scrollbar, dont hide the div when "blur" happens when the user on "div" clicks 
	/*alert(event.x - parseInt(this.update.style.left));
	alert(parseInt(this.update.style.left));
	alert(parseInt(this.update.style.width));
	alert(event.y - parseInt(this.update.style.top));
	alert(parseInt(this.update.style.height));*/ 
	
   // if((event.x == undefined) |/*event.y - parseInt(this.update.style.top)<0|event.y - parseInt(this.update.style.top)>0  |*/event.x - parseInt(this.update.style.left)<0 |(event.x - parseInt(this.update.style.left)) > parseInt(this.update.style.width) | (event.y - parseInt(this.update.style.top)) > parseInt(this.update.style.height)) 
      
	  this.hideTimeout = setTimeout(this.hide.bind(this), 250); 
       // setTimeout(this.hide.bind(this), 250); 
		this.hasFocus = false; 
		this.active = false;
	    this.clickedSelectDefault = false;

	},

	click: function(event) {
	    
		this.clickedSelectDefault = Math.abs(this.clickedSelectDefault - 1);
		this.element.focus();
		this.changed = false;
		this.hasFocus = true;
		this.getAllChoices();
		if ( Element.getStyle(this.update, 'display') != 'none' && this.clickedSelectDefault != 1) {
			this.onBlur(event, true);
		}
	},

  // this is *almost* exactly the same as Autocompleter.local.selector method
  // there can definitely be some refactoring done in the control.js file
  // to keep it DRY
  // What I changed was that if the user hits the defaultSelect element, I 
  // *always* want to show all the elements in the defaultSelect 'dropdown'.
  // that way there is some consistency about what is shown when the user
  // hits that element.  
  // but to recognize that they might have already typed something in the 
  // input field, this function (like the one in Autocomplete.local) highlights
  // the matching chars, if there are any.
	getAllChoices: function(e) {
	
		  var ret       = []; // Beginning matches
      var partial   = []; // Inside matches
      var entry     = this.getToken();
      var count     = 0;
	  
	  if(this.element.disabled==true || this.element.readOnly==true )
	  return;
      for (var i = 0; i < this.options.defaultArray.length &&  
        ret.length < this.options.defaultChoices; i++) { 
        var elem = this.options.defaultArray[i];
        var foundPos = this.options.ignoreCase ? 
          elem.toLowerCase().indexOf(entry.toLowerCase()) : 
          elem.indexOf(entry);
				if (entry == "" || foundPos == -1)
				{
					ret.push("<li>" + elem + "</li>");
					continue;					
				}
        while (foundPos != -1) {
          if (foundPos == 0 ) { 
            ret.push("<li><strong>" + elem.substr(0, entry.length) + "</strong>" + 
              elem.substr(entry.length) + "</li>");
            break;
          } else if (entry.length >= this.options.partialChars && 
            this.options.partialSearch && foundPos != -1) {
            if (this.options.fullSearch || /\s/.test(elem.substr(foundPos-1,1))) {
              partial.push("<li>" + elem.substr(0, foundPos) + "<strong>" +
                elem.substr(foundPos, entry.length) + "</strong>" + elem.substr(
                foundPos + entry.length) + "</li>");
              break;
            }
          }

          foundPos = this.options.ignoreCase ? 
            elem.toLowerCase().indexOf(entry.toLowerCase(), foundPos + 1) : 
            elem.indexOf(entry, foundPos + 1);

        }
      }
      if (partial.length)
      ret = ret.concat(partial.slice(0, this.options.defaultChoices - ret.length))
		this.updateChoices("<ul>" + ret.join('') + "</ul>");
	}
});

// AJAX in-place editor
//
// see documentation on http://wiki.script.aculo.us/scriptaculous/show/Ajax.InPlaceEditor

// Use this if you notice weird scrolling problems on some browsers,
// the DOM might be a bit confused when this gets called so do this
// waits 1 ms (with setTimeout) until it does the activation
Field.scrollFreeActivate = function(field) {
  setTimeout(function() {
    Field.activate(field);
  }, 1);
}

Ajax.InPlaceEditor = Class.create();
Ajax.InPlaceEditor.defaultHighlightColor = "#FFFF99";
Ajax.InPlaceEditor.prototype = {
  initialize: function(element, url, options) {
    this.url = url;
    this.element = $(element);

    this.options = Object.extend({
      paramName: "value",
      okButton: true,
      okText: "ok",
      cancelLink: true,
      cancelText: "cancel",
      savingText: "Saving...",
      clickToEditText: "Click to edit",
      okText: "ok",
      rows: 1,
      onComplete: function(transport, element) {
        new Effect.Highlight(element, {startcolor: this.options.highlightcolor});
      },
      onFailure: function(transport) {
        alert("Error communicating with the server: " + transport.responseText.stripTags());
      },
      callback: function(form) {
        return Form.serialize(form);
      },
      handleLineBreaks: true,
      loadingText: 'Loading...',
      savingClassName: 'inplaceeditor-saving',
      loadingClassName: 'inplaceeditor-loading',
      formClassName: 'inplaceeditor-form',
      highlightcolor: Ajax.InPlaceEditor.defaultHighlightColor,
      highlightendcolor: "#FFFFFF",
      externalControl: null,
      submitOnBlur: false,
      ajaxOptions: {},
      evalScripts: false
    }, options || {});

    if(!this.options.formId && this.element.id) {
      this.options.formId = this.element.id + "-inplaceeditor";
      if ($(this.options.formId)) {
        // there's already a form with that name, don't specify an id
        this.options.formId = null;
      }
    }
    
    if (this.options.externalControl) {
      this.options.externalControl = $(this.options.externalControl);
    }
    
    this.originalBackground = Element.getStyle(this.element, 'background-color');
    if (!this.originalBackground) {
      this.originalBackground = "transparent";
    }
    
    this.element.title = this.options.clickToEditText;
    
    this.onclickListener = this.enterEditMode.bindAsEventListener(this);
    this.mouseoverListener = this.enterHover.bindAsEventListener(this);
    this.mouseoutListener = this.leaveHover.bindAsEventListener(this);
    Event.observe(this.element, 'click', this.onclickListener);
    Event.observe(this.element, 'mouseover', this.mouseoverListener);
    Event.observe(this.element, 'mouseout', this.mouseoutListener);
    if (this.options.externalControl) {
      Event.observe(this.options.externalControl, 'click', this.onclickListener);
      Event.observe(this.options.externalControl, 'mouseover', this.mouseoverListener);
      Event.observe(this.options.externalControl, 'mouseout', this.mouseoutListener);
    }
  },
  enterEditMode: function(evt) {
    if (this.saving) return;
    if (this.editing) return;
    this.editing = true;
    this.onEnterEditMode();
    if (this.options.externalControl) {
      Element.hide(this.options.externalControl);
    }
    Element.hide(this.element);
    this.createForm();
    this.element.parentNode.insertBefore(this.form, this.element);
    if (!this.options.loadTextURL) Field.scrollFreeActivate(this.editField);
    // stop the event to avoid a page refresh in Safari
    if (evt) {
      Event.stop(evt);
    }
    return false;
  },
  createForm: function() {
    this.form = document.createElement("form");
    this.form.id = this.options.formId;
    Element.addClassName(this.form, this.options.formClassName)
    this.form.onsubmit = this.onSubmit.bind(this);

    this.createEditField();

    if (this.options.textarea) {
      var br = document.createElement("br");
      this.form.appendChild(br);
    }

    if (this.options.okButton) {
      okButton = document.createElement("input");
      okButton.type = "submit";
      okButton.value = this.options.okText;
      okButton.className = 'editor_ok_button';
      this.form.appendChild(okButton);
    }

    if (this.options.cancelLink) {
      cancelLink = document.createElement("a");
      cancelLink.href = "#";
      cancelLink.appendChild(document.createTextNode(this.options.cancelText));
      cancelLink.onclick = this.onclickCancel.bind(this);
      cancelLink.className = 'editor_cancel';      
      this.form.appendChild(cancelLink);
    }
  },
  hasHTMLLineBreaks: function(string) {
    if (!this.options.handleLineBreaks) return false;
    return string.match(/<br/i) || string.match(/<p>/i);
  },
  convertHTMLLineBreaks: function(string) {
    return string.replace(/<br>/gi, "\n").replace(/<br\/>/gi, "\n").replace(/<\/p>/gi, "\n").replace(/<p>/gi, "");
  },
  createEditField: function() {
    var text;
    if(this.options.loadTextURL) {
      text = this.options.loadingText;
    } else {
      text = this.getText();
    }

    var obj = this;
    
    if (this.options.rows == 1 && !this.hasHTMLLineBreaks(text)) {
      this.options.textarea = false;
      var textField = document.createElement("input");
      textField.obj = this;
      textField.type = "text";
      textField.name = this.options.paramName;
      textField.value = text;
      textField.style.backgroundColor = this.options.highlightcolor;
      textField.className = 'editor_field';
      var size = this.options.size || this.options.cols || 0;
      if (size != 0) textField.size = size;
      if (this.options.submitOnBlur)
        textField.onblur = this.onSubmit.bind(this);
      this.editField = textField;
    } else {
      this.options.textarea = true;
      var textArea = document.createElement("textarea");
      textArea.obj = this;
      textArea.name = this.options.paramName;
      textArea.value = this.convertHTMLLineBreaks(text);
      textArea.rows = this.options.rows;
      textArea.cols = this.options.cols || 40;
      textArea.className = 'editor_field';      
      if (this.options.submitOnBlur)
        textArea.onblur = this.onSubmit.bind(this);
      this.editField = textArea;
    }
    
    if(this.options.loadTextURL) {
      this.loadExternalText();
    }
    this.form.appendChild(this.editField);
  },
  getText: function() {
    return this.element.innerHTML;
  },
  loadExternalText: function() {
    Element.addClassName(this.form, this.options.loadingClassName);
    this.editField.disabled = true;
    new Ajax.Request(
      this.options.loadTextURL,
      Object.extend({
        asynchronous: true,
        onComplete: this.onLoadedExternalText.bind(this)
      }, this.options.ajaxOptions)
    );
  },
  onLoadedExternalText: function(transport) {
    Element.removeClassName(this.form, this.options.loadingClassName);
    this.editField.disabled = false;
    this.editField.value = transport.responseText.stripTags();
    Field.scrollFreeActivate(this.editField);
  },
  onclickCancel: function() {
    this.onComplete();
    this.leaveEditMode();
    return false;
  },
  onFailure: function(transport) {
    this.options.onFailure(transport);
    if (this.oldInnerHTML) {
      this.element.innerHTML = this.oldInnerHTML;
      this.oldInnerHTML = null;
    }
    return false;
  },
  onSubmit: function() {
    // onLoading resets these so we need to save them away for the Ajax call
    var form = this.form;
    var value = this.editField.value;
    
    // do this first, sometimes the ajax call returns before we get a chance to switch on Saving...
    // which means this will actually switch on Saving... *after* we've left edit mode causing Saving...
    // to be displayed indefinitely
    this.onLoading();
    
    if (this.options.evalScripts) {
      new Ajax.Request(
        this.url, Object.extend({
          parameters: this.options.callback(form, value),
          onComplete: this.onComplete.bind(this),
          onFailure: this.onFailure.bind(this),
          asynchronous:true, 
          evalScripts:true
        }, this.options.ajaxOptions));
    } else  {
      new Ajax.Updater(
        { success: this.element,
          // don't update on failure (this could be an option)
          failure: null }, 
        this.url, Object.extend({
          parameters: this.options.callback(form, value),
          onComplete: this.onComplete.bind(this),
          onFailure: this.onFailure.bind(this)
        }, this.options.ajaxOptions));
    }
    // stop the event to avoid a page refresh in Safari
    if (arguments.length > 1) {
      Event.stop(arguments[0]);
    }
    return false;
  },
  onLoading: function() {
    this.saving = true;
    this.removeForm();
    this.leaveHover();
    this.showSaving();
  },
  showSaving: function() {
    this.oldInnerHTML = this.element.innerHTML;
    this.element.innerHTML = this.options.savingText;
    Element.addClassName(this.element, this.options.savingClassName);
    this.element.style.backgroundColor = this.originalBackground;
    Element.show(this.element);
  },
  removeForm: function() {
    if(this.form) {
      if (this.form.parentNode) Element.remove(this.form);
      this.form = null;
    }
  },
  enterHover: function() {
    if (this.saving) return;
    this.element.style.backgroundColor = this.options.highlightcolor;
    if (this.effect) {
      this.effect.cancel();
    }
    Element.addClassName(this.element, this.options.hoverClassName)
  },
  leaveHover: function() {
    if (this.options.backgroundColor) {
      this.element.style.backgroundColor = this.oldBackground;
    }
    Element.removeClassName(this.element, this.options.hoverClassName)
    if (this.saving) return;
    this.effect = new Effect.Highlight(this.element, {
      startcolor: this.options.highlightcolor,
      endcolor: this.options.highlightendcolor,
      restorecolor: this.originalBackground
    });
  },
  leaveEditMode: function() {
    Element.removeClassName(this.element, this.options.savingClassName);
    this.removeForm();
    this.leaveHover();
    this.element.style.backgroundColor = this.originalBackground;
    Element.show(this.element);
    if (this.options.externalControl) {
      Element.show(this.options.externalControl);
    }
    this.editing = false;
    this.saving = false;
    this.oldInnerHTML = null;
    this.onLeaveEditMode();
  },
  onComplete: function(transport) {
    this.leaveEditMode();
    this.options.onComplete.bind(this)(transport, this.element);
  },
  onEnterEditMode: function() {},
  onLeaveEditMode: function() {},
  dispose: function() {
    if (this.oldInnerHTML) {
      this.element.innerHTML = this.oldInnerHTML;
    }
    this.leaveEditMode();
    Event.stopObserving(this.element, 'click', this.onclickListener);
    Event.stopObserving(this.element, 'mouseover', this.mouseoverListener);
    Event.stopObserving(this.element, 'mouseout', this.mouseoutListener);
    if (this.options.externalControl) {
      Event.stopObserving(this.options.externalControl, 'click', this.onclickListener);
      Event.stopObserving(this.options.externalControl, 'mouseover', this.mouseoverListener);
      Event.stopObserving(this.options.externalControl, 'mouseout', this.mouseoutListener);
    }
  }
};

Ajax.InPlaceCollectionEditor = Class.create();
Object.extend(Ajax.InPlaceCollectionEditor.prototype, Ajax.InPlaceEditor.prototype);
Object.extend(Ajax.InPlaceCollectionEditor.prototype, {
  createEditField: function() {
    if (!this.cached_selectTag) {
      var selectTag = document.createElement("select");
      var collection = this.options.collection || [];
      var optionTag;
      collection.each(function(e,i) {
        optionTag = document.createElement("option");
        optionTag.value = (e instanceof Array) ? e[0] : e;
        if((typeof this.options.value == 'undefined') && 
          ((e instanceof Array) ? this.element.innerHTML == e[1] : e == optionTag.value)) optionTag.selected = true;
        if(this.options.value==optionTag.value) optionTag.selected = true;
        optionTag.appendChild(document.createTextNode((e instanceof Array) ? e[1] : e));
        selectTag.appendChild(optionTag);
      }.bind(this));
      this.cached_selectTag = selectTag;
    }

    this.editField = this.cached_selectTag;
    if(this.options.loadTextURL) this.loadExternalText();
    this.form.appendChild(this.editField);
    this.options.callback = function(form, value) {
      return "value=" + encodeURIComponent(value);
    }
  }
});

// Delayed observer, like Form.Element.Observer, 
// but waits for delay after last key input
// Ideal for live-search fields

Form.Element.DelayedObserver = Class.create();
Form.Element.DelayedObserver.prototype = {
  initialize: function(element, delay, callback) {
    this.delay     = delay || 0.5;
    this.element   = $(element);
    this.callback  = callback;
    this.timer     = null;
    this.lastValue = $F(this.element); 
    Event.observe(this.element,'keyup',this.delayedListener.bindAsEventListener(this));
  },
  delayedListener: function(event) {
    if(this.lastValue == $F(this.element)) return;
    if(this.timer) clearTimeout(this.timer);
    this.timer = setTimeout(this.onTimerEvent.bind(this), this.delay * 1000);
    this.lastValue = $F(this.element);
  },
  onTimerEvent: function() {
    this.timer = null;
    this.callback(this.element, $F(this.element));
  }
};

function trim(inputString) {
   // Removes leading and trailing spaces from the passed string. Also removes
   // consecutive spaces and replaces it with one space. If something besides
   // a string is passed in (null, custom object, etc.) then return the input.
   if (typeof inputString != "string") { return inputString; }
   var retValue = inputString;
   var ch = retValue.substring(0, 1);
   while (ch == " ") { // Check for spaces at the beginning of the string
      retValue = retValue.substring(1, retValue.length);
      ch = retValue.substring(0, 1);
   }
   ch = retValue.substring(retValue.length-1, retValue.length);
   while (ch == " ") { // Check for spaces at the end of the string
      retValue = retValue.substring(0, retValue.length-1);
      ch = retValue.substring(retValue.length-1, retValue.length);
   }
   while (retValue.indexOf("  ") != -1) { // Note that there are two spaces in the string - look for multiple spaces within the string
      retValue = retValue.substring(0, retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1, retValue.length); // Again, there are two spaces in each of the strings
   }
   return retValue; // Return the trimmed string back to the user
} // Ends the "trim" function

var ComboBox = Class.create();

ComboBox.Autocompleter = Autocompleter.Local;

ComboBox.Autocompleter.prototype.onBlur = function(event) {
	if (Element.getStyle(this.update, 'display') == 'none') { return; }
	setTimeout(this.hide.bind(this), 250);
	this.hasFocus = false;
	this.active = false;
}


ComboBox.prototype = {
	initialize: function(textElement, resultsElement, array, options) {
		this.textElement = $(textElement);
        
        // the first text box inside the container
        this.textBox = $A( this.textElement.getElementsByTagName('INPUT') ).findAll( function(input) {
            return (input.getAttribute('type') == 'text');
        })[0];
	
        this.results = $(resultsElement);
        this.textBox.paddingRight = '20px';
        this.textElement.style.width = (this.textBox.offsetWidth) + 'px';
        this.textElement.style.position = 'relative';

		// we dynamically insert a SPAN that will serve as the drop-down 'arrow'
        this.arrow = Element.extend(Builder.node('span'));
        Object.extend(this.arrow.style, {
            cursor: 'default',
            backgroundColor: '#ddd',
            color: '#000',
            width: '20px',
            position: 'absolute',
            top: '0',
			right: '0px',
			textAlign: 'center',
			fontSize: 'xx-small',
			height: (this.textElement.offsetHeight - 2) + 'px'
        });

		if (document.all) {
			this.arrow.setStyle({ padding: '2px 0 0 3px', width: '18px', height: '17px'});

		}
        this.arrow.innerHTML = '&darr;';
        this.textElement.appendChild(this.arrow);
	    this.array = array;

		this.results.style.display 	= 'none';
		
		this.events = {
			showChoices: 	    this.showChoices.bindAsEventListener(this),
			hideChoices: 	    this.hideChoices.bindAsEventListener(this),
			click:				this.click.bindAsEventListener(this),
			keyDown:			this.keyDown.bindAsEventListener(this)
		}
		
		this.autocompleter = new ComboBox.Autocompleter(this.textBox, this.results, this.array, options);
				
		Event.observe(this.arrow, 'click', this.events.click);
		Event.observe(this.textBox, 'keydown', this.events.keyDown);
	},
	
	getAllChoices: function(e) {
		var choices = this.array.collect( function(choice) { return '<li>' + choice + '</li>'; } );
		var html = '<ul>' + choices.join('') + '</ul>';
		this.autocompleter.updateChoices(html);
	},
	
	keyDown: function(e) {
		if (e.keyCode == Event.KEY_DOWN && this.choicesVisible() ) {
			this.showChoices();
		}
	},
	
	// returns boolean indicating whether the choices are displayed
	choicesVisible: function() { return (Element.getStyle(this.autocompleter.update, 'display') == 'none'); },
	
	click: function() {
		if (this.choicesVisible() ) {
			this.showChoices();
		} else {
			this.hideChoices();
		}
	},
		
	showChoices: function() {
		this.textBox.focus();
    this.autocompleter.changed = false;
    this.autocompleter.hasFocus = true;
    this.getAllChoices();
	},
	
	hideChoices: function() {
		this.autocompleter.onBlur();
	}
}
 