
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, $state, $modal, distributionProtocol, DistributionProtocol) {
    $scope.distributionProtocol = distributionProtocol;
    
    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/administrative/dp/delete.html',
        controller: 'DpDeleteCtrl',
        resolve: {
          distributionProtocol: function() {
            return $scope.distributionProtocol;
          },
          dpDependencies: function() {
            return DistributionProtocol.getDependencies($scope.distributionProtocol.id);
          }
        }
      });

      modalInstance.result.then(function () {
        $state.go('dp-list');
      });
    }

  });
