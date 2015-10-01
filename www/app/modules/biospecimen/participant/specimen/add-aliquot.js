
angular.module('os.biospecimen.specimen.addaliquot',
  [
    'os.biospecimen.common',
    'os.biospecimen.models',
    'os.biospecimen.participant.collect-specimens'
  ])
  .controller('AddAliquotsCtrl', function($scope, $state, $stateParams, $modalInstance, specimen, cpr, visit, CollectSpecimensSvc, Specimen, SpecimenUtil) {
    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.aliquotSpec = {createdOn : new Date()};
      $scope.modal = true;
    }

    $scope.revertEdit = function() {
      $modalInstance.dismiss('cancel');
    }

    $scope.collectAliquots = function() {
      var specimens = SpecimenUtil.collectAliquots($scope);
      CollectSpecimensSvc.collect(getState(), $scope.visit, specimens, parent);
      $scope.revertEdit();
    };

    function getState() {
      return {state: $state.current, params: $stateParams};
    };

    init();
  });
