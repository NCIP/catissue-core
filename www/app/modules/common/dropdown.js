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

    function getItemDisplayValue(item, tAttrs) {
      var result = item;
      if (!!tAttrs.displayProp) {
        result += '.' + tAttrs.displayProp;
      }
      return result;
    }  

    return {
      restrict: 'E',
      compile: function(tElem, tAttrs) {
        var multiple = angular.isDefined(tAttrs.multiple);
        var uiSelect = angular.element(multiple ? '<ui-select multiple/>' : '<ui-select/>')
          .attr('ng-model', tAttrs.ngModel)
          .attr('ng-disabled', tAttrs.ngDisabled)
          .attr('reset-search-input', true)
          .attr('append-to-body', tAttrs.appendToBody == true)
          .attr('os-tabable', !!tAttrs.osTabable);
    
        if (tAttrs.ngInit) {
          uiSelect.attr('ng-init', tAttrs.ngInit);
        }

        if (tAttrs.onSelect) {
          uiSelect.attr('on-select', tAttrs.onSelect);
        }

        if (tAttrs.onRemove) {
          uiSelect.attr('on-remove', tAttrs.onRemove);
        }

        if (tAttrs.title) {
           uiSelect.attr('title', tAttrs.title);
        }

        var uiSelectMatch = angular.element('<ui-select-match/>')
          .attr('placeholder', tAttrs.placeholder)
          .attr('allow-clear', tAttrs.required == undefined);
        
        var searchItem = getItemDisplayValue('item', tAttrs);
        var uiSelectChoices = angular.element('<ui-select-choices/>');

        if (tAttrs.selectProp) {
          uiSelectChoices.attr('repeat', "item." + tAttrs.selectProp + " as item in " + tAttrs.list + " | filter: $select.search");
        } else {
          uiSelectChoices.attr('repeat', "item in " + tAttrs.list + " | filter: $select.search");
        }

        if (tAttrs.groupBy) {
          uiSelectChoices.attr('group-by', tAttrs.groupBy);
        }

        uiSelectChoices.append('<span ng-bind-html="' + searchItem + ' | highlight: $select.search"></span>');

        if (multiple) {
          uiSelectMatch.append('{{' + getItemDisplayValue('$item', tAttrs) + '}}');
        } else {
          uiSelectMatch.append('{{' + getItemDisplayValue('$select.selected', tAttrs) + '}}');
        }

        if (angular.isDefined(tAttrs.refresh)) {
          uiSelectChoices.attr({
            'refresh': tAttrs.refresh + '($select.search, ' + tAttrs.refreshArgs + ')',
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
