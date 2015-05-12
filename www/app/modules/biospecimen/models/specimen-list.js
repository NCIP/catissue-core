angular.module('os.biospecimen.models.specimenlist', ['os.common.models'])
  .factory('SpecimenList', function(osModel, $http) {
    var SpecimenList = osModel('specimen-lists');

    function getSpecimensUrl(id) {
      return SpecimenList.url() + id + '/specimens';
    }

    SpecimenList.prototype.getSpecimens = function() {
      return $http.get(getSpecimensUrl(this.$id())).then(
        function(result) {
          return result.data;
        }
      );
    };

    SpecimenList.prototype.updateSpecimens = function() {
      var specimenLabels = this.specimens.map(function(spec) {return spec.label;})
      var param = "?operation=" + this.operation;

      return $http.put(getSpecimensUrl(this.$id()) + param, specimenLabels).then(SpecimenList.noTransform);
    }

    return SpecimenList;
  });
