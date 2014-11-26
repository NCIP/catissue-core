angular.module('openspecimen')
  .controller('InstituteCtrl', function($scope, $modal, AlertService, InstituteService) {
    var loadInstituteList = function() {
      InstituteService.getInstituteList().then(function(result) {
        if(result.status == 'ok') {
          $scope.instituteList = result.data;
        } else {
          AlertService.display($scope, 'Failed to load institute list.', 'danger');
        }
      });
    }

    $scope.addEditInstitute = function(institute) {
      var modalInstance = $modal.open({
        templateUrl: 'modules/administrative/institute/addedit.html',
        controller: 'InstituteAddEditCtrl',
        resolve: {
          institute: function() {
            return institute == undefined ? {} : angular.copy(institute);
          }
        }
      });
      modalInstance.result.then(function(result) {
        AlertService.display($scope, 'Institute Saved Successfully', 'success');
        loadInstituteList();
      });
    }

    loadInstituteList();
  });
