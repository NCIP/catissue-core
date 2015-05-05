
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, visits, Visit, AuthorizationService) {
    if (!$scope.cpr.participant.phiAccess) {
      $scope.cpr.participant.birthDate = "###";
    }

    $scope.occurredVisits    = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
