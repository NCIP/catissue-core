
var Select2 = function(element, config) {
  this.element = element;
  this.config = config || {}

  var that = this;
  this.element.bind("change", function(e) {
    e.stopImmediatePropagation();
    var option = element.select2('data');

    var selected = {};
    selected[that.config.id || "id"] = option.id;
    selected[that.config.value || "value"] = option.text;
    if (that.config.onSelect) {
      that.config.onSelect(selected);
    }
  });

  this.render = function() {
    if (!this.config.options) {
      return this;
    }

    this.element.children().remove();
    this.element.append($('<option/>').prop('value', -1).append(""));

    var id = this.config.id || "id";
    var value = this.config.value || "value";
    for (var i = 0; i < this.config.options.length; ++i) {
      var option = this.config.options[i];
      if (typeof option == "object") {
        this.element.append($('<option/>').prop('value', option[id]).append(option[value]));
      } else {
        this.element.append($('<option/>').prop('value', option).append(option));
      }
    }

    this.element.select2();
    
    if (this.config.selectedOpts) {
      this.element.select2('val', this.config.selectedOpts);
    }

    return this;
  };

  this.options = function(options) {
    this.config.options = options;
    return this;
  };

  this.selectedOpts = function(selectedOpts) {
    this.config.selectedOpts = selectedOpts;
    return this;
  };

  this.onSelect = function(onSelect) {
    this.config.onSelect = onSelect;
    return this;
  }
};

