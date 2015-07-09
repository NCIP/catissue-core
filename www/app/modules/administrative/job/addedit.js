
angular.module('os.administrative.job.addedit', ['os.administrative.models', 'os.biospecimen.models'])
  .controller('JobAddEditCtrl', function(
    $scope, $state, $translate, job, ScheduledJobs, Alerts, User) {
    
    function init() {
     console.warn("Job Add edit");
     if (job.id != undefined) {
       job.createdBy = $scope.currentUser;
       job.startDate = new Date();
     }
     $scope.job =  job;
    }


    $scope.createJob = function() {
      console.warn("Create job");
      var scheduledJob = angular.copy($scope.job);
      angular.copy(scheduledJob).$saveOrUpdate().then(
        function(scheduledJob) {
          $state.go('job-list');
        }
      );
    }
    
    init();
  });
