
angular.module('openspecimen')
  .directive('osWizard', function() {
    return {
      restrict: 'A',

      transclude: true,

      controller: function($scope) {
        $scope.ctrl = this;
        $scope.steps = [];

        this.addStep = function(step) {
          angular.extend(step, {selected: false, finished: false});
          $scope.steps.push(step);
        };

        this.previous = function() {
          this.forward = false;
          if ($scope.selectedStep == 0) {
            return false;
          }

          if ($scope.steps[$scope.selectedStep].onFinish() &&
              !$scope.steps[$scope.selectedStep].onFinish()(this.forward)) {
            return false;
          }

          $scope.selectedStep--;
          $scope.steps[$scope.selectedStep + 1].selected = false;
          $scope.steps[$scope.selectedStep].selected = true;
          $scope.steps[$scope.selectedStep].finished = false;
          return true;
        };

        this.next = function() {
          this.forward = true;
          if ($scope.steps[$scope.selectedStep].onFinish() &&
              !$scope.steps[$scope.selectedStep].onFinish()(this.forward)) {
            return false;
          }

          if ($scope.selectedStep == $scope.steps.length - 1) {
            return true; // should it return true or false; // earlier it was false
          }

          $scope.selectedStep++;
          $scope.steps[$scope.selectedStep - 1].finished = true;
          $scope.steps[$scope.selectedStep - 1].selected = false;
          $scope.steps[$scope.selectedStep].selected= true;
          return true;
        };

        this.getCurrentStep = function() {
          return $scope.selectedStep ;
        };

        $scope.gotoStep = function(step) {
          if ($scope.selectedStep == step) {
            return;
          }

          var fn = undefined;
          var numSteps = undefined;
          if (step > $scope.selectedStep) {
            numSteps = step - $scope.selectedStep;
            fn = $scope.ctrl.next;
          } else {
            numSteps = $scope.selectedStep - step;
            fn = $scope.ctrl.previous;
          }

          for (var i = 0; i < numSteps; ++i) {
            if(!fn()) {
              break;
            }
          }
        };

        this.isFirstStep = function() {
          return $scope.selectedStep == 0;
        };

        this.isLastStep = function() {
          return $scope.selectedStep == $scope.steps.length - 1;
        };
      },

      compile: function(element, attributes, transclude) {
        return {
          pre: function (scope, element, attrs, controller) {
            scope[attrs.osWizard] = controller;
            // scope['dfTemplate'] = (attrs.dfTemplate == undefined ? 'df_std_wizard.html' : attrs.dfTemplate);
          },

          post: function (scope, element, attributes, controller) {
            scope.selectedStep = 0;
            scope.steps[0].selected = true;
          }
        }
      },

      templateUrl: 'modules/common/wizard-template.html'
    };
  })

  .directive('osWizardStep', function() {
    return {
      restrict: 'E',
      require : '^osWizard',
      template: '<div class="os-wizard-step-content" ng-transclude ng-show="selected"></div>',
      transclude: true,
      replace : true,
      scope : {
        title : '@',
        onFinish : '&'
      },

      link: function(scope, element, attrs, wizardCtrl) {
        wizardCtrl.addStep(scope);
      }
    };
  });
