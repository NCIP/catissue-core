angular.module('openspecimen')
  .controller('CpBulkRegistrationsCtrl', function(
     $scope, $state, $stateParams, $modal, $http,
     cp, ApiUrls, Alerts) {

    var baseUrl = ApiUrls.getBaseUrl();
    var pvsLoaded = false;
    var copyFrom = undefined;
    
    function init() {
      $scope.cp = cp;
      $scope.mode = undefined;
      $scope.selected = {};
    };

    $scope.bulkRegister = function() {
      if (!validParticipantsInfo()) {
        return;
      }

      var req = {
        cpId: $scope.cp.id,
        participantCount: parseInt($scope.cp.paticipantCount)
      };

      $http.post(baseUrl + 'sgh/registrations', req).then(
        function(result) {
          Alerts.success("cp.participant_registered", {participantCount: $scope.cp.paticipantCount});
        }
      );
    }

    function validParticipantsInfo() {
      return true;
    }
    
    init();
  });
