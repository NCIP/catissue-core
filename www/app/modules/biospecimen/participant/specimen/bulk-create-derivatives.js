angular.module('os.biospecimen.specimen')
  .controller('BulkCreateDerivativesCtrl', function($scope, parentSpmns, Specimen, Alerts, Util, SpecimenUtil) {
    function init() {
      var createdOn = new Date().getTime();

      var derivedSpmns = parentSpmns.map(
        function(ps) {
          return new Specimen({
            lineage: 'Derived',
            cpId: ps.cpId,
            ppid: ps.ppid,
            parentId: ps.id,
            parentLabel: ps.label,
            parentType: ps.type,
            parentCreatedOn: ps.createdOn,
            createdOn: createdOn,
            status: 'Collected'
          });
        }
      );

      $scope.ctx = {derivedSpmns: derivedSpmns}
    }

    function isValidCreatedOn(spmn) {
      if (spmn.createdOn < spmn.parentCreatedOn) {
        Alerts.error("specimens.errors.children_created_on_lt_parent", {parentLabel: spmn.parentLabel});
        return false;
      } else if (spmn.createdOn > new Date().getTime()) {
        Alerts.error("specimens.errors.children_created_on_gt_curr_time", {parentLabel: spmn.parentLabel});
        return false;
      } else {
        return true;
      }
    }

    $scope.copyFirstToAll = function() {
      var spmnToCopy = $scope.ctx.derivedSpmns[0];
      var attrsToCopy = ['specimenClass', 'type', 'initialQty', 'createdOn', 'printLabel', 'closeParent'];
      Util.copyAttrs(spmnToCopy, attrsToCopy, $scope.ctx.derivedSpmns);
      SpecimenUtil.copyContainerName(spmnToCopy, $scope.ctx.derivedSpmns);
    }

    $scope.removeSpecimen = function(index) {
      $scope.ctx.derivedSpmns.splice(index, 1);
      if ($scope.ctx.derivedSpmns.length == 0) {
        $scope.back();
      }
    }

    $scope.createDerivatives = function() {
      var result = [];

      for (var i = 0; i < $scope.ctx.derivedSpmns.length; ++i) {
        var spmn = angular.copy($scope.ctx.derivedSpmns[i]);
        if (!isValidCreatedOn(spmn)) {
          return;
        }

        delete spmn.ppid;
        delete spmn.parentCreatedOn;
        delete spmn.parentType;

        if (spmn.closeParent) {
          result.push(new Specimen({
            id: spmn.parentId,
            status: 'Collected',
            children: [spmn],
            closeAfterChildrenCreation: true
          }));
        } else {
          result.push(spmn);
        }
      }

      Specimen.save(result).then(
        function() {
          Alerts.success('specimens.derivatives_created');
          $scope.back();
        }
      );
    }

    init();
  });
