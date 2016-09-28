
angular.module('openspecimen')
  .factory('CpConfigSvc', function(CollectionProtocol, $q) {
    var cpWorkflowsMap = {};

    var listCfgsMap = {};

    var summarySt = undefined;
    
    function getRegParticipantTmpl(cpId, cprId) {
      return getTmpl(cpId, cprId, 'registerParticipant', 'modules/biospecimen/participant/addedit.html');
    }

    function getRegParticipantCtrl(cpId, cprId) {
      return getCtrl(cpId, cprId, 'registerParticipant','ParticipantAddEditCtrl');
    }

    function getBulkRegParticipantTmpl(cpId, cprId) {
      return getTmpl(cpId, cprId, 'registerBulkParticipant');
    }

    function getBulkRegParticipantCtrl(cpId, cprId) {
      return getCtrl(cpId, cprId, 'registerBulkParticipant');
    }

    function getTmpl(cpId, cprId, name, defaultTmpl){
      var cfg = cpWorkflowsMap[cpId];
      var workflow = cfg.workflows[name];
      if (workflow) {
        return workflow.view;
      }
      return defaultTmpl;      
    }

    function getCtrl(cpId, cprId, name, defaultCtrl){
      var cfg = cpWorkflowsMap[cpId];
      var workflow = cfg.workflows[name];
      if (workflow) {
        return workflow.ctrl;
      }
      return defaultCtrl;      
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

    function getWorkflowData(cpId, name) {
      return loadWorkflows(cpId).then(
        function(cfg) {
          var workflow = cfg.workflows[name];
          return workflow ? (workflow.data || {}) : {};
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

      getWorkflowData: getWorkflowData,

      getBulkRegParticipantTmpl: function(cpId, cprId) {
        return loadWorkflows(cpId).then(
          function() {
            return getBulkRegParticipantTmpl(cpId, cprId);
          }
        );
      },

      getBulkRegParticipantCtrl: function(cpId, cprId) {
        // we do not call loadWorkflows, as it would have been loaded by above 
        // template provider
        return getBulkRegParticipantCtrl(cpId, cprId);
      },

      getDictionary: function(cpId, defValue) {
        return getWorkflowData(cpId, 'dictionary').then(
          function(data) {
            return data.fields || defValue || [];
          }
        );
      },

      setSummaryState: function(summaryState) {
        summarySt = summaryState;
      },

      getSummaryState: function() {
        return summarySt;
      },

      getListView: function(cpId, defValue) {
        return getWorkflowData(cpId, 'common').then(
          function(data) {
            return data.listView || defValue;
          }
        );
      },

      getListConfig: function(cp, listName) {
        var key = 'cp-' + cp.id + '-' + listName;
        if (!listCfgsMap[key]) {
          listCfgsMap[key] = cp.getListConfig(listName);
        }

        return listCfgsMap[key].then(
          function(cfg) {
            return cfg;
          }
        );
      },

      getLockedParticipantFields: function() {
        return getWorkflowData(-1, 'locked-fields').then(
          function(data) {
            if (!data) {
              return [];
            }

            return data.participant || [];
          }
        );
      }
    }
  });
