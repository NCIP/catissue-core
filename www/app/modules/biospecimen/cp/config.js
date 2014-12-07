
angular.module('openspecimen')
  .factory('CpConfigSvc', function($http) {
    return {
      getRegParticipantTmpl: function(cpId) {
        if (cpId == 9933) {
          return 'custom-modules/biospecimen/participant/register.html';
        }

        return 'modules/common/blank.html';
      },

      getRegParticipantCtrl : function(cpId) {
        if (cpId == 9933) {
          return 'CustomRegParticipantCtrl';
        }

        return 'RegParticipantCtrl'
      }
    }
  });
