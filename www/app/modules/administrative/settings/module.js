angular.module('os.administrative.setting', 
  [ 
    'os.administrative.setting.list',
    'os.administrative.setting.util'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('settings', {
        url: '/settings',
        controller: function($scope, $translate, settings, areTranslationsLoaded) {
          $scope.ctx = {
            dbModules: {},
            dbModuleNames: [],
            filteredModules: {},
            filteredModuleNames: [],
            search: ''
          };

          var dbModules = {}, dbModuleNames = [];
          angular.forEach(settings, 
            function(setting) {
              var module = dbModules[setting.module];
              if (!module) {
                module = {name: setting.module, settings: []};
                dbModules[setting.module] = module;
                dbModuleNames.push(setting.module);
              }

              module.settings.push(setting);

              var pnKey = 'settings.' + setting.module + '.' + setting.name;
              setting.$$osPropName = $translate.instant(pnKey);
              setting.$$osPropDesc = $translate.instant(pnKey + '_desc');
            }
          );

          $scope.ctx.dbModules     = $scope.ctx.filteredModules     = dbModules;
          $scope.ctx.dbModuleNames = $scope.ctx.filteredModuleNames = dbModuleNames;
        },
        template: '<div ui-view></div>',
        resolve: {
          settings: function(isAdmin, Setting) {
            return Setting.query();
          },

          areTranslationsLoaded: function($translate) {
            return $translate('common.none').then(
              function() {
                return true;
              }
            );
          }
        },
        parent: 'admin-view',
        abstract: true
      })
      .state('settings-list', {
        url: '/settings-list?moduleName',
        templateUrl: 'modules/administrative/settings/list.html',
        controller: 'SettingsListCtrl',
        parent: 'settings'
      });
  });
