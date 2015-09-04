angular.module('os.administrative.site.addedit', ['os.administrative.models'])
  .controller('SiteAddEditCtrl', function($scope, $state, site, extensionCtxt, Institute, PvManager) {

    function init() {
      $scope.site = site;
      $scope.deFormCtrl = {};
      loadPvs();

      if (!!extensionCtxt) {
        $scope.extnOpts = {
          formId: extensionCtxt.formId,
          recordId: !!site.id ? site.extensionDetail.id : undefined,
          formCtxtId: parseInt(extensionCtxt.formCtxtId),
          objectId: site.id,
          showActionBtns: false,
          labelAlignment: 'horizontal'
        }
      }
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
      if (!$scope.deFormCtrl.ctrl.validate()) {
        return;
      }
      var site = angular.copy($scope.site);
      site.extensionDetail = $scope.deFormCtrl.ctrl.getFormData();
      site.$saveOrUpdate().then(
        function(savedSite) {
          $state.go('site-detail.overview', {siteId: savedSite.id});
        }
      );
    }

    init();
  });
