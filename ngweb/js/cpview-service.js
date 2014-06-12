angular.module('plus.cpviewService', [])
  .factory('repository', function($http) {
    return { 
     
     getAllCps: function() {
        var url = 'rest/ng/collection-protocols';
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      },
    
      getCollectionGroups: function(cprId){
        var url = 'rest/ng/collection-protocol-registrations/'+cprId+'/specimen-collection-groups';
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      },
    
      getRegisteredParticipants: function(cpId,query) {//alert('d');
        var url = 'rest/ng/collection-protocols/'+cpId+'/participants';
        var params = {
          query : query,
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      },
    
      getSpecimens: function(scgId,objectType) {
        var url = 'rest/ng/specimen-collection-groups/'+scgId+'/specimens';
        var params = {
          objectType : objectType,
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      },
    
      getChildSpecimens: function(parentId) {
        var url = 'rest/ng/specimens/'+parentId+'/child-specimens';
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      },
      
      getScgId: function(specimenId) {
        var url = 'rest/ng/specimens/'+specimenId+'/scgId';
        var params = {
          '_reqTime' : new Date().getTime()
        }
        return $http({method: 'GET', url: url, params: params});
      }
    };
 });
 
