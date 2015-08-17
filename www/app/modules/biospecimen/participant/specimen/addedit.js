
angular.module('os.biospecimen.specimen.addedit', [])
  .controller('AddEditSpecimenCtrl', function($scope, $state, cpr, visit, specimen, PvManager) {
    function loadPvs() {
      $scope.loadSpecimenTypes = function(specimenClass, notclear) {
        $scope.specimenTypes = PvManager.getPvsByParent('specimen-class', specimenClass);
        if (!notclear) {
          $scope.currSpecimen.type = '';
        }
      };

      $scope.collectionStatuses = PvManager.getPvs('specimen-status');
      $scope.specimenClasses = PvManager.getPvs('specimen-class');
      if (!!specimen.specimenClass) {
        $scope.loadSpecimenTypes(specimen.specimenClass, true);
      }
      $scope.anatomicSites = PvManager.getLeafPvs('anatomic-site');
      $scope.lateralities = PvManager.getPvs('laterality');
      $scope.pathologyStatuses = PvManager.getPvs('pathology-status');
      $scope.biohazards = PvManager.getPvs('specimen-biohazard');
    };

    function init() {
      loadPvs();

      var currSpecimen = $scope.currSpecimen = angular.copy(specimen);
      currSpecimen.visitId = visit.id;

      if (currSpecimen.status != 'Collected') {
        if (!currSpecimen.id) {
          currSpecimen.status = 'Collected';
        }

        currSpecimen.availableQty = currSpecimen.initialQty;
      }

      if (!currSpecimen.storageLocation) {
        currSpecimen.storageLocation = {};
      }

      if (!currSpecimen.labelFmt) {
        if (specimen.lineage == 'New') {
          currSpecimen.labelFmt = cpr.specimenLabelFmt;
        } else if (specimen.lineage == 'Aliquot') {
          currSpecimen.labelFmt = cpr.aliquotLabelFmt;
        } else if (specimen.lineage == 'Derived') {
          currSpecimen.labelFmt = cpr.derivativeLabelFmt;
        }
      }
    }

    $scope.saveSpecimen = function() {
      $scope.currSpecimen.$saveOrUpdate().then(
        function(result) {
          angular.extend($scope.specimen, result);
          var params = {specimenId: result.id, srId: result.reqId};
          $state.go('specimen-detail.overview', params);
        }
      );
    }

    init();
  });
