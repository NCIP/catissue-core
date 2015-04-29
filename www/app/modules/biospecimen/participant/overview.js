
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, visits, Visit) {
    var opts = {cp: $scope.cpr.cpShortTitle};
    angular.extend($scope.visitAndSpecimenResource.createOpts, opts);

    $scope.occurredVisits    = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
