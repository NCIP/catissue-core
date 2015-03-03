angular.module('openspecimen')
  .directive('osMatchField', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function(scope, elem, attrs, ctrl) {
        scope.ngModel = undefined;
        scope.matchTo = undefined;

        var onChange = function() {
          var isChanged = (ctrl.$pristine && angular.isUndefined(scope.ngModel)) || scope.matchTo === scope.ngModel;
          ctrl.$setValidity('match', isChanged);
        }

        scope.$watch(attrs.ngModel, function(newVal) {
          scope.ngModel = newVal;
          onChange();
        });

        scope.$watch(attrs.osMatchField, function(newVal) {
          scope.matchTo = newVal;
          onChange();
        })

      }
    }
  });

