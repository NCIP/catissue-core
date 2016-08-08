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
        var user = $rootScope.currentUser;
        if (user.admin || (attrs.showIfAdmin == 'institute' && user.instituteAdmin)) {
          element.show();
        } else {
          element.remove();
        }
      }
    }
  });
