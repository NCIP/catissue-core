
angular.module('openspecimen')
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, CprService, PvManager) {
    $scope.cpr = cpr;

    PvManager.loadPvs($scope, 'gender');
    PvManager.loadPvs($scope, 'ethnicity');
    PvManager.loadPvs($scope, 'vitalStatus');
    PvManager.loadPvs($scope, 'race');

    $scope.editCpr = function(property, value) {
      //alert("Edit " + cpr.id + " for " + property + " with " + value); 
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    CprService.getVisits(cpr.id).then(
      function(result) {
        $scope.visits = result.data;
      }
    );
  });
