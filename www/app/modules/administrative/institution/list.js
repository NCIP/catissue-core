angular.module('openspecimen')
  .controller('InstituteCtrl', function($scope, $modal, AlertService, InstituteService){
    var loadInstituteList = function() {
      InstituteService.getInstituteList().then(function(result) {
        if(result.status == 'ok') {
          $scope.instituteList = result.data;
        } else {
          AlertService.display($scope, 'Failed to load institute list.', 'danger');
        }
      });
    }

  $scope.addEditInstitute = function(instituteDetail) {
    var modalInstance = $modal.open({
      templateUrl: 'modules/administrative/institution/addedit.html',
      controller: 'InstituteAddEditCtrl',
      resolve: {
        instituteDetail: function() {
          return instituteDetail == undefined ? {} : angular.copy(instituteDetail);
        }
      }
    });
    modalInstance.result.then(function(result) {
      AlertService.display($scope, 'Institution Data Saved Successfully', 'success');
      loadInstituteList();
    });
  }

  loadInstituteList();
});
