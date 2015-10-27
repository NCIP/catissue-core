
angular.module('os.administrative.setting.configuration', ['os.administrative.models'])
  .controller('ConfigurationCtrl', function($scope, Setting) {
  
    var loadConfigurations = function() {
      Setting.query().then(
        function(configList) {
          $scope.configList = configList;
        }
      );
    }
    
    loadConfigurations();
  });
