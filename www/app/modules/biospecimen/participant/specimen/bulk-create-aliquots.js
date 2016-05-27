angular.module('os.biospecimen.specimen')
  .controller('BulkCreateAliquotsCtrl', function($scope, parentSpmns, Specimen, Alerts, Util, SpecimenUtil) {
    function init() {
      var createdOn = new Date().getTime();

      var aliquotsSpec = parentSpmns.map(
        function(ps) {
          return new Specimen({
            cpId: ps.cpId,
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

      $scope.ctx = {aliquotsSpec: aliquotsSpec}
    }

    function isValidCountQty(spec) {
      if (!!spec.quantity && !!spec.count) {
        var reqQty = spec.quantity * spec.count;
        if (reqQty > spec.parentAvailableQty) {
          Alerts.error("specimens.errors.insufficient_parent_qty", {label: spec.parentLabel});
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
        specimenClass: spec.specimenClass,
        type: spec.type,
        parentId: spec.parentId,
        initialQty: spec.quantity,
        storageLocation: spec.storageLocation,
        status: 'Collected',
        children: [],
        createdOn: spec.createdOn,
        printLabel: spec.printLabel
      });
    }

    $scope.copyFirstToAll = function() {
      var specToCopy = $scope.ctx.aliquotsSpec[0];
      var attrsToCopy = ['count', 'quantity', 'createdOn', 'printLabel', 'closeParent'];
      Util.copyAttrs(specToCopy, attrsToCopy, $scope.ctx.aliquotsSpec);
      SpecimenUtil.copyContainerName(specToCopy, $scope.ctx.aliquotsSpec);
    }

    $scope.removeSpec = function(index) {
      $scope.ctx.aliquotsSpec.splice(index, 1);
      if ($scope.ctx.aliquotsSpec.length == 0) {
        $scope.back();
      }
    }

    $scope.createAliquots = function() {
      var result = [];

      for (var i = 0; i < $scope.ctx.aliquotsSpec.length; ++i) {
        var spec = $scope.ctx.aliquotsSpec[i];
        if (!isValidCountQty(spec) || !isValidCreatedOn(spec)) {
          return;
        }

        var aliquots = [];
        var aliquotTmpl = getAliquotTmpl(spec);
        for (var j = 0; j < spec.count; ++j) {
          var clonedAlqt = angular.copy(aliquotTmpl);
          if (j != 0 && clonedAlqt.storageLocation) {
            delete clonedAlqt.storageLocation.positionX;
            delete clonedAlqt.storageLocation.positionY;
          }

          aliquots.push(clonedAlqt);
        }
         
        if (spec.closeParent) {
          result.push(new Specimen({
            id: spec.parentId,
            status: 'Collected',
            children: aliquots,
            closeAfterChildrenCreation: true
          }));
        } else {
          result = result.concat(aliquots);
        }
      }

      Specimen.save(result).then(
        function() {
          Alerts.success('specimens.aliquots_created');
          $scope.back();
        }
      );
    }

    init();
  });
