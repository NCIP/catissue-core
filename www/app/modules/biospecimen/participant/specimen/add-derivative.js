
angular.module('os.biospecimen.specimen.addderivative', [])
  .controller('AddDerivativeCtrl', function(
    $scope, cp, specimen, cpr, visit, extensionCtxt, hasDict,
    SpecimenUtil, Container, ExtensionsUtil, Alerts) {

    var autoAllocator;

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
        SpecimenUtil.loadPathologyStatuses($scope);
        $scope.extnOpts = ExtensionsUtil.getExtnOpts($scope.derivative, extensionCtxt);
      }

      $scope.deFormCtrl = {};

      autoAllocator = enableAutoAllocator();
      $scope.$on('$destroy', cancelReservation);
    }

    function enableAutoAllocator() {
      $scope.autoAllocation = false;
      if (!cp.containerSelectionStrategy) {
        return undefined;
      }

      $scope.autoAllocation = true;
      return $scope.$watch('derivative.type',
        function(newType, oldType) {
          if (newType == oldType) {
            return;
          }

          var spmn = $scope.derivative;
          var resvToCancel;
          if (!!spmn.storageLocation) {
            resvToCancel = spmn.storageLocation.reservationId;
          }

          Container.reservePositionForSpecimen(cp.id, spmn, resvToCancel).then(
            function(position) {
              spmn.storageLocation = position;
            }
          );
        }
      );
    }

    function disableAutoAllocator() {
      if (autoAllocator) {
        autoAllocator();
        autoAllocator = undefined;
      }

      $scope.autoAllocation = false;
    }

    function cancelReservation() {
      var loc = $scope.derivative.storageLocation;
      if (!loc || !loc.reservationId) {
        disableAutoAllocator();
        return;
      }

      Container.cancelReservation(loc.reservationId).then(
        function() {
          $scope.derivative.storageLocation = {};
          disableAutoAllocator();
        }
      );
    }

    $scope.cancelReservation = cancelReservation;

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

    $scope.createDerivative = function() {
      SpecimenUtil.createDerivatives($scope);
    };

    $scope.revertEdit = function () {
      $scope.back();
    }

    init();
  });
