
angular.module('os.administrative.job.addedit', ['os.administrative.models'])
  .controller('JobAddEditCtrl', function($scope, $state, job) {
    
    function init() {
      if (!!job.id) {
        job.startDate = new Date();
      }

      $scope.job =  job;
    }


    $scope.saveOrUpdateJob = function() {
      $scope.job.$saveOrUpdate().then(
        function(result) {
          $state.go('job-list');
        }
      );
    }
    
    init();
  });
