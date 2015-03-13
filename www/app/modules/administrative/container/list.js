angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container) {

    function init() {
      $scope.containerFilterOpts = {};
      loadContainers();
    }

    var loadContainers = function(filterOpts) {
      Container.list(filterOpts).then(
        function(containers) {
          $scope.containerList = containers;
        }
      );
    }

    $scope.showContainerOverview = function(container) {
      $state.go('container-detail.overview', {containerId: container.id});
    };

    $scope.filter = function() {
      loadContainers($scope.containerFilterOpts);
    }

    init();
  });