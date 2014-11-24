angular.module('openspecimen')
  .controller('InstituteAddEditCtrl', function($scope, $modalInstance, 
    institute, AlertService, InstituteService) {

    var init = function() {
      $scope.institute = institute;
      $scope.institute.departments = institute.departments || [];
    }

    var handleResult = function(result) {
      if(result.status == 'ok') {
        $modalInstance.close(result);
      } else if (result.status == 'user_error') {
        var errMsgs = result.data.errorMessages;
        if(errMsgs.length > 0) {
          var errMsg = errMsgs[0].attributeName + ": " + errMsgs[0].message;
          AlertService.display($scope, errMsg, 'danger');
        }
      }
    };

    $scope.save = function(valid) {
      if(valid) {
        InstituteService.saveOrUpdateInstitute($scope.institute).then(handleResult);
      } else {
        AlertService.display($scope, 'There are some validation errors in form below. Please rectify them.', 'danger');
      }
    }

    $scope.deleteInstitute = function() {
      InstituteService.deleteInstitute($scope.institute.id).then(handleResult);
    }

    $scope.addDepartment = function() {
      $scope.institute.departments.push({id: '', name: ''});
    }

    $scope.removeDepartment = function(index) {
      $scope.institute.departments.splice(index, 1);
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
