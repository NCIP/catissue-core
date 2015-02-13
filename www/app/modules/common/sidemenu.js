
angular.module('openspecimen')
  .directive('osSideMenu', function($window) {
    return {
      restrict: "A",
      link: function(scope, element, attr) {
        var elTop = 57;//element.position().top; // TODO:
        element.addClass('os-side-menu-wrapper');
          //.css("height", $window.innerHeight - elTop);

        var open = $("<button/>")
          .addClass("btn btn-xs os-side-menu-show-btn")
          .append($("<span/>").append("&raquo;"));

        var close = $("<button/>")
          .addClass("btn btn-xs os-side-menu-close-btn")
          .append($("<span/>").append("&laquo;"));

        element.prepend(open);
        element.prepend(close);

        var ul = element.find('ul').addClass('os-side-menu');

        /*var win = angular.element($window);
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
        });*/

        element.mouseenter(function() {
          element.addClass("os-show-side-menu");
        });

        element.mouseleave(function() {
          element.removeClass("os-show-side-menu");
        });

        open.on("click", function() {
          element.addClass("os-show-side-menu");
        });

        close.on("click", function() {
          element.removeClass("os-show-side-menu");
        });
      }
    };
  });
