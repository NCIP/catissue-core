angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, currentUser, Alerts, AuthorizationService, SettingUtil) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
       $scope.userCtx = {
         hasPhiAccess: AuthorizationService.hasPhiAccess()
       }

       setSetting('training', 'training_url', 'trainingUrl');
       setSetting('training', 'help_link',    'helpUrl');
       setSetting('training', 'forum_link',   'forumUrl');
     }

     function setSetting(module, settingName, propName) {
       SettingUtil.getSetting(module, settingName).then(
         function(setting) {
           $scope.userCtx[propName] = setting.value;
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
