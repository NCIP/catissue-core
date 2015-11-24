
angular.module('os.biospecimen.cp.catalog', ['os.biospecimen.models', 'os.query.models.savedquery'])
  .controller('CpCatalogSettingsCtrl', function($scope, cp, SavedQuery) {
    function init() {
      $scope.catSettingsCtx = {};
      setViewCtx();
    }

    function setViewCtx() {
      var ctx = {view: 'view_settings', setting: cp.catalogSetting};
      angular.extend($scope.catSettingsCtx, ctx);
    }

    function setEditCtx() {
      var ctx = {view: 'edit_settings', setting: angular.copy(cp.catalogSetting)};
      angular.extend($scope.catSettingsCtx, ctx);
    }

    $scope.showEditForm = function() {
      setEditCtx();

      if (!$scope.catSettingsCtx.queryList) {
        $scope.catSettingsCtx.queryList = SavedQuery.list();
      }
    }

    $scope.revertEdit = function() {
      setViewCtx();
    }

    $scope.save = function() {
      cp.saveCatalogSetting($scope.catSettingsCtx.setting).then(
        function(setting) {
          angular.extend(cp.catalogSetting, setting);
          setViewCtx();
        }
      );
    }

    $scope.delete = function() {
      cp.deleteCatalogSetting().then(
        function() {
          cp.catalogSetting = {};
          setViewCtx();
        }
      );
    }

    init();
  });
