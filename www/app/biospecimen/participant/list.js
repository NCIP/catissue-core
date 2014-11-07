
angular.module('openspecimen')
  .controller('ParticipantListCtrl', function($scope, $stateParams, CollectionProtocolService) {
    $scope.cpId = $stateParams.cpId;
    CollectionProtocolService.getRegisteredParticipants($scope.cpId, true).then(
      function(result) {
        if (result.status == "ok") {
          $scope.cprList = result.data;
        } else {
          alert("Error loading participants");
        }
      }
    );
  });
