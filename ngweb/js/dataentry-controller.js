
angular.module('plus.dataentry', [])
  .controller('FormsDataController', ['$scope', '$filter', '$timeout', 'FormsService', function($scope, $filter, $timeout, FormsService) {

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
      } else if (entity == 'SpecimenEvent'){
        formsq = FormsService.getSpecimenEventForms(entityObjId);
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
      $scope.showDeleteBtn = false;
      var recordsq = undefined;

      if (entity == 'Participant') {
        recordsq = FormsService.getCprFormRecords(entityObjId, form.formCtxtId);
      } else if ( (entity == 'Specimen') || (entity == 'SpecimenEvent') ) {
        recordsq = FormsService.getSpecimenFormRecords(entityObjId, form.formCtxtId);
      } else if (entity == 'SpecimenCollectionGroup') {
        recordsq = FormsService.getScgFormRecords(entityObjId, form.formCtxtId);
      }

      recordsq.then(function(records) {
        if (records.length == 0 && redirect) {
          $scope.renderForm(form);
        } else if (records.length == 1 && redirect && !form.multiRecord) {
          $scope.renderForm(form, records[0].recordId);
        } else {
          $scope.records = records;
          $scope.currentView = 'records-list';
        }
      });
    };
    
    $scope.showDeleteButton = function() {
      var show = false;
      for (var i = 0; i < $scope.records.length; ++i) {
        if ($scope.records[i].mfd) {
          show = true;
          break;
        }
      }
      
      return show;
    }
    
    $scope.deleteRecords = function() {
      var recIds = [];
      for (var i = 0 ; i < $scope.records.length ; i++) {
        if ($scope.records[i].mfd) {
          recIds.push($scope.records[i].recordId);
        }
      }
      
      FormsService.deleteRecords($scope.form.formId, recIds).then(function(deletedRecIds) {
        for (var i = $scope.records.length - 1; i >= 0; --i) {
          if (deletedRecIds.indexOf($scope.records[i].recordId) != -1) {
            $scope.records.splice(i, 1);
          }
        }
      });
    };

    $scope.deleteFormRecord = function(recordId) {
      var recIds = [];
      recIds.push(recordId);

      FormsService.deleteRecords($scope.form.formId, recIds).then(function(deletedRecIds) {
        if ($scope.currentView  =='forms-list') {
          $scope.showForms();
          return;
        } 
        
        for (var i = $scope.records.length - 1; i >= 0; --i) {
          if (deletedRecIds.indexOf($scope.records[i].recordId) != -1) {
            $scope.records.splice(i, 1);
          }
        }
      });
    };
      
    $scope.editFormData = function(record) {
      $scope.renderForm($scope.form, record.recordId);
    };
    
    $scope.setCurrentView = function() {
      if ($scope.form.multiRecord) {
        $scope.currentView = 'records-list';
      } else {
        $scope.currentView = 'forms-list';
      }
    };
    
    $scope.renderForm = function(form, recordId) {
      if ( $scope.currentView == 'form') {
        // link has been clicked more than once.. !
        return;
      }
      $scope.printCaption = "temp name";
      $scope.currentView = 'form';
      FormsService.getFormDef(form.formId).then(function(data) {
        var that = this;
        var _reqTime = new Date().getTime();
        this.form = new edu.common.de.Form({
          id           : form.formId,
          formDef      : data,
          recordId     : recordId,
          formDiv      : 'form-view',
          dateFormat   : dateFormat,
          appData      : {formCtxtId: form.formCtxtId, objectId: entityObjId},
          formDataUrl  : '/openspecimen/rest/ng/forms/:formId/data/:recordId?_reqTime='+_reqTime,
          formSaveUrl  : '/openspecimen/rest/ng/forms/:formId/data',
          fileUploadUrl : '/openspecimen/rest/ng/form-files?_reqTime='+_reqTime,
          fileDownloadUrl: function(formId, recordId, ctrlName) {
            return '/openspecimen/rest/ng/form-files?formId=' + formId + '&recordId=' + recordId + '&ctrlName=' + ctrlName + '&_reqTime='+_reqTime;
          },

          onSaveSuccess: function() {
            that.form.destroy();
            $scope.setCurrentView();
            if ($scope.currentView  != 'forms-list') {
              $scope.displayRecords(form, false);
            } else {
              $scope.showForms();
            }
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
            $scope.setCurrentView();
            if ($scope.currentView  !='forms-list') {
              $scope.displayRecords(form, false);
            } else {
              $scope.showForms();
            }
          },
          
          onDelete: function() {
            that.form.destroy();
            $scope.setCurrentView();
            $scope.deleteFormRecord(recordId);
            
            Utility.notify($("#notifications"), "Form Data Deleted", "success", true);
          },
          
          onPrint: function(printableFormHtml) {
            $scope.printableFormHtml = printableFormHtml.html();
            $scope.printCaption = form.formCaption;
            $scope.headerInfo = 
              {user: {
                printedOn: ($filter('date')(new Date().getTime(), 'MM-dd-yyyy HH:mm')),
                printedBy: userName
              },
                 
              participant: {
                ppId    : ppId,
                cpTitle : cpTitle
              },
               
              scg: {
                label        : scgLabel,
                cpEventLabel : cpEventLabel
              },
               
              specimen: {
                label : specimenLabel
              }};
            
            if (!$scope.$$phase) {
            	$scope.$apply();
            }
            $timeout(function() { 
              Utility.printHtml($('#print-view').html(), form.formCaption);
            }, 0);
          }
        });
        this.form.render();
      });
    }; 
  }]);