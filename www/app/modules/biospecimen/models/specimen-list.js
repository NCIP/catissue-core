angular.module('os.biospecimen.models.specimenlist', ['os.common.models'])
  .factory('SpecimenList', function(osModel, $http) {
    var SpecimenList = osModel('specimen-lists');

    function getSpecimensUrl(id) {
      return SpecimenList.url() + id + '/specimens';
    }

    function doSpecimenListOp(listId, specimens, op) {
      var params = "?operation=" + op;
      var labels = specimens.map(function(specimen) { return specimen.label; });
      return $http.put(getSpecimensUrl(listId) + params, labels).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    SpecimenList.prototype.getType = function() {
      return 'specimen_list';
    }

    SpecimenList.prototype.getDisplayName = function() {
      return this.name;
    }

    SpecimenList.prototype.getSpecimens = function() {
      var params = {maxResults: 1000, includeListCount: true};
      return $http.get(getSpecimensUrl(this.$id()), {params: params}).then(
        function(result) {
          return result.data;
        }
      );
    };

    SpecimenList.prototype.addSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'ADD');
    }

    SpecimenList.prototype.removeSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'REMOVE');
    }

    SpecimenList.prototype.updateSpecimens = function(specimens) {
      return doSpecimenListOp(this.$id(), specimens, 'UPDATE');
    }


    return SpecimenList;
  });
