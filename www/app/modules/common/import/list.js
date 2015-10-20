
angular.module('os.common.import.list', ['os.common.import.importjob'])
  .controller('ImportJobsListCtrl', function($scope, importDetail, ImportJob) {

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
      ImportJob.query({objectType: importDetail.objectTypes, startAt: startAt, maxResults: maxResults}).then(
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

    init();
  });
