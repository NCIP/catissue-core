
angular.module('os.biospecimen.participant.detail', ['os.biospecimen.models'])
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, visits, Visit, PvManager) {
    $scope.cpr = cpr;

    PvManager.loadPvs($scope, 'gender');
    PvManager.loadPvs($scope, 'ethnicity');
    PvManager.loadPvs($scope, 'vitalStatus');
    PvManager.loadPvs($scope, 'race');

    $scope.editCpr = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.occurredVisits = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
