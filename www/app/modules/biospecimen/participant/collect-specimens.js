
angular.module('os.biospecimen.participant.collect-specimens', 
  [ 
    'os.biospecimen.models',
    'os.biospecimen.participant.specimen-position'
  ])
  .factory('CollectSpecimensSvc', function($state) {
    var data = {};
    return {
      collect: function(stateDetail, visit, specimens) {
        data.specimens = specimens;
        data.stateDetail = stateDetail;
        data.visit = visit;
        $state.go('participant-detail.collect-specimens', {visitId: visit.id, eventId: visit.eventId});
      },

      clear: function() {
        data.specimens = [];
        data.visit = undefined;
        data.stateDetail = undefined;
      },

      getSpecimens: function() {
        return data.specimens || []; 
      },

      getVisit: function() {
        return data.visit;
      },

      getStateDetail: function() {
        return data.stateDetail;
      }
    };
  })
  .controller('CollectSpecimensCtrl', 
    function(
      $scope, $translate, $state,
      cpr, visit, 
      Visit, Specimen, PvManager, 
      CollectSpecimensSvc, Container, Alerts) {

      function init() {
        $scope.specimens = CollectSpecimensSvc.getSpecimens().map(
          function(specimen) {
            specimen.existingStatus = specimen.status;
            if (specimen.status != 'Collected') {
              specimen.status = 'Collected';
            }

            if (specimen.closeAfterChildrenCreation) {
              specimen.selected = true;
            }

            return specimen;
          }
        );

        visit.visitDate = visit.visitDate || visit.anticipatedVisitDate;
        visit.cprId = cpr.id;
        delete visit.anticipatedVisitDate;
        $scope.visit = visit;
        
        $scope.collDetail = {
          collector: undefined,
          collectionDate: new Date(),
          receiver: undefined,
          receiveDate: new Date()
        };

        loadPvs();
      };

      function loadPvs() {
        $scope.notSpecified = $translate.instant('pvs.not_specified');
        $scope.sites = PvManager.getSites();
        $scope.specimenStatuses = PvManager.getPvs('specimen-status');
      };

      $scope.applyFirstLocationToAll = function() {
        var containerName = undefined;
        for (var i = 0; i < $scope.specimens.length; ++i) {
          if ($scope.specimens[i].isOpened && $scope.specimens[i].existingStatus != 'Collected') {
            containerName = $scope.specimens[i].storageLocation.name;
            break;
          }
        }

        for (var i = 1; i < $scope.specimens.length; i++) {
          if ($scope.specimens[i].existingStatus != 'Collected') {
            $scope.specimens[i].storageLocation = {name: containerName};
          }
        }
      };

      $scope.openSpecimenNode = function(specimen) {
        specimen.isOpened = true;
      };

      $scope.closeSpecimenNode = function(specimen) {
        specimen.isOpened = false;
      };

      $scope.remove = function(specimen) {
        var idx = $scope.specimens.indexOf(specimen);
        var descCnt = descendentCount(specimen);

        for (var i = idx + descCnt; i >= idx; --i) {
          $scope.specimens[i].selected = false;
          $scope.specimens.splice(i, 1);
        }
      };

      $scope.statusChanged = function(specimen) {
        setDescendentStatus(specimen); 

        if (specimen.status == 'Collected') {
          var curr = specimen.parent;
          while (curr) {
            curr.status = specimen.status;
            curr = curr.parent;
          }
        }
      };
        
      $scope.saveSpecimens = function() {
        if (areDuplicateLabelsPresent($scope.specimens)) {
          Alerts.error('specimens.errors.duplicate_labels');
          return;
        }

        var specimensToSave = getSpecimensToSave($scope.specimens, []);
        if (!!$scope.visit.id && $scope.visit.status == 'Complete') {
          Specimen.save(specimensToSave).then(
            function() {
              CollectSpecimensSvc.clear();
              $scope.back();
            });
        } else {
          var visitToSave = angular.copy($scope.visit);
          visitToSave.status = 'Complete';

          var payload = {visit: visitToSave, specimens: specimensToSave};
          Visit.collectVisitAndSpecimens(payload).then(
            function(result) {
              var visitId = result.data.visit.id;
              var sd = CollectSpecimensSvc.getStateDetail();
              CollectSpecimensSvc.clear();
              $state.go(sd.state.name, angular.extend(sd.params, {visitId: visitId}));
            });
        }
      };

      function descendentCount(specimen) { 
        var count = 0;
        for (var i = 0; i < specimen.children.length; ++i) {
          count += 1 + descendentCount(specimen.children[i]);
        }

        return count;
      };

      function setDescendentStatus(specimen) {
        for (var i = 0; i < specimen.children.length; ++i) {
          specimen.children[i].status = specimen.status;
          setDescendentStatus(specimen.children[i]);
        }
      };

      function areDuplicateLabelsPresent(input) {
        var labels = [];
        for (var i = 0; i < input.length; ++i) {
          if (!!input[i].label && labels.indexOf(input[i].label) != -1) {
            return true;
          }

          labels.push(input[i].label);
        }

        return false;
      };

      function getSpecimensToSave(uiSpecimens, visited) {
        var result = [];
        angular.forEach(uiSpecimens, function(uiSpecimen) {
          if (visited.indexOf(uiSpecimen) >= 0 || // already visited
              !uiSpecimen.selected || // not selected
              (uiSpecimen.existingStatus == 'Collected' && 
                !uiSpecimen.closeAfterChildrenCreation)) { // collected and not close after children creation
            return;
          }

          visited.push(uiSpecimen);

          var specimen = getSpecimenToSave(uiSpecimen);
          specimen.children = getSpecimensToSave(uiSpecimen.children, visited);
          result.push(specimen);
          return result;
        });

        return result;
      };

      function getSpecimenToSave(uiSpecimen) { // Make it object Specimen and do checks like isNew/isCollected
        var specimen = {
          id: uiSpecimen.id,
          initialQty: uiSpecimen.initialQty,
          label: uiSpecimen.label,
          reqId: uiSpecimen.reqId,
          visitId: $scope.visit.id,
          storageLocation: uiSpecimen.storageLocation,
          parentId: angular.isDefined(uiSpecimen.parent) ? uiSpecimen.parent.id : undefined,
          lineage: uiSpecimen.lineage,
          concentration: uiSpecimen.concentration,
          status: uiSpecimen.status,
          closeAfterChildrenCreation: uiSpecimen.closeAfterChildrenCreation,
          createdOn: uiSpecimen.lineage != 'New' ? uiSpecimen.createdOn : undefined
        };

        if (specimen.lineage == 'New' && specimen.status == 'Collected') {
          specimen.collectionEvent = {
            user: $scope.collDetail.collector,
            time: $scope.collDetail.collectionDate
          };

          specimen.receivedEvent = {
            user: $scope.collDetail.receiver,
            time: $scope.collDetail.receiveDate
          };
        }

        if (!!specimen.reqId || specimen.lineage == 'Aliquot') {
          return specimen;
        }

        specimen.specimenClass = uiSpecimen.specimenClass;
        specimen.type = uiSpecimen.type;

        if (uiSpecimen.lineage == 'Derived') {
          return specimen;
        }

        specimen.pathology = uiSpecimen.pathology;
        specimen.anatomicSite = uiSpecimen.anatomicSite;
        specimen.laterality = uiSpecimen.laterality;
        return specimen;
      };

      init();
    });
