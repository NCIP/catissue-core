
angular.module('os.biospecimen.specimen.addaliquots', [])
  .controller('AddAliquotsCtrl', function(
    $scope, $rootScope, $state, $stateParams, specimen, cpr, visit, extensionCtxt, hasDict,
    CollectSpecimensSvc, SpecimenUtil, ExtensionsUtil, Alerts) {

    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.aliquotSpec = {
        cpId: visit.cpId,
        lineage: 'Aliquot',
        specimenClass: specimen.specimenClass,
        type: specimen.type,
        createdOn : new Date(),
        freezeThawCycles: specimen.freezeThawCycles + 1,
        incrParentFreezeThaw: 1
      };

      //
      // On successful collection of aliquots, direct user to specimen detail view
      // TODO: where to go should be state input param
      //
      if ($rootScope.stateChangeInfo.fromState.url.indexOf("collect-specimens") == 1) {
        var params = {specimenId:  $scope.parentSpecimen.id, srId:  $scope.parentSpecimen.reqId};
        $state.go('specimen-detail.overview', params);
      }

      var exObjs = [
        'specimen.lineage', 'specimen.parentLabel', 'specimen.initialQty',
        'specimen.availableQty', 'specimen.storageLocation',
        'specimen.events', 'specimen.collectionEvent', 'specimen.receivedEvent'
      ];

      if (hasDict) {
        $scope.spmnCtx = {
          obj: {specimen: $scope.aliquotSpec}, inObjs: ['specimen'], exObjs: exObjs
        }
      } else {
        $scope.extnOpts = ExtensionsUtil.getExtnOpts($scope.aliquotSpec, extensionCtxt);
      }

      $scope.deFormCtrl = {};
    }

    function getState() {
      return {state: $state.current, params: $stateParams};
    }

    $scope.toggleIncrParentFreezeThaw = function() {
      if ($scope.aliquotSpec.incrParentFreezeThaw) {
        if ($scope.parentSpecimen.freezeThawCycles == $scope.aliquotSpec.freezeThawCycles) {
          $scope.aliquotSpec.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles + 1;
        }
      } else {
        if (($scope.parentSpecimen.freezeThawCycles + 1) == $scope.aliquotSpec.freezeThawCycles) {
          $scope.aliquotSpec.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles;
        }
      }
    }

    $scope.collectAliquots = function() {
      var specimens = SpecimenUtil.collectAliquots($scope);
      if (specimens) {
        CollectSpecimensSvc.collect(getState(), $scope.visit, specimens, $scope.ignoreQtyWarning);
      }
    }

    init();
  });
