angular.module('openspecimen')
  .directive('osShowIfMenuItemsPresent', function($timeout) {
    function isElementDisplayed(item) {
      return !(item.style.display == 'none' || item.style.visibility == 'hidden' || parseFloat(item.style.opacity) <= 0);
    }

    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(
          function() {
            return element.find("ul.dropdown-menu > li").
	      filter(function() {
                return isElementDisplayed(this);
              }).length;
          }, function(val) {
            if (val == 0) {
              element.hide();
            } else {
              element.show();
            }
          }
        );
      }
    }
  });
