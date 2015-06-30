angular.module('openspecimen')
  .controller('le.RegAndCollectSpecimensCtrl', 
    function(
      $rootScope, $scope, $state, $stateParams, $http,
      CollectionProtocolEvent, SpecimenRequirement, Visit, Specimen, 
      User, CpConfigSvc, Alerts, Util, ApiUrls) {

      var baseUrl = ApiUrls.getBaseUrl();
      var currCpe = undefined;

      function init() {
        $scope.cpId = $stateParams.cpId
        $scope.view = 'register';
        $scope.participants = [];
        $scope.users = [];
        $scope.visit = {};
        $scope.tabOrderCnt = 0;
        $scope.boxOpts = {
          compact: false,
          dimension: {rows: 9, columns: 9},
          specimens: []
        },

        $scope.boxData = {
          csLabels: '',
          delimiter: ',',
          ctrl: undefined
        },
 
        $scope.childSpmnsData = {
          view: 'box',
          specimens: undefined 
        },

        CpConfigSvc.getWorkflowData($scope.cpId, 'registerParticipant').then(
          function(data) {
            var boxOpts = data.boxOpts || {};
            angular.extend($scope.boxOpts, boxOpts);
          }
        );

        User.query().then(
          function(users) {
            $scope.users = users;
          }
        );  
      }

      function registerParticipants() {
        if (!validParticipantsInfo()) {
          return;
        }

        var req = {
          cpId: $scope.cpId,
          registrations: $scope.participants.map(
            function(p) { 
              delete p.isOpen 
              return p;
            }
          )
        };

        $http.post(baseUrl + 'le/registrations', req).then(
          function(result) {
            onRegistrations(result.data.registrations);
          }
        );
      }

      function validParticipantsInfo() {
        var dupObjs = Util.getDupObjects($scope.participants, ['empi', 'ppid']);
        if (dupObjs.empi.length > 0) {
          Alerts.errorText('Duplicate participant IDs: ' + dupObjs.empi.join());
          return false;
        }

        if (dupObjs.ppid.length > 0) {
          Alerts.errorText('Duplicate participant protocol IDs: ' + dupObjs.ppid.join());
          return false;
        }

        return true;
      }

      function onRegistrations(participants) {
        prepareForSpecimenCollection(participants);
      }

      function prepareForSpecimenCollection(participants) {
        $scope.participants = participants.map(
          function(participant) {
            participant.collDetail = {collectionDate: new Date()};
            participant.recvDetail = {receiveDate: new Date()};
            participant.comments = '';
            return participant;
          }
        ); 

        $scope.view = 'collect-primary-specimens';
        loadCpes();
      }

      function loadCpes() {
        CollectionProtocolEvent.listFor($scope.cpId).then(
          function(cpes) {
            $scope.cpes = cpes.map(
              function(cpe) {
                cpe.eventName = cpe.eventPoint + ": " + cpe.eventLabel;
                return cpe;
              }
            );

            if ($scope.cpes.length >= 1) {
              $scope.visit.cpe = $scope.cpes[0];
              loadSpecimenRequirements();
            }
          }
        );
      }

      function loadSpecimenRequirements() {
        if (currCpe == $scope.visit.cpe) {
          return;
        }

        SpecimenRequirement.listFor($scope.visit.cpe.id).then(
          function(srs) {
            angular.forEach($scope.participants, function(participant) {
              participant.specimens = angular.copy(srs);
            });

            ++$scope.tabOrderCnt;
          }
        );

        currCpe = $scope.visit.cpe;
      }

      function getVisitAndSpecimensToSave(participant) {
        var visit = {
          cprId: participant.cprId,
          eventId: $scope.visit.cpe.id,
          visitDate: participant.collDetail.collectionDate,
          status: 'Complete',
          site: $scope.visit.cpe.defaultSite
        };

        var specimens = participant.specimens.map(
          function(specimen) {
            return getPrimarySpecimenToSave(
              specimen, 
              participant.comments,
              participant.collDetail, 
              participant.recvDetail);
          }
        );

        return {visit: visit, specimens: specimens};
      }

      function getPrimarySpecimenToSave(req, comments, collDetail, recvDetail) {
        return {
          initialQty: req.initialQty,
          label: req.label,
          reqId: req.id,
          lineage: 'New',
          status: 'Collected',
          comments: comments,
          collectionEvent: {
            user: collDetail.collector,
            time: collDetail.collectionDate
          },
          receivedEvent: {
            user: recvDetail.receiver,
            time: recvDetail.receiveDate
          },
          children: []
        }
      }

      function getChildSpecimensToShow(response) {
        var result = [];
        for (var i = 0; i < $scope.participants.length; ++i) {
          var participant = $scope.participants[i]; 
          var collPrimarySpmns = response[i].specimens;
          var visitId = response[i].visit.id;

          for (var j = 0; j < participant.specimens.length; ++j) {
            var  childSpecimens = prepareChildSpecimens(
              participant.specimens[j].children, 
              participant.empi, 
              participant.ppid,
              visitId,
              collPrimarySpmns[j]);
            result = result.concat(childSpecimens);
          }
        }

        return result;
      }

      function prepareChildSpecimens(requirements, empi, ppid, visitId, primarySpmn) {
        var result = [];
        primarySpmn.depth = 0;
        primarySpmn.frozenBy = {};
        
        angular.forEach(requirements, function(requirement, $index) {
          if ($index == 0) {
            requirement.parent = primarySpmn;
          }

          requirement.parentSpecimenId = primarySpmn.id;
          requirement.primarySpmnLabel = primarySpmn.label;
          requirement.showInTree = requirement.nonVirtual = anyNonVirtualSpecimen(requirement);
          requirement.depth = 1;
          requirement.frozenBy = {};
          if (requirement.lineage == 'Aliquot') {
            requirement.frozenBy = $rootScope.currentUser;
            requirement.frozenTime = new Date();
          }

          setEmpiAndVisitId(requirement, empi, ppid, visitId);
          flatten(requirement, result, 2);
        });

        groupAliquots(requirements);
        return result;
      }

      function groupAliquots(specimens) {
        var group = [];
        angular.forEach(specimens, function(specimen) {
          if (specimen.lineage == 'Aliquot' && specimen.nonVirtual) {
            group.push(specimen);
          }
        });

        if (group.length > 1) {
          angular.forEach(group, function(specimen, $index) {
            specimen.grouped = true;
            setShowInTree($index == 0 ? specimen.children : [specimen], false);
          });

          group[0].group = group;
          watchAliquotGrpChanges(group[0]);
        }
      }

      function setShowInTree(specimens, showInTree, expandOrCollapse) {
        angular.forEach(specimens, function(specimen) {
          var nextLevel = expandOrCollapse;
          if (showInTree) {
            if (!specimen.grouped && specimen.nonVirtual) {
              specimen.showInTree = true;
            } else if (specimen.grouped && expandOrCollapse) {
              specimen.showInTree = true;
              nextLevel = false;
            } else if (specimen.grouped && !!specimen.group) {
              specimen.showInTree = true;
            }
          } else {
            if (!expandOrCollapse || !specimen.grouped || !specimen.group) {
              specimen.showInTree = false;
              nextLevel = false;
            }

            if (specimen.group) {
              specimen.expanded = false;
            }
          }

          setShowInTree(specimen.children, showInTree, nextLevel);
        });
      }

      function setEmpiAndVisitId(requirement, empi, ppid, visitId) {
        requirement.visitId = visitId;
        requirement.empi = empi;
        requirement.ppid = ppid;
        angular.forEach(requirement.children, function(childReq) {
          setEmpiAndVisitId(childReq, empi, ppid, visitId);
        });
      }

      function flatten(requirement, result, depth) {
        if (result) {
          result.push(requirement);
        } else {
          result = [requirement]
        }

        angular.forEach(requirement.children, function(childReq) {
          childReq.primarySpmnLabel = requirement.primarySpmnLabel;
          childReq.depth = depth;
          childReq.showInTree = childReq.nonVirtual = anyNonVirtualSpecimen(childReq);
          childReq.frozenBy = {};
          if (childReq.lineage == 'Aliquot') {
            childReq.frozenBy = $rootScope.currentUser;
            childReq.frozenTime = new Date();
          }

          flatten(childReq, result, depth + 1);
        });

        groupAliquots(requirement.children);
      }      

      function assignLabels(labels) {
        var labelIdx = 0;

        for (var i = 0; i < $scope.boxOpts.specimens.length; ++i) {        
          var specimen = $scope.boxOpts.specimens[i];
          if (specimen.storageType == 'Virtual') {
            continue;
          }

          if (labelIdx < labels.length) {
            specimen.label = labels[labelIdx++];
          } else {
            specimen.label = '';
          }
        }
      }

      function validPrimarySpecimens(visitsAndSpecimens) {
        var specimens = [];
        angular.forEach(visitsAndSpecimens, function(visitSpecimens) {
          specimens = specimens.concat(visitSpecimens.specimens);
        });

        return validSpecimens(specimens);
      }

      function validSpecimens(specimens) {
        var dupObjs = Util.getDupObjects(specimens, ['label']);
        if (dupObjs.label.length > 0) {
          Alerts.errorText("Duplicate specimen labels: " + dupObjs.label.join());
          return false;
        }

        return true;
      }

      function watchAliquotGrpChanges(grpLeader) {
        $scope.$watchGroup(
          [
            function() { return grpLeader.initialQty; },
            function() { return grpLeader.frozenBy; },
            function() { return grpLeader.frozenTime; }
          ],

          function(newVal, oldVal) {
            if (oldVal == undefined) {
              return;
            }

            if (!grpLeader.group || grpLeader.expanded) {
              return;
            }

            angular.forEach(grpLeader.group, function(aliquot, $index) {
              if ($index == 0) {
                return;
              }

              aliquot.initialQty = grpLeader.initialQty; 
              aliquot.frozenBy = grpLeader.frozenBy; 
              aliquot.frozenTime = grpLeader.frozenTime; 
            });
          }
        );
      }

      $scope.addParticipant = function() {
        $scope.participants.push({empi: null, ppid: null, regDate: null});
      }
  
      $scope.removeParticipant = function(p) {
        var idx = $scope.participants.indexOf(p);
        if (idx >= 0) {
          $scope.participants.splice(idx, 1);
        }
      }

      $scope.registerParticipants = registerParticipants;

      $scope.cancel = function() {
        $state.go('participant-list', {cpId: $scope.cpId});
      }

      $scope.loadSpecimenRequirements = loadSpecimenRequirements;

      $scope.removePrimarySpecimen = function(participant, specimen) {
        $scope.tabOrderCnt++;

        var idx = participant.specimens.indexOf(specimen);
        if (idx >= 0) {
          participant.specimens.splice(idx, 1);
        }

        if (participant.specimens.length > 0) {
          return;
        }

        idx = $scope.participants.indexOf(participant);
        if (idx >= 0) {
          $scope.participants.splice(idx, 1);
        }

        if ($scope.participants.length == 0) {
          $scope.view = 'register';       
        }
      }

      $scope.collectPrimarySpecimens = function() {
        var payload = [];
        angular.forEach($scope.participants, function(participant) {
          payload.push(getVisitAndSpecimensToSave(participant));
        });

        if (!validPrimarySpecimens(payload)) {
          return;
        }
 
        $http.post(baseUrl + 'le/specimens', payload).then(
          function(result) {
            $scope.view = 'collect-child-specimens';
            $scope.boxOpts.specimens = getChildSpecimensToShow(result.data);     
          }
        );
      }

      $scope.processAliquotLabels = function(input) {
        var labels = $scope.boxData.csLabels.split(/,|\t/);
        assignLabels(labels); 
        if ($scope.boxData.ctrl) {
          $scope.boxData.ctrl.draw();
        } else {
          console.log("box not yet available");
        }
      };

      $scope.toggleView = function() {
        if ($scope.childSpmnsData.view == 'box') {
          $scope.childSpmnsData.view = 'tree'; 
          if (!!$scope.childSpmnsData.specimens) {
            return;
          }

          var treeViewSpmns = [],
              partChildSpmns = [],
              lastEmpi = undefined,
              lastPpid = undefined;

          angular.forEach($scope.boxOpts.specimens, function(specimen, $index) {
            if ($index != 0 && specimen.empi != lastEmpi) {
              treeViewSpmns.push({empi: lastEmpi, ppid: lastPpid, specimens: partChildSpmns});
              partChildSpmns = [];
            }

            var parent = specimen.parent;
            specimen.parent = undefined;

            if (parent) {
              partChildSpmns.push(parent);
              parent.showInTree = true;
            }

            partChildSpmns.push(specimen);
            lastEmpi = specimen.empi;
            lastPpid = specimen.ppid;
          });

          if (partChildSpmns.length > 0) {
            treeViewSpmns.push({empi: lastEmpi, ppid: lastPpid, specimens: partChildSpmns});
          }
            
          $scope.childSpmnsData.specimens = treeViewSpmns;
        } else {
          $scope.treeViewValidator.formSubmitted(true);
          if (!$scope.treeViewValidator.isValidForm()) {
            Alerts.error("common.form_validation_error");
            return;
          }

          $scope.childSpmnsData.view = 'box'; 
        }
      }

      $scope.expandAliquotsGroup = function(spmn) {
        setShowInTree(spmn.group, true, true)
        spmn.expanded = true;
      }

      $scope.collapseAliquotsGroup = function(spmn) {
        setShowInTree(spmn.group, false, true)
        spmn.expanded = false;
      }

      $scope.setTreeViewFormValidator = function(validator) {
        $scope.treeViewValidator = validator;
      }

      $scope.saveAliquots = function() {
        if ($scope.childSpmnsData.view == 'tree') {
          $scope.treeViewValidator.formSubmitted(true);
          if (!$scope.treeViewValidator.isValidForm()) {
            Alerts.error("common.form_validation_error");
            return;
          }
        }
          
        var specimens = $scope.boxOpts.specimens;
        if (!validSpecimens(specimens)) {
          return;
        }

        var payload = { specimens: [], events: [] };

        angular.forEach($scope.childSpmnsData.specimens, function(partSpmns) {
          angular.forEach(partSpmns.specimens, function(spmn) {
            if (spmn.lineage == 'New' && (spmn.frozenTime  || spmn.frozenBy.id)) {
              payload.events.push({
                specimenId: spmn.id,
                user: spmn.frozenBy,
                time: spmn.frozenTime
              });
            }
          });
        });

        for (var i = 0; i < specimens.length; ++i) {
          if (!specimens[i].parentSpecimenId) {
            continue;
          }

          var toSave = getChildSpecimensToSave(specimens[i]);
          if (toSave) {
            payload.specimens.push(toSave.specimen);
            payload.events = payload.events.concat(toSave.events);
          }
        } 

        var missingLabels = getMissingLabels(payload.specimens, specimens);
        if (missingLabels.length > 0) {
          Alerts.errorText(
            "Parent specimen labels not specified." +
            " Child specimens: " + missingLabels.join()
          );
          return;
        }

        $http.post(baseUrl + 'le/specimens/children', payload).then(
          function() {
            $state.go('participant-list', {cpId: $scope.cpId});
          }
        );
      }

      function getChildSpecimensToSave(specimen) {
        var label = specimen.label || '';
        if (specimen.storageType != 'Virtual' && (!label  || label.trim().length == 0)) {
          return null;
        }

        var children = [];
        var events = [];
        angular.forEach(specimen.children, function(childSpecimen) {
          var toSave = getChildSpecimensToSave(childSpecimen);
          if (toSave) {
            children.push(toSave.specimen);
            events = events.concat(toSave.events);
          }     
        });

        var specimenToSave = {
          label: label.trim(),
          reqId: specimen.id,
          status: 'Collected',      
          parentId: specimen.parentSpecimenId,
          visitId: specimen.visitId,
          children: children,
          initialQty: specimen.initialQty
        };

        if (!!specimen.frozenTime  || !!specimen.frozenBy.id) {
          events.push({
            reqId: specimen.id,
            visitId: specimen.visitId,
            user: specimen.frozenBy, 
            time: specimen.frozenTime
          });
        }
  
        var specimenAndEvents = {specimen: specimenToSave, events: events};
        return anyChildHasLabel(specimenToSave) ? specimenAndEvents : null;
      }

      function anyChildHasLabel(specimen) {
        return anySpecimen(specimen, function(s) { return !!s.label; });
      }

      function anyNonVirtualSpecimen(specimen) {
        return anySpecimen(specimen, function(s) { return s.storageType != 'Virtual' });
      }

      function anySpecimen(specimen, predicateFn) {
        if (predicateFn(specimen)) {
          return true;
        }

        for (var i = 0; i < specimen.children.length; ++i) {
          if (predicateFn(specimen.children[i])) {
            return true;
          }
        }

        return false;
      }

      function getMissingLabels(specimenTree, specimens) {
        var labelsToSave = getLabelsFromSpecimenTree(specimenTree);
        var missing = [];
        angular.forEach(specimens, function(specimen) {
          var label = specimen.label;
          if (!!label && labelsToSave.indexOf(label) == -1) {
            missing.push(label);
          }
        }); 

        return missing;
      }

      function getLabelsFromSpecimenTree(specimenTree) {
        var labels = [];
        angular.forEach(specimenTree, function(specimen) {
          if (!!specimen.label) {
            labels.push(specimen.label);
          }

          labels = labels.concat(getLabelsFromSpecimenTree(specimen.children));
        });

        return labels;
      }

      init();
    }
  );
