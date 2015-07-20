
angular.module('os.biospecimen.participant.specimen-tree', 
  [
    'os.biospecimen.models', 
    'os.biospecimen.participant.collect-specimens'
  ])
  .directive('osSpecimenTree', function(
    $state, $stateParams, 
    CollectSpecimensSvc, Specimen, SpecimenLabelPrinter, SpecimenList, SpecimensHolder,
    Alerts, PvManager, Util) {

    function openSpecimenTree(specimens) {
      angular.forEach(specimens, function(specimen) {
        specimen.isOpened = true;
        openSpecimenTree(specimen.children);
      });
    }

    function loadSpecimenClasses(scope) {
      if (scope.classesLoaded) {
        return;
      }

      scope.specimenClasses = PvManager.getPvs('specimen-class');
      scope.classesLoaded = true;
    }

    function loadAllSpecimenList(scope) {
      SpecimenList.query().then(
        function(lists) {
          if (scope.$parent.currentUser.admin) {
            scope.specimenLists = lists;
          } else {
            scope.specimenLists = [];
            angular.forEach(lists, function(list) {
              if (list.owner.id == scope.$parent.currentUser.id) {
                scope.specimenLists.push(list);
              }
            })
          }
        }
      );
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

    function isAnyChildSpecimenSelected(specimen) {
      if (!specimen.children) {
        return false;
      }

      for (var i = 0; i < specimen.children.length; ++i) {
        if (specimen.children[i].selected) {
          return true;
        }

        if (isAnyChildSpecimenSelected(specimen.children[i])) {
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

    function getSelectedSpecimens(scope) {
      var selectedSpecimens = [];
      angular.forEach(scope.specimens, function(specimen) {
        if (specimen.selected && specimen.id != null) {
          selectedSpecimens.push({label: specimen.label});
        }
      });

      return selectedSpecimens;
    };

    return {
      restrict: 'E',

      scope: {
        cpr: '=',
        visit: '=',
        specimenTree: '=specimens',
        allowedOps: '='
      },

      templateUrl: 'modules/biospecimen/participant/specimens.html',

      link: function(scope, element, attrs) {
        scope.view = 'list';
        scope.parentSpecimen = undefined;
        loadAllSpecimenList(scope);

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
          selectParentSpecimen(specimen);  
          toggleAllSelected(scope.selection, scope.specimens, specimen);

          scope.selection.any = specimen.selected ? true : isAnySelected(scope.specimens);
        };

        scope.collectSpecimens = function() {
          if (!scope.selection.any) {
            showSelectSpecimens('specimens.no_specimens_for_collection');
            return;
          }

          var specimensToCollect = [];
          angular.forEach(scope.specimens, function(specimen) {
            if (specimen.selected) {
              specimen.isOpened = true;
              specimensToCollect.push(specimen);
            } else if (isAnyChildSpecimenSelected(specimen)) {
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
          if (!scope.selection.any) {
            showSelectSpecimens('specimens.no_specimens_for_print');
            return;
          }

          var specimensToPrint = [];
          var anyUncollected = false;
          angular.forEach(scope.specimens, function(specimen) {
            if (!specimen.selected) {
              return;
            }

            if (specimen.status == 'Collected') {
              specimensToPrint.push(specimen.id);
            } else {
              anyUncollected = true;
            }
          });

          if (specimensToPrint.length == 0) {
            showSelectSpecimens('specimens.no_specimens_for_print');
            return;
          }
             
          SpecimenLabelPrinter.printLabels({specimenIds: specimensToPrint});
        };

        scope.addSpecimensToSpecimenList = function(list) {
          if (!scope.selection.any) {
            showSelectSpecimens('specimens.no_specimens_for_specimen_list');
            return;
          }
 
          var selectedSpecimens = getSelectedSpecimens(scope);
          if (!!list) {
            list.addSpecimens(selectedSpecimens).then(function(specimens) {
              Alerts.success('specimen_list.specimens_added', {name: list.name});
            })
          } else {
            SpecimensHolder.setSpecimens(selectedSpecimens);
            $state.go('specimen-list-addedit', {listId: ''});
          }
        }

        scope.showCreateAliquots = function(specimen) {
          scope.view = 'create_aliquots';      
          scope.parentSpecimen = specimen;
          scope.aliquotSpec = {createdOn : new Date()};
        };

        scope.collectAliquots = function() {
          var spec = scope.aliquotSpec;
          var parent = scope.parentSpecimen;

          if (!!spec.qtyPerAliquot && !!spec.noOfAliquots) {
            var requiredQty = spec.qtyPerAliquot * spec.noOfAliquots;
            if (requiredQty > parent.availableQty) {
              Alerts.error("specimens.errors.insufficient_qty");
              return;
            }
          } else if (!!spec.qtyPerAliquot) {
            spec.noOfAliquots = Math.floor(parent.availableQty / spec.qtyPerAliquot);
          } else if (!!spec.noOfAliquots) {
            spec.qtyPerAliquot = Math.round(parent.availableQty / spec.noOfAliquots * 10000) / 10000;
          }

          if (scope.aliquotSpec.createdOn.getTime() < scope.parentSpecimen.createdOn) {
            Alerts.error("specimens.errors.created_on_lt_parent");
            return;
          } else if (scope.aliquotSpec.createdOn > new Date()) {
            Alerts.error("specimens.errors.created_on_gt_curr_time");
            return;
          }

          parent.isOpened = parent.hasChildren = true;
          parent.hasChildren = true;
          parent.depth = 0;
          parent.closeAfterChildrenCreation = spec.closeParent;

          var aliquot = new Specimen({
            lineage: 'Aliquot',
            specimenClass: parent.specimenClass,
            type: parent.type,
            parentId: parent.id,
            initialQty: spec.qtyPerAliquot,
            storageLocation: {name: '', positionX:'', positionY: ''},
            status: 'Pending', 
            children: [],
            cprId: scope.cpr.id,
            visitId: parent.visitId,
            createdOn: spec.createdOn,

            selected: true,
            parent: parent,
            depth: 1,
            isOpened: true,
            hasChildren: false,
            labelFmt: scope.cpr.aliquotLabelFmt
          });

          var aliquots = [];
          for (var i = 0; i < spec.noOfAliquots; ++i) {
            aliquots.push(angular.copy(aliquot));
          }

          parent.children = [].concat(aliquots);
          var specimens = aliquots;
          specimens.unshift(parent);
          CollectSpecimensSvc.collect(getState(), scope.visit, specimens, parent);
        };

        scope.loadSpecimenTypes = function(specimenClass, notclear) {
          if (!notclear) {
            scope.derivative.type = '';
          }

          if (!specimenClass) {
            scope.specimenTypes = [];
            return;
          }

          if (!scope.specimenClasses[specimenClass]) {
            scope.specimenClasses[specimenClass] = 
              PvManager.getPvsByParent('specimen-class', specimenClass);
          }

          scope.specimenTypes = scope.specimenClasses[specimenClass];
        };

        scope.showCreateDerivative = function(specimen) {
          scope.view = 'create_derivatives';      
          scope.parentSpecimen = specimen;
          scope.derivative = new Specimen({
            parentId: scope.parentSpecimen.id, 
            lineage: 'Derived',
            storageLocation: {},
            status: 'Collected',
            visitId: scope.visit.id,
            pathology: scope.parentSpecimen.pathology,
            closeParent: false,
            createdOn : new Date()
          });

          loadSpecimenClasses(scope);
        };

        scope.createDerivative = function() {
          var closeParent = scope.derivative.closeParent;
          delete scope.derivative.closeParent;

          if (scope.derivative.createdOn.getTime() < scope.parentSpecimen.createdOn) {
            Alerts.error("specimens.errors.created_on_lt_parent");
            return;
          } else if (scope.derivative.createdOn > new Date()) {
            Alerts.error("specimens.errors.created_on_gt_curr_time");
            return;
          }

          var specimensToSave = undefined;
          if (closeParent) {
            specimensToSave = [new Specimen({
              id: scope.parentSpecimen.id,
              visitId: scope.visit.id,
              closeAfterChildrenCreation: true,
              children: [scope.derivative]
            })];
          } else {
            specimensToSave = [scope.derivative];
          }
 
          Specimen.save(specimensToSave).then(
            function(result) {
              if (closeParent) {
                scope.parentSpecimen.activityStatus = result[0].activityStatus;
                scope.parentSpecimen.children = result[0].children;
              } else {
                scope.parentSpecimen.children.push(result[0]);
              }
              scope.parentSpecimen.isOpened = true;
              scope.specimens = Specimen.flatten(scope.specimenTree);
              scope.revertEdit();
            }
          );
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
      }
    }
  });
