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
              confirmDelete: confirmDelete,
              forceDelete: !!props.forceDelete
            }
          },
          dependentEntities: function() {
            return props.deleteWithoutCheck ? [] :  object.getDependentEntities();
          }
        }
      });

      modalInstance.result.then(
        function(object) {
          if (typeof props.onDeletion == 'function') {
            props.onDeletion();
          } else {
            $state.go(props.onDeleteState);
          }
        },

        function() {
          if (typeof props.onDeleteFail == 'function') {
            props.onDeleteFail();
          }
        }
      );
    }

    function confirmDelete(opts) {
      var modalInstance = $modal.open({
        templateUrl: opts.templateUrl,
        controller: function($scope, $modalInstance, dependentEntities) {
          $scope.dependentEntities = dependentEntities;
          $scope.entity = opts.entity;
          $scope.props = opts.props;

          $scope.ok = function() {
            $modalInstance.close(true);
          }

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        },
        resolve: {
          //
          // Default behaviour is to delete record without checks for
          // dependent records. In order to check for dependent records
          // before deleting, set opts.deleteWithoutCheck = false.
          //
          dependentEntities: function() {
            return opts.deleteWithoutCheck === false && opts.entity ? opts.entity.getDependentEntities() : [];
          }
        }
      });

      modalInstance.result.then(
        function() {
          opts.delete();
        }
      );
    };

    function bulkDelete(object, entityIds, props) {
      var confirmDelete = props.confirmDelete || 'delete_entity.confirm_delete';
      var successMessage = props.successMessage || 'delete_entity.entity_deleted';
      var modalInstance = $modal.open({
        templateUrl: 'modules/common/delete/modal.html',
        controller: 'EntityDeleteCtrl',
        resolve: {
          entityProps: function() {
            return {
              entity: object,
              entityIds: entityIds,
              confirmDelete: confirmDelete,
              successMessage: successMessage,
              bulkDelete: true
            }
          },
          dependentEntities: function() {
            return [];
          }
        }
      });

      modalInstance.result.then(
        function(object) {
          if (typeof props.onBulkDeletion == 'function') {
            props.onBulkDeletion(object);
          }
        }
      );
    }

    return {
      delete : deleteEntity,

      bulkDelete : bulkDelete,

      confirmDelete: confirmDelete
    }

  });
