
angular.module('openspecimen')
  .factory('CpConfigSvc', function($http) {
    return {
      getRegParticipantTmpl: function(cpId) {
        if (cpId == 18) {
          return 'custom-modules/biospecimen/participant/register.html';
        }

        return 'modules/biospecimen/participant/addedit.html';
      },

      getRegParticipantCtrl : function(cpId) {
        if (cpId == 18) {
          return 'CustomRegParticipantCtrl';
        }

        return 'ParticipantAddEditCtrl';
      }
    }
  });
