angular.module('openspecimen')
  .controller('FeedbackCtrl', function($scope, $modal) {
    $scope.openFeedbackForm = function() {
       var modalInstance = $modal.open({
         templateUrl: 'modules/user/feedback.html',
         controller: modalCtrl
       });
     }
     
     var modalCtrl = function($scope, $translate, $modalInstance, User, Alerts) {
       $scope.feedbackDetail = {};
       $scope.submit = function() {
         User.sendFeedback($scope.feedback).then(
           function(resp) {
             $modalInstance.close('ok');
             if(resp.status == 'ok') {
               Alerts.success($translate.instant('feedback.success_message'));
             }
           }
         );
       }
       
       $scope.cancel = function() {
         $modalInstance.dismiss('cancel');
       }
     }
  });
