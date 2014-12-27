
angular.module('os.biospecimen.cp.specimens', ['os.biospecimen.models'])
  .controller('CpSpecimensCtrl', function($scope, $state, $stateParams, cp, events) {
    $scope.cp = cp;
    $scope.events = events;

    if (!$stateParams.eventId && !!events && events.length > 0) {
      $state.go('cp-detail.specimen-requirements', {eventId: events[0].id});
      return;
    }

    $scope.eventId = $stateParams.eventId;
  });
