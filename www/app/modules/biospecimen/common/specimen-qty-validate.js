angular.module('os.biospecimen.common.specimenqtyvalidate', [])
  .directive('osSpecimenQtyValidators', function() {
    return {
      controller: function($scope) {
        this.modelCtrls = {};
        this.validateInProgress = false;

        this.addCtrl = function(idx, ctrl) {
          if (this.modelCtrls[idx]) {
            this.modelCtrls[idx].push(ctrl);
          } else {
            this.modelCtrls[idx] = [ctrl];
          }
        };

        this.removeCtrl = function(idx) {
          delete this.modelCtrls[idx];
        };

        this.validate = function(ctrl, ctrlIdx) {
          this.validateInProgress = true;

          var toRemove = [];
          angular.forEach(this.modelCtrls, function(modelCtrl, idx) {
            if (ctrlIdx == idx) {
              return;
            }

            modelCtrl[0].$validate();
            if (modelCtrl[0].$valid) {
              angular.forEach(modelCtrl, function(c) {
                c.$setValidity('specimenQty', true);
              });
              toRemove.push(idx);
            }
          });

          var that = this;
          angular.forEach(toRemove, function(i) {
            delete that.modelCtrls[i];
          });

          this.validateInProgress = false;
        }
      }
    };
  })
  .directive('osSpecimenQtyValidate', function($q, $timeout) {
    return {
      require: ['ngModel', '^osSpecimenQtyValidators'],
  
      link: function(scope, elm, attrs, ctrls) {
        var modelCtrl = ctrls[0];
        var validators = ctrls[1];
        var props = scope.$eval(attrs.osSpecimenQtyValidate);

        modelCtrl.$validators.specimenQty = function(modelValue, viewValue) {
          if (modelCtrl.$isEmpty(modelValue)) {
            return true;
          }

          var specimen = props.specimen;
          if (!specimen.hasSufficientQty()) {
            validators.addCtrl(props.idx, modelCtrl);
            return false;
          } 

          var root = specimen.rootSpecimen();
          if (root != specimen && !root.hasSufficientQty()) {
            validators.addCtrl(props.idx, modelCtrl);
            return false;
          }

          if (!validators.validateInProgress) {
            validators.validate(modelCtrl, props.idx);
            validators.removeCtrl(props.idx);
          }
          return true;
        }
      }
    };
  });
