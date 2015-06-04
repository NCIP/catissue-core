angular.module('openspecimen')
  .directive('leSpecimenBox', function(SpecimenQtyUnitSvc) {
    function drawBox(opts, element) {
      var dimension = opts.dimension;
      var specimens = opts.specimens;

      var height = (element.height() - 40)/ dimension.rows;
      var width = (element.width() - 40)/ dimension.columns;

      if (width < 175) {
        width = 175;
      }

      if (height < 125) {
        height = 125;
      }

      var box = angular.element('<table/>').addClass('os-le-box');
      var boxContent = angular.element('<tbody/>');
      box.append(boxContent);

      var idx = 0, lastEmpi = undefined, labelPresent = false;
      for (var i = 0; i < dimension.rows; i++) {
        var row = angular.element('<tr class="os-le-box-row"/>');
        for (var j = 0; j < dimension.columns; ++j) {
          var col = angular.element('<td class="os-le-box-col"/>')
            .css('height', height)
            .css('width', width);

          var colContent = angular.element('<div class="content"/>');

          idx = getNonVirtualSpecimenIdx(idx, specimens);
          if (idx != -1) {
            var specimen = specimens[idx];
            if (opts.compact || specimen.empi == lastEmpi || j == 0) {
              colContent.append(angular.element('<div class="title"/>').append(specimen.primarySpmnLabel));
              colContent.append(angular.element('<div class="label"/>').append(specimen.label));
              colContent.append(
                angular.element('<div class="desc"/>')
                  .append(angular.element('<div/>').append(specimen.type))
                  .append(angular.element('<span/>').append(specimen.initialQty))
                  .append(getUnit(specimen)).append(angular.element('<span/>').append('&nbsp;'))
                  .append(angular.element('<span/>').append(specimen.collectionContainer))
              );

              lastEmpi = specimen.empi;
              idx++;

              if (specimen.label && specimen.label.trim().length > 0) {
                labelPresent = true;
                col.addClass('collected');
              } else {
                col.addClass('not-collected');
              }
            }
          }

          col.append(colContent);
          row.append(col);
        }

        boxContent.append(row);
      }
       
      if (labelPresent) {
        boxContent.addClass('show-not-collected');
      }

      element.append(box);
    }

    function getNonVirtualSpecimenIdx(startIdx, specimens) {
      if (startIdx == -1) {
        return -1;
      }

      while (startIdx < specimens.length) {
        if (specimens[startIdx].storageType != 'Virtual') {
          return startIdx;
        }
        
        startIdx++;
      }

      return -1;
    }

    function getUnit(specimen) {
      var unitEl = angular.element('<span/>');
      SpecimenQtyUnitSvc.getUnit(specimen.specimenClass, specimen.type).then(
        function(unit) {
          unitEl.html(unit.htmlDisplayCode || unit.unit)
        }
      );

      return unitEl;
    }

    return {
      restrict: 'E',

      replace: 'true',

      template: '<div></div>',

      scope: {
        opts: '=',
        ctrl: '='       
      },

      controller: function($scope, $element) {
        this.draw = function() {
          $element.children().remove();
          drawBox($scope.opts, $element);
        }
      },

      link: function(scope, element, attrs, ctrl) {
        scope.ctrl = ctrl;

        scope.$watch('opts', function(val) {
          ctrl.draw();
        });
      }
    }
  });
