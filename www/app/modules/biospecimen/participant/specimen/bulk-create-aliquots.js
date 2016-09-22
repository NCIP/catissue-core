angular.module('os.biospecimen.specimen')
  .controller('BulkCreateAliquotsCtrl', function(
    $scope, $q, parentSpmns, cp,
    Specimen, Alerts, Util, SpecimenUtil, Container) {

    var ignoreQtyWarning = false, reservationId;

    function init() {
      var createdOn = new Date().getTime();

      var aliquotsSpec = parentSpmns.map(
        function(ps) {
          return new Specimen({
            cpId: ps.cpId,
            ppid: ps.ppid,
            parentId: ps.id,
            parentLabel: ps.label,
            parentAvailableQty: ps.availableQty,
            parentCreatedOn: ps.createdOn,
            specimenClass: ps.specimenClass,
            type: ps.type,
            createdOn: createdOn
          });
        }
      );

      $scope.ctx = {aliquotsSpec: aliquotsSpec, aliquots: []};
      if (!!cp.containerSelectionStrategy) {
        $scope.ctx.step2Title = 'specimens.review_locations';
        $scope.ctx.autoPosAllocate = true;
        $scope.$on('$destroy', vacateReservedPositions);
      } else {
        $scope.ctx.step2Title= 'specimens.assign_locations';
        $scope.ctx.autoPosAllocate = false;
      }
    }

    function showInsufficientQtyWarning(q) {
      SpecimenUtil.showInsufficientQtyWarning({
        ok: function () {
          ignoreQtyWarning = true;
          q.resolve($scope.validateSpecs());
        }
      });
    }

    function isValidCountQty(spec) {
      if (!!spec.quantity && !!spec.count) {
        var reqQty = spec.quantity * spec.count;
        if (!ignoreQtyWarning && spec.parentAvailableQty != undefined && reqQty > spec.parentAvailableQty) {
          return false;
        }
      } else if (!!spec.quantity) {
        spec.count = Math.floor(spec.parentAvailableQty / spec.quantity);
      } else if (!!spec.count) {
        spec.quantity = Math.round(spec.parentAvailableQty / spec.count * 10000) / 10000;
      }

      return true;
    }

    function isValidCreatedOn(spec) {
      if (spec.createdOn < spec.parentCreatedOn) {
        Alerts.error("specimens.errors.children_created_on_lt_parent", {parentLabel: spec.parentLabel});
        return false;
      } else if (spec.createdOn > new Date().getTime()) {
        Alerts.error("specimens.errors.children_created_on_gt_curr_time", {parentLabel: spec.parentLabel});
        return false;
      } else {
        return true;
      }
    }

    function getAliquotTmpl(spec) {
      return new Specimen({
        lineage: 'Aliquot',
        cpId: spec.cpId,
        specimenClass: spec.specimenClass,
        type: spec.type,
        ppid: spec.ppid,
        parentId: spec.parentId,
        parentLabel: spec.parentLabel,
        initialQty: spec.quantity,
        storageLocation: spec.storageLocation,
        status: 'Collected',
        children: [],
        createdOn: spec.createdOn,
        printLabel: spec.printLabel
      });
    }

    function vacateReservedPositions() {
      if (!!reservationId) {
        return Container.cancelReservation(reservationId);
      }

      return null;
    }

    function reservePositions() {
      return Container.getReservedPositions({
        cpId: cp.id,
        reservationToCancel: reservationId,
        tenants: $scope.ctx.aliquotsSpec.map(
          function(spec) {
            return {
              specimenClass: spec.specimenClass,
              specimenType: spec.type,
              numOfAliquots: spec.count
            }
          }
        )
      }).then(
        function(locations) {
          if (locations.length > 0) {
            reservationId = locations[0].reservationId;
          }

          return locations;
        }
      );
    }

    function setAliquots(specs, locations) {
      var aliquots = [], locationIdx = 0;

      for (var i = 0; i < specs.length; ++i) {
        var tmpl = getAliquotTmpl(specs[i]);

        for (var j = 0; j < specs[i].count; ++j) {
          var aliquot = angular.copy(tmpl);
          if (locations.length > 0) {
            aliquot.storageLocation = locations[locationIdx++];
          }

          aliquots.push(aliquot);
        }
      }

      $scope.ctx.aliquots = aliquots;
    }

    $scope.copyFirstToAll = function() {
      var specToCopy = $scope.ctx.aliquotsSpec[0];
      var attrsToCopy = ['count', 'quantity', 'createdOn', 'printLabel', 'closeParent'];
      Util.copyAttrs(specToCopy, attrsToCopy, $scope.ctx.aliquotsSpec);
    }

    $scope.removeSpec = function(index) {
      $scope.ctx.aliquotsSpec.splice(index, 1);
      if ($scope.ctx.aliquotsSpec.length == 0) {
        $scope.back();
      }
    }

    $scope.validateSpecs = function() {
      for (var i = 0; i < $scope.ctx.aliquotsSpec.length; ++i) {
        var spec = $scope.ctx.aliquotsSpec[i];
        if (!isValidCountQty(spec)) {
          if (!ignoreQtyWarning) {
            var q = $q.defer();
            showInsufficientQtyWarning(q);
            return q.promise;
          }
        } else if (!isValidCreatedOn(spec)) {
          return false;
        }
      }

      if (!!cp.containerSelectionStrategy) {
        return reservePositions().then(
          function(locations) {
            setAliquots($scope.ctx.aliquotsSpec, locations);
            return true;
          }
        );
      } else {
        setAliquots($scope.ctx.aliquotsSpec, []);
        return true;
      }
    }

    $scope.manuallySelectContainers = function() {
      $q.when(vacateReservedPositions()).then(
        function() {
          reservationId = undefined;

          angular.forEach($scope.ctx.aliquots,
            function(aliquot) {
              aliquot.storageLocation = {};
            }
          );

          $scope.ctx.autoPosAllocate = false;
        }
      );
    }

    $scope.applyFirstLocationToAll = function() {
      var loc = undefined;
      if ($scope.ctx.aliquots.length > 0 && !!$scope.ctx.aliquots[0].storageLocation) {
        loc = $scope.ctx.aliquots[0].storageLocation;
      }

      angular.forEach($scope.ctx.aliquots,
        function(aliquot, idx) {
          if (idx != 0) {
            aliquot.storageLocation = {name: loc.name, mode: loc.mode};
          }
        }
      );
    }

    $scope.showSpecs = function() {
      vacateReservedPositions();
      ignoreQtyWarning = false;
      reservationId = undefined;
      $scope.ctx.aliquots.length = 0;
      $scope.ctx.autoPosAllocate = !!cp.containerSelectionStrategy
      return true;
    }

    $scope.createAliquots = function() {
      var result = [],
          aliquotIdx = 0,
          aliquots = $scope.ctx.aliquots;

      angular.forEach($scope.ctx.aliquotsSpec,
        function(spec) {
          if (spec.closeParent) {
            result.push(new Specimen({
              id: spec.parentId,
              status: 'Collected',
              children: aliquots.slice(aliquotIdx, aliquotIdx + spec.count),
              closeAfterChildrenCreation: true
            }));
          } else {
            result = result.concat(aliquots.slice(aliquotIdx, aliquotIdx + spec.count))
          }

          aliquotIdx += spec.count;
        }
      );

      Specimen.save(result).then(
        function() {
          Alerts.success('specimens.aliquots_created');
          $scope.back();
        }
      );
    }

    init();
  });
