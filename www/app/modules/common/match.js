angular.module('openspecimen')
  .directive('osMatch', function () {
    return {
      require: 'ngModel',
      restrict: 'A',
      scope: {
        osMatch: '='
      },
      link: function(scope, elem, attrs, ctrl) {
        scope.$watch(function() {
          var modelValue = ctrl.$modelValue || ctrl.$$invalidModelValue;
          return (ctrl.$pristine && angular.isUndefined(modelValue)) || scope.osMatch === modelValue;
        }, function(currentValue) {
          ctrl.$setValidity('match', currentValue);
        });
      }
    }
  });

