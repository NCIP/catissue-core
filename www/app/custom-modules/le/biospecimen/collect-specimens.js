angular.module('openspecimen')
  .controller('le.RegAndCollectSpecimensCtrl', 
    function(
      $scope, $state, $stateParams, $http, 
      CollectionProtocolEvent, SpecimenRequirement, Visit, Specimen, 
      CpConfigSvc, Alerts, Util, ApiUrls) {

      var baseUrl = ApiUrls.getBaseUrl();
      var currCpe = undefined;

      function init() {
        $scope.cpId = $stateParams.cpId
        $scope.view = 'register';
        $scope.participants = [];
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

        CpConfigSvc.getWorkflowData($scope.cpId, 'registerParticipant').then(
          function(data) {
            var boxOpts = data.boxOpts || {};
            angular.extend($scope.boxOpts, boxOpts);
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
              participant.collDetail, 
              participant.recvDetail);
          }
        );

        return {visit: visit, specimens: specimens};
      }

      function getPrimarySpecimenToSave(req, collDetail, recvDetail) {
        return {
          initialQty: req.initialQty,
          label: req.label,
          reqId: req.id,
          lineage: 'New',
          status: 'Collected',
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
              visitId,
              collPrimarySpmns[j]);
            result = result.concat(childSpecimens);
          }
        }

        return result;
      }

      function prepareChildSpecimens(requirements, empi, visitId, primarySpmn) {
        var result = [];
        angular.forEach(requirements, function(requirement) {
          requirement.parentSpecimenId = primarySpmn.id;
          requirement.primarySpmnLabel = primarySpmn.label;
          setEmpiAndVisitId(requirement, empi, visitId);
          flatten(requirement, result);
        });

        return result;
      }

      function setEmpiAndVisitId(requirement, empi, visitId) {
        requirement.visitId = visitId;
        requirement.empi = empi;
        angular.forEach(requirement.children, function(childReq) {
          setEmpiAndVisitId(childReq, empi, visitId);
        });
      }

      function flatten(requirement, result) {
        if (result) {
          result.push(requirement);
        } else {
          result = [requirement]
        }

        angular.forEach(requirement.children, function(childReq) {
          childReq.primarySpmnLabel = requirement.primarySpmnLabel;
          flatten(childReq, result);
        });
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

      $scope.saveAliquots = function() {
        var payload = [];
        var specimens = $scope.boxOpts.specimens;
        if (!validSpecimens(specimens)) {
          return;
        }

        for (var i = 0; i < specimens.length; ++i) {
          if (!specimens[i].parentSpecimenId) {
            continue;
          }

          var toSave = getChildSpecimensToSave(specimens[i]);
          if (toSave) {
            payload.push(toSave);
          }
        } 

        var missingLabels = getMissingLabels(payload, specimens);
        if (missingLabels.length > 0) {
          Alerts.errorText(
            "Parent specimen labels not specified." +
            " Child specimens: " + missingLabels.join()
          );
          return;
        }

        Specimen.save(payload).then(
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
        angular.forEach(specimen.children, function(childSpecimen) {
          var toSave = getChildSpecimensToSave(childSpecimen);
          if (toSave) {
            children.push(toSave);
          }     
        });

        var specimenToSave = {
          label: label.trim(),
          reqId: specimen.id,
          status: 'Collected',      
          parentId: specimen.parentSpecimenId,
          visitId: specimen.visitId,
          children: children
        };
  
        return anyChildHasLabel(specimenToSave) ? specimenToSave : null;
      }

      function anyChildHasLabel(specimen) {
        if (!!specimen.label) {
          return true;
        }

        for (var i = 0; i < specimen.children.length; ++i) {
          if (anyChildHasLabel(specimen.children[i])) {
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
