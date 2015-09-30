angular.module('openspecimen')
  .controller('FeedbackCtrl', function($scope, $modalInstance, User, Alerts)  {
    $scope.feedbackDetail = {};
    $scope.submit = function() {
      User.sendFeedback($scope.feedback).then(
        function(resp) {
          $modalInstance.close('ok');
          if(resp.status == 'ok') {
            Alerts.success('Feedback sent successfully');
          } else {
            Alerts.error('Something went wrong, please contact system administrator');
          }
        }
      );
    }
    
    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }
  });
