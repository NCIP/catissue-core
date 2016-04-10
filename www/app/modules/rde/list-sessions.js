angular.module('os.rde')
  .controller('RdeListSessionsCtrl', function($scope, $state, sessions, DeleteUtil) {
    function init() {
      $scope.sessions = sessions
      if (sessions.length == 0) {
        newSession();
      }
    }

    function removeSession(session) {
      var idx = $scope.sessions.indexOf(session);
      $scope.sessions.splice(idx, 1);

      if ($scope.sessions.length == 0) {
        newSession();
      }
    }
         
    function newSession() {
      $state.go('rde-select-workflow');
    }

    $scope.newSession = newSession;

    $scope.restoreSession = function(session) {
      $state.go(session.data.step, {sessionId: session.uid});
    }

    $scope.deleteSession = function(session) {
      DeleteUtil.confirmDelete({
        entity: session,
        templateUrl: 'modules/rde/confirm-delete-session.html',
        delete: function() {
          session.$remove().then(function() { removeSession(session) });
        }
      });
    }

    init();
  });
