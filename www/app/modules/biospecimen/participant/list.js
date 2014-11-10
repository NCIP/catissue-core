
angular.module('openspecimen')
  .controller('ParticipantListCtrl', function($scope, $stateParams, $modal, CollectionProtocolService) {
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

    $scope.registerParticipant = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/participant/addedit.html',
        controller: 'ParticipantAddEditCtrl',
        resolve: {
          cpId: $scope.cpId
        },
        windowClass: 'os-modal-800'
      });
    };
  });
