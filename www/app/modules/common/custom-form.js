angular.module("openspecimen")
  .directive('osCustomForm', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {

        scope.$watch(attrs.osCustomForm, function(entity) {
          if (!entity) {
            return;
          }

          entity.getFormCtxt().then(
            function(formCtxt) {
              if (!!formCtxt) {
                scope.formOpts = {
                  formId: formCtxt.formId,
                  recordId: !!entity.id ? entity.extensionDetail.id : undefined,
                  formCtxtId: parseInt(formCtxt.formCtxtId),
                  objectId: entity.id
                }
              }
            }
          );
        });
      }
    };
  });

