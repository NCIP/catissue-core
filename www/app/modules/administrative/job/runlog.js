
angular.module('os.administrative.job.runlog', ['os.administrative.models'])
  .controller('JobRunLogCtrl', function($scope, job) {

    function init() {
      $scope.jobRuns = [];
      $scope.job = job;
      job.getRuns().then(
        function(jobRuns) {
          $scope.jobRuns = jobRuns;
      });
    }
    
    init();
  });
