angular.module('os.administrative.containertype.list', ['os.administrative.models'])
  .controller('ContainerTypeListCtrl', function($scope, $state, ContainerType, Util, ListPagerOpts) {

    var pagerOpts;

    function init() {
      $scope.containerTypeFilterOpts = {};
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getContainerTypesCount});
      loadContainerTypes($scope.containerTypeFilterOpts);
      Util.filter($scope, 'containerTypeFilterOpts', loadContainerTypes);
    }

    function loadContainerTypes(filterOpts) {
      ContainerType.query(filterOpts).then(
        function(containerTypes) {
          $scope.containerTypes = containerTypes;
          pagerOpts.refreshOpts(containerTypes);
          if (Object.keys(filterOpts).length == 0) {
            $scope.canHolds = angular.copy(containerTypes);
          }
        }
      );
    };

    function getContainerTypesCount() {
      return ContainerType.getCount($scope.containerTypeFilterOpts);
    }

    $scope.showContainerTypeOverview = function(containerType) {
      $state.go('container-type-detail.overview', {containerTypeId: containerType.id});
    };

    init();
  });

