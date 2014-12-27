
angular.module('os.biospecimen.cp.events', ['os.biospecimen.models'])
  .controller('CpEventsCtrl', function($scope, $state, $stateParams, cp, events) {
    $scope.cp = cp;
    $scope.events = events;
  });
