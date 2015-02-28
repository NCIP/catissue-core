
angular.module('openspecimen')
  .factory('SideMenuState', function() {
    var isOpened = false;

    return {
      isOpened : function() {
        return isOpened;
      },

      open: function() {
        isOpened = true;
      },

      close: function() {
        isOpened = false;
      }
    }
  })
  .directive('osSideMenu', function(SideMenuState) {
    return {
      restrict: "A",
      link: function(scope, element, attr) {
        var elTop = 57;
        element.addClass('os-side-menu-wrapper');

        if (!SideMenuState.isOpened()) {
          element.addClass('closed');
        } else {
          element.addClass('opened');
        }

        var ul = element.find('ul').addClass('os-side-menu');

        var open = $("<button/>")
          .addClass("btn open")
          .append($("<span/>").append("&raquo;"));

        var close = $("<button/>")
          .addClass("btn close")
          .append($("<span/>").append("&laquo;"));

        var div = angular.element('<div/>')
          .addClass('open-close')
          .append(open)
          .append(close);

        element.append(div);

        open.on("click", function(event) {
          event.stopPropagation();
          element.removeClass('closed');
          element.addClass('opened');
          SideMenuState.open();
        });

        close.on("click", function(event) {
          event.stopPropagation();
          element.removeClass('opened');
          element.addClass('closed');
          SideMenuState.close();
        });
      }
    };
  });
