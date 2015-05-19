angular.module('openspecimen')
  .controller('sgh.CpBulkRegistrationsCtrl', function(
     $scope, $http, cp, ApiUrls, Alerts) {

    var baseUrl = ApiUrls.getBaseUrl();
    var pvsLoaded = false;
    var copyFrom = undefined;
    
    function init() {
      $scope.cp = cp;
      $scope.mode = undefined;
      $scope.sgh = {};
    };

    $scope.bulkRegister = function() {
      var req = {
        cpId: $scope.cp.id,
        participantCount: $scope.sgh.paticipantCount
      };

      $http.post(baseUrl + 'sgh/registrations', req).then(
        function(result) {
          Alerts.success("custom_sgh.participant_registered", {participantCount: $scope.sgh.paticipantCount});
        }
      );
    }
    init();
  });
