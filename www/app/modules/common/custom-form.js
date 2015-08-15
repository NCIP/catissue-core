angular.module("openspecimen")
  .directive('osCustomForm', function($timeout) {
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
        
        scope.$watch("extForm.rendered", function(newVal) {
          if (newVal) {
              reformHtml();
          }
        });

        scope.getExtFormData = function () {
          if (!scope.extForm) {
            return null;
          }

          var fieldValues = [];
          var formData = scope.extForm.getValue();
          angular.forEach(formData, function(value, key) {
            fieldValues.push({name: key, value: value});
          });

          return {fieldValues: fieldValues};
        }

        function reformHtml() {
          element.find(".col-xs-8")
            .each(function(index) {
               angular.element(this)
                 .addClass("col-xs-12")
                 .removeClass("col-xs-8");
            });

          element.find('label')
            .each(function(index) {
               angular.element(this).addClass("col-xs-3");
            });

          element.find(".form-control")
            .each(function(index) {
              angular.element(this).wrap("<div class='col-xs-6'></div>");
            });
        }
      }
    };
  });

