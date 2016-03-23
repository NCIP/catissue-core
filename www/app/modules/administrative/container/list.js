angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Site, Util) {

    function init() {
      $scope.containerFilterOpts = {};
      loadContainers();
      loadSites();
      Util.filter($scope, 'containerFilterOpts', loadContainers);
    }

    function loadContainers(filterOpts) {
      Container.list(filterOpts).then(
        function(containers) {
          $scope.containerList = containers;
        }
      );
    }

    function loadSites() {
      $scope.sites = [];
      Site.listForContainers().then(function(sites) {
        $scope.sites = sites;
      });
    }

    $scope.showContainerOverview = function(container) {
      $state.go('container-detail.overview', {containerId: container.id});
    };

    init();
  });
