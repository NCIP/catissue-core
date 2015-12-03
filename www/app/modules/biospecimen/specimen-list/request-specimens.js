angular.module('os.biospecimen.specimenlist.reqspmns', ['os.biospecimen.models'])
  .controller('RequestSpecimensCtrl', function(
    $scope, $q, list, reqFormIds, Form, SpecimenRequest, Alerts) {

    function init() {
      $scope.ctx = {
        list: list,
        reqFormIds: reqFormIds,
        formsOpts: [],
        formCtrls: {},
        initForms: false
      };

      loadFormDefs(reqFormIds);
    }

    function loadFormDefs(formIds) {
      $q.all(formIds.map(Form.getDefinition)).then(
        function(formDefs) {
          angular.forEach(formDefs, function(formDef, index) {
            $scope.ctx.formsOpts.push({formDef: formDef, showActionBtns: false, showPanel: false});
            $scope.ctx.formCtrls[index] = {};
          });
          $scope.ctx.initForms = true;
        }
      );
    }

    $scope.previous = function(wizard) {
      wizard.previous();
    }

    $scope.next = function(wizard) {
      var step = wizard.getCurrentStep();
      if (!$scope.ctx.formCtrls[step].ctrl.validate()) {
        return;
      }

      wizard.next();
    }

    $scope.saveRequest = function() {
      var numForms = $scope.ctx.formsOpts.length;
      if (!$scope.ctx.formCtrls[numForms - 1].ctrl.validate()) {
        return;
      }

      var requestForms = [];
      angular.forEach($scope.ctx.formCtrls, 
        function(ctrl, index) {
          var data = ctrl.ctrl.form.getValue();
          data['containerId'] = reqFormIds[index];
          requestForms.push(data);
        }
      );

      var spmnReq = new SpecimenRequest({listId: list.id, requestForms: requestForms});
      spmnReq.$saveOrUpdate().then(
        function() {
          Alerts.success('specimen_requests.request_submitted');
          $scope.back();
        }
      );
    }

    init();
  });
