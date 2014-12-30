angular.module('os.administrative.institute.list', ['os.administrative.models'])
  .controller('InstituteListCtrl', function($scope, $state, Institute) {

    var loadInstitutes = function() {
      Institute.list().then(
        function(instituteList) {
          $scope.instituteList = instituteList;
        }
      )
    }

    $scope.showInstituteOverview = function(institute) {
      $state.go('institute-detail.overview', {instituteId: institute.id});
    };

    loadInstitutes();
  });
