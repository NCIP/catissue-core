
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
      $scope, $translate, $state, $document,
      cpr, visit, 
      Visit, Specimen, PvManager, 
      CollectSpecimensSvc, Container, Alerts, Util) {

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
        initTree($scope.specimens);
      };

      function initTree(specimens) {
        angular.forEach(specimens, function(specimen) {
          specimen.save = true;
          if (specimen.parent == undefined) {
            specimen.showInTree = true;
            createAliquotGrp(specimen);
          }
        });

        // Logic of show/hide of aliquots and in the tree
        angular.forEach(specimens, function(specimen){
          if (specimen.aliquotGrp || specimen.children.length > 0 || specimen.lineage != 'Aliquot') {
            specimen.showInTree = true;
          }

          if (specimen.aliquotGrp) {
            specimen.siblingCount = specimen.aliquotGrp.length;
            var showAllAliquots = false;
            angular.forEach(specimen.aliquotGrp, function(sibling) {
              if (sibling.children.length > 0 || specimen.initialQty != sibling.initialQty) {
                showAllAliquots = true;
              }
            });

            if (showAllAliquots) {
               $scope.expandAliquotsGroup(specimen);
            }
          }
        });
      }

      function createAliquotGrp(specimen) {
        if (!specimen.children[0]) {
          return;
        }

        specimen.children[0].aliquotGrp = [];
        angular.forEach(specimen.children, function(child) {
          createAliquotGrp(child);
          if (child.lineage == 'Aliquot') {
            specimen.children[0].aliquotGrp.push(child)
          }
        });
      }

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
          $scope.specimens[i].removed = true;
          $scope.specimens.splice(i, 1);
        }

        var value = specimen.parent.children[0];
        if (value != undefined) {
          value.siblingCount -= 1;
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
          if (specimen.children[i].removed) {
            continue;
          }

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

          if (uiSpecimen.save) {
            result.push(specimen);
          }

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

      $scope.assignLabels = function(aliquot, allLabels) {
        var labels = Util.splitStr(allLabels, /,|\t|\n/);
          aliquot.siblingCount = labels.length;
        setSpecimens(aliquot);

        angular.forEach(aliquot.aliquotGrp, function(sibling, $index){
          var label = labels[$index];
          Specimen.isUniqueLabel(label).then(function(result) {
            if (!result) {
              Alerts.error('specimens.errors.duplicate_labels');
              return;
            }
          });

          sibling.label = label;
          if (!sibling.label) {
            sibling.save = false;
          }
        });
      }

      $scope.expandAliquotsGroup = function(aliquot) {
        showAliquots(aliquot, true)
        aliquot.expanded = true;
      }

      $scope.collapseAliquotsGroup = function(aliquot) {
        showAliquots(aliquot, false)
        aliquot.expanded = false;
      }

      function showAliquots(aliquot, showInTree) {
        angular.forEach(aliquot.aliquotGrp, function(sibling) {
          if (aliquot != sibling) {
            sibling.showInTree = showInTree;
          }
        });
      }

      function setSpecimens(aliquot) {
        if (aliquot.aliquotGrp.length < aliquot.siblingCount) {
          var newSiblingsCount = aliquot.siblingCount - aliquot.aliquotGrp.length;
          var pos;

          // Fetch the position of the grouped aliquot
          angular.forEach($scope.specimens, function(sp, $index) {
            if (aliquot == sp) {
              pos = $index;
            }
          });

          // Add the newly created aliquot to the last in the aliquot group
          // correspondingly in the flat specimen tree
          for (var i = 0; i < newSiblingsCount; i++) {
            var newSibling = angular.copy(aliquot.aliquotGrp[aliquot.aliquotGrp.length-1]);
            aliquot.parent.children.push(newSibling);
            $scope.specimens.splice(pos + aliquot.aliquotGrp.length, 0, newSibling);
            aliquot.aliquotGrp.push(newSibling);
          }
        }
      }

      $scope.changeQuantity = function(specimen, qty) {
        if (!specimen.expanded) {
          angular.forEach(specimen.aliquotGrp, function(sibling) {
            sibling.initialQty = qty;
          });
        }
      }

      $scope.updateCount = function(specimen) {
        specimen.siblingCount = specimen.newSiblingCount
        setSpecimens(specimen);

        if (specimen.siblingCount < specimen.aliquotGrp.length) {
          // Logic to avoid save of removed specimens
          angular.forEach(specimen.aliquotGrp, function(sibling, $index) {
            if ($index >= specimen.siblingCount) {
              sibling.save = false;
            }
          });

          // Remove from aliquot group
          specimen.aliquotGrp.splice(specimen.siblingCount, specimen.aliquotGrp.length);
        }

        $scope.closePopover();
      }

      $scope.closePopover = function() {
        var popups = $document.find('div.popover');
        angular.forEach(popups, function(popup) {
          angular.element(popup).scope().$hide();
        });
      }

      init();
    });
