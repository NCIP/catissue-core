angular.module('plus.cpviewService', [])
  .factory('repository', function($http) {
    return { 
     
     getAllCps: function() {
        var url = 'rest/ng/collection-protocols?chkPrivilege=true';
        return Utility.get($http, url);
      },
	 
	 getChildCpRegistrations: function(participantId,cpId) {
        var url = 'rest/ng/participants/'+participantId+'/SubCpRegistrations?cpId='+cpId;
        return Utility.get($http, url);
      },
	  
	  getChildCps: function(cpId) {
        var url = 'rest/ng/collection-protocols/'+cpId+'/childProtocols';
        return Utility.get($http, url);
      },
    
      getCollectionGroups: function(cprId){
        var url = 'rest/ng/collection-protocol-registrations/'+cprId+'/specimen-collection-groups';
        return Utility.get($http, url);
      },
    
      getRegisteredParticipants: function(cpId,query) {//alert('d');
        var url = 'rest/ng/collection-protocols/'+cpId+'/participants';
        var params = {
          query : query,
          '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, url, undefined, params);
      },

       getParticipantById: function(cpId, participantId) {
         var url = 'rest/ng/collection-protocols/'+cpId+'/participant';
         var params = {
           'pId' : participantId,
           '_reqTime' : new Date().getTime()
         }
         return Utility.get($http, url, undefined, params);
      },

      getSpecimens: function(scgId,objectType) {
        var url = 'rest/ng/specimen-collection-groups/'+scgId+'/specimens';
        var params = {
          objectType : objectType,
          '_reqTime' : new Date().getTime()
        }
        return Utility.get($http, url, undefined, params);
      },
    
      getChildSpecimens: function(parentId) {
        var url = 'rest/ng/specimens/'+parentId+'/child-specimens';
        return Utility.get($http, url);
      },
      
      getScgId: function(specimenId) {
        var url = 'rest/ng/specimens/'+specimenId+'/scgId';
        return Utility.get($http, url);
      }
    };
 });
 
