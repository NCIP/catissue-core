
angular.module('os.administrative.job.list', ['os.administrative.models'])
  .controller('JobListCtrl', function($scope, $modal, ScheduledJob, Alerts) {

    function init() {
      $scope.jobs = [];
      $scope.filterOpts = {query: undefined};
      loadJobs($scope.filterOpts);
    }

    function loadJobs(filterOpts) {
      ScheduledJob.query(filterOpts).then(
        function(jobs) {
          $scope.jobs = jobs;
        }
      );
    }

    function runJob(job, args) {
      args = args || {};
      job.executeJob(args).then(
        function() {
          Alerts.success("jobs.queued_for_exec", job);
        }
      );
    }
    
    $scope.executeJob = function(job) {
      if (!job.rtArgsProvided) {
        runJob(job);
        return;
      }

      $modal.open({
        templateUrl: 'modules/administrative/job/args.html',
        controller: function($scope, $modalInstance) {
          $scope.job = job;
          $scope.args = {};

          $scope.ok = function() {
            $modalInstance.close($scope.args); 
          }

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        }
      }).result.then(
        function(args) {
          runJob(job, args);
        }
      );
    }


    $scope.deleteJob = function(job) {
      job.$remove().then(
        function() {
          var idx = $scope.jobs.indexOf(job);
          $scope.jobs.splice(idx, 1);
        }
      );
    }

    init();
  });
