
angular.module('os.administrative.setting.configuration', ['os.administrative.models'])
  .controller('ConfigurationCtrl', function($scope, $modal, $translate, Setting, Alerts) {
  
    var loadConfigurations = function() {
      Setting.query().then(
        function(configList) {
          $scope.configList = configList;
        }
      );
    }
    
    var updateConfig = function(config) {
      Setting.updateProperty(config).then(
        function(resp) {
          Alerts.success($translate.instant('configuration.success_message'));
          loadConfigurations();
        }
      )
    }
    
    loadConfigurations();
    
    $scope.editProperty = function(config) {
      var modalInstance =  $modal.open({
        templateUrl: 'modules/administrative/settings/editConfiguration.html',
        controller: function($scope, $modalInstance) {
          $scope.config = angular.copy(config);
          
          $scope.submit = function() {
            $modalInstance.close($scope.config);
          }
          
          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      });
      
      modalInstance.result.then(
        function(config) {
          updateConfig(config);
        }
      );
    }
  });
