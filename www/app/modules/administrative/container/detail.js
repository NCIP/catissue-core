angular.module('os.administrative.container.detail', ['os.administrative.models'])
  .controller('ContainerDetailCtrl', function($scope, $q, container, Site, CollectionProtocol, PvManager) {

    function init() {
      $scope.container = container;
      loadPvs();
    }

    function loadPvs () {
      $scope.dimensionLabelSchemes = PvManager.getPvs('dimension-label-scheme');
      $scope.sites = PvManager.getSites();

      CollectionProtocol.query().then(
        function(cps) {
          $scope.cps = [];
          angular.forEach(cps, function(cp) {
            $scope.cps.push(cp.shortTitle);
          });
        }
      );
    }

    $scope.editContainer = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    init();
  });