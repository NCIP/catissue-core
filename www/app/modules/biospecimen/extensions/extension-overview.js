angular.module("os.biospecimen.extensions")
  .directive("osExtensionOverview", function(ExtensionsUtil) {

     function processAttrs(formId, recordId, attrs) {
       angular.forEach(attrs, function(attr) {
         if (attr.type == 'subForm') {
           if (attr.value instanceof Array) {
             angular.forEach(attr.value, function(sfAttrs) {
               processAttrs(formId, recordId, sfAttrs);
             })
           }
         } else {
           if (attr.type == 'fileUpload') {
             attr.fileDownloadUrl = ExtensionsUtil.getFileDownloadUrl(formId, recordId, attr.name);
           }
         }
       });
     }

     return {
       restrict: 'A',

       templateUrl: 'modules/biospecimen/extensions/extension-overview.html',

       scope: {
         extObject : "=",
         showColumn: "="
       },

       link: function(scope, element, attrs) {
         processAttrs(scope.extObject.formId, scope.extObject.id, scope.extObject.attrs);
       }
     }
  });
