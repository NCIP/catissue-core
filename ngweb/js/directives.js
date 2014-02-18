
angular.module("plus.directives", [])
  .directive("kaSelect", function() {
    return {
      restrict: "E",
      replace: "true",
      template: "<select></select>",
      scope: {
        options: "=options",
        selected: "=selected",
        onSelect: "&onSelect",
        disabled: "=disabled"
      },

      link: function(scope, element, attrs) {
        var config = {
          options: [],
          id: attrs.optionId,
          value: attrs.optionValue,
          onSelect: function(selected) {
            scope.selected = selected;
            var fn = scope.onSelect();
            if (fn) {
              fn(selected);
            }

            if (!scope.$$phase) {
              scope.$apply();
            }
          }
        };
 
        scope.select = new Select2(element, config).render();
        scope.$watch('selected', function(selected) {
          scope.select.selectedOpts(selected);
        });

        scope.$watch('options', function(options) {
          scope.select.options(options).selectedOpts(scope.selected).render();
        });

        scope.$watch('disabled', function(disabled) {
        });
      }
    };
  })

  .directive("kaDraggable", function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        var options = scope.$eval(attrs.kaDraggable);
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
  })

  .directive('kaJqGrid', function() {
    return {
      restrict: "E",
      scope: { 
        config: "=",
        data: "="
      },

      link: function(scope, element, attrs) {
        var table, nameValue;

        scope.$watch('config', function (newValue) {
          element.children().empty();
          table = angular.element('<table></table>');
          element.append(table);

          var colNames = [];
          var colModel = [];

          var colDefs = newValue.colDefs;
          for (var i = 0; i < colDefs.length; ++i) {
            colNames.push(colDefs[i].caption);
            colModel.push({
              name: colDefs[i].name,
              index: colDefs[i].name,
              sorttype: colDefs[i].sorttype ? colDefs[i].sorttype : "string"
            });
          }

          nameValue = newValue.nameValue ? true : false;
          $(table).jqGrid({
            datatype: "local",
            height: newValue.height,
            colNames: colNames,
            colModel: colModel,
            caption: newValue.caption,
            autowidth: true
          });
        });

        scope.$watch('data', function (newValue, oldValue) {
          var i;
          for (i = oldValue.length - 1; i >= 0; i--) {
            $(table).jqGrid('delRowData', i);
          }
          for (i = 0; i < newValue.length; i++) {
            $(table).jqGrid('addRowData', i, newValue[i]);
          }
        });
      }
    };
  })

  .directive('kaShowOnParentFocus', function() {
    return {
      restrict: "A",
      link: function(scope, element, attrs) {
        element.parent().bind('mouseenter', function() { element.show(); });
        element.parent().bind('mouseleave', function() { element.hide(); });
      }
    };
  })

  .directive('kaCallout', function() {
    return {
      restrict: "E",
      link: function(scope, element, attrs) {
        attrs.$observe('showWhen', function(show) {
          if (show == "true") {
            element.popover().popover('show');
            if (attrs.fadeOut) {
              setTimeout(function() { console.log("timeout destroy"); element.popover('destroy'); }, parseInt(attrs.fadeOut));
            }
          } else {
            element.popover('destroy');
          }
        });
      }
    };
  })
  
  .directive("kaFixHeight", function() {
    return {
      restrict: "A",
      priority: 9933,
      link: function(scope, element, attrs) {
        var height = attrs.kaFixHeight;
        element.children().first().css("height", height);
      }
    }
  })

  .directive('kaPopoverTemplatePopup', ['$templateCache', '$compile', function ( $templateCache, $compile ) {
    return {
      restrict: 'EA',
      replace: true,
      scope: { title: '@', content: '@', placement: '@', animation: '&', isOpen: '&' },
      templateUrl: 'templates/popover-template.html',
      link: function(scope, iElement) {
        var content = angular.fromJson(scope.content),
        template = $templateCache.get(content.templateUrl),
        templateScope = scope,
        scopeElements = document.getElementsByClassName('ng-scope');
 
        angular.forEach(scopeElements, function(element) {
          var aScope = angular.element(element).scope();
          if (aScope.$id == content.scopeId) {
            templateScope = aScope;
          }
        });
 
        iElement.find('div.popover-content').html($compile(template)(templateScope));
      }
    };
  }])

  .directive('kaPopoverTemplate', ['$tooltip', function($tooltip) {
    var tooltip = $tooltip('kaPopoverTemplate', 'popover', 'click');
    var linker = tooltip.compile;
 
    tooltip.compile = function(tElem, tAttrs) {
      return {
        'pre': function(scope, iElement, iAttrs) {
          iAttrs.$set('kaPopoverTemplate', {templateUrl: iAttrs.kaPopoverTemplate, scopeId: scope.$id});
        },
        'post': linker(tElem, tAttrs)
      };
    };
 
    return tooltip;
  }]);
