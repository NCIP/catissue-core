angular.module('os.rde')
  .controller('RdeCollectParticipantDetailsCtrl', function($scope, $state, cps, RdeApis) {

    function init() {
      $scope.ctx.workflow = 'process-visits-slow';
      $scope.input = {
        cps: cps,
        participants: [{regDate: new Date()}]
      }
    }

    $scope.addParticipant = function() {
      $scope.input.participants.push({regDate: new Date()});
    }

    $scope.removeParticipant = function(participant) {
      var idx = $scope.input.participants.indexOf(participant);
      $scope.input.participants.splice(idx, 1);
    }

    $scope.registerParticipants = function() {
      var participants = $scope.input.participants.map(
        function(p) {
          return {
            cpShortTitle: p.cpShortTitle,
            empi: p.empi,
            ppid: p.ppid,
            regDate: p.regDate
          }
        }
      );

      RdeApis.registerParticipants(participants).then(
        function(savedParticipants) {
          var error = false;
          angular.forEach(savedParticipants, function(participant, index) {
            if (participant.errorMessage) {
              error = true;
              $scope.input.participants[index].errorMessage = participant.errorMessage;
            }
          });

          if (!error) {
            $scope.ctx.participants = savedParticipants;
            $state.go('rde-collect-visit-details');
          }
        }
      );
    }

    init();
  });

