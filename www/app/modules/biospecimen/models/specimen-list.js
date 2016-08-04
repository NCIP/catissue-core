angular.module('os.biospecimen.models.specimenlist', ['os.common.models'])
  .factory('SpecimenList', function(osModel, $http) {
    var SpecimenList = osModel('specimen-lists');

    var def_list_name_pattern = /\$\$\$\$user_\d+/;

    function getSpecimensUrl(id) {
      return SpecimenList.url() + id + '/specimens';
    }

    function doSpecimenListOp(listId, specimens, op) {
      var params = "?operation=" + op;
      var ids = specimens.map(function(specimen) { return specimen.id; });
      return $http.put(getSpecimensUrl(listId) + params, ids).then(
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

    SpecimenList.prototype.getListType = function(user) {
      if (this.name === undefined || this.name === null || def_list_name_pattern.exec(this.name) == null) {
        return 'normal_list';
      }

      if (this.owner.id == user.id) {
        return 'default_list';
      } else {
        return 'user_default_list';
      }
    }

    SpecimenList.prototype.addChildSpecimens = function() {
      return $http.post(SpecimenList.url() + this.$id() + '/add-child-specimens').then(
        function(result) {
          return result.data;
        }
      );
    }

    return SpecimenList;
  });
