
angular.module('os.biospecimen.extensions', ['os.biospecimen.models'])
  .directive('osDeForm', function(Form) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        scope.$watch(attrs.opts, function(opts, oldVal) {
          var baseUrl = Form.url();
          var args = {
            id            : opts.formId,
            formDiv       : element,
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
            onDelete      : opts.onDelete
          };

          var form = new edu.common.de.Form(args);
          form.render();
        }, true);
      }
    }
  });
