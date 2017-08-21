
angular.module('os.common.import',
  [
    'os.common.import.importjob',
    'os.common.import.list',
    'os.common.import.addctrl'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('import-jobs', {
        url: '/bulk-import-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              title: 'bulk_imports.jobs_list'
            }
          }
        },
        parent: 'signed-in'
      })
  });
