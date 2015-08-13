
angular.module('os.administrative.dp.close', [])
.controller('DpCloseCtrl', function ($scope, $modalInstance, distributionProtocol) {
  $scope.distributionProtocol = distributionProtocol;
  
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  }
  
  $scope.close = function () {
    distributionProtocol.close().then(
      function (result) {
        $modalInstance.close(result);
      }
    );
  }
});
