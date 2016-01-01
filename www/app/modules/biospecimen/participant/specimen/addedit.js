
angular.module('os.biospecimen.specimen.addedit', [])
  .controller('AddEditSpecimenCtrl', function($scope, $state, cpr, visit, specimen, extensionCtxt, PvManager, Util) {
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
      $scope.lateralities = PvManager.getPvs('laterality');
      $scope.pathologyStatuses = PvManager.getPvs('pathology-status');
      $scope.biohazards = PvManager.getPvs('specimen-biohazard');

      if (!specimen.id && !specimen.reqId) {
        $scope.collectionProcedures = PvManager.getPvs('collection-procedure');
        $scope.collectionContainers = PvManager.getPvs('collection-container');
        $scope.receivedQualities =  PvManager.getPvs('received-quality');
      }      
    };

    function init() {
      loadPvs();

      var currSpecimen = $scope.currSpecimen = angular.copy(specimen);

      currSpecimen.visitId = visit.id;
      currSpecimen.createdOn = currSpecimen.createdOn || new Date();

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

      if (!$scope.currSpecimen.id && !$scope.currSpecimen.reqId) {
        var currentDate = new Date();
        $scope.currSpecimen.collectionEvent = {
          user: $scope.currentUser,
          time: currentDate
        };

        $scope.currSpecimen.receivedEvent = {
          user: $scope.currentUser,
          time: currentDate
        };

        $scope.currSpecimen.collectionEvent.container = "Not Specified";
        $scope.currSpecimen.collectionEvent.procedure = "Not Specified";
        $scope.currSpecimen.receivedEvent.receivedQuality = "Acceptable";
      }

      $scope.currSpecimen.initialQty = Util.getNumberInScientificNotation($scope.currSpecimen.initialQty);
      $scope.currSpecimen.availableQty = Util.getNumberInScientificNotation($scope.currSpecimen.availableQty);
      $scope.currSpecimen.concentration = Util.getNumberInScientificNotation($scope.currSpecimen.concentration);

      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts(currSpecimen, extensionCtxt);
    }

    $scope.saveSpecimen = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      if (formCtrl) {
         $scope.currSpecimen.extensionDetail = formCtrl.getFormData();
      }

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
