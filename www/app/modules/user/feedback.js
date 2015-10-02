angular.module('openspecimen')
  .controller('FeedbackCtrl', function($scope, $modal, User, Alerts) {

    function sendFeedback(feedback) {
      User.sendFeedback(feedback).then(
        function(resp) {
          Alerts.success($translate.instant('feedback.success_message'));
        }
      );
    }

    $scope.openFeedbackForm = function() {
       var modalInstance = $modal.open({
         templateUrl: 'modules/user/feedback.html',
         controller: function ($scope, $modalInstance) {
           $scope.submit = function() {
             $modalInstance.close($scope.feedback);
           }
       
           $scope.cancel = function() {
             $modalInstance.dismiss('cancel');
           }
         }
       });

       modalInstance.result.then(
         function(feedback) {
           sendFeedback(feedback); 
         }
       );
     }
  });
