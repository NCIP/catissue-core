angular.module('openspecimen')
  .directive('osDateParser', function ($parse) {
    return {
      require: 'ngModel',
      restrict: 'A',
      link: function(scope, element, attrs, ngModel) {

        if (attrs.ngModel) {
          scope.$watch(attrs.ngModel, function(newVal) {
            if (newVal != undefined && !(newVal instanceof Date)) {
              ngModel.$setViewValue(new Date(newVal));
            }
          })
        }
        
      }
    }
  });
