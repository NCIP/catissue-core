angular.module('openspecimen')
  .controller('le.RegAndCollectSpecimensCtrl', 
    function(
      $scope, $state, $stateParams, $http, 
      CollectionProtocolEvent, SpecimenRequirement, Visit, Specimen, Util, ApiUrls) {

      var baseUrl = ApiUrls.getBaseUrl();

      function init() {
        $scope.cpId = $stateParams.cpId
        $scope.view = 'register';
        $scope.participants = [];
        $scope.cpe = {};
        $scope.boxOpts = {
          compact: true,
          dimension: {rows: 9, columns: 9},
          specimens: []
        },

        $scope.boxData = {
          csLabels: '',
          delimiter: ',',
          ctrl: undefined
        }
      }

      function registerParticipants() {
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
      };

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

            if ($scope.cpes.length == 1) {
              $scope.cpe = $scope.cpes[0];
              loadSpecimenRequirements();
            }
          }
        );
      }

      function loadSpecimenRequirements() {
        SpecimenRequirement.listFor($scope.cpe.id).then(
          function(srs) {
            angular.forEach($scope.participants, function(participant) {
              participant.specimens = angular.copy(srs);
            });
          }
        )
      }

      function getVisitAndSpecimensToSave(participant) {
        var visit = {
          cprId: participant.cprId,
          eventId: $scope.cpe.id,
          visitDate: participant.collDetail.collectionDate,
          status: 'Complete',
          site: $scope.cpe.defaultSite
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
            user: recvDetail.collector,
            time: recvDetail.collectionDate
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

      $scope.addParticipant = function() {
        $scope.participants.push({empi: null, ppid: null, regDate: null});
      }

      $scope.registerParticipants = registerParticipants;

      $scope.cancel = function() {
        $state.go('participant-list', {cpId: $scope.cpId});
      }

      $scope.collectPrimarySpecimens = function() {
        var payload = [];
        angular.forEach($scope.participants, function(participant) {
          payload.push(getVisitAndSpecimensToSave(participant));
        });
 
        $http.post(baseUrl + 'le/specimens', payload).then(
          function(result) {
            $scope.view = 'collect-child-specimens';
            $scope.boxOpts.specimens = getChildSpecimensToShow(result.data);     
          }
        );
      }

      $scope.processAliquotLabels = function(input) {
        var labels = Util.csvToArray($scope.boxData.csLabels, ',');
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
        for (var i = 0; i < specimens.length; ++i) {
          if (!specimens[i].parentSpecimenId) {
            continue;
          }

          var toSave = getChildSpecimensToSave(specimens[i]);
          if (toSave) {
            payload.push(toSave);
          }
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

        return {
          label: label.trim(),
          reqId: specimen.id,
          status: 'Collected',      
          parentId: specimen.parentSpecimenId,
          visitId: specimen.visitId,
          children: children
        };
      }

      init();
    }
  );
