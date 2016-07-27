(function() {
    'use strict';

    angular
        .module('gestioncompetencesApp')
        .controller('RubriqueController', RubriqueController);

    RubriqueController.$inject = ['$scope', '$state', 'Rubrique'];

    function RubriqueController ($scope, $state, Rubrique) {
        var vm = this;
        
        vm.rubriques = [];

        loadAll();

        function loadAll() {
            Rubrique.query(function(result) {
                vm.rubriques = result;
            });
        }
    }
})();
