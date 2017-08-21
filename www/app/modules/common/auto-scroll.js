
angular.module('openspecimen')
  .directive('osAutoScroll', function() {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element[0].scrollIntoView();
      }
    }
  });
