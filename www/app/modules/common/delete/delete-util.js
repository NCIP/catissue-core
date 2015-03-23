DeleteUtil =  {
  delete: function(object, props, $modal, $state) {
    var modalInstance = $modal.open({
      templateUrl: 'modules/common/delete/delete-entity-template.html',
      controller: 'EntityDeleteCtrl',
      resolve: {
        entityProps: function() {
          return {
            entity: object,
            name: props.entityNameProp,
            type: props.entityTypeProp
          }
        },
        dependentEntities: function() {
          return object.getDependentEntities();
        }
      }
    });

    modalInstance.result.then(function(object) {
      $state.go(props.onDeleteState);
    });
  }
}
