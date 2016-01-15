
angular.module('os.common.delete')
  .controller('CommonCloseCtrl', function($scope, $modalInstance, entityName) {
    function init() {
      $scope.ctx = {
        entityName: entityName,
        reason: undefined
      }
    };

    $scope.close = function() {
      $modalInstance.close($scope.ctx.reason);
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    }

    init();
  })

  .factory('CloseUtil', function($modal) {
    return {
      close: function(entity, callback) {
        return $modal.open({
          templateUrl: 'modules/common/delete/close.html',
          controller: 'CommonCloseCtrl',
          resolve: {
            entityName: function() {
              return entity.getDisplayName()
            }
          }
        }).result.then(callback);
      }
    }
  });
