
angular.module('openspecimen')
  .directive('osRowClick', function($parse) {
    function linker(fn) {
      return function(scope, element, attrs) {
        element.on('click', function(event) {
          var target = event.target;
          while (target != this) {
            if (target.nodeName == 'A' || (target.classList && target.classList.contains('os-click-esc'))) {
              event.stopPropagation();
              return;
            }

            target = target.parentNode;
          }
          
          var cb = function() { 
            fn(scope, {$event: event});
          }
          scope.$apply(cb);
        })
      };
    };

    return {
      restrict: 'A',
 
      compile: function(element, attrs) {
        element.addClass('os-pointer-cursor');
        var fn = $parse(attrs.osRowClick);
        return linker(fn);
      }
    }
  });
