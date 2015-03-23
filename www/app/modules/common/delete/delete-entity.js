angular.module('os.common.delete', [])
  .controller('EntityDeleteCtrl', function($scope, $modalInstance, $translate, entityProps, dependentEntities, Alerts) {

   function init() {
      $scope.entity = entityProps.entity;
      $scope.entityProps = entityProps;
      $scope.dependentEntities = dependentEntities;
    }

    function onDeleted(entity) {
      if (!!entity) {
        Alerts.success("delete_entity.entity_deleted", 
          {entityName: entityProps.name, entityType: entityProps.type});

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


