
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

            specimen.pLabel = !!specimen.label;
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
        initAliquotGrps($scope.specimens);
      };

      function initAliquotGrps(specimens) {
        angular.forEach(specimens, function(specimen, $index) {
          if (specimen.parent == undefined || $index == 0) {
            //
            // Either primary specimen or parent of ad-hoc aliquots
            //
            specimen.showInTree = true;
            createAliquotGrp(specimen);
          }
        });

        // Logic of show/hide of aliquots and in the tree
        angular.forEach(specimens, function(specimen) {
          if (!!specimen.grpLeader) { 
            // A aliquot specimen's show/hide is determined by group leader
            return;
          }

          if (specimen.aliquotGrp || specimen.lineage != 'Aliquot') {
            specimen.showInTree = true;
          }

          if (!specimen.aliquotGrp) {
            return;
          }

          var expandGrp = specimen.aliquotGrp.some(
            function(sibling) {
              return sibling.children.length > 0 || specimen.initialQty != sibling.initialQty;
            }
          );

          if (expandGrp) {
            expandOrCollapseAliquotsGrp(specimen, expandGrp);
          }
        });
      }

      function createAliquotGrp(specimen) {
        if (!specimen.children || specimen.children.length == 0) {
          return;
        }

        var aliquotGrp = [];
        var grpLeader = undefined;
        angular.forEach(specimen.children, function(child) {
          if (child.lineage == 'Aliquot' && child.selected && child.existingStatus != 'Collected') {
            aliquotGrp.push(child);

            if (!grpLeader) {
              grpLeader = child;
            } else {
              child.grpLeader = grpLeader;
            }
          }

          createAliquotGrp(child);
        });

        if (grpLeader) {
          grpLeader.aliquotGrp = aliquotGrp;
          listenContainerChanges(grpLeader);
        }
      }

      function expandOrCollapseAliquotsGrp(aliquot, expandOrCollapse) {
        if (!aliquot.aliquotGrp) {
          return;
        }

        setShowInTree(aliquot, expandOrCollapse)
        aliquot.expanded = expandOrCollapse;
        if (!aliquot.expanded) {
          aliquot.aliquotLabels = aliquot.aliquotGrp.map(
            function(s) {
              return s.label;
            }
          ).join(aliquot.aliquotLabels ? "," : "");
        }

        angular.forEach(aliquot.aliquotGrp, function(sibling){
          if (aliquot != sibling) {
            setChildrenShowInTree(sibling, sibling.showInTree);
          }
        });

      }

      function setShowInTree(aliquot, showInTree) {
        angular.forEach(aliquot.aliquotGrp, function(sibling) {
          if (aliquot != sibling) {
            sibling.showInTree = showInTree;
          }
        });
      }

      function setChildrenShowInTree(aliquot, showInTree) {
        angular.forEach(aliquot.children, function(child) {
          child.showInTree = showInTree;
          if (!child.children[0].aliquotGrp) {
            setChildrenShowInTree(child, showInTree);
          } else {
            child.children[0].showInTree = showInTree;
          }
        });
      }

      function addAliquotsToGrp(grpLeader, newSpmnsCnt) {
        var lastSpmn = grpLeader.aliquotGrp[grpLeader.aliquotGrp.length - 1];
        
        var newSpmns = [];
        var pos = $scope.specimens.indexOf(lastSpmn);
        for (var i = 0; i < newSpmnsCnt; ++i) {
          var newSpmn = angular.copy(lastSpmn);
          grpLeader.aliquotGrp.push(newSpmn);
          grpLeader.parent.children.push(newSpmn);
          $scope.specimens.splice(pos + i + 1, 0, newSpmn);
        }
      }

      function removeAliquotsFromGrp(grpLeader, count) {
        var grp = grpLeader.aliquotGrp;
        for (var i = grp.length - 1; i >= 0 && count >= 1; --i, --count) {
          $scope.remove(grp[i]);
        }
      }

      function listenContainerChanges(specimen) {
        $scope.$watch(
          function() {
            return specimen.storageLocation;
          },
          function() {
            if (specimen.expanded) {
              return;
            }

            angular.forEach(specimen.aliquotGrp, function(aliquot, $index) {
              if ($index == 0) {
                return;
              }

              aliquot.storageLocation = {name: specimen.storageLocation.name};
            });
          }
        );
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

        if (specimen.grpLeader) {
          var grp = specimen.grpLeader.aliquotGrp;
          var grpIdx = grp.indexOf(specimen);
          grp.splice(grpIdx, 1);
        }

        if (specimen.aliquotGrp) {
          angular.forEach(specimen.aliquotGrp, function(aliquot) {
            aliquot.selected = false;
            aliquot.removed = true;
          });
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

        if (!specimen.expanded) {
          angular.forEach(specimen.aliquotGrp, function(sibling) {
            sibling.status = specimen.status;
          });
        }
      };
        
      $scope.saveSpecimens = function() {
        if (areDuplicateLabelsPresent($scope.specimens)) {
          Alerts.error('specimens.errors.duplicate_labels');
          return;
        }

        var specimensToSave = getSpecimensToSave($scope.cp, $scope.specimens, []);
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

      function getSpecimensToSave(cp, uiSpecimens, visited) {
        var result = [];
        angular.forEach(uiSpecimens, function(uiSpecimen) {
          if (visited.indexOf(uiSpecimen) >= 0 || // already visited
              !uiSpecimen.selected || // not selected
              (uiSpecimen.existingStatus == 'Collected' && 
                !uiSpecimen.closeAfterChildrenCreation)) { // collected and not close after children creation
            return;
          }

          visited.push(uiSpecimen);

          if ((cp.manualSpecLabelEnabled || !uiSpecimen.labelFmt) && !uiSpecimen.label) {
            if (!uiSpecimen.grpLeader.expanded) {
              //
              // Specimen label is not specified when expected but aliquot group is
              // in collapsed state. Therefore ignore the specimen or do not save
              //
              return;
            }
          }


          var specimen = getSpecimenToSave(uiSpecimen);
          specimen.children = getSpecimensToSave(cp, uiSpecimen.children, visited);
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

      $scope.assignLabels = function(aliquot, labels) {
        var labels = Util.splitStr(labels, /,|\t|\n/);
        var newSpmnsCnt = labels.length - aliquot.aliquotGrp.length;
        if (newSpmnsCnt > 0) {
          addAliquotsToGrp(aliquot, newSpmnsCnt);
        }

        angular.forEach(aliquot.aliquotGrp, function(spmn, $index) {
          if ($index < labels.length) {
            spmn.label = labels[$index];
            spmn.selected = true;
            spmn.removed = false;
          } 
        });
      }

      $scope.expandAliquotsGroup = function(aliquot) {
        expandOrCollapseAliquotsGrp(aliquot, true);
      }

      $scope.collapseAliquotsGroup = function(aliquot) {
        expandOrCollapseAliquotsGrp(aliquot, false);
      }

      $scope.changeQuantity = function(specimen, qty) {
        if (!specimen.expanded) {
          angular.forEach(specimen.aliquotGrp, function(sibling) {
            sibling.initialQty = qty;
          });
        }
      }

      $scope.updateCount = function(specimen) {
        var grp = specimen.aliquotGrp;
        var grpLen = grp.length;

        if (specimen.newAliquotsCnt < grpLen) {
          removeAliquotsFromGrp(specimen, grpLen - specimen.newAliquotsCnt);
        } else {
          addAliquotsToGrp(specimen, specimen.newAliquotsCnt - grpLen);
        }     

        Util.hidePopovers();
      }

      $scope.closePopover = function() {
        Util.hidePopovers();
      }
      
      init();
    });
