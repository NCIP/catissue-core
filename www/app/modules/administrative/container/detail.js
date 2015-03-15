angular.module('os.administrative.container.detail', ['os.administrative.models'])
  .controller('ContainerDetailCtrl', function($scope, $q, $state, $modal, container, Site, Container, CollectionProtocol, PvManager) {

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

    $scope.deleteContainer = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/administrative/container/delete.html',
        controller: 'ContainerDeleteCtrl',
        resolve: {
          container: function () {
            return $scope.container;
          },
          containerDependencies: function() {
            return Container.getDependencies($scope.container.id);
          }
        }
      });

      modalInstance.result.then(function () {
        $state.go('container-list');
      });
    }

    init();
  });
