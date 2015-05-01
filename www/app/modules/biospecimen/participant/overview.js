
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, visits, Visit, AuthorizationService) {
    var opts = {cp: $scope.cpr.cpShortTitle};
    angular.extend($scope.visitAndSpecimenResource.createOpts, opts);
    angular.extend($scope.participantResource.readOpts, opts);
    if (!AuthorizationService.isAllowed($scope.participantResource.readOpts)) {
      $scope.cpr.participant.birthDate = "###";
    }

    $scope.occurredVisits    = Visit.completedVisits(visits);
    $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
  });
