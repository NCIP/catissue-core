angular.module('os.administrative.job',
  [ 
    'ui.router',
    'os.administrative.job.list',
    'os.administrative.job.runlog',
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
            runOpts   : {resource: 'ScheduledJob', operations: ['Read']},
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
        controller: 'JobAddEditCtrl',
        resolve: {
          job: function($stateParams, ScheduledJob) {
            if (!!$stateParams.jobId) {
              return ScheduledJob.getById($stateParams.jobId);
            }

            return new ScheduledJob({
              repeatSchedule: 'ONDEMAND',
              recipients: [],
              startDate: new Date()
            })
          }
        },
        parent: 'job-root'
      })
      .state('job-run-log', {
        url: '/job-run-log/:jobId',
        templateUrl: 'modules/administrative/job/runlog.html',
        controller: 'JobRunLogCtrl',
        resolve: {
          job: function($stateParams, ScheduledJob) {
            return ScheduledJob.getById($stateParams.jobId);
          }
        },
        parent: 'job-root'
      })
  });
