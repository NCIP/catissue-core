angular.module('os.administrative.institute.addedit',['os.administrative.models'])
  .controller('InstituteAddEditCtrl', function($scope, $state, institute, Institute) {

    var init = function() {
      $scope.institute = institute; 
    }

    $scope.save = function() {
      var institute = angular.copy($scope.institute);
      institute.$saveOrUpdate().then(
        function(savedInstitute) {
          $state.go('institute-detail.overview', {instituteId: savedInstitute.id});
        }
      );
    }

    init();
  });
