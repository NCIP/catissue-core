
angular.module('os.biospecimen.extensions')
  .directive('osDeTable', function($http, $rootScope, Form, ApiUrls, ExtensionsUtil) {
    return {
      restrict: 'A',
      controller: function() {
        this.getData = function() {
          return this.table.getData();
        }
        
        this.copyFirstToAll = function() {
          this.table.copyFirstToAll();
        }
      },
      
      link: function(scope, element, attrs, ctrl) {
        element.attr('id', 'de-form');    //added because fancy controls assume de-form element

        if (!!attrs.ctrl) {
          var parts = attrs.ctrl.split('\.');
          var obj = scope;
          angular.forEach(parts, function(part) {
            obj = obj[part];
          });
          obj.ctrl = ctrl;
        }
        
        scope.$watch(attrs.opts, function(opts) {
          if (!opts) {
            return;
          }
          
          var baseUrl = Form.url();
          var filesUrl = ApiUrls.getBaseUrl() + 'form-files';
          var hdrs = {
            'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']
          };
          
          var args = {
            formId            : opts.formId,
            formDiv           : element,
            formDef           : opts.formDef,
            formDefUrl        : baseUrl + ':formId/definition',
            fileUploadUrl     : filesUrl,
            fileDownloadUrl   : ExtensionsUtil.getFileDownloadUrl,
            dateFormat        : $rootScope.global.queryDateFmt.format,
            tableData         : opts.tableData,
            idColumnLabel     : opts.idColumnLabel,
            appColumns        : opts.appColumns,
            mode              : opts.mode,
            allowRowSelection : opts.allowRowSelection,
            customHdrs        : hdrs,
            onValidationError : opts.onValidationError
          };
          
          ctrl.table = new edu.common.de.DataTable(args);
          ctrl.table.render();
        }, true);
      }
    }
  });

