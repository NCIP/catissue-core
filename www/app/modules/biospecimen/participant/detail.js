
angular.module('openspecimen')
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, visits, Visit, PvManager) {
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

    $scope.occurredVisits = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
