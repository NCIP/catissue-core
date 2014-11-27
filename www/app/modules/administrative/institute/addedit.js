angular.module('openspecimen')
  .controller('InstituteAddEditCtrl', function($scope, $modalInstance, 
    institute, AlertService, InstituteService) {

    var init = function() {
      $scope.institute = institute;
      $scope.institute.departments = institute.departments || [];
    }

    var handleResult = function(result) {
      if (result.status == 'ok') {
        $modalInstance.close('ok');
      } else if (result.status == 'user_error') {
        var errMsgs = result.data.errorMessages;
        if (errMsgs.length > 0) {
          var errMsg = errMsgs[0].attributeName + ": " + errMsgs[0].message;
          AlertService.display($scope, errMsg, 'danger');
        }
      }
    };

    $scope.forms = {
      instituteForm : {submitted: false}
    };

    $scope.setForm = function(form) {
      $scope.forms[form.$name].form = form;
    };

    var validateForm = function(form) {
      form.submitted = true;
      if (!form.form.$valid) {
        var err = 'There are some validation errors in form below. Please rectify them.';
        AlertService.display($scope, err, 'danger');
        return false;
      }
      return true;
    };

    $scope.save = function() {
      if(validateForm($scope.forms.instituteForm)) {
        InstituteService.saveOrUpdateInstitute($scope.institute).then(handleResult);
      }
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
