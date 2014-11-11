
angular.module('openspecimen')
  .controller('ParticipantAddEditCtrl', function($scope, $stateParams, SiteService, PvManager) {
    $scope.cpId = $stateParams.cpId;

    $scope.cpr = {
      participant: {
        pmis: []
      }
    };

    $scope.addPmi = function() {
      $scope.cpr.participant.pmis.push({mrn: '', site: ''});
    };

    $scope.removePmi = function(index) {
      $scope.cpr.participant.pmis.splice(index, 1);
    };

    SiteService.getSites().then(
      function(result) {
        if (result.status != "ok") {
          alert("Failed to load sites information");
        }
        $scope.sites = result.data;
      }
    );

    PvManager.loadPvs($scope, 'gender');
    PvManager.loadPvs($scope, 'ethnicity');
    PvManager.loadPvs($scope, 'vitalStatus');
    PvManager.loadPvs($scope, 'race');

    $scope.register = function() {
      alert(JSON.stringify($scope.cpr));
    }
  });
