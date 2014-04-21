angular.module('plus.cpviewService', [])
  .factory('repository', function($http) {
    return { 
     
     getAllCps: function() {
        var url = 'rest/ng/collection-protocols';
        return $http.get(url);
      },
    
      getCollectionGroups: function(cprId){
        var url = 'rest/ng/collection-protocol-registrations/'+cprId+'/specimen-collection-groups';
        return $http.get(url);
      },
    
      getRegisteredParticipants: function(cpId,query) {//alert('d');
        var url = 'rest/ng/collection-protocols/'+cpId+'/participants?query='+query;
        return $http.get(url);
      },
    
      getSpecimens: function(scgId) {
        var url = 'rest/ng/specimen-collection-groups/'+scgId+'/specimens';
        return $http.get(url);
      },
    
      getChildSpecimens: function(parentId) {
        var url = 'rest/ng/specimens/'+parentId+'/child-specimens';
        return $http.get(url);
      },
      
      getScgId: function(specimenId) {
        var url = 'rest/ng/specimens/'+specimenId+'/scgId';
        return $http.get(url);
        }
    };
 });
 
