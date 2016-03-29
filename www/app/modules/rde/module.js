
angular.module('os.rde', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('rde', {
        url: '/rde',
        templateUrl: 'modules/rde/main.html',
        controller: 'rdeCollectSpecimensCtrl',
        parent: 'signed-in'
      })
      .state('cp-detail.bde-config', {
        url: '/rde-config',
        templateUrl: 'modules/rde/cp-cfg.html',
        controller: 'rdeConfigCtrl', 
        parent: 'cp-detail'
      });
  });
