angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, $state, currentUser, Alerts, AuthorizationService) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
     }

     init();
  })
