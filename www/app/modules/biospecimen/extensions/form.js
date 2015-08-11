/*
* For temporary we hide the print button using css.
* Css changes are in extension.css file.
*/
angular.module('os.biospecimen.extensions', ['os.biospecimen.models'])
  .directive('osDeForm', function($http, $rootScope, Form, ApiUrls, LocationChangeListener) {
    return {
      restrict: 'A',

      link: function(scope, element, attrs) {
        var onceRendered = false;
        scope.$watch(attrs.opts, function(opts, oldVal) {
          if (onceRendered) {
            return;
          }

          var baseUrl = Form.url();
          var filesUrl = ApiUrls.getBaseUrl() + 'form-files';
          var hdrs = {
            'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']
          };
          var args = {
            id             : opts.formId,
            formDiv        : element,
            formDef        : opts.formDef,
            formDefUrl     : baseUrl + '/:formId/definition',
            formDataUrl    : baseUrl + '/:formId/data/:recordId',
            formSaveUrl    : baseUrl + '/:formId/data',
            fileUploadUrl  : filesUrl,
            fileDownloadUrl: function(formId, recordId, ctrlName) {
              var params = '?formId=' + formId +
                           '&recordId=' + recordId +
                           '&ctrlName=' + ctrlName +
                           '&_reqTime=' + new Date().getTime();
                           
              return filesUrl + params;
            },
            recordId       : opts.recordId,
            dateFormat     : $rootScope.global.queryDateFmt.format,
            appData        : {formCtxtId: opts.formCtxtId, objectId: opts.objectId},
            onSaveSuccess  : opts.onSave,
            onSaveError    : opts.onError,
            onCancel       : opts.onCancel,
            onPrint        : opts.onPrint,
            onDelete       : opts.onDelete,
            customHdrs     : hdrs
          };

          var form = new edu.common.de.Form(args);
          form.render();
          onceRendered = true;
          LocationChangeListener.preventChange();
        }, true);
      }
    }
  });
