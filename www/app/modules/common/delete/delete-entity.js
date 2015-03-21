angular.module('os.common.delete', [])
  .controller('entityDeleteCtrl', function($scope, $modalInstance, $translate, entityProps, entityDependencyStat, Alerts) {

   function init() {
      $scope.entity = entityProps.entity;
      $scope.entityProps = entityProps;
      $scope.entityDependencyStat = $.isEmptyObject(entityDependencyStat) ? undefined : entityDependencyStat;
    }

    function onDeleted(entity) {
      if (!!entity) {
        $translate("entity.entity_deleted", {name: entityProps.name})
          .then(function(msg) {
            Alerts.success(msg);
          })
        $modalInstance.close(entity);
      }
    }

    $scope.delete = function () {
      $scope.entity.$remove().then(onDeleted)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


