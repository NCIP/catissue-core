
angular.module('os.administrative.setting.list', ['os.administrative.models'])
  .controller('SettingsListCtrl', function($scope, $state, $stateParams, $translate, Setting, Alerts) {

    function init() {
      var moduleName = $stateParams.moduleName;
      if (!moduleName) {
        $state.go('settings-list', {moduleName: $scope.modules[0].name});
        return;
      } 

      $scope.selectedModule = $scope.modulesMap[moduleName];
    }
    
    init();
    /*function updateSetting(setting) {
      Setting.updateSetting(setting).then(
        function(resp) {
          Alerts.success($translate.instant('setting.success_message'));
          loadSettings();
        }
      )
    }*/
    
    
    /*$scope.editSetting = function(setting) {
      var modalInstance =  $modal.open({
        templateUrl: 'modules/administrative/settings/editSetting.html',
        controller: function($scope, $modalInstance) {
          $scope.setting = angular.copy(setting);
          
          $scope.submit = function() {
            $modalInstance.close($scope.setting);
          }
          
          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });
      
      modalInstance.result.then(
        function(newSetting) {
          updateSetting(newSetting);
        }
      );
    } */
  });
