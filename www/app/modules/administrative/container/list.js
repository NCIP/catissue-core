angular.module('os.administrative.container.list', ['os.administrative.models'])
  .controller('ContainerListCtrl', function($scope, $state, Container, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainersCount});
      $scope.containerFilterOpts = {
        maxResults: pagerOpts.recordsPerPage + 1,
        includeStats: true,
        topLevelContainers: true
      };

      loadContainers($scope.containerFilterOpts);
      Util.filter($scope, 'containerFilterOpts', loadContainers);
    }

    function loadContainers(filterOpts) {
      Container.list(filterOpts).then(
        function(containers) {
          angular.forEach(containers,
            function(container) {
              if (container.capacity) {
                container.utilisation = Math.round(container.storedSpecimens / container.capacity * 100);
              }
            }
          );

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
