
angular.module('os.common.import.list', ['os.common.import.importjob'])
  .controller('ImportJobsListCtrl', function($scope, importDetail, ImportJob) {

    function init() {
      $scope.importJobs = [];
      $scope.importDetail = importDetail;

      loadAllJobs();
    };

    function loadAllJobs() {
      ImportJob.query({objectType: importDetail.objectTypes}).then(
        function(importJobs) {
          $scope.importJobs = importJobs;
        }
      );
    };
 
    init();
  });
