
var UploadHandlerXhr = UploadHandlerXhr || {};
var FileUploader = function(o){
	if(UploadHandlerXhr.isSupported()){
		new UploadHandlerXhr(o).upload()
	}else{
		new UploadHandlerForm(o).upload()
	}
}	
	
	/**
 * Class for uploading files using xhr
 * @inherits UploadHandlerAbstract
 */
 

UploadHandlerXhr = function(o){
	this._xhrs = [];
	this.element = o.element;
    this.endpoint =  o.endpoint;
    // current loaded size in bytes for each file
	this.params = o.params;
    this._loaded = [];
	
	this.onComplete=o.onComplete;
};

// static method
UploadHandlerXhr.isSupported = function(){
    var input = document.createElement('input');
    input.type = 'file';

    return (
        'multiple' in input &&
            typeof File != "undefined" &&
            typeof FormData != "undefined" &&
            typeof (new XMLHttpRequest()).upload != "undefined" );
};
UploadHandlerXhr.prototype = {
	onComplete: function(response) {},
    /**
     * Adds file to the queue
     * Returns id to use with upload, cancel
     **/
    add: function(file){
        if (!(file instanceof File)){
            throw new Error('Passed obj in not a File (in UploadHandlerXhr)');
        }

        return this._files.push(file) - 1;
    },
    getName: function(file){
        // fix missing name in Safari 4
        //NOTE: fixed missing name firefox 11.0a2 file.fileName is actually undefined
        return (file.fileName !== null && file.fileName !== undefined) ? file.fileName : file.name;
    },
    getSize: function(file){
		return file.fileSize != null ? file.fileSize : file.size;
    },
    isValid: function(id) {
        return this._files[id] !== undefined;
    },
    reset: function() {
        UploadHandlerAbstract.prototype.reset.apply(this, arguments);
        this._files = [];
        this._xhrs = [];
        this._loaded = [];
    },
    /**
     * Sends the file identified by id and additional query params to the server
     * @param {Object} params name-value string pairs
     */
    upload: function(id, params){
		var file = this.element.files[0],
            name = this.getName(this.element.files[0]),
            size = this.getSize(this.element.files[0]);

        var xhr = this._xhrs[id] = new XMLHttpRequest();
        var self = this;

        xhr.upload.onprogress = function(e){
            
        };

        xhr.onreadystatechange = function(){
		
		
            if (xhr.readyState == 4){
            	 response = eval("(" + xhr.responseText + ")");
               self.onComplete(response);
            }
        };

        // build query string
        params = this.params || {};
        params["name"] = name;
        var queryString = obj2url(params, this.endpoint);

        var protocol = this.demoMode ? "GET" : "POST";
        xhr.open(protocol, queryString, true);
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.setRequestHeader("X-File-Name", encodeURIComponent(name));
        xhr.setRequestHeader("Cache-Control", "no-cache");
       var boundary = Math.floor(Math.random()*32768);
	  // xhr.setRequestHeader("Content-Type", "multipart/form-data;boundary="+boundary);
        xhr.setRequestHeader("X-Mime-Type",file.type );
      //  this.log('Sending upload request for ' + name);
	              var formData = new FormData();
            formData.append("sprReport", file);
            file = formData;
        xhr.send(file);
    }
};
	
var obj2url = function(obj, temp, prefixDone){
    var uristrings = [],
        prefix = '&',
        add = function(nextObj, i){
            var nextTemp = temp
                ? (/\[\]$/.test(temp)) // prevent double-encoding
                ? temp
                : temp+'['+i+']'
                : i;
            if ((nextTemp != 'undefined') && (i != 'undefined')) {
                uristrings.push(
                    (typeof nextObj === 'object')
                        ? obj2url(nextObj, nextTemp, true)
                        : (Object.prototype.toString.call(nextObj) === '[object Function]')
                        ? encodeURIComponent(nextTemp) + '=' + encodeURIComponent(nextObj())
                        : encodeURIComponent(nextTemp) + '=' + encodeURIComponent(nextObj)
                );
            }
        };

    if (!prefixDone && temp) {
        prefix = (/\?/.test(temp)) ? (/\?$/.test(temp)) ? '' : '&' : '?';
        uristrings.push(temp);
        uristrings.push(obj2url(obj));
    } else if ((Object.prototype.toString.call(obj) === '[object Array]') && (typeof obj != 'undefined') ) {
        // we wont use a for-in-loop on an array (performance)
        for (var i = 0, len = obj.length; i < len; ++i){
            add(obj[i], i);
        }
    } else if ((typeof obj != 'undefined') && (obj !== null) && (typeof obj === "object")){
        // for anything else but a scalar, we will use for-in-loop
        for (var i in obj){
            add(obj[i], i);
        }
    } else {
        uristrings.push(encodeURIComponent(temp) + '=' + encodeURIComponent(obj));
    }

    if (temp) {
        return uristrings.join(prefix);
    } else {
        return uristrings.join(prefix)
            .replace(/^&/, '')
            .replace(/%20/g, '+');
    }
};
	
	
	
	
/**
 * Class for uploading files using form and iframe
 * @inherits UploadHandlerAbstract
 */
UploadHandlerForm = function(o){
    this.element = o.element;
    this.endpoint =  o.endpoint;
    // current loaded size in bytes for each file
	this.params = o.params;
    this._loaded = [];
	
	this.onComplete=o.onComplete;
	
    this._inputs = {};
    this._detach_load_events = {};
};

UploadHandlerForm.prototype= {
onComplete: function(response) {},
    add: function(fileInput){
        fileInput.setAttribute('name', this._options.inputName);
        var id = 'qq-upload-handler-iframe' + qq.getUniqueId();

        this._inputs[id] = fileInput;

        // remove file input from DOM
        if (fileInput.parentNode){
            qq(fileInput).remove();
        }

        return id;
    },
    getName: function(obj){
        // get input value and remove path to normalize
        return obj.value.replace(/.*(\/|\\)/, "");
    },
    isValid: function(id) {
        return this._inputs[id] !== undefined;
    },
    reset: function() {
        this._inputs = {};
        this._detach_load_events = {};
    },
    upload: function(id, params){
		var id = 'upload-handler-iframe';
        var input = this.element;

        if (!input){
            throw new Error('file with passed id was not added, or already uploaded or cancelled');
        }

        var fileName = this.getName(input);
		 params = this.params || {};
        params["name"] = fileName;

        var iframe = this._createIframe(id);
        var form = this._createForm(iframe, params);
		var parentElement = input.parentElement;
		var position = this.getPosition(input,parentElement.children);
		form.appendChild(input);
		var self = this;
        this._attachLoadEvent(iframe, function(){
            var response = self._getIframeContentJSON(iframe);

            // timeout added to fix busy state in FF3.6
            setTimeout(function(){
                self._detach_load_events[id]();
                delete self._detach_load_events[id];
                qq(iframe).remove();
            }, 1);

            if (!response.success) {
               
            }
            self.onComplete(response);
            
        });
        form.submit();
		if(parentElement.children.length>=position){
			parentElement.insertBefore(input, parentElement.children[position]);
		}
      //  form.remove();

        return id;
    },
	attach: function(element,type, fn) {
            if (element.addEventListener){
                element.addEventListener(type, fn, false);
            } else if (element.attachEvent){
                element.attachEvent('on' + type, fn);
            }
            return function() {
                this.detach(element,type, fn);
            };
        },
		detach: function(element,type, fn) {
            if (element.removeEventListener){
                element.removeEventListener(type, fn, false);
            } else if (element.attachEvent){
                element.detachEvent('on' + type, fn);
            }
            return this;
        },
    _attachLoadEvent: function(iframe, callback){
        var self = this;
        this.attach(iframe,'load', function(){
            
            // when we remove iframe from dom
            // the request stops, but in IE load
            // event fires
            if (!iframe.parentNode){
                return;
            }

            try {
                // fixing Opera 10.53
                if (iframe.contentDocument &&
                    iframe.contentDocument.body &&
                    iframe.contentDocument.body.innerHTML == "false"){
                    // In Opera event is fired second time
                    // when body.innerHTML changed from false
                    // to server response approx. after 1 sec
                    // when we upload file with iframe
                    return;
                }
            }
            catch (error) {
                //IE may throw an "access is denied" error when attempting to access contentDocument on the iframe in some cases
            }

            callback();
        });
    },
    /**
     * Returns json object received by iframe from server.
     */
    _getIframeContentJSON: function(iframe){
        //IE may throw an "access is denied" error when attempting to access contentDocument on the iframe in some cases
        try {
            // iframe.contentWindow.document - for IE<7
            var doc = iframe.contentDocument ? iframe.contentDocument: iframe.contentWindow.document,
                response;

            var innerHTML = doc.body.innerHTML;
            //plain text response may be wrapped in <pre> tag
            if (innerHTML && innerHTML.match(/^<pre/i)) {
                innerHTML = doc.body.firstChild.firstChild.nodeValue;
            }
            response = eval("(" + innerHTML + ")");
        } catch(error){
            response = {success: false};
        }

        return response;
    },
	toElement:function(html){
		var div = document.createElement('div');
		div.innerHTML = html;
		var element = div.firstChild;
		div.removeChild(element);
		return element;
	},
    /**
     * Creates iframe with unique name
     */
    _createIframe: function(id){
        // We can't use following code as the name attribute
        // won't be properly registered in IE6, and new window
        // on form submit will open
        // var iframe = document.createElement('iframe');
        // iframe.setAttribute('name', id);

        var iframe = this.toElement('<iframe src="javascript:false;" name="' + id + '" />');
        // src="javascript:false;" removes ie6 prompt on https

        iframe.setAttribute('id', id);

        iframe.style.display = 'none';
        document.body.appendChild(iframe);

        return iframe;
    },
    /**
     * Creates form, that will be submitted to iframe
     */
    _createForm: function(iframe, params){
        // We can't use the following code in IE6
        // var form = document.createElement('form');
        // form.setAttribute('method', 'post');
        // form.setAttribute('enctype', 'multipart/form-data');
        // Because in this case file won't be attached to request
        var protocol = "POST";
        var form = this.toElement('<form method="' + protocol + '" enctype="multipart/form-data"><input type="file" id="hiddenFileField"></form>');

        var queryString = obj2url(params, this.endpoint);

        form.setAttribute('action', queryString);
        form.setAttribute('target', iframe.name);
        form.style.display = 'none';
        document.body.appendChild(form);

        return form;
    },
	getPosition:function(elementToFind, arrayElements) {
    var i;
    for (i = 0; i < arrayElements.length; i += 1) {
        if (arrayElements[i] === elementToFind) {
            return i;
        }
    }
    return null; //not found
}
};

	
	
