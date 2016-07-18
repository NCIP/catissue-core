angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Site, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainersCount});
      $scope.containerFilterOpts = {topLevelContainers: true, maxResults: pagerOpts.recordsPerPage + 1};
      loadContainers($scope.containerFilterOpts);
      loadSites();
      Util.filter($scope, 'containerFilterOpts', loadContainers);
    }

    function loadContainers(filterOpts) {
      Container.list(filterOpts).then(
        function(containers) {
          $scope.containerList = containers;
          pagerOpts.refreshOpts(containers);
        }
      );
    }

    function loadSites() {
      $scope.sites = [];
      Site.listForContainers().then(function(sites) {
        $scope.sites = sites;
      });
    }

    function getContainersCount() {
      return Container.getCount($scope.containerFilterOpts);
    }

    $scope.showContainerOverview = function(container) {
      $state.go('container-detail.overview', {containerId: container.id});
    };

    init();
  });
