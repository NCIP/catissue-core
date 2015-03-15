angular.module('os.administrative.user.delete', ['os.administrative.models'])
  .controller('UserDeleteCtrl', function($scope, $modalInstance, $translate, user, userDependencies, Alerts) {

    var init = function() {
      $scope.user = user;
      $scope.siteDependencies = $.isEmptyObject(userDependencies) ? undefined : userDependencies;
    }
    
    var onDeleted = function(user) {
      if (!!user) {
        var name = user.lastName + ' ' + user.firstName;
        $translate('user.user_deleted', {name: name}).then(function(msg) {
          Alerts.success(msg);
        })
        $modalInstance.close(user);
      }
    }    

    $scope.delete = function () {
      $scope.user.$remove().then(onDeleted);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init(); 
  })

