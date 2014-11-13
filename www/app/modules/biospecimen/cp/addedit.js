
angular.module('openspecimen')
  .controller('CollectionProtocolAddEditCtrl', function($scope, $modalInstance, AlertService, CollectionProtocolService, UserService) {

    $scope.collectionProtocol = {
      pi: '',
      coordinator: [],
      statements: []
    };

    UserService.getUsers().then(
      function(result) {
        if (result.status != 'ok') {
          AlertService.display($scope, 'Failed to load users information', 'danger');
        }
        $scope.users = result.data.users;
      }
    );

    $scope.addStatement = function() {
      $scope.collectionProtocol.statements.push({text:''});
    }

    $scope.removeStatement = function(index) {
      $scope.collectionProtocol.statements.splice(index,1);
    }

    var handelCPResult = function(result) {
      if(result.status == 'ok') {
        $modalInstance.close('ok');
      } else {
        AlertService.display($scope, 'Create Collection Protocol Failed', 'danger');
      }
    }

    $scope.save = function() {
      CollectionProtocolService.createCollectionProtocol($scope.collectionProtocol).then(handelCPResult);
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  });
