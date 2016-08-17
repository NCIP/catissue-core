angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, currentUser, Alerts, AuthorizationService, SettingUtil) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
       $scope.userCtx = {
         hasPhiAccess: AuthorizationService.hasPhiAccess()
       }
       
       getTrainingUrl();
     }
     
     function getTrainingUrl() {
       SettingUtil.getSetting("training", "training_url").then(
         function(setting) {
           $scope.userCtx.trainingUrl = setting.value;
         }
       );
     }

     $scope.userCreateUpdateOpts = {resource: 'User', operations: ['Create', 'Update']};
     $scope.cpReadOpts = {resource: 'CollectionProtocol', operations: ['Read']};
     $scope.containerReadOpts = {resource: 'StorageContainer', operations: ['Read']};
     $scope.orderReadOpts = {resource: 'Order', operations: ['Read']};
     $scope.shipmentReadOpts = {resource: 'ShippingAndTracking', operations: ['Read']};
     $scope.scheduledJobReadOpts = {resource: 'ScheduledJob', operations: ['Read']};

     init();
  })
