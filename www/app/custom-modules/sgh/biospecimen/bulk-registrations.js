angular.module('openspecimen')
  .controller('sgh.CpBulkRegistrationsCtrl', function(
    $scope, $http, cp, ApiUrls, Alerts) {
    var baseUrl = ApiUrls.getBaseUrl();
    
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
          Alerts.success("custom_sgh.participant_registered", {$scope.regReq});
        }
      );
    }
    init();
  });
