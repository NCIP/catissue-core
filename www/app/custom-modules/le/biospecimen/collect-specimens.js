angular.module('openspecimen')
  .controller('le.RegAndCollectSpecimensCtrl', 
    function(
      $scope, $state, $stateParams, $http, 
      CollectionProtocolEvent, SpecimenRequirement, Visit, ApiUrls) {

      var baseUrl = ApiUrls.getBaseUrl();

      function init() {
        $scope.cpId = $stateParams.cpId
        $scope.view = 'register';
        $scope.participants = [];
        $scope.cpe = {};
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
          function() {
            $state.go('participant-list', {cpId: $scope.cpId});
          }
        );
      }

      init();
    }
  );
