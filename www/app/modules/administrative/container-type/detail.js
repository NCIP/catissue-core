angular.module('os.administrative.containertype.detail', ['os.administrative.models'])
  .controller('ContainerTypeDetailCtrl', function($scope, containerType, DeleteUtil) {
    $scope.containerType = containerType;

    $scope.deleteContainerType = function() {
      DeleteUtil.delete($scope.containerType, {onDeleteState: 'container-type-list'});
    }

  });
