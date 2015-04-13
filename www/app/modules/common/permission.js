angular.module('openspecimen')
  .directive('hasPermission', function(AuthorizationService) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var showEle = false;
        var attributes = attrs.hasPermission.split(":");
        var resource = attributes[0];
        var permission = attributes[1];
        var cp = attributes[2];
        var site = attributes[3];
        if (AuthorizationService.hasPermission(resource, permission, site, cp)) {
          showEle = true;
        }

        if (showEle) {
          element.show();
        } else {
          element.hide();
        }
      }
    };
  })