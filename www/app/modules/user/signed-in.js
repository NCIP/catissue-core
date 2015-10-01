angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, $state, $modal, currentUser, Setting, Alerts, AuthorizationService) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
       $scope.feedback_enabled = $rootScope.global.appProps.feedback_enabled;
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
