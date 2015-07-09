
angular.module('os.administrative.job.viewlog', ['os.administrative.models'])
  .controller('JobViewLogCtrl', function($scope, $state, $stateParams, ScheduledJobRuns) {

    function init() {
      ScheduledJobRuns.query({scheduledJobId:$stateParams.jobId}).then(
        function(jobRuns) {
          $scope.jobRuns = jobRuns;
      });
    }
    
    init();

  });
