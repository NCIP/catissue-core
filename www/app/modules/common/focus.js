
angular.module('openspecimen')
  .directive('osFocus', function($timeout) {
    return { 
      restict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.osFocus, function(newVal) {
          if (newVal == true) {
            $timeout(function() {
              element[0].focus();
            }, 0, false);
          }
        });
      }
    };
  });
