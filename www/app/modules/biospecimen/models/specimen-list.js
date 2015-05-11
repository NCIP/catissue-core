angular.module('os.biospecimen.models.specimenlist', ['os.common.models'])
  .factory('SpecimenList', function(osModel, $http) {
    var SpecimenList = 
      osModel(
        'specimen-lists',
        function(specimenList) {
          specimenList.SpecimenModel = osModel('specimen-lists/' + specimenList.$id() + '/specimens');
          specimenList.SpecimenModel.prototype.$saveOrUpdate = updateSpecimenList;
        }
      );

    function updateSpecimenList() {
      var list = this.list;
      var specimenLabels = this.specimens.map(function(spec) {return spec.label;})
      var param = "?operation=" + this.operation;

      return $http.put(list.SpecimenModel.url() + param, specimenLabels).then(list.SpecimenModel.modelArrayRespTransform);
    }

    SpecimenList.prototype.getSpecimens = function() {
      return this.SpecimenModel.query();
    };

    SpecimenList.prototype.newSpecimen = function(data) {
      return new this.SpecimenModel(data);
    };

    return SpecimenList;
  });
