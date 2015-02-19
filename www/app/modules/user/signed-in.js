angular.module('openspecimen')
  .controller('signedInCtrl', function($scope, $rootScope, $state, User, Alerts) {

     function init() {
       $scope.alerts = Alerts.messages;

       User.getCurrentUser().then(function(user) {
         $rootScope.currentUser = {
           firstName: user.firstName,
           lastName: user.lastName,
           loginName: user.loginName
         }
       })
     }

     $scope.logout = function() {
       $state.go('login', {logout: true});
     }

     init();
  })
