
angular.module('os.administrative.setting.settings', ['os.administrative.models'])
  .controller('SettingsCtrl', function($scope, $modal, $translate, Setting, Alerts) {
  
    function loadSettings() {
      Setting.query().then(
        function(settings) {
          $scope.settings = settings;
        }
      );
    }
    
    function updateSetting(setting) {
      Setting.updateSetting(setting).then(
        function(resp) {
          Alerts.success($translate.instant('setting.success_message'));
          loadSettings();
        }
      )
    }
    
    loadSettings();
    
    $scope.editSetting = function(setting) {
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
    }
  });
