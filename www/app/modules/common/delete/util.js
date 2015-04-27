angular.module('os.common.delete')
  .factory('DeleteUtil', function($modal, $state, $translate) {
  
    function deleteEntity(object, props) {
      var confirmDelete = props.confirmDelete ? props.confirmDelete : 'delete_entity.confirm_delete';

      var modalInstance = $modal.open({
        templateUrl: 'modules/common/delete/modal.html',
        controller: 'EntityDeleteCtrl',
        resolve: {
          entityProps: function() {
            return {
              entity: object,
              entityName: object.getDisplayName(),
              entityType: $translate.instant("entities." + object.getType()),
              confirmDelete: confirmDelete
            }
          },
          dependentEntities: function() {
            return props.deleteWithoutCheck ? [] :  object.getDependentEntities();
          }
        }
      });

      modalInstance.result.then(function(object) {
        if (!props.onDeletion) {
           $state.go(props.onDeleteState);
        } else {
          props.onDeletion();
        }
      });
    }

    return {
      delete : deleteEntity
    }

  });
