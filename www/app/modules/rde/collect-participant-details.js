angular.module('os.rde')
  .controller('RdeCollectParticipantDetailsCtrl', function($scope, $state, session, cps, RdeApis) {
    function init() {
      $scope.ctx.workflow = 'process-visits-slow';
      $scope.input = {
        cps: cps,
        participants: session.getParticipants()
      }
    }

    function saveSession(participants, step) {
      angular.extend(session.data, {
        workflow: $scope.ctx.workflow,
        step: step || $state.$current.name,
        participants: participants
      });

      return session.$saveOrUpdate();
    }

    function getParticipantsToSave() {
      return $scope.input.participants.map(
        function(p) {
          return {
            cpShortTitle: p.cpShortTitle,
            empi: p.empi,
            ppid: p.ppid,
            regDate: p.regDate
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
                $state.go(nextStep, {sessionId: session.id});
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

