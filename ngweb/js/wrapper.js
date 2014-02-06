
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
    this.element.append($('<option/>').prop('value', "").append(""));

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

