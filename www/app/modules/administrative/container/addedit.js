angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function($scope, $state, Site, container, Container, CollectionProtocol, PvManager){

    var init = function() {
      $scope.container = container;
      $scope.cps = [];
      loadPvs();
    }

    var loadPvs = function() {
      $scope.dimensionLabelSchemes = PvManager.getPvs('dimension-label-scheme');
      $scope.sites = PvManager.getSites();

      CollectionProtocol.query().then(
        function(cps) {
          angular.forEach(cps, function(cp) {
            $scope.cps.push(cp.shortTitle);
          });
        }
      );
    }

    $scope.loadContainers = function(siteName) {
      Container.listForSite(siteName).then(function(containerList) {
        $scope.containers = containerList;
      });
    };

    $scope.save = function() {
      var container = angular.copy($scope.container);
      container.createdBy = {loginName: 'admin@admin.com', domainName:'catissue'}; // TODO: Handle this in server side.
      container.$saveOrUpdate().then(
        function(savedContainer) {
          $state.go('container-detail.overview', {containerId: savedContainer.id});
        }
      );
    };

    init();

  })
