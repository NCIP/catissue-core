angular.module('os.administrative.setting', 
  [ 
    'os.administrative.setting.list'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('settings', {
        url: '/settings',
        controller: function($scope, settings) {
          $scope.modulesMap = {};
          $scope.modules = [];
          angular.forEach(settings, 
            function(setting) {
              var module = $scope.modulesMap[setting.module];
              if (!module) {
                module = {name: setting.module, settings: []};
                $scope.modulesMap[setting.module] = module;
                $scope.modules.push(module);
              }

              module.settings.push(setting);
            }
          );
        },
        template: '<div ui-view></div>',
        resolve: {
          settings: function(Setting) {
            return Setting.query();
          }
        },
        parent: 'signed-in',
        abstract: true
      })
      .state('settings-list', {
        url: '/settings-list?moduleName',
        templateUrl: 'modules/administrative/settings/list.html',
        controller: 'SettingsListCtrl',
        parent: 'settings'
      });
  });
