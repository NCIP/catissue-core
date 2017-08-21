
angular.module('plus.forms', [])
  .controller(
    'FormsController', ['$scope', '$modal', 'FormsService', 'CollectionProtocolService', 
    function($scope, $modal, FormsService, CollectionProtocolService) {

    $scope.cpList = [];
    $scope.formsList = [];

    var loadAllForms = function() {
      FormsService.getAllForms().then(
        function(formsList) {
          $scope.formsList = formsList;
        }
      );
    };

    loadAllForms();

    /** Remove this call and use promises... */
    CollectionProtocolService.getCpList().then(function(cpList) {
      $scope.cpList = cpList;
    });

    $scope.extnEntities = [ 
      {entity: 'Participant', name: 'Participant'},
      {entity: 'Specimen', name: 'Specimen'},
      {entity: 'SpecimenCollectionGroup', name: 'Specimen Collection Group'},
      {entity: 'SpecimenEvent', name:'Specimen Event'}
      
    ];

    $scope.formatDate = function(timeInMs) {
      return Utility.formatDate(timeInMs);
    };

    $scope.formatUsername = function(user) {
      var username = user.lastName;
      if (username && user.firstName) {
        username += ", ";
      }

      if (user.firstName) {
        username += user.firstName;
      }

      return username;
    };

    $scope.addForm = function() {
        window.top.location.href = "/openspecimen/loadcsd.do"; 
    };

    $scope.editForm = function(form) {
      window.top.location.href = "/openspecimen/loadcsd.do#loadCachedForm/" + form.formId + "/true ?_reqTime=" +  new Date().getTime();
    };

    $scope.showFormContexts = function(form) {
      form.highlight = false;

      FormsService.getFormContexts(form.formId).then(function(formCtxts) {
        var formCtxtsModal = $modal.open({
          templateUrl: 'formCtxts.html',
          controller: FormCtxtsCtrl,

          resolve: {
            args: function() {
              return {
                formCtxts: formCtxts,
                form: form,
                cpList: $scope.cpList,
                extnEntities: $scope.extnEntities
              }
            }
          }
        });

        formCtxtsModal.result.then(function(newCpCnt) {
          form.cpCount = newCpCnt;
        });
      });
    };

    var FormCtxtsCtrl = function($scope, $modalInstance, args) {
      var map = {};
      for (var i = 0; i < args.formCtxts.length; ++i) {
        var cp = args.formCtxts[i].collectionProtocol;
        var cpId = cp.id, title = cp.shortTitle;
        var level = args.formCtxts[i].level;
        var multiRecord = args.formCtxts[i].multiRecord;
        var cpFormCtxt = map[cpId];
        if (cpFormCtxt) {
          cpFormCtxt.levels.push(level);
        } else {
          map[cpId] = {title: title ? title : "ALL", levels: [level], multiRecord: multiRecord};
        }
      }

      var result = [];
      for (var k in map) {
        result.push(map[k]);
      }

      var existingCpCount = result.length;
      if (map["-1"]) {
        existingCpCount = -1;
      }

      $scope.cpFormCtxts = result;
      $scope.form = args.form;
      $scope.cpList = args.cpList;
      $scope.extnEntities = args.extnEntities;

      $scope.allProtocols = false;
      $scope.isMultiRecord = false;
      $scope.selectedCps = undefined;
      $scope.selectedEntity = undefined;
      $scope.isSpecimenEvent = false;

      $scope.onEntitySelect = function(selected) {
    	  $scope.allProtocols = $scope.isSpecimenEvent = selected.entity == 'SpecimenEvent';
      }

      $scope.enableAttach = function(allProtocols, selectedCps, selectedEntity) {
         return ((allProtocols || (selectedCps && selectedCps.length > 0)) && selectedEntity) ? true : false;
      };

      $scope.attach = function(allProtocols, selectedCps, selectedEntity, isMultiRecord) {
         var cpIds = [], newCpCnt = 0;
         if (allProtocols) { 
           cpIds = [-1];
         } else {
           for (var i = 0; i < selectedCps.length; ++i) {
             cpIds.push(selectedCps[i].id);
             if (!map[selectedCps[i].id + ""]) {
               ++newCpCnt;
             }
           }
         }

         FormsService.addFormContexts($scope.form.formId, cpIds, selectedEntity.entity, isMultiRecord).then(
           function(data) {
             var cpCnt = -1;
             if (existingCpCount != -1 && !allProtocols) {
               cpCnt = existingCpCount + newCpCnt;
             } 
               
             Utility.notify($("#notifications"), "Form Successfully Attached", "success", true);
             $modalInstance.close(cpCnt);
           },
 
           function() {
             Utility.notify($("#notifications"), "Error in attaching form. Contact administrator", "error", true);
             $modalInstance.dismiss('cancel');
           }
         );
      }

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      }
    };

    $scope.attachForm = function() {
      var formIds = [];
      for (var i = 0; i < $scope.formsList.length; ++i) {
        if ($scope.formsList[i].checked) {
          formIds.push($scope.formsList[i].containerId);
        }
      }

      var modalInstance = $modal.open({
        templateUrl: 'formAssociation.html',
        controller: formAssociationCtrl,
        resolve: {
          formIds: function() {
            return formIds;
          },
          cps: function () {
            return $scope.cps;
          },
          entities: function() {
            return $scope.entities;
          }
        }
      });
    };

    $scope.deleteForm = function(form) {
      var modalInstance = $modal.open({
        templateUrl: 'delete-form-confirm.html',
        controller: function($scope, $modalInstance, form) {
          $scope.form = form;

          $scope.ok = function() {
            $modalInstance.close('ok');
          },

          $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
          }
        },
        resolve: {
          form: function() {
            return form;
          }
        }
      });

      modalInstance.result.then(
        function() {
          FormsService.deleteForm(form.formId).then(
            function(result) {
              if (!result) {
                Utility.notify($("#notifications"), "You do not have permission to delete form", "error", true);
                return;
              }

              Utility.notify($("#notifications"), "Form deleted!", "success", true);
              loadAllForms();
            }
          );
        }
      );
    };

    var formAssociationCtrl = function ($scope, $modalInstance, cps, entities, formIds) {
      $scope.cps = cps;
      $scope.entities = entities;
      $scope.attach = function (selectedCP, selectedEntity) {
        var cpIds = [];
        for (var i = 0 ; i < selectedCP.length; ++i) {
          cpIds.push(selectedCP[i].id);
          console.warn(selectedCP[i].shortTitle, selectedCP[i].id);
        }
        FormsService.attachForms(formIds, cpIds, selectedEntity.value).then(function(result) {
          $scope.formCtxtIds = result;
          // TODO : Need to display it from the scope
          if (result.length > 0) {
            Utility.notify($("#notifications"), "Form Successfully Attached!", "success");
          } else {
            Utility.notify($("#notifications"), "Failed to attach the form!", "error");
          }
          //$scope.notify={ type: 'success', msg: 'Form Suucessfully Attached' };
          console.warn("FormContext Ids : " + result);
        });
        $modalInstance.close();
      };

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };
    };
    
    
    $scope.alert = function(message, type) {
      Utility.notify($("#notifications"), message, type);
    };
  }]);
