angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function(
    $scope, $state, site, extensionCtxt, currentUser, Institute, ExtensionsUtil) {

    function init() {
      $scope.site = site;
      if (!currentUser.admin && !site.id) {
        site.instituteName = currentUser.instituteName;
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(site, extensionCtxt);
      $scope.coordFilterOpts = {institute: currentUser.instituteName};
      
      if (currentUser.admin) {
        loadPvs();
      }
    }

    function loadPvs() {
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
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var site = angular.copy($scope.site);
      if (formCtrl) {
        site.extensionDetail = formCtrl.getFormData();
      }

      site.$saveOrUpdate().then(
        function(savedSite) {
          $state.go('site-detail.overview', {siteId: savedSite.id});
        }
      );
    }

    init();
  });
