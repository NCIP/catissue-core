
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
        templateUrl: 'modules/common/delete/delete-entity-template.html',
        controller: 'entityDeleteCtrl',
        resolve: {
          entityProps: function() {
            return {
              entity: $scope.distributionProtocol,
              name: $scope.distributionProtocol.name,
            }
          },
          entityDependencyStat: function() {
            return DistributionProtocol.getDependencyStat($scope.distributionProtocol.id);
          }
        }
      });

      modalInstance.result.then(function (distributionProtocol) {
        $state.go('dp-list');
      });
    }

  });
