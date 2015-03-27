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

    $scope.delete = function () {
      $scope.entity.$remove().then(onDeletion)
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    init();
  })


