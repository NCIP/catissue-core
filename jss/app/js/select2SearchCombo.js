var Select2Search = function(element) {
        this.element = element;
        this.queryFn = null;
        this.onChangeFn = null;
		this.setValue = null;
        
        var that = this;
        this.element.bind("change", function(e) {
          e.stopImmediatePropagation();
          var option = that.element.select2('data');
          var selected = {id: option.id, text: option.text};
          if (that.onChangeFn) {
            that.onChangeFn(selected);
          }
        });
		
		this.render = function() {
          var that = this;
          this.element.select2({
            minimumInputLength: 0, 
            query: function(query) { 
			  that.queryFn(query.term, function(result){
					console.log(result);
					query.callback({results: result});
			  })
              // var results = [];
              // if (that.queryFn) {
                // results = that.queryFn(query.term);
              // }

              // query.callback({results: results});
            }});

          return this;
        },

        this.onQuery = function(queryFn) {
          this.queryFn = queryFn;
          return this;
        },

        this.onChange = function(onChangeFn) {
          this.onChangeFn = onChangeFn;
          return this;
        }
		
		this.setValue = function(){
		$(this.element).select2("val","");
		return this;
		}
		
      };