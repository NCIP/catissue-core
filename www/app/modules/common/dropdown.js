
angular.module('openspecimen')
  .directive('osSelect', function() {
    function linker(scope, element, attrs) {
      var changeFn = undefined
      if (attrs.onChange) {
        changeFn = scope.$eval(attrs.onChange);
        scope.$watch(attrs.ngModel, function(newVal, oldVal) {
          if (newVal != oldVal) {
            scope.$eval(attrs.onChange)(newVal);
          }
        });
      }
    }

    return {
      restrict: 'E',
      compile: function(tElem, tAttrs) {
        var multiple = angular.isDefined(tAttrs.multiple);
        var uiSelect = angular.element(multiple ? '<ui-select multiple/>' : '<ui-select/>')
          .attr('ng-model', tAttrs.ngModel)

        var uiSelectMatch = angular.element('<ui-select-match/>')
          .attr('placeholder', tAttrs.placeholder);
        
        var uiSelectChoices = angular.element('<ui-select-choices/>')
          .attr('repeat', "item in " + tAttrs.list + " | filter: $select.search")
          .append('<span ng-bind-html="item | highlight: $select.search"></span>');

        if (multiple) {
          uiSelectMatch.append('{{$item}}');
        } else {
          uiSelectMatch.append('{{$select.selected}}');
        }

        if (angular.isDefined(tAttrs.refresh)) {
          uiSelectChoices.attr({
            'refresh': tAttrs.refresh + '($select.search)',
            'refresh-delay': tAttrs.refreshDelay || 750
          });
        }
            
        uiSelect.append(uiSelectMatch).append(uiSelectChoices);
        
        var selectContainer = angular.element("<div/>")
          .addClass("os-select-container")
          .append(uiSelect);

        tElem.replaceWith(selectContainer);
        return linker;
      }
    };
  });
