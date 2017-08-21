/*
* For temporary we hide the print button using css.
* Css changes are in extension.css file.
*/
angular.module('os.biospecimen.extensions', ['os.biospecimen.models'])
  .directive('osDeForm', function($http, $rootScope, Form, ApiUrls, ExtensionsUtil, LocationChangeListener) {
    return {
      restrict: 'A',
      controller: function() {
        this.form = null;

        this.validate = function() {
          return this.form.validate();
        }

        this.evaluateSkipLogic = function() {
          if (this.form) {
            this.form.evaluateSkipLogic();
          }
        }

        this.getFormData = function() {
          var attrValues = [];
          angular.forEach(this.form.getValue(), function(value, key) {
            attrValues.push({name: key, value: value});
          });

          return {attrs: attrValues};
        }
      },

      link: function(scope, element, attrs, ctrl) {
        if (!!attrs.ctrl) {
          var parts = attrs.ctrl.split("\.")
          var obj = scope;
          angular.forEach(parts, function(part) {
            obj = obj[part];
          });
          obj.ctrl = ctrl;
        }

        var onceRendered = false;
        scope.$watch(attrs.opts, function(opts, oldVal) {
          if (!opts || onceRendered) {
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
            formDefUrl     : baseUrl + '/:formId/definition?maxPvs=100',
            formDataUrl    : baseUrl + '/:formId/data/:recordId',
            formSaveUrl    : baseUrl + '/:formId/data',
            formPvUrl      : baseUrl + '/:formId/permissible-values',
            fileUploadUrl  : filesUrl,
            fileDownloadUrl: ExtensionsUtil.getFileDownloadUrl,
            formData       : opts.formData,
            recordId       : opts.recordId,
            dateFormat     : $rootScope.global.queryDateFmt.format,
            appData        : {formCtxtId: opts.formCtxtId, objectId: opts.objectId},
            onSaveSuccess  : opts.onSave,
            onSaveError    : opts.onError,
            onCancel       : opts.onCancel,
            onPrint        : opts.onPrint,
            onDelete       : opts.onDelete,
            showActionBtns : opts.showActionBtns,
            showPanel      : opts.showPanel,
            customHdrs     : hdrs,
            skipLogicFieldValue   : opts.skipLogicFieldValue ||
              function(attrName) {
                return ctrl.extendedObj ? ctrl.extendedObj.attrValue(attrName) : undefined
              },
            disableFields  : opts.disableFields || []
          };

          ctrl.form = new edu.common.de.Form(args);
          ctrl.form.render();
          onceRendered = true;
          LocationChangeListener.preventChange();
          addWatchForDomChanges(opts); 
        }, true);

        if (attrs.extendedObj) {
          scope.$watch(attrs.extendedObj,
            function(extendedObj) {
              ctrl.extendedObj = extendedObj;
              ctrl.evaluateSkipLogic();
            },
            true
          );
        }

        function addWatchForDomChanges(opts) {
          if (opts.labelAlignment == 'horizontal') {
            var domChange = scope.$watch(
              function() {
                return element.children().length;
              },

              function(length) {
                if (length > 0) {
                  domChange(); // kill the watch
                  alignLabelsHorizontally();
                }
              }
            );
          }
        }

        function alignLabelsHorizontally() {
          element.find(".col-xs-8").each(
            function(index) {
              angular.element(this).removeClass("col-xs-8").addClass("col-xs-12");
            }
          );

          element.find('label.control-label').each(
            function(index) {
              angular.element(this).addClass("col-xs-3");
              angular.element(this).siblings().wrapAll("<div class='col-xs-6'></div>");
            }
          );
        }
      }
    }
  });
