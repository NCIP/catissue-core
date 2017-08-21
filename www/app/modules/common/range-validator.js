angular.module('openspecimen')
  .directive('osEnsureRange', function() {
    return {
      require: '^ngModel',
 
      restrict: 'A',

      link: function(scope, element, attr, modelCtrl) {
        modelCtrl.$validators.ensureRange = function(modelValue, viewValue) {
          var props = scope.$eval(attr.osEnsureRange);

          //
          // To begin with, all restrictions/rules are satisfied
          //
          angular.forEach(props, function(prop) {
            modelCtrl.$setValidity(prop.errorKey, true);
          });
          modelCtrl.$setValidity('nan', true);

          if (modelCtrl.$isEmpty(modelValue)) {
            return true;
          }

          var val = +modelValue;
          if (isNaN(val)) {
            modelCtrl.$setValidity('nan', false);
            return false;
          }

          for (var i = 0; i < props.length; ++i) {
            var prop = props[i];
            var truthy = true;
            if (prop.cmp == 'lt') {
              truthy = (val < prop.tgt);
            } else if (prop.cmp == 'le') {
              truthy = (val <= prop.tgt);
            } else if (prop.cmp == 'gt') {
              truthy = (val > prop.tgt);
            } else if (prop.cmp == 'ge') {
              truthy = (val >= prop.tgt);
            } else if (prop.cmp == 'eq') {
              truthy = (val == prop.tgt);
            } else if (prop.cmp == 'ne') {
              truthy = (val != prop.tgt);
            }

            modelCtrl.$setValidity(prop.errorKey, truthy);
            if (!truthy) {
              return false;
            }
          }

          return true;
        };
      }
    };
  });
