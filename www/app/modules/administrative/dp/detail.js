
angular.module('os.administrative.dp.detail', ['os.administrative.models'])
  .controller('DpDetailCtrl', function($scope, $q, distributionProtocol, DeleteUtil) {
    $scope.distributionProtocol = distributionProtocol;
    
    $scope.editDp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteDp = function() {
      DeleteUtil.delete($scope.distributionProtocol, {
        onDeleteState: 'dp-list',
        entityNameProp: $scope.distributionProtocol.title,
        entityTypeProp: 'entity_name.dp'
      });
    }

  });
