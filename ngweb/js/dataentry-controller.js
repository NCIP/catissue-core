
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
      $scope.showDeleteBtn = false;
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
        	this.headerInfo = $("<label/>").append(form.formCaption).css({"font-size": "20px","text-align": "center","width": "100%"});
        	this.headerTable = $("<table />").addClass("table");
        	this.headerBody = $("<tbody/>");
        	this.headerTable.append(this.headerBody);
            var t = new Date();
            var formattedDate = t.getDate() + "/" + t.getMonth() + "/" + t.getFullYear() + " " + t.getHours() + ":" + t.getMinutes() 
        
            var userEl = $("<tr/>").append($("<td/>").addClass("cp-print-label").append("Printed on : "))
        	                       .append($("<td/>").addClass("cp-print-val").append(formattedDate))
                                   .append($("<td/>").addClass("cp-print-label").append("Printed By : "))
                                   .append($("<td/>").addClass("cp-print-val").append(userName));
            
        	var participantEl = $("<tr/>").append($("<td/>").addClass("cp-print-label").append("Participant protocol ID : "))
        	                              .append($("<td/>").addClass("cp-print-val").append(ppId))
        	                              .append($("<td/>").addClass("cp-print-label").append("CP Title : "))
        	                              .append($("<td/>").addClass("cp-print-val").append(cpTitle));
        	
        	this.headerBody.append(userEl).append(participantEl);
        	
        	if (entity == 'Specimen' || entity == 'SpecimenCollectionGroup') {
              var scgEl = $("<tr/>").append($("<td/>").addClass("cp-print-label").append("SCG Label : "))
                                    .append($("<td/>").addClass("cp-print-val").append(scgLabel))
                                    .append($("<td/>").addClass("cp-print-label").append("CP Event Label : "))
                                    .append($("<td/>").addClass("cp-print-val").append(cpEventLabel));
              this.headerBody.append(scgEl);
        	} 
        	
        	if (entity == 'specimen') {
              var specimenEl = $("<tr/>").append($("<td/>").addClass("cp-print-label").append("Specimen Label : "))
                                         .append($("<td/>").addClass("cp-print-val").append(specimenLabel))
                                         .append($("<td/>").addClass("cp-print-label"))
                                         .append($("<td/>").addClass("cp-print-val"));
              this.headerBody.append(specimenEl);
            }
        	
        	this.printView = $("#" + 'print-view');
        	this.printView.children().remove();
        	this.outerTable 
            this.printView.append(this.headerInfo).append(this.headerTable)
                          .append($("<div/>").css({"border-top": "2px solid #ccc","border-bottom": "2px solid #000", "margin-bottom": "25px"}))
                          .append(printableFormHtml);
           
            Utility.printDiv(this.printView.html(), form.formCaption);
          }
        });
        this.form.render();
      });
    }; 
  }]);
