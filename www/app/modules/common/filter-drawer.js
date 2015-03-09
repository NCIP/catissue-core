angular.module('openspecimen')
  .directive('osFilterDrawer', function(osFilterDrawerSvc) {
    function getHeader() {
      var div = angular.element('<div/>');

      var title = angular.element('<h3/>')
        .addClass('os-filter-title')
        .attr('translate', 'site.filter.filters')
        .append('Filters');

      var divider = angular.element('<div/>')
        .addClass('os-divider');

      div.append(title).append(divider);
      return div;
    }

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        element.addClass('os-filter-drawer')
          .prepend(getHeader());
        element.removeAttr('os-filter-drawer');

        osFilterDrawerSvc.setDrawer(element);
      }
    };
  })
  .directive('osSearchButton', function(osFilterDrawerSvc) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        element.on('click', function() {
          osFilterDrawerSvc.toggle();
        });
      }
    };
  })
  .factory('osFilterDrawerSvc', function() {
    var drawerEl = undefined;
    return {
      setDrawer: function(drawer) {
        drawerEl = drawer;
      },

      toggle: function() {
        drawerEl.toggleClass('active');

        var cardsDiv = drawerEl.parent().find("div.os-cards");
        if (drawerEl.hasClass('active')) {
          cardsDiv.css("width", "75%");
        } else {
          cardsDiv.css("width", "100%");
        }
      }
    }
  });
