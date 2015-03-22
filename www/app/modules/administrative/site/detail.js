angular.module('os.administrative.site.detail', ['os.administrative.models'])
  .controller('SiteDetailCtrl', function($scope, $q, $state, $modal, site, Site, Institute, PvManager) {

    function init() {
      $scope.site = site;
      $scope.institutes = [];
      loadPvs();
    }

    function loadPvs() {
      $scope.siteTypes = PvManager.getPvs('site-type');

      Institute.query().then(
        function(instituteList) {
          angular.forEach(instituteList, function(institute) {
            $scope.institutes.push(institute.name);
          });
        }
      );
    }

    $scope.editSite = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteSite = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/common/delete/delete-entity-template.html',
        controller: 'entityDeleteCtrl',
        resolve: {
          entityProps: function() {
            return {
              entity: $scope.site,
              name: $scope.site.name,
            }
          },
          entityDependencyStat: function() {
            return Site.getDependencyStat($scope.site.id);
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
