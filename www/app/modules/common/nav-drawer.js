angular.module('openspecimen')
  .directive('osNavDrawer', function($compile, $translate, osNavDrawerSvc) {
    function getNavHeader() {
      var div = angular.element('<div class="os-page-header no-breadcrumbs"/>');

      var navBtn = angular.element('<button class="os-nav-button"/>')
        .append('<span class="fa fa-bars"></span>');

      var navEl = angular.element('<div class="os-nav-button-wrapper"/>')
        .append(navBtn);

      var navigateTo = $translate.instant("menu.navigate_to");
      var title = angular.element('<h3/>').append(navigateTo);

      var innerEl = angular.element('<div class="os-page-header-inner"/>')
        .append(angular.element('<div class="os-page-header-content-wrapper"/>')
          .append(angular.element('<div class="os-page-header-content"/>').append(title)));

      return div.append(navEl).append(innerEl);
    }

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element.find('ul').addClass('os-menu-items');
        element.find('ul').on('click', function() {
          osNavDrawerSvc.toggle();
        });

        element.addClass('os-nav-drawer')
          .prepend(getNavHeader());

        var overlay = angular.element('<div/>').addClass('os-nav-drawer-overlay');
        element.after(overlay);
        overlay.on('click', function() {
          osNavDrawerSvc.toggle();
        });

        element.removeAttr('os-nav-drawer');
        osNavDrawerSvc.setDrawer(element);
        $compile(element)(scope);
      }
    };
  })
  .directive('osNavButton', function(osNavDrawerSvc) {
    return {
      restrict: 'AC',
      link: function(scope, element, attrs) {
        element.on('click', function() {
          osNavDrawerSvc.toggle();
        });
      }
    };
  })
  .factory('osNavDrawerSvc', function() {
    var drawerEl = undefined;
    return {
      setDrawer: function(drawer) {
        drawerEl = drawer;
      },

      toggle: function() {
        drawerEl.toggleClass('active');
      }
    }
  });
