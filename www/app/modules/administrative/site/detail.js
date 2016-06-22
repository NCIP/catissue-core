angular.module('os.administrative.site.detail', ['os.administrative.models'])
  .controller('SiteDetailCtrl', function($scope, $q, site, Institute, DeleteUtil) {

    function init() {
      $scope.site = site;
      $scope.institutes = [];
      loadPvs();
    }

    function loadPvs() {
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
      DeleteUtil.delete($scope.site, {onDeleteState: 'site-list'});
    }

    $scope.getCoordinatorDisplayText = function(coordinator) {
      return coordinator.firstName + ' ' + coordinator.lastName;
    }

    init();
  });
