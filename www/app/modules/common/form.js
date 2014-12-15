angular.module('os.common.form', [])
  .directive('osFormValidator', function() {
    return {
      restrict: 'A',

      controller: function($scope) {
        this._formSubmitted = false;

        this._parentValidator = undefined;

        this._form = undefined;

        this.formSubmitted = function(val) {
          this._formSubmitted = val;
        };

        this.isFormSubmitted = function() {
          return this._formSubmitted;
        };

        this.setForm = function(form) {
          this._form = form;
        };

        this.isValidForm = function() {
          return !this._form.$invalid;
        };

        this.isInteracted = function(field) {
          if (this._parentValidator && this._parentValidator.isFormSubmitted()) {
            return true;
          }
            
          return this.isFormSubmitted() || (field && field.$dirty);
        };

        this.setParentValidator = function(parentValidator) {
          this._parentValidator = parentValidator;
        };
      },

      link: function(scope, element, attrs, controller) {
        scope.$watch(attrs.osFormValidator, function(form) {
          controller.setForm(form);
        });

        if (attrs.validator) {
          scope[attrs.validator] = controller;
        }

        if (attrs.parentValidator) {
          scope.$watch(attrs.parentValidator, function(parentValidator) {
            controller.setParentValidator(parentValidator);
          });
        }
      }
    };
  })

  .directive('osFormSubmit', function() {
    return {
      restrict: 'A',
  
      require: '^osFormValidator',

      priority: 1,

      terminal: true,

      link: function(scope, element, attrs, ctrl) {
        var ngClick = attrs.ngClick;

        element.bind('click', function() {
          ctrl.formSubmitted(true);
          if (ctrl.isValidForm()) {
            scope.$eval(ngClick);
          }
        });
      }
    };
  })

  .directive('osFieldError', function() {
    return {
      restrict: 'A',

      require: '^osFormValidator',

      scope: {
        field: '='
      },

      link: function(scope, element, attrs, ctrl) {
        scope.isInteracted = function() {
          return ctrl.isInteracted(scope.field);
        };
      },

      replace: 'true',

      template: 
        '<div>' +
        '  <div ng-if="isInteracted()" class="alert alert-danger os-form-err-msg" ' +
        '    ng-messages="field.$error" ng-messages-include="modules/common/error-messages.html"> ' +
        '  </div>' +
        '</div>'
    };
  });
