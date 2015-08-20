angular.module('os.common.delete', [])
  .controller('EntityDeleteCtrl', function($scope, $modalInstance, entityProps, dependentEntities, Alerts) {

    function init() {
      $scope.entity = entityProps.entity;
      $scope.entityProps = entityProps;
      $scope.dependentEntities = dependentEntities;
      $scope.entityIds = entityProps.entityIds;
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

    $scope.delete = function () {
      if (entityProps.entityIds) {
        $scope.bulkDelete();
      } else {
        $scope.entity.$remove().then(onDeletion)
      }
    };

    $scope.bulkDelete = function () {
      $scope.entity.bulkDelete($scope.entityIds).then(onBulkDeletion)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


