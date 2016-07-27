(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('FormationController', FormationController);

    FormationController.$inject = ['$scope', '$state', 'Formation'];

    function FormationController ($scope, $state, Formation) {
        var vm = this;
        
        vm.formations = [];

        loadAll();

        function loadAll() {
            Formation.query(function(result) {
                vm.formations = result;
            });
        }
    }
})();
