
angular.module('os.biospecimen.participant.specimen-tree', 
  [
    'os.biospecimen.models', 
    'os.biospecimen.participant.collect-specimens',
  ])
  .directive('osSpecimenTree', function(
    $state, $stateParams, $modal, $timeout, $rootScope,
    CollectSpecimensSvc, Specimen, SpecimenLabelPrinter, SpecimensHolder,
    Alerts, Util, DeleteUtil, SpecimenUtil) {

    function openSpecimenTree(specimens) {
      angular.forEach(specimens, function(specimen) {
        specimen.isOpened = true;
        openSpecimenTree(specimen.children);
      });
    }

    function toggleAllSelected(selection, specimens, specimen) {
      if (!specimen.selected) {
        selection.all = false;
        return;
      }

      for (var i = 0; i < specimens.length; ++i) {
        if (!specimens[i].selected) {
          selection.all = false;
          return;
        }
      }

      selection.all = true;
    };

    function selectParentSpecimen(specimen) {
      if (!specimen.selected) {
        return false;
      }

      var parent = specimen.parent;
      while (parent) {
        parent.selected = true;
        parent = parent.parent;
      }
    };

    function isAnySelected(specimens) {
      for (var i = 0; i < specimens.length; ++i) {
        if (specimens[i].selected) {
          return true;
        }
      }

      return false;
    }

    function isAnyChildOrPoolSpecimenSelected(specimen) {
      if (!!specimen.specimensPool) {
        for (var i = 0; i < specimen.specimensPool.length; ++i) {
          if (specimen.specimensPool[i].selected) {
            return true;
          }
        }
      }

      if (!specimen.children) {
        return false;
      }

      for (var i = 0; i < specimen.children.length; ++i) {
        if (specimen.children[i].selected) {
          return true;
        }

        if (isAnyChildOrPoolSpecimenSelected(specimen.children[i])) {
          return true;
        }
      }

      return false;
    };

    function getState() {
      return {state: $state.current, params: $stateParams};
    };

    function showSelectSpecimens(msgCode) {
      Alerts.error(msgCode);
    };

    function getSelectedSpecimens (scope, message, anyStatus) {
      if (!scope.selection.any) {
        showSelectSpecimens(message);
        return [];
      }

      var specimens = [];
      angular.forEach(scope.specimens, function(specimen) {
        if (!specimen.selected) {
          return;
        }

        if ((specimen.status == 'Collected' || anyStatus) && specimen.id) {
          specimens.push(specimen);
        }
      });

      if (specimens.length == 0) {
        showSelectSpecimens(message);
      }

      return specimens;
    };

    function shouldHidePendingSpmns(collectionDate, pendingSpmnsDispInterval) {
      var hidePendingSpmns = false;

      if (!collectionDate || !pendingSpmnsDispInterval) {
        return hidePendingSpmns;
      }

      var dispCutOff = new Date(collectionDate);
      dispCutOff.setDate(dispCutOff.getDate() + pendingSpmnsDispInterval);
      return dispCutOff.getTime() < (new Date().getTime());
    }

    function onlyPendingSpmns(specimens) {
      return specimens.every(
        function(spmn) {
          return !spmn.status || spmn.status == 'Pending';
        }
      );
    }

    function anyPendingSpmnsInTree(specimens) {
      return specimens.some(
        function(spmn) {
          if (!spmn.status || spmn.status == 'Pending') {
            return true;
          }

          if (!!spmn.children && anyPendingSpmnsInTree(spmn.children)) {
            return true;
          }

          if (!!spmn.specimensPool && anyPendingSpmnsInTree(spmn.specimensPool)) {
            return true;
          }
        }
      );
    }

    return {
      restrict: 'E',

      scope: {
        cp: '=',
        cpr: '=',
        visit: '=',
        specimenTree: '=specimens',
        allowedOps: '=',
        reload: '&reload',
        collectionDate: '=?',
        pendingSpmnsDispInterval: '=?'
      },

      templateUrl: 'modules/biospecimen/participant/specimens.html',

      link: function(scope, element, attrs) {
        scope.view = 'list';
        scope.parentSpecimen = undefined;

        scope.hidePendingSpmns = shouldHidePendingSpmns(scope.collectionDate, scope.pendingSpmnsDispInterval);
        scope.onlyPendingSpmns = onlyPendingSpmns(scope.specimenTree);
        scope.anyPendingSpmns  = anyPendingSpmnsInTree(scope.specimenTree);

        scope.specimens = Specimen.flatten(scope.specimenTree);
        openSpecimenTree(scope.specimens);

        scope.openSpecimenNode = function(specimen) {
          specimen.isOpened = true;
        };

        scope.closeSpecimenNode = function(specimen) {
          specimen.isOpened = false;
        };

        scope.selection = {all: false, any: false};
        scope.toggleAllSpecimenSelect = function() {
          angular.forEach(scope.specimens, function(specimen) {
            specimen.selected = scope.selection.all;
          });

          scope.selection.any = scope.selection.all;
        };

        scope.toggleSpecimenSelect = function(specimen) {
          if (specimen.status != 'Collected') {
            selectParentSpecimen(specimen);
          }

          toggleAllSelected(scope.selection, scope.specimens, specimen);

          scope.selection.any = specimen.selected ? true : isAnySelected(scope.specimens);
        };

        scope.collectSpecimens = function() {
          if (!scope.selection.any) {
            if (!scope.visit || !scope.visit.id) {
              Alerts.error('specimens.errors.visit_not_completed');
            } else {
              $state.go('specimen-addedit', {specimenId: '', visitId: scope.visit.id});
            }

            return;
          }

          var specimensToCollect = [];
          angular.forEach(scope.specimens, function(specimen) {
            if (specimen.selected) {
              specimen.isOpened = true;
              specimensToCollect.push(specimen);
            } else if (isAnyChildOrPoolSpecimenSelected(specimen)) {
              if (specimen.status != 'Collected') {
                // a parent needs to be collected first
                specimen.selected = true;
              }
              specimen.isOpened = true;
              specimensToCollect.push(specimen);
            }
          });

          var onlyCollected = true;
          for (var i = 0; i < specimensToCollect.length; ++i) {
            if (specimensToCollect[i].status != 'Collected') {
              onlyCollected = false;
              break;
            }
          }

          if (onlyCollected) {
            showSelectSpecimens('specimens.no_specimens_for_collection');
            return;
          }

          CollectSpecimensSvc.collect(getState(), scope.visit, specimensToCollect);
        };

        scope.printSpecimenLabels = function() {
          var specimensToPrint = getSelectedSpecimens(scope, 'specimens.no_specimens_for_print', false);
          if (specimensToPrint == undefined || specimensToPrint.length == 0) {
            return;
          }

          var specimenIds = getSpecimenIds(specimensToPrint);
          SpecimenLabelPrinter.printLabels({specimenIds: specimenIds});
        };

        scope.deleteSpecimens = function() {
          var specimensToDelete = getSelectedSpecimens(scope, 'specimens.no_specimens_for_delete', true);
          if (specimensToDelete.length == 0) {
            return;
          }

          var specimenIds = getSpecimenIdsForDeletion(specimensToDelete); 
          DeleteUtil.bulkDelete({bulkDelete: Specimen.bulkDelete}, specimenIds, getBulkDeleteOpts(specimensToDelete));
          scope.selection.all = false;
        }

        scope.closeSpecimens = function() {
          var specimensToClose = getSelectedSpecimens(scope, 'specimens.no_specimens_for_close', false);
          if (specimensToClose.length == 0) {
            return;
          }

          var modalInstance = $modal.open({
            templateUrl: 'modules/biospecimen/participant/specimen/close.html',
            controller: 'SpecimenCloseCtrl',
            resolve: {
              specimens: function() {
                return specimensToClose;
              }
            }
          });
          scope.selection.all = false;
        };

        scope.addSpecimensToSpecimenList = function(list) {
          if (!scope.selection.any) {
            showSelectSpecimens('specimens.no_specimens_for_specimen_list');
            return;
          }
          var selectedSpecimens = [];
          getSelectedSpecimens(scope, 'specimens.no_specimens_for_specimen_list', true).map(
            function(specimen) {
              selectedSpecimens.push({id: specimen.id});
            }
          );

          if (selectedSpecimens.length == 0) {
            return;
          }

          if (!!list) {
            list.addSpecimens(selectedSpecimens).then(
              function(specimens) {
                var listType = list.getListType($rootScope.currentUser);
                Alerts.success('specimen_list.specimens_added_to_' + listType , list);
              }
            )
          } else {
            SpecimensHolder.setSpecimens(selectedSpecimens);
            $state.go('specimen-list-addedit', {listId: ''});
          }
        }

        scope.loadSpecimenTypes = function(specimenClass, notClear) {
          SpecimenUtil.loadSpecimenTypes(scope, specimenClass, notClear);
        };

        scope.showCloseSpecimen = function(specimen) {
          scope.view = 'close_specimen';
          scope.specStatus = { reason: '' };
          scope.parentSpecimen = specimen;
        };

        scope.closeSpecimen = function() {
          scope.parentSpecimen.close(scope.specStatus.reason).then(
            function() {
              scope.revertEdit();
            }
          );
        };
         
        scope.revertEdit = function() {
          scope.view = 'list';
          scope.parentSpecimen = undefined;
        }

        scope.toggleHidePendingSpmns = function() {
          scope.hidePendingSpmns = !scope.hidePendingSpmns;
        }

        function getSpecimenIds(specimens) {
          return specimens.map(
            function(s) {
              return s.id;
            }
          );
        }

        function getSpecimenIdsForDeletion(specimens) {
          var specimenIds = [];
          var idx = 0;

          while (idx < specimens.length) {
            specimenIds.push(specimens[idx].id);

            //
            // +1 for self
            // + descendent count to exclude all of specimen descendants…
            // 
            idx += 1 + getSelectedDescendantCount(specimens[idx]);
          }

          return specimenIds;
        }

        function getSelectedDescendantCount(specimen) {
          var count = 0;
          angular.forEach(specimen.children, function(child) {
            if (child.selected) {
              count++;
            }
        
            count += getSelectedDescendantCount(child);
          });
        
          angular.forEach(specimen.specimensPool, function(poolSpmn) {
            if (poolSpmn.selected) {
              count++;
            }
          });
        
          return count;
        }

        function getBulkDeleteOpts(specimensToDelete) {
          var hasChildren = childrenExists(specimensToDelete);
          return {
            confirmDelete : hasChildren ? 'specimens.delete_specimens_heirarchy' :'specimens.delete_specimens',
            successMessage: hasChildren ? 'specimens.specimens_hierarchy_deleted' : 'specimens.specimens_deleted',
            onBulkDeletion: function(result) {
              if (typeof scope.reload == "function") {
                scope.reload().then(
                  function() {
                    $timeout(function() {
                      scope.specimens = Specimen.flatten(scope.specimenTree);
                      openSpecimenTree(scope.specimens);
                    });
                  }
                );
              }
              scope.selection.any = false;
            }
          }
        }

        function childrenExists(specimens) {
          for (var i = 0; i < specimens.length; ++i) {
            if (specimens[i].children.length > 0) {
              return true;
            }
          }
          return false;
        }
      }
    }
  });
