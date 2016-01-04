angular.module('openspecimen')
  .directive('showIfAllowed', function(AuthorizationService) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.showIfAllowed, function(newOpts) {
          if (!AuthorizationService.isAllowed(newOpts)) {
            element.remove();
          }
        });
      }
    };
  })

  .directive('showIfAdmin', function($rootScope) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        if ($rootScope.currentUser.admin) {
          element.show();
        } else {
          element.remove();
        }
      }
    }
  });
