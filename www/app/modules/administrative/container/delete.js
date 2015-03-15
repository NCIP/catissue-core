angular.module('os.administrative.container.delete', ['os.administrative.models'])
  .controller('ContainerDeleteCtrl', function($scope, $modalInstance, $translate, container, containerDependencies, Alerts) {

    var init = function() {
      $scope.container = container;
      $scope.containerDependencies = $.isEmptyObject(containerDependencies) ? undefined : containerDependencies;
    }

    var onDeleted = function(container) {
      if (!!container) {
        $translate('container.container_deleted', {name: container.name}).then(function(msg) {
          Alerts.success(msg);
        })

        $modalInstance.close(container);
      }
    }

    $scope.delete = function () {
      $scope.container.$remove().then(onDeleted)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })

