
angular.module('openspecimen')
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, visits, CprService, PvManager) {
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

    $scope.occurredVisits = [];
    $scope.anticipatedVisits = [];

    angular.forEach(visits, function(visit) {
      if (visit.status === 'Complete') {
        $scope.occurredVisits.push(visit);
      } else if (visit.status === 'Pending' || !visit.status) {
        $scope.anticipatedVisits.push(visit);
      }
    });
  });
