angular.module('os.rde')
  .controller('RdeCollectVisitDetailsCtrl', function(
    $scope, $state, session, ctx, participants, CollectionProtocolEvent, RdeApis) {

    function init() {
      $scope.input = {
        cpEvents: {},
        participants: participants
      }

      angular.forEach(participants, function(participant) {
        participant.eventLabel = participant.eventLabel || participant.nextEventLabel;
        participant.events = $scope.input.cpEvents[participant.cpId] || []
        if (participant.eventLabel && participant.events.length == 0) {
          participant.events.push({eventLabel: participant.eventLabel});
        }

        participant.visitDate = new Date();
      });
    }

    $scope.loadCpEvents = function(visit) {
      var loadEvents = !$scope.input.cpEvents[visit.cpId]
      $scope.input.cpEvents[visit.cpId] = $scope.input.cpEvents[visit.cpId] || [];
      if (!loadEvents) {
        return;
      }

      CollectionProtocolEvent.listFor(visit.cpId).then(
        function(events) {
          visit.events = $scope.input.cpEvents[visit.cpId] = events;
        }
      )
    }

    function getVisitsToSave() {
      return $scope.input.participants.map(
        function(participant) {
          return {
            cpShortTitle: participant.cpShortTitle,
            cpId: participant.cpId,
            cprId: participant.cprId,
            ppid: participant.ppid,
            visitDate: participant.visitDate,
            eventLabel: participant.eventLabel
          }
        }
      );
    }

    function saveSession(visitsSpmns, step) {
      angular.extend(session.data, {
        participants: $scope.input.participants,
        step: step || $state.$current.name,
        visits: (visitsSpmns || []).map(function(v) { return {name: v.name, visitDate: v.visitDate}; })
      });

      return session.saveOrUpdate().then($scope.showSessionSaved(!step));
    }

    $scope.registerVisits = function() {
      var visits = getVisitsToSave();
      RdeApis.saveVisits(visits).then(
        function(visitsSpmns) {
          ctx.visitsSpmns = visitsSpmns;

          var nextStep = 'rde-collect-primary-specimens';
          saveSession(visitsSpmns, nextStep).then(
            function() {
              $state.go(nextStep, {sessionId: session.uid});
            }
          );
        }
      );
    }

    $scope.saveSession = saveSession;

    init();
  });

