
angular.module('os.administrative.institute',
  [
    'ui.router',
    'os.administrative.institute.list',
    'os.administrative.institute.addedit',
    'os.administrative.institute.detail',
    'os.administrative.institute.departments'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('institute-list', {
        url: '/institutes',
        templateUrl: 'modules/administrative/institute/list.html',
        controller: 'InstituteListCtrl',
        parent: 'signed-in'
      })
      .state('institute-new', {
        url: '/new-institute',
        templateUrl: 'modules/administrative/institute/addedit.html',
        resolve: {
          institute: function(Institute) {
            return new Institute();
          }
        },
        controller: 'InstituteAddEditCtrl',
        parent: 'signed-in'
      })
      .state('institute-edit', {
        url: '/institutes/:instituteId/edit',
        templateUrl: 'modules/administrative/institute/addedit.html',
        resolve: {
          institute: function($stateParams, Institute) {
            return Institute.getById($stateParams.instituteId);
          }
        },
        controller: 'InstituteAddEditCtrl',
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
