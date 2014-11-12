
angular.module('openspecimen')
  .factory('AlertService', function($timeout) {
    return {
      display: function(scope, msg, type, fade) {
        fade = (fade === undefined || fade === null) ? 5000 : fade;
        scope.alert = {msg: msg, type: type};

        if (fade) {
          $timeout(function() {scope.alert = undefined}, fade);
        }
      }
    };
  });
