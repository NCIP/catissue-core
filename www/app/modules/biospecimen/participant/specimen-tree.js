
angular.module('os.biospecimen.participant.specimen-tree', 
  [
    'os.biospecimen.models', 
    'os.biospecimen.participant.collect-specimens'
  ])
  .directive('osSpecimenTree', function(
    $state, $stateParams, 
    CollectSpecimensSvc, Specimen, SpecimenLabelPrinter,
    Alerts, PvManager, Util) {

    function loadSpecimenClasses(scope) {
      if (scope.classesLoaded) {
        return;
      }

      scope.specimenClasses = PvManager.getPvs('specimen-class');
      scope.classesLoaded = true;
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

    function selectChildrenSpecimens(specimen) {
      if (!specimen.selected) {
        return;
      }

      angular.forEach(specimen.children, function(child) {
        child.selected = true;
        selectChildrenSpecimens(child);
      });
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

    return {
      restrict: 'E',

      scope: {
        cpr: '=',
        visit: '=',
        specimenTree: '=specimens'
      },

      templateUrl: 'modules/biospecimen/participant/specimens.html',

      link: function(scope, element, attrs) {
        scope.specimens = Specimen.flatten(scope.specimenTree);
        scope.view = 'list';
        scope.parentSpecimen = undefined;

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
          selectChildrenSpecimens(specimen);
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

        scope.showCreateAliquots = function(specimen) {
          scope.view = 'create_aliquots';      
          scope.parentSpecimen = specimen;
          scope.aliquotSpec = {};
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

          parent.isOpened = parent.hasChildren = true;
          parent.hasChildren = true;
          parent.depth = 0;

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

          parent.children = aliquots;
          var specimens = angular.copy(aliquots);
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
            pathology: scope.parentSpecimen.pathology
          });

          loadSpecimenClasses(scope);
        };

        scope.createDerivative = function() {
          scope.derivative.$saveOrUpdate().then(
            function(result) {
              scope.parentSpecimen.children.push(result);
              scope.parentSpecimen.isOpened = true;
              scope.specimens = Specimen.flatten(scope.specimenTree);
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
