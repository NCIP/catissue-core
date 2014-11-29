
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

    CprService.getVisits(cpr.id, true).then(
      function(result) {
        $scope.occurredVisits = [];
        $scope.anticipatedVisits = [];

        var visits = result.data;
        angular.forEach(visits, function(visit) {
          visit.pendingSpecimens = visit.anticipatedSpecimens - (visit.collectedSpecimens + visit.uncollectedSpecimens);
          visit.totalSpecimens = visit.anticipatedSpecimens + visit.unplannedSpecimens;
          if (visit.status === 'Complete') {
            $scope.occurredVisits.push(visit);
          } else if (visit.status === 'Pending' || !visit.status) {
            $scope.anticipatedVisits.push(visit);
          }
        });
      }
    );
  });
