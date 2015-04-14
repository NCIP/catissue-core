
/**
 * Interim implementation. This should make way for backend supported
 * CP config
 */
angular.module('openspecimen')
  .factory('CpConfigSvc', function(CollectionProtocol, $q) {
    var cpWorkflowsMap = {};

    function getRegParticipantTmpl(cpId, cprId) {
      var cfg = cpWorkflowsMap[cpId];
      var workflow = cfg.workflows['registerParticipant'];
      if (workflow) {
        return workflow.view;
      }

      return 'modules/biospecimen/participant/addedit.html';
    }

    function getRegParticipantCtrl(cpId, cprId) {
      var cfg = cpWorkflowsMap[cpId];
      var workflow = cfg.workflows['registerParticipant'];
      if (workflow) {
        return workflow.ctrl;
      }

      return 'ParticipantAddEditCtrl';
    }

    function loadWorkflows(cpId) {
      var d = $q.defer();
      if (cpWorkflowsMap[cpId]) {
        d.resolve(cpWorkflowsMap[cpId]);
        return d.promise;
      }

      return CollectionProtocol.getWorkflows(cpId).then(
        function(cpWorkflows) {
          cpWorkflowsMap[cpId] = cpWorkflows;
          return cpWorkflows;
        }
      );
    }

    return {
      getRegParticipantTmpl: function(cpId, cprId) {
        if (cprId != -1) { //edit case
          return 'modules/biospecimen/participant/addedit.html';
        }

        return loadWorkflows(cpId).then(
          function() {
            return getRegParticipantTmpl(cpId, cprId);
          }
        );
      },

      getRegParticipantCtrl : function(cpId, cprId) {
        if (cprId != -1) { // edit case
          return 'ParticipantAddEditCtrl';
        } 

        // we do not call loadWorkflows, as it would have been loaded by above 
        // template provider
        return getRegParticipantCtrl(cpId, cprId);
      },

      getWorkflowData: function(cpId, name) {
        return loadWorkflows(cpId).then(
          function(cfg) {
            var workflow = cfg.workflows[name];
            return workflow ? (workflow.data || {}) : {};
          }
        );
      }
    }
  });
