angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container) {

    var loadContainers = function() {
      Container.list().then(
        function(containers) {
          $scope.containerList = containers;
        }
      );
    }

    $scope.showContainerOverview = function(container) {
      $state.go('container-detail.overview', {containerId: container.id});
    };

    loadContainers();
  });