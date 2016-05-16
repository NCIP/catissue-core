
angular.module('os.biospecimen.specimen.addderivative', [])
  .controller('AddDerivativeCtrl', function(
    $scope, specimen, cpr, visit, extensionCtxt, hasDict,
    SpecimenUtil, ExtensionsUtil, Alerts) {

    function init() {
      $scope.parentSpecimen = specimen;
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.derivative = SpecimenUtil.getNewDerivative($scope);

      var exObjs = [
        'specimen.lineage', 'specimen.parentLabel', 'specimen.events',
        'specimen.collectionEvent', 'specimen.receivedEvent'
      ];

      if (hasDict) {
        $scope.spmnCtx = {
          obj: {specimen: $scope.derivative}, inObjs: ['specimen'], exObjs: exObjs
        }
      } else {
        SpecimenUtil.loadSpecimenClasses($scope);
        SpecimenUtil.loadPathologyStatuses($scope);
        $scope.extnOpts = ExtensionsUtil.getExtnOpts($scope.derivative, extensionCtxt);
      }

      $scope.deFormCtrl = {};
    }

    $scope.toggleIncrParentFreezeThaw = function() {
      if ($scope.derivative.incrParentFreezeThaw) {
        if ($scope.parentSpecimen.freezeThawCycles == $scope.derivative.freezeThawCycles) {
          $scope.derivative.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles + 1;
        }
      } else {
        if (($scope.parentSpecimen.freezeThawCycles + 1) == $scope.derivative.freezeThawCycles) {
          $scope.derivative.freezeThawCycles = $scope.parentSpecimen.freezeThawCycles;
        }
      }
    };

    $scope.loadSpecimenTypes = function(specimenClass, notClear) {
      SpecimenUtil.loadSpecimenTypes($scope, specimenClass, notClear);
    };

    $scope.createDerivative = function() {
      SpecimenUtil.createDerivatives($scope);
    };

    $scope.revertEdit = function () {
      $scope.back();
    }

    init();
  });
