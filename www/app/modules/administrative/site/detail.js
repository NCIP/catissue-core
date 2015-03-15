angular.module('os.administrative.site.detail', ['os.administrative.models'])
  .controller('SiteDetailCtrl', function($scope, $q, $state, $modal, site, Site, PvManager) {

    function init() {
      $scope.site = site;
      $scope.siteTypes = PvManager.getPvs('site-type');
    }

    $scope.editSite = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteSite = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/administrative/site/delete.html',
        controller: 'SiteDeleteCtrl',
        resolve: {
          site: function() {
            return $scope.site;
          },
          siteDependencies: function() {
            return Site.getDependencies($scope.site.id);
          }
        }
      });

      modalInstance.result.then(function (site) {
        $state.go('site-list');
      });
    }

    $scope.getCoordinatorDisplayText = function(coordinator) {
      return coordinator.lastName + ' ' + coordinator.firstName;
    }

    init();
  });
