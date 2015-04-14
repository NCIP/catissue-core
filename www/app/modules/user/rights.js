angular.module('openspecimen')
  .directive('hasRights', function(AuthorizationService) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        scope.$watch(attrs.hasRights, function(oldOpts, newOpts) {
          if (AuthorizationService.hasRights(newOpts)) {
            element.show();
          } else {
            element.hide();
          }
        });
      }
    };
  })

  .directive('isAdmin', function($rootScope) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        if ($rootScope.currentUser.admin) {
          element.show();
        } else {
          element.hide();
        }
      }
    }
  });