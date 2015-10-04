
angular.module('os.biospecimen.specimen.addaliquot',
  [
    'os.biospecimen.common',
    'os.biospecimen.models',
    'os.biospecimen.participant.collect-specimens'
  ])
  .controller('AddAliquotsCtrl', function(
    $scope, $state, $stateParams, specimen, cpr, visit,
    CollectSpecimensSvc, SpecimenUtil) {
    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.aliquotSpec = {createdOn : new Date()};

      if (!!$state.previous && $state.previous.url.indexOf("collect-specimens") == 1   ) {
         var params = {specimenId:  $scope.parentSpecimen.id, srId:  $scope.parentSpecimen.reqId};
         $state.go('specimen-detail.overview', params);
      }
    }

    $scope.collectAliquots = function() {
      var specimens = SpecimenUtil.collectAliquots($scope);
      CollectSpecimensSvc.collect(getState(), $scope.visit, specimens, parent);
    };

    function getState() {
      return {state: $state.current, params: $stateParams};
    };

    init();
  });
