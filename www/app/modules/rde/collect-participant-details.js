angular.module('os.rde')
  .controller('RdeCollectParticipantDetailsCtrl', function($scope, $state, session, cps, CollectionProtocol, RdeApis) {
    function init() {
      $scope.ctx.workflow = 'process_visits_slow';
      $scope.input = {
        cps: cps,
        cpSites: {},
        participants: session.getParticipants()
      }
    }

    function saveSession(participants, step) {
      angular.extend(session.data, {
        workflow: $scope.ctx.workflow,
        step: step || $state.$current.name,
        participants: participants
      });

      return session.saveOrUpdate().then($scope.showSessionSaved(!step));
    }

    function getParticipantsToSave() {
      return $scope.input.participants.map(
        function(p) {
          return {
            cpShortTitle: p.cpShortTitle,
            empi: p.empi,
            ppid: p.ppid,
            regDate: p.regDate,
            pmi: {siteName: p.siteName, mrn: p.mrn}
          }
        }
      );
    }

    $scope.cpSelected = function(participant) {
      var sites = $scope.input.cpSites[participant.cpShortTitle];
      if (!!sites) {
        if (sites.length == 1) {
          participant.siteName = sites[0];
        }

        return;
      }

      var cpId = -1;
      for (var i = 0; i < $scope.input.cps.length; ++i) {
        if ($scope.input.cps[i].shortTitle == participant.cpShortTitle) {
          cpId = $scope.input.cps[i].id;
          break;
        }
      }

      CollectionProtocol.getById(cpId).then(
        function(cp) {
          sites = $scope.input.cpSites[cp.shortTitle] = cp.cpSites.map(
            function(cpSite) {
              return cpSite.siteName;
            }
          );

          if (sites.length == 1) {
            participant.siteName = sites[0];
          }
        }
      );
    }

    $scope.addParticipant = function() {
      $scope.input.participants.push({regDate: new Date()});
    }

    $scope.removeParticipant = function(participant) {
      var idx = $scope.input.participants.indexOf(participant);
      $scope.input.participants.splice(idx, 1);
    }

    $scope.registerParticipants = function() {
      RdeApis.registerParticipants(getParticipantsToSave()).then(
        function(savedParticipants) {
          var error = false;
          angular.forEach(savedParticipants, function(participant, index) {
            if (participant.errorMessage) {
              error = true;
              $scope.input.participants[index].errorMessage = participant.errorMessage;
            }
          });

          if (!error) {
            var nextStep = 'rde-collect-visit-details';
            saveSession(savedParticipants, nextStep).then(
              function() {
                $scope.ctx.participants = savedParticipants;
                $state.go(nextStep, {sessionId: session.uid});
              }
            );
          }
        }
      );
    }

    $scope.saveSession = function() {
      saveSession(getParticipantsToSave());
    }

    init();
  });

