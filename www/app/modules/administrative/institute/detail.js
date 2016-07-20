angular.module('os.administrative.institute.detail', ['os.administrative.models'])
  .controller('InstituteDetailCtrl', function($scope, institute, DeleteUtil) {
    $scope.institute = institute;

    $scope.deleteInstitute = function() {
      DeleteUtil.delete($scope.institute, {onDeleteState: 'institute-list'});
    }

  });
