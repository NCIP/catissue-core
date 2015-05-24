angular.module('openspecimen')
  .controller('sgh.CpBulkRegistrationsCtrl', function(
    $scope, $http, $translate, $translatePartialLoader, 
    cp, ApiUrls, Alerts) {
    var baseUrl = ApiUrls.getBaseUrl();
    $translatePartialLoader.addPart('custom-modules/sgh');
    $translate.refresh();
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
          Alerts.success("custom_sgh.participant_registered", {participantCount:result.data.participantCount});
          $state.go('participant-list', {cpId: $scope.regReq.cpId});  
        }
      );
    }
    init();
  });
