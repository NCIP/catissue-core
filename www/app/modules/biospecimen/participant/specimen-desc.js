
angular.module('os.biospecimen.participant.specimen-desc', [])
  .directive('osSpecimenDesc', function($translate) {
    return {
      restrict: 'E',
 
      replace: 'true',

      scope: {
        specimen: '='
      },

      link: function(scope, element, attrs) {
        scope.notSpecified = $translate.instant('pvs.not_specified');
      },

      template: 
        '<span class="os-specimen-desc">' +
          '<span ng-if="specimen.lineage == \'New\'">' +
            '<span ng-if="!!specimen.pathology && specimen.pathology != notSpecified">' +
              '{{specimen.pathology}} ' +
            '</span>' +
            '<span>{{specimen.type}} </span>' +
            '<span ng-if="specimen.specimenClass == \'Tissue\' && ' +
              '!!specimen.anatomicSite && specimen.anatomicSite != notSpecified">' +
              '<span translate="specimens.from">from</span> {{specimen.anatomicSite}}' +
            '</span>' +
            '<span ng-if="specimen.specimenClass == \'Fluid\' && ' +
              '!!specimen.collectionContainer && specimen.collectionContainer != notSpecified">' +
              '<span translate="specimens.collected_in">collected in</span> {{specimen.collectionContainer}}' +
            '</span>' +
          '</span>' +
          '<span ng-if="specimen.lineage == \'Aliquot\'">' +
            '<span translate="specimens.aliquot">Aliquot</span>' +
          '</span>' +
          '<span ng-if="specimen.lineage == \'Derived\'">' +
            '<span translate="specimens.derived">Derived</span> {{specimen.type}}' +
          '</span>' +
        '</span>'
    };
  });
