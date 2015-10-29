angular.module('os.administrative.setting', 
  [ 
    'ui.router',
    'os.administrative.setting.settings'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('settings', {
        url: '/settings',
        templateUrl: 'modules/administrative/settings/settings.html',
        controller: 'SettingsCtrl',
        parent: 'signed-in'
      });
  });
