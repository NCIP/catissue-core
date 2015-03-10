angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container) {

    function init() {
      $scope.containerFilterOpts = {};
      loadContainers();
    }

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

    $scope.filter = function(containerFilterOpts) {
      Container.list(containerFilterOpts).then(
        function(containers) {
          $scope.containerList = containers;
        }
      );
    }

    init();
  });