
angular.module('os.administrative.container.specimenTypeDropdown', ['os.administrative.models'])
  .directive('osSpecimenType', function(PvManager) {
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

        scope.loadSpecimenTypes = function() {
          scope.specimenClasses = PvManager.getPvs('specimen-class');
          scope.specimenClassTypeMap = {};

          scope.$watch('specimenClasses', function(newVal, oldVal) {
            if (!newVal || newVal == oldVal || newVal.length ==0) {
              return;
            }

            angular.forEach(scope.specimenClasses, function(specimenClass) {
              scope.specimenClassTypeMap[specimenClass] = PvManager.getPvsByParent('specimen-class', specimenClass);
            });
          }, true);

          scope.$watch('specimenClassTypeMap', function(newVal) {
            scope.specimenTypes = [];
            angular.forEach(scope.specimenClassTypeMap, function(value, key) {
              var allOption = 'All ' + key;
              var obj = {class: key, value: allOption};
              scope.specimenTypes.push(obj);
              angular.forEach(value, function(type) {
                var obj = {class: key, value: type};
                scope.specimenTypes.push(obj);
              });
            });
          }, true);
        }

        scope.onSelect = function(item) {
          var allOptionText = 'All '+ item.class;
          if (item.value == allOptionText) {
            angular.forEach(scope.specimenTypes, function(value, key) {
              if(value.class == item.class) {
                var index = scope.ngModel.indexOf(value.value);
                if (index > -1) {
                  scope.ngModel.splice(index, 1);
                }
              }
            });

            if(!scope.$$phase) {
              scope.$apply();
            }
          }
        }
      },

      template: function(tElem, tAttrs) {
        return angular.isDefined(tAttrs.multiple) ?
          '<div>' +
            '<ui-select multiple ng-model="$parent.ngModel" on-select="onSelect($item)">' +
              '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                '{{$item.value}}' +
              '</ui-select-match>' +
              '<ui-select-choices group-by="\'class\'" repeat="specimenType.value as specimenType in specimenTypes |' +
                ' filter: $select.search | groupSelect:$parent.ngModel" refresh="loadSpecimenTypes()" refresh-delay="750">' +
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
                ' filter: $select.search | groupSelect:$parent.ngModel" refresh="loadSpecimenTypes()" refresh-delay="750">' +
                '<span ng-bind-html="specimenType.value | highlight: $select.search"></span>' +
              '</ui-select-choices>' +
            '</ui-select>' +
          '</div>';
      }
    };
  });
