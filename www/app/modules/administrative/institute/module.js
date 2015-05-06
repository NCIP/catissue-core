
angular.module('os.administrative.institute',
  [
    'ui.router',
    'os.administrative.institute.list',
    'os.administrative.institute.addedit',
    'os.administrative.institute.detail'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('institute-list', {
        url: '/institutes',
        templateUrl: 'modules/administrative/institute/list.html',
        controller: 'InstituteListCtrl',
        parent: 'signed-in'
      })
      .state('institute-addedit', {
        url: '/institute-addedit/:instituteId',
        templateUrl: 'modules/administrative/institute/addedit.html',
        resolve: {
          institute: function($stateParams, Institute) {
            if ($stateParams.instituteId) {
              return Institute.getById($stateParams.instituteId);
            }
            return new Institute();
          }
        },
        controller: 'InstituteAddEditCtrl',
        parent: 'signed-in'
      })
      .state('institute-import', {
        url: '/institutes-import',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'institute-list', title: 'institutes.list'}],
              objectType: 'institute',
              title: 'institutes.bulk_import',
              onSuccess: {state: 'institute-list'}
            };
          }
        },
        parent: 'signed-in'
      })
      .state('institute-import-jobs', {
        url: '/institutes-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [{state: 'institute-list', title: 'institutes.list'}],
              title: 'institutes.bulk_import_jobs',
              objectTypes: ['institute']
            }
          }
        },
        parent: 'signed-in'
      })
      .state('institute-detail', {
        url: '/institutes/:instituteId',
        templateUrl: 'modules/administrative/institute/detail.html',
        resolve: {
          institute: function($stateParams, Institute) {
            return Institute.getById($stateParams.instituteId);
          }
        },
        controller: 'InstituteDetailCtrl',
        parent: 'signed-in'
      })
      .state('institute-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/administrative/institute/overview.html',
        controller: function() {
        },
        parent: 'institute-detail'
      })
  });
