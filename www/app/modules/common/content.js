
angular.module('openspecimen')
  .directive('osContent', function($window) {
    return {
      restrict: "A",
      link: function(scope, element, attr) {
        var sibling = element.siblings("." + attr.osContent);
        var elTop = sibling.outerHeight();

        element.css({
          'height': $window.innerHeight, 
          'padding-top': elTop
        });

        element.children().css({
          'height': '100%',
          'overflow-y': 'auto'
        });
      }
    };
  });
