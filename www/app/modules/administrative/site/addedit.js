angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function($scope, $state, site, customForm, Institute, PvManager) {

    function init() {
      $scope.site = site;
      $scope.customForm = customForm;
      loadPvs();
    }

    function loadPvs() {
      $scope.siteTypes = PvManager.getPvs('site-type');

      $scope.institutes = [];
      Institute.query().then(
        function(instituteList) {
          angular.forEach(instituteList, function(institute) {
            $scope.institutes.push(institute.name);
          });
        }
      );
    }

    $scope.save = function() {
      var site = angular.copy($scope.site);
      site.customForm = $scope.getCustomFormData();
 
      site.$saveOrUpdate().then(
        function(savedSite) {
          $state.go('site-detail.overview', {siteId: savedSite.id});
        }
      );
    }

    init();
  });
