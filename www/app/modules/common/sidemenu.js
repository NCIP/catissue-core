
angular.module('openspecimen')
  .directive('osSideMenu', function($window) {
    return {
      restrict: "A",
      link: function(scope, element, attr) {
        var elTop = element.position().top;
        element.addClass('os-side-menu-wrapper')
          .css("height", $window.innerHeight - elTop);

        var ul = element.find('ul').addClass('os-side-menu');

        var win = angular.element($window);
        var docked = false;
        win.bind('scroll', function() { 
          var scrollTop = win.scrollTop();
          if (scrollTop > elTop && !docked) {
            element.addClass("docked")
              .css("height", $window.innerHeight);
            docked = true;
          } else if (scrollTop < elTop && docked) {
            element.removeClass("docked");
            docked = false;
          }

          if (!docked) {
            element.css("height", $window.innerHeight - elTop + scrollTop);
          }
        });
      }
    };
  });
