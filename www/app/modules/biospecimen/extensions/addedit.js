
angular.module('os.biospecimen.extensions.addedit-record', [])
  .controller('FormRecordAddEditCtrl', function($scope, $state, $stateParams, formDef, Form, Alerts) {
    var recId = $stateParams.recordId;
    if (!!recId) {
      recId = parseInt(recId);
    }

    $scope.formOpts = {
      formId: $stateParams.formId,
      formDef: formDef,
      recordId: recId,
      formCtxtId: parseInt($stateParams.formCtxId),
      objectId: $scope.object.id,

      onSave: function() {
        $scope.back();
        Alerts.success("extensions.record_saved");
      },

      onError: function() {
        alert("Error");
      },

      onCancel: function() {
        $scope.back();
      },

      onPrint: function(html) {
        alert(html);
      },

      onDelete: function() {
        Form.deleteRecord($stateParams.formId, recId).then(
          function() {
            $scope.back();
            Alerts.success('extensions.record_deleted');
          }
        );
      }
    };
  });
