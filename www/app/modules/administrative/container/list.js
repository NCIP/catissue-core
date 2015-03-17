angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Util) {

    function init() {
      $scope.containerFilterOpts = {};
      loadContainers();
      Util.filter($scope, 'containerFilterOpts', filter);
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

    function filter(filterOpts) {
      loadContainers(filterOpts);
    }

    init();
  });