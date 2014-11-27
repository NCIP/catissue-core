
angular.module('openspecimen')
  .controller('CpListCtrl', function($scope, $state, $modal, AlertService, CollectionProtocolService) {
    var loadCollectionProtocols = function() {
      CollectionProtocolService.getCpList(true).then(
        function(result) {
          if (result.status == 'ok') {
            $scope.cpList = result.data;
          } else {
            AlertService.display($scope, 'Failed to load cp list', 'danger');
          }
        }
      );
    }

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

      modalInstance.result.then(
        function(result) {
          AlertService.display($scope, 'Collection Protocol Created Successfully', 'success');
          loadCollectionProtocols();
        }
      );
    }

    loadCollectionProtocols();
  });
