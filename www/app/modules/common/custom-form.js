angular.module("openspecimen")
  .directive('osCustomForm', function($timeout) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        
        if (scope.customForm) {
          scope.formOpts = {
            formId: scope.customForm.formId,
            recordId: scope.customForm.recordId,
            formCtxtId: parseInt(scope.customForm.formCtxtId),
            objectId: scope.customForm.objectId
          }
        }

        scope.$watch("form.rendered", function(newVal) {
          if (newVal) {
              reformHtml();
          }
        });

        scope.getCustomFormData = function () {
          if (!!scope.customForm) {
             var formData = {appData: scope.form.appData};
             angular.extend(formData, scope.form.getValue());
             scope.customForm.formDataJson = JSON.stringify(formData);
          }
          return scope.customForm;
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

