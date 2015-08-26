angular.module('os.common.delete', [])
  .controller('EntityDeleteCtrl', function($scope, $modalInstance, entityProps, dependentEntities, Alerts) {

    function init() {
      $scope.entity = entityProps.entity;
      $scope.entityProps = entityProps;
      $scope.dependentEntities = dependentEntities;
    }

    function onDeletion(entity) {
      if (!!entity) {
        Alerts.success("delete_entity.entity_deleted", entityProps);
        $modalInstance.close(entity);
      }
    }

    function onBulkDeletion(entity) {
      if (!!entity) {
        Alerts.success(entityProps.successMessage);
        $modalInstance.close(entity);
      }
    }

    function bulkDelete() {
      $scope.entity.bulkDelete(entityProps.entityIds).then(onBulkDeletion)
    };

    $scope.delete = function () {
      if (entityProps.entityIds) {
        bulkDelete();
      } else {
        $scope.entity.$remove().then(onDeletion);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


