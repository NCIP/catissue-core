
angular.module('openspecimen')
  .controller('CpListCtrl', function($scope, $state, $modal, CollectionProtocolService) {
    CollectionProtocolService.getCpList(true).then(
      function(result) {
        if (result.status == "ok") {
          $scope.cpList = result.data;
        } else {
          alert("Failed to load cp list");
        }
      }
    );

    $scope.showParticipants = function(cp) {
      $state.go('participant-list', {cpId: cp.id});
    };

    $scope.addCollectionProtocol = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/cp/addedit.html',
        controller: 'CollectionProtocolAddEditCtrl',
        resolve: {
        },
        windowClass: 'os-modal-800'
      });
    }
  });
