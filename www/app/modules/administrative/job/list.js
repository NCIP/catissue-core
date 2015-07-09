
angular.module('os.administrative.job.list', ['os.administrative.models'])
  .controller('JobListCtrl', function($scope, $state, ScheduledJobs) {

    function init() {
      $scope.jobs = [];
      ScheduledJobs.query().then(
        function(jobs) {
          $scope.jobs = jobs;
        }
      );
    }
    
    init();

    $scope.executeJob = function(jobId) {
      ScheduledJobs.executeJob(jobId);
    }

    $scope.deleteJob = function(jobId) {
      ScheduledJobs.$remove(jobId);
      console.warn("Delete job" + jobId);
    }
  });
