angular.module('os.administrative.models.institute', ['os.common.models'])
  .factory('Institute', function(osModel, $http, $q) {
    var Institute = osModel('institutes');

    Institute.list = function() {
      return Institute.query();
    }

    Institute.prototype.newDepartment = function() {
      return {id: '', name: ''}
    };

    Institute.prototype.addDepartment = function(department) {
      if(!this.departments) {
        this.departments = [];
      }

      this.departments.push(department);
    }

    Institute.prototype.removeDepartment = function(department) {
      var idx = this.departments ? this.departments.indexOf(department) : -1;
      if (idx != -1) {
        this.departments.splice(idx, 1);
      }

      return idx;
    };

    Institute.prototype.$saveProps = function() {
      var DepartmentNames = {};

      this.departments = this.departments.filter(
        function(department) {
          if(department.name in DepartmentNames) {
            return false;
          } else {
            DepartmentNames[department.name] = true;
            return true;
          }
        }
      );

      return this;
    }

    Institute.getByName = function (instituteName) {
      return Institute.query({name:instituteName});
    }

    return Institute;
  });

