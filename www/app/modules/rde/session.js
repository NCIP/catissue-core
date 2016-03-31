
angular.module('os.rde')
  .factory('SpmnCollSession', function(osModel) {
    var SpmnCollSession = new osModel(
      'specimen-collection-sessions',
      function(obj) {
        if (obj.data) {
          obj.dataObj = JSON.parse(obj.data);
        }
      }
    );


    SpmnCollSession.prototype.$saveProps = function() {
      return {
        id: this.$id,
        data: JSON.stringify(this.dataObj)
      }
    };

    return SpmnCollSession;
  });
