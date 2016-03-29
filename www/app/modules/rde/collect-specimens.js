angular.module('os.rde')
  .controller('rdeCollectSpecimensCtrl', 
    function(
      $rootScope, $scope, $state, $http, $q, $modal, $translate, $filter,
      SpmnCollSession, CollectionProtocol, CollectionProtocolEvent, SpecimenRequirement, Visit,
      Specimen, Container, CpConfigSvc, Alerts, Util, ApiUrls, ContainerUtil, LocalStorageSvc, DeleteUtil) {

      var baseUrl = ApiUrls.getBaseUrl();
      var cpBdeCfg = {};

      var stateInitMap = {
        'printLabels': initPrintLabels
      }

      function init() {
        SpmnCollSession.query().then(
          function(result) {
            $scope.sessions = result;
            if (!$scope.sessions || $scope.sessions.length == 0) {
              $scope.newSession();
            } else {
              $scope.ctx = {view: 'listSessions'};
            }
        });
      }

      function setContListOpts(visits) {
        $scope.ctx.contListOpts.criteria.cpShortTitle = 
          visits.filter(function(visit) { return visit.specimens.length > 0; })
            .map(function(visit) { return visit.cpShortTitle; });
      }

      function onSaveVisit(visitsSpmns) {
        var visits = [];
        var cpIds = [];
        angular.forEach(visitsSpmns, function(visitSpmns) {
          var visit = visitSpmns.visit;
          visit.specimens = visitSpmns.specimens;
          visits.push(visit);
          if (cpIds.indexOf(visit.cpId) == -1) {
            cpIds.push(visit.cpId);
          }
        });

        var promises = loadWorkflowCfg(cpIds);
        $q.all(promises).then(
          function() {
            loadPrimarySpmns(visits);
            $scope.visits = visits;
            $scope.ctx.visits = $scope.visits;
            $scope.ctx.view = 'collectPrimarySpmns';
            saveSession();
          }
        );
      }

      function loadWorkflowCfg(cpIds) {
        return cpIds.map(
          function(cpId) {
            return CollectionProtocol.getWorkflows(cpId).then(
              function(result) {
                cpBdeCfg[result.shortTitle] = result.workflows.bde || {};
              } 
            );
          }
        );
      }

      function loadPrimarySpmns(visits) {
        var user = $rootScope.currentUser;
        var time = new Date().getTime();

        angular.forEach(visits, function(visit) {
          visit.collDetail = {user: user, time: time};
          visit.recvDetail = {user: user, time: time};

          visit.primarySpmnLabels = '';

          var cfg = cpBdeCfg[visit.cpShortTitle].data || {};
          visit.scanEnabled = (cfg.primarySpmnScanning == true);
             
          var scanRequired = false;
          angular.forEach(visit.specimens, function(specimen) {
            if (!visit.scanEnabled || (cfg.spmnScanning && cfg.spmnScanning[specimen.reqId] == false)) {
              specimen.status = 'Collected';
            } else {
              specimen.status = 'Missed Collection';
              specimen.scanEnabled = true;
              scanRequired = true;
            }
           
            angular.forEach(specimen.specimensPool, function(poolSpmn) {
              poolSpmn.status = specimen.status;
              poolSpmn.scanEnabled = specimen.scanEnabled;
            });
          });

          visit.scanEnabled = scanRequired;
          setPrimarySpmns(visit);
        });
      }

      function setPrimarySpmns(visit) {
        visit.primarySpmns = [];
        angular.forEach(visit.specimens, function(specimen) {
          if (!specimen.specimensPool) {
            visit.primarySpmns.push(specimen);
          } else {
            visit.primarySpmns = visit.primarySpmns.concat(specimen.specimensPool);
          }
        });
      }
      
      function initPrimarySpmns() {
        angular.forEach($scope.visits, function(visit) {
          setPrimarySpmns(visit);
        });
      }

      function getMissedChildSpmns(visit, childSpmns) {
        if (!childSpmns) {
          return [];
        }

        return childSpmns.map(
          function(spmn) {
            return {
              id: spmn.id,
              visitId: visit.id,
              reqId: spmn.reqId,
              label: spmn.label,
              status: 'Missed Collection',
              initialQty: spmn.initialQty,
              lineage: spmn.lineage,
              children: getMissedChildSpmns(visit, spmn.children)
            }
          }
        );
      }

      function removeMissedSpmns(visits, savedSpmns) {
        var missedLabels = [];
        angular.forEach(savedSpmns, function(spmn) {
          if (spmn.status == 'Missed Collection') {
            missedLabels.push(spmn.label);
          }

          angular.forEach(spmn.specimensPool, function(poolSpmn) {
            if (poolSpmn.status == 'Missed Collection') {
              missedLabels.push(poolSpmn.label);
            }
          });
        });

        if (missedLabels.length == 0) {
          return;
        }

        for (var i = 0; i < visits.length; ++i) {
          for (var j = visits[i].specimens.length - 1; j >= 0; j--) {
            var spmn = visits[i].specimens[j];
            if (missedLabels.indexOf(spmn.label) != -1) {
              visits[i].specimens.splice(j, 1);
            } else if (spmn.specimensPool) {
              for (var k = spmn.specimensPool.length - 1; k >= 0; k--) {
                if (missedLabels.indexOf(spmn.specimensPool[k].label) != -1) {
                  spmn.specimensPool.splice(k, 1);
                }
              }
            }
          }
        }
      }

      function showContainerSelector() {
        $scope.ctx.view = 'selectContainer'
        $scope.ctx.virtualAliquots = false;
        $scope.ctx.aliquotErrors = [];
        $scope.ctx.aliquotLabels = '';
        saveSession();
      }

      function getAliquotsFromMap() {
        var aliquots = [];
        var containerName = $scope.ctx.selectedContainer.name;
        angular.forEach($scope.ctx.contOccupancyMap, function(pos) {
          if (!!pos.id) {
            return;
          }

          aliquots.push({
            label: pos.occupyingEntityName, 
            storageLocation: {name: containerName, positionX: pos.posOne, positionY: pos.posTwo}
          });
        });

        return aliquots;
      }

      function processAliquot(aliquot) {
        var resultStatuses = [
          'ok',
          'spmn_already_collected',
          'primary_spmn_not_collected'
        ];

        for (var i = 0; i < $scope.visits.length; ++i) {
          var result = assignPosition($scope.visits[i], aliquot);

          if (resultStatuses.indexOf(result.status) != -1) {
            return result;
          }
        }

        return {status: 'no_spmn_in_visit'};
      }

      function assignPosition(visit, aliquot) {
        var result = undefined;
        var req = undefined;
        for (var i = 0; i < visit.specimens.length; ++i) {
          var primarySpmn = visit.specimens[i];
          req = searchRequirement(visit.name, primarySpmn.children, aliquot);
          if (req) {
            break;
          }     
        };

        if (!req) {
          return {status: 'no_spmn_in_visit'};
        }

        if (req.saved || req.status == 'Collected') {
          return {status: 'spmn_already_collected'};
        }

        if (primarySpmn.status != 'Collected') {
          return {status: 'primary_spmn_not_collected'};
        }

        req.toSave = true;
        req.visitId = visit.id;
        req.frozenBy = $rootScope.currentUser;       
        req.frozenTime = new Date().getTime();
        angular.extend(req, aliquot);
        return {status: 'ok', visit: visit, specimen: req};
      }

      function searchRequirement(visitName, spmnReqs, aliquot) {
        for (var i = 0; i < spmnReqs.length; ++i) {
          if (spmnReqs[i].label == aliquot.label) {
            return spmnReqs[i];
          }

          var result = searchRequirement(visitName, spmnReqs[i].children, aliquot);
          if (result) {
            return result;
          }
        }

        return undefined;
      }

      function cleanupAssignedPositions() {
        angular.forEach($scope.visits, function(visit) {
          angular.forEach(visit.specimens, function(specimen) {
            cleanupSpmnPositions(specimen.children);
          });
        });
      }

      function cleanupSpmnPositions(specimens) {
        angular.forEach(specimens, function(specimen) {
          if (specimen.toSave) {
            specimen.toSave = specimen.storageLocation = undefined;
            specimen.frozenBy = specimen.frozenTime = undefined;
          }

          cleanupSpmnPositions(specimen.children);
        });
      }

      function getAliquotsToSave() {
        var result = {specimens: [], events: []};
        angular.forEach($scope.visits, function(visit) {
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
            var dataToSave = getSpecimensToSave(visit, primarySpmn.children);
            result.specimens = result.specimens.concat(dataToSave.specimens);
            result.events = result.events.concat(dataToSave.events);
          }     
        }

        return result;
      }

      function hasAnySpecimenToSave(specimens) {
        for (var i = 0; i < specimens.length; ++i) {
          if (specimens[i].toSave || hasAnySpecimenToSave(specimens[i].children)) {
            return true;
          }     
        }

        return false;
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

      function getSpecimensToSave(visit, specimens) {
        var result = {specimens: [], events: []};
        angular.forEach(specimens, function(specimen) {
          if (!specimen.saved && (specimen.toSave || hasAnySpecimenToSave(specimen.children))) {
            var childSpmns = getSpecimensToSave(visit, specimen.children);
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
            result.specimens.push(spmnToSave);

            if (specimen.specimenClass == 'Cell' && (!!specimen.volume || specimen.volume == 0)) {
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
                attr.value = specimen.volume;
              } else {
                attr = {name: 'freezingMediaVolume', value: specimen.volume};
                extn.attrs.push(attr);
              }
            }

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
            var childSpmns = getSpecimensToSave(visit, specimen.children);
            result.specimens = result.specimens.concat(childSpmns.specimens); 
            result.events = result.events.concat(childSpmns.events);
          }
        });

        return result;
      }

      function markSavedSpmns(savedSpmns) {
        var savedSpmnsMap = {};
        initSavedSpmnsMap(savedSpmns, savedSpmnsMap);

        angular.forEach($scope.visits, function(visit) {
          mergeSavedSpmns(visit.name, visit.specimens, savedSpmnsMap);
        });

      }

      function initSavedSpmnsMap(spmns, savedSpmnsMap) {
        angular.forEach(spmns, function(spmn) {
          if (!spmn.label || spmn.status != 'Collected') {
            return;
          }

          savedSpmnsMap[spmn.label] = spmn;
          initSavedSpmnsMap(spmn.children, savedSpmnsMap);
        });
      }

      function mergeSavedSpmns(visitName, specimens, savedSpmnsMap) {
        angular.forEach(specimens, function(spmn) {
          if (savedSpmnsMap[spmn.label]) {
            spmn.saved = true;
            delete spmn.toSave;
            delete spmn.childrenToReview;
          }

          mergeSavedSpmns(visitName, spmn.children, savedSpmnsMap);
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
              showContainerSelector();
            } else {
              deleteSession($scope.session, true);
            }
          }
        );
      }

      function initPrintLabels() {
        angular.forEach($scope.ctx.visits, function(visit) {
          visit.specimensForPrint = Specimen.flatten(visit.specimens);
          angular.forEach(visit.specimensForPrint, function(spmn) {
            spmn.parent = undefined;

            if (!!spmn.pooledSpecimen) {
              spmn.pooled = true;
              spmn.pooledSpecimen = undefined;
            }

            if (!angular.isDefined(spmn.selected)) {
              spmn.selected = (spmn.labelAutoPrintMode == 'ON_COLLECTION');
            }
          });
        });

        $scope.ctx.view = 'printLabels';
      }

      function getSpmnsToPrint(visit, specimens) {
        var result = [];
        angular.forEach(specimens, function(specimen) {
          if (!hasAnySpmnToPrint(specimen)) {
            return;
          }

          var spmnToPrint = {
            label: specimen.label,
            reqId: specimen.reqId,
            visitName: visit.name,
            print: specimen.selected
          };

          spmnToPrint.specimensPool = getSpmnsToPrint(visit, specimen.specimensPool);
          spmnToPrint.children = getSpmnsToPrint(visit, specimen.children);
          result.push(spmnToPrint);
        });

        return result;
      }

      function hasAnySpmnToPrint(specimen) {
        if (specimen.selected) {
          return true;
        }

        if (specimen.specimensPool) {
          for (var i = 0; i < specimen.specimensPool.length; ++i) {
            if (hasAnySpmnToPrint(specimen.specimensPool[i])) {
              return true;
            }
          }
        }

        if (specimen.children) {
          for (var i = 0; i < specimen.children.length; ++i) {
            if (hasAnySpmnToPrint(specimen.children[i])) {
              return true;
            }
          }
        }

        return false;
      }

      $scope.removeVisit = function(visit) {
        var idx = $scope.visits.indexOf(visit);
        if (idx == -1) {
          return;
        }

        $scope.visits.splice(idx, 1);
        if ($scope.visits.length == 0) {
          $scope.ctx.view = 'collectVisitBcs';
        }
      }

      $scope.addVisit = function() {
        $scope.visits.push({barcode: '', visitDate: new Date()});
      }

      $scope.validateVisitBarcodes = function(form) {
        var bcMap = {};
        angular.forEach($scope.visits, function(visit) {
          bcMap[visit.barcode] = visit;
        });

        $http.post(baseUrl + 'visit-barcodes/validate', Object.keys(bcMap)).then(
          function(result) {
            $scope.ctx.error = false;
            var headers = [];
            angular.forEach(result.data, function(bcDetail) {
              var detail = {};
              angular.forEach(bcDetail.parts, function(part) {
                var found = false;
                for (var i = 0; i < headers.length; ++i) {
                  if (headers[i].token == part.token) {
                    found = true;
                    break;
                  }
                }

                if (!found) {
                  headers.push({token: part.token, caption: part.caption});
                }

                detail[part.token] = part;
              });

              bcMap[bcDetail.barcode].detail = detail;
              bcMap[bcDetail.barcode].error = bcDetail.erroneous; 
              if (bcDetail.erroneous) {
                $scope.ctx.error = true;
              }
            });

            $scope.ctx.view = 'visitBcDetails';
            $scope.ctx.visitBcHeaders = headers;
            $scope.ctx.visitBcHeaderWidth = 85 / headers.length;
            if (form) {
              form.$setPristine();
            }
            saveSession();
          }
        );
      }

      $scope.validateVisitDetails = function() {
        alert(JSON.stringify($scope.visits));
      }

      $scope.addOrUpdateVisits = function(form) {
        var visits = [];
        angular.forEach($scope.visits, function(visit) {
          visits.push({barcode: visit.barcode, visitDate: visit.visitDate});
        });

        $http.post(baseUrl + 'visit-barcodes', visits).then(
          function(result) {
            onSaveVisit(result.data);
          }
        );
      }

      $scope.addParticipant = function() {
        $scope.participants.push({regDate: new Date()});
      }

      $scope.removeParticipant = function(participant) {
        var idx = $scope.participants.indexOf(participant);
        $scope.participants.splice(idx, 1);
      }

      $scope.registerParticipants = function() {
        var participants = $scope.participants.map(
          function(p) {
            return {
              cpShortTitle: p.cpShortTitle,
              empi: p.empi,
              ppid: p.ppid,
              regDate: p.regDate
            }
          }
        );

        $http.post(baseUrl + 'visit-barcodes/participants', participants).then(
          function(resp) {
            var error = false;
            angular.forEach(resp.data, function(participant, index) {
              participant.eventLabel = participant.nextEventLabel;
              participant.events = $scope.cpEvents[participant.cpId] || []
              if (participant.eventLabel && participant.events.length == 0) {
                participant.events.push({eventLabel: participant.eventLabel});
              }

              delete participant.regDate;
              angular.extend($scope.participants[index], participant);
              if (participant.errorMessage) {
                error = true;
              }
            });

            if (!error) {
              $scope.ctx.view = 'collectVisitDetails';
            }
          }
        );
      }

      $scope.registerVisits = function() {
        angular.forEach($scope.participants, function(participant) {
          $scope.visits.push({
            cpShortTitle: participant.cpShortTitle,
            cpId: participant.cpId,
            cprId: participant.cprId,
            ppid: participant.ppid,
            visitDate: participant.regDate,
            eventLabel: participant.eventLabel
          });
        });

        $http.post(baseUrl + 'visit-barcodes/visits', $scope.visits).then(
          function(result) {
            onSaveVisit(result.data);
          }
        );
      }

      $scope.loadCpEvents = function(participant) {
        var loadEvents = !$scope.cpEvents[participant.cpId]
        $scope.cpEvents[participant.cpId] = $scope.cpEvents[participant.cpId] || [];
        if (!loadEvents) {
          return;
        }

        CollectionProtocolEvent.listFor(participant.cpId).then(
          function(events) {
            participant.events = $scope.cpEvents[participant.cpId] = events;
          }
        )
      }

      $scope.parsePrimarySpmnLabels = function(visit) {
        var labels = Util.splitStr(visit.primarySpmnLabels, /,|\t|\n/);
        angular.forEach(visit.specimens, function(specimen) {
          if (!specimen.scanEnabled) {
            return;
          }

          var specimensPool = [specimen];
          if (specimen.specimensPool && specimen.specimensPool.length > 0) {
            specimensPool = specimen.specimensPool;
          } 

          var status = 'Missed Collection';
          angular.forEach(specimensPool, function(poolSpmn) {
            if (labels.indexOf(poolSpmn.label) == -1) {
              poolSpmn.status = 'Missed Collection';
            } else {
              poolSpmn.status = status = 'Collected';
              poolSpmn.comments = '';
            }
          });

          specimen.status = status;
        });
      }

      $scope.removePrimarySpmn = function(visit, specimen) {
        var idx = visit.specimens.indexOf(specimen);
        visit.specimens.splice(idx, 1);
        if (visit.specimens.length == 0) {
          idx = $scope.visits.indexOf(visit);
          $scope.visits.splice(idx, 1);

          if ($scope.visits.length == 0) {
            $scope.ctx.view = 'collectVisitBcs';
          }
        }
      }

      function getSpecimenToSave(visit, spmn) {
        var spmnToSave = {
          id: spmn.id,
          label: spmn.label,
          lineage: 'New',
          initialQty: spmn.initialQty,
          visitId: visit.id,
          reqId: spmn.reqId,
          collectionEvent: visit.collDetail,
          receivedEvent: visit.recvDetail,
          status: spmn.status,
          comments: visit.comments
        };
       
        var status = 'Missed Collection';
        var specimensPool = [];
        angular.forEach(spmn.specimensPool, function(poolSpmn) {
          specimensPool.push(getSpecimenToSave(visit, poolSpmn));
        });

        if (specimensPool.length > 0) {
          spmnToSave.specimensPool = specimensPool;
        }

        if (spmnToSave.status == 'Missed Collection') {
          spmnToSave.comments = spmn.comments;
          spmnToSave.children = getMissedChildSpmns(visit, spmn.children);
        }

        return spmnToSave;
      }

      $scope.collectPrimarySpmns = function() {
        var spmnsToSave = [];
        angular.forEach($scope.visits, function(visit) {
          var visitSpmns = visit.specimens.map(
            function(spmn) {
              return getSpecimenToSave(visit, spmn);
            }
          );

          spmnsToSave = spmnsToSave.concat(visitSpmns);
        });

        $http.post(baseUrl + 'visit-barcodes/primary-specimens', spmnsToSave).then(
          function(result) {
            removeMissedSpmns($scope.visits, result.data);
            initPrintLabels();
            saveSession();
          }
        );
      }

      $scope.toggleAllSpmnsSelForPrint = function(visit) {
        angular.forEach(visit.specimensForPrint, function(spmn) {
          spmn.selected = visit.selAllForPrint;
        });
      };

      $scope.toggleSpmnSelForPrint = function(visit, spmn) {
        if (spmn.selected) {
          var allSel = true;
          for (var i = 0; i < visit.specimensForPrint.length; ++i) {
            if (!visit.specimensForPrint[i].selected) {
              allSel = false;
              break;
            }
          }

          visit.selAllForPrint = allSel;
        } else {
          if (visit.selAllForPrint) {
            visit.selAllForPrint = false;
          }
        }
      }

      $scope.printLabels = function() {
        var spmnsToPrint = [];
        angular.forEach($scope.visits, function(visit) {
          spmnsToPrint = spmnsToPrint.concat(getSpmnsToPrint(visit, visit.specimens));
        });

        if (spmnsToPrint.length == 0) {
          $scope.skipPrintLabels();
          return;
        }

        $http.post(baseUrl + 'visit-barcodes/specimen-labels-print-jobs', spmnsToPrint).then(
          function(result) {
            Alerts.success('specimens.labels_print_job_created', {jobId: result.data.id});
            $scope.skipPrintLabels();
          }
        );
      }

      $scope.skipPrintLabels = function() {
        angular.forEach($scope.visits, function(visit) {
          delete visit.specimensForPrint;
        });

        setContListOpts($scope.visits);
        showContainerSelector();
      }

      $scope.toggleContainerSel = function(container) {
        //
        // Removing loops from objects to enable saving of
        // context to local storage without running out of
        // stack space
        //
        delete container.childContainers;
        delete container.parent;
        delete container.childContainersLoaded;

        if (container.selected) {
          $scope.ctx.selectedContainer = container;
          if (!container.occupiedPositions) {
            var contQ = Container.getById(container.id);
            var occupancyQ = container.getOccupiedPositions();
            $q.all([contQ, occupancyQ]).then(
              function(result) {
                angular.extend(container, result[0]);
                container.occupiedPositions = result[1];
                $scope.ctx.contOccupancyMap = result[1];
                $scope.ctx.view = 'assignPositions';
                $scope.ctx.aliquotErrors = [];
                saveSession();
              }
            );
          } else {
            $scope.ctx.contOccupanyMap = container.occupiedPositions;
            $scope.ctx.view = 'assignPositions';
            $scope.ctx.aliquotErrors = [];
            saveSession();
          }
        }
      }

      $scope.collectAliquots = function() {
        $scope.ctx.workflow = 'collectAliquots';
        $scope.showContainerSelector();
      }

      $scope.showContainerSelector = showContainerSelector;

      $scope.showUpdatedMap = function() {
        var container = $scope.ctx.selectedContainer;

        var result = ContainerUtil.assignPositions(
          container, 
          container.occupiedPositions, 
          $scope.ctx.aliquotLabels);

        $scope.ctx.contOccupancyMap = result.map;
        if (result.noFreeLocs) {
          Alerts.error("container.no_free_locs");
          return;
        }
      }

      $scope.showScanVirtualAliquots = function() {
        $scope.ctx.view = 'assignPositions';
        saveSession();
      }

      $scope.processAliquots = function() {
        var aliquots = $scope.ctx.virtualAliquots ? getVirtualAliquots() : getAliquotsFromMap();
        if (!aliquots || aliquots.length == 0) {
          Alerts.error("Please scan at least one aliquot label");
          return;
        }

        if ($scope.ctx.workflow != 'collectAliquots') {
          processAliquots(aliquots);
          return;
        }

        var aliquotLabels =  Util.splitStr($scope.ctx.aliquotLabels, /,|\t|\n/);
        $http.get(baseUrl + 'visit-barcodes/visits', {params: {aliquotLabels: aliquotLabels}}).then(
          function(result) {
            var visits = [];
            angular.forEach(result.data, function(visitSpmns) {
              var visit = visitSpmns.visit;
              visit.specimens = visitSpmns.specimens;
              visits.push(visit);
            });

            $scope.visits = visits;
            $scope.ctx.visits = $scope.visits;
            processAliquots(aliquots);
          }
        )
      }


      function processAliquots(aliquots) {
        var errors = [], positions = [];
        for (var i = 0; i < aliquots.length; ++i) {
          var result = processAliquot(aliquots[i]);
          if (result.status != 'ok') {
            errors.push({status: result.status, label: aliquots[i].label});
            continue;
          }

          if (!$scope.ctx.virtualAliquots) {
            positions.push({
              cpShortTitle: result.visit.cpShortTitle,
              label: result.specimen.label,
              specimenClass: result.specimen.specimenClass,
              type: result.specimen.type,
              positionX: result.specimen.storageLocation.positionX,
              positionY: result.specimen.storageLocation.positionY
            });
          }
        }

        $scope.ctx.aliquotErrors = errors;
        if (errors.length > 0) {
          cleanupAssignedPositions();
          return;
        }

        if ($scope.ctx.virtualAliquots) {
          showCollectChildSpmns();
          return;
        }

        var occupancyDetail = {
          containerName: $scope.ctx.selectedContainer.name,
          positions: positions
        };

        $http.post(baseUrl + 'visit-barcodes/validate-specimens-occupancy', occupancyDetail).then(
          function(result) {
            angular.forEach(result.data.positions, function(pos) {
              if (!!pos.errorCode || !!pos.errorMsg) {
                errors.push({status: pos.errorCode, msg: pos.errorMsg , label: pos.label});
              }
            });

            $scope.ctx.aliquotErrors = errors;
            if (errors.length > 0) {
              cleanupAssignedPositions();
              return;
            }

            showCollectChildSpmns();
          }
        );
      }

      function getVirtualAliquots() {
        var labels = Util.splitStr($scope.ctx.aliquotLabels, /,|\t|\n/);
        return labels.map(function(label) {
          return {
            label: label
          };
        });
      }

      function showCollectChildSpmns() {
        var visitsToReview = [];
        angular.forEach($scope.visits, function(visit) {
          var spmnsToReview = getSpmnsToReview(visit.specimens);
          if (spmnsToReview.length > 0) {
            visit.hasCellSpmns = false;
            visit.spmnsToReview = Specimen._flatten(spmnsToReview, 'childrenToReview');
            angular.forEach(visit.spmnsToReview, function(spmn) {
              //
              // Avoid loops to enable saving session context to local storage
              //
              spmn.parent = undefined;
              spmn.pooledSpecimen = undefined;

              //
              // to track whether to display volume in visit card or not
              //
              if (!visit.hasCellSpmns && spmn.specimenClass == 'Cell') {
                visit.hasCellSpmns = true;
              }
            });
            visitsToReview.push(visit);
          }
        });

        $scope.ctx.visitsToReview = visitsToReview;
        $scope.ctx.view = 'collectChildSpmns';
        //saveSession();
      }

      $scope.reAssignLocations = function() {
        cleanupAssignedPositions();
        $scope.ctx.view = 'assignPositions';
        saveSession();
      }

      $scope.saveAliquots = function() {
        var spmnsToSave = getAliquotsToSave();
        $http.post(baseUrl + 'visit-barcodes/child-specimens', spmnsToSave).then(
          function(result) {
            markSavedSpmns(result.data.specimens);
            continueOrExit();                  
          }
        ); 
      }

      function saveSession(showSuccessMsg) {
        $scope.session.$saveOrUpdate().then(
          function(session) {
            initContext(session);
            if (showSuccessMsg) {
              Alerts.success("rde.session.saved");
            }
          }
        );
      }

      function initContext(session) {
        $scope.session = session;
        $scope.ctx = $scope.session.dataObj;
        $scope.visits = $scope.ctx.visits;
        initPrimarySpmns();

        var initFn = stateInitMap[$scope.ctx.view]
        if (typeof initFn == "function") {
          initFn();
        }
      }

      function getVisitDisplayName(visit) {
        return visit.id ? visit.name : visit.barcode;
      }

      function deleteSession(session, loadSessionsList) {
        session.$remove().then(
          function() {
            for (var i = 0; i < $scope.sessions.length; i++) {
              if ($scope.sessions[i].id == session.id) {
                $scope.sessions.splice(i, 1);
                break;
              }
            }

            if ($scope.sessions.length == 0) {
              loadSessionsList ? $state.go('home') : $scope.newSession();
            } else {
              $scope.ctx.view = 'listSessions';
            }
          }
        )
      }

      $scope.newSession = function() {
        $scope.cps = [];
        $scope.cpEvents = {};
        $scope.participants = [];
        $scope.visits = [];
        $scope.ctx = {
          view: 'selectWorkflow',
          visits: $scope.visits,
          contListOpts: {
            type: 'specimen',
            criteria: {cpShortTitle: [], storeSpecimensEnabled: true}
          },
          aliquotLabels: ''
        };
        $scope.session = new SpmnCollSession({dataObj: $scope.ctx});
      }

      $scope.oldSession = function(session) {
        initContext(session);
      }


      $scope.collectVisitBcs = function() {
        $scope.ctx.workflow = $scope.ctx.view = 'collectVisitBcs';
      }

      $scope.collectVisitDetails = function() {
        $scope.ctx.workflow = 'collectVisitDetails';
        $scope.ctx.view = 'collectParticipantDetails';
        CollectionProtocol.listForRegistrations().then(
          function(cps) {
            $scope.cps = cps;
          }
        );
      }
      
      $scope.saveSession = function() {
        saveSession(true);
      }

      $scope.getVisitDisplayName = getVisitDisplayName;

      $scope.confirmDeleteSession = function(session) {
        DeleteUtil.confirmDelete({
          entity: session,
          templateUrl: 'modules/rde/confirm-delete-session.html',
          props: {
            getVisitDisplayName: $scope.getVisitDisplayName
          },
          delete: function() {
            deleteSession(session);
          }
        });
      }

      init();
    }
  );
