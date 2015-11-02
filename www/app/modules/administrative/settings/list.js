
angular.module('os.administrative.setting.list', ['os.administrative.models'])
  .controller('SettingsListCtrl', function($scope, $state, $stateParams, $translate, Setting, Alerts) {

    function init() {
      $scope.isEdit = false;
      var moduleName = $stateParams.moduleName;
      if (!moduleName) {
        $state.go('settings-list', {moduleName: $scope.modules[0].name});
        return;
      } 

      $scope.selectedModule = $scope.modulesMap[moduleName];
    }
    
    init();
    
    $scope.updateSetting = function(setting) {
      $scope.isEdit = true;
      $scope.setting = angular.copy(setting);
    }
    
    $scope.cancel = function() {
      $scope.isEdit = false;
    }
    
    $scope.submit = function() {
      Setting.updateSetting($scope.setting).then(
        function(resp) {
          Alerts.success($translate.instant('settings.success_message'));
          angular.forEach($scope.modulesMap[resp.module].settings, function(setting) {
            if(setting.name == resp.name) {
              setting.value = resp.value;
            }
          });
          $scope.isEdit = false;
        }
      )
    }
  });
