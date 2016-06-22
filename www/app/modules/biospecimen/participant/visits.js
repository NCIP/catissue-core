
angular.module('os.biospecimen.participant.visits', ['os.biospecimen.models'])
  .controller('ParticipantVisitsTreeCtrl', function(
    $scope, $state, $stateParams, $timeout,
    cpr, visits, specimens, 
    Specimen) {

    if (!$stateParams.visitId && !$stateParams.eventId && visits && visits.length > 0) {
      $state.go(
        'participant-detail.visits', 
        {visitId: visits[0].id, eventId: visits[0].eventId}
      );
      return;
    }

    $scope.cpr = cpr;
    $scope.visits = visits;
    $scope.errorCode = '';
    $scope.visit = {};
    for (var i = 0; i < visits.length; ++i) {
      if ((!!$stateParams.visitId && $stateParams.visitId == visits[i].id) ||
          (!$stateParams.visitId && !!$stateParams.eventId && $stateParams.eventId == visits[i].eventId)) {
        $scope.visit = visits[i];
        break;
      } 
    }

    $scope.showVisitDetail = function(event, visitId, eventId) {
      event.preventDefault();
      event.stopPropagation();
      $state.go('visit-detail.overview', {visitId: visitId, eventId: eventId});
    };

    $scope.reload = function() {
      var visitDetail = {
        visitId: $stateParams.visitId,
        eventId: $stateParams.eventId
      };

      return Specimen.listFor($stateParams.cprId, visitDetail).then(
        function(specimens) {
          $scope.specimens = specimens;
        }
      );
    }

    $scope.specimens = specimens;
  });
