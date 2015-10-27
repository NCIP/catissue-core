angular.module('os.administrative.setting', 
  [ 
    'ui.router',
    'os.administrative.setting.configuration'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('configuration', {
        url: '/configuration',
        templateUrl: 'modules/administrative/settings/configurations.html',
        controller: 'ConfigurationCtrl',
        parent: 'signed-in'
      });
  });
