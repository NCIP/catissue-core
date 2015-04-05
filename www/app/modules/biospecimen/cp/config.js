
/**
 * Interim implementation. This should make way for backend supported
 * CP config
 */
angular.module('openspecimen')
  .factory('CpConfigSvc', function($http) {
    return {
      getRegParticipantTmpl: function(cpId, cprId) {
        if (cprId != -1) { //edit case
          return 'modules/biospecimen/participant/addedit.html';
        } else if (cpId == 18) {
          return 'custom-modules/biospecimen/participant/register.html';
        } else if (cpId == 17) {
          return 'custom-modules/le/biospecimen/collect-specimens.html';
        }

        return 'modules/biospecimen/participant/addedit.html';
      },

      getRegParticipantCtrl : function(cpId, cprId) {
        if (cprId != -1) { // edit case
          return 'ParticipantAddEditCtrl';
        } else if (cpId == 18) {
          return 'CustomRegParticipantCtrl';
        } else if (cpId == 17) {
          return 'le.RegAndCollectSpecimensCtrl'
        }

        return 'ParticipantAddEditCtrl';
      }
    }
  });
