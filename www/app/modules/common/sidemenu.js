
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
  .directive('osSideMenu', function($compile, SideMenuState) {
    return {
      restrict: "A",
      link: function(scope, element, attr) {
        element.addClass('os-side-menu-wrapper');

        if (!SideMenuState.isOpened()) {
          element.addClass('closed');
        } else {
          element.addClass('opened');
        }

        var navBtn = angular.element('<a class="os-nav-button"/>')
          .append('<span class="fa fa-bars"></span>')
          .append('<span class="os-title" translate="menu.navigate_to"></span>');
        var navEl = angular.element('<li>').append(navBtn);

        element.find('ul').addClass('os-side-menu').prepend(navEl);

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
          element.siblings('.os-page-header').addClass('side-menu-opened');
          SideMenuState.open();
        });

        close.on("click", function(event) {
          event.stopPropagation();
          element.removeClass('opened');
          element.addClass('closed');
          element.siblings('.os-page-header').removeClass('side-menu-opened');
          SideMenuState.close();
        });

        $compile(navBtn)(scope);
      }
    };
  });
