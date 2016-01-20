angular.module('os.biospecimen.common.eventdesc', [])
  .directive('osEventDesc', function() {
    return {
      restrict: 'E',
 
      replace: 'true',

      scope: {
        eventPoint: '=',
        eventLabel: '='
      },

      template:
        '<span>' +
          '<span ng-switch on="eventPoint != null">' +
            '<span ng-switch-when="true">' +
              '<span ng-if="eventPoint >= 0">' +
                'T{{eventPoint}}: ' +
              '</span>' +
              '<span ng-if="eventPoint < 0">' +
                '-T{{-eventPoint}}: ' +
              '</span>' +
            '</span>' +
            '<span ng-switch-default>' +
              'T<span translate="visits.eos">EOS</span>: ' +
            '</span>' +
          '</span>' +
          '<span>' +
            '{{eventLabel}}' +
          '</span>' +
        '</span>'
    };
  });
