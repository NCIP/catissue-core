angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, $state, $modal, currentUser, Setting, Alerts, AuthorizationService) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
       $scope.feedback_enabled = true;
       
       Setting.query({module: 'common'}).then(
         function(result) {
           angular.forEach(result, function(item) {
             if(item.name == "feedback_enabled" && item.value == "false") {
               $scope.feedback_enabled = false;
             }
           });
         }
       );
     }

     $scope.userCreateUpdateOpts = {resource: 'User', operations: ['Create', 'Update']};
     $scope.cpReadOpts = {resource: 'CollectionProtocol', operations: ['Read']};
     $scope.containerReadOpts = {resource: 'StorageContainer', operations: ['Read']};
     $scope.orderReadOpts = {resource: 'Order', operations: ['Read']};
     $scope.scheduledJobReadOpts = {resource: 'ScheduledJob', operations: ['Read']};

     init();
     
     $scope.openFeedbackForm = function() {
       var modalInstance = $modal.open({
         templateUrl: 'modules/user/feedback.html',
         controller: 'FeedbackCtrl'
       });
     }
  })
