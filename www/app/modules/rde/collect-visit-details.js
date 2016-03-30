angular.module('os.rde')
  .controller('RdeCollectVisitDetailsCtrl', function($scope, $state, ctx, participants, CollectionProtocolEvent, RdeApis) {
    function init() {
      $scope.input = {
        cpEvents: {},
        participants: participants
      }

      angular.forEach(participants, function(participant) {
        participant.eventLabel = participant.nextEventLabel;
        participant.events = $scope.input.cpEvents[participant.cpId] || []
        if (participant.eventLabel && participant.events.length == 0) {
          participant.events.push({eventLabel: participant.eventLabel});
        }

        participant.regDate = new Date();
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

    $scope.registerVisits = function() {
      var visits = $scope.input.participants.map(
        function(participant) {
          return {
            cpShortTitle: participant.cpShortTitle,
            cpId: participant.cpId,
            cprId: participant.cprId,
            ppid: participant.ppid,
            visitDate: participant.regDate,
            eventLabel: participant.eventLabel
          }
        }
      );

      RdeApis.saveVisits(visits).then(
        function(visitsSpmns) {
          ctx.visitsSpmns = visitsSpmns;
          $state.go('rde-collect-primary-specimens');
        }
      );
    }

    init();
  });

