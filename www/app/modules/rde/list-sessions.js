angular.module('os.rde')
  .controller('RdeListSessionsCtrl', function($scope, $state, sessions, DeleteUtil) {
    function init() {
      $scope.sessions = sessions
      if (sessions.length == 0) {
        newSession();
      }
    }

    function removeSession(session) {
      for (var i = 0; i < $scope.sessions.length; ++i) {
        if ($scope.sessions[i].id == session.id) {
          $scope.sessions.splice(i, 1);
          break;
        }
      }

      if ($scope.sessions.length == 0) {
        newSession();
      }
    }
         
    function newSession() {
      $state.go('rde-select-workflow');
    }

    $scope.newSession = newSession;

    $scope.oldSession = function(session) {
      $state.go(session.data.step, {sessionId: session.id});
    }

    $scope.confirmDeleteSession = function(session) {
      DeleteUtil.confirmDelete({
        entity: session,
        templateUrl: 'modules/rde/confirm-delete-session.html',
        delete: function() {
          session.$remove().then(removeSession);
        }
      });
    }

    init();
  });
