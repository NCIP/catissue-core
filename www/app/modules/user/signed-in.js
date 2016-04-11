angular.module('openspecimen')
  .controller('SignedInCtrl', function($scope, $rootScope, currentUser, Alerts, AuthorizationService, Setting) {
     function init() {
       $scope.alerts = Alerts.messages;
       $rootScope.currentUser = currentUser;
       $scope.homeCtx = {};
       
       var isAllowed = AuthorizationService.isAllowed;
       var isParticipantRegAllowed  = isAllowed({resource: 'ParticipantPhi',   operations: ['Create']});
       var isVisitSpmnUpdateAllowed = isAllowed({resource: 'VisitAndSpecimen', operations: ['Create', 'Update']});
       $scope.rdeAllowed = isParticipantRegAllowed && isVisitSpmnUpdateAllowed;

       getTrainingUrl();
     }
     
     function getTrainingUrl() {
       Setting.query({module: 'common'}).then(function(props) {
         angular.forEach(props, function(property) {
           if (property.name == 'training_url') {
             $scope.homeCtx.trainingUrl = property.value;
           }
         });
       });
     }

     $scope.userCreateUpdateOpts = {resource: 'User', operations: ['Create', 'Update']};
     $scope.cpReadOpts = {resource: 'CollectionProtocol', operations: ['Read']};
     $scope.containerReadOpts = {resource: 'StorageContainer', operations: ['Read']};
     $scope.orderReadOpts = {resource: 'Order', operations: ['Read']};
     $scope.shipmentReadOpts = {resource: 'ShippingAndTracking', operations: ['Read']};
     $scope.scheduledJobReadOpts = {resource: 'ScheduledJob', operations: ['Read']};

     init();
  })
