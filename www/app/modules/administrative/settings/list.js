
angular.module('os.administrative.setting.list', ['os.administrative.models'])
  .controller('SettingsListCtrl', function(
    $scope, $state, $stateParams, Setting, SettingUtil, Alerts) {

    function init() {
      $scope.isEdit = false;
      var moduleName = $stateParams.moduleName;
      if (!moduleName) {
        $state.go('settings-list', {moduleName: $scope.modules[0].name});
        return;
      } 

      $scope.selectedModule = $scope.modulesMap[moduleName];
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
      if($scope.existingSetting.value == $scope.setting.value) {
        Alerts.error('settings.invalid_value');
        return;
      } 

      Setting.updateSetting($scope.setting).then(
        function(resp) {
          Alerts.success('settings.success_message');
          angular.extend($scope.existingSetting, resp);
          $scope.isEdit = false;
          SettingUtil.clearSetting(resp.module, resp.name);
        }
      );
    }

    init();
  });
