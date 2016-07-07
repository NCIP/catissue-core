
angular.module('os.administrative.user')
  .controller('AnnouncementCtrl', function($scope, $modalInstance, Setting) {
    function init() {
      $scope.ctx = {
        announcement: {},
        activeFromDt: new Date()
      }

      getActiveUserFromDate();
    }

    function getActiveUserFromDate() {
      Setting.getByProp('administrative', 'active_users_login_days').then(
        function(property) {
          var fromDate = new Date();
          fromDate.setDate(new Date().getDate() - property.value);
          $scope.ctx.activeFromDt = fromDate;
        }
      );
    }

    $scope.submit = function() {
      $modalInstance.close($scope.ctx.announcement);
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  });
