
angular.module('openspecimen')
  .controller('ParticipantAddEditCtrl', function($scope, $stateParams, SiteService, PvManager) {
    $scope.cpId = $stateParams.cpId;

    $scope.participant = {
      birthDate: '',
      gender: '',
      pmis: []
    };

    $scope.addPmi = function() {
      $scope.participant.pmis.push({mrn: '', site: ''});
    };

    $scope.removePmi = function(index) {
      $scope.participant.pmis.splice(index, 1);
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
  });
