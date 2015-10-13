angular.module('openspecimen')
  .directive('osShowIfMenuItemsPresent', function($timeout) {
    function isElementDisplayed(item) {
      return !(item.style.display == 'none' || item.style.visibility == 'hidden' || parseFloat(item.style.opacity) <= 0);
    }

    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(function() {
            return element[0].innerHTML;
          }, function(val) {
            var menuItems = element.find("ul.dropdown-menu li");
            var displayMenu = false;
            for (var i = 0; i < menuItems.length; i++) {
              if (isElementDisplayed(menuItems[i])) {
                displayMenu = true;
                break;
              }
            }

            if (!displayMenu) {
              element.hide();
            } else {
              element.show();
            }
          }
        );
      }
    }
  });
