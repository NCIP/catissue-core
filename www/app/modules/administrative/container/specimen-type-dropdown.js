
angular.module('os.administrative.container.specimenTypeDropdown', ['os.administrative.models'])
  .directive('osSpecimenType', function($timeout, $translate, $filter, PvManager) {
    function linker(scope, element, attrs) {
      scope.spClassTypeMap = PvManager.getSpecimenTypes();
      var all = $translate.instant('container.all');

      function populateSpecimenTypeList() {
        scope.specimenTypes = [];
        angular.forEach(scope.spClassTypeMap, function(spTypes, spClass) {
          scope.specimenTypes.push({class: spClass, type: all + ' ' + spClass, visible: true });
          angular.forEach(spTypes, function(spType) {
            scope.specimenTypes.push({class: spClass, type: spType, visible: true});
          });
        });
      }

      function setVisibility(spClass, visible) {
        angular.forEach(scope.specimenTypes, function(specimenType) {
          if (specimenType.class == spClass) {
            specimenType.visible = visible;
          }
        })
      }

      scope.onSelect = function(item) {
        var allOptionText = all + ' ' + item.class;
        if (item.type == allOptionText) {
          setVisibility(item.class, false);

          angular.forEach(scope.spClassTypeMap[item.class], function(type) {
            $timeout(function() {
              var index = scope.$eval(attrs.ngModel).indexOf(type);
              if (index > -1) {
                scope.$eval(attrs.ngModel).splice(index, 1);
                scope.$apply();
              }
            });
          });
        }
      }

      scope.onRemove = function(item) {
        var allOptionText = all + ' ' + item.class;
        if (item.type == allOptionText) {
          setVisibility(item.class, true);
        }
      }

      populateSpecimenTypeList();
    }

    return {
      restrict: 'E',

      compile: function(tElem, tAttrs) {
        var multiple = angular.isDefined(tAttrs.multiple);
        var uiSelect = angular.element(multiple ? '<ui-select multiple/>' : '<ui-select/>')
          .attr('ng-model', tAttrs.ngModel)
          .attr('on-select', 'onSelect($item)')
          .attr('on-remove', 'onRemove($item)')

        var uiSelectMatch = angular.element('<ui-select-match/>')
          .attr('placeholder', tAttrs.placeholder);

        var uiSelectChoices = angular.element('<ui-select-choices/>')
          .attr('group-by',"'class'")
          .attr('repeat', "specimenType.type as specimenType in specimenTypes | filter: $select.search | filter: { visible: true}")
          .append('<span ng-bind-html="specimenType.type | highlight: $select.search"></span>');

        if (multiple) {
          uiSelectMatch.append('{{$item.type}}');
        } else {
          uiSelectMatch.append('{{$select.selected.type}}');
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

