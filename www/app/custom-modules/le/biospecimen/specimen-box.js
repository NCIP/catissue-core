angular.module('openspecimen')
  .directive('leSpecimenBox', function() {
    function drawBox(opts, element) {
      var dimension = opts.dimension;
      var specimens = opts.specimens;

      var height = (element.height() - 40)/ dimension.rows;
      var width = (element.width() - 40)/ dimension.columns;

      if (width < 120) {
        width = 120;
      }

      if (height < 100) {
        height = 100;
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
              colContent.append(angular.element('<div class="title"/>').append(specimen.empi));
              colContent.append(angular.element('<div class="label"/>').append(specimen.label));
              colContent.append(angular.element('<div class="desc"/>').append(specimen.type));
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
