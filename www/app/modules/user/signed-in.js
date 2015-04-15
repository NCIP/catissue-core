angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, $state, currentUser, Alerts, AuthorizationService) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;

       /*
        * Authorization options
        */
       $scope.userAuthOpts = {
         createOpts: {resource: 'User', operation: 'Create'},
         updateOpts: {resource: 'User', operation: 'Update'},
         deleteOpts: {resource: 'User', operation: 'Delete'}
       }
     }

     init();
  })
