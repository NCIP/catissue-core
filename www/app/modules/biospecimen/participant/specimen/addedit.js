
angular.module('os.biospecimen.specimen.addedit', [])
  .controller('AddEditSpecimenCtrl', function(
    $scope, $state, cpr, visit, specimen, extensionCtxt, hasDict,
    PvManager, Util, ExtensionsUtil) {

    function loadPvs() {
      $scope.biohazards = PvManager.getPvs('specimen-biohazard');
    };

    function init() {
      var currSpecimen = $scope.currSpecimen = angular.copy(specimen);
      delete currSpecimen.children;

      currSpecimen.visitId = visit.id;
      currSpecimen.createdOn = currSpecimen.createdOn || new Date();

      if (currSpecimen.lineage != 'New') {
        currSpecimen.anatomicSite = currSpecimen.laterality = undefined;
      }

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

      var exObjs = ['specimen.lineage', 'specimen.parentLabel', 'specimen.events'];
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
      } else {
        exObjs.push('specimen.collectionEvent', 'specimen.receivedEvent');
      }

      $scope.currSpecimen.initialQty = Util.getNumberInScientificNotation($scope.currSpecimen.initialQty);
      $scope.currSpecimen.availableQty = Util.getNumberInScientificNotation($scope.currSpecimen.availableQty);
      $scope.currSpecimen.concentration = Util.getNumberInScientificNotation($scope.currSpecimen.concentration);

      $scope.spmnCtx = {
        obj: {specimen: $scope.currSpecimen}, inObjs: ['specimen'], exObjs: exObjs
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(currSpecimen, extensionCtxt);

      if (!hasDict) {
        loadPvs();
      }

      $scope.$watch('currSpecimen.specimenClass', function(newVal, oldVal) {
        if (!newVal || newVal == oldVal || !oldVal) {
          return;
        }
        $scope.currSpecimen.type = '';
      });
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
