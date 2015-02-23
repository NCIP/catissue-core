angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, $state, User, Alerts) {

     function init() {
       $scope.alerts = Alerts.messages;

       User.getCurrentUser().then(function(user) {
         $rootScope.currentUser = {
           id: user.id,
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
