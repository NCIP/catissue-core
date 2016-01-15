
angular.module('os.biospecimen.specimensrequest.holder', ['os.biospecimen.models'])
  .factory('SpecimenRequestHolder', function() {
    this.request = undefined;

    return {
      set: function(request) {
        this.request = request;
      },

      get: function(request) {
        return this.request;
      },

      clear: function() {
        this.request = undefined;
      }
    };
  });
