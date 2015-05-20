angular.module('openspecimen')

  .controller('sgh.CpBulkRegistrationsCtrl', function(
     $scope, $http,
     cp, ApiUrls, Alerts) {
    var baseUrl = ApiUrls.getBaseUrl();
    var pvsLoaded = false;
    var copyFrom = undefined;
    
    function init() {
      $scope.cp = cp;
      $scope.regReq = {
        cpId: cp.id,
        participantCount: 0,
        printLabels: false
      }
    };

    $scope.bulkRegister = function() {
      $http.post(baseUrl + 'sgh/registrations', $scope.regReq).then(
        function(result) {
          Alerts.success("custom_sgh.participant_registered", {participantCount: $scope.regReq.paticipantCount});
        }
      );
    }
    init();
  });
