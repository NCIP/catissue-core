
angular.module('os.biospecimen.specimenlist.specimensholder', [])
  .factory('SpecimensHolder', function() {
     this.specimens = undefined;

     return {
       getSpecimens: function() {
         return this.specimens;
       },

       setSpecimens: function(specimens) {
         this.specimens = specimens;
       }
     }     
  });
