
angular.module('os.administrative.job.list', ['os.administrative.models'])
  .controller('JobListCtrl', function($scope, $modal, ScheduledJob, Alerts, ListPagerOpts) {

    var pagerOpts;

    function init() {
      $scope.jobs = [];
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getJobsCount});
      $scope.filterOpts = {query: undefined, maxResults: pagerOpts.recordsPerPage + 1};
      loadJobs($scope.filterOpts);
    }

    function loadJobs(filterOpts) {
      ScheduledJob.query(filterOpts).then(
        function(jobs) {
          $scope.jobs = jobs;
          pagerOpts.refreshOpts(jobs);
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

    function getJobsCount() {
      return ScheduledJob.getCount($scope.filterOpts);
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
