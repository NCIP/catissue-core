angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainersCount});
      $scope.containerFilterOpts = {topLevelContainers: true, maxResults: pagerOpts.recordsPerPage + 1};
      loadContainers($scope.containerFilterOpts);
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

    function getContainersCount() {
      return Container.getCount($scope.containerFilterOpts);
    }

    $scope.showContainerDetail = function(container) {
      $state.go('container-detail.locations', {containerId: container.id});
    };

    init();
  });
