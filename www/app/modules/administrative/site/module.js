
angular.module('os.administrative.site',
  [
    'ui.router',
    'os.administrative.site.list',
    'os.administrative.site.detail',
    'os.administrative.site.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('site-list', {
        url: '/sites',
        templateUrl: 'modules/administrative/site/list.html',
        controller: 'SiteListCtrl',
        parent: 'signed-in'
      })
      .state('site-addedit', {
        url: '/site-addedit/:siteId',
        templateUrl: 'modules/administrative/site/addedit.html',
        resolve: {
          site: function($stateParams, Site) {
            if ($stateParams.siteId) {
              return Site.getById($stateParams.siteId);
            }
            return new Site();
          },
          extensionCtxt: function(Site) {
            return Site.getExtensionCtxt();
          }
        },
        controller: 'SiteAddEditCtrl',
        parent: 'signed-in'
      })
      .state('site-import', {
        url: '/sites-import',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'site-list', title: 'site.list'}],
              objectType: 'site',
              title: 'site.bulk_import',
              onSuccess: {state: 'site-list'}
            };
          }
        },
        parent: 'signed-in'
      })
      .state('site-import-jobs', {
        url: '/sites-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'site-list', title: 'site.list'}],
              title: 'site.bulk_import_jobs',
              objectTypes: ['site']
            }
          }
        },
        parent: 'signed-in'
      })
      .state('site-detail', {
        url: '/sites/:siteId',
        templateUrl: 'modules/administrative/site/detail.html',
        resolve: {
          site: function($stateParams, Site) {
            return Site.getById($stateParams.siteId);
          }
        },
        controller: 'SiteDetailCtrl',
        parent: 'signed-in'
      })
      .state('site-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/site/overview.html',
        controller: function() {
        },
        parent: 'site-detail'
      });
  });
