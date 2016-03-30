angular.module('os.rde')
  .controller('RdeCollectChildSpecimensCtrl', function($scope, $modal, $state, visits, Specimen, RdeApis) {
    function init() {
      var visitsToReview = [];
      angular.forEach(visits, function(visit) {
        var spmnsToReview = getSpmnsToReview(visit.specimens);
        if (spmnsToReview.length > 0) {
          visit.spmnsToReview = Specimen._flatten(spmnsToReview, 'childrenToReview');
          visit.hasCellSpmns = visit.spmnsToReview.some(function(s) { return s.specimenClass == 'Cell' });
          visitsToReview.push(visit);
        }
      });

      $scope.input = {
        visits: visits,
        visitsToReview: visitsToReview
      }
    }

    function hasAnySpecimenToSave(specimens) {
      return specimens.some(function(s) { return s.toSave || hasAnySpecimenToSave(s.children) });
    }

    function getSpmnsToReview(specimens) {
      var result = [];
      angular.forEach(specimens, function(specimen) {
        if (hasAnySpecimenToSave([specimen])) {
          specimen.childrenToReview = getSpmnsToReview(specimen.children);
          result.push(specimen);
        }
      });

      return result;
    }

    function getAliquotsToSave() {
      var result = {specimens: [], events: []};
      angular.forEach($scope.input.visits, function(visit) {
        var visitData = getVisitAliquotsToSave(visit);
        result.specimens = result.specimens.concat(visitData.specimens);
        result.events = result.events.concat(visitData.events);
      });

      return result;
    }

    function getVisitAliquotsToSave(visit) {
      var result = {specimens: [], events: []}
      for (var i = 0; i < visit.specimens.length; ++i) {
        var primarySpmn = visit.specimens[i];     
        if (hasAnySpecimenToSave(primarySpmn.children)) {
          var dataToSave = getSpmnsToSave(visit, primarySpmn.children);
          result.specimens = result.specimens.concat(dataToSave.specimens);
          result.events = result.events.concat(dataToSave.events);
        }     
      }

      return result;
    }

    function getSpmnsToSave(visit, specimens) {
      var result = {specimens: [], events: []};
      angular.forEach(specimens, function(specimen) {
        if (!specimen.saved && (specimen.toSave || hasAnySpecimenToSave(specimen.children))) {
          var childSpmns = getSpmnsToSave(visit, specimen.children);
          var spmnToSave = {
            id: specimen.id,
            label: specimen.label,
            visitId: visit.id,
            reqId: specimen.reqId,
            lineage: specimen.lineage,
            status: 'Collected',
            storageLocation: specimen.storageLocation,
            initialQty: specimen.initialQty,
            children: childSpmns.specimens
          };
          setFreezingMediaVol(specimen, spmnToSave);
          result.specimens.push(spmnToSave);

          if (specimen.frozenBy || specimen.frozenTime) {
            result.events.push({
              visitId: visit.id, 
              reqId: specimen.id, 
              user: specimen.frozenBy, 
              time: specimen.frozenTime
            });
          }

          result.events = result.events.concat(childSpmns.events);
        } else {
          var childSpmns = getSpmnsToSave(visit, specimen.children);
          result.specimens = result.specimens.concat(childSpmns.specimens); 
          result.events = result.events.concat(childSpmns.events);
        }
      });

      return result;
    }

    function setFreezingMediaVol(spmn, spmnToSave) {
      if (spmn.specimenClass != 'Cell' || (!specimen.volume && specimen.volume != 0)) {
        return;
      }

      var extn = spmnToSave.extensionDetail = spmnToSave.extensionDetail || {}
      extn.attrs = extn.attrs || [];
      var attr = {};
      for (var i = 0; i < extn.attrs.length; ++i) {
        if (extn.attrs[i].name == 'freezingMediaVolume') {
          attr = extn.attrs[i];
          break;
        }
      }

      if (attr.name) {
        attr.value = spmn.volume;
      } else {
        attr = {name: 'freezingMediaVolume', value: spmn.volume};
        extn.attrs.push(attr);
      }
    }

    function markSavedSpmns(savedSpmns) {
      var savedSpmnsMap = getSavedSpmnsMap(savedSpmns, {});
      angular.forEach($scope.input.visits, function(visit) {
        mergeSavedSpmns(visit.specimens, savedSpmnsMap);
      });
    }

    function getSavedSpmnsMap(spmns, savedSpmnsMap) {
      angular.forEach(spmns, function(spmn) {
        if (!spmn.label || spmn.status != 'Collected') {
          return;
        }

        savedSpmnsMap[spmn.label] = spmn;
        getSavedSpmnsMap(spmn.children, savedSpmnsMap);
      });

      return savedSpmnsMap;
    }

    function mergeSavedSpmns(specimens, savedSpmnsMap) {
      angular.forEach(specimens, function(spmn) {
        if (savedSpmnsMap[spmn.label]) {
          spmn.saved = true;
          delete spmn.toSave;
          delete spmn.childrenToReview;
        }

        mergeSavedSpmns(spmn.children, savedSpmnsMap);
      });
    }

    function continueOrExit() {
      $modal.open({
        templateUrl: 'modules/rde/continue-or-exit.html',
        controller: function($scope, $modalInstance) {
          $scope.close = function(answer) {
            $modalInstance.close(answer);
          }
        }
      }).result.then(
        function(moreToAdd) {
          if (moreToAdd) {
            $scope.ctx.visitsSpmns = $scope.input.visits;
            $state.go('rde-select-container');
          } else {
            $state.go('rde-select-workflow');
          }
        }
      );
    }

    $scope.saveAliquots = function() {
      var spmnsToSave = getAliquotsToSave();
      RdeApis.saveChildSpecimens(getAliquotsToSave()).then(
        function(result) {
          markSavedSpmns(result.specimens);
          continueOrExit();                  
        }
      );
    }

    init();
  });
