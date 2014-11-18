angular.module('openspecimen')
  .controller('InstituteAddEditCtrl', function($scope, $modalInstance, 
    AlertService, InstituteService, instituteDetail) {

    $scope.isEdit = false;
    if (instituteDetail.id) {
      $scope.isEdit = true;
    }

    $scope.instituteDetail = instituteDetail;

    var handleResult = function(result) {
      if(result.status == 'ok') {
        $modalInstance.close('ok');
      } else if (result.status == 'user_error') {
        var errMsgs = result.data.errorMessages;
        if(errMsgs.length > 0) {
          var errMsg = errMsgs[0].attributeName + ": " + errMsgs[0].message;
          AlertService.display($scope, errMsg, 'danger');
        }
      }
    };

    $scope.save = function() {
      InstituteService.saveOrUpdateInstitute($scope.instituteDetail).then(handleResult);
    }

    if($scope.instituteDetail.departments == null) {
      $scope.instituteDetail.departments = [];
    }

    $scope.addDepartment = function() {
      $scope.instituteDetail.departments.push({id:'', name: ''});
    }

    $scope.removeDepartment = function(index) {
      $scope.instituteDetail.departments.splice(index, 1);
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }
  });
