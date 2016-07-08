
angular.module('os.administrative.setting.list', ['os.administrative.models'])
  .controller('SettingsListCtrl', function(
    $scope, $state, $stateParams, $sce, Setting, SettingUtil, Alerts) {

    function init() {
      $scope.isEdit = false;
      $scope.fileCtx = {
        ctrl: {},
        uploadUrl: $sce.trustAsResourceUrl(Setting.getFileUploadUrl())
      };

      var moduleName = $stateParams.moduleName;
      if (!moduleName) {
        $state.go('settings-list', {moduleName: $scope.modules[0].name});
        return;
      } 

      $scope.selectedModule = $scope.modulesMap[moduleName];
    }

    function saveSetting() {
      Setting.updateSetting($scope.setting).then(
        function(resp) {
          Alerts.success('settings.success_message');
          angular.extend($scope.existingSetting, resp);
          $scope.isEdit = false;
          SettingUtil.clearSetting(resp.module, resp.name);
        }
      );
    }
    
    $scope.updateSetting = function(setting) {
      $scope.isEdit = true;
      $scope.existingSetting = setting;
      $scope.setting = angular.copy(setting);
      $scope.setting.value = '';
    }
    
    $scope.cancel = function() {
      $scope.isEdit = false;
    }
    
    $scope.submit = function() {
      var type = $scope.setting.type;
      if (type != 'FILE' && $scope.existingSetting.value == $scope.setting.value) {
        Alerts.error('settings.invalid_value');
        return;
      } 

      if (type == 'FILE') {
        $scope.fileCtx.ctrl.submit().then(
          function(filename) {
            $scope.setting.value = filename;
            saveSetting(); 
          }
        );
      } else {
        saveSetting();
      }
    }

    init();
  });
