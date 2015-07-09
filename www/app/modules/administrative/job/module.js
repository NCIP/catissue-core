angular.module('os.administrative.job',
  [ 
    'ui.router',
    'os.administrative.job.list',
    'os.administrative.job.viewlog',
    'os.administrative.job.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('job-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Storage Container Authorization Options
          $scope.jobResource = {
            readOpts  : {resource: 'ScheduledJob', operations: ['Read']},
            createOpts: {resource: 'ScheduledJob', operations: ['Create']},
            updateOpts: {resource: 'ScheduledJob', operations: ['Update']},
            runOpts   : {resource: 'ScheduledJob', operations: ['Read', 'Update']},
            deleteOpts: {resource: 'ScheduledJob', operations: ['Delete']}
          }
        },
        parent: 'signed-in'
      })

      .state('job-list', {
        url: '/jobs',
        templateUrl: 'modules/administrative/job/list.html',
        controller: 'JobListCtrl',
        parent: 'job-root'
      })

      .state('job-addedit', {
        url: '/job-addedit/:jobId',
        templateUrl: 'modules/administrative/job/addedit.html',
        resolve: {
          job: function($stateParams, ScheduledJobs) {
            if ($stateParams.jobId) {
              return ScheduledJobs.getById($stateParams.jobId);
            }

            return new ScheduledJobs({
              repeatSchedule: 'ONDEMAND',
              recipients: [],
              startDate: new Date()
            })

          }
        },
        controller: 'JobAddEditCtrl',
        parent: 'job-root'
      })

      .state('job-log', {
        url: '/job-log/:jobId',
        templateUrl: 'modules/administrative/job/viewlog.html',
        controller: 'JobViewLogCtrl',
        parent: 'job-root'
      })



  });
