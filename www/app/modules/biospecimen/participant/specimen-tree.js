
angular.module('os.biospecimen.participant.specimen-tree', 
  [
    'os.biospecimen.models', 
    'os.biospecimen.participant.collect-specimens'
  ])
  .directive('osSpecimenTree', function(CollectSpecimensSvc, Specimen, Alerts) {
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

    return {
      restrict: 'E',

      scope: {
        visit: '=',
        specimens: '='
      },

      templateUrl: 'modules/biospecimen/participant/specimens.html',

      link: function(scope, element, attrs) {
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

          CollectSpecimensSvc.collect(scope.visit, specimensToCollect);
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

          var aliquot = angular.extend(new Specimen(parent), {
            id: undefined, 
            reqId: undefined, 
            label: '',
            lineage: 'Aliquot', 
            parentId: parent.id,
            initialQty: spec.qtyPerAliquot,
            storageLocation: {name: '', positionX:'', positionY: ''},
            status: 'Pending', 
            selected: true
          });

          var aliquots = [];
          for (var i = 0; i < spec.noOfAliquots; ++i) {
            aliquots.push(angular.copy(aliquot));
          }

          CollectSpecimensSvc.collect(scope.visit, aliquots);
        };

        scope.showCreateDerivatives = function(specimen) {
          scope.view = 'create_derivatives';      
          scope.parentSpecimen = specimen;
        };

        scope.revertEdit = function() {
          scope.view = 'list';
          scope.parentSpecimen = undefined;
        }
      }
    }
  });
