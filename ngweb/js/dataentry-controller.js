
angular.module('plus.dataentry', [])
  .controller('FormsDataController', ['$scope', 'FormsService', function($scope, FormsService) {

    $scope.formatDate = function(timeInMs) {
      return Utility.formatDate(timeInMs);
    };
    
    $scope.showForms = function(redirect) {
      if (redirect == null || redirect == undefined) {
        redirect = true;
      }
      $scope.currentView = 'forms-list'; // default to begin with
      var formsq = undefined;
      if (entity == 'Participant') {
        formsq = FormsService.getCprForms(entityObjId);
      } else if (entity == 'Specimen') {
        formsq = FormsService.getSpecimenForms(entityObjId);
      } else if (entity == 'SpecimenCollectionGroup') {
        formsq = FormsService.getScgForms(entityObjId);
      }
 
      formsq.then(function(formsList) {
        if (formsList.length == 1 && redirect) {
          $scope.displayRecords(formsList[0], redirect);
        } else {
          $scope.formsList = formsList;
        }
      });
    };

    $scope.showForms();
    
    $scope.addRecord = function() {
      $scope.renderForm($scope.form);
    };
    
    $scope.displayRecords = function(form, redirect) {
      if (redirect == null || redirect == undefined) {
        redirect = true;
      }

      $scope.form = form;
      var recordsq = undefined;
      if (entity == 'Participant') {
        recordsq = FormsService.getCprFormRecords(entityObjId, form.formCtxtId);
      } else if (entity == 'Specimen') {
        recordsq = FormsService.getSpecimenFormRecords(entityObjId, form.formCtxtId);
      } else if (entity == 'SpecimenCollectionGroup') {
        recordsq = FormsService.getScgFormRecords(entityObjId, form.formCtxtId);
      }

      recordsq.then(function(records) {
        if (records.length == 0 && redirect) {
          $scope.renderForm(form);
        } else if (records.length == 1 && redirect) {
          $scope.renderForm(form, records[0].recordId);
        } else {
          $scope.records = records;
          $scope.currentView = 'records-list';
        }
      });
    };
      
    $scope.editFormData = function(record) {
      $scope.renderForm($scope.form, record.recordId);
    };
    
    $scope.renderForm = function(form, recordId) {
      if ( $scope.currentView == 'form') {
        // link has been clicked more than once.. !
        return;
      }

      $scope.currentView = 'form';
      FormsService.getFormDef(form.formId).then(function(data) {
        var that = this;
        this.form = new edu.common.de.Form({
          id           : form.formId,
          formDef      : data,
          recordId     : recordId,
          formDiv      : 'form-view',
          appData      : {formCtxtId: form.formCtxtId, objectId: entityObjId},
          formDataUrl  : '/catissuecore/rest/ng/forms/:formId/data/:recordId',
          fileUploadUrl : '/catissuecore/rest/ng/form-files',
          fileDownloadUrl: function(formId, recordId, ctrlName) {
            return '/catissuecore/rest/ng/form-files?formId=' + formId + '&recordId=' + recordId + '&ctrlName=' + ctrlName;
          },

          onSaveSuccess: function() {
            that.form.destroy();
            $scope.displayRecords(form, false);
            Utility.notify($("#notifications"), "Form Data Saved", "success", true);
          },

          onSaveError: function() {
            Utility.notify($("#notifications"), "Form Data Save Failed", "error", true);
          },

          onValidationError: function() {
            Utility.notify($("#notifications"), "There are some errors on form. Please rectify them before saving", "error", true);
          },

          onCancel: function() {
            that.form.destroy();
            $scope.displayRecords(form, false);
          }
        });
        this.form.render();
      });
    }; 
  }]);
