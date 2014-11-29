
angular.module('openspecimen')
  .controller('ParticipantListCtrl', function($scope, $state, $stateParams, $modal, AlertService, CollectionProtocolService) {
    $scope.cpId = $stateParams.cpId;

    var loadParticipants = function() {
      CollectionProtocolService.getRegisteredParticipants($scope.cpId, true).then(
        function(result) {
          if (result.status == "ok") {
            $scope.cprList = result.data;
          } else {
            AlertService.display($scope, 'Error loading participants', 'danger');
          }
        }
      );
    };

    $scope.registerParticipant = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/participant/addedit.html',
        controller: 'ParticipantAddEditCtrl',
        resolve: {
          cpId: $scope.cpId
        },
        windowClass: 'os-modal-800'
      });

      modalInstance.result.then(
        function(result) {
          AlertService.display($scope, 'Participant Registered', 'success');
          $state.go('participant-detail.overview', {cprId: result.id});
        }
      );
    };

    loadParticipants();
  });
