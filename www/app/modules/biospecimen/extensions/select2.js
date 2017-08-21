
var Select2 = function(element, config) {
  this.element = element;
  this.config = config || {}
  this.rendered = false;

  var that = this;
  this.element.bind("change", function(e) {
    //e.stopImmediatePropagation();
    var selectedOpt = element.select2('data');

    if (!that.config.onSelect) {
      return;
    }

    var id = that.config.id || "id";
    var selectedOptIds = [];
    var multiple = false;
    if (selectedOpt instanceof Array) {
      for (var i = 0; i < selectedOpt.length; ++i) {
        selectedOptIds.push(selectedOpt[i].id);
      }
      multiple = true;
    } else {
      selectedOptIds.push(selectedOpt.id);
    }

    var selectedOptObjs = [];
    for (var i = 0; i < that.config.options.length; ++i) {
      var option = that.config.options[i];
      var optionId = (typeof option == "object") ? option[id] : option;
      for (var j = 0; j < selectedOptIds.length; j++) {
        if (selectedOptIds[j] == optionId) {
          selectedOptObjs.push(option);
          break;
        }
      }
      if (selectedOptObjs.length == selectedOptIds.length) {
        break;
      }
    }

    if (multiple) {
      that.config.onSelect(selectedOptObjs);
    } else {
      that.config.onSelect(selectedOptObjs.length > 0 ? selectedOptObjs[0] : null);
    }
  });

  this.render = function() {
    if (!this.config.options) {
      return this;
    }

    this.element.children().remove();
    if (!element.attr('multiple')) {
      this.element.append($('<option/>').prop('value', "").append(""));
    }

    var id = this.config.id || "id";
    var value = this.config.value || "value";
    for (var i = 0; i < this.config.options.length; ++i) {
      var option = this.config.options[i];
      if (typeof option == "object") {
        var idVal = (id in option) ? option[id] : i;
        this.element.append($('<option/>').prop('value', idVal).append(option[value]));
      } else {
        this.element.append($('<option/>').prop('value', option).append(option));
      }
    }

    this.element.select2();

    this.highlightSelectedOpts();
    this.rendered = true;
    return this;
  };

  this.options = function(options) {
    this.config.options = options;
    this.rendered = false;
    return this;
  };

  this.selectedOpts = function(selectedOpts) {
    this.config.selectedOpts = selectedOpts;
    if (this.rendered) {
      this.highlightSelectedOpts();
    }
    return this;
  };

  this.onSelect = function(onSelect) {
    console.log("On selected:");
    this.config.onSelect = onSelect;
    return this;
  };

  this.highlightSelectedOpts = function() {
    var id = this.config.id || "id";
    var value = this.config.value || "value";
    if (this.config.selectedOpts) {
      if (this.config.selectedOpts instanceof Array) {
        var selectedVals = [];
        for (var i = 0; i < this.config.selectedOpts.length; ++i) {
          if (typeof this.config.selectedOpts[i] == "object") {
            selectedVals.push(this.config.selectedOpts[i][id]);
          } else {
            selectedVals.push(this.config.selectedOpts[i]);
          }
        }
        this.element.select2('val', selectedVals);
      } else if (typeof this.config.selectedOpts == "object") {
        this.element.select2('val', this.config.selectedOpts[id]);
      } else {
        this.element.select2('val', this.config.selectedOpts);
      }
    } else {
      this.element.select2('val', '');
    }
  };

  this.enable = function(enable) {
    this.element.select2('enable', enable);
  };
};

var Select2Search = function(element, opts) {
  this.element = element;
  this.queryFn = null;
  this.onChangeFn = null;
  this.initSelectionFn = null;

  var that = this;
  this.element.bind("change", function(e) {
    //e.stopImmediatePropagation();
    var selected = that.element.select2('data');
    if (that.onChangeFn) {
      that.onChangeFn(selected);
    }
  });

  this.render = function() {
    var that = this;
    this.element.select2({
      minimumInputLength: 0,
      multiple: typeof opts != "undefined" && opts.multiple == true,
      query: function(query) {
        that.queryFn(query.term, function(result) {
          query.callback({results: result});
	})
      },
      initSelection: function(element, callback) {
        that.initSelectionFn(element,callback);
      }
    });

    return this;
  },

  this.onQuery = function(queryFn) {
    this.queryFn = queryFn;
    return this;
  },

  this.onChange = function(onChangeFn) {
    this.onChangeFn = onChangeFn;
    return this;
  },

  this.onInitSelection = function(initSelectionFn) {
    this.initSelectionFn = initSelectionFn;
    return this;
  },

  this.setValue = function(value) {
    $(this.element).select2("val", value);
    return this;
  },

  this.getValue = function() {
    var data = $(this.element).select2('data');
    if (!data) {
      return {id: '', text: ''};
    }

    return {id: data.id, text: data.text};
  }
};

