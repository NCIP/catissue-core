
angular.module('os.biospecimen.extensions', ['os.biospecimen.models'])
  .directive('osDeForm', function($http, Form) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        var onceRendered = false;
        scope.$watch(attrs.opts, function(opts, oldVal) {
          if (onceRendered) {
            return;
          }

          var baseUrl = Form.url();
          var hdrs = {
            'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']
          };
          var args = {
            id            : opts.formId,
            formDiv       : element,
            formDef       : opts.formDef,
            formDefUrl    : baseUrl + '/:formId/definition',
            formDataUrl   : baseUrl + '/:formId/data/:recordId',
            formSaveUrl   : baseUrl + '/:formId/data',
            recordId      : opts.recordId,
            dateFormat    : 'mm-dd-yyyy',
            appData       : {formCtxtId: opts.formCtxtId, objectId: opts.objectId},
            onSaveSuccess : opts.onSave,
            onSaveError   : opts.onError,
            onCancel      : opts.onCancel,
            onPrint       : opts.onPrint,
            onDelete      : opts.onDelete,
            customHdrs    : hdrs
          };

          var form = new edu.common.de.Form(args);
          form.render();
          onceRendered = true;
        }, true);
      }
    }
  });
