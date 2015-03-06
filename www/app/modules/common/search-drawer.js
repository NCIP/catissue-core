angular.module('openspecimen')
  .directive('osSearchDrawer', function($compile, osSearchDrawerSvc) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element.addClass('os-search-drawer');
        element.removeAttr('os-search-drawer');

        osSearchDrawerSvc.setDrawer(element);
        $compile(element)(scope);
      }
    };
  })
  .directive('osSearchButton', function(osSearchDrawerSvc) {
    return {
      restrict: 'AC',
      link: function(scope, element, attrs) {
        element.on('click', function() {
          osSearchDrawerSvc.toggle();
        });
      }
    };
  })
  .factory('osSearchDrawerSvc', function() {
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
