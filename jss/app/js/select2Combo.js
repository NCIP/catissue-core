var Select2 = function(element, options) {
        this.element = element;
        this.selOpts = options;
        this.onChangeFn = null;

        var that = this;
        this.element.bind("change", function(e) {
          e.stopImmediatePropagation();
          var option = element.select2('data');
          var selected = {id: option.id, text: option.text};
          if (that.onChangeFn) {
            that.onChangeFn(selected);
          }
        });

        this.render = function() {
          this.element.children().remove();
          for (var i = 0; i < this.selOpts.length; ++i) {
            var option = this.selOpts[i];
            this.element.append($('<option/>').prop('value', option.id).append(option.name));
          }

          this.element.select2();
          return this;
        };

        this.options = function(options) {
          this.selOpts = options;
          return this;
        };

        this.onChange = function(onChangeFn) {
          this.onChangeFn = onChangeFn;
          return this;
        }
      };