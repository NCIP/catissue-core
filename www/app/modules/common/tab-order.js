angular.module('openspecimen')
  .directive('osTabOrder', function($timeout) {
    function addTabIndices(element) {
      var selectors = [
        'input[os-tabable="true"]',
        '[os-tabable="true"] input',
        'button[os-tabable="true"]'
      ];

      var idx = 1;
      var tabableEls = element.find(selectors.join());
      angular.forEach(tabableEls, function(el) {
        angular.element(el).attr('tabindex', idx++);
      });
    }

    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        scope.$watch(attrs.osTabOrder, function() {
          $timeout(function() { addTabIndices(element); });
        }); 
      }
    };
  });
