
angular.module('os.biospecimen.specimen.addaliquots', [])
  .controller('AddAliquotsCtrl', function(
    $scope, $rootScope, $state, $stateParams, specimen, cpr, visit,
    CollectSpecimensSvc, SpecimenUtil) {
    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.aliquotSpec = {createdOn : new Date()};

      console.warn("$rootScope.stateChangeInfo " + $rootScope.stateChangeInfo);
      // After collecting specimens, control will come to add-aliquot page
      // because of the collect-specimen implementation,
      // where, on success control is returning to previous state
      if ($rootScope.stateChangeInfo.fromState.url.indexOf("collect-specimens") == 1) {
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
