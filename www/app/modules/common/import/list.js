
angular.module('os.common.import.list', ['os.common.import.importjob'])
  .controller('ImportJobsListCtrl', function($scope, importDetail, ImportJob, Util, Alerts) {

    function init() {
      $scope.importJobs = [];
      $scope.importDetail = importDetail;
      $scope.pagingOpts = {
        totalJobs: 0,
        currPage: 1,
        jobsPerPage: 25
      };


      $scope.$watch('pagingOpts.currPage', function() {
        loadJobs($scope.pagingOpts);
      });
    };

    function loadJobs(pagingOpts) {
      var startAt = (pagingOpts.currPage - 1) * pagingOpts.jobsPerPage;
      var maxResults = pagingOpts.jobsPerPage + 1;

      var queryParams = {objectType: importDetail.objectTypes, startAt: startAt, maxResults: maxResults};
      ImportJob.query(angular.extend(queryParams, importDetail.objectParams)).then(
        function(importJobs) {
          pagingOpts.totalJobs = (pagingOpts.currPage - 1) * pagingOpts.jobsPerPage + importJobs.length;
 
          if (importJobs.length >= maxResults) {
            importJobs.splice(importJobs.length - 1, 1);
          }

          $scope.importJobs = importJobs;
          angular.forEach(importJobs, function(job) {
            job.outputFileUrl = ImportJob.url() + job.$id() + '/output';
          });
        }
      );
    };

    $scope.stopJob = function(job) {
      var inputParams = {jobId: job.id};
      Util.showConfirm({
        ok: function () {
          job.stop().then(
            function(savedJob) {
              if (savedJob.status == 'STOPPED') {
                Alerts.success('bulk_imports.job_stopped', inputParams);
                loadJobs($scope.pagingOpts);
              } else if (savedJob.status == 'COMPLETED') {
                Alerts.success('bulk_imports.job_completed', inputParams);
                loadJobs($scope.pagingOpts);
              } else {
                Alerts.success('bulk_imports.job_stop_in_progress', inputParams);
              }
            }
          );
        },
        isWarning: true,
        title: 'bulk_imports.confirm_job_stop_title',
        confirmMsg: 'bulk_imports.confirm_job_stop',
        input: inputParams
      });
    }

    init();
  });
