
angular.module('os.query.delete', ['os.query.models'])
  .controller('DeleteQueryConfirmCtrl', function($scope, $modalInstance, query) {
    $scope.query = query;

    $scope.ok = function() {
      query.$remove().then(
        function() {
          $modalInstance.close(true);
        }
      );
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }
  });
