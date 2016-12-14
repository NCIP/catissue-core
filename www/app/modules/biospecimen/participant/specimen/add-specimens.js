
angular.module('os.biospecimen.specimen')
  .directive('osAddSpecimens', function($q, CollectionProtocol, SpecimenUtil) {
    return {
      restrict: 'E',

      scope: {
        onAdd: '&',
        filterOpts: '=?',
        errorOpts: '=?'
      },

      controller: function($scope) {
        $scope.input = {useBarcode: false};

        $scope.addSpecimen = function(inputs) {
          var filterOpts = $scope.filterOpts || {};
          var errorOpts = $scope.errorOpts || {};
          var labels = undefined;
          if (!!$scope.input.useBarcode) {
            filterOpts.barcode = inputs;
          } else {
            labels = inputs;
          }

          return SpecimenUtil.getSpecimens(labels, filterOpts, errorOpts).then(
            function (specimens) {
              if (!specimens) {
                return false;
              }

              return $q.when($scope.onAdd({specimens: specimens})).then(
                function(success) {
                  return success;
                }
              );
            }
          );
        }
      },

      link: function(scope, element, attrs) {
        scope.barcodingEnabled = false;
        CollectionProtocol.getBarcodingEnabled().then(
          function(barcodingEnabled) {
            scope.barcodingEnabled = barcodingEnabled;
          }
        );
      },

      template: function(tElem, tAttrs) {
        return  '<div>' +
                '  <div class="os-text-checkbox" ng-if="barcodingEnabled">' +
                '    <div class="checkbox">' +
                '      <os-checkbox ng-model="input.useBarcode"></os-checkbox>' +
                '    </div>' +
                '    <div class="message os-ctrl-padding-top">' +
                '      <span translate="specimens.use_barcode">Use Barcode</span>' +
                '    </div>' +
                '  </div>' +
                '  <os-add-items on-add="addSpecimen(itemLabels)"' +
                '    placeholder="' + tAttrs.placeholder + '">' +
                '  </os-add-items>' +
                '</div>';
      }
    }
  });
