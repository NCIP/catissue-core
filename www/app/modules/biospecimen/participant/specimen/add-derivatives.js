
angular.module('os.biospecimen.specimen.addderivatives',
  [
    'os.biospecimen.common',
    'os.biospecimen.models',
    'os.biospecimen.specimen'
  ])
  .controller('AddDerivativeCtrl', function($scope, $state, $stateParams, PvManager, specimen, cpr, visit, CollectSpecimensSvc, Specimen, SpecimenUtil) {
    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      SpecimenUtil.loadSpecimenClasses($scope);
      $scope.derivative = SpecimenUtil.getNewDerivative($scope);
    }

    $scope.loadSpecimenTypes = function(specimenClass, notclear) {
      SpecimenUtil.loadSpecimenTypes($scope, specimenClass, notclear);
    };

    $scope.createDerivative = function() {
      SpecimenUtil.createDerivatives($scope);
      var params = {specimenId:  $scope.parentSpecimen.id, srId:  $scope.parentSpecimen.reqId};
      $state.go('specimen-detail.overview', params);
    };

    init();
  });
