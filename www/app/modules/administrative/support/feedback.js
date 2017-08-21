angular.module('openspecimen')
  .controller('FeedbackCtrl', function($scope, $translate, $modal, Support, Alerts) {

    function sendFeedback(feedback) {
      Support.sendFeedback(feedback).then(
        function(resp) {
          Alerts.success($translate.instant('feedback.success_message'));
        }
      );
    }

    $scope.openFeedbackForm = function() {
       var modalInstance = $modal.open({
         templateUrl: 'modules/administrative/support/feedback.html',
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
