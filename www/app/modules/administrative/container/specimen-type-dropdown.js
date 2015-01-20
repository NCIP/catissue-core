
angular.module('os.administrative.container.specimenTypeDropdown', ['os.administrative.models'])
  .directive('osSpecimenType', function($timeout, $translate, $filter, PvManager) {
    return {
      restrict: 'E',

      replace: true,

      scope: {
        ngModel: '=ngModel',
        placeholder: '@'
      },

      link: function(scope, element, attrs, ctrl) {
        if (!scope.ngModel && angular.isDefined(attrs.multiple)) {
          scope.ngModel = [];
        }

        scope.gblSpecimenTypes = PvManager.getSpecimenTypes();
        scope.specimenTypes = angular.copy(scope.gblSpecimenTypes);

        scope.onSelect = function(item) {
          // If All specimen Type is selected , then remove already selected specimen types of that class.
          var all = $translate.instant('container.all');
          var allOptionText = all + ' ' + item.class;
          if (item.value == allOptionText) {
            var classSpecimenTypes = $filter('filter')(scope.specimenTypes, {class: item.class});
            angular.forEach(classSpecimenTypes, function(value, key) {
              if (value.class == item.class) {
                var index = scope.ngModel.indexOf(value.value);
                if (index > -1) {
                  $timeout(function() {
                    scope.ngModel.splice(index, 1);
                    scope.$apply();
                  });
                }
              }
            });
            // Remove specimen types of the class from specimen type list ,if all types of that class are selected.
            scope.specimenTypes = $filter('filter')(scope.specimenTypes, {class: '!' + item.class});
          }
        }

        scope.onRemove = function(item) {
          /*If remove all specimen type (Ex- "All Fluid", "All Molecule" etc.)
            then add specimen types of that class in specimen type list. */
          var all = $translate.instant('container.all');
          var allOptionText = all + ' ' + item.class;
          if (item.value == allOptionText) {
             var classSpecimenTypes = $filter('filter')(scope.gblSpecimenTypes, {class: item.class});
             scope.specimenTypes = scope.specimenTypes.concat(classSpecimenTypes);
          }
        }
      },

      template: function(tElem, tAttrs) {
        return angular.isDefined(tAttrs.multiple) ?
          '<div>' +
            '<ui-select multiple ng-model="$parent.ngModel" on-select="onSelect($item)" on-remove="onRemove($item)">' +
              '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                '{{$item.value}}' +
              '</ui-select-match>' +
              '<ui-select-choices group-by="\'class\'" repeat="specimenType.value as specimenType in specimenTypes |' +
                ' filter: $select.search">'+
                '<span ng-bind-html="specimenType.value | highlight: $select.search"></span>' +
              '</ui-select-choices>' +
            '</ui-select>' +
          '</div>'

          :

          '<div>' +
            '<ui-select ng-model="$parent.ngModel">' +
              '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                '{{$select.selected.value}}' +
              '</ui-select-match>' +
              '<ui-select-choices group-by="\'class\'" repeat="specimenType.value as specimenType in specimenTypes |' +
                ' filter: $select.search">' +
                '<span ng-bind-html="specimenType.value | highlight: $select.search"></span>' +
              '</ui-select-choices>' +
            '</ui-select>' +
          '</div>';
      }
    };
  });
