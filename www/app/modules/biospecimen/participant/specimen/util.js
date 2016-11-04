angular.module('os.biospecimen.specimen')
  .factory('SpecimenUtil', function($modal, $q, Specimen, PvManager, Alerts, Util) {

    function collectAliquots(scope) {
      var spec = scope.aliquotSpec;
      var parent = scope.parentSpecimen;
      var extensionDetail = getExtensionDetail(scope);
      if (!extensionDetail) {
        extensionDetail = scope.aliquotSpec.extensionDetail;
      }

      if (!!spec.qtyPerAliquot && !!spec.noOfAliquots) {
        var requiredQty = spec.qtyPerAliquot * spec.noOfAliquots;
        if (parent.type == spec.type &&
          parent.availableQty != undefined && requiredQty > parent.availableQty &&
          !scope.ignoreQtyWarning) {

          showInsufficientQtyWarning({
            ok: function () {
              scope.ignoreQtyWarning = true;
              scope.collectAliquots();
            }
          });
          return;
        }
      } else if (!!spec.qtyPerAliquot) {
        spec.noOfAliquots = Math.floor(parent.availableQty / spec.qtyPerAliquot);
      } else if (!!spec.noOfAliquots) {
        spec.qtyPerAliquot = Math.round(parent.availableQty / spec.noOfAliquots * 10000) / 10000;
      }

      if (scope.aliquotSpec.createdOn.getTime() < scope.parentSpecimen.createdOn) {
        Alerts.error("specimens.errors.created_on_lt_parent");
        return;
      } else if (scope.aliquotSpec.createdOn > new Date()) {
        Alerts.error("specimens.errors.created_on_gt_curr_time");
        return;
      }

      parent.isOpened = parent.hasChildren = true;
      parent.depth = 0;
      parent.closeAfterChildrenCreation = spec.closeParent;

      var derived = undefined;
      if (spec.specimenClass != parent.specimenClass || spec.type != parent.type) {
        derived = getSpmnToSave(
          'Derived', spec, parent,
          Math.round(spec.qtyPerAliquot * spec.noOfAliquots),
          scope.cpr.derivativeLabelFmt);
      }

      var aliquot = getSpmnToSave(
        'Aliquot', spec, (derived ? derived : parent),
        spec.qtyPerAliquot,
        scope.cpr.aliquotLabelFmt);

      var aliquots = [];
      for (var i = 0; i < spec.noOfAliquots; ++i) {
        var clonedAlqt = angular.copy(aliquot);
        clonedAlqt.extensionDetail = extensionDetail;
        aliquots.push(clonedAlqt);
      }

      var specimens = [];
      if (derived) {
        derived.storageType = 'Virtual';
        derived.closeAfterChildrenCreation = spec.closeParent;
        derived.hasChildren = true;
        derived.children = [].concat(aliquots);
        parent.children = [derived];
        specimens = [derived].concat(aliquots);
      } else {
        parent.children = [].concat(aliquots);
        specimens = aliquots;
      }

      specimens.unshift(parent);
      return specimens;
    }

    function createDerivatives(scope) {
      var extensionDetail = getExtensionDetail(scope);
      if (extensionDetail) {
        scope.derivative.extensionDetail = extensionDetail;
      }

      var closeParent = scope.derivative.closeParent;
      delete scope.derivative.closeParent;

      if (scope.derivative.createdOn.getTime() < scope.parentSpecimen.createdOn) {
        Alerts.error("specimens.errors.created_on_lt_parent");
        return;
      } else if (scope.derivative.createdOn > new Date()) {
        Alerts.error("specimens.errors.created_on_gt_curr_time");
        return;
      }

      var specimensToSave = undefined;
      if (closeParent) {
        specimensToSave = [new Specimen({
          id: scope.parentSpecimen.id,
          lineage: scope.parentSpecimen.lineage,
          visitId: scope.visit.id,
          closeAfterChildrenCreation: true,
          children: [scope.derivative]
        })];
      } else {
        specimensToSave = [scope.derivative];
      }

      return Specimen.save(specimensToSave).then(
        function(result) {
          if (closeParent) {
            scope.parentSpecimen.children.push(result[0].children[0]);
            scope.parentSpecimen.activityStatus = 'Closed';
          } else {
            scope.parentSpecimen.children.push(result[0]);
          }

          if (scope.derivative.incrParentFreezeThaw) {
            scope.parentSpecimen.freezeThawCycles++;
          }

          if (scope.derivative.storageLocation) {
            delete scope.derivative.storageLocation.reservationId;
          }

          scope.revertEdit();
        }
      );
    }

    function getNewDerivative(scope) {
      return new Specimen({
        parentId: scope.parentSpecimen.id,
        lineage: 'Derived',
        storageLocation: {},
        status: 'Collected',
        visitId: scope.visit.id,
        cpId: scope.visit.cpId,
        pathology: scope.parentSpecimen.pathology,
        closeParent: false,
        createdOn : new Date(),
        incrParentFreezeThaw: 1,
        freezeThawCycles: scope.parentSpecimen.freezeThawCycles + 1
      });
    }

    function loadSpecimenClasses(scope) {
      if (scope.classesLoaded) {
        return;
      }

      scope.specimenClasses = PvManager.getPvs('specimen-class');
      scope.classesLoaded = true;
    }

    function loadSpecimenTypes(scope, specimenClass, notClear) {
      if (!notClear) {
        scope.derivative.type = '';
      }

      if (!specimenClass) {
        scope.specimenTypes = [];
        return;
      }

      if (!scope.specimenClasses[specimenClass]) {
        scope.specimenClasses[specimenClass] = PvManager.getPvsByParent('specimen-class', specimenClass);
      }

      scope.specimenTypes = scope.specimenClasses[specimenClass];
    }

    function getExtensionDetail(scope) {
      var formCtrl = scope.deFormCtrl.ctrl;
      if (!formCtrl || !formCtrl.validate()) {
        return;
      }

      return formCtrl.getFormData();
    }

    function copyContainerName(src, array) {
      if (!src.storageLocation || !src.storageLocation.name) {
        return;
      }

      var containerName = src.storageLocation.name;
      angular.forEach(array,
        function(dst) {
          if (src == dst || src.specimenClass != dst.specimenClass || src.type != dst.type) {
            return;
          }

          if (!dst.storageLocation || containerName != dst.storageLocation.name) {
            dst.storageLocation = {name: containerName};
          }
        }
      );
    }

    function getSpecimens(labels, filterOpts, errorOpts) {
      filterOpts = filterOpts || {};
      filterOpts.label = labels;

      return Specimen.query(filterOpts).then(
        function(specimens) {
          return resolveSpecimens(labels, specimens, errorOpts);
        }
      );
    }

    function deferred(resp) {
      var deferred = $q.defer();
      deferred.resolve(resp);
      return deferred.promise;
    }

    function resolveSpecimens(labels, specimens, errorOpts) {
      var specimensMap = {};
      angular.forEach(specimens, function(spmn) {
        if (!specimensMap[spmn.label]) {
          specimensMap[spmn.label] = [spmn];
        } else {
          specimensMap[spmn.label].push(spmn);
        }
      });

      //
      // {label: label, specimens; [s1, s2], selected: s1}
      //
      var labelsInfo = [];
      var dupLabels = [], notFoundLabels = [];

      angular.forEach(labels, function(label) {
        var labelInfo = {label: label};
        var spmns = specimensMap[label];
        if (!spmns) {
          notFoundLabels.push(label);
          return;
        }

        labelInfo.specimens = spmns;
        if (spmns.length > 1) {
          dupLabels.push(labelInfo);
        } else {
          labelInfo.selected = spmns[0];
        }

        labelsInfo.push(labelInfo);
      });

      if (notFoundLabels.length != 0) {
        showError(notFoundLabels, errorOpts)
        return deferred(undefined);
      }

      if (dupLabels.length == 0) {
        return deferred(specimens);
      }

      return $modal.open({
        templateUrl: 'modules/biospecimen/participant/specimen/resolve-specimens.html',
        controller: 'ResolveSpecimensCtrl',
        resolve: {
          labels: function() {
            return dupLabels;
          }
        }
      }).result.then(
        function(spmns) {
          //
          // Duplicate labels info passed to modal is a sub-view of labelsInfo list;
          // therefore any updates/selection done in modal are visible in labelsInfo
          // list as well
          //
          return labelsInfo.map(
            function(labelInfo) {
              return labelInfo.selected;
            }
          );
        }
      );
    }

    function showError(notFoundLabels, errorOpts) {
      errorOpts = errorOpts || {};
      errorOpts.code = errorOpts.code || 'specimens.specimen_not_found';
      errorOpts.params = errorOpts.params || {};
      errorOpts.params.label = notFoundLabels.join(', ');
      Alerts.error(errorOpts.code, errorOpts.params);
    }

    function showInsufficientQtyWarning(opts) {
      Util.showConfirm(angular.extend({
        title: "common.warning",
        isWarning: true,
        confirmMsg: "specimens.errors.insufficient_qty",
      }, opts));
    }

    function getSpmnToSave(lineage, spec, parent, qty, fmt) {
      return new Specimen({
        lineage: lineage,
        specimenClass: spec.specimenClass,
        type: spec.type,
        parentId: parent.id,
        initialQty: qty,
        storageLocation: {name: '', positionX:'', positionY: ''},
        status: 'Pending',
        children: [],
        cprId: parent.cprId,
        visitId: parent.visitId,
        createdOn: spec.createdOn,
        freezeThawCycles: spec.freezeThawCycles,
        incrParentFreezeThaw: spec.incrParentFreezeThaw,
        comments: spec.comments,

        selected: true,
        parent: parent,
        depth: parent.depth + 1,
        isOpened: true,
        hasChildren: false,
        labelFmt: fmt
      });
    }

    return {
      collectAliquots: collectAliquots,

      createDerivatives: createDerivatives,

      getNewDerivative: getNewDerivative,

      loadSpecimenClasses: loadSpecimenClasses,

      loadSpecimenTypes: loadSpecimenTypes,

      copyContainerName: copyContainerName,

      getSpecimens: getSpecimens,

      resolveSpecimens: resolveSpecimens,

      showInsufficientQtyWarning: showInsufficientQtyWarning
    };
  })
  .controller('ResolveSpecimensCtrl', function($scope, $modalInstance, labels, Alerts) {
    function init() {
      $scope.labels = labels;
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

    $scope.done = function() {
      var selectedSpmns = $scope.labels.map(
        function(label) {
          return label.selected;
        }
      );

      $modalInstance.close(selectedSpmns);
    }

    init();
  });
