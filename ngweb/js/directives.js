
angular.module("plus.directives", [])
  .directive("kaSelect", function() {
    return {
      restrict: "E",
      replace: "true",
      template: "<select></select>",
      scope: {
        options: "=options",
        selected: "=ngModel",
        onSelect: "&onSelect"
      },

      link: function(scope, element, attrs) {
        var config = {
          options: [], 
          id: attrs.optionId,
          value: attrs.optionValue,
          onSelect: scope.onSelect()
        };
 
        scope.select = new Select2(element, config).render();

        scope.$watch('options', function(options) {
          scope.select.options(options).selectedOpts(scope.selected).render();
        });
      }
    };
  })

  .directive("kaDraggable", function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var options = scope.$eval(attrs.kaDraggable);
        console.log(options.helper);
        console.log(typeof options.helper);
        element.draggable(options);
      }
    };
  })

  .directive("kaDroppable", function() {
    return {
      restrict: "A",
      scope: {
        onDrop: "&"
      },
      link: function(scope, element, attrs) {
        var options = scope.$eval(attrs.kaDroppable);
        options.drop = function(event, ui) {
          var onDrop = scope.onDrop();
          if (onDrop) {
            onDrop(ui.draggable, angular.element(this));
          } else {
            alert(ui.draggable.text());
          }
        };
        element.droppable(options);
      }
    };
  })

  .directive("kaSortable", function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var opts = scope.$eval(attrs.sortOpts);
        console.log("Sort opts: " + opts);
        element.sortable(opts);
      }
    };
  })
      
  .directive('kaDatePicker', function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        element.datepicker({autoclose: true});
      }
    };
  });
