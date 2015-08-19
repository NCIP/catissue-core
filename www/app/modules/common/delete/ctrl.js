angular.module('os.common.delete', [])
  .controller('EntityDeleteCtrl', function($scope, $modalInstance, entityProps, dependentEntities, Alerts) {

   function init() {
      $scope.entity = entityProps.entity;
      $scope.entityProps = entityProps;
      $scope.dependentEntities = dependentEntities;
      $scope.bulkDeleteSuccess = entityProps.successMessage;
      $scope.entityIds = entityProps.entityIds;
      $scope.deleteAll = entityProps.deleteAll;
    }

    function onDeletion(entity) {
      if (!!entity) {
        if ($scope.bulkDeleteSuccess) {
          Alerts.success($scope.bulkDeleteSuccess);
        } else {
          Alerts.success("delete_entity.entity_deleted", entityProps);
        }
        $modalInstance.close(entity);
      }
    }

    $scope.delete = function () {
      $scope.entity.$remove().then(onDeletion)
    };

    $scope.bulkDelete = function () {
      $scope.entity.bulkDelete($scope.entityIds).then(onDeletion)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


