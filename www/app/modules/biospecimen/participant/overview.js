
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, visits, Visit) {
    $scope.occurredVisits    = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
