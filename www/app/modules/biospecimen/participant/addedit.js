
angular.module('openspecimen')
  .controller('ParticipantAddEditCtrl', function($scope, $stateParams) {
    $scope.cpId = $stateParams.cpId;
  });
