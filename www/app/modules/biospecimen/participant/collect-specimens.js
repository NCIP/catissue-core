
angular.module('os.biospecimen.participant.collect-specimens', 
  [ 
    'os.biospecimen.models'
  ])
  .factory('CollectSpecimensSvc', function($state, Container) {
    var data = {};

    function getReservePositionsOp(cpId, specimens) {
      var aliquots = {}, result = [];
      angular.forEach(specimens,
        function(specimen) {
          if (specimen.storageType == 'Virtual' || (!!specimen.status && specimen.status != 'Pending')) {
            return;
          }

          if (specimen.lineage == 'Aliquot') {
            var key = specimen.parent.id + "-" + specimen.parent.reqId;
            var tenantDetail = aliquots[key];
            if (!tenantDetail) {
              aliquots[key] = tenantDetail = {
                lineage: specimen.lineage,
                specimenClass: specimen.specimenClass,
                specimenType: specimen.type,
                numOfAliquots: 0
              };

              result.push(tenantDetail);
            }

            tenantDetail.numOfAliquots++;
          } else {
            result.push({
              lineage: specimen.lineage,
              specimenClass: specimen.specimenClass,
              specimenType: specimen.type
            })
          }
        }
      );

      return {cpId: cpId, tenants: result};
    }

    function assignReservedPositions(specimens, positions) {
      var idx = 0;
      angular.forEach(specimens,
        function(specimen) {
          if (specimen.storageType == 'Virtual' || (!!specimen.status && specimen.status != 'Pending')) {
            return;
          }

          specimen.storageLocation = positions[idx++];
        }
      );
    }

    return {
      collect: function(stateDetail, visit, specimens, ignoreQtyWarn) {
        data.specimens = specimens;
        data.stateDetail = stateDetail;
        data.visit = visit;
        data.ignoreQtyWarn = ignoreQtyWarn;

        Container.getReservedPositions(getReservePositionsOp(visit.cpId, specimens)).then(
          function(positions) {
            if (positions.length > 0) {
              assignReservedPositions(specimens, positions);
            }
            $state.go('participant-detail.collect-specimens', {visitId: visit.id, eventId: visit.eventId});
          }
        );
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
      },

      isIgnoreQtyWarn: function() {
        return data.ignoreQtyWarn;
      }
    };
  })
  .controller('CollectSpecimensCtrl', 
    function(
      $scope, $translate, $state, $document, $q,
      cpr, visit, 
      Visit, Specimen, PvManager, 
      CollectSpecimensSvc, Container, Alerts, Util, SpecimenUtil) {

      var ignoreQtyWarning = false;

      function init() {
        ignoreQtyWarning = CollectSpecimensSvc.isIgnoreQtyWarn() || false;
        $scope.specimens = CollectSpecimensSvc.getSpecimens().map(
          function(specimen) {
            specimen.existingStatus = specimen.status;
            specimen.isVirtual = specimen.showVirtual();
            specimen.initialQty = Util.getNumberInScientificNotation(specimen.initialQty);
            if (specimen.status != 'Collected') {
              specimen.status = 'Collected';
              specimen.printLabel = (specimen.labelAutoPrintMode == 'ON_COLLECTION');
            }

            if (specimen.closeAfterChildrenCreation) {
              specimen.selected = true;
            }

            specimen.pLabel = !!specimen.label;
            return specimen;
          }
        );

        visit.visitDate = visit.visitDate || visit.anticipatedVisitDate || new Date();
        visit.cprId = cpr.id;
        delete visit.anticipatedVisitDate;
        $scope.visit = visit;
        $scope.autoPosAllocate = !!$scope.cp.containerSelectionStrategy;
        
        $scope.collDetail = {
          collector: undefined,
          collectionDate: new Date(),
          receiver: undefined,
          receiveDate: new Date()
        };

        loadPvs();
        initAliquotGrps($scope.specimens);
        $scope.$on('$destroy', vacateReservedPositions);
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

          initAliquotGrpPrintLabel(specimen);
          specimen.aliquotLabels = getAliquotGrpLabels(specimen);
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
          aliquot.aliquotLabels = getAliquotGrpLabels(aliquot);
        }
      }

      function initAliquotGrpPrintLabel(aliquot) {
        if (aliquot.expanded) {
          return;
        }

        var printLabel = aliquot.aliquotGrp.some(
          function(sibling) {
            return sibling.printLabel;
          }
        );

        if (!printLabel) {
          return;
        }

        aliquot.printLabel = printLabel;
        setAliquotGrpPrintLabel(aliquot);
      }

      function setAliquotGrpPrintLabel(aliquot) {
        if (aliquot.expanded || !aliquot.aliquotGrp) {
          return;
        }

        angular.forEach(aliquot.aliquotGrp, function(sibling) {
          sibling.printLabel = aliquot.printLabel;
        });
      }

      function getAliquotGrpLabels(specimen) {
        return specimen.aliquotGrp.filter(
          function(s) {
            return !!s.label;
          }
        ).map(
          function(s) {
            return s.label;
          }
        ).join(",");
      }

      function vacateReservedPositions() {
        for (var i = 0; i < $scope.specimens.length; ++i) {
          var loc = $scope.specimens[i].storageLocation;
          if (loc && loc.reservationId) {
            return Container.cancelReservation(loc.reservationId);
          }
        }

        return null;
      }

      function setShowInTree(aliquot, showInTree) {
        angular.forEach(aliquot.aliquotGrp, function(specimen) {
          if (specimen == aliquot) {
            return;
          }

          if (showInTree) {
            specimen.showInTree = true;
            showSpecimenInTree(specimen);
          } else {
            hideSpecimenInTree(specimen);
          }
        });
      }

      function showSpecimenInTree(specimen) {
        if (specimen.grpLeader && (!specimen.children || specimen.children.length == 0)) {
          return;
        }

        specimen.showInTree = true;
        angular.forEach(specimen.children, function(child) {
          showSpecimenInTree(child);
        });
      }

      function hideSpecimenInTree(specimen) {
        specimen.showInTree = false;
        if (specimen.children.length > 0) {
          angular.forEach(specimen.children, function(child) {
            hideSpecimenInTree(child);
          });
        }
      }

      function addAliquotsToGrp(grpLeader, newSpmnsCnt) {
        var lastSpmn = grpLeader.aliquotGrp[grpLeader.aliquotGrp.length - 1];

        var newSpmns = [];
        var pos = $scope.specimens.indexOf(lastSpmn);
        for (var i = 0; i < newSpmnsCnt; ++i) {
          var newSpmn = shallowCopy(lastSpmn);
          grpLeader.aliquotGrp.push(newSpmn);
          grpLeader.parent.children.push(newSpmn);
          $scope.specimens.splice(pos + i + 1, 0, newSpmn);
        }
      }

      function shallowCopy(spmn) {
        var copy = new Specimen(spmn);
        copy.storageLocation = !spmn.storageLocation ? {} : {name: spmn.storageLocation.name}
        copy.children = [];
        return copy;
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
          function(newVal, oldVal) {
            if (newVal == oldVal) {
              return;
            }

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
          if ($scope.specimens[i].existingStatus != 'Collected' && $scope.specimens[i].storageType != 'Virtual') {
            $scope.specimens[i].storageLocation = {name: containerName};
          }
        }
      };

      $scope.manuallySelectContainers = function() {
        $q.when(vacateReservedPositions()).then(
          function() {
            angular.forEach($scope.specimens,
              function(spmn) {
                spmn.storageLocation = {};
              }
            );

            $scope.autoPosAllocate = false;
          }
        );
      }

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
        } else if (specimen.aliquotGrp) {
          if (!specimen.expanded) {
            angular.forEach(specimen.aliquotGrp, function(aliquot) {
              aliquot.selected = false;
              aliquot.removed = true;
            });
          } else {
            // logic of changing group leader.
            adjustGrpLeader(specimen);
          }
        }
      };

      function adjustGrpLeader(specimen) {
        if (!specimen.aliquotGrp) {
          return;
        }

        var members = specimen.aliquotGrp.splice(1);
        var newLeader = members.length > 0 ? members[0] : null;
        if (!newLeader) {
          return;
        }

        newLeader.aliquotGrp = members;
        newLeader.expanded = true;
        newLeader.grpLeader = null;
        angular.forEach(members, function(member) {
          if (member != newLeader) {
            member.grpLeader = newLeader;
          }
        });
      }

      function handleSpecimensPoolStatus(specimen) {
        var pooledSpmn = specimen.pooledSpecimen;
        if (!pooledSpmn) {
          return;
        }

        var allSameStatus = pooledSpmn.specimensPool.every(
          function(s) {
            return s.status == specimen.status;
          }
        );

        if (allSameStatus|| pooledSpmn.status == 'Missed Collection') {
          pooledSpmn.status = specimen.status;
        } else if (specimen.status != 'Collected' && pooledSpmn.status == 'Collected') {
          var atLeastOneColl = pooledSpmn.specimensPool.some(
            function(s) {
              return s.status == 'Collected';
            }
          );

          if (!atLeastOneColl) {
            pooledSpmn.status = specimen.status;
          }
        }
      }  

      $scope.statusChanged = function(specimen) {
        setDescendentStatus(specimen); 

        if (specimen.status == 'Collected') {
          var curr = specimen.parent;
          while (curr) {
            curr.status = specimen.status;
            curr = curr.parent;
          }
        }
        
        handleSpecimensPoolStatus(specimen);

        if (!specimen.expanded) {
          angular.forEach(specimen.aliquotGrp, function(sibling) {
            sibling.status = specimen.status;
          });
        }
      };

      $scope.togglePrintLabels = setAliquotGrpPrintLabel;
        
      $scope.saveSpecimens = function() {
        if (areDuplicateLabelsPresent($scope.specimens)) {
          Alerts.error('specimens.errors.duplicate_labels');
          return;
        }

        if (!ignoreQtyWarning && !areAliquotsQtyOk($scope.specimens)) {
          return;
        }

        var specimensToSave = getSpecimensToSave($scope.cp, $scope.specimens, []);
        if (!!$scope.visit.id && $scope.visit.status == 'Complete') {
          Specimen.save(specimensToSave).then(
            function() {
              $scope.specimens.length = 0;
              CollectSpecimensSvc.clear();
              $scope.back();
            }
          );
        } else {
          var visitToSave = angular.copy($scope.visit);
          visitToSave.status = 'Complete';

          var payload = {visit: visitToSave, specimens: specimensToSave};
          Visit.collectVisitAndSpecimens(payload).then(
            function(result) {
              var visitId = result.data.visit.id;
              var sd = CollectSpecimensSvc.getStateDetail();
              $scope.specimens.length = 0;
              CollectSpecimensSvc.clear();
              $state.go(sd.state.name, angular.extend(sd.params, {visitId: visitId}));
            }
          );
        }
      };

      function descendentCount(specimen, onlySelected) {
        onlySelected = (onlySelected != false);

        var count = 0;
        angular.forEach(specimen.children, function(child) {
          if (child.removed || (!child.selected && onlySelected)) {
            return;
          }

          count += 1 + descendentCount(child);
        });

        angular.forEach(specimen.specimensPool, function(poolSpmn) {
          if (poolSpmn.removed || (!poolSpmn.selected && onlySelected)) {
            return;
          }

          count += 1 + descendentCount(poolSpmn);
        });

        return count;
      };

      function setDescendentStatus(specimen) {
        angular.forEach(specimen.specimensPool, 
          function(poolSpmn) {
            poolSpmn.status = specimen.status;
          }
        );

        angular.forEach(specimen.children,
          function(child) {
            child.status = specimen.status;
            setDescendentStatus(child);
          }
        );
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
        angular.forEach(uiSpecimens, 
          function(uiSpecimen) {
            if (visited.indexOf(uiSpecimen) >= 0 || // already visited
                !uiSpecimen.selected || // not selected
                (uiSpecimen.existingStatus == 'Collected' && 
                !uiSpecimen.closeAfterChildrenCreation)) {
                // collected and not close after children creation
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
            specimen.specimensPool = getSpecimensToSave(cp, uiSpecimen.specimensPool, visited);
            result.push(specimen);
            return result;
          }
        );

        return result;
      };

      function getSpecimenToSave(uiSpecimen) { // Make it object Specimen and do checks like isNew/isCollected
        var specimen = {
          id: uiSpecimen.id,
          initialQty: uiSpecimen.initialQty,
          label: uiSpecimen.label,
          printLabel: uiSpecimen.printLabel,
          reqId: uiSpecimen.reqId,
          visitId: $scope.visit.id,
          storageLocation: uiSpecimen.storageLocation,
          parentId: angular.isDefined(uiSpecimen.parent) ? uiSpecimen.parent.id : undefined,
          lineage: uiSpecimen.lineage,
          concentration: uiSpecimen.concentration,
          status: uiSpecimen.status,
          closeAfterChildrenCreation: uiSpecimen.closeAfterChildrenCreation,
          createdOn: uiSpecimen.lineage != 'New' ? uiSpecimen.createdOn : undefined,
          freezeThawCycles: uiSpecimen.freezeThawCycles,
          incrParentFreezeThaw: uiSpecimen.incrParentFreezeThaw,
          comments: uiSpecimen.comments,
          extensionDetail: uiSpecimen.extensionDetail
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

      function areAliquotsQtyOk(specimens) {
        for (var i = 0; i < specimens.length; i++) {
          var specimen = specimens[i];
          if (!specimen.children || specimen.children.length == 0) {
            continue;
          }

          var aliquots = specimen.children.filter(
            function(child) {
               return child.selected && child.lineage == 'Aliquot' && child.existingStatus != 'Collected';
             }
          );

          var aliquotsQty = aliquots.reduce(
            function(sum, aliquot) {
              return sum + (!aliquot.initialQty ? 0 : +aliquot.initialQty);
            }, 0);

          var parentQty = specimen.existingStatus == 'Collected' ? specimen.availableQty : specimen.initialQty;
          if (parentQty != undefined && parentQty < aliquotsQty) {
            showInsufficientQtyWarning();
            return false;
          }

          if (!areAliquotsQtyOk(specimen.children)) {
            return false;
          }
        };

        return true;
      }

      function showInsufficientQtyWarning() {
        SpecimenUtil.showInsufficientQtyWarning({
          ok: function () {
            ignoreQtyWarning = true;
            $scope.saveSpecimens();
          }
        });
      }

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
