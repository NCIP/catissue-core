angular.module('openspecimen')
  .controller('le.RegAndCollectSpecimensCtrl', 
    function($scope, $state, $stateParams, $http, ApiUrls) {
      function init() {
        $scope.cpId = $stateParams.cpId
        $scope.participants = [];
      }

      $scope.addParticipant = function() {
        $scope.participants.push({empi: null, ppid: null, regDate: null});
      }

      $scope.registerParticipants = function() {
        var req = {
          cpId: $scope.cpId,
          registrations: $scope.participants.map(
            function(p) { 
              delete p.isOpen 
              return p;
            }
          );
        };

        var baseUrl = ApiUrls.getBaseUrl();
        $http.post(baseUrl + 'le/registrations', req).then(
          function(result) {
            $state.go('participant-list', {cpId: $scope.cpId});
          }
        );
      }

      $scope.cancel = function() {
        $state.go('participant-list', {cpId: $scope.cpId});
      }

      init();
    }
  );
