
angular.module('openspecimen')
  .factory('AlertService', function($timeout) {
    var alertSvc = undefined;
    alertSvc = {
      display: function(scope, msg, type, timeout) {
        scope.alert = {
          msg: msg, 
          type: type,
          close: function() {
            return alertSvc.closeAlert(this);
          }
        };

        if (timeout === false) {
          return;
        }

        var that = scope.alert;
        if (timeout === undefined || timeout === null) {
          timeout = 5000;
        }

        $timeout(function() { alertSvc.closeAlert(scope, that); }, timeout);
      },

      closeAlert: function(scope, alert) {
        if (scope.alert == alert) {
          scope.alert = undefined;
        }
      },

      clear: function(scope) {
        scope.alert = undefined;
      }
    };

    return alertSvc;
  });
