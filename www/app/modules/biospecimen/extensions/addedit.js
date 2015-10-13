
angular.module('os.biospecimen.extensions.addedit-record', [])
  .controller('FormRecordAddEditCtrl', function($scope, $state, $stateParams, formDef, postSaveFilters, ExtensionsUtil, Alerts) {
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
      showActionBtns: true,

      onSave: function(formData) {
        angular.forEach(postSaveFilters, function(filter) {
          filter($scope.object, formDef.name, formData);
        });

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
        var record = {recordId: recId, formId: $stateParams.formId, formCaption: formDef.caption}
        ExtensionsUtil.deleteRecord(record, function(record) {
          $scope.back();
        });
      }
    };
  });
