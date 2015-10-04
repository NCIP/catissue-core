
angular.module('os.biospecimen.specimen.addderivatives',
  [
    'os.biospecimen.common',
    'os.biospecimen.models',
    'os.biospecimen.specimen'
  ])
  .controller('AddDerivativeCtrl', function(
    $scope, $state, $stateParams, specimen, cpr, visit, SpecimenUtil) {
    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.derivative = SpecimenUtil.getNewDerivative($scope);
      SpecimenUtil.loadSpecimenClasses($scope);
    }

    $scope.loadSpecimenTypes = function(specimenClass, notClear) {
      SpecimenUtil.loadSpecimenTypes($scope, specimenClass, notClear);
    };

    $scope.createDerivative = function() {
      SpecimenUtil.createDerivatives($scope);
      var params = {specimenId:  $scope.parentSpecimen.id, srId:  $scope.parentSpecimen.reqId};
      $state.go('specimen-detail.overview', params);
    };

    init();
  });
